#include "processes.h"

#include "return_codes.h"
#include <sys/stat.h>
#include <sys/types.h>

#include <ctype.h>
#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>

#define INIT_CAP 16
#define CMD_MAX_SIZE 1024

static int init(ProcessBucket *bucket)
{
	pid_t *new_data = (pid_t *)malloc(INIT_CAP * sizeof(pid_t));
	if (new_data == NULL)
		return memory_error();
	bucket->identifiers = new_data;
	bucket->cap = INIT_CAP;
	bucket->size = 0;
	return SUCCESS;
}

static int push(ProcessBucket *bucket, pid_t new_id)
{
	bucket->identifiers[bucket->size] = new_id;
	bucket->size++;
	if (bucket->size >= bucket->cap)
	{
		size_t new_cap = bucket->cap * 2;
		pid_t *new_data = (pid_t *)realloc(bucket->identifiers, new_cap * sizeof(pid_t));
		if (new_data == NULL)
			return memory_error();
		bucket->identifiers = new_data;
		bucket->cap = new_cap;
	}
	return SUCCESS;
}

static int is_number(const char *str)
{
	for (size_t i = 0; str[i] != '\0'; i++)
		if (!isdigit(str[i]))
			return 0;
	return 1;
}

static int is_directory(const char *path)
{
	struct stat statbuf;
	if (stat(path, &statbuf))
	{
		fprintf(stderr, "can not get info about entity %s... skipped\n", path);
		return 0;
	}
	return S_ISDIR(statbuf.st_mode);
}

int memory_error(void)
{
	fprintf(stderr, "memory allocation failed, try again\n");
	return MEMORY_ALLOCATION_ERROR;
}

int read_error(const char *filename)
{
	fprintf(stderr, "reading from file %s failed\n", filename);
	return FILE_READING_ERROR;
}

int write_error(const char *filename)
{
	fprintf(stderr, "writing to file %s failed\n", filename);
	return FILE_WRITING_ERROR;
}

int open_error(const char *filename)
{
	fprintf(stderr, "can not open file %s\n", filename);
	return CANNOT_OPEN_FILE_ERROR;
}

int read_value_error(const char *value, const char *filename)
{
	fprintf(stderr, "failed to read value %s from file %s\n", value, filename);
	return FILE_READING_ERROR;
}

void free_bucket(ProcessBucket *bucket)
{
	bucket->size = 0;
	bucket->cap = 0;
	free(bucket->identifiers);
}

int fill(ProcessBucket *bucket)
{
	int code;
	DIR *proc_dir = opendir("/proc");
	if (proc_dir == NULL)
	{
		fprintf(stderr, "error while opening /proc directory\n");
		return CANNOT_OPEN_FILE_ERROR;
	}
	if ((code = init(bucket)) != SUCCESS)
	{
		closedir(proc_dir);
		return code;
	}
	struct dirent *entry;
	while ((entry = readdir(proc_dir)) != NULL)
	{
		char path[264];
		snprintf(path, sizeof(path), "/proc/%s", entry->d_name);
		if (!(is_directory(path) && is_number(entry->d_name)))
			continue;
		if ((code = push(bucket, (pid_t)atoi(entry->d_name))) != SUCCESS)
		{
			free_bucket(bucket);
			closedir(proc_dir);
			return code;
		}
	}
	closedir(proc_dir);
	return SUCCESS;
}

int get_cmdline(pid_t pid, char **src)
{
	char file_path[32];
	snprintf(file_path, sizeof(file_path), CMDLINE_FMT, pid);
	FILE *cmd_file = fopen(file_path, "r");
	if (cmd_file == NULL)
		return open_error(file_path);

	*src = (char *)malloc(CMD_MAX_SIZE * sizeof(char));
	if (*src == NULL)
	{
		fclose(cmd_file);
		return memory_error();
	}

	size_t read_size = fread(*src, 1, CMD_MAX_SIZE, cmd_file);
	if (ferror(cmd_file))
	{
		free(*src);
		*src = NULL;
		fclose(cmd_file);
		return read_error(file_path);
	}

	fclose(cmd_file);

	if (read_size == 0)
	{
		char *string = "empty";
		for (int i = 0; i < 5; i++)
			(*src)[i] = string[i];
		(*src)[5] = '\0';
		return SUCCESS;
	}

	for (size_t i = 0; i < read_size - 1; i++)
		if ((*src)[i] == '\0')
			(*src)[i] = ' ';
	return SUCCESS;
}

int compare_process_info_1(const void *a, const void *b)
{
	ProcessInfo1 *A = (ProcessInfo1 *)a;
	ProcessInfo1 *B = (ProcessInfo1 *)b;
	return (int)(A->ppid != B->ppid ? A->ppid - B->ppid : A->pid - B->pid);
}

int compare_process_info_2(const void *a, const void *b)
{
	ProcessInfo2 *A = (ProcessInfo2 *)a;
	ProcessInfo2 *B = (ProcessInfo2 *)b;
	if (A->start_rb < 0 || A->final_rb < 0)
		return 1;
	else if (B->start_rb < 0 || B->final_rb < 0)
		return -1;
	return B->final_rb - B->start_rb - A->final_rb + A->start_rb;
}
