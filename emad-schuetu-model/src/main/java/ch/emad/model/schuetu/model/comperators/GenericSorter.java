/**
 * Apache License 2.0
 */
package ch.emad.model.schuetu.model.comperators;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Generischer sortierer
 *
 * @author $Author: marthaler.worb@gmail.com $
 * @since 0.7
 */
public class GenericSorter {

    private static final Logger LOG = Logger.getLogger(GenericSorter.class);

    public static <T> void sortAsc(List<T> list, String methodName) {
        Comparator<T> byNameComparator = null;
        try {
            byNameComparator = (Comparator<T>) newMethodComparator(list.get(0).getClass(), methodName);
        } catch (Exception e) {
            LOG.fatal(e.getMessage());
        }
        Collections.sort(list, byNameComparator);
    }

    public static <T> void sortDesc(List<T> list, String methodName) {
        sortAsc(list, methodName);
        Collections.reverse(list);
    }


    private static <T> Comparator<T> newMethodComparator(Class<T> cls, String methodName) throws Exception {
        Method method = cls.getMethod(methodName);
        if (method.getParameterTypes().length != 0)
            throw new Exception("Method " + method + " takes parameters");

        Class<?> returnType = method.getReturnType();
        if (!Comparable.class.isAssignableFrom(returnType))
            throw new Exception("The return type " + returnType + " is not Comparable");

        return newMethodComparator(method, (Class<? extends Comparable>) returnType);
    }

    private static <T, R extends Comparable<R>> Comparator<T> newMethodComparator(
            final Method method, final Class<R> returnType) throws Exception {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                try {
                    R a = invoke(method, o1);
                    R b = invoke(method, o2);
                    return a.compareTo(b);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private R invoke(Method method, T o) throws Exception {
                return returnType.cast(method.invoke(o));
            }
        };
    }
}
