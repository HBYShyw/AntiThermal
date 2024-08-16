package com.oplus.datanormalization;

/* loaded from: classes.dex */
public enum Type {
    INTERNAL("internal"),
    INTERNAL_PENDING("internal_pending"),
    EXTERNAL("external");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.type;
    }
}
