package com.coui.appcompat.floatingactionbutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import c.AppCompatResources;

/* loaded from: classes.dex */
public class COUIFloatingButtonItem implements Parcelable {
    public static final Parcelable.Creator<COUIFloatingButtonItem> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final int f6022e;

    /* renamed from: f, reason: collision with root package name */
    private final String f6023f;

    /* renamed from: g, reason: collision with root package name */
    private final int f6024g;

    /* renamed from: h, reason: collision with root package name */
    private final int f6025h;

    /* renamed from: i, reason: collision with root package name */
    private final Drawable f6026i;

    /* renamed from: j, reason: collision with root package name */
    private ColorStateList f6027j;

    /* renamed from: k, reason: collision with root package name */
    private ColorStateList f6028k;

    /* renamed from: l, reason: collision with root package name */
    private ColorStateList f6029l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f6030m;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<COUIFloatingButtonItem> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public COUIFloatingButtonItem createFromParcel(Parcel parcel) {
            return new COUIFloatingButtonItem(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public COUIFloatingButtonItem[] newArray(int i10) {
            return new COUIFloatingButtonItem[i10];
        }
    }

    /* synthetic */ COUIFloatingButtonItem(b bVar, a aVar) {
        this(bVar);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public COUIFloatingButtonLabel s(Context context) {
        COUIFloatingButtonLabel cOUIFloatingButtonLabel = new COUIFloatingButtonLabel(context);
        cOUIFloatingButtonLabel.setFloatingButtonItem(this);
        return cOUIFloatingButtonLabel;
    }

    public ColorStateList t() {
        return this.f6027j;
    }

    public Drawable u(Context context) {
        Drawable drawable = this.f6026i;
        if (drawable != null) {
            return drawable;
        }
        int i10 = this.f6025h;
        if (i10 != Integer.MIN_VALUE) {
            return AppCompatResources.b(context, i10);
        }
        return null;
    }

    public int v() {
        return this.f6022e;
    }

    public String w(Context context) {
        String str = this.f6023f;
        if (str != null) {
            return str;
        }
        int i10 = this.f6024g;
        if (i10 != Integer.MIN_VALUE) {
            return context.getString(i10);
        }
        return null;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.f6023f);
        parcel.writeInt(this.f6024g);
        parcel.writeInt(this.f6025h);
        parcel.writeInt(this.f6022e);
    }

    public ColorStateList x() {
        return this.f6029l;
    }

    public ColorStateList y() {
        return this.f6028k;
    }

    public boolean z() {
        return this.f6030m;
    }

    private COUIFloatingButtonItem(b bVar) {
        this.f6027j = ColorStateList.valueOf(Integer.MIN_VALUE);
        this.f6028k = ColorStateList.valueOf(Integer.MIN_VALUE);
        this.f6029l = ColorStateList.valueOf(Integer.MIN_VALUE);
        this.f6030m = true;
        this.f6023f = bVar.f6034d;
        this.f6024g = bVar.f6035e;
        this.f6025h = bVar.f6032b;
        this.f6026i = bVar.f6033c;
        this.f6027j = bVar.f6036f;
        this.f6028k = bVar.f6037g;
        this.f6029l = bVar.f6038h;
        this.f6030m = bVar.f6039i;
        this.f6022e = bVar.f6031a;
    }

    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private final int f6031a;

        /* renamed from: b, reason: collision with root package name */
        private final int f6032b;

        /* renamed from: c, reason: collision with root package name */
        private Drawable f6033c;

        /* renamed from: d, reason: collision with root package name */
        private String f6034d;

        /* renamed from: e, reason: collision with root package name */
        private int f6035e;

        /* renamed from: f, reason: collision with root package name */
        private ColorStateList f6036f;

        /* renamed from: g, reason: collision with root package name */
        private ColorStateList f6037g;

        /* renamed from: h, reason: collision with root package name */
        private ColorStateList f6038h;

        /* renamed from: i, reason: collision with root package name */
        private boolean f6039i;

        public b(int i10, int i11) {
            this.f6035e = Integer.MIN_VALUE;
            this.f6036f = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f6037g = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f6038h = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f6039i = true;
            this.f6031a = i10;
            this.f6032b = i11;
            this.f6033c = null;
        }

        public COUIFloatingButtonItem j() {
            return new COUIFloatingButtonItem(this, null);
        }

        public b k(ColorStateList colorStateList) {
            this.f6036f = colorStateList;
            return this;
        }

        public b l(String str) {
            this.f6034d = str;
            return this;
        }

        public b m(ColorStateList colorStateList) {
            this.f6038h = colorStateList;
            return this;
        }

        public b n(ColorStateList colorStateList) {
            this.f6037g = colorStateList;
            return this;
        }

        public b(COUIFloatingButtonItem cOUIFloatingButtonItem) {
            this.f6035e = Integer.MIN_VALUE;
            this.f6036f = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f6037g = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f6038h = ColorStateList.valueOf(Integer.MIN_VALUE);
            this.f6039i = true;
            this.f6034d = cOUIFloatingButtonItem.f6023f;
            this.f6035e = cOUIFloatingButtonItem.f6024g;
            this.f6032b = cOUIFloatingButtonItem.f6025h;
            this.f6033c = cOUIFloatingButtonItem.f6026i;
            this.f6036f = cOUIFloatingButtonItem.f6027j;
            this.f6037g = cOUIFloatingButtonItem.f6028k;
            this.f6038h = cOUIFloatingButtonItem.f6029l;
            this.f6039i = cOUIFloatingButtonItem.f6030m;
            this.f6031a = cOUIFloatingButtonItem.f6022e;
        }
    }

    protected COUIFloatingButtonItem(Parcel parcel) {
        this.f6027j = ColorStateList.valueOf(Integer.MIN_VALUE);
        this.f6028k = ColorStateList.valueOf(Integer.MIN_VALUE);
        this.f6029l = ColorStateList.valueOf(Integer.MIN_VALUE);
        this.f6030m = true;
        this.f6023f = parcel.readString();
        this.f6024g = parcel.readInt();
        this.f6025h = parcel.readInt();
        this.f6026i = null;
        this.f6022e = parcel.readInt();
    }
}
