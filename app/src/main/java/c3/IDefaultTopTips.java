package c3;

import android.graphics.drawable.Drawable;
import android.view.View;

/* compiled from: IDefaultTopTips.java */
/* renamed from: c3.a, reason: use source file name */
/* loaded from: classes.dex */
public interface IDefaultTopTips {
    void setCloseBtnListener(View.OnClickListener onClickListener);

    void setCloseDrawable(Drawable drawable);

    void setNegativeButton(CharSequence charSequence);

    void setNegativeButtonListener(View.OnClickListener onClickListener);

    void setPositiveButton(CharSequence charSequence);

    void setPositiveButtonListener(View.OnClickListener onClickListener);

    void setStartIcon(Drawable drawable);

    void setTipsText(CharSequence charSequence);

    void setTipsTextColor(int i10);
}
