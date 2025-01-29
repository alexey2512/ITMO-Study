#include "6_helpers.h"
#include "return_codes.h"
#include <sys/types.h>

#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int64_t number = 1;

static void handle_signal(int sig)
{
	switch (sig)
	{
	case SIGUSR1:
		number += 2;
		printf("Result: %ld\n", number);
		break;
	case SIGUSR2:
		number *= 2;
		printf("Result: %ld\n", number);
		break;
	case SIGTERM:
		printf("HANDLER: got SIGTERM... terminating with value=%ld\n", number);
		exit(SUCCESS);
	}
}

int main(void)
{
	signal(SIGUSR1, handle_signal);
	signal(SIGUSR2, handle_signal);
	signal(SIGTERM, handle_signal);

	FILE *channel = fopen(CHANNEL_FILE, "w");
	if (channel == NULL)
	{
		fprintf(stderr, "HANDLER: failed to open file %s\n", CHANNEL_FILE);
		return OPEN_FILE_ERROR;
	}

	if (fprintf(channel, "handler_pid=%d\n", getpid()) < 0)
	{
		fprintf(stderr, "HANDLER: failed to write to file %s\n", CHANNEL_FILE);
		fclose(channel);
		return WRITE_FILE_ERROR;
	}

	fclose(channel);

	while (1)
		sleep(1);

	return SUCCESS;
}
