#include "7_helpers.h"
#include "return_codes.h"

#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

FILE *channel = NULL;

static void print_move(const char *literal)
{
	printf("%s move: ", literal);
	uint8_t x, y, x_, y_;
	fscanf(SOURCE, "%hhu %hhu %hhu %hhu", &x, &y, &x_, &y_);
	if (fprintf(channel, "%hhu %hhu %hhu %hhu\n", x, y, x_, y_) < 0)
	{
		fprintf(stderr, "PRODUCER: failed to write to file %s\n", CHANNEL_FILE);
		fclose(channel);
		exit(WRITE_FILE_ERROR);
	}
	else
		fflush(channel);
}

static void handle_signal(int sig)
{
	switch (sig)
	{
	case SIGTERM:
		printf("PRODUCER: finished\n");
		fclose(channel);
		exit(SUCCESS);
	case SIGUSR1:	 // first move
		print_move("First");
		break;
	case SIGUSR2:	 // second move
		print_move("Second");
		break;
	}
}

int main(void)
{
	signal(SIGTERM, handle_signal);
	signal(SIGUSR1, handle_signal);
	signal(SIGUSR2, handle_signal);

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

	while (1)
		sleep(1);

	return SUCCESS;
}
