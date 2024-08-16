package com.google.gson.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class JsonWriter implements Closeable, Flushable {
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private int[] stack = new int[32];
    private int stackSize = 0;
    private static final Pattern VALID_JSON_NUMBER_PATTERN = Pattern.compile("-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][-+]?[0-9]+)?");
    private static final String[] REPLACEMENT_CHARS = new String[128];

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
        String[] strArr2 = (String[]) strArr.clone();
        HTML_SAFE_REPLACEMENT_CHARS = strArr2;
        strArr2[60] = "\\u003c";
        strArr2[62] = "\\u003e";
        strArr2[38] = "\\u0026";
        strArr2[61] = "\\u003d";
        strArr2[39] = "\\u0027";
    }

    public JsonWriter(Writer writer) {
        push(6);
        this.separator = ":";
        this.serializeNulls = true;
        Objects.requireNonNull(writer, "out == null");
        this.out = writer;
    }

    private void beforeName() {
        int peek = peek();
        if (peek == 5) {
            this.out.write(44);
        } else if (peek != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        newline();
        replaceTop(4);
    }

    private void beforeValue() {
        int peek = peek();
        if (peek == 1) {
            replaceTop(2);
            newline();
            return;
        }
        if (peek == 2) {
            this.out.append(',');
            newline();
        } else {
            if (peek != 4) {
                if (peek != 6) {
                    if (peek == 7) {
                        if (!this.lenient) {
                            throw new IllegalStateException("JSON must have only one top-level value.");
                        }
                    } else {
                        throw new IllegalStateException("Nesting problem.");
                    }
                }
                replaceTop(7);
                return;
            }
            this.out.append((CharSequence) this.separator);
            replaceTop(5);
        }
    }

    private JsonWriter close(int i10, int i11, char c10) {
        int peek = peek();
        if (peek != i11 && peek != i10) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName == null) {
            this.stackSize--;
            if (peek == i11) {
                newline();
            }
            this.out.write(c10);
            return this;
        }
        throw new IllegalStateException("Dangling name: " + this.deferredName);
    }

    private static boolean isTrustedNumberType(Class<? extends Number> cls) {
        return cls == Integer.class || cls == Long.class || cls == Double.class || cls == Float.class || cls == Byte.class || cls == Short.class || cls == BigDecimal.class || cls == BigInteger.class || cls == AtomicInteger.class || cls == AtomicLong.class;
    }

    private void newline() {
        if (this.indent == null) {
            return;
        }
        this.out.write(10);
        int i10 = this.stackSize;
        for (int i11 = 1; i11 < i10; i11++) {
            this.out.write(this.indent);
        }
    }

    private JsonWriter open(int i10, char c10) {
        beforeValue();
        push(i10);
        this.out.write(c10);
        return this;
    }

    private int peek() {
        int i10 = this.stackSize;
        if (i10 != 0) {
            return this.stack[i10 - 1];
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    private void push(int i10) {
        int i11 = this.stackSize;
        int[] iArr = this.stack;
        if (i11 == iArr.length) {
            this.stack = Arrays.copyOf(iArr, i11 * 2);
        }
        int[] iArr2 = this.stack;
        int i12 = this.stackSize;
        this.stackSize = i12 + 1;
        iArr2[i12] = i10;
    }

    private void replaceTop(int i10) {
        this.stack[this.stackSize - 1] = i10;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void string(String str) {
        int i10;
        String str2;
        String[] strArr = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
        this.out.write(34);
        int length = str.length();
        int i11 = 0;
        while (i10 < length) {
            char charAt = str.charAt(i10);
            if (charAt < 128) {
                str2 = strArr[charAt];
                i10 = str2 == null ? i10 + 1 : 0;
                if (i11 < i10) {
                    this.out.write(str, i11, i10 - i11);
                }
                this.out.write(str2);
                i11 = i10 + 1;
            } else {
                if (charAt == 8232) {
                    str2 = "\\u2028";
                } else if (charAt == 8233) {
                    str2 = "\\u2029";
                }
                if (i11 < i10) {
                }
                this.out.write(str2);
                i11 = i10 + 1;
            }
        }
        if (i11 < length) {
            this.out.write(str, i11, length - i11);
        }
        this.out.write(34);
    }

    private void writeDeferredName() {
        if (this.deferredName != null) {
            beforeName();
            string(this.deferredName);
            this.deferredName = null;
        }
    }

    public JsonWriter beginArray() {
        writeDeferredName();
        return open(1, '[');
    }

    public JsonWriter beginObject() {
        writeDeferredName();
        return open(3, '{');
    }

    public JsonWriter endArray() {
        return close(1, 2, ']');
    }

    public JsonWriter endObject() {
        return close(3, 5, '}');
    }

    public void flush() {
        if (this.stackSize != 0) {
            this.out.flush();
            return;
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public JsonWriter jsonValue(String str) {
        if (str == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue();
        this.out.append((CharSequence) str);
        return this;
    }

    public JsonWriter name(String str) {
        Objects.requireNonNull(str, "name == null");
        if (this.deferredName == null) {
            if (this.stackSize != 0) {
                this.deferredName = str;
                return this;
            }
            throw new IllegalStateException("JsonWriter is closed.");
        }
        throw new IllegalStateException();
    }

    public JsonWriter nullValue() {
        if (this.deferredName != null) {
            if (this.serializeNulls) {
                writeDeferredName();
            } else {
                this.deferredName = null;
                return this;
            }
        }
        beforeValue();
        this.out.write("null");
        return this;
    }

    public final void setHtmlSafe(boolean z10) {
        this.htmlSafe = z10;
    }

    public final void setIndent(String str) {
        if (str.length() == 0) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = str;
            this.separator = ": ";
        }
    }

    public final void setLenient(boolean z10) {
        this.lenient = z10;
    }

    public final void setSerializeNulls(boolean z10) {
        this.serializeNulls = z10;
    }

    public JsonWriter value(String str) {
        if (str == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue();
        string(str);
        return this;
    }

    public JsonWriter value(boolean z10) {
        writeDeferredName();
        beforeValue();
        this.out.write(z10 ? "true" : "false");
        return this;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.out.close();
        int i10 = this.stackSize;
        if (i10 <= 1 && (i10 != 1 || this.stack[i10 - 1] == 7)) {
            this.stackSize = 0;
            return;
        }
        throw new IOException("Incomplete document");
    }

    public JsonWriter value(Boolean bool) {
        if (bool == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue();
        this.out.write(bool.booleanValue() ? "true" : "false");
        return this;
    }

    public JsonWriter value(float f10) {
        writeDeferredName();
        if (!this.lenient && (Float.isNaN(f10) || Float.isInfinite(f10))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + f10);
        }
        beforeValue();
        this.out.append((CharSequence) Float.toString(f10));
        return this;
    }

    public JsonWriter value(double d10) {
        writeDeferredName();
        if (!this.lenient && (Double.isNaN(d10) || Double.isInfinite(d10))) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + d10);
        }
        beforeValue();
        this.out.append((CharSequence) Double.toString(d10));
        return this;
    }

    public JsonWriter value(long j10) {
        writeDeferredName();
        beforeValue();
        this.out.write(Long.toString(j10));
        return this;
    }

    public JsonWriter value(Number number) {
        if (number == null) {
            return nullValue();
        }
        writeDeferredName();
        String obj = number.toString();
        if (!obj.equals("-Infinity") && !obj.equals("Infinity") && !obj.equals("NaN")) {
            Class<?> cls = number.getClass();
            if (!isTrustedNumberType(cls) && !VALID_JSON_NUMBER_PATTERN.matcher(obj).matches()) {
                throw new IllegalArgumentException("String created by " + cls + " is not a valid JSON number: " + obj);
            }
        } else if (!this.lenient) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + obj);
        }
        beforeValue();
        this.out.append((CharSequence) obj);
        return this;
    }
}
