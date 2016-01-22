package ted.convenience;

import ted.core.distances.TreeEditDistance;
import ted.core.interfaces.CostFunction;
import ted.core.interfaces.DistanceFunction;
import ted.core.repr.OrderedLabeledTree;
import ted.core.repr.StringTree;

import java.util.ArrayList;


/**
 * Easy interface to the TED library, includes many levels of calls for convenience.
 * Most of the calls should be done through this class.
 */
public final class TED {

    //region Distance Functions (just as example, define your owns if you need to)
    // we need to implement some simple distance functions
    static class UnitCost implements CostFunction<String> {

        @Override
        public double getCost(String label) {
            return 1;
        }
    }

    static class EqualDistanceCost implements DistanceFunction<String, String> {

        @Override
        public double getDistance(String label1, String label2) {
            // to be used with unit cost, a substitution becomes equivalent to
            // a delete and insert...
            return label1.compareTo(label2) == 0 ? 0. : 2.;
        }
    }
    //endregion


    //region Called From String

    /**
     * Compute the distance between two trees in bracket form (RTED format)
     * @param bracket1 first tree in bracket format
     * @param bracket2 second tree in bracket format
     * @return tree edit distance between the two
     */
    public static double computeDistance(String bracket1, String bracket2) {
        return computeDistance(new StringTree(bracket1), new StringTree(bracket2));
    }


    public static double computeDistance(String bracket1, String bracket2,
                                  CostFunction<String> insertCost,
                                  CostFunction<String> deleteCost,
                                  DistanceFunction<String, String> subCost) {

        return computeDistance(new StringTree(bracket1), new StringTree(bracket2), new TreeEditDistance<>(insertCost, deleteCost, subCost));
    }


    public static double computeDistance(String bracket1, String bracket2, TreeEditDistance<String> dist) {
        return computeDistance(new StringTree(bracket1), new StringTree(bracket2), dist);
    }
    //endregion


    //region Called From StringTree
    public static double computeDistance(StringTree tree1, StringTree tree2) {
        return computeDistance(tree1, tree2, new UnitCost(), new UnitCost(), new EqualDistanceCost());
    }


    public static double computeDistance(StringTree tree1, StringTree tree2,
                                  CostFunction<String> insertCost,
                                  CostFunction<String> deleteCost,
                                  DistanceFunction<String, String> subCost) {
        return computeDistance(tree1, tree2, new TreeEditDistance<>(insertCost, deleteCost, subCost));
    }

    public static double computeDistance(StringTree tree1, StringTree tree2, TreeEditDistance<String> dist) {
        return dist.getDistance(tree1, tree2);
    }
    //endregion


    //region Called From Ordered Labeled Tree
    // this is mostly for internal use
    public static <T> double computeDistance(String brackets1, ArrayList<T> preOrderLabels1,
                                      String brackets2, ArrayList<T> preOrderLabels2,
                                      CostFunction<T> insertCost,
                                      CostFunction<T> deleteCost,
                                      DistanceFunction<T, T> subCost) {
        return computeDistance(new OrderedLabeledTree<>(brackets1, preOrderLabels1),
                new OrderedLabeledTree<T>(brackets2, preOrderLabels2),
                insertCost, deleteCost, subCost);
    }

    public static <T> double computeDistance(OrderedLabeledTree<T> tree1, OrderedLabeledTree<T> tree2,
                                      CostFunction<T> insertCost, CostFunction<T> deleteCost,
                                      DistanceFunction<T, T> subCost) {
        return computeDistance(tree1, tree2, new TreeEditDistance<T>(insertCost, deleteCost, subCost));
    }


    public static <T> double computeDistance(OrderedLabeledTree<T> tree1, OrderedLabeledTree<T> tree2,
                                      TreeEditDistance<T> distanceFunction) {
        return distanceFunction.getDistance(tree1, tree2);
    }

    //endregion

}
