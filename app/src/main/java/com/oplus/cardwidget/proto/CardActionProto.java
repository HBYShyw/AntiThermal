package com.oplus.cardwidget.proto;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MapEntry;
import com.google.protobuf.MapField;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

/* loaded from: classes.dex */
public final class CardActionProto extends GeneratedMessageV3 implements CardActionProtoOrBuilder {
    public static final int ACTION_FIELD_NUMBER = 4;
    public static final int CARDID_FIELD_NUMBER = 2;
    public static final int CARDTYPE_FIELD_NUMBER = 1;
    public static final int HOSTID_FIELD_NUMBER = 3;
    public static final int PARAM_FIELD_NUMBER = 5;
    private static final long serialVersionUID = 0;
    private int action_;
    private int bitField0_;
    private int cardId_;
    private int cardType_;
    private int hostId_;
    private byte memoizedIsInitialized;
    private MapField<String, String> param_;
    private static final CardActionProto DEFAULT_INSTANCE = new CardActionProto();

    @Deprecated
    public static final Parser<CardActionProto> PARSER = new AbstractParser<CardActionProto>() { // from class: com.oplus.cardwidget.proto.CardActionProto.1
        @Override // com.google.protobuf.Parser
        public CardActionProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return new CardActionProto(codedInputStream, extensionRegistryLite);
        }
    };

    /* loaded from: classes.dex */
    public enum ACTION implements ProtocolMessageEnum {
        CLICK(1),
        LIFE_CIRCLE(2),
        EXPOSED_STATE(3),
        INVALIDATE(4);

        public static final int CLICK_VALUE = 1;
        public static final int EXPOSED_STATE_VALUE = 3;
        public static final int INVALIDATE_VALUE = 4;
        public static final int LIFE_CIRCLE_VALUE = 2;
        private final int value;
        private static final Internal.EnumLiteMap<ACTION> internalValueMap = new Internal.EnumLiteMap<ACTION>() { // from class: com.oplus.cardwidget.proto.CardActionProto.ACTION.1
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public ACTION findValueByNumber(int i10) {
                return ACTION.forNumber(i10);
            }
        };
        private static final ACTION[] VALUES = values();

        ACTION(int i10) {
            this.value = i10;
        }

        public static ACTION forNumber(int i10) {
            if (i10 == 1) {
                return CLICK;
            }
            if (i10 == 2) {
                return LIFE_CIRCLE;
            }
            if (i10 == 3) {
                return EXPOSED_STATE;
            }
            if (i10 != 4) {
                return null;
            }
            return INVALIDATE;
        }

        public static final Descriptors.EnumDescriptor getDescriptor() {
            return CardActionProto.getDescriptor().getEnumTypes().get(0);
        }

        public static Internal.EnumLiteMap<ACTION> internalGetValueMap() {
            return internalValueMap;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
        }

        @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
        public final int getNumber() {
            return this.value;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return getDescriptor().getValues().get(ordinal());
        }

        @Deprecated
        public static ACTION valueOf(int i10) {
            return forNumber(i10);
        }

        public static ACTION valueOf(Descriptors.EnumValueDescriptor enumValueDescriptor) {
            if (enumValueDescriptor.getType() == getDescriptor()) {
                return VALUES[enumValueDescriptor.getIndex()];
            }
            throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ParamDefaultEntryHolder {
        static final MapEntry<String, String> defaultEntry;

        static {
            Descriptors.Descriptor descriptor = CardAction.internal_static_com_oplus_cardwidget_proto_CardActionProto_ParamEntry_descriptor;
            WireFormat.FieldType fieldType = WireFormat.FieldType.STRING;
            defaultEntry = MapEntry.newDefaultInstance(descriptor, fieldType, "", fieldType, "");
        }

        private ParamDefaultEntryHolder() {
        }
    }

    public static CardActionProto getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static final Descriptors.Descriptor getDescriptor() {
        return CardAction.internal_static_com_oplus_cardwidget_proto_CardActionProto_descriptor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MapField<String, String> internalGetParam() {
        MapField<String, String> mapField = this.param_;
        return mapField == null ? MapField.emptyMapField(ParamDefaultEntryHolder.defaultEntry) : mapField;
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static CardActionProto parseDelimitedFrom(InputStream inputStream) {
        return (CardActionProto) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
    }

    public static CardActionProto parseFrom(ByteBuffer byteBuffer) {
        return PARSER.parseFrom(byteBuffer);
    }

    public static Parser<CardActionProto> parser() {
        return PARSER;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public boolean containsParam(String str) {
        Objects.requireNonNull(str);
        return internalGetParam().getMap().containsKey(str);
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CardActionProto)) {
            return super.equals(obj);
        }
        CardActionProto cardActionProto = (CardActionProto) obj;
        if (hasCardType() != cardActionProto.hasCardType()) {
            return false;
        }
        if ((hasCardType() && getCardType() != cardActionProto.getCardType()) || hasCardId() != cardActionProto.hasCardId()) {
            return false;
        }
        if ((hasCardId() && getCardId() != cardActionProto.getCardId()) || hasHostId() != cardActionProto.hasHostId()) {
            return false;
        }
        if ((!hasHostId() || getHostId() == cardActionProto.getHostId()) && hasAction() == cardActionProto.hasAction()) {
            return (!hasAction() || getAction() == cardActionProto.getAction()) && internalGetParam().equals(cardActionProto.internalGetParam()) && this.unknownFields.equals(cardActionProto.unknownFields);
        }
        return false;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public int getAction() {
        return this.action_;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public int getCardId() {
        return this.cardId_;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public int getCardType() {
        return this.cardType_;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public int getHostId() {
        return this.hostId_;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    @Deprecated
    public Map<String, String> getParam() {
        return getParamMap();
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public int getParamCount() {
        return internalGetParam().getMap().size();
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public Map<String, String> getParamMap() {
        return internalGetParam().getMap();
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public String getParamOrDefault(String str, String str2) {
        Objects.requireNonNull(str);
        Map<String, String> map = internalGetParam().getMap();
        return map.containsKey(str) ? map.get(str) : str2;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public String getParamOrThrow(String str) {
        Objects.requireNonNull(str);
        Map<String, String> map = internalGetParam().getMap();
        if (map.containsKey(str)) {
            return map.get(str);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Parser<CardActionProto> getParserForType() {
        return PARSER;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public int getSerializedSize() {
        int i10 = this.memoizedSize;
        if (i10 != -1) {
            return i10;
        }
        int computeInt32Size = (this.bitField0_ & 1) != 0 ? 0 + CodedOutputStream.computeInt32Size(1, this.cardType_) : 0;
        if ((this.bitField0_ & 2) != 0) {
            computeInt32Size += CodedOutputStream.computeInt32Size(2, this.cardId_);
        }
        if ((this.bitField0_ & 4) != 0) {
            computeInt32Size += CodedOutputStream.computeInt32Size(3, this.hostId_);
        }
        if ((this.bitField0_ & 8) != 0) {
            computeInt32Size += CodedOutputStream.computeInt32Size(4, this.action_);
        }
        for (Map.Entry<String, String> entry : internalGetParam().getMap().entrySet()) {
            computeInt32Size += CodedOutputStream.computeMessageSize(5, ParamDefaultEntryHolder.defaultEntry.newBuilderForType().setKey(entry.getKey()).setValue(entry.getValue()).build());
        }
        int serializedSize = computeInt32Size + this.unknownFields.getSerializedSize();
        this.memoizedSize = serializedSize;
        return serializedSize;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
    public final UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public boolean hasAction() {
        return (this.bitField0_ & 8) != 0;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public boolean hasCardId() {
        return (this.bitField0_ & 2) != 0;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public boolean hasCardType() {
        return (this.bitField0_ & 1) != 0;
    }

    @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
    public boolean hasHostId() {
        return (this.bitField0_ & 4) != 0;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public int hashCode() {
        int i10 = this.memoizedHashCode;
        if (i10 != 0) {
            return i10;
        }
        int hashCode = 779 + getDescriptor().hashCode();
        if (hasCardType()) {
            hashCode = (((hashCode * 37) + 1) * 53) + getCardType();
        }
        if (hasCardId()) {
            hashCode = (((hashCode * 37) + 2) * 53) + getCardId();
        }
        if (hasHostId()) {
            hashCode = (((hashCode * 37) + 3) * 53) + getHostId();
        }
        if (hasAction()) {
            hashCode = (((hashCode * 37) + 4) * 53) + getAction();
        }
        if (!internalGetParam().getMap().isEmpty()) {
            hashCode = (((hashCode * 37) + 5) * 53) + internalGetParam().hashCode();
        }
        int hashCode2 = (hashCode * 29) + this.unknownFields.hashCode();
        this.memoizedHashCode = hashCode2;
        return hashCode2;
    }

    @Override // com.google.protobuf.GeneratedMessageV3
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return CardAction.internal_static_com_oplus_cardwidget_proto_CardActionProto_fieldAccessorTable.ensureFieldAccessorsInitialized(CardActionProto.class, Builder.class);
    }

    @Override // com.google.protobuf.GeneratedMessageV3
    protected MapField internalGetMapField(int i10) {
        if (i10 == 5) {
            return internalGetParam();
        }
        throw new RuntimeException("Invalid map field number: " + i10);
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
    public final boolean isInitialized() {
        byte b10 = this.memoizedIsInitialized;
        if (b10 == 1) {
            return true;
        }
        if (b10 == 0) {
            return false;
        }
        if (!hasCardType()) {
            this.memoizedIsInitialized = (byte) 0;
            return false;
        }
        if (!hasCardId()) {
            this.memoizedIsInitialized = (byte) 0;
            return false;
        }
        if (!hasHostId()) {
            this.memoizedIsInitialized = (byte) 0;
            return false;
        }
        if (!hasAction()) {
            this.memoizedIsInitialized = (byte) 0;
            return false;
        }
        this.memoizedIsInitialized = (byte) 1;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unusedPrivateParameter) {
        return new CardActionProto();
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public void writeTo(CodedOutputStream codedOutputStream) {
        if ((this.bitField0_ & 1) != 0) {
            codedOutputStream.writeInt32(1, this.cardType_);
        }
        if ((this.bitField0_ & 2) != 0) {
            codedOutputStream.writeInt32(2, this.cardId_);
        }
        if ((this.bitField0_ & 4) != 0) {
            codedOutputStream.writeInt32(3, this.hostId_);
        }
        if ((this.bitField0_ & 8) != 0) {
            codedOutputStream.writeInt32(4, this.action_);
        }
        GeneratedMessageV3.serializeStringMapTo(codedOutputStream, internalGetParam(), ParamDefaultEntryHolder.defaultEntry, 5);
        this.unknownFields.writeTo(codedOutputStream);
    }

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements CardActionProtoOrBuilder {
        private int action_;
        private int bitField0_;
        private int cardId_;
        private int cardType_;
        private int hostId_;
        private MapField<String, String> param_;

        public static final Descriptors.Descriptor getDescriptor() {
            return CardAction.internal_static_com_oplus_cardwidget_proto_CardActionProto_descriptor;
        }

        private MapField<String, String> internalGetMutableParam() {
            onChanged();
            if (this.param_ == null) {
                this.param_ = MapField.newMapField(ParamDefaultEntryHolder.defaultEntry);
            }
            if (!this.param_.isMutable()) {
                this.param_ = this.param_.copy();
            }
            return this.param_;
        }

        private MapField<String, String> internalGetParam() {
            MapField<String, String> mapField = this.param_;
            return mapField == null ? MapField.emptyMapField(ParamDefaultEntryHolder.defaultEntry) : mapField;
        }

        private void maybeForceBuilderInitialization() {
            boolean unused = GeneratedMessageV3.alwaysUseFieldBuilders;
        }

        public Builder clearAction() {
            this.bitField0_ &= -9;
            this.action_ = 0;
            onChanged();
            return this;
        }

        public Builder clearCardId() {
            this.bitField0_ &= -3;
            this.cardId_ = 0;
            onChanged();
            return this;
        }

        public Builder clearCardType() {
            this.bitField0_ &= -2;
            this.cardType_ = 0;
            onChanged();
            return this;
        }

        public Builder clearHostId() {
            this.bitField0_ &= -5;
            this.hostId_ = 0;
            onChanged();
            return this;
        }

        public Builder clearParam() {
            internalGetMutableParam().getMutableMap().clear();
            return this;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public boolean containsParam(String str) {
            Objects.requireNonNull(str);
            return internalGetParam().getMap().containsKey(str);
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public int getAction() {
            return this.action_;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public int getCardId() {
            return this.cardId_;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public int getCardType() {
            return this.cardType_;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
        public Descriptors.Descriptor getDescriptorForType() {
            return CardAction.internal_static_com_oplus_cardwidget_proto_CardActionProto_descriptor;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public int getHostId() {
            return this.hostId_;
        }

        @Deprecated
        public Map<String, String> getMutableParam() {
            return internalGetMutableParam().getMutableMap();
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        @Deprecated
        public Map<String, String> getParam() {
            return getParamMap();
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public int getParamCount() {
            return internalGetParam().getMap().size();
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public Map<String, String> getParamMap() {
            return internalGetParam().getMap();
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public String getParamOrDefault(String str, String str2) {
            Objects.requireNonNull(str);
            Map<String, String> map = internalGetParam().getMap();
            return map.containsKey(str) ? map.get(str) : str2;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public String getParamOrThrow(String str) {
            Objects.requireNonNull(str);
            Map<String, String> map = internalGetParam().getMap();
            if (map.containsKey(str)) {
                return map.get(str);
            }
            throw new IllegalArgumentException();
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public boolean hasAction() {
            return (this.bitField0_ & 8) != 0;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public boolean hasCardId() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public boolean hasCardType() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.oplus.cardwidget.proto.CardActionProtoOrBuilder
        public boolean hasHostId() {
            return (this.bitField0_ & 4) != 0;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return CardAction.internal_static_com_oplus_cardwidget_proto_CardActionProto_fieldAccessorTable.ensureFieldAccessorsInitialized(CardActionProto.class, Builder.class);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected MapField internalGetMapField(int i10) {
            if (i10 == 5) {
                return internalGetParam();
            }
            throw new RuntimeException("Invalid map field number: " + i10);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected MapField internalGetMutableMapField(int i10) {
            if (i10 == 5) {
                return internalGetMutableParam();
            }
            throw new RuntimeException("Invalid map field number: " + i10);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            return hasCardType() && hasCardId() && hasHostId() && hasAction();
        }

        public Builder putAllParam(Map<String, String> map) {
            internalGetMutableParam().getMutableMap().putAll(map);
            return this;
        }

        public Builder putParam(String str, String str2) {
            Objects.requireNonNull(str);
            Objects.requireNonNull(str2);
            internalGetMutableParam().getMutableMap().put(str, str2);
            return this;
        }

        public Builder removeParam(String str) {
            Objects.requireNonNull(str);
            internalGetMutableParam().getMutableMap().remove(str);
            return this;
        }

        public Builder setAction(int i10) {
            this.bitField0_ |= 8;
            this.action_ = i10;
            onChanged();
            return this;
        }

        public Builder setCardId(int i10) {
            this.bitField0_ |= 2;
            this.cardId_ = i10;
            onChanged();
            return this;
        }

        public Builder setCardType(int i10) {
            this.bitField0_ |= 1;
            this.cardType_ = i10;
            onChanged();
            return this;
        }

        public Builder setHostId(int i10) {
            this.bitField0_ |= 4;
            this.hostId_ = i10;
            onChanged();
            return this;
        }

        private Builder() {
            maybeForceBuilderInitialization();
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
            return (Builder) super.addRepeatedField(fieldDescriptor, obj);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public CardActionProto build() {
            CardActionProto buildPartial = buildPartial();
            if (buildPartial.isInitialized()) {
                return buildPartial;
            }
            throw AbstractMessage.Builder.newUninitializedMessageException((Message) buildPartial);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public CardActionProto buildPartial() {
            int i10;
            CardActionProto cardActionProto = new CardActionProto(this);
            int i11 = this.bitField0_;
            if ((i11 & 1) != 0) {
                cardActionProto.cardType_ = this.cardType_;
                i10 = 1;
            } else {
                i10 = 0;
            }
            if ((i11 & 2) != 0) {
                cardActionProto.cardId_ = this.cardId_;
                i10 |= 2;
            }
            if ((i11 & 4) != 0) {
                cardActionProto.hostId_ = this.hostId_;
                i10 |= 4;
            }
            if ((i11 & 8) != 0) {
                cardActionProto.action_ = this.action_;
                i10 |= 8;
            }
            cardActionProto.param_ = internalGetParam();
            cardActionProto.param_.makeImmutable();
            cardActionProto.bitField0_ = i10;
            onBuilt();
            return cardActionProto;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
            return (Builder) super.clearField(fieldDescriptor);
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public CardActionProto getDefaultInstanceForType() {
            return CardActionProto.getDefaultInstance();
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
            return (Builder) super.setField(fieldDescriptor, obj);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i10, Object obj) {
            return (Builder) super.setRepeatedField(fieldDescriptor, i10, obj);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public final Builder setUnknownFields(UnknownFieldSet unknownFieldSet) {
            return (Builder) super.setUnknownFields(unknownFieldSet);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor) {
            return (Builder) super.clearOneof(oneofDescriptor);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public final Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet) {
            return (Builder) super.mergeUnknownFields(unknownFieldSet);
        }

        private Builder(GeneratedMessageV3.BuilderParent builderParent) {
            super(builderParent);
            maybeForceBuilderInitialization();
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder clear() {
            super.clear();
            this.cardType_ = 0;
            int i10 = this.bitField0_ & (-2);
            this.cardId_ = 0;
            this.hostId_ = 0;
            this.action_ = 0;
            this.bitField0_ = i10 & (-3) & (-5) & (-9);
            internalGetMutableParam().clear();
            return this;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
        /* renamed from: clone */
        public Builder mo3clone() {
            return (Builder) super.mo3clone();
        }

        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(Message message) {
            if (message instanceof CardActionProto) {
                return mergeFrom((CardActionProto) message);
            }
            super.mergeFrom(message);
            return this;
        }

        public Builder mergeFrom(CardActionProto cardActionProto) {
            if (cardActionProto == CardActionProto.getDefaultInstance()) {
                return this;
            }
            if (cardActionProto.hasCardType()) {
                setCardType(cardActionProto.getCardType());
            }
            if (cardActionProto.hasCardId()) {
                setCardId(cardActionProto.getCardId());
            }
            if (cardActionProto.hasHostId()) {
                setHostId(cardActionProto.getHostId());
            }
            if (cardActionProto.hasAction()) {
                setAction(cardActionProto.getAction());
            }
            internalGetMutableParam().mergeFrom(cardActionProto.internalGetParam());
            mergeUnknownFields(((GeneratedMessageV3) cardActionProto).unknownFields);
            onChanged();
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0021  */
        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            CardActionProto cardActionProto = null;
            try {
                try {
                    CardActionProto parsePartialFrom = CardActionProto.PARSER.parsePartialFrom(codedInputStream, extensionRegistryLite);
                    if (parsePartialFrom != null) {
                        mergeFrom(parsePartialFrom);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (cardActionProto != null) {
                    }
                    throw th;
                }
            } catch (InvalidProtocolBufferException e10) {
                CardActionProto cardActionProto2 = (CardActionProto) e10.getUnfinishedMessage();
                try {
                    throw e10.unwrapIOException();
                } catch (Throwable th2) {
                    th = th2;
                    cardActionProto = cardActionProto2;
                    if (cardActionProto != null) {
                        mergeFrom(cardActionProto);
                    }
                    throw th;
                }
            }
        }
    }

    public static Builder newBuilder(CardActionProto cardActionProto) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(cardActionProto);
    }

    public static CardActionProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
        return PARSER.parseFrom(byteBuffer, extensionRegistryLite);
    }

    private CardActionProto(GeneratedMessageV3.Builder<?> builder) {
        super(builder);
        this.memoizedIsInitialized = (byte) -1;
    }

    public static CardActionProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
        return (CardActionProto) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
    }

    public static CardActionProto parseFrom(ByteString byteString) {
        return PARSER.parseFrom(byteString);
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
    public CardActionProto getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    public static CardActionProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
        return PARSER.parseFrom(byteString, extensionRegistryLite);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder newBuilderForType() {
        return newBuilder();
    }

    private CardActionProto() {
        this.memoizedIsInitialized = (byte) -1;
    }

    public static CardActionProto parseFrom(byte[] bArr) {
        return PARSER.parseFrom(bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
        return new Builder(builderParent);
    }

    public static CardActionProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
        return PARSER.parseFrom(bArr, extensionRegistryLite);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CardActionProto(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
        this();
        Objects.requireNonNull(extensionRegistryLite);
        UnknownFieldSet.Builder newBuilder = UnknownFieldSet.newBuilder();
        boolean z10 = false;
        int i10 = 0;
        while (!z10) {
            try {
                try {
                    try {
                        int readTag = codedInputStream.readTag();
                        if (readTag != 0) {
                            if (readTag == 8) {
                                this.bitField0_ |= 1;
                                this.cardType_ = codedInputStream.readInt32();
                            } else if (readTag == 16) {
                                this.bitField0_ |= 2;
                                this.cardId_ = codedInputStream.readInt32();
                            } else if (readTag == 24) {
                                this.bitField0_ |= 4;
                                this.hostId_ = codedInputStream.readInt32();
                            } else if (readTag == 32) {
                                this.bitField0_ |= 8;
                                this.action_ = codedInputStream.readInt32();
                            } else if (readTag != 42) {
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                }
                            } else {
                                if ((i10 & 16) == 0) {
                                    this.param_ = MapField.newMapField(ParamDefaultEntryHolder.defaultEntry);
                                    i10 |= 16;
                                }
                                MapEntry mapEntry = (MapEntry) codedInputStream.readMessage(ParamDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistryLite);
                                this.param_.getMutableMap().put(mapEntry.getKey(), mapEntry.getValue());
                            }
                        }
                        z10 = true;
                    } catch (InvalidProtocolBufferException e10) {
                        throw e10.setUnfinishedMessage(this);
                    }
                } catch (IOException e11) {
                    throw new InvalidProtocolBufferException(e11).setUnfinishedMessage(this);
                }
            } finally {
                this.unknownFields = newBuilder.build();
                makeExtensionsImmutable();
            }
        }
    }

    public static CardActionProto parseFrom(InputStream inputStream) {
        return (CardActionProto) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
    }

    public static CardActionProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
        return (CardActionProto) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
    }

    public static CardActionProto parseFrom(CodedInputStream codedInputStream) {
        return (CardActionProto) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
    }

    public static CardActionProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
        return (CardActionProto) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
    }
}
