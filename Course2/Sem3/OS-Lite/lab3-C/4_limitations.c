#include "return_codes.h"
#include <sys/types.h>

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

#define CPU_LIMIT 0.1
#define CYCLE_DURATION 100000

static void infinite_calculating(void)
{
	volatile int64_t a = 0;
	while (1)
		a++;
}

static void limit_cpu_usage(pid_t pid)
{
	__useconds_t active = (__useconds_t)(CYCLE_DURATION * CPU_LIMIT);
	__useconds_t sleeping = (__useconds_t)(CYCLE_DURATION - active);

	while (1)
	{
		if (kill(pid, SIGCONT) != 0)
		{
			fprintf(stderr, "failed to send SIGCONT to proccess with pid=%d\n", pid);
			break;
		}
		usleep(active);
		if (kill(pid, SIGSTOP) != 0)
		{
			fprintf(stderr, "failed to send SIGSTOP to proccess with pid=%d\n", pid);
			break;
		}
		usleep(sleeping);
	}
}

static void kill_process(pid_t pid)
{
	if (kill(pid, SIGKILL) != 0)
		fprintf(stderr, "failed to kill process with pid=%d\n", pid);
	else
		printf("process with pid=%d successfully killed\n", pid);
}

// kill 1 and 2 processes by yourself
int main(void)
{
	pid_t inf_pids[3];

	for (size_t i = 0; i < 3; i++)
	{
		inf_pids[i] = fork();

		if (inf_pids[i] < 0)
		{
			fprintf(stderr, "failed to run %zu fork... killing all previous\n", i + 1);
			for (size_t j = 0; j < i; j++)
				kill_process(inf_pids[i]);
			return FORK_ERROR;
		}
		else if (inf_pids[i] == 0)
		{
			infinite_calculating();
			return INTERRUPTION_ERROR;
		}
		else
			printf("Started fork %zu with pid=%d\n", i + 1, inf_pids[i]);
	}

	sleep(1);

	kill_process(inf_pids[2]);

	pid_t pid_limiter = fork();

	if (pid_limiter < 0)
	{
		fprintf(stderr, "failed to start limiting\n");
		kill_process(inf_pids[0]);
		kill_process(inf_pids[1]);
		return FORK_ERROR;
	}
	else if (pid_limiter == 0)
	{
		limit_cpu_usage(inf_pids[0]);
		return INTERRUPTION_ERROR;
	}
	else
		printf("Started limiter with pid=%d\n", pid_limiter);

	return SUCCESS;
}
