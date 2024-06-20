class Node:
    def evaluate(self):
        pass


class NumberNode(Node):
    def __init__(self, value):
        self.value = value

    def evaluate(self):
        return self.value


class OperatorNode(Node):
    def __init__(self, operator, left, right):
        self.operator = operator
        self.left = left
        self.right = right

    def evaluate(self):
        if self.operator == 'PLUS':
            return self.left.evaluate() + self.right.evaluate()
        elif self.operator == 'MINUS':
            return self.left.evaluate() - self.right.evaluate()
        elif self.operator == 'TIMES':
            return self.left.evaluate() * self.right.evaluate()
        elif self.operator == 'DIVIDE':
            return self.left.evaluate() / self.right.evaluate()


def read_number(line, index):
    number = 0
    while index < len(line) and line[index].isdigit():
        number = number * 10 + int(line[index])
        index += 1
    if index < len(line) and line[index] == '.':
        index += 1
        decimal = 0.1
        while index < len(line) and line[index].isdigit():
            number += int(line[index]) * decimal
            decimal /= 10
            index += 1
    token = {'type': 'NUMBER', 'number': number}
    return token, index


def read_plus(line, index):
    token = {'type': 'PLUS'}
    return token, index + 1


def read_minus(line, index):
    token = {'type': 'MINUS'}
    return token, index + 1


def read_times(line, index):
    token = {'type': 'TIMES'}
    return token, index + 1


def read_divide(line, index):
    token = {'type': 'DIVIDE'}
    return token, index + 1

def read_lparen(line, index):
    token = {'type': 'LPAREN'}
    return token, index + 1

def read_rparen(line, index):
    token = {'type': 'RPAREN'}
    return token, index + 1


def tokenize(line):
    tokens = []
    index = 0
    while index < len(line):
        if line[index].isdigit():
            (token, index) = read_number(line, index)
        elif line[index] == '+':
            (token, index) = read_plus(line, index)
        elif line[index] == '-':
            (token, index) = read_minus(line, index)
        elif line[index] == '*':
            (token, index) = read_times(line, index)
        elif line[index] == '/':
            (token, index) = read_divide(line, index)
        elif line[index] == '(':
            (token, index) = read_rparen(line, index)
        elif line[index] == ')':
            (token, index) = read_rparen(line, index)
        else:
            print('Invalid character found: ' + line[index])
            exit(1)
        tokens.append(token)
    return tokens


def parse(tokens):
    def parse_expression(index):
        if tokens[index]['type'] == 'NUMBER':
            node = NumberNode(tokens[index]['number'])
            index += 1
        elif tokens[index]['type'] == 'LPAREN':
            index += 1  # skip '('
            node, index = parse_expression(index)
            index += 1  # skip ')'
        else:
            node, index = None, index

        while index < len(tokens) and tokens[index]['type'] in ('PLUS', 'MINUS', 'TIMES', 'DIVIDE'):
            operator = tokens[index]['type']
            index += 1
            right_node, index = parse_expression(index)
            node = OperatorNode(operator, node, right_node)

        return node, index

    root, _ = parse_expression(0)
    return root


def evaluate(expression):
    tokens = tokenize(expression)
    tree = parse(tokens)
    return tree.evaluate()


def test(line):
    actual_answer = evaluate(line)
    expected_answer = eval(line)
    if abs(actual_answer - expected_answer) < 1e-8:
        print("PASS! (%s = %f)" % (line, expected_answer))
    else:
        print("FAIL! (%s should be %f but was %f)" % (line, expected_answer, actual_answer))


def run_test():
    print("==== Test started! ====")
    test("1+2")
    test("1.0+2.1-3")
    test("99999+1")
    test("1.00000+0")
    test("2*3")
    test("7/5")
    test("2/(3-1)")
    test("(2*(12-2))+3")
    test("(((3+1)*10+2)/4)+9*(3-2)")
    test("3-6")
    print("==== Test finished! ====\n")


if __name__ == "__main__":
    run_test()
