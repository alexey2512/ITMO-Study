#include "helpers.h"

#include "return_codes.h"
#include <sys/stat.h>
#include <sys/types.h>

#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int get_last_date(time_t current_time, char *last_date, time_t *last_time)
{
	DIR *home_user = opendir(getenv("HOME"));
	if (home_user == NULL)
	{
		fprintf(stderr, "failed to open home directory\n");
		return OPEN_DIR_ERROR;
	}

	size_t prefix_len = strlen(BACKUP_PREFIX);
	char latest_backup[DATE_LENGTH] = "1970-01-01";
	time_t latest_time = 0;
	struct dirent *entry;

	while ((entry = readdir(home_user)) != NULL)
	{
		if (strncmp(entry->d_name, BACKUP_PREFIX, prefix_len) != 0)
			continue;
		struct tm _tm = { 0 };
		char *date_part = entry->d_name + prefix_len;
		if (sscanf(date_part, "%4d-%2d-%2d", &(_tm.tm_year), &(_tm.tm_mon), &(_tm.tm_mday)) < 3)
			continue;
		_tm.tm_year -= 1900;
		_tm.tm_mon -= 1;
		time_t backup_time = mktime(&_tm);
		if (backup_time > latest_time && backup_time <= current_time)
		{
			latest_time = backup_time;
			strncpy(latest_backup, date_part, DATE_LENGTH);
		}
	}

	closedir(home_user);
	strncpy(last_date, latest_backup, DATE_LENGTH);
	*last_time = latest_time;
	return SUCCESS;
}

int create_dir(const char *path)
{
	struct stat st = { 0 };
	if (stat(path, &st) == -1 && mkdir(path, 0755) == -1)
	{
		fprintf(stderr, "creating directory %s failed\n", path);
		return CREATE_DIR_ERROR;
	}
	return SUCCESS;
}

int copy_file(const char *from, const char *to)
{
	FILE *source = fopen(from, "rb");
	if (source == NULL)
	{
		fprintf(stderr, "can not open file %s\n", from);
		return OPEN_FILE_ERROR;
	}

	FILE *target = fopen(to, "wb");
	if (target == NULL)
	{
		fprintf(stderr, "can not create file %s\n", to);
		return CREATE_FILE_ERROR;
	}

	char buf[4096];
	size_t bytes;
	while ((bytes = fread(buf, 1, sizeof(buf), source)) > 0)
	{
		if (fwrite(buf, 1, bytes, target) == bytes)
			continue;
		fclose(target);
		fclose(source);
		fprintf(stderr, "copy failed: %s -> %s\n", from, to);
		if (remove(to) == -1)
			fprintf(stderr, "can not delete file %s, delete by yourself\n", to);
		return WRITE_FILE_ERROR;
	}

	fclose(target);
	fclose(source);
	return SUCCESS;
}

int change_version(const char *from, const char *to)
{
	if (rename(from, to) == -1)
	{
		fprintf(stderr, "failed to change version of file %s\n", from);
		return RENAME_FILE_ERROR;
	}
	return SUCCESS;
}
