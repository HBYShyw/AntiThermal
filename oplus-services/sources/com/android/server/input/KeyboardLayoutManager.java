package com.android.server.input;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.hardware.input.InputDeviceIdentifier;
import android.hardware.input.InputManager;
import android.hardware.input.KeyboardLayout;
import android.icu.lang.UScript;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.InputDevice;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.InputMethodSubtypeHandle;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.XmlUtils;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.slice.SliceClientPermissions;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import libcore.io.Streams;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class KeyboardLayoutManager implements InputManager.InputDeviceListener {
    private static final String DEFAULT_KEYBOARD_LAYOUT = "com.android.inputdevices/com.android.inputdevices.InputDeviceReceiver/keyboard_layout_english_us";
    private static final int MSG_CURRENT_IME_INFO_CHANGED = 5;
    private static final int MSG_RELOAD_KEYBOARD_LAYOUTS = 3;
    private static final int MSG_SWITCH_KEYBOARD_LAYOUT = 2;
    private static final int MSG_UPDATE_EXISTING_DEVICES = 1;
    private static final int MSG_UPDATE_KEYBOARD_LAYOUTS = 4;
    private static final String VENDOR_ID = "22d9";
    private final Context mContext;

    @GuardedBy({"mImeInfoLock"})
    private ImeInfo mCurrentImeInfo;

    @GuardedBy({"mDataStore"})
    private final PersistentDataStore mDataStore;
    private final Handler mHandler;
    private final NativeInputManagerService mNative;
    private Toast mSwitchedKeyboardLayoutToast;
    private static final String TAG = "KeyboardLayoutManager";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final SparseArray<KeyboardConfiguration> mConfiguredKeyboards = new SparseArray<>();

    @GuardedBy({"mKeyboardLayoutCache"})
    private final Map<String, String> mKeyboardLayoutCache = new ArrayMap();
    private final Object mImeInfoLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface KeyboardLayoutVisitor {
        void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyboardLayoutManager(Context context, NativeInputManagerService nativeInputManagerService, PersistentDataStore persistentDataStore, Looper looper) {
        this.mContext = context;
        this.mNative = nativeInputManagerService;
        this.mDataStore = persistentDataStore;
        this.mHandler = new Handler(looper, new Handler.Callback() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda5
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                boolean handleMessage;
                handleMessage = KeyboardLayoutManager.this.handleMessage(message);
                return handleMessage;
            }
        }, true);
    }

    public void systemRunning() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.input.KeyboardLayoutManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                KeyboardLayoutManager.this.updateKeyboardLayouts();
            }
        }, intentFilter, null, this.mHandler);
        this.mHandler.sendEmptyMessage(4);
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        Objects.requireNonNull(inputManager);
        inputManager.registerInputDeviceListener(this, this.mHandler);
        this.mHandler.sendMessage(Message.obtain(this.mHandler, 1, inputManager.getInputDeviceIds()));
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(int i) {
        onInputDeviceChanged(i);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int i) {
        this.mConfiguredKeyboards.remove(i);
        maybeUpdateNotification();
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int i) {
        boolean selectedKeyboardLayouts;
        boolean z;
        InputDevice inputDevice = getInputDevice(i);
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return;
        }
        KeyboardConfiguration keyboardConfiguration = this.mConfiguredKeyboards.get(i);
        if (keyboardConfiguration == null) {
            keyboardConfiguration = new KeyboardConfiguration();
            this.mConfiguredKeyboards.put(i, keyboardConfiguration);
        }
        if (!useNewSettingsUi()) {
            synchronized (this.mDataStore) {
                String currentKeyboardLayoutForInputDevice = getCurrentKeyboardLayoutForInputDevice(inputDevice.getIdentifier());
                if (currentKeyboardLayoutForInputDevice == null) {
                    currentKeyboardLayoutForInputDevice = Integer.toHexString(inputDevice.getVendorId()).equals(VENDOR_ID) ? DEFAULT_KEYBOARD_LAYOUT : getDefaultKeyboardLayout(inputDevice);
                    if (currentKeyboardLayoutForInputDevice != null) {
                        setCurrentKeyboardLayoutForInputDevice(inputDevice.getIdentifier(), currentKeyboardLayoutForInputDevice);
                    }
                }
                keyboardConfiguration.setCurrentLayout(currentKeyboardLayoutForInputDevice);
                z = currentKeyboardLayoutForInputDevice == null;
            }
        } else {
            InputDeviceIdentifier identifier = inputDevice.getIdentifier();
            String layoutDescriptor = getLayoutDescriptor(identifier);
            HashSet hashSet = new HashSet();
            Iterator<ImeInfo> it = getImeInfoListForLayoutMapping().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ImeInfo next = it.next();
                String keyboardLayoutForInputDeviceInternal = getKeyboardLayoutForInputDeviceInternal(identifier, new ImeInfo(next.mUserId, next.mImeSubtypeHandle, next.mImeSubtype));
                if (keyboardLayoutForInputDeviceInternal == null) {
                    hashSet.clear();
                    break;
                }
                hashSet.add(keyboardLayoutForInputDeviceInternal);
            }
            if (DEBUG) {
                Slog.d(TAG, "Layouts selected for input device: " + identifier + " -> selectedLayouts: " + hashSet);
            }
            keyboardConfiguration.setConfiguredLayouts(hashSet);
            synchronized (this.mImeInfoLock) {
                String keyboardLayoutForInputDeviceInternal2 = getKeyboardLayoutForInputDeviceInternal(inputDevice.getIdentifier(), this.mCurrentImeInfo);
                if (!Objects.equals(keyboardLayoutForInputDeviceInternal2, keyboardConfiguration.getCurrentLayout())) {
                    keyboardConfiguration.setCurrentLayout(keyboardLayoutForInputDeviceInternal2);
                    this.mHandler.sendEmptyMessage(3);
                }
            }
            synchronized (this.mDataStore) {
                try {
                    selectedKeyboardLayouts = this.mDataStore.setSelectedKeyboardLayouts(layoutDescriptor, hashSet);
                } finally {
                    this.mDataStore.saveIfNeeded();
                }
            }
            z = selectedKeyboardLayouts;
        }
        if (z) {
            maybeUpdateNotification();
        }
    }

    private String getDefaultKeyboardLayout(final InputDevice inputDevice) {
        final Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (TextUtils.isEmpty(locale.getLanguage())) {
            return null;
        }
        final ArrayList<KeyboardLayout> arrayList = new ArrayList();
        visitAllKeyboardLayouts(new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda0
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout) {
                KeyboardLayoutManager.lambda$getDefaultKeyboardLayout$0(inputDevice, locale, arrayList, resources, i, keyboardLayout);
            }
        });
        if (arrayList.isEmpty()) {
            return null;
        }
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        while (true) {
            if (it.hasNext()) {
                KeyboardLayout keyboardLayout = (KeyboardLayout) it.next();
                LocaleList locales = keyboardLayout.getLocales();
                for (int i = 0; i < locales.size(); i++) {
                    Locale locale2 = locales.get(i);
                    if (locale2 != null && locale2.getCountry().equals(locale.getCountry()) && locale2.getVariant().equals(locale.getVariant())) {
                        return keyboardLayout.getDescriptor();
                    }
                }
            } else {
                for (KeyboardLayout keyboardLayout2 : arrayList) {
                    LocaleList locales2 = keyboardLayout2.getLocales();
                    for (int i2 = 0; i2 < locales2.size(); i2++) {
                        Locale locale3 = locales2.get(i2);
                        if (locale3 != null && locale3.getCountry().equals(locale.getCountry())) {
                            return keyboardLayout2.getDescriptor();
                        }
                    }
                }
                return ((KeyboardLayout) arrayList.get(0)).getDescriptor();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getDefaultKeyboardLayout$0(InputDevice inputDevice, Locale locale, List list, Resources resources, int i, KeyboardLayout keyboardLayout) {
        if (keyboardLayout.getVendorId() == inputDevice.getVendorId() && keyboardLayout.getProductId() == inputDevice.getProductId()) {
            LocaleList locales = keyboardLayout.getLocales();
            for (int i2 = 0; i2 < locales.size(); i2++) {
                Locale locale2 = locales.get(i2);
                if (locale2 != null && isCompatibleLocale(locale, locale2)) {
                    list.add(keyboardLayout);
                    return;
                }
            }
        }
    }

    private static boolean isCompatibleLocale(Locale locale, Locale locale2) {
        if (locale.getLanguage().equals(locale2.getLanguage())) {
            return TextUtils.isEmpty(locale.getCountry()) || TextUtils.isEmpty(locale2.getCountry()) || locale.getCountry().equals(locale2.getCountry());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKeyboardLayouts() {
        final HashSet hashSet = new HashSet();
        visitAllKeyboardLayouts(new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda6
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout) {
                KeyboardLayoutManager.lambda$updateKeyboardLayouts$1(hashSet, resources, i, keyboardLayout);
            }
        });
        synchronized (this.mDataStore) {
            try {
                this.mDataStore.removeUninstalledKeyboardLayouts(hashSet);
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
        synchronized (this.mKeyboardLayoutCache) {
            this.mKeyboardLayoutCache.clear();
        }
        reloadKeyboardLayouts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateKeyboardLayouts$1(HashSet hashSet, Resources resources, int i, KeyboardLayout keyboardLayout) {
        hashSet.add(keyboardLayout.getDescriptor());
    }

    public KeyboardLayout[] getKeyboardLayouts() {
        final ArrayList arrayList = new ArrayList();
        visitAllKeyboardLayouts(new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda7
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout) {
                arrayList.add(keyboardLayout);
            }
        });
        return (KeyboardLayout[]) arrayList.toArray(new KeyboardLayout[0]);
    }

    public KeyboardLayout[] getKeyboardLayoutsForInputDevice(final InputDeviceIdentifier inputDeviceIdentifier) {
        if (useNewSettingsUi()) {
            return getKeyboardLayouts();
        }
        final String[] enabledKeyboardLayoutsForInputDevice = getEnabledKeyboardLayoutsForInputDevice(inputDeviceIdentifier);
        final ArrayList arrayList = new ArrayList(enabledKeyboardLayoutsForInputDevice.length);
        final ArrayList arrayList2 = new ArrayList();
        visitAllKeyboardLayouts(new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager.2
            boolean mHasSeenDeviceSpecificLayout;

            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout) {
                for (String str : enabledKeyboardLayoutsForInputDevice) {
                    if (str != null && str.equals(keyboardLayout.getDescriptor())) {
                        arrayList.add(keyboardLayout);
                        return;
                    }
                }
                if (keyboardLayout.getVendorId() == inputDeviceIdentifier.getVendorId() && keyboardLayout.getProductId() == inputDeviceIdentifier.getProductId()) {
                    if (!this.mHasSeenDeviceSpecificLayout) {
                        this.mHasSeenDeviceSpecificLayout = true;
                        arrayList2.clear();
                    }
                    arrayList2.add(keyboardLayout);
                    return;
                }
                if (keyboardLayout.getVendorId() == -1 && keyboardLayout.getProductId() == -1 && !this.mHasSeenDeviceSpecificLayout) {
                    arrayList2.add(keyboardLayout);
                }
            }
        });
        return (KeyboardLayout[]) Stream.concat(arrayList.stream(), arrayList2.stream()).toArray(new IntFunction() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                KeyboardLayout[] lambda$getKeyboardLayoutsForInputDevice$3;
                lambda$getKeyboardLayoutsForInputDevice$3 = KeyboardLayoutManager.lambda$getKeyboardLayoutsForInputDevice$3(i);
                return lambda$getKeyboardLayoutsForInputDevice$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ KeyboardLayout[] lambda$getKeyboardLayoutsForInputDevice$3(int i) {
        return new KeyboardLayout[i];
    }

    public KeyboardLayout getKeyboardLayout(String str) {
        Objects.requireNonNull(str, "keyboardLayoutDescriptor must not be null");
        final KeyboardLayout[] keyboardLayoutArr = new KeyboardLayout[1];
        visitKeyboardLayout(str, new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda4
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout) {
                KeyboardLayoutManager.lambda$getKeyboardLayout$4(keyboardLayoutArr, resources, i, keyboardLayout);
            }
        });
        if (keyboardLayoutArr[0] == null) {
            Slog.w(TAG, "Could not get keyboard layout with descriptor '" + str + "'.");
        }
        return keyboardLayoutArr[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getKeyboardLayout$4(KeyboardLayout[] keyboardLayoutArr, Resources resources, int i, KeyboardLayout keyboardLayout) {
        keyboardLayoutArr[0] = keyboardLayout;
    }

    private void visitAllKeyboardLayouts(KeyboardLayoutVisitor keyboardLayoutVisitor) {
        PackageManager packageManager = this.mContext.getPackageManager();
        for (ResolveInfo resolveInfo : packageManager.queryBroadcastReceiversAsUser(new Intent("android.hardware.input.action.QUERY_KEYBOARD_LAYOUTS"), 786560, 0)) {
            visitKeyboardLayoutsInPackage(packageManager, resolveInfo.activityInfo, null, resolveInfo.priority, keyboardLayoutVisitor);
        }
    }

    private void visitKeyboardLayout(String str, KeyboardLayoutVisitor keyboardLayoutVisitor) {
        KeyboardLayoutDescriptor parse = KeyboardLayoutDescriptor.parse(str);
        if (parse != null) {
            PackageManager packageManager = this.mContext.getPackageManager();
            try {
                visitKeyboardLayoutsInPackage(packageManager, packageManager.getReceiverInfo(new ComponentName(parse.packageName, parse.receiverName), 786560), parse.keyboardLayoutName, 0, keyboardLayoutVisitor);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }

    private void visitKeyboardLayoutsInPackage(PackageManager packageManager, ActivityInfo activityInfo, String str, int i, KeyboardLayoutVisitor keyboardLayoutVisitor) {
        String str2;
        int i2;
        int i3;
        TypedArray typedArray;
        Object obj = str;
        Bundle bundle = activityInfo.metaData;
        if (bundle == null) {
            return;
        }
        int i4 = bundle.getInt("android.hardware.input.metadata.KEYBOARD_LAYOUTS");
        if (i4 == 0) {
            Slog.w(TAG, "Missing meta-data 'android.hardware.input.metadata.KEYBOARD_LAYOUTS' on receiver " + activityInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + activityInfo.name);
            return;
        }
        CharSequence loadLabel = activityInfo.loadLabel(packageManager);
        String charSequence = loadLabel != null ? loadLabel.toString() : "";
        ApplicationInfo applicationInfo = activityInfo.applicationInfo;
        int i5 = 1;
        int i6 = 0;
        int i7 = (applicationInfo.flags & 1) != 0 ? i : 0;
        try {
            Resources resourcesForApplication = packageManager.getResourcesForApplication(applicationInfo);
            XmlResourceParser xml = resourcesForApplication.getXml(i4);
            try {
                XmlUtils.beginDocument(xml, "keyboard-layouts");
                while (true) {
                    XmlUtils.nextElement(xml);
                    String name = xml.getName();
                    if (name != null) {
                        if (name.equals("keyboard-layout")) {
                            TypedArray obtainAttributes = resourcesForApplication.obtainAttributes(xml, R.styleable.KeyboardLayout);
                            try {
                                String string = obtainAttributes.getString(i5);
                                String string2 = obtainAttributes.getString(i6);
                                int resourceId = obtainAttributes.getResourceId(2, i6);
                                LocaleList localesFromLanguageTags = getLocalesFromLanguageTags(obtainAttributes.getString(3));
                                int i8 = obtainAttributes.getInt(4, i6);
                                int i9 = obtainAttributes.getInt(6, -1);
                                int i10 = obtainAttributes.getInt(5, -1);
                                if (string != null && string2 != null && resourceId != 0) {
                                    String format = KeyboardLayoutDescriptor.format(activityInfo.packageName, activityInfo.name, string);
                                    try {
                                        if (obj != null && !string.equals(obj)) {
                                            str2 = charSequence;
                                            typedArray = obtainAttributes;
                                            i2 = i6;
                                            i3 = i5;
                                            typedArray.recycle();
                                        }
                                        keyboardLayoutVisitor.visitKeyboardLayout(resourcesForApplication, resourceId, new KeyboardLayout(format, string2, charSequence, i7, localesFromLanguageTags, i8, i9, i10));
                                        typedArray.recycle();
                                    } catch (Throwable th) {
                                        th = th;
                                        typedArray.recycle();
                                        throw th;
                                    }
                                    str2 = charSequence;
                                    typedArray = obtainAttributes;
                                    i2 = i6;
                                    i3 = i5;
                                }
                                str2 = charSequence;
                                typedArray = obtainAttributes;
                                i2 = i6;
                                i3 = i5;
                                Slog.w(TAG, "Missing required 'name', 'label' or 'keyboardLayout' attributes in keyboard layout resource from receiver " + activityInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + activityInfo.name);
                                typedArray.recycle();
                            } catch (Throwable th2) {
                                th = th2;
                                typedArray = obtainAttributes;
                            }
                        } else {
                            str2 = charSequence;
                            i2 = i6;
                            i3 = i5;
                            Slog.w(TAG, "Skipping unrecognized element '" + name + "' in keyboard layout resource from receiver " + activityInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + activityInfo.name);
                        }
                        charSequence = str2;
                        obj = str;
                        i6 = i2;
                        i5 = i3;
                    } else {
                        xml.close();
                        return;
                    }
                }
            } finally {
            }
        } catch (Exception e) {
            Slog.w(TAG, "Could not parse keyboard layout resource from receiver " + activityInfo.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + activityInfo.name, e);
        }
    }

    private static LocaleList getLocalesFromLanguageTags(String str) {
        if (TextUtils.isEmpty(str)) {
            return LocaleList.getEmptyLocaleList();
        }
        return LocaleList.forLanguageTags(str.replace('|', ','));
    }

    private String getLayoutDescriptor(InputDeviceIdentifier inputDeviceIdentifier) {
        Objects.requireNonNull(inputDeviceIdentifier, "identifier must not be null");
        Objects.requireNonNull(inputDeviceIdentifier.getDescriptor(), "descriptor must not be null");
        if (inputDeviceIdentifier.getVendorId() == 0 && inputDeviceIdentifier.getProductId() == 0) {
            return inputDeviceIdentifier.getDescriptor();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("vendor:");
        sb.append(inputDeviceIdentifier.getVendorId());
        sb.append(",product:");
        sb.append(inputDeviceIdentifier.getProductId());
        if (useNewSettingsUi()) {
            InputDevice inputDevice = getInputDevice(inputDeviceIdentifier);
            Objects.requireNonNull(inputDevice, "Input device must not be null");
            if (!TextUtils.isEmpty(inputDevice.getKeyboardLanguageTag())) {
                sb.append(",languageTag:");
                sb.append(inputDevice.getKeyboardLanguageTag());
            }
            if (!TextUtils.isEmpty(inputDevice.getKeyboardLayoutType())) {
                sb.append(",layoutType:");
                sb.append(inputDevice.getKeyboardLayoutType());
            }
        }
        return sb.toString();
    }

    public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) {
        String currentKeyboardLayout;
        if (useNewSettingsUi()) {
            Slog.e(TAG, "getCurrentKeyboardLayoutForInputDevice API not supported");
            return null;
        }
        String layoutDescriptor = getLayoutDescriptor(inputDeviceIdentifier);
        synchronized (this.mDataStore) {
            currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(layoutDescriptor);
            if (currentKeyboardLayout == null && !layoutDescriptor.equals(inputDeviceIdentifier.getDescriptor())) {
                currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(inputDeviceIdentifier.getDescriptor());
            }
            if (DEBUG) {
                Slog.d(TAG, "getCurrentKeyboardLayoutForInputDevice() " + inputDeviceIdentifier.toString() + ": " + currentKeyboardLayout);
            }
        }
        return currentKeyboardLayout;
    }

    public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) {
        if (useNewSettingsUi()) {
            Slog.e(TAG, "setCurrentKeyboardLayoutForInputDevice API not supported");
            return;
        }
        Objects.requireNonNull(str, "keyboardLayoutDescriptor must not be null");
        String layoutDescriptor = getLayoutDescriptor(inputDeviceIdentifier);
        synchronized (this.mDataStore) {
            try {
                if (this.mDataStore.setCurrentKeyboardLayout(layoutDescriptor, str)) {
                    if (DEBUG) {
                        Slog.d(TAG, "setCurrentKeyboardLayoutForInputDevice() " + inputDeviceIdentifier + " key: " + layoutDescriptor + " keyboardLayoutDescriptor: " + str);
                    }
                    this.mHandler.sendEmptyMessage(3);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    public String[] getEnabledKeyboardLayoutsForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) {
        String[] keyboardLayouts;
        if (useNewSettingsUi()) {
            Slog.e(TAG, "getEnabledKeyboardLayoutsForInputDevice API not supported");
            return new String[0];
        }
        String layoutDescriptor = getLayoutDescriptor(inputDeviceIdentifier);
        synchronized (this.mDataStore) {
            keyboardLayouts = this.mDataStore.getKeyboardLayouts(layoutDescriptor);
            if ((keyboardLayouts == null || keyboardLayouts.length == 0) && !layoutDescriptor.equals(inputDeviceIdentifier.getDescriptor())) {
                keyboardLayouts = this.mDataStore.getKeyboardLayouts(inputDeviceIdentifier.getDescriptor());
            }
        }
        return keyboardLayouts;
    }

    public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) {
        if (useNewSettingsUi()) {
            Slog.e(TAG, "addKeyboardLayoutForInputDevice API not supported");
            return;
        }
        Objects.requireNonNull(str, "keyboardLayoutDescriptor must not be null");
        String layoutDescriptor = getLayoutDescriptor(inputDeviceIdentifier);
        synchronized (this.mDataStore) {
            try {
                String currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(layoutDescriptor);
                if (currentKeyboardLayout == null && !layoutDescriptor.equals(inputDeviceIdentifier.getDescriptor())) {
                    currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(inputDeviceIdentifier.getDescriptor());
                }
                if (this.mDataStore.addKeyboardLayout(layoutDescriptor, str) && !Objects.equals(currentKeyboardLayout, this.mDataStore.getCurrentKeyboardLayout(layoutDescriptor))) {
                    this.mHandler.sendEmptyMessage(3);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) {
        if (useNewSettingsUi()) {
            Slog.e(TAG, "removeKeyboardLayoutForInputDevice API not supported");
            return;
        }
        Objects.requireNonNull(str, "keyboardLayoutDescriptor must not be null");
        String layoutDescriptor = getLayoutDescriptor(inputDeviceIdentifier);
        synchronized (this.mDataStore) {
            try {
                String currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(layoutDescriptor);
                if (currentKeyboardLayout == null && !layoutDescriptor.equals(inputDeviceIdentifier.getDescriptor())) {
                    currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(inputDeviceIdentifier.getDescriptor());
                }
                boolean removeKeyboardLayout = this.mDataStore.removeKeyboardLayout(layoutDescriptor, str);
                if (!layoutDescriptor.equals(inputDeviceIdentifier.getDescriptor())) {
                    removeKeyboardLayout |= this.mDataStore.removeKeyboardLayout(inputDeviceIdentifier.getDescriptor(), str);
                }
                if (removeKeyboardLayout && !Objects.equals(currentKeyboardLayout, this.mDataStore.getCurrentKeyboardLayout(layoutDescriptor))) {
                    this.mHandler.sendEmptyMessage(3);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    public void switchKeyboardLayout(int i, int i2) {
        if (useNewSettingsUi()) {
            Slog.e(TAG, "switchKeyboardLayout API not supported");
        } else {
            this.mHandler.obtainMessage(2, i, i2).sendToTarget();
        }
    }

    private void handleSwitchKeyboardLayout(int i, int i2) {
        boolean switchKeyboardLayout;
        String currentKeyboardLayout;
        KeyboardLayout keyboardLayout;
        InputDevice inputDevice = getInputDevice(i);
        if (inputDevice != null) {
            String layoutDescriptor = getLayoutDescriptor(inputDevice.getIdentifier());
            synchronized (this.mDataStore) {
                try {
                    switchKeyboardLayout = this.mDataStore.switchKeyboardLayout(layoutDescriptor, i2);
                    currentKeyboardLayout = this.mDataStore.getCurrentKeyboardLayout(layoutDescriptor);
                } finally {
                    this.mDataStore.saveIfNeeded();
                }
            }
            if (switchKeyboardLayout) {
                Toast toast = this.mSwitchedKeyboardLayoutToast;
                if (toast != null) {
                    toast.cancel();
                    this.mSwitchedKeyboardLayoutToast = null;
                }
                if (currentKeyboardLayout != null && (keyboardLayout = getKeyboardLayout(currentKeyboardLayout)) != null) {
                    Toast makeText = Toast.makeText(this.mContext, keyboardLayout.getLabel(), 0);
                    this.mSwitchedKeyboardLayoutToast = makeText;
                    makeText.show();
                }
                reloadKeyboardLayouts();
            }
        }
    }

    public String[] getKeyboardLayoutOverlay(InputDeviceIdentifier inputDeviceIdentifier) {
        String currentKeyboardLayoutForInputDevice;
        if (useNewSettingsUi()) {
            synchronized (this.mImeInfoLock) {
                currentKeyboardLayoutForInputDevice = getKeyboardLayoutForInputDeviceInternal(inputDeviceIdentifier, this.mCurrentImeInfo);
            }
        } else {
            currentKeyboardLayoutForInputDevice = getCurrentKeyboardLayoutForInputDevice(inputDeviceIdentifier);
        }
        if (currentKeyboardLayoutForInputDevice == null) {
            return null;
        }
        final String[] strArr = new String[2];
        visitKeyboardLayout(currentKeyboardLayoutForInputDevice, new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda1
            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public final void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout) {
                KeyboardLayoutManager.lambda$getKeyboardLayoutOverlay$5(strArr, resources, i, keyboardLayout);
            }
        });
        if (strArr[0] != null) {
            return strArr;
        }
        Slog.w(TAG, "Could not get keyboard layout with descriptor '" + currentKeyboardLayoutForInputDevice + "'.");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getKeyboardLayoutOverlay$5(String[] strArr, Resources resources, int i, KeyboardLayout keyboardLayout) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resources.openRawResource(i));
            try {
                strArr[0] = keyboardLayout.getDescriptor();
                strArr[1] = Streams.readFully(inputStreamReader);
                inputStreamReader.close();
            } catch (Throwable th) {
                try {
                    inputStreamReader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Resources.NotFoundException | IOException unused) {
        }
    }

    public String getKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, int i, InputMethodInfo inputMethodInfo, InputMethodSubtype inputMethodSubtype) {
        if (!useNewSettingsUi()) {
            Slog.e(TAG, "getKeyboardLayoutForInputDevice() API not supported");
            return null;
        }
        InputMethodSubtypeHandle of = InputMethodSubtypeHandle.of(inputMethodInfo, inputMethodSubtype);
        String keyboardLayoutForInputDeviceInternal = getKeyboardLayoutForInputDeviceInternal(inputDeviceIdentifier, new ImeInfo(i, of, inputMethodSubtype));
        if (DEBUG) {
            Slog.d(TAG, "getKeyboardLayoutForInputDevice() " + inputDeviceIdentifier.toString() + ", userId : " + i + ", subtypeHandle = " + of + " -> " + keyboardLayoutForInputDeviceInternal);
        }
        return keyboardLayoutForInputDeviceInternal;
    }

    public void setKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, int i, InputMethodInfo inputMethodInfo, InputMethodSubtype inputMethodSubtype, String str) {
        if (!useNewSettingsUi()) {
            Slog.e(TAG, "setKeyboardLayoutForInputDevice() API not supported");
            return;
        }
        Objects.requireNonNull(str, "keyboardLayoutDescriptor must not be null");
        String createLayoutKey = createLayoutKey(inputDeviceIdentifier, new ImeInfo(i, InputMethodSubtypeHandle.of(inputMethodInfo, inputMethodSubtype), inputMethodSubtype));
        synchronized (this.mDataStore) {
            try {
                if (this.mDataStore.setKeyboardLayout(getLayoutDescriptor(inputDeviceIdentifier), createLayoutKey, str)) {
                    if (DEBUG) {
                        Slog.d(TAG, "setKeyboardLayoutForInputDevice() " + inputDeviceIdentifier + " key: " + createLayoutKey + " keyboardLayoutDescriptor: " + str);
                    }
                    this.mHandler.sendEmptyMessage(3);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    public KeyboardLayout[] getKeyboardLayoutListForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, int i, InputMethodInfo inputMethodInfo, InputMethodSubtype inputMethodSubtype) {
        if (!useNewSettingsUi()) {
            Slog.e(TAG, "getKeyboardLayoutListForInputDevice() API not supported");
            return new KeyboardLayout[0];
        }
        return getKeyboardLayoutListForInputDeviceInternal(inputDeviceIdentifier, new ImeInfo(i, InputMethodSubtypeHandle.of(inputMethodInfo, inputMethodSubtype), inputMethodSubtype));
    }

    private KeyboardLayout[] getKeyboardLayoutListForInputDeviceInternal(final InputDeviceIdentifier inputDeviceIdentifier, ImeInfo imeInfo) {
        final String keyboardLayout;
        String str;
        InputMethodSubtype inputMethodSubtype;
        String createLayoutKey = createLayoutKey(inputDeviceIdentifier, imeInfo);
        synchronized (this.mDataStore) {
            keyboardLayout = this.mDataStore.getKeyboardLayout(getLayoutDescriptor(inputDeviceIdentifier), createLayoutKey);
        }
        final ArrayList arrayList = new ArrayList();
        if (imeInfo == null || (inputMethodSubtype = imeInfo.mImeSubtype) == null) {
            str = "";
        } else {
            ULocale physicalKeyboardHintLanguageTag = inputMethodSubtype.getPhysicalKeyboardHintLanguageTag();
            if (physicalKeyboardHintLanguageTag != null) {
                str = physicalKeyboardHintLanguageTag.toLanguageTag();
            } else {
                str = imeInfo.mImeSubtype.getCanonicalizedLanguageTag();
            }
        }
        final String str2 = str;
        visitAllKeyboardLayouts(new KeyboardLayoutVisitor() { // from class: com.android.server.input.KeyboardLayoutManager.3
            boolean mDeviceSpecificLayoutAvailable;

            @Override // com.android.server.input.KeyboardLayoutManager.KeyboardLayoutVisitor
            public void visitKeyboardLayout(Resources resources, int i, KeyboardLayout keyboardLayout2) {
                if (keyboardLayout2.getVendorId() == inputDeviceIdentifier.getVendorId() && keyboardLayout2.getProductId() == inputDeviceIdentifier.getProductId()) {
                    if (!this.mDeviceSpecificLayoutAvailable) {
                        this.mDeviceSpecificLayoutAvailable = true;
                        arrayList.clear();
                    }
                    arrayList.add(keyboardLayout2);
                    return;
                }
                if (keyboardLayout2.getVendorId() == -1 && keyboardLayout2.getProductId() == -1 && !this.mDeviceSpecificLayoutAvailable && KeyboardLayoutManager.isLayoutCompatibleWithLanguageTag(keyboardLayout2, str2)) {
                    arrayList.add(keyboardLayout2);
                } else if (keyboardLayout2.getDescriptor().equals(keyboardLayout)) {
                    arrayList.add(keyboardLayout2);
                }
            }
        });
        Collections.sort(arrayList);
        return (KeyboardLayout[]) arrayList.toArray(new KeyboardLayout[0]);
    }

    public void onInputMethodSubtypeChanged(int i, InputMethodSubtypeHandle inputMethodSubtypeHandle, InputMethodSubtype inputMethodSubtype) {
        if (!useNewSettingsUi()) {
            Slog.e(TAG, "onInputMethodSubtypeChanged() API not supported");
            return;
        }
        if (inputMethodSubtypeHandle == null) {
            if (DEBUG) {
                Slog.d(TAG, "No InputMethod is running, ignoring change");
                return;
            }
            return;
        }
        synchronized (this.mImeInfoLock) {
            ImeInfo imeInfo = this.mCurrentImeInfo;
            if (imeInfo == null || !inputMethodSubtypeHandle.equals(imeInfo.mImeSubtypeHandle) || this.mCurrentImeInfo.mUserId != i) {
                this.mCurrentImeInfo = new ImeInfo(i, inputMethodSubtypeHandle, inputMethodSubtype);
                this.mHandler.sendEmptyMessage(5);
                if (DEBUG) {
                    Slog.d(TAG, "InputMethodSubtype changed: userId=" + i + " subtypeHandle=" + inputMethodSubtypeHandle);
                }
            }
        }
    }

    private void onCurrentImeInfoChanged() {
        synchronized (this.mImeInfoLock) {
            for (int i = 0; i < this.mConfiguredKeyboards.size(); i++) {
                InputDevice inputDevice = getInputDevice(this.mConfiguredKeyboards.keyAt(i));
                Objects.requireNonNull(inputDevice);
                InputDevice inputDevice2 = inputDevice;
                String keyboardLayoutForInputDeviceInternal = getKeyboardLayoutForInputDeviceInternal(inputDevice.getIdentifier(), this.mCurrentImeInfo);
                KeyboardConfiguration valueAt = this.mConfiguredKeyboards.valueAt(i);
                if (!Objects.equals(keyboardLayoutForInputDeviceInternal, valueAt.getCurrentLayout())) {
                    valueAt.setCurrentLayout(keyboardLayoutForInputDeviceInternal);
                    this.mHandler.sendEmptyMessage(3);
                    return;
                }
            }
        }
    }

    private String getKeyboardLayoutForInputDeviceInternal(InputDeviceIdentifier inputDeviceIdentifier, ImeInfo imeInfo) {
        String keyboardLayout;
        InputDevice inputDevice = getInputDevice(inputDeviceIdentifier);
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return null;
        }
        String createLayoutKey = createLayoutKey(inputDeviceIdentifier, imeInfo);
        synchronized (this.mDataStore) {
            keyboardLayout = this.mDataStore.getKeyboardLayout(getLayoutDescriptor(inputDeviceIdentifier), createLayoutKey);
        }
        if (keyboardLayout == null) {
            synchronized (this.mKeyboardLayoutCache) {
                if (this.mKeyboardLayoutCache.containsKey(createLayoutKey)) {
                    keyboardLayout = this.mKeyboardLayoutCache.get(createLayoutKey);
                } else {
                    String defaultKeyboardLayoutBasedOnImeInfo = getDefaultKeyboardLayoutBasedOnImeInfo(inputDevice, imeInfo, getKeyboardLayoutListForInputDeviceInternal(inputDeviceIdentifier, imeInfo));
                    this.mKeyboardLayoutCache.put(createLayoutKey, defaultKeyboardLayoutBasedOnImeInfo);
                    keyboardLayout = defaultKeyboardLayoutBasedOnImeInfo;
                }
            }
        }
        return keyboardLayout;
    }

    private static String getDefaultKeyboardLayoutBasedOnImeInfo(InputDevice inputDevice, ImeInfo imeInfo, KeyboardLayout[] keyboardLayoutArr) {
        InputMethodSubtype inputMethodSubtype;
        String matchingLayoutForProvidedLanguageTagAndLayoutType;
        Arrays.sort(keyboardLayoutArr);
        for (KeyboardLayout keyboardLayout : keyboardLayoutArr) {
            if (keyboardLayout.getVendorId() == inputDevice.getVendorId() && keyboardLayout.getProductId() == inputDevice.getProductId()) {
                if (DEBUG) {
                    Slog.d(TAG, "getDefaultKeyboardLayoutBasedOnImeInfo() : Layout found based on vendor and product Ids. " + inputDevice.getIdentifier() + " : " + keyboardLayout.getDescriptor());
                }
                return keyboardLayout.getDescriptor();
            }
        }
        String keyboardLanguageTag = inputDevice.getKeyboardLanguageTag();
        if (keyboardLanguageTag != null && (matchingLayoutForProvidedLanguageTagAndLayoutType = getMatchingLayoutForProvidedLanguageTagAndLayoutType(keyboardLayoutArr, keyboardLanguageTag, inputDevice.getKeyboardLayoutType())) != null) {
            if (DEBUG) {
                Slog.d(TAG, "getDefaultKeyboardLayoutBasedOnImeInfo() : Layout found based on HW information (Language tag and Layout type). " + inputDevice.getIdentifier() + " : " + matchingLayoutForProvidedLanguageTagAndLayoutType);
            }
            return matchingLayoutForProvidedLanguageTagAndLayoutType;
        }
        if (imeInfo == null || imeInfo.mImeSubtypeHandle == null || (inputMethodSubtype = imeInfo.mImeSubtype) == null) {
            return null;
        }
        ULocale physicalKeyboardHintLanguageTag = inputMethodSubtype.getPhysicalKeyboardHintLanguageTag();
        String matchingLayoutForProvidedLanguageTagAndLayoutType2 = getMatchingLayoutForProvidedLanguageTagAndLayoutType(keyboardLayoutArr, physicalKeyboardHintLanguageTag != null ? physicalKeyboardHintLanguageTag.toLanguageTag() : inputMethodSubtype.getCanonicalizedLanguageTag(), inputMethodSubtype.getPhysicalKeyboardHintLayoutType());
        if (DEBUG) {
            Slog.d(TAG, "getDefaultKeyboardLayoutBasedOnImeInfo() : Layout found based on IME locale matching. " + inputDevice.getIdentifier() + " : " + matchingLayoutForProvidedLanguageTagAndLayoutType2);
        }
        return matchingLayoutForProvidedLanguageTagAndLayoutType2;
    }

    private static String getMatchingLayoutForProvidedLanguageTagAndLayoutType(KeyboardLayout[] keyboardLayoutArr, String str, String str2) {
        if (str2 == null || !KeyboardLayout.isLayoutTypeValid(str2)) {
            str2 = "undefined";
        }
        ArrayList arrayList = new ArrayList();
        for (KeyboardLayout keyboardLayout : keyboardLayoutArr) {
            if (keyboardLayout.getLayoutType().equals(str2)) {
                arrayList.add(keyboardLayout);
            }
        }
        String matchingLayoutForProvidedLanguageTag = getMatchingLayoutForProvidedLanguageTag(arrayList, str);
        return matchingLayoutForProvidedLanguageTag != null ? matchingLayoutForProvidedLanguageTag : getMatchingLayoutForProvidedLanguageTag(Arrays.asList(keyboardLayoutArr), str);
    }

    private static String getMatchingLayoutForProvidedLanguageTag(List<KeyboardLayout> list, String str) {
        Locale forLanguageTag = Locale.forLanguageTag(str);
        String str2 = null;
        String str3 = null;
        for (KeyboardLayout keyboardLayout : list) {
            LocaleList locales = keyboardLayout.getLocales();
            for (int i = 0; i < locales.size(); i++) {
                Locale locale = locales.get(i);
                if (locale != null && locale.getLanguage().equals(forLanguageTag.getLanguage())) {
                    if (str3 == null) {
                        str3 = keyboardLayout.getDescriptor();
                    }
                    if (locale.getCountry().equals(forLanguageTag.getCountry())) {
                        if (str2 == null) {
                            str2 = keyboardLayout.getDescriptor();
                        }
                        if (locale.getVariant().equals(forLanguageTag.getVariant())) {
                            return keyboardLayout.getDescriptor();
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return str2 != null ? str2 : str3;
    }

    private void reloadKeyboardLayouts() {
        if (DEBUG) {
            Slog.d(TAG, "Reloading keyboard layouts.");
        }
        this.mNative.reloadKeyboardLayouts();
    }

    private void maybeUpdateNotification() {
        if (this.mConfiguredKeyboards.size() == 0) {
            hideKeyboardLayoutNotification();
            return;
        }
        for (int i = 0; i < this.mConfiguredKeyboards.size(); i++) {
            if (!this.mConfiguredKeyboards.valueAt(i).hasConfiguredLayouts()) {
                showMissingKeyboardLayoutNotification();
                return;
            }
        }
        showConfiguredKeyboardLayoutNotification();
    }

    private void showMissingKeyboardLayoutNotification() {
        Resources resources = this.mContext.getResources();
        String string = resources.getString(17041582);
        if (this.mConfiguredKeyboards.size() == 1) {
            InputDevice inputDevice = getInputDevice(this.mConfiguredKeyboards.keyAt(0));
            if (inputDevice == null) {
                return;
            }
            showKeyboardLayoutNotification(resources.getString(17041583, inputDevice.getName()), string, inputDevice);
            return;
        }
        showKeyboardLayoutNotification(resources.getString(17041585), string, null);
    }

    private void showKeyboardLayoutNotification(String str, String str2, InputDevice inputDevice) {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        if (notificationManager == null) {
            return;
        }
        Intent intent = new Intent("android.settings.HARD_KEYBOARD_SETTINGS");
        if (inputDevice != null) {
            intent.putExtra("input_device_identifier", (Parcelable) inputDevice.getIdentifier());
        }
        intent.setFlags(337641472);
        notificationManager.notifyAsUser(null, 19, new Notification.Builder(this.mContext, SystemNotificationChannels.PHYSICAL_KEYBOARD).setContentTitle(str).setContentText(str2).setContentIntent(PendingIntent.getActivityAsUser(this.mContext, 0, intent, 67108864, null, UserHandle.CURRENT)).setSmallIcon(android.R.drawable.ic_volume_bluetooth_ad2p).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setAutoCancel(true).build(), UserHandle.ALL);
    }

    private void hideKeyboardLayoutNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        if (notificationManager == null) {
            return;
        }
        notificationManager.cancelAsUser(null, 19, UserHandle.ALL);
    }

    private void showConfiguredKeyboardLayoutNotification() {
        Resources resources = this.mContext.getResources();
        if (this.mConfiguredKeyboards.size() != 1) {
            showKeyboardLayoutNotification(resources.getString(android.R.string.mediasize_iso_c1), resources.getString(android.R.string.mediasize_iso_c0), null);
            return;
        }
        InputDevice inputDevice = getInputDevice(this.mConfiguredKeyboards.keyAt(0));
        KeyboardConfiguration valueAt = this.mConfiguredKeyboards.valueAt(0);
        if (inputDevice == null || !valueAt.hasConfiguredLayouts()) {
            return;
        }
        showKeyboardLayoutNotification(resources.getString(android.R.string.mediasize_iso_c2, inputDevice.getName()), createConfiguredNotificationText(this.mContext, valueAt.getConfiguredLayouts()), inputDevice);
    }

    private String createConfiguredNotificationText(Context context, Set<String> set) {
        Resources resources = context.getResources();
        final ArrayList arrayList = new ArrayList();
        set.forEach(new Consumer() { // from class: com.android.server.input.KeyboardLayoutManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyboardLayoutManager.this.lambda$createConfiguredNotificationText$6(arrayList, (String) obj);
            }
        });
        Collections.sort(arrayList);
        int size = arrayList.size();
        if (size == 1) {
            return resources.getString(android.R.string.mediasize_iso_c10, arrayList.get(0));
        }
        if (size == 2) {
            return resources.getString(android.R.string.mediasize_iso_c4, arrayList.get(0), arrayList.get(1));
        }
        if (size == 3) {
            return resources.getString(android.R.string.mediasize_iso_c3, arrayList.get(0), arrayList.get(1), arrayList.get(2));
        }
        return resources.getString(android.R.string.mediasize_iso_b9, arrayList.get(0), arrayList.get(1), arrayList.get(2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createConfiguredNotificationText$6(List list, String str) {
        list.add(getKeyboardLayout(str).getLabel());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            for (int i2 : (int[]) message.obj) {
                onInputDeviceAdded(i2);
            }
            return true;
        }
        if (i == 2) {
            handleSwitchKeyboardLayout(message.arg1, message.arg2);
            return true;
        }
        if (i == 3) {
            reloadKeyboardLayouts();
            return true;
        }
        if (i == 4) {
            updateKeyboardLayouts();
            return true;
        }
        if (i != 5) {
            return false;
        }
        onCurrentImeInfoChanged();
        return true;
    }

    private boolean useNewSettingsUi() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_new_keyboard_ui");
    }

    private InputDevice getInputDevice(int i) {
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        if (inputManager != null) {
            return inputManager.getInputDevice(i);
        }
        return null;
    }

    private InputDevice getInputDevice(InputDeviceIdentifier inputDeviceIdentifier) {
        InputManager inputManager = (InputManager) this.mContext.getSystemService(InputManager.class);
        if (inputManager != null) {
            return inputManager.getInputDeviceByDescriptor(inputDeviceIdentifier.getDescriptor());
        }
        return null;
    }

    private List<ImeInfo> getImeInfoListForLayoutMapping() {
        ArrayList arrayList = new ArrayList();
        UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
        Objects.requireNonNull(userManager);
        InputMethodManager inputMethodManager = (InputMethodManager) this.mContext.getSystemService(InputMethodManager.class);
        Objects.requireNonNull(inputMethodManager);
        InputMethodManagerInternal inputMethodManagerInternal = InputMethodManagerInternal.get();
        Iterator it = userManager.getUserHandles(true).iterator();
        while (it.hasNext()) {
            int identifier = ((UserHandle) it.next()).getIdentifier();
            for (InputMethodInfo inputMethodInfo : inputMethodManagerInternal.getEnabledInputMethodListAsUser(identifier)) {
                for (InputMethodSubtype inputMethodSubtype : inputMethodManager.getEnabledInputMethodSubtypeList(inputMethodInfo, true)) {
                    if (inputMethodSubtype.isSuitableForPhysicalKeyboardLayoutMapping()) {
                        arrayList.add(new ImeInfo(identifier, InputMethodSubtypeHandle.of(inputMethodInfo, inputMethodSubtype), inputMethodSubtype));
                    }
                }
            }
        }
        return arrayList;
    }

    private String createLayoutKey(InputDeviceIdentifier inputDeviceIdentifier, ImeInfo imeInfo) {
        if (imeInfo == null) {
            return getLayoutDescriptor(inputDeviceIdentifier);
        }
        Objects.requireNonNull(imeInfo.mImeSubtypeHandle, "subtypeHandle must not be null");
        return "layoutDescriptor:" + getLayoutDescriptor(inputDeviceIdentifier) + ",userId:" + imeInfo.mUserId + ",subtypeHandle:" + imeInfo.mImeSubtypeHandle.toStringHandle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isLayoutCompatibleWithLanguageTag(KeyboardLayout keyboardLayout, String str) {
        LocaleList locales = keyboardLayout.getLocales();
        if (locales.isEmpty() || TextUtils.isEmpty(str)) {
            return true;
        }
        int[] scriptCodes = getScriptCodes(Locale.forLanguageTag(str));
        if (scriptCodes.length == 0) {
            return true;
        }
        for (int i = 0; i < locales.size(); i++) {
            if (haveCommonValue(getScriptCodes(locales.get(i)), scriptCodes)) {
                return true;
            }
        }
        return false;
    }

    private static int[] getScriptCodes(Locale locale) {
        int codeFromName;
        if (locale == null) {
            return new int[0];
        }
        if (!TextUtils.isEmpty(locale.getScript()) && (codeFromName = UScript.getCodeFromName(locale.getScript())) != -1) {
            return new int[]{codeFromName};
        }
        int[] code = UScript.getCode(locale);
        return code != null ? code : new int[0];
    }

    private static boolean haveCommonValue(int[] iArr, int[] iArr2) {
        for (int i : iArr) {
            for (int i2 : iArr2) {
                if (i == i2) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class KeyboardLayoutDescriptor {
        public String keyboardLayoutName;
        public String packageName;
        public String receiverName;

        private KeyboardLayoutDescriptor() {
        }

        public static String format(String str, String str2, String str3) {
            return str + SliceClientPermissions.SliceAuthority.DELIMITER + str2 + SliceClientPermissions.SliceAuthority.DELIMITER + str3;
        }

        public static KeyboardLayoutDescriptor parse(String str) {
            int i;
            int indexOf;
            int i2;
            int indexOf2 = str.indexOf(47);
            if (indexOf2 < 0 || (i = indexOf2 + 1) == str.length() || (indexOf = str.indexOf(47, i)) < indexOf2 + 2 || (i2 = indexOf + 1) == str.length()) {
                return null;
            }
            KeyboardLayoutDescriptor keyboardLayoutDescriptor = new KeyboardLayoutDescriptor();
            keyboardLayoutDescriptor.packageName = str.substring(0, indexOf2);
            keyboardLayoutDescriptor.receiverName = str.substring(i, indexOf);
            keyboardLayoutDescriptor.keyboardLayoutName = str.substring(i2);
            return keyboardLayoutDescriptor;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ImeInfo {
        InputMethodSubtype mImeSubtype;
        InputMethodSubtypeHandle mImeSubtypeHandle;
        int mUserId;

        ImeInfo(int i, InputMethodSubtypeHandle inputMethodSubtypeHandle, InputMethodSubtype inputMethodSubtype) {
            this.mUserId = i;
            this.mImeSubtypeHandle = inputMethodSubtypeHandle;
            this.mImeSubtype = inputMethodSubtype;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class KeyboardConfiguration {
        private Set<String> mConfiguredLayouts;
        private String mCurrentLayout;

        private KeyboardConfiguration() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasConfiguredLayouts() {
            Set<String> set = this.mConfiguredLayouts;
            return (set == null || set.isEmpty()) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Set<String> getConfiguredLayouts() {
            return this.mConfiguredLayouts;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setConfiguredLayouts(Set<String> set) {
            this.mConfiguredLayouts = set;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getCurrentLayout() {
            return this.mCurrentLayout;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCurrentLayout(String str) {
            this.mCurrentLayout = str;
        }
    }
}
