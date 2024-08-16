package com.google.protobuf;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Utf8 {
    private static final long ASCII_MASK_LONG = -9187201950435737472L;
    static final int COMPLETE = 0;
    static final int MALFORMED = -1;
    static final int MAX_BYTES_PER_CHAR = 3;
    private static final int UNSAFE_COUNT_ASCII_THRESHOLD = 16;
    private static final Processor processor;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DecodeUtil {
        private DecodeUtil() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleFourBytes(byte b10, byte b11, byte b12, byte b13, char[] cArr, int i10) {
            if (!isNotTrailingByte(b11) && (((b10 << 28) + (b11 + 112)) >> 30) == 0 && !isNotTrailingByte(b12) && !isNotTrailingByte(b13)) {
                int trailingByteValue = ((b10 & 7) << 18) | (trailingByteValue(b11) << 12) | (trailingByteValue(b12) << 6) | trailingByteValue(b13);
                cArr[i10] = highSurrogate(trailingByteValue);
                cArr[i10 + 1] = lowSurrogate(trailingByteValue);
                return;
            }
            throw InvalidProtocolBufferException.invalidUtf8();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleOneByte(byte b10, char[] cArr, int i10) {
            cArr[i10] = (char) b10;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleThreeBytes(byte b10, byte b11, byte b12, char[] cArr, int i10) {
            if (!isNotTrailingByte(b11) && ((b10 != -32 || b11 >= -96) && ((b10 != -19 || b11 < -96) && !isNotTrailingByte(b12)))) {
                cArr[i10] = (char) (((b10 & 15) << 12) | (trailingByteValue(b11) << 6) | trailingByteValue(b12));
                return;
            }
            throw InvalidProtocolBufferException.invalidUtf8();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleTwoBytes(byte b10, byte b11, char[] cArr, int i10) {
            if (b10 >= -62 && !isNotTrailingByte(b11)) {
                cArr[i10] = (char) (((b10 & 31) << 6) | trailingByteValue(b11));
                return;
            }
            throw InvalidProtocolBufferException.invalidUtf8();
        }

        private static char highSurrogate(int i10) {
            return (char) ((i10 >>> 10) + 55232);
        }

        private static boolean isNotTrailingByte(byte b10) {
            return b10 > -65;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isOneByte(byte b10) {
            return b10 >= 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isThreeBytes(byte b10) {
            return b10 < -16;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isTwoBytes(byte b10) {
            return b10 < -32;
        }

        private static char lowSurrogate(int i10) {
            return (char) ((i10 & 1023) + 56320);
        }

        private static int trailingByteValue(byte b10) {
            return b10 & 63;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class Processor {
        Processor() {
        }

        final String decodeUtf8(ByteBuffer byteBuffer, int i10, int i11) {
            if (byteBuffer.hasArray()) {
                return decodeUtf8(byteBuffer.array(), byteBuffer.arrayOffset() + i10, i11);
            }
            if (byteBuffer.isDirect()) {
                return decodeUtf8Direct(byteBuffer, i10, i11);
            }
            return decodeUtf8Default(byteBuffer, i10, i11);
        }

        abstract String decodeUtf8(byte[] bArr, int i10, int i11);

        final String decodeUtf8Default(ByteBuffer byteBuffer, int i10, int i11) {
            if ((i10 | i11 | ((byteBuffer.limit() - i10) - i11)) >= 0) {
                int i12 = i10 + i11;
                char[] cArr = new char[i11];
                int i13 = 0;
                while (i10 < i12) {
                    byte b10 = byteBuffer.get(i10);
                    if (!DecodeUtil.isOneByte(b10)) {
                        break;
                    }
                    i10++;
                    DecodeUtil.handleOneByte(b10, cArr, i13);
                    i13++;
                }
                int i14 = i13;
                while (i10 < i12) {
                    int i15 = i10 + 1;
                    byte b11 = byteBuffer.get(i10);
                    if (DecodeUtil.isOneByte(b11)) {
                        int i16 = i14 + 1;
                        DecodeUtil.handleOneByte(b11, cArr, i14);
                        while (i15 < i12) {
                            byte b12 = byteBuffer.get(i15);
                            if (!DecodeUtil.isOneByte(b12)) {
                                break;
                            }
                            i15++;
                            DecodeUtil.handleOneByte(b12, cArr, i16);
                            i16++;
                        }
                        i10 = i15;
                        i14 = i16;
                    } else if (DecodeUtil.isTwoBytes(b11)) {
                        if (i15 < i12) {
                            DecodeUtil.handleTwoBytes(b11, byteBuffer.get(i15), cArr, i14);
                            i10 = i15 + 1;
                            i14++;
                        } else {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                    } else if (DecodeUtil.isThreeBytes(b11)) {
                        if (i15 < i12 - 1) {
                            int i17 = i15 + 1;
                            DecodeUtil.handleThreeBytes(b11, byteBuffer.get(i15), byteBuffer.get(i17), cArr, i14);
                            i10 = i17 + 1;
                            i14++;
                        } else {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                    } else if (i15 < i12 - 2) {
                        int i18 = i15 + 1;
                        byte b13 = byteBuffer.get(i15);
                        int i19 = i18 + 1;
                        DecodeUtil.handleFourBytes(b11, b13, byteBuffer.get(i18), byteBuffer.get(i19), cArr, i14);
                        i10 = i19 + 1;
                        i14 = i14 + 1 + 1;
                    } else {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                }
                return new String(cArr, 0, i14);
            }
            throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(byteBuffer.limit()), Integer.valueOf(i10), Integer.valueOf(i11)));
        }

        abstract String decodeUtf8Direct(ByteBuffer byteBuffer, int i10, int i11);

        abstract int encodeUtf8(CharSequence charSequence, byte[] bArr, int i10, int i11);

        final void encodeUtf8(CharSequence charSequence, ByteBuffer byteBuffer) {
            if (byteBuffer.hasArray()) {
                int arrayOffset = byteBuffer.arrayOffset();
                byteBuffer.position(Utf8.encode(charSequence, byteBuffer.array(), byteBuffer.position() + arrayOffset, byteBuffer.remaining()) - arrayOffset);
            } else if (byteBuffer.isDirect()) {
                encodeUtf8Direct(charSequence, byteBuffer);
            } else {
                encodeUtf8Default(charSequence, byteBuffer);
            }
        }

        final void encodeUtf8Default(CharSequence charSequence, ByteBuffer byteBuffer) {
            int length = charSequence.length();
            int position = byteBuffer.position();
            int i10 = 0;
            while (i10 < length) {
                try {
                    char charAt = charSequence.charAt(i10);
                    if (charAt >= 128) {
                        break;
                    }
                    byteBuffer.put(position + i10, (byte) charAt);
                    i10++;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i10) + " at index " + (byteBuffer.position() + Math.max(i10, (position - byteBuffer.position()) + 1)));
                }
            }
            if (i10 == length) {
                byteBuffer.position(position + i10);
                return;
            }
            position += i10;
            while (i10 < length) {
                char charAt2 = charSequence.charAt(i10);
                if (charAt2 < 128) {
                    byteBuffer.put(position, (byte) charAt2);
                } else if (charAt2 < 2048) {
                    int i11 = position + 1;
                    try {
                        byteBuffer.put(position, (byte) ((charAt2 >>> 6) | 192));
                        byteBuffer.put(i11, (byte) ((charAt2 & '?') | 128));
                        position = i11;
                    } catch (IndexOutOfBoundsException unused2) {
                        position = i11;
                        throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i10) + " at index " + (byteBuffer.position() + Math.max(i10, (position - byteBuffer.position()) + 1)));
                    }
                } else {
                    if (charAt2 >= 55296 && 57343 >= charAt2) {
                        int i12 = i10 + 1;
                        if (i12 != length) {
                            try {
                                char charAt3 = charSequence.charAt(i12);
                                if (Character.isSurrogatePair(charAt2, charAt3)) {
                                    int codePoint = Character.toCodePoint(charAt2, charAt3);
                                    int i13 = position + 1;
                                    try {
                                        byteBuffer.put(position, (byte) ((codePoint >>> 18) | 240));
                                        int i14 = i13 + 1;
                                        byteBuffer.put(i13, (byte) (((codePoint >>> 12) & 63) | 128));
                                        int i15 = i14 + 1;
                                        byteBuffer.put(i14, (byte) (((codePoint >>> 6) & 63) | 128));
                                        byteBuffer.put(i15, (byte) ((codePoint & 63) | 128));
                                        position = i15;
                                        i10 = i12;
                                    } catch (IndexOutOfBoundsException unused3) {
                                        position = i13;
                                        i10 = i12;
                                        throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i10) + " at index " + (byteBuffer.position() + Math.max(i10, (position - byteBuffer.position()) + 1)));
                                    }
                                } else {
                                    i10 = i12;
                                }
                            } catch (IndexOutOfBoundsException unused4) {
                            }
                        }
                        throw new UnpairedSurrogateException(i10, length);
                    }
                    int i16 = position + 1;
                    byteBuffer.put(position, (byte) ((charAt2 >>> '\f') | 224));
                    position = i16 + 1;
                    byteBuffer.put(i16, (byte) (((charAt2 >>> 6) & 63) | 128));
                    byteBuffer.put(position, (byte) ((charAt2 & '?') | 128));
                }
                i10++;
                position++;
            }
            byteBuffer.position(position);
        }

        abstract void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer);

        final boolean isValidUtf8(byte[] bArr, int i10, int i11) {
            return partialIsValidUtf8(0, bArr, i10, i11) == 0;
        }

        final int partialIsValidUtf8(int i10, ByteBuffer byteBuffer, int i11, int i12) {
            if (byteBuffer.hasArray()) {
                int arrayOffset = byteBuffer.arrayOffset();
                return partialIsValidUtf8(i10, byteBuffer.array(), i11 + arrayOffset, arrayOffset + i12);
            }
            if (byteBuffer.isDirect()) {
                return partialIsValidUtf8Direct(i10, byteBuffer, i11, i12);
            }
            return partialIsValidUtf8Default(i10, byteBuffer, i11, i12);
        }

        abstract int partialIsValidUtf8(int i10, byte[] bArr, int i11, int i12);

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0017, code lost:
        
            if (r7.get(r8) > (-65)) goto L13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x004c, code lost:
        
            if (r7.get(r8) > (-65)) goto L32;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x008b, code lost:
        
            if (r7.get(r8) > (-65)) goto L53;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        final int partialIsValidUtf8Default(int i10, ByteBuffer byteBuffer, int i11, int i12) {
            int i13;
            if (i10 != 0) {
                if (i11 >= i12) {
                    return i10;
                }
                byte b10 = (byte) i10;
                if (b10 < -32) {
                    if (b10 >= -62) {
                        i13 = i11 + 1;
                    }
                    return -1;
                }
                if (b10 < -16) {
                    byte b11 = (byte) (~(i10 >> 8));
                    if (b11 == 0) {
                        int i14 = i11 + 1;
                        byte b12 = byteBuffer.get(i11);
                        if (i14 >= i12) {
                            return Utf8.incompleteStateFor(b10, b12);
                        }
                        i11 = i14;
                        b11 = b12;
                    }
                    if (b11 <= -65 && ((b10 != -32 || b11 >= -96) && (b10 != -19 || b11 < -96))) {
                        i13 = i11 + 1;
                    }
                    return -1;
                }
                byte b13 = (byte) (~(i10 >> 8));
                byte b14 = 0;
                if (b13 == 0) {
                    int i15 = i11 + 1;
                    b13 = byteBuffer.get(i11);
                    if (i15 >= i12) {
                        return Utf8.incompleteStateFor(b10, b13);
                    }
                    i11 = i15;
                } else {
                    b14 = (byte) (i10 >> 16);
                }
                if (b14 == 0) {
                    int i16 = i11 + 1;
                    b14 = byteBuffer.get(i11);
                    if (i16 >= i12) {
                        return Utf8.incompleteStateFor(b10, b13, b14);
                    }
                    i11 = i16;
                }
                if (b13 <= -65 && (((b10 << 28) + (b13 + 112)) >> 30) == 0 && b14 <= -65) {
                    i13 = i11 + 1;
                }
                return -1;
                i11 = i13;
            }
            return partialIsValidUtf8(byteBuffer, i11, i12);
        }

        abstract int partialIsValidUtf8Direct(int i10, ByteBuffer byteBuffer, int i11, int i12);

        final boolean isValidUtf8(ByteBuffer byteBuffer, int i10, int i11) {
            return partialIsValidUtf8(0, byteBuffer, i10, i11) == 0;
        }

        private static int partialIsValidUtf8(ByteBuffer byteBuffer, int i10, int i11) {
            int estimateConsecutiveAscii = i10 + Utf8.estimateConsecutiveAscii(byteBuffer, i10, i11);
            while (estimateConsecutiveAscii < i11) {
                int i12 = estimateConsecutiveAscii + 1;
                byte b10 = byteBuffer.get(estimateConsecutiveAscii);
                if (b10 < 0) {
                    if (b10 < -32) {
                        if (i12 >= i11) {
                            return b10;
                        }
                        if (b10 < -62 || byteBuffer.get(i12) > -65) {
                            return -1;
                        }
                        i12++;
                    } else {
                        if (b10 >= -16) {
                            if (i12 >= i11 - 2) {
                                return Utf8.incompleteStateFor(byteBuffer, b10, i12, i11 - i12);
                            }
                            int i13 = i12 + 1;
                            byte b11 = byteBuffer.get(i12);
                            if (b11 <= -65 && (((b10 << 28) + (b11 + 112)) >> 30) == 0) {
                                int i14 = i13 + 1;
                                if (byteBuffer.get(i13) <= -65) {
                                    i12 = i14 + 1;
                                    if (byteBuffer.get(i14) > -65) {
                                    }
                                }
                            }
                            return -1;
                        }
                        if (i12 >= i11 - 1) {
                            return Utf8.incompleteStateFor(byteBuffer, b10, i12, i11 - i12);
                        }
                        int i15 = i12 + 1;
                        byte b12 = byteBuffer.get(i12);
                        if (b12 > -65 || ((b10 == -32 && b12 < -96) || ((b10 == -19 && b12 >= -96) || byteBuffer.get(i15) > -65))) {
                            return -1;
                        }
                        estimateConsecutiveAscii = i15 + 1;
                    }
                }
                estimateConsecutiveAscii = i12;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class UnpairedSurrogateException extends IllegalArgumentException {
        /* JADX INFO: Access modifiers changed from: package-private */
        public UnpairedSurrogateException(int i10, int i11) {
            super("Unpaired surrogate at index " + i10 + " of " + i11);
        }
    }

    static {
        processor = (!UnsafeProcessor.isAvailable() || Android.isOnAndroidDevice()) ? new SafeProcessor() : new UnsafeProcessor();
    }

    private Utf8() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String decodeUtf8(ByteBuffer byteBuffer, int i10, int i11) {
        return processor.decodeUtf8(byteBuffer, i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int encode(CharSequence charSequence, byte[] bArr, int i10, int i11) {
        return processor.encodeUtf8(charSequence, bArr, i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void encodeUtf8(CharSequence charSequence, ByteBuffer byteBuffer) {
        processor.encodeUtf8(charSequence, byteBuffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int encodedLength(CharSequence charSequence) {
        int length = charSequence.length();
        int i10 = 0;
        while (i10 < length && charSequence.charAt(i10) < 128) {
            i10++;
        }
        int i11 = length;
        while (true) {
            if (i10 < length) {
                char charAt = charSequence.charAt(i10);
                if (charAt >= 2048) {
                    i11 += encodedLengthGeneral(charSequence, i10);
                    break;
                }
                i11 += (127 - charAt) >>> 31;
                i10++;
            } else {
                break;
            }
        }
        if (i11 >= length) {
            return i11;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (i11 + 4294967296L));
    }

    private static int encodedLengthGeneral(CharSequence charSequence, int i10) {
        int length = charSequence.length();
        int i11 = 0;
        while (i10 < length) {
            char charAt = charSequence.charAt(i10);
            if (charAt < 2048) {
                i11 += (127 - charAt) >>> 31;
            } else {
                i11 += 2;
                if (55296 <= charAt && charAt <= 57343) {
                    if (Character.codePointAt(charSequence, i10) < 65536) {
                        throw new UnpairedSurrogateException(i10, length);
                    }
                    i10++;
                }
            }
            i10++;
        }
        return i11;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int estimateConsecutiveAscii(ByteBuffer byteBuffer, int i10, int i11) {
        int i12 = i11 - 7;
        int i13 = i10;
        while (i13 < i12 && (byteBuffer.getLong(i13) & ASCII_MASK_LONG) == 0) {
            i13 += 8;
        }
        return i13 - i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int i10) {
        if (i10 > -12) {
            return -1;
        }
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int i10, int i11) {
        if (i10 > -12 || i11 > -65) {
            return -1;
        }
        return i10 ^ (i11 << 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int i10, int i11, int i12) {
        if (i10 > -12 || i11 > -65 || i12 > -65) {
            return -1;
        }
        return (i10 ^ (i11 << 8)) ^ (i12 << 16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(byte[] bArr, int i10, int i11) {
        byte b10 = bArr[i10 - 1];
        int i12 = i11 - i10;
        if (i12 == 0) {
            return incompleteStateFor(b10);
        }
        if (i12 == 1) {
            return incompleteStateFor(b10, bArr[i10]);
        }
        if (i12 == 2) {
            return incompleteStateFor(b10, bArr[i10], bArr[i10 + 1]);
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidUtf8(byte[] bArr) {
        return processor.isValidUtf8(bArr, 0, bArr.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int partialIsValidUtf8(int i10, byte[] bArr, int i11, int i12) {
        return processor.partialIsValidUtf8(i10, bArr, i11, i12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String decodeUtf8(byte[] bArr, int i10, int i11) {
        return processor.decodeUtf8(bArr, i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidUtf8(byte[] bArr, int i10, int i11) {
        return processor.isValidUtf8(bArr, i10, i11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int partialIsValidUtf8(int i10, ByteBuffer byteBuffer, int i11, int i12) {
        return processor.partialIsValidUtf8(i10, byteBuffer, i11, i12);
    }

    /* loaded from: classes.dex */
    static final class UnsafeProcessor extends Processor {
        UnsafeProcessor() {
        }

        static boolean isAvailable() {
            return UnsafeUtil.hasUnsafeArrayOperations() && UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        private static int unsafeEstimateConsecutiveAscii(byte[] bArr, long j10, int i10) {
            int i11 = 0;
            if (i10 < 16) {
                return 0;
            }
            int i12 = 8 - (((int) j10) & 7);
            while (i11 < i12) {
                long j11 = 1 + j10;
                if (UnsafeUtil.getByte(bArr, j10) < 0) {
                    return i11;
                }
                i11++;
                j10 = j11;
            }
            while (true) {
                int i13 = i11 + 8;
                if (i13 > i10 || (UnsafeUtil.getLong((Object) bArr, UnsafeUtil.BYTE_ARRAY_BASE_OFFSET + j10) & Utf8.ASCII_MASK_LONG) != 0) {
                    break;
                }
                j10 += 8;
                i11 = i13;
            }
            while (i11 < i10) {
                long j12 = j10 + 1;
                if (UnsafeUtil.getByte(bArr, j10) < 0) {
                    return i11;
                }
                i11++;
                j10 = j12;
            }
            return i10;
        }

        private static int unsafeIncompleteStateFor(byte[] bArr, int i10, long j10, int i11) {
            if (i11 == 0) {
                return Utf8.incompleteStateFor(i10);
            }
            if (i11 == 1) {
                return Utf8.incompleteStateFor(i10, UnsafeUtil.getByte(bArr, j10));
            }
            if (i11 == 2) {
                return Utf8.incompleteStateFor(i10, UnsafeUtil.getByte(bArr, j10), UnsafeUtil.getByte(bArr, j10 + 1));
            }
            throw new AssertionError();
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8(byte[] bArr, int i10, int i11) {
            Charset charset = Internal.UTF_8;
            String str = new String(bArr, i10, i11, charset);
            if (str.contains("�") && !Arrays.equals(str.getBytes(charset), Arrays.copyOfRange(bArr, i10, i11 + i10))) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            return str;
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8Direct(ByteBuffer byteBuffer, int i10, int i11) {
            if ((i10 | i11 | ((byteBuffer.limit() - i10) - i11)) >= 0) {
                long addressOffset = UnsafeUtil.addressOffset(byteBuffer) + i10;
                long j10 = i11 + addressOffset;
                char[] cArr = new char[i11];
                int i12 = 0;
                while (addressOffset < j10) {
                    byte b10 = UnsafeUtil.getByte(addressOffset);
                    if (!DecodeUtil.isOneByte(b10)) {
                        break;
                    }
                    addressOffset++;
                    DecodeUtil.handleOneByte(b10, cArr, i12);
                    i12++;
                }
                while (true) {
                    int i13 = i12;
                    while (addressOffset < j10) {
                        long j11 = addressOffset + 1;
                        byte b11 = UnsafeUtil.getByte(addressOffset);
                        if (DecodeUtil.isOneByte(b11)) {
                            int i14 = i13 + 1;
                            DecodeUtil.handleOneByte(b11, cArr, i13);
                            while (j11 < j10) {
                                byte b12 = UnsafeUtil.getByte(j11);
                                if (!DecodeUtil.isOneByte(b12)) {
                                    break;
                                }
                                j11++;
                                DecodeUtil.handleOneByte(b12, cArr, i14);
                                i14++;
                            }
                            i13 = i14;
                            addressOffset = j11;
                        } else if (DecodeUtil.isTwoBytes(b11)) {
                            if (j11 < j10) {
                                addressOffset = j11 + 1;
                                DecodeUtil.handleTwoBytes(b11, UnsafeUtil.getByte(j11), cArr, i13);
                                i13++;
                            } else {
                                throw InvalidProtocolBufferException.invalidUtf8();
                            }
                        } else if (DecodeUtil.isThreeBytes(b11)) {
                            if (j11 < j10 - 1) {
                                long j12 = j11 + 1;
                                DecodeUtil.handleThreeBytes(b11, UnsafeUtil.getByte(j11), UnsafeUtil.getByte(j12), cArr, i13);
                                i13++;
                                addressOffset = j12 + 1;
                            } else {
                                throw InvalidProtocolBufferException.invalidUtf8();
                            }
                        } else if (j11 < j10 - 2) {
                            long j13 = j11 + 1;
                            byte b13 = UnsafeUtil.getByte(j11);
                            long j14 = j13 + 1;
                            byte b14 = UnsafeUtil.getByte(j13);
                            addressOffset = j14 + 1;
                            DecodeUtil.handleFourBytes(b11, b13, b14, UnsafeUtil.getByte(j14), cArr, i13);
                            i12 = i13 + 1 + 1;
                        } else {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                    }
                    return new String(cArr, 0, i13);
                }
            }
            throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(byteBuffer.limit()), Integer.valueOf(i10), Integer.valueOf(i11)));
        }

        @Override // com.google.protobuf.Utf8.Processor
        int encodeUtf8(CharSequence charSequence, byte[] bArr, int i10, int i11) {
            char c10;
            long j10;
            long j11;
            long j12;
            char c11;
            int i12;
            char charAt;
            long j13 = i10;
            long j14 = i11 + j13;
            int length = charSequence.length();
            if (length > i11 || bArr.length - i11 < i10) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(length - 1) + " at index " + (i10 + i11));
            }
            int i13 = 0;
            while (true) {
                c10 = 128;
                j10 = 1;
                if (i13 >= length || (charAt = charSequence.charAt(i13)) >= 128) {
                    break;
                }
                UnsafeUtil.putByte(bArr, j13, (byte) charAt);
                i13++;
                j13 = 1 + j13;
            }
            if (i13 == length) {
                return (int) j13;
            }
            while (i13 < length) {
                char charAt2 = charSequence.charAt(i13);
                if (charAt2 < c10 && j13 < j14) {
                    long j15 = j13 + j10;
                    UnsafeUtil.putByte(bArr, j13, (byte) charAt2);
                    j12 = j10;
                    j11 = j15;
                    c11 = c10;
                } else if (charAt2 < 2048 && j13 <= j14 - 2) {
                    long j16 = j13 + j10;
                    UnsafeUtil.putByte(bArr, j13, (byte) ((charAt2 >>> 6) | 960));
                    long j17 = j16 + j10;
                    UnsafeUtil.putByte(bArr, j16, (byte) ((charAt2 & '?') | 128));
                    long j18 = j10;
                    c11 = 128;
                    j11 = j17;
                    j12 = j18;
                } else {
                    if ((charAt2 >= 55296 && 57343 >= charAt2) || j13 > j14 - 3) {
                        if (j13 <= j14 - 4) {
                            int i14 = i13 + 1;
                            if (i14 != length) {
                                char charAt3 = charSequence.charAt(i14);
                                if (Character.isSurrogatePair(charAt2, charAt3)) {
                                    int codePoint = Character.toCodePoint(charAt2, charAt3);
                                    long j19 = j13 + 1;
                                    UnsafeUtil.putByte(bArr, j13, (byte) ((codePoint >>> 18) | 240));
                                    long j20 = j19 + 1;
                                    c11 = 128;
                                    UnsafeUtil.putByte(bArr, j19, (byte) (((codePoint >>> 12) & 63) | 128));
                                    long j21 = j20 + 1;
                                    UnsafeUtil.putByte(bArr, j20, (byte) (((codePoint >>> 6) & 63) | 128));
                                    j12 = 1;
                                    j11 = j21 + 1;
                                    UnsafeUtil.putByte(bArr, j21, (byte) ((codePoint & 63) | 128));
                                    i13 = i14;
                                } else {
                                    i13 = i14;
                                }
                            }
                            throw new UnpairedSurrogateException(i13 - 1, length);
                        }
                        if (55296 <= charAt2 && charAt2 <= 57343 && ((i12 = i13 + 1) == length || !Character.isSurrogatePair(charAt2, charSequence.charAt(i12)))) {
                            throw new UnpairedSurrogateException(i13, length);
                        }
                        throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + j13);
                    }
                    long j22 = j13 + j10;
                    UnsafeUtil.putByte(bArr, j13, (byte) ((charAt2 >>> '\f') | 480));
                    long j23 = j22 + j10;
                    UnsafeUtil.putByte(bArr, j22, (byte) (((charAt2 >>> 6) & 63) | 128));
                    UnsafeUtil.putByte(bArr, j23, (byte) ((charAt2 & '?') | 128));
                    j11 = j23 + 1;
                    j12 = 1;
                    c11 = 128;
                }
                i13++;
                c10 = c11;
                long j24 = j12;
                j13 = j11;
                j10 = j24;
            }
            return (int) j13;
        }

        @Override // com.google.protobuf.Utf8.Processor
        void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer) {
            char c10;
            long j10;
            int i10;
            int i11;
            long j11;
            char c11;
            char charAt;
            long addressOffset = UnsafeUtil.addressOffset(byteBuffer);
            long position = byteBuffer.position() + addressOffset;
            long limit = byteBuffer.limit() + addressOffset;
            int length = charSequence.length();
            if (length > limit - position) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(length - 1) + " at index " + byteBuffer.limit());
            }
            int i12 = 0;
            while (true) {
                c10 = 128;
                if (i12 >= length || (charAt = charSequence.charAt(i12)) >= 128) {
                    break;
                }
                UnsafeUtil.putByte(position, (byte) charAt);
                i12++;
                position++;
            }
            if (i12 == length) {
                byteBuffer.position((int) (position - addressOffset));
                return;
            }
            while (i12 < length) {
                char charAt2 = charSequence.charAt(i12);
                if (charAt2 >= c10 || position >= limit) {
                    if (charAt2 >= 2048 || position > limit - 2) {
                        j10 = addressOffset;
                        if ((charAt2 >= 55296 && 57343 >= charAt2) || position > limit - 3) {
                            if (position <= limit - 4) {
                                i11 = i12 + 1;
                                if (i11 != length) {
                                    char charAt3 = charSequence.charAt(i11);
                                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                                        int codePoint = Character.toCodePoint(charAt2, charAt3);
                                        j11 = limit;
                                        long j12 = position + 1;
                                        UnsafeUtil.putByte(position, (byte) ((codePoint >>> 18) | 240));
                                        long j13 = j12 + 1;
                                        c11 = 128;
                                        UnsafeUtil.putByte(j12, (byte) (((codePoint >>> 12) & 63) | 128));
                                        long j14 = j13 + 1;
                                        UnsafeUtil.putByte(j13, (byte) (((codePoint >>> 6) & 63) | 128));
                                        UnsafeUtil.putByte(j14, (byte) ((codePoint & 63) | 128));
                                        position = j14 + 1;
                                    } else {
                                        i12 = i11;
                                    }
                                }
                                throw new UnpairedSurrogateException(i12 - 1, length);
                            }
                            if (55296 <= charAt2 && charAt2 <= 57343 && ((i10 = i12 + 1) == length || !Character.isSurrogatePair(charAt2, charSequence.charAt(i10)))) {
                                throw new UnpairedSurrogateException(i12, length);
                            }
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + position);
                        }
                        long j15 = position + 1;
                        UnsafeUtil.putByte(position, (byte) ((charAt2 >>> '\f') | 480));
                        long j16 = j15 + 1;
                        UnsafeUtil.putByte(j15, (byte) (((charAt2 >>> 6) & 63) | 128));
                        UnsafeUtil.putByte(j16, (byte) ((charAt2 & '?') | 128));
                        position = j16 + 1;
                    } else {
                        j10 = addressOffset;
                        long j17 = position + 1;
                        UnsafeUtil.putByte(position, (byte) ((charAt2 >>> 6) | 960));
                        UnsafeUtil.putByte(j17, (byte) ((charAt2 & '?') | 128));
                        position = j17 + 1;
                    }
                    j11 = limit;
                    i11 = i12;
                    c11 = 128;
                } else {
                    UnsafeUtil.putByte(position, (byte) charAt2);
                    j11 = limit;
                    i11 = i12;
                    c11 = c10;
                    position++;
                    j10 = addressOffset;
                }
                c10 = c11;
                addressOffset = j10;
                limit = j11;
                i12 = i11 + 1;
            }
            byteBuffer.position((int) (position - addressOffset));
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
        
            if (com.google.protobuf.UnsafeUtil.getByte(r12, r1) > (-65)) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x009e, code lost:
        
            if (com.google.protobuf.UnsafeUtil.getByte(r12, r1) > (-65)) goto L59;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int partialIsValidUtf8(int i10, byte[] bArr, int i11, int i12) {
            long j10;
            byte b10 = 0;
            if ((i11 | i12 | (bArr.length - i12)) >= 0) {
                long j11 = i11;
                long j12 = i12;
                if (i10 != 0) {
                    if (j11 >= j12) {
                        return i10;
                    }
                    byte b11 = (byte) i10;
                    if (b11 < -32) {
                        if (b11 >= -62) {
                            long j13 = 1 + j11;
                            if (UnsafeUtil.getByte(bArr, j11) <= -65) {
                                j11 = j13;
                            }
                        }
                        return -1;
                    }
                    if (b11 < -16) {
                        byte b12 = (byte) (~(i10 >> 8));
                        if (b12 == 0) {
                            long j14 = j11 + 1;
                            b12 = UnsafeUtil.getByte(bArr, j11);
                            if (j14 >= j12) {
                                return Utf8.incompleteStateFor(b11, b12);
                            }
                            j11 = j14;
                        }
                        if (b12 <= -65 && ((b11 != -32 || b12 >= -96) && (b11 != -19 || b12 < -96))) {
                            j10 = j11 + 1;
                        }
                        return -1;
                    }
                    byte b13 = (byte) (~(i10 >> 8));
                    if (b13 == 0) {
                        long j15 = j11 + 1;
                        b13 = UnsafeUtil.getByte(bArr, j11);
                        if (j15 >= j12) {
                            return Utf8.incompleteStateFor(b11, b13);
                        }
                        j11 = j15;
                    } else {
                        b10 = (byte) (i10 >> 16);
                    }
                    if (b10 == 0) {
                        long j16 = j11 + 1;
                        b10 = UnsafeUtil.getByte(bArr, j11);
                        if (j16 >= j12) {
                            return Utf8.incompleteStateFor(b11, b13, b10);
                        }
                        j11 = j16;
                    }
                    if (b13 <= -65 && (((b11 << 28) + (b13 + 112)) >> 30) == 0 && b10 <= -65) {
                        j10 = j11 + 1;
                    }
                    return -1;
                    j11 = j10;
                }
                return partialIsValidUtf8(bArr, j11, (int) (j12 - j11));
            }
            throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bArr.length), Integer.valueOf(i11), Integer.valueOf(i12)));
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x0063, code lost:
        
            if (com.google.protobuf.UnsafeUtil.getByte(r1) > (-65)) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x00a8, code lost:
        
            if (com.google.protobuf.UnsafeUtil.getByte(r1) > (-65)) goto L59;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int partialIsValidUtf8Direct(int i10, ByteBuffer byteBuffer, int i11, int i12) {
            long j10;
            byte b10 = 0;
            if ((i11 | i12 | (byteBuffer.limit() - i12)) >= 0) {
                long addressOffset = UnsafeUtil.addressOffset(byteBuffer) + i11;
                long j11 = (i12 - i11) + addressOffset;
                if (i10 != 0) {
                    if (addressOffset >= j11) {
                        return i10;
                    }
                    byte b11 = (byte) i10;
                    if (b11 < -32) {
                        if (b11 >= -62) {
                            long j12 = 1 + addressOffset;
                            if (UnsafeUtil.getByte(addressOffset) <= -65) {
                                addressOffset = j12;
                            }
                        }
                        return -1;
                    }
                    if (b11 < -16) {
                        byte b12 = (byte) (~(i10 >> 8));
                        if (b12 == 0) {
                            long j13 = addressOffset + 1;
                            b12 = UnsafeUtil.getByte(addressOffset);
                            if (j13 >= j11) {
                                return Utf8.incompleteStateFor(b11, b12);
                            }
                            addressOffset = j13;
                        }
                        if (b12 <= -65 && ((b11 != -32 || b12 >= -96) && (b11 != -19 || b12 < -96))) {
                            j10 = addressOffset + 1;
                        }
                        return -1;
                    }
                    byte b13 = (byte) (~(i10 >> 8));
                    if (b13 == 0) {
                        long j14 = addressOffset + 1;
                        b13 = UnsafeUtil.getByte(addressOffset);
                        if (j14 >= j11) {
                            return Utf8.incompleteStateFor(b11, b13);
                        }
                        addressOffset = j14;
                    } else {
                        b10 = (byte) (i10 >> 16);
                    }
                    if (b10 == 0) {
                        long j15 = addressOffset + 1;
                        b10 = UnsafeUtil.getByte(addressOffset);
                        if (j15 >= j11) {
                            return Utf8.incompleteStateFor(b11, b13, b10);
                        }
                        addressOffset = j15;
                    }
                    if (b13 <= -65 && (((b11 << 28) + (b13 + 112)) >> 30) == 0 && b10 <= -65) {
                        j10 = addressOffset + 1;
                    }
                    return -1;
                    addressOffset = j10;
                }
                return partialIsValidUtf8(addressOffset, (int) (j11 - addressOffset));
            }
            throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(byteBuffer.limit()), Integer.valueOf(i11), Integer.valueOf(i12)));
        }

        private static int unsafeEstimateConsecutiveAscii(long j10, int i10) {
            if (i10 < 16) {
                return 0;
            }
            int i11 = (int) ((-j10) & 7);
            int i12 = i11;
            while (i12 > 0) {
                long j11 = 1 + j10;
                if (UnsafeUtil.getByte(j10) < 0) {
                    return i11 - i12;
                }
                i12--;
                j10 = j11;
            }
            int i13 = i10 - i11;
            while (i13 >= 8 && (UnsafeUtil.getLong(j10) & Utf8.ASCII_MASK_LONG) == 0) {
                j10 += 8;
                i13 -= 8;
            }
            return i10 - i13;
        }

        private static int unsafeIncompleteStateFor(long j10, int i10, int i11) {
            if (i11 == 0) {
                return Utf8.incompleteStateFor(i10);
            }
            if (i11 == 1) {
                return Utf8.incompleteStateFor(i10, UnsafeUtil.getByte(j10));
            }
            if (i11 == 2) {
                return Utf8.incompleteStateFor(i10, UnsafeUtil.getByte(j10), UnsafeUtil.getByte(j10 + 1));
            }
            throw new AssertionError();
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x008e, code lost:
        
            return -1;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private static int partialIsValidUtf8(byte[] bArr, long j10, int i10) {
            long j11;
            int unsafeEstimateConsecutiveAscii = unsafeEstimateConsecutiveAscii(bArr, j10, i10);
            int i11 = i10 - unsafeEstimateConsecutiveAscii;
            long j12 = j10 + unsafeEstimateConsecutiveAscii;
            while (true) {
                byte b10 = 0;
                while (true) {
                    if (i11 <= 0) {
                        break;
                    }
                    long j13 = j12 + 1;
                    b10 = UnsafeUtil.getByte(bArr, j12);
                    if (b10 < 0) {
                        j12 = j13;
                        break;
                    }
                    i11--;
                    j12 = j13;
                }
                if (i11 == 0) {
                    return 0;
                }
                int i12 = i11 - 1;
                if (b10 >= -32) {
                    if (b10 >= -16) {
                        if (i12 < 3) {
                            return unsafeIncompleteStateFor(bArr, b10, j12, i12);
                        }
                        i11 = i12 - 3;
                        long j14 = j12 + 1;
                        byte b11 = UnsafeUtil.getByte(bArr, j12);
                        if (b11 > -65 || (((b10 << 28) + (b11 + 112)) >> 30) != 0) {
                            break;
                        }
                        long j15 = j14 + 1;
                        if (UnsafeUtil.getByte(bArr, j14) > -65) {
                            break;
                        }
                        j11 = 1 + j15;
                        if (UnsafeUtil.getByte(bArr, j15) > -65) {
                            break;
                        }
                    } else {
                        if (i12 < 2) {
                            return unsafeIncompleteStateFor(bArr, b10, j12, i12);
                        }
                        i11 = i12 - 2;
                        long j16 = j12 + 1;
                        byte b12 = UnsafeUtil.getByte(bArr, j12);
                        if (b12 > -65 || ((b10 == -32 && b12 < -96) || (b10 == -19 && b12 >= -96))) {
                            break;
                        }
                        j11 = 1 + j16;
                        if (UnsafeUtil.getByte(bArr, j16) > -65) {
                            break;
                        }
                    }
                } else if (i12 != 0) {
                    i11 = i12 - 1;
                    if (b10 < -62) {
                        break;
                    }
                    j11 = 1 + j12;
                    if (UnsafeUtil.getByte(bArr, j12) > -65) {
                        break;
                    }
                } else {
                    return b10;
                }
                j12 = j11;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x008e, code lost:
        
            return -1;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private static int partialIsValidUtf8(long j10, int i10) {
            long j11;
            int unsafeEstimateConsecutiveAscii = unsafeEstimateConsecutiveAscii(j10, i10);
            long j12 = j10 + unsafeEstimateConsecutiveAscii;
            int i11 = i10 - unsafeEstimateConsecutiveAscii;
            while (true) {
                byte b10 = 0;
                while (true) {
                    if (i11 <= 0) {
                        break;
                    }
                    long j13 = j12 + 1;
                    b10 = UnsafeUtil.getByte(j12);
                    if (b10 < 0) {
                        j12 = j13;
                        break;
                    }
                    i11--;
                    j12 = j13;
                }
                if (i11 == 0) {
                    return 0;
                }
                int i12 = i11 - 1;
                if (b10 >= -32) {
                    if (b10 >= -16) {
                        if (i12 < 3) {
                            return unsafeIncompleteStateFor(j12, b10, i12);
                        }
                        i11 = i12 - 3;
                        long j14 = j12 + 1;
                        byte b11 = UnsafeUtil.getByte(j12);
                        if (b11 > -65 || (((b10 << 28) + (b11 + 112)) >> 30) != 0) {
                            break;
                        }
                        long j15 = j14 + 1;
                        if (UnsafeUtil.getByte(j14) > -65) {
                            break;
                        }
                        j11 = 1 + j15;
                        if (UnsafeUtil.getByte(j15) > -65) {
                            break;
                        }
                    } else {
                        if (i12 < 2) {
                            return unsafeIncompleteStateFor(j12, b10, i12);
                        }
                        i11 = i12 - 2;
                        long j16 = j12 + 1;
                        byte b12 = UnsafeUtil.getByte(j12);
                        if (b12 > -65 || ((b10 == -32 && b12 < -96) || (b10 == -19 && b12 >= -96))) {
                            break;
                        }
                        j11 = 1 + j16;
                        if (UnsafeUtil.getByte(j16) > -65) {
                            break;
                        }
                    }
                } else if (i12 != 0) {
                    i11 = i12 - 1;
                    if (b10 < -62) {
                        break;
                    }
                    j11 = 1 + j12;
                    if (UnsafeUtil.getByte(j12) > -65) {
                        break;
                    }
                } else {
                    return b10;
                }
                j12 = j11;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isValidUtf8(ByteBuffer byteBuffer) {
        return processor.isValidUtf8(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(ByteBuffer byteBuffer, int i10, int i11, int i12) {
        if (i12 == 0) {
            return incompleteStateFor(i10);
        }
        if (i12 == 1) {
            return incompleteStateFor(i10, byteBuffer.get(i11));
        }
        if (i12 == 2) {
            return incompleteStateFor(i10, byteBuffer.get(i11), byteBuffer.get(i11 + 1));
        }
        throw new AssertionError();
    }

    /* loaded from: classes.dex */
    static final class SafeProcessor extends Processor {
        SafeProcessor() {
        }

        private static int partialIsValidUtf8NonAscii(byte[] bArr, int i10, int i11) {
            while (i10 < i11) {
                int i12 = i10 + 1;
                byte b10 = bArr[i10];
                if (b10 < 0) {
                    if (b10 < -32) {
                        if (i12 >= i11) {
                            return b10;
                        }
                        if (b10 >= -62) {
                            i10 = i12 + 1;
                            if (bArr[i12] > -65) {
                            }
                        }
                        return -1;
                    }
                    if (b10 >= -16) {
                        if (i12 >= i11 - 2) {
                            return Utf8.incompleteStateFor(bArr, i12, i11);
                        }
                        int i13 = i12 + 1;
                        byte b11 = bArr[i12];
                        if (b11 <= -65 && (((b10 << 28) + (b11 + 112)) >> 30) == 0) {
                            int i14 = i13 + 1;
                            if (bArr[i13] <= -65) {
                                i12 = i14 + 1;
                                if (bArr[i14] > -65) {
                                }
                            }
                        }
                        return -1;
                    }
                    if (i12 >= i11 - 1) {
                        return Utf8.incompleteStateFor(bArr, i12, i11);
                    }
                    int i15 = i12 + 1;
                    byte b12 = bArr[i12];
                    if (b12 <= -65 && ((b10 != -32 || b12 >= -96) && (b10 != -19 || b12 < -96))) {
                        i10 = i15 + 1;
                        if (bArr[i15] > -65) {
                        }
                    }
                    return -1;
                }
                i10 = i12;
            }
            return 0;
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8(byte[] bArr, int i10, int i11) {
            if ((i10 | i11 | ((bArr.length - i10) - i11)) >= 0) {
                int i12 = i10 + i11;
                char[] cArr = new char[i11];
                int i13 = 0;
                while (i10 < i12) {
                    byte b10 = bArr[i10];
                    if (!DecodeUtil.isOneByte(b10)) {
                        break;
                    }
                    i10++;
                    DecodeUtil.handleOneByte(b10, cArr, i13);
                    i13++;
                }
                int i14 = i13;
                while (i10 < i12) {
                    int i15 = i10 + 1;
                    byte b11 = bArr[i10];
                    if (DecodeUtil.isOneByte(b11)) {
                        int i16 = i14 + 1;
                        DecodeUtil.handleOneByte(b11, cArr, i14);
                        while (i15 < i12) {
                            byte b12 = bArr[i15];
                            if (!DecodeUtil.isOneByte(b12)) {
                                break;
                            }
                            i15++;
                            DecodeUtil.handleOneByte(b12, cArr, i16);
                            i16++;
                        }
                        i10 = i15;
                        i14 = i16;
                    } else if (DecodeUtil.isTwoBytes(b11)) {
                        if (i15 < i12) {
                            DecodeUtil.handleTwoBytes(b11, bArr[i15], cArr, i14);
                            i10 = i15 + 1;
                            i14++;
                        } else {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                    } else if (DecodeUtil.isThreeBytes(b11)) {
                        if (i15 < i12 - 1) {
                            int i17 = i15 + 1;
                            DecodeUtil.handleThreeBytes(b11, bArr[i15], bArr[i17], cArr, i14);
                            i10 = i17 + 1;
                            i14++;
                        } else {
                            throw InvalidProtocolBufferException.invalidUtf8();
                        }
                    } else if (i15 < i12 - 2) {
                        int i18 = i15 + 1;
                        byte b13 = bArr[i15];
                        int i19 = i18 + 1;
                        DecodeUtil.handleFourBytes(b11, b13, bArr[i18], bArr[i19], cArr, i14);
                        i10 = i19 + 1;
                        i14 = i14 + 1 + 1;
                    } else {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                }
                return new String(cArr, 0, i14);
            }
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i10), Integer.valueOf(i11)));
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8Direct(ByteBuffer byteBuffer, int i10, int i11) {
            return decodeUtf8Default(byteBuffer, i10, i11);
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x001d, code lost:
        
            return r9 + r6;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int encodeUtf8(CharSequence charSequence, byte[] bArr, int i10, int i11) {
            int i12;
            int i13;
            int i14;
            char charAt;
            int length = charSequence.length();
            int i15 = i11 + i10;
            int i16 = 0;
            while (i16 < length && (i14 = i16 + i10) < i15 && (charAt = charSequence.charAt(i16)) < 128) {
                bArr[i14] = (byte) charAt;
                i16++;
            }
            int i17 = i10 + i16;
            while (i16 < length) {
                char charAt2 = charSequence.charAt(i16);
                if (charAt2 >= 128 || i17 >= i15) {
                    if (charAt2 < 2048 && i17 <= i15 - 2) {
                        int i18 = i17 + 1;
                        bArr[i17] = (byte) ((charAt2 >>> 6) | 960);
                        i17 = i18 + 1;
                        bArr[i18] = (byte) ((charAt2 & '?') | 128);
                    } else {
                        if ((charAt2 >= 55296 && 57343 >= charAt2) || i17 > i15 - 3) {
                            if (i17 <= i15 - 4) {
                                int i19 = i16 + 1;
                                if (i19 != charSequence.length()) {
                                    char charAt3 = charSequence.charAt(i19);
                                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                                        int codePoint = Character.toCodePoint(charAt2, charAt3);
                                        int i20 = i17 + 1;
                                        bArr[i17] = (byte) ((codePoint >>> 18) | 240);
                                        int i21 = i20 + 1;
                                        bArr[i20] = (byte) (((codePoint >>> 12) & 63) | 128);
                                        int i22 = i21 + 1;
                                        bArr[i21] = (byte) (((codePoint >>> 6) & 63) | 128);
                                        i17 = i22 + 1;
                                        bArr[i22] = (byte) ((codePoint & 63) | 128);
                                        i16 = i19;
                                    } else {
                                        i16 = i19;
                                    }
                                }
                                throw new UnpairedSurrogateException(i16 - 1, length);
                            }
                            if (55296 <= charAt2 && charAt2 <= 57343 && ((i13 = i16 + 1) == charSequence.length() || !Character.isSurrogatePair(charAt2, charSequence.charAt(i13)))) {
                                throw new UnpairedSurrogateException(i16, length);
                            }
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + i17);
                        }
                        int i23 = i17 + 1;
                        bArr[i17] = (byte) ((charAt2 >>> '\f') | 480);
                        int i24 = i23 + 1;
                        bArr[i23] = (byte) (((charAt2 >>> 6) & 63) | 128);
                        i12 = i24 + 1;
                        bArr[i24] = (byte) ((charAt2 & '?') | 128);
                    }
                    i16++;
                } else {
                    i12 = i17 + 1;
                    bArr[i17] = (byte) charAt2;
                }
                i17 = i12;
                i16++;
            }
            return i17;
        }

        @Override // com.google.protobuf.Utf8.Processor
        void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer) {
            encodeUtf8Default(charSequence, byteBuffer);
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0015, code lost:
        
            if (r7[r8] > (-65)) goto L13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x0046, code lost:
        
            if (r7[r8] > (-65)) goto L32;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x007f, code lost:
        
            if (r7[r8] > (-65)) goto L53;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int partialIsValidUtf8(int i10, byte[] bArr, int i11, int i12) {
            int i13;
            if (i10 != 0) {
                if (i11 >= i12) {
                    return i10;
                }
                byte b10 = (byte) i10;
                if (b10 < -32) {
                    if (b10 >= -62) {
                        i13 = i11 + 1;
                    }
                    return -1;
                }
                if (b10 < -16) {
                    byte b11 = (byte) (~(i10 >> 8));
                    if (b11 == 0) {
                        int i14 = i11 + 1;
                        byte b12 = bArr[i11];
                        if (i14 >= i12) {
                            return Utf8.incompleteStateFor(b10, b12);
                        }
                        i11 = i14;
                        b11 = b12;
                    }
                    if (b11 <= -65 && ((b10 != -32 || b11 >= -96) && (b10 != -19 || b11 < -96))) {
                        i13 = i11 + 1;
                    }
                    return -1;
                }
                byte b13 = (byte) (~(i10 >> 8));
                byte b14 = 0;
                if (b13 == 0) {
                    int i15 = i11 + 1;
                    b13 = bArr[i11];
                    if (i15 >= i12) {
                        return Utf8.incompleteStateFor(b10, b13);
                    }
                    i11 = i15;
                } else {
                    b14 = (byte) (i10 >> 16);
                }
                if (b14 == 0) {
                    int i16 = i11 + 1;
                    b14 = bArr[i11];
                    if (i16 >= i12) {
                        return Utf8.incompleteStateFor(b10, b13, b14);
                    }
                    i11 = i16;
                }
                if (b13 <= -65 && (((b10 << 28) + (b13 + 112)) >> 30) == 0 && b14 <= -65) {
                    i13 = i11 + 1;
                }
                return -1;
                i11 = i13;
            }
            return partialIsValidUtf8(bArr, i11, i12);
        }

        @Override // com.google.protobuf.Utf8.Processor
        int partialIsValidUtf8Direct(int i10, ByteBuffer byteBuffer, int i11, int i12) {
            return partialIsValidUtf8Default(i10, byteBuffer, i11, i12);
        }

        private static int partialIsValidUtf8(byte[] bArr, int i10, int i11) {
            while (i10 < i11 && bArr[i10] >= 0) {
                i10++;
            }
            if (i10 >= i11) {
                return 0;
            }
            return partialIsValidUtf8NonAscii(bArr, i10, i11);
        }
    }
}
