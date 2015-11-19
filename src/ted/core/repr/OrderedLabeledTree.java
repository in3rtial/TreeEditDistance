package ted.core.repr;


import ted.core.util.Verifier;

import java.util.*;


/**
 * Ordered Labeled Tree class. Don't give it a forest!
 */
public class OrderedLabeledTree<T> {

    private final Node<T> root;
    //
    private final ArrayList<T> postOrderLabels;
    private final ArrayList<Node<T>> postOrderNodes;
    //
    private final int[] leftmostDescendants;
    private final int[] keyRoots;


    /**
     * Ordered Labeled Tree that contains arbitrary data as labels.
     * This is the internal constructor, can be used directly, or use the more friendly String Tree constructor
     * if the labels are of type String (good enough for most cases).
     *
     * @param brackets       bracket notation that specifies the topology of the tree:
     *                       '(' : nesting symbol, adds a child and sets the position as a child of the current position
     *                       ')' : closing symbol, position = position.parent
     *                       e.g. (()()(()()))
     *                       These symbols are all that's necessary to create an ordered labeled tree.
     * @param preOrderLabels labels of the tree, organized in a pre-order fashion.
     */
    public OrderedLabeledTree(String brackets, ArrayList<T> preOrderLabels) throws IllegalArgumentException {
        // check that the tree is really a tree, and is balanced
        if (!Verifier.isValidTreeStructure(brackets)) {
            throw new IllegalArgumentException("This structure seems unbalanced " + brackets);
        }

        // instantiate the root and start building the tree
        String bracketsMinusTheRoot = brackets.substring(1, brackets.length() - 2);
        root = new Node<>(null, preOrderLabels.get(0));
        Node<T> position = root;

        int nodeCount = 1;
        for (char c : bracketsMinusTheRoot.toCharArray()) {
            if (c == '(')       // create new node and position goes down
            {
                position = new Node<>(position, preOrderLabels.get(nodeCount));

                nodeCount += 1;
            } else if (c == ')')  // position goes up
            {
                position = position.getParent();
            }
        }


        // get post order pointers, set the index and get the labels
        postOrderNodes = getPostOrderEnumeration(root);
        postOrderLabels = new ArrayList<>();
        assert preOrderLabels.size() == postOrderNodes.size();
        for (int i = 0; i != postOrderNodes.size(); ++i) {
            // set the index of the nodes
            postOrderNodes.get(i).setIndex(i);

            // extract the post-order label
            postOrderLabels.add(postOrderNodes.get(i).getLabel());
        }


        // compute the leftmost descendants
        leftmostDescendants = new int[postOrderNodes.size()];
        for (int i = 0; i != postOrderNodes.size(); ++i) {
            position = postOrderNodes.get(i);
            while (position.getChildren().size() != 0) {
                position = position.getChildren().get(0);
            }
            leftmostDescendants[i] = position.getIndex();
        }

        // compute the keyRoots
        Hashtable<Integer, Integer> nodeToLeftmostDescendant = new Hashtable<>();
        Integer lmd;
        for (int i = postOrderNodes.size() - 1; i != -1; --i) {
            lmd = leftmostDescendants[i];

            if (!(nodeToLeftmostDescendant.containsKey(lmd))) {
                nodeToLeftmostDescendant.put(lmd, i);
            }
        }

        keyRoots = new int[nodeToLeftmostDescendant.size()];
        Enumeration<Integer> keyRootsEnum = nodeToLeftmostDescendant.elements();
        int index = 0;
        while (keyRootsEnum.hasMoreElements()) {
            keyRoots[index] = keyRootsEnum.nextElement();
            index += 1;
        }
        java.util.Arrays.sort(keyRoots);
    }


    /**
     * Used by the getPostOrderEnumeration method to perform a post-order traversal
     * of the tree, while filling an array of Nodes.
     *
     * @param position pointer to the node in the tree.
     */
    private void postorderHelper(Node<T> position, ArrayList<Node<T>> postOrderNodes) {
        if (position != null) {
            for (Node<T> n : position.getChildren()) {
                postorderHelper(n, postOrderNodes);
            }
            postOrderNodes.add(position);
        }
    }


    /**
     * Computes a list of the nodes, in post-order traversal order.
     *
     * @param root root node of the tree
     * @return the nodes, in post-order traversal order.
     */
    private ArrayList<Node<T>> getPostOrderEnumeration(Node<T> root) {
        ArrayList<Node<T>> postOrderEnum = new ArrayList<>();
        postorderHelper(root, postOrderEnum);
        return postOrderEnum;
    }


    public Node<T> getRoot() {
        return root;
    }


    /**
     * getter
     *
     * @return labels in post-order traversal order
     */
    public ArrayList<T> getPostOrderLabels() {
        return postOrderLabels;
    }

    /**
     * getter
     *
     * @return leftmost descendants for each of the nodes
     */
    public int[] getLeftmostDescendants() {
        return leftmostDescendants;
    }

    /**
     * getter
     *
     * @return the keyRoots of the tree
     */
    public int[] getKeyRoots() {
        return keyRoots;
    }


}