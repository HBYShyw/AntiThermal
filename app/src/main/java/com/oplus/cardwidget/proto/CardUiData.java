package com.oplus.cardwidget.proto;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;

/* loaded from: classes.dex */
public final class CardUiData {
    private static Descriptors.FileDescriptor descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(new String[]{"\n\u0012card_ui_data.proto\u0012\u001acom.oplus.cardwidget.proto\"\u0089\u0003\n\u000bUIDataProto\u0012\u000e\n\u0006cardId\u0018\u0001 \u0002(\u0005\u0012\f\n\u0004data\u0018\u0002 \u0002(\t\u0012C\n\u0006idMaps\u0018\u0003 \u0003(\u000b23.com.oplus.cardwidget.proto.UIDataProto.IdMapsEntry\u0012\f\n\u0004name\u0018\u0004 \u0001(\t\u0012\u000f\n\u0007version\u0018\u0005 \u0001(\u0003\u0012\u000f\n\u0007themeId\u0018\u0006 \u0001(\u0005\u0012\r\n\u0005value\u0018\u0007 \u0001(\f\u0012\u0019\n\u0011forceChangeCardUI\u0018\b \u0001(\b\u0012F\n\bcompress\u0018\t \u0001(\u000e24.com.oplus.cardwidget.proto.UIDataProto.DataCompress\u0012\u0012\n\nlayoutName\u0018\n \u0001(\t\u001a-\n\u000bIdMapsEntry\u0012\u000b\n\u0003key\u0018\u0001 \u0001(\t\u0012\r\n\u0005value\u0018\u0002 \u0001(\u0005:\u00028\u0001\"2\n\fDataCompress\u0012\b\n\u0004NONE\u0010\u0000\u0012\n\n\u0006FLATER\u0010\u0001\u0012\f\n\bSIMPLIFY\u0010\u0002B\u0002P\u0001"}, new Descriptors.FileDescriptor[0]);
    static final Descriptors.Descriptor internal_static_com_oplus_cardwidget_proto_UIDataProto_IdMapsEntry_descriptor;
    static final GeneratedMessageV3.FieldAccessorTable internal_static_com_oplus_cardwidget_proto_UIDataProto_IdMapsEntry_fieldAccessorTable;
    static final Descriptors.Descriptor internal_static_com_oplus_cardwidget_proto_UIDataProto_descriptor;
    static final GeneratedMessageV3.FieldAccessorTable internal_static_com_oplus_cardwidget_proto_UIDataProto_fieldAccessorTable;

    static {
        Descriptors.Descriptor descriptor2 = getDescriptor().getMessageTypes().get(0);
        internal_static_com_oplus_cardwidget_proto_UIDataProto_descriptor = descriptor2;
        internal_static_com_oplus_cardwidget_proto_UIDataProto_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(descriptor2, new String[]{"CardId", "Data", "IdMaps", "Name", "Version", "ThemeId", "Value", "ForceChangeCardUI", "Compress", "LayoutName"});
        Descriptors.Descriptor descriptor3 = descriptor2.getNestedTypes().get(0);
        internal_static_com_oplus_cardwidget_proto_UIDataProto_IdMapsEntry_descriptor = descriptor3;
        internal_static_com_oplus_cardwidget_proto_UIDataProto_IdMapsEntry_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(descriptor3, new String[]{"Key", "Value"});
    }

    private CardUiData() {
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    public static void registerAllExtensions(ExtensionRegistry extensionRegistry) {
        registerAllExtensions((ExtensionRegistryLite) extensionRegistry);
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }
}
