## Travelling Salesman Problem
> 貪欲法を利用して最短距離を求めるコードを書こうとしましたが、現時点では実行できない状態にあります。（Nodeクラスを使用しないコードに書き換えている最中なので、似た機能のメソッドが複数ある状態です。）またコード自体もまともな状態でないため、ここには試みと今後完成させたいシステムについてを記載します。
### Nodeクラスを使用した貪欲法
```Methods: greedyAlgoSearch(), removeIntersection(), fixPath(), doIntersect(), onSegment(), orientation()```

現ノードの番号(町の番号)、次のノードの番号、次のノード、次のノードまでの距離の4つの情報を保持するNodeクラスを作り、連結リストの役割を持たせようと試みました。これにより、2-opt実行後のルートの順序の並び替えを可能にしようとしましたが、並び替えにはひとつ前のノード情報も保持しなければならないため修正が必要です。また、次のノードまでの距離(エッジ)の情報は必要ないと教えていただいた為削除予定。\
1. 貪欲法により、greedyAlgoSearch()にてノード0から延びる最短距離を選択して、次のノードから延びる最短距離をまた選択していき、ノード0に戻ったところで終了する。
2. 現在のパスが交点を持つかどうかをremoveIntersection()で確認。存在すればfixPath()でパスの繋ぎなおしを行い、より最適解に近づけるように試みている。

### distance[][]を使用した貪欲法
```Methods: firstShortestPath(), modifyPath()```

各ノードから到達し得るノードと距離を記録する二次元配列(距離リスト)と、Nodeクラスの連結リストの代わりとなる一次元の配列を利用した貪欲法。\
先ほど複数のメソッドを介して交点の有無を計算していたところを、二つのエッジを繋ぎなおした際の距離と現在の距離をリストから計算し、短くなるようだったらその繋ぎなおしを採用するようにしている。\
ここまではいいけれど、繋ぎなおしによる順序の変更方法がまだ

---
---

## Travelling Salesman Problem
> I tried to write code to find the shortest distance using a greedy algorithm, but it is currently in a state where it cannot be executed.（It is in the process of being rewrited ti the code without using the Node class, so there are multiple methods with similar functionality.）Since the code itself is not in a proper state, I will describe my attempts here and the system I aim to complete in the future.
### Greedy algorithm with Node class
```Methods: greedyAlgoSearch(), removeIntersection(), fixPath(), doIntersect(), onSegment(), orientation()```

&ensp; I tried to create a Node class that holds four pieces of information: the current node number (town number), the next node number, the next node, and the distance to the next node, and attempted to give it the role of a linked list. With this, I tried to make it possible to reorder the route after running 2-opt, but since it also need to keep the information of the previous node for the reordering, a modification is necessary.\
&ensp; Also, since I was told that the distance to the next node (edge) is not required, I plan to delete it.
1. Using the greedy algorithm, in `greedyAlgoSearch()`, it selects the shortest distance extending from node 0, then select the shortest distance from the next node, and continue until returning to node 0, at which point the process ends.
2. It checks whether the current path has intersections with `removeIntersection()`. If intersections exist, it attempts to re-connect the path using `fixPath()` to move closer to an optimal solution.

### Greedy algorithm with distance[][]
```Methods: firstShortestPath(), modifyPath()```

&ensp; A greedy algorithm using a 2D array (distance list) to record the reachable nodes and their distances from each node, and a 1D array as a replacement for the linked list of the Node class.\
&ensp; Where it was previously calculating the presence of intersections through multiple methods, now it calculates the distance when reconnecting two edges and compare it with the current distance from the list. If the new distance is shorter, it would adopt the reconnected path.
(So far, this is fine, but the method for changing the order due to the reconnection is still missing.)
