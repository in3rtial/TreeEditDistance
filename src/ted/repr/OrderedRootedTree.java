package ted.repr;

import ted.repr.Node;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Ordered labeled tree used to represent both RNA abstract shapes
 * and Vienna dot-bracket (planar ordered labeled tree)
 */

public class OrderedRootedTree<T> {

    private final ArrayList<T> postOrderLabels;
    private final int[] leftmostDescendants;
    private final int[] keyRoots;

    private final Node<T> root;
    private final String stringRepresentation;


    /**
     * Ordered Rooted Labeled Tree that contains arbitrary data as labels.
     * @param stringRepresentation  The string representation is Vienna Dot Bracket and specifies the topology of the tree:
     *  '(' : nesting symbol, adds a child and sets the position as a child of the current position
     *  ')' : closing symbol, position = position.parent
     *  '.' : add a node, parent stays the same
     *  These symbols are all that's necessary to create an ordered rooted labeled tree.
     * @param postOrderLabels labels of the tree, organized in a postorder fashion already
     */
    public OrderedRootedTree(String stringRepresentation,
                             ArrayList<T> postOrderLabels) {

        // some easy assignations
        this.postOrderLabels = postOrderLabels;
        this.stringRepresentation = stringRepresentation;

        // instantiate the root and start building the tree
        this.root = new Node<T>(null, null);
        Node<T> position = root;

        for (char c : stringRepresentation.toCharArray()) {
            if (c == '(')       // create new node and position goes down
            {
                position = new Node<T>(position, null);
            } else if (c == ')')  // position goes up
            {
                position = position.getParent();
            } else if (c == '.') // add a node, position stays same
            {
                new Node<T>(position, null);
            }
        }

        // get post order pointers, set the index and label the nodes
        ArrayList<Node<T>> postOrderNodes = getPostOrderEnumeration(root);
        assert postOrderLabels.size() == postOrderNodes.size();
        for (int i = 0; i != postOrderNodes.size(); ++i)
        {
            postOrderNodes.get(i).setIndex(i);
            postOrderNodes.get(i).setLabel(postOrderLabels.get(i));
        }


        // compute the leftmost descendants
        leftmostDescendants = new int[postOrderNodes.size()];
        for (int index = 0; index != postOrderNodes.size(); ++index) {
            position = postOrderNodes.get(index);
            while(position.getChildren().size() != 0)
            {
                position = position.getChildren().get(0);
            }
            leftmostDescendants[index] = position.getIndex();
        }

        // compute the keyRoots
        Hashtable<Integer, Integer> nodeToLeftmostDescendant = new Hashtable<>();
        Integer lmd;
        for (int node_index = postOrderNodes.size() - 1; node_index != -1; --node_index) {
            lmd = leftmostDescendants[node_index];

            if (!(nodeToLeftmostDescendant.containsKey(lmd))) {
                nodeToLeftmostDescendant.put(lmd, node_index);
            }
        }

        keyRoots = new int[nodeToLeftmostDescendant.size()];
        Enumeration<Integer> keyrootsEnum = nodeToLeftmostDescendant.elements();
        int index = 0;
        while (keyrootsEnum.hasMoreElements()) {
            keyRoots[index] = keyrootsEnum.nextElement();
            index += 1;
        }
        java.util.Arrays.sort(keyRoots);
    }





    /**
     * recursive helper to go through the nodes and add them to array
     * during a postorder traversal
     *
     * @param node  current node in the traversal
     * @param array contains the nodes in postorder
     */
    private void postorderHelper(Node<T> node, ArrayList<Node<T>> array) {
        if (node != null) {
            for (Node<T> n : node.getChildren()) {
                postorderHelper(n, array);
            }
            array.add(node);
        }
    }


    /**
     * get list of node pointers to the postorder enumeration
     *
     * @param root root node of the tree
     * @return an arraylist of the post-order traversal of the tree
     */
    private ArrayList<Node<T>> getPostOrderEnumeration(Node<T> root) {
        ArrayList<Node<T>> postOrderEnum = new ArrayList<>();
        postorderHelper(root, postOrderEnum);
        return postOrderEnum;
    }

    public Node getRoot() {
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


    //TODO figure out how the meaning of toString for this data structure...
    /*
    public String toString()
    {
        ArrayList<Character> symbolList = new ArrayList<>();
        for (Node position : root.getChildren())
        {
            print_helper(position, symbolList);
        }
        StringBuilder builder = new StringBuilder(symbolList.size());
        for (Character c  : symbolList)
        {
            builder.append(c);
        }
        return builder.toString();
    }
    */



}