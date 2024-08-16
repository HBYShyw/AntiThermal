package com.android.server.pm.pkg.component;

import android.annotation.NonNull;
import android.annotation.StringRes;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedAttributionImpl implements ParsedAttribution, Parcelable {
    public static final Parcelable.Creator<ParsedAttributionImpl> CREATOR = new Parcelable.Creator<ParsedAttributionImpl>() { // from class: com.android.server.pm.pkg.component.ParsedAttributionImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParsedAttributionImpl[] newArray(int i) {
            return new ParsedAttributionImpl[i];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParsedAttributionImpl createFromParcel(Parcel parcel) {
            return new ParsedAttributionImpl(parcel);
        }
    };
    static final int MAX_NUM_ATTRIBUTIONS = 1000;
    private List<String> inheritFrom;
    private int label;
    private String tag;

    @Deprecated
    private void __metadata() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ParsedAttributionImpl() {
    }

    public ParsedAttributionImpl(String str, int i, List<String> list) {
        this.tag = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.label = i;
        AnnotationValidations.validate(StringRes.class, (Annotation) null, i);
        this.inheritFrom = list;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, list);
    }

    @Override // com.android.server.pm.pkg.component.ParsedAttribution
    public String getTag() {
        return this.tag;
    }

    @Override // com.android.server.pm.pkg.component.ParsedAttribution
    public int getLabel() {
        return this.label;
    }

    @Override // com.android.server.pm.pkg.component.ParsedAttribution
    public List<String> getInheritFrom() {
        return this.inheritFrom;
    }

    public ParsedAttributionImpl setTag(String str) {
        this.tag = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        return this;
    }

    public ParsedAttributionImpl setLabel(int i) {
        this.label = i;
        AnnotationValidations.validate(StringRes.class, (Annotation) null, i);
        return this;
    }

    public ParsedAttributionImpl setInheritFrom(List<String> list) {
        this.inheritFrom = list;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, list);
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tag);
        parcel.writeInt(this.label);
        parcel.writeStringList(this.inheritFrom);
    }

    protected ParsedAttributionImpl(Parcel parcel) {
        String readString = parcel.readString();
        int readInt = parcel.readInt();
        ArrayList arrayList = new ArrayList();
        parcel.readStringList(arrayList);
        this.tag = readString;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, readString);
        this.label = readInt;
        AnnotationValidations.validate(StringRes.class, (Annotation) null, readInt);
        this.inheritFrom = arrayList;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, arrayList);
    }
}
