package ted.core.interfaces;

/**
 * Unary cost function
 * @param <T> type of the label
 */
public interface CostFunction<T> {

    double getCost(T label);
}

