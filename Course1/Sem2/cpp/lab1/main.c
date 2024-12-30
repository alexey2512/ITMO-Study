#include "return_codes.h"

#include <stdint.h>
#include <stdio.h>
#include <string.h>

#define MIN(a, b) (a > b ? b : a)
#define INF(a) (a == 2 || a == 3)

typedef struct
{
	uint8_t sign;
	int16_t exponent;
	uint32_t mantis;
} Frac;

Frac createFrac(uint32_t number, uint8_t e_size, uint8_t m_size)
{
	uint8_t sign = number >> (e_size + m_size);
	int16_t exponent = (int16_t)(number << (32 - e_size - m_size) >> (32 - e_size));
	uint32_t mantis = number << (32 - m_size) >> (32 - m_size);
	if (exponent == (1 << e_size) - 1)
	{
		sign += 2 + (mantis != 0) * 2;
		mantis = 1;
	}
	else if (!exponent && mantis)
	{
		int8_t power = 0;
		while (mantis < (1 << m_size))
		{
			mantis <<= 1;
			power++;
		}
		exponent = (int16_t)(-(1 << (e_size - 1)) + 2 - power);
	}
	else if (exponent)
	{
		mantis += 1 << m_size;
		exponent = (int16_t)(exponent - (1 << (e_size - 1)) + 1);
	}
	Frac result = { sign, exponent, mantis };
	return result;
}

void print(Frac number, uint8_t m_size)
{
	uint8_t sign = number.sign;
	int16_t exponent = number.exponent;
	uint32_t mantis = number.mantis;
	uint8_t out_size = (m_size >> 2) + ((m_size & 3) != 0);

	if (sign >= 4)
	{
		printf("nan");
		return;
	}
	if (sign & 1)
		printf("-");
	if (sign >= 2)
		printf("inf");
	else if (!mantis)
	{
		printf("0x0.");
		while (out_size--)
			printf("0");
		printf("p+0");
	}
	else
	{
		mantis = (mantis & ((1 << m_size) - 1)) << ((4 - (m_size & 3)) & 3);
		printf("0x1.%0*xp", out_size, mantis);
		printf("%s%hd", exponent >= 0 ? "+" : "", exponent);
	}
	printf("\n");
}

Frac getNan(uint8_t e_size, uint8_t m_size)
{
	return createFrac((((1 << e_size) - 1) << (m_size)) + 1, e_size, m_size);
	// cast to Frac breaks the tests on GH for windows(
}

Frac getInfinity(uint8_t sign, uint8_t e_size, uint8_t m_size)
{
	return createFrac((sign << (e_size + m_size)) + (((1 << e_size) - 1) << m_size), e_size, m_size);
}

Frac getZero(uint8_t sign, uint8_t e_size, uint8_t m_size)
{
	return createFrac(sign << (m_size + e_size), e_size, m_size);
}

void ensureMantis(uint8_t *s, uint64_t *m, int16_t *e, uint8_t e_size, uint8_t m_size, uint8_t round_type, uint8_t tail)
{
	if (*m >= (1 << (m_size + 1)))
	{
		uint8_t all_size = 0;
		uint64_t x = 1;
		while (*m >= x)
		{
			all_size += 1;
			x <<= 1;
		}
		uint8_t suf_size = all_size - m_size - 1;
		uint64_t suffix = *m << (64 - suf_size) >> (64 - suf_size);
		*m >>= suf_size;
		*e = (int16_t)(*e + suf_size);
		if (round_type == 1 &&
				(suffix > ((uint64_t)1 << (suf_size - 1)) || suffix == ((uint64_t)1 << (suf_size - 1)) && ((*m & 1) || tail)) ||
			(suffix || tail) && (round_type == 2 && !(*s) || round_type == 3 && *s))
		{
			*m += 1;
			if (*m == (1 << (m_size + 1)))
			{
				*m >>= 1;
				*e += 1;
			}
		}
	}
	else if (*m)
		while (*m < (1 << m_size))
		{
			*m <<= 1;
			*e -= 1;
		}
	if (*e >= 1 << (e_size - 1))
	{
		if (round_type == 2 && *s || round_type == 3 && !(*s) || round_type == 0)
		{
			*e = (int16_t)((1 << (e_size - 1)) - 1);
			*m = (1 << (m_size + 1)) - 1;
		}
		else
			*s += 2;
	}
	else if (*e < -(1 << (e_size - 1)) + 2 - m_size)
	{
		if (round_type == 2 && !(*s) || round_type == 3 && *s)
		{
			*e = (int16_t)(-(1 << (e_size - 1)) + 2 - m_size);
			*m = 1 << m_size;
		}
		else
			*m = 0;
	}
}

Frac add(Frac num1, Frac num2, uint8_t round_type, uint8_t e_size, uint8_t m_size)
{
	if (num2.exponent > num1.exponent || num2.exponent == num1.exponent && num2.mantis > num1.mantis)
	{
		Frac num = num1;
		num1 = num2;
		num2 = num;
	}
	uint8_t s1 = num1.sign, s2 = num2.sign;
	int16_t e1 = num1.exponent, e2 = num2.exponent;
	uint64_t m1 = num1.mantis;
	uint32_t m2 = num2.mantis;

	if (s1 >= 4 || s2 >= 4 || s1 == 2 && s2 == 3 || s1 == 3 && s2 == 2)
		return getNan(e_size, m_size);
	else if (!m1 && !m2 || e1 == e2 && m1 == m2 && s1 ^ s2)
		return getZero(s1 & s2, e_size, m_size);
	else if (INF(s1) || !m2)
		return num1;
	else if (INF(s2) || !m1)
		return num2;

	uint8_t delta = MIN(e1 - e2, 62 - m_size);
	m1 <<= delta;
	m2 >>= MIN(e1 - e2 - delta, m_size + 1);
	uint8_t tail = (e1 - e2 - delta > m_size);
	e1 = (int16_t)(e1 - delta);
	m1 = s1 == s2 ? m1 + m2 : m1 - m2;
	if (tail && s1 != s2)
		m1--;
	ensureMantis(&s1, &m1, &e1, e_size, m_size, round_type, tail);
	Frac result = { s1, e1, (uint32_t)m1 };
	return result;
}

