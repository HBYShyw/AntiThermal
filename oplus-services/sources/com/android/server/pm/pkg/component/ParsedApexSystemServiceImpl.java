package com.android.server.pm.pkg.component;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
import com.android.internal.util.Parcelling;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedApexSystemServiceImpl implements ParsedApexSystemService, Parcelable {
    public static final Parcelable.Creator<ParsedApexSystemServiceImpl> CREATOR;
    static Parcelling<String> sParcellingForJarPath;
    static Parcelling<String> sParcellingForMaxSdkVersion;
    static Parcelling<String> sParcellingForMinSdkVersion;
    static Parcelling<String> sParcellingForName;
    private int initOrder;
    private String jarPath;
    private String maxSdkVersion;
    private String minSdkVersion;
    private String name;

    @Deprecated
    private void __metadata() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ParsedApexSystemServiceImpl() {
    }

    public ParsedApexSystemServiceImpl(String str, String str2, String str3, String str4, int i) {
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.jarPath = str2;
        this.minSdkVersion = str3;
        this.maxSdkVersion = str4;
        this.initOrder = i;
    }

    @Override // com.android.server.pm.pkg.component.ParsedApexSystemService
    public String getName() {
        return this.name;
    }

    @Override // com.android.server.pm.pkg.component.ParsedApexSystemService
    public String getJarPath() {
        return this.jarPath;
    }

    @Override // com.android.server.pm.pkg.component.ParsedApexSystemService
    public String getMinSdkVersion() {
        return this.minSdkVersion;
    }

    @Override // com.android.server.pm.pkg.component.ParsedApexSystemService
    public String getMaxSdkVersion() {
        return this.maxSdkVersion;
    }

    @Override // com.android.server.pm.pkg.component.ParsedApexSystemService
    public int getInitOrder() {
        return this.initOrder;
    }

    public ParsedApexSystemServiceImpl setName(String str) {
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        return this;
    }

    public ParsedApexSystemServiceImpl setJarPath(String str) {
        this.jarPath = str;
        return this;
    }

    public ParsedApexSystemServiceImpl setMinSdkVersion(String str) {
        this.minSdkVersion = str;
        return this;
    }

    public ParsedApexSystemServiceImpl setMaxSdkVersion(String str) {
        this.maxSdkVersion = str;
        return this;
    }

    public ParsedApexSystemServiceImpl setInitOrder(int i) {
        this.initOrder = i;
        return this;
    }

    static {
        Parcelling<String> parcelling = Parcelling.Cache.get(Parcelling.BuiltIn.ForInternedString.class);
        sParcellingForName = parcelling;
        if (parcelling == null) {
            sParcellingForName = Parcelling.Cache.put(new Parcelling.BuiltIn.ForInternedString());
        }
        Parcelling<String> parcelling2 = Parcelling.Cache.get(Parcelling.BuiltIn.ForInternedString.class);
        sParcellingForJarPath = parcelling2;
        if (parcelling2 == null) {
            sParcellingForJarPath = Parcelling.Cache.put(new Parcelling.BuiltIn.ForInternedString());
        }
        Parcelling<String> parcelling3 = Parcelling.Cache.get(Parcelling.BuiltIn.ForInternedString.class);
        sParcellingForMinSdkVersion = parcelling3;
        if (parcelling3 == null) {
            sParcellingForMinSdkVersion = Parcelling.Cache.put(new Parcelling.BuiltIn.ForInternedString());
        }
        Parcelling<String> parcelling4 = Parcelling.Cache.get(Parcelling.BuiltIn.ForInternedString.class);
        sParcellingForMaxSdkVersion = parcelling4;
        if (parcelling4 == null) {
            sParcellingForMaxSdkVersion = Parcelling.Cache.put(new Parcelling.BuiltIn.ForInternedString());
        }
        CREATOR = new Parcelable.Creator<ParsedApexSystemServiceImpl>() { // from class: com.android.server.pm.pkg.component.ParsedApexSystemServiceImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParsedApexSystemServiceImpl[] newArray(int i) {
                return new ParsedApexSystemServiceImpl[i];
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParsedApexSystemServiceImpl createFromParcel(Parcel parcel) {
                return new ParsedApexSystemServiceImpl(parcel);
            }
        };
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        byte b = this.jarPath != null ? (byte) 2 : (byte) 0;
        if (this.minSdkVersion != null) {
            b = (byte) (b | 4);
        }
        if (this.maxSdkVersion != null) {
            b = (byte) (b | 8);
        }
        parcel.writeByte(b);
        sParcellingForName.parcel(this.name, parcel, i);
        sParcellingForJarPath.parcel(this.jarPath, parcel, i);
        sParcellingForMinSdkVersion.parcel(this.minSdkVersion, parcel, i);
        sParcellingForMaxSdkVersion.parcel(this.maxSdkVersion, parcel, i);
        parcel.writeInt(this.initOrder);
    }

    protected ParsedApexSystemServiceImpl(Parcel parcel) {
        parcel.readByte();
        String str = (String) sParcellingForName.unparcel(parcel);
        String str2 = (String) sParcellingForJarPath.unparcel(parcel);
        String str3 = (String) sParcellingForMinSdkVersion.unparcel(parcel);
        String str4 = (String) sParcellingForMaxSdkVersion.unparcel(parcel);
        int readInt = parcel.readInt();
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.jarPath = str2;
        this.minSdkVersion = str3;
        this.maxSdkVersion = str4;
        this.initOrder = readInt;
    }
}
