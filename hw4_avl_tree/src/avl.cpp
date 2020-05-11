#include "../include/avl.hpp"
#include <fstream>
#include <iostream>
using namespace std;

// #     #
// ##    #  ####  #####  ######
// # #   # #    # #    # #
// #  #  # #    # #    # #####
// #   # # #    # #    # #
// #    ## #    # #    # #
// #     #  ####  #####  ######

// #     #
// ##    #  ####  #####  ######
// # #   # #    # #    # #
// #  #  # #    # #    # #####
// #   # # #    # #    # #
// #    ## #    # #    # #
// #     #  ####  #####  ######

AVL::Node::Node(const string &e, Node *parent, Node *left, Node *right) {
  this->element = e;
  this->parent = parent;
  this->left = left;
  this->right = right;
  this->height = 0;
  this->height_diff = 0;
}

AVL::Node *AVL::Node::getParent(void) const { return this->parent; }

AVL::Node *AVL::Node::getLeft(void) const { return this->left; }

AVL::Node *AVL::Node::getRight(void) const { return this->right; }

string AVL::Node::getElement(void) const { return this->element; }

int AVL::Node::getHeight(void) const { return this->height; }

void AVL::Node::setLeft(Node *left_ptr) { this->left = left_ptr; }

void AVL::Node::setRight(Node *right_ptr) { this->right = right_ptr; }

void AVL::Node::setParent(Node *parent_ptr) { this->parent = parent_ptr; }

void AVL::Node::setElement(string e) { this->element = e; }

bool AVL::Node::isLeft(void) const {
  if (this->getParent()->getLeft() == this) {
    return true;
  }
  return false;
}

bool AVL::Node::isRight(void) const {
  if (this->getParent()->getRight() == this) {
    return true;
  }
  return false;
}

int AVL::Node::leftChildHeight(void) const {
  Node *left_child = this->getLeft();
  if (left_child == NULL) {
    return 0;
  }

  return left_child->getHeight();
}

int AVL::Node::rightChildHeight(void) const {
  Node *right_child = this->getRight();
  if (right_child == NULL) {
    return 0;
  }

  return right_child->getHeight();
}
int AVL::Node::getHeightDiff(void) const { return this->height_diff; }

int AVL::Node::setHeight(AVL::Node *child_node) {
  int h = 0;
  if (child_node != NULL) {

    int l_height = setHeight(child_node->left);
    int r_height = setHeight(child_node->right);
    child_node->height_diff = l_height - r_height;
    int max_height = max(l_height, r_height);
    h = max_height + 1;
    child_node->height = h;
  }
  return h;
}
int AVL::Node::updateHeight(void) {
  // int l_height = this->leftChildHeight();
  // int r_height = this->rightChildHeight();

  // // store the new height difference between the subtrees
  // this->height_diff = l_height - r_height;

  // int max_length = max(l_height, r_height);

  this->setHeight(this);

  return (this->getHeight());
}

bool AVL::Node::isBalanced(void) {
  int diff = this->getHeightDiff();
  if (diff > 1 || diff < -1) {
    return false;
  }
  return true;
}

//    #    #     # #
//   # #   #     # #
//  #   #  #     # #
// #     # #     # #
// #######  #   #  #
// #     #   # #   #
// #     #    #    #######

//    #    #     # #
//   # #   #     # #
//  #   #  #     # #
// #     # #     # #
// #######  #   #  #
// #     #   # #   #
// #     #    #    #######
AVL::AVL() {
  this->root = NULL;
  this->size = 0;
}

AVL::AVL(const AVL &avl_tree) {

  // TODO create a new Node object instead of passing the pointer
  this->size = avl_tree.size;
  this->root = avl_tree.root;
}

bool AVL::add(string e) {

  AVL::Node *node;
  node = insertRecursively(this->root, NULL, e);

  if (node == NULL) {
    return false;
  } else {
    return true;
  }
}

