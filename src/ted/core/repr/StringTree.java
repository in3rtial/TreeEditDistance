package ted.core.repr;

import java.util.ArrayList;

public class StringTree extends OrderedLabeledTree<String> {

    /**
     * String Tree is the Ordered Labeled Tree with String labels.
     * It is expected to be the most used interface for tree edit distance calculation.
     * It also allows us to add a simple toString method.
     * @param brackets bracket notation of the tree e.g. "{a{b}{c}}"
     * @return a String Tree instance corresponding to the bracket notation
     */
    public StringTree(String brackets)
    {
        super(extractTopology(brackets), extractPreOrderLabels(brackets));
    }


    /**
     * Extracts the topology by removing the labels from the bracket string
     * and converting { -> ( and } -> )
     * @param brackets bracket representation of a tree e.g. {a {b}{c}}
     * @return the topology of the brackets e.g. {a {b}{c}} would be (()())
     */
    public static String extractTopology(String brackets)
    {
        StringBuilder bracketsBuilder = new StringBuilder();
        for (char c : brackets.toCharArray())
        {
            if (c == '{' || c == '}')
            {
                bracketsBuilder.append(c);
            }
        }

        return  bracketsBuilder.toString().replace("{", "(").replace("}", ")");
    }


    /**
     * Reads the brackets and extracts the labels, in pre-order traversal order.
     * @param brackets bracket notation of the tree e.g. "{a{b}{c}}"
     * @return the labels of the tree, in pre-order traversal order
     */
    public static ArrayList<String> extractPreOrderLabels(String brackets)
    {
        ArrayList<String> postOrderLabels = new ArrayList<>();
        char[] bracketsChars = brackets.toCharArray();
        int i = 0;
        while (i < bracketsChars.length)
        {
            char c = bracketsChars[i];
            if (c == '{')
            {
                // step
                i+=1;
                c = bracketsChars[i];

                // read the label
                StringBuilder builder = new StringBuilder();
                while(c != '{' && c != '}'  && i < bracketsChars.length)
                {
                    builder.append(c);
                    i+=1;
                    c = bracketsChars[i];
                }
                postOrderLabels.add(builder.toString());
            }
            else
            {
                i+=1;
            }
        }
        return postOrderLabels;
    }

    /**
     * Helper for the toString function, called recursively mostly. It is tasked with filling the labels StringBuilder.
     * @param position node position in the tree, usually called on the root, and it calls itself on the subtrees.
     * @param brackets the brackets notation of the String Tree
     */
    private static void toStringHelper(Node<String> position, StringBuilder brackets)
    {
        //
        brackets.append("{");
        brackets.append(position.getLabel());
        for(Node<String> child : position.getChildren())
        {
            toStringHelper(child, brackets);
        }
        brackets.append("}");
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        toStringHelper(getRoot(), builder);
        return builder.toString();
    }
}
