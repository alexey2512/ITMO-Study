#include "6_helpers.h"
#include "return_codes.h"
#include <sys/types.h>

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(void)
{
	sleep(1);
	FILE *channel = fopen(CHANNEL_FILE, "r");
	if (channel == NULL)
	{
		fprintf(stderr, "PRODUCER: failed to open file %s\n", CHANNEL_FILE);
		return OPEN_FILE_ERROR;
	}

	pid_t handler;
	if (fscanf(channel, "handler_pid=%d\n", &handler) < 1)
	{
		fprintf(stderr, "PRODUCER: failed to read handler_uid from file %s\n", CHANNEL_FILE);
		fclose(channel);
		return READ_FILE_ERROR;
	}

	fclose(channel);

	char string[32];
	while (fgets(string, sizeof(string), SOURCE))
	{
		sleep(1);
		if (strcmp(string, "+\n") == 0 && kill(handler, SIGUSR1) != 0)
			fprintf(stderr, "PRODUCER: failed to send SIGUSR1 to handler with pid=%d\n", handler);
		else if (strcmp(string, "*\n") == 0 && kill(handler, SIGUSR2) != 0)
			fprintf(stderr, "PRODUCER: failed to send SIGUSR2 to handler with pid=%d\n", handler);
		else if (strcmp(string, "TERM\n") == 0)
			break;
	}

	if (kill(handler, SIGTERM) != 0)
	{
		fprintf(stderr, "PRODUCER: failed to send SIGTERM to handler with pid=%d\n", handler);
		return SYSTEM_ERROR;
	}

	printf("PRODUCER: terminating\n");
	return SUCCESS;
}
