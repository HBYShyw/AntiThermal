package androidx.lifecycle;

import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import b0.SavedStateRegistry;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import kotlin.Metadata;
import wd.StateFlow;
import za.DefaultConstructorMarker;

/* compiled from: SavedStateHandle.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0005\u0018\u0000 \t2\u00020\u0001:\u0001\fB\u001f\b\u0016\u0012\u0014\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0015¢\u0006\u0004\b\u0017\u0010\u0018B\t\b\u0016¢\u0006\u0004\b\u0017\u0010\u0019J\b\u0010\u0003\u001a\u00020\u0002H\u0007J(\u0010\t\u001a\u00020\b\"\u0004\b\u0000\u0010\u00042\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\u0007\u001a\u0004\u0018\u00018\u0000H\u0087\u0002¢\u0006\u0004\b\t\u0010\nR\"\u0010\u000e\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u000b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\f\u0010\rR \u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00020\u000b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000f\u0010\rR$\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\u000b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0011\u0010\rR(\u0010\u0014\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00130\u000b8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\r¨\u0006\u001a"}, d2 = {"Landroidx/lifecycle/a0;", "", "Lb0/b$c;", "d", "T", "", "key", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "f", "(Ljava/lang/String;Ljava/lang/Object;)V", "", "a", "Ljava/util/Map;", "regular", "b", "savedStateProviders", "c", "liveDatas", "Lwd/f;", "flows", "", "initialState", "<init>", "(Ljava/util/Map;)V", "()V", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
/* renamed from: androidx.lifecycle.a0, reason: use source file name */
/* loaded from: classes.dex */
public final class SavedStateHandle {

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: g, reason: collision with root package name */
    private static final Class<? extends Object>[] f3149g = {Boolean.TYPE, boolean[].class, Double.TYPE, double[].class, Integer.TYPE, int[].class, Long.TYPE, long[].class, String.class, String[].class, Binder.class, Bundle.class, Byte.TYPE, byte[].class, Character.TYPE, char[].class, CharSequence.class, CharSequence[].class, ArrayList.class, Float.TYPE, float[].class, Parcelable.class, Parcelable[].class, Serializable.class, Short.TYPE, short[].class, SparseArray.class, Size.class, SizeF.class};

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final Map<String, Object> regular;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final Map<String, SavedStateRegistry.c> savedStateProviders;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final Map<String, Object> liveDatas;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private final Map<String, StateFlow<Object>> flows;

    /* renamed from: e, reason: collision with root package name */
    private final SavedStateRegistry.c f3154e;

    /* compiled from: SavedStateHandle.kt */
    @Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u001c\u0010\u0006\u001a\u00020\u00052\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\b\u0010\u0004\u001a\u0004\u0018\u00010\u0002H\u0007J\u0012\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0007R$\u0010\f\u001a\u0012\u0012\u000e\u0012\f\u0012\u0006\b\u0001\u0012\u00020\u0001\u0018\u00010\u000b0\n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\f\u0010\rR\u0014\u0010\u000f\u001a\u00020\u000e8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u000e8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0011\u0010\u0010¨\u0006\u0014"}, d2 = {"Landroidx/lifecycle/a0$a;", "", "Landroid/os/Bundle;", "restoredState", "defaultState", "Landroidx/lifecycle/a0;", "a", ThermalBaseConfig.Item.ATTR_VALUE, "", "b", "", "Ljava/lang/Class;", "ACCEPTABLE_CLASSES", "[Ljava/lang/Class;", "", "KEYS", "Ljava/lang/String;", "VALUES", "<init>", "()V", "lifecycle-viewmodel-savedstate_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: androidx.lifecycle.a0$a, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final SavedStateHandle a(Bundle restoredState, Bundle defaultState) {
            if (restoredState == null) {
                if (defaultState == null) {
                    return new SavedStateHandle();
                }
                HashMap hashMap = new HashMap();
                for (String str : defaultState.keySet()) {
                    za.k.d(str, "key");
                    hashMap.put(str, defaultState.get(str));
                }
                return new SavedStateHandle(hashMap);
            }
            ArrayList parcelableArrayList = restoredState.getParcelableArrayList("keys");
            ArrayList parcelableArrayList2 = restoredState.getParcelableArrayList("values");
            if ((parcelableArrayList == null || parcelableArrayList2 == null || parcelableArrayList.size() != parcelableArrayList2.size()) ? false : true) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                int size = parcelableArrayList.size();
                for (int i10 = 0; i10 < size; i10++) {
                    Object obj = parcelableArrayList.get(i10);
                    Objects.requireNonNull(obj, "null cannot be cast to non-null type kotlin.String");
                    linkedHashMap.put((String) obj, parcelableArrayList2.get(i10));
                }
                return new SavedStateHandle(linkedHashMap);
            }
            throw new IllegalStateException("Invalid bundle passed as restored state".toString());
        }

