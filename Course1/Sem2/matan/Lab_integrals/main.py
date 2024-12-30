import matplotlib.pyplot as plt
import numpy as np
from random import random

expression = input("Function from x: ")


def f(x):
    global expression
    return eval(expression, {'x': x,
                             'sin': np.sin,
                             'cos': np.cos,
                             'tan': np.tan,
                             'pow': np.power,
                             'log': np.log,
                             'exp': np.exp,
                             'sqrt': np.sqrt})


def get_equipment(b1, b2, typeE):
    if typeE == 'right':
        return b2
    elif typeE == 'left':
        return b1
    elif typeE == 'median':
        return (b1 + b2) / 2
    elif typeE == 'random':
        return random() * (b2 - b1) + b1


a, b = map(float, input("limits of integration: ").split())
equipment_type = input("type of equipment: ")
number_of_segments = int(input("number of division segments: "))
accuracy = 10
X = []
Y = []
dx = (b - a) / number_of_segments
dx_construction = dx / accuracy
temp = a

for i in range(number_of_segments * accuracy):
    X.append(temp)
    Y.append(f(temp))
    temp += dx_construction

plt.plot(X, Y, color="red")

integral_sum = 0
temp = a
for i in range(number_of_segments):
    x1 = temp
    x2 = x1 + dx
    h = f(get_equipment(x1, x2, equipment_type))
    integral_sum += dx * h
    plt.plot([x1, x2, x2, x1, x1], [0, 0, h, h, 0], color="blue")
    temp += dx

print("Integral summary: " + str(integral_sum))
plt.title("Тип оснащения = " + equipment_type +
          ", количество разбиений = " + str(number_of_segments) +
          "\nИнтегральная сумма: " + str(integral_sum))
plt.show()
