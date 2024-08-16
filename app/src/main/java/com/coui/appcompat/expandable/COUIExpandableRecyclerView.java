package com.coui.appcompat.expandable;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.COUILinearLayoutManager;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.expandable.ExpandableRecyclerConnector;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class COUIExpandableRecyclerView extends COUIRecyclerView {

    /* renamed from: r0, reason: collision with root package name */
    private COUIExpandableRecyclerAdapter f5910r0;

    /* renamed from: s0, reason: collision with root package name */
    private ExpandableRecyclerConnector f5911s0;

    /* renamed from: t0, reason: collision with root package name */
    private c f5912t0;

    /* renamed from: u0, reason: collision with root package name */
    private b f5913u0;

    /* renamed from: v0, reason: collision with root package name */
    private d f5914v0;

    /* renamed from: w0, reason: collision with root package name */
    private e f5915w0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        ArrayList<ExpandableRecyclerConnector.GroupMetadata> f5916e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, ExpandableRecyclerConnector.class.getClassLoader());
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeList(this.f5916e);
        }

        SavedState(Parcelable parcelable, ArrayList<ExpandableRecyclerConnector.GroupMetadata> arrayList) {
            super(parcelable);
            this.f5916e = arrayList;
        }

        private SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            ArrayList<ExpandableRecyclerConnector.GroupMetadata> arrayList = new ArrayList<>();
            this.f5916e = arrayList;
            parcel.readList(arrayList, ExpandableRecyclerConnector.class.getClassLoader());
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        boolean a(COUIRecyclerView cOUIRecyclerView, View view, int i10, int i11, long j10);
    }

    /* loaded from: classes.dex */
    public interface c {
        boolean a(COUIExpandableRecyclerView cOUIExpandableRecyclerView, View view, int i10, long j10);
    }

    /* loaded from: classes.dex */
    public interface d {
        void a(int i10);
    }

    /* loaded from: classes.dex */
    public interface e {
        void a(int i10);
    }

    public COUIExpandableRecyclerView(Context context) {
        super(context);
        setItemAnimator(null);
    }

    private long J(ExpandableRecyclerPosition expandableRecyclerPosition) {
        if (expandableRecyclerPosition.f5966d == 1) {
            return this.f5910r0.getChildId(expandableRecyclerPosition.f5963a, expandableRecyclerPosition.f5964b);
        }
        return this.f5910r0.getGroupId(expandableRecyclerPosition.f5963a);
    }

    public boolean H(int i10) {
        if (!this.f5911s0.B(i10)) {
            return false;
        }
        this.f5911s0.j();
        d dVar = this.f5914v0;
        if (dVar == null) {
            return true;
        }
        dVar.a(i10);
        return true;
    }

    public boolean I(int i10) {
        e eVar;
        boolean l10 = this.f5911s0.l(i10);
        if (l10 && (eVar = this.f5915w0) != null) {
            eVar.a(i10);
        }
        return l10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean K(View view, int i10) {
        ExpandableRecyclerConnector.k w10 = this.f5911s0.w(i10);
        long J = J(w10.f5959a);
        ExpandableRecyclerPosition expandableRecyclerPosition = w10.f5959a;
        boolean z10 = true;
        if (expandableRecyclerPosition.f5966d == 2) {
            c cVar = this.f5912t0;
            if (cVar != null && cVar.a(this, view, expandableRecyclerPosition.f5963a, J)) {
                w10.d();
                return true;
            }
            if (w10.b()) {
                H(w10.f5959a.f5963a);
            } else {
                I(w10.f5959a.f5963a);
            }
        } else {
            b bVar = this.f5913u0;
            if (bVar != null) {
                return bVar.a(this, view, expandableRecyclerPosition.f5963a, expandableRecyclerPosition.f5964b, J);
            }
            z10 = false;
        }
        w10.d();
        return z10;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        ArrayList<ExpandableRecyclerConnector.GroupMetadata> arrayList;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        ExpandableRecyclerConnector expandableRecyclerConnector = this.f5911s0;
        if (expandableRecyclerConnector == null || (arrayList = savedState.f5916e) == null) {
            return;
        }
        expandableRecyclerConnector.A(arrayList);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        ExpandableRecyclerConnector expandableRecyclerConnector = this.f5911s0;
        return new SavedState(onSaveInstanceState, expandableRecyclerConnector != null ? expandableRecyclerConnector.s() : null);
    }

    public void setAdapter(COUIExpandableRecyclerAdapter cOUIExpandableRecyclerAdapter) {
        this.f5910r0 = cOUIExpandableRecyclerAdapter;
        ExpandableRecyclerConnector expandableRecyclerConnector = new ExpandableRecyclerConnector(cOUIExpandableRecyclerAdapter, this);
        this.f5911s0 = expandableRecyclerConnector;
        super.setAdapter(expandableRecyclerConnector);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setItemAnimator(RecyclerView.m mVar) {
        if (mVar == null) {
            super.setItemAnimator(null);
            return;
        }
        throw new RuntimeException("not set ItemAnimator");
    }

    @Override // androidx.recyclerview.widget.COUIRecyclerView, androidx.recyclerview.widget.RecyclerView
    public void setLayoutManager(RecyclerView.p pVar) {
        if (pVar instanceof COUILinearLayoutManager) {
            if (((COUILinearLayoutManager) pVar).p2() == 1) {
                super.setLayoutManager(pVar);
                return;
            }
            throw new RuntimeException("only vertical orientation");
        }
        throw new RuntimeException("only COUILinearLayoutManager");
    }

    public void setOnChildClickListener(b bVar) {
        this.f5913u0 = bVar;
    }

    public void setOnGroupClickListener(c cVar) {
        this.f5912t0 = cVar;
    }

    public void setOnGroupCollapseListener(d dVar) {
        this.f5914v0 = dVar;
    }

    public void setOnGroupExpandListener(e eVar) {
        this.f5915w0 = eVar;
    }

    public COUIExpandableRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setItemAnimator(null);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setAdapter(RecyclerView.h hVar) {
        throw new RuntimeException("adapter instansof COUIExpandableRecyclerAdapter");
    }

    public COUIExpandableRecyclerView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        setItemAnimator(null);
    }
}
