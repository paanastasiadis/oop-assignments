
#ifndef _GRAPH_HPP_
#define _GRAPH_HPP_

#include <fstream>
#include <list>
#include <map>
#include <ostream>
#include <set>
#include <vector>
using namespace std;

template <typename T> struct Edge {
  T from;
  T to;
  int dist;
  Edge(T f, T t, int d) : from(f), to(t), dist(d) {}
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

  template <typename U> class Vertex {

  public:
    U info;
    vector<pair<Vertex<U> *, int>> adjList;
    Vertex(U nodeInfo);
  };

  vector<Vertex<T> *> *vertexSet;
  int totalVertices;
  bool isDirected;

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
  list<T> bfs(const T &info) const;
  list<Edge<T>> mst();

  void print2DotFile(const char *filename) const;
  list<T> dijkstra(const T &from, const T &to);
  list<T> bellman_ford(const T &from, const T &to);
};

// template <typename T>
// bool Edge<T>::operator<(const Edge<T> &e) const {}
// template <typename T>
// bool Edge<T>::operator>(const Edge<T> &e) const {}

template <typename T>
template <typename U>
Graph<T>::Vertex<U>::Vertex(U nodeInfo) {
  this->info = nodeInfo;
}

template <typename T> Graph<T>::Graph(bool isDirectedGraph, int capacity) {
  this->isDirected = isDirectedGraph;
  this->totalVertices = 0;
  this->vertexSet = new vector<Vertex<T> *>;
}

template <typename T> bool Graph<T>::addVtx(const T &info) {

  for (typename vector<Vertex<T> *>::iterator it = vertexSet->begin();
       it != vertexSet->end(); it++) {
    if ((*it)->info == info) {
      return false;
    }
  }
  this->totalVertices++;
  Vertex<T> *vtx = new Vertex<T>(info);
  vertexSet->push_back(vtx);
  return true;
}

template <typename T> bool Graph<T>::rmvVtx(const T &info) {
  for (typename vector<Vertex<T> *>::iterator it = vertexSet->begin();
       it != vertexSet->end(); it++) {
    if ((*it)->info == info) {
      this->totalVertices--;
      Vertex<T> *vtx = *it;
      vertexSet->erase(it);
      // TODO Erase also the associated edges
      for (auto it2 = vertexSet->begin(); it2 != vertexSet->adjList->end();
           it2++) {
        for (auto it3 = it2->adjList->begin(); it3 != it2->adjList->end();
             it3++) {
          if ((*it3)->first->info == vtx->info) {
            it2->adjList->erase(it3);
          }
        }
      }
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
  for (typename vector<Vertex<T> *>::iterator it = vertexSet->begin();
       it != vertexSet->end(); it++) {
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

  for (auto it = edge_dst->adjList.begin(); it != edge_dst->adjList.end();
       it++) {
    if ((*it).first->info == edge_src->info) {
      return false;
    }
  }

  pair<Vertex<T> *, int> edgeToAdd(edge_dst, distance);

  if (this->isDirected == false) {
    edge_src->adjList.push_back(edgeToAdd);

    pair<Vertex<T> *, int> edgeToAdd2(edge_src, distance);
    edge_dst->adjList.push_back(edgeToAdd2);
  } else {
    edge_src->adjList.push_back(edgeToAdd);
  }

  return true;
}
template <typename T> bool Graph<T>::rmvEdg(const T &from, const T &to) {
  int nodesFound = 0;
  Vertex<T> *edge_src;
  Vertex<T> *edge_dst;
  for (typename vector<Vertex<T> *>::iterator it = vertexSet->begin();
       it != vertexSet->end(); it++) {
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

  for (auto it = edge_src->adjList->begin(); it != edge_src->adjList->end();
       it++) {
    if ((*it)->first->info == edge_dst->info) {
      edge_src->adjList->erase(it);
    }
  }

  for (auto it = edge_dst->adjList->begin(); it != edge_dst->adjList->end();
       it++) {
    if ((*it)->first->info == edge_src->info) {
      edge_dst->adjList->erase(it);
    }
  }

  return true;
}
template <typename T> list<T> Graph<T>::dfs(const T &info) const {}
template <typename T> list<T> Graph<T>::bfs(const T &info) const {}
template <typename T> list<Edge<T>> Graph<T>::mst() {}

template <typename T> void Graph<T>::print2DotFile(const char *filename) const {
  ofstream fout(filename);
  fout << "digraph Trie {" << endl;

  for (auto it = vertexSet->begin(); it != vertexSet->end(); it++) {
    fout << (*it)->info;
    fout << " [label = \"";
    fout << (*it)->info;
    fout << "\", shape=egg, ";
    fout << "color=red, style=filled]" << endl;
    for (auto it2 = (*it)->adjList.begin(); it2 != (*it)->adjList.end();
         it2++) {

      fout << (*it)->info;
      fout << " -> ";
      fout << it2->first->info << endl;

      fout << " [label = \"";
      fout << it2->second;
      fout << "\",]";
    }
  }

  fout << "}" << endl;
}

template <typename T> list<T> Graph<T>::dijkstra(const T &from, const T &to) {}
template <typename T>
list<T> Graph<T>::bellman_ford(const T &from, const T &to) {}

#endif
