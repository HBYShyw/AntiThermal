package com.oplus.wrapper.hardware.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger;
import android.os.Handler;
import com.oplus.wrapper.media.permission.Identity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class SoundTrigger {
    private final Map<StatusListener, StatusListenerImpl> mRegisterStatusListenerCache = new ConcurrentHashMap();
    public static final int STATUS_BAD_VALUE = getStatusBadValue();
    public static final int STATUS_DEAD_OBJECT = getStatusDeadObject();
    public static final int STATUS_INVALID_OPERATION = getStatusInvalidOperation();
    public static final int STATUS_NO_INIT = getStatusNoInit();
    public static final int STATUS_PERMISSION_DENIED = getStatusPermissionDenied();
    public static final int RECOGNITION_MODE_VOICE_TRIGGER = getRecognitionModeVoiceTrigger();
    public static final int RECOGNITION_MODE_USER_IDENTIFICATION = getRecognitionModeUserIdentification();
    public static final int STATUS_OK = getStatusOk();
    public static final int STATUS_ERROR = getStatusError();
    public static final int RECOGNITION_STATUS_ABORT = getRecognitionStatusAbort();

    /* loaded from: classes.dex */
    public interface StatusListener {
        void onModelUnloaded(int i);

        void onRecognition(RecognitionEvent recognitionEvent);

        void onResourcesAvailable();

        void onServiceDied();
    }

    private static int getStatusBadValue() {
        return android.hardware.soundtrigger.SoundTrigger.STATUS_BAD_VALUE;
    }

    private static int getStatusDeadObject() {
        return android.hardware.soundtrigger.SoundTrigger.STATUS_DEAD_OBJECT;
    }

    private static int getStatusInvalidOperation() {
        return android.hardware.soundtrigger.SoundTrigger.STATUS_INVALID_OPERATION;
    }

    private static int getStatusNoInit() {
        return android.hardware.soundtrigger.SoundTrigger.STATUS_NO_INIT;
    }

    private static int getStatusPermissionDenied() {
        return android.hardware.soundtrigger.SoundTrigger.STATUS_PERMISSION_DENIED;
    }

    private static int getRecognitionModeVoiceTrigger() {
        return 1;
    }

    private static int getRecognitionModeUserIdentification() {
        return 2;
    }

    private static int getStatusOk() {
        return 0;
    }

    private static int getStatusError() {
        return Integer.MIN_VALUE;
    }

    private static int getRecognitionStatusAbort() {
        return 1;
    }

    /* loaded from: classes.dex */
    public static class ConfidenceLevel {
        private final SoundTrigger.ConfidenceLevel mConfidenceLevel;

        public ConfidenceLevel(int userId, int confidenceLevel) {
            this.mConfidenceLevel = new SoundTrigger.ConfidenceLevel(userId, confidenceLevel);
        }

        ConfidenceLevel(SoundTrigger.ConfidenceLevel confidenceLevel) {
            this.mConfidenceLevel = confidenceLevel;
        }

        public int getUserId() {
            return this.mConfidenceLevel.userId;
        }

        SoundTrigger.ConfidenceLevel getConfidenceLevel() {
            return this.mConfidenceLevel;
        }
    }

    /* loaded from: classes.dex */
    public static class Keyphrase {
        private final SoundTrigger.Keyphrase mKeyphrase;

        public Keyphrase(int id, int recognitionModes, Locale locale, String text, int[] users) {
            this.mKeyphrase = new SoundTrigger.Keyphrase(id, recognitionModes, locale, text, users);
        }

        public int[] getUsers() {
            return this.mKeyphrase.getUsers();
        }

        SoundTrigger.Keyphrase getKeyphrase() {
            return this.mKeyphrase;
        }
    }

    /* loaded from: classes.dex */
    public static final class KeyphraseRecognitionExtra {
        private static final int HUNDRED = 100;
        private static final int ZERO = 0;
        private final SoundTrigger.KeyphraseRecognitionExtra mKeyphraseRecognitionExtra;

        public KeyphraseRecognitionExtra(int id, int recognitionModes, int coarseConfidenceLevel, ConfidenceLevel[] confidenceLevels) {
            SoundTrigger.ConfidenceLevel[] internalConfidenceLevels = new SoundTrigger.ConfidenceLevel[confidenceLevels.length];
            for (int i = 0; i < confidenceLevels.length; i++) {
                internalConfidenceLevels[i] = confidenceLevels[i].getConfidenceLevel();
            }
            this.mKeyphraseRecognitionExtra = new SoundTrigger.KeyphraseRecognitionExtra(id, recognitionModes, coarseConfidenceLevel, internalConfidenceLevels);
        }

        SoundTrigger.KeyphraseRecognitionExtra getKeyphraseRecognitionExtra() {
            return this.mKeyphraseRecognitionExtra;
        }

        public ConfidenceLevel[] getconfidenceLevels() {
            ConfidenceLevel[] confidenceLevels = new ConfidenceLevel[this.mKeyphraseRecognitionExtra.confidenceLevels.length];
            for (int i = 0; i < confidenceLevels.length; i++) {
                confidenceLevels[i] = new ConfidenceLevel(this.mKeyphraseRecognitionExtra.confidenceLevels[i]);
            }
            return confidenceLevels;
        }
    }

    /* loaded from: classes.dex */
    public static final class KeyphraseSoundModel {
        private final SoundTrigger.KeyphraseSoundModel mKeyphraseSoundModel;

        public KeyphraseSoundModel(UUID uuid, UUID vendorUuid, byte[] data, Keyphrase[] keyphrases) {
            SoundTrigger.Keyphrase[] internalKeyphrases = new SoundTrigger.Keyphrase[keyphrases.length];
            for (int i = 0; i < keyphrases.length; i++) {
                internalKeyphrases[i] = keyphrases[i].getKeyphrase();
            }
            this.mKeyphraseSoundModel = new SoundTrigger.KeyphraseSoundModel(uuid, vendorUuid, data, internalKeyphrases);
        }

        public SoundModel getSoundModel() {
            return new SoundModel(this);
        }
    }

    /* loaded from: classes.dex */
    public static final class ModuleProperties {
        public SoundTrigger.ModuleProperties mModuleProperties;

        ModuleProperties(SoundTrigger.ModuleProperties moduleProperties) {
            this.mModuleProperties = moduleProperties;
        }

        public int getId() {
            return this.mModuleProperties.getId();
        }

        public UUID getUuid() {
            return this.mModuleProperties.getUuid();
        }
    }

    /* loaded from: classes.dex */
    public static final class RecognitionConfig {
        private final SoundTrigger.RecognitionConfig mRecognitionConfig;

        public RecognitionConfig(boolean captureRequested, boolean allowMultipleTriggers, KeyphraseRecognitionExtra[] keyphrases, byte[] data) {
            SoundTrigger.KeyphraseRecognitionExtra[] internalKeyphraseRecognitionExtra = new SoundTrigger.KeyphraseRecognitionExtra[keyphrases.length];
            for (int i = 0; i < keyphrases.length; i++) {
                internalKeyphraseRecognitionExtra[i] = keyphrases[i].getKeyphraseRecognitionExtra();
            }
            this.mRecognitionConfig = new SoundTrigger.RecognitionConfig(captureRequested, allowMultipleTriggers, internalKeyphraseRecognitionExtra, data);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SoundTrigger.RecognitionConfig getRecognitionConfig() {
            return this.mRecognitionConfig;
        }
    }

    /* loaded from: classes.dex */
    public static class RecognitionEvent {
        private final SoundTrigger.RecognitionEvent mRecognitionEvent;

        RecognitionEvent(SoundTrigger.RecognitionEvent recognitionEvent) {
            this.mRecognitionEvent = recognitionEvent;
        }

        public byte[] getData() {
            return this.mRecognitionEvent.data;
        }

        public int getSoundModelHandle() {
            return this.mRecognitionEvent.soundModelHandle;
        }

        public int getStatus() {
            return this.mRecognitionEvent.status;
        }

        public int getCaptureSession() {
            return this.mRecognitionEvent.getCaptureSession();
        }
    }

    /* loaded from: classes.dex */
    public static class GenericSoundModel {
        private final SoundTrigger.GenericSoundModel mGenericSoundModel;

        public GenericSoundModel(UUID uuid, UUID vendorUuid, byte[] data) {
            this.mGenericSoundModel = new SoundTrigger.GenericSoundModel(uuid, vendorUuid, data);
        }
    }

    /* loaded from: classes.dex */
    public static class SoundModel {
        private SoundTrigger.SoundModel mSoundModel;

        SoundModel(SoundTrigger.SoundModel soundModel) {
            this.mSoundModel = soundModel;
        }

        public SoundModel(KeyphraseSoundModel keyphraseSoundModel) {
            this.mSoundModel = keyphraseSoundModel.mKeyphraseSoundModel;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SoundTrigger.SoundModel getSoundModel() {
            return this.mSoundModel;
        }
    }

    @Deprecated
    public static int listModulesAsOriginator(ArrayList<ModuleProperties> modules, Identity originatorIdentity) {
        ArrayList<SoundTrigger.ModuleProperties> internalModules = new ArrayList<>();
        int result = android.hardware.soundtrigger.SoundTrigger.listModulesAsOriginator(internalModules, originatorIdentity.getIdentity());
        Iterator<SoundTrigger.ModuleProperties> it = internalModules.iterator();
        while (it.hasNext()) {
            SoundTrigger.ModuleProperties moduleProperties = it.next();
            modules.add(new ModuleProperties(moduleProperties));
        }
        return result;
    }

    /* loaded from: classes.dex */
    static class StatusListenerImpl implements SoundTrigger.StatusListener {
        private final StatusListener mStatusListener;

        public StatusListenerImpl(StatusListener statusListener) {
            this.mStatusListener = statusListener;
        }

        public void onRecognition(SoundTrigger.RecognitionEvent recognitionEvent) {
            RecognitionEvent wrapperRecognitionEvent = new RecognitionEvent(recognitionEvent);
            this.mStatusListener.onRecognition(wrapperRecognitionEvent);
        }

        public void onModelUnloaded(int i) {
            this.mStatusListener.onModelUnloaded(i);
        }

        public void onResourcesAvailable() {
            this.mStatusListener.onResourcesAvailable();
        }

        public void onServiceDied() {
            this.mStatusListener.onServiceDied();
        }
    }

    public SoundTriggerModule attachModuleAsOriginator(int moduleId, StatusListener listener, Handler handler, Identity originatorIdentity) {
        StatusListenerImpl statusListener = this.mRegisterStatusListenerCache.get(listener);
        if (statusListener == null) {
            statusListener = new StatusListenerImpl(listener);
            this.mRegisterStatusListenerCache.put(listener, statusListener);
        }
        return new SoundTriggerModule(android.hardware.soundtrigger.SoundTrigger.attachModuleAsOriginator(moduleId, statusListener, handler, originatorIdentity.getIdentity()));
    }
}
