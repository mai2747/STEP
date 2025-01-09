## 積と商への対応
既存のevaluateメソッドをもとに、'*' と '/' の記号に対応させたものを作成した。(evaluate_mul_div method)/
トークン化された情報の中でTIMESとDIVIDEのタイプを持つ演算子の前後をこのメソッド内で計算し、各々の積や商を一つのNUMBERトークンとして再度トークン内に格納する。
これをevaluateメソッド内の計算過程の前に組み込むことで、実際の計算における優先順を再現している。/
またこの際に0で割ろうとしている場合には、"/ 0" を読み取った時点で終了コードを出すようにしているが、この場合、以降のテストを実施されなくなるが許容されるものなのか

## 括弧への対応
入力された式の中に括弧を見つけた時点から再帰的に計算するように設計した。\
括弧で囲まれた式を先に計算し、答えをひとつのNUMBERトークンにまとめてtokeniseをするようにしている。二重、三重となっている場合は一番奥の括弧がはじめに計算される。その計算により取得されたNUMBERトークンを利用して一つ外の括弧、そしてまた次の括弧への計算につなげられる。

---
---

## Handling multiplication and division
&emsp; Based on the existing evaluate method, I created `evaluate_mul_div` to handle the '*' and '/' operators.\
In this method, the multiplication (TIMES) and division (DIVIDE) operators in the tokenised data are processed, calculating the products and quotients, and storing them as NUMBER tokens back into the token list. By incorporating this method into the calculation process before the main computation in the evaluate method, the precedence of these operations in actual calculations is replicated.\
&emsp; Additionally, when attempting to divide by zero (i.e., encountering "/ 0"), the program terminates with an error code at that point. However, this prevents subsequent tests from being run, and I wonder if this behavior is acceptable.

## Handling multiplication and division
&emsp; The design ensures that, when parentheses are detected in the input expression, the calculation is done recursively. 
Expressions within parentheses are evaluated first, and the result is converted into a single NUMBER token, which is then tokenised. In the case of nested parentheses (double, triple, etc.), the innermost parentheses are calculated first. The result of this calculation, a NUMBER token, is then used to continue the calculation in the next outer parentheses, and so on.
