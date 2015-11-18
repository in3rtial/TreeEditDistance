package ted.convenience;

import ted.core.distances.TreeEditDistance;
import ted.core.interfaces.CostFunction;
import ted.core.interfaces.DistanceFunction;

public class TED {
    // we need to implement some simple distance functions
    class UnitCost implements CostFunction<String>
    {
        double cost;
        UnitCost(double cost)
        {
            this.cost = cost;
        }

        @Override
        public double getCost(String label) {
            return cost;
        }
    }

    class EqualDistanceCost implements DistanceFunction<String, String>
    {
        double equalCost;
        double notEqualCost;
        EqualDistanceCost(double equalCost, double notEqualCost)
        {
            this.equalCost = equalCost;
            this.notEqualCost = notEqualCost;
        }

        @Override
        public double getDistance(String label1, String label2) {
            // to be used with unit cost, a substitution becomes equivalent to
            // a delete and insert...
            return label1.compareTo(label2) == 0 ? equalCost : notEqualCost;
        }
    }


    public double computeDistance(String bracket1, String bracket2)
    {
        TreeEditDistance<String> dist = new TreeEditDistance<>(new UnitCost(1), new UnitCost(1), new EqualDistanceCost(0, 2));
        return dist.getDistance(new StringTree(bracket1), new StringTree(bracket2));
    }


    public double computeDistance(String bracket1, String bracket2,
                                  CostFunction<String> insertCost,
                                  CostFunction<String> deleteCost,
                                  DistanceFunction<String, String> subCost)
    {
        TreeEditDistance<String> dist = new TreeEditDistance<>(insertCost, deleteCost, subCost);
        return dist.getDistance(new StringTree(bracket1), new StringTree(bracket2));
    }
}
