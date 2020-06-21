
#ifndef _GRAPH_HPP_
#define _GRAPH_HPP_

#include "NegativeGraphCycle.hpp"
#include "UnionFind.hpp"
#include <fstream>
#include <list>
#include <queue>
#include <set>
#include <vector>
using namespace std;

template <typename T> class Vertex {

public:
  T info;       // stored object of vertex
  int priority; // priority among the vertexes(insertion-in-graph order)

  int tempIdx; // temporary index in the vertex array of the graph.
               // NOT INITIALLY ASSIGNED, NOT A VALID VALUE AFTER A VERTEX
               // DELETION. MUST BE CORRECTLY ASSIGNED BEFORE EVERY USE.

  template <typename P>

  /**
   * @brief operator < between two elements of pair(vertex, weight)
   */
  friend bool operator<(const pair<Vertex<P> *, int> &pair1,
                        const pair<Vertex<P> *, int> &pair2) {
    return pair1.first->priority <= pair2.first->priority;
  }

  // adjacency list of vertex. A set of dest vertexes or edges sorted in
  // time-of-insertion order
  set<pair<Vertex<T> *, int>, less<pair<Vertex<T> *, int>>> adjList;

  Vertex(T nodeInfo, int insPriority);
};

template <typename T> struct Edge {
  T from;
  T to;
  int dist;

  // pair of pointers of the src and dst vertexes
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

  vector<Vertex<T> *> *vtxArray;
  int totalVertices;
  int insertionPriority;
  bool isDirected;

  int mstCost;

  void expand_table();
  void shrink_table();

public:
  Graph(bool isDirectedGraph = true, int capacity = 2);

  ~Graph();
  bool contains(const T &info);
  bool addVtx(const T &info);
  bool rmvVtx(const T &info);
  bool addEdg(const T &from, const T &to, int distance);
  bool rmvEdg(const T &from, const T &to);
  list<T> dfs(const T &info) const;

  void recursiveDfs(list<T> *dfsList, Vertex<T> *vtx, bool *visitedArray) const;
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

/**
 * @brief Construct a new Vertex< T>:: Vertex object
 */
template <typename T> Vertex<T>::Vertex(T nodeInfo, int insPriority) {
  this->info = nodeInfo;
  this->priority = insPriority;
  this->tempIdx = -1;
}
/**
 * @brief Construct a new Graph< T>:: Graph object
 */
template <typename T> Graph<T>::Graph(bool isDirectedGraph, int capacity) {
  this->isDirected = isDirectedGraph;
  this->totalVertices = 0;
  this->insertionPriority = 0;
  this->mstCost = 0;
  this->vtxArray = new vector<Vertex<T> *>;
}

/**
 * @brief Destroy the Graph< T>:: Graph object
 */
template <typename T> Graph<T>::~Graph() {
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    delete (*it);
  }
  delete this->vtxArray;
}

/**
 * @brief Check if there is a node in graph with the specified info
 */
template <typename T> bool Graph<T>::contains(const T &info) {
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    if ((*it)->info == info) {
      return true; // found the node
    }
  }
  return false; // no node was found with the specified info
}

/**
 * @brief Add a new vertex to Graph if it does not already exist
 */
template <typename T> bool Graph<T>::addVtx(const T &info) {

  if (this->contains(info) == true) {
    return false; // there is already a vertex with this info
  }

  // Update graph information
  this->totalVertices++; // update graph size
  // increase the priority ticket. Lower value -> bigger priority
  this->insertionPriority++;

  // Add the new vertex
  Vertex<T> *vtx = new Vertex<T>(info, this->insertionPriority);
  vtxArray->push_back(vtx);

  return true;
}

/**
 * @brief Remove a vertex from Graph if it exists
 */
template <typename T> bool Graph<T>::rmvVtx(const T &info) {
  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if ((*it)->info == info) { // found the vertex
      this->totalVertices--;   // update graph size

      Vertex<T> *vtx = *it;
      vtxArray->erase(it); // remove the vertex from the vertex array

      // Delete any edges containing that vertex
      for (auto it2 = vtxArray->begin(); it2 != vtxArray->end();
           it2++) { // for each vertex it2
        for (auto it3 = (*it2)->adjList.begin(); it3 != (*it2)->adjList.end();
             it3++) { // for each edge it3 of vertex it2
          if (it3->first->info ==
              vtx->info) { // found an edge containing the deleted vertex
            (*it2)->adjList.erase(it3); // erase the edge
            break;
          }
        }
      }
      delete vtx; // free the memory of the deleted vertex
      return true;
    }
  }
  return false;
}

