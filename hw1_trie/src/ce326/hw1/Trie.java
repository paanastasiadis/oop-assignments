
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


public class Trie {
    private TrieNode root;
    private int wordsCounter;
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    public static final int ALPHABET_SIZE = 26;

    /**
     * Trie constructor
     *
     * @param words an array of words to be stored in the trie
     */
    public Trie(String[] words) {
        root = new TrieNode();  //create the root of the trie
        wordsCounter = 0;       //var to store the total words of the trie
        int index = 0;

        while (words[index] != null) {
            add(words[index]);
            index++;
        }
    }

    /**
     * Adds specified word to the trie structure
     *
     * @param word the input word as a string
     * @return true if the save was successful, false otherwise
     */
    public boolean add(String word) {
        TrieNode currentNode = root;
        int nodeDepth = 0;
        int index;
        char charToCheck;

        for (int i = 0; i < word.length(); i++) {
            charToCheck = word.charAt(i);

            /* check for incompatible characters in the word string */
            if (alphabet.indexOf(charToCheck) == -1) {
                /* return with failure if at least one incompatible character was found. */
                return false;
            }

            index = charToCheck - 'a';  //the index for every character node is its own character
            nodeDepth++;  //increment the depth for every new character-node that is checked in the Trie

            if (currentNode.children[index] == null) {
                /*if the node for that character does not exist, assign a new node to the trie*/
                TrieNode tempNode = new TrieNode(charToCheck);

                /* walk through the next node for the next assignment */
                currentNode.children[index] = tempNode;
                currentNode = tempNode;

                currentNode.setDepth(nodeDepth);
            } else {
                currentNode = currentNode.children[index];
            }
        }

        if (!currentNode.isTerminal) {
            currentNode.isTerminal = true;  //declare the last node as the last character of the the word
            wordsCounter++; //at this point a new word was added to the Trie, so increment the Trie size
        } else {
            return false; //the word was already on the tree,  none new word was added
        }

        return true;
    }

    /**
     * Checks if the specified word exists in the Trie
     *
     * @param word the input word
     * @return true if the word exists, false otherwise
     */
    public boolean contains(String word) {
        TrieNode currentNode = root;  //start from the root
        char charToCheck;

        for (int i = 0; i < word.length(); i++) {
            charToCheck = word.charAt(i);

            /* stop the searching process if there is no alphabet characters in the word */
            if (alphabet.indexOf(charToCheck) == -1) {
                return false;
            }

            /* check for every character in the word
               if there is a not-null node with the same character in the Trie */
            if (currentNode.children[charToCheck - 'a'] != null) {
                currentNode = currentNode.children[charToCheck - 'a'];
            } else {
                return false;
            }
        }


        return currentNode.isTerminal;
    }

    /**
     * Size indicator method of the Trie
     *
     * @return the total number of the words stored in the Trie
     */
    public int size() {
        return wordsCounter;
    }

    /**
     * Overrides the toString method and creates a string with the pre-order description of the Trie.
     *
     * @return the string with all the words of the Trie in Pre-Order.
     */
    public String toString() {
        StringBuilder strTrie = new StringBuilder();
        recursionPreorder(root, strTrie);
        return strTrie.toString();
    }

