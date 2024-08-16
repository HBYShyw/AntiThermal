package com.oplus.util;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OplusReflectWidget implements Parcelable {
    public static final Parcelable.Creator<OplusReflectWidget> CREATOR = new Parcelable.Creator<OplusReflectWidget>() { // from class: com.oplus.util.OplusReflectWidget.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusReflectWidget createFromParcel(Parcel source) {
            return new OplusReflectWidget(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusReflectWidget[] newArray(int size) {
            return new OplusReflectWidget[size];
        }
    };
    public static final OplusReflectWidget DEFAULT_WIDGET = new OplusReflectWidget("com.tencent.mm", 1280, "com.tencent.mm.ui.widget.MMNeatTextView", 1, "mText");
    public static final OplusReflectWidget DEFAULT_WIDGET_WECHAT_1420 = new OplusReflectWidget("com.tencent.mm", 1420, "com.tencent.mm.ui.widget.MMNeat7extView", 1, "mText");
    private static final int HASH_CODE_OFFSET = 31;
    private String mClassName;
    private String mField;
    private int mFieldLevel;
    private String mPackageName;
    private int mVersionCode;

    public OplusReflectWidget() {
    }

    public OplusReflectWidget(String packageName, int versionCode, String className, int fieldLevel, String field) {
        this.mPackageName = packageName;
        this.mVersionCode = versionCode;
        this.mClassName = className;
        this.mFieldLevel = fieldLevel;
        this.mField = field;
    }

    protected OplusReflectWidget(Parcel in) {
        this.mPackageName = in.readString();
        this.mVersionCode = in.readInt();
        this.mClassName = in.readString();
        this.mFieldLevel = in.readInt();
        this.mField = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mVersionCode);
        dest.writeString(this.mClassName);
        dest.writeInt(this.mFieldLevel);
        dest.writeString(this.mField);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OplusReflectWidget that = (OplusReflectWidget) o;
        if (this.mVersionCode != that.mVersionCode || this.mFieldLevel != that.mFieldLevel || !this.mPackageName.equals(that.mPackageName) || !this.mClassName.equals(that.mClassName)) {
            return false;
        }
        return this.mField.equals(that.mField);
    }

    public String toString() {
        return "OplusReflectWidget{packageName='" + this.mPackageName + "', versionCode=" + this.mVersionCode + ", className='" + this.mClassName + "', fieldLevel=" + this.mFieldLevel + ", field='" + this.mField + "'}";
    }

    public int hashCode() {
        int result = this.mPackageName.hashCode();
        return (((((((result * 31) + this.mVersionCode) * 31) + this.mClassName.hashCode()) * 31) + this.mVersionCode) * 31) + this.mField.hashCode();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public void setVersionCode(int versionCode) {
        this.mVersionCode = versionCode;
    }

    public String getClassName() {
        return this.mClassName;
    }

    public void setClassName(String className) {
        this.mClassName = className;
    }

    public void setFieldLevel(int fieldLevel) {
        this.mFieldLevel = fieldLevel;
    }

    public int getFieldLevel() {
        return this.mFieldLevel;
    }

    public String getField() {
        return this.mField;
    }

    public void setField(String field) {
        this.mField = field;
    }
}
