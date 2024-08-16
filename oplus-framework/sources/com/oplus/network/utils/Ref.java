package com.oplus.network.utils;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Ref {

    /* loaded from: classes.dex */
    public static class Default {
        public boolean update = false;
    }

    private Ref() {
    }

    /* loaded from: classes.dex */
    public static final class ObjectRef<T> extends Default implements Serializable {
        public T element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class ByteRef extends Default implements Serializable {
        public byte element;

        public String toString() {
            return String.valueOf((int) this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class ShortRef extends Default implements Serializable {
        public short element;

        public String toString() {
            return String.valueOf((int) this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class IntRef extends Default implements Serializable {
        public int element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class LongRef extends Default implements Serializable {
        public long element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class FloatRef extends Default implements Serializable {
        public float element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class DoubleRef extends Default implements Serializable {
        public double element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class CharRef extends Default implements Serializable {
        public char element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }

    /* loaded from: classes.dex */
    public static final class BooleanRef extends Default implements Serializable {
        public boolean element;

        public String toString() {
            return String.valueOf(this.element);
        }
    }
}
