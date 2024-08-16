package android.widget;

import android.R;
import android.app.ActivityThread;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.CursorAnchorInfo;
import android.widget.Editor;
import android.widget.IEditorWrapper;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.os.WaveformEffect;

/* loaded from: classes.dex */
public class EditorExtImpl implements IEditorExt {
    private static final int MENU_ITEM_ORDER_SELECT = 11;
    private static final int SOURCE_OPLUS_TOUCHPAD = 24578;
    private static final String TAG = EditorExtImpl.class.getSimpleName();
    private static final int VIBRATE_PERMISISON_DENIED = 2;
    private static final int VIBRATE_PERMISSION_GRANTED = 1;
    private static final int VIBRATE_PERMISSION_UNDEFIED = -1;
    private Editor mBase;
    private LinearmotorVibrator mLinearmotorVibrator;
    private IOplusCursorFeedbackManager mOplusCursorFeedbackManager;
    private OplusEditorUtils mOplusEditorUtils;
    private boolean mIsFousedBeforeTouch = false;
    private int mLastOffset = -1;
    private int mVibratePermission = -1;

    public EditorExtImpl(Object base) {
        this.mBase = (Editor) base;
    }

    public void setEditorUtils(Editor editor) {
        this.mOplusEditorUtils = new OplusEditorUtils(editor);
    }

    public boolean[] handleCursorControllersEnabled(boolean insertionControllerEnabled, boolean selectionControllerEnabled) {
        boolean[] controllersEnabled = ((IOplusFloatingToolbarUtil) OplusFeatureCache.getOrCreate(IOplusFloatingToolbarUtil.DEFAULT, new Object[0])).handleCursorControllersEnabled(this.mOplusEditorUtils.isMenuEnabled(), this.mOplusEditorUtils.isInsertMenuEnabled(), this.mOplusEditorUtils.isSelectMenuEnabled(), insertionControllerEnabled, selectionControllerEnabled);
        return controllersEnabled;
    }

    public boolean needAllSelected() {
        return ((IOplusFloatingToolbarUtil) OplusFeatureCache.getOrCreate(IOplusFloatingToolbarUtil.DEFAULT, new Object[0])).needAllSelected(this.mOplusEditorUtils.needAllSelected());
    }

    public boolean selectAllText(TextView textView) {
        return textView.selectAllText();
    }

    public void setFocused(boolean value) {
        this.mIsFousedBeforeTouch = value;
    }

    public boolean needHook() {
        return ((IOplusFloatingToolbarUtil) OplusFeatureCache.getOrCreate(IOplusFloatingToolbarUtil.DEFAULT, new Object[0])).needHook();
    }

    public void setLastOffset(int value) {
        this.mLastOffset = value;
    }

    public void startInsertionActionMode(ActionMode textActionMode, int offset, Editor editor) {
    }

    public void layout(int shadowViewWidth, int shadowViewHeight, CharSequence text, TextView shadowView) {
        if (shadowViewWidth == 0 && text != null && text.length() > 0) {
            shadowViewWidth = 1;
            shadowViewHeight = 1;
        }
        shadowView.layout(0, 0, shadowViewWidth, shadowViewHeight);
    }

    public void setBackground(ListView suggestionListView, ColorDrawable colorDrawable) {
        suggestionListView.setBackground(colorDrawable);
    }

    public void updateSelectAllItem(Menu menu, TextView textView) {
        ((IOplusFloatingToolbarUtil) OplusFeatureCache.getOrCreate(IOplusFloatingToolbarUtil.DEFAULT, new Object[0])).updateSelectAllItem(menu, textView, 11);
    }

    public void toHandleItemClicked(int id, TextView textView, Editor editor) {
        if (needHook()) {
            handleItemClicked(id, textView, editor);
        }
    }

    private void handleItemClicked(int id, TextView textView, Editor editor) {
        Context context = textView.getContext();
        switch (id) {
            case R.id.cut:
                Toast.makeText(context, context.getString(201588937), 0).show();
                return;
            case R.id.copy:
                Toast.makeText(context, context.getString(201588936), 0).show();
                return;
            case 201457773:
                int offset = textView.getSelectionStart();
                setMinMaxOffset(offset, editor.getSelectionController());
                editor.selectCurrentWord();
                return;
            default:
                return;
        }
    }

    public void setMinMaxOffset(int offset, Editor.SelectionModifierCursorController controller) {
        IEditorWrapper.ISelectionModifierCursorControllerWrapper wrapper = controller.getWrapper();
        if (wrapper != null) {
            wrapper.setMaxTouchOffset(offset);
            wrapper.setMinTouchOffset(offset);
        }
    }

    public boolean setSearchMenuItem(int index, Intent intent, CharSequence title, ResolveInfo resolveInfo, Menu menu) {
        return ((IOplusFloatingToolbarUtil) OplusFeatureCache.getOrCreate(IOplusFloatingToolbarUtil.DEFAULT, new Object[0])).setSearchMenuItem(index, intent, title, resolveInfo, menu);
    }

    public View.DragShadowBuilder getOplusTextThumbnailBuilder(View textview, String text) {
        return ((IOplusDragTextShadowHelper) OplusFeatureCache.getOrCreate(IOplusDragTextShadowHelper.DEFAULT, new Object[0])).getOplusTextThumbnailBuilder(textview, text);
    }

    private IOplusCursorFeedbackManager getOplusCursorFeedbackManager() {
        if (this.mOplusCursorFeedbackManager == null) {
            this.mOplusCursorFeedbackManager = (IOplusCursorFeedbackManager) OplusFeatureCache.getOrCreate(IOplusCursorFeedbackManager.DEFAULT, new Object[0]);
        }
        return this.mOplusCursorFeedbackManager;
    }

