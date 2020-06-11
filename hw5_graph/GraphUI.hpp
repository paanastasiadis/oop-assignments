#ifndef _GRAPH_UI_
#define _GRAPH_UI_

#include "Graph.hpp"
#include <iostream>
#include <sstream>
#include <string>
using namespace std;
template <typename T> int graphUI() {

  string option, line, token;
  int distance;
  bool digraph = false;

  cin >> option;
  if (!option.compare("digraph"))
    digraph = true;
  Graph<T> g(digraph);

  while (true) {

    std::stringstream stream;
    cin >> option;

    if (!option.compare("av")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if (g.addVtx(vtx))
        cout << "av " << vtx << " OK\n";
      else
        cout << "av " << vtx << " NOK\n";
    } else if (!option.compare("rv")) {

    } else if (!option.compare("ae")) {
      getline(std::cin, line);
      stream << line;

      T from(stream);

      T to(stream);
      stream >> token;
      distance = stoi(token);
      // stream >> token;
      // distance = atoi(stream;
      if (g.addEdg(from, to, distance))
        cout << "ae " << from << " " << to << " OK\n";
      else
        cout << "ae " << from << " " << to << " NOK\n";
    } else if (!option.compare("re")) {

    } else if (!option.compare("dot")) {
      // getline(std::cin, line);
      // stream << line;
      
      char filename[] = "dotavl.dot";
      g.print2DotFile(filename);
      string str = "dot -Tpng ";
      str = str + filename + " -o " + "dotavl.png";
      const char *command = str.c_str();
      system(command);

    } else if (!option.compare("bfs")) {

      cout << "\n----- BFS Traversal -----\n";

      cout << "\n-------------------------\n";
    } else if (!option.compare("dfs")) {

      cout << "\n----- DFS Traversal -----\n";

      cout << "\n-------------------------\n";
    } else if (!option.compare("dijkstra")) {
      getline(std::cin, line);
      stream << line;
      T from(stream);
      T to(stream);

      cout << "Dijkstra (" << from << " - " << to << "): ";

    } else if (!option.compare("bellman-ford")) {
      getline(std::cin, line);
      stream << line;
      T from(stream);
      T to(stream);

      cout << "Bellman-Ford (" << from << " - " << to << "): ";

    } else if (!option.compare("mst")) {

      cout << "\n--- Min Spanning Tree ---\n";
      //! sum previously not in commentsd
      cout << "MST Cost: "
           << "sum" << endl;
    } else if (!option.compare("q")) {
      cerr << "bye bye...\n";
      return 0;
    } else if (!option.compare("#")) {
      string line;
      getline(cin, line);
      cerr << "Skipping line: " << line << endl;
    } else {
      cout << "INPUT ERROR\n";
      return -1;
    }
  }
  return -1;
}

#endif
