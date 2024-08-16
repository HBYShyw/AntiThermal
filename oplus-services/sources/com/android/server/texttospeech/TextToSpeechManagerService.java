package com.android.server.texttospeech;

import android.content.Context;
import android.os.UserHandle;
import android.speech.tts.ITextToSpeechManager;
import android.speech.tts.ITextToSpeechSessionCallback;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.texttospeech.TextToSpeechManagerPerUserService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class TextToSpeechManagerService extends AbstractMasterSystemService<TextToSpeechManagerService, TextToSpeechManagerPerUserService> {
    private static final String TAG = "TextToSpeechManagerService";

    public TextToSpeechManagerService(Context context) {
        super(context, null, null);
    }

    public void onStart() {
        publishBinderService("texttospeech", new TextToSpeechManagerServiceStub());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public TextToSpeechManagerPerUserService newServiceLocked(int i, boolean z) {
        return new TextToSpeechManagerPerUserService(this, this.mLock, i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class TextToSpeechManagerServiceStub extends ITextToSpeechManager.Stub {
        private TextToSpeechManagerServiceStub() {
        }

        public void createSession(String str, final ITextToSpeechSessionCallback iTextToSpeechSessionCallback) {
            synchronized (((AbstractMasterSystemService) TextToSpeechManagerService.this).mLock) {
                TextToSpeechManagerPerUserService textToSpeechManagerPerUserService = (TextToSpeechManagerPerUserService) TextToSpeechManagerService.this.getServiceForUserLocked(UserHandle.getCallingUserId());
                if (textToSpeechManagerPerUserService != null) {
                    textToSpeechManagerPerUserService.createSessionLocked(str, iTextToSpeechSessionCallback);
                } else {
                    TextToSpeechManagerPerUserService.runSessionCallbackMethod(new TextToSpeechManagerPerUserService.ThrowingRunnable() { // from class: com.android.server.texttospeech.TextToSpeechManagerService$TextToSpeechManagerServiceStub$$ExternalSyntheticLambda0
                        @Override // com.android.server.texttospeech.TextToSpeechManagerPerUserService.ThrowingRunnable
                        public final void runOrThrow() {
                            iTextToSpeechSessionCallback.onError("Service is not available for user");
                        }
                    });
                }
            }
        }
    }
}