    public boolean refreshCursorRenderTime(Editor editor) {
        return getOplusCursorFeedbackManager().refreshCursorRenderTime(editor);
    }

    public int getExtraCursorWidth(Editor editor) {
        return getOplusCursorFeedbackManager().getExtraCursorWidth(editor);
    }

    public int getExtraLeftOffset(Editor editor) {
        return getOplusCursorFeedbackManager().getExtraLeftOffset(editor);
    }

    public void handleKeyCodeDelDown(Editor editor, KeyEvent keyEvent) {
        getOplusCursorFeedbackManager().handleKeyCodeDelDown(editor, keyEvent);
    }

    public void handleKeyCodeDelUp(Editor editor, KeyEvent keyEvent) {
        getOplusCursorFeedbackManager().handleKeyCodeDelUp(editor, keyEvent);
    }

    public void editorDetachFromWindow(Editor editor) {
        getOplusCursorFeedbackManager().editorDetachFromWindow(editor);
    }

    private IOplusReorderActionMenuManager getOplusReorderActionMenuManager() {
        return (IOplusReorderActionMenuManager) OplusFeatureCache.getOrCreate(IOplusReorderActionMenuManager.DEFAULT, new Object[0]);
    }

    public void onInitializeReorderActionMenu(Menu menu, Context context, TextView textView) {
        getOplusReorderActionMenuManager().onInitializeReorderActionMenu(menu, context, textView);
    }

    public boolean isOplusReorderActionMenu(Intent intent) {
        return getOplusReorderActionMenuManager().isOplusReorderActionMenu(intent);
    }

    public void hookFireIntent(TextView textview, Intent intent) {
        getOplusReorderActionMenuManager().hookFireIntent(textview, intent);
    }

    public boolean raiseOplusMenuPriority(int order, CharSequence label, Intent intent, ResolveInfo resolveInfo, Menu menu) {
        return getOplusReorderActionMenuManager().raiseOplusMenuPriority(order, label, intent, resolveInfo, menu);
    }

    public boolean performVibrateForCursorOffsetChange(boolean fromTouch, boolean textHandleHapticEnabled, TextView textView) {
        getVibrator(textView.getContext());
        if (this.mLinearmotorVibrator == null) {
            return true;
        }
        if (fromTouch) {
            vibrateForCursorMove(textView.getContext());
            return false;
        }
        return false;
    }

    public boolean performVibrateForCursorDragging(boolean textHandleHapticEnabled, TextView textView) {
        getVibrator(textView.getContext());
        if (this.mLinearmotorVibrator == null) {
            return true;
        }
        vibrateForCursorMove(textView.getContext());
        return false;
    }

    public boolean performVibrateForSelectionUpdate(boolean fromTouch, boolean textHandleHapticEnabled, TextView textView, int offset) {
        getVibrator(textView.getContext());
        if (this.mLinearmotorVibrator == null) {
            return true;
        }
        if (fromTouch) {
            vibrateForCursorMove(textView.getContext());
            return false;
        }
        return false;
    }

    private LinearmotorVibrator getVibrator(Context context) {
        if (this.mLinearmotorVibrator == null) {
            this.mLinearmotorVibrator = (LinearmotorVibrator) context.getSystemService(LinearmotorVibrator.LINEARMOTORVIBRATOR_SERVICE);
        }
        return this.mLinearmotorVibrator;
    }

    private void vibrateForCursorMove(Context context) {
        if (this.mVibratePermission == -1) {
            if (context.checkSelfPermission("android.permission.VIBRATE") == 0) {
                this.mVibratePermission = 1;
            } else {
                this.mVibratePermission = 2;
            }
        }
        if (this.mVibratePermission == 2) {
            Log.d(TAG, "need VIBRATE permission to vibrateForCursorMove");
            return;
        }
        try {
            if (Settings.System.getInt(context.getContentResolver(), "haptic_feedback_enabled") == 0) {
                Log.d(TAG, "haptic feedback disabled, do not vibrateForCursorMove");
                return;
            }
            if (this.mLinearmotorVibrator != null && this.mBase.getTextView().isHapticFeedbackEnabled()) {
                try {
                    WaveformEffect waveformEffect = new WaveformEffect.Builder().setEffectType(0).setUsageHint(13).setAsynchronous(true).build();
                    this.mLinearmotorVibrator.vibrate(waveformEffect);
                } catch (Exception e) {
                    Log.d(TAG, "failed to vibrateForCursorMove " + e.getMessage());
                }
            }
        } catch (Exception e2) {
            Log.d(TAG, "failed to read HAPTIC_FEEDBACK_ENABLED when vibrateForCursorMove " + e2.getMessage());
        }
    }

    public void scaleCursorAnchorInfo(View textView, CursorAnchorInfo.Builder builder, float insertionMarkX, float insertionMarkerTop, float insertionMarkBaseline, float insertionMarkBottom, int flag) {
        float scaleX = 1.0f;
        if ("com.coloros.note".equals(ActivityThread.currentOpPackageName())) {
            Object scaleTag = textView.getTag();
            if (scaleTag instanceof Float) {
                scaleX = ((Float) scaleTag).floatValue();
            }
        }
        builder.setInsertionMarkerLocation(insertionMarkX * scaleX, insertionMarkerTop * scaleX, insertionMarkBaseline * scaleX, insertionMarkBottom * scaleX, flag);
    }

    public boolean skipTouchPadButtonState(MotionEvent event) {
        return event != null && event.getSource() == SOURCE_OPLUS_TOUCHPAD;
    }

    public boolean isFromStylus(Editor editor, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        return event.getToolType(pointerIndex) == 2;
    }
}
