package com.android.server.inputmethod;

import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.inputmethod.InputMethodSubtypeHandle;
import com.android.server.inputmethod.InputMethodUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class HardwareKeyboardShortcutController {

    @GuardedBy({"ImfLock.class"})
    private final ArrayList<InputMethodSubtypeHandle> mSubtypeHandles = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void reset(InputMethodUtils.InputMethodSettings inputMethodSettings) {
        this.mSubtypeHandles.clear();
        Iterator<InputMethodInfo> it = inputMethodSettings.getEnabledInputMethodListLocked().iterator();
        while (it.hasNext()) {
            InputMethodInfo next = it.next();
            if (next.shouldShowInInputMethodPicker()) {
                List<InputMethodSubtype> enabledInputMethodSubtypeListLocked = inputMethodSettings.getEnabledInputMethodSubtypeListLocked(next, true);
                if (enabledInputMethodSubtypeListLocked.isEmpty()) {
                    this.mSubtypeHandles.add(InputMethodSubtypeHandle.of(next, (InputMethodSubtype) null));
                } else {
                    for (InputMethodSubtype inputMethodSubtype : enabledInputMethodSubtypeListLocked) {
                        if (inputMethodSubtype.isSuitableForPhysicalKeyboardLayoutMapping()) {
                            this.mSubtypeHandles.add(InputMethodSubtypeHandle.of(next, inputMethodSubtype));
                        }
                    }
                }
            }
        }
    }

    static <T> T getNeighborItem(List<T> list, T t, boolean z) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (Objects.equals(t, list.get(i))) {
                return list.get(((i + (z ? 1 : -1)) + size) % size);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public InputMethodSubtypeHandle onSubtypeSwitch(InputMethodSubtypeHandle inputMethodSubtypeHandle, boolean z) {
        return (InputMethodSubtypeHandle) getNeighborItem(this.mSubtypeHandles, inputMethodSubtypeHandle, z);
    }
}
