package com.android.server.audio;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlayerBase;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.PrintWriterPrinter;
import com.android.internal.util.XmlUtils;
import com.android.server.utils.EventLogger;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SoundEffectsHelper {
    private static final String ASSET_FILE_VERSION = "1.0";
    private static final String ATTR_ASSET_FILE = "file";
    private static final String ATTR_ASSET_ID = "id";
    private static final String ATTR_GROUP_NAME = "name";
    private static final String ATTR_VERSION = "version";
    private static final int EFFECT_NOT_IN_SOUND_POOL = 0;
    private static final String GROUP_TOUCH_SOUNDS = "touch_sounds";
    private static final int MSG_LOAD_EFFECTS = 0;
    private static final int MSG_LOAD_EFFECTS_TIMEOUT = 3;
    private static final int MSG_PLAY_EFFECT = 2;
    private static final int MSG_UNLOAD_EFFECTS = 1;
    private static final int NUM_SOUNDPOOL_CHANNELS = 4;
    private static final int SOUND_EFFECTS_LOAD_TIMEOUT_MS = 15000;
    private static final String SOUND_EFFECTS_PATH = "/media/audio/ui/";
    private static final String TAG = "AS.SfxHelper";
    private static final String TAG_ASSET = "asset";
    private static final String TAG_AUDIO_ASSETS = "audio_assets";
    private static final String TAG_GROUP = "group";
    private final Context mContext;
    private final Consumer<PlayerBase> mPlayerAvailableCb;
    private final int mSfxAttenuationDb;
    private SfxHandler mSfxHandler;
    private SfxWorker mSfxWorker;
    private SoundPool mSoundPool;
    private SoundPoolLoader mSoundPoolLoader;
    private final EventLogger mSfxLogger = new EventLogger(26, "Sound Effects Loading");
    private final List<Resource> mResources = new ArrayList();
    private final int[] mEffects = new int[16];

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface OnEffectsLoadCompleteHandler {
        void run(boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Resource {
        final String mFileName;
        boolean mLoaded;
        int mSampleId = 0;

        Resource(String str) {
            this.mFileName = str;
        }

        void unload() {
            this.mSampleId = 0;
            this.mLoaded = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SoundEffectsHelper(Context context, Consumer<PlayerBase> consumer) {
        this.mContext = context;
        this.mSfxAttenuationDb = context.getResources().getInteger(R.integer.leanback_setup_alpha_backward_in_content_delay);
        this.mPlayerAvailableCb = consumer;
        startWorker();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadSoundEffects(OnEffectsLoadCompleteHandler onEffectsLoadCompleteHandler) {
        sendMsg(0, 0, 0, onEffectsLoadCompleteHandler, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unloadSoundEffects() {
        sendMsg(1, 0, 0, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playSoundEffect(int i, int i2) {
        sendMsg(2, i, i2, null, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        if (this.mSfxHandler != null) {
            printWriter.println(str + "Message handler (watch for unhandled messages):");
            this.mSfxHandler.dump(new PrintWriterPrinter(printWriter), "  ");
        } else {
            printWriter.println(str + "Message handler is null");
        }
        printWriter.println(str + "Default attenuation (dB): " + this.mSfxAttenuationDb);
        this.mSfxLogger.dump(printWriter);
    }

    private void startWorker() {
        SfxWorker sfxWorker = new SfxWorker();
        this.mSfxWorker = sfxWorker;
        sfxWorker.start();
        synchronized (this) {
            while (this.mSfxHandler == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    Log.w(TAG, "Interrupted while waiting " + this.mSfxWorker.getName() + " to start");
                }
            }
        }
    }

    private void sendMsg(int i, int i2, int i3, Object obj, int i4) {
        SfxHandler sfxHandler = this.mSfxHandler;
        sfxHandler.sendMessageDelayed(sfxHandler.obtainMessage(i, i2, i3, obj), i4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logEvent(String str) {
        this.mSfxLogger.enqueue(new EventLogger.StringEvent(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLoadSoundEffects(OnEffectsLoadCompleteHandler onEffectsLoadCompleteHandler) {
        SoundPoolLoader soundPoolLoader = this.mSoundPoolLoader;
        if (soundPoolLoader != null) {
            soundPoolLoader.addHandler(onEffectsLoadCompleteHandler);
            return;
        }
        if (this.mSoundPool != null) {
            if (onEffectsLoadCompleteHandler != null) {
                onEffectsLoadCompleteHandler.run(true);
                return;
            }
            return;
        }
        logEvent("effects loading started");
        PlayerBase build = new SoundPool.Builder().setMaxStreams(4).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        this.mSoundPool = build;
        this.mPlayerAvailableCb.accept(build);
        loadSoundAssets();
        SoundPoolLoader soundPoolLoader2 = new SoundPoolLoader();
        this.mSoundPoolLoader = soundPoolLoader2;
        soundPoolLoader2.addHandler(new OnEffectsLoadCompleteHandler() { // from class: com.android.server.audio.SoundEffectsHelper.1
            @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
            public void run(boolean z) {
                SoundEffectsHelper.this.mSoundPoolLoader = null;
                if (z) {
                    return;
                }
                Log.w(SoundEffectsHelper.TAG, "onLoadSoundEffects(), Error while loading samples");
                SoundEffectsHelper.this.onUnloadSoundEffects();
            }
        });
        this.mSoundPoolLoader.addHandler(onEffectsLoadCompleteHandler);
        int i = 0;
        for (Resource resource : this.mResources) {
            String resourceFilePath = getResourceFilePath(resource);
            int load = this.mSoundPool.load(resourceFilePath, 0);
            if (load > 0) {
                resource.mSampleId = load;
                resource.mLoaded = false;
                i++;
            } else {
                logEvent("effect " + resourceFilePath + " rejected by SoundPool");
                StringBuilder sb = new StringBuilder();
                sb.append("SoundPool could not load file: ");
                sb.append(resourceFilePath);
                Log.w(TAG, sb.toString());
            }
        }
        if (i > 0) {
            sendMsg(3, 0, 0, null, 15000);
        } else {
            logEvent("effects loading completed, no effects to load");
            this.mSoundPoolLoader.onComplete(true);
        }
    }

    void onUnloadSoundEffects() {
        if (this.mSoundPool == null) {
            return;
        }
        SoundPoolLoader soundPoolLoader = this.mSoundPoolLoader;
        if (soundPoolLoader != null) {
            soundPoolLoader.addHandler(new OnEffectsLoadCompleteHandler() { // from class: com.android.server.audio.SoundEffectsHelper.2
                @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
                public void run(boolean z) {
                    SoundEffectsHelper.this.onUnloadSoundEffects();
                }
            });
        }
        logEvent("effects unloading started");
        for (Resource resource : this.mResources) {
            int i = resource.mSampleId;
            if (i != 0) {
                this.mSoundPool.unload(i);
                resource.unload();
            }
        }
        this.mSoundPool.release();
        this.mSoundPool = null;
        logEvent("effects unloading completed");
    }

    void onPlaySoundEffect(int i, int i2) {
        int i3;
        float pow = i2 < 0 ? (float) Math.pow(10.0d, this.mSfxAttenuationDb / 20.0f) : i2 / 1000.0f;
        if (i < 0 || i >= 16) {
            return;
        }
        Resource resource = this.mResources.get(this.mEffects[i]);
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null && (i3 = resource.mSampleId) != 0 && resource.mLoaded) {
            soundPool.play(i3, pow, pow, 0, 0, 1.0f);
            return;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getResourceFilePath(resource));
            mediaPlayer.setAudioStreamType(1);
            mediaPlayer.prepare();
            mediaPlayer.setVolume(pow);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.android.server.audio.SoundEffectsHelper.3
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mediaPlayer2) {
                    SoundEffectsHelper.cleanupPlayer(mediaPlayer2);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.android.server.audio.SoundEffectsHelper.4
                @Override // android.media.MediaPlayer.OnErrorListener
                public boolean onError(MediaPlayer mediaPlayer2, int i4, int i5) {
                    SoundEffectsHelper.cleanupPlayer(mediaPlayer2);
                    return true;
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            Log.w(TAG, "MediaPlayer IOException: " + e);
        } catch (IllegalArgumentException e2) {
            Log.w(TAG, "MediaPlayer IllegalArgumentException: " + e2);
        } catch (IllegalStateException e3) {
            Log.w(TAG, "MediaPlayer IllegalStateException: " + e3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cleanupPlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (IllegalStateException e) {
                Log.w(TAG, "MediaPlayer IllegalStateException: " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getResourceFilePath(Resource resource) {
        String str = Environment.getProductDirectory() + SOUND_EFFECTS_PATH + resource.mFileName;
        if (new File(str).isFile()) {
            return str;
        }
        return Environment.getRootDirectory() + SOUND_EFFECTS_PATH + resource.mFileName;
    }

    private void loadSoundAssetDefaults() {
        int size = this.mResources.size();
        this.mResources.add(new Resource("Effect_Tick.ogg"));
        Arrays.fill(this.mEffects, size);
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x0144, code lost:
    
        if (r1 == null) goto L67;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v20 */
    /* JADX WARN: Type inference failed for: r1v21 */
    /* JADX WARN: Type inference failed for: r1v22 */
    /* JADX WARN: Type inference failed for: r1v23 */
    /* JADX WARN: Type inference failed for: r1v24 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v8, types: [android.content.res.XmlResourceParser] */
    /* JADX WARN: Type inference failed for: r1v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void loadSoundAssets() {
        XmlResourceParser xmlResourceParser;
        XmlResourceParser xml;
        if (!this.mResources.isEmpty()) {
            return;
        }
        loadSoundAssetDefaults();
        ?? r1 = 0;
        boolean z = false;
        XmlResourceParser xmlResourceParser2 = null;
        XmlResourceParser xmlResourceParser3 = null;
        XmlResourceParser xmlResourceParser4 = null;
        try {
            try {
                xml = this.mContext.getResources().getXml(R.xml.audio_assets);
            } catch (Resources.NotFoundException e) {
                e = e;
            } catch (IOException e2) {
                e = e2;
            } catch (XmlPullParserException e3) {
                e = e3;
            }
            try {
                try {
                    XmlUtils.beginDocument(xml, TAG_AUDIO_ASSETS);
                    String attributeValue = xml.getAttributeValue(null, ATTR_VERSION);
                    HashMap hashMap = new HashMap();
                    if (ASSET_FILE_VERSION.equals(attributeValue)) {
                        while (true) {
                            XmlUtils.nextElement(xml);
                            String name = xml.getName();
                            if (name == null) {
                                break;
                            }
                            if (name.equals(TAG_GROUP)) {
                                String attributeValue2 = xml.getAttributeValue(null, ATTR_GROUP_NAME);
                                if (!GROUP_TOUCH_SOUNDS.equals(attributeValue2)) {
                                    Log.w(TAG, "Unsupported group name: " + attributeValue2);
                                }
                            } else {
                                if (!name.equals(TAG_ASSET)) {
                                    break;
                                }
                                String attributeValue3 = xml.getAttributeValue(null, ATTR_ASSET_ID);
                                String attributeValue4 = xml.getAttributeValue(null, ATTR_ASSET_FILE);
                                try {
                                    int i = AudioManager.class.getField(attributeValue3).getInt(null);
                                    int intValue = hashMap.getOrDefault(Integer.valueOf(i), 0).intValue() + 1;
                                    hashMap.put(Integer.valueOf(i), Integer.valueOf(intValue));
                                    if (intValue > 1) {
                                        Log.w(TAG, "Duplicate definition for sound ID: " + attributeValue3);
                                    }
                                    this.mEffects[i] = findOrAddResourceByFileName(attributeValue4);
                                } catch (Exception unused) {
                                    Log.w(TAG, "Invalid sound ID: " + attributeValue3);
                                }
                            }
                        }
                        z = allNavigationRepeatSoundsParsed(hashMap);
                        boolean z2 = hashMap.getOrDefault(11, 0).intValue() > 0;
                        if (z || z2) {
                            AudioManager audioManager = (AudioManager) this.mContext.getSystemService(AudioManager.class);
                            if (audioManager != null && z) {
                                audioManager.setNavigationRepeatSoundEffectsEnabled(true);
                            }
                            if (audioManager != null && z2) {
                                audioManager.setHomeSoundEffectEnabled(true);
                            }
                        }
                    }
                    xml.close();
                    r1 = z;
                } catch (Resources.NotFoundException e4) {
                    e = e4;
                    xmlResourceParser2 = xml;
                    Log.w(TAG, "audio assets file not found", e);
                    r1 = xmlResourceParser2;
                    xmlResourceParser = xmlResourceParser2;
                } catch (IOException e5) {
                    e = e5;
                    xmlResourceParser3 = xml;
                    Log.w(TAG, "I/O exception reading sound assets", e);
                    r1 = xmlResourceParser3;
                    if (xmlResourceParser3 != null) {
                        xmlResourceParser = xmlResourceParser3;
                        xmlResourceParser.close();
                        r1 = xmlResourceParser;
                    }
                } catch (XmlPullParserException e6) {
                    e = e6;
                    xmlResourceParser4 = xml;
                    Log.w(TAG, "XML parser exception reading sound assets", e);
                    r1 = xmlResourceParser4;
                    if (xmlResourceParser4 != null) {
                        xmlResourceParser = xmlResourceParser4;
                        xmlResourceParser.close();
                        r1 = xmlResourceParser;
                    }
                }
            } catch (Throwable th) {
                th = th;
                r1 = xml;
                if (r1 != 0) {
                    r1.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private boolean allNavigationRepeatSoundsParsed(Map<Integer, Integer> map) {
        return ((map.getOrDefault(12, 0).intValue() + map.getOrDefault(13, 0).intValue()) + map.getOrDefault(14, 0).intValue()) + map.getOrDefault(15, 0).intValue() == 4;
    }

    private int findOrAddResourceByFileName(String str) {
        for (int i = 0; i < this.mResources.size(); i++) {
            if (this.mResources.get(i).mFileName.equals(str)) {
                return i;
            }
        }
        int size = this.mResources.size();
        this.mResources.add(new Resource(str));
        return size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Resource findResourceBySampleId(int i) {
        for (Resource resource : this.mResources) {
            if (resource.mSampleId == i) {
                return resource;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SfxWorker extends Thread {
        SfxWorker() {
            super("AS.SfxWorker");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (SoundEffectsHelper.this) {
                SoundEffectsHelper.this.mSfxHandler = new SfxHandler();
                SoundEffectsHelper.this.notify();
            }
            Looper.loop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SfxHandler extends Handler {
        private SfxHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                SoundEffectsHelper.this.onLoadSoundEffects((OnEffectsLoadCompleteHandler) message.obj);
                return;
            }
            if (i == 1) {
                SoundEffectsHelper.this.onUnloadSoundEffects();
                return;
            }
            if (i == 2) {
                final int i2 = message.arg1;
                final int i3 = message.arg2;
                SoundEffectsHelper.this.onLoadSoundEffects(new OnEffectsLoadCompleteHandler() { // from class: com.android.server.audio.SoundEffectsHelper.SfxHandler.1
                    @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
                    public void run(boolean z) {
                        if (z) {
                            SoundEffectsHelper.this.onPlaySoundEffect(i2, i3);
                        }
                    }
                });
            } else if (i == 3 && SoundEffectsHelper.this.mSoundPoolLoader != null) {
                SoundEffectsHelper.this.mSoundPoolLoader.onTimeout();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SoundPoolLoader implements SoundPool.OnLoadCompleteListener {
        private List<OnEffectsLoadCompleteHandler> mLoadCompleteHandlers = new ArrayList();

        SoundPoolLoader() {
            SoundEffectsHelper.this.mSoundPool.setOnLoadCompleteListener(this);
        }

        void addHandler(OnEffectsLoadCompleteHandler onEffectsLoadCompleteHandler) {
            if (onEffectsLoadCompleteHandler != null) {
                this.mLoadCompleteHandlers.add(onEffectsLoadCompleteHandler);
            }
        }

        @Override // android.media.SoundPool.OnLoadCompleteListener
        public void onLoadComplete(SoundPool soundPool, int i, int i2) {
            int i3 = 0;
            if (i2 == 0) {
                for (Resource resource : SoundEffectsHelper.this.mResources) {
                    if (resource.mSampleId == i && !resource.mLoaded) {
                        SoundEffectsHelper.this.logEvent("effect " + resource.mFileName + " loaded");
                        resource.mLoaded = true;
                    }
                    if (resource.mSampleId != 0 && !resource.mLoaded) {
                        i3++;
                    }
                }
                if (i3 == 0) {
                    onComplete(true);
                    return;
                }
                return;
            }
            Resource findResourceBySampleId = SoundEffectsHelper.this.findResourceBySampleId(i);
            String resourceFilePath = findResourceBySampleId != null ? SoundEffectsHelper.this.getResourceFilePath(findResourceBySampleId) : "with unknown sample ID " + i;
            SoundEffectsHelper.this.logEvent("effect " + resourceFilePath + " loading failed, status " + i2);
            Log.w(SoundEffectsHelper.TAG, "onLoadSoundEffects(), Error " + i2 + " while loading sample " + resourceFilePath);
            onComplete(false);
        }

        void onTimeout() {
            onComplete(false);
        }

        void onComplete(boolean z) {
            if (SoundEffectsHelper.this.mSoundPool != null) {
                SoundEffectsHelper.this.mSoundPool.setOnLoadCompleteListener(null);
            }
            Iterator<OnEffectsLoadCompleteHandler> it = this.mLoadCompleteHandlers.iterator();
            while (it.hasNext()) {
                it.next().run(z);
            }
            SoundEffectsHelper soundEffectsHelper = SoundEffectsHelper.this;
            StringBuilder sb = new StringBuilder();
            sb.append("effects loading ");
            sb.append(z ? "completed" : "failed");
            soundEffectsHelper.logEvent(sb.toString());
        }
    }
}
