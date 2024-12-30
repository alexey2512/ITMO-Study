#pragma once

#include "channel.h"
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>

#include <stdint.h>

typedef struct
{
	const char* filename;	 // save it here to print more detail errors without copy-paste
	AVFormatContext* format_context;
	AVCodecContext* codec_context;
	unsigned stream_index;
} Audio;

/**
 * Prints formatted error message to stderr.
 * @param message error message
 * @param filename name of file which error relates to
 * @param code exit code of operation
 */
void file_error(const char* message, const char* filename, int code);

/**
 * Frees codec context and closes format input
 * @param audio struct containing codec context and format context to be closed
 */
void free_audio(Audio* audio);

/**
 * Initializes format context, codec and codec context and save them in the given struct audio.
 * Frees all opened resources if error occurs.
 * @param filename name of given file
 * @param audio struct to be prepared
 * @param ch_cnt count of necessary channels
 * @param rate the pointer to int which will be filled by audio sample rate
 * @return SUCCESS if audio was filled successfully else corresponding code from return_codes.h
 */
int prepare_audio(const char* filename, Audio* audio, uint8_t ch_cnt, int* rate);

/**
 * Reads samples from the audio stream and saves them to the struct channels. Frees all resources regardless of
 * operation success
 * @param audio struct of given audio
 * @param channels structs to be filled by samples read from the audio stream
 * @param ch_cnt count of channels to be filled
 * @return SUCCESS if audio was filled successfully else corresponding code from return_codes.h
 */
int read_samples(const Audio* audio, Channel** channels, uint8_t ch_cnt);