    /**
     * Walks through all the nodes recursively and constructs the string with the pre-order description of the Trie.
     *
     * @param node    the starting node of the recursion
     * @param strTrie the string to be constructed.
     */
    private void recursionPreorder(TrieNode node, StringBuilder strTrie) {
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                strTrie.append(" ");
                strTrie.append(node.children[i].getCharacter());
                if (node.children[i].isTerminal) {
                    strTrie.append("!");
                }
                recursionPreorder(node.children[i], strTrie);
            }
        }
    }

    /**
     * Creates a string with a ".dot file" description of the trie, suitable for the graphviz tool
     *
     * @return A string with the ".dot" contents of the trie
     */
    public String toDotString() {
        StringBuilder strTrie = new StringBuilder();

        strTrie.append("graph Trie {\n");
        recursionDot(root, strTrie);
        strTrie.append("}");

        return strTrie.toString();
    }

    /**
     * Walks through all the nodes of the trie recursively and creates a string with the the compatible-for-dot
     * contents of the nodes.
     *
     * @param node    the starting node of the recursion
     * @param strTrie the string to be constructed
     */
    private void recursionDot(TrieNode node, StringBuilder strTrie) {

        if (node == root) {
            strTrie.append(node.hashCode());
            strTrie.append(" [label = \"ROOT\", shape=house, color=black]\n");
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                strTrie.append(node.hashCode());
                strTrie.append(" -- ");
                strTrie.append(node.children[i].hashCode());
                strTrie.append("\n");
                strTrie.append(node.children[i].hashCode());
                strTrie.append(" [label = \"");
                strTrie.append(node.children[i].getCharacter());
                strTrie.append("\", shape=egg, ");

                if (node.children[i].isTerminal) {
                    strTrie.append("color=red, style=filled]\n");
                } else {
                    strTrie.append("color=black]\n");
                }
                recursionDot(node.children[i], strTrie);
            }
        }
    }

    /**
     * Finds all the words in the Trie that differ from the specified word by one character.
     *
     * @param word the input word
     * @return returns a String array with the matching words
     */
    public String[] differByOne(String word) {
        StringBuilder Str = new StringBuilder();
        int charsDiffer = 0;
        String[] matchingWords = new String[this.size()];

        recursionDiff(root, Str, matchingWords, word, charsDiffer, 1);

        return matchingWords;
    }

    /**
     * Finds all the words in the Trie that differ from the specified word by one to max characters.
     *
     * @param word the input word
     * @param max  the max number of characters that is allowed to be different between the compared words
     * @return a String array with the matching words
     */
    public String[] differBy(String word, int max) {
        StringBuilder Str = new StringBuilder();
        int charsDiffer = 0;

        String[] matchingWords = new String[this.size()];
        recursionDiff(root, Str, matchingWords, word, charsDiffer, max);

        return matchingWords;
    }

    /**
     * Walks through the nodes of the trie recursively, compares and finds the matching words and stores them in a
     * String array
     *
     * @param node          the starting node of the recursion
     * @param Str           the string to be transferred in every state of the recursion, in order to be able
     *                      to compare the input word with the current word prefix in that state
     * @param matchingWords the String array to be constructed with the matching words
     * @param inputWord     the input word that will be compared
     * @param charsDiffer   the number of characters in every recursion state that the word has different
     * @param maxAllowed    the number of max characters allowed to differ
     */
    private void recursionDiff(TrieNode node, StringBuilder Str, String[] matchingWords,
                               String inputWord, int charsDiffer, int maxAllowed) {

        StringBuilder matchStr = new StringBuilder();

        /* there is no point in checking further if the word of the trie is longer than the input word */
        if (node.getDepth() > inputWord.length()) {
            return;
        } else if ((node != root) && (node.getCharacter() != inputWord.charAt(node.getDepth() - 1))) {
            charsDiffer++; //increments when the corresponding characters of the input and Trie word differ
            if (charsDiffer > maxAllowed) { //no point checking beyond the max allowed characters
                return;
            }
        }

        /*append the string prefix of the previous state along with the current node character
          to construct the current word of the recursion */
        if (node != root) {
            matchStr.append(Str);
            matchStr.append(node.getCharacter());
        }

        /* put the matching word in the final string array if we reached a terminal node
           and the the length of the current word fits with the input word */
        if ((node.isTerminal) && (node.getDepth() == inputWord.length())) {
            for (int i = 0; i < matchingWords.length; i++) {
                if (matchingWords[i] == null) {
                    matchingWords[i] = matchStr.toString();
                    break;
                }
            }
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                /* walk recursively and transfer the current word prefix(matchStr)
                   and the current number of characters that differ (charsDiffer) to the next state
                 */
                recursionDiff(node.children[i], matchStr, matchingWords, inputWord, charsDiffer, maxAllowed);
            }
        }
    }


    /**
     * Find all the words in the Trie with the specified prefix
     *
     * @param prefix the input String to serve as the prefix
     * @return a String array with the matching words
     */
    public String[] wordsOfPrefix(String prefix) {
        TrieNode currentNode = root;
        String[] matchingWords = new String[size()];
        char charToCheck;
        int prefixedRootDepth; //the depth of the last node-character of the prefix in the Trie

        StringBuilder prefixStr = new StringBuilder();
        prefixStr.append(prefix);

        for (int i = 0; i < prefix.length(); i++) {
            charToCheck = prefix.charAt(i);

            if (alphabet.indexOf(charToCheck) == -1) {
                return null;
            }

            /* search for the prefix in the Trie */
            if (currentNode.children[charToCheck - 'a'] != null) {
                currentNode = currentNode.children[charToCheck - 'a'];
            } else {
                return null;
            }
        }
        prefixedRootDepth = currentNode.getDepth();

        //go recursively
        recursionPrefix(currentNode, prefixStr, matchingWords, prefixedRootDepth);
        return matchingWords;
    }

    /**
     * Walk recursively, find and store  all the words with the specified prefix
     *
     * @param node              the starting node of the trie. Also the last character of the prefix
     * @param Str               the string to be transferred in every state of the recursion, in order to be able
     *                          to compare the input word with the current word prefix in that state
     * @param matchingWords     the String array to be constructed with the matching words
     * @param prefixedRootDepth the depth of the last node-character of the prefix
     */
    private void recursionPrefix(TrieNode node, StringBuilder Str, String[] matchingWords, int prefixedRootDepth) {
        StringBuilder matchStr = new StringBuilder();
        matchStr.append(Str);

        /* do not append the last character of the prefix to the current word of the trie because it's already there*/
        if (node.getDepth() > prefixedRootDepth) {
            matchStr.append(node.getCharacter());
        }

        if (node.isTerminal) {
            for (int i = 0; i < matchingWords.length; i++) {
                if (matchingWords[i] == null) {
                    matchingWords[i] = matchStr.toString();
                    break;
                }
            }
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                recursionPrefix(node.children[i], matchStr, matchingWords, prefixedRootDepth);
            }
        }
    }
}
