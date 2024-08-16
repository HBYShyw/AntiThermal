package com.android.server.autofill.ui;

import android.R;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.autofill.BatchUpdates;
import android.service.autofill.CustomDescription;
import android.service.autofill.InternalOnClickAction;
import android.service.autofill.InternalTransformation;
import android.service.autofill.InternalValidator;
import android.service.autofill.SaveInfo;
import android.service.autofill.ValueFinder;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ArrayUtils;
import com.android.server.UiThread;
import com.android.server.autofill.Helper;
import com.android.server.utils.Slogf;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SaveUi {
    private static final int SCROLL_BAR_DEFAULT_DELAY_BEFORE_FADE_MS = 500;
    private static final String TAG = "SaveUi";
    private static final int THEME_ID_DARK = 16974809;
    private static final int THEME_ID_LIGHT = 16974820;
    private final boolean mCompatMode;
    private final ComponentName mComponentName;
    private boolean mDestroyed;
    private final Dialog mDialog;
    private final OneActionThenDestroyListener mListener;
    private final OverlayControl mOverlayControl;
    private final PendingUi mPendingUi;
    private final String mServicePackageName;
    private final CharSequence mSubTitle;
    private final int mThemeId;
    private final CharSequence mTitle;
    private final int mType;
    private final Handler mHandler = UiThread.getHandler();
    private final MetricsLogger mMetricsLogger = new MetricsLogger();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface OnSaveListener {
        void onCancel(IntentSender intentSender);

        void onDestroy();

        void onSave();

        void startIntentSender(IntentSender intentSender, Intent intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class OneActionThenDestroyListener implements OnSaveListener {
        private boolean mDone;
        private final OnSaveListener mRealListener;

        OneActionThenDestroyListener(OnSaveListener onSaveListener) {
            this.mRealListener = onSaveListener;
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void onSave() {
            if (Helper.sDebug) {
                Slog.d(SaveUi.TAG, "OneTimeListener.onSave(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mRealListener.onSave();
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void onCancel(IntentSender intentSender) {
            if (Helper.sDebug) {
                Slog.d(SaveUi.TAG, "OneTimeListener.onCancel(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mRealListener.onCancel(intentSender);
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void onDestroy() {
            if (Helper.sDebug) {
                Slog.d(SaveUi.TAG, "OneTimeListener.onDestroy(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mDone = true;
            this.mRealListener.onDestroy();
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void startIntentSender(IntentSender intentSender, Intent intent) {
            if (Helper.sDebug) {
                Slog.d(SaveUi.TAG, "OneTimeListener.startIntentSender(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mRealListener.startIntentSender(intentSender, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SaveUi(Context context, PendingUi pendingUi, CharSequence charSequence, Drawable drawable, String str, ComponentName componentName, final SaveInfo saveInfo, ValueFinder valueFinder, OverlayControl overlayControl, OnSaveListener onSaveListener, boolean z, boolean z2, boolean z3, boolean z4) {
        if (Helper.sVerbose) {
            Slogf.v(TAG, "nightMode: %b displayId: %d", new Object[]{Boolean.valueOf(z), Integer.valueOf(context.getDisplayId())});
        }
        int i = z ? 16974809 : 16974820;
        this.mThemeId = i;
        this.mPendingUi = pendingUi;
        this.mListener = new OneActionThenDestroyListener(onSaveListener);
        this.mOverlayControl = overlayControl;
        this.mServicePackageName = str;
        this.mComponentName = componentName;
        this.mCompatMode = z3;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, i) { // from class: com.android.server.autofill.ui.SaveUi.1
            @Override // android.content.ContextWrapper, android.content.Context
            public void startActivity(Intent intent) {
                if (resolveActivity(intent) == null) {
                    if (Helper.sDebug) {
                        Slog.d(SaveUi.TAG, "Can not startActivity for save UI with intent=" + intent);
                        return;
                    }
                    return;
                }
                intent.putExtra("android.view.autofill.extra.RESTORE_CROSS_ACTIVITY", true);
                PendingIntent activityAsUser = PendingIntent.getActivityAsUser(this, 0, intent, AudioFormat.AMR_WB, null, UserHandle.CURRENT);
                if (Helper.sDebug) {
                    Slog.d(SaveUi.TAG, "startActivity add save UI restored with intent=" + intent);
                }
                SaveUi.this.startIntentSenderWithRestore(activityAsUser, intent);
            }

            private ComponentName resolveActivity(Intent intent) {
                PackageManager packageManager = getPackageManager();
                ComponentName resolveActivity = intent.resolveActivity(packageManager);
                if (resolveActivity != null) {
                    return resolveActivity;
                }
                intent.addFlags(2048);
                ActivityInfo resolveActivityInfo = intent.resolveActivityInfo(packageManager, AudioDevice.OUT_IP);
                if (resolveActivityInfo != null) {
                    return new ComponentName(resolveActivityInfo.applicationInfo.packageName, resolveActivityInfo.name);
                }
                return null;
            }
        };
        View inflate = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.car_preference_category, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.by_org);
        ArraySet arraySet = new ArraySet(3);
        int type = saveInfo.getType();
        this.mType = type;
        if ((type & 1) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.badPuk));
        }
        if ((type & 2) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.autofill_zip_4_re));
        }
        if (Integer.bitCount(type & 100) > 1 || (type & 128) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.badPin));
        } else if ((type & 64) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.battery_saver_charged_notification_summary));
        } else if ((type & 4) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.autofill_zip_code));
        } else if ((type & 32) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.autofill_zip_code_re));
        }
        if ((type & 8) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.battery_saver_description));
        }
        if ((type & 16) != 0) {
            arraySet.add(contextThemeWrapper.getString(R.string.back_button_label));
        }
        int size = arraySet.size();
        if (size == 1) {
            this.mTitle = Html.fromHtml(contextThemeWrapper.getString(z2 ? R.string.biometric_error_device_not_secured : R.string.autofill_window_title, arraySet.valueAt(0), charSequence), 0);
        } else if (size == 2) {
            this.mTitle = Html.fromHtml(contextThemeWrapper.getString(z2 ? R.string.biometric_dialog_default_title : R.string.autofill_update_yes, arraySet.valueAt(0), arraySet.valueAt(1), charSequence), 0);
        } else if (size == 3) {
            this.mTitle = Html.fromHtml(contextThemeWrapper.getString(z2 ? R.string.biometric_error_canceled : R.string.autofill_username_re, arraySet.valueAt(0), arraySet.valueAt(1), arraySet.valueAt(2), charSequence), 0);
        } else {
            this.mTitle = Html.fromHtml(contextThemeWrapper.getString(z2 ? R.string.beforeOneMonthDurationPast : R.string.autofill_update_title_with_type, charSequence), 0);
        }
        textView.setText(this.mTitle);
        if (z4) {
            setServiceIcon(contextThemeWrapper, inflate, drawable);
        }
        if (applyCustomDescription(contextThemeWrapper, inflate, valueFinder, saveInfo)) {
            this.mSubTitle = null;
            if (Helper.sDebug) {
                Slog.d(TAG, "on constructor: applied custom description");
            }
        } else {
            CharSequence description = saveInfo.getDescription();
            this.mSubTitle = description;
            if (description != null) {
                writeLog(1131);
                ViewGroup viewGroup = (ViewGroup) inflate.findViewById(R.id.buttons);
                TextView textView2 = new TextView(contextThemeWrapper);
                textView2.setText(description);
                applyMovementMethodIfNeed(textView2);
                viewGroup.addView(textView2, new ViewGroup.LayoutParams(-1, -2));
                viewGroup.setVisibility(0);
                viewGroup.setScrollBarDefaultDelayBeforeFade(500);
            }
            if (Helper.sDebug) {
                Slog.d(TAG, "on constructor: title=" + ((Object) this.mTitle) + ", subTitle=" + ((Object) description));
            }
        }
        TextView textView3 = (TextView) inflate.findViewById(R.id.by_common_header);
        int negativeActionStyle = saveInfo.getNegativeActionStyle();
        if (negativeActionStyle == 1) {
            textView3.setText(R.string.autofill_update_title_with_3types);
        } else if (negativeActionStyle == 2) {
            textView3.setText(R.string.autofill_update_title);
        } else {
            textView3.setText(R.string.autofill_update_title_with_2types);
        }
        textView3.setOnClickListener(new View.OnClickListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SaveUi.this.lambda$new$0(saveInfo, view);
            }
        });
        TextView textView4 = (TextView) inflate.findViewById(R.id.by_org_header);
        if (saveInfo.getPositiveActionStyle() == 1) {
            textView4.setText(R.string.autofill_save_title_with_3types);
        } else if (z2) {
            textView4.setText(R.string.biometric_error_hw_unavailable);
        }
        textView4.setOnClickListener(new View.OnClickListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SaveUi.this.lambda$new$1(view);
            }
        });
        Dialog dialog = new Dialog(contextThemeWrapper, i);
        this.mDialog = dialog;
        dialog.setContentView(inflate);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                SaveUi.this.lambda$new$2(dialogInterface);
            }
        });
        Window window = dialog.getWindow();
        window.setType(2038);
        window.addFlags(131074);
        window.setDimAmount(0.6f);
        window.addPrivateFlags(16);
        window.setSoftInputMode(32);
        window.setGravity(81);
        window.setCloseOnTouchOutside(true);
        WindowManager.LayoutParams attributes = window.getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        attributes.width = Math.min(displayMetrics.widthPixels, contextThemeWrapper.getResources().getDimensionPixelSize(R.dimen.button_bar_layout_end_padding));
        attributes.accessibilityTitle = contextThemeWrapper.getString(R.string.autofill_this_form);
        attributes.windowAnimations = R.style.CarAction1.Dark;
        attributes.setTrustedOverlay();
        show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(SaveInfo saveInfo, View view) {
        this.mListener.onCancel(saveInfo.getNegativeActionListener());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        this.mListener.onSave();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(DialogInterface dialogInterface) {
        this.mListener.onCancel(null);
    }

    private boolean applyCustomDescription(Context context, View view, ValueFinder valueFinder, SaveInfo saveInfo) {
        CustomDescription customDescription = saveInfo.getCustomDescription();
        if (customDescription == null) {
            return false;
        }
        writeLog(1129);
        RemoteViews sanitizeRemoteView = Helper.sanitizeRemoteView(customDescription.getPresentation());
        if (sanitizeRemoteView == null) {
            Slog.w(TAG, "No remote view on custom description");
            return false;
        }
        ArrayList transformations = customDescription.getTransformations();
        if (Helper.sVerbose) {
            Slog.v(TAG, "applyCustomDescription(): transformations = " + transformations);
        }
        if (transformations != null && !InternalTransformation.batchApply(valueFinder, sanitizeRemoteView, transformations)) {
            Slog.w(TAG, "could not apply main transformations on custom description");
            return false;
        }
        try {
            View applyWithTheme = sanitizeRemoteView.applyWithTheme(context, null, new RemoteViews.InteractionHandler() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda4
                public final boolean onInteraction(View view2, PendingIntent pendingIntent, RemoteViews.RemoteResponse remoteResponse) {
                    boolean lambda$applyCustomDescription$3;
                    lambda$applyCustomDescription$3 = SaveUi.this.lambda$applyCustomDescription$3(view2, pendingIntent, remoteResponse);
                    return lambda$applyCustomDescription$3;
                }
            }, this.mThemeId);
            ArrayList updates = customDescription.getUpdates();
            if (Helper.sVerbose) {
                Slog.v(TAG, "applyCustomDescription(): view = " + applyWithTheme + " updates=" + updates);
            }
            if (updates != null) {
                int size = updates.size();
                if (Helper.sDebug) {
                    Slog.d(TAG, "custom description has " + size + " batch updates");
                }
                for (int i = 0; i < size; i++) {
                    Pair pair = (Pair) updates.get(i);
                    InternalValidator internalValidator = (InternalValidator) pair.first;
                    if (internalValidator != null && internalValidator.isValid(valueFinder)) {
                        BatchUpdates batchUpdates = (BatchUpdates) pair.second;
                        RemoteViews sanitizeRemoteView2 = Helper.sanitizeRemoteView(batchUpdates.getUpdates());
                        if (sanitizeRemoteView2 != null) {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "Applying template updates for batch update #" + i);
                            }
                            sanitizeRemoteView2.reapply(context, applyWithTheme);
                        }
                        ArrayList transformations2 = batchUpdates.getTransformations();
                        if (transformations2 == null) {
                            continue;
                        } else {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "Applying child transformation for batch update #" + i + ": " + transformations2);
                            }
                            if (!InternalTransformation.batchApply(valueFinder, sanitizeRemoteView, transformations2)) {
                                Slog.w(TAG, "Could not apply child transformation for batch update #" + i + ": " + transformations2);
                                return false;
                            }
                            sanitizeRemoteView.reapply(context, applyWithTheme);
                        }
                    }
                    if (Helper.sDebug) {
                        Slog.d(TAG, "Skipping batch update #" + i);
                    }
                }
            }
            SparseArray actions = customDescription.getActions();
            if (actions != null) {
                int size2 = actions.size();
                if (Helper.sDebug) {
                    Slog.d(TAG, "custom description has " + size2 + " actions");
                }
                if (applyWithTheme instanceof ViewGroup) {
                    final ViewGroup viewGroup = (ViewGroup) applyWithTheme;
                    for (int i2 = 0; i2 < size2; i2++) {
                        int keyAt = actions.keyAt(i2);
                        final InternalOnClickAction internalOnClickAction = (InternalOnClickAction) actions.valueAt(i2);
                        View findViewById = viewGroup.findViewById(keyAt);
                        if (findViewById == null) {
                            Slog.w(TAG, "Ignoring action " + internalOnClickAction + " for view " + keyAt + " because it's not on " + viewGroup);
                        } else {
                            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda5
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view2) {
                                    SaveUi.lambda$applyCustomDescription$4(internalOnClickAction, viewGroup, view2);
                                }
                            });
                        }
                    }
                } else {
                    Slog.w(TAG, "cannot apply actions because custom description root is not a ViewGroup: " + applyWithTheme);
                }
            }
            applyTextViewStyle(applyWithTheme);
            ViewGroup viewGroup2 = (ViewGroup) view.findViewById(R.id.buttons);
            viewGroup2.addView(applyWithTheme);
            viewGroup2.setVisibility(0);
            viewGroup2.setScrollBarDefaultDelayBeforeFade(500);
            return true;
        } catch (Exception e) {
            Slog.e(TAG, "Error applying custom description. ", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyCustomDescription$3(View view, PendingIntent pendingIntent, RemoteViews.RemoteResponse remoteResponse) {
        Intent intent = (Intent) remoteResponse.getLaunchOptions(view).first;
        if (!isValidLink(pendingIntent, intent)) {
            LogMaker newLogMaker = newLogMaker(1132, this.mType);
            newLogMaker.setType(0);
            this.mMetricsLogger.write(newLogMaker);
            return false;
        }
        startIntentSenderWithRestore(pendingIntent, intent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyCustomDescription$4(InternalOnClickAction internalOnClickAction, ViewGroup viewGroup, View view) {
        if (Helper.sVerbose) {
            Slog.v(TAG, "Applying " + internalOnClickAction + " after " + view + " was clicked");
        }
        internalOnClickAction.onClick(viewGroup);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startIntentSenderWithRestore(PendingIntent pendingIntent, Intent intent) {
        if (Helper.sVerbose) {
            Slog.v(TAG, "Intercepting custom description intent");
        }
        IBinder token = this.mPendingUi.getToken();
        intent.putExtra("android.view.autofill.extra.RESTORE_SESSION_TOKEN", token);
        this.mListener.startIntentSender(pendingIntent.getIntentSender(), intent);
        this.mPendingUi.setState(2);
        if (Helper.sDebug) {
            Slog.d(TAG, "hiding UI until restored with token " + token);
        }
        hide();
        LogMaker newLogMaker = newLogMaker(1132, this.mType);
        newLogMaker.setType(1);
        this.mMetricsLogger.write(newLogMaker);
    }

    private void applyTextViewStyle(View view) {
        final ArrayList arrayList = new ArrayList();
        view.findViewByPredicate(new Predicate() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$applyTextViewStyle$5;
                lambda$applyTextViewStyle$5 = SaveUi.lambda$applyTextViewStyle$5(arrayList, (View) obj);
                return lambda$applyTextViewStyle$5;
            }
        });
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            applyMovementMethodIfNeed((TextView) arrayList.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$applyTextViewStyle$5(List list, View view) {
        if (!(view instanceof TextView)) {
            return false;
        }
        list.add((TextView) view);
        return false;
    }

    private void applyMovementMethodIfNeed(TextView textView) {
        CharSequence text = textView.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (ArrayUtils.isEmpty((ClickableSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ClickableSpan.class))) {
            return;
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setServiceIcon(Context context, View view, Drawable drawable) {
        ImageView imageView = (ImageView) view.findViewById(R.id.by_common);
        context.getResources();
        imageView.setImageDrawable(drawable);
    }

    private static boolean isValidLink(PendingIntent pendingIntent, Intent intent) {
        if (pendingIntent == null) {
            Slog.w(TAG, "isValidLink(): custom description without pending intent");
            return false;
        }
        if (!pendingIntent.isActivity()) {
            Slog.w(TAG, "isValidLink(): pending intent not for activity");
            return false;
        }
        if (intent != null) {
            return true;
        }
        Slog.w(TAG, "isValidLink(): no intent");
        return false;
    }

    private LogMaker newLogMaker(int i, int i2) {
        return newLogMaker(i).addTaggedData(1130, Integer.valueOf(i2));
    }

    private LogMaker newLogMaker(int i) {
        return Helper.newLogMaker(i, this.mComponentName, this.mServicePackageName, this.mPendingUi.sessionId, this.mCompatMode);
    }

    private void writeLog(int i) {
        this.mMetricsLogger.write(newLogMaker(i, this.mType));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPendingUi(int i, IBinder iBinder) {
        if (!this.mPendingUi.matches(iBinder)) {
            Slog.w(TAG, "restore(" + i + "): got token " + iBinder + " instead of " + this.mPendingUi.getToken());
            return;
        }
        LogMaker newLogMaker = newLogMaker(1134);
        try {
            if (i == 1) {
                newLogMaker.setType(5);
                if (Helper.sDebug) {
                    Slog.d(TAG, "Cancelling pending save dialog for " + iBinder);
                }
                hide();
            } else if (i == 2) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "Restoring save dialog for " + iBinder);
                }
                newLogMaker.setType(1);
                show();
            } else {
                newLogMaker.setType(11);
                Slog.w(TAG, "restore(): invalid operation " + i);
            }
            this.mMetricsLogger.write(newLogMaker);
            this.mPendingUi.setState(4);
        } catch (Throwable th) {
            this.mMetricsLogger.write(newLogMaker);
            throw th;
        }
    }

    private void show() {
        Slog.i(TAG, "Showing save dialog: " + ((Object) this.mTitle));
        this.mDialog.show();
        this.mOverlayControl.hideOverlays();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingUi hide() {
        if (Helper.sVerbose) {
            Slog.v(TAG, "Hiding save dialog.");
        }
        try {
            this.mDialog.hide();
            this.mOverlayControl.showOverlays();
            return this.mPendingUi;
        } catch (Throwable th) {
            this.mOverlayControl.showOverlays();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isShowing() {
        return this.mDialog.isShowing();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        try {
            if (Helper.sDebug) {
                Slog.d(TAG, "destroy()");
            }
            throwIfDestroyed();
            this.mListener.onDestroy();
            this.mHandler.removeCallbacksAndMessages(this.mListener);
            this.mDialog.dismiss();
            this.mDestroyed = true;
        } finally {
            this.mOverlayControl.showOverlays();
        }
    }

    private void throwIfDestroyed() {
        if (this.mDestroyed) {
            throw new IllegalStateException("cannot interact with a destroyed instance");
        }
    }

    public String toString() {
        CharSequence charSequence = this.mTitle;
        return charSequence == null ? "NO TITLE" : charSequence.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("title: ");
        printWriter.println(this.mTitle);
        printWriter.print(str);
        printWriter.print("subtitle: ");
        printWriter.println(this.mSubTitle);
        printWriter.print(str);
        printWriter.print("pendingUi: ");
        printWriter.println(this.mPendingUi);
        printWriter.print(str);
        printWriter.print("service: ");
        printWriter.println(this.mServicePackageName);
        printWriter.print(str);
        printWriter.print("app: ");
        printWriter.println(this.mComponentName.toShortString());
        printWriter.print(str);
        printWriter.print("compat mode: ");
        printWriter.println(this.mCompatMode);
        printWriter.print(str);
        printWriter.print("theme id: ");
        printWriter.print(this.mThemeId);
        int i = this.mThemeId;
        if (i == 16974809) {
            printWriter.println(" (dark)");
        } else if (i == 16974820) {
            printWriter.println(" (light)");
        } else {
            printWriter.println("(UNKNOWN_MODE)");
        }
        View decorView = this.mDialog.getWindow().getDecorView();
        int[] locationOnScreen = decorView.getLocationOnScreen();
        printWriter.print(str);
        printWriter.print("coordinates: ");
        printWriter.print('(');
        printWriter.print(locationOnScreen[0]);
        printWriter.print(',');
        printWriter.print(locationOnScreen[1]);
        printWriter.print(')');
        printWriter.print('(');
        printWriter.print(locationOnScreen[0] + decorView.getWidth());
        printWriter.print(',');
        printWriter.print(locationOnScreen[1] + decorView.getHeight());
        printWriter.println(')');
        printWriter.print(str);
        printWriter.print("destroyed: ");
        printWriter.println(this.mDestroyed);
    }
}
