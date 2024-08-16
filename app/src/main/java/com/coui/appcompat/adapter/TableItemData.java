package com.coui.appcompat.adapter;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import k1.a;
import kotlin.Metadata;
import za.k;

/* compiled from: COUIMultiTabAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0007\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u0000 @2\u00020\u0001:\u0001AB=\u0012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010\u0012\u000e\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u0010\u0012\u000e\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u0010\u0012\u0006\u0010=\u001a\u00020<¢\u0006\u0004\b>\u0010?J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÖ\u0003J\t\u0010\n\u001a\u00020\u0004HÖ\u0001J\u0019\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0004HÖ\u0001R\u001d\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u00108\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u001f\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u00108\u0006¢\u0006\f\n\u0004\b\u0018\u0010\u0013\u001a\u0004\b\u0019\u0010\u0015R\u001f\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u00108\u0006¢\u0006\f\n\u0004\b\u001c\u0010\u0013\u001a\u0004\b\u001d\u0010\u0015R\"\u0010%\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001f\u0010 \u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\"\u0010)\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b&\u0010 \u001a\u0004\b'\u0010\"\"\u0004\b(\u0010$R\"\u0010-\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b*\u0010 \u001a\u0004\b+\u0010\"\"\u0004\b,\u0010$R\"\u00105\u001a\u00020.8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b/\u00100\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\"\u00108\u001a\u00020\b8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b6\u00107\u001a\u0004\b8\u00109\"\u0004\b:\u0010;¨\u0006B"}, d2 = {"com/coui/appcompat/adapter/COUIMultiTabAdapter$TableItemData", "Landroid/os/Parcelable;", "", "toString", "", "hashCode", "", "other", "", "equals", "describeContents", "Landroid/os/Parcel;", "parcel", "flags", "Lma/f0;", "writeToParcel", "", "Lcom/coui/appcompat/adapter/COUIMultiTabAdapter$TabData;", "e", "Ljava/util/List;", "getTabNames", "()Ljava/util/List;", "tabNames", "Lcom/coui/appcompat/adapter/COUIMultiTabAdapter$TableItemData;", "f", "getListContent", "listContent", "Landroid/content/Intent;", "g", "getLayoutContent", "layoutContent", "i", "I", "getTextNormalColor", "()I", "setTextNormalColor", "(I)V", "textNormalColor", "j", "getTextSelectedColor", "setTextSelectedColor", "textSelectedColor", "k", "getSelectedTabIndicatorColor", "setSelectedTabIndicatorColor", "selectedTabIndicatorColor", "", "l", "F", "getTextSize", "()F", "setTextSize", "(F)V", "textSize", "m", "Z", "isAutoBold", "()Z", "setAutoBold", "(Z)V", "Lk1/a;", "itemType", "<init>", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Lk1/a;)V", "n", "a", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: com.coui.appcompat.adapter.COUIMultiTabAdapter$TableItemData, reason: from toString */
/* loaded from: classes.dex */
public final /* data */ class TableItemData implements Parcelable {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata and from toString */
    private final List<TabData> tabNames;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata and from toString */
    private final List<TableItemData> listContent;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata and from toString */
    private final List<Intent> layoutContent;

    /* renamed from: h, reason: collision with root package name and from toString */
    private final a itemType;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private int textNormalColor;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private int textSelectedColor;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private int selectedTabIndicatorColor;

    /* renamed from: l, reason: collision with root package name and from kotlin metadata */
    private float textSize;

    /* renamed from: m, reason: collision with root package name and from kotlin metadata */
    private boolean isAutoBold;
    public static final Parcelable.Creator<TableItemData> CREATOR = new b();

    /* compiled from: COUIMultiTabAdapter.kt */
    @Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    /* renamed from: com.coui.appcompat.adapter.COUIMultiTabAdapter$TableItemData$b */
    /* loaded from: classes.dex */
    public static final class b implements Parcelable.Creator<TableItemData> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TableItemData createFromParcel(Parcel parcel) {
            ArrayList arrayList;
            k.e(parcel, "parcel");
            int readInt = parcel.readInt();
            ArrayList arrayList2 = new ArrayList(readInt);
            for (int i10 = 0; i10 != readInt; i10++) {
                arrayList2.add(TabData.CREATOR.createFromParcel(parcel));
            }
            ArrayList arrayList3 = null;
            if (parcel.readInt() == 0) {
                arrayList = null;
            } else {
                int readInt2 = parcel.readInt();
                arrayList = new ArrayList(readInt2);
                for (int i11 = 0; i11 != readInt2; i11++) {
                    arrayList.add(TableItemData.CREATOR.createFromParcel(parcel));
                }
            }
            if (parcel.readInt() != 0) {
                int readInt3 = parcel.readInt();
                arrayList3 = new ArrayList(readInt3);
                for (int i12 = 0; i12 != readInt3; i12++) {
                    arrayList3.add(parcel.readParcelable(TableItemData.class.getClassLoader()));
                }
            }
            return new TableItemData(arrayList2, arrayList, arrayList3, a.valueOf(parcel.readString()));
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final TableItemData[] newArray(int i10) {
            return new TableItemData[i10];
        }
    }

    public TableItemData(List<TabData> list, List<TableItemData> list2, List<Intent> list3, a aVar) {
        k.e(list, "tabNames");
        k.e(aVar, "itemType");
        this.tabNames = list;
        this.listContent = list2;
        this.layoutContent = list3;
        this.itemType = aVar;
        this.textNormalColor = -1;
        this.textSelectedColor = -1;
        this.selectedTabIndicatorColor = -1;
        this.textSize = -1.0f;
        this.isAutoBold = true;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TableItemData)) {
            return false;
        }
        TableItemData tableItemData = (TableItemData) other;
        return k.a(this.tabNames, tableItemData.tabNames) && k.a(this.listContent, tableItemData.listContent) && k.a(this.layoutContent, tableItemData.layoutContent) && this.itemType == tableItemData.itemType;
    }

    public int hashCode() {
        int hashCode = this.tabNames.hashCode() * 31;
        List<TableItemData> list = this.listContent;
        int hashCode2 = (hashCode + (list == null ? 0 : list.hashCode())) * 31;
        List<Intent> list2 = this.layoutContent;
        return ((hashCode2 + (list2 != null ? list2.hashCode() : 0)) * 31) + this.itemType.hashCode();
    }

    public String toString() {
        return "TableItemData(tabNames=" + this.tabNames + ", listContent=" + this.listContent + ", layoutContent=" + this.layoutContent + ", itemType=" + this.itemType + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        List<TabData> list = this.tabNames;
        parcel.writeInt(list.size());
        Iterator<TabData> it = list.iterator();
        while (it.hasNext()) {
            it.next().writeToParcel(parcel, i10);
        }
        List<TableItemData> list2 = this.listContent;
        if (list2 == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            parcel.writeInt(list2.size());
            Iterator<TableItemData> it2 = list2.iterator();
            while (it2.hasNext()) {
                it2.next().writeToParcel(parcel, i10);
            }
        }
        List<Intent> list3 = this.layoutContent;
        if (list3 == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            parcel.writeInt(list3.size());
            Iterator<Intent> it3 = list3.iterator();
            while (it3.hasNext()) {
                parcel.writeParcelable(it3.next(), i10);
            }
        }
        parcel.writeString(this.itemType.name());
    }
}
