#include "return_codes.h"
#include <sys/types.h>

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

#define EXE "1_datetime"

static void schedule(void)
{
	while (1)
	{
		time_t now = time(NULL);
		if (now < 0)
		{
			fprintf(stderr, "recieving system current time failed\n");
			goto slp;
		}
		struct tm *time = localtime(&now);
		if (time == NULL)
		{
			fprintf(stderr, "conversion from system time to local time failed\n");
			goto slp;
		}
		if (time->tm_wday != 2 || time->tm_min != 5)
			goto slp;
		char cmd[64];
		snprintf(cmd, sizeof(cmd), "./%s", EXE);
		int code;
		if ((code = system(cmd)) != 0)
			fprintf(stderr, "execution of file %s failed with exit code: %d\n", EXE, code);
slp:
		sleep(60);
	}
}

int main(void)
{
	pid_t pid_schedule = fork();
	if (pid_schedule < 0)
	{
		fprintf(stderr, "failed to start scheduling\n");
		return FORK_ERROR;
	}
	else if (pid_schedule == 0)
		schedule();
	else
		printf("Started scheduler with pid=%d\n", pid_schedule);

	return SUCCESS;
}
