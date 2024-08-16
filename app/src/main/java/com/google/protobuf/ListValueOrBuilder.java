package com.google.protobuf;

import java.util.List;

/* loaded from: classes.dex */
public interface ListValueOrBuilder extends MessageOrBuilder {
    Value getValues(int i10);

    int getValuesCount();

    List<Value> getValuesList();

    ValueOrBuilder getValuesOrBuilder(int i10);

    List<? extends ValueOrBuilder> getValuesOrBuilderList();
}
