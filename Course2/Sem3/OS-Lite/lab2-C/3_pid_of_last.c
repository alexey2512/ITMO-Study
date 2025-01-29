#include "processes.h"
#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

clock_t get_start_time(pid_t pid)
{
	clock_t result = -1;
	char file_path[32];
	snprintf(file_path, sizeof(file_path), STAT_FMT, pid);
	FILE *stat = fopen(file_path, "r");
	if (stat == NULL)
	{
		open_error(file_path);
		return result;
	}
	for (size_t i = 0; i < 21; i++)
		if (fscanf(stat, "%*s") < 0)
		{
			fclose(stat);
			read_error(file_path);
			return result;
		}
	if (fscanf(stat, "%ld", &result) < 0)
	{
		fclose(stat);
		read_value_error("22", file_path);
		return result;
	}
	fclose(stat);
	return result;
}

int main(void)
{
	int code;
	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;

	pid_t last_proc = -1;
	clock_t last_time = 0;
	for (size_t i = 0; i < bucket.size; i++)
	{
		clock_t time = get_start_time(bucket.identifiers[i]);
		if (time > last_time)
		{
			last_proc = bucket.identifiers[i];
			last_time = time;
		}
	}

	if (last_proc == -1)
	{
		fprintf(stderr, "failed to get start_time from at least one process\n");
		free_bucket(&bucket);
		return UNKNOWN_ERROR;
	}
	printf("%d\n", last_proc);
	free_bucket(&bucket);
	return SUCCESS;
}
