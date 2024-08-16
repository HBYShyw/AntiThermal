package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public final class FragmentManagerState implements Parcelable {
    public static final Parcelable.Creator<FragmentManagerState> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    ArrayList<FragmentState> f2780e;

    /* renamed from: f, reason: collision with root package name */
    ArrayList<String> f2781f;

    /* renamed from: g, reason: collision with root package name */
    BackStackState[] f2782g;

    /* renamed from: h, reason: collision with root package name */
    int f2783h;

    /* renamed from: i, reason: collision with root package name */
    String f2784i;

    /* renamed from: j, reason: collision with root package name */
    ArrayList<String> f2785j;

    /* renamed from: k, reason: collision with root package name */
    ArrayList<Bundle> f2786k;

    /* renamed from: l, reason: collision with root package name */
    ArrayList<FragmentManager.LaunchedFragmentInfo> f2787l;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<FragmentManagerState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public FragmentManagerState createFromParcel(Parcel parcel) {
            return new FragmentManagerState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public FragmentManagerState[] newArray(int i10) {
            return new FragmentManagerState[i10];
        }
    }

    public FragmentManagerState() {
        this.f2784i = null;
        this.f2785j = new ArrayList<>();
        this.f2786k = new ArrayList<>();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeTypedList(this.f2780e);
        parcel.writeStringList(this.f2781f);
        parcel.writeTypedArray(this.f2782g, i10);
        parcel.writeInt(this.f2783h);
        parcel.writeString(this.f2784i);
        parcel.writeStringList(this.f2785j);
        parcel.writeTypedList(this.f2786k);
        parcel.writeTypedList(this.f2787l);
    }

    public FragmentManagerState(Parcel parcel) {
        this.f2784i = null;
        this.f2785j = new ArrayList<>();
        this.f2786k = new ArrayList<>();
        this.f2780e = parcel.createTypedArrayList(FragmentState.CREATOR);
        this.f2781f = parcel.createStringArrayList();
        this.f2782g = (BackStackState[]) parcel.createTypedArray(BackStackState.CREATOR);
        this.f2783h = parcel.readInt();
        this.f2784i = parcel.readString();
        this.f2785j = parcel.createStringArrayList();
        this.f2786k = parcel.createTypedArrayList(Bundle.CREATOR);
        this.f2787l = parcel.createTypedArrayList(FragmentManager.LaunchedFragmentInfo.CREATOR);
    }
}
