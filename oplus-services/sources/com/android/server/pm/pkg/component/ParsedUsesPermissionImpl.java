package com.android.server.pm.pkg.component;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
import com.android.internal.util.Parcelling;
import com.android.server.pm.pkg.component.ParsedUsesPermission;
import java.lang.annotation.Annotation;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedUsesPermissionImpl implements ParsedUsesPermission, Parcelable {
    public static final Parcelable.Creator<ParsedUsesPermissionImpl> CREATOR;
    static Parcelling<String> sParcellingForName;
    private String name;
    private int usesPermissionFlags;

    @Deprecated
    private void __metadata() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ParsedUsesPermissionImpl(String str, int i) {
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.usesPermissionFlags = i;
        AnnotationValidations.validate(ParsedUsesPermission.UsesPermissionFlags.class, (Annotation) null, i);
    }

    @Override // com.android.server.pm.pkg.component.ParsedUsesPermission
    public String getName() {
        return this.name;
    }

    @Override // com.android.server.pm.pkg.component.ParsedUsesPermission
    public int getUsesPermissionFlags() {
        return this.usesPermissionFlags;
    }

    public ParsedUsesPermissionImpl setName(String str) {
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        return this;
    }

    public ParsedUsesPermissionImpl setUsesPermissionFlags(int i) {
        this.usesPermissionFlags = i;
        AnnotationValidations.validate(ParsedUsesPermission.UsesPermissionFlags.class, (Annotation) null, i);
        return this;
    }

    static {
        Parcelling<String> parcelling = Parcelling.Cache.get(Parcelling.BuiltIn.ForInternedString.class);
        sParcellingForName = parcelling;
        if (parcelling == null) {
            sParcellingForName = Parcelling.Cache.put(new Parcelling.BuiltIn.ForInternedString());
        }
        CREATOR = new Parcelable.Creator<ParsedUsesPermissionImpl>() { // from class: com.android.server.pm.pkg.component.ParsedUsesPermissionImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParsedUsesPermissionImpl[] newArray(int i) {
                return new ParsedUsesPermissionImpl[i];
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParsedUsesPermissionImpl createFromParcel(Parcel parcel) {
                return new ParsedUsesPermissionImpl(parcel);
            }
        };
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        sParcellingForName.parcel(this.name, parcel, i);
        parcel.writeInt(this.usesPermissionFlags);
    }

    protected ParsedUsesPermissionImpl(Parcel parcel) {
        String str = (String) sParcellingForName.unparcel(parcel);
        int readInt = parcel.readInt();
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.usesPermissionFlags = readInt;
        AnnotationValidations.validate(ParsedUsesPermission.UsesPermissionFlags.class, (Annotation) null, readInt);
    }
}
