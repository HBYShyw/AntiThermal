package com.oplus.oms.split.core.splitinstall;

import android.app.Activity;
import android.content.IntentSender;
import com.oplus.oms.split.core.listener.OplusStateUpdatedListener;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallSessionState;
import com.oplus.oms.split.core.tasks.OplusTask;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes.dex */
public interface OplusSplitInstallManager<S extends OplusSplitInstallSessionState> {
    OplusTask<Void> cancelInstall(int i);

    OplusTask<Void> deferredInstall(List<String> list);

    OplusTask<Void> deferredLanguageInstall(List<Locale> list);

    OplusTask<Void> deferredLanguageUninstall(List<Locale> list);

    OplusTask<Void> deferredUninstall(List<String> list);

    Set<String> getInstalledLanguages();

    Set<String> getInstalledModules();

    OplusTask<S> getSessionState(int i);

    OplusTask<List<S>> getSessionStates();

    void registerListener(OplusStateUpdatedListener<S> oplusStateUpdatedListener);

    boolean startConfirmationDialogForResult(S s, Activity activity, int i) throws IntentSender.SendIntentException;

    OplusTask<Integer> startInstall(OplusSplitInstallRequest oplusSplitInstallRequest);

    void unregisterListener(OplusStateUpdatedListener<S> oplusStateUpdatedListener);
}
