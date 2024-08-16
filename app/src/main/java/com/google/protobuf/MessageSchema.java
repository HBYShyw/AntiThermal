package com.google.protobuf;

import com.google.protobuf.ArrayDecoders;
import com.google.protobuf.ByteString;
import com.google.protobuf.FieldSet;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.WireFormat;
import com.google.protobuf.Writer;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sun.misc.Unsafe;

/* JADX INFO: Access modifiers changed from: package-private */
@CheckReturnValue
/* loaded from: classes.dex */
public final class MessageSchema<T> implements Schema<T> {
    private static final int ENFORCE_UTF8_MASK = 536870912;
    private static final int FIELD_TYPE_MASK = 267386880;
    private static final int INTS_PER_FIELD = 3;
    private static final int NO_PRESENCE_SENTINEL = 1048575;
    private static final int OFFSET_BITS = 20;
    private static final int OFFSET_MASK = 1048575;
    static final int ONEOF_TYPE_OFFSET = 51;
    private static final int REQUIRED_MASK = 268435456;
    private final int[] buffer;
    private final int checkInitializedCount;
    private final MessageLite defaultInstance;
    private final ExtensionSchema<?> extensionSchema;
    private final boolean hasExtensions;
    private final int[] intArray;
    private final ListFieldSchema listFieldSchema;
    private final boolean lite;
    private final MapFieldSchema mapFieldSchema;
    private final int maxFieldNumber;
    private final int minFieldNumber;
    private final NewInstanceSchema newInstanceSchema;
    private final Object[] objects;
    private final boolean proto3;
    private final int repeatedFieldOffsetStart;
    private final UnknownFieldSchema<?, ?> unknownFieldSchema;
    private final boolean useCachedSizeField;
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.protobuf.MessageSchema$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$WireFormat$FieldType;

