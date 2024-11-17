#include "processes.h"
#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

void get_read_bytes(pid_t pid, long *read_bytes)
{
	*read_bytes = -1;
	char file_path[32];
	snprintf(file_path, sizeof(file_path), IO_FMT, pid);
	FILE *io = fopen(file_path, "r");
	if (io == NULL)
	{
		open_error(file_path);
		return;
	}
	char line[256];
	while (fgets(line, sizeof(line), io))
	{
		if (strncmp(line, "read_bytes:", 11) != 0)
			continue;
		sscanf(line, "read_bytes: %ld", read_bytes);
		break;
	}
	if (*read_bytes == -1)
		read_value_error("read_bytes", file_path);
	fclose(io);
}

int main(void)
{
	int code;

	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;

	ProcessInfo2 *infos = (ProcessInfo2 *)malloc(bucket.size * sizeof(ProcessInfo2));
	if (infos == NULL)
	{
		free_bucket(&bucket);
		code = memory_error();
		return code;
	}

	size_t count = 0;
	for (size_t i = 0; i < bucket.size; i++)
	{
		infos[i].pid = bucket.identifiers[i];
		get_read_bytes(infos[i].pid, &infos[i].start_rb);
		if (infos[i].start_rb >= 0)
			count++;
	}

	if (count == 0)
	{
		fprintf(stderr, "no processes to wait for\n");
		free(infos);
		free_bucket(&bucket);
		return UNKNOWN_ERROR;
	}

	printf("pending");
	for (int i = 0; i < 60; i++)
	{
		sleep(1);
		printf(".");
		fflush(stdout);
	}
	printf("\n");

	for (size_t i = 0; i < bucket.size; i++)
		get_read_bytes(infos[i].pid, &infos[i].final_rb);

	qsort(infos, bucket.size, sizeof(ProcessInfo2), compare_process_info_2);

	for (size_t i = 0; i < 3; i++)
	{
		if (infos[i].start_rb < 0 || infos[i].final_rb < 0)
			break;
		char *cmdline;
		if (get_cmdline(infos[i].pid, &cmdline) != SUCCESS)
			continue;
		printf("PId=%d : command=%s : delta=%ld\n", infos[i].pid, cmdline, infos[i].final_rb - infos[i].start_rb);
		free(cmdline);
	}

	free(infos);
	free_bucket(&bucket);
	return SUCCESS;
}
