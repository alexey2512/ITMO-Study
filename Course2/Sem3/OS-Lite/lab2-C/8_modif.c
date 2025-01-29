#include "processes.h"
#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

long get_vsize(pid_t pid)
{
	long result = -1;
	char file_path[32];
	snprintf(file_path, sizeof(file_path), STAT_FMT, pid);
	FILE *stat = fopen(file_path, "r");
	if (stat == NULL)
	{
		open_error(file_path);
		return result;
	}
	for (size_t i = 0; i < 22; i++)
		if (fscanf(stat, "%*s") < 0)
		{
			fclose(stat);
			read_error(file_path);
			return result;
		}
	if (fscanf(stat, "%ld", &result) < 0)
	{
		fclose(stat);
		read_value_error("23", file_path);
		return result;
	}
	fclose(stat);
	return result;
}

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

int get_state(pid_t pid, char *state)
{
	char file_path[32];
	snprintf(file_path, sizeof(file_path), STATUS_FMT, pid);
	FILE *status = fopen(file_path, "r");
	if (status == NULL)
		return open_error(file_path);
	char line[256];
	while (fgets(line, sizeof(line), status))
	{
		if (strncmp(line, "State:", 6) != 0)
			continue;
		if (sscanf(line, "State:\t%c", state) < 0)
		{
			fclose(status);
			return read_error(file_path);
		}
		break;
	}
	fclose(status);
	return SUCCESS;
}

int main(void)
{
	int code;
	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;

	char *start_states = (char *)malloc(bucket.size * sizeof(char));
	if (start_states == NULL)
	{
		code = MEMORY_ALLOCATION_ERROR;
		goto free_bck;
	}
	long *start_vsizes = (long *)malloc(bucket.size * sizeof(long));
	if (start_states == NULL)
	{
		code = MEMORY_ALLOCATION_ERROR;
		goto free_sts;
	}

	for (size_t i = 0; i < bucket.size; i++)
	{
		pid_t cur = bucket.identifiers[i];
		char state;
		if ((code = get_state(cur, &state)) != SUCCESS)
			state = '\0';
		start_states[i] = state;
		start_vsizes[i] = state == 'R' ? get_vsize(cur) : -1;
	}

	sleep(180);

	size_t count = 0;
	for (size_t i = 0; i < bucket.size; i++)
	{
		if (start_states[i] != 'R' || start_vsizes[i] < 0)
			continue;
		pid_t cur = bucket.identifiers[i];
		char state;
		if ((code = get_state(cur, &state)) != SUCCESS)
			state = '\0';
		if (state != 'S')
			continue;
		long final_vsize = get_vsize(cur);
		if (final_vsize < 0)
			continue;
		uid_t uid;
		if ((code = get_uid(cur, &uid)) != SUCCESS)
			continue;
		char *cmdline;
		if ((code = get_cmdline(cur, &cmdline)) != SUCCESS)
			continue;
		printf("pid=%d : uid=%u : cmdline=%s : vmem_increase=%f%%\n", cur, uid, cmdline, (float)(final_vsize - start_vsizes[i]) / start_vsizes[i] * 100);
		free(cmdline);
		count++;
		if (count >= 5)
			break;
	}

	code = SUCCESS;
	free(start_vsizes);
free_sts:
	free(start_states);
free_bck:
	free_bucket(&bucket);
	return code;
}
