#ifndef __AVL_HPP_
#define __AVL_HPP_

#include <fstream>
#include <iostream>
#include <stack>

using namespace std;

class AVL {
private:
  class Node {
    Node *parent, *left, *right;
    int height;
    string element;

    //    #
    //   # #   #####  #####  ###### #####     #####  #   #    #    # ######
    //  #   #  #    # #    # #      #    #    #    #  # #     ##  ## #
    // #     # #    # #    # #####  #    #    #####    #      # ## # #####
    // ####### #    # #    # #      #    #    #    #   #      #    # #
    // #     # #    # #    # #      #    #    #    #   #      #    # #
    // #     # #####  #####  ###### #####     #####    #      #    # ######
    int height_diff;

  public:
    Node(const string &e, Node *parent, Node *left, Node *right);
    Node *getParent() const;
    Node *getLeft() const;
    Node *getRight() const;
    string getElement() const;
    int getHeight() const;

    int getHeightDiff() const;
    void setLeft(Node *);
    void setRight(Node *);
    void setParent(Node *);
    void setElement(string e);

    bool isLeft() const;
    bool isRight() const;
    int rightChildHeight() const;
    int leftChildHeight() const;
    int updateHeight();
    bool isBalanced();

    //    #
    //   # #   #####  #####  ###### #####     #####  #   #    #    # ######
    //  #   #  #    # #    # #      #    #    #    #  # #     ##  ## #
    // #     # #    # #    # #####  #    #    #####    #      # ## # #####
    // ####### #    # #    # #      #    #    #    #   #      #    # #
    // #     # #    # #    # #      #    #    #    #   #      #    # #
    // #     # #####  #####  ###### #####     #####    #      #    # ######
    int setHeight(Node *temp);
  };

public:
  int size;
  Node *root;

public:
  class Iterator {

  private:
    std::stack<AVL::Node *> node_stack;
    AVL::Node *point_node;

  public:
    Iterator(AVL::Node *root);

    AVL::Node* getNode();
    Iterator &operator++();
    Iterator operator++(int a);
    string operator*();
    bool operator!=(Iterator it);
    bool operator==(Iterator it);
  };

  Iterator begin() const;
  Iterator end() const;

  static const int MAX_HEIGHT_DIFF = 1;
  AVL();
  AVL(const AVL &);
  bool contains(string e);
  bool add(string e);
  bool rmv(string e);
  void print2DotFile(char *filename);
  void pre_order(std::ostream &out) const;

  friend std::ostream &operator<<(std::ostream &out, const AVL &tree);
  AVL &operator=(const AVL &avl);
  AVL operator+(const AVL &avl);
  AVL &operator+=(const AVL &avl);
  AVL &operator+=(const string &e);
  AVL &operator-=(const string &e);
  AVL operator+(const string &e);
  AVL operator-(const string &e);

  //    #
  //   # #   #####  #####  ###### #####     #####  #   #    #    # ######
  //  #   #  #    # #    # #      #    #    #    #  # #     ##  ## #
  // #     # #    # #    # #####  #    #    #####    #      # ## # #####
  // ####### #    # #    # #      #    #    #    #   #      #    # #
  // #     # #    # #    # #      #    #    #    #   #      #    # #
  // #     # #####  #####  ###### #####     #####    #      #    # ######

  AVL::Node *deleteLeftestInRight(AVL::Node *node);
  AVL::Node *deleteRecursively(AVL::Node *node, const string &value);

  AVL::Node *insertRecursively(AVL::Node *curr_node, AVL::Node *parent_node,
                               const string &value);

  AVL::Node *findRecursively(AVL::Node *curr_node, const string &value);

  AVL::Node *setBalance(AVL::Node *n);

  AVL::Node *rotateRightRight(AVL::Node *n);
  AVL::Node *rotateLeftLeft(AVL::Node *n);
  AVL::Node *rotateLeftRight(AVL::Node *n);
  AVL::Node *rotateRightLeft(AVL::Node *n);
};

#endif
