package d5;

import a5.BaseCardSource;
import a5.BaseKeyValueCache;
import a5.CardParamCache;
import a5.DSLCardMemSource;
import android.content.Context;
import gb.KClass;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import k5.CardStateEvent;
import kotlin.Metadata;
import ma.h;
import ma.j;
import o5.DataPackCompressor;
import o5.IDataCompress;
import r5.DataTranslator;
import r5.IClientFacade;
import r5.IDataHandle;
import ya.l;
import za.Lambda;
import za.Reflection;
import za.k;

/* compiled from: GlobalDIConfig.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\b"}, d2 = {"Ld5/a;", "", "Landroid/content/Context;", "context", "Lma/f0;", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: d5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class GlobalDIConfig {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f10713a;

    /* renamed from: b, reason: collision with root package name */
    public static final GlobalDIConfig f10714b = new GlobalDIConfig();

    /* compiled from: GlobalDIConfig.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lr5/f;", "a", "()Lr5/f;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: d5.a$a */
    /* loaded from: classes.dex */
    static final class a extends Lambda implements ya.a<IDataHandle> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f10715e = new a();

        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final IDataHandle invoke() {
            return new DataTranslator();
        }
    }

    /* compiled from: GlobalDIConfig.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lo5/b;", "a", "()Lo5/b;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: d5.a$b */
    /* loaded from: classes.dex */
    static final class b extends Lambda implements ya.a<IDataCompress> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f10716e = new b();

        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final IDataCompress invoke() {
            return new DataPackCompressor();
        }
    }

    /* compiled from: GlobalDIConfig.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0010 \n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0000H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"", "", "it", "Lr5/e;", "Lk5/b;", "a", "(Ljava/util/List;)Lr5/e;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: d5.a$c */
    /* loaded from: classes.dex */
    static final class c extends Lambda implements l<List<? extends Object>, IClientFacade<CardStateEvent>> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f10717e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final IClientFacade<CardStateEvent> invoke(List<? extends Object> list) {
            k.e(list, "it");
            return new r5.a();
        }
    }

    /* compiled from: GlobalDIConfig.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"La5/b;", "a", "()La5/b;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: d5.a$d */
    /* loaded from: classes.dex */
    static final class d extends Lambda implements ya.a<BaseKeyValueCache> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f10718e = new d();

        d() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final BaseKeyValueCache invoke() {
            return new CardParamCache();
        }
    }

    /* compiled from: GlobalDIConfig.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"La5/a;", "a", "()La5/a;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: d5.a$e */
    /* loaded from: classes.dex */
    static final class e extends Lambda implements ya.a<BaseCardSource> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f10719e = new e();

        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final BaseCardSource invoke() {
            return new DSLCardMemSource();
        }
    }

    private GlobalDIConfig() {
    }

    public final void a(Context context) {
        h<?> b10;
        h<?> b11;
        h<?> b12;
        h<?> b13;
        k.e(context, "context");
        if (f10713a) {
            return;
        }
        f10713a = true;
        s5.b bVar = s5.b.f18066c;
        bVar.h(context);
        bVar.g("GlobalDIConfig", "initial... ");
        v5.b bVar2 = v5.b.f19122c;
        a aVar = a.f10715e;
        if (bVar2.b().get(Reflection.b(IDataHandle.class)) == null) {
            ConcurrentHashMap<KClass<?>, h<?>> b14 = bVar2.b();
            KClass<?> b15 = Reflection.b(IDataHandle.class);
            b10 = j.b(new v5.a(aVar));
            b14.put(b15, b10);
            b bVar3 = b.f10716e;
            if (bVar2.b().get(Reflection.b(IDataCompress.class)) == null) {
                ConcurrentHashMap<KClass<?>, h<?>> b16 = bVar2.b();
                KClass<?> b17 = Reflection.b(IDataCompress.class);
                b11 = j.b(new v5.a(bVar3));
                b16.put(b17, b11);
                c cVar = c.f10717e;
                if (bVar2.a().get(Reflection.b(IClientFacade.class)) == null) {
                    bVar2.a().put(Reflection.b(IClientFacade.class), cVar);
                    d dVar = d.f10718e;
                    if (bVar2.b().get(Reflection.b(BaseKeyValueCache.class)) == null) {
                        ConcurrentHashMap<KClass<?>, h<?>> b18 = bVar2.b();
                        KClass<?> b19 = Reflection.b(BaseKeyValueCache.class);
                        b12 = j.b(new v5.a(dVar));
                        b18.put(b19, b12);
                        e eVar = e.f10719e;
                        if (bVar2.b().get(Reflection.b(BaseCardSource.class)) == null) {
                            ConcurrentHashMap<KClass<?>, h<?>> b20 = bVar2.b();
                            KClass<?> b21 = Reflection.b(BaseCardSource.class);
                            b13 = j.b(new v5.a(eVar));
                            b20.put(b21, b13);
                            return;
                        }
                        throw new IllegalStateException("Object of the same class type are injected");
                    }
                    throw new IllegalStateException("Object of the same class type are injected");
                }
                throw new IllegalStateException("Factory of the same class type are injected");
            }
            throw new IllegalStateException("Object of the same class type are injected");
        }
        throw new IllegalStateException("Object of the same class type are injected");
    }
}
