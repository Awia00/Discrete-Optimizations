package TSP.Set;

import java.util.Arrays;
import java.util.Collection;

public interface Set<T> extends Iterable<T>  {
    boolean isEmpty();

    default boolean isProperSubsetOf(Set<T> other) {
        return size() < other.size() && isSubsetOf(other);
    }

    boolean isSubsetOf(Set<T> other);

    boolean contains(T element);

    int size();

    Set<T> add(T element);

    Set<T> remove(T element);

    Set<T> union(Set<T> other);

    Set<T> intersect(Set<T> other);

    Set<T> minus(Set<T> other);

    static <T> Set<T> empty() {
        return EmptySet.instance();
    }

    static <T> Set<T> of(T element) {
        return new ImmutableSet<>(element);
    }

    static <T> Set<T> of(T... elements) {
        if (elements.length == 0) return empty();
        return new ImmutableSet<>(Arrays.asList(elements));
    }

    static <T> Set<T> of(Collection<T> elements) {
        if (elements.isEmpty()) return empty();
        return new ImmutableSet<>(elements);
    }

    static <T extends Comparable<T>> Iterable<Set<T>> subsetsOfSizeAtMost(Set<T> elements, int maxSize) {
        return new SubsetOfAtMostSizeIterable<>(elements, maxSize);
    }
}
