package vinted.common;

import java.util.Objects;

public class Tuple<T, U> {

    private final T first;
    private final U second;

    public Tuple(T first, U second) {
        this.first = Objects.requireNonNull(first);
        this.second = Objects.requireNonNull(second);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Tuple<?, ?> tuple = (Tuple<?, ?>) other;
        return Objects.equals(first, tuple.first) && Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
