#pragma once

#include <stddef.h>	   // for size_t

typedef struct
{
	double* data;
	size_t cap;
	size_t len;
} Channel;

/**
 * Prints error message about allocation memory failure and returns ERROR_NOTENOUGN_MEMORY code.
 * @param bytes size of memory which allocation failed
 * @return ERROR_NOTENOUGN_MEMORY code from return_codes.h
 */
int memory_error(size_t bytes);

/**
 * Frees data inside the given structure and sets data pointer to NULL, len and cap to zero.
 * To use given channel again it must be initialized by init_channel function
 * @param ch channel to destroy
 */
void free_channel(Channel* ch);

/**
 * Inits given channel allocating memory for data and sets cap to 1024 and len to 0.
 * After using this function given channel is ready for using.
 * @param ch channel to initialize
 * @return SUCCESS if initialization process is successful else ERROR_NOTENOUGN_MEMORY code from return_codes.h if
 * allocation failed
 */
int init_channel(Channel* ch);

/**
 * Adds given double sample to the end of given channel and increases the len parameter.
 * @param ch source channel
 * @param sample sample to be added to channel
 * @return SUCCESS if operation successful else corresponding code from return_codes.h
 */
int add_sample(Channel* ch, double sample);
