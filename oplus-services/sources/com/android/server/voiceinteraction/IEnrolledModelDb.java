package com.android.server.voiceinteraction;

import android.hardware.soundtrigger.SoundTrigger;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IEnrolledModelDb {
    boolean deleteKeyphraseSoundModel(int i, int i2, String str);

    void dump(PrintWriter printWriter);

    SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int i, int i2, String str);

    SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(String str, int i, String str2);

    boolean updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel);
}
