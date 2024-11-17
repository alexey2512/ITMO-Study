#pragma once

#include <sys/types.h>

#include <stdlib.h>

#ifndef CMDLINE_FMT
#define CMDLINE_FMT "/proc/%d/cmdline"
#endif

#ifndef IO_FMT
#define IO_FMT "/proc/%d/io"
#endif

#ifndef SCHED_FMT
#define SCHED_FMT "/proc/%d/sched"
#endif

#ifndef STATUS_FMT
#define STATUS_FMT "/proc/%d/status"
#endif

#ifndef STAT_FMT
#define STAT_FMT "/proc/%d/stat"
#endif

typedef struct
{
	pid_t *identifiers;
	size_t size;
	size_t cap;
} ProcessBucket;

int memory_error(void);

int read_error(const char *filename);

int write_error(const char *filename);

int open_error(const char *filename);

int read_value_error(const char *value, const char *filename);

void free_bucket(ProcessBucket *bucket);

int fill(ProcessBucket *bucket);

int get_cmdline(pid_t pid, char **src);

typedef struct
{
	pid_t pid;
	pid_t ppid;
	float avg_runtime;
} ProcessInfo1;

int compare_process_info_1(const void *a, const void *b);

typedef struct
{
	pid_t pid;
	long start_rb;
	long final_rb;
} ProcessInfo2;

int compare_process_info_2(const void *a, const void *b);
