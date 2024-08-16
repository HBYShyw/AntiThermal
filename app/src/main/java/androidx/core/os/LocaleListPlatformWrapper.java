package androidx.core.os;

import android.os.LocaleList;
import java.util.Locale;

/* compiled from: LocaleListPlatformWrapper.java */
/* renamed from: androidx.core.os.h, reason: use source file name */
/* loaded from: classes.dex */
final class LocaleListPlatformWrapper implements LocaleListInterface {

    /* renamed from: a, reason: collision with root package name */
    private final LocaleList f2218a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocaleListPlatformWrapper(Object obj) {
        this.f2218a = (LocaleList) obj;
    }

    @Override // androidx.core.os.LocaleListInterface
    public String a() {
        return this.f2218a.toLanguageTags();
    }

    @Override // androidx.core.os.LocaleListInterface
    public Object b() {
        return this.f2218a;
    }

    public boolean equals(Object obj) {
        return this.f2218a.equals(((LocaleListInterface) obj).b());
    }

    @Override // androidx.core.os.LocaleListInterface
    public Locale get(int i10) {
        return this.f2218a.get(i10);
    }

    public int hashCode() {
        return this.f2218a.hashCode();
    }

    @Override // androidx.core.os.LocaleListInterface
    public boolean isEmpty() {
        return this.f2218a.isEmpty();
    }

    @Override // androidx.core.os.LocaleListInterface
    public int size() {
        return this.f2218a.size();
    }

    public String toString() {
        return this.f2218a.toString();
    }
}
