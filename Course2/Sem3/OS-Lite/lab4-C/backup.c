#include "helpers.h"
#include "return_codes.h"
#include <sys/stat.h>

#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

static int walk(const char *source_path, const char *backup_path, const char *today, const char *log_date, FILE *report, const char *dir_stack)
{
	char temp_source_path[PATH_MAX];
	snprintf(temp_source_path, sizeof(temp_source_path), "%s%s", source_path, dir_stack);
	char temp_backup_path[PATH_MAX];
	snprintf(temp_backup_path, sizeof(temp_backup_path), "%s%s", backup_path, dir_stack);

	DIR *source = opendir(temp_source_path);
	if (source == NULL)
	{
		fprintf(stderr, "can not open directory %s\n", temp_source_path);
		return OPEN_DIR_ERROR;
	}

	int code;
	struct dirent *entry;
	struct stat source_stat, backup_stat;
	while ((entry = readdir(source)) != NULL)
	{
		if (strncmp(entry->d_name, ".", 1) == 0 || strncmp(entry->d_name, "..", 2) == 0)
			continue;
		char source_entry_path[PATH_MAX];
		snprintf(source_entry_path, sizeof(source_entry_path), "%s/%s", temp_source_path, entry->d_name);
		char backup_entry_path[PATH_MAX];
		snprintf(backup_entry_path, sizeof(backup_entry_path), "%s/%s", temp_backup_path, entry->d_name);
		char new_stack[PATH_MAX];
		snprintf(new_stack, sizeof(new_stack), "%s/%s", dir_stack, entry->d_name);
		if (stat(source_entry_path, &source_stat) != 0)
			continue;
		if (S_ISDIR(source_stat.st_mode))
		{
			if (stat(backup_entry_path, &backup_stat) != 0)
			{
				if ((code = create_dir(backup_entry_path)) != SUCCESS)
				{
					closedir(source);
					return code;
				}
				if (fprintf(report, "%s folder: '%s/'\n", log_date, new_stack) < 0)
					goto log_fail;
			}
			else if (!S_ISDIR(backup_stat.st_mode))
			{
				fprintf(stderr, "%s is not a directory, but expected directory or nothing\n", backup_entry_path);
				continue;
			}
			if ((code = walk(source_path, backup_path, today, log_date, report, new_stack)) != SUCCESS)
			{
				closedir(source);
				return code;
			}
		}
		else if (S_ISREG(source_stat.st_mode))
		{
			if (stat(backup_entry_path, &backup_stat) != 0)
			{
				if (copy_file(source_entry_path, backup_entry_path) != SUCCESS)
					continue;
				if (fprintf(report, "%s create: '%s'\n", log_date, new_stack) < 0)
					goto log_fail;
			}
			else
			{
				if (!S_ISREG(backup_stat.st_mode))
				{
					fprintf(stderr, "%s is not a file, but expected file or nothing\n", backup_entry_path);
					continue;
				}
				if (backup_stat.st_size == source_stat.st_size)
				{
					printf("files at %s have equal sizes, refreshing was cancelled\n", new_stack);
					continue;
				}
				char old_version_path[PATH_MAX];
				snprintf(old_version_path, sizeof(old_version_path), "%s.%s", backup_entry_path, today);
				if (change_version(backup_entry_path, old_version_path) != SUCCESS)
					continue;
				if (copy_file(source_entry_path, backup_entry_path) != SUCCESS)
					continue;
				if (fprintf(report, "%s update: '%s'\n", log_date, new_stack) < 0)
					goto log_fail;
			}
		}
	}

	closedir(source);
	return SUCCESS;

log_fail:
	fprintf(stderr, "can not write to backup-report\n");
	closedir(source);
	return WRITE_FILE_ERROR;
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

	char today[DATE_LENGTH];
	struct tm *_tm = localtime(&now);
	if (_tm == NULL)
	{
		fprintf(stderr, "conversion from system time to local time failed\n");
		return UNKNOWN_ERROR;
	}
	if (strftime(today, sizeof(today), "%Y-%m-%d", _tm) == 0)
	{
		fprintf(stderr, "conversion from date to string failed\n");
		return UNKNOWN_ERROR;
	}

	char backup_path[PATH_MAX];
	char log_date[DATE_LENGTH];
	if (now - last_backup_time > DELAY)
	{
		strncpy(log_date, today, DATE_LENGTH);
		snprintf(backup_path, sizeof(backup_path), "%s/%s%s", home, BACKUP_PREFIX, log_date);
		if ((code = create_dir(backup_path)) != SUCCESS)
			return code;
	}
	else
	{
		strncpy(log_date, last_backup_date, DATE_LENGTH);
		snprintf(backup_path, sizeof(backup_path), "%s/%s%s", home, BACKUP_PREFIX, log_date);
	}

	char report_path[PATH_MAX];
	snprintf(report_path, sizeof(report_path), "%s/%s", home, BACKUP_REPORT);
	FILE *report = fopen(report_path, "a+");
	if (report == NULL)
	{
		fprintf(stderr, "can not open report file %s\n", report_path);
		return OPEN_FILE_ERROR;
	}

	if (fprintf(report, "%s: session started\n", backup_path) < 0)
	{
		fprintf(stderr, "can not write to report file %s\n", report_path);
		fclose(report);
		return WRITE_FILE_ERROR;
	}

	char source_path[PATH_MAX];
	snprintf(source_path, sizeof(source_path), "%s/%s", home, SOURCE);

	if ((code = walk(source_path, backup_path, today, log_date, report, "")) != SUCCESS)
	{
		fclose(report);
		return code;
	}

	fclose(report);
	return SUCCESS;
}
