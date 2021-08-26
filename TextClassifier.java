// Christopher Ku
// Section: AG with Jiamae Wang
// Assessment 7: TextClassifier
//
// The TextClassifier class represents an abstract data type which can be used for
// classifying text documents. The TextClassifier class can be used to determine
// the levels of toxicity that exists within the english language.
public class TextClassifier {
    private Node tree;
    private Vectorizer designMatrix;

    /**
     * Constructs a TextClassifier instance by taking in 2 parameters. One being
     * a Vectorizer and the other being a Splitter.
     *
     * @param vectorizer   This Vecotrizer parameter is responsible for constructing
     *                     a design matrix from a given text in English.
     *
     * @param splitter   This Splitter parameter here is used for constructing the
     *                   binary tree that is used within the TextClassifier class.
     */
    public TextClassifier(Vectorizer vectorizer, Splitter splitter) {
        this.tree = constructTree(splitter);
        this.designMatrix = vectorizer;
    }

    /**
     * Takes in a Splitter parameter and returns a Node data type that represents a
     * binary tree for the classification structure of different english texts.
     *
     * @param splitter   This Splitter parameter is used for building the binary
     *                   tree associated with the TextClassifier instance.
     *
     * @return   A non-null binary tree that is constructed based on a given
     *           english text.
     */
    private Node constructTree(Splitter splitter) {
        Splitter.Result seperate = splitter.split();
        if (seperate == null) {
            return new Node(splitter.label());
        } else {
            Node parent = new Node(splitter.label());
            parent.left = constructTree(seperate.left);
            parent.right = constructTree(seperate.right);
            parent.split = seperate.split;
            return parent;
        }
    }

    /**
     * Takes in a non-null String and processes the individual texts within the
     * String and returns a boolean value representing the predicted label for
     * the given text in english which is case insensitive.
     *
     * @param text   A non-null String with case insensitive texts which will be
     *               processed by the method.
     *
     * @return   A boolean value that predicts the label for the given text in
     *           english.
     */
    public boolean classify(String text) {
        double[] textValues = this.designMatrix.transform(text)[0];
        return classify(tree, textValues);
    }

    /**
     * Takes in 2 parameters one being a Node and the other being a double array
     * and then returns a boolean value representing label of the double array.
     *
     * @param current   A Node data type which represents a node within the
     *                  constructed binary tree for the TextClassifier instance.
     *
     * @param textValues   A double array which contains text values that represent
     *                     the given english text in the TextClassifier instance.
     *
     * @return   A boolean value which represents predicted label of the double array
     *           of text values.
     */
    private boolean classify(Node current, double[] textValues) {
        if (current.split == null) {
            return current.label;
        } else {
            if (current.split.goLeft(textValues)) {
                return classify(current.left, textValues);
            } else {
                return classify(current.right, textValues);
            }
        }
    }

    /**
     * Prints out a java code representation of the if else decision tree without
     * braces and one additional indetation space per level.
     */
    public void print() {
        String indent = "";
        print(this.tree, indent);
    }

    /**
     * Takes in 2 parameters one being a Node data type and the other being a
     * String data type, and prints out the java code representation of the if
     * else decision process within the binary tree of the TextClassifier instance.
     *
     * @param current   A Node data type which represents a node within the
     *                  constructed binary tree for the TextClassifier instance.
     *
     * @param indent   A String that represents the indentation spaces for each
     *                 depth level within the decision tree.
     */
    private void print(Node current, String indent) {
        if (current.isLeaf()) {
            System.out.println(indent + "return " + current.label + ";");
        } else {
            System.out.println(indent + "if (" + current.split + ")");
            print(current.left, indent + " ");
            System.out.println(indent + "else");
            print(current.right, indent + " ");
        }
    }

    /**
     * Takes in an integer value representing the depth to which the binary tree
     * of the TextClassifier instance would be pruned (The level where each node
     * would be replaced by a leaf node)
     *
     * @param depth   An integer value representing the depth to which the binary tree
     *                of the TextClassifier instance would be pruned.
     */
    public void prune(int depth) {
        this.tree = prune(tree, depth);
    }

    /**
     * Takes in two parameters one being a node data type which represents the
     * current node of the binary tree and the other being an integer representing
     * the depth to which nodes would be replaced by leaf nodes. Then returns a Node
     * data type which represents a new decision tree with nodes at the given depth
     * replaced with leaf nodes.
     *
     * @param current   A Node data type which represents a node within the
     *                  constructed binary tree for the TextClassifier instance.
     *
     * @param targetDepth   An integer value representing the depth to which the binary tree
     *                      of the TextClassifier instance would be pruned
     *
     * @return   a Node data type which represents a new decision tree with nodes at the
     *           given depth replaced with leaf nodes.
     */
    private Node prune(Node current, int targetDepth) {
        if (current != null) {
            if (targetDepth == 0 ) {
                return new Node(current.label);
            } else {
                current.left = prune(current.left, targetDepth - 1);
                current.right = prune(current.right, targetDepth - 1);
            }
        }
        return current;
    }

    // An internal node or a leaf node in the decision tree.
    private static class Node {
        public Split split;
        public boolean label;
        public Node left;
        public Node right;

        // Constructs a new leaf node with the given label.
        public Node(boolean label) {
            this(null, label, null, null);
        }

        // Constructs a new internal node with the given split, label, and left and right nodes.
        public Node(Split split, boolean label, Node left, Node right) {
            this.split = split;
            this.label = label;
            this.left = left;
            this.right = right;
        }

        // Returns true if and only if this node is a leaf node.
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }
}


