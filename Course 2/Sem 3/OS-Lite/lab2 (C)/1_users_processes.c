#include "processes.h"
#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int get_uid(pid_t pid, uid_t *uid)
{
	char file_path[32];
	snprintf(file_path, sizeof(file_path), STATUS_FMT, pid);
	FILE *status = fopen(file_path, "r");
	if (status == NULL)
		return open_error(file_path);
	char line[256];
	while (fgets(line, sizeof(line), status))
	{
		if (strncmp(line, "Uid:", 4) != 0)
			continue;
		if (sscanf(line, "Uid:\t%u\t", uid) < 0)
		{
			fclose(status);
			return read_error(file_path);
		}
		break;
	}
	fclose(status);
	return SUCCESS;
}

void free_strings(char **array, size_t size)
{
	for (size_t i = 0; i < size; i++)
		free(array[i]);
	free(array);
}

int main(void)
{
	int code;
	uid_t uid = getuid();
	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;
	char **cmdlines = (char **)malloc(bucket.size * sizeof(char *));
	if (cmdlines == NULL)
	{
		code = memory_error();
		goto free1;
	}

	size_t count = 0;
	for (size_t i = 0; i < bucket.size; i++)
	{
		cmdlines[i] = NULL;
		pid_t pid = bucket.identifiers[i];
		uid_t real_uid = 0;
		if ((code = get_uid(pid, &real_uid)) != SUCCESS)
			continue;
		if (real_uid != uid)
			continue;
		if ((code = get_cmdline(pid, &(cmdlines[i]))) == MEMORY_ALLOCATION_ERROR)
			goto free2;
		if (code == SUCCESS)
			count++;
	}

	const char *filename = "users_processes.txt";
	FILE *to_write = fopen(filename, "w");
	if (to_write == NULL)
	{
		code = open_error(filename);
		goto free2;
	}
	if (fprintf(to_write, "%zu\n", count) < 0)
	{
		code = write_error(filename);
		goto free3;
	}
	for (size_t i = 0; i < bucket.size; i++)
	{
		if (cmdlines[i] != NULL && fprintf(to_write, "%d:%s\n", bucket.identifiers[i], cmdlines[i]) < 0)
		{
			code = write_error(filename);
			goto free3;
		}
	}

	code = SUCCESS;
free3:
	fclose(to_write);
free2:
	free_strings(cmdlines, bucket.size);
free1:
	free_bucket(&bucket);
	return SUCCESS;
}
