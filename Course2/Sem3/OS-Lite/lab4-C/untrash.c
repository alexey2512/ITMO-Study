#include "helpers.h"
#include "return_codes.h"
#include <sys/stat.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef enum
{
	IGNORE,
	UNIQUE,
	OVERWRITE
} Policy;

static int restore(char *trash_path, char *orig_path, Policy policy)
{
	struct stat buf;

	if (stat(orig_path, &buf) == 0)
	{
		char vers_path[PATH_MAX];
		size_t copy = 1;
		switch (policy)
		{
		case IGNORE:
			printf("file already exists, ignoring...\n");
			return UNKNOWN_ERROR;
		case UNIQUE:
			while (1)
			{
				snprintf(vers_path, sizeof(vers_path), "%s(%zu)", orig_path, copy);
				if (stat(vers_path, &buf) != 0)
					break;
				copy++;
			}
			orig_path = vers_path;
			break;
		case OVERWRITE:
			if (remove(orig_path) == -1)
			{
				fprintf(stderr, "failed to overwrite file %s\n", orig_path);
				return DELETE_FILE_ERROR;
			}
			break;
		}
	}

	if (rename(trash_path, orig_path) == -1)
	{
		fprintf(stderr, "failed to restore file with (re)assigned name: %s\n", orig_path);
		return RENAME_FILE_ERROR;
	}

	printf("file restored with (re)assigned name: %s\n", orig_path);
	return SUCCESS;
}

int main(int argc, char **argv)
{
	if (argc < 2 || argc > 3)
	{
		fprintf(stderr, "Usage: %s <file_name> [--ignore | --unique | --overwrite]\n", argv[0]);
		return INVALID_ARGUMENTS_ERROR;
	}

	const char *file_name = argv[1];
	Policy policy;

	if (argc == 2 || strcmp(argv[2], "--ignore") == 0 || strcmp(argv[2], "-i") == 0)
		policy = IGNORE;
	else if (strcmp(argv[2], "--unique") == 0 || strcmp(argv[2], "-u") == 0)
		policy = UNIQUE;
	else if (strcmp(argv[2], "--overwrite") == 0 || strcmp(argv[2], "-o") == 0)
		policy = OVERWRITE;
	else
	{
		fprintf(stderr, "invalid option %s, use --ignore, --unique or --overwrite", argv[2]);
		return INVALID_ARGUMENTS_ERROR;
	}

	const char *home = getenv("HOME");
	char log_path[PATH_MAX];
	snprintf(log_path, sizeof(log_path), "%s/%s", home, LOG_FILE);
	FILE *log_file = fopen(log_path, "r");
	if (log_file == NULL)
	{
		fprintf(stderr, "can not open log file %s\n", log_path);
		return OPEN_FILE_ERROR;
	}

	struct stat buf;
	char line[PATH_MAX + 48];
	size_t found_count = 0;
	size_t restored_count = 0;
	while (fgets(line, sizeof(line), log_file))
	{
		char trash_file[32];
		char orig_path[PATH_MAX];
		if (sscanf(line, "%s '%4095[^']\n", trash_file, orig_path) < 1)
		{
			fprintf(stderr, "data in file %s was damaged, can not read next log\n", log_path);
			fclose(log_file);
			return READ_FILE_ERROR;
		}
		if (!strstr(orig_path, file_name))
			continue;
		char trash_path[PATH_MAX];
		snprintf(trash_path, sizeof(trash_path), "%s/%s/%s", home, TRASH_DIR, trash_file);
		if (stat(trash_path, &buf) != 0)
			continue;
		printf("Found: %s -> %s\n", trash_file, orig_path);
		printf("Restore this file? [Y/n]: ");
		found_count++;
		char res;
		scanf(" %c", &res);
		if (res != 'Y' && res != 'y')
		{
			printf("skipped\n");
			continue;
		}
		if (restore(trash_path, orig_path, policy) == SUCCESS)
			restored_count++;
	}

	fclose(log_file);

	printf("%zu matches found, %zu successfully restored\n", found_count, restored_count);
	return SUCCESS;
}
