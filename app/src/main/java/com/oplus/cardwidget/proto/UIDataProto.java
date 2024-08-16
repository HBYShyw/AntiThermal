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
public final class UIDataProto extends GeneratedMessageV3 implements UIDataProtoOrBuilder {
    public static final int CARDID_FIELD_NUMBER = 1;
    public static final int COMPRESS_FIELD_NUMBER = 9;
    public static final int DATA_FIELD_NUMBER = 2;
    public static final int FORCECHANGECARDUI_FIELD_NUMBER = 8;
    public static final int IDMAPS_FIELD_NUMBER = 3;
    public static final int LAYOUTNAME_FIELD_NUMBER = 10;
    public static final int NAME_FIELD_NUMBER = 4;
    public static final int THEMEID_FIELD_NUMBER = 6;
    public static final int VALUE_FIELD_NUMBER = 7;
    public static final int VERSION_FIELD_NUMBER = 5;
    private static final long serialVersionUID = 0;
    private int bitField0_;
    private int cardId_;
    private int compress_;
    private volatile Object data_;
    private boolean forceChangeCardUI_;
    private MapField<String, Integer> idMaps_;
    private volatile Object layoutName_;
    private byte memoizedIsInitialized;
    private volatile Object name_;
    private int themeId_;
    private ByteString value_;
    private long version_;
    private static final UIDataProto DEFAULT_INSTANCE = new UIDataProto();

