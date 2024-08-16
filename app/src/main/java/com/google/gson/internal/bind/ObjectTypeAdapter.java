package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class ObjectTypeAdapter extends TypeAdapter<Object> {
    private static final TypeAdapterFactory DOUBLE_FACTORY = newFactory(ToNumberPolicy.DOUBLE);
    private final Gson gson;
    private final ToNumberStrategy toNumberStrategy;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.gson.internal.bind.ObjectTypeAdapter$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$google$gson$stream$JsonToken;

        static {
            int[] iArr = new int[JsonToken.values().length];
            $SwitchMap$com$google$gson$stream$JsonToken = iArr;
            try {
                iArr[JsonToken.BEGIN_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public static TypeAdapterFactory getFactory(ToNumberStrategy toNumberStrategy) {
        if (toNumberStrategy == ToNumberPolicy.DOUBLE) {
            return DOUBLE_FACTORY;
        }
        return newFactory(toNumberStrategy);
    }

    private static TypeAdapterFactory newFactory(final ToNumberStrategy toNumberStrategy) {
        return new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.ObjectTypeAdapter.1
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Object.class) {
                    return new ObjectTypeAdapter(gson, ToNumberStrategy.this);
                }
                return null;
            }
        };
    }

    private Object readTerminal(JsonReader jsonReader, JsonToken jsonToken) {
        int i10 = AnonymousClass2.$SwitchMap$com$google$gson$stream$JsonToken[jsonToken.ordinal()];
        if (i10 == 3) {
            return jsonReader.nextString();
        }
        if (i10 == 4) {
            return this.toNumberStrategy.readNumber(jsonReader);
        }
        if (i10 == 5) {
            return Boolean.valueOf(jsonReader.nextBoolean());
        }
        if (i10 == 6) {
            jsonReader.nextNull();
            return null;
        }
        throw new IllegalStateException("Unexpected token: " + jsonToken);
    }

    private Object tryBeginNesting(JsonReader jsonReader, JsonToken jsonToken) {
        int i10 = AnonymousClass2.$SwitchMap$com$google$gson$stream$JsonToken[jsonToken.ordinal()];
        if (i10 == 1) {
            jsonReader.beginArray();
            return new ArrayList();
        }
        if (i10 != 2) {
            return null;
        }
        jsonReader.beginObject();
        return new LinkedTreeMap();
    }

    @Override // com.google.gson.TypeAdapter
    /* renamed from: read */
    public Object read2(JsonReader jsonReader) {
        JsonToken peek = jsonReader.peek();
        Object tryBeginNesting = tryBeginNesting(jsonReader, peek);
        if (tryBeginNesting == null) {
            return readTerminal(jsonReader, peek);
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        while (true) {
            if (jsonReader.hasNext()) {
                String nextName = tryBeginNesting instanceof Map ? jsonReader.nextName() : null;
                JsonToken peek2 = jsonReader.peek();
                Object tryBeginNesting2 = tryBeginNesting(jsonReader, peek2);
                boolean z10 = tryBeginNesting2 != null;
                if (tryBeginNesting2 == null) {
                    tryBeginNesting2 = readTerminal(jsonReader, peek2);
                }
                if (tryBeginNesting instanceof List) {
                    ((List) tryBeginNesting).add(tryBeginNesting2);
                } else {
                    ((Map) tryBeginNesting).put(nextName, tryBeginNesting2);
                }
                if (z10) {
                    arrayDeque.addLast(tryBeginNesting);
                    tryBeginNesting = tryBeginNesting2;
                }
            } else {
                if (tryBeginNesting instanceof List) {
                    jsonReader.endArray();
                } else {
                    jsonReader.endObject();
                }
                if (arrayDeque.isEmpty()) {
                    return tryBeginNesting;
                }
                tryBeginNesting = arrayDeque.removeLast();
            }
        }
    }

    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter jsonWriter, Object obj) {
        if (obj == null) {
            jsonWriter.nullValue();
            return;
        }
        TypeAdapter adapter = this.gson.getAdapter(obj.getClass());
        if (adapter instanceof ObjectTypeAdapter) {
            jsonWriter.beginObject();
            jsonWriter.endObject();
        } else {
            adapter.write(jsonWriter, obj);
        }
    }

    private ObjectTypeAdapter(Gson gson, ToNumberStrategy toNumberStrategy) {
        this.gson = gson;
        this.toNumberStrategy = toNumberStrategy;
    }
}
