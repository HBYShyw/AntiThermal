package com.squareup.moshi;

import com.squareup.moshi.JsonReader;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.Nullable;
import me.BufferedSource;
import me.d;
import me.g;
import me.n;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class JsonUtf8Reader extends JsonReader {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_BUFFERED_NAME = 15;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 18;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 16;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 17;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final d buffer;
    private int peeked;
    private long peekedLong;
    private int peekedNumberLength;

    @Nullable
    private String peekedString;
    private final BufferedSource source;

    @Nullable
    private JsonValueSource valueSource;
    private static final g SINGLE_QUOTE_OR_SLASH = g.d("'\\");
    private static final g DOUBLE_QUOTE_OR_SLASH = g.d("\"\\");
    private static final g UNQUOTED_STRING_TERMINALS = g.d("{}[]:, \n\t\r\f/\\;#=");
    private static final g LINEFEED_OR_CARRIAGE_RETURN = g.d("\n\r");
    private static final g CLOSING_BLOCK_COMMENT = g.d("*/");

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonUtf8Reader(BufferedSource bufferedSource) {
        this.peeked = 0;
        Objects.requireNonNull(bufferedSource, "source == null");
        this.source = bufferedSource;
        this.buffer = bufferedSource.a();
        pushScope(6);
    }

    private void checkLenient() {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private int doPeek() {
        int[] iArr = this.scopes;
        int i10 = this.stackSize;
        int i11 = iArr[i10 - 1];
        if (i11 == 1) {
            iArr[i10 - 1] = 2;
        } else if (i11 == 2) {
            int nextNonWhitespace = nextNonWhitespace(true);
            this.buffer.M();
            if (nextNonWhitespace != 44) {
                if (nextNonWhitespace != 59) {
                    if (nextNonWhitespace == 93) {
                        this.peeked = 4;
                        return 4;
                    }
                    throw syntaxError("Unterminated array");
                }
                checkLenient();
            }
        } else {
            if (i11 == 3 || i11 == 5) {
                iArr[i10 - 1] = 4;
                if (i11 == 5) {
                    int nextNonWhitespace2 = nextNonWhitespace(true);
                    this.buffer.M();
                    if (nextNonWhitespace2 != 44) {
                        if (nextNonWhitespace2 != 59) {
                            if (nextNonWhitespace2 == 125) {
                                this.peeked = 2;
                                return 2;
                            }
                            throw syntaxError("Unterminated object");
                        }
                        checkLenient();
                    }
                }
                int nextNonWhitespace3 = nextNonWhitespace(true);
                if (nextNonWhitespace3 == 34) {
                    this.buffer.M();
                    this.peeked = 13;
                    return 13;
                }
                if (nextNonWhitespace3 == 39) {
                    this.buffer.M();
                    checkLenient();
                    this.peeked = 12;
                    return 12;
                }
                if (nextNonWhitespace3 != 125) {
                    checkLenient();
                    if (isLiteral((char) nextNonWhitespace3)) {
                        this.peeked = 14;
                        return 14;
                    }
                    throw syntaxError("Expected name");
                }
                if (i11 != 5) {
                    this.buffer.M();
                    this.peeked = 2;
                    return 2;
                }
                throw syntaxError("Expected name");
            }
            if (i11 == 4) {
                iArr[i10 - 1] = 5;
                int nextNonWhitespace4 = nextNonWhitespace(true);
                this.buffer.M();
                if (nextNonWhitespace4 != 58) {
                    if (nextNonWhitespace4 == 61) {
                        checkLenient();
                        if (this.source.W(1L) && this.buffer.L(0L) == 62) {
                            this.buffer.M();
                        }
                    } else {
                        throw syntaxError("Expected ':'");
                    }
                }
            } else if (i11 == 6) {
                iArr[i10 - 1] = 7;
            } else if (i11 == 7) {
                if (nextNonWhitespace(false) == -1) {
                    this.peeked = 18;
                    return 18;
                }
                checkLenient();
            } else {
                if (i11 == 9) {
                    this.valueSource.discard();
                    this.valueSource = null;
                    this.stackSize--;
                    return doPeek();
                }
                if (i11 == 8) {
                    throw new IllegalStateException("JsonReader is closed");
                }
            }
        }
        int nextNonWhitespace5 = nextNonWhitespace(true);
        if (nextNonWhitespace5 == 34) {
            this.buffer.M();
            this.peeked = 9;
            return 9;
        }
        if (nextNonWhitespace5 == 39) {
            checkLenient();
            this.buffer.M();
            this.peeked = 8;
            return 8;
        }
        if (nextNonWhitespace5 != 44 && nextNonWhitespace5 != 59) {
            if (nextNonWhitespace5 == 91) {
                this.buffer.M();
                this.peeked = 3;
                return 3;
            }
            if (nextNonWhitespace5 != 93) {
                if (nextNonWhitespace5 != 123) {
                    int peekKeyword = peekKeyword();
                    if (peekKeyword != 0) {
                        return peekKeyword;
                    }
                    int peekNumber = peekNumber();
                    if (peekNumber != 0) {
                        return peekNumber;
                    }
                    if (isLiteral(this.buffer.L(0L))) {
                        checkLenient();
                        this.peeked = 10;
                        return 10;
                    }
                    throw syntaxError("Expected value");
                }
                this.buffer.M();
                this.peeked = 1;
                return 1;
            }
            if (i11 == 1) {
                this.buffer.M();
                this.peeked = 4;
                return 4;
            }
        }
        if (i11 != 1 && i11 != 2) {
            throw syntaxError("Unexpected value");
        }
        checkLenient();
        this.peeked = 7;
        return 7;
    }

    private int findName(String str, JsonReader.Options options) {
        int length = options.strings.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (str.equals(options.strings[i10])) {
                this.peeked = 0;
                this.pathNames[this.stackSize - 1] = str;
                return i10;
            }
        }
        return -1;
    }

    private int findString(String str, JsonReader.Options options) {
        int length = options.strings.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (str.equals(options.strings[i10])) {
                this.peeked = 0;
                int[] iArr = this.pathIndices;
                int i11 = this.stackSize - 1;
                iArr[i11] = iArr[i11] + 1;
                return i10;
            }
        }
        return -1;
    }

    private boolean isLiteral(int i10) {
        if (i10 == 9 || i10 == 10 || i10 == 12 || i10 == 13 || i10 == 32) {
            return false;
        }
        if (i10 != 35) {
            if (i10 == 44) {
                return false;
            }
            if (i10 != 47 && i10 != 61) {
                if (i10 == 123 || i10 == 125 || i10 == 58) {
                    return false;
                }
                if (i10 != 59) {
                    switch (i10) {
                        case 91:
                        case 93:
                            return false;
                        case 92:
                            break;
                        default:
                            return true;
                    }
                }
            }
        }
        checkLenient();
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0025, code lost:
    
        r6.buffer.V(r3 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
    
        if (r1 != 47) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0076, code lost:
    
        if (r1 != 35) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0078, code lost:
    
        checkLenient();
        skipToEndOfLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007f, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0039, code lost:
    
        if (r6.source.W(2) != false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x003c, code lost:
    
        checkLenient();
        r3 = r6.buffer.L(1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0049, code lost:
    
        if (r3 == 42) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005c, code lost:
    
        r6.buffer.M();
        r6.buffer.M();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006a, code lost:
    
        if (skipToEndOfBlockComment() == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0073, code lost:
    
        throw syntaxError("Unterminated comment");
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x004b, code lost:
    
        if (r3 == 47) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x004e, code lost:
    
        r6.buffer.M();
        r6.buffer.M();
        skipToEndOfLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x004d, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x003b, code lost:
    
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int nextNonWhitespace(boolean z10) {
        while (true) {
            int i10 = 0;
            while (true) {
                int i11 = i10 + 1;
                if (!this.source.W(i11)) {
                    if (z10) {
                        throw new EOFException("End of input");
                    }
                    return -1;
                }
                byte L = this.buffer.L(i10);
                if (L != 10 && L != 32 && L != 13 && L != 9) {
                    break;
                }
                i10 = i11;
            }
        }
    }

    private String nextQuotedValue(g gVar) {
        StringBuilder sb2 = null;
        while (true) {
            long g02 = this.source.g0(gVar);
            if (g02 != -1) {
                if (this.buffer.L(g02) != 92) {
                    if (sb2 == null) {
                        String t02 = this.buffer.t0(g02);
                        this.buffer.M();
                        return t02;
                    }
                    sb2.append(this.buffer.t0(g02));
                    this.buffer.M();
                    return sb2.toString();
                }
                if (sb2 == null) {
                    sb2 = new StringBuilder();
                }
                sb2.append(this.buffer.t0(g02));
                this.buffer.M();
                sb2.append(readEscapeCharacter());
            } else {
                throw syntaxError("Unterminated string");
            }
        }
    }

    private String nextUnquotedValue() {
        long g02 = this.source.g0(UNQUOTED_STRING_TERMINALS);
        d dVar = this.buffer;
        return g02 != -1 ? dVar.t0(g02) : dVar.o0();
    }

    private int peekKeyword() {
        int i10;
        String str;
        String str2;
        byte L = this.buffer.L(0L);
        if (L == 116 || L == 84) {
            i10 = 5;
            str = "true";
            str2 = "TRUE";
        } else if (L == 102 || L == 70) {
            i10 = 6;
            str = "false";
            str2 = "FALSE";
        } else {
            if (L != 110 && L != 78) {
                return 0;
            }
            i10 = 7;
            str = "null";
            str2 = "NULL";
        }
        int length = str.length();
        int i11 = 1;
        while (i11 < length) {
            int i12 = i11 + 1;
            if (!this.source.W(i12)) {
                return 0;
            }
            byte L2 = this.buffer.L(i11);
            if (L2 != str.charAt(i11) && L2 != str2.charAt(i11)) {
                return 0;
            }
            i11 = i12;
        }
        if (this.source.W(length + 1) && isLiteral(this.buffer.L(length))) {
            return 0;
        }
        this.buffer.V(length);
        this.peeked = i10;
        return i10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0082, code lost:
    
        if (isLiteral(r11) != false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0084, code lost:
    
        if (r6 != 2) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0086, code lost:
    
        if (r7 == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x008c, code lost:
    
        if (r8 != Long.MIN_VALUE) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x008e, code lost:
    
        if (r10 == false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0092, code lost:
    
        if (r8 != 0) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0094, code lost:
    
        if (r10 != false) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0096, code lost:
    
        if (r10 == false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0099, code lost:
    
        r8 = -r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x009a, code lost:
    
        r16.peekedLong = r8;
        r16.buffer.V(r5);
        r16.peeked = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00a6, code lost:
    
        return 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a7, code lost:
    
        if (r6 == 2) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00aa, code lost:
    
        if (r6 == 4) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00ad, code lost:
    
        if (r6 != 7) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00b0, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00b2, code lost:
    
        r16.peekedNumberLength = r5;
        r16.peeked = 17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00b8, code lost:
    
        return 17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00b9, code lost:
    
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int peekNumber() {
        char c10;
        boolean z10 = true;
        int i10 = 0;
        long j10 = 0;
        boolean z11 = true;
        int i11 = 0;
        char c11 = 0;
        boolean z12 = false;
        while (true) {
            int i12 = i11 + 1;
            if (!this.source.W(i12)) {
                break;
            }
            byte L = this.buffer.L(i11);
            if (L != 43) {
                if (L == 69 || L == 101) {
                    if (c11 != 2 && c11 != 4) {
                        return i10;
                    }
                    c11 = 5;
                } else if (L == 45) {
                    c10 = 6;
                    if (c11 == 0) {
                        c11 = 1;
                        z12 = true;
                    } else if (c11 != 5) {
                        return i10;
                    }
                } else if (L == 46) {
                    c10 = 3;
                    if (c11 != 2) {
                        return i10;
                    }
                } else {
                    if (L < 48 || L > 57) {
                        break;
                    }
                    if (c11 == z10 || c11 == 0) {
                        j10 = -(L - 48);
                        c11 = 2;
                    } else if (c11 == 2) {
                        if (j10 == 0) {
                            return i10;
                        }
                        long j11 = (10 * j10) - (L - 48);
                        z11 &= j10 > MIN_INCOMPLETE_INTEGER || (j10 == MIN_INCOMPLETE_INTEGER && j11 < j10);
                        j10 = j11;
                    } else if (c11 == 3) {
                        i10 = 0;
                        c11 = 4;
                    } else if (c11 == 5 || c11 == 6) {
                        i10 = 0;
                        c11 = 7;
                    }
                    i10 = 0;
                }
                i11 = i12;
                z10 = true;
            } else {
                c10 = 6;
                if (c11 != 5) {
                    return i10;
                }
            }
            c11 = c10;
            i11 = i12;
            z10 = true;
        }
    }

    private char readEscapeCharacter() {
        int i10;
        int i11;
        if (this.source.W(1L)) {
            byte M = this.buffer.M();
            if (M == 10 || M == 34 || M == 39 || M == 47 || M == 92) {
                return (char) M;
            }
            if (M == 98) {
                return '\b';
            }
            if (M == 102) {
                return '\f';
            }
            if (M == 110) {
                return '\n';
            }
            if (M == 114) {
                return '\r';
            }
            if (M == 116) {
                return '\t';
            }
            if (M != 117) {
                if (this.lenient) {
                    return (char) M;
                }
                throw syntaxError("Invalid escape sequence: \\" + ((char) M));
            }
            if (this.source.W(4L)) {
                char c10 = 0;
                for (int i12 = 0; i12 < 4; i12++) {
                    byte L = this.buffer.L(i12);
                    char c11 = (char) (c10 << 4);
                    if (L < 48 || L > 57) {
                        if (L >= 97 && L <= 102) {
                            i10 = L - 97;
                        } else {
                            if (L < 65 || L > 70) {
                                throw syntaxError("\\u" + this.buffer.t0(4L));
                            }
                            i10 = L - 65;
                        }
                        i11 = i10 + 10;
                    } else {
                        i11 = L - 48;
                    }
                    c10 = (char) (c11 + i11);
                }
                this.buffer.V(4L);
                return c10;
            }
            throw new EOFException("Unterminated escape sequence at path " + getPath());
        }
        throw syntaxError("Unterminated escape sequence");
    }

    private void skipQuotedValue(g gVar) {
        while (true) {
            long g02 = this.source.g0(gVar);
            if (g02 != -1) {
                if (this.buffer.L(g02) == 92) {
                    this.buffer.V(g02 + 1);
                    readEscapeCharacter();
                } else {
                    this.buffer.V(g02 + 1);
                    return;
                }
            } else {
                throw syntaxError("Unterminated string");
            }
        }
    }

    private boolean skipToEndOfBlockComment() {
        long I = this.source.I(CLOSING_BLOCK_COMMENT);
        boolean z10 = I != -1;
        d dVar = this.buffer;
        dVar.V(z10 ? I + r1.t() : dVar.v0());
        return z10;
    }

    private void skipToEndOfLine() {
        long g02 = this.source.g0(LINEFEED_OR_CARRIAGE_RETURN);
        d dVar = this.buffer;
        dVar.V(g02 != -1 ? g02 + 1 : dVar.v0());
    }

    private void skipUnquotedValue() {
        long g02 = this.source.g0(UNQUOTED_STRING_TERMINALS);
        d dVar = this.buffer;
        if (g02 == -1) {
            g02 = dVar.v0();
        }
        dVar.V(g02);
    }

    @Override // com.squareup.moshi.JsonReader
    public void beginArray() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 3) {
            pushScope(1);
            this.pathIndices[this.stackSize - 1] = 0;
            this.peeked = 0;
        } else {
            throw new JsonDataException("Expected BEGIN_ARRAY but was " + peek() + " at path " + getPath());
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public void beginObject() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 1) {
            pushScope(3);
            this.peeked = 0;
            return;
        }
        throw new JsonDataException("Expected BEGIN_OBJECT but was " + peek() + " at path " + getPath());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.peeked = 0;
        this.scopes[0] = 8;
        this.stackSize = 1;
        this.buffer.b();
        this.source.close();
    }

    @Override // com.squareup.moshi.JsonReader
    public void endArray() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 4) {
            int i11 = this.stackSize - 1;
            this.stackSize = i11;
            int[] iArr = this.pathIndices;
            int i12 = i11 - 1;
            iArr[i12] = iArr[i12] + 1;
            this.peeked = 0;
            return;
        }
        throw new JsonDataException("Expected END_ARRAY but was " + peek() + " at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonReader
    public void endObject() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 2) {
            int i11 = this.stackSize - 1;
            this.stackSize = i11;
            this.pathNames[i11] = null;
            int[] iArr = this.pathIndices;
            int i12 = i11 - 1;
            iArr[i12] = iArr[i12] + 1;
            this.peeked = 0;
            return;
        }
        throw new JsonDataException("Expected END_OBJECT but was " + peek() + " at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonReader
    public boolean hasNext() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        return (i10 == 2 || i10 == 4 || i10 == 18) ? false : true;
    }

    @Override // com.squareup.moshi.JsonReader
    public boolean nextBoolean() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 5) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i11 = this.stackSize - 1;
            iArr[i11] = iArr[i11] + 1;
            return true;
        }
        if (i10 == 6) {
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i12 = this.stackSize - 1;
            iArr2[i12] = iArr2[i12] + 1;
            return false;
        }
        throw new JsonDataException("Expected a boolean but was " + peek() + " at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonReader
    public double nextDouble() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 16) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i11 = this.stackSize - 1;
            iArr[i11] = iArr[i11] + 1;
            return this.peekedLong;
        }
        if (i10 == 17) {
            this.peekedString = this.buffer.t0(this.peekedNumberLength);
        } else if (i10 == 9) {
            this.peekedString = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i10 == 8) {
            this.peekedString = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i10 == 10) {
            this.peekedString = nextUnquotedValue();
        } else if (i10 != 11) {
            throw new JsonDataException("Expected a double but was " + peek() + " at path " + getPath());
        }
        this.peeked = 11;
        try {
            double parseDouble = Double.parseDouble(this.peekedString);
            if (!this.lenient && (Double.isNaN(parseDouble) || Double.isInfinite(parseDouble))) {
                throw new JsonEncodingException("JSON forbids NaN and infinities: " + parseDouble + " at path " + getPath());
            }
            this.peekedString = null;
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i12 = this.stackSize - 1;
            iArr2[i12] = iArr2[i12] + 1;
            return parseDouble;
        } catch (NumberFormatException unused) {
            throw new JsonDataException("Expected a double but was " + this.peekedString + " at path " + getPath());
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public int nextInt() {
        String nextQuotedValue;
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 16) {
            long j10 = this.peekedLong;
            int i11 = (int) j10;
            if (j10 == i11) {
                this.peeked = 0;
                int[] iArr = this.pathIndices;
                int i12 = this.stackSize - 1;
                iArr[i12] = iArr[i12] + 1;
                return i11;
            }
            throw new JsonDataException("Expected an int but was " + this.peekedLong + " at path " + getPath());
        }
        if (i10 == 17) {
            this.peekedString = this.buffer.t0(this.peekedNumberLength);
        } else if (i10 == 9 || i10 == 8) {
            if (i10 == 9) {
                nextQuotedValue = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
            } else {
                nextQuotedValue = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
            }
            this.peekedString = nextQuotedValue;
            try {
                int parseInt = Integer.parseInt(nextQuotedValue);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i13 = this.stackSize - 1;
                iArr2[i13] = iArr2[i13] + 1;
                return parseInt;
            } catch (NumberFormatException unused) {
            }
        } else if (i10 != 11) {
            throw new JsonDataException("Expected an int but was " + peek() + " at path " + getPath());
        }
        this.peeked = 11;
        try {
            double parseDouble = Double.parseDouble(this.peekedString);
            int i14 = (int) parseDouble;
            if (i14 == parseDouble) {
                this.peekedString = null;
                this.peeked = 0;
                int[] iArr3 = this.pathIndices;
                int i15 = this.stackSize - 1;
                iArr3[i15] = iArr3[i15] + 1;
                return i14;
            }
            throw new JsonDataException("Expected an int but was " + this.peekedString + " at path " + getPath());
        } catch (NumberFormatException unused2) {
            throw new JsonDataException("Expected an int but was " + this.peekedString + " at path " + getPath());
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public long nextLong() {
        String nextQuotedValue;
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 16) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i11 = this.stackSize - 1;
            iArr[i11] = iArr[i11] + 1;
            return this.peekedLong;
        }
        if (i10 == 17) {
            this.peekedString = this.buffer.t0(this.peekedNumberLength);
        } else if (i10 == 9 || i10 == 8) {
            if (i10 == 9) {
                nextQuotedValue = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
            } else {
                nextQuotedValue = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
            }
            this.peekedString = nextQuotedValue;
            try {
                long parseLong = Long.parseLong(nextQuotedValue);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i12 = this.stackSize - 1;
                iArr2[i12] = iArr2[i12] + 1;
                return parseLong;
            } catch (NumberFormatException unused) {
            }
        } else if (i10 != 11) {
            throw new JsonDataException("Expected a long but was " + peek() + " at path " + getPath());
        }
        this.peeked = 11;
        try {
            long longValueExact = new BigDecimal(this.peekedString).longValueExact();
            this.peekedString = null;
            this.peeked = 0;
            int[] iArr3 = this.pathIndices;
            int i13 = this.stackSize - 1;
            iArr3[i13] = iArr3[i13] + 1;
            return longValueExact;
        } catch (ArithmeticException | NumberFormatException unused2) {
            throw new JsonDataException("Expected a long but was " + this.peekedString + " at path " + getPath());
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public String nextName() {
        String str;
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 14) {
            str = nextUnquotedValue();
        } else if (i10 == 13) {
            str = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i10 == 12) {
            str = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i10 == 15) {
            str = this.peekedString;
            this.peekedString = null;
        } else {
            throw new JsonDataException("Expected a name but was " + peek() + " at path " + getPath());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = str;
        return str;
    }

    @Override // com.squareup.moshi.JsonReader
    @Nullable
    public <T> T nextNull() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 7) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i11 = this.stackSize - 1;
            iArr[i11] = iArr[i11] + 1;
            return null;
        }
        throw new JsonDataException("Expected null but was " + peek() + " at path " + getPath());
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x00bd  */
    @Override // com.squareup.moshi.JsonReader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BufferedSource nextSource() {
        int i10;
        int i11 = this.peeked;
        if (i11 == 0) {
            i11 = doPeek();
        }
        d dVar = new d();
        g gVar = JsonValueSource.STATE_END_OF_JSON;
        if (i11 == 3) {
            dVar.E("[");
            gVar = JsonValueSource.STATE_JSON;
        } else if (i11 == 1) {
            dVar.E("{");
            gVar = JsonValueSource.STATE_JSON;
        } else {
            if (i11 == 9) {
                dVar.E("\"");
                gVar = JsonValueSource.STATE_DOUBLE_QUOTED;
            } else if (i11 == 8) {
                dVar.E("'");
                gVar = JsonValueSource.STATE_SINGLE_QUOTED;
            } else if (i11 == 17 || i11 == 16 || i11 == 10) {
                dVar.E(nextString());
            } else if (i11 == 5) {
                dVar.E("true");
            } else if (i11 == 6) {
                dVar.E("false");
            } else if (i11 == 7) {
                dVar.E("null");
            } else if (i11 == 11) {
                String nextString = nextString();
                JsonWriter of = JsonWriter.of(dVar);
                try {
                    of.value(nextString);
                    of.close();
                } catch (Throwable th) {
                    if (of != null) {
                        try {
                            of.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } else {
                throw new JsonDataException("Expected a value but was " + peek() + " at path " + getPath());
            }
            i10 = 0;
            if (this.peeked != 0) {
                int[] iArr = this.pathIndices;
                int i12 = this.stackSize - 1;
                iArr[i12] = iArr[i12] + 1;
                this.peeked = 0;
            }
            this.valueSource = new JsonValueSource(this.source, dVar, gVar, i10);
            pushScope(9);
            return n.b(this.valueSource);
        }
        i10 = 1;
        if (this.peeked != 0) {
        }
        this.valueSource = new JsonValueSource(this.source, dVar, gVar, i10);
        pushScope(9);
        return n.b(this.valueSource);
    }

    @Override // com.squareup.moshi.JsonReader
    public String nextString() {
        String t02;
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 == 10) {
            t02 = nextUnquotedValue();
        } else if (i10 == 9) {
            t02 = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i10 == 8) {
            t02 = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i10 == 11) {
            t02 = this.peekedString;
            this.peekedString = null;
        } else if (i10 == 16) {
            t02 = Long.toString(this.peekedLong);
        } else if (i10 == 17) {
            t02 = this.buffer.t0(this.peekedNumberLength);
        } else {
            throw new JsonDataException("Expected a string but was " + peek() + " at path " + getPath());
        }
        this.peeked = 0;
        int[] iArr = this.pathIndices;
        int i11 = this.stackSize - 1;
        iArr[i11] = iArr[i11] + 1;
        return t02;
    }

    @Override // com.squareup.moshi.JsonReader
    public JsonReader.Token peek() {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        switch (i10) {
            case 1:
                return JsonReader.Token.BEGIN_OBJECT;
            case 2:
                return JsonReader.Token.END_OBJECT;
            case 3:
                return JsonReader.Token.BEGIN_ARRAY;
            case 4:
                return JsonReader.Token.END_ARRAY;
            case 5:
            case 6:
                return JsonReader.Token.BOOLEAN;
            case 7:
                return JsonReader.Token.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return JsonReader.Token.STRING;
            case 12:
            case 13:
            case 14:
            case 15:
                return JsonReader.Token.NAME;
            case 16:
            case 17:
                return JsonReader.Token.NUMBER;
            case 18:
                return JsonReader.Token.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public JsonReader peekJson() {
        return new JsonUtf8Reader(this);
    }

    @Override // com.squareup.moshi.JsonReader
    public void promoteNameToValue() {
        if (hasNext()) {
            this.peekedString = nextName();
            this.peeked = 11;
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public int selectName(JsonReader.Options options) {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 < 12 || i10 > 15) {
            return -1;
        }
        if (i10 == 15) {
            return findName(this.peekedString, options);
        }
        int z10 = this.source.z(options.doubleQuoteSuffix);
        if (z10 != -1) {
            this.peeked = 0;
            this.pathNames[this.stackSize - 1] = options.strings[z10];
            return z10;
        }
        String str = this.pathNames[this.stackSize - 1];
        String nextName = nextName();
        int findName = findName(nextName, options);
        if (findName == -1) {
            this.peeked = 15;
            this.peekedString = nextName;
            this.pathNames[this.stackSize - 1] = str;
        }
        return findName;
    }

    @Override // com.squareup.moshi.JsonReader
    public int selectString(JsonReader.Options options) {
        int i10 = this.peeked;
        if (i10 == 0) {
            i10 = doPeek();
        }
        if (i10 < 8 || i10 > 11) {
            return -1;
        }
        if (i10 == 11) {
            return findString(this.peekedString, options);
        }
        int z10 = this.source.z(options.doubleQuoteSuffix);
        if (z10 != -1) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i11 = this.stackSize - 1;
            iArr[i11] = iArr[i11] + 1;
            return z10;
        }
        String nextString = nextString();
        int findString = findString(nextString, options);
        if (findString == -1) {
            this.peeked = 11;
            this.peekedString = nextString;
            this.pathIndices[this.stackSize - 1] = r0[r4] - 1;
        }
        return findString;
    }

    @Override // com.squareup.moshi.JsonReader
    public void skipName() {
        if (!this.failOnUnknown) {
            int i10 = this.peeked;
            if (i10 == 0) {
                i10 = doPeek();
            }
            if (i10 == 14) {
                skipUnquotedValue();
            } else if (i10 == 13) {
                skipQuotedValue(DOUBLE_QUOTE_OR_SLASH);
            } else if (i10 == 12) {
                skipQuotedValue(SINGLE_QUOTE_OR_SLASH);
            } else if (i10 != 15) {
                throw new JsonDataException("Expected a name but was " + peek() + " at path " + getPath());
            }
            this.peeked = 0;
            this.pathNames[this.stackSize - 1] = "null";
            return;
        }
        JsonReader.Token peek = peek();
        nextName();
        throw new JsonDataException("Cannot skip unexpected " + peek + " at " + getPath());
    }

    @Override // com.squareup.moshi.JsonReader
    public void skipValue() {
        if (!this.failOnUnknown) {
            int i10 = 0;
            do {
                int i11 = this.peeked;
                if (i11 == 0) {
                    i11 = doPeek();
                }
                if (i11 == 3) {
                    pushScope(1);
                } else if (i11 == 1) {
                    pushScope(3);
                } else {
                    if (i11 == 4) {
                        i10--;
                        if (i10 >= 0) {
                            this.stackSize--;
                        } else {
                            throw new JsonDataException("Expected a value but was " + peek() + " at path " + getPath());
                        }
                    } else if (i11 == 2) {
                        i10--;
                        if (i10 >= 0) {
                            this.stackSize--;
                        } else {
                            throw new JsonDataException("Expected a value but was " + peek() + " at path " + getPath());
                        }
                    } else if (i11 == 14 || i11 == 10) {
                        skipUnquotedValue();
                    } else if (i11 == 9 || i11 == 13) {
                        skipQuotedValue(DOUBLE_QUOTE_OR_SLASH);
                    } else if (i11 == 8 || i11 == 12) {
                        skipQuotedValue(SINGLE_QUOTE_OR_SLASH);
                    } else if (i11 == 17) {
                        this.buffer.V(this.peekedNumberLength);
                    } else if (i11 == 18) {
                        throw new JsonDataException("Expected a value but was " + peek() + " at path " + getPath());
                    }
                    this.peeked = 0;
                }
                i10++;
                this.peeked = 0;
            } while (i10 != 0);
            int[] iArr = this.pathIndices;
            int i12 = this.stackSize;
            int i13 = i12 - 1;
            iArr[i13] = iArr[i13] + 1;
            this.pathNames[i12 - 1] = "null";
            return;
        }
        throw new JsonDataException("Cannot skip unexpected " + peek() + " at " + getPath());
    }

    public String toString() {
        return "JsonReader(" + this.source + ")";
    }

    JsonUtf8Reader(JsonUtf8Reader jsonUtf8Reader) {
        super(jsonUtf8Reader);
        this.peeked = 0;
        BufferedSource n02 = jsonUtf8Reader.source.n0();
        this.source = n02;
        this.buffer = n02.a();
        this.peeked = jsonUtf8Reader.peeked;
        this.peekedLong = jsonUtf8Reader.peekedLong;
        this.peekedNumberLength = jsonUtf8Reader.peekedNumberLength;
        this.peekedString = jsonUtf8Reader.peekedString;
        try {
            n02.p0(jsonUtf8Reader.buffer.v0());
        } catch (IOException unused) {
            throw new AssertionError();
        }
    }
}
