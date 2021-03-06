# Tree Edit Distance

## Summary
TreeEditDistance is a java module that can be used to compute the tree edit distance
between two ordered labeled trees.

It is strongly inspired by Tim Henderson (timtadh) and Steve Johnson (irskep) excellent
zhang-shasha python library (https://github.com/timtadh/zhang-shasha).


## Installation
Just use it in jar format as a library? It has no external dependencies.


## How It Works
Most of the code you need is in the ted.convenience.TED class.
It wraps calls to constructors and distance functions for hopefully the most common tasks.

```java
// initialize the trees to compare
StringTree tree1 = new StringTree("{f{d{a}{c{b}}}{e}}");
StringTree tree2 = new StringTree("{f{c{d{a}{b}}}{e}}");

// using the unit cost function
// (indel = 1, substitution = 2 if labels are not equal, 0 otherwise)
TED.computeDistance(tree1, tree2);
```

If more fancy cost functions are needed, their interface is described in the ted.core.interfaces.
Just define them and build a new tree edit distance object. A small example of this is done in the
ted.convenience.TED class where simple cost functions are defined and tree edit distance is built from
these.

```java
// create a cost function for insertions and deletions
// they all cost 1 for this example
static class UnitCost implements CostFunction<String> {

    @Override
    public double getCost(String label) {
        return 1;
    }
}

// create a cost function for label substitution (relabeling)
static class EqualDistanceCost implements DistanceFunction<String, String> {

    @Override
    public double getDistance(String label1, String label2) {
        // to be used with unit cost, a substitution becomes equivalent to
        // a delete and insert...
        return label1.compareTo(label2) == 0 ? 0. : 2.;
    }
}

// instantiate a new tree edit distance object
CostFunction<String> indelCost = new UnitCost();
DistanceFunction<String, String> substitutionCost = new EqualDistanceCost();
TreeEditDistance<String> distance = new TreeEditDistance<>(indelCost,
                                                           indelCost,
                                                           substitutionCost);
```

