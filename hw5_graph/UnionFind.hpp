#ifndef _UNION_FIND_HPP_
#define _UNION_FIND_HPP_

// Optional implementation of Union-Find
// Submit blank if you don't implement a
// Union-Find class.

#include <vector>
using namespace std;
class UnionFind {
  int size;
  int *parentArray;
  int *rankingArray;

public:
  // UnionFind(std::vector<int> *vertices);
  UnionFind(int s);

  int find(int u);
  void merge_sets(int x, int y);
};

UnionFind::UnionFind(int s) {
  rankingArray = new int[s];
  parentArray = new int[s];
  for (int i = 0; i < s; i++) {
    rankingArray[i] = 0;
    parentArray[i] = i;
  }

  // parentArray = vertices;
}

// Find the parent of a node 'u'
// Path Compression
int UnionFind::find(int u) {
  /* Make the parent of the nodes in the path
     from u--> parent[u] point to parent[u] */
  // if (u != (*parentArray).at(u))
  //  (*parentArray).at(u) = find((*parentArray).at(u));
  // return (*parentArray).at(u);
  if (u != parentArray[u])
    parentArray[u] = find(parentArray[u]);
  return parentArray[u];
}

// Union by rank
void UnionFind::merge_sets(int x, int y) {
  x = find(x), y = find(y);

  /* Make tree with smaller height
     a subtree of the other tree  */
  if (rankingArray[x] > rankingArray[y])
    parentArray[y] = x;
  else // If rnk[x] <= rnk[y]
    parentArray[x] = y;

  if (rankingArray[x] == rankingArray[y])
    rankingArray[y]++;
}

#endif
