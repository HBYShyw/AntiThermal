package com.android.server.inputmethod;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.server.LocalServices;
import com.android.server.inputmethod.InputMethodSubtypeSwitchingController;
import com.android.server.inputmethod.InputMethodUtils;
import com.android.server.wm.WindowManagerInternal;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InputMethodMenuController {
    private static final String TAG = "InputMethodMenuController";
    private AlertDialog.Builder mDialogBuilder;

    @GuardedBy({"ImfLock.class"})
    private InputMethodDialogWindowContext mDialogWindowContext;
    private InputMethodInfo[] mIms;
    private final ArrayMap<String, InputMethodInfo> mMethodMap;
    private final InputMethodManagerService mService;
    private final InputMethodUtils.InputMethodSettings mSettings;
    private boolean mShowImeWithHardKeyboard;
    private int[] mSubtypeIds;
    private final InputMethodSubtypeSwitchingController mSwitchingController;
    private AlertDialog mSwitchingDialog;
    private View mSwitchingDialogTitleView;
    private InputMethodMenuControllerWrapper mImmcWrapper = new InputMethodMenuControllerWrapper();
    private final WindowManagerInternal mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputMethodMenuController(InputMethodManagerService inputMethodManagerService) {
        this.mService = inputMethodManagerService;
        this.mSettings = inputMethodManagerService.mSettings;
        this.mSwitchingController = inputMethodManagerService.mSwitchingController;
        this.mMethodMap = inputMethodManagerService.mMethodMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showInputMethodMenu(boolean z, int i) {
        int i2;
        InputMethodSubtype currentInputMethodSubtypeLocked;
        if (InputMethodManagerService.DEBUG) {
            Slog.v(TAG, "Show switching menu. showAuxSubtypes=" + z);
        }
        boolean isScreenLocked = isScreenLocked();
        String selectedInputMethod = this.mSettings.getSelectedInputMethod();
        int selectedInputMethodSubtypeId = this.mSettings.getSelectedInputMethodSubtypeId(selectedInputMethod);
        if (InputMethodManagerService.DEBUG) {
            Slog.v(TAG, "Current IME: " + selectedInputMethod);
        }
        synchronized (ImfLock.class) {
            List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> sortedInputMethodAndSubtypeListForImeMenuLocked = this.mSwitchingController.getSortedInputMethodAndSubtypeListForImeMenuLocked(z, isScreenLocked);
            if (sortedInputMethodAndSubtypeListForImeMenuLocked.isEmpty()) {
                return;
            }
            hideInputMethodMenuLocked();
            if (selectedInputMethodSubtypeId == -1 && (currentInputMethodSubtypeLocked = this.mService.getCurrentInputMethodSubtypeLocked()) != null) {
                selectedInputMethodSubtypeId = SubtypeUtils.getSubtypeIdFromHashCode(this.mMethodMap.get(this.mService.getSelectedMethodIdLocked()), currentInputMethodSubtypeLocked.hashCode());
            }
            int size = sortedInputMethodAndSubtypeListForImeMenuLocked.size();
            this.mIms = new InputMethodInfo[size];
            this.mSubtypeIds = new int[size];
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < size; i5++) {
                InputMethodSubtypeSwitchingController.ImeSubtypeListItem imeSubtypeListItem = sortedInputMethodAndSubtypeListForImeMenuLocked.get(i5);
                InputMethodInfo[] inputMethodInfoArr = this.mIms;
                InputMethodInfo inputMethodInfo = imeSubtypeListItem.mImi;
                inputMethodInfoArr[i5] = inputMethodInfo;
                this.mSubtypeIds[i5] = imeSubtypeListItem.mSubtypeId;
                if (inputMethodInfo.getId().equals(selectedInputMethod) && ((i2 = this.mSubtypeIds[i5]) == -1 || ((selectedInputMethodSubtypeId == -1 && i2 == 0) || i2 == selectedInputMethodSubtypeId))) {
                    i4 = i5;
                }
            }
            if (this.mDialogWindowContext == null) {
                this.mDialogWindowContext = new InputMethodDialogWindowContext();
            }
            Context context = this.mDialogWindowContext.get(i);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            this.mDialogBuilder = builder;
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    InputMethodMenuController.this.lambda$showInputMethodMenu$0(dialogInterface);
                }
            });
            Context context2 = this.mDialogBuilder.getContext();
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(null, R.styleable.DialogPreference, android.R.attr.alertDialogStyle, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(2);
            obtainStyledAttributes.recycle();
            this.mDialogBuilder.setIcon(drawable);
            View inflate = ((LayoutInflater) context2.getSystemService(LayoutInflater.class)).inflate(android.R.layout.list_menu_item_radio, (ViewGroup) null);
            this.mDialogBuilder.setCustomTitle(inflate);
            this.mSwitchingDialogTitleView = inflate;
            View findViewById = inflate.findViewById(android.R.id.input_separator);
            if (!this.mWindowManagerInternal.isHardKeyboardAvailable()) {
                i3 = 8;
            }
            findViewById.setVisibility(i3);
            Switch r4 = (Switch) this.mSwitchingDialogTitleView.findViewById(android.R.id.insertion_handle);
            r4.setChecked(this.mShowImeWithHardKeyboard);
            r4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    InputMethodMenuController.this.lambda$showInputMethodMenu$1(compoundButton, z2);
                }
            });
            final ImeSubtypeListAdapter imeSubtypeListAdapter = new ImeSubtypeListAdapter(context2, android.R.layout.locale_picker_item, sortedInputMethodAndSubtypeListForImeMenuLocked, i4);
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i6) {
                    InputMethodMenuController.this.lambda$showInputMethodMenu$2(imeSubtypeListAdapter, dialogInterface, i6);
                }
            };
            this.mDialogBuilder.setSingleChoiceItems(imeSubtypeListAdapter, i4, onClickListener);
            AlertDialog create = this.mDialogBuilder.create();
            this.mSwitchingDialog = create;
            create.setCanceledOnTouchOutside(true);
            Window window = this.mSwitchingDialog.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            window.setType(2012);
            window.setHideOverlayWindows(true);
            attributes.token = context.getWindowContextToken();
            attributes.privateFlags |= 16;
            attributes.setTitle("Select input method");
            window.setAttributes(attributes);
            this.mService.updateSystemUiLocked();
            this.mService.sendOnNavButtonFlagsChangedLocked();
            if (this.mImmcWrapper.getExtImpl().showInputMethodMenu(this.mService.mContext, this.mSettings.getCurrentUserId(), i, attributes.token, sortedInputMethodAndSubtypeListForImeMenuLocked, i4, this.mWindowManagerInternal.isHardKeyboardAvailable(), this.mShowImeWithHardKeyboard, new CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda3
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    InputMethodMenuController.this.lambda$showInputMethodMenu$3(compoundButton, z2);
                }
            }, onClickListener, new DialogInterface.OnCancelListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    InputMethodMenuController.this.lambda$showInputMethodMenu$4(dialogInterface);
                }
            })) {
                return;
            }
            this.mSwitchingDialog.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenu$0(DialogInterface dialogInterface) {
        hideInputMethodMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenu$1(CompoundButton compoundButton, boolean z) {
        this.mSettings.setShowImeWithHardKeyboard(z);
        hideInputMethodMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenu$2(ImeSubtypeListAdapter imeSubtypeListAdapter, DialogInterface dialogInterface, int i) {
        int[] iArr;
        synchronized (ImfLock.class) {
            InputMethodInfo[] inputMethodInfoArr = this.mIms;
            if (inputMethodInfoArr != null && inputMethodInfoArr.length > i && (iArr = this.mSubtypeIds) != null && iArr.length > i) {
                InputMethodInfo inputMethodInfo = inputMethodInfoArr[i];
                int i2 = iArr[i];
                int i3 = imeSubtypeListAdapter.mCheckedItem;
                imeSubtypeListAdapter.mCheckedItem = i;
                imeSubtypeListAdapter.notifyDataSetChanged();
                hideInputMethodMenu();
                if (inputMethodInfo != null) {
                    if (i2 < 0 || i2 >= inputMethodInfo.getSubtypeCount()) {
                        i2 = -1;
                    }
                    this.mService.setInputMethodLocked(inputMethodInfo.getId(), i2);
                    this.mService.getWrapper().getExtImpl().onInputMethodPickByUser(imeSubtypeListAdapter.getItem(i3), imeSubtypeListAdapter.getItem(i));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenu$3(CompoundButton compoundButton, boolean z) {
        this.mSettings.setShowImeWithHardKeyboard(z);
        hideInputMethodMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenu$4(DialogInterface dialogInterface) {
        hideInputMethodMenu();
    }

    private boolean isScreenLocked() {
        return this.mWindowManagerInternal.isKeyguardLocked() && this.mWindowManagerInternal.isKeyguardSecure(this.mSettings.getCurrentUserId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateKeyboardFromSettingsLocked() {
        this.mShowImeWithHardKeyboard = this.mSettings.isShowImeWithHardKeyboardEnabled();
        this.mImmcWrapper.getExtImpl().setShowImeWithHardKeyboard(this.mShowImeWithHardKeyboard);
        AlertDialog alertDialog = this.mSwitchingDialog;
        if (alertDialog == null || this.mSwitchingDialogTitleView == null || !alertDialog.isShowing()) {
            return;
        }
        ((Switch) this.mSwitchingDialogTitleView.findViewById(android.R.id.insertion_handle)).setChecked(this.mShowImeWithHardKeyboard);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideInputMethodMenu() {
        synchronized (ImfLock.class) {
            hideInputMethodMenuLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void hideInputMethodMenuLocked() {
        if (InputMethodManagerService.DEBUG) {
            Slog.v(TAG, "Hide switching menu");
        }
        if (this.mSwitchingDialog != null) {
            this.mImmcWrapper.getExtImpl().hideInputMethodMenu();
            this.mSwitchingDialog.dismiss();
            this.mSwitchingDialog = null;
            this.mSwitchingDialogTitleView = null;
            this.mService.updateSystemUiLocked();
            this.mService.sendOnNavButtonFlagsChangedLocked();
            this.mDialogBuilder = null;
            this.mIms = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AlertDialog getSwitchingDialogLocked() {
        return this.mSwitchingDialog;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getShowImeWithHardKeyboard() {
        return this.mShowImeWithHardKeyboard;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isisInputMethodPickerShownForTestLocked() {
        if (this.mSwitchingDialog == null) {
            return false;
        }
        if (this.mImmcWrapper.getExtImpl().isInputMethodMenuShowing()) {
            return true;
        }
        return this.mSwitchingDialog.isShowing();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleHardKeyboardStatusChange(boolean z) {
        if (InputMethodManagerService.DEBUG) {
            Slog.w(TAG, "HardKeyboardStatusChanged: available=" + z);
        }
        synchronized (ImfLock.class) {
            this.mImmcWrapper.getExtImpl().setShowHardKeyboardSwitch(z);
            AlertDialog alertDialog = this.mSwitchingDialog;
            if (alertDialog != null && this.mSwitchingDialogTitleView != null && alertDialog.isShowing()) {
                this.mSwitchingDialogTitleView.findViewById(android.R.id.input_separator).setVisibility(z ? 0 : 8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ImeSubtypeListAdapter extends ArrayAdapter<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> {
        public int mCheckedItem;
        private final LayoutInflater mInflater;
        private final List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> mItemsList;
        private final int mTextViewResourceId;

        private ImeSubtypeListAdapter(Context context, int i, List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> list, int i2) {
            super(context, i, list);
            this.mTextViewResourceId = i;
            this.mItemsList = list;
            this.mCheckedItem = i2;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(this.mTextViewResourceId, (ViewGroup) null);
            }
            if (i >= 0 && i < this.mItemsList.size()) {
                InputMethodSubtypeSwitchingController.ImeSubtypeListItem imeSubtypeListItem = this.mItemsList.get(i);
                CharSequence charSequence = imeSubtypeListItem.mImeName;
                CharSequence charSequence2 = imeSubtypeListItem.mSubtypeName;
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                if (TextUtils.isEmpty(charSequence2)) {
                    textView.setText(charSequence);
                    textView2.setVisibility(8);
                } else {
                    textView.setText(charSequence2);
                    textView2.setText(charSequence);
                    textView2.setVisibility(0);
                }
                ((RadioButton) view.findViewById(android.R.id.serial_number_header)).setChecked(i == this.mCheckedItem);
            }
            return view;
        }
    }

    public IInputMethodMenuControllerWrapper getWrapper() {
        return this.mImmcWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class InputMethodMenuControllerWrapper implements IInputMethodMenuControllerWrapper {
        private final IInputMethodMenuControllerExt mImmcExt;

        private InputMethodMenuControllerWrapper() {
            this.mImmcExt = (IInputMethodMenuControllerExt) ExtLoader.type(IInputMethodMenuControllerExt.class).base(InputMethodMenuController.this).create();
        }

        public IInputMethodMenuControllerExt getExtImpl() {
            return this.mImmcExt;
        }
    }
}
