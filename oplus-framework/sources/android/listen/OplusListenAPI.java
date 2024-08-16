package android.listen;

import com.qualcomm.listen.ListenSoundModel;
import com.qualcomm.listen.ListenTypes;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class OplusListenAPI {
    public static ListenTypes.SoundModelInfo query(ByteBuffer soundModel) {
        return ListenSoundModel.query(soundModel);
    }
}
