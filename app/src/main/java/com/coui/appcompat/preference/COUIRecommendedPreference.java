package com.coui.appcompat.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.support.appcompat.R$color;
import com.support.list.R$attr;
import com.support.list.R$dimen;
import com.support.list.R$drawable;
import com.support.list.R$id;
import com.support.list.R$layout;
import com.support.list.R$string;
import com.support.list.R$style;
import com.support.list.R$styleable;
import java.util.ArrayList;
import java.util.List;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIRecommendedPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private List<c> f6995e;

    /* renamed from: f, reason: collision with root package name */
    private float f6996f;

    /* renamed from: g, reason: collision with root package name */
    private int f6997g;

    /* renamed from: h, reason: collision with root package name */
    private COUIRecommendedDrawable f6998h;

    /* renamed from: i, reason: collision with root package name */
    private String f6999i;

    /* loaded from: classes.dex */
    public interface a {
        void a(View view);
    }

    /* loaded from: classes.dex */
    private static class b extends RecyclerView.h<d> {

        /* renamed from: a, reason: collision with root package name */
        private Context f7000a;

        /* renamed from: b, reason: collision with root package name */
        private List<c> f7001b = new ArrayList();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a implements View.OnClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ c f7002e;

            a(c cVar) {
                this.f7002e = cVar;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (this.f7002e.f7005b != null) {
                    this.f7002e.f7005b.a(view);
                }
            }
        }

        public b(Context context, List<c> list, String str) {
            this.f7000a = context;
            e(list, str);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(d dVar, int i10) {
            c cVar = this.f7001b.get(i10);
            dVar.f7006a.setText(cVar.f7004a);
            if (i10 <= 0) {
                if (i10 == 0) {
                    dVar.f7007b.setClickable(false);
                    return;
                }
                return;
            }
            if (i10 == getItemCount() - 1) {
                dVar.f7007b.setPaddingRelative(dVar.f7007b.getPaddingStart(), dVar.f7007b.getPaddingTop(), dVar.f7007b.getPaddingEnd(), this.f7000a.getResources().getDimensionPixelOffset(R$dimen.recommended_recyclerView_padding_bottom));
                dVar.f7007b.setBackgroundAnimationDrawable(this.f7000a.getDrawable(R$drawable.coui_recommended_last_bg));
            } else if (dVar.f7007b.getPaddingBottom() == this.f7000a.getResources().getDimensionPixelOffset(R$dimen.recommended_recyclerView_padding_bottom)) {
                dVar.f7007b.setPaddingRelative(dVar.f7007b.getPaddingStart(), dVar.f7007b.getPaddingTop(), dVar.f7007b.getPaddingEnd(), 0);
                dVar.f7007b.setBackgroundAnimationDrawable(new ColorDrawable(COUIContextUtil.d(this.f7000a, R$color.coui_list_color_pressed)));
            }
            dVar.f7007b.setOnClickListener(new a(cVar));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public d onCreateViewHolder(ViewGroup viewGroup, int i10) {
            if (i10 == 0) {
                return new d(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.item_recommended_head_textview, viewGroup, false));
            }
            return new d(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.item_recommended_common_textview, viewGroup, false));
        }

        public void e(List<c> list, String str) {
            this.f7001b.clear();
            if (list != null) {
                this.f7001b.addAll(list);
                this.f7001b.add(0, new c(str));
            }
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemCount() {
            return this.f7001b.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemViewType(int i10) {
            return i10 == 0 ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d extends RecyclerView.c0 {

        /* renamed from: a, reason: collision with root package name */
        private TextView f7006a;

        /* renamed from: b, reason: collision with root package name */
        private ListSelectedItemLayout f7007b;

        public d(View view) {
            super(view);
            this.f7007b = (ListSelectedItemLayout) view;
            this.f7006a = (TextView) view.findViewById(R$id.txt_content);
            this.f7007b.setClickable(true);
        }
    }

    public COUIRecommendedPreference(Context context) {
        this(context, null);
    }

    public void c(List<c> list) {
        if (list != null && !list.isEmpty()) {
            setVisible(true);
            this.f6995e = list;
            notifyChanged();
            return;
        }
        setVisible(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.itemView;
        recyclerView.setBackground(this.f6998h);
        RecyclerView.h adapter = recyclerView.getAdapter();
        if (adapter == null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new b(getContext(), this.f6995e, this.f6999i));
        } else {
            ((b) adapter).e(this.f6995e, this.f6999i);
        }
        recyclerView.setFocusable(false);
    }

    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        private String f7004a;

        /* renamed from: b, reason: collision with root package name */
        private a f7005b;

        public c(String str) {
            this.f7004a = str;
        }

        public c(String str, a aVar) {
            this.f7004a = str;
            this.f7005b = aVar;
        }
    }

    public COUIRecommendedPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiRecommendedPreferenceStyle);
    }

    public COUIRecommendedPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Preference_COUIRecommendedPreference);
    }

    public COUIRecommendedPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10);
        setLayoutResource(R$layout.coui_recommended_preference_layout);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIRecommendedPreference, i10, 0);
        this.f6996f = obtainStyledAttributes.getDimension(R$styleable.COUIRecommendedPreference_recommendedCardBgRadius, getContext().getResources().getDimension(R$dimen.recommended_preference_list_card_radius));
        this.f6997g = obtainStyledAttributes.getColor(R$styleable.COUIRecommendedPreference_recommendedCardBgColor, COUIContextUtil.d(getContext(), com.support.list.R$color.bottom_recommended_recycler_view_bg));
        this.f6998h = new COUIRecommendedDrawable(this.f6996f, this.f6997g);
        String string = obtainStyledAttributes.getString(R$styleable.COUIRecommendedPreference_recommendedHeaderTitle);
        this.f6999i = string;
        if (string == null) {
            this.f6999i = getContext().getResources().getString(R$string.bottom_recommended_header_title);
        }
        obtainStyledAttributes.recycle();
    }
}
