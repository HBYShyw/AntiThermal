package com.google.android.material.badge;

import android.content.Context;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.badge.BadgeState;
import com.google.android.material.internal.ParcelableSparseArray;

/* compiled from: BadgeUtils.java */
/* renamed from: com.google.android.material.badge.b, reason: use source file name */
/* loaded from: classes.dex */
public class BadgeUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final boolean f8315a = false;

    public static void a(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        e(badgeDrawable, view, frameLayout);
        if (badgeDrawable.g() != null) {
            badgeDrawable.g().setForeground(badgeDrawable);
        } else {
            if (!f8315a) {
                view.getOverlay().add(badgeDrawable);
                return;
            }
            throw new IllegalArgumentException("Trying to reference null customBadgeParent");
        }
    }

    public static SparseArray<BadgeDrawable> b(Context context, ParcelableSparseArray parcelableSparseArray) {
        SparseArray<BadgeDrawable> sparseArray = new SparseArray<>(parcelableSparseArray.size());
        for (int i10 = 0; i10 < parcelableSparseArray.size(); i10++) {
            int keyAt = parcelableSparseArray.keyAt(i10);
            BadgeState.State state = (BadgeState.State) parcelableSparseArray.valueAt(i10);
            if (state != null) {
                sparseArray.put(keyAt, BadgeDrawable.c(context, state));
            } else {
                throw new IllegalArgumentException("BadgeDrawable's savedState cannot be null");
            }
        }
        return sparseArray;
    }

    public static ParcelableSparseArray c(SparseArray<BadgeDrawable> sparseArray) {
        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
        for (int i10 = 0; i10 < sparseArray.size(); i10++) {
            int keyAt = sparseArray.keyAt(i10);
            BadgeDrawable valueAt = sparseArray.valueAt(i10);
            if (valueAt != null) {
                parcelableSparseArray.put(keyAt, valueAt.k());
            } else {
                throw new IllegalArgumentException("badgeDrawable cannot be null");
            }
        }
        return parcelableSparseArray;
    }

    public static void d(BadgeDrawable badgeDrawable, View view) {
        if (badgeDrawable == null) {
            return;
        }
        if (!f8315a && badgeDrawable.g() == null) {
            view.getOverlay().remove(badgeDrawable);
        } else {
            badgeDrawable.g().setForeground(null);
        }
    }

    public static void e(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        badgeDrawable.setBounds(rect);
        badgeDrawable.A(view, frameLayout);
    }

    public static void f(Rect rect, float f10, float f11, float f12, float f13) {
        rect.set((int) (f10 - f12), (int) (f11 - f13), (int) (f10 + f12), (int) (f11 + f13));
    }
}
