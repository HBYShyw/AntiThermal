package androidx.loader.app;

import android.os.Bundle;
import android.util.Log;
import androidx.core.util.DebugUtils;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.h0;
import androidx.lifecycle.o;
import j.SparseArrayCompat;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import x.Loader;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LoaderManagerImpl.java */
/* renamed from: androidx.loader.app.b, reason: use source file name */
/* loaded from: classes.dex */
public class LoaderManagerImpl extends LoaderManager {

    /* renamed from: c, reason: collision with root package name */
    static boolean f3233c = false;

    /* renamed from: a, reason: collision with root package name */
    private final o f3234a;

    /* renamed from: b, reason: collision with root package name */
    private final b f3235b;

    /* compiled from: LoaderManagerImpl.java */
    /* renamed from: androidx.loader.app.b$a */
    /* loaded from: classes.dex */
    public static class a<D> extends MutableLiveData<D> {

        /* renamed from: l, reason: collision with root package name */
        private final int f3236l;

        /* renamed from: m, reason: collision with root package name */
        private final Bundle f3237m;

        /* renamed from: n, reason: collision with root package name */
        private o f3238n;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.lifecycle.LiveData
        public void i() {
            if (LoaderManagerImpl.f3233c) {
                Log.v("LoaderManager", "  Starting: " + this);
            }
            throw null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.lifecycle.LiveData
        public void j() {
            if (LoaderManagerImpl.f3233c) {
                Log.v("LoaderManager", "  Stopping: " + this);
            }
            throw null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.lifecycle.LiveData
        public void l(Observer<? super D> observer) {
            super.l(observer);
            this.f3238n = null;
        }

        @Override // androidx.lifecycle.MutableLiveData, androidx.lifecycle.LiveData
        public void m(D d10) {
            super.m(d10);
        }

        Loader<D> n(boolean z10) {
            if (LoaderManagerImpl.f3233c) {
                Log.v("LoaderManager", "  Destroying: " + this);
            }
            throw null;
        }

        public void o(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.print(str);
            printWriter.print("mId=");
            printWriter.print(this.f3236l);
            printWriter.print(" mArgs=");
            printWriter.println(this.f3237m);
            printWriter.print(str);
            printWriter.print("mLoader=");
            printWriter.println((Object) null);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append("  ");
            throw null;
        }

        void p() {
        }

        public String toString() {
            StringBuilder sb2 = new StringBuilder(64);
            sb2.append("LoaderInfo{");
            sb2.append(Integer.toHexString(System.identityHashCode(this)));
            sb2.append(" #");
            sb2.append(this.f3236l);
            sb2.append(" : ");
            DebugUtils.a(null, sb2);
            sb2.append("}}");
            return sb2.toString();
        }
    }

    /* compiled from: LoaderManagerImpl.java */
    /* renamed from: androidx.loader.app.b$b */
    /* loaded from: classes.dex */
    static class b extends ViewModel {

        /* renamed from: f, reason: collision with root package name */
        private static final h0.b f3239f = new a();

        /* renamed from: d, reason: collision with root package name */
        private SparseArrayCompat<a> f3240d = new SparseArrayCompat<>();

        /* renamed from: e, reason: collision with root package name */
        private boolean f3241e = false;

        /* compiled from: LoaderManagerImpl.java */
        /* renamed from: androidx.loader.app.b$b$a */
        /* loaded from: classes.dex */
        static class a implements h0.b {
            a() {
            }

            @Override // androidx.lifecycle.h0.b
            public <T extends ViewModel> T a(Class<T> cls) {
                return new b();
            }
        }

        b() {
        }

        static b g(ViewModelStore viewModelStore) {
            return (b) new h0(viewModelStore, f3239f).a(b.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.lifecycle.ViewModel
        public void d() {
            super.d();
            int k10 = this.f3240d.k();
            for (int i10 = 0; i10 < k10; i10++) {
                this.f3240d.l(i10).n(true);
            }
            this.f3240d.b();
        }

        public void f(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (this.f3240d.k() > 0) {
                printWriter.print(str);
                printWriter.println("Loaders:");
                String str2 = str + "    ";
                for (int i10 = 0; i10 < this.f3240d.k(); i10++) {
                    a l10 = this.f3240d.l(i10);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(this.f3240d.h(i10));
                    printWriter.print(": ");
                    printWriter.println(l10.toString());
                    l10.o(str2, fileDescriptor, printWriter, strArr);
                }
            }
        }

        void h() {
            int k10 = this.f3240d.k();
            for (int i10 = 0; i10 < k10; i10++) {
                this.f3240d.l(i10).p();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoaderManagerImpl(o oVar, ViewModelStore viewModelStore) {
        this.f3234a = oVar;
        this.f3235b = b.g(viewModelStore);
    }

    @Override // androidx.loader.app.LoaderManager
    @Deprecated
    public void a(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.f3235b.f(str, fileDescriptor, printWriter, strArr);
    }

    @Override // androidx.loader.app.LoaderManager
    public void c() {
        this.f3235b.h();
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("LoaderManager{");
        sb2.append(Integer.toHexString(System.identityHashCode(this)));
        sb2.append(" in ");
        DebugUtils.a(this.f3234a, sb2);
        sb2.append("}}");
        return sb2.toString();
    }
}
