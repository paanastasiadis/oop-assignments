package ce326.hw1;

/**
 * @author Panagiotis Anastasiadis 2134
 * <p>
 * Mail:   paanastasiadis@uth.gr
 * Course: Object-oriented Programming
 * Assignment: Homework 1
 * Department: Electrical and Computer Engineering
 * University: University of Thessaly
 */

public class TrieNode {
    TrieNode[] children;
    boolean isTerminal;
    private char character;
    private int depth;

    /**
     * Constructor of the root node
     */
    public TrieNode() {
        this.children = new TrieNode[26];
    }

    /**
     * Constructor of a not-root node
     *
     * @param c the character to be assigned to the node
     */
    public TrieNode(char c) {
        setCharacter(c);
        this.children = new TrieNode[26];
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return this.character;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

}
