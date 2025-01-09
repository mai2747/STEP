## <宿題１：ハッシュテーブル>
キーの何文字目かの記録をするcountを掛けることにより、アナグラム同士が同じハッシュ値を持つことを避けようと試みた。
> ex) alice -> org(‘a’)*1 + org(‘l’)*2 + org(‘i’)*3 + org(‘c’)*4 + org(‘e’)*e5

しかしここに素数をかけようと何をしようと実行時間が10秒に達することが多かった為、ビットシフトとの掛け合わせを試したところ大きく短縮することができた。\
いずれの案でも複数のテストケースに生まれる差への解決策、また素数の利用よりビットシフトがより効果的な理由は思いついていない。\
以下は上記の計算を試した際の実行時間を秒単位で記した表で、いくつか実行に時間がかかったケースから最長で合った57番目のケースをピックアップしている。

 ハッシュ値 \ テストケース        | 1      | 40    | 57     | 99   
 ------------------------------ | ------ | ----- | ------ | ----- 
 hash += ord(i) * count         | 0.0826 | 4.764 | 13.213 | 12.672
 hash += ord(i) << idx          | 0.0462 | 0.447 | 3.474  | 1.268
 hash += ord(i) * count << idx  | 0.0534 | 0.090 | 2.820  | 0.184
 hash += ord(i) *10^(count-1)   | 0.0380 | 0.026 | 1.432  | 0.030
 
追記) キーの各桁に対して 10^(count-1) を掛けてみたら一番早く計算が終わった…代わりに、長いキーではハッシュ値の桁数が大きくなってしまうのではないかと懸念もしている


 ## <宿題２：ハッシュテーブル vs 木構造>
 #### 木構造が好まれるわけ
 ・ハッシュはメモリの使用量が多いから\
&emsp;&emsp; 衝突を減らすことを目的にテーブルのサイズがある程度確保されている為、余分なメモリまで消費することになる\
・木構造では関連性のあるデータを扱いやすいから\
&emsp;&emsp; ハッシュテーブルは検索等が容易である反面、一つ一つのデータの関連性を無視している為にランダムに格納されてしまう。そのためデータ同士の関連性を残したい場合には木構造のほうが適している。\
・実行時間が不安定だから\
&emsp;&emsp; ハッシュ値を工夫しようと、多くの衝突が起こる可能性自体は存在してしまう。

## <宿題３：キャッシュ>
直近に訪問したサイトを先頭、一番過去に訪問したサイトを末尾に置く双方向連結リストをハッシュテーブルと組み合わせてキャッシュを作る。
```
Node(){
url = https://...
page = ...
previous = ...
next = ...
}
```
以上の要素を持つノードとして、アクセスしたサイトを記録し双方向連結リストを作成する。これにより、訪問順を保管しつつ、キャッシュ内に訪問歴のない場合は新たに先頭へ追加し、末尾のサイトの記録を取り出せるようになる。この際headとtailとなるノードも記録しておく。また、各ノードをハッシュテーブルにも入れることによって、キャッシュ内の記録の有無をO(1)で確認可能にする。\
サイトのURLをキーとして扱い、ハッシュテーブルにて一致するキーがあるかを検索する。そののちの動作は以下のようになる。
### ①アクセス歴のない場合
新たなノードを作成し、ページの情報を記録する。そのうえにheadをこの新ノードに向け、nextを現headとして記録する。同様に現headのpreviousを新ノードへと変更する。その後tailのノードを取り出し、同一のキー(URL)を持つをハッシュで検索して削除、新ノードを追加。
### ②アクセス歴のある場合 / 再訪問
ハッシュ内の該当するノードに対し、`node.previous.next = node.next`、`node.next.previous = node.previous`として前後をリンクさせる。それから該当ノードがheadとなるように、①と同じようにしてpreviousとnextを変更していく。(既にheadである場合は変更なし)

---

---

## <Challenge 1: Hash Table>
By multiplying by "count" that records indices of the characters in the key, an attempt was made to avoid anagrams having the same hash value for each other.
> ex) alice -> org(‘a’)*1 + org(‘l’)*2 + org(‘i’)*3 + org(‘c’)*4 + org(‘e’)*e5

I learnt that multiplying prime numbers could reduce executing time, however, However, attempts to multiply by prime numbers or any other method often resulted in execution times reaching 10 seconds. Combining these attempts with bit-shifting, however, significantly reduced the execution time.\
For any of the proposed methods, no clear solution has been found for addressing the variations observed across multiple test cases, nor a solid explanation as to why bit-shifting is more effective than using prime numbers.\
The table below records the execution times (in seconds) from the above calculations, focusing on cases that took a significant amount of time, including the 57th case, which had the longest execution time.

 Hash values \ testcases        | 1      | 40    | 57     | 99   
 ------------------------------ | ------ | ----- | ------ | ----- 
 hash += ord(i) * count         | 0.0826 | 4.764 | 13.213 | 12.672
 hash += ord(i) << idx          | 0.0462 | 0.447 | 3.474  | 1.268
 hash += ord(i) * count << idx  | 0.0534 | 0.090 | 2.820  | 0.184
 hash += ord(i) *10^(count-1)   | 0.0380 | 0.026 | 1.432  | 0.030
 


 ## <Challenge 2：Hash table vs Tree structure>
 #### Reasons why tree structure are preffered...
 ・Hash structures consume a large amount of memory.\
&emsp;&emsp; Since a certain table size is allocated to reduce collisions, additional memory is inevitably used.\
・Tree structures make it easier to handle related data.\
&emsp;&emsp; While hash tables excel at searches and similar operations, they store data randomly, disregarding the relationships between individual pieces of data. When preserving data relationships is important, tree structures are more suitable.\
・Execution times are unstable.\
&emsp;&emsp; Even with optimized hash functions, the potential for frequent collisions cannot be entirely eliminated.

## <Challenge 3：Cache>
Create a cache by combining a doubly linked list and a hash table, with the most recently visited site placed at the head and the least recently visited site at the tail.
```
Node(){
url = https://...
page = ...
previous = ...
next = ...
}
```
Create nodes with the above elements to record visited sites and form a doubly linked list. This setup preserves the order of visits while enabling the following operations:

&emsp;・If a site is not found in the cache, it is added as a new node at the head of the list.\
&emsp;・The record of the site at the tail can be removed when necessary.\

Additionally, maintain references to the head and tail nodes for efficient updates. By also storing each node in a hash table, the presence of a record in the cache can be checked in O(1) time.\
Use the site's URL as the key to search for a match in the hash table. The subsequent operations are as follows:
### ①When there is no access history
・Create a new node and record the page's information.\
・Set the head pointer to this new node, and update the next pointer of the new node to the current head.\
・Similarly, update the previous pointer of the current head to the new node.\
・If the cache is full, remove the tail node and use the hash table to search for and delete the entry with the same key (URL).\
・Add the new node to the hash table.
### ②When there is access history / Revisit
・Locate the corresponding node in the hash table.\
・Update the links:\
&emsp; `node.previous.next = node.next`\
&emsp; `node.next.previous = node.previous`\
&emsp;  This re-links the nodes around the accessed node.\
・Move the accessed node to the head position using the same process as in ①:\
&emsp; Set the previous and next pointers of the node to reflect its new position as the head.\
・If the node is already the head, no changes are made.

