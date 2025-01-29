#include "5_helpers.h"
#include "return_codes.h"

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

FILE *channel = NULL;

static void handle_signal(int sig)
{
	if (sig == SIGTERM)
	{
		printf("PRODUCER: finished\n");
		fclose(channel);
		exit(SUCCESS);
	}
}

int main(void)
{
	signal(SIGTERM, handle_signal);

	channel = fopen(CHANNEL_FILE, "w");
	if (channel == NULL)
	{
		fprintf(stderr, "PRODUCER: failed to open file %s\n", CHANNEL_FILE);
		return OPEN_FILE_ERROR;
	}
	if (fprintf(channel, "generator_pid=%d\n", getpid()) < 0)
	{
		fprintf(stderr, "PRODUCER: failed to write to file %s\n", CHANNEL_FILE);
		fclose(channel);
		return WRITE_FILE_ERROR;
	}
	else
		fflush(channel);

	char string[32];
	while (1)
	{
		sleep(1);
		if (fgets(string, sizeof(string), SOURCE) == NULL)
			continue;
		if (fprintf(channel, "%s", string) < 0)
		{
			fprintf(stderr, "PRODUCER: failed to write to file %s\n", CHANNEL_FILE);
			fclose(channel);
			return WRITE_FILE_ERROR;
		}
		else
			fflush(channel);
	}

	return SUCCESS;
}
