package com.squareup.moshi;

import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nullable;
import me.BufferedSink;
import me.Sink;
import me.Timeout;
import me.d;
import me.n;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class JsonUtf8Writer extends JsonWriter {
    private static final String[] REPLACEMENT_CHARS = new String[128];
    private String deferredName;
    private String separator = ":";
    private final BufferedSink sink;

    static {
        for (int i10 = 0; i10 <= 31; i10++) {
            REPLACEMENT_CHARS[i10] = String.format("\\u%04x", Integer.valueOf(i10));
        }
        String[] strArr = REPLACEMENT_CHARS;
        strArr[34] = "\\\"";
        strArr[92] = "\\\\";
        strArr[9] = "\\t";
        strArr[8] = "\\b";
        strArr[10] = "\\n";
        strArr[13] = "\\r";
        strArr[12] = "\\f";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonUtf8Writer(BufferedSink bufferedSink) {
        Objects.requireNonNull(bufferedSink, "sink == null");
        this.sink = bufferedSink;
        pushScope(6);
    }

    private void beforeName() {
        int peekScope = peekScope();
        if (peekScope == 5) {
            this.sink.t(44);
        } else if (peekScope != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        newline();
        replaceTop(4);
    }

    private void beforeValue() {
        int peekScope = peekScope();
        int i10 = 7;
        if (peekScope != 1) {
            if (peekScope == 2) {
                this.sink.t(44);
            } else {
                if (peekScope == 4) {
                    i10 = 5;
                    this.sink.E(this.separator);
                } else {
                    if (peekScope == 9) {
                        throw new IllegalStateException("Sink from valueSink() was not closed");
                    }
                    if (peekScope != 6) {
                        if (peekScope == 7) {
                            if (!this.lenient) {
                                throw new IllegalStateException("JSON must have only one top-level value.");
                            }
                        } else {
                            throw new IllegalStateException("Nesting problem.");
                        }
                    }
                }
                replaceTop(i10);
            }
        }
        newline();
        i10 = 2;
        replaceTop(i10);
    }

    private JsonWriter close(int i10, int i11, char c10) {
        int peekScope = peekScope();
        if (peekScope != i11 && peekScope != i10) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName == null) {
            int i12 = this.stackSize;
            int i13 = this.flattenStackSize;
            if (i12 == (~i13)) {
                this.flattenStackSize = ~i13;
                return this;
            }
            int i14 = i12 - 1;
            this.stackSize = i14;
            this.pathNames[i14] = null;
            int[] iArr = this.pathIndices;
            int i15 = i14 - 1;
            iArr[i15] = iArr[i15] + 1;
            if (peekScope == i11) {
                newline();
            }
            this.sink.t(c10);
            return this;
        }
        throw new IllegalStateException("Dangling name: " + this.deferredName);
    }

    private void newline() {
        if (this.indent == null) {
            return;
        }
        this.sink.t(10);
        int i10 = this.stackSize;
        for (int i11 = 1; i11 < i10; i11++) {
            this.sink.E(this.indent);
        }
    }

    private JsonWriter open(int i10, int i11, char c10) {
        int i12 = this.stackSize;
        int i13 = this.flattenStackSize;
        if (i12 == i13) {
            int[] iArr = this.scopes;
            if (iArr[i12 - 1] == i10 || iArr[i12 - 1] == i11) {
                this.flattenStackSize = ~i13;
                return this;
            }
        }
        beforeValue();
        checkStack();
        pushScope(i10);
        this.pathIndices[this.stackSize - 1] = 0;
        this.sink.t(c10);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void string(BufferedSink bufferedSink, String str) {
        int i10;
        String str2;
        String[] strArr = REPLACEMENT_CHARS;
        bufferedSink.t(34);
        int length = str.length();
        int i11 = 0;
        while (i10 < length) {
            char charAt = str.charAt(i10);
            if (charAt < 128) {
                str2 = strArr[charAt];
                i10 = str2 == null ? i10 + 1 : 0;
                if (i11 < i10) {
                    bufferedSink.R(str, i11, i10);
                }
                bufferedSink.E(str2);
                i11 = i10 + 1;
            } else {
                if (charAt == 8232) {
                    str2 = "\\u2028";
                } else if (charAt == 8233) {
                    str2 = "\\u2029";
                }
                if (i11 < i10) {
                }
                bufferedSink.E(str2);
                i11 = i10 + 1;
            }
        }
        if (i11 < length) {
            bufferedSink.R(str, i11, length);
        }
        bufferedSink.t(34);
    }

    private void writeDeferredName() {
        if (this.deferredName != null) {
            beforeName();
            string(this.sink, this.deferredName);
            this.deferredName = null;
        }
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter beginArray() {
        if (!this.promoteValueToName) {
            writeDeferredName();
            return open(1, 2, '[');
        }
        throw new IllegalStateException("Array cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter beginObject() {
        if (!this.promoteValueToName) {
            writeDeferredName();
            return open(3, 5, '{');
        }
        throw new IllegalStateException("Object cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter endArray() {
        return close(1, 2, ']');
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter endObject() {
        this.promoteValueToName = false;
        return close(3, 5, '}');
    }

    @Override // java.io.Flushable
    public void flush() {
        if (this.stackSize != 0) {
            this.sink.flush();
            return;
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter name(String str) {
        Objects.requireNonNull(str, "name == null");
        if (this.stackSize != 0) {
            int peekScope = peekScope();
            if ((peekScope == 3 || peekScope == 5) && this.deferredName == null && !this.promoteValueToName) {
                this.deferredName = str;
                this.pathNames[this.stackSize - 1] = str;
                return this;
            }
            throw new IllegalStateException("Nesting problem.");
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter nullValue() {
        if (!this.promoteValueToName) {
            if (this.deferredName != null) {
                if (this.serializeNulls) {
                    writeDeferredName();
                } else {
                    this.deferredName = null;
                    return this;
                }
            }
            beforeValue();
            this.sink.E("null");
            int[] iArr = this.pathIndices;
            int i10 = this.stackSize - 1;
            iArr[i10] = iArr[i10] + 1;
            return this;
        }
        throw new IllegalStateException("null cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public void setIndent(String str) {
        super.setIndent(str);
        this.separator = !str.isEmpty() ? ": " : ":";
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(String str) {
        if (str == null) {
            return nullValue();
        }
        if (this.promoteValueToName) {
            this.promoteValueToName = false;
            return name(str);
        }
        writeDeferredName();
        beforeValue();
        string(this.sink, str);
        int[] iArr = this.pathIndices;
        int i10 = this.stackSize - 1;
        iArr[i10] = iArr[i10] + 1;
        return this;
    }

    @Override // com.squareup.moshi.JsonWriter
    public BufferedSink valueSink() {
        if (!this.promoteValueToName) {
            writeDeferredName();
            beforeValue();
            pushScope(9);
            return n.a(new Sink() { // from class: com.squareup.moshi.JsonUtf8Writer.1
                @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                    if (JsonUtf8Writer.this.peekScope() == 9) {
                        JsonUtf8Writer jsonUtf8Writer = JsonUtf8Writer.this;
                        int i10 = jsonUtf8Writer.stackSize - 1;
                        jsonUtf8Writer.stackSize = i10;
                        int[] iArr = jsonUtf8Writer.pathIndices;
                        int i11 = i10 - 1;
                        iArr[i11] = iArr[i11] + 1;
                        return;
                    }
                    throw new AssertionError();
                }

                @Override // me.Sink, java.io.Flushable
                public void flush() {
                    JsonUtf8Writer.this.sink.flush();
                }

                @Override // me.Sink
                public Timeout timeout() {
                    return Timeout.f15467e;
                }

                @Override // me.Sink
                public void write(d dVar, long j10) {
                    JsonUtf8Writer.this.sink.write(dVar, j10);
                }
            });
        }
        throw new IllegalStateException("BufferedSink cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(boolean z10) {
        if (!this.promoteValueToName) {
            writeDeferredName();
            beforeValue();
            this.sink.E(z10 ? "true" : "false");
            int[] iArr = this.pathIndices;
            int i10 = this.stackSize - 1;
            iArr[i10] = iArr[i10] + 1;
            return this;
        }
        throw new IllegalStateException("Boolean cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.sink.close();
        int i10 = this.stackSize;
        if (i10 <= 1 && (i10 != 1 || this.scopes[i10 - 1] == 7)) {
            this.stackSize = 0;
            return;
        }
        throw new IOException("Incomplete document");
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(Boolean bool) {
        if (bool == null) {
            return nullValue();
        }
        return value(bool.booleanValue());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(double d10) {
        if (!this.lenient && (Double.isNaN(d10) || Double.isInfinite(d10))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + d10);
        }
        if (this.promoteValueToName) {
            this.promoteValueToName = false;
            return name(Double.toString(d10));
        }
        writeDeferredName();
        beforeValue();
        this.sink.E(Double.toString(d10));
        int[] iArr = this.pathIndices;
        int i10 = this.stackSize - 1;
        iArr[i10] = iArr[i10] + 1;
        return this;
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(long j10) {
        if (this.promoteValueToName) {
            this.promoteValueToName = false;
            return name(Long.toString(j10));
        }
        writeDeferredName();
        beforeValue();
        this.sink.E(Long.toString(j10));
        int[] iArr = this.pathIndices;
        int i10 = this.stackSize - 1;
        iArr[i10] = iArr[i10] + 1;
        return this;
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(@Nullable Number number) {
        if (number == null) {
            return nullValue();
        }
        String obj = number.toString();
        if (!this.lenient && (obj.equals("-Infinity") || obj.equals("Infinity") || obj.equals("NaN"))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + number);
        }
        if (this.promoteValueToName) {
            this.promoteValueToName = false;
            return name(obj);
        }
        writeDeferredName();
        beforeValue();
        this.sink.E(obj);
        int[] iArr = this.pathIndices;
        int i10 = this.stackSize - 1;
        iArr[i10] = iArr[i10] + 1;
        return this;
    }
}