    @Deprecated
    public static final Parser<UIDataProto> PARSER = new AbstractParser<UIDataProto>() { // from class: com.oplus.cardwidget.proto.UIDataProto.1
        @Override // com.google.protobuf.Parser
        public UIDataProto parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            return new UIDataProto(codedInputStream, extensionRegistryLite);
        }
    };

    /* loaded from: classes.dex */
    public enum DataCompress implements ProtocolMessageEnum {
        NONE(0),
        FLATER(1),
        SIMPLIFY(2);

        public static final int FLATER_VALUE = 1;
        public static final int NONE_VALUE = 0;
        public static final int SIMPLIFY_VALUE = 2;
        private final int value;
        private static final Internal.EnumLiteMap<DataCompress> internalValueMap = new Internal.EnumLiteMap<DataCompress>() { // from class: com.oplus.cardwidget.proto.UIDataProto.DataCompress.1
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public DataCompress findValueByNumber(int i10) {
                return DataCompress.forNumber(i10);
            }
        };
        private static final DataCompress[] VALUES = values();

        DataCompress(int i10) {
            this.value = i10;
        }

        public static DataCompress forNumber(int i10) {
            if (i10 == 0) {
                return NONE;
            }
            if (i10 == 1) {
                return FLATER;
            }
            if (i10 != 2) {
                return null;
            }
            return SIMPLIFY;
        }

        public static final Descriptors.EnumDescriptor getDescriptor() {
            return UIDataProto.getDescriptor().getEnumTypes().get(0);
        }

        public static Internal.EnumLiteMap<DataCompress> internalGetValueMap() {
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
        public static DataCompress valueOf(int i10) {
            return forNumber(i10);
        }

        public static DataCompress valueOf(Descriptors.EnumValueDescriptor enumValueDescriptor) {
            if (enumValueDescriptor.getType() == getDescriptor()) {
                return VALUES[enumValueDescriptor.getIndex()];
            }
            throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class IdMapsDefaultEntryHolder {
        static final MapEntry<String, Integer> defaultEntry = MapEntry.newDefaultInstance(CardUiData.internal_static_com_oplus_cardwidget_proto_UIDataProto_IdMapsEntry_descriptor, WireFormat.FieldType.STRING, "", WireFormat.FieldType.INT32, 0);

        private IdMapsDefaultEntryHolder() {
        }
    }

    public static UIDataProto getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static final Descriptors.Descriptor getDescriptor() {
        return CardUiData.internal_static_com_oplus_cardwidget_proto_UIDataProto_descriptor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MapField<String, Integer> internalGetIdMaps() {
        MapField<String, Integer> mapField = this.idMaps_;
        return mapField == null ? MapField.emptyMapField(IdMapsDefaultEntryHolder.defaultEntry) : mapField;
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static UIDataProto parseDelimitedFrom(InputStream inputStream) {
        return (UIDataProto) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream);
    }

    public static UIDataProto parseFrom(ByteBuffer byteBuffer) {
        return PARSER.parseFrom(byteBuffer);
    }

    public static Parser<UIDataProto> parser() {
        return PARSER;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean containsIdMaps(String str) {
        Objects.requireNonNull(str);
        return internalGetIdMaps().getMap().containsKey(str);
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UIDataProto)) {
            return super.equals(obj);
        }
        UIDataProto uIDataProto = (UIDataProto) obj;
        if (hasCardId() != uIDataProto.hasCardId()) {
            return false;
        }
        if ((hasCardId() && getCardId() != uIDataProto.getCardId()) || hasData() != uIDataProto.hasData()) {
            return false;
        }
        if ((hasData() && !getData().equals(uIDataProto.getData())) || !internalGetIdMaps().equals(uIDataProto.internalGetIdMaps()) || hasName() != uIDataProto.hasName()) {
            return false;
        }
        if ((hasName() && !getName().equals(uIDataProto.getName())) || hasVersion() != uIDataProto.hasVersion()) {
            return false;
        }
        if ((hasVersion() && getVersion() != uIDataProto.getVersion()) || hasThemeId() != uIDataProto.hasThemeId()) {
            return false;
        }
        if ((hasThemeId() && getThemeId() != uIDataProto.getThemeId()) || hasValue() != uIDataProto.hasValue()) {
            return false;
        }
        if ((hasValue() && !getValue().equals(uIDataProto.getValue())) || hasForceChangeCardUI() != uIDataProto.hasForceChangeCardUI()) {
            return false;
        }
        if ((hasForceChangeCardUI() && getForceChangeCardUI() != uIDataProto.getForceChangeCardUI()) || hasCompress() != uIDataProto.hasCompress()) {
            return false;
        }
        if ((!hasCompress() || this.compress_ == uIDataProto.compress_) && hasLayoutName() == uIDataProto.hasLayoutName()) {
            return (!hasLayoutName() || getLayoutName().equals(uIDataProto.getLayoutName())) && this.unknownFields.equals(uIDataProto.unknownFields);
        }
        return false;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public int getCardId() {
        return this.cardId_;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public DataCompress getCompress() {
        DataCompress valueOf = DataCompress.valueOf(this.compress_);
        return valueOf == null ? DataCompress.NONE : valueOf;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public String getData() {
        Object obj = this.data_;
        if (obj instanceof String) {
            return (String) obj;
        }
        ByteString byteString = (ByteString) obj;
        String stringUtf8 = byteString.toStringUtf8();
        if (byteString.isValidUtf8()) {
            this.data_ = stringUtf8;
        }
        return stringUtf8;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public ByteString getDataBytes() {
        Object obj = this.data_;
        if (obj instanceof String) {
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.data_ = copyFromUtf8;
            return copyFromUtf8;
        }
        return (ByteString) obj;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean getForceChangeCardUI() {
        return this.forceChangeCardUI_;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    @Deprecated
    public Map<String, Integer> getIdMaps() {
        return getIdMapsMap();
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public int getIdMapsCount() {
        return internalGetIdMaps().getMap().size();
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public Map<String, Integer> getIdMapsMap() {
        return internalGetIdMaps().getMap();
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public int getIdMapsOrDefault(String str, int i10) {
        Objects.requireNonNull(str);
        Map<String, Integer> map = internalGetIdMaps().getMap();
        return map.containsKey(str) ? map.get(str).intValue() : i10;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public int getIdMapsOrThrow(String str) {
        Objects.requireNonNull(str);
        Map<String, Integer> map = internalGetIdMaps().getMap();
        if (map.containsKey(str)) {
            return map.get(str).intValue();
        }
        throw new IllegalArgumentException();
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public String getLayoutName() {
        Object obj = this.layoutName_;
        if (obj instanceof String) {
            return (String) obj;
        }
        ByteString byteString = (ByteString) obj;
        String stringUtf8 = byteString.toStringUtf8();
        if (byteString.isValidUtf8()) {
            this.layoutName_ = stringUtf8;
        }
        return stringUtf8;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public ByteString getLayoutNameBytes() {
        Object obj = this.layoutName_;
        if (obj instanceof String) {
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.layoutName_ = copyFromUtf8;
            return copyFromUtf8;
        }
        return (ByteString) obj;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public String getName() {
        Object obj = this.name_;
        if (obj instanceof String) {
            return (String) obj;
        }
        ByteString byteString = (ByteString) obj;
        String stringUtf8 = byteString.toStringUtf8();
        if (byteString.isValidUtf8()) {
            this.name_ = stringUtf8;
        }
        return stringUtf8;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public ByteString getNameBytes() {
        Object obj = this.name_;
        if (obj instanceof String) {
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }
        return (ByteString) obj;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Parser<UIDataProto> getParserForType() {
        return PARSER;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public int getSerializedSize() {
        int i10 = this.memoizedSize;
        if (i10 != -1) {
            return i10;
        }
        int computeInt32Size = (this.bitField0_ & 1) != 0 ? 0 + CodedOutputStream.computeInt32Size(1, this.cardId_) : 0;
        if ((this.bitField0_ & 2) != 0) {
            computeInt32Size += GeneratedMessageV3.computeStringSize(2, this.data_);
        }
        for (Map.Entry<String, Integer> entry : internalGetIdMaps().getMap().entrySet()) {
            computeInt32Size += CodedOutputStream.computeMessageSize(3, IdMapsDefaultEntryHolder.defaultEntry.newBuilderForType().setKey(entry.getKey()).setValue(entry.getValue()).build());
        }
        if ((this.bitField0_ & 4) != 0) {
            computeInt32Size += GeneratedMessageV3.computeStringSize(4, this.name_);
        }
        if ((this.bitField0_ & 8) != 0) {
            computeInt32Size += CodedOutputStream.computeInt64Size(5, this.version_);
        }
        if ((this.bitField0_ & 16) != 0) {
            computeInt32Size += CodedOutputStream.computeInt32Size(6, this.themeId_);
        }
        if ((this.bitField0_ & 32) != 0) {
            computeInt32Size += CodedOutputStream.computeBytesSize(7, this.value_);
        }
        if ((this.bitField0_ & 64) != 0) {
            computeInt32Size += CodedOutputStream.computeBoolSize(8, this.forceChangeCardUI_);
        }
        if ((this.bitField0_ & 128) != 0) {
            computeInt32Size += CodedOutputStream.computeEnumSize(9, this.compress_);
        }
        if ((this.bitField0_ & 256) != 0) {
            computeInt32Size += GeneratedMessageV3.computeStringSize(10, this.layoutName_);
        }
        int serializedSize = computeInt32Size + this.unknownFields.getSerializedSize();
        this.memoizedSize = serializedSize;
        return serializedSize;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public int getThemeId() {
        return this.themeId_;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
    public final UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public ByteString getValue() {
        return this.value_;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public long getVersion() {
        return this.version_;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasCardId() {
        return (this.bitField0_ & 1) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasCompress() {
        return (this.bitField0_ & 128) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasData() {
        return (this.bitField0_ & 2) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasForceChangeCardUI() {
        return (this.bitField0_ & 64) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasLayoutName() {
        return (this.bitField0_ & 256) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasName() {
        return (this.bitField0_ & 4) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasThemeId() {
        return (this.bitField0_ & 16) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasValue() {
        return (this.bitField0_ & 32) != 0;
    }

    @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
    public boolean hasVersion() {
        return (this.bitField0_ & 8) != 0;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public int hashCode() {
        int i10 = this.memoizedHashCode;
        if (i10 != 0) {
            return i10;
        }
        int hashCode = 779 + getDescriptor().hashCode();
        if (hasCardId()) {
            hashCode = (((hashCode * 37) + 1) * 53) + getCardId();
        }
        if (hasData()) {
            hashCode = (((hashCode * 37) + 2) * 53) + getData().hashCode();
        }
        if (!internalGetIdMaps().getMap().isEmpty()) {
            hashCode = (((hashCode * 37) + 3) * 53) + internalGetIdMaps().hashCode();
        }
        if (hasName()) {
            hashCode = (((hashCode * 37) + 4) * 53) + getName().hashCode();
        }
        if (hasVersion()) {
            hashCode = (((hashCode * 37) + 5) * 53) + Internal.hashLong(getVersion());
        }
        if (hasThemeId()) {
            hashCode = (((hashCode * 37) + 6) * 53) + getThemeId();
        }
        if (hasValue()) {
            hashCode = (((hashCode * 37) + 7) * 53) + getValue().hashCode();
        }
        if (hasForceChangeCardUI()) {
            hashCode = (((hashCode * 37) + 8) * 53) + Internal.hashBoolean(getForceChangeCardUI());
        }
        if (hasCompress()) {
            hashCode = (((hashCode * 37) + 9) * 53) + this.compress_;
        }
        if (hasLayoutName()) {
            hashCode = (((hashCode * 37) + 10) * 53) + getLayoutName().hashCode();
        }
        int hashCode2 = (hashCode * 29) + this.unknownFields.hashCode();
        this.memoizedHashCode = hashCode2;
        return hashCode2;
    }

    @Override // com.google.protobuf.GeneratedMessageV3
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return CardUiData.internal_static_com_oplus_cardwidget_proto_UIDataProto_fieldAccessorTable.ensureFieldAccessorsInitialized(UIDataProto.class, Builder.class);
    }

    @Override // com.google.protobuf.GeneratedMessageV3
    protected MapField internalGetMapField(int i10) {
        if (i10 == 3) {
            return internalGetIdMaps();
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
        if (!hasCardId()) {
            this.memoizedIsInitialized = (byte) 0;
            return false;
        }
        if (!hasData()) {
            this.memoizedIsInitialized = (byte) 0;
            return false;
        }
        this.memoizedIsInitialized = (byte) 1;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unusedPrivateParameter) {
        return new UIDataProto();
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public void writeTo(CodedOutputStream codedOutputStream) {
        if ((this.bitField0_ & 1) != 0) {
            codedOutputStream.writeInt32(1, this.cardId_);
        }
        if ((this.bitField0_ & 2) != 0) {
            GeneratedMessageV3.writeString(codedOutputStream, 2, this.data_);
        }
        GeneratedMessageV3.serializeStringMapTo(codedOutputStream, internalGetIdMaps(), IdMapsDefaultEntryHolder.defaultEntry, 3);
        if ((this.bitField0_ & 4) != 0) {
            GeneratedMessageV3.writeString(codedOutputStream, 4, this.name_);
        }
        if ((this.bitField0_ & 8) != 0) {
            codedOutputStream.writeInt64(5, this.version_);
        }
        if ((this.bitField0_ & 16) != 0) {
            codedOutputStream.writeInt32(6, this.themeId_);
        }
        if ((this.bitField0_ & 32) != 0) {
            codedOutputStream.writeBytes(7, this.value_);
        }
        if ((this.bitField0_ & 64) != 0) {
            codedOutputStream.writeBool(8, this.forceChangeCardUI_);
        }
        if ((this.bitField0_ & 128) != 0) {
            codedOutputStream.writeEnum(9, this.compress_);
        }
        if ((this.bitField0_ & 256) != 0) {
            GeneratedMessageV3.writeString(codedOutputStream, 10, this.layoutName_);
        }
        this.unknownFields.writeTo(codedOutputStream);
    }

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements UIDataProtoOrBuilder {
        private int bitField0_;
        private int cardId_;
        private int compress_;
        private Object data_;
        private boolean forceChangeCardUI_;
        private MapField<String, Integer> idMaps_;
        private Object layoutName_;
        private Object name_;
        private int themeId_;
        private ByteString value_;
        private long version_;

        public static final Descriptors.Descriptor getDescriptor() {
            return CardUiData.internal_static_com_oplus_cardwidget_proto_UIDataProto_descriptor;
        }

        private MapField<String, Integer> internalGetIdMaps() {
            MapField<String, Integer> mapField = this.idMaps_;
            return mapField == null ? MapField.emptyMapField(IdMapsDefaultEntryHolder.defaultEntry) : mapField;
        }

        private MapField<String, Integer> internalGetMutableIdMaps() {
            onChanged();
            if (this.idMaps_ == null) {
                this.idMaps_ = MapField.newMapField(IdMapsDefaultEntryHolder.defaultEntry);
            }
            if (!this.idMaps_.isMutable()) {
                this.idMaps_ = this.idMaps_.copy();
            }
            return this.idMaps_;
        }

        private void maybeForceBuilderInitialization() {
            boolean unused = GeneratedMessageV3.alwaysUseFieldBuilders;
        }

        public Builder clearCardId() {
            this.bitField0_ &= -2;
            this.cardId_ = 0;
            onChanged();
            return this;
        }

        public Builder clearCompress() {
            this.bitField0_ &= -257;
            this.compress_ = 0;
            onChanged();
            return this;
        }

        public Builder clearData() {
            this.bitField0_ &= -3;
            this.data_ = UIDataProto.getDefaultInstance().getData();
            onChanged();
            return this;
        }

        public Builder clearForceChangeCardUI() {
            this.bitField0_ &= -129;
            this.forceChangeCardUI_ = false;
            onChanged();
            return this;
        }

        public Builder clearIdMaps() {
            internalGetMutableIdMaps().getMutableMap().clear();
            return this;
        }

        public Builder clearLayoutName() {
            this.bitField0_ &= -513;
            this.layoutName_ = UIDataProto.getDefaultInstance().getLayoutName();
            onChanged();
            return this;
        }

        public Builder clearName() {
            this.bitField0_ &= -9;
            this.name_ = UIDataProto.getDefaultInstance().getName();
            onChanged();
            return this;
        }

        public Builder clearThemeId() {
            this.bitField0_ &= -33;
            this.themeId_ = 0;
            onChanged();
            return this;
        }

        public Builder clearValue() {
            this.bitField0_ &= -65;
            this.value_ = UIDataProto.getDefaultInstance().getValue();
            onChanged();
            return this;
        }

        public Builder clearVersion() {
            this.bitField0_ &= -17;
            this.version_ = 0L;
            onChanged();
            return this;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean containsIdMaps(String str) {
            Objects.requireNonNull(str);
            return internalGetIdMaps().getMap().containsKey(str);
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public int getCardId() {
            return this.cardId_;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public DataCompress getCompress() {
            DataCompress valueOf = DataCompress.valueOf(this.compress_);
            return valueOf == null ? DataCompress.NONE : valueOf;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public String getData() {
            Object obj = this.data_;
            if (!(obj instanceof String)) {
                ByteString byteString = (ByteString) obj;
                String stringUtf8 = byteString.toStringUtf8();
                if (byteString.isValidUtf8()) {
                    this.data_ = stringUtf8;
                }
                return stringUtf8;
            }
            return (String) obj;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public ByteString getDataBytes() {
            Object obj = this.data_;
            if (obj instanceof String) {
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.data_ = copyFromUtf8;
                return copyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
        public Descriptors.Descriptor getDescriptorForType() {
            return CardUiData.internal_static_com_oplus_cardwidget_proto_UIDataProto_descriptor;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean getForceChangeCardUI() {
            return this.forceChangeCardUI_;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        @Deprecated
        public Map<String, Integer> getIdMaps() {
            return getIdMapsMap();
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public int getIdMapsCount() {
            return internalGetIdMaps().getMap().size();
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public Map<String, Integer> getIdMapsMap() {
            return internalGetIdMaps().getMap();
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public int getIdMapsOrDefault(String str, int i10) {
            Objects.requireNonNull(str);
            Map<String, Integer> map = internalGetIdMaps().getMap();
            return map.containsKey(str) ? map.get(str).intValue() : i10;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public int getIdMapsOrThrow(String str) {
            Objects.requireNonNull(str);
            Map<String, Integer> map = internalGetIdMaps().getMap();
            if (map.containsKey(str)) {
                return map.get(str).intValue();
            }
            throw new IllegalArgumentException();
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public String getLayoutName() {
            Object obj = this.layoutName_;
            if (!(obj instanceof String)) {
                ByteString byteString = (ByteString) obj;
                String stringUtf8 = byteString.toStringUtf8();
                if (byteString.isValidUtf8()) {
                    this.layoutName_ = stringUtf8;
                }
                return stringUtf8;
            }
            return (String) obj;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public ByteString getLayoutNameBytes() {
            Object obj = this.layoutName_;
            if (obj instanceof String) {
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.layoutName_ = copyFromUtf8;
                return copyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Deprecated
        public Map<String, Integer> getMutableIdMaps() {
            return internalGetMutableIdMaps().getMutableMap();
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public String getName() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                ByteString byteString = (ByteString) obj;
                String stringUtf8 = byteString.toStringUtf8();
                if (byteString.isValidUtf8()) {
                    this.name_ = stringUtf8;
                }
                return stringUtf8;
            }
            return (String) obj;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public ByteString getNameBytes() {
            Object obj = this.name_;
            if (obj instanceof String) {
                ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
                this.name_ = copyFromUtf8;
                return copyFromUtf8;
            }
            return (ByteString) obj;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public int getThemeId() {
            return this.themeId_;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public ByteString getValue() {
            return this.value_;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public long getVersion() {
            return this.version_;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasCardId() {
            return (this.bitField0_ & 1) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasCompress() {
            return (this.bitField0_ & 256) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasData() {
            return (this.bitField0_ & 2) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasForceChangeCardUI() {
            return (this.bitField0_ & 128) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasLayoutName() {
            return (this.bitField0_ & 512) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasName() {
            return (this.bitField0_ & 8) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasThemeId() {
            return (this.bitField0_ & 32) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasValue() {
            return (this.bitField0_ & 64) != 0;
        }

        @Override // com.oplus.cardwidget.proto.UIDataProtoOrBuilder
        public boolean hasVersion() {
            return (this.bitField0_ & 16) != 0;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return CardUiData.internal_static_com_oplus_cardwidget_proto_UIDataProto_fieldAccessorTable.ensureFieldAccessorsInitialized(UIDataProto.class, Builder.class);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected MapField internalGetMapField(int i10) {
            if (i10 == 3) {
                return internalGetIdMaps();
            }
            throw new RuntimeException("Invalid map field number: " + i10);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected MapField internalGetMutableMapField(int i10) {
            if (i10 == 3) {
                return internalGetMutableIdMaps();
            }
            throw new RuntimeException("Invalid map field number: " + i10);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            return hasCardId() && hasData();
        }

        public Builder putAllIdMaps(Map<String, Integer> map) {
            internalGetMutableIdMaps().getMutableMap().putAll(map);
            return this;
        }

        public Builder putIdMaps(String str, int i10) {
            Objects.requireNonNull(str);
            internalGetMutableIdMaps().getMutableMap().put(str, Integer.valueOf(i10));
            return this;
        }

        public Builder removeIdMaps(String str) {
            Objects.requireNonNull(str);
            internalGetMutableIdMaps().getMutableMap().remove(str);
            return this;
        }

        public Builder setCardId(int i10) {
            this.bitField0_ |= 1;
            this.cardId_ = i10;
            onChanged();
            return this;
        }

        public Builder setCompress(DataCompress dataCompress) {
            Objects.requireNonNull(dataCompress);
            this.bitField0_ |= 256;
            this.compress_ = dataCompress.getNumber();
            onChanged();
            return this;
        }

        public Builder setData(String str) {
            Objects.requireNonNull(str);
            this.bitField0_ |= 2;
            this.data_ = str;
            onChanged();
            return this;
        }

        public Builder setDataBytes(ByteString byteString) {
            Objects.requireNonNull(byteString);
            this.bitField0_ |= 2;
            this.data_ = byteString;
            onChanged();
            return this;
        }

        public Builder setForceChangeCardUI(boolean z10) {
            this.bitField0_ |= 128;
            this.forceChangeCardUI_ = z10;
            onChanged();
            return this;
        }

        public Builder setLayoutName(String str) {
            Objects.requireNonNull(str);
            this.bitField0_ |= 512;
            this.layoutName_ = str;
            onChanged();
            return this;
        }

        public Builder setLayoutNameBytes(ByteString byteString) {
            Objects.requireNonNull(byteString);
            this.bitField0_ |= 512;
            this.layoutName_ = byteString;
            onChanged();
            return this;
        }

        public Builder setName(String str) {
            Objects.requireNonNull(str);
            this.bitField0_ |= 8;
            this.name_ = str;
            onChanged();
            return this;
        }

        public Builder setNameBytes(ByteString byteString) {
            Objects.requireNonNull(byteString);
            this.bitField0_ |= 8;
            this.name_ = byteString;
            onChanged();
            return this;
        }

        public Builder setThemeId(int i10) {
            this.bitField0_ |= 32;
            this.themeId_ = i10;
            onChanged();
            return this;
        }

        public Builder setValue(ByteString byteString) {
            Objects.requireNonNull(byteString);
            this.bitField0_ |= 64;
            this.value_ = byteString;
            onChanged();
            return this;
        }

        public Builder setVersion(long j10) {
            this.bitField0_ |= 16;
            this.version_ = j10;
            onChanged();
            return this;
        }

        private Builder() {
            this.data_ = "";
            this.name_ = "";
            this.value_ = ByteString.EMPTY;
            this.compress_ = 0;
            this.layoutName_ = "";
            maybeForceBuilderInitialization();
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj) {
            return (Builder) super.addRepeatedField(fieldDescriptor, obj);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public UIDataProto build() {
            UIDataProto buildPartial = buildPartial();
            if (buildPartial.isInitialized()) {
                return buildPartial;
            }
            throw AbstractMessage.Builder.newUninitializedMessageException((Message) buildPartial);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public UIDataProto buildPartial() {
            int i10;
            UIDataProto uIDataProto = new UIDataProto(this);
            int i11 = this.bitField0_;
            if ((i11 & 1) != 0) {
                uIDataProto.cardId_ = this.cardId_;
                i10 = 1;
            } else {
                i10 = 0;
            }
            if ((i11 & 2) != 0) {
                i10 |= 2;
            }
            uIDataProto.data_ = this.data_;
            uIDataProto.idMaps_ = internalGetIdMaps();
            uIDataProto.idMaps_.makeImmutable();
            if ((i11 & 8) != 0) {
                i10 |= 4;
            }
            uIDataProto.name_ = this.name_;
            if ((i11 & 16) != 0) {
                uIDataProto.version_ = this.version_;
                i10 |= 8;
            }
            if ((i11 & 32) != 0) {
                uIDataProto.themeId_ = this.themeId_;
                i10 |= 16;
            }
            if ((i11 & 64) != 0) {
                i10 |= 32;
            }
            uIDataProto.value_ = this.value_;
            if ((i11 & 128) != 0) {
                uIDataProto.forceChangeCardUI_ = this.forceChangeCardUI_;
                i10 |= 64;
            }
            if ((i11 & 256) != 0) {
                i10 |= 128;
            }
            uIDataProto.compress_ = this.compress_;
            if ((i11 & 512) != 0) {
                i10 |= 256;
            }
            uIDataProto.layoutName_ = this.layoutName_;
            uIDataProto.bitField0_ = i10;
            onBuilt();
            return uIDataProto;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder clearField(Descriptors.FieldDescriptor fieldDescriptor) {
            return (Builder) super.clearField(fieldDescriptor);
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public UIDataProto getDefaultInstanceForType() {
            return UIDataProto.getDefaultInstance();
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

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder clear() {
            super.clear();
            this.cardId_ = 0;
            int i10 = this.bitField0_ & (-2);
            this.data_ = "";
            this.bitField0_ = i10 & (-3);
            internalGetMutableIdMaps().clear();
            this.name_ = "";
            int i11 = this.bitField0_ & (-9);
            this.version_ = 0L;
            this.themeId_ = 0;
            int i12 = i11 & (-17) & (-33);
            this.bitField0_ = i12;
            this.value_ = ByteString.EMPTY;
            this.forceChangeCardUI_ = false;
            this.compress_ = 0;
            this.layoutName_ = "";
            this.bitField0_ = i12 & (-65) & (-129) & (-257) & (-513);
            return this;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
        /* renamed from: clone */
        public Builder mo3clone() {
            return (Builder) super.mo3clone();
        }

        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(Message message) {
            if (message instanceof UIDataProto) {
                return mergeFrom((UIDataProto) message);
            }
            super.mergeFrom(message);
            return this;
        }

        private Builder(GeneratedMessageV3.BuilderParent builderParent) {
            super(builderParent);
            this.data_ = "";
            this.name_ = "";
            this.value_ = ByteString.EMPTY;
            this.compress_ = 0;
            this.layoutName_ = "";
            maybeForceBuilderInitialization();
        }

        public Builder mergeFrom(UIDataProto uIDataProto) {
            if (uIDataProto == UIDataProto.getDefaultInstance()) {
                return this;
            }
            if (uIDataProto.hasCardId()) {
                setCardId(uIDataProto.getCardId());
            }
            if (uIDataProto.hasData()) {
                this.bitField0_ |= 2;
                this.data_ = uIDataProto.data_;
                onChanged();
            }
            internalGetMutableIdMaps().mergeFrom(uIDataProto.internalGetIdMaps());
            if (uIDataProto.hasName()) {
                this.bitField0_ |= 8;
                this.name_ = uIDataProto.name_;
                onChanged();
            }
            if (uIDataProto.hasVersion()) {
                setVersion(uIDataProto.getVersion());
            }
            if (uIDataProto.hasThemeId()) {
                setThemeId(uIDataProto.getThemeId());
            }
            if (uIDataProto.hasValue()) {
                setValue(uIDataProto.getValue());
            }
            if (uIDataProto.hasForceChangeCardUI()) {
                setForceChangeCardUI(uIDataProto.getForceChangeCardUI());
            }
            if (uIDataProto.hasCompress()) {
                setCompress(uIDataProto.getCompress());
            }
            if (uIDataProto.hasLayoutName()) {
                this.bitField0_ |= 512;
                this.layoutName_ = uIDataProto.layoutName_;
                onChanged();
            }
            mergeUnknownFields(((GeneratedMessageV3) uIDataProto).unknownFields);
            onChanged();
            return this;
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0021  */
        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
            UIDataProto uIDataProto = null;
            try {
                try {
                    UIDataProto parsePartialFrom = UIDataProto.PARSER.parsePartialFrom(codedInputStream, extensionRegistryLite);
                    if (parsePartialFrom != null) {
                        mergeFrom(parsePartialFrom);
                    }
                    return this;
                } catch (Throwable th) {
                    th = th;
                    if (uIDataProto != null) {
                    }
                    throw th;
                }
            } catch (InvalidProtocolBufferException e10) {
                UIDataProto uIDataProto2 = (UIDataProto) e10.getUnfinishedMessage();
                try {
                    throw e10.unwrapIOException();
                } catch (Throwable th2) {
                    th = th2;
                    uIDataProto = uIDataProto2;
                    if (uIDataProto != null) {
                        mergeFrom(uIDataProto);
                    }
                    throw th;
                }
            }
        }
    }

    public static Builder newBuilder(UIDataProto uIDataProto) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(uIDataProto);
    }

    public static UIDataProto parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) {
        return PARSER.parseFrom(byteBuffer, extensionRegistryLite);
    }

    private UIDataProto(GeneratedMessageV3.Builder<?> builder) {
        super(builder);
        this.memoizedIsInitialized = (byte) -1;
    }

    public static UIDataProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
        return (UIDataProto) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, inputStream, extensionRegistryLite);
    }

    public static UIDataProto parseFrom(ByteString byteString) {
        return PARSER.parseFrom(byteString);
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
    public UIDataProto getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    public static UIDataProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) {
        return PARSER.parseFrom(byteString, extensionRegistryLite);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder newBuilderForType() {
        return newBuilder();
    }

    private UIDataProto() {
        this.memoizedIsInitialized = (byte) -1;
        this.data_ = "";
        this.name_ = "";
        this.value_ = ByteString.EMPTY;
        this.compress_ = 0;
        this.layoutName_ = "";
    }

    public static UIDataProto parseFrom(byte[] bArr) {
        return PARSER.parseFrom(bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Builder newBuilderForType(GeneratedMessageV3.BuilderParent builderParent) {
        return new Builder(builderParent);
    }

    public static UIDataProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) {
        return PARSER.parseFrom(bArr, extensionRegistryLite);
    }

    public static UIDataProto parseFrom(InputStream inputStream) {
        return (UIDataProto) GeneratedMessageV3.parseWithIOException(PARSER, inputStream);
    }

    public static UIDataProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) {
        return (UIDataProto) GeneratedMessageV3.parseWithIOException(PARSER, inputStream, extensionRegistryLite);
    }

    public static UIDataProto parseFrom(CodedInputStream codedInputStream) {
        return (UIDataProto) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0013. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    private UIDataProto(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
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
                        switch (readTag) {
                            case 0:
                                z10 = true;
                            case 8:
                                this.bitField0_ |= 1;
                                this.cardId_ = codedInputStream.readInt32();
                            case 18:
                                ByteString readBytes = codedInputStream.readBytes();
                                this.bitField0_ |= 2;
                                this.data_ = readBytes;
                            case 26:
                                if ((i10 & 4) == 0) {
                                    this.idMaps_ = MapField.newMapField(IdMapsDefaultEntryHolder.defaultEntry);
                                    i10 |= 4;
                                }
                                MapEntry mapEntry = (MapEntry) codedInputStream.readMessage(IdMapsDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistryLite);
                                this.idMaps_.getMutableMap().put(mapEntry.getKey(), mapEntry.getValue());
                            case 34:
                                ByteString readBytes2 = codedInputStream.readBytes();
                                this.bitField0_ |= 4;
                                this.name_ = readBytes2;
                            case 40:
                                this.bitField0_ |= 8;
                                this.version_ = codedInputStream.readInt64();
                            case 48:
                                this.bitField0_ |= 16;
                                this.themeId_ = codedInputStream.readInt32();
                            case 58:
                                this.bitField0_ |= 32;
                                this.value_ = codedInputStream.readBytes();
                            case 64:
                                this.bitField0_ |= 64;
                                this.forceChangeCardUI_ = codedInputStream.readBool();
                            case 72:
                                int readEnum = codedInputStream.readEnum();
                                if (DataCompress.valueOf(readEnum) == null) {
                                    newBuilder.mergeVarintField(9, readEnum);
                                } else {
                                    this.bitField0_ |= 128;
                                    this.compress_ = readEnum;
                                }
                            case 82:
                                ByteString readBytes3 = codedInputStream.readBytes();
                                this.bitField0_ |= 256;
                                this.layoutName_ = readBytes3;
                            default:
                                if (!parseUnknownField(codedInputStream, newBuilder, extensionRegistryLite, readTag)) {
                                    z10 = true;
                                }
                        }
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

    public static UIDataProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) {
        return (UIDataProto) GeneratedMessageV3.parseWithIOException(PARSER, codedInputStream, extensionRegistryLite);
    }
}
