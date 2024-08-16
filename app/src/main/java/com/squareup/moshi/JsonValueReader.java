package com.squareup.moshi;

import com.squareup.moshi.JsonReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import me.BufferedSource;
import me.d;

/* loaded from: classes2.dex */
final class JsonValueReader extends JsonReader {
    private static final Object JSON_READER_CLOSED = new Object();
    private Object[] stack;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class JsonIterator implements Iterator<Object>, Cloneable {
        final Object[] array;
        final JsonReader.Token endToken;
        int next;

        JsonIterator(JsonReader.Token token, Object[] objArr, int i10) {
            this.endToken = token;
            this.array = objArr;
            this.next = i10;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next < this.array.length;
        }

        @Override // java.util.Iterator
        public Object next() {
            Object[] objArr = this.array;
            int i10 = this.next;
            this.next = i10 + 1;
            return objArr[i10];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public JsonIterator m27clone() {
            return new JsonIterator(this.endToken, this.array, this.next);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonValueReader(Object obj) {
        int[] iArr = this.scopes;
        int i10 = this.stackSize;
        iArr[i10] = 7;
        Object[] objArr = new Object[32];
        this.stack = objArr;
        this.stackSize = i10 + 1;
        objArr[i10] = obj;
    }

    private void push(Object obj) {
        int i10 = this.stackSize;
        if (i10 == this.stack.length) {
            if (i10 != 256) {
                int[] iArr = this.scopes;
                this.scopes = Arrays.copyOf(iArr, iArr.length * 2);
                String[] strArr = this.pathNames;
                this.pathNames = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                int[] iArr2 = this.pathIndices;
                this.pathIndices = Arrays.copyOf(iArr2, iArr2.length * 2);
                Object[] objArr = this.stack;
                this.stack = Arrays.copyOf(objArr, objArr.length * 2);
            } else {
                throw new JsonDataException("Nesting too deep at " + getPath());
            }
        }
        Object[] objArr2 = this.stack;
        int i11 = this.stackSize;
        this.stackSize = i11 + 1;
        objArr2[i11] = obj;
    }

    private void remove() {
        int i10 = this.stackSize - 1;
        this.stackSize = i10;
        Object[] objArr = this.stack;
        objArr[i10] = null;
        this.scopes[i10] = 0;
        if (i10 > 0) {
            int[] iArr = this.pathIndices;
            int i11 = i10 - 1;
            iArr[i11] = iArr[i11] + 1;
            Object obj = objArr[i10 - 1];
            if (obj instanceof Iterator) {
                Iterator it = (Iterator) obj;
                if (it.hasNext()) {
                    push(it.next());
                }
            }
        }
    }

    @Nullable
    private <T> T require(Class<T> cls, JsonReader.Token token) {
        int i10 = this.stackSize;
        Object obj = i10 != 0 ? this.stack[i10 - 1] : null;
        if (cls.isInstance(obj)) {
            return cls.cast(obj);
        }
        if (obj == null && token == JsonReader.Token.NULL) {
            return null;
        }
        if (obj == JSON_READER_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw typeMismatch(obj, token);
    }

    private String stringKey(Map.Entry<?, ?> entry) {
        Object key = entry.getKey();
        if (key instanceof String) {
            return (String) key;
        }
        throw typeMismatch(key, JsonReader.Token.NAME);
    }

    @Override // com.squareup.moshi.JsonReader
    public void beginArray() {
        List list = (List) require(List.class, JsonReader.Token.BEGIN_ARRAY);
        JsonIterator jsonIterator = new JsonIterator(JsonReader.Token.END_ARRAY, list.toArray(new Object[list.size()]), 0);
        Object[] objArr = this.stack;
        int i10 = this.stackSize;
        objArr[i10 - 1] = jsonIterator;
        this.scopes[i10 - 1] = 1;
        this.pathIndices[i10 - 1] = 0;
        if (jsonIterator.hasNext()) {
            push(jsonIterator.next());
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public void beginObject() {
        Map map = (Map) require(Map.class, JsonReader.Token.BEGIN_OBJECT);
        JsonIterator jsonIterator = new JsonIterator(JsonReader.Token.END_OBJECT, map.entrySet().toArray(new Object[map.size()]), 0);
        Object[] objArr = this.stack;
        int i10 = this.stackSize;
        objArr[i10 - 1] = jsonIterator;
        this.scopes[i10 - 1] = 3;
        if (jsonIterator.hasNext()) {
            push(jsonIterator.next());
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Arrays.fill(this.stack, 0, this.stackSize, (Object) null);
        this.stack[0] = JSON_READER_CLOSED;
        this.scopes[0] = 8;
        this.stackSize = 1;
    }

    @Override // com.squareup.moshi.JsonReader
    public void endArray() {
        JsonReader.Token token = JsonReader.Token.END_ARRAY;
        JsonIterator jsonIterator = (JsonIterator) require(JsonIterator.class, token);
        if (jsonIterator.endToken == token && !jsonIterator.hasNext()) {
            remove();
            return;
        }
        throw typeMismatch(jsonIterator, token);
    }

    @Override // com.squareup.moshi.JsonReader
    public void endObject() {
        JsonReader.Token token = JsonReader.Token.END_OBJECT;
        JsonIterator jsonIterator = (JsonIterator) require(JsonIterator.class, token);
        if (jsonIterator.endToken == token && !jsonIterator.hasNext()) {
            this.pathNames[this.stackSize - 1] = null;
            remove();
            return;
        }
        throw typeMismatch(jsonIterator, token);
    }

    @Override // com.squareup.moshi.JsonReader
    public boolean hasNext() {
        int i10 = this.stackSize;
        if (i10 == 0) {
            return false;
        }
        Object obj = this.stack[i10 - 1];
        return !(obj instanceof Iterator) || ((Iterator) obj).hasNext();
    }

    @Override // com.squareup.moshi.JsonReader
    public boolean nextBoolean() {
        Boolean bool = (Boolean) require(Boolean.class, JsonReader.Token.BOOLEAN);
        remove();
        return bool.booleanValue();
    }

    @Override // com.squareup.moshi.JsonReader
    public double nextDouble() {
        double parseDouble;
        JsonReader.Token token = JsonReader.Token.NUMBER;
        Object require = require(Object.class, token);
        if (require instanceof Number) {
            parseDouble = ((Number) require).doubleValue();
        } else if (require instanceof String) {
            try {
                parseDouble = Double.parseDouble((String) require);
            } catch (NumberFormatException unused) {
                throw typeMismatch(require, JsonReader.Token.NUMBER);
            }
        } else {
            throw typeMismatch(require, token);
        }
        if (!this.lenient && (Double.isNaN(parseDouble) || Double.isInfinite(parseDouble))) {
            throw new JsonEncodingException("JSON forbids NaN and infinities: " + parseDouble + " at path " + getPath());
        }
        remove();
        return parseDouble;
    }

    @Override // com.squareup.moshi.JsonReader
    public int nextInt() {
        int intValueExact;
        JsonReader.Token token = JsonReader.Token.NUMBER;
        Object require = require(Object.class, token);
        if (require instanceof Number) {
            intValueExact = ((Number) require).intValue();
        } else if (require instanceof String) {
            try {
                try {
                    intValueExact = Integer.parseInt((String) require);
                } catch (NumberFormatException unused) {
                    intValueExact = new BigDecimal((String) require).intValueExact();
                }
            } catch (NumberFormatException unused2) {
                throw typeMismatch(require, JsonReader.Token.NUMBER);
            }
        } else {
            throw typeMismatch(require, token);
        }
        remove();
        return intValueExact;
    }

    @Override // com.squareup.moshi.JsonReader
    public long nextLong() {
        long longValueExact;
        JsonReader.Token token = JsonReader.Token.NUMBER;
        Object require = require(Object.class, token);
        if (require instanceof Number) {
            longValueExact = ((Number) require).longValue();
        } else if (require instanceof String) {
            try {
                try {
                    longValueExact = Long.parseLong((String) require);
                } catch (NumberFormatException unused) {
                    longValueExact = new BigDecimal((String) require).longValueExact();
                }
            } catch (NumberFormatException unused2) {
                throw typeMismatch(require, JsonReader.Token.NUMBER);
            }
        } else {
            throw typeMismatch(require, token);
        }
        remove();
        return longValueExact;
    }

    @Override // com.squareup.moshi.JsonReader
    public String nextName() {
        Map.Entry<?, ?> entry = (Map.Entry) require(Map.Entry.class, JsonReader.Token.NAME);
        String stringKey = stringKey(entry);
        this.stack[this.stackSize - 1] = entry.getValue();
        this.pathNames[this.stackSize - 2] = stringKey;
        return stringKey;
    }

    @Override // com.squareup.moshi.JsonReader
    @Nullable
    public <T> T nextNull() {
        require(Void.class, JsonReader.Token.NULL);
        remove();
        return null;
    }

    @Override // com.squareup.moshi.JsonReader
    public BufferedSource nextSource() {
        Object readJsonValue = readJsonValue();
        d dVar = new d();
        JsonWriter of = JsonWriter.of(dVar);
        try {
            of.jsonValue(readJsonValue);
            of.close();
            return dVar;
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
    }

    @Override // com.squareup.moshi.JsonReader
    public String nextString() {
        int i10 = this.stackSize;
        Object obj = i10 != 0 ? this.stack[i10 - 1] : null;
        if (obj instanceof String) {
            remove();
            return (String) obj;
        }
        if (obj instanceof Number) {
            remove();
            return obj.toString();
        }
        if (obj == JSON_READER_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw typeMismatch(obj, JsonReader.Token.STRING);
    }

    @Override // com.squareup.moshi.JsonReader
    public JsonReader.Token peek() {
        int i10 = this.stackSize;
        if (i10 == 0) {
            return JsonReader.Token.END_DOCUMENT;
        }
        Object obj = this.stack[i10 - 1];
        if (obj instanceof JsonIterator) {
            return ((JsonIterator) obj).endToken;
        }
        if (obj instanceof List) {
            return JsonReader.Token.BEGIN_ARRAY;
        }
        if (obj instanceof Map) {
            return JsonReader.Token.BEGIN_OBJECT;
        }
        if (obj instanceof Map.Entry) {
            return JsonReader.Token.NAME;
        }
        if (obj instanceof String) {
            return JsonReader.Token.STRING;
        }
        if (obj instanceof Boolean) {
            return JsonReader.Token.BOOLEAN;
        }
        if (obj instanceof Number) {
            return JsonReader.Token.NUMBER;
        }
        if (obj == null) {
            return JsonReader.Token.NULL;
        }
        if (obj == JSON_READER_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw typeMismatch(obj, "a JSON value");
    }

    @Override // com.squareup.moshi.JsonReader
    public JsonReader peekJson() {
        return new JsonValueReader(this);
    }

    @Override // com.squareup.moshi.JsonReader
    public void promoteNameToValue() {
        if (hasNext()) {
            push(nextName());
        }
    }

    @Override // com.squareup.moshi.JsonReader
    public int selectName(JsonReader.Options options) {
        Map.Entry<?, ?> entry = (Map.Entry) require(Map.Entry.class, JsonReader.Token.NAME);
        String stringKey = stringKey(entry);
        int length = options.strings.length;
        for (int i10 = 0; i10 < length; i10++) {
            if (options.strings[i10].equals(stringKey)) {
                this.stack[this.stackSize - 1] = entry.getValue();
                this.pathNames[this.stackSize - 2] = stringKey;
                return i10;
            }
        }
        return -1;
    }

    @Override // com.squareup.moshi.JsonReader
    public int selectString(JsonReader.Options options) {
        int i10 = this.stackSize;
        Object obj = i10 != 0 ? this.stack[i10 - 1] : null;
        if (!(obj instanceof String)) {
            if (obj != JSON_READER_CLOSED) {
                return -1;
            }
            throw new IllegalStateException("JsonReader is closed");
        }
        String str = (String) obj;
        int length = options.strings.length;
        for (int i11 = 0; i11 < length; i11++) {
            if (options.strings[i11].equals(str)) {
                remove();
                return i11;
            }
        }
        return -1;
    }

    @Override // com.squareup.moshi.JsonReader
    public void skipName() {
        if (!this.failOnUnknown) {
            this.stack[this.stackSize - 1] = ((Map.Entry) require(Map.Entry.class, JsonReader.Token.NAME)).getValue();
            this.pathNames[this.stackSize - 2] = "null";
            return;
        }
        JsonReader.Token peek = peek();
        nextName();
        throw new JsonDataException("Cannot skip unexpected " + peek + " at " + getPath());
    }

    @Override // com.squareup.moshi.JsonReader
    public void skipValue() {
        if (!this.failOnUnknown) {
            int i10 = this.stackSize;
            if (i10 > 1) {
                this.pathNames[i10 - 2] = "null";
            }
            Object obj = i10 != 0 ? this.stack[i10 - 1] : null;
            if (!(obj instanceof JsonIterator)) {
                if (obj instanceof Map.Entry) {
                    Object[] objArr = this.stack;
                    objArr[i10 - 1] = ((Map.Entry) objArr[i10 - 1]).getValue();
                    return;
                } else {
                    if (i10 > 0) {
                        remove();
                        return;
                    }
                    throw new JsonDataException("Expected a value but was " + peek() + " at path " + getPath());
                }
            }
            throw new JsonDataException("Expected a value but was " + peek() + " at path " + getPath());
        }
        throw new JsonDataException("Cannot skip unexpected " + peek() + " at " + getPath());
    }

    JsonValueReader(JsonValueReader jsonValueReader) {
        super(jsonValueReader);
        this.stack = (Object[]) jsonValueReader.stack.clone();
        for (int i10 = 0; i10 < this.stackSize; i10++) {
            Object[] objArr = this.stack;
            if (objArr[i10] instanceof JsonIterator) {
                objArr[i10] = ((JsonIterator) objArr[i10]).m27clone();
            }
        }
    }
}