/**
 * @brief Add a new edge to Graph
 */
template <typename T>
bool Graph<T>::addEdg(const T &from, const T &to, int distance) {

  int nodesFound = 0;
  Vertex<T> *edge_src;
  Vertex<T> *edge_dst;

  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if (nodesFound == 2) { // found the vertexes. The edge can be constructed.
      break;
    }
    if ((*it)->info == from) { // found the 'from' or 'src' vertex in Graph
      edge_src = *it;
      nodesFound++;
    }
    if ((*it)->info == to) { // found the 'to' or 'dst' vertex in Graph
      edge_dst = *it;
      nodesFound++;
    }
  }

  if (nodesFound < 2) { // Vertexes not found. Edge cannot be constructed
    return false;
  }

  // check if there is already the same edge in Graph
  for (auto it = edge_src->adjList.begin(); it != edge_src->adjList.end();
       it++) {
    if ((*it).first->info == edge_dst->info) {
      return false;
    }
  }

  // Create a new edge => pair (destination vertex, weight)
  pair<Vertex<T> *, int> edgeToAdd(edge_dst, distance);

  if (this->isDirected == false) {
    edge_src->adjList.insert(
        edgeToAdd); // add in the adjacency list of the 'src' vertex

    pair<Vertex<T> *, int> edgeToAdd2(edge_src, distance);
    edge_dst->adjList.insert(edgeToAdd2);

  } else { // if the graph is not directed, add the edge in the adjacency
           // list of the 'dst' vertex, too.
    edge_src->adjList.insert(edgeToAdd);
  }

  return true;
}

/**
 * @brief Remove an edge from the Graph
 *
 */
template <typename T> bool Graph<T>::rmvEdg(const T &from, const T &to) {
  int nodesFound = 0;
  Vertex<T> *edge_src;
  Vertex<T> *edge_dst;

  for (typename vector<Vertex<T> *>::iterator it = vtxArray->begin();
       it != vtxArray->end(); it++) {
    if (nodesFound == 2) { // found the vertexes. The edge can be removed.
      break;
    }
    if ((*it)->info == from) { // found the 'from' or 'src' vertex in Graph
      edge_src = *it;
      nodesFound++;
    }
    if ((*it)->info == to) { // found the 'to' or 'dst' vertex in Graph
      edge_dst = *it;
      nodesFound++;
    }
  }

  if (nodesFound < 2) { // Vertexes not found so there is no such edge.
    return false;
  }

  for (auto it = edge_src->adjList.begin(); it != edge_src->adjList.end();
       it++) {
    if (it->first->info == edge_dst->info) {
      // erase the edge from the adjacency list of Vertex 'from'
      edge_src->adjList.erase(it);
      break;
    }
  }

  if (this->isDirected == false) {
    for (auto it = edge_dst->adjList.begin(); it != edge_dst->adjList.end();
         it++) {
      if (it->first->info == edge_src->info) {
        // Graph NOT directed. Also erase the edge
        // from the adjacency list of Vertex 'to'
        edge_dst->adjList.erase(it);
        break;
      }
    }
  }

  return true;
}
/**
 * @brief Depth First Search Algorithm.
 */
template <typename T> list<T> Graph<T>::dfs(const T &info) const {
  Vertex<T> *vtx = NULL;
  bool visited[this->totalVertices];

  int i = 0;
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    visited[i] = false;
    (*it)->tempIdx = i;
    if ((*it)->info == info) { // found the starting point vertex
      visited[i] = true;       // mark it as visited
      vtx = *it;
    }
    i++;
  }

  if (vtx == NULL) {
    return {}; // No vertex found with the specified info.
  }

  list<T> *dfsList = new list<T>;

  // recursive function to construct the DFS list of vertexes
  recursiveDfs(dfsList, vtx, visited);

  return *dfsList;
}

/**
 * @brief Helping recursive function for the DFS algorithm
 *
 * @tparam info Starting vertex
 * @param dfsList The list with all the vertexes in DFS order
 */
template <typename T>
void Graph<T>::recursiveDfs(list<T> *dfsList, Vertex<T> *vtx,
                            bool *visitedArray) const {

  visitedArray[vtx->tempIdx] = true;
  dfsList->push_back(vtx->info);

  // go to all the vertexes recursively by visiting all the adjacent edges
  for (auto it = vtx->adjList.begin(); it != vtx->adjList.end(); it++) {
    if (visitedArray[it->first->tempIdx] == false) {
      recursiveDfs(dfsList, it->first, visitedArray);
    }
  }
}

