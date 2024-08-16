package android.inputmethodservice;

import android.view.InsetsState;

/* loaded from: classes.dex */
public class OplusInputMethodServiceInternal {
    private static final String TAG = OplusInputMethodServiceInternal.class.getSimpleName();
    private InputMethodServiceExtImpl mServcieExt;
    private InputMethodService mService;

    /* loaded from: classes.dex */
    private static final class OplusInputMethodServiceHolder {
        private static final OplusInputMethodServiceInternal INSTANCE = new OplusInputMethodServiceInternal();

        private OplusInputMethodServiceHolder() {
        }
    }

    private OplusInputMethodServiceInternal() {
    }

    public static OplusInputMethodServiceInternal getInstance() {
        return OplusInputMethodServiceHolder.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(InputMethodService service, InputMethodServiceExtImpl serviceExt) {
        this.mService = service;
        this.mServcieExt = serviceExt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        this.mService = null;
        this.mServcieExt = null;
    }

    public boolean existInputMethodService() {
        return (this.mService == null || this.mServcieExt == null) ? false : true;
    }

    public void updateNavInsets(InsetsState insetsState) {
        if (existInputMethodService()) {
            this.mServcieExt.updateNavInsets(insetsState);
        }
    }
}
