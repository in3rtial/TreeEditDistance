package ted.core.distances;


import ted.core.interfaces.CostFunction;
import ted.core.interfaces.DistanceFunction;
import ted.core.repr.OrderedLabeledTree;


/**
 * Tree Edit Distance on Ordered Rooted Trees
 *
 * @param <T> type of the label of the Ordered Rooted Tree.
 */
public class TreeEditDistance<T> implements DistanceFunction<OrderedLabeledTree<T>,
        OrderedLabeledTree<T>> {

    private CostFunction<T> deletionCost, insertionCost;
    private DistanceFunction<T, T> substitutionCost;


    /**
     * Tree Edit Distance constructor with functions that calculate cost of
     * deletion, insertion and relabeling on labels.
     *
     * @param insertionCost    scoring function for insertions
     * @param deletionCost     scoring function for deletions
     * @param substitutionCost scoring function for label replacement (to -> from)
     */
    public TreeEditDistance(CostFunction<T> insertionCost,
                            CostFunction<T> deletionCost,
                            DistanceFunction<T, T> substitutionCost) {
        this.insertionCost = insertionCost;
        this.deletionCost = deletionCost;
        this.substitutionCost = substitutionCost;
    }


    /**
     * Performs the calculation of the tree edit distance on two subtrees, given
     * post-order indices of the subtrees. Fills the dynamic programming table at the suitable index.
     *
     * @param tree1                   first tree to compare
     * @param tree2                   second tree to compare
     * @param index1                  postorder index of the keyroot in the first tree
     * @param index2                  postorder index of the keyroot in the second tree
     * @param dynamicProgrammingTable dynamic programming table to modify
     */
    private void treeEditDistanceHelper(OrderedLabeledTree<T> tree1,
                                        OrderedLabeledTree<T> tree2,
                                        int index1, int index2,
                                        double[][] dynamicProgrammingTable) {
        //region Initialization
        int[] FirstLMDS = tree1.getLeftmostDescendants();
        int[] SecondLMDS = tree2.getLeftmostDescendants();
        int p, q;

        int m = index1 - FirstLMDS[index1] + 2;
        int n = index2 - SecondLMDS[index2] + 2;

        // create a m x n matrix of zeros
        double[][] forestDistance = new double[m][n];

        // figure out the offset
        int iOffset = FirstLMDS[index1] - 1;
        int jOffset = SecondLMDS[index2] - 1;
        //endregion

        //region Distance Calculations
        // fill deletions (tree1 row) and insertions (tree1 column)
        for (int x = 1; x != m; ++x) {
            forestDistance[x][0] = forestDistance[x - 1][0] + 1;
        }
        for (int y = 1; y != n; ++y) {
            forestDistance[0][y] = forestDistance[0][y - 1] + 1;
        }


        // fill the rest of the matrix
        for (int x = 1; x != m; ++x) {
            for (int y = 1; y != n; ++y) {
                // some situation independent data
                T label1 = tree1.getPostOrderLabels().get((x + iOffset));
                T label2 = tree2.getPostOrderLabels().get((y + jOffset));

                double del = deletionCost.getCost(label1);
                double insert = insertionCost.getCost(label2);

                // case 1
                // x is an ancestor of i and y is an ancestor of j
                if ((FirstLMDS[index1] == FirstLMDS[x + iOffset]) &&
                        (SecondLMDS[index2] == SecondLMDS[y + jOffset])) {
                    double sub = substitutionCost.getDistance(label1, label2);
                    forestDistance[x][y] = Math.min(Math.min(
                                    // deletion
                                    (forestDistance[x - 1][y] + del),
                                    // insertion
                                    (forestDistance[x][y - 1] + insert)),
                            // substitution
                            (forestDistance[x - 1][y - 1] + sub)
                    );

                    dynamicProgrammingTable[x + iOffset][y + jOffset] = forestDistance[x][y];
                }
                // case 2
                else {
                    p = FirstLMDS[x + iOffset] - 1 - iOffset;
                    q = SecondLMDS[y + jOffset] - 1 - jOffset;
                    forestDistance[x][y] = Math.min(Math.min(
                                    // deletion
                                    (forestDistance[x - 1][y] + del),
                                    // insertion
                                    (forestDistance[x][y - 1] + insert)),
                            // substitution
                            (forestDistance[p][q] + dynamicProgrammingTable[x + iOffset][y + jOffset])
                    );
                }
            }
        }
        //endregion
    }


    /**
     * Performs the tree edit distance calculation between two Ordered Labeled Trees.
     *
     * @param tree1 first whole tree to compare
     * @param tree2 second whole tree to compare
     * @return dynamic programming table (can be interpreted for matches, or just get the distance by
     * looking at the last cell on the lower right.
     */
    public double[][] treeEditDistance(OrderedLabeledTree<T> tree1,
                                       OrderedLabeledTree<T> tree2) {
        int sizeTree1 = tree1.getPostOrderLabels().size();
        int sizeTree2 = tree2.getPostOrderLabels().size();

        // create the matrix holding tree cost
        double[][] treeDistances = new double[sizeTree1][sizeTree2];


        int[] keyRoots1 = tree1.getKeyRoots();
        int[] keyRoots2 = tree2.getKeyRoots();
        int keyRoot1, keyRoot2;
        for (int i = 0; i != keyRoots1.length; ++i) {
            keyRoot1 = keyRoots1[i];
            for (int j = 0; j != keyRoots2.length; ++j) {
                keyRoot2 = keyRoots2[j];
                treeEditDistanceHelper(tree1, tree2, keyRoot1, keyRoot2, treeDistances);
            }
        }
        return treeDistances;
    }


    /**
     * Computes the tree edit distance between two trees, according to the
     * cost functions specified in the tree edit distance declaration.
     *
     * @param tree1 first tree
     * @param tree2 second tree
     * @return tree edit distance between the two trees
     */
    public double getDistance(OrderedLabeledTree<T> tree1,
                              OrderedLabeledTree<T> tree2) {
        double[][] dynamicProgrammingTable = treeEditDistance(tree1, tree2);
        return dynamicProgrammingTable[dynamicProgrammingTable.length - 1][dynamicProgrammingTable[0].length - 1];
    }


}
