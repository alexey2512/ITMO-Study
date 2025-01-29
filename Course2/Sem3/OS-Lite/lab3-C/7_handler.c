#include "7_helpers.h"
#include "return_codes.h"
#include <sys/types.h>

#include <math.h>
#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define FSZ 8

typedef enum
{
	NF = 0,	   // empty cell
	WK = 1,	   // white king
	WH = 2,	   // white horse
	WP = 3,	   // white pawn
	BK = 4,	   // black king
	BH = 5,	   // black horse
	BP = 6	   // black pawn
} ChessFigure;

static uint8_t is_white(ChessFigure field[FSZ][FSZ], uint8_t x, uint8_t y)
{
	return field[x][y] == WP || field[x][y] == WK || field[x][y] == WH;
}

static uint8_t is_black(ChessFigure field[FSZ][FSZ], uint8_t x, uint8_t y)
{
	return field[x][y] == BP || field[x][y] == BK || field[x][y] == BH;
}

static uint8_t check_king_move(ChessFigure field[FSZ][FSZ], uint8_t owner, uint8_t mover, uint8_t x, uint8_t y, uint8_t x_, uint8_t y_)
{
	if (mover != owner)
	{
		fprintf(stderr, "You can't move this figure. Try again\n");
		return 0;
	}
	if (!(abs(x - x_) <= 1 && abs(y - y_) <= 1 && (x != x_ || y != y_) &&
		  (owner ? !is_black(field, x_, y_) : !is_white(field, x_, y_))))
	{
		fprintf(stderr, "Incorrect move for king. Try again\n");
		return 0;
	}
	return 1;
}

static uint8_t check_horse_move(ChessFigure field[FSZ][FSZ], uint8_t owner, uint8_t mover, uint8_t x, uint8_t y, uint8_t x_, uint8_t y_)
{
	if (mover != owner)
	{
		fprintf(stderr, "You can't move this figure. Try again\n");
		return 0;
	}
	if (!(abs(x - x_) == 1 && abs(y - y_) == 2 || abs(x - x_) == 2 && abs(y - y_) == 1) &&
		(owner ? !is_black(field, x_, y_) : !is_white(field, x_, y_)))
	{
		fprintf(stderr, "Incorrect move for horse. Try again\n");
		return 0;
	}
	return 1;
}

static uint8_t move_is_correct(ChessFigure field[FSZ][FSZ], uint8_t mover, uint8_t x, uint8_t y, uint8_t x_, uint8_t y_)
{
	switch (field[x][y])
	{
	case NF:
		fprintf(stderr, "This an empty cell. Try again\n");
		return 0;
	case WK:
		return check_king_move(field, 0, mover, x, y, x_, y_);
	case BK:
		return check_king_move(field, 1, mover, x, y, x_, y_);
	case WH:
		return check_horse_move(field, 0, mover, x, y, x_, y_);
	case BH:
		return check_horse_move(field, 1, mover, x, y, x_, y_);
	case WP:
		if (mover != 0)
		{
			fprintf(stderr, "You can't move this figure. Try again\n");
			return 0;
		}
		if ((x == 1 && x_ == 3 && y == y_ && field[2][y] == NF && field[3][y] == NF) ||
			(x_ == x + 1 && y == y_ && field[x_][y] == NF) || (x_ == x + 1 && abs(y - y_) == 1 && is_black(field, x_, y_)))
			return 1;
		fprintf(stderr, "Incorrect pawn move. Try again\n");
		return 0;
	case BP:
		if (mover != 1)
		{
			fprintf(stderr, "You can't move this figure. Try again\n");
			return 0;
		}
		if ((x == 6 && x_ == 4 && y == y_ && field[5][y] == NF && field[4][y] == NF) ||
			(x_ == x - 1 && y == y_ && field[x_][y] == NF) || (x_ == x - 1 && abs(y - y_) == 1 && is_white(field, x_, y_)))
			return 1;
		fprintf(stderr, "Incorrect pawn move. Try again\n");
		return 0;
	}
	return 0;
}

static void print_field(ChessFigure field[FSZ][FSZ])
{
	char symbols[7][3] = { "..", "WK", "WH", "WP", "BK", "BH", "BP" };
	for (size_t i = 0; i < FSZ; i++)
	{
		for (size_t j = 0; j < FSZ; j++)
			printf("%s ", symbols[field[i][j]]);
		printf("\n");
	}
}

int main(void)
{
	sleep(1);
	int code = SUCCESS;
	FILE *channel = fopen(CHANNEL_FILE, "r");
	if (channel == NULL)
	{
		fprintf(stderr, "HANDLER: failed to open file %s\n", CHANNEL_FILE);
		return OPEN_FILE_ERROR;
	}
	setvbuf(channel, NULL, _IONBF, 0);

	pid_t gen_pid;
	if (fscanf(channel, "generator_pid=%d\n", &gen_pid) < 1)
	{
		fprintf(stderr, "HANDLER: failed to read generator_pid from file %s\n", CHANNEL_FILE);
		fclose(channel);
		return READ_FILE_ERROR;
	}
	size_t last_pos = ftell(channel);

	ChessFigure field[FSZ][FSZ] = {
		{ NF, WH, NF, WK, NF, NF, WH, NF }, { WP, WP, WP, WP, WP, WP, WP, WP }, { NF, NF, NF, NF, NF, NF, NF, NF },
		{ NF, NF, NF, NF, NF, NF, NF, NF }, { NF, NF, NF, NF, NF, NF, NF, NF }, { NF, NF, NF, NF, NF, NF, NF, NF },
		{ BP, BP, BP, BP, BP, BP, BP, BP }, { NF, BH, NF, NF, BK, NF, BH, NF }
	};
	print_field(field);

	uint8_t cur_mover = 0;
	uint8_t waiting = 0;
	char string[32];
	while (1)
	{
		if (!waiting && kill(gen_pid, cur_mover ? SIGUSR2 : SIGUSR1) != 0)
		{
			fprintf(stderr, "HANDLER: failed to send SIGUSR to producer with pid=%d\n", gen_pid);
			break;
		}
		sleep(1);
		fseek(channel, last_pos, SEEK_SET);
		if (fgets(string, sizeof(string), channel) == NULL)
		{
			waiting = 1;
			continue;
		}
		waiting = 0;
		last_pos = ftell(channel);

		uint8_t x, y, x_, y_;
		if (sscanf(string, "%hhu %hhu %hhu %hhu\n", &x, &y, &x_, &y_) < 4)
		{
			fprintf(stderr, "HANDLER: failed to get next move\n");
			code = READ_FILE_ERROR;
			break;
		}

		if (move_is_correct(field, cur_mover, x, y, x_, y_))
		{
			ChessFigure beated = field[x_][y_];
			field[x_][y_] = field[x][y];
			field[x][y] = NF;
			print_field(field);
			cur_mover ^= 1;
			if (beated == WK)
			{
				printf("Second player won!\n");
				break;
			}
			else if (beated == BK)
			{
				printf("First player won!\n");
				break;
			}
		}
	}

	fclose(channel);
	if (kill(gen_pid, SIGTERM) != 0)
	{
		fprintf(stderr, "HANDLER: failed to send SIGTERM to producer with pid=%d\n", gen_pid);
		code = SYSTEM_ERROR;
	}

	return code;
}
