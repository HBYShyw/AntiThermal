package com.coui.appcompat.toolbar.userfollow;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.coui.appcompat.imageview.COUIRoundImageView;

/* compiled from: IUserFollowView.java */
/* renamed from: com.coui.appcompat.toolbar.userfollow.a, reason: use source file name */
/* loaded from: classes.dex */
public interface IUserFollowView {

    /* compiled from: IUserFollowView.java */
    /* renamed from: com.coui.appcompat.toolbar.userfollow.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(IUserFollowView iUserFollowView, boolean z10);
    }

    COUIRoundImageView getImage();

    void setAnimate(boolean z10);

    void setBtnBg(Drawable drawable);

    void setBtnText(CharSequence charSequence);

    void setFill(boolean z10);

    void setFollowTitle(CharSequence charSequence);

    void setFollowTitleColor(int i10);

    void setFollowing(boolean z10);

    void setImage(int i10);

    void setImage(Bitmap bitmap);

    void setImage(Drawable drawable);

    void setOnStateChangeListener(a aVar);

    void setSubFollowTitle(CharSequence charSequence);

    void setSubFollowTitleColor(int i10);

    void setSubFollowTitleEnable(boolean z10);
}
