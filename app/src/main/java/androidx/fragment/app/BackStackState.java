package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.h;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public final class BackStackState implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    final int[] f2665e;

    /* renamed from: f, reason: collision with root package name */
    final ArrayList<String> f2666f;

    /* renamed from: g, reason: collision with root package name */
    final int[] f2667g;

    /* renamed from: h, reason: collision with root package name */
    final int[] f2668h;

    /* renamed from: i, reason: collision with root package name */
    final int f2669i;

    /* renamed from: j, reason: collision with root package name */
    final String f2670j;

    /* renamed from: k, reason: collision with root package name */
    final int f2671k;

    /* renamed from: l, reason: collision with root package name */
    final int f2672l;

    /* renamed from: m, reason: collision with root package name */
    final CharSequence f2673m;

    /* renamed from: n, reason: collision with root package name */
    final int f2674n;

    /* renamed from: o, reason: collision with root package name */
    final CharSequence f2675o;

    /* renamed from: p, reason: collision with root package name */
    final ArrayList<String> f2676p;

    /* renamed from: q, reason: collision with root package name */
    final ArrayList<String> f2677q;

    /* renamed from: r, reason: collision with root package name */
    final boolean f2678r;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<BackStackState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public BackStackState[] newArray(int i10) {
            return new BackStackState[i10];
        }
    }

    public BackStackState(BackStackRecord backStackRecord) {
        int size = backStackRecord.f2940c.size();
        this.f2665e = new int[size * 5];
        if (backStackRecord.f2946i) {
            this.f2666f = new ArrayList<>(size);
            this.f2667g = new int[size];
            this.f2668h = new int[size];
            int i10 = 0;
            int i11 = 0;
            while (i10 < size) {
                FragmentTransaction.a aVar = backStackRecord.f2940c.get(i10);
                int i12 = i11 + 1;
                this.f2665e[i11] = aVar.f2957a;
                ArrayList<String> arrayList = this.f2666f;
                Fragment fragment = aVar.f2958b;
                arrayList.add(fragment != null ? fragment.mWho : null);
                int[] iArr = this.f2665e;
                int i13 = i12 + 1;
                iArr[i12] = aVar.f2959c;
                int i14 = i13 + 1;
                iArr[i13] = aVar.f2960d;
                int i15 = i14 + 1;
                iArr[i14] = aVar.f2961e;
                iArr[i15] = aVar.f2962f;
                this.f2667g[i10] = aVar.f2963g.ordinal();
                this.f2668h[i10] = aVar.f2964h.ordinal();
                i10++;
                i11 = i15 + 1;
            }
            this.f2669i = backStackRecord.f2945h;
            this.f2670j = backStackRecord.f2948k;
            this.f2671k = backStackRecord.f2815v;
            this.f2672l = backStackRecord.f2949l;
            this.f2673m = backStackRecord.f2950m;
            this.f2674n = backStackRecord.f2951n;
            this.f2675o = backStackRecord.f2952o;
            this.f2676p = backStackRecord.f2953p;
            this.f2677q = backStackRecord.f2954q;
            this.f2678r = backStackRecord.f2955r;
            return;
        }
        throw new IllegalStateException("Not on back stack");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BackStackRecord j(FragmentManager fragmentManager) {
        BackStackRecord backStackRecord = new BackStackRecord(fragmentManager);
        int i10 = 0;
        int i11 = 0;
        while (i10 < this.f2665e.length) {
            FragmentTransaction.a aVar = new FragmentTransaction.a();
            int i12 = i10 + 1;
            aVar.f2957a = this.f2665e[i10];
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Instantiate " + backStackRecord + " op #" + i11 + " base fragment #" + this.f2665e[i12]);
            }
            String str = this.f2666f.get(i11);
            if (str != null) {
                aVar.f2958b = fragmentManager.h0(str);
            } else {
                aVar.f2958b = null;
            }
            aVar.f2963g = h.c.values()[this.f2667g[i11]];
            aVar.f2964h = h.c.values()[this.f2668h[i11]];
            int[] iArr = this.f2665e;
            int i13 = i12 + 1;
            int i14 = iArr[i12];
            aVar.f2959c = i14;
            int i15 = i13 + 1;
            int i16 = iArr[i13];
            aVar.f2960d = i16;
            int i17 = i15 + 1;
            int i18 = iArr[i15];
            aVar.f2961e = i18;
            int i19 = iArr[i17];
            aVar.f2962f = i19;
            backStackRecord.f2941d = i14;
            backStackRecord.f2942e = i16;
            backStackRecord.f2943f = i18;
            backStackRecord.f2944g = i19;
            backStackRecord.f(aVar);
            i11++;
            i10 = i17 + 1;
        }
        backStackRecord.f2945h = this.f2669i;
        backStackRecord.f2948k = this.f2670j;
        backStackRecord.f2815v = this.f2671k;
        backStackRecord.f2946i = true;
        backStackRecord.f2949l = this.f2672l;
        backStackRecord.f2950m = this.f2673m;
        backStackRecord.f2951n = this.f2674n;
        backStackRecord.f2952o = this.f2675o;
        backStackRecord.f2953p = this.f2676p;
        backStackRecord.f2954q = this.f2677q;
        backStackRecord.f2955r = this.f2678r;
        backStackRecord.v(1);
        return backStackRecord;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeIntArray(this.f2665e);
        parcel.writeStringList(this.f2666f);
        parcel.writeIntArray(this.f2667g);
        parcel.writeIntArray(this.f2668h);
        parcel.writeInt(this.f2669i);
        parcel.writeString(this.f2670j);
        parcel.writeInt(this.f2671k);
        parcel.writeInt(this.f2672l);
        TextUtils.writeToParcel(this.f2673m, parcel, 0);
        parcel.writeInt(this.f2674n);
        TextUtils.writeToParcel(this.f2675o, parcel, 0);
        parcel.writeStringList(this.f2676p);
        parcel.writeStringList(this.f2677q);
        parcel.writeInt(this.f2678r ? 1 : 0);
    }

    public BackStackState(Parcel parcel) {
        this.f2665e = parcel.createIntArray();
        this.f2666f = parcel.createStringArrayList();
        this.f2667g = parcel.createIntArray();
        this.f2668h = parcel.createIntArray();
        this.f2669i = parcel.readInt();
        this.f2670j = parcel.readString();
        this.f2671k = parcel.readInt();
        this.f2672l = parcel.readInt();
        this.f2673m = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f2674n = parcel.readInt();
        this.f2675o = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f2676p = parcel.createStringArrayList();
        this.f2677q = parcel.createStringArrayList();
        this.f2678r = parcel.readInt() != 0;
    }
}
