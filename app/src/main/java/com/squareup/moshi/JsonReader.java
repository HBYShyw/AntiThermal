package com.squareup.moshi;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import me.BufferedSource;
import me.d;
import me.g;
import me.q;

/* loaded from: classes2.dex */
public abstract class JsonReader implements Closeable {
    boolean failOnUnknown;
    boolean lenient;
    int[] pathIndices;
    String[] pathNames;
    int[] scopes;
    int stackSize;
    private Map<Class<?>, Object> tags;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.squareup.moshi.JsonReader$1, reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$squareup$moshi$JsonReader$Token;

        static {
            int[] iArr = new int[Token.values().length];
            $SwitchMap$com$squareup$moshi$JsonReader$Token = iArr;
            try {
                iArr[Token.BEGIN_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$squareup$moshi$JsonReader$Token[Token.BEGIN_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$squareup$moshi$JsonReader$Token[Token.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$squareup$moshi$JsonReader$Token[Token.NUMBER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$squareup$moshi$JsonReader$Token[Token.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$squareup$moshi$JsonReader$Token[Token.NULL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Options {
        final q doubleQuoteSuffix;
        final String[] strings;

        private Options(String[] strArr, q qVar) {
            this.strings = strArr;
            this.doubleQuoteSuffix = qVar;
        }

        @CheckReturnValue
        public static Options of(String... strArr) {
            try {
                g[] gVarArr = new g[strArr.length];
                d dVar = new d();
                for (int i10 = 0; i10 < strArr.length; i10++) {
                    JsonUtf8Writer.string(dVar, strArr[i10]);
                    dVar.M();
                    gVarArr[i10] = dVar.e0();
                }
                return new Options((String[]) strArr.clone(), q.l(gVarArr));
            } catch (IOException e10) {
                throw new AssertionError(e10);
            }
        }

        public List<String> strings() {
            return Collections.unmodifiableList(Arrays.asList(this.strings));
        }
    }

    /* loaded from: classes2.dex */
    public enum Token {
        BEGIN_ARRAY,
        END_ARRAY,
        BEGIN_OBJECT,
        END_OBJECT,
        NAME,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL,
        END_DOCUMENT
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonReader() {
        this.scopes = new int[32];
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
    }

    @CheckReturnValue
    public static JsonReader of(BufferedSource bufferedSource) {
        return new JsonUtf8Reader(bufferedSource);
    }

    public abstract void beginArray();

    public abstract void beginObject();

    public abstract void endArray();

    public abstract void endObject();

    @CheckReturnValue
    public final boolean failOnUnknown() {
        return this.failOnUnknown;
    }

    @CheckReturnValue
    public final String getPath() {
        return JsonScope.getPath(this.stackSize, this.scopes, this.pathNames, this.pathIndices);
    }

    @CheckReturnValue
    public abstract boolean hasNext();

    @CheckReturnValue
    public final boolean isLenient() {
        return this.lenient;
    }

    public abstract boolean nextBoolean();

    public abstract double nextDouble();

    public abstract int nextInt();

    public abstract long nextLong();

    @CheckReturnValue
    public abstract String nextName();

    @Nullable
    public abstract <T> T nextNull();

    public abstract BufferedSource nextSource();

    public abstract String nextString();

    @CheckReturnValue
    public abstract Token peek();

    @CheckReturnValue
    public abstract JsonReader peekJson();

    public abstract void promoteNameToValue();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void pushScope(int i10) {
        int i11 = this.stackSize;
        int[] iArr = this.scopes;
        if (i11 == iArr.length) {
            if (i11 != 256) {
                this.scopes = Arrays.copyOf(iArr, iArr.length * 2);
                String[] strArr = this.pathNames;
                this.pathNames = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                int[] iArr2 = this.pathIndices;
                this.pathIndices = Arrays.copyOf(iArr2, iArr2.length * 2);
            } else {
                throw new JsonDataException("Nesting too deep at " + getPath());
            }
        }
        int[] iArr3 = this.scopes;
        int i12 = this.stackSize;
        this.stackSize = i12 + 1;
        iArr3[i12] = i10;
    }

    @Nullable
    public final Object readJsonValue() {
        switch (AnonymousClass1.$SwitchMap$com$squareup$moshi$JsonReader$Token[peek().ordinal()]) {
            case 1:
                ArrayList arrayList = new ArrayList();
                beginArray();
                while (hasNext()) {
                    arrayList.add(readJsonValue());
                }
                endArray();
                return arrayList;
            case 2:
                LinkedHashTreeMap linkedHashTreeMap = new LinkedHashTreeMap();
                beginObject();
                while (hasNext()) {
                    String nextName = nextName();
                    Object readJsonValue = readJsonValue();
                    Object put = linkedHashTreeMap.put(nextName, readJsonValue);
                    if (put != null) {
                        throw new JsonDataException("Map key '" + nextName + "' has multiple values at path " + getPath() + ": " + put + " and " + readJsonValue);
                    }
                }
                endObject();
                return linkedHashTreeMap;
            case 3:
                return nextString();
            case 4:
                return Double.valueOf(nextDouble());
            case 5:
                return Boolean.valueOf(nextBoolean());
            case 6:
                return nextNull();
            default:
                throw new IllegalStateException("Expected a value but was " + peek() + " at path " + getPath());
        }
    }

    @CheckReturnValue
    public abstract int selectName(Options options);

    @CheckReturnValue
    public abstract int selectString(Options options);

    public final void setFailOnUnknown(boolean z10) {
        this.failOnUnknown = z10;
    }

    public final void setLenient(boolean z10) {
        this.lenient = z10;
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

    public abstract void skipName();

    public abstract void skipValue();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final JsonEncodingException syntaxError(String str) {
        throw new JsonEncodingException(str + " at path " + getPath());
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public final JsonDataException typeMismatch(@Nullable Object obj, Object obj2) {
        if (obj == null) {
            return new JsonDataException("Expected " + obj2 + " but was null at path " + getPath());
        }
        return new JsonDataException("Expected " + obj2 + " but was " + obj + ", a " + obj.getClass().getName() + ", at path " + getPath());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JsonReader(JsonReader jsonReader) {
        this.stackSize = jsonReader.stackSize;
        this.scopes = (int[]) jsonReader.scopes.clone();
        this.pathNames = (String[]) jsonReader.pathNames.clone();
        this.pathIndices = (int[]) jsonReader.pathIndices.clone();
        this.lenient = jsonReader.lenient;
        this.failOnUnknown = jsonReader.failOnUnknown;
    }
}
