package android.view.result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public final class IntentSenderRequest implements Parcelable {
    public static final Parcelable.Creator<IntentSenderRequest> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final IntentSender f312e;

    /* renamed from: f, reason: collision with root package name */
    private final Intent f313f;

    /* renamed from: g, reason: collision with root package name */
    private final int f314g;

    /* renamed from: h, reason: collision with root package name */
    private final int f315h;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<IntentSenderRequest> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public IntentSenderRequest createFromParcel(Parcel parcel) {
            return new IntentSenderRequest(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public IntentSenderRequest[] newArray(int i10) {
            return new IntentSenderRequest[i10];
        }
    }

    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private IntentSender f316a;

        /* renamed from: b, reason: collision with root package name */
        private Intent f317b;

        /* renamed from: c, reason: collision with root package name */
        private int f318c;

        /* renamed from: d, reason: collision with root package name */
        private int f319d;

        public b(IntentSender intentSender) {
            this.f316a = intentSender;
        }

        public IntentSenderRequest a() {
            return new IntentSenderRequest(this.f316a, this.f317b, this.f318c, this.f319d);
        }

        public b b(Intent intent) {
            this.f317b = intent;
            return this;
        }

        public b c(int i10, int i11) {
            this.f319d = i10;
            this.f318c = i11;
            return this;
        }
    }

    IntentSenderRequest(IntentSender intentSender, Intent intent, int i10, int i11) {
        this.f312e = intentSender;
        this.f313f = intent;
        this.f314g = i10;
        this.f315h = i11;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Intent j() {
        return this.f313f;
    }

    public int k() {
        return this.f314g;
    }

    public int l() {
        return this.f315h;
    }

    public IntentSender m() {
        return this.f312e;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeParcelable(this.f312e, i10);
        parcel.writeParcelable(this.f313f, i10);
        parcel.writeInt(this.f314g);
        parcel.writeInt(this.f315h);
    }

    IntentSenderRequest(Parcel parcel) {
        this.f312e = (IntentSender) parcel.readParcelable(IntentSender.class.getClassLoader());
        this.f313f = (Intent) parcel.readParcelable(Intent.class.getClassLoader());
        this.f314g = parcel.readInt();
        this.f315h = parcel.readInt();
    }
}
