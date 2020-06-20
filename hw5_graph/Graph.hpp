
#ifndef _GRAPH_HPP_
#define _GRAPH_HPP_

#include "NegativeGraphCycle.hpp"
#include "UnionFind.hpp"
#include <cstdint>
#include <fstream>
#include <iostream>
#include <list>
#include <map>
#include <ostream>
#include <queue>
#include <set>
#include <vector>
using namespace std;

template <typename T> class Vertex {

public:
  T info;
  int priority;
  bool visited;

  int tempIdx;

  template <typename P>
  friend bool operator<(const pair<Vertex<P> *, int> &pair1,
                        const pair<Vertex<P> *, int> &pair2) {
    return pair1.first->priority <= pair2.first->priority;
  }

  set<pair<Vertex<T> *, int>, less<pair<Vertex<T> *, int>>> adjList;
  Vertex(T nodeInfo, int pr);
};

template <typename T> struct Edge {
  T from;
  T to;
  int dist;

  pair<Vertex<T> *, Vertex<T> *> address;

  Edge(T f, T t, int d, pair<Vertex<T> *, Vertex<T> *> addr)
      : from(f), to(t), dist(d), address(addr) {}
  bool operator<(const Edge<T> &e) const;
  bool operator>(const Edge<T> &e) const;
  template <typename U>
  friend std::ostream &operator<<(std::ostream &out, const Edge<U> &e);
};

template <typename T>
std::ostream &operator<<(std::ostream &out, const Edge<T> &e) {
  out << e.from << " -- " << e.to << " (" << e.dist << ")";
  return out;
}

template <typename T> class Graph {

  // fields

  vector<Vertex<T> *> *vtxArray;
  int totalVertices;
  int insertionPriority;
  bool isDirected;

  int mstCost;

  void expand_table();
  void shrink_table();

public:
  Graph(bool isDirectedGraph = true, int capacity = 2);
  bool contains(const T &info);
  bool addVtx(const T &info);
  bool rmvVtx(const T &info);
  bool addEdg(const T &from, const T &to, int distance);
  bool rmvEdg(const T &from, const T &to);
  list<T> dfs(const T &info) const;

  void dfsRecur(list<T> *dfsList, Vertex<T> *vtx) const;
  list<T> bfs(const T &info) const;
  list<Edge<T>> mst();

  int getMSTCost();

  void print2DotFile(const char *filename) const;
  list<T> dijkstra(const T &from, const T &to);
  list<T> bellman_ford(const T &from, const T &to);
};

template <typename T> bool Edge<T>::operator<(const Edge<T> &e) const {
  return (this->dist < e.dist);
}
template <typename T> bool Edge<T>::operator>(const Edge<T> &e) const {
  return (this->dist > e.dist);
}

template <typename T> Vertex<T>::Vertex(T nodeInfo, int prio) {
  this->info = nodeInfo;
  this->priority = prio;
  this->visited = false;
  this->tempIdx = -1;
}
template <typename T> Graph<T>::Graph(bool isDirectedGraph, int capacity) {
  this->isDirected = isDirectedGraph;
  this->totalVertices = 0;
  this->insertionPriority = 0;
  this->mstCost = 0;
  this->vtxArray = new vector<Vertex<T> *>;
}

template <typename T> bool Graph<T>::contains(const T &info) {
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    if ((*it)->info == info) {
      return true;
    }
  }
  return false;
}
template <typename T> bool Graph<T>::addVtx(const T &info) {

  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if ((*it)->info == info) {
      return false;
    }
  }
  this->totalVertices++;
  this->insertionPriority++;
  Vertex<T> *vtx = new Vertex<T>(info, this->insertionPriority);
  vtxArray->push_back(vtx);
  return true;
}

