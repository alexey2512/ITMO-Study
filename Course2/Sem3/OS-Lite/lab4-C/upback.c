#include "helpers.h"
#include "return_codes.h"
#include <sys/stat.h>

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

static int mkdirs(char *path, char *p)
{
	int code;
	struct stat st;
	while ((p = strchr(p, '/')) != NULL)
	{
		*p = '\0';
		if (stat(path, &st) == 0 && !S_ISDIR(st.st_mode))
		{
			fprintf(stderr, "%s is not a directory, but expected directory or nothing\n", path);
			*p = '/';
			return CREATE_DIR_ERROR;
		}
		if ((code = create_dir(path)) != SUCCESS)
		{
			*p = '/';
			return code;
		}
		*p = '/';
		p++;
	}
	return SUCCESS;
}

int main(void)
{
	int code;
	char *home = getenv("HOME");

	time_t now = time(NULL);
	if (now < 0)
	{
		fprintf(stderr, "failed to get current time\n");
		return UNKNOWN_ERROR;
	}

	char last_backup_date[DATE_LENGTH];
	time_t last_backup_time = 0;
	if ((code = get_last_date(now, last_backup_date, &last_backup_time)) != SUCCESS)
		return code;

	char backup_path[PATH_MAX];
	snprintf(backup_path, sizeof(backup_path), "%s/%s%s", home, BACKUP_PREFIX, last_backup_date);

	char restore_path[PATH_MAX];
	snprintf(restore_path, sizeof(restore_path), "%s/%s", home, RESTORE);
	if ((code = create_dir(restore_path)) != SUCCESS)
		return code;

	char report_path[PATH_MAX];
	snprintf(report_path, sizeof(report_path), "%s/%s", home, BACKUP_REPORT);
	FILE *report = fopen(report_path, "r");
	if (report == NULL)
	{
		fprintf(stderr, "can not open report file %s\n", report_path);
		return OPEN_FILE_ERROR;
	}

	char line[PATH_MAX + 64];
	while (fgets(line, sizeof(line), report) != NULL)
	{
		char date[DATE_LENGTH];
		char read_path[PATH_MAX];
		int8_t create;
		if (sscanf(line, "%10s create: '%4095[^']", date, read_path) == 2)
			create = 1;
		else if (sscanf(line, "%10s folder: '%4095[^']", date, read_path) == 2)
			create = 0;
		else
			continue;

		if (strcmp(date, last_backup_date) != 0)
			continue;
		char restore_file_path[PATH_MAX];
		snprintf(restore_file_path, sizeof(restore_file_path), "%s%s", restore_path, read_path);
		if (mkdirs(restore_file_path, restore_file_path + strlen(restore_path) + 1) != SUCCESS)
			continue;

		if (create)
		{
			char backup_file_path[PATH_MAX];
			snprintf(backup_file_path, sizeof(backup_file_path), "%s%s", backup_path, read_path);
			copy_file(backup_file_path, restore_file_path);
		}
	}

	fclose(report);
	return SUCCESS;
}
