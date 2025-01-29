#include "processes.h"
#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

long get_vmrss(pid_t pid)
{
	long vmrss = -1;
	char file_path[32];
	snprintf(file_path, sizeof(file_path), STATUS_FMT, pid);
	FILE *status = fopen(file_path, "r");
	if (status == NULL)
	{
		open_error(file_path);
		return vmrss;
	}
	char line[256];
	while (fgets(line, sizeof(line), status))
	{
		if (strncmp(line, "VmRSS:", 6) != 0)
			continue;
		if (sscanf(line, "VmRSS:\t%ld kB", &vmrss) < 0)
			read_value_error("VmRSS", file_path);
		goto free;
	}
	if (vmrss == -1)
		vmrss = 0;
free:
	fclose(status);
	return vmrss;
}

int main(void)
{
	int code;

	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;

	long max_vmrss = -1;
	pid_t max_proc = -1;
	for (size_t i = 0; i < bucket.size; i++)
	{
		long vmrss = get_vmrss(bucket.identifiers[i]);
		if (vmrss > max_vmrss)
		{
			max_vmrss = vmrss;
			max_proc = bucket.identifiers[i];
		}
	}

	if (max_vmrss == -1)
	{
		fprintf(stderr, "failed to get VmRSS from at least one process\n");
		free_bucket(&bucket);
		return UNKNOWN_ERROR;
	}
	printf("%d : %ld kB\n", max_proc, max_vmrss);
	free_bucket(&bucket);
	return SUCCESS;
}