Frac subtract(Frac num1, Frac num2, uint8_t round_type, uint8_t e_size, uint8_t m_size)
{
	num2.sign ^= 1;
	return add(num1, num2, round_type, e_size, m_size);
}

Frac multiply(Frac num1, Frac num2, uint8_t round_type, uint8_t e_size, uint8_t m_size)
{
	uint8_t s1 = num1.sign, s2 = num2.sign;
	int16_t e1 = num1.exponent, e2 = num2.exponent;
	uint64_t m1 = num1.mantis;
	uint32_t m2 = num2.mantis;

	if (s1 >= 4 || s2 >= 4 || INF(s1) && !m2 || INF(s2) && !m1)
		return getNan(e_size, m_size);
	else if (!m1 || !m2)
		return getZero(s1 ^ s2, e_size, m_size);
	else if (INF(s1) || INF(s2))
		return getInfinity((s1 ^ s2) & 1, e_size, m_size);

	m1 *= m2;
	e1 = (int16_t)(e1 + e2 - m_size);
	s1 ^= s2;
	ensureMantis(&s1, &m1, &e1, e_size, m_size, round_type, 0);
	Frac result = { s1, e1, (uint32_t)m1 };
	return result;
}

Frac divide(Frac num1, Frac num2, uint8_t round_type, uint8_t e_size, uint8_t m_size)
{
	uint8_t s1 = num1.sign, s2 = num2.sign;
	int16_t e1 = num1.exponent, e2 = num2.exponent;
	uint64_t m1 = num1.mantis;
	uint32_t m2 = num2.mantis;

	if (s1 >= 4 || s2 >= 4 || INF(s1) && INF(s2) || !m1 && !m2)
		return getNan(e_size, m_size);
	else if (INF(s1) || !m2)
		return getInfinity((s1 ^ s2) & 1, e_size, m_size);
	else if (!m1 || INF(s2))
		return getZero((s1 ^ s2) & 1, e_size, m_size);

	uint8_t delta = 63 - m_size;
	m1 <<= delta;
	e1 = (int16_t)(e1 - e2 - delta + m_size);
	uint64_t m = m1 / m2;
	uint8_t tail = m1 % m2 != 0;
	s1 ^= s2;
	ensureMantis(&s1, &m, &e1, e_size, m_size, round_type, tail);
	Frac result = { s1, e1, (uint32_t)m };
	return result;
}

int main(int argc, char *argv[])
{
	if (argc != 4 && argc != 6)
	{
		fprintf(stderr, "expected 3 or 5 tokens, actual %d", argc - 1);
		goto invalid;
	}

	char format;
	if (strlen(argv[1]) != 1 || sscanf(argv[1], "%c", &format) != 1)
	{
		fprintf(stderr, "input is incorrect: can not read format");
		goto invalid;
	}
	if (format != 'h' && format != 'f')
	{
		fprintf(stderr, "expected format 'h' or 'f', actual: '%c'", format);
		goto invalid;
	}

	uint8_t round_type;
	if (sscanf(argv[2], "%hhd", &round_type) != 1)
	{
		fprintf(stderr, "can not read rounding type %s as integer", argv[2]);
		goto invalid;
	}
	if (round_type > 3 || round_type < 0)
	{
		fprintf(stderr, "expected rounding type 0, 1, 2 or 3, actual: '%hhd'", round_type);
		goto invalid;
	}

	uint8_t e_size = format == 'f' ? 8 : 5;
	uint8_t m_size = format == 'f' ? 23 : 10;
	uint32_t number;
	sscanf(argv[3], "%x", &number);
	Frac structure1 = createFrac(number, e_size, m_size);
	if (argc == 4)
		print(structure1, m_size);
	else
	{
		char op;
		if (strlen(argv[4]) != 1 || sscanf(argv[4], "%c", &op) != 1)
		{
			fprintf(stderr, "can not read operation %s", argv[4]);
			goto invalid;
		}
		sscanf(argv[5], "%x", &number);
		Frac structure2 = createFrac(number, e_size, m_size);
		Frac result;
		switch (op)
		{
		case '+':
			result = add(structure1, structure2, round_type, e_size, m_size);
			break;
		case '-':
			result = subtract(structure1, structure2, round_type, e_size, m_size);
			break;
		case '*':
			result = multiply(structure1, structure2, round_type, e_size, m_size);
			break;
		case '/':
			result = divide(structure1, structure2, round_type, e_size, m_size);
			break;
		default:
			fprintf(stderr, "expected operation '+', '-', '*' or '/', actual: '%c'", op);
			goto invalid;
		}
		print(result, m_size);
	}
	return SUCCESS;

invalid:
	return ERROR_ARGUMENTS_INVALID;
}
