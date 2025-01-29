#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define REPORT "/home/test/report"
#define EXE "1_datetime"

static int schedule(size_t seconds)
{
	int code;
	char cmd[64];
	snprintf(cmd, sizeof(cmd), "sudo ./%s", EXE);
	sleep(seconds);
	if ((code = system(cmd)) != 0)
	{
		fprintf(stderr, "execution of file %s failed with exit code: %d\n", EXE, code);
		return SYSTEM_ERROR;
	}
	return SUCCESS;
}

static int follow(size_t seconds)
{
	FILE *report_file = fopen(REPORT, "r");
	if (report_file == NULL)
	{
		fprintf(stderr, "failed to open file %s... following interrupted\n", REPORT);
		return OPEN_FILE_ERROR;
	}
	fseek(report_file, 0, SEEK_END);
	size_t last_pos = ftell(report_file);
	char buffer[1024];
	for (size_t i = 0; i < seconds; i++)
	{
		fseek(report_file, last_pos, SEEK_SET);
		while (fgets(buffer, sizeof(buffer), report_file))
			printf("\n%s", buffer);
		last_pos = ftell(report_file);
		sleep(1);
	}
	fclose(report_file);
	return SUCCESS;
}

// run without sudo
int main(void)
{
	pid_t pid_schedule = fork();
	if (pid_schedule < 0)
	{
		fprintf(stderr, "failed to start scheduling\n");
		return FORK_ERROR;
	}
	else if (pid_schedule == 0)
		return schedule(120);
	else
	{
		printf("Started scheduler with pid=%d\n", pid_schedule);
		pid_t pid_follow = fork();
		if (pid_follow < 0)
		{
			fprintf(stderr, "failed to start following\n");
			return FORK_ERROR;
		}
		else if (pid_follow == 0)
			return follow(123);
		else
			printf("Started follower with pid=%d\n", pid_follow);
	}
	return SUCCESS;
}
