#include "channel.h"

#include "return_codes.h"

#include <stdio.h>	  // already contains size_t and NULL
#include <stdlib.h>

int memory_error(size_t bytes)
{
	int code = ERROR_NOTENOUGH_MEMORY;
	fprintf(stderr, "%d: allocation of %zu bytes failed\n", code, bytes);
	return code;
}

void free_channel(Channel* ch)
{
	if (ch->data)
	{
		free(ch->data);
		ch->data = NULL;
	}
	ch->len = 0;
	ch->cap = 0;
}

int init_channel(Channel* ch)
{
	ch->cap = 1024;
	ch->len = 0;
	size_t new_size = ch->cap * sizeof(double);
	double* temp = (double*)malloc(new_size);
	if (!temp)
		return memory_error(new_size);
	ch->data = temp;
	return SUCCESS;
}

static int resize(Channel* ch, size_t new_cap)
{
	size_t new_size = new_cap * sizeof(double);
	double* temp = realloc(ch->data, new_size);
	if (!temp)
		return memory_error(new_size);
	ch->data = temp;
	ch->cap = new_cap;
	return SUCCESS;
}

int add_sample(Channel* ch, double sample)
{
	int code = SUCCESS;
	size_t new_cap = ch->cap * 2;
	if (ch->len >= ch->cap && (code = resize(ch, new_cap)) != SUCCESS)
		return code;
	ch->data[ch->len++] = sample;
	return code;
}
