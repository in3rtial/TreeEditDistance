package ted.convenience;

import ted.core.repr.Node;
import ted.core.repr.OrderedLabeledTree;

import java.util.ArrayList;

public class StringTree extends OrderedLabeledTree<String> {

    /**
     * Builder for the
     * @param brackets
     * @return
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
     *
     * @param brackets
     * @return
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

    private static void printHelper(Node<String> position, StringBuilder symbols)
    {
        //
        symbols.append("{");
        symbols.append(position.getLabel());
        for(Node<String> child : position.getChildren())
        {
            printHelper(child, symbols);
        }
        symbols.append("}");
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        printHelper(getRoot(), builder);
        return builder.toString();
    }
}