/**
 * @brief Breadth-First Search Algorithm.
 *
 * @param info Starting Vertex
 * @return list<T> The list with all the vertexes in BFS order
 */
template <typename T> list<T> Graph<T>::bfs(const T &info) const {

  Vertex<T> *vtx;
  queue<Vertex<T> *> pq;
  bool visited[this->totalVertices];

  int i = 0;
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    (*it)->tempIdx = i;
    visited[i] = false;
    if ((*it)->info == info) {
      pq.push(*it);
      visited[i] = true; // add the starting vertex to the queue
      vtx = (*it);
    }
    i++;
  }

  if (vtx == NULL) {
    return {}; // No vertex found with the specified info.
  }

  list<T> bfsResultList;

  // we use queue instead of priority queue because the adjacency list of
  // all vertexes are already ordered with the insertion priority
  while (!pq.empty()) {
    vtx = pq.front();
    pq.pop();
    bfsResultList.push_back(vtx->info);

    for (auto it2 = vtx->adjList.begin(); it2 != vtx->adjList.end(); it2++) {
      if (visited[it2->first->tempIdx] == false) {
        visited[it2->first->tempIdx] = true;

        pq.push(it2->first);
      }
    }
  }
  return bfsResultList;
}

/**
 * @brief Finds the minimum spanning tree of the Graph if it is undirected.
 * Also calculates the total cost of the MST.
 */
template <typename T> list<Edge<T>> Graph<T>::mst() {

  // mst only for non-directed graphs. Return an empty list.
  if (this->isDirected == true) {
    return {};
  }

  list<Edge<T>> edgeList;
  bool visited[this->totalVertices];

  int i = 0;
  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    // add a temporary index for each vertex in the array
    (*it)->tempIdx = i;
    visited[i] = false;
    i++;

    // construct a list of all the edges in graph
    for (auto it2 = (*it)->adjList.begin(); it2 != (*it)->adjList.end();
         it2++) {

      // because the Graph is undirected, avoid the duplicate edges by
      // adding only the edges where the src vertex was inserted before the
      // dst vertex in the Graph
      if ((*it)->priority < it2->first->priority) {
        pair<Vertex<T> *, Vertex<T> *> addr((*it), it2->first);
        Edge<T> e((*it)->info, it2->first->info, it2->second, addr);
        edgeList.push_back(e);
      }
    }
  }
  // sort all the edges from the smaller weight to the highest
  edgeList.sort();

  // Initialize a UnionFind object to prevent cycles in the final list
  UnionFind uf(this->totalVertices);

  i = 0;
  list<Edge<T>> resList;

  for (auto it = edgeList.begin(); it != edgeList.end(); it++) {

    int v_from = it->address.first->tempIdx; // edge src-vertex index
    int v_to = it->address.second->tempIdx;  // edge dst-vertex index

    // stop when all the vertices have been visited
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

    // find the sets that 'from' and 'to' vertexes belong
    int from_set = uf.findParent(v_from);
    int to_set = uf.findParent(v_to);

    if (from_set != to_set) {
      // find the set that from vertex belongs
      resList.push_back(*it);
      // update the total weight in the mst
      this->mstCost += (*it).dist;
      // merge the sets to avoid possible cycles in the future
      uf.merge_sets(from_set, to_set);
    }
  }
  return resList;
}

/**
 * @brief Get the MST total cost. Valid only when the mst() function was
 * previously called
 */
template <typename T> int Graph<T>::getMSTCost() { return this->mstCost; }

/**
 * @brief Print the Graph in dot-compatible form for the graphiz tool
 */
