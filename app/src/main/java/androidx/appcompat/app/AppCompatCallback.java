package androidx.appcompat.app;

import androidx.appcompat.view.ActionMode;

/* compiled from: AppCompatCallback.java */
/* renamed from: androidx.appcompat.app.c, reason: use source file name */
/* loaded from: classes.dex */
public interface AppCompatCallback {
    void onSupportActionModeFinished(ActionMode actionMode);

    void onSupportActionModeStarted(ActionMode actionMode);

    ActionMode onWindowStartingSupportActionMode(ActionMode.a aVar);
}
