
#include "../include/AVL.hpp"
#define MAX_LINE_LEN 128
#define MAX_ENTRIES 64

int main(int argc, char *argv[]) {

  AVL avl;
  if (argc < 2) {
    cout << "\nInsufficient number of arguments. Exiting..." << endl;
    return 0;
  }

  ifstream inf(argv[1]);
  char line[MAX_LINE_LEN];
  string words[MAX_ENTRIES];

  if (!inf.is_open()) {
    cout << "Unable to open file " << argv[1];
    return -1;
  }

  int i = 0;
  while (inf.getline(line, MAX_LINE_LEN)) {
    words[i] = string(line);
    // cout << i+1 << ". " << words[i] << endl;
    avl += words[i++];
    cout << avl << endl;
  }
  inf.close();

  char filename[] = "dotavl.dot";
  avl.print2DotFile(filename);
  string str = "dot -Tpng ";
  str = str + filename + " -o " + "dotavl.png";
  const char *command = str.c_str();
  system(command);

  cout << avl << endl;

  inf.open(argv[2], std::ifstream::in);
  if (!inf.is_open()) {
    cout << "Unable to open file " << argv[2];
    return -1;
  }

  i = 0;
  while (inf.getline(line, MAX_LINE_LEN)) {
    words[i] = string(line);
    if (i == 11) {
      avl.print2DotFile(filename);
      str = "dot -Tpng ";
      str = str + filename + " -o " + "dotavl.png";
      command = str.c_str();
      system(command);
    }
    // cout << i+1 << ". " << words[i] << endl;
    avl -= words[i++];

    if (i == 12) {
      avl.print2DotFile(filename);
      str = "dot -Tpng ";
      str = str + filename + " -o " + "dotavl2.png";
      command = str.c_str();
      system(command);
    }

    cout << avl << endl;
  }

  inf.close();
}
