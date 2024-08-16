package v5;

import gb.KClass;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import ma.h;
import ya.l;

/* compiled from: ClientDI.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000e\u0010\u000fR+\u0010\u0005\u001a\u0016\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0003\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00040\u00028\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR?\u0010\f\u001a*\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0003\u0012\u001c\u0012\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\n\u0012\u0002\b\u00030\tj\u0006\u0012\u0002\b\u0003`\u000b0\u00028\u0006¢\u0006\f\n\u0004\b\f\u0010\u0006\u001a\u0004\b\r\u0010\b¨\u0006\u0010"}, d2 = {"Lv5/b;", "", "Ljava/util/concurrent/ConcurrentHashMap;", "Lgb/d;", "Lma/h;", "singleInstanceMap", "Ljava/util/concurrent/ConcurrentHashMap;", "b", "()Ljava/util/concurrent/ConcurrentHashMap;", "Lkotlin/Function1;", "", "Lcom/oplus/channel/client/utils/Factory;", "factoryInstanceMap", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public final class b {

    /* renamed from: c, reason: collision with root package name */
    public static final b f19122c = new b();

    /* renamed from: a, reason: collision with root package name */
    private static final ConcurrentHashMap<KClass<?>, h<?>> f19120a = new ConcurrentHashMap<>();

    /* renamed from: b, reason: collision with root package name */
    private static final ConcurrentHashMap<KClass<?>, l<List<? extends Object>, ?>> f19121b = new ConcurrentHashMap<>();

    private b() {
    }

    public final ConcurrentHashMap<KClass<?>, l<List<? extends Object>, ?>> a() {
        return f19121b;
    }

    public final ConcurrentHashMap<KClass<?>, h<?>> b() {
        return f19120a;
    }
}
