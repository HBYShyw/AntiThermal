package androidx.versionedparcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import java.lang.reflect.Method;

/* compiled from: VersionedParcelParcel.java */
/* renamed from: androidx.versionedparcelable.b, reason: use source file name */
/* loaded from: classes.dex */
class VersionedParcelParcel extends VersionedParcel {

    /* renamed from: d, reason: collision with root package name */
    private final SparseIntArray f4173d;

    /* renamed from: e, reason: collision with root package name */
    private final Parcel f4174e;

    /* renamed from: f, reason: collision with root package name */
    private final int f4175f;

    /* renamed from: g, reason: collision with root package name */
    private final int f4176g;

    /* renamed from: h, reason: collision with root package name */
    private final String f4177h;

    /* renamed from: i, reason: collision with root package name */
    private int f4178i;

    /* renamed from: j, reason: collision with root package name */
    private int f4179j;

    /* renamed from: k, reason: collision with root package name */
    private int f4180k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionedParcelParcel(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), "", new j.a(), new j.a(), new j.a());
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void A(byte[] bArr) {
        if (bArr != null) {
            this.f4174e.writeInt(bArr.length);
            this.f4174e.writeByteArray(bArr);
        } else {
            this.f4174e.writeInt(-1);
        }
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected void C(CharSequence charSequence) {
        TextUtils.writeToParcel(charSequence, this.f4174e, 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void E(int i10) {
        this.f4174e.writeInt(i10);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void G(Parcelable parcelable) {
        this.f4174e.writeParcelable(parcelable, 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void I(String str) {
        this.f4174e.writeString(str);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void a() {
        int i10 = this.f4178i;
        if (i10 >= 0) {
            int i11 = this.f4173d.get(i10);
            int dataPosition = this.f4174e.dataPosition();
            this.f4174e.setDataPosition(i11);
            this.f4174e.writeInt(dataPosition - i11);
            this.f4174e.setDataPosition(dataPosition);
        }
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected VersionedParcel b() {
        Parcel parcel = this.f4174e;
        int dataPosition = parcel.dataPosition();
        int i10 = this.f4179j;
        if (i10 == this.f4175f) {
            i10 = this.f4176g;
        }
        return new VersionedParcelParcel(parcel, dataPosition, i10, this.f4177h + "  ", this.f4170a, this.f4171b, this.f4172c);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean g() {
        return this.f4174e.readInt() != 0;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public byte[] i() {
        int readInt = this.f4174e.readInt();
        if (readInt < 0) {
            return null;
        }
        byte[] bArr = new byte[readInt];
        this.f4174e.readByteArray(bArr);
        return bArr;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected CharSequence k() {
        return (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.f4174e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean m(int i10) {
        while (this.f4179j < this.f4176g) {
            int i11 = this.f4180k;
            if (i11 == i10) {
                return true;
            }
            if (String.valueOf(i11).compareTo(String.valueOf(i10)) > 0) {
                return false;
            }
            this.f4174e.setDataPosition(this.f4179j);
            int readInt = this.f4174e.readInt();
            this.f4180k = this.f4174e.readInt();
            this.f4179j += readInt;
        }
        return this.f4180k == i10;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public int o() {
        return this.f4174e.readInt();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public <T extends Parcelable> T q() {
        return (T) this.f4174e.readParcelable(getClass().getClassLoader());
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public String s() {
        return this.f4174e.readString();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void w(int i10) {
        a();
        this.f4178i = i10;
        this.f4173d.put(i10, this.f4174e.dataPosition());
        E(0);
        E(i10);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void y(boolean z10) {
        this.f4174e.writeInt(z10 ? 1 : 0);
    }

    private VersionedParcelParcel(Parcel parcel, int i10, int i11, String str, j.a<String, Method> aVar, j.a<String, Method> aVar2, j.a<String, Class> aVar3) {
        super(aVar, aVar2, aVar3);
        this.f4173d = new SparseIntArray();
        this.f4178i = -1;
        this.f4180k = -1;
        this.f4174e = parcel;
        this.f4175f = i10;
        this.f4176g = i11;
        this.f4179j = i10;
        this.f4177h = str;
    }
}
