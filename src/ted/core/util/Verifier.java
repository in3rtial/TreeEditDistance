package ted.core.util;


/**
 * Holds input verification methods.
 */
public final class Verifier {


    /**
     * Verifies that the bracket given is balanced and contains no illegal symbols.
     * @param brackets bracket notation of the tree e.g. (()())
     * @return true if the tree is balanced and conrtains no illegal symbols, false otherwise.
     */
    public static boolean isValidTreeStructure(String brackets)
    {
        int counter = 0;
        for (char c : brackets.toCharArray())
        {
            if (c == '(')      // stack
            {
                counter += 1;
            }
            else if (c == ')') // unstack
            {
                counter -= 1;
            }
            else               // illegal character
            {
                return false;
            }
            if (counter < 0)     // left unbalanced
            {
                return false;
            }
        }
        return counter == 0;
    }
}
