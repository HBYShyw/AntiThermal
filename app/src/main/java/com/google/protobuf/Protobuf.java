package com.google.protobuf;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* JADX INFO: Access modifiers changed from: package-private */
@CheckReturnValue
/* loaded from: classes.dex */
public final class Protobuf {
    private static final Protobuf INSTANCE = new Protobuf();
    private final ConcurrentMap<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap();
    private final SchemaFactory schemaFactory = new ManifestSchemaFactory();

    private Protobuf() {
    }

    public static Protobuf getInstance() {
        return INSTANCE;
    }

    int getTotalSchemaSize() {
        int i10 = 0;
        for (Schema<?> schema : this.schemaCache.values()) {
            if (schema instanceof MessageSchema) {
                i10 += ((MessageSchema) schema).getSchemaSize();
            }
        }
        return i10;
    }

    <T> boolean isInitialized(T t7) {
        return schemaFor((Protobuf) t7).isInitialized(t7);
    }

    public <T> void makeImmutable(T t7) {
        schemaFor((Protobuf) t7).makeImmutable(t7);
    }

    public <T> void mergeFrom(T t7, Reader reader) {
        mergeFrom(t7, reader, ExtensionRegistryLite.getEmptyRegistry());
    }

    public Schema<?> registerSchema(Class<?> cls, Schema<?> schema) {
        Internal.checkNotNull(cls, "messageType");
        Internal.checkNotNull(schema, "schema");
        return this.schemaCache.putIfAbsent(cls, schema);
    }

    @CanIgnoreReturnValue
    public Schema<?> registerSchemaOverride(Class<?> cls, Schema<?> schema) {
        Internal.checkNotNull(cls, "messageType");
        Internal.checkNotNull(schema, "schema");
        return this.schemaCache.put(cls, schema);
    }

    public <T> Schema<T> schemaFor(Class<T> cls) {
        Internal.checkNotNull(cls, "messageType");
        Schema<T> schema = (Schema) this.schemaCache.get(cls);
        if (schema != null) {
            return schema;
        }
        Schema<T> createSchema = this.schemaFactory.createSchema(cls);
        Schema<T> schema2 = (Schema<T>) registerSchema(cls, createSchema);
        return schema2 != null ? schema2 : createSchema;
    }

    public <T> void writeTo(T t7, Writer writer) {
        schemaFor((Protobuf) t7).writeTo(t7, writer);
    }

    public <T> void mergeFrom(T t7, Reader reader, ExtensionRegistryLite extensionRegistryLite) {
        schemaFor((Protobuf) t7).mergeFrom(t7, reader, extensionRegistryLite);
    }

    public <T> Schema<T> schemaFor(T t7) {
        return schemaFor((Class) t7.getClass());
    }
}
