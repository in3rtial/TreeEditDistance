package ted.core.interfaces;

/**
 * binary cost function...
 * @param <T1>
 * @param <T2>
 */
public interface DistanceFunction<T1, T2> {

    double getDistance(T1 label1, T2 label2);
}


