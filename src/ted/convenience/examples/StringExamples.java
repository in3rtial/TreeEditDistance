package ted.convenience.examples;

import ted.core.distances.TreeEditDistance;
import ted.core.interfaces.CostFunction;
import ted.core.interfaces.DistanceFunction;
import ted.convenience.StringTree;

public final class StringExamples {




    /**
     * Initialization of a tree from brackets notation.
     */
    public static void Example1()
    {
        System.out.println("Example 1");

        // instantiate the tree
        StringTree tree = new StringTree("{ab{c}{d}{e}}");

        // if it works well, the string representation should be equal to the input
        System.out.println(tree);
        System.out.println();
    }


    /**
     * Initialization and use of the tree edit distance
     */
    public static void Example2()
    {
        // we need to implement some simple distance functions
        class UnitCost implements CostFunction<String>
        {
            @Override
            public double getCost(String label) {
                return 1.;
            }
        }

        class EqualDistanceCost implements DistanceFunction<String, String>
        {

            @Override
            public double getDistance(String label1, String label2) {
                // to be used with unit cost, a substitution becomes equivalent to
                // a delete and insert...
                return label1.compareTo(label2) == 0 ? 0. : 2.;
            }
        }

        // initialize the simple cost functions on labels
        UnitCost deleteCost = new UnitCost();
        UnitCost insertCost = new UnitCost();
        EqualDistanceCost substitutionCost = new EqualDistanceCost();

        // initialize the tree edit distance function from the simple cost functions on labels
        DistanceFunction distanceFunction = new TreeEditDistance<>(insertCost, deleteCost, substitutionCost);

        // initialize the trees to be compared
        StringTree tree1 = new StringTree("{f{d{a}{c{b}}}{e}}");
        StringTree tree2 = new StringTree("{f{c{d{a}{b}}}{e}}");

        // verify the values
        // should be 0, dist(x, x) = 0
        System.out.println("Distance from tree1 to tree1 = " + distanceFunction.getDistance(tree1, tree1));

        // should be 2, can remove C and add insert it back on the left of B
        System.out.println("Distance from tree1 to tree2 = " + distanceFunction.getDistance(tree1, tree2));

    }




    public static void main(String[] args)
    {
        Example1();

        Example2();
    }
}
