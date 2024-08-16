package com.android.server;

import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.ModelParams;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.permission.Identity;
import android.os.IBinder;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface SoundTriggerInternal {
    public static final int STATUS_ERROR = Integer.MIN_VALUE;
    public static final int STATUS_OK = 0;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Session {
        void detach();

        SoundTrigger.ModuleProperties getModuleProperties();

        int getParameter(int i, @ModelParams int i2);

        SoundTrigger.ModelParamRange queryParameter(int i, @ModelParams int i2);

        int setParameter(int i, @ModelParams int i2, int i3);

        int startRecognition(int i, SoundTrigger.KeyphraseSoundModel keyphraseSoundModel, IRecognitionStatusCallback iRecognitionStatusCallback, SoundTrigger.RecognitionConfig recognitionConfig, boolean z);

        int stopRecognition(int i, IRecognitionStatusCallback iRecognitionStatusCallback);

        int unloadKeyphraseModel(int i);
    }

    Session attach(IBinder iBinder, SoundTrigger.ModuleProperties moduleProperties, boolean z);

    List<SoundTrigger.ModuleProperties> listModuleProperties(Identity identity);
}
