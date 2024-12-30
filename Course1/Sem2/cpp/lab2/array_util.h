#pragma once

#include "channel.h"

#include <stddef.h>

/**
 * Calculates the cross-correlation of the two transmitted channels and
 * writes the shift value to int via its pointer. Uses fftw lib for fast fourier transformation.
 * If n = max(ch1->len, ch2->len) working time of algorithm is O(n log n).
 * All resourced opened by this function will be closed after execution regardless of operation success.
 * @param ch1 base channel to calculate the shift regarding it
 * @param ch2 channel shifted relative to the base
 * @param delta pointer to int to write the shift value
 * @return SUCCESS if all operations inside executed successfully, else corresponding code from return_codes.h
 */
int cross_correlation(const Channel* ch1, const Channel* ch2, int* delta);

/**
 * Resamples the transmitted array from a one sample rate to other by making
 * linear combinations of two adjacent samples of the original array.
 * The resampled array will be written to the transmitted channel with the update of the len and cap parameters.
 * @param ch source channel
 * @param from source sample rate
 * @param to destination sample rate
 * @return SUCCESS if all operations inside executed successfully, else corresponding code from return_codes.h
 */
int resampling(Channel* ch, size_t from, size_t to);
