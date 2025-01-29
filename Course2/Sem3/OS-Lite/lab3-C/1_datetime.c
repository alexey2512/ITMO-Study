#include "return_codes.h"
#include <sys/stat.h>
#include <sys/types.h>

#include <dirent.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#define TEST_DIR "/home/test"
#define ARCH_DIR "/home/test/archived"
#define REPORT "/home/test/report"

static int get_current_date_time(char *date, char *date_time)
{
	time_t cur = time(NULL);
	if (cur < 0)
	{
		fprintf(stderr, "recieving system current time failed\n");
		return SYSTEM_ERROR;
	}
	struct tm *time = localtime(&cur);
	if (time == NULL)
	{
		fprintf(stderr, "conversion from system time to local time failed\n");
		return ALGORITHM_ERROR;
	}
	if (strftime(date_time, 32, "%Y-%m-%d:%H-%M-%S", time) == 0 || strftime(date, 32, "%Y-%m-%d", time) == 0)
	{
		fprintf(stderr, "formatting date/time failed\n");
		return UNKNOWN_ERROR;
	}
	return SUCCESS;
}

static int create_dir(const char *path)
{
	struct stat st = { 0 };
	if (stat(path, &st) == -1 && mkdir(path, 0777) == -1)
	{
		fprintf(stderr, "creating directory %s failed\n", path);
		return CREATE_DIR_ERROR;
	}
	return SUCCESS;
}

static int32_t compare_dates(const char *date_a, const char *date_b)
{
	int32_t ya, ma, da;
	if (sscanf(date_a, "%d-%d-%d", &ya, &ma, &da) < 3)
		return 0;
	int32_t yb, mb, db;
	if (sscanf(date_b, "%d-%d-%d", &yb, &mb, &db) < 3)
		return 0;
	int32_t num_a = 10000 * ya + 100 * ma + da;
	int32_t num_b = 10000 * yb + 100 * mb + db;
	return num_a - num_b;
}

static int archive_by_date(const char *date)
{
	char archive_command[1024];
	char delete_command[1024];
	snprintf(archive_command, sizeof(archive_command), "find %s -type f -name '%s:*' -exec tar -rf %s/%s.tar {} +", TEST_DIR, date, ARCH_DIR, date);
	if (system(archive_command) != 0)
	{
		fprintf(stderr, "archivation files for date %s failed\n", date);
		return ARCHIVATION_ERROR;
	}
	snprintf(delete_command, sizeof(delete_command), "find %s -type f -name '%s:*' -delete", TEST_DIR, date);
	if (system(delete_command) != 0)
	{
		fprintf(stderr, "deleting files for date %s failed\n", date);
		return DELETE_FILE_ERROR;
	}
	return SUCCESS;
}

static int archive_all(const char *today)
{
	int code;
	DIR *dir = opendir(TEST_DIR);
	if (dir == NULL)
	{
		fprintf(stderr, "failed to open test directory\n");
		return OPEN_DIR_ERROR;
	}
	struct dirent *entry;
	struct stat fstat;
	char filepath[288];
	while ((entry = readdir(dir)) != NULL)
	{
		if (strncmp(entry->d_name, ".", 1) == 0 || strncmp(entry->d_name, "..", 2) == 0)
			continue;
		snprintf(filepath, sizeof(filepath), "%s/%s", TEST_DIR, entry->d_name);
		if (stat(filepath, &fstat) == 0 && S_ISREG(fstat.st_mode))
		{
			char mb_date[32] = { 0 };
			if (sscanf(entry->d_name, "%31[^:]:", mb_date) == 1 && compare_dates(mb_date, today) < 0 &&
				(code = archive_by_date(mb_date)) != SUCCESS)
				return code;
		}
	}
	closedir(dir);
	return SUCCESS;
}

// run with sudo
int main(void)
{
	int code;
	if ((code = create_dir(TEST_DIR) != SUCCESS) || (code = create_dir(ARCH_DIR)) != SUCCESS)
		return code;

	char date[32];
	char date_time[32];
	if ((code = get_current_date_time(date, date_time)) != SUCCESS)
		return code;

	char test_path[288];
	snprintf(test_path, sizeof(test_path), "%s/%s", TEST_DIR, date_time);
	FILE *test_file = fopen(test_path, "w");
	if (test_file == NULL)
	{
		fprintf(stderr, "failed to create file %s\n", test_path);
		return CREATE_FILE_ERROR;
	}
	fclose(test_file);

	FILE *report_file = fopen(REPORT, "a");
	if (report_file == NULL)
	{
		fprintf(stderr, "failed to open file %s\n", REPORT);
		return OPEN_FILE_ERROR;
	}
	if (fprintf(report_file, "%s test was created successfully\n", date_time) < 0)
	{
		fclose(report_file);
		fprintf(stderr, "writing to file %s failed\n", REPORT);
		return WRITE_FILE_ERROR;
	}
	fclose(report_file);

	if ((code = archive_all(date)) != SUCCESS)
		return code;

	return SUCCESS;
}
