#include "processes.h"
#include "return_codes.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void)
{
	int code;
	ProcessBucket bucket;
	if ((code = fill(&bucket)) != SUCCESS)
		return code;

	const char *filename = "from_sbin.txt";
	FILE *to_write = fopen(filename, "w");
	if (to_write == NULL)
	{
		free_bucket(&bucket);
		return open_error(filename);
	}

	for (size_t i = 0; i < bucket.size; i++)
	{
		char *cmdline = NULL;
		if ((code = get_cmdline(bucket.identifiers[i], &cmdline)) == MEMORY_ALLOCATION_ERROR)
			goto free;
		if (code != SUCCESS)
			continue;
		if (strncmp(cmdline, "/sbin/", 6) == 0 && fprintf(to_write, "%d\n", bucket.identifiers[i]) < 0)
		{
			free(cmdline);
			code = write_error(filename);
			goto free;
		}
		free(cmdline);
	}

	code = SUCCESS;
free:
	fclose(to_write);
	free_bucket(&bucket);
	return code;
}
