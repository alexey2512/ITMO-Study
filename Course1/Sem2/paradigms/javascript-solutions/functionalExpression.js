"use strict"

const binaryOp = (op) => (a, b) => (...args) => op(a(...args), b(...args));
const unaryOp = (op) => (a) => (...args) => op(a(...args));

const cnst = (a) => () => a;
const pi = cnst(Math.PI);
const e = cnst(Math.E);
const cube = unaryOp((a) => Math.pow(a, 3));
const cbrt = unaryOp((a) => Math.cbrt(a));
const negate = unaryOp((a) => -a);
const add = binaryOp((a, b) => a + b);
const subtract = binaryOp((a, b) => a - b);
const multiply = binaryOp((a, b) => a * b);
const divide = binaryOp((a, b) => a / b);
const variable = (string) => (x, y, z) => { return {x, y, z}[string] };

let expression = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            cnst(2),
            variable("x")
        )
    ),
    cnst(1)
);

for (let i = 0; i < 11; i++) {
    console.log("x = " + i + ": " + expression(i, 0, 0));
}
