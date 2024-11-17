#ifndef MATH_FUNCTIONS_H
#define MATH_FUNCTIONS_H
#include <cmath>
#include <functional>
#endif

template< typename T >
struct matrix_t
{
	T data[16];
};

template< typename T >
struct vector3_t
{
	T x, y, z;
};

template< typename T >
class Quat
{
  public:
	Quat() : m_value{ 0, 0, 0, 0 } {}

	Quat(T a, T b, T c, T d) : m_value{ b, c, d, a } {}

	Quat(T angle, bool isInRad, struct vector3_t< T > vector)
	{
		T ang = isInRad ? angle / 2 : angle * M_PI / 360;
		T mul = std::sin(ang) / std::sqrt(sqr(vector.x) + sqr(vector.y) + sqr(vector.z));
		m_value[3] = cos(ang);
		m_value[0] = vector.x * mul;
		m_value[1] = vector.y * mul;
		m_value[2] = vector.z * mul;
	}

	const T* data() const { return m_value; }

	Quat< T >& operator+=(const Quat< T >& quat)
	{
		return assign(quat, [](T a, T b) { return a + b; });
	}

	Quat< T >& operator-=(const Quat< T >& quat)
	{
		return assign(quat, [](T a, T b) { return a - b; });
	}

	Quat< T > operator+(const Quat< T >& quat) const { return Quat< T >(m_value) += quat; }

	Quat< T > operator-(const Quat< T >& quat) const { return Quat< T >(m_value) -= quat; }

	Quat< T > operator*(const Quat< T >& quat) const
	{
		return Quat< T >(
			m_value[3] * quat.m_value[3] - m_value[0] * quat.m_value[0] - m_value[1] * quat.m_value[1] - m_value[2] * quat.m_value[2],
			deploy(quat, 0, 1, 2),
			deploy(quat, 1, 2, 0),
			deploy(quat, 2, 0, 1));
	}

	Quat< T > operator~() const { return Quat< T >(m_value[3], -m_value[0], -m_value[1], -m_value[2]); }

	bool operator==(const Quat< T >& quat) const
	{
		bool result = true;
		for (int i = 0; i < 4; i++)
			result &= m_value[i] == quat.m_value[i];
		return result;
	}

	bool operator!=(const Quat< T >& quat) const { return !(this->operator==(quat)); }

	Quat< T > operator*(const T scalar) const
	{
		T tempData[4];
		for (int i = 0; i < 4; i++)
			tempData[i] = m_value[i] * scalar;
		return Quat< T >(tempData);
	}

	Quat< T > operator*(const vector3_t< T >& vec) const
	{
		return Quat< T >(-m_value[0] * vec.x - m_value[1] * vec.y - m_value[2] * vec.z, vec.x * m_value[3], vec.y * m_value[3], vec.z * m_value[3]);
	}

	T angle(bool isInRad = true) const
	{
		T w = m_value[3] / T();
		return isInRad ? std::acos(w) * 2 : std::acos(w) * 360 / M_PI;
	}

	vector3_t< T > apply(const vector3_t< T >& vector) const
	{
		Quat< T > temp = *this;
		temp.normalize();
		Quat< T > result = temp * Quat< T >(0, vector.x, vector.y, vector.z) * ~temp;
		return { result.m_value[0], result.m_value[1], result.m_value[2] };
	}

	matrix_t< T > rotation_matrix() const
	{
		matrix_t< T > result;
		T norm = static_cast< T >(*this);	 // can not replace this with T(), because it doesn't work
		T ar[4];
		for (int i = 0; i < 4; i++)
			ar[i] = m_value[i] / norm;
		for (int i = 0; i < 3; i++)
		{
			result.data[4 * i + i] = 1 - 2 * (sqr(ar[0]) + sqr(ar[1]) + sqr(ar[2]) - sqr(ar[i]));
			result.data[12 + i] = 0;
			result.data[4 * i + 3] = 0;
		}
		result.data[15] = 1;
		result.data[1] = 2 * (ar[0] * ar[1] + ar[2] * ar[3]);
		result.data[2] = 2 * (ar[0] * ar[2] - ar[1] * ar[3]);
		result.data[4] = 2 * (ar[0] * ar[1] - ar[2] * ar[3]);
		result.data[6] = 2 * (ar[1] * ar[2] + ar[0] * ar[3]);
		result.data[8] = 2 * (ar[0] * ar[2] + ar[1] * ar[3]);
		result.data[9] = 2 * (ar[1] * ar[2] - ar[0] * ar[3]);
		return result;
	}

	matrix_t< T > matrix() const
	{
		matrix_t< T > result;
		for (int i = 0; i < 4; i++)
		{
			result.data[4 * i + i] = m_value[3];
			result.data[4 * i + ((1 + 3 * i) & 3)] = ((i & 1) == 0 ? -1 : 1) * m_value[0];
			result.data[4 * i + ((2 + i) & 3)] = (i == 0 || i == 3 ? -1 : 1) * m_value[1];
			result.data[4 * i + ((3 + 3 * i) & 3)] = (i < 2 ? -1 : 1) * m_value[2];
		}
		return result;
	}

	explicit operator T() const
	{
		return static_cast< T >(std::sqrt(sqr(m_value[0]) + sqr(m_value[1]) + sqr(m_value[2]) + sqr(m_value[3])));
	}

  private:
	T m_value[4];

	explicit Quat(const T data[])
	{
		for (int i = 0; i < 4; i++)
			m_value[i] = data[i];
	}

	static T sqr(T a) { return std::pow(a, 2); }	// created this function to make code more compact

	T deploy(const Quat< T >& quat, int by, int f, int s) const
	{
		return m_value[3] * quat.m_value[by] + quat.m_value[3] * m_value[by] + m_value[f] * quat.m_value[s] -
			   quat.m_value[f] * m_value[s];
	}

	Quat< T >& assign(const Quat< T >& quat, T (*func)(T, T))
	{
		for (int i = 0; i < 4; i++)
			m_value[i] = func(m_value[i], quat.m_value[i]);
		return *this;
	}

	void normalize()
	{
		T norm = static_cast< T >(*this);	 // can not replace this with T(), because it doesn't work
		if (norm != static_cast< T >(0))
			for (auto& i : m_value)
				i /= norm;
	}
};
