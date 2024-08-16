package com.android.server.policy;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.KeyEvent;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ToBooleanFunction;
import com.android.server.policy.KeyCombinationManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class KeyCombinationManager {
    private static final long COMBINE_KEY_DELAY_MILLIS = 150;
    private static final String TAG = "KeyCombinationManager";
    private final Handler mHandler;

    @GuardedBy({"mLock"})
    private TwoKeysCombinationRule mTriggeredRule;

    @GuardedBy({"mLock"})
    private final SparseLongArray mDownTimes = new SparseLongArray(2);
    private final ArrayList<TwoKeysCombinationRule> mRules = new ArrayList<>();
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final ArrayList<TwoKeysCombinationRule> mActiveRules = new ArrayList<>();
    private KeyCombinationManagerWrapper mWrapper = new KeyCombinationManagerWrapper();
    private IKeyCombinationManagerExt mExt = (IKeyCombinationManagerExt) ExtLoader.type(IKeyCombinationManagerExt.class).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class TwoKeysCombinationRule {
        private int mKeyCode1;
        private int mKeyCode2;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void cancel();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void execute();

        long getKeyInterceptDelayMs() {
            return KeyCombinationManager.COMBINE_KEY_DELAY_MILLIS;
        }

        boolean preCondition() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public TwoKeysCombinationRule(int i, int i2) {
            this.mKeyCode1 = i;
            this.mKeyCode2 = i2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean shouldInterceptKey(int i) {
            return preCondition() && (i == this.mKeyCode1 || i == this.mKeyCode2);
        }

        boolean shouldInterceptKeys(SparseLongArray sparseLongArray) {
            long uptimeMillis = SystemClock.uptimeMillis();
            Log.d(KeyCombinationManager.TAG, "mKeyCode1 = " + sparseLongArray.get(this.mKeyCode1) + " mKeyCode2 = " + sparseLongArray.get(this.mKeyCode2) + " now = " + uptimeMillis + " this:" + toString());
            return sparseLongArray.get(this.mKeyCode1) > 0 && sparseLongArray.get(this.mKeyCode2) > 0 && uptimeMillis <= sparseLongArray.get(this.mKeyCode1) + KeyCombinationManager.COMBINE_KEY_DELAY_MILLIS && uptimeMillis <= sparseLongArray.get(this.mKeyCode2) + KeyCombinationManager.COMBINE_KEY_DELAY_MILLIS;
        }

        public String toString() {
            return KeyEvent.keyCodeToString(this.mKeyCode1) + " + " + KeyEvent.keyCodeToString(this.mKeyCode2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TwoKeysCombinationRule)) {
                return false;
            }
            TwoKeysCombinationRule twoKeysCombinationRule = (TwoKeysCombinationRule) obj;
            int i = this.mKeyCode1;
            int i2 = twoKeysCombinationRule.mKeyCode1;
            if (i == i2 && this.mKeyCode2 == twoKeysCombinationRule.mKeyCode2) {
                return true;
            }
            return i == twoKeysCombinationRule.mKeyCode2 && this.mKeyCode2 == i2;
        }

        public int hashCode() {
            return (this.mKeyCode1 * 31) + this.mKeyCode2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyCombinationManager(Handler handler) {
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRule(TwoKeysCombinationRule twoKeysCombinationRule) {
        if (this.mRules.contains(twoKeysCombinationRule)) {
            throw new IllegalArgumentException("Rule : " + twoKeysCombinationRule + " already exists.");
        }
        this.mRules.add(twoKeysCombinationRule);
    }

    void removeRule(TwoKeysCombinationRule twoKeysCombinationRule) {
        this.mRules.remove(twoKeysCombinationRule);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean interceptKey(KeyEvent keyEvent, boolean z) {
        boolean interceptKeyLocked;
        synchronized (this.mLock) {
            interceptKeyLocked = interceptKeyLocked(keyEvent, z);
        }
        return interceptKeyLocked;
    }

    private boolean interceptKeyLocked(KeyEvent keyEvent, boolean z) {
        boolean z2 = keyEvent.getAction() == 0;
        final int keyCode = keyEvent.getKeyCode();
        int size = this.mActiveRules.size();
        long eventTime = keyEvent.getEventTime();
        Log.d(TAG, "down " + z2 + " keyCode " + keyCode + " count " + size + " interactive " + z + " eventTime " + eventTime + " mDownTimes: " + this.mDownTimes.toString());
        if (z2 && (this.mWrapper.getExtImpl().canAODScreenshot(keyEvent) || z)) {
            if (this.mDownTimes.size() > 0) {
                if (size > 0 && eventTime > this.mDownTimes.valueAt(0) + COMBINE_KEY_DELAY_MILLIS) {
                    forAllRules(this.mActiveRules, new Consumer() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            ((KeyCombinationManager.TwoKeysCombinationRule) obj).cancel();
                        }
                    });
                    this.mActiveRules.clear();
                    Log.d(TAG, "exceed time from first key down, clear active rules");
                    return false;
                }
                if (size == 0) {
                    return false;
                }
            }
            if (this.mDownTimes.get(keyCode) != 0) {
                return false;
            }
            this.mDownTimes.put(keyCode, eventTime);
            if (this.mDownTimes.size() == 1) {
                this.mTriggeredRule = null;
                forAllRules(this.mRules, new Consumer() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        KeyCombinationManager.this.lambda$interceptKeyLocked$1(keyCode, (KeyCombinationManager.TwoKeysCombinationRule) obj);
                    }
                });
            } else {
                if (this.mTriggeredRule != null) {
                    Log.d(TAG, "mTriggeredRule != null " + this.mTriggeredRule);
                    return true;
                }
                forAllActiveRules(new ToBooleanFunction() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda4
                    public final boolean apply(Object obj) {
                        boolean lambda$interceptKeyLocked$2;
                        lambda$interceptKeyLocked$2 = KeyCombinationManager.this.lambda$interceptKeyLocked$2((KeyCombinationManager.TwoKeysCombinationRule) obj);
                        return lambda$interceptKeyLocked$2;
                    }
                });
                this.mActiveRules.clear();
                TwoKeysCombinationRule twoKeysCombinationRule = this.mTriggeredRule;
                if (twoKeysCombinationRule != null) {
                    this.mActiveRules.add(twoKeysCombinationRule);
                    return true;
                }
            }
        } else {
            this.mDownTimes.delete(keyCode);
            for (int i = size - 1; i >= 0; i--) {
                final TwoKeysCombinationRule twoKeysCombinationRule2 = this.mActiveRules.get(i);
                if (twoKeysCombinationRule2.shouldInterceptKey(keyCode)) {
                    this.mHandler.post(new Runnable() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            KeyCombinationManager.TwoKeysCombinationRule.this.cancel();
                        }
                    });
                    this.mActiveRules.remove(i);
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$interceptKeyLocked$1(int i, TwoKeysCombinationRule twoKeysCombinationRule) {
        if (twoKeysCombinationRule.shouldInterceptKey(i)) {
            this.mActiveRules.add(twoKeysCombinationRule);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$interceptKeyLocked$2(final TwoKeysCombinationRule twoKeysCombinationRule) {
        if (!twoKeysCombinationRule.shouldInterceptKeys(this.mDownTimes)) {
            return false;
        }
        Log.v(TAG, "Performing combination rule : " + twoKeysCombinationRule);
        this.mHandler.post(new Runnable() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                KeyCombinationManager.TwoKeysCombinationRule.this.execute();
            }
        });
        this.mTriggeredRule = twoKeysCombinationRule;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getKeyInterceptTimeout(int i) {
        synchronized (this.mLock) {
            long j = 0;
            if (this.mDownTimes.get(i) == 0) {
                return 0L;
            }
            Iterator<TwoKeysCombinationRule> it = this.mActiveRules.iterator();
            while (it.hasNext()) {
                TwoKeysCombinationRule next = it.next();
                if (next.shouldInterceptKey(i)) {
                    j = Math.max(j, next.getKeyInterceptDelayMs());
                }
            }
            return this.mDownTimes.get(i) + Math.min(j, COMBINE_KEY_DELAY_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isKeyConsumed(KeyEvent keyEvent) {
        synchronized (this.mLock) {
            boolean z = false;
            if ((keyEvent.getFlags() & 1024) != 0) {
                return false;
            }
            TwoKeysCombinationRule twoKeysCombinationRule = this.mTriggeredRule;
            if (twoKeysCombinationRule != null && twoKeysCombinationRule.shouldInterceptKey(keyEvent.getKeyCode())) {
                z = true;
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPowerKeyIntercepted() {
        synchronized (this.mLock) {
            if (forAllActiveRules(new ToBooleanFunction() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda0
                public final boolean apply(Object obj) {
                    boolean shouldInterceptKey;
                    shouldInterceptKey = ((KeyCombinationManager.TwoKeysCombinationRule) obj).shouldInterceptKey(26);
                    return shouldInterceptKey;
                }
            })) {
                return this.mDownTimes.size() > 1 || this.mDownTimes.get(26) == 0;
            }
            return false;
        }
    }

    private void forAllRules(ArrayList<TwoKeysCombinationRule> arrayList, Consumer<TwoKeysCombinationRule> consumer) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            consumer.accept(arrayList.get(i));
        }
    }

    private boolean forAllActiveRules(ToBooleanFunction<TwoKeysCombinationRule> toBooleanFunction) {
        int size = this.mActiveRules.size();
        for (int i = 0; i < size; i++) {
            if (toBooleanFunction.apply(this.mActiveRules.get(i))) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final String str, final PrintWriter printWriter) {
        printWriter.println(str + "KeyCombination rules:");
        forAllRules(this.mRules, new Consumer() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyCombinationManager.lambda$dump$4(printWriter, str, (KeyCombinationManager.TwoKeysCombinationRule) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dump$4(PrintWriter printWriter, String str, TwoKeysCombinationRule twoKeysCombinationRule) {
        printWriter.println(str + "  " + twoKeysCombinationRule);
    }

    public IKeyCombinationManagerWrapper getWrapper() {
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class KeyCombinationManagerWrapper implements IKeyCombinationManagerWrapper {
        private KeyCombinationManagerWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IKeyCombinationManagerExt getExtImpl() {
            return KeyCombinationManager.this.mExt;
        }

        @Override // com.android.server.policy.IKeyCombinationManagerWrapper
        public SparseLongArray getDownTimes() {
            return KeyCombinationManager.this.mDownTimes;
        }

        @Override // com.android.server.policy.IKeyCombinationManagerWrapper
        public ArrayList<TwoKeysCombinationRule> getRules() {
            return KeyCombinationManager.this.mRules;
        }
    }
}
