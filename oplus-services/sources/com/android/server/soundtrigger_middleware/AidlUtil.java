package com.android.server.soundtrigger_middleware;

import android.media.soundtrigger.PhraseRecognitionEvent;
import android.media.soundtrigger.PhraseRecognitionExtra;
import android.media.soundtrigger.RecognitionEvent;
import android.media.soundtrigger_middleware.PhraseRecognitionEventSys;
import android.media.soundtrigger_middleware.RecognitionEventSys;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AidlUtil {
    static RecognitionEvent newEmptyRecognitionEvent() {
        RecognitionEvent recognitionEvent = new RecognitionEvent();
        recognitionEvent.data = new byte[0];
        return recognitionEvent;
    }

    static PhraseRecognitionEvent newEmptyPhraseRecognitionEvent() {
        PhraseRecognitionEvent phraseRecognitionEvent = new PhraseRecognitionEvent();
        phraseRecognitionEvent.common = newEmptyRecognitionEvent();
        phraseRecognitionEvent.phraseExtras = new PhraseRecognitionExtra[0];
        return phraseRecognitionEvent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RecognitionEventSys newAbortEvent() {
        RecognitionEvent newEmptyRecognitionEvent = newEmptyRecognitionEvent();
        newEmptyRecognitionEvent.type = 1;
        newEmptyRecognitionEvent.status = 1;
        RecognitionEventSys recognitionEventSys = new RecognitionEventSys();
        recognitionEventSys.recognitionEvent = newEmptyRecognitionEvent;
        return recognitionEventSys;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PhraseRecognitionEventSys newAbortPhraseEvent() {
        PhraseRecognitionEvent newEmptyPhraseRecognitionEvent = newEmptyPhraseRecognitionEvent();
        RecognitionEvent recognitionEvent = newEmptyPhraseRecognitionEvent.common;
        recognitionEvent.type = 0;
        recognitionEvent.status = 1;
        PhraseRecognitionEventSys phraseRecognitionEventSys = new PhraseRecognitionEventSys();
        phraseRecognitionEventSys.phraseRecognitionEvent = newEmptyPhraseRecognitionEvent;
        return phraseRecognitionEventSys;
    }
}
