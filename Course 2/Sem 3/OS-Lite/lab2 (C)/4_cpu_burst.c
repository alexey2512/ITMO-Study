#include "processes.h"
#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

pid_t get_ppid(pid_t pid)
{
	pid_t ppid = -1;
	char file_path[32];
	snprintf(file_path, sizeof(file_path), STATUS_FMT, pid);
	FILE *status = fopen(file_path, "r");
	if (status == NULL)
	{
		open_error(file_path);
		return ppid;
	}
	char line[256];
	while (fgets(line, sizeof(line), status))
	{
		if (strncmp(line, "PPid:", 5) != 0)
			continue;
		sscanf(line, "PPid:\t%d", &ppid);
		break;
	}
	if (ppid == -1)
		read_value_error("PPid", file_path);
	fclose(status);
	return ppid;
}

float get_avg_runtime(pid_t pid)
{
	char file_path[32];
	snprintf(file_path, sizeof(file_path), SCHED_FMT, pid);
	FILE *sched = fopen(file_path, "r");
	if (sched == NULL)
	{
		open_error(file_path);
		return -1.0f;
	}
	float sum_exec = -1.0f;
	int nr_switches = -1;
	char line[256];
	while (fgets(line, sizeof(line), sched))
	{
		if (strncmp(line, "se.sum_exec_runtime", 19) == 0 && sscanf(line, "se.sum_exec_runtime : %f", &sum_exec) < 0)
			read_value_error("sum_exec_runtime", file_path);
		if (strncmp(line, "nr_switches", 11) == 0 && sscanf(line, "nr_switches : %d", &nr_switches) < 0)
			read_value_error("nr_switches", file_path);
	}
	fclose(sched);
	if (sum_exec < 0 || nr_switches < 0)
	{
		read_error(file_path);
		return -1.0f;
	}
	else if (nr_switches == 0)
	{
		fprintf(stderr, "can not calculate avg_runtime because nr_switches=0 for pid=%d\n", pid);
		return -1.0f;
	}
	return sum_exec / (float)nr_switches;
}

int main(void)
{
	int code;
	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;

	ProcessInfo1 *infos = (ProcessInfo1 *)malloc(bucket.size * sizeof(ProcessInfo1));
	if (infos == NULL)
	{
		code = memory_error();
		goto free1;
	}

	size_t count = 0;
	for (size_t i = 0; i < bucket.size; i++)
	{
		pid_t pid = bucket.identifiers[i];
		pid_t ppid = -1;
		float avg_runtime = -1.0f;
		ppid = get_ppid(pid);
		if (ppid < 0)
			goto fill;
		avg_runtime = get_avg_runtime(pid);
		if (avg_runtime < 0)
		{
			ppid = -1;
			goto fill;
		}
		count++;
fill:
		infos[i].pid = pid;
		infos[i].ppid = ppid;
		infos[i].avg_runtime = avg_runtime;
	}

	qsort(infos, bucket.size, sizeof(ProcessInfo1), compare_process_info_1);

	const char *filename = "cpu_burst.txt";
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
	for (size_t i = bucket.size - count; i < bucket.size; i++)
	{
		if (fprintf(to_write,
					"ProcessID=%d : Parent_ProcessID=%d : Average_Running_Time=%f\n",
					infos[i].pid,
					infos[i].ppid,
					infos[i].avg_runtime) < 0)
		{
			code = write_error(filename);
			goto free3;
		}
	}

	code = SUCCESS;
free3:
	fclose(to_write);
free2:
	free(infos);
free1:
	free_bucket(&bucket);
	return code;
}
