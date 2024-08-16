package com.squareup.moshi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import me.BufferedSink;
import me.ForwardingSink;
import me.d;
import me.n;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class JsonValueWriter extends JsonWriter {

    @Nullable
    private String deferredName;
    Object[] stack = new Object[32];

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonValueWriter() {
        pushScope(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JsonValueWriter add(@Nullable Object obj) {
        String str;
        Object put;
        int peekScope = peekScope();
        int i10 = this.stackSize;
        if (i10 == 1) {
            if (peekScope == 6) {
                this.scopes[i10 - 1] = 7;
                this.stack[i10 - 1] = obj;
            } else {
                throw new IllegalStateException("JSON must have only one top-level value.");
            }
        } else if (peekScope != 3 || (str = this.deferredName) == null) {
            if (peekScope != 1) {
                if (peekScope == 9) {
                    throw new IllegalStateException("Sink from valueSink() was not closed");
                }
                throw new IllegalStateException("Nesting problem.");
            }
            ((List) this.stack[i10 - 1]).add(obj);
        } else if ((obj == null && !this.serializeNulls) || (put = ((Map) this.stack[i10 - 1]).put(str, obj)) == null) {
            this.deferredName = null;
        } else {
            throw new IllegalArgumentException("Map key '" + this.deferredName + "' has multiple values at path " + getPath() + ": " + put + " and " + obj);
        }
        return this;
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter beginArray() {
        if (!this.promoteValueToName) {
            int i10 = this.stackSize;
            int i11 = this.flattenStackSize;
            if (i10 == i11 && this.scopes[i10 - 1] == 1) {
                this.flattenStackSize = ~i11;
                return this;
            }
            checkStack();
            ArrayList arrayList = new ArrayList();
            add(arrayList);
            Object[] objArr = this.stack;
            int i12 = this.stackSize;
            objArr[i12] = arrayList;
            this.pathIndices[i12] = 0;
            pushScope(1);
            return this;
        }
        throw new IllegalStateException("Array cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter beginObject() {
        if (!this.promoteValueToName) {
            int i10 = this.stackSize;
            int i11 = this.flattenStackSize;
            if (i10 == i11 && this.scopes[i10 - 1] == 3) {
                this.flattenStackSize = ~i11;
                return this;
            }
            checkStack();
            LinkedHashTreeMap linkedHashTreeMap = new LinkedHashTreeMap();
            add(linkedHashTreeMap);
            this.stack[this.stackSize] = linkedHashTreeMap;
            pushScope(3);
            return this;
        }
        throw new IllegalStateException("Object cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        int i10 = this.stackSize;
        if (i10 <= 1 && (i10 != 1 || this.scopes[i10 - 1] == 7)) {
            this.stackSize = 0;
            return;
        }
        throw new IOException("Incomplete document");
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter endArray() {
        if (peekScope() == 1) {
            int i10 = this.stackSize;
            int i11 = this.flattenStackSize;
            if (i10 == (~i11)) {
                this.flattenStackSize = ~i11;
                return this;
            }
            int i12 = i10 - 1;
            this.stackSize = i12;
            this.stack[i12] = null;
            int[] iArr = this.pathIndices;
            int i13 = i12 - 1;
            iArr[i13] = iArr[i13] + 1;
            return this;
        }
        throw new IllegalStateException("Nesting problem.");
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter endObject() {
        if (peekScope() == 3) {
            if (this.deferredName == null) {
                int i10 = this.stackSize;
                int i11 = this.flattenStackSize;
                if (i10 == (~i11)) {
                    this.flattenStackSize = ~i11;
                    return this;
                }
                this.promoteValueToName = false;
                int i12 = i10 - 1;
                this.stackSize = i12;
                this.stack[i12] = null;
                this.pathNames[i12] = null;
                int[] iArr = this.pathIndices;
                int i13 = i12 - 1;
                iArr[i13] = iArr[i13] + 1;
                return this;
            }
            throw new IllegalStateException("Dangling name: " + this.deferredName);
        }
        throw new IllegalStateException("Nesting problem.");
    }

    @Override // java.io.Flushable
    public void flush() {
        if (this.stackSize == 0) {
            throw new IllegalStateException("JsonWriter is closed.");
        }
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter name(String str) {
        Objects.requireNonNull(str, "name == null");
        if (this.stackSize != 0) {
            if (peekScope() == 3 && this.deferredName == null && !this.promoteValueToName) {
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
            add(null);
            int[] iArr = this.pathIndices;
            int i10 = this.stackSize - 1;
            iArr[i10] = iArr[i10] + 1;
            return this;
        }
        throw new IllegalStateException("null cannot be used as a map key in JSON at path " + getPath());
    }

    public Object root() {
        int i10 = this.stackSize;
        if (i10 <= 1 && (i10 != 1 || this.scopes[i10 - 1] == 7)) {
            return this.stack[0];
        }
        throw new IllegalStateException("Incomplete document");
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(@Nullable String str) {
        if (this.promoteValueToName) {
            this.promoteValueToName = false;
            return name(str);
        }
        add(str);
        int[] iArr = this.pathIndices;
        int i10 = this.stackSize - 1;
        iArr[i10] = iArr[i10] + 1;
        return this;
    }

    @Override // com.squareup.moshi.JsonWriter
    public BufferedSink valueSink() {
        if (!this.promoteValueToName) {
            if (peekScope() != 9) {
                pushScope(9);
                final d dVar = new d();
                return n.a(new ForwardingSink(dVar) { // from class: com.squareup.moshi.JsonValueWriter.1
                    @Override // me.ForwardingSink, me.Sink, java.io.Closeable, java.lang.AutoCloseable
                    public void close() {
                        if (JsonValueWriter.this.peekScope() == 9) {
                            JsonValueWriter jsonValueWriter = JsonValueWriter.this;
                            Object[] objArr = jsonValueWriter.stack;
                            int i10 = jsonValueWriter.stackSize;
                            if (objArr[i10] == null) {
                                jsonValueWriter.stackSize = i10 - 1;
                                Object readJsonValue = JsonReader.of(dVar).readJsonValue();
                                JsonValueWriter jsonValueWriter2 = JsonValueWriter.this;
                                boolean z10 = jsonValueWriter2.serializeNulls;
                                jsonValueWriter2.serializeNulls = true;
                                try {
                                    jsonValueWriter2.add(readJsonValue);
                                    JsonValueWriter jsonValueWriter3 = JsonValueWriter.this;
                                    jsonValueWriter3.serializeNulls = z10;
                                    int[] iArr = jsonValueWriter3.pathIndices;
                                    int i11 = jsonValueWriter3.stackSize - 1;
                                    iArr[i11] = iArr[i11] + 1;
                                    return;
                                } catch (Throwable th) {
                                    JsonValueWriter.this.serializeNulls = z10;
                                    throw th;
                                }
                            }
                        }
                        throw new AssertionError();
                    }
                });
            }
            throw new IllegalStateException("Sink from valueSink() was not closed");
        }
        throw new IllegalStateException("BufferedSink cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(boolean z10) {
        if (!this.promoteValueToName) {
            add(Boolean.valueOf(z10));
            int[] iArr = this.pathIndices;
            int i10 = this.stackSize - 1;
            iArr[i10] = iArr[i10] + 1;
            return this;
        }
        throw new IllegalStateException("Boolean cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(@Nullable Boolean bool) {
        if (!this.promoteValueToName) {
            add(bool);
            int[] iArr = this.pathIndices;
            int i10 = this.stackSize - 1;
            iArr[i10] = iArr[i10] + 1;
            return this;
        }
        throw new IllegalStateException("Boolean cannot be used as a map key in JSON at path " + getPath());
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(double d10) {
        if (!this.lenient && (Double.isNaN(d10) || d10 == Double.NEGATIVE_INFINITY || d10 == Double.POSITIVE_INFINITY)) {
            throw new IllegalArgumentException("Numeric values must be finite, but was " + d10);
        }
        if (this.promoteValueToName) {
            this.promoteValueToName = false;
            return name(Double.toString(d10));
        }
        add(Double.valueOf(d10));
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
        add(Long.valueOf(j10));
        int[] iArr = this.pathIndices;
        int i10 = this.stackSize - 1;
        iArr[i10] = iArr[i10] + 1;
        return this;
    }

    @Override // com.squareup.moshi.JsonWriter
    public JsonWriter value(@Nullable Number number) {
        if (!(number instanceof Byte) && !(number instanceof Short) && !(number instanceof Integer) && !(number instanceof Long)) {
            if ((number instanceof Float) || (number instanceof Double)) {
                return value(number.doubleValue());
            }
            if (number == null) {
                return nullValue();
            }
            BigDecimal bigDecimal = number instanceof BigDecimal ? (BigDecimal) number : new BigDecimal(number.toString());
            if (this.promoteValueToName) {
                this.promoteValueToName = false;
                return name(bigDecimal.toString());
            }
            add(bigDecimal);
            int[] iArr = this.pathIndices;
            int i10 = this.stackSize - 1;
            iArr[i10] = iArr[i10] + 1;
            return this;
        }
        return value(number.longValue());
    }
}
