package com.oplus.wrapper.view;

import android.graphics.Matrix;

/* loaded from: classes.dex */
public class GhostView {
    private final android.view.GhostView mGhostView;

    public GhostView(android.view.GhostView ghostView) {
        this.mGhostView = ghostView;
    }

    public static void calculateMatrix(android.view.View view, android.view.ViewGroup host, Matrix matrix) {
        android.view.GhostView.calculateMatrix(view, host, matrix);
    }

    public static GhostView addGhost(android.view.View view, android.view.ViewGroup viewGroup, Matrix matrix) {
        return new GhostView(android.view.GhostView.addGhost(view, viewGroup, matrix));
    }

    public static void removeGhost(android.view.View view) {
        android.view.GhostView.removeGhost(view);
    }

    public void setMatrix(Matrix matrix) {
        this.mGhostView.setMatrix(matrix);
    }
}
