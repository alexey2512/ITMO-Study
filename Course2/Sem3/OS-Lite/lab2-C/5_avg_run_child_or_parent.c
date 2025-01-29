#include "processes.h"
#include "return_codes.h"

#include <stdio.h>
#include <stdlib.h>

int main(void)
{
	int code;

	const char *filename = "cpu_burst.txt";
	FILE *cpu_burst = fopen(filename, "r");
	if (cpu_burst == NULL)
		return open_error(filename);

	size_t count;
	if (fscanf(cpu_burst, "%zu\n", &count) < 0)
	{
		fclose(cpu_burst);
		return read_error(filename);
	}

	ProcessInfo1 *infos = (ProcessInfo1 *)malloc(count * sizeof(ProcessInfo1));
	if (infos == NULL)
	{
		fclose(cpu_burst);
		return memory_error();
	}

	for (size_t i = 0; i < count; i++)
	{
		if (fscanf(cpu_burst,
				   "ProcessID=%d : Parent_ProcessID=%d : Average_Running_Time=%f\n",
				   &(infos[i].pid),
				   &(infos[i].ppid),
				   &(infos[i].avg_runtime)) < 0)
		{
			fclose(cpu_burst);
			code = read_error(filename);
			goto free1;
		}
	}
	fclose(cpu_burst);

	FILE *to_write = fopen(filename, "w");
	if (to_write == NULL)
	{
		code = open_error(filename);
		goto free1;
	}
	if (fprintf(to_write, "%zu\n", count) < 0)
	{
		code = write_error(filename);
		goto free2;
	}
	float cur_sum = 0;
	size_t cur_cnt = 0;
	for (size_t i = 0; i < count; i++)
	{
		if (fprintf(to_write,
					"ProcessID=%d : Parent_ProcessID=%d : Average_Running_Time=%f\n",
					infos[i].pid,
					infos[i].ppid,
					infos[i].avg_runtime) < 0)
		{
			code = write_error(filename);
			goto free2;
		}
		cur_sum += infos[i].avg_runtime;
		cur_cnt++;
		if (i == count - 1 || infos[i].ppid != infos[i + 1].ppid)
		{
			if (fprintf(to_write, "Average_Running_Children_of_ParentID=%d is %f\n", infos[i].ppid, cur_sum / (float)cur_cnt) < 0)
			{
				code = write_error(filename);
				goto free2;
			}
			cur_sum = 0;
			cur_cnt = 0;
		}
	}

	code = SUCCESS;
free2:
	fclose(to_write);
free1:
	free(infos);
	return code;
}
