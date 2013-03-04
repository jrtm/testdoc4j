package no.uio.tools.testdoc.util;

import java.util.Comparator;
import java.util.Map;

public class MethodOrderComparator<T extends MethodWrapper> implements Comparator<T> {

    private Map<String, Integer> methodOrders;


    public MethodOrderComparator(final Class<?> clazz) {
        methodOrders = MethodIndexer.getMethodOrders(clazz, JavapMethodLister.getInstance());
    }


    public boolean canSort() {
        return !methodOrders.isEmpty();
    }


    public int compare(final T a, final T b) {
        Integer orderA = methodOrders.get(a.getMethodName());
        Integer orderB = methodOrders.get(b.getMethodName());
        if (orderA == null || orderB == null) {
            return 0;
        }
        return orderA - orderB;
    }

}