AVL::Node *AVL::insertRecursively(AVL::Node *curr_node, AVL::Node *parent_node,
                                  const string &value) {

  if (curr_node == NULL) {
    curr_node = new AVL::Node(value, parent_node, NULL, NULL);
    if (this->root == NULL) {
      this->root = curr_node;
    }
    curr_node->updateHeight();

    return curr_node;

  } else if (value.compare(curr_node->getElement()) == 0) {

    return NULL;

  } else if (value < curr_node->getElement()) {

    AVL::Node *node = insertRecursively(curr_node->getLeft(), curr_node, value);

    if (node == NULL) {
      return NULL;
    }
    curr_node->setLeft(node);
    node->setParent(curr_node);

    curr_node->updateHeight();
    if (!curr_node->isBalanced()) {
      if (root == curr_node) {
        root = this->setBalance(curr_node);
        curr_node = root;
      } else {
        curr_node = this->setBalance(curr_node);
      }
    }

  } else if (value >= curr_node->getElement()) {

    AVL::Node *node =
        insertRecursively(curr_node->getRight(), curr_node, value);

    if (node == NULL) {
      return NULL;
    }
    curr_node->setRight(node);
    node->setParent(curr_node);

    curr_node->updateHeight();
    if (!curr_node->isBalanced()) {
      if (root == curr_node) {
        root = this->setBalance(curr_node);
        curr_node = root;
      } else {
        curr_node = this->setBalance(curr_node);
      }
    }
  }

  return curr_node;
}

AVL::Node *AVL::setBalance(AVL::Node *n) {
  int diff = n->getHeightDiff();
  if (diff > 1) {
    if (n->getLeft()->getHeightDiff() <= 0) {
      n = rotateLeftRight(n);
    } else {
      n = rotateLeftLeft(n);
    }
  } else if (diff < -1) {
    if (n->getRight()->getHeightDiff() <= 0) {
      n = rotateRightRight(n);
    } else {
      n = rotateRightLeft(n);
    }
  }
  return n;
}

/**
 * Right - Right Rotation
 */
AVL::Node *AVL::rotateRightRight(AVL::Node *n) {

  AVL::Node *tmp;
  tmp = n->getRight();

  AVL::Node *rotated_child = tmp->getLeft();
  if (rotated_child != NULL) {
    rotated_child->setParent(n);
  }

  n->setRight(rotated_child);
  tmp->setLeft(n);

  n->setParent(tmp);

  return tmp;
}

/**
 * Left - Left Rotation
 */
AVL::Node *AVL::rotateLeftLeft(AVL::Node *n) {
  AVL::Node *tmp;
  tmp = n->getLeft();

  AVL::Node *rotated_child = tmp->getRight();
  if (rotated_child != NULL) {
    rotated_child->setParent(n);
  }

  n->setLeft(rotated_child);
  tmp->setRight(n);

  n->setParent(tmp);
  return tmp;
}

/**
 * Left - Right Rotation
 */
AVL::Node *AVL::rotateLeftRight(AVL::Node *n) {
  AVL::Node *tmp;
  tmp = n->getLeft();
  AVL::Node *rotated_child = rotateRightRight(tmp);
  if (rotated_child != NULL) {
    rotated_child->setParent(n);
  }
  n->setLeft(rotated_child);
  return rotateLeftLeft(n);
}

/**
 * Right - Left Rotation
 */
AVL::Node *AVL::rotateRightLeft(AVL::Node *n) {

  AVL::Node *tmp;
  tmp = n->getRight();
  AVL::Node *rotated_child = rotateLeftLeft(tmp);
  if (rotated_child != NULL) {
    rotated_child->setParent(n);
  }
  n->setRight(rotated_child);
  return rotateRightRight(n);
}

void AVL::preorderTraversal(AVL::Node *node, std::ostream &out) const {
  if (node == NULL)
    return;
  out << node->getElement() << " ";
  preorderTraversal(node->getLeft(), out);
  preorderTraversal(node->getRight(), out);
}

void AVL::pre_order(std::ostream &out) { preorderTraversal(this->root, out); }

std::ostream &operator<<(std::ostream &out, const AVL &tree) {

  tree.preorderTraversal(tree.root, out);

  return out;
}
