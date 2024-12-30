#include <stdint.h>
#include <stdio.h>

uint32_t product(uint32_t fact, uint16_t num, uint32_t mod)
{
	return (((uint64_t)fact * num) % mod);
}

uint32_t factorial(uint16_t num, uint32_t mod)
{
	uint32_t result = 1;
	// uint16_MAX + 1 = 0
	for (uint16_t i = 2; i <= num && i != 0; i++)
	{
		result = product(result, i, mod);
	}
	return result;
}

uint8_t length(uint32_t num)
{
	uint8_t result = 1;
	while (num >= 10)
	{
		num /= 10;
		result++;
	}
	return result;
}

uint8_t choose_length(uint32_t num, uint8_t max_len)
{
	uint8_t len = length(num);
	if (len > max_len)
	{
		return len;
	}
	return max_len;
}

uint32_t next_factorial(uint32_t old_fact, uint16_t n_start, uint16_t num, uint32_t mod)
{
	if (num == n_start || num == 0)
	{
		return factorial(num, mod);
	}
	return product(old_fact, num, mod);
}

void space_print(uint8_t len)
{
	for (int i = 0; i < len; i++)
	{
		printf(" ");
	}
}

void number_format_print(uint32_t num, int8_t align, uint8_t max_len)
{
	uint8_t space_count = max_len - length(num);
	if (align == -1)
	{
		printf("%d", num);
		space_print(space_count);
	}
	else if (align == 1)
	{
		space_print(space_count);
		printf("%d", num);
	}
	else
	{
		uint8_t half_space = space_count >> 1;
		space_print(space_count - half_space);
		printf("%d", num);
		space_print(half_space);
	}
}

void table_string_print(uint32_t fact, uint32_t num, int8_t align, uint8_t max_arg_len, uint8_t max_fact_len)
{
	printf("| ");
	number_format_print(num, align, max_arg_len);
	printf(" | ");
	number_format_print(fact, align, max_fact_len);
	printf(" |\n");
}

void frame_print(uint8_t max_arg_len, uint8_t max_fact_len)
{
	printf("+-");
	for (uint8_t i = 0; i < max_arg_len; i++)
	{
		printf("-");
	}
	printf("-+-");
	for (uint8_t i = 0; i < max_fact_len; i++)
	{
		printf("-");
	}
	printf("-+\n");
}

int main()
{
	int32_t temp_start;
	int32_t temp_end;
	int8_t align;

	if (scanf("%d %d %hhd", &temp_start, &temp_end, &align) != 3)
	{
		fprintf(stderr, "your input is incorrect");
		return 1;
	}
	if (temp_start < 0 || temp_end < 0)
	{
		fprintf(stderr, "your input contains negative start/end");
		return 1;
	}

	uint16_t n_start = temp_start;
	uint16_t n_end = temp_end;
	const uint32_t mod = 2147483647;
	uint8_t max_arg_len = 1;
	uint8_t max_fact_len = 2;

	uint32_t fact = 1;
	for (uint16_t i = n_start; i != n_end; i++)
	{
		fact = next_factorial(fact, n_start, i, mod);
		max_arg_len = choose_length(i, max_arg_len);
		max_fact_len = choose_length(fact, max_fact_len);
	}
	fact = next_factorial(fact, n_start, n_end, mod);
	max_arg_len = choose_length(n_end, max_arg_len);
	max_fact_len = choose_length(fact, max_fact_len);

	frame_print(max_arg_len, max_fact_len);

	if (align == -1)
	{
		printf("| n");
		space_print(max_arg_len - 1);
		printf(" | n!");
		space_print(max_fact_len - 2);
		printf(" |\n");
	}
	else if (align == 1)
	{
		printf("| ");
		space_print(max_arg_len - 1);
		printf("n | ");
		space_print(max_fact_len - 2);
		printf("n! |\n");
	}
	else
	{
		printf("| ");
		uint8_t half_space = (max_arg_len - 1) >> 1;
		space_print(max_arg_len - 1 - half_space);
		printf("n");
		space_print(half_space);
		printf(" | ");
		half_space = (max_fact_len - 2) >> 1;
		space_print(max_fact_len - 2 - half_space);
		printf("n!");
		space_print(half_space);
		printf(" |\n");
	}

	frame_print(max_arg_len, max_fact_len);

	fact = 1;
	for (uint16_t i = n_start; i != n_end; i++)
	{
		fact = next_factorial(fact, n_start, i, mod);
		table_string_print(fact, i, align, max_arg_len, max_fact_len);
	}
	fact = next_factorial(fact, n_start, n_end, mod);
	table_string_print(fact, n_end, align, max_arg_len, max_fact_len);

	frame_print(max_arg_len, max_fact_len);

	return 0;
}