        static {
            int[] iArr = new int[WireFormat.FieldType.values().length];
            $SwitchMap$com$google$protobuf$WireFormat$FieldType = iArr;
            try {
                iArr[WireFormat.FieldType.BOOL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.BYTES.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.DOUBLE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FIXED32.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SFIXED32.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FIXED64.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SFIXED64.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FLOAT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.ENUM.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.INT32.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.UINT32.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.INT64.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.UINT64.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.MESSAGE.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SINT32.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SINT64.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.STRING.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
        }
    }

    private MessageSchema(int[] iArr, Object[] objArr, int i10, int i11, MessageLite messageLite, boolean z10, boolean z11, int[] iArr2, int i12, int i13, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        this.buffer = iArr;
        this.objects = objArr;
        this.minFieldNumber = i10;
        this.maxFieldNumber = i11;
        this.lite = messageLite instanceof GeneratedMessageLite;
        this.proto3 = z10;
        this.hasExtensions = extensionSchema != null && extensionSchema.hasExtensions(messageLite);
        this.useCachedSizeField = z11;
        this.intArray = iArr2;
        this.checkInitializedCount = i12;
        this.repeatedFieldOffsetStart = i13;
        this.newInstanceSchema = newInstanceSchema;
        this.listFieldSchema = listFieldSchema;
        this.unknownFieldSchema = unknownFieldSchema;
        this.extensionSchema = extensionSchema;
        this.defaultInstance = messageLite;
        this.mapFieldSchema = mapFieldSchema;
    }

    private boolean arePresentForEquals(T t7, T t10, int i10) {
        return isFieldPresent(t7, i10) == isFieldPresent(t10, i10);
    }

    private static <T> boolean booleanAt(T t7, long j10) {
        return UnsafeUtil.getBoolean(t7, j10);
    }

    private static void checkMutable(Object obj) {
        if (isMutable(obj)) {
            return;
        }
        throw new IllegalArgumentException("Mutating immutable message: " + obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r19v0, types: [java.util.Map, java.util.Map<K, V>] */
    /* JADX WARN: Type inference failed for: r1v10, types: [int] */
    private <K, V> int decodeMapEntry(byte[] bArr, int i10, int i11, MapEntryLite.Metadata<K, V> metadata, Map<K, V> map, ArrayDecoders.Registers registers) {
        int i12;
        int decodeVarint32 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
        int i13 = registers.int1;
        if (i13 >= 0 && i13 <= i11 - decodeVarint32) {
            int i14 = decodeVarint32 + i13;
            Object obj = metadata.defaultKey;
            Object obj2 = metadata.defaultValue;
            while (decodeVarint32 < i14) {
                int i15 = decodeVarint32 + 1;
                byte b10 = bArr[decodeVarint32];
                if (b10 < 0) {
                    i12 = ArrayDecoders.decodeVarint32(b10, bArr, i15, registers);
                    b10 = registers.int1;
                } else {
                    i12 = i15;
                }
                int i16 = b10 >>> 3;
                int i17 = b10 & 7;
                if (i16 != 1) {
                    if (i16 == 2 && i17 == metadata.valueType.getWireType()) {
                        decodeVarint32 = decodeMapEntryValue(bArr, i12, i11, metadata.valueType, metadata.defaultValue.getClass(), registers);
                        obj2 = registers.object1;
                    }
                    decodeVarint32 = ArrayDecoders.skipField(b10, bArr, i12, i11, registers);
                } else if (i17 == metadata.keyType.getWireType()) {
                    decodeVarint32 = decodeMapEntryValue(bArr, i12, i11, metadata.keyType, null, registers);
                    obj = registers.object1;
                } else {
                    decodeVarint32 = ArrayDecoders.skipField(b10, bArr, i12, i11, registers);
                }
            }
            if (decodeVarint32 == i14) {
                map.put(obj, obj2);
                return i14;
            }
            throw InvalidProtocolBufferException.parseFailure();
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0008. Please report as an issue. */
    private int decodeMapEntryValue(byte[] bArr, int i10, int i11, WireFormat.FieldType fieldType, Class<?> cls, ArrayDecoders.Registers registers) {
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$FieldType[fieldType.ordinal()]) {
            case 1:
                int decodeVarint64 = ArrayDecoders.decodeVarint64(bArr, i10, registers);
                registers.object1 = Boolean.valueOf(registers.long1 != 0);
                return decodeVarint64;
            case 2:
                return ArrayDecoders.decodeBytes(bArr, i10, registers);
            case 3:
                registers.object1 = Double.valueOf(ArrayDecoders.decodeDouble(bArr, i10));
                return i10 + 8;
            case 4:
            case 5:
                registers.object1 = Integer.valueOf(ArrayDecoders.decodeFixed32(bArr, i10));
                return i10 + 4;
            case 6:
            case 7:
                registers.object1 = Long.valueOf(ArrayDecoders.decodeFixed64(bArr, i10));
                return i10 + 8;
            case 8:
                registers.object1 = Float.valueOf(ArrayDecoders.decodeFloat(bArr, i10));
                return i10 + 4;
            case 9:
            case 10:
            case 11:
                int decodeVarint32 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
                registers.object1 = Integer.valueOf(registers.int1);
                return decodeVarint32;
            case 12:
            case 13:
                int decodeVarint642 = ArrayDecoders.decodeVarint64(bArr, i10, registers);
                registers.object1 = Long.valueOf(registers.long1);
                return decodeVarint642;
            case 14:
                return ArrayDecoders.decodeMessageField(Protobuf.getInstance().schemaFor((Class) cls), bArr, i10, i11, registers);
            case 15:
                int decodeVarint322 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
                registers.object1 = Integer.valueOf(CodedInputStream.decodeZigZag32(registers.int1));
                return decodeVarint322;
            case 16:
                int decodeVarint643 = ArrayDecoders.decodeVarint64(bArr, i10, registers);
                registers.object1 = Long.valueOf(CodedInputStream.decodeZigZag64(registers.long1));
                return decodeVarint643;
            case 17:
                return ArrayDecoders.decodeStringRequireUtf8(bArr, i10, registers);
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    private static <T> double doubleAt(T t7, long j10) {
        return UnsafeUtil.getDouble(t7, j10);
    }

    private <UT, UB> UB filterMapUnknownEnumValues(Object obj, int i10, UB ub2, UnknownFieldSchema<UT, UB> unknownFieldSchema, Object obj2) {
        Internal.EnumVerifier enumFieldVerifier;
        int numberAt = numberAt(i10);
        Object object = UnsafeUtil.getObject(obj, offset(typeAndOffsetAt(i10)));
        return (object == null || (enumFieldVerifier = getEnumFieldVerifier(i10)) == null) ? ub2 : (UB) filterUnknownEnumMap(i10, numberAt, this.mapFieldSchema.forMutableMapData(object), enumFieldVerifier, ub2, unknownFieldSchema, obj2);
    }

    private <K, V, UT, UB> UB filterUnknownEnumMap(int i10, int i11, Map<K, V> map, Internal.EnumVerifier enumVerifier, UB ub2, UnknownFieldSchema<UT, UB> unknownFieldSchema, Object obj) {
        MapEntryLite.Metadata<?, ?> forMapMetadata = this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i10));
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (!enumVerifier.isInRange(((Integer) next.getValue()).intValue())) {
                if (ub2 == null) {
                    ub2 = unknownFieldSchema.getBuilderFromMessage(obj);
                }
                ByteString.CodedBuilder newCodedBuilder = ByteString.newCodedBuilder(MapEntryLite.computeSerializedSize(forMapMetadata, next.getKey(), next.getValue()));
                try {
                    MapEntryLite.writeTo(newCodedBuilder.getCodedOutput(), forMapMetadata, next.getKey(), next.getValue());
                    unknownFieldSchema.addLengthDelimited(ub2, i11, newCodedBuilder.build());
                    it.remove();
                } catch (IOException e10) {
                    throw new RuntimeException(e10);
                }
            }
        }
        return ub2;
    }

    private static <T> float floatAt(T t7, long j10) {
        return UnsafeUtil.getFloat(t7, j10);
    }

    private Internal.EnumVerifier getEnumFieldVerifier(int i10) {
        return (Internal.EnumVerifier) this.objects[((i10 / 3) * 2) + 1];
    }

    private Object getMapFieldDefaultEntry(int i10) {
        return this.objects[(i10 / 3) * 2];
    }

    private Schema getMessageFieldSchema(int i10) {
        int i11 = (i10 / 3) * 2;
        Schema schema = (Schema) this.objects[i11];
        if (schema != null) {
            return schema;
        }
        Schema<T> schemaFor = Protobuf.getInstance().schemaFor((Class) this.objects[i11 + 1]);
        this.objects[i11] = schemaFor;
        return schemaFor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UnknownFieldSetLite getMutableUnknownFields(Object obj) {
        GeneratedMessageLite generatedMessageLite = (GeneratedMessageLite) obj;
        UnknownFieldSetLite unknownFieldSetLite = generatedMessageLite.unknownFields;
        if (unknownFieldSetLite != UnknownFieldSetLite.getDefaultInstance()) {
            return unknownFieldSetLite;
        }
        UnknownFieldSetLite newInstance = UnknownFieldSetLite.newInstance();
        generatedMessageLite.unknownFields = newInstance;
        return newInstance;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x005d. Please report as an issue. */
    private int getSerializedSizeProto2(T t7) {
        int i10;
        int i11;
        int computeDoubleSize;
        int computeBoolSize;
        int computeSFixed32Size;
        boolean z10;
        int computeSizeFixed32List;
        int computeSizeFixed64ListNoTag;
        int computeTagSize;
        int computeUInt32SizeNoTag;
        Unsafe unsafe = UNSAFE;
        int i12 = 1048575;
        int i13 = 1048575;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        while (i14 < this.buffer.length) {
            int typeAndOffsetAt = typeAndOffsetAt(i14);
            int numberAt = numberAt(i14);
            int type = type(typeAndOffsetAt);
            if (type <= 17) {
                i10 = this.buffer[i14 + 2];
                int i17 = i10 & i12;
                i11 = 1 << (i10 >>> 20);
                if (i17 != i13) {
                    i16 = unsafe.getInt(t7, i17);
                    i13 = i17;
                }
            } else {
                i10 = (!this.useCachedSizeField || type < FieldType.DOUBLE_LIST_PACKED.id() || type > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i14 + 2] & i12;
                i11 = 0;
            }
            long offset = offset(typeAndOffsetAt);
            switch (type) {
                case 0:
                    if ((i16 & i11) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeDoubleSize(numberAt, UserProfileInfo.Constant.NA_LAT_LON);
                        i15 += computeDoubleSize;
                        break;
                    }
                case 1:
                    if ((i16 & i11) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        i15 += computeDoubleSize;
                        break;
                    }
                case 2:
                    if ((i16 & i11) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeInt64Size(numberAt, unsafe.getLong(t7, offset));
                        i15 += computeDoubleSize;
                        break;
                    }
                case 3:
                    if ((i16 & i11) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeUInt64Size(numberAt, unsafe.getLong(t7, offset));
                        i15 += computeDoubleSize;
                        break;
                    }
                case 4:
                    if ((i16 & i11) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeInt32Size(numberAt, unsafe.getInt(t7, offset));
                        i15 += computeDoubleSize;
                        break;
                    }
                case 5:
                    if ((i16 & i11) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        i15 += computeDoubleSize;
                        break;
                    }
                case 6:
                    if ((i16 & i11) != 0) {
                        computeDoubleSize = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        i15 += computeDoubleSize;
                        break;
                    }
                    break;
                case 7:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        i15 += computeBoolSize;
                    }
                    break;
                case 8:
                    if ((i16 & i11) != 0) {
                        Object object = unsafe.getObject(t7, offset);
                        if (object instanceof ByteString) {
                            computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object);
                        } else {
                            computeBoolSize = CodedOutputStream.computeStringSize(numberAt, (String) object);
                        }
                        i15 += computeBoolSize;
                    }
                    break;
                case 9:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = SchemaUtil.computeSizeMessage(numberAt, unsafe.getObject(t7, offset), getMessageFieldSchema(i14));
                        i15 += computeBoolSize;
                    }
                    break;
                case 10:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) unsafe.getObject(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 11:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeUInt32Size(numberAt, unsafe.getInt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 12:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeEnumSize(numberAt, unsafe.getInt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 13:
                    if ((i16 & i11) != 0) {
                        computeSFixed32Size = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 14:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        i15 += computeBoolSize;
                    }
                    break;
                case 15:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeSInt32Size(numberAt, unsafe.getInt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 16:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeSInt64Size(numberAt, unsafe.getLong(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 17:
                    if ((i16 & i11) != 0) {
                        computeBoolSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) unsafe.getObject(t7, offset), getMessageFieldSchema(i14));
                        i15 += computeBoolSize;
                    }
                    break;
                case 18:
                    computeBoolSize = SchemaUtil.computeSizeFixed64List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeBoolSize;
                    break;
                case 19:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed32List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 20:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeInt64List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 21:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeUInt64List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 22:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeInt32List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 23:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed64List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 24:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed32List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 25:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeBoolList(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 26:
                    computeBoolSize = SchemaUtil.computeSizeStringList(numberAt, (List) unsafe.getObject(t7, offset));
                    i15 += computeBoolSize;
                    break;
                case 27:
                    computeBoolSize = SchemaUtil.computeSizeMessageList(numberAt, (List) unsafe.getObject(t7, offset), getMessageFieldSchema(i14));
                    i15 += computeBoolSize;
                    break;
                case 28:
                    computeBoolSize = SchemaUtil.computeSizeByteStringList(numberAt, (List) unsafe.getObject(t7, offset));
                    i15 += computeBoolSize;
                    break;
                case 29:
                    computeBoolSize = SchemaUtil.computeSizeUInt32List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeBoolSize;
                    break;
                case 30:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeEnumList(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 31:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed32List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 32:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed64List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 33:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeSInt32List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 34:
                    z10 = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeSInt64List(numberAt, (List) unsafe.getObject(t7, offset), false);
                    i15 += computeSizeFixed32List;
                    break;
                case 35:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 36:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 37:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 38:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 39:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 40:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 41:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 42:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 43:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 44:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 45:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 46:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 47:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 48:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i10, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 49:
                    computeBoolSize = SchemaUtil.computeSizeGroupList(numberAt, (List) unsafe.getObject(t7, offset), getMessageFieldSchema(i14));
                    i15 += computeBoolSize;
                    break;
                case 50:
                    computeBoolSize = this.mapFieldSchema.getSerializedSize(numberAt, unsafe.getObject(t7, offset), getMapFieldDefaultEntry(i14));
                    i15 += computeBoolSize;
                    break;
                case 51:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeDoubleSize(numberAt, UserProfileInfo.Constant.NA_LAT_LON);
                        i15 += computeBoolSize;
                    }
                    break;
                case 52:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        i15 += computeBoolSize;
                    }
                    break;
                case 53:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeInt64Size(numberAt, oneofLongAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 54:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeUInt64Size(numberAt, oneofLongAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 55:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeInt32Size(numberAt, oneofIntAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 56:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        i15 += computeBoolSize;
                    }
                    break;
                case 57:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeSFixed32Size = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 58:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        i15 += computeBoolSize;
                    }
                    break;
                case 59:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        Object object2 = unsafe.getObject(t7, offset);
                        if (object2 instanceof ByteString) {
                            computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object2);
                        } else {
                            computeBoolSize = CodedOutputStream.computeStringSize(numberAt, (String) object2);
                        }
                        i15 += computeBoolSize;
                    }
                    break;
                case 60:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = SchemaUtil.computeSizeMessage(numberAt, unsafe.getObject(t7, offset), getMessageFieldSchema(i14));
                        i15 += computeBoolSize;
                    }
                    break;
                case 61:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) unsafe.getObject(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 62:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeUInt32Size(numberAt, oneofIntAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 63:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeEnumSize(numberAt, oneofIntAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 64:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeSFixed32Size = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        i15 += computeSFixed32Size;
                    }
                    break;
                case 65:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        i15 += computeBoolSize;
                    }
                    break;
                case 66:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeSInt32Size(numberAt, oneofIntAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 67:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeSInt64Size(numberAt, oneofLongAt(t7, offset));
                        i15 += computeBoolSize;
                    }
                    break;
                case 68:
                    if (isOneofPresent(t7, numberAt, i14)) {
                        computeBoolSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) unsafe.getObject(t7, offset), getMessageFieldSchema(i14));
                        i15 += computeBoolSize;
                    }
                    break;
            }
            i14 += 3;
            i12 = 1048575;
        }
        int unknownFieldsSerializedSize = i15 + getUnknownFieldsSerializedSize(this.unknownFieldSchema, t7);
        return this.hasExtensions ? unknownFieldsSerializedSize + this.extensionSchema.getExtensions(t7).getSerializedSize() : unknownFieldsSerializedSize;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x003d. Please report as an issue. */
    private int getSerializedSizeProto3(T t7) {
        int computeDoubleSize;
        int computeSizeFixed64ListNoTag;
        int computeTagSize;
        int computeUInt32SizeNoTag;
        Unsafe unsafe = UNSAFE;
        int i10 = 0;
        for (int i11 = 0; i11 < this.buffer.length; i11 += 3) {
            int typeAndOffsetAt = typeAndOffsetAt(i11);
            int type = type(typeAndOffsetAt);
            int numberAt = numberAt(i11);
            long offset = offset(typeAndOffsetAt);
            int i12 = (type < FieldType.DOUBLE_LIST_PACKED.id() || type > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i11 + 2] & 1048575;
            switch (type) {
                case 0:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeDoubleSize(numberAt, UserProfileInfo.Constant.NA_LAT_LON);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeInt64Size(numberAt, UnsafeUtil.getLong(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeUInt64Size(numberAt, UnsafeUtil.getLong(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 4:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeInt32Size(numberAt, UnsafeUtil.getInt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 5:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 6:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 7:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 8:
                    if (isFieldPresent(t7, i11)) {
                        Object object = UnsafeUtil.getObject(t7, offset);
                        if (object instanceof ByteString) {
                            computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object);
                        } else {
                            computeDoubleSize = CodedOutputStream.computeStringSize(numberAt, (String) object);
                        }
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 9:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = SchemaUtil.computeSizeMessage(numberAt, UnsafeUtil.getObject(t7, offset), getMessageFieldSchema(i11));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 10:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) UnsafeUtil.getObject(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 11:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeUInt32Size(numberAt, UnsafeUtil.getInt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 12:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeEnumSize(numberAt, UnsafeUtil.getInt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 13:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 14:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 15:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSInt32Size(numberAt, UnsafeUtil.getInt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 16:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSInt64Size(numberAt, UnsafeUtil.getLong(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 17:
                    if (isFieldPresent(t7, i11)) {
                        computeDoubleSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) UnsafeUtil.getObject(t7, offset), getMessageFieldSchema(i11));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 18:
                    computeDoubleSize = SchemaUtil.computeSizeFixed64List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 19:
                    computeDoubleSize = SchemaUtil.computeSizeFixed32List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 20:
                    computeDoubleSize = SchemaUtil.computeSizeInt64List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 21:
                    computeDoubleSize = SchemaUtil.computeSizeUInt64List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 22:
                    computeDoubleSize = SchemaUtil.computeSizeInt32List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 23:
                    computeDoubleSize = SchemaUtil.computeSizeFixed64List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 24:
                    computeDoubleSize = SchemaUtil.computeSizeFixed32List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 25:
                    computeDoubleSize = SchemaUtil.computeSizeBoolList(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 26:
                    computeDoubleSize = SchemaUtil.computeSizeStringList(numberAt, listAt(t7, offset));
                    i10 += computeDoubleSize;
                    break;
                case 27:
                    computeDoubleSize = SchemaUtil.computeSizeMessageList(numberAt, listAt(t7, offset), getMessageFieldSchema(i11));
                    i10 += computeDoubleSize;
                    break;
                case 28:
                    computeDoubleSize = SchemaUtil.computeSizeByteStringList(numberAt, listAt(t7, offset));
                    i10 += computeDoubleSize;
                    break;
                case 29:
                    computeDoubleSize = SchemaUtil.computeSizeUInt32List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 30:
                    computeDoubleSize = SchemaUtil.computeSizeEnumList(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 31:
                    computeDoubleSize = SchemaUtil.computeSizeFixed32List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 32:
                    computeDoubleSize = SchemaUtil.computeSizeFixed64List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 33:
                    computeDoubleSize = SchemaUtil.computeSizeSInt32List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 34:
                    computeDoubleSize = SchemaUtil.computeSizeSInt64List(numberAt, listAt(t7, offset), false);
                    i10 += computeDoubleSize;
                    break;
                case 35:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 36:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 37:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 38:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 39:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 40:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 41:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 42:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 43:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 44:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 45:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 46:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 47:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 48:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(t7, offset));
                    if (computeSizeFixed64ListNoTag <= 0) {
                        break;
                    } else {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t7, i12, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i10 += computeDoubleSize;
                        break;
                    }
                case 49:
                    computeDoubleSize = SchemaUtil.computeSizeGroupList(numberAt, listAt(t7, offset), getMessageFieldSchema(i11));
                    i10 += computeDoubleSize;
                    break;
                case 50:
                    computeDoubleSize = this.mapFieldSchema.getSerializedSize(numberAt, UnsafeUtil.getObject(t7, offset), getMapFieldDefaultEntry(i11));
                    i10 += computeDoubleSize;
                    break;
                case 51:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeDoubleSize(numberAt, UserProfileInfo.Constant.NA_LAT_LON);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeInt64Size(numberAt, oneofLongAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeUInt64Size(numberAt, oneofLongAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeInt32Size(numberAt, oneofIntAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        Object object2 = UnsafeUtil.getObject(t7, offset);
                        if (object2 instanceof ByteString) {
                            computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object2);
                        } else {
                            computeDoubleSize = CodedOutputStream.computeStringSize(numberAt, (String) object2);
                        }
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = SchemaUtil.computeSizeMessage(numberAt, UnsafeUtil.getObject(t7, offset), getMessageFieldSchema(i11));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) UnsafeUtil.getObject(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeUInt32Size(numberAt, oneofIntAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeEnumSize(numberAt, oneofIntAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSInt32Size(numberAt, oneofIntAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeSInt64Size(numberAt, oneofLongAt(t7, offset));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (isOneofPresent(t7, numberAt, i11)) {
                        computeDoubleSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) UnsafeUtil.getObject(t7, offset), getMessageFieldSchema(i11));
                        i10 += computeDoubleSize;
                        break;
                    } else {
                        break;
                    }
            }
        }
        return i10 + getUnknownFieldsSerializedSize(this.unknownFieldSchema, t7);
    }

    private <UT, UB> int getUnknownFieldsSerializedSize(UnknownFieldSchema<UT, UB> unknownFieldSchema, T t7) {
        return unknownFieldSchema.getSerializedSize(unknownFieldSchema.getFromMessage(t7));
    }

    private static <T> int intAt(T t7, long j10) {
        return UnsafeUtil.getInt(t7, j10);
    }

    private static boolean isEnforceUtf8(int i10) {
        return (i10 & ENFORCE_UTF8_MASK) != 0;
    }

    private boolean isFieldPresent(T t7, int i10, int i11, int i12, int i13) {
        if (i11 == 1048575) {
            return isFieldPresent(t7, i10);
        }
        return (i12 & i13) != 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <N> boolean isListInitialized(Object obj, int i10, int i11) {
        List list = (List) UnsafeUtil.getObject(obj, offset(i10));
        if (list.isEmpty()) {
            return true;
        }
        Schema messageFieldSchema = getMessageFieldSchema(i11);
        for (int i12 = 0; i12 < list.size(); i12++) {
            if (!messageFieldSchema.isInitialized(list.get(i12))) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7, types: [com.google.protobuf.Schema] */
    private boolean isMapInitialized(T t7, int i10, int i11) {
        Map<?, ?> forMapData = this.mapFieldSchema.forMapData(UnsafeUtil.getObject(t7, offset(i10)));
        if (forMapData.isEmpty()) {
            return true;
        }
        if (this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i11)).valueType.getJavaType() != WireFormat.JavaType.MESSAGE) {
            return true;
        }
        ?? r32 = 0;
        for (Object obj : forMapData.values()) {
            r32 = r32;
            if (r32 == 0) {
                r32 = Protobuf.getInstance().schemaFor((Class) obj.getClass());
            }
            if (!r32.isInitialized(obj)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isMutable(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof GeneratedMessageLite) {
            return ((GeneratedMessageLite) obj).isMutable();
        }
        return true;
    }

    private boolean isOneofCaseEqual(T t7, T t10, int i10) {
        long presenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i10) & 1048575;
        return UnsafeUtil.getInt(t7, presenceMaskAndOffsetAt) == UnsafeUtil.getInt(t10, presenceMaskAndOffsetAt);
    }

    private boolean isOneofPresent(T t7, int i10, int i11) {
        return UnsafeUtil.getInt(t7, (long) (presenceMaskAndOffsetAt(i11) & 1048575)) == i10;
    }

    private static boolean isRequired(int i10) {
        return (i10 & REQUIRED_MASK) != 0;
    }

    private static List<?> listAt(Object obj, long j10) {
        return (List) UnsafeUtil.getObject(obj, j10);
    }

    private static <T> long longAt(T t7, long j10) {
        return UnsafeUtil.getLong(t7, j10);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x00c4. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:37:0x06b3 A[LOOP:2: B:35:0x06af->B:37:0x06b3, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x06c8  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0649 A[Catch: all -> 0x06a1, TRY_LEAVE, TryCatch #0 {all -> 0x06a1, blocks: (B:16:0x061a, B:43:0x0643, B:45:0x0649, B:58:0x0671, B:59:0x0676), top: B:15:0x061a }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x066f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private <UT, UB, ET extends FieldSet.FieldDescriptorLite<ET>> void mergeFromHelper(UnknownFieldSchema<UT, UB> unknownFieldSchema, ExtensionSchema<ET> extensionSchema, T t7, Reader reader, ExtensionRegistryLite extensionRegistryLite) {
        UnknownFieldSchema unknownFieldSchema2;
        T t10;
        int i10;
        Object obj;
        T t11;
        FieldSet<ET> mutableExtensions;
        ExtensionRegistryLite extensionRegistryLite2;
        Object obj2;
        UnknownFieldSchema unknownFieldSchema3 = unknownFieldSchema;
        T t12 = t7;
        ExtensionRegistryLite extensionRegistryLite3 = extensionRegistryLite;
        Object obj3 = null;
        FieldSet<ET> fieldSet = null;
        while (true) {
            try {
                int fieldNumber = reader.getFieldNumber();
                int positionForFieldNumber = positionForFieldNumber(fieldNumber);
                if (positionForFieldNumber < 0) {
                    if (fieldNumber == Integer.MAX_VALUE) {
                        Object obj4 = obj3;
                        for (int i11 = this.checkInitializedCount; i11 < this.repeatedFieldOffsetStart; i11++) {
                            obj4 = filterMapUnknownEnumValues(t7, this.intArray[i11], obj4, unknownFieldSchema, t7);
                        }
                        if (obj4 != null) {
                            unknownFieldSchema3.setBuilderToMessage(t12, obj4);
                            return;
                        }
                        return;
                    }
                    try {
                        Object findExtensionByNumber = !this.hasExtensions ? null : extensionSchema.findExtensionByNumber(extensionRegistryLite3, this.defaultInstance, fieldNumber);
                        if (findExtensionByNumber != null) {
                            if (fieldSet == null) {
                                try {
                                    mutableExtensions = extensionSchema.getMutableExtensions(t7);
                                } catch (Throwable th) {
                                    th = th;
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    t10 = t12;
                                    obj = obj3;
                                    while (i10 < this.repeatedFieldOffsetStart) {
                                    }
                                    if (obj != null) {
                                    }
                                    throw th;
                                }
                            } else {
                                mutableExtensions = fieldSet;
                            }
                            t11 = t12;
                            try {
                                obj3 = extensionSchema.parseExtension(t7, reader, findExtensionByNumber, extensionRegistryLite, mutableExtensions, obj3, unknownFieldSchema);
                                fieldSet = mutableExtensions;
                            } catch (Throwable th2) {
                                th = th2;
                                t10 = t11;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj = obj3;
                                while (i10 < this.repeatedFieldOffsetStart) {
                                }
                                if (obj != null) {
                                }
                                throw th;
                            }
                        } else {
                            t11 = t12;
                            if (unknownFieldSchema3.shouldDiscardUnknownFields(reader)) {
                                if (!reader.skipField()) {
                                }
                            } else {
                                if (obj3 == null) {
                                    obj3 = unknownFieldSchema3.getBuilderFromMessage(t11);
                                }
                                if (!unknownFieldSchema3.mergeOneFieldFrom(obj3, reader)) {
                                }
                            }
                        }
                        t12 = t11;
                    } catch (Throwable th3) {
                        th = th3;
                        t10 = t12;
                    }
                } else {
                    t10 = t12;
                    try {
                        int typeAndOffsetAt = typeAndOffsetAt(positionForFieldNumber);
                        switch (type(typeAndOffsetAt)) {
                            case 0:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putDouble(t10, offset(typeAndOffsetAt), reader.readDouble());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 1:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putFloat(t10, offset(typeAndOffsetAt), reader.readFloat());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 2:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putLong(t10, offset(typeAndOffsetAt), reader.readInt64());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 3:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putLong(t10, offset(typeAndOffsetAt), reader.readUInt64());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 4:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putInt(t10, offset(typeAndOffsetAt), reader.readInt32());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 5:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putLong(t10, offset(typeAndOffsetAt), reader.readFixed64());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 6:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putInt(t10, offset(typeAndOffsetAt), reader.readFixed32());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 7:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putBoolean(t10, offset(typeAndOffsetAt), reader.readBool());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 8:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                readString(t10, typeAndOffsetAt, reader);
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 9:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                MessageLite messageLite = (MessageLite) mutableMessageFieldForMerge(t10, positionForFieldNumber);
                                reader.mergeMessageField(messageLite, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite2);
                                storeMessageField(t10, positionForFieldNumber, messageLite);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 10:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), reader.readBytes());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 11:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putInt(t10, offset(typeAndOffsetAt), reader.readUInt32());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 12:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                int readEnum = reader.readEnum();
                                Internal.EnumVerifier enumFieldVerifier = getEnumFieldVerifier(positionForFieldNumber);
                                if (enumFieldVerifier != null && !enumFieldVerifier.isInRange(readEnum)) {
                                    obj3 = SchemaUtil.storeUnknownEnum(t10, fieldNumber, readEnum, obj2, unknownFieldSchema2);
                                    t12 = t10;
                                    extensionRegistryLite3 = extensionRegistryLite2;
                                    unknownFieldSchema3 = unknownFieldSchema2;
                                    break;
                                }
                                UnsafeUtil.putInt(t10, offset(typeAndOffsetAt), readEnum);
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 13:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putInt(t10, offset(typeAndOffsetAt), reader.readSFixed32());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 14:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putLong(t10, offset(typeAndOffsetAt), reader.readSFixed64());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 15:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putInt(t10, offset(typeAndOffsetAt), reader.readSInt32());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 16:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                UnsafeUtil.putLong(t10, offset(typeAndOffsetAt), reader.readSInt64());
                                setFieldPresent(t10, positionForFieldNumber);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 17:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                MessageLite messageLite2 = (MessageLite) mutableMessageFieldForMerge(t10, positionForFieldNumber);
                                reader.mergeGroupField(messageLite2, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite2);
                                storeMessageField(t10, positionForFieldNumber, messageLite2);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 18:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readDoubleList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 19:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readFloatList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 20:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readInt64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 21:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readUInt64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 22:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readInt32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 23:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readFixed64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 24:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readFixed32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 25:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readBoolList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 26:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                readStringList(t10, typeAndOffsetAt, reader);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 27:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                readMessageList(t7, typeAndOffsetAt, reader, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 28:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readBytesList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 29:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readUInt32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 30:
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                List<Integer> mutableListAt = this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt));
                                reader.readEnumList(mutableListAt);
                                obj3 = SchemaUtil.filterUnknownEnumList(t7, fieldNumber, mutableListAt, getEnumFieldVerifier(positionForFieldNumber), obj3, unknownFieldSchema);
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 31:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSFixed32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 32:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSFixed64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 33:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSInt32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 34:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSInt64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 35:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readDoubleList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 36:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readFloatList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 37:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readInt64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 38:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readUInt64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 39:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readInt32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 40:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readFixed64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 41:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readFixed32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 42:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readBoolList(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 43:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readUInt32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 44:
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                List<Integer> mutableListAt2 = this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt));
                                reader.readEnumList(mutableListAt2);
                                obj3 = SchemaUtil.filterUnknownEnumList(t7, fieldNumber, mutableListAt2, getEnumFieldVerifier(positionForFieldNumber), obj3, unknownFieldSchema);
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 45:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSFixed32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 46:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSFixed64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 47:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSInt32List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 48:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                reader.readSInt64List(this.listFieldSchema.mutableListAt(t10, offset(typeAndOffsetAt)));
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 49:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                readGroupList(t7, offset(typeAndOffsetAt), reader, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite);
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 50:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                try {
                                    mergeMap(t7, positionForFieldNumber, getMapFieldDefaultEntry(positionForFieldNumber), extensionRegistryLite, reader);
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    obj3 = obj2;
                                } catch (InvalidProtocolBufferException.InvalidWireTypeException unused) {
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    obj3 = obj2;
                                    if (!unknownFieldSchema2.shouldDiscardUnknownFields(reader)) {
                                    }
                                    t12 = t10;
                                    extensionRegistryLite3 = extensionRegistryLite2;
                                    unknownFieldSchema3 = unknownFieldSchema2;
                                } catch (Throwable th4) {
                                    th = th4;
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    obj3 = obj2;
                                    obj = obj3;
                                    while (i10 < this.repeatedFieldOffsetStart) {
                                    }
                                    if (obj != null) {
                                    }
                                    throw th;
                                }
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 51:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Double.valueOf(reader.readDouble()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 52:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Float.valueOf(reader.readFloat()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 53:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Long.valueOf(reader.readInt64()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 54:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Long.valueOf(reader.readUInt64()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 55:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Integer.valueOf(reader.readInt32()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 56:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Long.valueOf(reader.readFixed64()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 57:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Integer.valueOf(reader.readFixed32()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 58:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Boolean.valueOf(reader.readBool()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 59:
                                readString(t10, typeAndOffsetAt, reader);
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 60:
                                MessageLite messageLite3 = (MessageLite) mutableOneofMessageFieldForMerge(t10, fieldNumber, positionForFieldNumber);
                                reader.mergeMessageField(messageLite3, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite3);
                                storeOneofMessageField(t10, fieldNumber, positionForFieldNumber, messageLite3);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 61:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), reader.readBytes());
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 62:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Integer.valueOf(reader.readUInt32()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 63:
                                int readEnum2 = reader.readEnum();
                                Internal.EnumVerifier enumFieldVerifier2 = getEnumFieldVerifier(positionForFieldNumber);
                                if (enumFieldVerifier2 != null && !enumFieldVerifier2.isInRange(readEnum2)) {
                                    obj3 = SchemaUtil.storeUnknownEnum(t10, fieldNumber, readEnum2, obj3, unknownFieldSchema3);
                                    extensionRegistryLite2 = extensionRegistryLite3;
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    t12 = t10;
                                    extensionRegistryLite3 = extensionRegistryLite2;
                                    unknownFieldSchema3 = unknownFieldSchema2;
                                    break;
                                }
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Integer.valueOf(readEnum2));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 64:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Integer.valueOf(reader.readSFixed32()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 65:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Long.valueOf(reader.readSFixed64()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 66:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Integer.valueOf(reader.readSInt32()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 67:
                                UnsafeUtil.putObject(t10, offset(typeAndOffsetAt), Long.valueOf(reader.readSInt64()));
                                setOneofPresent(t10, fieldNumber, positionForFieldNumber);
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                obj3 = obj2;
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            case 68:
                                try {
                                    MessageLite messageLite4 = (MessageLite) mutableOneofMessageFieldForMerge(t10, fieldNumber, positionForFieldNumber);
                                    reader.mergeGroupField(messageLite4, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite3);
                                    storeOneofMessageField(t10, fieldNumber, positionForFieldNumber, messageLite4);
                                    obj2 = obj3;
                                    extensionRegistryLite2 = extensionRegistryLite3;
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    obj3 = obj2;
                                } catch (InvalidProtocolBufferException.InvalidWireTypeException unused2) {
                                    extensionRegistryLite2 = extensionRegistryLite3;
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    if (!unknownFieldSchema2.shouldDiscardUnknownFields(reader)) {
                                    }
                                    t12 = t10;
                                    extensionRegistryLite3 = extensionRegistryLite2;
                                    unknownFieldSchema3 = unknownFieldSchema2;
                                } catch (Throwable th5) {
                                    th = th5;
                                    unknownFieldSchema2 = unknownFieldSchema3;
                                    obj = obj3;
                                    while (i10 < this.repeatedFieldOffsetStart) {
                                    }
                                    if (obj != null) {
                                    }
                                    throw th;
                                }
                                t12 = t10;
                                extensionRegistryLite3 = extensionRegistryLite2;
                                unknownFieldSchema3 = unknownFieldSchema2;
                                break;
                            default:
                                obj2 = obj3;
                                extensionRegistryLite2 = extensionRegistryLite3;
                                unknownFieldSchema2 = unknownFieldSchema3;
                                if (obj2 == null) {
                                    try {
                                        obj3 = unknownFieldSchema2.getBuilderFromMessage(t10);
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused3) {
                                        obj3 = obj2;
                                        if (!unknownFieldSchema2.shouldDiscardUnknownFields(reader)) {
                                        }
                                        t12 = t10;
                                        extensionRegistryLite3 = extensionRegistryLite2;
                                        unknownFieldSchema3 = unknownFieldSchema2;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        obj3 = obj2;
                                        obj = obj3;
                                        while (i10 < this.repeatedFieldOffsetStart) {
                                        }
                                        if (obj != null) {
                                        }
                                        throw th;
                                    }
                                } else {
                                    obj3 = obj2;
                                }
                                try {
                                    try {
                                        if (!unknownFieldSchema2.mergeOneFieldFrom(obj3, reader)) {
                                            Object obj5 = obj3;
                                            for (int i12 = this.checkInitializedCount; i12 < this.repeatedFieldOffsetStart; i12++) {
                                                obj5 = filterMapUnknownEnumValues(t7, this.intArray[i12], obj5, unknownFieldSchema, t7);
                                            }
                                            if (obj5 != null) {
                                                unknownFieldSchema2.setBuilderToMessage(t10, obj5);
                                                return;
                                            }
                                            return;
                                        }
                                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused4) {
                                        if (!unknownFieldSchema2.shouldDiscardUnknownFields(reader)) {
                                            if (!reader.skipField()) {
                                                Object obj6 = obj3;
                                                for (int i13 = this.checkInitializedCount; i13 < this.repeatedFieldOffsetStart; i13++) {
                                                    obj6 = filterMapUnknownEnumValues(t7, this.intArray[i13], obj6, unknownFieldSchema, t7);
                                                }
                                                if (obj6 != null) {
                                                    unknownFieldSchema2.setBuilderToMessage(t10, obj6);
                                                    return;
                                                }
                                                return;
                                            }
                                        } else {
                                            if (obj3 == null) {
                                                obj3 = unknownFieldSchema2.getBuilderFromMessage(t10);
                                            }
                                            if (!unknownFieldSchema2.mergeOneFieldFrom(obj3, reader)) {
                                                Object obj7 = obj3;
                                                for (int i14 = this.checkInitializedCount; i14 < this.repeatedFieldOffsetStart; i14++) {
                                                    obj7 = filterMapUnknownEnumValues(t7, this.intArray[i14], obj7, unknownFieldSchema, t7);
                                                }
                                                if (obj7 != null) {
                                                    unknownFieldSchema2.setBuilderToMessage(t10, obj7);
                                                    return;
                                                }
                                                return;
                                            }
                                        }
                                        t12 = t10;
                                        extensionRegistryLite3 = extensionRegistryLite2;
                                        unknownFieldSchema3 = unknownFieldSchema2;
                                    }
                                    t12 = t10;
                                    extensionRegistryLite3 = extensionRegistryLite2;
                                    unknownFieldSchema3 = unknownFieldSchema2;
                                } catch (Throwable th7) {
                                    th = th7;
                                    obj = obj3;
                                    for (i10 = this.checkInitializedCount; i10 < this.repeatedFieldOffsetStart; i10++) {
                                        obj = filterMapUnknownEnumValues(t7, this.intArray[i10], obj, unknownFieldSchema, t7);
                                    }
                                    if (obj != null) {
                                        unknownFieldSchema2.setBuilderToMessage(t10, obj);
                                    }
                                    throw th;
                                }
                        }
                    } catch (Throwable th8) {
                        th = th8;
                    }
                }
            } catch (Throwable th9) {
                th = th9;
            }
        }
        int i15 = this.checkInitializedCount;
        Object obj8 = obj3;
        while (i15 < this.repeatedFieldOffsetStart) {
            obj8 = filterMapUnknownEnumValues(t7, this.intArray[i15], obj8, unknownFieldSchema, t7);
            i15++;
            t11 = t11;
        }
        T t13 = t11;
        if (obj8 != null) {
            unknownFieldSchema3.setBuilderToMessage(t13, obj8);
        }
    }

    private final <K, V> void mergeMap(Object obj, int i10, Object obj2, ExtensionRegistryLite extensionRegistryLite, Reader reader) {
        long offset = offset(typeAndOffsetAt(i10));
        Object object = UnsafeUtil.getObject(obj, offset);
        if (object == null) {
            object = this.mapFieldSchema.newMapField(obj2);
            UnsafeUtil.putObject(obj, offset, object);
        } else if (this.mapFieldSchema.isImmutable(object)) {
            Object newMapField = this.mapFieldSchema.newMapField(obj2);
            this.mapFieldSchema.mergeFrom(newMapField, object);
            UnsafeUtil.putObject(obj, offset, newMapField);
            object = newMapField;
        }
        reader.readMap(this.mapFieldSchema.forMutableMapData(object), this.mapFieldSchema.forMapMetadata(obj2), extensionRegistryLite);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void mergeMessage(T t7, T t10, int i10) {
        if (isFieldPresent(t10, i10)) {
            long offset = offset(typeAndOffsetAt(i10));
            Unsafe unsafe = UNSAFE;
            Object object = unsafe.getObject(t10, offset);
            if (object != null) {
                Schema messageFieldSchema = getMessageFieldSchema(i10);
                if (!isFieldPresent(t7, i10)) {
                    if (!isMutable(object)) {
                        unsafe.putObject(t7, offset, object);
                    } else {
                        Object newInstance = messageFieldSchema.newInstance();
                        messageFieldSchema.mergeFrom(newInstance, object);
                        unsafe.putObject(t7, offset, newInstance);
                    }
                    setFieldPresent(t7, i10);
                    return;
                }
                Object object2 = unsafe.getObject(t7, offset);
                if (!isMutable(object2)) {
                    Object newInstance2 = messageFieldSchema.newInstance();
                    messageFieldSchema.mergeFrom(newInstance2, object2);
                    unsafe.putObject(t7, offset, newInstance2);
                    object2 = newInstance2;
                }
                messageFieldSchema.mergeFrom(object2, object);
                return;
            }
            throw new IllegalStateException("Source subfield " + numberAt(i10) + " is present but null: " + t10);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void mergeOneofMessage(T t7, T t10, int i10) {
        int numberAt = numberAt(i10);
        if (isOneofPresent(t10, numberAt, i10)) {
            long offset = offset(typeAndOffsetAt(i10));
            Unsafe unsafe = UNSAFE;
            Object object = unsafe.getObject(t10, offset);
            if (object != null) {
                Schema messageFieldSchema = getMessageFieldSchema(i10);
                if (!isOneofPresent(t7, numberAt, i10)) {
                    if (!isMutable(object)) {
                        unsafe.putObject(t7, offset, object);
                    } else {
                        Object newInstance = messageFieldSchema.newInstance();
                        messageFieldSchema.mergeFrom(newInstance, object);
                        unsafe.putObject(t7, offset, newInstance);
                    }
                    setOneofPresent(t7, numberAt, i10);
                    return;
                }
                Object object2 = unsafe.getObject(t7, offset);
                if (!isMutable(object2)) {
                    Object newInstance2 = messageFieldSchema.newInstance();
                    messageFieldSchema.mergeFrom(newInstance2, object2);
                    unsafe.putObject(t7, offset, newInstance2);
                    object2 = newInstance2;
                }
                messageFieldSchema.mergeFrom(object2, object);
                return;
            }
            throw new IllegalStateException("Source subfield " + numberAt(i10) + " is present but null: " + t10);
        }
    }

    private void mergeSingleField(T t7, T t10, int i10) {
        int typeAndOffsetAt = typeAndOffsetAt(i10);
        long offset = offset(typeAndOffsetAt);
        int numberAt = numberAt(i10);
        switch (type(typeAndOffsetAt)) {
            case 0:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putDouble(t7, offset, UnsafeUtil.getDouble(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 1:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putFloat(t7, offset, UnsafeUtil.getFloat(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 2:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putLong(t7, offset, UnsafeUtil.getLong(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 3:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putLong(t7, offset, UnsafeUtil.getLong(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 4:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putInt(t7, offset, UnsafeUtil.getInt(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 5:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putLong(t7, offset, UnsafeUtil.getLong(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 6:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putInt(t7, offset, UnsafeUtil.getInt(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 7:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putBoolean(t7, offset, UnsafeUtil.getBoolean(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 8:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putObject(t7, offset, UnsafeUtil.getObject(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 9:
                mergeMessage(t7, t10, i10);
                return;
            case 10:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putObject(t7, offset, UnsafeUtil.getObject(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 11:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putInt(t7, offset, UnsafeUtil.getInt(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 12:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putInt(t7, offset, UnsafeUtil.getInt(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 13:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putInt(t7, offset, UnsafeUtil.getInt(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 14:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putLong(t7, offset, UnsafeUtil.getLong(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 15:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putInt(t7, offset, UnsafeUtil.getInt(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 16:
                if (isFieldPresent(t10, i10)) {
                    UnsafeUtil.putLong(t7, offset, UnsafeUtil.getLong(t10, offset));
                    setFieldPresent(t7, i10);
                    return;
                }
                return;
            case 17:
                mergeMessage(t7, t10, i10);
                return;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                this.listFieldSchema.mergeListsAt(t7, t10, offset);
                return;
            case 50:
                SchemaUtil.mergeMap(this.mapFieldSchema, t7, t10, offset);
                return;
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
                if (isOneofPresent(t10, numberAt, i10)) {
                    UnsafeUtil.putObject(t7, offset, UnsafeUtil.getObject(t10, offset));
                    setOneofPresent(t7, numberAt, i10);
                    return;
                }
                return;
            case 60:
                mergeOneofMessage(t7, t10, i10);
                return;
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                if (isOneofPresent(t10, numberAt, i10)) {
                    UnsafeUtil.putObject(t7, offset, UnsafeUtil.getObject(t10, offset));
                    setOneofPresent(t7, numberAt, i10);
                    return;
                }
                return;
            case 68:
                mergeOneofMessage(t7, t10, i10);
                return;
            default:
                return;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Object mutableMessageFieldForMerge(T t7, int i10) {
        Schema messageFieldSchema = getMessageFieldSchema(i10);
        long offset = offset(typeAndOffsetAt(i10));
        if (!isFieldPresent(t7, i10)) {
            return messageFieldSchema.newInstance();
        }
        Object object = UNSAFE.getObject(t7, offset);
        if (isMutable(object)) {
            return object;
        }
        Object newInstance = messageFieldSchema.newInstance();
        if (object != null) {
            messageFieldSchema.mergeFrom(newInstance, object);
        }
        return newInstance;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Object mutableOneofMessageFieldForMerge(T t7, int i10, int i11) {
        Schema messageFieldSchema = getMessageFieldSchema(i11);
        if (!isOneofPresent(t7, i10, i11)) {
            return messageFieldSchema.newInstance();
        }
        Object object = UNSAFE.getObject(t7, offset(typeAndOffsetAt(i11)));
        if (isMutable(object)) {
            return object;
        }
        Object newInstance = messageFieldSchema.newInstance();
        if (object != null) {
            messageFieldSchema.mergeFrom(newInstance, object);
        }
        return newInstance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> MessageSchema<T> newSchema(Class<T> cls, MessageInfo messageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        if (messageInfo instanceof RawMessageInfo) {
            return newSchemaForRawMessageInfo((RawMessageInfo) messageInfo, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
        }
        return newSchemaForMessageInfo((StructuralMessageInfo) messageInfo, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    static <T> MessageSchema<T> newSchemaForMessageInfo(StructuralMessageInfo structuralMessageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        int fieldNumber;
        int fieldNumber2;
        int i10;
        boolean z10 = structuralMessageInfo.getSyntax() == ProtoSyntax.PROTO3;
        FieldInfo[] fields = structuralMessageInfo.getFields();
        if (fields.length == 0) {
            fieldNumber = 0;
            fieldNumber2 = 0;
        } else {
            fieldNumber = fields[0].getFieldNumber();
            fieldNumber2 = fields[fields.length - 1].getFieldNumber();
        }
        int length = fields.length;
        int[] iArr = new int[length * 3];
        Object[] objArr = new Object[length * 2];
        int i11 = 0;
        int i12 = 0;
        for (FieldInfo fieldInfo : fields) {
            if (fieldInfo.getType() == FieldType.MAP) {
                i11++;
            } else if (fieldInfo.getType().id() >= 18 && fieldInfo.getType().id() <= 49) {
                i12++;
            }
        }
        int[] iArr2 = i11 > 0 ? new int[i11] : null;
        int[] iArr3 = i12 > 0 ? new int[i12] : null;
        int[] checkInitialized = structuralMessageInfo.getCheckInitialized();
        if (checkInitialized == null) {
            checkInitialized = EMPTY_INT_ARRAY;
        }
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        while (i13 < fields.length) {
            FieldInfo fieldInfo2 = fields[i13];
            int fieldNumber3 = fieldInfo2.getFieldNumber();
            storeFieldData(fieldInfo2, iArr, i14, objArr);
            if (i15 < checkInitialized.length && checkInitialized[i15] == fieldNumber3) {
                checkInitialized[i15] = i14;
                i15++;
            }
            if (fieldInfo2.getType() == FieldType.MAP) {
                iArr2[i16] = i14;
                i16++;
            } else if (fieldInfo2.getType().id() >= 18 && fieldInfo2.getType().id() <= 49) {
                i10 = i14;
                iArr3[i17] = (int) UnsafeUtil.objectFieldOffset(fieldInfo2.getField());
                i17++;
                i13++;
                i14 = i10 + 3;
            }
            i10 = i14;
            i13++;
            i14 = i10 + 3;
        }
        if (iArr2 == null) {
            iArr2 = EMPTY_INT_ARRAY;
        }
        if (iArr3 == null) {
            iArr3 = EMPTY_INT_ARRAY;
        }
        int[] iArr4 = new int[checkInitialized.length + iArr2.length + iArr3.length];
        System.arraycopy(checkInitialized, 0, iArr4, 0, checkInitialized.length);
        System.arraycopy(iArr2, 0, iArr4, checkInitialized.length, iArr2.length);
        System.arraycopy(iArr3, 0, iArr4, checkInitialized.length + iArr2.length, iArr3.length);
        return new MessageSchema<>(iArr, objArr, fieldNumber, fieldNumber2, structuralMessageInfo.getDefaultInstance(), z10, true, iArr4, checkInitialized.length, checkInitialized.length + iArr2.length, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x031a  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x031d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x024e  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0251  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static <T> MessageSchema<T> newSchemaForRawMessageInfo(RawMessageInfo rawMessageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        int i10;
        int charAt;
        int charAt2;
        int charAt3;
        int charAt4;
        int charAt5;
        int[] iArr;
        int i11;
        int i12;
        int i13;
        char charAt6;
        int i14;
        char charAt7;
        int i15;
        char charAt8;
        int i16;
        char charAt9;
        int i17;
        char charAt10;
        int i18;
        char charAt11;
        int i19;
        char charAt12;
        int i20;
        char charAt13;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        boolean z10;
        int i26;
        int objectFieldOffset;
        String str;
        boolean z11;
        int i27;
        int i28;
        int i29;
        java.lang.reflect.Field reflectField;
        char charAt14;
        int i30;
        int i31;
        Object obj;
        java.lang.reflect.Field reflectField2;
        Object obj2;
        java.lang.reflect.Field reflectField3;
        int i32;
        char charAt15;
        int i33;
        char charAt16;
        int i34;
        char charAt17;
        int i35;
        char charAt18;
        boolean z12 = rawMessageInfo.getSyntax() == ProtoSyntax.PROTO3;
        String stringInfo = rawMessageInfo.getStringInfo();
        int length = stringInfo.length();
        char c10 = 55296;
        if (stringInfo.charAt(0) >= 55296) {
            int i36 = 1;
            while (true) {
                i10 = i36 + 1;
                if (stringInfo.charAt(i36) < 55296) {
                    break;
                }
                i36 = i10;
            }
        } else {
            i10 = 1;
        }
        int i37 = i10 + 1;
        int charAt19 = stringInfo.charAt(i10);
        if (charAt19 >= 55296) {
            int i38 = charAt19 & 8191;
            int i39 = 13;
            while (true) {
                i35 = i37 + 1;
                charAt18 = stringInfo.charAt(i37);
                if (charAt18 < 55296) {
                    break;
                }
                i38 |= (charAt18 & 8191) << i39;
                i39 += 13;
                i37 = i35;
            }
            charAt19 = i38 | (charAt18 << i39);
            i37 = i35;
        }
        if (charAt19 == 0) {
            charAt = 0;
            charAt2 = 0;
            charAt3 = 0;
            charAt4 = 0;
            charAt5 = 0;
            i11 = 0;
            iArr = EMPTY_INT_ARRAY;
            i12 = 0;
        } else {
            int i40 = i37 + 1;
            int charAt20 = stringInfo.charAt(i37);
            if (charAt20 >= 55296) {
                int i41 = charAt20 & 8191;
                int i42 = 13;
                while (true) {
                    i20 = i40 + 1;
                    charAt13 = stringInfo.charAt(i40);
                    if (charAt13 < 55296) {
                        break;
                    }
                    i41 |= (charAt13 & 8191) << i42;
                    i42 += 13;
                    i40 = i20;
                }
                charAt20 = i41 | (charAt13 << i42);
                i40 = i20;
            }
            int i43 = i40 + 1;
            int charAt21 = stringInfo.charAt(i40);
            if (charAt21 >= 55296) {
                int i44 = charAt21 & 8191;
                int i45 = 13;
                while (true) {
                    i19 = i43 + 1;
                    charAt12 = stringInfo.charAt(i43);
                    if (charAt12 < 55296) {
                        break;
                    }
                    i44 |= (charAt12 & 8191) << i45;
                    i45 += 13;
                    i43 = i19;
                }
                charAt21 = i44 | (charAt12 << i45);
                i43 = i19;
            }
            int i46 = i43 + 1;
            charAt = stringInfo.charAt(i43);
            if (charAt >= 55296) {
                int i47 = charAt & 8191;
                int i48 = 13;
                while (true) {
                    i18 = i46 + 1;
                    charAt11 = stringInfo.charAt(i46);
                    if (charAt11 < 55296) {
                        break;
                    }
                    i47 |= (charAt11 & 8191) << i48;
                    i48 += 13;
                    i46 = i18;
                }
                charAt = i47 | (charAt11 << i48);
                i46 = i18;
            }
            int i49 = i46 + 1;
            charAt2 = stringInfo.charAt(i46);
            if (charAt2 >= 55296) {
                int i50 = charAt2 & 8191;
                int i51 = 13;
                while (true) {
                    i17 = i49 + 1;
                    charAt10 = stringInfo.charAt(i49);
                    if (charAt10 < 55296) {
                        break;
                    }
                    i50 |= (charAt10 & 8191) << i51;
                    i51 += 13;
                    i49 = i17;
                }
                charAt2 = i50 | (charAt10 << i51);
                i49 = i17;
            }
            int i52 = i49 + 1;
            charAt3 = stringInfo.charAt(i49);
            if (charAt3 >= 55296) {
                int i53 = charAt3 & 8191;
                int i54 = 13;
                while (true) {
                    i16 = i52 + 1;
                    charAt9 = stringInfo.charAt(i52);
                    if (charAt9 < 55296) {
                        break;
                    }
                    i53 |= (charAt9 & 8191) << i54;
                    i54 += 13;
                    i52 = i16;
                }
                charAt3 = i53 | (charAt9 << i54);
                i52 = i16;
            }
            int i55 = i52 + 1;
            charAt4 = stringInfo.charAt(i52);
            if (charAt4 >= 55296) {
                int i56 = charAt4 & 8191;
                int i57 = 13;
                while (true) {
                    i15 = i55 + 1;
                    charAt8 = stringInfo.charAt(i55);
                    if (charAt8 < 55296) {
                        break;
                    }
                    i56 |= (charAt8 & 8191) << i57;
                    i57 += 13;
                    i55 = i15;
                }
                charAt4 = i56 | (charAt8 << i57);
                i55 = i15;
            }
            int i58 = i55 + 1;
            int charAt22 = stringInfo.charAt(i55);
            if (charAt22 >= 55296) {
                int i59 = charAt22 & 8191;
                int i60 = 13;
                while (true) {
                    i14 = i58 + 1;
                    charAt7 = stringInfo.charAt(i58);
                    if (charAt7 < 55296) {
                        break;
                    }
                    i59 |= (charAt7 & 8191) << i60;
                    i60 += 13;
                    i58 = i14;
                }
                charAt22 = i59 | (charAt7 << i60);
                i58 = i14;
            }
            int i61 = i58 + 1;
            charAt5 = stringInfo.charAt(i58);
            if (charAt5 >= 55296) {
                int i62 = charAt5 & 8191;
                int i63 = 13;
                while (true) {
                    i13 = i61 + 1;
                    charAt6 = stringInfo.charAt(i61);
                    if (charAt6 < 55296) {
                        break;
                    }
                    i62 |= (charAt6 & 8191) << i63;
                    i63 += 13;
                    i61 = i13;
                }
                charAt5 = i62 | (charAt6 << i63);
                i61 = i13;
            }
            iArr = new int[charAt5 + charAt4 + charAt22];
            i11 = (charAt20 * 2) + charAt21;
            i12 = charAt20;
            i37 = i61;
        }
        Unsafe unsafe = UNSAFE;
        Object[] objects = rawMessageInfo.getObjects();
        Class<?> cls = rawMessageInfo.getDefaultInstance().getClass();
        int[] iArr2 = new int[charAt3 * 3];
        Object[] objArr = new Object[charAt3 * 2];
        int i64 = charAt5 + charAt4;
        int i65 = charAt5;
        int i66 = i64;
        int i67 = 0;
        int i68 = 0;
        while (i37 < length) {
            int i69 = i37 + 1;
            int charAt23 = stringInfo.charAt(i37);
            if (charAt23 >= c10) {
                int i70 = charAt23 & 8191;
                int i71 = i69;
                int i72 = 13;
                while (true) {
                    i34 = i71 + 1;
                    charAt17 = stringInfo.charAt(i71);
                    if (charAt17 < c10) {
                        break;
                    }
                    i70 |= (charAt17 & 8191) << i72;
                    i72 += 13;
                    i71 = i34;
                }
                charAt23 = i70 | (charAt17 << i72);
                i21 = i34;
            } else {
                i21 = i69;
            }
            int i73 = i21 + 1;
            int charAt24 = stringInfo.charAt(i21);
            if (charAt24 >= c10) {
                int i74 = charAt24 & 8191;
                int i75 = i73;
                int i76 = 13;
                while (true) {
                    i33 = i75 + 1;
                    charAt16 = stringInfo.charAt(i75);
                    i22 = length;
                    if (charAt16 < 55296) {
                        break;
                    }
                    i74 |= (charAt16 & 8191) << i76;
                    i76 += 13;
                    i75 = i33;
                    length = i22;
                }
                charAt24 = i74 | (charAt16 << i76);
                i23 = i33;
            } else {
                i22 = length;
                i23 = i73;
            }
            int i77 = charAt24 & 255;
            int i78 = charAt5;
            if ((charAt24 & 1024) != 0) {
                iArr[i67] = i68;
                i67++;
            }
            int i79 = i67;
            if (i77 >= 51) {
                int i80 = i23 + 1;
                int charAt25 = stringInfo.charAt(i23);
                char c11 = 55296;
                if (charAt25 >= 55296) {
                    int i81 = charAt25 & 8191;
                    int i82 = 13;
                    while (true) {
                        i32 = i80 + 1;
                        charAt15 = stringInfo.charAt(i80);
                        if (charAt15 < c11) {
                            break;
                        }
                        i81 |= (charAt15 & 8191) << i82;
                        i82 += 13;
                        i80 = i32;
                        c11 = 55296;
                    }
                    charAt25 = i81 | (charAt15 << i82);
                    i80 = i32;
                }
                int i83 = i77 - 51;
                int i84 = i80;
                if (i83 == 9 || i83 == 17) {
                    i31 = i11 + 1;
                    objArr[((i68 / 3) * 2) + 1] = objects[i11];
                } else {
                    if (i83 == 12 && !z12) {
                        i31 = i11 + 1;
                        objArr[((i68 / 3) * 2) + 1] = objects[i11];
                    }
                    int i85 = charAt25 * 2;
                    obj = objects[i85];
                    if (!(obj instanceof java.lang.reflect.Field)) {
                        reflectField2 = (java.lang.reflect.Field) obj;
                    } else {
                        reflectField2 = reflectField(cls, (String) obj);
                        objects[i85] = reflectField2;
                    }
                    i24 = charAt;
                    i25 = charAt2;
                    int objectFieldOffset2 = (int) unsafe.objectFieldOffset(reflectField2);
                    int i86 = i85 + 1;
                    obj2 = objects[i86];
                    if (!(obj2 instanceof java.lang.reflect.Field)) {
                        reflectField3 = (java.lang.reflect.Field) obj2;
                    } else {
                        reflectField3 = reflectField(cls, (String) obj2);
                        objects[i86] = reflectField3;
                    }
                    str = stringInfo;
                    i27 = (int) unsafe.objectFieldOffset(reflectField3);
                    z11 = z12;
                    i28 = i84;
                    objectFieldOffset = objectFieldOffset2;
                    i29 = 0;
                }
                i11 = i31;
                int i852 = charAt25 * 2;
                obj = objects[i852];
                if (!(obj instanceof java.lang.reflect.Field)) {
                }
                i24 = charAt;
                i25 = charAt2;
                int objectFieldOffset22 = (int) unsafe.objectFieldOffset(reflectField2);
                int i862 = i852 + 1;
                obj2 = objects[i862];
                if (!(obj2 instanceof java.lang.reflect.Field)) {
                }
                str = stringInfo;
                i27 = (int) unsafe.objectFieldOffset(reflectField3);
                z11 = z12;
                i28 = i84;
                objectFieldOffset = objectFieldOffset22;
                i29 = 0;
            } else {
                i24 = charAt;
                i25 = charAt2;
                int i87 = i11 + 1;
                java.lang.reflect.Field reflectField4 = reflectField(cls, (String) objects[i11]);
                if (i77 == 9 || i77 == 17) {
                    z10 = true;
                    objArr[((i68 / 3) * 2) + 1] = reflectField4.getType();
                } else {
                    if (i77 == 27 || i77 == 49) {
                        z10 = true;
                        i30 = i87 + 1;
                        objArr[((i68 / 3) * 2) + 1] = objects[i87];
                    } else {
                        if (i77 == 12 || i77 == 30 || i77 == 44) {
                            if (!z12) {
                                z10 = true;
                                i30 = i87 + 1;
                                objArr[((i68 / 3) * 2) + 1] = objects[i87];
                            }
                        } else if (i77 == 50) {
                            int i88 = i65 + 1;
                            iArr[i65] = i68;
                            int i89 = (i68 / 3) * 2;
                            int i90 = i87 + 1;
                            objArr[i89] = objects[i87];
                            if ((charAt24 & 2048) != 0) {
                                i87 = i90 + 1;
                                objArr[i89 + 1] = objects[i90];
                                i65 = i88;
                            } else {
                                i65 = i88;
                                i26 = i90;
                                z10 = true;
                                objectFieldOffset = (int) unsafe.objectFieldOffset(reflectField4);
                                int i91 = i26;
                                if (((charAt24 & 4096) == 4096 ? z10 : false) || i77 > 17) {
                                    str = stringInfo;
                                    z11 = z12;
                                    i27 = 1048575;
                                    i28 = i23;
                                    i29 = 0;
                                } else {
                                    int i92 = i23 + 1;
                                    int charAt26 = stringInfo.charAt(i23);
                                    if (charAt26 >= 55296) {
                                        int i93 = charAt26 & 8191;
                                        int i94 = 13;
                                        while (true) {
                                            i28 = i92 + 1;
                                            charAt14 = stringInfo.charAt(i92);
                                            if (charAt14 < 55296) {
                                                break;
                                            }
                                            i93 |= (charAt14 & 8191) << i94;
                                            i94 += 13;
                                            i92 = i28;
                                        }
                                        charAt26 = i93 | (charAt14 << i94);
                                    } else {
                                        i28 = i92;
                                    }
                                    int i95 = (i12 * 2) + (charAt26 / 32);
                                    Object obj3 = objects[i95];
                                    str = stringInfo;
                                    if (obj3 instanceof java.lang.reflect.Field) {
                                        reflectField = (java.lang.reflect.Field) obj3;
                                    } else {
                                        reflectField = reflectField(cls, (String) obj3);
                                        objects[i95] = reflectField;
                                    }
                                    z11 = z12;
                                    i27 = (int) unsafe.objectFieldOffset(reflectField);
                                    i29 = charAt26 % 32;
                                }
                                if (i77 >= 18 && i77 <= 49) {
                                    iArr[i66] = objectFieldOffset;
                                    i66++;
                                }
                                i11 = i91;
                            }
                        }
                        z10 = true;
                    }
                    i26 = i30;
                    objectFieldOffset = (int) unsafe.objectFieldOffset(reflectField4);
                    int i912 = i26;
                    if ((charAt24 & 4096) == 4096 ? z10 : false) {
                    }
                    str = stringInfo;
                    z11 = z12;
                    i27 = 1048575;
                    i28 = i23;
                    i29 = 0;
                    if (i77 >= 18) {
                        iArr[i66] = objectFieldOffset;
                        i66++;
                    }
                    i11 = i912;
                }
                i26 = i87;
                objectFieldOffset = (int) unsafe.objectFieldOffset(reflectField4);
                int i9122 = i26;
                if ((charAt24 & 4096) == 4096 ? z10 : false) {
                }
                str = stringInfo;
                z11 = z12;
                i27 = 1048575;
                i28 = i23;
                i29 = 0;
                if (i77 >= 18) {
                }
                i11 = i9122;
            }
            int i96 = i68 + 1;
            iArr2[i68] = charAt23;
            int i97 = i96 + 1;
            iArr2[i96] = ((charAt24 & 256) != 0 ? REQUIRED_MASK : 0) | ((charAt24 & 512) != 0 ? ENFORCE_UTF8_MASK : 0) | (i77 << 20) | objectFieldOffset;
            i68 = i97 + 1;
            iArr2[i97] = (i29 << 20) | i27;
            z12 = z11;
            charAt = i24;
            charAt5 = i78;
            length = i22;
            i37 = i28;
            i67 = i79;
            stringInfo = str;
            charAt2 = i25;
            c10 = 55296;
        }
        return new MessageSchema<>(iArr2, objArr, charAt, charAt2, rawMessageInfo.getDefaultInstance(), z12, false, iArr, charAt5, i64, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    private int numberAt(int i10) {
        return this.buffer[i10];
    }

    private static long offset(int i10) {
        return i10 & 1048575;
    }

    private static <T> boolean oneofBooleanAt(T t7, long j10) {
        return ((Boolean) UnsafeUtil.getObject(t7, j10)).booleanValue();
    }

    private static <T> double oneofDoubleAt(T t7, long j10) {
        return ((Double) UnsafeUtil.getObject(t7, j10)).doubleValue();
    }

    private static <T> float oneofFloatAt(T t7, long j10) {
        return ((Float) UnsafeUtil.getObject(t7, j10)).floatValue();
    }

    private static <T> int oneofIntAt(T t7, long j10) {
        return ((Integer) UnsafeUtil.getObject(t7, j10)).intValue();
    }

    private static <T> long oneofLongAt(T t7, long j10) {
        return ((Long) UnsafeUtil.getObject(t7, j10)).longValue();
    }

    private <K, V> int parseMapField(T t7, byte[] bArr, int i10, int i11, int i12, long j10, ArrayDecoders.Registers registers) {
        Unsafe unsafe = UNSAFE;
        Object mapFieldDefaultEntry = getMapFieldDefaultEntry(i12);
        Object object = unsafe.getObject(t7, j10);
        if (this.mapFieldSchema.isImmutable(object)) {
            Object newMapField = this.mapFieldSchema.newMapField(mapFieldDefaultEntry);
            this.mapFieldSchema.mergeFrom(newMapField, object);
            unsafe.putObject(t7, j10, newMapField);
            object = newMapField;
        }
        return decodeMapEntry(bArr, i10, i11, this.mapFieldSchema.forMapMetadata(mapFieldDefaultEntry), this.mapFieldSchema.forMutableMapData(object), registers);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int parseOneofField(T t7, byte[] bArr, int i10, int i11, int i12, int i13, int i14, int i15, int i16, long j10, int i17, ArrayDecoders.Registers registers) {
        int mergeMessageField;
        Unsafe unsafe = UNSAFE;
        long j11 = this.buffer[i17 + 2] & 1048575;
        switch (i16) {
            case 51:
                if (i14 == 1) {
                    unsafe.putObject(t7, j10, Double.valueOf(ArrayDecoders.decodeDouble(bArr, i10)));
                    int i18 = i10 + 8;
                    unsafe.putInt(t7, j11, i13);
                    return i18;
                }
                return i10;
            case 52:
                if (i14 == 5) {
                    unsafe.putObject(t7, j10, Float.valueOf(ArrayDecoders.decodeFloat(bArr, i10)));
                    int i19 = i10 + 4;
                    unsafe.putInt(t7, j11, i13);
                    return i19;
                }
                return i10;
            case 53:
            case 54:
                if (i14 == 0) {
                    int decodeVarint64 = ArrayDecoders.decodeVarint64(bArr, i10, registers);
                    unsafe.putObject(t7, j10, Long.valueOf(registers.long1));
                    unsafe.putInt(t7, j11, i13);
                    return decodeVarint64;
                }
                return i10;
            case 55:
            case 62:
                if (i14 == 0) {
                    int decodeVarint32 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
                    unsafe.putObject(t7, j10, Integer.valueOf(registers.int1));
                    unsafe.putInt(t7, j11, i13);
                    return decodeVarint32;
                }
                return i10;
            case 56:
            case 65:
                if (i14 == 1) {
                    unsafe.putObject(t7, j10, Long.valueOf(ArrayDecoders.decodeFixed64(bArr, i10)));
                    int i20 = i10 + 8;
                    unsafe.putInt(t7, j11, i13);
                    return i20;
                }
                return i10;
            case 57:
            case 64:
                if (i14 == 5) {
                    unsafe.putObject(t7, j10, Integer.valueOf(ArrayDecoders.decodeFixed32(bArr, i10)));
                    int i21 = i10 + 4;
                    unsafe.putInt(t7, j11, i13);
                    return i21;
                }
                return i10;
            case 58:
                if (i14 == 0) {
                    int decodeVarint642 = ArrayDecoders.decodeVarint64(bArr, i10, registers);
                    unsafe.putObject(t7, j10, Boolean.valueOf(registers.long1 != 0));
                    unsafe.putInt(t7, j11, i13);
                    return decodeVarint642;
                }
                return i10;
            case 59:
                if (i14 == 2) {
                    int decodeVarint322 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
                    int i22 = registers.int1;
                    if (i22 == 0) {
                        unsafe.putObject(t7, j10, "");
                    } else {
                        if ((i15 & ENFORCE_UTF8_MASK) != 0 && !Utf8.isValidUtf8(bArr, decodeVarint322, decodeVarint322 + i22)) {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                        unsafe.putObject(t7, j10, new String(bArr, decodeVarint322, i22, Internal.UTF_8));
                        decodeVarint322 += i22;
                    }
                    unsafe.putInt(t7, j11, i13);
                    return decodeVarint322;
                }
                return i10;
            case 60:
                if (i14 == 2) {
                    Object mutableOneofMessageFieldForMerge = mutableOneofMessageFieldForMerge(t7, i13, i17);
                    mergeMessageField = ArrayDecoders.mergeMessageField(mutableOneofMessageFieldForMerge, getMessageFieldSchema(i17), bArr, i10, i11, registers);
                    storeOneofMessageField(t7, i13, i17, mutableOneofMessageFieldForMerge);
                    break;
                }
                return i10;
            case 61:
                if (i14 == 2) {
                    int decodeBytes = ArrayDecoders.decodeBytes(bArr, i10, registers);
                    unsafe.putObject(t7, j10, registers.object1);
                    unsafe.putInt(t7, j11, i13);
                    return decodeBytes;
                }
                return i10;
            case 63:
                if (i14 == 0) {
                    int decodeVarint323 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
                    int i23 = registers.int1;
                    Internal.EnumVerifier enumFieldVerifier = getEnumFieldVerifier(i17);
                    if (enumFieldVerifier != null && !enumFieldVerifier.isInRange(i23)) {
                        getMutableUnknownFields(t7).storeField(i12, Long.valueOf(i23));
                    } else {
                        unsafe.putObject(t7, j10, Integer.valueOf(i23));
                        unsafe.putInt(t7, j11, i13);
                    }
                    return decodeVarint323;
                }
                return i10;
            case 66:
                if (i14 == 0) {
                    int decodeVarint324 = ArrayDecoders.decodeVarint32(bArr, i10, registers);
                    unsafe.putObject(t7, j10, Integer.valueOf(CodedInputStream.decodeZigZag32(registers.int1)));
                    unsafe.putInt(t7, j11, i13);
                    return decodeVarint324;
                }
                return i10;
            case 67:
                if (i14 == 0) {
                    int decodeVarint643 = ArrayDecoders.decodeVarint64(bArr, i10, registers);
                    unsafe.putObject(t7, j10, Long.valueOf(CodedInputStream.decodeZigZag64(registers.long1)));
                    unsafe.putInt(t7, j11, i13);
                    return decodeVarint643;
                }
                return i10;
            case 68:
                if (i14 == 3) {
                    Object mutableOneofMessageFieldForMerge2 = mutableOneofMessageFieldForMerge(t7, i13, i17);
                    mergeMessageField = ArrayDecoders.mergeGroupField(mutableOneofMessageFieldForMerge2, getMessageFieldSchema(i17), bArr, i10, i11, (i12 & (-8)) | 4, registers);
                    storeOneofMessageField(t7, i13, i17, mutableOneofMessageFieldForMerge2);
                    break;
                }
                return i10;
            default:
                return i10;
        }
        return mergeMessageField;
    }

    /* JADX WARN: Code restructure failed: missing block: B:129:0x0252, code lost:
    
        if (r0 != r15) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0254, code lost:
    
        r15 = r30;
        r14 = r31;
        r12 = r32;
        r13 = r34;
        r11 = r35;
        r10 = r18;
        r1 = r19;
        r2 = r20;
        r6 = r24;
        r7 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x02c1, code lost:
    
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x029b, code lost:
    
        if (r0 != r15) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x02be, code lost:
    
        if (r0 != r15) goto L98;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:21:0x0096. Please report as an issue. */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v10, types: [int] */
    @CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int parseProto3Message(T t7, byte[] bArr, int i10, int i11, ArrayDecoders.Registers registers) {
        byte b10;
        int i12;
        int positionForFieldNumber;
        int i13;
        int i14;
        Unsafe unsafe;
        int i15;
        int i16;
        int i17;
        Unsafe unsafe2;
        int i18;
        int i19;
        int i20;
        int decodeVarint64;
        Unsafe unsafe3;
        MessageSchema<T> messageSchema = this;
        T t10 = t7;
        byte[] bArr2 = bArr;
        int i21 = i11;
        ArrayDecoders.Registers registers2 = registers;
        checkMutable(t7);
        Unsafe unsafe4 = UNSAFE;
        int i22 = -1;
        int i23 = i10;
        int i24 = -1;
        int i25 = 0;
        int i26 = 0;
        int i27 = 1048575;
        while (i23 < i21) {
            int i28 = i23 + 1;
            byte b11 = bArr2[i23];
            if (b11 < 0) {
                i12 = ArrayDecoders.decodeVarint32(b11, bArr2, i28, registers2);
                b10 = registers2.int1;
            } else {
                b10 = b11;
                i12 = i28;
            }
            int i29 = b10 >>> 3;
            int i30 = b10 & 7;
            if (i29 > i24) {
                positionForFieldNumber = messageSchema.positionForFieldNumber(i29, i25 / 3);
            } else {
                positionForFieldNumber = messageSchema.positionForFieldNumber(i29);
            }
            int i31 = positionForFieldNumber;
            if (i31 == i22) {
                i13 = i12;
                i14 = i29;
                unsafe = unsafe4;
                i15 = i22;
                i16 = 0;
            } else {
                int i32 = messageSchema.buffer[i31 + 1];
                int type = type(i32);
                Unsafe unsafe5 = unsafe4;
                long offset = offset(i32);
                if (type <= 17) {
                    int i33 = messageSchema.buffer[i31 + 2];
                    int i34 = 1 << (i33 >>> 20);
                    int i35 = i33 & 1048575;
                    if (i35 != i27) {
                        if (i27 != 1048575) {
                            long j10 = i27;
                            unsafe3 = unsafe5;
                            unsafe3.putInt(t10, j10, i26);
                        } else {
                            unsafe3 = unsafe5;
                        }
                        if (i35 != 1048575) {
                            i26 = unsafe3.getInt(t10, i35);
                        }
                        unsafe2 = unsafe3;
                        i27 = i35;
                    } else {
                        unsafe2 = unsafe5;
                    }
                    switch (type) {
                        case 0:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 1) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                UnsafeUtil.putDouble(t10, offset, ArrayDecoders.decodeDouble(bArr2, i12));
                                i23 = i12 + 8;
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 1:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 5) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                UnsafeUtil.putFloat(t10, offset, ArrayDecoders.decodeFloat(bArr2, i12));
                                i23 = i12 + 4;
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 2:
                        case 3:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 0) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                decodeVarint64 = ArrayDecoders.decodeVarint64(bArr2, i12, registers2);
                                unsafe2.putLong(t7, offset, registers2.long1);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i23 = decodeVarint64;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 4:
                        case 11:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 0) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                i23 = ArrayDecoders.decodeVarint32(bArr2, i12, registers2);
                                unsafe2.putInt(t10, offset, registers2.int1);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 5:
                        case 14:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 1) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                unsafe2.putLong(t7, offset, ArrayDecoders.decodeFixed64(bArr2, i12));
                                i23 = i12 + 8;
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 6:
                        case 13:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 5) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                unsafe2.putInt(t10, offset, ArrayDecoders.decodeFixed32(bArr2, i12));
                                i23 = i12 + 4;
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 7:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 0) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                i23 = ArrayDecoders.decodeVarint64(bArr2, i12, registers2);
                                UnsafeUtil.putBoolean(t10, offset, registers2.long1 != 0);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 8:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 2) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                if ((i32 & ENFORCE_UTF8_MASK) == 0) {
                                    i23 = ArrayDecoders.decodeString(bArr2, i12, registers2);
                                } else {
                                    i23 = ArrayDecoders.decodeStringRequireUtf8(bArr2, i12, registers2);
                                }
                                unsafe2.putObject(t10, offset, registers2.object1);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 9:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 2) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                Object mutableMessageFieldForMerge = messageSchema.mutableMessageFieldForMerge(t10, i17);
                                i23 = ArrayDecoders.mergeMessageField(mutableMessageFieldForMerge, messageSchema.getMessageFieldSchema(i17), bArr, i12, i11, registers);
                                messageSchema.storeMessageField(t10, i17, mutableMessageFieldForMerge);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 10:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 2) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                i23 = ArrayDecoders.decodeBytes(bArr2, i12, registers2);
                                unsafe2.putObject(t10, offset, registers2.object1);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 12:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 0) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                i23 = ArrayDecoders.decodeVarint32(bArr2, i12, registers2);
                                unsafe2.putInt(t10, offset, registers2.int1);
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 15:
                            i17 = i31;
                            i14 = i29;
                            if (i30 != 0) {
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                i23 = ArrayDecoders.decodeVarint32(bArr2, i12, registers2);
                                unsafe2.putInt(t10, offset, CodedInputStream.decodeZigZag32(registers2.int1));
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        case 16:
                            if (i30 != 0) {
                                i17 = i31;
                                i14 = i29;
                                i13 = i12;
                                unsafe = unsafe2;
                                i16 = i17;
                                i15 = -1;
                                break;
                            } else {
                                decodeVarint64 = ArrayDecoders.decodeVarint64(bArr2, i12, registers2);
                                i17 = i31;
                                i14 = i29;
                                unsafe2.putLong(t7, offset, CodedInputStream.decodeZigZag64(registers2.long1));
                                i26 |= i34;
                                unsafe4 = unsafe2;
                                i25 = i17;
                                i23 = decodeVarint64;
                                i24 = i14;
                                i22 = -1;
                                i21 = i11;
                                break;
                            }
                        default:
                            i17 = i31;
                            i14 = i29;
                            i13 = i12;
                            unsafe = unsafe2;
                            i16 = i17;
                            i15 = -1;
                            break;
                    }
                } else {
                    i14 = i29;
                    i17 = i31;
                    unsafe2 = unsafe5;
                    if (type != 27) {
                        if (type <= 49) {
                            int i36 = i12;
                            i19 = i26;
                            i20 = i27;
                            unsafe = unsafe2;
                            i15 = -1;
                            i16 = i17;
                            i23 = parseRepeatedField(t7, bArr, i12, i11, b10, i14, i30, i17, i32, type, offset, registers);
                        } else {
                            i18 = i12;
                            i19 = i26;
                            i20 = i27;
                            unsafe = unsafe2;
                            i16 = i17;
                            i15 = -1;
                            if (type != 50) {
                                i23 = parseOneofField(t7, bArr, i18, i11, b10, i14, i30, i32, type, offset, i16, registers);
                            } else if (i30 == 2) {
                                i23 = parseMapField(t7, bArr, i18, i11, i16, offset, registers);
                            }
                        }
                        unsafe4 = unsafe;
                    } else if (i30 == 2) {
                        Internal.ProtobufList protobufList = (Internal.ProtobufList) unsafe2.getObject(t10, offset);
                        if (!protobufList.isModifiable()) {
                            int size = protobufList.size();
                            protobufList = protobufList.mutableCopyWithCapacity2(size == 0 ? 10 : size * 2);
                            unsafe2.putObject(t10, offset, protobufList);
                        }
                        i23 = ArrayDecoders.decodeMessageList(messageSchema.getMessageFieldSchema(i17), b10, bArr, i12, i11, protobufList, registers);
                        i26 = i26;
                        unsafe4 = unsafe2;
                        i25 = i17;
                        i24 = i14;
                        i22 = -1;
                        i21 = i11;
                    } else {
                        i18 = i12;
                        i19 = i26;
                        i20 = i27;
                        unsafe = unsafe2;
                        i16 = i17;
                        i15 = -1;
                    }
                    i13 = i18;
                    i26 = i19;
                    i27 = i20;
                }
            }
            i23 = ArrayDecoders.decodeUnknownField(b10, bArr, i13, i11, getMutableUnknownFields(t7), registers);
            messageSchema = this;
            t10 = t7;
            bArr2 = bArr;
            i21 = i11;
            registers2 = registers;
            i22 = i15;
            i24 = i14;
            i25 = i16;
            unsafe4 = unsafe;
        }
        int i37 = i26;
        Unsafe unsafe6 = unsafe4;
        if (i27 != 1048575) {
            unsafe6.putInt(t7, i27, i37);
        }
        if (i23 == i11) {
            return i23;
        }
        throw InvalidProtocolBufferException.parseFailure();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x002f. Please report as an issue. */
    private int parseRepeatedField(T t7, byte[] bArr, int i10, int i11, int i12, int i13, int i14, int i15, long j10, int i16, long j11, ArrayDecoders.Registers registers) {
        int decodeVarint32List;
        Unsafe unsafe = UNSAFE;
        Internal.ProtobufList protobufList = (Internal.ProtobufList) unsafe.getObject(t7, j11);
        if (!protobufList.isModifiable()) {
            int size = protobufList.size();
            protobufList = protobufList.mutableCopyWithCapacity2(size == 0 ? 10 : size * 2);
            unsafe.putObject(t7, j11, protobufList);
        }
        switch (i16) {
            case 18:
            case 35:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedDoubleList(bArr, i10, protobufList, registers);
                }
                if (i14 == 1) {
                    return ArrayDecoders.decodeDoubleList(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 19:
            case 36:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedFloatList(bArr, i10, protobufList, registers);
                }
                if (i14 == 5) {
                    return ArrayDecoders.decodeFloatList(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedVarint64List(bArr, i10, protobufList, registers);
                }
                if (i14 == 0) {
                    return ArrayDecoders.decodeVarint64List(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 22:
            case 29:
            case 39:
            case 43:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedVarint32List(bArr, i10, protobufList, registers);
                }
                if (i14 == 0) {
                    return ArrayDecoders.decodeVarint32List(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedFixed64List(bArr, i10, protobufList, registers);
                }
                if (i14 == 1) {
                    return ArrayDecoders.decodeFixed64List(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedFixed32List(bArr, i10, protobufList, registers);
                }
                if (i14 == 5) {
                    return ArrayDecoders.decodeFixed32List(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 25:
            case 42:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedBoolList(bArr, i10, protobufList, registers);
                }
                if (i14 == 0) {
                    return ArrayDecoders.decodeBoolList(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 26:
                if (i14 == 2) {
                    if ((j10 & 536870912) == 0) {
                        return ArrayDecoders.decodeStringList(i12, bArr, i10, i11, protobufList, registers);
                    }
                    return ArrayDecoders.decodeStringListRequireUtf8(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 27:
                if (i14 == 2) {
                    return ArrayDecoders.decodeMessageList(getMessageFieldSchema(i15), i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 28:
                if (i14 == 2) {
                    return ArrayDecoders.decodeBytesList(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 30:
            case 44:
                if (i14 != 2) {
                    if (i14 == 0) {
                        decodeVarint32List = ArrayDecoders.decodeVarint32List(i12, bArr, i10, i11, protobufList, registers);
                    }
                    return i10;
                }
                decodeVarint32List = ArrayDecoders.decodePackedVarint32List(bArr, i10, protobufList, registers);
                SchemaUtil.filterUnknownEnumList((Object) t7, i13, (List<Integer>) protobufList, getEnumFieldVerifier(i15), (Object) null, (UnknownFieldSchema<UT, Object>) this.unknownFieldSchema);
                return decodeVarint32List;
            case 33:
            case 47:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedSInt32List(bArr, i10, protobufList, registers);
                }
                if (i14 == 0) {
                    return ArrayDecoders.decodeSInt32List(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 34:
            case 48:
                if (i14 == 2) {
                    return ArrayDecoders.decodePackedSInt64List(bArr, i10, protobufList, registers);
                }
                if (i14 == 0) {
                    return ArrayDecoders.decodeSInt64List(i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            case 49:
                if (i14 == 3) {
                    return ArrayDecoders.decodeGroupList(getMessageFieldSchema(i15), i12, bArr, i10, i11, protobufList, registers);
                }
                return i10;
            default:
                return i10;
        }
    }

    private int positionForFieldNumber(int i10) {
        if (i10 < this.minFieldNumber || i10 > this.maxFieldNumber) {
            return -1;
        }
        return slowPositionForFieldNumber(i10, 0);
    }

    private int presenceMaskAndOffsetAt(int i10) {
        return this.buffer[i10 + 2];
    }

    private <E> void readGroupList(Object obj, long j10, Reader reader, Schema<E> schema, ExtensionRegistryLite extensionRegistryLite) {
        reader.readGroupList(this.listFieldSchema.mutableListAt(obj, j10), schema, extensionRegistryLite);
    }

    private <E> void readMessageList(Object obj, int i10, Reader reader, Schema<E> schema, ExtensionRegistryLite extensionRegistryLite) {
        reader.readMessageList(this.listFieldSchema.mutableListAt(obj, offset(i10)), schema, extensionRegistryLite);
    }

    private void readString(Object obj, int i10, Reader reader) {
        if (isEnforceUtf8(i10)) {
            UnsafeUtil.putObject(obj, offset(i10), reader.readStringRequireUtf8());
        } else if (this.lite) {
            UnsafeUtil.putObject(obj, offset(i10), reader.readString());
        } else {
            UnsafeUtil.putObject(obj, offset(i10), reader.readBytes());
        }
    }

    private void readStringList(Object obj, int i10, Reader reader) {
        if (isEnforceUtf8(i10)) {
            reader.readStringListRequireUtf8(this.listFieldSchema.mutableListAt(obj, offset(i10)));
        } else {
            reader.readStringList(this.listFieldSchema.mutableListAt(obj, offset(i10)));
        }
    }

    private static java.lang.reflect.Field reflectField(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            java.lang.reflect.Field[] declaredFields = cls.getDeclaredFields();
            for (java.lang.reflect.Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
        }
    }

    private void setFieldPresent(T t7, int i10) {
        int presenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i10);
        long j10 = 1048575 & presenceMaskAndOffsetAt;
        if (j10 == 1048575) {
            return;
        }
        UnsafeUtil.putInt(t7, j10, (1 << (presenceMaskAndOffsetAt >>> 20)) | UnsafeUtil.getInt(t7, j10));
    }

    private void setOneofPresent(T t7, int i10, int i11) {
        UnsafeUtil.putInt(t7, presenceMaskAndOffsetAt(i11) & 1048575, i10);
    }

    private int slowPositionForFieldNumber(int i10, int i11) {
        int length = (this.buffer.length / 3) - 1;
        while (i11 <= length) {
            int i12 = (length + i11) >>> 1;
            int i13 = i12 * 3;
            int numberAt = numberAt(i13);
            if (i10 == numberAt) {
                return i13;
            }
            if (i10 < numberAt) {
                length = i12 - 1;
            } else {
                i11 = i12 + 1;
            }
        }
        return -1;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x007a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void storeFieldData(FieldInfo fieldInfo, int[] iArr, int i10, Object[] objArr) {
        int objectFieldOffset;
        int id2;
        long objectFieldOffset2;
        int i11;
        int i12;
        OneofInfo oneof = fieldInfo.getOneof();
        if (oneof != null) {
            id2 = fieldInfo.getType().id() + 51;
            objectFieldOffset = (int) UnsafeUtil.objectFieldOffset(oneof.getValueField());
            objectFieldOffset2 = UnsafeUtil.objectFieldOffset(oneof.getCaseField());
        } else {
            FieldType type = fieldInfo.getType();
            objectFieldOffset = (int) UnsafeUtil.objectFieldOffset(fieldInfo.getField());
            id2 = type.id();
            if (!type.isList() && !type.isMap()) {
                java.lang.reflect.Field presenceField = fieldInfo.getPresenceField();
                i11 = presenceField == null ? 1048575 : (int) UnsafeUtil.objectFieldOffset(presenceField);
                i12 = Integer.numberOfTrailingZeros(fieldInfo.getPresenceMask());
            } else if (fieldInfo.getCachedSizeField() == null) {
                i11 = 0;
                i12 = 0;
            } else {
                objectFieldOffset2 = UnsafeUtil.objectFieldOffset(fieldInfo.getCachedSizeField());
            }
            iArr[i10] = fieldInfo.getFieldNumber();
            iArr[i10 + 1] = (fieldInfo.isRequired() ? REQUIRED_MASK : 0) | (!fieldInfo.isEnforceUtf8() ? ENFORCE_UTF8_MASK : 0) | (id2 << 20) | objectFieldOffset;
            iArr[i10 + 2] = i11 | (i12 << 20);
            Class<?> messageFieldClass = fieldInfo.getMessageFieldClass();
            if (fieldInfo.getMapDefaultEntry() != null) {
                if (messageFieldClass != null) {
                    objArr[((i10 / 3) * 2) + 1] = messageFieldClass;
                    return;
                } else {
                    if (fieldInfo.getEnumVerifier() != null) {
                        objArr[((i10 / 3) * 2) + 1] = fieldInfo.getEnumVerifier();
                        return;
                    }
                    return;
                }
            }
            int i13 = (i10 / 3) * 2;
            objArr[i13] = fieldInfo.getMapDefaultEntry();
            if (messageFieldClass != null) {
                objArr[i13 + 1] = messageFieldClass;
                return;
            } else {
                if (fieldInfo.getEnumVerifier() != null) {
                    objArr[i13 + 1] = fieldInfo.getEnumVerifier();
                    return;
                }
                return;
            }
        }
        i11 = (int) objectFieldOffset2;
        i12 = 0;
        iArr[i10] = fieldInfo.getFieldNumber();
        if (!fieldInfo.isEnforceUtf8()) {
        }
        iArr[i10 + 1] = (fieldInfo.isRequired() ? REQUIRED_MASK : 0) | (!fieldInfo.isEnforceUtf8() ? ENFORCE_UTF8_MASK : 0) | (id2 << 20) | objectFieldOffset;
        iArr[i10 + 2] = i11 | (i12 << 20);
        Class<?> messageFieldClass2 = fieldInfo.getMessageFieldClass();
        if (fieldInfo.getMapDefaultEntry() != null) {
        }
    }

    private void storeMessageField(T t7, int i10, Object obj) {
        UNSAFE.putObject(t7, offset(typeAndOffsetAt(i10)), obj);
        setFieldPresent(t7, i10);
    }

    private void storeOneofMessageField(T t7, int i10, int i11, Object obj) {
        UNSAFE.putObject(t7, offset(typeAndOffsetAt(i11)), obj);
        setOneofPresent(t7, i10, i11);
    }

    private static int type(int i10) {
        return (i10 & FIELD_TYPE_MASK) >>> 20;
    }

    private int typeAndOffsetAt(int i10) {
        return this.buffer[i10 + 1];
    }

    /* JADX WARN: Removed duplicated region for block: B:228:0x048f  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFieldsInAscendingOrderProto2(T t7, Writer writer) {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, ?> entry;
        int length;
        int i10;
        int i11;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(t7);
            if (!extensions.isEmpty()) {
                it = extensions.iterator();
                entry = (Map.Entry) it.next();
                length = this.buffer.length;
                Unsafe unsafe = UNSAFE;
                int i12 = 1048575;
                int i13 = 1048575;
                i10 = 0;
                int i14 = 0;
                while (i10 < length) {
                    int typeAndOffsetAt = typeAndOffsetAt(i10);
                    int numberAt = numberAt(i10);
                    int type = type(typeAndOffsetAt);
                    if (type <= 17) {
                        int i15 = this.buffer[i10 + 2];
                        int i16 = i15 & i12;
                        if (i16 != i13) {
                            i14 = unsafe.getInt(t7, i16);
                            i13 = i16;
                        }
                        i11 = 1 << (i15 >>> 20);
                    } else {
                        i11 = 0;
                    }
                    while (entry != null && this.extensionSchema.extensionNumber(entry) <= numberAt) {
                        this.extensionSchema.serializeExtension(writer, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    long offset = offset(typeAndOffsetAt);
                    switch (type) {
                        case 0:
                            if ((i11 & i14) == 0) {
                                break;
                            } else {
                                writer.writeDouble(numberAt, doubleAt(t7, offset));
                                continue;
                            }
                        case 1:
                            if ((i11 & i14) != 0) {
                                writer.writeFloat(numberAt, floatAt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 2:
                            if ((i11 & i14) != 0) {
                                writer.writeInt64(numberAt, unsafe.getLong(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 3:
                            if ((i11 & i14) != 0) {
                                writer.writeUInt64(numberAt, unsafe.getLong(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 4:
                            if ((i11 & i14) != 0) {
                                writer.writeInt32(numberAt, unsafe.getInt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 5:
                            if ((i11 & i14) != 0) {
                                writer.writeFixed64(numberAt, unsafe.getLong(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 6:
                            if ((i11 & i14) != 0) {
                                writer.writeFixed32(numberAt, unsafe.getInt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 7:
                            if ((i11 & i14) != 0) {
                                writer.writeBool(numberAt, booleanAt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 8:
                            if ((i11 & i14) != 0) {
                                writeString(numberAt, unsafe.getObject(t7, offset), writer);
                                break;
                            } else {
                                continue;
                            }
                        case 9:
                            if ((i11 & i14) != 0) {
                                writer.writeMessage(numberAt, unsafe.getObject(t7, offset), getMessageFieldSchema(i10));
                                break;
                            } else {
                                continue;
                            }
                        case 10:
                            if ((i11 & i14) != 0) {
                                writer.writeBytes(numberAt, (ByteString) unsafe.getObject(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 11:
                            if ((i11 & i14) != 0) {
                                writer.writeUInt32(numberAt, unsafe.getInt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 12:
                            if ((i11 & i14) != 0) {
                                writer.writeEnum(numberAt, unsafe.getInt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 13:
                            if ((i11 & i14) != 0) {
                                writer.writeSFixed32(numberAt, unsafe.getInt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 14:
                            if ((i11 & i14) != 0) {
                                writer.writeSFixed64(numberAt, unsafe.getLong(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 15:
                            if ((i11 & i14) != 0) {
                                writer.writeSInt32(numberAt, unsafe.getInt(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 16:
                            if ((i11 & i14) != 0) {
                                writer.writeSInt64(numberAt, unsafe.getLong(t7, offset));
                                break;
                            } else {
                                continue;
                            }
                        case 17:
                            if ((i11 & i14) != 0) {
                                writer.writeGroup(numberAt, unsafe.getObject(t7, offset), getMessageFieldSchema(i10));
                                break;
                            } else {
                                continue;
                            }
                        case 18:
                            SchemaUtil.writeDoubleList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 19:
                            SchemaUtil.writeFloatList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 20:
                            SchemaUtil.writeInt64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 21:
                            SchemaUtil.writeUInt64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 22:
                            SchemaUtil.writeInt32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 23:
                            SchemaUtil.writeFixed64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 24:
                            SchemaUtil.writeFixed32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 25:
                            SchemaUtil.writeBoolList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 26:
                            SchemaUtil.writeStringList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer);
                            break;
                        case 27:
                            SchemaUtil.writeMessageList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, getMessageFieldSchema(i10));
                            break;
                        case 28:
                            SchemaUtil.writeBytesList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer);
                            break;
                        case 29:
                            SchemaUtil.writeUInt32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 30:
                            SchemaUtil.writeEnumList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 31:
                            SchemaUtil.writeSFixed32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 32:
                            SchemaUtil.writeSFixed64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 33:
                            SchemaUtil.writeSInt32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 34:
                            SchemaUtil.writeSInt64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, false);
                            continue;
                        case 35:
                            SchemaUtil.writeDoubleList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 36:
                            SchemaUtil.writeFloatList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 37:
                            SchemaUtil.writeInt64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 38:
                            SchemaUtil.writeUInt64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 39:
                            SchemaUtil.writeInt32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 40:
                            SchemaUtil.writeFixed64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 41:
                            SchemaUtil.writeFixed32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 42:
                            SchemaUtil.writeBoolList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 43:
                            SchemaUtil.writeUInt32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 44:
                            SchemaUtil.writeEnumList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 45:
                            SchemaUtil.writeSFixed32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 46:
                            SchemaUtil.writeSFixed64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 47:
                            SchemaUtil.writeSInt32List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 48:
                            SchemaUtil.writeSInt64List(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, true);
                            break;
                        case 49:
                            SchemaUtil.writeGroupList(numberAt(i10), (List) unsafe.getObject(t7, offset), writer, getMessageFieldSchema(i10));
                            break;
                        case 50:
                            writeMapHelper(writer, numberAt, unsafe.getObject(t7, offset), i10);
                            break;
                        case 51:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeDouble(numberAt, oneofDoubleAt(t7, offset));
                                break;
                            }
                            break;
                        case 52:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeFloat(numberAt, oneofFloatAt(t7, offset));
                                break;
                            }
                            break;
                        case 53:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeInt64(numberAt, oneofLongAt(t7, offset));
                                break;
                            }
                            break;
                        case 54:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeUInt64(numberAt, oneofLongAt(t7, offset));
                                break;
                            }
                            break;
                        case 55:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeInt32(numberAt, oneofIntAt(t7, offset));
                                break;
                            }
                            break;
                        case 56:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeFixed64(numberAt, oneofLongAt(t7, offset));
                                break;
                            }
                            break;
                        case 57:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeFixed32(numberAt, oneofIntAt(t7, offset));
                                break;
                            }
                            break;
                        case 58:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeBool(numberAt, oneofBooleanAt(t7, offset));
                                break;
                            }
                            break;
                        case 59:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writeString(numberAt, unsafe.getObject(t7, offset), writer);
                                break;
                            }
                            break;
                        case 60:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeMessage(numberAt, unsafe.getObject(t7, offset), getMessageFieldSchema(i10));
                                break;
                            }
                            break;
                        case 61:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeBytes(numberAt, (ByteString) unsafe.getObject(t7, offset));
                                break;
                            }
                            break;
                        case 62:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeUInt32(numberAt, oneofIntAt(t7, offset));
                                break;
                            }
                            break;
                        case 63:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeEnum(numberAt, oneofIntAt(t7, offset));
                                break;
                            }
                            break;
                        case 64:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSFixed32(numberAt, oneofIntAt(t7, offset));
                                break;
                            }
                            break;
                        case 65:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSFixed64(numberAt, oneofLongAt(t7, offset));
                                break;
                            }
                            break;
                        case 66:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSInt32(numberAt, oneofIntAt(t7, offset));
                                break;
                            }
                            break;
                        case 67:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSInt64(numberAt, oneofLongAt(t7, offset));
                                break;
                            }
                            break;
                        case 68:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeGroup(numberAt, unsafe.getObject(t7, offset), getMessageFieldSchema(i10));
                                break;
                            }
                            break;
                    }
                    i10 += 3;
                    i12 = 1048575;
                }
                while (entry != null) {
                    this.extensionSchema.serializeExtension(writer, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                writeUnknownInMessageTo(this.unknownFieldSchema, t7, writer);
            }
        }
        it = null;
        entry = null;
        length = this.buffer.length;
        Unsafe unsafe2 = UNSAFE;
        int i122 = 1048575;
        int i132 = 1048575;
        i10 = 0;
        int i142 = 0;
        while (i10 < length) {
        }
        while (entry != null) {
        }
        writeUnknownInMessageTo(this.unknownFieldSchema, t7, writer);
    }

    /* JADX WARN: Removed duplicated region for block: B:275:0x0588  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFieldsInAscendingOrderProto3(T t7, Writer writer) {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, ?> entry;
        int length;
        int i10;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(t7);
            if (!extensions.isEmpty()) {
                it = extensions.iterator();
                entry = (Map.Entry) it.next();
                length = this.buffer.length;
                for (i10 = 0; i10 < length; i10 += 3) {
                    int typeAndOffsetAt = typeAndOffsetAt(i10);
                    int numberAt = numberAt(i10);
                    while (entry != null && this.extensionSchema.extensionNumber(entry) <= numberAt) {
                        this.extensionSchema.serializeExtension(writer, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    switch (type(typeAndOffsetAt)) {
                        case 0:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeDouble(numberAt, doubleAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeFloat(numberAt, floatAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeInt64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeUInt64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeInt32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeFixed64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeFixed32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeBool(numberAt, booleanAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (isFieldPresent(t7, i10)) {
                                writeString(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(i10));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeUInt32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeEnum(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeSFixed32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeSFixed64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeSInt32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeSInt64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (isFieldPresent(t7, i10)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(i10));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            SchemaUtil.writeDoubleList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 19:
                            SchemaUtil.writeFloatList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 20:
                            SchemaUtil.writeInt64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 21:
                            SchemaUtil.writeUInt64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 22:
                            SchemaUtil.writeInt32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 23:
                            SchemaUtil.writeFixed64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 24:
                            SchemaUtil.writeFixed32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 25:
                            SchemaUtil.writeBoolList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 26:
                            SchemaUtil.writeStringList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                            break;
                        case 27:
                            SchemaUtil.writeMessageList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(i10));
                            break;
                        case 28:
                            SchemaUtil.writeBytesList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                            break;
                        case 29:
                            SchemaUtil.writeUInt32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 30:
                            SchemaUtil.writeEnumList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 31:
                            SchemaUtil.writeSFixed32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 32:
                            SchemaUtil.writeSFixed64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 33:
                            SchemaUtil.writeSInt32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 34:
                            SchemaUtil.writeSInt64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 35:
                            SchemaUtil.writeDoubleList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 36:
                            SchemaUtil.writeFloatList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 37:
                            SchemaUtil.writeInt64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 38:
                            SchemaUtil.writeUInt64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 39:
                            SchemaUtil.writeInt32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 40:
                            SchemaUtil.writeFixed64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 41:
                            SchemaUtil.writeFixed32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 42:
                            SchemaUtil.writeBoolList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 43:
                            SchemaUtil.writeUInt32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 44:
                            SchemaUtil.writeEnumList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 45:
                            SchemaUtil.writeSFixed32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 46:
                            SchemaUtil.writeSFixed64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 47:
                            SchemaUtil.writeSInt32List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 48:
                            SchemaUtil.writeSInt64List(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 49:
                            SchemaUtil.writeGroupList(numberAt(i10), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(i10));
                            break;
                        case 50:
                            writeMapHelper(writer, numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), i10);
                            break;
                        case 51:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeDouble(numberAt, oneofDoubleAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeFloat(numberAt, oneofFloatAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeInt64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeUInt64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeInt32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeFixed64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeFixed32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeBool(numberAt, oneofBooleanAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writeString(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 60:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(i10));
                                break;
                            } else {
                                break;
                            }
                        case 61:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 62:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeUInt32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeEnum(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSFixed32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSFixed64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSInt32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeSInt64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (isOneofPresent(t7, numberAt, i10)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(i10));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                while (entry != null) {
                    this.extensionSchema.serializeExtension(writer, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
                writeUnknownInMessageTo(this.unknownFieldSchema, t7, writer);
            }
        }
        it = null;
        entry = null;
        length = this.buffer.length;
        while (i10 < length) {
        }
        while (entry != null) {
        }
        writeUnknownInMessageTo(this.unknownFieldSchema, t7, writer);
    }

    /* JADX WARN: Removed duplicated region for block: B:275:0x058e  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFieldsInDescendingOrder(T t7, Writer writer) {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, ?> entry;
        int length;
        writeUnknownInMessageTo(this.unknownFieldSchema, t7, writer);
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(t7);
            if (!extensions.isEmpty()) {
                it = extensions.descendingIterator();
                entry = (Map.Entry) it.next();
                for (length = this.buffer.length - 3; length >= 0; length -= 3) {
                    int typeAndOffsetAt = typeAndOffsetAt(length);
                    int numberAt = numberAt(length);
                    while (entry != null && this.extensionSchema.extensionNumber(entry) > numberAt) {
                        this.extensionSchema.serializeExtension(writer, entry);
                        entry = it.hasNext() ? (Map.Entry) it.next() : null;
                    }
                    switch (type(typeAndOffsetAt)) {
                        case 0:
                            if (isFieldPresent(t7, length)) {
                                writer.writeDouble(numberAt, doubleAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (isFieldPresent(t7, length)) {
                                writer.writeFloat(numberAt, floatAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (isFieldPresent(t7, length)) {
                                writer.writeInt64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (isFieldPresent(t7, length)) {
                                writer.writeUInt64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (isFieldPresent(t7, length)) {
                                writer.writeInt32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (isFieldPresent(t7, length)) {
                                writer.writeFixed64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (isFieldPresent(t7, length)) {
                                writer.writeFixed32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (isFieldPresent(t7, length)) {
                                writer.writeBool(numberAt, booleanAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (isFieldPresent(t7, length)) {
                                writeString(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if (isFieldPresent(t7, length)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if (isFieldPresent(t7, length)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if (isFieldPresent(t7, length)) {
                                writer.writeUInt32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (isFieldPresent(t7, length)) {
                                writer.writeEnum(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (isFieldPresent(t7, length)) {
                                writer.writeSFixed32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (isFieldPresent(t7, length)) {
                                writer.writeSFixed64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (isFieldPresent(t7, length)) {
                                writer.writeSInt32(numberAt, intAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (isFieldPresent(t7, length)) {
                                writer.writeSInt64(numberAt, longAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (isFieldPresent(t7, length)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            SchemaUtil.writeDoubleList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 19:
                            SchemaUtil.writeFloatList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 20:
                            SchemaUtil.writeInt64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 21:
                            SchemaUtil.writeUInt64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 22:
                            SchemaUtil.writeInt32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 23:
                            SchemaUtil.writeFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 24:
                            SchemaUtil.writeFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 25:
                            SchemaUtil.writeBoolList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 26:
                            SchemaUtil.writeStringList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                            break;
                        case 27:
                            SchemaUtil.writeMessageList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(length));
                            break;
                        case 28:
                            SchemaUtil.writeBytesList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                            break;
                        case 29:
                            SchemaUtil.writeUInt32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 30:
                            SchemaUtil.writeEnumList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 31:
                            SchemaUtil.writeSFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 32:
                            SchemaUtil.writeSFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 33:
                            SchemaUtil.writeSInt32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 34:
                            SchemaUtil.writeSInt64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 35:
                            SchemaUtil.writeDoubleList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 36:
                            SchemaUtil.writeFloatList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 37:
                            SchemaUtil.writeInt64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 38:
                            SchemaUtil.writeUInt64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 39:
                            SchemaUtil.writeInt32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 40:
                            SchemaUtil.writeFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 41:
                            SchemaUtil.writeFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 42:
                            SchemaUtil.writeBoolList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 43:
                            SchemaUtil.writeUInt32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 44:
                            SchemaUtil.writeEnumList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 45:
                            SchemaUtil.writeSFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 46:
                            SchemaUtil.writeSFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 47:
                            SchemaUtil.writeSInt32List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 48:
                            SchemaUtil.writeSInt64List(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 49:
                            SchemaUtil.writeGroupList(numberAt(length), (List) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(length));
                            break;
                        case 50:
                            writeMapHelper(writer, numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), length);
                            break;
                        case 51:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeDouble(numberAt, oneofDoubleAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeFloat(numberAt, oneofFloatAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeInt64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeUInt64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeInt32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeFixed64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeFixed32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeBool(numberAt, oneofBooleanAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writeString(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 60:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                        case 61:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 62:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeUInt32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeEnum(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeSFixed32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeSFixed64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeSInt32(numberAt, oneofIntAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeSInt64(numberAt, oneofLongAt(t7, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (isOneofPresent(t7, numberAt, length)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t7, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                while (entry != null) {
                    this.extensionSchema.serializeExtension(writer, entry);
                    entry = it.hasNext() ? (Map.Entry) it.next() : null;
                }
            }
        }
        it = null;
        entry = null;
        while (length >= 0) {
        }
        while (entry != null) {
        }
    }

    private <K, V> void writeMapHelper(Writer writer, int i10, Object obj, int i11) {
        if (obj != null) {
            writer.writeMap(i10, this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i11)), this.mapFieldSchema.forMapData(obj));
        }
    }

    private void writeString(int i10, Object obj, Writer writer) {
        if (obj instanceof String) {
            writer.writeString(i10, (String) obj);
        } else {
            writer.writeBytes(i10, (ByteString) obj);
        }
    }

    private <UT, UB> void writeUnknownInMessageTo(UnknownFieldSchema<UT, UB> unknownFieldSchema, T t7, Writer writer) {
        unknownFieldSchema.writeTo(unknownFieldSchema.getFromMessage(t7), writer);
    }

    @Override // com.google.protobuf.Schema
    public boolean equals(T t7, T t10) {
        int length = this.buffer.length;
        for (int i10 = 0; i10 < length; i10 += 3) {
            if (!equals(t7, t10, i10)) {
                return false;
            }
        }
        if (!this.unknownFieldSchema.getFromMessage(t7).equals(this.unknownFieldSchema.getFromMessage(t10))) {
            return false;
        }
        if (this.hasExtensions) {
            return this.extensionSchema.getExtensions(t7).equals(this.extensionSchema.getExtensions(t10));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSchemaSize() {
        return this.buffer.length * 3;
    }

    @Override // com.google.protobuf.Schema
    public int getSerializedSize(T t7) {
        return this.proto3 ? getSerializedSizeProto3(t7) : getSerializedSizeProto2(t7);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0019. Please report as an issue. */
    @Override // com.google.protobuf.Schema
    public int hashCode(T t7) {
        int i10;
        int hashLong;
        int length = this.buffer.length;
        int i11 = 0;
        for (int i12 = 0; i12 < length; i12 += 3) {
            int typeAndOffsetAt = typeAndOffsetAt(i12);
            int numberAt = numberAt(i12);
            long offset = offset(typeAndOffsetAt);
            int i13 = 37;
            switch (type(typeAndOffsetAt)) {
                case 0:
                    i10 = i11 * 53;
                    hashLong = Internal.hashLong(Double.doubleToLongBits(UnsafeUtil.getDouble(t7, offset)));
                    i11 = i10 + hashLong;
                    break;
                case 1:
                    i10 = i11 * 53;
                    hashLong = Float.floatToIntBits(UnsafeUtil.getFloat(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 2:
                    i10 = i11 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 3:
                    i10 = i11 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 4:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getInt(t7, offset);
                    i11 = i10 + hashLong;
                    break;
                case 5:
                    i10 = i11 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 6:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getInt(t7, offset);
                    i11 = i10 + hashLong;
                    break;
                case 7:
                    i10 = i11 * 53;
                    hashLong = Internal.hashBoolean(UnsafeUtil.getBoolean(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 8:
                    i10 = i11 * 53;
                    hashLong = ((String) UnsafeUtil.getObject(t7, offset)).hashCode();
                    i11 = i10 + hashLong;
                    break;
                case 9:
                    Object object = UnsafeUtil.getObject(t7, offset);
                    if (object != null) {
                        i13 = object.hashCode();
                    }
                    i11 = (i11 * 53) + i13;
                    break;
                case 10:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getObject(t7, offset).hashCode();
                    i11 = i10 + hashLong;
                    break;
                case 11:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getInt(t7, offset);
                    i11 = i10 + hashLong;
                    break;
                case 12:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getInt(t7, offset);
                    i11 = i10 + hashLong;
                    break;
                case 13:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getInt(t7, offset);
                    i11 = i10 + hashLong;
                    break;
                case 14:
                    i10 = i11 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 15:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getInt(t7, offset);
                    i11 = i10 + hashLong;
                    break;
                case 16:
                    i10 = i11 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t7, offset));
                    i11 = i10 + hashLong;
                    break;
                case 17:
                    Object object2 = UnsafeUtil.getObject(t7, offset);
                    if (object2 != null) {
                        i13 = object2.hashCode();
                    }
                    i11 = (i11 * 53) + i13;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getObject(t7, offset).hashCode();
                    i11 = i10 + hashLong;
                    break;
                case 50:
                    i10 = i11 * 53;
                    hashLong = UnsafeUtil.getObject(t7, offset).hashCode();
                    i11 = i10 + hashLong;
                    break;
                case 51:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashLong(Double.doubleToLongBits(oneofDoubleAt(t7, offset)));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Float.floatToIntBits(oneofFloatAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = oneofIntAt(t7, offset);
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = oneofIntAt(t7, offset);
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashBoolean(oneofBooleanAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = ((String) UnsafeUtil.getObject(t7, offset)).hashCode();
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = UnsafeUtil.getObject(t7, offset).hashCode();
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = UnsafeUtil.getObject(t7, offset).hashCode();
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = oneofIntAt(t7, offset);
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = oneofIntAt(t7, offset);
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = oneofIntAt(t7, offset);
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = oneofIntAt(t7, offset);
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t7, offset));
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (isOneofPresent(t7, numberAt, i12)) {
                        i10 = i11 * 53;
                        hashLong = UnsafeUtil.getObject(t7, offset).hashCode();
                        i11 = i10 + hashLong;
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i11 * 53) + this.unknownFieldSchema.getFromMessage(t7).hashCode();
        return this.hasExtensions ? (hashCode * 53) + this.extensionSchema.getExtensions(t7).hashCode() : hashCode;
    }

    @Override // com.google.protobuf.Schema
    public final boolean isInitialized(T t7) {
        int i10;
        int i11;
        int i12 = 1048575;
        int i13 = 0;
        int i14 = 0;
        while (i14 < this.checkInitializedCount) {
            int i15 = this.intArray[i14];
            int numberAt = numberAt(i15);
            int typeAndOffsetAt = typeAndOffsetAt(i15);
            int i16 = this.buffer[i15 + 2];
            int i17 = i16 & 1048575;
            int i18 = 1 << (i16 >>> 20);
            if (i17 != i12) {
                if (i17 != 1048575) {
                    i13 = UNSAFE.getInt(t7, i17);
                }
                i11 = i13;
                i10 = i17;
            } else {
                i10 = i12;
                i11 = i13;
            }
            if (isRequired(typeAndOffsetAt) && !isFieldPresent(t7, i15, i10, i11, i18)) {
                return false;
            }
            int type = type(typeAndOffsetAt);
            if (type != 9 && type != 17) {
                if (type != 27) {
                    if (type == 60 || type == 68) {
                        if (isOneofPresent(t7, numberAt, i15) && !isInitialized(t7, typeAndOffsetAt, getMessageFieldSchema(i15))) {
                            return false;
                        }
                    } else if (type != 49) {
                        if (type == 50 && !isMapInitialized(t7, typeAndOffsetAt, i15)) {
                            return false;
                        }
                    }
                }
                if (!isListInitialized(t7, typeAndOffsetAt, i15)) {
                    return false;
                }
            } else if (isFieldPresent(t7, i15, i10, i11, i18) && !isInitialized(t7, typeAndOffsetAt, getMessageFieldSchema(i15))) {
                return false;
            }
            i14++;
            i12 = i10;
            i13 = i11;
        }
        return !this.hasExtensions || this.extensionSchema.getExtensions(t7).isInitialized();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.protobuf.Schema
    public void makeImmutable(T t7) {
        if (isMutable(t7)) {
            if (t7 instanceof GeneratedMessageLite) {
                GeneratedMessageLite generatedMessageLite = (GeneratedMessageLite) t7;
                generatedMessageLite.clearMemoizedSerializedSize();
                generatedMessageLite.clearMemoizedHashCode();
                generatedMessageLite.markImmutable();
            }
            int length = this.buffer.length;
            for (int i10 = 0; i10 < length; i10 += 3) {
                int typeAndOffsetAt = typeAndOffsetAt(i10);
                long offset = offset(typeAndOffsetAt);
                int type = type(typeAndOffsetAt);
                if (type != 9) {
                    switch (type) {
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                            this.listFieldSchema.makeImmutableListAt(t7, offset);
                            break;
                        case 50:
                            Unsafe unsafe = UNSAFE;
                            Object object = unsafe.getObject(t7, offset);
                            if (object != null) {
                                unsafe.putObject(t7, offset, this.mapFieldSchema.toImmutable(object));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                if (isFieldPresent(t7, i10)) {
                    getMessageFieldSchema(i10).makeImmutable(UNSAFE.getObject(t7, offset));
                }
            }
            this.unknownFieldSchema.makeImmutable(t7);
            if (this.hasExtensions) {
                this.extensionSchema.makeImmutable(t7);
            }
        }
    }

    @Override // com.google.protobuf.Schema
    public void mergeFrom(T t7, T t10) {
        checkMutable(t7);
        Objects.requireNonNull(t10);
        for (int i10 = 0; i10 < this.buffer.length; i10 += 3) {
            mergeSingleField(t7, t10, i10);
        }
        SchemaUtil.mergeUnknownFields(this.unknownFieldSchema, t7, t10);
        if (this.hasExtensions) {
            SchemaUtil.mergeExtensions(this.extensionSchema, t7, t10);
        }
    }

    @Override // com.google.protobuf.Schema
    public T newInstance() {
        return (T) this.newInstanceSchema.newInstance(this.defaultInstance);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:98:0x008f. Please report as an issue. */
    @CanIgnoreReturnValue
    public int parseProto2Message(T t7, byte[] bArr, int i10, int i11, int i12, ArrayDecoders.Registers registers) {
        Unsafe unsafe;
        int i13;
        MessageSchema<T> messageSchema;
        int i14;
        int i15;
        int i16;
        int i17;
        T t10;
        int i18;
        int positionForFieldNumber;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        byte[] bArr2;
        int decodeVarint64;
        int i29;
        int i30;
        MessageSchema<T> messageSchema2 = this;
        T t11 = t7;
        byte[] bArr3 = bArr;
        int i31 = i11;
        int i32 = i12;
        ArrayDecoders.Registers registers2 = registers;
        checkMutable(t7);
        Unsafe unsafe2 = UNSAFE;
        int i33 = i10;
        int i34 = 0;
        int i35 = 0;
        int i36 = 0;
        int i37 = -1;
        int i38 = 1048575;
        while (true) {
            if (i33 < i31) {
                int i39 = i33 + 1;
                byte b10 = bArr3[i33];
                if (b10 < 0) {
                    int decodeVarint32 = ArrayDecoders.decodeVarint32(b10, bArr3, i39, registers2);
                    i18 = registers2.int1;
                    i39 = decodeVarint32;
                } else {
                    i18 = b10;
                }
                int i40 = i18 >>> 3;
                int i41 = i18 & 7;
                if (i40 > i37) {
                    positionForFieldNumber = messageSchema2.positionForFieldNumber(i40, i34 / 3);
                } else {
                    positionForFieldNumber = messageSchema2.positionForFieldNumber(i40);
                }
                int i42 = positionForFieldNumber;
                if (i42 == -1) {
                    i19 = i40;
                    i20 = i39;
                    i15 = i18;
                    i21 = i36;
                    i22 = i38;
                    unsafe = unsafe2;
                    i13 = i32;
                    i23 = 0;
                } else {
                    int i43 = messageSchema2.buffer[i42 + 1];
                    int type = type(i43);
                    long offset = offset(i43);
                    int i44 = i18;
                    if (type <= 17) {
                        int i45 = messageSchema2.buffer[i42 + 2];
                        int i46 = 1 << (i45 >>> 20);
                        int i47 = i45 & 1048575;
                        if (i47 != i38) {
                            if (i38 != 1048575) {
                                unsafe2.putInt(t11, i38, i36);
                            }
                            i25 = i47;
                            i24 = unsafe2.getInt(t11, i47);
                        } else {
                            i24 = i36;
                            i25 = i38;
                        }
                        switch (type) {
                            case 0:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 1) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    UnsafeUtil.putDouble(t11, offset, ArrayDecoders.decodeDouble(bArr2, i39));
                                    i33 = i39 + 8;
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 1:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 5) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    UnsafeUtil.putFloat(t11, offset, ArrayDecoders.decodeFloat(bArr2, i39));
                                    i33 = i39 + 4;
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 2:
                            case 3:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 0) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    decodeVarint64 = ArrayDecoders.decodeVarint64(bArr2, i39, registers2);
                                    unsafe2.putLong(t7, offset, registers2.long1);
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i33 = decodeVarint64;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 4:
                            case 11:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 0) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    i33 = ArrayDecoders.decodeVarint32(bArr2, i39, registers2);
                                    unsafe2.putInt(t11, offset, registers2.int1);
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 5:
                            case 14:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 1) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    unsafe2.putLong(t7, offset, ArrayDecoders.decodeFixed64(bArr2, i39));
                                    i33 = i39 + 8;
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 6:
                            case 13:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 5) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    unsafe2.putInt(t11, offset, ArrayDecoders.decodeFixed32(bArr2, i39));
                                    i33 = i39 + 4;
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 7:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 0) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    i33 = ArrayDecoders.decodeVarint64(bArr2, i39, registers2);
                                    UnsafeUtil.putBoolean(t11, offset, registers2.long1 != 0);
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 8:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 2) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    if ((ENFORCE_UTF8_MASK & i43) == 0) {
                                        i33 = ArrayDecoders.decodeString(bArr2, i39, registers2);
                                    } else {
                                        i33 = ArrayDecoders.decodeStringRequireUtf8(bArr2, i39, registers2);
                                    }
                                    unsafe2.putObject(t11, offset, registers2.object1);
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 9:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 2) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    Object mutableMessageFieldForMerge = messageSchema2.mutableMessageFieldForMerge(t11, i26);
                                    i33 = ArrayDecoders.mergeMessageField(mutableMessageFieldForMerge, messageSchema2.getMessageFieldSchema(i26), bArr, i39, i11, registers);
                                    messageSchema2.storeMessageField(t11, i26, mutableMessageFieldForMerge);
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 10:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 2) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    i33 = ArrayDecoders.decodeBytes(bArr2, i39, registers2);
                                    unsafe2.putObject(t11, offset, registers2.object1);
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 12:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 0) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    i33 = ArrayDecoders.decodeVarint32(bArr2, i39, registers2);
                                    int i48 = registers2.int1;
                                    Internal.EnumVerifier enumFieldVerifier = messageSchema2.getEnumFieldVerifier(i26);
                                    if (enumFieldVerifier != null && !enumFieldVerifier.isInRange(i48)) {
                                        getMutableUnknownFields(t7).storeField(i28, Long.valueOf(i48));
                                        i34 = i26;
                                        i36 = i24;
                                        i35 = i28;
                                        i37 = i19;
                                        i32 = i12;
                                        bArr3 = bArr2;
                                        i38 = i27;
                                    } else {
                                        unsafe2.putInt(t11, offset, i48);
                                        i36 = i24 | i46;
                                        i32 = i12;
                                        i34 = i26;
                                        i35 = i28;
                                        i37 = i19;
                                        bArr3 = bArr2;
                                        i38 = i27;
                                    }
                                }
                                break;
                            case 15:
                                bArr2 = bArr;
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                if (i41 != 0) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    i33 = ArrayDecoders.decodeVarint32(bArr2, i39, registers2);
                                    unsafe2.putInt(t11, offset, CodedInputStream.decodeZigZag32(registers2.int1));
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 16:
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                bArr2 = bArr;
                                if (i41 != 0) {
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    decodeVarint64 = ArrayDecoders.decodeVarint64(bArr2, i39, registers2);
                                    unsafe2.putLong(t7, offset, CodedInputStream.decodeZigZag64(registers2.long1));
                                    i36 = i24 | i46;
                                    i32 = i12;
                                    i33 = decodeVarint64;
                                    i34 = i26;
                                    i35 = i28;
                                    i37 = i19;
                                    bArr3 = bArr2;
                                    i38 = i27;
                                }
                            case 17:
                                if (i41 != 3) {
                                    i19 = i40;
                                    i26 = i42;
                                    i27 = i25;
                                    i28 = i44;
                                    i22 = i27;
                                    i20 = i39;
                                    i23 = i26;
                                    unsafe = unsafe2;
                                    i21 = i24;
                                    i15 = i28;
                                    i13 = i12;
                                    break;
                                } else {
                                    Object mutableMessageFieldForMerge2 = messageSchema2.mutableMessageFieldForMerge(t11, i42);
                                    i33 = ArrayDecoders.mergeGroupField(mutableMessageFieldForMerge2, messageSchema2.getMessageFieldSchema(i42), bArr, i39, i11, (i40 << 3) | 4, registers);
                                    messageSchema2.storeMessageField(t11, i42, mutableMessageFieldForMerge2);
                                    i36 = i24 | i46;
                                    i38 = i25;
                                    i32 = i12;
                                    i34 = i42;
                                    i35 = i44;
                                    i37 = i40;
                                    bArr3 = bArr;
                                }
                            default:
                                i19 = i40;
                                i26 = i42;
                                i27 = i25;
                                i28 = i44;
                                i22 = i27;
                                i20 = i39;
                                i23 = i26;
                                unsafe = unsafe2;
                                i21 = i24;
                                i15 = i28;
                                i13 = i12;
                                break;
                        }
                    } else {
                        i19 = i40;
                        i22 = i38;
                        i21 = i36;
                        if (type == 27) {
                            if (i41 == 2) {
                                Internal.ProtobufList protobufList = (Internal.ProtobufList) unsafe2.getObject(t11, offset);
                                if (!protobufList.isModifiable()) {
                                    int size = protobufList.size();
                                    protobufList = protobufList.mutableCopyWithCapacity2(size == 0 ? 10 : size * 2);
                                    unsafe2.putObject(t11, offset, protobufList);
                                }
                                i33 = ArrayDecoders.decodeMessageList(messageSchema2.getMessageFieldSchema(i42), i44, bArr, i39, i11, protobufList, registers);
                                i34 = i42;
                                i35 = i44;
                                i38 = i22;
                                i36 = i21;
                                i37 = i19;
                                bArr3 = bArr;
                                i32 = i12;
                            } else {
                                i29 = i39;
                                unsafe = unsafe2;
                                i23 = i42;
                                i30 = i44;
                                i13 = i12;
                                i20 = i29;
                            }
                        } else if (type <= 49) {
                            int i49 = i39;
                            unsafe = unsafe2;
                            i23 = i42;
                            i30 = i44;
                            i33 = parseRepeatedField(t7, bArr, i39, i11, i44, i19, i41, i42, i43, type, offset, registers);
                            if (i33 != i49) {
                                messageSchema2 = this;
                                t11 = t7;
                                bArr3 = bArr;
                                i31 = i11;
                                i32 = i12;
                                registers2 = registers;
                                i38 = i22;
                                i36 = i21;
                                i34 = i23;
                                i35 = i30;
                                i37 = i19;
                                unsafe2 = unsafe;
                            } else {
                                i13 = i12;
                                i20 = i33;
                            }
                        } else {
                            i29 = i39;
                            unsafe = unsafe2;
                            i23 = i42;
                            i30 = i44;
                            if (type != 50) {
                                i33 = parseOneofField(t7, bArr, i29, i11, i30, i19, i41, i43, type, offset, i23, registers);
                                if (i33 != i29) {
                                    messageSchema2 = this;
                                    t11 = t7;
                                    bArr3 = bArr;
                                    i31 = i11;
                                    i32 = i12;
                                    registers2 = registers;
                                    i38 = i22;
                                    i36 = i21;
                                    i34 = i23;
                                    i35 = i30;
                                    i37 = i19;
                                    unsafe2 = unsafe;
                                } else {
                                    i13 = i12;
                                    i20 = i33;
                                }
                            } else if (i41 == 2) {
                                i33 = parseMapField(t7, bArr, i29, i11, i23, offset, registers);
                                if (i33 != i29) {
                                    messageSchema2 = this;
                                    t11 = t7;
                                    bArr3 = bArr;
                                    i31 = i11;
                                    i32 = i12;
                                    registers2 = registers;
                                    i38 = i22;
                                    i36 = i21;
                                    i34 = i23;
                                    i35 = i30;
                                    i37 = i19;
                                    unsafe2 = unsafe;
                                } else {
                                    i13 = i12;
                                    i20 = i33;
                                }
                            } else {
                                i13 = i12;
                                i20 = i29;
                            }
                        }
                        i15 = i30;
                    }
                }
                if (i15 != i13 || i13 == 0) {
                    if (this.hasExtensions && registers.extensionRegistry != ExtensionRegistryLite.getEmptyRegistry()) {
                        i33 = ArrayDecoders.decodeExtensionOrUnknownField(i15, bArr, i20, i11, t7, this.defaultInstance, this.unknownFieldSchema, registers);
                    } else {
                        i33 = ArrayDecoders.decodeUnknownField(i15, bArr, i20, i11, getMutableUnknownFields(t7), registers);
                    }
                    t11 = t7;
                    bArr3 = bArr;
                    i31 = i11;
                    i35 = i15;
                    messageSchema2 = this;
                    registers2 = registers;
                    i38 = i22;
                    i36 = i21;
                    i34 = i23;
                    i37 = i19;
                    unsafe2 = unsafe;
                    i32 = i13;
                } else {
                    i17 = 1048575;
                    messageSchema = this;
                    i14 = i20;
                    i16 = i22;
                    i36 = i21;
                }
            } else {
                int i50 = i38;
                unsafe = unsafe2;
                i13 = i32;
                messageSchema = messageSchema2;
                i14 = i33;
                i15 = i35;
                i16 = i50;
                i17 = 1048575;
            }
        }
        if (i16 != i17) {
            t10 = t7;
            unsafe.putInt(t10, i16, i36);
        } else {
            t10 = t7;
        }
        UnknownFieldSetLite unknownFieldSetLite = null;
        for (int i51 = messageSchema.checkInitializedCount; i51 < messageSchema.repeatedFieldOffsetStart; i51++) {
            unknownFieldSetLite = (UnknownFieldSetLite) filterMapUnknownEnumValues(t7, messageSchema.intArray[i51], unknownFieldSetLite, messageSchema.unknownFieldSchema, t7);
        }
        if (unknownFieldSetLite != null) {
            messageSchema.unknownFieldSchema.setBuilderToMessage(t10, unknownFieldSetLite);
        }
        if (i13 == 0) {
            if (i14 != i11) {
                throw InvalidProtocolBufferException.parseFailure();
            }
        } else if (i14 > i11 || i15 != i13) {
            throw InvalidProtocolBufferException.parseFailure();
        }
        return i14;
    }

    @Override // com.google.protobuf.Schema
    public void writeTo(T t7, Writer writer) {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            writeFieldsInDescendingOrder(t7, writer);
        } else if (this.proto3) {
            writeFieldsInAscendingOrderProto3(t7, writer);
        } else {
            writeFieldsInAscendingOrderProto2(t7, writer);
        }
    }

    private boolean isFieldPresent(T t7, int i10) {
        int presenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i10);
        long j10 = 1048575 & presenceMaskAndOffsetAt;
        if (j10 != 1048575) {
            return ((1 << (presenceMaskAndOffsetAt >>> 20)) & UnsafeUtil.getInt(t7, j10)) != 0;
        }
        int typeAndOffsetAt = typeAndOffsetAt(i10);
        long offset = offset(typeAndOffsetAt);
        switch (type(typeAndOffsetAt)) {
            case 0:
                return Double.doubleToRawLongBits(UnsafeUtil.getDouble(t7, offset)) != 0;
            case 1:
                return Float.floatToRawIntBits(UnsafeUtil.getFloat(t7, offset)) != 0;
            case 2:
                return UnsafeUtil.getLong(t7, offset) != 0;
            case 3:
                return UnsafeUtil.getLong(t7, offset) != 0;
            case 4:
                return UnsafeUtil.getInt(t7, offset) != 0;
            case 5:
                return UnsafeUtil.getLong(t7, offset) != 0;
            case 6:
                return UnsafeUtil.getInt(t7, offset) != 0;
            case 7:
                return UnsafeUtil.getBoolean(t7, offset);
            case 8:
                Object object = UnsafeUtil.getObject(t7, offset);
                if (object instanceof String) {
                    return !((String) object).isEmpty();
                }
                if (object instanceof ByteString) {
                    return !ByteString.EMPTY.equals(object);
                }
                throw new IllegalArgumentException();
            case 9:
                return UnsafeUtil.getObject(t7, offset) != null;
            case 10:
                return !ByteString.EMPTY.equals(UnsafeUtil.getObject(t7, offset));
            case 11:
                return UnsafeUtil.getInt(t7, offset) != 0;
            case 12:
                return UnsafeUtil.getInt(t7, offset) != 0;
            case 13:
                return UnsafeUtil.getInt(t7, offset) != 0;
            case 14:
                return UnsafeUtil.getLong(t7, offset) != 0;
            case 15:
                return UnsafeUtil.getInt(t7, offset) != 0;
            case 16:
                return UnsafeUtil.getLong(t7, offset) != 0;
            case 17:
                return UnsafeUtil.getObject(t7, offset) != null;
            default:
                throw new IllegalArgumentException();
        }
    }

    private int positionForFieldNumber(int i10, int i11) {
        if (i10 < this.minFieldNumber || i10 > this.maxFieldNumber) {
            return -1;
        }
        return slowPositionForFieldNumber(i10, i11);
    }

    @Override // com.google.protobuf.Schema
    public void mergeFrom(T t7, Reader reader, ExtensionRegistryLite extensionRegistryLite) {
        Objects.requireNonNull(extensionRegistryLite);
        checkMutable(t7);
        mergeFromHelper(this.unknownFieldSchema, this.extensionSchema, t7, reader, extensionRegistryLite);
    }

    private boolean equals(T t7, T t10, int i10) {
        int typeAndOffsetAt = typeAndOffsetAt(i10);
        long offset = offset(typeAndOffsetAt);
        switch (type(typeAndOffsetAt)) {
            case 0:
                return arePresentForEquals(t7, t10, i10) && Double.doubleToLongBits(UnsafeUtil.getDouble(t7, offset)) == Double.doubleToLongBits(UnsafeUtil.getDouble(t10, offset));
            case 1:
                return arePresentForEquals(t7, t10, i10) && Float.floatToIntBits(UnsafeUtil.getFloat(t7, offset)) == Float.floatToIntBits(UnsafeUtil.getFloat(t10, offset));
            case 2:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getLong(t7, offset) == UnsafeUtil.getLong(t10, offset);
            case 3:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getLong(t7, offset) == UnsafeUtil.getLong(t10, offset);
            case 4:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getInt(t7, offset) == UnsafeUtil.getInt(t10, offset);
            case 5:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getLong(t7, offset) == UnsafeUtil.getLong(t10, offset);
            case 6:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getInt(t7, offset) == UnsafeUtil.getInt(t10, offset);
            case 7:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getBoolean(t7, offset) == UnsafeUtil.getBoolean(t10, offset);
            case 8:
                return arePresentForEquals(t7, t10, i10) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            case 9:
                return arePresentForEquals(t7, t10, i10) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            case 10:
                return arePresentForEquals(t7, t10, i10) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            case 11:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getInt(t7, offset) == UnsafeUtil.getInt(t10, offset);
            case 12:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getInt(t7, offset) == UnsafeUtil.getInt(t10, offset);
            case 13:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getInt(t7, offset) == UnsafeUtil.getInt(t10, offset);
            case 14:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getLong(t7, offset) == UnsafeUtil.getLong(t10, offset);
            case 15:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getInt(t7, offset) == UnsafeUtil.getInt(t10, offset);
            case 16:
                return arePresentForEquals(t7, t10, i10) && UnsafeUtil.getLong(t7, offset) == UnsafeUtil.getLong(t10, offset);
            case 17:
                return arePresentForEquals(t7, t10, i10) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            case 50:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                return isOneofCaseEqual(t7, t10, i10) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t7, offset), UnsafeUtil.getObject(t10, offset));
            default:
                return true;
        }
    }

    @Override // com.google.protobuf.Schema
    public void mergeFrom(T t7, byte[] bArr, int i10, int i11, ArrayDecoders.Registers registers) {
        if (this.proto3) {
            parseProto3Message(t7, bArr, i10, i11, registers);
        } else {
            parseProto2Message(t7, bArr, i10, i11, 0, registers);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean isInitialized(Object obj, int i10, Schema schema) {
        return schema.isInitialized(UnsafeUtil.getObject(obj, offset(i10)));
    }
}
