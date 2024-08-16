package com.android.server.pm.pkg.component;

import android.annotation.NonNull;
import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AnnotationValidations;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.Parcelling;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ParsedProcessImpl implements ParsedProcess, Parcelable {
    public static final Parcelable.Creator<ParsedProcessImpl> CREATOR;
    static Parcelling<Set<String>> sParcellingForDeniedPermissions;
    private ArrayMap<String, String> appClassNamesByPackage;
    private Set<String> deniedPermissions;
    private int gwpAsanMode;
    private int memtagMode;
    private String name;
    private int nativeHeapZeroInitialized;

    @Deprecated
    private void __metadata() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ParsedProcessImpl() {
        this.appClassNamesByPackage = ArrayMap.EMPTY;
        this.deniedPermissions = Collections.emptySet();
        this.gwpAsanMode = -1;
        this.memtagMode = -1;
        this.nativeHeapZeroInitialized = -1;
    }

    public ParsedProcessImpl(ParsedProcess parsedProcess) {
        this.appClassNamesByPackage = ArrayMap.EMPTY;
        this.deniedPermissions = Collections.emptySet();
        this.gwpAsanMode = -1;
        this.memtagMode = -1;
        this.nativeHeapZeroInitialized = -1;
        this.name = parsedProcess.getName();
        this.appClassNamesByPackage = parsedProcess.getAppClassNamesByPackage().size() == 0 ? ArrayMap.EMPTY : new ArrayMap<>(parsedProcess.getAppClassNamesByPackage());
        this.deniedPermissions = new ArraySet(parsedProcess.getDeniedPermissions());
        this.gwpAsanMode = parsedProcess.getGwpAsanMode();
        this.memtagMode = parsedProcess.getMemtagMode();
        this.nativeHeapZeroInitialized = parsedProcess.getNativeHeapZeroInitialized();
    }

    public void addStateFrom(ParsedProcess parsedProcess) {
        this.deniedPermissions = CollectionUtils.addAll(this.deniedPermissions, parsedProcess.getDeniedPermissions());
        this.gwpAsanMode = parsedProcess.getGwpAsanMode();
        this.memtagMode = parsedProcess.getMemtagMode();
        this.nativeHeapZeroInitialized = parsedProcess.getNativeHeapZeroInitialized();
        ArrayMap<String, String> appClassNamesByPackage = parsedProcess.getAppClassNamesByPackage();
        for (int i = 0; i < appClassNamesByPackage.size(); i++) {
            this.appClassNamesByPackage.put(appClassNamesByPackage.keyAt(i), appClassNamesByPackage.valueAt(i));
        }
    }

    public void putAppClassNameForPackage(String str, String str2) {
        if (this.appClassNamesByPackage.size() == 0) {
            this.appClassNamesByPackage = new ArrayMap<>(4);
        }
        this.appClassNamesByPackage.put(str, str2);
    }

    public ParsedProcessImpl(String str, ArrayMap<String, String> arrayMap, Set<String> set, int i, int i2, int i3) {
        this.appClassNamesByPackage = ArrayMap.EMPTY;
        this.deniedPermissions = Collections.emptySet();
        this.gwpAsanMode = -1;
        this.memtagMode = -1;
        this.nativeHeapZeroInitialized = -1;
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        this.appClassNamesByPackage = arrayMap;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, arrayMap);
        this.deniedPermissions = set;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, set);
        this.gwpAsanMode = i;
        AnnotationValidations.validate(ApplicationInfo.GwpAsanMode.class, (Annotation) null, i);
        this.memtagMode = i2;
        AnnotationValidations.validate(ApplicationInfo.MemtagMode.class, (Annotation) null, i2);
        this.nativeHeapZeroInitialized = i3;
        AnnotationValidations.validate(ApplicationInfo.NativeHeapZeroInitialized.class, (Annotation) null, i3);
    }

    @Override // com.android.server.pm.pkg.component.ParsedProcess
    public String getName() {
        return this.name;
    }

    @Override // com.android.server.pm.pkg.component.ParsedProcess
    public ArrayMap<String, String> getAppClassNamesByPackage() {
        return this.appClassNamesByPackage;
    }

    @Override // com.android.server.pm.pkg.component.ParsedProcess
    public Set<String> getDeniedPermissions() {
        return this.deniedPermissions;
    }

    @Override // com.android.server.pm.pkg.component.ParsedProcess
    public int getGwpAsanMode() {
        return this.gwpAsanMode;
    }

    @Override // com.android.server.pm.pkg.component.ParsedProcess
    public int getMemtagMode() {
        return this.memtagMode;
    }

    @Override // com.android.server.pm.pkg.component.ParsedProcess
    public int getNativeHeapZeroInitialized() {
        return this.nativeHeapZeroInitialized;
    }

    public ParsedProcessImpl setName(String str) {
        this.name = str;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, str);
        return this;
    }

    public ParsedProcessImpl setAppClassNamesByPackage(ArrayMap<String, String> arrayMap) {
        this.appClassNamesByPackage = arrayMap;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, arrayMap);
        return this;
    }

    public ParsedProcessImpl setDeniedPermissions(Set<String> set) {
        this.deniedPermissions = set;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, set);
        return this;
    }

    public ParsedProcessImpl setGwpAsanMode(int i) {
        this.gwpAsanMode = i;
        AnnotationValidations.validate(ApplicationInfo.GwpAsanMode.class, (Annotation) null, i);
        return this;
    }

    public ParsedProcessImpl setMemtagMode(int i) {
        this.memtagMode = i;
        AnnotationValidations.validate(ApplicationInfo.MemtagMode.class, (Annotation) null, i);
        return this;
    }

    public ParsedProcessImpl setNativeHeapZeroInitialized(int i) {
        this.nativeHeapZeroInitialized = i;
        AnnotationValidations.validate(ApplicationInfo.NativeHeapZeroInitialized.class, (Annotation) null, i);
        return this;
    }

    static {
        Parcelling<Set<String>> parcelling = Parcelling.Cache.get(Parcelling.BuiltIn.ForInternedStringSet.class);
        sParcellingForDeniedPermissions = parcelling;
        if (parcelling == null) {
            sParcellingForDeniedPermissions = Parcelling.Cache.put(new Parcelling.BuiltIn.ForInternedStringSet());
        }
        CREATOR = new Parcelable.Creator<ParsedProcessImpl>() { // from class: com.android.server.pm.pkg.component.ParsedProcessImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParsedProcessImpl[] newArray(int i) {
                return new ParsedProcessImpl[i];
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParsedProcessImpl createFromParcel(Parcel parcel) {
                return new ParsedProcessImpl(parcel);
            }
        };
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeMap(this.appClassNamesByPackage);
        sParcellingForDeniedPermissions.parcel(this.deniedPermissions, parcel, i);
        parcel.writeInt(this.gwpAsanMode);
        parcel.writeInt(this.memtagMode);
        parcel.writeInt(this.nativeHeapZeroInitialized);
    }

    protected ParsedProcessImpl(Parcel parcel) {
        this.appClassNamesByPackage = ArrayMap.EMPTY;
        this.deniedPermissions = Collections.emptySet();
        this.gwpAsanMode = -1;
        this.memtagMode = -1;
        this.nativeHeapZeroInitialized = -1;
        String readString = parcel.readString();
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        parcel.readMap(arrayMap, String.class.getClassLoader());
        Set<String> set = (Set) sParcellingForDeniedPermissions.unparcel(parcel);
        int readInt = parcel.readInt();
        int readInt2 = parcel.readInt();
        int readInt3 = parcel.readInt();
        this.name = readString;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, readString);
        this.appClassNamesByPackage = arrayMap;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, arrayMap);
        this.deniedPermissions = set;
        AnnotationValidations.validate(NonNull.class, (NonNull) null, set);
        this.gwpAsanMode = readInt;
        AnnotationValidations.validate(ApplicationInfo.GwpAsanMode.class, (Annotation) null, readInt);
        this.memtagMode = readInt2;
        AnnotationValidations.validate(ApplicationInfo.MemtagMode.class, (Annotation) null, readInt2);
        this.nativeHeapZeroInitialized = readInt3;
        AnnotationValidations.validate(ApplicationInfo.NativeHeapZeroInitialized.class, (Annotation) null, readInt3);
    }
}
