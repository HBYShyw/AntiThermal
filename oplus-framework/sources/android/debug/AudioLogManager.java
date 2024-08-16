package android.debug;

import android.content.Context;
import android.media.AudioManager;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.util.OplusResolverIntentUtil;

/* loaded from: classes.dex */
public class AudioLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + AudioLogManager.class.getSimpleName();
    private static volatile AudioLogManager sInstance = null;
    private AudioManager mAudioManager;
    private Context mContext;

    public AudioLogManager(Context context) {
        this.mAudioManager = null;
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService(OplusResolverIntentUtil.DEFAULT_APP_AUDIO);
    }

    public static AudioLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AudioLogManager.class) {
                if (sInstance == null) {
                    sInstance = new AudioLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.d(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.getWrapper().getExtImpl().setLogOn();
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.d(TAG, "setLogOff: ");
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.getWrapper().getExtImpl().setLogOff();
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.d(TAG, "setLogDump, audio setLogOff begin.");
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            audioManager.getWrapper().getExtImpl().setLogDump();
        }
        setLogOff();
    }
}
