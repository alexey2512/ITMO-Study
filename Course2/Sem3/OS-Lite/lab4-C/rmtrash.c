#include "helpers.h"
#include "return_codes.h"
#include <sys/stat.h>
#include <sys/types.h>

#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
	if (argc != 2)
	{
		fprintf(stderr, "Usage: %s <file_name>\n", argv[0]);
		return INVALID_ARGUMENTS_ERROR;
	}

	char *file_name = argv[1];
	struct stat file_stat;

	if (stat(file_name, &file_stat) == -1)
	{
		fprintf(stderr, "file %s doesn't exist\n", file_name);
		return NO_SUCH_FILE_ERROR;
	}

	if (S_ISDIR(file_stat.st_mode))
	{
		fprintf(stderr, "%s is a directory\n", file_name);
		return INVALID_ARGUMENTS_ERROR;
	}

	if (!S_ISREG(file_stat.st_mode))
	{
		fprintf(stderr, "No such file %s\n", file_name);
		return NO_SUCH_FILE_ERROR;
	}

	ino_t inode = file_stat.st_ino;

	char real_path[PATH_MAX];
	if (!realpath(file_name, real_path))
	{
		fprintf(stderr, "failed to resolve filepath\n");
		return SYSTEM_ERROR;
	}

	char trash_path[PATH_MAX];
	snprintf(trash_path, sizeof(trash_path), "%s/%s", getenv("HOME"), TRASH_DIR);
	if (mkdir(trash_path, 0755) == -1 && errno != EEXIST)
	{
		fprintf(stderr, "Error creating .trash directory");
		return CREATE_DIR_ERROR;
	}

	char log_path[PATH_MAX];
	snprintf(log_path, sizeof(log_path), "%s/%s", getenv("HOME"), LOG_FILE);
	FILE *log_file = fopen(log_path, "a+");
	if (log_file == NULL)
	{
		fprintf(stderr, "can not open log file, to untrash your file go to %s\n", trash_path);
		return OPEN_FILE_ERROR;
	}

	int code = SUCCESS;

	char new_link_path[PATH_MAX];
	snprintf(new_link_path, sizeof(new_link_path), "%s/%lu", trash_path, inode);
	if (link(file_name, new_link_path) == -1)
	{
		fprintf(stderr, "failed to remove file %s to trash\n", file_name);
		code = LINK_ERROR;
		goto close;
	}

	if (remove(file_name) == -1)
	{
		fprintf(stderr, "failed to remove file %s\n", file_name);
		if (unlink(new_link_path) == -1)
			fprintf(stderr, "failed to remove matching hard link: %s\n", new_link_path);
		code = DELETE_FILE_ERROR;
		goto close;
	}

	if (fprintf(log_file, "%lu '%s'\n", inode, real_path) < 0)
	{
		fprintf(stderr, "failed to log trashing, you can untrash your file by hard link: %s\n", new_link_path);
		code = WRITE_FILE_ERROR;
		goto close;
	}

	printf("file '%s' moved to trash with ID %lu\n", file_name, inode);
close:
	fclose(log_file);
	return code;
}
