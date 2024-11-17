#include "audio.h"

#include "channel.h"
#include "return_codes.h"
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>

#include <stdint.h>
#include <stdio.h>

void file_error(const char* message, const char* filename, int code)
{
	fprintf(stderr, "%d: %s, file=\"%s\"", code, message, filename);
}

void free_audio(Audio* audio)
{
	avcodec_free_context(&audio->codec_context);
	avformat_close_input(&audio->format_context);
}

static double get_sample(enum AVSampleFormat format, const uint8_t* buffer, int index)
{
	int64_t val;
	double ret;
	switch (format)
	{
	case AV_SAMPLE_FMT_U8:
	case AV_SAMPLE_FMT_U8P:
		val = ((uint8_t*)buffer)[index];
		ret = (double)(val - 128) / INT8_MAX;
		break;
	case AV_SAMPLE_FMT_S16:
	case AV_SAMPLE_FMT_S16P:
		val = ((int16_t*)buffer)[index];
		ret = (double)val / INT16_MAX;
		break;
	case AV_SAMPLE_FMT_S32:
	case AV_SAMPLE_FMT_S32P:
		val = ((int32_t*)buffer)[index];
		ret = (double)val / INT32_MAX;
		break;
	case AV_SAMPLE_FMT_S64:
	case AV_SAMPLE_FMT_S64P:
		val = ((int64_t*)buffer)[index];
		ret = (double)val / INT64_MAX;
		// (double) INT64_MAX = 9223372036854775808.0, this is normal for normalization
		break;
	case AV_SAMPLE_FMT_FLT:
	case AV_SAMPLE_FMT_FLTP:
		ret = ((float*)buffer)[index];
		break;
	case AV_SAMPLE_FMT_DBL:
	case AV_SAMPLE_FMT_DBLP:
		ret = ((double*)buffer)[index];
		break;
	default:
		ret = 0.0;
	}
	return ret;
}

static int read_frame(const Audio* audio, Channel** channels, uint8_t ch_cnt, AVFrame* frame)
{
	int code;
	AVCodecContext* cdc_ctx = audio->codec_context;
	while ((code = avcodec_receive_frame(cdc_ctx, frame)) == 0)
	{
		for (int s = 0; s < frame->nb_samples; s++)
		{
			for (int c = 0; c < ch_cnt; c++)
			{
				double sample =
					av_sample_fmt_is_planar(cdc_ctx->sample_fmt)
						? get_sample(cdc_ctx->sample_fmt, frame->extended_data[c], s)
						: get_sample(cdc_ctx->sample_fmt, frame->extended_data[0], s * cdc_ctx->ch_layout.nb_channels + c);

				if ((code = add_sample(channels[c], sample)) != SUCCESS)
				{
					av_frame_unref(frame);
					return code;
				}
			}
		}
		av_frame_unref(frame);
	}
	if (code != AVERROR_EOF && code != AVERROR(EAGAIN))
	{
		code = ERROR_UNKNOWN;
		file_error("error trying to read samples", audio->filename, code);
		return code;
	}
	return SUCCESS;
}

int read_samples(const Audio* audio, Channel** channels, uint8_t ch_cnt)
{
	int code;
	AVFrame* frame = av_frame_alloc();
	if (!frame)
	{
		code = ERROR_NOTENOUGH_MEMORY;
		file_error("not enough memory for allocating frame", audio->filename, code);
		goto ret;
	}

	AVPacket packet;
	while ((code = av_read_frame(audio->format_context, &packet)) != AVERROR_EOF)
	{
		if (code)
		{
			code = ERROR_UNKNOWN;
			file_error("error trying to get packet", audio->filename, code);
			goto free;
		}
		// int packet.stream_index will be cast to unsigned (so if it is not negative, comparing will be correct)
		if (packet.stream_index != audio->stream_index)
		{
			av_packet_unref(&packet);
			continue;
		}
		while ((code = avcodec_send_packet(audio->codec_context, &packet)) == AVERROR(EAGAIN))
		{
			code = read_frame(audio, channels, ch_cnt, frame);
			if (code != SUCCESS)
			{
				av_packet_unref(&packet);
				goto free;
			}
		}
		av_packet_unref(&packet);
		switch (code)
		{
		case 0:
			break;
		case AVERROR_EOF:
			goto drain;
		case AVERROR(ENOMEM):
			code = ERROR_NOTENOUGH_MEMORY;
			file_error("memory allocation while sending packet failed", audio->filename, code);
			goto free;
		default:
			code = ERROR_UNKNOWN;
			file_error("error sending packet to codec", audio->filename, code);
			goto free;
		}
		code = read_frame(audio, channels, ch_cnt, frame);
		if (code != SUCCESS)
			goto free;
	}

drain:
	while ((code = avcodec_send_packet(audio->codec_context, NULL)) == AVERROR(EAGAIN))
	{
		code = read_frame(audio, channels, ch_cnt, frame);
		if (code != SUCCESS)
			goto free;
	}
	if (code != 0 && code != AVERROR_EOF)
	{
		code = ERROR_UNKNOWN;
		file_error("error draining decoder", audio->filename, code);
		goto free;
	}
	code = SUCCESS;

free:
	av_frame_unref(frame);
	av_frame_free(&frame);
ret:
	return code;
}

