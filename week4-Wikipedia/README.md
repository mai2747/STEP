## FindMostPopularPages()
足されるvalueをまとめるための新リストを作成。scores[]*0.15/scores.lengthの値を格納する addToAll を作成。\
各IDにおいて、linksを使用して"リンクされている数 n" と"リンク先ID"の割り出し、
scores[]にあるvalue*0.85/n と addToAll を IDのvalueへ追加していく。\
全リンク先IDをループできたらscoresに追加、収束済みか確認 -> false であれば再度ループ。\
収束はscoresサイズのループにて int syusoku += Math.sqrt(scores[] - newScores[]) が 0.01を下回ればtrueとする。\
が、\
小数の位が多すぎるのか、収束する前にノードの値がINFINITYになり正しく動かない。
```
scores[] + scoresToAdd => 1.0 + 0.3625
old: 1.0
new: 1.3625
sum: 0.36249995
scores[] + scoresToAdd => 1.0 + 1.6658332
old: 1.0
new: 2.6658332
sum: 2.0283332
.
.
.
scores[] + scoresToAdd => 3.00545E37 + 3.0054497E37
old: 3.00545E37
new: 6.0109E37
sum: 3.00545E37
scores[] + scoresToAdd => 2.7639395E38 + 2.7639397E38
old: 2.7639395E38
new: Infinity
sum: Infinity
```
後にNANも登場してしまうので修正が必要、、

#### 更新
分配しきって0になるべきスコアを、分配後も保持してしまっていたことが原因でInfinityが表示されていたようでした。またデバッグ用に書いていたコード自体にも不備があり、そもそも正しくテストすらできていないじょうたいでした。\
スコアの計算自体は改善でき、正しく収束するようにできましたがデバッグ用コードに関してはまだ触れていません。3日以内に修正する所存。

---
---

## FindMostPopularPages()
Create a new list to sum value and`addToAll` which store `scores[]*0.15/scores.length`.\
For each ID, use the links to determine the number of links n and linked ID, then add `scores[]*0.85 / n` and `addToAll` to the value of the ID.\
Once all linked IDs have been looped through, add them to scores and check if convergence has been reached. If it is false, loop again.\
Convergence is determined by looping through the size of scores and calculating int `syusoku += Math.sqrt(scores[] - newScores[])`. If the result is below 0.01, it is considered true.\
However,\
The node values turn to INFINITY before convergence and it doesn't work correctly. (It might be because there are too many decimal places.)
```
scores[] + scoresToAdd => 1.0 + 0.3625
old: 1.0
new: 1.3625
sum: 0.36249995
scores[] + scoresToAdd => 1.0 + 1.6658332
old: 1.0
new: 2.6658332
sum: 2.0283332
.
.
.
scores[] + scoresToAdd => 3.00545E37 + 3.0054497E37
old: 3.00545E37
new: 6.0109E37
sum: 3.00545E37
scores[] + scoresToAdd => 2.7639395E38 + 2.7639397E38
old: 2.7639395E38
new: Infinity
sum: Infinity
```
(Need to be fixed as NAN appears later...)

#### Update
It seems that the cause of the Infinity display was due to the scores that should have been completely distributed, but were still retained after distribution.\ Additionally, there were flaws in the code I had written for debugging, and as a result, I wasn't even able to test it correctly in the first place.\
I was able to improve the score calculation and make it converge correctly, but I haven't touched the debugging code yet.
