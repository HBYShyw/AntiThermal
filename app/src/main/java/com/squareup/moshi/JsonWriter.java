package com.squareup.moshi;

import java.io.Closeable;
import java.io.Flushable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import me.BufferedSink;
import me.BufferedSource;

/* loaded from: classes2.dex */
public abstract class JsonWriter implements Closeable, Flushable {
    String indent;
    boolean lenient;
    boolean promoteValueToName;
    boolean serializeNulls;
    private Map<Class<?>, Object> tags;
    int stackSize = 0;
    int[] scopes = new int[32];
    String[] pathNames = new String[32];
    int[] pathIndices = new int[32];
    int flattenStackSize = -1;

    @CheckReturnValue
    public static JsonWriter of(BufferedSink bufferedSink) {
        return new JsonUtf8Writer(bufferedSink);
    }

    public abstract JsonWriter beginArray();

    @CheckReturnValue
    public final int beginFlatten() {
        int peekScope = peekScope();
        if (peekScope != 5 && peekScope != 3 && peekScope != 2 && peekScope != 1) {
            throw new IllegalStateException("Nesting problem.");
        }
        int i10 = this.flattenStackSize;
        this.flattenStackSize = this.stackSize;
        return i10;
    }

    public abstract JsonWriter beginObject();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean checkStack() {
        int i10 = this.stackSize;
        int[] iArr = this.scopes;
        if (i10 != iArr.length) {
            return false;
        }
        if (i10 != 256) {
            this.scopes = Arrays.copyOf(iArr, iArr.length * 2);
            String[] strArr = this.pathNames;
            this.pathNames = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
            int[] iArr2 = this.pathIndices;
            this.pathIndices = Arrays.copyOf(iArr2, iArr2.length * 2);
            if (!(this instanceof JsonValueWriter)) {
                return true;
            }
            JsonValueWriter jsonValueWriter = (JsonValueWriter) this;
            Object[] objArr = jsonValueWriter.stack;
            jsonValueWriter.stack = Arrays.copyOf(objArr, objArr.length * 2);
            return true;
        }
        throw new JsonDataException("Nesting too deep at " + getPath() + ": circular reference?");
    }

    public abstract JsonWriter endArray();

    public final void endFlatten(int i10) {
        this.flattenStackSize = i10;
    }

    public abstract JsonWriter endObject();

    @CheckReturnValue
    public final String getIndent() {
        String str = this.indent;
        return str != null ? str : "";
    }

    @CheckReturnValue
    public final String getPath() {
        return JsonScope.getPath(this.stackSize, this.scopes, this.pathNames, this.pathIndices);
    }

    @CheckReturnValue
    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    @CheckReturnValue
    public final boolean isLenient() {
        return this.lenient;
    }

    public final JsonWriter jsonValue(@Nullable Object obj) {
        if (obj instanceof Map) {
            beginObject();
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (!(key instanceof String)) {
                    throw new IllegalArgumentException(key == null ? "Map keys must be non-null" : "Map keys must be of type String: " + key.getClass().getName());
                }
                name((String) key);
                jsonValue(entry.getValue());
            }
            endObject();
        } else if (obj instanceof List) {
            beginArray();
            Iterator it = ((List) obj).iterator();
            while (it.hasNext()) {
                jsonValue(it.next());
            }
            endArray();
        } else if (obj instanceof String) {
            value((String) obj);
        } else if (obj instanceof Boolean) {
            value(((Boolean) obj).booleanValue());
        } else if (obj instanceof Double) {
            value(((Double) obj).doubleValue());
        } else if (obj instanceof Long) {
            value(((Long) obj).longValue());
        } else if (obj instanceof Number) {
            value((Number) obj);
        } else if (obj == null) {
            nullValue();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
        return this;
    }

    public abstract JsonWriter name(String str);

    public abstract JsonWriter nullValue();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int peekScope() {
        int i10 = this.stackSize;
        if (i10 != 0) {
            return this.scopes[i10 - 1];
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    public final void promoteValueToName() {
        int peekScope = peekScope();
        if (peekScope != 5 && peekScope != 3) {
            throw new IllegalStateException("Nesting problem.");
        }
        this.promoteValueToName = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void pushScope(int i10) {
        int[] iArr = this.scopes;
        int i11 = this.stackSize;
        this.stackSize = i11 + 1;
        iArr[i11] = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void replaceTop(int i10) {
        this.scopes[this.stackSize - 1] = i10;
    }

    public void setIndent(String str) {
        if (str.isEmpty()) {
            str = null;
        }
        this.indent = str;
    }

    public final void setLenient(boolean z10) {
        this.lenient = z10;
    }

    public final void setSerializeNulls(boolean z10) {
        this.serializeNulls = z10;
    }

    public final <T> void setTag(Class<T> cls, T t7) {
        if (cls.isAssignableFrom(t7.getClass())) {
            if (this.tags == null) {
                this.tags = new LinkedHashMap();
            }
            this.tags.put(cls, t7);
        } else {
            throw new IllegalArgumentException("Tag value must be of type " + cls.getName());
        }
    }

    @CheckReturnValue
    @Nullable
    public final <T> T tag(Class<T> cls) {
        Map<Class<?>, Object> map = this.tags;
        if (map == null) {
            return null;
        }
        return (T) map.get(cls);
    }

    public abstract JsonWriter value(double d10);

    public abstract JsonWriter value(long j10);

    public abstract JsonWriter value(@Nullable Boolean bool);

    public abstract JsonWriter value(@Nullable Number number);

    public abstract JsonWriter value(@Nullable String str);

    public final JsonWriter value(BufferedSource bufferedSource) {
        if (!this.promoteValueToName) {
            BufferedSink valueSink = valueSink();
            try {
                bufferedSource.l0(valueSink);
                if (valueSink != null) {
                    valueSink.close();
                }
                return this;
            } catch (Throwable th) {
                if (valueSink != null) {
                    try {
                        valueSink.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        throw new IllegalStateException("BufferedSource cannot be used as a map key in JSON at path " + getPath());
    }

    public abstract JsonWriter value(boolean z10);

    @CheckReturnValue
    public abstract BufferedSink valueSink();
}
