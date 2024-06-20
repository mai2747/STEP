
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
            (token, index) = detect_bracket(line, index)
        elif line[index] == ')':  # Will be only called for terminate recursion for brackets
            return tokens, index+1
        else:
            print('Invalid character found: ' + line[index])
            exit(1)
        tokens.append(token)
    return tokens, index

def detect_bracket(line, index):
    # Recursively tokenise inside brackets
    index += 1  # Count for left bracket
    sub_tokens, new_index = tokenize(line[index:])
    sub_tokens = evaluate_mul_div(sub_tokens)  # Update index to the end of bracket part
    part_ans = evaluate(sub_tokens)
    index += new_index
    print('Ans inside of bracket: ', part_ans)

    token = {'type': 'NUMBER', 'number': part_ans}
    return token, index



def evaluate_mul_div(tokens):
    tokens.insert(0, {'type': 'PLUS'})  # Insert a dummy '+' token
    new_tokens = []
    index = 1
    new_index = 1
    while index < len(tokens):
        if tokens[index]['type'] == 'NUMBER':
            if tokens[index - 1]['type'] == 'TIMES':
                new_tokens[new_index - 1]['number'] *= tokens[index]['number']

            elif tokens[index - 1]['type'] == 'DIVIDE':
                if tokens[index]['number'] == 0:
                    print('Invalid calculation: cannot be divided by 0')
                    exit(1)
                 new_tokens[new_index - 1]['number'] /= tokens[index]['number']

            else
                new_tokens[new_index]['type'] = tokens[index]['type']  // operator
                new_index += 1
                new_tokens[new_index]['type'] = tokens[index]['type']  // operand
                new_tokens[new_index]['number'] = tokens[index]['number'] // ?
        index += 1
        new_index += 1
    

    return new_tokens

def evaluate(tokens):
    answer = 0
    tokens.insert(0, {'type': 'PLUS'})  # Insert a dummy '+' token
    index = 1

    # Evaluate multiplication and division first and
    # update to tokens only containing NUMBER, PLUS, MINUS
    tokens = evaluate_mul_div(tokens)

    while index < len(tokens):
        if tokens[index]['type'] == 'NUMBER':
            if tokens[index - 1]['type'] == 'PLUS':
                answer += tokens[index]['number']
            elif tokens[index - 1]['type'] == 'MINUS':
                answer -= tokens[index]['number']
            else:
                print('Invalid syntax')
                print(tokens[index-1]['number'])
                exit(1)
        index += 1
    return answer


def test(line):
    tokens, _ = tokenize(line)
    actual_answer = evaluate(tokens)
    expected_answer = eval(line)
    if abs(actual_answer - expected_answer) < 1e-8:
        print("PASS! (%s = %f)" % (line, expected_answer))
    else:
        print("FAIL! (%s should be %f but was %f)" % (line, expected_answer, actual_answer))


# Add more tests to this function :)
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
    test("5/0")

    print("==== Test finished! ====\n")



#while True:
 #   print('> ', end="")
  #  line = input()
   # tokens = tokenize(line)
    #answer = evaluate(tokens)
    #print("answer = %f\n" % answer)

def main():
    run_test()

if __name__ == "__main__":
    run_test()
