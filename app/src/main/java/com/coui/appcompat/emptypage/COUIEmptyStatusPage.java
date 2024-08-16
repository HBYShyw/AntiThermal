package com.coui.appcompat.emptypage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coui.appcompat.button.COUIButton;
import com.coui.appcompat.emptypage.COUIEmptyStatusPage;
import com.oplus.anim.EffectiveAnimationView;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$styleable;
import kotlin.Metadata;
import za.k;

/* compiled from: COUIEmptyStatusPage.kt */
@Metadata(bv = {}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0001=B\u0011\b\u0016\u0012\u0006\u00105\u001a\u00020 ¢\u0006\u0004\b6\u00107B\u001b\b\u0016\u0012\u0006\u00105\u001a\u00020 \u0012\b\u00109\u001a\u0004\u0018\u000108¢\u0006\u0004\b6\u0010:B#\b\u0016\u0012\u0006\u00105\u001a\u00020 \u0012\b\u00109\u001a\u0004\u0018\u000108\u0012\u0006\u0010;\u001a\u00020\u0004¢\u0006\u0004\b6\u0010<J\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\t\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\u0007J\u0010\u0010\u000b\u001a\u00020\u00022\b\b\u0001\u0010\n\u001a\u00020\u0004J\u000e\u0010\u000b\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\fJ\u000e\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u000eJ\u000e\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\fJ\u000e\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\fJ\u000e\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0004J\u000e\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\fJ\u000e\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0004J\u000e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u001aR\u0016\u0010\u001f\u001a\u00020\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001d\u0010\u001eR\u0018\u0010#\u001a\u0004\u0018\u00010 8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b!\u0010\"R\u0016\u0010'\u001a\u00020$8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b%\u0010&R\u0016\u0010+\u001a\u00020(8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b)\u0010*R\u0016\u0010-\u001a\u00020(8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b,\u0010*R\u0016\u00101\u001a\u00020.8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b/\u00100R\u0018\u00104\u001a\u0004\u0018\u00010\u001a8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b2\u00103¨\u0006>"}, d2 = {"Lcom/coui/appcompat/emptypage/COUIEmptyStatusPage;", "Landroid/widget/LinearLayout;", "Lma/f0;", "b", "", "resId", "setImageResoure", "Landroid/graphics/drawable/Drawable;", "drawable", "setImageDrawable", "rawRes", "setAnimation", "", "assetName", "Landroid/graphics/Bitmap;", "bitmap", "setImageBitmap", "content", "setMessage", "secondMessage", "setSecondMessage", "visibility", "setSecondMessageVisibility", "buttonText", "setButtonText", "setButtonVisibility", "Lcom/coui/appcompat/emptypage/COUIEmptyStatusPage$a;", "listener", "setButtonListener", "e", "I", "mStyle", "Landroid/content/Context;", "f", "Landroid/content/Context;", "mContext", "Lcom/oplus/anim/EffectiveAnimationView;", "g", "Lcom/oplus/anim/EffectiveAnimationView;", "mImage", "Landroid/widget/TextView;", "h", "Landroid/widget/TextView;", "mMessage", "i", "mSecondMessage", "Lcom/coui/appcompat/button/COUIButton;", "j", "Lcom/coui/appcompat/button/COUIButton;", "mButton", "k", "Lcom/coui/appcompat/emptypage/COUIEmptyStatusPage$a;", "mOnButtonClickListener", "context", "<init>", "(Landroid/content/Context;)V", "Landroid/util/AttributeSet;", "attrs", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "a", "coui-support-appcompat_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUIEmptyStatusPage extends LinearLayout {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private int mStyle;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private Context mContext;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private EffectiveAnimationView mImage;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private TextView mMessage;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private TextView mSecondMessage;

    /* renamed from: j, reason: collision with root package name and from kotlin metadata */
    private COUIButton mButton;

    /* renamed from: k, reason: collision with root package name and from kotlin metadata */
    private a mOnButtonClickListener;

    /* compiled from: COUIEmptyStatusPage.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0004"}, d2 = {"Lcom/coui/appcompat/emptypage/COUIEmptyStatusPage$a;", "", "Lma/f0;", "a", "coui-support-appcompat_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public interface a {
        void a();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStatusPage(Context context) {
        this(context, null);
        k.e(context, "context");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(COUIEmptyStatusPage cOUIEmptyStatusPage, View view) {
        k.e(cOUIEmptyStatusPage, "this$0");
        a aVar = cOUIEmptyStatusPage.mOnButtonClickListener;
        if (aVar == null) {
            return;
        }
        aVar.a();
    }

    public final void b() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.coui_empty_status_page_label, (ViewGroup) this, true);
        View findViewById = inflate.findViewById(R$id.image);
        k.d(findViewById, "inflate.findViewById(R.id.image)");
        this.mImage = (EffectiveAnimationView) findViewById;
        View findViewById2 = inflate.findViewById(R$id.message);
        k.d(findViewById2, "inflate.findViewById(R.id.message)");
        this.mMessage = (TextView) findViewById2;
        View findViewById3 = inflate.findViewById(R$id.second_message);
        k.d(findViewById3, "inflate.findViewById(R.id.second_message)");
        this.mSecondMessage = (TextView) findViewById3;
        View findViewById4 = inflate.findViewById(R$id.btn);
        k.d(findViewById4, "inflate.findViewById(R.id.btn)");
        COUIButton cOUIButton = (COUIButton) findViewById4;
        this.mButton = cOUIButton;
        if (cOUIButton != null) {
            cOUIButton.setOnClickListener(new View.OnClickListener() { // from class: z1.a
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    COUIEmptyStatusPage.c(COUIEmptyStatusPage.this, view);
                }
            });
        } else {
            k.s("mButton");
            throw null;
        }
    }

    public final void setAnimation(int i10) {
        EffectiveAnimationView effectiveAnimationView = this.mImage;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setAnimation(i10);
        } else {
            k.s("mImage");
            throw null;
        }
    }

    public final void setButtonListener(a aVar) {
        k.e(aVar, "listener");
        this.mOnButtonClickListener = aVar;
    }

    public final void setButtonText(String str) {
        k.e(str, "buttonText");
        COUIButton cOUIButton = this.mButton;
        if (cOUIButton != null) {
            cOUIButton.setText(str);
        } else {
            k.s("mButton");
            throw null;
        }
    }

    public final void setButtonVisibility(int i10) {
        COUIButton cOUIButton = this.mButton;
        if (cOUIButton != null) {
            cOUIButton.setVisibility(i10);
        } else {
            k.s("mButton");
            throw null;
        }
    }

    public final void setImageBitmap(Bitmap bitmap) {
        k.e(bitmap, "bitmap");
        EffectiveAnimationView effectiveAnimationView = this.mImage;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setImageBitmap(bitmap);
        } else {
            k.s("mImage");
            throw null;
        }
    }

    public final void setImageDrawable(Drawable drawable) {
        k.e(drawable, "drawable");
        EffectiveAnimationView effectiveAnimationView = this.mImage;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setImageDrawable(drawable);
        } else {
            k.s("mImage");
            throw null;
        }
    }

    public final void setImageResoure(int i10) {
        EffectiveAnimationView effectiveAnimationView = this.mImage;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setImageResource(i10);
        } else {
            k.s("mImage");
            throw null;
        }
    }

    public final void setMessage(String str) {
        k.e(str, "content");
        TextView textView = this.mMessage;
        if (textView != null) {
            textView.setText(str);
        } else {
            k.s("mMessage");
            throw null;
        }
    }

    public final void setSecondMessage(String str) {
        k.e(str, "secondMessage");
        TextView textView = this.mSecondMessage;
        if (textView != null) {
            textView.setText(str);
        } else {
            k.s("mSecondMessage");
            throw null;
        }
    }

    public final void setSecondMessageVisibility(int i10) {
        TextView textView = this.mSecondMessage;
        if (textView != null) {
            textView.setVisibility(i10);
        } else {
            k.s("mSecondMessage");
            throw null;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStatusPage(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        k.e(context, "context");
    }

    public final void setAnimation(String str) {
        k.e(str, "assetName");
        EffectiveAnimationView effectiveAnimationView = this.mImage;
        if (effectiveAnimationView != null) {
            effectiveAnimationView.setAnimation(str);
        } else {
            k.s("mImage");
            throw null;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIEmptyStatusPage(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        k.e(context, "context");
        this.mContext = context;
        this.mStyle = (attributeSet == null || attributeSet.getStyleAttribute() == 0) ? i10 : attributeSet.getStyleAttribute();
        b();
        Context context2 = this.mContext;
        k.b(context2);
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R$styleable.COUIEmptyStatusPage, i10, 0);
        String string = obtainStyledAttributes.getString(R$styleable.COUIEmptyStatusPage_couiEmptyStatusPageText);
        String string2 = obtainStyledAttributes.getString(R$styleable.COUIEmptyStatusPage_couiEmptyStatusPageSecondText);
        String string3 = obtainStyledAttributes.getString(R$styleable.COUIEmptyStatusPage_couiEmptyStatusPageButtonText);
        if (string != null) {
            TextView textView = this.mMessage;
            if (textView == null) {
                k.s("mMessage");
                throw null;
            }
            textView.setText(string);
        }
        if (string2 != null) {
            TextView textView2 = this.mSecondMessage;
            if (textView2 == null) {
                k.s("mSecondMessage");
                throw null;
            }
            textView2.setText(string2);
        }
        if (string3 != null) {
            COUIButton cOUIButton = this.mButton;
            if (cOUIButton == null) {
                k.s("mButton");
                throw null;
            }
            cOUIButton.setText(string3);
        }
        obtainStyledAttributes.recycle();
    }
}