int prepare_audio(const char* filename, Audio* audio, uint8_t ch_cnt, int* rate)
{
	AVFormatContext* fmt_ctx = NULL;
	int code = avformat_open_input(&fmt_ctx, filename, NULL, 0);
	if (code == AVERROR(ENOMEM))
	{
		code = ERROR_NOTENOUGH_MEMORY;
		file_error("can not allocate format context", filename, code);
		goto ret;
	}
	else if (code != 0)
	{
		code = ERROR_CANNOT_OPEN_FILE;
		file_error("can not open file, maybe it does not exist or is not accessible", filename, code);
		goto ret;
	}

	if (avformat_find_stream_info(fmt_ctx, NULL))
	{
		code = ERROR_FORMAT_INVALID;
		file_error("can not find stream info", filename, code);
		goto free1;
	}

	unsigned index;
	uint8_t not_found = 1;
	for (unsigned i = 0; i < fmt_ctx->nb_streams; i++)
		if (fmt_ctx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO)
		{
			index = i;
			not_found = 0;
			break;
		}
	if (not_found)
	{
		code = ERROR_FORMAT_INVALID;
		file_error("can not find audio stream", filename, code);
		goto free1;
	}

	switch (fmt_ctx->streams[index]->codecpar->codec_id)
	{
	case AV_CODEC_ID_MP2:
	case AV_CODEC_ID_MP3:
	case AV_CODEC_ID_OPUS:
	case AV_CODEC_ID_FLAC:
	case AV_CODEC_ID_AAC:
		break;
	default:
		code = ERROR_FORMAT_INVALID;
		file_error("unsupported file format (expected mp2/mp3/opus/flac/aac)", filename, code);
		goto free1;
	}

	const AVCodec* codec;
	if ((codec = avcodec_find_decoder(fmt_ctx->streams[index]->codecpar->codec_id)) == NULL)
	{
		code = ERROR_FORMAT_INVALID;
		file_error("codec is not supported", filename, code);
		goto free1;
	}

	AVCodecContext* cdc_ctx;
	if ((cdc_ctx = avcodec_alloc_context3(codec)) == NULL)
	{
		code = ERROR_UNKNOWN;
		file_error("can not allocate decoding context", filename, code);
		goto free1;
	}

	if (avcodec_parameters_to_context(cdc_ctx, fmt_ctx->streams[index]->codecpar) < 0)
	{
		code = ERROR_UNKNOWN;
		file_error("error setting codec context parameters", filename, code);
		goto free2;
	}

	cdc_ctx->pkt_timebase = fmt_ctx->streams[index]->time_base;

	if (avcodec_open2(cdc_ctx, codec, NULL))
	{
		code = ERROR_UNKNOWN;
		file_error("codec context initialization failed", filename, code);
		goto free2;
	}

	int nb_chs = cdc_ctx->ch_layout.nb_channels;
	if (nb_chs < ch_cnt)
	{
		code = ERROR_FORMAT_INVALID;
		fprintf(stderr, "%d: expected at least %d channels but found %d, file=\"%s\"", code, ch_cnt, nb_chs, filename);
		goto free2;
	}

	*rate = cdc_ctx->sample_rate;
	audio->filename = filename;
	audio->format_context = fmt_ctx;
	audio->codec_context = cdc_ctx;
	audio->stream_index = index;
	return SUCCESS;

free2:
	avcodec_free_context(&cdc_ctx);
free1:
	avformat_close_input(&fmt_ctx);
ret:
	return code;
}
