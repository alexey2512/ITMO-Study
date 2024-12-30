"use strict"

const varToIndex = {"x" : 0, "y" : 1, "z" : 2};

const Expression = function (constructor, toString, evaluate, prefix = toString, arn = 0) {
    constructor.prototype.toString = toString;
    constructor.prototype.evaluate = evaluate;
    constructor.prototype.prefix = prefix;
    constructor.arn = arn;
    return constructor;
}

const Const = Expression(
    function (value) { this.value = value; },
    function () { return String(this.value); },
    function () { return this.value; },
)

const Variable = Expression(
    function (name) { this.name = name; },
    function () { return this.name; },
    function (...args) { return args[varToIndex[this.name]]; },
)

const ArbitraryOperation = (sign, calc, arn) => Expression(
    function (...operands) {
        this.operands = Array.from(operands);
    },
    function () {
        return this.operands.map(a => a.toString()).join(' ') + ' ' + sign;
    },
    function (...args) {
        return calc(this.operands.map(a => a.evaluate(...args)));
    },
    function () {
        return '(' + sign + ' ' + this.operands.map(a => a.prefix()).join(' ') + ')';
    },
    arn
)

const product = (operands) => {
    let result = 1;
    for (let i = 0; i < operands.length; i++) {
        result *= operands[i];
    }
    return result;
}

const Add = ArbitraryOperation("+", ops => ops[0] + ops[1], 2);
const Subtract = ArbitraryOperation("-", ops => ops[0] - ops[1], 2);
const Multiply = ArbitraryOperation("*", ops => ops[0] * ops[1], 2);
const Divide = ArbitraryOperation("/", ops => ops[0] / ops[1], 2);
const Negate = ArbitraryOperation("negate", ops => -ops[0], 1);
const Sinh = ArbitraryOperation("sinh", Math.sinh, 1);
const Cosh = ArbitraryOperation("cosh", Math.cosh, 1);
const Product = ArbitraryOperation("product", product, -1);
const Geom = ArbitraryOperation("geom", ops => Math.pow(Math.abs(product(ops)), 1 / ops.length), -1);

const constructors = {'negate' : Negate, 'sinh' : Sinh, 'cosh' : Cosh,
    '+' : Add, '-' : Subtract, '*' : Multiply, '/' : Divide,
    'product' : Product, 'geom' : Geom};

function createError(name) {
    function CustomError(message) {
        this.message = message;
    }
    CustomError.prototype = Object.create(Error.prototype);
    CustomError.prototype.name = name;
    return CustomError;
}

const IncorrectBracketSequenceError = createError("IncorrectBracketSequenceError");
const UnknownOperationError = createError("UnknownOperationError");
const UnknownTokenError = createError("UnknownTokenError");
const UnknownSymbolError = createError("UnknownSymbolError");
const EmptyExpressionError = createError("EmptyExpressionError");
const MissingOperationError = createError("MissingOperationError");
const MissingArgumentError = createError("MissingArgumentError");

const parse = (expr) => {

    let expression = expr.trim().split(/\s+/);
    let index = expression.length - 1;
    let cur = expression[index];

    const next = () => {
        index--;
        cur = index < 0 ? null : expression[index];
    }

    const tk = () => {
        let result = cur;
        next();
        return String(result);
    }

    const multiArgument = (count) => {
        let result = [];
        for (let i = 0; i < count; i++) {
            result.unshift(sift());
        }
        return result;
    }

    const sift = () => {
        let token = tk();
        if (token in constructors) {
            let args = multiArgument(constructors[token].arn);
            return new constructors[token](...args);
        } else if (token in varToIndex) {
            return new Variable(String(token));
        } else {
            return new Const(parseInt(token));
        }
    }

    return sift();
}

const parsePrefix = (expression) => {

    if (expression === '') {
        throw new EmptyExpressionError("got empty expression: ''");
    }

    let index = 0;
    let cur = expression[index];
    let eof = '\0';

    const next = () => {
        index++;
        cur = index < expression.length ? expression[index] : eof;
    }

    const code = (char) => char.charCodeAt(0);
    const test = (char) => code(char) === code(cur);

    const tk = () => {
        let result = cur;
        next();
        return result;
    }

    const take = (char) => {
        if (test(char)) {
            tk();
            return true;
        }
        return false;
    }

    const skipWhitespaces = () => { while (test(' ')) tk(); }

    const getToken = () => {
        skipWhitespaces();
        let token = '';
        while (!test('(') && !test(')') && !test(' ') && !test(eof)) {
            token += tk();
        }
        skipWhitespaces();
        return token;
    }

    const multiArgument = (condition) => {
        let result = [];
        for (let i = 0; condition(i) && !test(eof); i++) {
            result.push(sift());
            skipWhitespaces();
        }
        return result;
    }

    const parse = () => {
        skipWhitespaces();
        if (test(eof)) {
            throw new EmptyExpressionError("got empty expression: '" + expression + "'");
        }
        let result = sift();
        skipWhitespaces();
        if (!test(eof)) {
            throw new UnknownSymbolError("unexpected symbol '" + cur + "' at position " + index);
        }
        return result;
    }

    const sift = () => {
        skipWhitespaces();
        if (take('(')) {
            let token = getToken();
            if (token in constructors) {
                let args = multiArgument(constructors[token].arn > 0 ?
                    (i) => i < constructors[token].arn :
                    () => !test(')'));
                skipWhitespaces();
                if (!take(')')) {
                    throw new IncorrectBracketSequenceError("expected closing parenthesis at position " + index);
                }
                return new constructors[token](...args);
            } else if (token === '') {
                throw new MissingOperationError("expected operation at position " + index + ", but got nothing");
            } else {
                throw new UnknownOperationError("unknown operation '" + token + "' at position " + (index - token.length - 1));
            }
        }
        let token = getToken();
        if (token in varToIndex) {
            return new Variable(token);
        } else if (/^-?\d+$/.test(token)) {
            return new Const(parseInt(token));
        } else if (token === '') {
            throw new MissingArgumentError("expected argument at position " + index + ", but got nothing");
        } else {
            throw new UnknownTokenError("can not parse '" + token + "' as variable or constant at position " + (index - token.length - 1));
        }
    }

    return parse();
}