        public final boolean b(Object value) {
            if (value == null) {
                return true;
            }
            for (Class cls : SavedStateHandle.f3149g) {
                za.k.b(cls);
                if (cls.isInstance(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    public SavedStateHandle(Map<String, ? extends Object> map) {
        za.k.e(map, "initialState");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.regular = linkedHashMap;
        this.savedStateProviders = new LinkedHashMap();
        this.liveDatas = new LinkedHashMap();
        this.flows = new LinkedHashMap();
        this.f3154e = new SavedStateRegistry.c() { // from class: androidx.lifecycle.z
            @Override // b0.SavedStateRegistry.c
            public final Bundle a() {
                Bundle e10;
                e10 = SavedStateHandle.e(SavedStateHandle.this);
                return e10;
            }
        };
        linkedHashMap.putAll(map);
    }

    public static final SavedStateHandle c(Bundle bundle, Bundle bundle2) {
        return INSTANCE.a(bundle, bundle2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Bundle e(SavedStateHandle savedStateHandle) {
        Map s7;
        za.k.e(savedStateHandle, "this$0");
        s7 = kotlin.collections.m0.s(savedStateHandle.savedStateProviders);
        for (Map.Entry entry : s7.entrySet()) {
            savedStateHandle.f((String) entry.getKey(), ((SavedStateRegistry.c) entry.getValue()).a());
        }
        Set<String> keySet = savedStateHandle.regular.keySet();
        ArrayList arrayList = new ArrayList(keySet.size());
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        for (String str : keySet) {
            arrayList.add(str);
            arrayList2.add(savedStateHandle.regular.get(str));
        }
        return androidx.core.os.d.a(ma.u.a("keys", arrayList), ma.u.a("values", arrayList2));
    }

    /* renamed from: d, reason: from getter */
    public final SavedStateRegistry.c getF3154e() {
        return this.f3154e;
    }

    public final <T> void f(String key, T value) {
        za.k.e(key, "key");
        if (INSTANCE.b(value)) {
            Object obj = this.liveDatas.get(key);
            MutableLiveData mutableLiveData = obj instanceof MutableLiveData ? (MutableLiveData) obj : null;
            if (mutableLiveData != null) {
                mutableLiveData.m(value);
            } else {
                this.regular.put(key, value);
            }
            StateFlow<Object> stateFlow = this.flows.get(key);
            if (stateFlow == null) {
                return;
            }
            stateFlow.setValue(value);
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Can't put value with type ");
        za.k.b(value);
        sb2.append(value.getClass());
        sb2.append(" into saved state");
        throw new IllegalArgumentException(sb2.toString());
    }

    public SavedStateHandle() {
        this.regular = new LinkedHashMap();
        this.savedStateProviders = new LinkedHashMap();
        this.liveDatas = new LinkedHashMap();
        this.flows = new LinkedHashMap();
        this.f3154e = new SavedStateRegistry.c() { // from class: androidx.lifecycle.z
            @Override // b0.SavedStateRegistry.c
            public final Bundle a() {
                Bundle e10;
                e10 = SavedStateHandle.e(SavedStateHandle.this);
                return e10;
            }
        };
    }
}
