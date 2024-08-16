package t2;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.provider.Settings;
import java.util.HashMap;

/* compiled from: COUISoundLoadUtil.java */
/* renamed from: t2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUISoundLoadUtil {

    /* renamed from: c, reason: collision with root package name */
    private static COUISoundLoadUtil f18556c;

    /* renamed from: a, reason: collision with root package name */
    private HashMap<Integer, Integer> f18557a = new HashMap<>();

    /* renamed from: b, reason: collision with root package name */
    private SoundPool f18558b;

    private COUISoundLoadUtil() {
        b();
    }

    public static synchronized COUISoundLoadUtil a() {
        COUISoundLoadUtil cOUISoundLoadUtil;
        synchronized (COUISoundLoadUtil.class) {
            if (f18556c == null) {
                f18556c = new COUISoundLoadUtil();
            }
            cOUISoundLoadUtil = f18556c;
        }
        return cOUISoundLoadUtil;
    }

    private void b() {
        SoundPool.Builder builder = new SoundPool.Builder();
        AudioAttributes build = new AudioAttributes.Builder().setFlags(128).setLegacyStreamType(1).build();
        builder.setMaxStreams(10);
        builder.setAudioAttributes(build);
        this.f18558b = builder.build();
    }

    private boolean e(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "sound_effects_enabled", 0) != 0;
    }

    public int c(Context context, int i10) {
        if (this.f18557a.containsKey(Integer.valueOf(i10))) {
            return this.f18557a.get(Integer.valueOf(i10)).intValue();
        }
        int load = this.f18558b.load(context, i10, 0);
        this.f18557a.put(Integer.valueOf(i10), Integer.valueOf(load));
        return load;
    }

    public void d(Context context, int i10, float f10, float f11, int i11, int i12, float f12) {
        if (e(context)) {
            this.f18558b.play(i10, f10, f11, i11, i12, f12);
        }
    }
}
