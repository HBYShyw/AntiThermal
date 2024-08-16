package com.coui.appcompat.toolbar.userfollow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R$attr;
import com.coui.appcompat.imageview.COUIRoundImageView;
import com.coui.appcompat.toolbar.COUICustomToolbar;
import com.coui.appcompat.toolbar.userfollow.IUserFollowView;

/* loaded from: classes.dex */
public class COUIUserInfoToolbar extends COUICustomToolbar implements IUserFollowView {

    /* renamed from: r0, reason: collision with root package name */
    public static final int f8049r0 = View.generateViewId();

    /* renamed from: q0, reason: collision with root package name */
    protected IUserFollowView f8050q0;

    public COUIUserInfoToolbar(Context context) {
        this(context, null);
    }

    @Override // com.coui.appcompat.toolbar.COUICustomToolbar
    public int getCustomResId() {
        return 0;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public COUIRoundImageView getImage() {
        return this.f8050q0.getImage();
    }

    @Override // com.coui.appcompat.toolbar.COUICustomToolbar
    protected void p() {
        this.f8050q0 = r();
    }

    protected IUserFollowView r() {
        COUIUserFollowView cOUIUserFollowView = new COUIUserFollowView(getContext());
        cOUIUserFollowView.setId(f8049r0);
        cOUIUserFollowView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        setCustomView(cOUIUserFollowView);
        cOUIUserFollowView.setVisibility(4);
        return cOUIUserFollowView;
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setAnimate(boolean z10) {
        this.f8050q0.setAnimate(z10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setBtnBg(Drawable drawable) {
        this.f8050q0.setBtnBg(drawable);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setBtnText(CharSequence charSequence) {
        this.f8050q0.setBtnText(charSequence);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFill(boolean z10) {
        this.f8050q0.setFill(z10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFollowTitle(CharSequence charSequence) {
        this.f8050q0.setFollowTitle(charSequence);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFollowTitleColor(int i10) {
        this.f8050q0.setFollowTitleColor(i10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setFollowing(boolean z10) {
        this.f8050q0.setFollowing(z10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setImage(Drawable drawable) {
        this.f8050q0.setImage(drawable);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setOnStateChangeListener(IUserFollowView.a aVar) {
        this.f8050q0.setOnStateChangeListener(aVar);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setSubFollowTitle(CharSequence charSequence) {
        this.f8050q0.setSubFollowTitle(charSequence);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setSubFollowTitleColor(int i10) {
        this.f8050q0.setSubFollowTitleColor(i10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setSubFollowTitleEnable(boolean z10) {
        this.f8050q0.setSubFollowTitleEnable(z10);
    }

    public COUIUserInfoToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.toolbarStyle);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setImage(Bitmap bitmap) {
        this.f8050q0.setImage(bitmap);
    }

    public COUIUserInfoToolbar(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }

    @Override // com.coui.appcompat.toolbar.userfollow.IUserFollowView
    public void setImage(int i10) {
        this.f8050q0.setImage(i10);
    }
}
