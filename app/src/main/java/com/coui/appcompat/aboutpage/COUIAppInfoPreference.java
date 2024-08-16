package com.coui.appcompat.aboutpage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import com.coui.appcompat.aboutpage.COUIAppInfoPreference;
import com.coui.appcompat.poplist.COUIPopupWindow;
import com.coui.appcompat.preference.COUIPreference;
import com.support.appcompat.R$drawable;
import com.support.component.R$dimen;
import com.support.component.R$id;
import com.support.component.R$layout;
import com.support.component.R$styleable;
import java.util.Locale;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: COUIAppInfoPreference.kt */
@Metadata(bv = {}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u001b\b\u0016\u0012\u0006\u00101\u001a\u000200\u0012\b\u00103\u001a\u0004\u0018\u000102¢\u0006\u0004\b4\u00105B#\b\u0016\u0012\u0006\u00101\u001a\u000200\u0012\b\u00103\u001a\u0004\u0018\u000102\u0012\u0006\u00107\u001a\u000206¢\u0006\u0004\b4\u00108B+\b\u0016\u0012\u0006\u00101\u001a\u000200\u0012\b\u00103\u001a\u0004\u0018\u000102\u0012\u0006\u00107\u001a\u000206\u0012\u0006\u00109\u001a\u000206¢\u0006\u0004\b4\u0010:J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\tH\u0016R$\u0010\u0013\u001a\u0004\u0018\u00010\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R$\u0010\u001b\u001a\u0004\u0018\u00010\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR$\u0010\u001f\u001a\u0004\u0018\u00010\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001c\u0010\u0016\u001a\u0004\b\u001d\u0010\u0018\"\u0004\b\u001e\u0010\u001aR$\u0010#\u001a\u0004\u0018\u00010\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b \u0010\u0016\u001a\u0004\b!\u0010\u0018\"\u0004\b\"\u0010\u001aR$\u0010'\u001a\u0004\u0018\u00010\u00148\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b$\u0010\u0016\u001a\u0004\b%\u0010\u0018\"\u0004\b&\u0010\u001aR\u0018\u0010+\u001a\u0004\u0018\u00010(8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b)\u0010*R\u0018\u0010/\u001a\u0004\u0018\u00010,8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b-\u0010.¨\u0006;"}, d2 = {"Lcom/coui/appcompat/aboutpage/COUIAppInfoPreference;", "Lcom/coui/appcompat/preference/COUIPreference;", "Landroid/graphics/Rect;", "drawableRect", "Lma/f0;", "p", "Landroid/view/View;", "view", "x", "Landroidx/preference/l;", "holder", "onBindViewHolder", "Landroid/graphics/drawable/Drawable;", "F", "Landroid/graphics/drawable/Drawable;", "j", "()Landroid/graphics/drawable/Drawable;", "s", "(Landroid/graphics/drawable/Drawable;)V", "appIcon", "", "G", "Ljava/lang/String;", "l", "()Ljava/lang/String;", "t", "(Ljava/lang/String;)V", "appName", "H", "m", "u", "appVersion", "I", "o", "w", "copyText", "J", "n", "v", "copyFinishText", "Landroid/widget/Toast;", "K", "Landroid/widget/Toast;", "toast", "Lcom/coui/appcompat/poplist/COUIPopupWindow;", "L", "Lcom/coui/appcompat/poplist/COUIPopupWindow;", "popupWindow", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "", "defStyleAttr", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUIAppInfoPreference extends COUIPreference {

    /* renamed from: F, reason: from kotlin metadata */
    private Drawable appIcon;

    /* renamed from: G, reason: from kotlin metadata */
    private String appName;

    /* renamed from: H, reason: from kotlin metadata */
    private String appVersion;

    /* renamed from: I, reason: from kotlin metadata */
    private String copyText;

    /* renamed from: J, reason: from kotlin metadata */
    private String copyFinishText;

    /* renamed from: K, reason: from kotlin metadata */
    private Toast toast;

    /* renamed from: L, reason: from kotlin metadata */
    private COUIPopupWindow popupWindow;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIAppInfoPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.preferenceStyle);
        k.e(context, "context");
    }

    private final void p(Rect rect) {
        final COUIPopupWindow cOUIPopupWindow = new COUIPopupWindow(getContext());
        cOUIPopupWindow.setContentView(LayoutInflater.from(getContext()).inflate(R$layout.coui_component_popup_window_layout, (ViewGroup) null, false));
        Drawable f10 = ResourcesCompat.f(getContext().getResources(), R$drawable.coui_popup_window_bg, null);
        if (f10 != null) {
            f10.getPadding(rect);
            cOUIPopupWindow.setBackgroundDrawable(f10);
        }
        final TextView textView = (TextView) cOUIPopupWindow.getContentView().findViewById(R$id.popup_window_copy_body);
        textView.setText(getCopyText());
        textView.setOnClickListener(new View.OnClickListener() { // from class: j1.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                COUIAppInfoPreference.q(textView, this, cOUIPopupWindow, view);
            }
        });
        cOUIPopupWindow.b(true);
        Unit unit = Unit.f15173a;
        this.popupWindow = cOUIPopupWindow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void q(TextView textView, COUIAppInfoPreference cOUIAppInfoPreference, COUIPopupWindow cOUIPopupWindow, View view) {
        k.e(cOUIAppInfoPreference, "this$0");
        k.e(cOUIPopupWindow, "$this_apply$1");
        Object systemService = textView.getContext().getSystemService("clipboard");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.content.ClipboardManager");
        ((ClipboardManager) systemService).setPrimaryClip(ClipData.newPlainText(null, cOUIAppInfoPreference.getAppVersion()));
        Toast toast = cOUIAppInfoPreference.toast;
        if (toast != null) {
            toast.cancel();
        }
        String copyFinishText = cOUIAppInfoPreference.getCopyFinishText();
        if (copyFinishText != null) {
            Toast makeText = Toast.makeText(textView.getContext().getApplicationContext(), copyFinishText, 0);
            makeText.show();
            Unit unit = Unit.f15173a;
            cOUIAppInfoPreference.toast = makeText;
        }
        cOUIPopupWindow.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean r(COUIAppInfoPreference cOUIAppInfoPreference, Rect rect, TextView textView, View view) {
        k.e(cOUIAppInfoPreference, "this$0");
        k.e(rect, "$drawableRect");
        k.e(textView, "$this_apply");
        if (cOUIAppInfoPreference.popupWindow == null) {
            cOUIAppInfoPreference.p(rect);
        }
        cOUIAppInfoPreference.x(rect, textView);
        return true;
    }

    private final void x(Rect rect, View view) {
        int dimensionPixelOffset = (((rect.left + rect.right) + getContext().getResources().getDimensionPixelOffset(R$dimen.coui_component_copy_window_width)) - view.getMeasuredWidth()) / 2;
        int measuredHeight = view.getMeasuredHeight() + getContext().getResources().getDimensionPixelOffset(R$dimen.coui_component_copy_window_height) + getContext().getResources().getDimensionPixelOffset(R$dimen.coui_component_copy_window_margin_bottom) + rect.top;
        Locale locale = Locale.getDefault();
        k.d(locale, "getDefault()");
        int i10 = TextUtils.getLayoutDirectionFromLocale(locale) == 1 ? (-view.getMeasuredWidth()) - dimensionPixelOffset : -dimensionPixelOffset;
        COUIPopupWindow cOUIPopupWindow = this.popupWindow;
        if (cOUIPopupWindow == null) {
            return;
        }
        cOUIPopupWindow.showAsDropDown(view, i10, -measuredHeight);
    }

    /* renamed from: j, reason: from getter */
    public final Drawable getAppIcon() {
        return this.appIcon;
    }

    /* renamed from: l, reason: from getter */
    public final String getAppName() {
        return this.appName;
    }

    /* renamed from: m, reason: from getter */
    public final String getAppVersion() {
        return this.appVersion;
    }

    /* renamed from: n, reason: from getter */
    public final String getCopyFinishText() {
        return this.copyFinishText;
    }

    /* renamed from: o, reason: from getter */
    public final String getCopyText() {
        return this.copyText;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        k.e(preferenceViewHolder, "holder");
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.a(R$id.about_app_icon);
        if (imageView != null) {
            if (imageView.getParent() != null) {
                imageView.setOnClickListener(null);
                imageView.setBackground(null);
            }
            imageView.setImageDrawable(getAppIcon());
        }
        TextView textView = (TextView) preferenceViewHolder.a(R$id.about_app_name);
        if (textView != null) {
            textView.setText(getAppName());
        }
        final TextView textView2 = (TextView) preferenceViewHolder.a(R$id.about_app_version);
        if (textView2 == null) {
            return;
        }
        textView2.setText(getAppVersion());
        final Rect rect = new Rect();
        textView2.setOnLongClickListener(new View.OnLongClickListener() { // from class: j1.b
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean r10;
                r10 = COUIAppInfoPreference.r(COUIAppInfoPreference.this, rect, textView2, view);
                return r10;
            }
        });
    }

    public final void s(Drawable drawable) {
        this.appIcon = drawable;
    }

    public final void t(String str) {
        this.appName = str;
    }

    public final void u(String str) {
        this.appVersion = str;
    }

    public final void v(String str) {
        this.copyFinishText = str;
    }

    public final void w(String str) {
        this.copyText = str;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIAppInfoPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
        k.e(context, "context");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIAppInfoPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        setLayoutResource(R$layout.coui_component_preference_application_info);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIAppInfoPreference, 0, 0);
        t(obtainStyledAttributes.getString(R$styleable.COUIAppInfoPreference_appName));
        s(obtainStyledAttributes.getDrawable(R$styleable.COUIAppInfoPreference_appIcon));
        u(obtainStyledAttributes.getString(R$styleable.COUIAppInfoPreference_appVersion));
        w(obtainStyledAttributes.getString(R$styleable.COUIAppInfoPreference_copyText));
        v(obtainStyledAttributes.getString(R$styleable.COUIAppInfoPreference_copyFinishText));
        obtainStyledAttributes.recycle();
    }
}
