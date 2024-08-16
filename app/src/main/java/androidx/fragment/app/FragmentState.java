package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public final class FragmentState implements Parcelable {
    public static final Parcelable.Creator<FragmentState> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    final String f2788e;

    /* renamed from: f, reason: collision with root package name */
    final String f2789f;

    /* renamed from: g, reason: collision with root package name */
    final boolean f2790g;

    /* renamed from: h, reason: collision with root package name */
    final int f2791h;

    /* renamed from: i, reason: collision with root package name */
    final int f2792i;

    /* renamed from: j, reason: collision with root package name */
    final String f2793j;

    /* renamed from: k, reason: collision with root package name */
    final boolean f2794k;

    /* renamed from: l, reason: collision with root package name */
    final boolean f2795l;

    /* renamed from: m, reason: collision with root package name */
    final boolean f2796m;

    /* renamed from: n, reason: collision with root package name */
    final Bundle f2797n;

    /* renamed from: o, reason: collision with root package name */
    final boolean f2798o;

    /* renamed from: p, reason: collision with root package name */
    final int f2799p;

    /* renamed from: q, reason: collision with root package name */
    Bundle f2800q;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<FragmentState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public FragmentState createFromParcel(Parcel parcel) {
            return new FragmentState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public FragmentState[] newArray(int i10) {
            return new FragmentState[i10];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentState(Fragment fragment) {
        this.f2788e = fragment.getClass().getName();
        this.f2789f = fragment.mWho;
        this.f2790g = fragment.mFromLayout;
        this.f2791h = fragment.mFragmentId;
        this.f2792i = fragment.mContainerId;
        this.f2793j = fragment.mTag;
        this.f2794k = fragment.mRetainInstance;
        this.f2795l = fragment.mRemoving;
        this.f2796m = fragment.mDetached;
        this.f2797n = fragment.mArguments;
        this.f2798o = fragment.mHidden;
        this.f2799p = fragment.mMaxState.ordinal();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("FragmentState{");
        sb2.append(this.f2788e);
        sb2.append(" (");
        sb2.append(this.f2789f);
        sb2.append(")}:");
        if (this.f2790g) {
            sb2.append(" fromLayout");
        }
        if (this.f2792i != 0) {
            sb2.append(" id=0x");
            sb2.append(Integer.toHexString(this.f2792i));
        }
        String str = this.f2793j;
        if (str != null && !str.isEmpty()) {
            sb2.append(" tag=");
            sb2.append(this.f2793j);
        }
        if (this.f2794k) {
            sb2.append(" retainInstance");
        }
        if (this.f2795l) {
            sb2.append(" removing");
        }
        if (this.f2796m) {
            sb2.append(" detached");
        }
        if (this.f2798o) {
            sb2.append(" hidden");
        }
        return sb2.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.f2788e);
        parcel.writeString(this.f2789f);
        parcel.writeInt(this.f2790g ? 1 : 0);
        parcel.writeInt(this.f2791h);
        parcel.writeInt(this.f2792i);
        parcel.writeString(this.f2793j);
        parcel.writeInt(this.f2794k ? 1 : 0);
        parcel.writeInt(this.f2795l ? 1 : 0);
        parcel.writeInt(this.f2796m ? 1 : 0);
        parcel.writeBundle(this.f2797n);
        parcel.writeInt(this.f2798o ? 1 : 0);
        parcel.writeBundle(this.f2800q);
        parcel.writeInt(this.f2799p);
    }

    FragmentState(Parcel parcel) {
        this.f2788e = parcel.readString();
        this.f2789f = parcel.readString();
        this.f2790g = parcel.readInt() != 0;
        this.f2791h = parcel.readInt();
        this.f2792i = parcel.readInt();
        this.f2793j = parcel.readString();
        this.f2794k = parcel.readInt() != 0;
        this.f2795l = parcel.readInt() != 0;
        this.f2796m = parcel.readInt() != 0;
        this.f2797n = parcel.readBundle();
        this.f2798o = parcel.readInt() != 0;
        this.f2800q = parcel.readBundle();
        this.f2799p = parcel.readInt();
    }
}
