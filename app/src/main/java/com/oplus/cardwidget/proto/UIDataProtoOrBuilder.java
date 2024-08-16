package com.oplus.cardwidget.proto;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageOrBuilder;
import com.oplus.cardwidget.proto.UIDataProto;
import java.util.Map;

/* loaded from: classes.dex */
public interface UIDataProtoOrBuilder extends MessageOrBuilder {
    boolean containsIdMaps(String str);

    int getCardId();

    UIDataProto.DataCompress getCompress();

    String getData();

    ByteString getDataBytes();

    boolean getForceChangeCardUI();

    @Deprecated
    Map<String, Integer> getIdMaps();

    int getIdMapsCount();

    Map<String, Integer> getIdMapsMap();

    int getIdMapsOrDefault(String str, int i10);

    int getIdMapsOrThrow(String str);

    String getLayoutName();

    ByteString getLayoutNameBytes();

    String getName();

    ByteString getNameBytes();

    int getThemeId();

    ByteString getValue();

    long getVersion();

    boolean hasCardId();

    boolean hasCompress();

    boolean hasData();

    boolean hasForceChangeCardUI();

    boolean hasLayoutName();

    boolean hasName();

    boolean hasThemeId();

    boolean hasValue();

    boolean hasVersion();
}
