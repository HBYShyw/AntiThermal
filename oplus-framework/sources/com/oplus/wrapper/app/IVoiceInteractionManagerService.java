package com.oplus.wrapper.app;

import android.content.ComponentName;
import android.content.Intent;
import android.hardware.soundtrigger.KeyphraseMetadata;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.AudioFormat;
import android.media.permission.Identity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback;
import android.service.voice.IVisualQueryDetectionVoiceInteractionCallback;
import android.service.voice.IVoiceInteractionSession;
import com.android.internal.app.IHotwordRecognitionStatusCallback;
import com.android.internal.app.IVisualQueryDetectionAttentionListener;
import com.android.internal.app.IVoiceActionCheckCallback;
import com.android.internal.app.IVoiceInteractionManagerService;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.app.IVoiceInteractionSoundTriggerSession;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;

/* loaded from: classes.dex */
public interface IVoiceInteractionManagerService {
    boolean showSessionFromSession(IBinder iBinder, Bundle bundle, int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IVoiceInteractionManagerService {
        private final com.android.internal.app.IVoiceInteractionManagerService mTarget = new IVoiceInteractionManagerService.Stub() { // from class: com.oplus.wrapper.app.IVoiceInteractionManagerService.Stub.1
            public void showSession(Bundle bundle, int i, String s) throws RemoteException {
            }

            public boolean deliverNewSession(IBinder iBinder, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor) throws RemoteException {
                return false;
            }

            public boolean showSessionFromSession(IBinder iBinder, Bundle bundle, int i, String s) throws RemoteException {
                return Stub.this.showSessionFromSession(iBinder, bundle, i, s);
            }

            public boolean hideSessionFromSession(IBinder iBinder) throws RemoteException {
                return false;
            }

            public int startVoiceActivity(IBinder iBinder, Intent intent, String s, String s1) throws RemoteException {
                return 0;
            }

            public int startAssistantActivity(IBinder iBinder, Intent intent, String s, String s1, Bundle bundle) throws RemoteException {
                return 0;
            }

            public void setKeepAwake(IBinder iBinder, boolean b) throws RemoteException {
            }

            public void closeSystemDialogs(IBinder iBinder) throws RemoteException {
            }

            public void finish(IBinder iBinder) throws RemoteException {
            }

            public void setDisabledShowContext(int i) throws RemoteException {
            }

            public int getDisabledShowContext() throws RemoteException {
                return 0;
            }

            public int getUserDisabledShowContext() throws RemoteException {
                return 0;
            }

            public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int i, String s) throws RemoteException {
                return null;
            }

            public int updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) throws RemoteException {
                return 0;
            }

            public int deleteKeyphraseSoundModel(int i, String s) throws RemoteException {
                return 0;
            }

            public void setModelDatabaseForTestEnabled(boolean b, IBinder iBinder) throws RemoteException {
            }

            public boolean isEnrolledForKeyphrase(int i, String s) throws RemoteException {
                return false;
            }

            public KeyphraseMetadata getEnrolledKeyphraseMetadata(String s, String s1) throws RemoteException {
                return null;
            }

            public ComponentName getActiveServiceComponentName() throws RemoteException {
                return null;
            }

            public boolean showSessionForActiveService(Bundle bundle, int i, String s, IVoiceInteractionSessionShowCallback iVoiceInteractionSessionShowCallback, IBinder iBinder) throws RemoteException {
                return false;
            }

            public void hideCurrentSession() throws RemoteException {
            }

            public void launchVoiceAssistFromKeyguard() throws RemoteException {
            }

            public boolean isSessionRunning() throws RemoteException {
                return false;
            }

            public boolean activeServiceSupportsAssist() throws RemoteException {
                return false;
            }

            public boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException {
                return false;
            }

            public void onLockscreenShown() throws RemoteException {
            }

            public void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener iVoiceInteractionSessionListener) throws RemoteException {
            }

            public void getActiveServiceSupportedActions(List<String> list, IVoiceActionCheckCallback iVoiceActionCheckCallback) throws RemoteException {
            }

            public void setUiHints(Bundle bundle) throws RemoteException {
            }

            public void requestDirectActions(IBinder iBinder, int i, IBinder iBinder1, RemoteCallback remoteCallback, RemoteCallback remoteCallback1) throws RemoteException {
            }

            public void performDirectAction(IBinder iBinder, String s, Bundle bundle, int i, IBinder iBinder1, RemoteCallback remoteCallback, RemoteCallback remoteCallback1) throws RemoteException {
            }

            public void setDisabled(boolean b) throws RemoteException {
            }

            public IVoiceInteractionSoundTriggerSession createSoundTriggerSessionAsOriginator(Identity identity, IBinder iBinder, SoundTrigger.ModuleProperties moduleProperties) throws RemoteException {
                return null;
            }

            public List<SoundTrigger.ModuleProperties> listModuleProperties(Identity identity) throws RemoteException {
                return null;
            }

            public void updateState(PersistableBundle persistableBundle, SharedMemory sharedMemory, IBinder iBinder) throws RemoteException {
            }

            public void initAndVerifyDetector(Identity identity, PersistableBundle persistableBundle, SharedMemory sharedMemory, IBinder iBinder, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback, int i) throws RemoteException {
            }

            public void destroyDetector(IBinder iBinder) throws RemoteException {
            }

            public void shutdownHotwordDetectionService() throws RemoteException {
            }

            public void enableVisualQueryDetection(IVisualQueryDetectionAttentionListener iVisualQueryDetectionAttentionListener) throws RemoteException {
            }

            public void disableVisualQueryDetection() throws RemoteException {
            }

            public void startPerceiving(IVisualQueryDetectionVoiceInteractionCallback iVisualQueryDetectionVoiceInteractionCallback) throws RemoteException {
            }

            public void stopPerceiving() throws RemoteException {
            }

            public void startListeningFromMic(AudioFormat audioFormat, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) throws RemoteException {
            }

            public void stopListeningFromMic() throws RemoteException {
            }

            public void startListeningFromExternalSource(ParcelFileDescriptor parcelFileDescriptor, AudioFormat audioFormat, PersistableBundle persistableBundle, IBinder iBinder, IMicrophoneHotwordDetectionVoiceInteractionCallback iMicrophoneHotwordDetectionVoiceInteractionCallback) throws RemoteException {
            }

            public void triggerHardwareRecognitionEventForTest(SoundTrigger.KeyphraseRecognitionEvent keyphraseRecognitionEvent, IHotwordRecognitionStatusCallback iHotwordRecognitionStatusCallback) throws RemoteException {
            }

            public void startListeningVisibleActivityChanged(IBinder iBinder) throws RemoteException {
            }

            public void stopListeningVisibleActivityChanged(IBinder iBinder) throws RemoteException {
            }

            public void setSessionWindowVisible(IBinder iBinder, boolean b) throws RemoteException {
            }

            public void notifyActivityEventChanged(IBinder iBinder, int i) throws RemoteException {
            }
        };

        public static IVoiceInteractionManagerService asInterface(IBinder obj) {
            return new Proxy(IVoiceInteractionManagerService.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IVoiceInteractionManagerService {
            private final com.android.internal.app.IVoiceInteractionManagerService mTarget;

            Proxy(com.android.internal.app.IVoiceInteractionManagerService target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.IVoiceInteractionManagerService
            public boolean showSessionFromSession(IBinder token, Bundle sessionArgs, int flags, String attributionTag) throws RemoteException {
                return this.mTarget.showSessionFromSession(token, sessionArgs, flags, attributionTag);
            }
        }
    }
}
