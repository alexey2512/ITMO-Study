#include "array_util.h"
#include "audio.h"
#include "channel.h"
#include "return_codes.h"

#include <stdio.h>

int main(int argc, char* argv[])
{
	int code;
	if (argc < 2 || argc > 3)
	{
		code = ERROR_ARGUMENTS_INVALID;
		fprintf(stderr, "%d: invalid arguments\nusage: <file> / <file1> <file2>", code);
		return code;
	}

	int delta = 0;
	int rate;
	Channel ch1, ch2;
	if ((code = init_channel(&ch1)) != SUCCESS)
		goto ret;
	if ((code = init_channel(&ch2)) != SUCCESS)
		goto free1;
	Channel* chs[2] = { &ch1, &ch2 };

	if (argc == 2)
	{
		Audio audio;
		if ((code = prepare_audio(argv[1], &audio, 2, &rate)) != SUCCESS)
			goto free2;
		if ((code = read_samples(&audio, chs, 2)) != SUCCESS)
			goto free1_3;
		free_audio(&audio);
		goto corr;

free1_3:
		free_audio(&audio);
		goto free2;
	}
	else
	{
		int rate1;
		Audio audio1;
		if ((code = prepare_audio(argv[1], &audio1, 1, &rate1)) != SUCCESS)
			goto free2;
		if ((code = read_samples(&audio1, &(chs[0]), 1)) != SUCCESS)
			goto free2_3;

		int rate2;
		Audio audio2;
		if ((code = prepare_audio(argv[2], &audio2, 1, &rate2)) != SUCCESS)
			goto free2_3;
		if ((code = read_samples(&audio2, &(chs[1]), 1)) != SUCCESS)
			goto free2_4;

		if (rate1 < rate2 && (code = resampling(chs[0], rate1, rate2)) != SUCCESS ||
			rate1 > rate2 && (code = resampling(chs[1], rate2, rate1)) != SUCCESS)
			goto free2_4;
		rate = rate1 > rate2 ? rate1 : rate2;
		free_audio(&audio1);
		free_audio(&audio2);
		goto corr;

free2_4:
		free_audio(&audio2);
free2_3:
		free_audio(&audio1);
		goto free2;
	}

corr:
	if ((code = cross_correlation(chs[0], chs[1], &delta)) != SUCCESS)
		goto free2;
	printf("delta: %i samples\nsample rate: %i Hz\ndelta time: %i ms\n", delta, rate, (int)((double)(delta) / (double)rate * 1000.0));

free2:
	free_channel(&ch2);
free1:
	free_channel(&ch1);
ret:
	return code;
}
