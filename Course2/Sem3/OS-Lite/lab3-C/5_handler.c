#include "5_helpers.h"
#include "return_codes.h"
#include <sys/types.h>

#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

typedef enum
{
	ADD = 0,
	MUL = 1
} ops;

int main(void)
{
	sleep(1);
	int code = SUCCESS;
	FILE *channel = fopen(CHANNEL_FILE, "r");
	if (channel == NULL)
	{
		fprintf(stderr, "HANDLER: failed to open file %s\n", CHANNEL_FILE);
		return OPEN_FILE_ERROR;
	}
	setvbuf(channel, NULL, _IONBF, 0);

	pid_t gen_pid;
	if (fscanf(channel, "generator_pid=%d\n", &gen_pid) < 1)
	{
		fprintf(stderr, "HANDLER: failed to read generator_pid from file %s\n", CHANNEL_FILE);
		fclose(channel);
		return READ_FILE_ERROR;
	}
	size_t last_pos = ftell(channel);

	int64_t number = 1;
	ops mode = ADD;
	char string[32];
	while (1)
	{
		sleep(1);

		fseek(channel, last_pos, SEEK_SET);
		if (fgets(string, sizeof(string), channel) == NULL)
			continue;
		last_pos = ftell(channel);

		int64_t operand;
		if (strcmp(string, "+\n") == 0)
			mode = ADD;
		else if (strcmp(string, "*\n") == 0)
			mode = MUL;
		else if (sscanf(string, "%ld\n", &operand) == 1)
		{
			switch (mode)
			{
			case ADD:
				number += operand;
				break;
			case MUL:
				number *= operand;
				break;
			}
		}
		else if (strcmp(string, "QUIT\n") == 0)
		{
			printf("HANDLER: finished with value=%ld\n", number);
			code = SUCCESS;
			break;
		}
		else
		{
			fprintf(stderr, "HANDLER: finished with error: invalid input in line %s", string);
			code = INVALID_INPUT_ERROR;
			break;
		}
	}

	fclose(channel);
	if (kill(gen_pid, SIGTERM) != 0)
	{
		fprintf(stderr, "HANDLER: failed to send SIGTERM to producer with pid=%d\n", gen_pid);
		code = SYSTEM_ERROR;
	}

	return code;
}
