package org.iguana.utils.collections.key;

import java.util.Arrays;

public class IntArrayKey implements Key {

    private final int i;
    private final Object[] objects;

    public IntArrayKey(int i, Object[] objects) {
        this.i = i;
        this.objects = objects;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IntArrayKey)) return false;

        IntArrayKey other = (IntArrayKey) obj;
        return i == other.i && Arrays.deepEquals(objects, other.objects);
    }

    @Override
    public String toString() {
        return String.format("(%d, %s)", i, Arrays.deepToString(objects));
    }

    @Override
    public int hashCode() {
        return i * 31 + Arrays.deepHashCode(objects);
    }
}
