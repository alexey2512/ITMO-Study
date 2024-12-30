#include "array_util.h"

#include "channel.h"
#include "return_codes.h"

#include <fftw3.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static int plan_error()
{
	int code = ERROR_NOTENOUGH_MEMORY;
	fprintf(stderr, "%d: failed to create fftw_plan, most possible cause not enough memory", code);
	return code;
}

static int real_to_complex(const Channel* re, fftw_complex** out, size_t size, size_t shift)
{
	int code = SUCCESS;
	size_t bytes = size * sizeof(double);
	double* padded = (double*)malloc(bytes);
	if (!padded)
		return memory_error(bytes);
	bytes = size * sizeof(fftw_complex);
	*out = fftw_malloc(bytes);
	if (!*out)
	{
		code = memory_error(bytes);
		goto free;
	}
	memcpy(padded + shift, re->data, re->len * sizeof(double));
	memset(padded + (shift ? 0 : re->len), 0, (shift ? shift : size - re->len) * sizeof(double));
	fftw_plan plan = fftw_plan_dft_r2c_1d((int)size, padded, *out, FFTW_ESTIMATE);
	if (!plan)
	{
		code = plan_error();
		fftw_free(*out);
		goto free;
	}
	fftw_execute(plan);
	fftw_destroy_plan(plan);
free:
	free(padded);
	return code;
}

int cross_correlation(const Channel* ch1, const Channel* ch2, int* delta)
{
	int code;
	size_t d = ch2->len - 1;
	size_t size = ch1->len + d;

	fftw_complex* src1 = NULL;
	if ((code = real_to_complex(ch1, &src1, size, d)) != SUCCESS)
		goto free1;

	fftw_complex* src2 = NULL;
	if ((code = real_to_complex(ch2, &src2, size, 0)) != SUCCESS)
		goto free2;

	for (size_t i = 0; i < size; i++)
	{
		double r1 = src1[i][0];
		double i1 = src1[i][1];
		src1[i][0] = r1 * src2[i][0] + i1 * src2[i][1];
		src1[i][1] = i1 * src2[i][0] - r1 * src2[i][1];
	}

	fftw_plan plan = fftw_plan_dft_1d((int)size, src1, src2, FFTW_BACKWARD, FFTW_ESTIMATE);
	if (!plan)
	{
		code = plan_error();
		goto free3;
	}
	fftw_execute(plan);
	fftw_destroy_plan(plan);

	size_t index = 0;
	double max = src2[0][0];
	for (size_t i = 0; i < size; i++)
	{
		if (src2[i][0] > max)
		{
			max = src2[i][0];
			index = i;
		}
	}
	*delta = (int)index - (int)d;

free3:
	fftw_free(src2);
free2:
	fftw_free(src1);
free1:
	fftw_cleanup();	   // save it here, so if fftw won't init any plans it will do nothing
	return code;
}

int resampling(Channel* ch, size_t from, size_t to)
{
	double div = (double)to / (double)from;
	double eps = 0.000001;
	size_t new_size = (size_t)ceil((double)ch->len * div);
	double* out = malloc(new_size * sizeof(double));
	if (!out)
		return memory_error(new_size * sizeof(double));

	for (size_t i = 0; i < new_size; i++)
	{
		double j = (double)i / div;
		if (fabs(j - round(j)) < eps)
			out[i] = ch->data[(size_t)round(j)];
		else
		{
			double j_down = floor(j);
			double j_up = ceil(j);
			out[i] = ch->data[(size_t)j_down] * (j_up - j) + ch->data[(size_t)j_up] * (j - j_down);
		}
	}

	free_channel(ch);
	ch->data = out;
	ch->len = new_size;
	ch->cap = new_size;
	return SUCCESS;
}