template <typename T> bool Graph<T>::rmvVtx(const T &info) {
  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if ((*it)->info == info) {
      this->totalVertices--;
      Vertex<T> *vtx = *it;
      vtxArray->erase(it);

      for (auto it2 = vtxArray->begin(); it2 != vtxArray->end(); it2++) {
        for (auto it3 = (*it2)->adjList.begin(); it3 != (*it2)->adjList.end();
             it3++) {
          if (it3->first->info == vtx->info) {
            (*it2)->adjList.erase(it3);
            break;
          }
        }
      }
      delete vtx;
      return true;
    }
  }
  return false;
}
template <typename T>
bool Graph<T>::addEdg(const T &from, const T &to, int distance) {

  int nodesFound = 0;
  Vertex<T> *edge_src;
  Vertex<T> *edge_dst;
  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if (nodesFound == 2) {
      break;
    }
    if ((*it)->info == from) {
      edge_src = *it;
      nodesFound++;
    }
    if ((*it)->info == to) {
      edge_dst = *it;
      nodesFound++;
    }
  }
  if (nodesFound < 2) {
    return false;
  }

  for (auto it = edge_src->adjList.begin(); it != edge_src->adjList.end();
       it++) {
    if ((*it).first->info == edge_dst->info) {
      return false;
    }
  }

  if (this->isDirected == false) {
    for (auto it = edge_dst->adjList.begin(); it != edge_dst->adjList.end();
         it++) {
      if ((*it).first->info == edge_src->info) {
        return false;
      }
    }
  }

  pair<Vertex<T> *, int> edgeToAdd(edge_dst, distance);

  if (this->isDirected == false) {
    edge_src->adjList.insert(edgeToAdd);

    pair<Vertex<T> *, int> edgeToAdd2(edge_src, distance);
    edge_dst->adjList.insert(edgeToAdd2);
  } else {
    edge_src->adjList.insert(edgeToAdd);
  }

  return true;
}
template <typename T> bool Graph<T>::rmvEdg(const T &from, const T &to) {
  int nodesFound = 0;
  Vertex<T> *edge_src;
  Vertex<T> *edge_dst;
  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if (nodesFound == 2) {
      break;
    }
    if ((*it)->info == from) {
      edge_src = *it;
      nodesFound++;
    }
    if ((*it)->info == to) {
      edge_dst = *it;
      nodesFound++;
    }
  }

  if (nodesFound < 2) {
    return false;
  }

  for (auto it = edge_src->adjList.begin(); it != edge_src->adjList.end();
       it++) {
    if (it->first->info == edge_dst->info) {
      edge_src->adjList.erase(it);
      break;
    }
  }

  if (this->isDirected == false) {
    for (auto it = edge_dst->adjList.begin(); it != edge_dst->adjList.end();
         it++) {
      if (it->first->info == edge_src->info) {
        edge_dst->adjList.erase(it);
        break;
      }
    }
  }

  return true;
}
template <typename T> list<T> Graph<T>::dfs(const T &info) const {
  Vertex<T> *vtx;
  list<T> *dfsList = new list<T>;
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    if ((*it)->info == info) {
      (*it)->visited = true;
      vtx = (*it);
      break;
    }
  }

  dfsRecur(dfsList, vtx);

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {

    (*it)->visited = false;
  }

  return *dfsList;
}

template <typename T>
void Graph<T>::dfsRecur(list<T> *dfsList, Vertex<T> *vtx) const {

  vtx->visited = true;
  dfsList->push_back(vtx->info);

  for (auto it = vtx->adjList.begin(); it != vtx->adjList.end(); it++) {
    if (it->first->visited == false) {
      dfsRecur(dfsList, it->first);
    }
  }
}

template <typename T> list<T> Graph<T>::bfs(const T &info) const {
  Vertex<T> *vtx;
  list<T> bfsResultList;

  queue<Vertex<T> *> pq;

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    if ((*it)->info == info) {
      pq.push(*it);
      (*it)->visited = true;
      vtx = (*it);
      break;
    }
  }

  while (!pq.empty()) {
    vtx = pq.front();
    pq.pop();
    bfsResultList.push_back(vtx->info);
    for (auto it2 = vtx->adjList.begin(); it2 != vtx->adjList.end(); it2++) {
      if (it2->first->visited == false) {
        it2->first->visited = true;

        pq.push(it2->first);
      }
    }
  }

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    (*it)->visited = false;
  }

  return bfsResultList;
}
template <typename T> list<Edge<T>> Graph<T>::mst() {

  if (this->isDirected == true) {
    return {};
  }
  list<Edge<T>> edgeList;

  int i = 0;
  bool visited[this->totalVertices];
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    (*it)->tempIdx = i;
    visited[i] = false;
    i++;
    for (auto it2 = (*it)->adjList.begin(); it2 != (*it)->adjList.end();
         it2++) {

      if ((*it) < it2->first) {
        pair<Vertex<T> *, Vertex<T> *> addr((*it), it2->first);
        Edge<T> e((*it)->info, it2->first->info, it2->second, addr);
        edgeList.push_back(e);
      }
    }
  }
  edgeList.sort();

  UnionFind uf(this->totalVertices);

  i = 0;
  list<Edge<T>> resList;

  for (auto it = edgeList.begin(); it != edgeList.end(); it++) {
    int v_from = it->address.first->tempIdx;
    int v_to = it->address.second->tempIdx;

    if (i == this->totalVertices) {
      break;
    }

    if (visited[v_from] == false) {
      visited[v_from] = true;
      i++;
    }

    if (visited[v_to] == false) {
      visited[v_to] = true;
      i++;
    }

    int from_set = uf.find(v_from);
    int to_set = uf.find(v_to);

    if (from_set != to_set) {
      resList.push_back(*it);
      this->mstCost += (*it).dist;
      uf.merge_sets(from_set, to_set);
    }
  }
  return resList;
}

template <typename T> int Graph<T>::getMSTCost() { return this->mstCost; }

