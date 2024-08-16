package yc;

import fd.StorageManager;
import gd.o0;
import pb.ClassDescriptor;
import za.k;

/* compiled from: SamConversionResolverImpl.kt */
/* renamed from: yc.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class SamConversionResolverImpl implements SamConversionResolver {

    /* renamed from: a, reason: collision with root package name */
    private final Iterable<Object> f20161a;

    /* renamed from: b, reason: collision with root package name */
    private final fd.b<ClassDescriptor, o0> f20162b;

    public SamConversionResolverImpl(StorageManager storageManager, Iterable<? extends Object> iterable) {
        k.e(storageManager, "storageManager");
        k.e(iterable, "samWithReceiverResolvers");
        this.f20161a = iterable;
        this.f20162b = storageManager.c();
    }
}
