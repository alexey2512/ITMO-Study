#pragma once

#include <limits.h>
#include <time.h>

#ifndef PATH_MAX
#define PATH_MAX 4096
#endif

#ifndef TRASH_DIR
#define TRASH_DIR ".trash"
#endif

#ifndef LOG_FILE
#define LOG_FILE ".trash.log"
#endif

#ifndef SOURCE
#define SOURCE "source"
#endif

#ifndef RESTORE
#define RESTORE "restore"
#endif

#ifndef BACKUP_REPORT
#define BACKUP_REPORT "backup-report"
#endif

#ifndef BACKUP_PREFIX
#define BACKUP_PREFIX "Backup-"
#endif

#ifndef DELAY
#define DELAY (7 * 24 * 60 * 60)
#endif

#ifndef DATE_LENGTH
#define DATE_LENGTH 11
#endif

int get_last_date(time_t current_time, char *last_date, time_t *last_time);

int create_dir(const char *path);

int copy_file(const char *from, const char *to);

int change_version(const char *from, const char *to);
