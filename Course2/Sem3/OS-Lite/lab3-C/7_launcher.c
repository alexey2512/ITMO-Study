#include "7_helpers.h"
#include "return_codes.h"

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int run(const char *prog, int8_t async)
{
	int code;
	char cmd[32];
	snprintf(cmd, sizeof(cmd), async ? "./%s &" : "./%s", prog);
	if ((code = system(cmd)) != 0)
	{
		fprintf(stderr, "LAUNCHER: failed on running %s with exit code: %d\n", prog, code);
		return code;
	}
	return SUCCESS;
}

int main(void)
{
	int code;
	if ((code = run(HANDLER_EXE, 1)) != SUCCESS)
		return code;

	if ((code = run(PRODUCER_EXE, 0)) != SUCCESS)
		return code;

	return SUCCESS;
}
