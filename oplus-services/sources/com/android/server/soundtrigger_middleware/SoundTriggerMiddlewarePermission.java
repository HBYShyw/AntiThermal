package com.android.server.soundtrigger_middleware;

import android.content.Context;
import android.media.permission.Identity;
import android.media.permission.IdentityContext;
import android.media.permission.PermissionUtil;
import android.media.soundtrigger.ModelParameterRange;
import android.media.soundtrigger.PhraseSoundModel;
import android.media.soundtrigger.RecognitionConfig;
import android.media.soundtrigger.SoundModel;
import android.media.soundtrigger_middleware.ISoundTriggerCallback;
import android.media.soundtrigger_middleware.ISoundTriggerModule;
import android.media.soundtrigger_middleware.PhraseRecognitionEventSys;
import android.media.soundtrigger_middleware.RecognitionEventSys;
import android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor;
import android.os.IBinder;
import android.os.RemoteException;
import com.android.server.LocalServices;
import com.android.server.pm.permission.LegacyPermissionManagerInternal;
import java.io.PrintWriter;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SoundTriggerMiddlewarePermission implements ISoundTriggerMiddlewareInternal, Dumpable {
    private static final String TAG = "SoundTriggerMiddlewarePermission";
    private final Context mContext;
    private final ISoundTriggerMiddlewareInternal mDelegate;

    public SoundTriggerMiddlewarePermission(ISoundTriggerMiddlewareInternal iSoundTriggerMiddlewareInternal, Context context) {
        this.mDelegate = iSoundTriggerMiddlewareInternal;
        this.mContext = context;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public SoundTriggerModuleDescriptor[] listModules() {
        enforcePermissionForPreflight(this.mContext, getIdentity(), "android.permission.CAPTURE_AUDIO_HOTWORD", true);
        return this.mDelegate.listModules();
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public ISoundTriggerModule attach(int i, ISoundTriggerCallback iSoundTriggerCallback, boolean z) {
        Identity identity = getIdentity();
        enforcePermissionsForPreflight(identity);
        ModuleWrapper moduleWrapper = new ModuleWrapper(identity, iSoundTriggerCallback, z);
        return moduleWrapper.attach(this.mDelegate.attach(i, moduleWrapper.getCallbackWrapper(), z));
    }

    public String toString() {
        return Objects.toString(this.mDelegate);
    }

    private static Identity getIdentity() {
        return IdentityContext.getNonNull();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforcePermissionsForPreflight(Identity identity) {
        enforcePermissionForPreflight(this.mContext, identity, "android.permission.RECORD_AUDIO", true);
        enforcePermissionForPreflight(this.mContext, identity, "android.permission.CAPTURE_AUDIO_HOTWORD", true);
    }

    void enforcePermissionsForDataDelivery(Identity identity, String str) {
        enforceSoundTriggerRecordAudioPermissionForDataDelivery(identity, str);
        enforcePermissionForDataDelivery(this.mContext, identity, "android.permission.CAPTURE_AUDIO_HOTWORD", str);
    }

    private static void enforcePermissionForDataDelivery(Context context, Identity identity, String str, String str2) {
        if (PermissionUtil.checkPermissionForDataDelivery(context, identity, str, str2) != 0) {
            throw new SecurityException(String.format("Failed to obtain permission %s for identity %s", str, ObjectPrinter.print(identity, 16)));
        }
    }

    private static void enforceSoundTriggerRecordAudioPermissionForDataDelivery(Identity identity, String str) {
        if (((LegacyPermissionManagerInternal) LocalServices.getService(LegacyPermissionManagerInternal.class)).checkSoundTriggerRecordAudioPermissionForDataDelivery(identity.uid, identity.packageName, identity.attributionTag, str) != 0) {
            throw new SecurityException(String.format("Failed to obtain permission RECORD_AUDIO for identity %s", ObjectPrinter.print(identity, 16)));
        }
    }

    private static void enforcePermissionForPreflight(Context context, Identity identity, String str, boolean z) {
        int checkPermissionForPreflight = PermissionUtil.checkPermissionForPreflight(context, identity, str);
        if (checkPermissionForPreflight != 0) {
            if (checkPermissionForPreflight != 1) {
                if (checkPermissionForPreflight != 2) {
                    throw new RuntimeException("Unexpected perimission check result.");
                }
            } else if (z) {
                return;
            }
            throw new SecurityException(String.format("Failed to obtain permission %s for identity %s", str, ObjectPrinter.print(identity, 16)));
        }
    }

    @Override // com.android.server.soundtrigger_middleware.Dumpable
    public void dump(PrintWriter printWriter) {
        ISoundTriggerMiddlewareInternal iSoundTriggerMiddlewareInternal = this.mDelegate;
        if (iSoundTriggerMiddlewareInternal instanceof Dumpable) {
            ((Dumpable) iSoundTriggerMiddlewareInternal).dump(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ModuleWrapper extends ISoundTriggerModule.Stub {
        private final CallbackWrapper mCallbackWrapper;
        private ISoundTriggerModule mDelegate;
        private final boolean mIsTrusted;
        private final Identity mOriginatorIdentity;

        ModuleWrapper(Identity identity, ISoundTriggerCallback iSoundTriggerCallback, boolean z) {
            this.mOriginatorIdentity = identity;
            this.mCallbackWrapper = new CallbackWrapper(iSoundTriggerCallback);
            this.mIsTrusted = z;
        }

        ModuleWrapper attach(ISoundTriggerModule iSoundTriggerModule) {
            this.mDelegate = iSoundTriggerModule;
            return this;
        }

        ISoundTriggerCallback getCallbackWrapper() {
            return this.mCallbackWrapper;
        }

        public int loadModel(SoundModel soundModel) throws RemoteException {
            enforcePermissions();
            return this.mDelegate.loadModel(soundModel);
        }

        public int loadPhraseModel(PhraseSoundModel phraseSoundModel) throws RemoteException {
            enforcePermissions();
            return this.mDelegate.loadPhraseModel(phraseSoundModel);
        }

        public void unloadModel(int i) throws RemoteException {
            this.mDelegate.unloadModel(i);
        }

        public IBinder startRecognition(int i, RecognitionConfig recognitionConfig) throws RemoteException {
            enforcePermissions();
            return this.mDelegate.startRecognition(i, recognitionConfig);
        }

        public void stopRecognition(int i) throws RemoteException {
            this.mDelegate.stopRecognition(i);
        }

        public void forceRecognitionEvent(int i) throws RemoteException {
            enforcePermissions();
            this.mDelegate.forceRecognitionEvent(i);
        }

        public void setModelParameter(int i, int i2, int i3) throws RemoteException {
            enforcePermissions();
            this.mDelegate.setModelParameter(i, i2, i3);
        }

        public int getModelParameter(int i, int i2) throws RemoteException {
            enforcePermissions();
            return this.mDelegate.getModelParameter(i, i2);
        }

        public ModelParameterRange queryModelParameterSupport(int i, int i2) throws RemoteException {
            enforcePermissions();
            return this.mDelegate.queryModelParameterSupport(i, i2);
        }

        public void detach() throws RemoteException {
            this.mDelegate.detach();
        }

        public String toString() {
            return Objects.toString(this.mDelegate);
        }

        private void enforcePermissions() {
            SoundTriggerMiddlewarePermission.this.enforcePermissionsForPreflight(this.mOriginatorIdentity);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class CallbackWrapper implements ISoundTriggerCallback {
            private final ISoundTriggerCallback mDelegate;

            private CallbackWrapper(ISoundTriggerCallback iSoundTriggerCallback) {
                this.mDelegate = iSoundTriggerCallback;
            }

            public void onRecognition(int i, RecognitionEventSys recognitionEventSys, int i2) throws RemoteException {
                enforcePermissions("Sound trigger recognition.");
                this.mDelegate.onRecognition(i, recognitionEventSys, i2);
            }

            public void onPhraseRecognition(int i, PhraseRecognitionEventSys phraseRecognitionEventSys, int i2) throws RemoteException {
                enforcePermissions("Sound trigger phrase recognition.");
                this.mDelegate.onPhraseRecognition(i, phraseRecognitionEventSys, i2);
            }

            public void onResourcesAvailable() throws RemoteException {
                this.mDelegate.onResourcesAvailable();
            }

            public void onModelUnloaded(int i) throws RemoteException {
                this.mDelegate.onModelUnloaded(i);
            }

            public void onModuleDied() throws RemoteException {
                this.mDelegate.onModuleDied();
            }

            public IBinder asBinder() {
                return this.mDelegate.asBinder();
            }

            public String toString() {
                return this.mDelegate.toString();
            }

            private void enforcePermissions(String str) {
                if (ModuleWrapper.this.mIsTrusted) {
                    ModuleWrapper moduleWrapper = ModuleWrapper.this;
                    SoundTriggerMiddlewarePermission.this.enforcePermissionsForPreflight(moduleWrapper.mOriginatorIdentity);
                } else {
                    ModuleWrapper moduleWrapper2 = ModuleWrapper.this;
                    SoundTriggerMiddlewarePermission.this.enforcePermissionsForDataDelivery(moduleWrapper2.mOriginatorIdentity, str);
                }
            }
        }
    }
}
