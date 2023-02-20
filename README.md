# Assignments on Object-Oriented Programming
Collection of my Java and C++ homework assignments.

## Java Assignments

### HW1 - Trie

The application takes two command-line arguments: the first argument is the path to a text file that contains a list of words, and the second argument specifies the operation to perform on the words. The supported operations are:

* `size`: Prints the number of unique words in the text file.
* `contains_word`: Prints whether a given word is in the text file.
* `print_preorder`: Prints the words in the text file in pre-order traversal of the Trie.
* `print_dot`: Writes a Graphviz DOT file that represents the Trie and converts it to a PNG image.
* `distant_words`: Prints all the words in the text file that are a certain distance away from a given word, where the distance is defined as the number of characters that are different between the two words.
* `prefixed_words`: Prints all the words in the text file that start with a given prefix.

The program reads the text file using a utility class called `Utilities` and constructs a Trie data structure from the words. Depending on the command-line argument, it performs the corresponding operation using methods provided by the `Trie` class. If the command-line arguments are not valid, the program prints an error message.

### HW2 - Image Processing

This application is for opening and processing image files. 
It creates a user interface that enables the user to open PPM and YUV image files, and display the image in a graphical user interface. 
The program is also able to convert the YUV image format into PPM, which is a more commonly used format.

The user interface includes a main window with a scrollable panel for displaying images, a menu bar that provides options to open and save files, and labels that display the current image filename. 
The program includes methods for converting between different image formats, such as RGB and PPM, and handles errors with file loading and format conversions by displaying error messages to the user. 
The code makes use of the `javax.swing`, `java.awt`, `java.awt.image`, and `javax.imageio` packages.

### HW3 - File Manager

This Java application provides a user interface to browse, search and manipulate files and directories on the computer.

The GUI consists of several panels such as a favorites panel, a search bar, a path bar, and a contents panel where the files and directories are displayed. 
The application also includes a menu bar that allows the user to access different functionalities like opening a new window, editing, and searching.

The FileManagerGui class initializes and sets up the window with a JFrame, layouts, panels, and menus. 
It also instantiates the EditMenu and BrowserUtilities classes that provide additional functionality like renaming, copying, pasting files, and navigation between directories.

The user can navigate the file system and select files and directories using the mouse or the keyboard. 
Additionally, the search bar allows the user to search for files and directories by name or type.

This application uses the Java Swing library to create the GUI.

## C++ Assignments

### HW4 - AVL Tree

This code implements an AVL tree, a self-balancing binary search tree. 
The tree is defined by its nodes, which store elements of the same type as the one the user wants to use.

The implementation of the AVL tree follows the object-oriented programming paradigm. 
Namely, a Node class represents each node of the tree. 
The Node class is defined within the AVL class, which represents the AVL tree.

The Node class has a constructor, getters, and setters for the element, parent, left and right children of each node, 
and methods to find if a node is the left or right child of its parent, to calculate the height of its children, and to check if the node is balanced.

The AVL class implements the insert and remove functions for the AVL tree. 
Furthermore, it provides an Iterator class that implements the necessary methods to traverse the tree in order. 
The AVL tree supports insertions, removals, and finding elements in O(log n) time complexity, where n is the number of elements in the tree.

### HW5 - Graphs

This program allows the user to create and manipulate graphs in a user-friendly way. 
The user can create directed and undirected graphs, add and remove vertices and edges, 
and perform various operations on the graphs such as computing the minimum spanning tree, finding shortest paths, 
and traversing the graphs using breadth-first search and depth-first search.

The program is written in C++ and uses templates to allow for the use of different data types for vertices. 
The Graph class provides the underlying data structure for storing the graph, 
and the GraphUI class provides the user interface for interacting with the graph.

To use the program, simply run the executable and enter commands in the format "option parameters", 
where "option" is one of the available commands and "parameters" depend on the command. Available commands are:

* `digraph`: creates a directed graph.
* `av vertex`: adds a vertex to the graph.
* `rv vertex`: removes a vertex from the graph.
* `ae from_vertex to_vertex distance`: adds an edge from "from_vertex" to "to_vertex" with a given "distance".
* `re from_vertex to_vertex`: removes an edge from "from_vertex" to "to_vertex".
* `dot filename`: outputs the graph in Graphviz DOT format to a file with the given "filename" and generates a PNG image of the graph using the Graphviz "dot" command.
* `bfs start_vertex`: performs a breadth-first search of the graph starting at "start_vertex".
* `dfs start_vertex`: performs a depth-first search of the graph starting at "start_vertex".
* `dijkstra from_vertex to_vertex`: computes the shortest path between "from_vertex" and "to_vertex" using Dijkstra's algorithm.
* `bellman-ford from_vertex to_vertex`: computes the shortest path between "from_vertex" and "to_vertex" using the Bellman-Ford algorithm.
* `mst`: computes the minimum spanning tree of the graph.

Note that the program expects vertices to be entered as strings, and distances to be entered as integers.