template <typename T> void Graph<T>::print2DotFile(const char *filename) const {
  ofstream fout(filename);
  if (this->isDirected == true) {
    fout << "digraph G {" << endl;
  } else {
    fout << "graph G {" << endl;
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

/**
 * @brief Dijkstra's Algorithm. Finds the shortest path from vertex 'from'
 * to vertex 'to'.
 */
template <typename T> list<T> Graph<T>::dijkstra(const T &from, const T &to) {

  int i = 0;

  int distance[this->totalVertices];
  // array of the parent vertexes of every vertex in the dijkstra path
  Vertex<T> *previous[this->totalVertices];
  Vertex<T> *srcVtx;
  Vertex<T> *dstVtx;

  priority_queue<pair<int, Vertex<T> *>, vector<pair<int, Vertex<T> *>>,
                 greater<pair<int, Vertex<T> *>>>
      pq;

  for (auto it = vtxArray->begin(); it != vtxArray->end(); it++) {
    (*it)->tempIdx = i; // temp index for every vertex in the array
    previous[i] = NULL;

    if (from == (*it)->info) { // starting point vertex
      distance[i] = 0;
      srcVtx = *it;
      // the source vertex, hence the first element in the queue
      pq.push(make_pair(0, *it));
    } else {
      // initialize the distance for all the other vertexes to infinity.
      distance[i] = INT32_MAX;
    }

    if (to == (*it)->info) { // end point vertex
      dstVtx = (*it);
    }
    i++;
  }

  // the specified 'from' and 'to' vertexes where not found
  if (srcVtx == NULL || dstVtx == NULL) {
    return {};
  }

  while (!pq.empty()) {
    Vertex<T> *currSrcVtx = pq.top().second;
    pq.pop();

    for (auto it2 = currSrcVtx->adjList.begin();
         it2 != currSrcVtx->adjList.end(); it2++) {
      int weight = it2->second;

      // if edge weight is negative consider it as zero due to dijkstra's
      // limitations
      if (weight < 0) {
        weight = 0;
      }
      Vertex<T> *currDstVtx = it2->first;

      // update both distance info and the parents in dijkstra path when a
      // path with lesser cost is found
      if (distance[currSrcVtx->tempIdx] + weight <
          distance[currDstVtx->tempIdx]) {
        distance[currDstVtx->tempIdx] = distance[currSrcVtx->tempIdx] + weight;
        previous[currDstVtx->tempIdx] = currSrcVtx;

        // throw it to the priority queue
        pq.push(make_pair(distance[currDstVtx->tempIdx], currDstVtx));
      }
    }
  }

  //-- Dijkstra algorithm is completed --

  int j = dstVtx->tempIdx;

  // check for a path from dst to src vertex in dijkstra tree

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

  // found the inner vertexes in the path. Now add src and dst vertexes
  resList.push_back(dstVtx->info);
  resList.push_front(srcVtx->info);
  return resList;
}

/**
 * @brief Bellman-Ford's Algorithm. Finds the shortest path from vertex
 * 'from' to vertex 'to'.
 */
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
    (*it)->tempIdx = i; // temp index for every vertex in the array

    previous[i] = NULL;
    if (from == (*it)->info) { // starting point vertex
      distance[i] = 0;
      srcVtx = *it;
    } else {
      // initialize the distance of all the other vertexes to infinity.

      distance[i] = INT32_MAX;
    }

    if (to == (*it)->info) { // end point vertex
      dstVtx = (*it);
    }

    i++;
    for (auto it2 = (*it)->adjList.begin(); it2 != (*it)->adjList.end();
         it2++) {

      pair<Vertex<T> *, Vertex<T> *> addr((*it), it2->first);
      Edge<T> e((*it)->info, it2->first->info, it2->second, addr);
      edgeList.push_back(e);
    }
  }

  // the specified 'from' and 'to' vertexes where not found
  if (srcVtx == NULL || dstVtx == NULL) {
    return {};
  }

  // for iterations -> num of Vertexes-1
  for (int i = 1; i < this->totalVertices; i++) {
    // for all the edges
    for (auto it = edgeList.begin(); it != edgeList.end(); it++) {
      int v_from = it->address.first->tempIdx;
      int v_to = it->address.second->tempIdx;

      // update distance and previous arrays when a shorter path is found
      if (distance[v_from] != INT32_MAX &&
          ((distance[v_from] + it->dist) < distance[v_to])) {
        distance[v_to] = distance[v_from] + it->dist;
        previous[v_to] = it->address.first;
      }
    }
  }

  // check for negative cycles in Graph
  NegativeGraphCycle ex;
  for (auto it = edgeList.begin(); it != edgeList.end(); it++) {
    int v_from = it->address.first->tempIdx;
    int v_to = it->address.second->tempIdx;
    if (distance[v_from] != INT32_MAX &&
        ((distance[v_from] + it->dist) < distance[v_to])) {
      throw ex; // negative cycle is found, throw exception
    }
  }

  // -- Bellman-Ford Algorithm is complete --

  int j = dstVtx->tempIdx;

  // check for a path from dst to src vertex in bellman-ford tree
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
  // found the inner vertexes in the path. Now add src and dst vertexes to
  // complete the list

  resList.push_back(dstVtx->info);
  resList.push_front(srcVtx->info);

  return resList;
}

#endif
