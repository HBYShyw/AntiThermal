package com.oplus.cardwidget.proto;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;

/* loaded from: classes.dex */
public final class CardAction {
    private static Descriptors.FileDescriptor descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(new String[]{"\n\u0011card_action.proto\u0012\u001acom.oplus.cardwidget.proto\"\u0091\u0002\n\u000fCardActionProto\u0012\u0010\n\bcardType\u0018\u0001 \u0002(\u0005\u0012\u000e\n\u0006cardId\u0018\u0002 \u0002(\u0005\u0012\u000e\n\u0006hostId\u0018\u0003 \u0002(\u0005\u0012\u000e\n\u0006action\u0018\u0004 \u0002(\u0005\u0012E\n\u0005param\u0018\u0005 \u0003(\u000b26.com.oplus.cardwidget.proto.CardActionProto.ParamEntry\u001a,\n\nParamEntry\u0012\u000b\n\u0003key\u0018\u0001 \u0001(\t\u0012\r\n\u0005value\u0018\u0002 \u0001(\t:\u00028\u0001\"G\n\u0006ACTION\u0012\t\n\u0005CLICK\u0010\u0001\u0012\u000f\n\u000bLIFE_CIRCLE\u0010\u0002\u0012\u0011\n\rEXPOSED_STATE\u0010\u0003\u0012\u000e\n\nINVALIDATE\u0010\u0004B\u0002P\u0001"}, new Descriptors.FileDescriptor[0]);
    static final Descriptors.Descriptor internal_static_com_oplus_cardwidget_proto_CardActionProto_ParamEntry_descriptor;
    static final GeneratedMessageV3.FieldAccessorTable internal_static_com_oplus_cardwidget_proto_CardActionProto_ParamEntry_fieldAccessorTable;
    static final Descriptors.Descriptor internal_static_com_oplus_cardwidget_proto_CardActionProto_descriptor;
    static final GeneratedMessageV3.FieldAccessorTable internal_static_com_oplus_cardwidget_proto_CardActionProto_fieldAccessorTable;

    static {
        Descriptors.Descriptor descriptor2 = getDescriptor().getMessageTypes().get(0);
        internal_static_com_oplus_cardwidget_proto_CardActionProto_descriptor = descriptor2;
        internal_static_com_oplus_cardwidget_proto_CardActionProto_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(descriptor2, new String[]{"CardType", "CardId", "HostId", "Action", "Param"});
        Descriptors.Descriptor descriptor3 = descriptor2.getNestedTypes().get(0);
        internal_static_com_oplus_cardwidget_proto_CardActionProto_ParamEntry_descriptor = descriptor3;
        internal_static_com_oplus_cardwidget_proto_CardActionProto_ParamEntry_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(descriptor3, new String[]{"Key", "Value"});
    }

    private CardAction() {
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
