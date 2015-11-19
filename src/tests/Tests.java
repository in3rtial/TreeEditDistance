package tests;

import ted.core.distances.TreeEditDistance;
import ted.core.interfaces.CostFunction;
import ted.core.interfaces.DistanceFunction;

import ted.core.repr.StringTree;

public final class Tests {

    static boolean TestConstructor()
    {
        String first = "{f{d{a}{c{b}}}{e}}";
        String second = "{f{c{d{a}{b}}}{e}}";
        StringTree tree1 = new StringTree(first);
        StringTree tree2 = new StringTree(second);


        System.out.println("representation test");
        System.out.println(first.compareTo(tree1.toString()) == 0);
        System.out.println(second.compareTo(tree2.toString())== 0);

        // see the internal structures
        System.out.println("keyroots");
        System.out.println("first tree");
        for(int i : tree1.getKeyRoots())
        {
            System.out.println(i);
        }

        System.out.println("second tree");
        for(int i : tree2.getKeyRoots())
        {
            System.out.println(i);
        }


        System.out.println("lmds");
        for (int i : tree1.getLeftmostDescendants())
        {
            System.out.println(i);
        }

        return true;
    }


    // testing the distance functions
    private static class unitCost implements CostFunction<String>
    {
        @Override
        public double getCost(String label) {
            return 1.;
        }
    }

    private static class equalDistance implements DistanceFunction<String, String>
    {

        @Override
        public double getDistance(String label1, String label2) {
            return label1.compareTo(label2) == 0 ? 0. : 2.;
        }
    }

    static boolean TestDistance()
    {

        DistanceFunction f1 = new TreeEditDistance<>(new unitCost(), new unitCost(), new equalDistance());
        StringTree tree1 = new StringTree("{f{d{a}{c{b}}}{e}}");
        StringTree tree2 = new StringTree("{f{c{d{a}{b}}}{e}}");

        System.out.println(f1.getDistance(tree1, tree2));
        System.out.println(f1.getDistance(tree1, tree1));

        return true;
    }


    public static void main(String[] args)
    {
        TestConstructor();

        TestDistance();
        System.out.println("done");

    }
}
