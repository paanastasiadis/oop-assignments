#ifndef _UNION_FIND_HPP_
#define _UNION_FIND_HPP_

// Optional implementation of Union-Find
// Submit blank if you don't implement a
// Union-Find class.
class UnionFind {
  int *parentArray;
  int *rankingArray;

public:
  UnionFind(int numOfVertices);

  int findParent(int nodeIndex);
  void merge_sets(int node1, int node2);
};

UnionFind::UnionFind(int numOfVertices) {
  rankingArray = new int[numOfVertices];
  parentArray = new int[numOfVertices];
  for (int i = 0; i < numOfVertices; i++) {
    rankingArray[i] = 0;
    parentArray[i] = i;
  }
}

int UnionFind::findParent(int nodeIndex) {

  if (nodeIndex != parentArray[nodeIndex]) {
    parentArray[nodeIndex] = findParent(parentArray[nodeIndex]);
  }
  return parentArray[nodeIndex];
}

void UnionFind::merge_sets(int node1, int node2) {
  int setOfNode1 = findParent(node1);
  int setOfNode2 = findParent(node2);

  if (rankingArray[setOfNode1] > rankingArray[setOfNode2]) {
    parentArray[setOfNode2] = setOfNode1;
  } else {
    parentArray[setOfNode1] = setOfNode2;
  }
  if (rankingArray[setOfNode1] == rankingArray[setOfNode2]) {
    rankingArray[setOfNode2]++;
  }
}

#endif
