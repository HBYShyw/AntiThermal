package com.google.android.play.core.splitinstall;

import com.oplus.oms.split.core.splitinstall.OplusSplitInstallRequest;
import java.util.ArrayList;
import java.util.List;

/* compiled from: SplitInstallRequest.java */
/* renamed from: com.google.android.play.core.splitinstall.d, reason: use source file name */
/* loaded from: classes.dex */
public class SplitInstallRequest extends OplusSplitInstallRequest {

    /* compiled from: SplitInstallRequest.java */
    /* renamed from: com.google.android.play.core.splitinstall.d$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public final List<String> f9569a = new ArrayList();

        public a b(String str) {
            this.f9569a.add(str);
            return this;
        }

        public SplitInstallRequest c() {
            return new SplitInstallRequest(this);
        }
    }

    public SplitInstallRequest(a aVar) {
        super(new ArrayList(aVar.f9569a));
    }

    public static a a() {
        return new a();
    }

    public List<String> getModuleNames() {
        return super.getModuleNames();
    }

    public String toString() {
        return "SplitInstallRequest{modulesNames=" + String.valueOf(getModuleNames()) + "}";
    }
}