template <typename T> void Graph<T>::print2DotFile(const char *filename) const {
  ofstream fout(filename);
  if (this->isDirected == true) {
    fout << "digraph Trie {" << endl;
  } else {
    fout << "graph Trie {" << endl;
  }

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    fout << (*it)->info;
    fout << " [label = \"";
    fout << (*it)->info;
    fout << "\", shape=egg, ";
    fout << "color=red, style=filled]" << endl;
    for (auto it2 = (*it)->adjList.begin(); it2 != (*it)->adjList.end();
         it2++) {
      if (this->isDirected == true ||
          (this->isDirected == false && (*it) < it2->first)) {
        fout << (*it)->info;
        if (this->isDirected == true) {
          fout << " -> ";
        } else {
          fout << " -- ";
        }
        fout << it2->first->info << endl;

        fout << " [label = \"";
        fout << it2->second;
        fout << "\",]";
      }
    }
  }
  fout << "}" << endl;
}

template <typename T> list<T> Graph<T>::dijkstra(const T &from, const T &to) {

  int i = 0;

  int distance[this->totalVertices];
  Vertex<T> *previous[this->totalVertices];
  Vertex<T> *srcVtx;
  Vertex<T> *dstVtx;

  priority_queue<pair<int, Vertex<T> *>, vector<pair<int, Vertex<T> *>>,
                 greater<pair<int, Vertex<T> *>>>
      pq;

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    (*it)->tempIdx = i;
    previous[i] = NULL;

    if (from == (*it)->info) {
      distance[i] = 0;
      srcVtx = *it;
      pq.push(make_pair(0, *it));
    } else {
      distance[i] = INT32_MAX;
    }

    if (to == (*it)->info) {
      dstVtx = (*it);
    }
    i++;
  }

  while (!pq.empty()) {
    Vertex<T> *currSrcVtx = pq.top().second;
    pq.pop();

    for (auto it2 = currSrcVtx->adjList.begin();
         it2 != currSrcVtx->adjList.end(); it2++) {
      int weight = it2->second;
      if (weight < 0) {
        weight = 0;
      }
      Vertex<T> *currDstVtx = it2->first;

      if (distance[currSrcVtx->tempIdx] + weight <
          distance[currDstVtx->tempIdx]) {
        distance[currDstVtx->tempIdx] = distance[currSrcVtx->tempIdx] + weight;
        previous[currDstVtx->tempIdx] = currSrcVtx;

        pq.push(make_pair(distance[currDstVtx->tempIdx], currDstVtx));
      }
    }
  }

  int j = dstVtx->tempIdx;

  if (previous[j] == NULL) {
    return {};
  }
  list<T> resList;

  while (previous[j]->info != from) {
    resList.push_front(previous[j]->info);
    j = previous[j]->tempIdx;

    if (previous[j] == NULL) {
      return {};
    }
  }

  resList.push_back(dstVtx->info);
  resList.push_front(srcVtx->info);
  return resList;
}

template <typename T>
list<T> Graph<T>::bellman_ford(const T &from, const T &to) {

  list<T> resList;

  int i = 0;
  list<Edge<T>> edgeList;
  Vertex<T> *srcVtx;
  Vertex<T> *dstVtx;

  int distance[this->totalVertices];
  Vertex<T> *previous[this->totalVertices];

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    (*it)->tempIdx = i;

    previous[i] = NULL;
    if (from == (*it)->info) {
      distance[i] = 0;
      srcVtx = *it;
    } else {
      distance[i] = INT32_MAX;
    }

    if (to == (*it)->info) {
      dstVtx = (*it);
    }

    i++;
    for (auto it2 = (*it)->adjList.begin(); it2 != (*it)->adjList.end();
         it2++) {

      // if (this->isDirected == true ||
      //     (this->isDirected == false && (*it) < it2->first)) {

        pair<Vertex<T> *, Vertex<T> *> addr((*it), it2->first);
        Edge<T> e((*it)->info, it2->first->info, it2->second, addr);
        edgeList.push_back(e);
    }
  }

  for (int i = 1; i < this->totalVertices; i++) {
    for (auto it = edgeList.begin(); it != edgeList.end(); it++) {
      int v_from = it->address.first->tempIdx;
      int v_to = it->address.second->tempIdx;
      if (distance[v_from] != INT32_MAX &&
          ((distance[v_from] + it->dist) < distance[v_to])) {
        distance[v_to] = distance[v_from] + it->dist;
        previous[v_to] = it->address.first;
      }
    }
  }

  NegativeGraphCycle ex;
  for (auto it = edgeList.begin(); it != edgeList.end(); it++) {
    int v_from = it->address.first->tempIdx;
    int v_to = it->address.second->tempIdx;
    if (distance[v_from] != INT32_MAX &&
        ((distance[v_from] + it->dist) < distance[v_to])) {
      throw ex;
    }
  }

  int j = dstVtx->tempIdx;

  if (previous[j] == NULL) {
    return {};
  }

  while (previous[j]->info != from) {
    resList.push_front(previous[j]->info);
    j = previous[j]->tempIdx;

    if (previous[j] == NULL) {
      return {};
    }
  }

  resList.push_back(dstVtx->info);
  resList.push_front(srcVtx->info);

  return resList;
}

#endif
