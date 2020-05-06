#include "../include/avl.hpp"
#include <fstream>
#include <iostream>
using namespace std;

AVL::Node::Node(const string &e, Node *parent, Node *left, Node *right) {
  this->element = e;
  this->parent = parent;
  this->left = left;
  this->right = right;
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

void AVL::Node::setHeight(int h) { this->height = h; }

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
  return this->getLeft()->getHeight();
}

int AVL::Node::rightChildHeight(void) const {
  return this->getRight()->getHeight();
}

int AVL::Node::updateHeight(void) {
  int l_height = this->leftChildHeight();
  int r_height = this->rightChildHeight();
  if (l_height >= r_height) {
    this->setHeight(l_height);
  } else {
    this->setHeight(r_height);
  }

  return (this->getHeight());
}

bool AVL::Node::isBalanced(void) {
  int diff = this->leftChildHeight() - this->rightChildHeight();
  if (diff > 1 or diff < -1) {
    return false;
  }
  return true;
}