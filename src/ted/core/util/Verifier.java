package ted.core.util;

public final class Verifier {
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
