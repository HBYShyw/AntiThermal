package com.android.server.am;

import android.app.AppGlobals;
import android.app.ContentProviderHolder;
import android.app.IApplicationThread;
import android.content.AttributionSource;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.pm.UserInfo;
import android.content.pm.UserProperties;
import android.database.ContentObserver;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.TimeoutRecord;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.LocalManagerRegistry;
import com.android.server.LocalServices;
import com.android.server.OplusIoThread;
import com.android.server.RescueParty;
import com.android.server.am.ActivityManagerService;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.sdksandbox.SdkSandboxManagerLocal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ContentProviderHelper {
    private static final int[] PROCESS_STATE_STATS_FORMAT = {32, FrameworkStatsLog.PACKAGE_MANAGER_SNAPSHOT_REPORTED, 10272};
    private static final String TAG = "ContentProviderHelper";
    private final ProviderMap mProviderMap;
    private final ActivityManagerService mService;
    private boolean mSystemProvidersInstalled;
    private final ArrayList<ContentProviderRecord> mLaunchingProviders = new ArrayList<>();
    private boolean mIsKillOneTimes = false;
    public IContentProviderHelperExt mContentProviderHelperExt = (IContentProviderHelperExt) ExtLoader.type(IContentProviderHelperExt.class).create();
    private final long[] mProcessStateStatsLongs = new long[1];

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderHelper(ActivityManagerService activityManagerService, boolean z) {
        this.mService = activityManagerService;
        this.mProviderMap = z ? new ProviderMap(activityManagerService) : null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProviderMap getProviderMap() {
        return this.mProviderMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderHolder getContentProvider(IApplicationThread iApplicationThread, String str, String str2, int i, boolean z) {
        this.mService.enforceNotIsolatedCaller("getContentProvider");
        if (iApplicationThread == null) {
            String str3 = "null IApplicationThread when getting content provider " + str2;
            Slog.w(TAG, str3);
            throw new SecurityException(str3);
        }
        int callingUid = Binder.getCallingUid();
        if (str != null && this.mService.mAppOpsService.checkPackage(callingUid, str) != 0) {
            throw new SecurityException("Given calling package " + str + " does not match caller's uid " + callingUid);
        }
        return getContentProviderImpl(iApplicationThread, str2, null, callingUid, str, null, z, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderHolder getContentProviderExternal(String str, int i, IBinder iBinder, String str2) {
        this.mService.enforceCallingPermission("android.permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY", "Do not have permission in call getContentProviderExternal()");
        return getContentProviderExternalUnchecked(str, iBinder, Binder.getCallingUid(), str2 != null ? str2 : "*external*", this.mService.mUserController.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, 2, "getContentProvider", null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderHolder getContentProviderExternalUnchecked(String str, IBinder iBinder, int i, String str2, int i2) {
        return getContentProviderImpl(null, str, iBinder, i, null, str2, true, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f3, code lost:
    
        if (r47.mService.isValidSingletonCall(r5 == null ? r51 : r5.uid, r3.applicationInfo.uid) == false) goto L42;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0852  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0876 A[Catch: all -> 0x0adb, TRY_LEAVE, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0905  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0085 A[Catch: all -> 0x004d, TRY_ENTER, TryCatch #7 {all -> 0x004d, blocks: (B:465:0x0018, B:10:0x0067, B:20:0x0085, B:23:0x00c8, B:25:0x00d1, B:27:0x00e3, B:30:0x00eb, B:34:0x011f, B:36:0x0123, B:39:0x012c, B:41:0x0134, B:43:0x013c, B:47:0x0167, B:49:0x017e, B:50:0x0182, B:53:0x018e, B:58:0x01a6, B:450:0x00e9, B:451:0x00f5, B:453:0x0101, B:456:0x0109, B:468:0x0022, B:469:0x004c), top: B:464:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0364 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x03fb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:294:0x04aa A[Catch: all -> 0x0adb, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0528 A[Catch: all -> 0x0adb, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:320:0x054f A[Catch: all -> 0x0adb, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:327:0x059d A[Catch: all -> 0x0adb, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:333:0x05ab A[Catch: all -> 0x0adb, TRY_LEAVE, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:346:0x0614 A[Catch: all -> 0x0adb, DONT_GENERATE, TRY_ENTER, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x011f A[Catch: all -> 0x004d, TryCatch #7 {all -> 0x004d, blocks: (B:465:0x0018, B:10:0x0067, B:20:0x0085, B:23:0x00c8, B:25:0x00d1, B:27:0x00e3, B:30:0x00eb, B:34:0x011f, B:36:0x0123, B:39:0x012c, B:41:0x0134, B:43:0x013c, B:47:0x0167, B:49:0x017e, B:50:0x0182, B:53:0x018e, B:58:0x01a6, B:450:0x00e9, B:451:0x00f5, B:453:0x0101, B:456:0x0109, B:468:0x0022, B:469:0x004c), top: B:464:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:350:0x061e  */
    /* JADX WARN: Removed duplicated region for block: B:376:0x080e A[Catch: all -> 0x0adb, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0845 A[Catch: all -> 0x0adb, TRY_LEAVE, TryCatch #21 {all -> 0x0adb, blocks: (B:68:0x01d2, B:69:0x01fb, B:472:0x0ad6, B:75:0x020a, B:78:0x0210, B:80:0x0216, B:84:0x0222, B:99:0x0303, B:100:0x0306, B:105:0x0343, B:237:0x0364, B:240:0x0371, B:243:0x0380, B:247:0x0387, B:249:0x0397, B:252:0x03a0, B:257:0x03b4, B:260:0x03ca, B:262:0x03e6, B:265:0x03f2, B:266:0x03f9, B:267:0x03fa, B:279:0x0432, B:281:0x043d, B:282:0x0478, B:286:0x047f, B:288:0x049a, B:294:0x04aa, B:296:0x04ba, B:303:0x04f1, B:304:0x04f4, B:308:0x050f, B:309:0x0521, B:311:0x0528, B:313:0x052e, B:314:0x0541, B:318:0x054b, B:320:0x054f, B:322:0x055d, B:323:0x0565, B:325:0x0594, B:327:0x059d, B:329:0x05a6, B:333:0x05ab, B:346:0x0614, B:347:0x0617, B:373:0x07f5, B:374:0x0807, B:376:0x080e, B:377:0x0815, B:379:0x0845, B:108:0x085b, B:110:0x0876, B:118:0x08c6, B:128:0x08f5, B:129:0x08fc, B:136:0x0904, B:138:0x0908, B:383:0x06de, B:384:0x06e1, B:397:0x07a4, B:398:0x07a7, B:412:0x07fa, B:413:0x07fe, B:419:0x0515, B:420:0x0519, B:416:0x051a, B:426:0x0851, B:429:0x039e, B:438:0x034d, B:439:0x0351, B:112:0x0877, B:114:0x087b, B:116:0x087f, B:117:0x08c5, B:122:0x08ce, B:123:0x08d0, B:124:0x08e6, B:126:0x08ea, B:127:0x08f4, B:86:0x0247, B:88:0x0290, B:90:0x029a, B:93:0x02a3, B:95:0x02b7, B:97:0x02cf, B:104:0x030d, B:435:0x0317, B:269:0x03fb, B:271:0x03ff, B:273:0x0407, B:276:0x0413, B:277:0x0430, B:278:0x0431, B:335:0x05b3, B:337:0x05bd, B:339:0x05ca, B:342:0x05d8, B:344:0x060c, B:351:0x0620, B:353:0x0633, B:355:0x0639, B:357:0x063f, B:359:0x0643, B:360:0x0659, B:362:0x0663, B:364:0x066d, B:366:0x0671, B:367:0x068f, B:371:0x0694, B:372:0x07db, B:381:0x06c7, B:388:0x06e8, B:391:0x0711, B:393:0x071a, B:394:0x073a, B:396:0x0771, B:402:0x07b2, B:407:0x05e8, B:300:0x04bf, B:302:0x04d9, B:307:0x04fa), top: B:3:0x0012, inners: #0, #6, #9, #17, #22 }] */
    /* JADX WARN: Removed duplicated region for block: B:414:0x07ff  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:428:0x03b3  */
    /* JADX WARN: Removed duplicated region for block: B:444:0x0352  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x0195  */
    /* JADX WARN: Removed duplicated region for block: B:461:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0167 A[Catch: all -> 0x004d, TRY_ENTER, TryCatch #7 {all -> 0x004d, blocks: (B:465:0x0018, B:10:0x0067, B:20:0x0085, B:23:0x00c8, B:25:0x00d1, B:27:0x00e3, B:30:0x00eb, B:34:0x011f, B:36:0x0123, B:39:0x012c, B:41:0x0134, B:43:0x013c, B:47:0x0167, B:49:0x017e, B:50:0x0182, B:53:0x018e, B:58:0x01a6, B:450:0x00e9, B:451:0x00f5, B:453:0x0101, B:456:0x0109, B:468:0x0022, B:469:0x004c), top: B:464:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x018e A[Catch: all -> 0x004d, TRY_LEAVE, TryCatch #7 {all -> 0x004d, blocks: (B:465:0x0018, B:10:0x0067, B:20:0x0085, B:23:0x00c8, B:25:0x00d1, B:27:0x00e3, B:30:0x00eb, B:34:0x011f, B:36:0x0123, B:39:0x012c, B:41:0x0134, B:43:0x013c, B:47:0x0167, B:49:0x017e, B:50:0x0182, B:53:0x018e, B:58:0x01a6, B:450:0x00e9, B:451:0x00f5, B:453:0x0101, B:456:0x0109, B:468:0x0022, B:469:0x004c), top: B:464:0x0018 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x019c A[Catch: all -> 0x0ad2, TRY_ENTER, TRY_LEAVE, TryCatch #12 {all -> 0x0ad2, blocks: (B:5:0x0012, B:8:0x0054, B:13:0x0070, B:18:0x0081, B:45:0x0163, B:56:0x019c, B:64:0x01b1, B:66:0x01b7, B:462:0x007b), top: B:4:0x0012 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ContentProviderHolder getContentProviderImpl(IApplicationThread iApplicationThread, String str, IBinder iBinder, int i, String str2, String str3, boolean z, int i2) {
        ActivityManagerService activityManagerService;
        Throwable th;
        ProcessRecord processRecord;
        ContentProviderRecord providerByName;
        long j;
        ContentProviderRecord contentProviderRecord;
        int i3;
        ProviderInfo providerInfo;
        boolean z2;
        boolean z3;
        ProcessRecord processRecord2;
        String str4;
        ProcessRecord processRecord3;
        int i4;
        long j2;
        boolean z4;
        ProcessRecord processRecord4;
        ContentProviderConnection contentProviderConnection;
        int i5;
        boolean z5;
        boolean z6;
        boolean z7;
        ProcessRecord processRecord5;
        ContentProviderRecord contentProviderRecord2;
        int size;
        int i6;
        ComponentName componentName;
        ContentProviderRecord contentProviderRecord3;
        ProviderInfo providerInfo2;
        boolean z8;
        ProcessRecord processRecord6;
        boolean z9;
        long j3;
        ContentProviderRecord contentProviderRecord4;
        int i7;
        long clearCallingIdentity;
        ProcessRecord processRecord7;
        ProcessRecord processRecord8;
        IApplicationThread thread;
        ApplicationInfo applicationInfo;
        boolean z10;
        String str5;
        ProcessRecord processRecord9;
        long j4;
        boolean z11;
        ProviderInfo providerInfo3;
        boolean z12;
        ProcessRecord processRecord10;
        ActivityManagerService activityManagerService2 = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService2) {
            try {
            } catch (Throwable th2) {
                th = th2;
                th = th;
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
            try {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (iApplicationThread != null) {
                    try {
                        ProcessRecord recordForAppLOSP = this.mService.getRecordForAppLOSP(iApplicationThread);
                        if (recordForAppLOSP == null) {
                            throw new SecurityException("Unable to find app for caller " + iApplicationThread + " (pid=" + Binder.getCallingPid() + ") when getting content provider " + str);
                        }
                        processRecord = recordForAppLOSP;
                    } catch (Throwable th3) {
                        th = th3;
                        activityManagerService = activityManagerService2;
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } else {
                    processRecord = null;
                }
                checkTime(uptimeMillis, "getContentProviderImpl: getProviderByName");
                UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
                UserProperties userProperties = userManagerInternal.getUserProperties(i2);
                boolean z13 = userProperties != null && userProperties.isMediaSharedWithParent();
                if (ContentProvider.isAuthorityRedirectedForCloneProfile(str) && z13) {
                    providerByName = null;
                    if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                        j = uptimeMillis;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        j = uptimeMillis;
                        sb.append("getContentProviderImpl: from caller = ");
                        sb.append(iApplicationThread);
                        sb.append(" (pid = ");
                        sb.append(Binder.getCallingPid());
                        sb.append(", userId = ");
                        sb.append(i2);
                        sb.append(") to get content provider ");
                        sb.append(str);
                        sb.append(" cpr = ");
                        sb.append(providerByName);
                        Slog.d(TAG, sb.toString());
                    }
                    if (providerByName == null || i2 == 0 || (providerByName = this.mProviderMap.getProviderByName(str, 0)) == null) {
                        contentProviderRecord = providerByName;
                        i3 = i2;
                        providerInfo = null;
                    } else {
                        providerInfo = providerByName.info;
                        if (this.mService.isSingleton(providerInfo.processName, providerInfo.applicationInfo, providerInfo.name, providerInfo.flags)) {
                        }
                        if (!this.mContentProviderHelperExt.hookGetProviderAndInfo(providerByName, i2)) {
                            if (!ContentProvider.isAuthorityRedirectedForCloneProfile(str)) {
                                i3 = i2;
                                providerInfo = null;
                                z2 = true;
                                contentProviderRecord = null;
                            } else if (z13) {
                                i3 = userManagerInternal.getProfileParentId(i2);
                                contentProviderRecord = providerByName;
                                z2 = false;
                            } else {
                                contentProviderRecord = providerByName;
                                i3 = i2;
                            }
                            if (contentProviderRecord != null || (processRecord10 = contentProviderRecord.proc) == null) {
                                z3 = false;
                            } else {
                                boolean z14 = !processRecord10.isKilled();
                                if (contentProviderRecord.proc.isKilled() && contentProviderRecord.proc.isKilledByAm()) {
                                    Slog.w(TAG, contentProviderRecord.proc.toString() + " was killed by AM but isn't really dead");
                                    z3 = z14;
                                    processRecord2 = contentProviderRecord.proc;
                                    if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("providerRunning ");
                                        sb2.append(z3);
                                        sb2.append(" cpr.proc ");
                                        sb2.append(contentProviderRecord != null ? contentProviderRecord.proc : null);
                                        Slog.v("ActivityManager_MU", sb2.toString());
                                    }
                                    int curProcState = processRecord != null ? processRecord.mState.getCurProcState() : -1;
                                    if (z3) {
                                        ProviderInfo providerInfo4 = contentProviderRecord.info;
                                        if (this.mContentProviderHelperExt.hookHansProviderIfNeeded(providerInfo4, i, str2)) {
                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                            return null;
                                        }
                                        if (processRecord != null && contentProviderRecord.canRunHere(processRecord)) {
                                            ProcessRecord processRecord11 = processRecord;
                                            checkAssociationAndPermissionLocked(processRecord, providerInfo4, i, i3, z2, contentProviderRecord.name.flattenToShortString(), j);
                                            enforceContentProviderRestrictionsForSdkSandbox(providerInfo4);
                                            ContentProviderHolder newHolder = contentProviderRecord.newHolder(null, true);
                                            newHolder.provider = null;
                                            FrameworkStatsLog.write(FrameworkStatsLog.PROVIDER_ACQUISITION_EVENT_REPORTED, processRecord11.uid, i, 1, 1, providerInfo4.packageName, str2, curProcState, curProcState);
                                            this.mContentProviderHelperExt.handleReturnHolder(providerInfo4, str2, i, false);
                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                            return newHolder;
                                        }
                                        activityManagerService = activityManagerService2;
                                        long j5 = j;
                                        ProcessRecord processRecord12 = processRecord;
                                        try {
                                            try {
                                                if (AppGlobals.getPackageManager().resolveContentProvider(str, 0L, i3) == null) {
                                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                                    return null;
                                                }
                                            } catch (RemoteException unused) {
                                            }
                                        } catch (RemoteException unused2) {
                                        }
                                        boolean z15 = z3;
                                        checkAssociationAndPermissionLocked(processRecord12, providerInfo4, i, i3, z2, contentProviderRecord.name.flattenToShortString(), j5);
                                        int curProcState2 = contentProviderRecord.proc.mState.getCurProcState();
                                        clearCallingIdentity = Binder.clearCallingIdentity();
                                        try {
                                            checkTime(j5, "getContentProviderImpl: incProviderCountLocked");
                                            i4 = i3;
                                            ContentProviderConnection incProviderCountLocked = incProviderCountLocked(processRecord12, contentProviderRecord, iBinder, i, str2, str3, z, true, j5, this.mService.mProcessList, i2);
                                            j2 = j5;
                                            checkTime(j2, "getContentProviderImpl: before updateOomAdj");
                                            int verifiedAdj = contentProviderRecord.proc.mState.getVerifiedAdj();
                                            boolean updateOomAdjLocked = this.mService.updateOomAdjLocked(contentProviderRecord.proc, 7);
                                            if (updateOomAdjLocked && verifiedAdj != contentProviderRecord.proc.mState.getSetAdj() && !isProcessAliveLocked(contentProviderRecord.proc)) {
                                                updateOomAdjLocked = false;
                                            }
                                            str4 = str;
                                            maybeUpdateProviderUsageStatsLocked(processRecord12, contentProviderRecord.info.packageName, str4);
                                            checkTime(j2, "getContentProviderImpl: after updateOomAdj");
                                            if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                Slog.i(TAG, "Adjust success: " + updateOomAdjLocked);
                                            }
                                            if (updateOomAdjLocked) {
                                                ProcessStateRecord processStateRecord = contentProviderRecord.proc.mState;
                                                processStateRecord.setVerifiedAdj(processStateRecord.getSetAdj());
                                                providerInfo3 = providerInfo4;
                                                processRecord3 = processRecord12;
                                                FrameworkStatsLog.write(FrameworkStatsLog.PROVIDER_ACQUISITION_EVENT_REPORTED, contentProviderRecord.proc.uid, i, 1, 1, providerInfo4.packageName, str2, curProcState, curProcState2);
                                                z12 = z15;
                                                contentProviderConnection = incProviderCountLocked;
                                            } else {
                                                Slog.wtf(TAG, "Existing provider " + contentProviderRecord.name.flattenToShortString() + " is crashing; detaching " + processRecord12);
                                                if (!decProviderCountLocked(incProviderCountLocked, contentProviderRecord, iBinder, z, false, false)) {
                                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                                    return null;
                                                }
                                                processRecord2 = contentProviderRecord.proc;
                                                processRecord3 = processRecord12;
                                                providerInfo3 = providerInfo4;
                                                z12 = false;
                                                contentProviderConnection = null;
                                            }
                                            Binder.restoreCallingIdentity(clearCallingIdentity);
                                            z4 = z12;
                                            processRecord4 = processRecord2;
                                            providerInfo = providerInfo3;
                                        } finally {
                                        }
                                    } else {
                                        str4 = str;
                                        processRecord3 = processRecord;
                                        activityManagerService = activityManagerService2;
                                        i4 = i3;
                                        j2 = j;
                                        z4 = z3;
                                        processRecord4 = processRecord2;
                                        contentProviderConnection = null;
                                    }
                                    if (!z4) {
                                        try {
                                            checkTime(j2, "getContentProviderImpl: before resolveContentProvider");
                                            i5 = i4;
                                            try {
                                                providerInfo = AppGlobals.getPackageManager().resolveContentProvider(str4, 3072L, i5);
                                                checkTime(j2, "getContentProviderImpl: after resolveContentProvider");
                                            } catch (RemoteException unused3) {
                                            }
                                        } catch (RemoteException unused4) {
                                            i5 = i4;
                                        }
                                        ProviderInfo providerInfo5 = providerInfo;
                                        if (providerInfo5 == null) {
                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                            return null;
                                        }
                                        if (this.mService.isSingleton(providerInfo5.processName, providerInfo5.applicationInfo, providerInfo5.name, providerInfo5.flags)) {
                                            if (this.mService.isValidSingletonCall(processRecord3 == null ? i : processRecord3.uid, providerInfo5.applicationInfo.uid)) {
                                                z5 = true;
                                                int i8 = !z5 ? 0 : i5;
                                                providerInfo5.applicationInfo = this.mService.getAppInfoForUser(providerInfo5.applicationInfo, i8);
                                                checkTime(j2, "getContentProviderImpl: got app info for user");
                                                ProcessRecord processRecord13 = processRecord3;
                                                int i9 = i8;
                                                ProcessRecord processRecord14 = processRecord4;
                                                z6 = z4;
                                                checkAssociationAndPermissionLocked(processRecord3, providerInfo5, i, i8, z5, str, j2);
                                                if (!this.mService.mProcessesReady && !providerInfo5.processName.equals("system")) {
                                                    throw new IllegalArgumentException("Attempt to launch content provider before system ready");
                                                }
                                                synchronized (this) {
                                                    if (!this.mSystemProvidersInstalled && providerInfo5.applicationInfo.isSystemApp() && "system".equals(providerInfo5.processName)) {
                                                        throw new IllegalStateException("Cannot access system provider: '" + providerInfo5.authority + "' before system providers are installed!");
                                                    }
                                                }
                                                if (!this.mService.mUserController.isUserRunning(i9, 0)) {
                                                    Slog.w(TAG, "Unable to launch app " + providerInfo5.applicationInfo.packageName + "/" + providerInfo5.applicationInfo.uid + " for provider " + str4 + ": user " + i9 + " is stopped");
                                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                                    return null;
                                                }
                                                ComponentName componentName2 = new ComponentName(providerInfo5.packageName, providerInfo5.name);
                                                checkTime(j2, "getContentProviderImpl: before getProviderByClass");
                                                ContentProviderRecord providerByClass = this.mProviderMap.getProviderByClass(componentName2, i9);
                                                checkTime(j2, "getContentProviderImpl: after getProviderByClass");
                                                if (providerByClass != null && (processRecord14 != providerByClass.proc || processRecord14 == null)) {
                                                    z7 = false;
                                                    if (z7) {
                                                        processRecord5 = processRecord13;
                                                    } else {
                                                        clearCallingIdentity = Binder.clearCallingIdentity();
                                                        processRecord5 = processRecord13;
                                                        if (!requestTargetProviderPermissionsReviewIfNeededLocked(providerInfo5, processRecord5, i9, this.mService.mContext)) {
                                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                                            return null;
                                                        }
                                                        try {
                                                            checkTime(j2, "getContentProviderImpl: before getApplicationInfo");
                                                            applicationInfo = AppGlobals.getPackageManager().getApplicationInfo(providerInfo5.applicationInfo.packageName, 1024L, i9);
                                                            checkTime(j2, "getContentProviderImpl: after getApplicationInfo");
                                                        } catch (RemoteException unused5) {
                                                            Binder.restoreCallingIdentity(clearCallingIdentity);
                                                        } catch (Throwable th4) {
                                                            throw th4;
                                                        }
                                                        if (applicationInfo == null) {
                                                            Slog.w(TAG, "No package info for content provider " + providerInfo5.name);
                                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                                            return null;
                                                        }
                                                        ContentProviderRecord contentProviderRecord5 = new ContentProviderRecord(this.mService, providerInfo5, this.mService.getAppInfoForUser(applicationInfo, i9), componentName2, z5);
                                                        Binder.restoreCallingIdentity(clearCallingIdentity);
                                                        contentProviderRecord2 = contentProviderRecord5;
                                                        checkTime(j2, "getContentProviderImpl: now have ContentProviderRecord");
                                                        if (processRecord5 == null && contentProviderRecord2.canRunHere(processRecord5)) {
                                                            enforceContentProviderRestrictionsForSdkSandbox(providerInfo5);
                                                            this.mContentProviderHelperExt.handleReturnHolder(providerInfo5, str2, i, false);
                                                            ContentProviderHolder newHolder2 = contentProviderRecord2.newHolder(null, true);
                                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                                            return newHolder2;
                                                        }
                                                        if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                            StringBuilder sb3 = new StringBuilder();
                                                            sb3.append("LAUNCHING REMOTE PROVIDER (myuid ");
                                                            sb3.append(processRecord5 != null ? Integer.valueOf(processRecord5.uid) : null);
                                                            sb3.append(" pruid ");
                                                            sb3.append(contentProviderRecord2.appInfo.uid);
                                                            sb3.append("): ");
                                                            sb3.append(contentProviderRecord2.info.name);
                                                            sb3.append(" callers=");
                                                            sb3.append(Debug.getCallers(6));
                                                            Slog.w(TAG, sb3.toString());
                                                        }
                                                        size = this.mLaunchingProviders.size();
                                                        i6 = 0;
                                                        while (i6 < size && this.mLaunchingProviders.get(i6) != contentProviderRecord2) {
                                                            i6++;
                                                        }
                                                        if (i6 >= size) {
                                                            int callingPid = Binder.getCallingPid();
                                                            clearCallingIdentity = Binder.clearCallingIdentity();
                                                            try {
                                                                if (!TextUtils.equals(contentProviderRecord2.appInfo.packageName, str2)) {
                                                                    this.mService.mUsageStatsService.reportEvent(contentProviderRecord2.appInfo.packageName, i9, 31);
                                                                }
                                                                try {
                                                                    checkTime(j2, "getContentProviderImpl: before set stopped state");
                                                                } catch (IllegalArgumentException e) {
                                                                    e = e;
                                                                }
                                                                try {
                                                                    this.mService.mPackageManagerInt.setPackageStoppedState(contentProviderRecord2.appInfo.packageName, false, i9);
                                                                    checkTime(j2, "getContentProviderImpl: after set stopped state");
                                                                    processRecord7 = processRecord5;
                                                                } catch (IllegalArgumentException e2) {
                                                                    e = e2;
                                                                    IllegalArgumentException illegalArgumentException = e;
                                                                    StringBuilder sb4 = new StringBuilder();
                                                                    processRecord7 = processRecord5;
                                                                    sb4.append("Failed trying to unstop package ");
                                                                    sb4.append(contentProviderRecord2.appInfo.packageName);
                                                                    sb4.append(": ");
                                                                    sb4.append(illegalArgumentException);
                                                                    Slog.w(TAG, sb4.toString());
                                                                    if (!this.mContentProviderHelperExt.hookHansProviderIfNeeded(providerInfo5, i, str2)) {
                                                                    }
                                                                }
                                                                if (!this.mContentProviderHelperExt.hookHansProviderIfNeeded(providerInfo5, i, str2)) {
                                                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                                                    return null;
                                                                }
                                                                checkTime(j2, "getContentProviderImpl: looking for process record");
                                                                ProcessRecord processRecordLocked = this.mService.getProcessRecordLocked(providerInfo5.processName, contentProviderRecord2.appInfo.uid);
                                                                if (processRecordLocked == null || (thread = processRecordLocked.getThread()) == null || processRecordLocked.isKilled()) {
                                                                    componentName = componentName2;
                                                                    ContentProviderRecord contentProviderRecord6 = contentProviderRecord2;
                                                                    ProcessRecord processRecord15 = processRecord7;
                                                                    if (this.mContentProviderHelperExt.hookPreloadProviderBlock(callingPid, providerInfo5, i, str2, processRecord15, contentProviderRecord6)) {
                                                                        ActivityManagerService.resetPriorityAfterLockedSection();
                                                                        return null;
                                                                    }
                                                                    this.mContentProviderHelperExt.hookComsumeTokenIfNeeded(this.mService, contentProviderRecord6, providerInfo5, i, str2);
                                                                    this.mContentProviderHelperExt.handleReturnHolder(providerInfo5, str2, i, true);
                                                                    int i10 = (contentProviderRecord6.appInfo.flags & AudioDevice.OUT_AUX_LINE) != 0 ? 2 : 1;
                                                                    checkTime(j2, "getContentProviderImpl: before start process");
                                                                    if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                                        Slog.d(TAG, "Start process " + providerInfo5.processName + " for " + str4);
                                                                    }
                                                                    ProcessRecord startProcessLocked = this.mService.startProcessLocked(providerInfo5.processName, contentProviderRecord6.appInfo, false, 0, new HostingRecord(HostingRecord.HOSTING_TYPE_CONTENT_PROVIDER, new ComponentName(providerInfo5.applicationInfo.packageName, providerInfo5.name)), 0, false, false);
                                                                    checkTime(j2, "getContentProviderImpl: after start process");
                                                                    if (startProcessLocked == null) {
                                                                        Slog.w(TAG, "Unable to launch app " + providerInfo5.applicationInfo.packageName + "/" + providerInfo5.applicationInfo.uid + " for provider " + str4 + ": process is bad");
                                                                        ActivityManagerService.resetPriorityAfterLockedSection();
                                                                        return null;
                                                                    }
                                                                    providerInfo2 = providerInfo5;
                                                                    FrameworkStatsLog.write(FrameworkStatsLog.PROVIDER_ACQUISITION_EVENT_REPORTED, startProcessLocked.uid, i, 3, i10, providerInfo5.packageName, str2, curProcState, 20);
                                                                    processRecord6 = processRecord15;
                                                                    contentProviderRecord3 = contentProviderRecord6;
                                                                    this.mContentProviderHelperExt.hookGetContentProviderImplAfterStartProc(processRecord6, contentProviderRecord3);
                                                                    processRecord8 = startProcessLocked;
                                                                } else {
                                                                    if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                                        Slog.d(TAG, "Installing in existing process " + processRecordLocked);
                                                                    }
                                                                    ProcessProviderRecord processProviderRecord = processRecordLocked.mProviders;
                                                                    if (!processProviderRecord.hasProvider(providerInfo5.name)) {
                                                                        checkTime(j2, "getContentProviderImpl: scheduling install");
                                                                        processProviderRecord.installProvider(providerInfo5.name, contentProviderRecord2);
                                                                        try {
                                                                            if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                                                Slog.d(TAG, "Installing provider " + processRecordLocked + " cpr " + contentProviderRecord2);
                                                                            }
                                                                            thread.scheduleInstallProvider(providerInfo5);
                                                                        } catch (RemoteException unused6) {
                                                                        }
                                                                    }
                                                                    componentName = componentName2;
                                                                    FrameworkStatsLog.write(FrameworkStatsLog.PROVIDER_ACQUISITION_EVENT_REPORTED, processRecordLocked.uid, i, 1, 1, providerInfo5.packageName, str2, curProcState, processRecordLocked.mState.getCurProcState());
                                                                    contentProviderRecord3 = contentProviderRecord2;
                                                                    processRecord8 = processRecordLocked;
                                                                    processRecord6 = processRecord7;
                                                                    providerInfo2 = providerInfo5;
                                                                }
                                                                contentProviderRecord3.launchingApp = processRecord8;
                                                                this.mLaunchingProviders.add(contentProviderRecord3);
                                                                z8 = true;
                                                                contentProviderRecord3.launchingApp.getWrapper().getExtImpl().updateExecutingComponent(contentProviderRecord3.launchingApp, "provider", 1);
                                                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                                            } finally {
                                                            }
                                                        } else {
                                                            componentName = componentName2;
                                                            contentProviderRecord3 = contentProviderRecord2;
                                                            providerInfo2 = providerInfo5;
                                                            z8 = true;
                                                            processRecord6 = processRecord5;
                                                        }
                                                        checkTime(j2, "getContentProviderImpl: updating data structures");
                                                        if (z7) {
                                                            this.mProviderMap.putProviderByClass(componentName, contentProviderRecord3);
                                                        }
                                                        this.mProviderMap.putProviderByName(str4, contentProviderRecord3);
                                                        ContentProviderRecord contentProviderRecord7 = contentProviderRecord3;
                                                        z9 = z8;
                                                        j3 = j2;
                                                        contentProviderConnection = incProviderCountLocked(processRecord6, contentProviderRecord3, iBinder, i, str2, str3, z, false, j2, this.mService.mProcessList, i2);
                                                        if (contentProviderConnection != null) {
                                                            contentProviderConnection.waiting = z9;
                                                        }
                                                        contentProviderRecord4 = contentProviderRecord7;
                                                        providerInfo = providerInfo2;
                                                        i7 = i9;
                                                    }
                                                    contentProviderRecord2 = providerByClass;
                                                    checkTime(j2, "getContentProviderImpl: now have ContentProviderRecord");
                                                    if (processRecord5 == null) {
                                                    }
                                                    if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                    }
                                                    size = this.mLaunchingProviders.size();
                                                    i6 = 0;
                                                    while (i6 < size) {
                                                        i6++;
                                                    }
                                                    if (i6 >= size) {
                                                    }
                                                    checkTime(j2, "getContentProviderImpl: updating data structures");
                                                    if (z7) {
                                                    }
                                                    this.mProviderMap.putProviderByName(str4, contentProviderRecord3);
                                                    ContentProviderRecord contentProviderRecord72 = contentProviderRecord3;
                                                    z9 = z8;
                                                    j3 = j2;
                                                    contentProviderConnection = incProviderCountLocked(processRecord6, contentProviderRecord3, iBinder, i, str2, str3, z, false, j2, this.mService.mProcessList, i2);
                                                    if (contentProviderConnection != null) {
                                                    }
                                                    contentProviderRecord4 = contentProviderRecord72;
                                                    providerInfo = providerInfo2;
                                                    i7 = i9;
                                                }
                                                z7 = true;
                                                if (z7) {
                                                }
                                                contentProviderRecord2 = providerByClass;
                                                checkTime(j2, "getContentProviderImpl: now have ContentProviderRecord");
                                                if (processRecord5 == null) {
                                                }
                                                if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                                }
                                                size = this.mLaunchingProviders.size();
                                                i6 = 0;
                                                while (i6 < size) {
                                                }
                                                if (i6 >= size) {
                                                }
                                                checkTime(j2, "getContentProviderImpl: updating data structures");
                                                if (z7) {
                                                }
                                                this.mProviderMap.putProviderByName(str4, contentProviderRecord3);
                                                ContentProviderRecord contentProviderRecord722 = contentProviderRecord3;
                                                z9 = z8;
                                                j3 = j2;
                                                contentProviderConnection = incProviderCountLocked(processRecord6, contentProviderRecord3, iBinder, i, str2, str3, z, false, j2, this.mService.mProcessList, i2);
                                                if (contentProviderConnection != null) {
                                                }
                                                contentProviderRecord4 = contentProviderRecord722;
                                                providerInfo = providerInfo2;
                                                i7 = i9;
                                            }
                                        }
                                        z5 = false;
                                        if (!z5) {
                                        }
                                        providerInfo5.applicationInfo = this.mService.getAppInfoForUser(providerInfo5.applicationInfo, i8);
                                        checkTime(j2, "getContentProviderImpl: got app info for user");
                                        ProcessRecord processRecord132 = processRecord3;
                                        int i92 = i8;
                                        ProcessRecord processRecord142 = processRecord4;
                                        z6 = z4;
                                        checkAssociationAndPermissionLocked(processRecord3, providerInfo5, i, i8, z5, str, j2);
                                        if (!this.mService.mProcessesReady) {
                                            throw new IllegalArgumentException("Attempt to launch content provider before system ready");
                                        }
                                        synchronized (this) {
                                        }
                                        Slog.wtf(TAG, "Timeout waiting for provider " + providerInfo.applicationInfo.packageName + "/" + providerInfo.applicationInfo.uid + " for provider " + str + " providerRunning=" + z6 + " caller=" + str5 + "/" + Binder.getCallingUid());
                                        return null;
                                    }
                                    z6 = z4;
                                    j3 = j2;
                                    contentProviderRecord4 = contentProviderRecord;
                                    z9 = true;
                                    i7 = i4;
                                    checkTime(j3, "getContentProviderImpl: done!");
                                    this.mService.grantImplicitAccess(i7, null, i, UserHandle.getAppId(providerInfo.applicationInfo.uid));
                                    if (iApplicationThread != null) {
                                        synchronized (contentProviderRecord4) {
                                            if (contentProviderRecord4.provider == null) {
                                                if (contentProviderRecord4.launchingApp == null) {
                                                    Slog.w(TAG, "Unable to launch app " + providerInfo.applicationInfo.packageName + "/" + providerInfo.applicationInfo.uid + " for provider " + str + ": launching app became null");
                                                    int userId = UserHandle.getUserId(providerInfo.applicationInfo.uid);
                                                    ApplicationInfo applicationInfo2 = providerInfo.applicationInfo;
                                                    EventLogTags.writeAmProviderLostProcess(userId, applicationInfo2.packageName, applicationInfo2.uid, str);
                                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                                    return null;
                                                }
                                                if (contentProviderConnection != null) {
                                                    contentProviderConnection.waiting = z9;
                                                }
                                                Message obtainMessage = this.mService.mHandler.obtainMessage(73);
                                                obtainMessage.obj = contentProviderRecord4;
                                                this.mService.mHandler.sendMessageDelayed(obtainMessage, ContentResolver.CONTENT_PROVIDER_READY_TIMEOUT_MILLIS);
                                            }
                                            if (contentProviderRecord4.provider != null) {
                                                z11 = false;
                                                this.mContentProviderHelperExt.handleReturnHolder(providerInfo, str2, i, false);
                                            } else {
                                                z11 = false;
                                            }
                                            enforceContentProviderRestrictionsForSdkSandbox(providerInfo);
                                            ContentProviderHolder newHolder3 = contentProviderRecord4.newHolder(contentProviderConnection, z11);
                                            ActivityManagerService.resetPriorityAfterLockedSection();
                                            return newHolder3;
                                        }
                                    }
                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                    long uptimeMillis2 = SystemClock.uptimeMillis() + ContentResolver.CONTENT_PROVIDER_READY_TIMEOUT_MILLIS;
                                    synchronized (contentProviderRecord4) {
                                        if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                                            Slog.v("ActivityManager_MU", "cpr.provider " + contentProviderRecord4.provider + " launchingApp= " + contentProviderRecord4.launchingApp);
                                        }
                                        while (true) {
                                            if (contentProviderRecord4.provider != null) {
                                                z10 = false;
                                                break;
                                            }
                                            if (contentProviderRecord4.launchingApp == null) {
                                                Slog.w(TAG, "Unable to launch app " + providerInfo.applicationInfo.packageName + "/" + providerInfo.applicationInfo.uid + " for provider " + str + ": launching app became null");
                                                int userId2 = UserHandle.getUserId(providerInfo.applicationInfo.uid);
                                                ApplicationInfo applicationInfo3 = providerInfo.applicationInfo;
                                                EventLogTags.writeAmProviderLostProcess(userId2, applicationInfo3.packageName, applicationInfo3.uid, str);
                                                return null;
                                            }
                                            try {
                                                try {
                                                    j4 = uptimeMillis2;
                                                    try {
                                                        long max = Math.max(0L, uptimeMillis2 - SystemClock.uptimeMillis());
                                                        if (ActivityManagerDebugConfig.DEBUG_MU) {
                                                            Slog.v("ActivityManager_MU", "Waiting to start provider " + contentProviderRecord4 + " launchingApp=" + contentProviderRecord4.launchingApp + " for " + max + " ms");
                                                        }
                                                        if (contentProviderConnection != null) {
                                                            contentProviderConnection.waiting = z9;
                                                        }
                                                        contentProviderRecord4.wait(max);
                                                    } catch (InterruptedException unused7) {
                                                        if (contentProviderConnection != null) {
                                                            contentProviderConnection.waiting = false;
                                                        }
                                                        uptimeMillis2 = j4;
                                                    }
                                                } catch (InterruptedException unused8) {
                                                    j4 = uptimeMillis2;
                                                }
                                                if (contentProviderRecord4.provider == null) {
                                                    if (contentProviderConnection != null) {
                                                        contentProviderConnection.waiting = false;
                                                    }
                                                    z10 = z9;
                                                } else {
                                                    if (contentProviderConnection != null) {
                                                        contentProviderConnection.waiting = false;
                                                    }
                                                    uptimeMillis2 = j4;
                                                }
                                            } finally {
                                            }
                                        }
                                        if (!z10) {
                                            enforceContentProviderRestrictionsForSdkSandbox(providerInfo);
                                            return contentProviderRecord4.newHolder(contentProviderConnection, false);
                                        }
                                        str5 = "unknown";
                                        if (iApplicationThread != null) {
                                            ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
                                            ActivityManagerService.boostPriorityForProcLockedSection();
                                            synchronized (activityManagerGlobalLock) {
                                                try {
                                                    ProcessRecord lRURecordForAppLOSP = this.mService.mProcessList.getLRURecordForAppLOSP(iApplicationThread);
                                                    str5 = lRURecordForAppLOSP != null ? lRURecordForAppLOSP.processName : "unknown";
                                                } catch (Throwable th5) {
                                                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                                                    throw th5;
                                                }
                                            }
                                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                                            if (this.mIsKillOneTimes || (processRecord9 = contentProviderRecord4.launchingApp) == null || !"android.process.acore".equals(processRecord9.processName)) {
                                                Slog.v("ActivityManager_MU", "cleanupAppInLaunchingProvidersLocked " + contentProviderRecord4 + " cpr.launchingApp " + contentProviderRecord4.launchingApp);
                                                synchronized (this) {
                                                    cleanupAppInLaunchingProvidersLocked(contentProviderRecord4.launchingApp, z9);
                                                }
                                            } else {
                                                Slog.w("ActivityManager_MU", "force stop com.android.providers.contacts");
                                                clearCallingIdentity = Binder.clearCallingIdentity();
                                                this.mService.forceStopPackage("com.android.providers.contacts", i7);
                                                this.mIsKillOneTimes = z9;
                                            }
                                        }
                                        Slog.wtf(TAG, "Timeout waiting for provider " + providerInfo.applicationInfo.packageName + "/" + providerInfo.applicationInfo.uid + " for provider " + str + " providerRunning=" + z6 + " caller=" + str5 + "/" + Binder.getCallingUid());
                                        return null;
                                    }
                                }
                                z3 = z14;
                            }
                            processRecord2 = null;
                            if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                            }
                            int curProcState3 = processRecord != null ? processRecord.mState.getCurProcState() : -1;
                            if (z3) {
                            }
                            if (!z4) {
                            }
                            checkTime(j3, "getContentProviderImpl: done!");
                            this.mService.grantImplicitAccess(i7, null, i, UserHandle.getAppId(providerInfo.applicationInfo.uid));
                            if (iApplicationThread != null) {
                            }
                        }
                        contentProviderRecord = providerByName;
                        z2 = false;
                        i3 = 0;
                        if (contentProviderRecord != null) {
                        }
                        z3 = false;
                        processRecord2 = null;
                        if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                        }
                        int curProcState32 = processRecord != null ? processRecord.mState.getCurProcState() : -1;
                        if (z3) {
                        }
                        if (!z4) {
                        }
                        checkTime(j3, "getContentProviderImpl: done!");
                        this.mService.grantImplicitAccess(i7, null, i, UserHandle.getAppId(providerInfo.applicationInfo.uid));
                        if (iApplicationThread != null) {
                        }
                    }
                    z2 = true;
                    if (contentProviderRecord != null) {
                    }
                    z3 = false;
                    processRecord2 = null;
                    if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                    }
                    int curProcState322 = processRecord != null ? processRecord.mState.getCurProcState() : -1;
                    if (z3) {
                    }
                    if (!z4) {
                    }
                    checkTime(j3, "getContentProviderImpl: done!");
                    this.mService.grantImplicitAccess(i7, null, i, UserHandle.getAppId(providerInfo.applicationInfo.uid));
                    if (iApplicationThread != null) {
                    }
                }
                providerByName = this.mProviderMap.getProviderByName(str, i2);
                if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                }
                if (providerByName == null) {
                }
                contentProviderRecord = providerByName;
                i3 = i2;
                providerInfo = null;
                z2 = true;
                if (contentProviderRecord != null) {
                }
                z3 = false;
                processRecord2 = null;
                if (ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                }
                int curProcState3222 = processRecord != null ? processRecord.mState.getCurProcState() : -1;
                if (z3) {
                }
                if (!z4) {
                }
                checkTime(j3, "getContentProviderImpl: done!");
                this.mService.grantImplicitAccess(i7, null, i, UserHandle.getAppId(providerInfo.applicationInfo.uid));
                if (iApplicationThread != null) {
                }
            } catch (Throwable th6) {
                th = th6;
                activityManagerService = activityManagerService2;
                th = th;
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void checkAssociationAndPermissionLocked(ProcessRecord processRecord, ProviderInfo providerInfo, int i, int i2, boolean z, String str, long j) {
        String checkContentProviderAssociation = checkContentProviderAssociation(processRecord, i, providerInfo);
        if (checkContentProviderAssociation != null) {
            throw new SecurityException("Content provider lookup " + str + " failed: association not allowed with package " + checkContentProviderAssociation);
        }
        checkTime(j, "getContentProviderImpl: before checkContentProviderPermission");
        String checkContentProviderPermission = checkContentProviderPermission(providerInfo, Binder.getCallingPid(), Binder.getCallingUid(), i2, z, processRecord != null ? processRecord.toString() : null);
        if (checkContentProviderPermission != null) {
            throw new SecurityException(checkContentProviderPermission);
        }
        checkTime(j, "getContentProviderImpl: after checkContentProviderPermission");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void publishContentProviders(IApplicationThread iApplicationThread, List<ContentProviderHolder> list) {
        ProviderInfo providerInfo;
        ProviderInfo providerInfo2;
        ContentProviderRecord provider;
        int i;
        if (list == null) {
            return;
        }
        this.mService.enforceNotIsolatedOrSdkSandboxCaller("publishContentProviders");
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                ProcessRecord recordForAppLOSP = this.mService.getRecordForAppLOSP(iApplicationThread);
                if (ActivityManagerDebugConfig.DEBUG_MU && recordForAppLOSP != null) {
                    Slog.v("ActivityManager_MU", "ProcessRecord uid = " + recordForAppLOSP.uid);
                }
                if (recordForAppLOSP == null) {
                    throw new SecurityException("Unable to find app for caller " + iApplicationThread + " (pid=" + Binder.getCallingPid() + ") when publishing content providers");
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                int size = list.size();
                boolean z = false;
                for (int i2 = 0; i2 < size; i2++) {
                    ContentProviderHolder contentProviderHolder = list.get(i2);
                    if (contentProviderHolder != null && (providerInfo2 = contentProviderHolder.info) != null && contentProviderHolder.provider != null && (provider = recordForAppLOSP.mProviders.getProvider(providerInfo2.name)) != null) {
                        if (ActivityManagerDebugConfig.DEBUG_MU) {
                            Slog.v("ActivityManager_MU", "ContentProviderRecord uid = " + provider.uid);
                        }
                        ProviderInfo providerInfo3 = provider.info;
                        this.mProviderMap.putProviderByClass(new ComponentName(providerInfo3.packageName, providerInfo3.name), provider);
                        for (String str : provider.info.authority.split(";")) {
                            this.mProviderMap.putProviderByName(str, provider);
                        }
                        int size2 = this.mLaunchingProviders.size();
                        int i3 = 0;
                        boolean z2 = false;
                        while (i3 < size2) {
                            if (this.mLaunchingProviders.get(i3) == provider) {
                                if (this.mLaunchingProviders.get(i3).launchingApp != null && this.mLaunchingProviders.get(i3).launchingApp.getWrapper() != null) {
                                    this.mLaunchingProviders.get(i3).launchingApp.getWrapper().getExtImpl().updateExecutingComponent(this.mLaunchingProviders.get(i3).launchingApp, "provider", 2);
                                }
                                this.mLaunchingProviders.remove(i3);
                                i3--;
                                size2--;
                                i = 1;
                                z2 = true;
                            } else {
                                i = 1;
                            }
                            i3 += i;
                        }
                        if (z2) {
                            this.mService.mHandler.removeMessages(73, provider);
                            this.mService.mHandler.removeMessages(57, recordForAppLOSP);
                        }
                        ApplicationInfo applicationInfo = provider.info.applicationInfo;
                        recordForAppLOSP.addPackage(applicationInfo.packageName, applicationInfo.longVersionCode, this.mService.mProcessStats);
                        synchronized (provider) {
                            provider.provider = contentProviderHolder.provider;
                            provider.setProcess(recordForAppLOSP);
                            provider.notifyAll();
                            provider.onProviderPublishStatusLocked(true);
                        }
                        provider.mRestartCount = 0;
                        if (hasProviderConnectionLocked(recordForAppLOSP)) {
                            recordForAppLOSP.mProfile.addHostingComponentType(64);
                        }
                        z = true;
                    }
                }
                if (z) {
                    this.mService.updateOomAdjLocked(recordForAppLOSP, 7);
                    int size3 = list.size();
                    for (int i4 = 0; i4 < size3; i4++) {
                        ContentProviderHolder contentProviderHolder2 = list.get(i4);
                        if (contentProviderHolder2 != null && (providerInfo = contentProviderHolder2.info) != null && contentProviderHolder2.provider != null) {
                            maybeUpdateProviderUsageStatsLocked(recordForAppLOSP, providerInfo.packageName, providerInfo.authority);
                        }
                    }
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeContentProvider(IBinder iBinder, boolean z) {
        ProviderInfo providerInfo;
        this.mService.enforceNotIsolatedCaller("removeContentProvider");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                ContentProviderConnection contentProviderConnection = (ContentProviderConnection) iBinder;
                if (contentProviderConnection == null) {
                    throw new NullPointerException("connection is null");
                }
                ContentProviderRecord contentProviderRecord = contentProviderConnection.provider;
                ActivityManagerService.traceBegin(64L, "removeContentProvider: ", (contentProviderRecord == null || (providerInfo = contentProviderRecord.info) == null) ? "" : providerInfo.authority);
                try {
                    ActivityManagerService activityManagerService = this.mService;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            decProviderCountLocked(contentProviderConnection, null, null, z, true, true);
                        } catch (Throwable th) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                } finally {
                    Trace.traceEnd(64L);
                }
            } catch (ClassCastException unused) {
                String str = "removeContentProvider: " + iBinder + " not a ContentProviderConnection";
                Slog.w(TAG, str);
                throw new IllegalArgumentException(str);
            }
        } catch (Throwable th2) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeContentProviderExternalAsUser(String str, IBinder iBinder, int i) {
        this.mService.enforceCallingPermission("android.permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY", "Do not have permission in call removeContentProviderExternal()");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            removeContentProviderExternalUnchecked(str, iBinder, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeContentProviderExternalUnchecked(String str, IBinder iBinder, int i) {
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                ContentProviderRecord providerByName = this.mProviderMap.getProviderByName(str, i);
                if (providerByName == null) {
                    if (ActivityManagerDebugConfig.DEBUG_ALL) {
                        Slog.v(TAG, str + " content provider not found in providers list");
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                ProviderInfo providerInfo = providerByName.info;
                ContentProviderRecord providerByClass = this.mProviderMap.getProviderByClass(new ComponentName(providerInfo.packageName, providerInfo.name), i);
                if (providerByClass.hasExternalProcessHandles()) {
                    if (providerByClass.removeExternalProcessHandleLocked(iBinder)) {
                        this.mService.updateOomAdjLocked(providerByClass.proc, 8);
                    } else {
                        Slog.e(TAG, "Attempt to remove content provider " + providerByClass + " with no external reference for token: " + iBinder + ".");
                    }
                } else {
                    Slog.e(TAG, "Attempt to remove content provider: " + providerByClass + " with no external references.");
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean refContentProvider(IBinder iBinder, int i, int i2) {
        ProviderInfo providerInfo;
        try {
            ContentProviderConnection contentProviderConnection = (ContentProviderConnection) iBinder;
            if (contentProviderConnection == null) {
                throw new NullPointerException("connection is null");
            }
            ContentProviderRecord contentProviderRecord = contentProviderConnection.provider;
            ActivityManagerService.traceBegin(64L, "refContentProvider: ", (contentProviderRecord == null || (providerInfo = contentProviderRecord.info) == null) ? "" : providerInfo.authority);
            try {
                contentProviderConnection.adjustCounts(i, i2);
                return !contentProviderConnection.dead;
            } finally {
                Trace.traceEnd(64L);
            }
        } catch (ClassCastException unused) {
            String str = "refContentProvider: " + iBinder + " not a ContentProviderConnection";
            Slog.w(TAG, str);
            throw new IllegalArgumentException(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Finally extract failed */
    public void unstableProviderDied(IBinder iBinder) {
        IContentProvider iContentProvider;
        ProviderInfo providerInfo;
        try {
            ContentProviderConnection contentProviderConnection = (ContentProviderConnection) iBinder;
            if (contentProviderConnection == null) {
                throw new NullPointerException("connection is null");
            }
            ContentProviderRecord contentProviderRecord = contentProviderConnection.provider;
            ActivityManagerService.traceBegin(64L, "unstableProviderDied: ", (contentProviderRecord == null || (providerInfo = contentProviderRecord.info) == null) ? "" : providerInfo.authority);
            try {
                ActivityManagerService activityManagerService = this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        iContentProvider = contentProviderConnection.provider.provider;
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
                if (iContentProvider == null) {
                    return;
                }
                if (iContentProvider.asBinder().pingBinder()) {
                    ActivityManagerService activityManagerService2 = this.mService;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                        try {
                            Slog.w(TAG, "unstableProviderDied: caller " + Binder.getCallingUid() + " says " + contentProviderConnection + " died, but we don't agree");
                        } catch (Throwable th2) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th2;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
                ActivityManagerService activityManagerService3 = this.mService;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService3) {
                    try {
                        ContentProviderRecord contentProviderRecord2 = contentProviderConnection.provider;
                        if (contentProviderRecord2.provider == iContentProvider) {
                            ProcessRecord processRecord = contentProviderRecord2.proc;
                            if (processRecord != null && processRecord.getThread() != null) {
                                this.mService.reportUidInfoMessageLocked(TAG, "Process " + processRecord.processName + " (pid " + processRecord.getPid() + ") early provider death", processRecord.info.uid);
                                long clearCallingIdentity = Binder.clearCallingIdentity();
                                try {
                                    this.mService.appDiedLocked(processRecord, "unstable content provider");
                                    ActivityManagerService.resetPriorityAfterLockedSection();
                                    return;
                                } finally {
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                }
                            }
                        }
                        ActivityManagerService.resetPriorityAfterLockedSection();
                    } catch (Throwable th3) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th3;
                    }
                }
            } finally {
                Trace.traceEnd(64L);
            }
        } catch (ClassCastException unused) {
            String str = "refContentProvider: " + iBinder + " not a ContentProviderConnection";
            Slog.w(TAG, str);
            throw new IllegalArgumentException(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appNotRespondingViaProvider(IBinder iBinder) {
        ProviderInfo providerInfo;
        this.mService.enforceCallingPermission("android.permission.REMOVE_TASKS", "appNotRespondingViaProvider()");
        ContentProviderConnection contentProviderConnection = (ContentProviderConnection) iBinder;
        if (contentProviderConnection == null) {
            Slog.w(TAG, "ContentProviderConnection is null");
            return;
        }
        ContentProviderRecord contentProviderRecord = contentProviderConnection.provider;
        ActivityManagerService.traceBegin(64L, "appNotRespondingViaProvider: ", (contentProviderRecord == null || (providerInfo = contentProviderRecord.info) == null) ? "" : providerInfo.authority);
        try {
            ProcessRecord processRecord = contentProviderConnection.provider.proc;
            if (processRecord == null) {
                Slog.w(TAG, "Failed to find hosting ProcessRecord");
            } else {
                this.mService.mAnrHelper.appNotResponding(processRecord, TimeoutRecord.forContentProvider("ContentProvider not responding"));
            }
        } finally {
            Trace.traceEnd(64L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getMimeTypeFilterAsync(final Uri uri, int i, final RemoteCallback remoteCallback) {
        this.mService.enforceNotIsolatedCaller("getProviderMimeTypeAsync");
        final String authority = uri.getAuthority();
        final int callingUid = Binder.getCallingUid();
        int callingPid = Binder.getCallingPid();
        final int unsafeConvertIncomingUser = this.mService.mUserController.unsafeConvertIncomingUser(i);
        long clearCallingIdentity = canClearIdentity(callingPid, callingUid, unsafeConvertIncomingUser) ? Binder.clearCallingIdentity() : 0L;
        try {
            ContentProviderHolder contentProviderExternalUnchecked = getContentProviderExternalUnchecked(authority, null, callingUid, "*getmimetype*", unsafeConvertIncomingUser);
            try {
                if (isHolderVisibleToCaller(contentProviderExternalUnchecked, callingUid, unsafeConvertIncomingUser)) {
                    if (checkGetAnyTypePermission(callingUid, callingPid)) {
                        contentProviderExternalUnchecked.provider.getTypeAsync(new AttributionSource.Builder(callingUid).build(), uri, new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda1
                            public final void onResult(Bundle bundle) {
                                ContentProviderHelper.this.lambda$getMimeTypeFilterAsync$0(authority, unsafeConvertIncomingUser, remoteCallback, bundle);
                            }
                        }));
                        return;
                    } else {
                        contentProviderExternalUnchecked.provider.getTypeAnonymousAsync(uri, new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda2
                            public final void onResult(Bundle bundle) {
                                ContentProviderHelper.this.lambda$getMimeTypeFilterAsync$1(authority, unsafeConvertIncomingUser, remoteCallback, callingUid, uri, bundle);
                            }
                        }));
                        return;
                    }
                }
                remoteCallback.sendResult(Bundle.EMPTY);
            } catch (RemoteException e) {
                Log.w(TAG, "Content provider dead retrieving " + uri, e);
                remoteCallback.sendResult(Bundle.EMPTY);
            }
        } finally {
            if (clearCallingIdentity != 0) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMimeTypeFilterAsync$0(String str, int i, RemoteCallback remoteCallback, Bundle bundle) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            removeContentProviderExternalUnchecked(str, null, i);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            remoteCallback.sendResult(bundle);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMimeTypeFilterAsync$1(String str, int i, RemoteCallback remoteCallback, int i2, Uri uri, Bundle bundle) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            removeContentProviderExternalUnchecked(str, null, i);
            Binder.restoreCallingIdentity(clearCallingIdentity);
            remoteCallback.sendResult(bundle);
            String pairValue = bundle.getPairValue();
            if (pairValue != null) {
                logGetTypeData(i2, uri, pairValue);
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    private boolean checkGetAnyTypePermission(int i, int i2) {
        return this.mService.checkPermission("android.permission.GET_ANY_PROVIDER_TYPE", i2, i) == 0;
    }

    private void logGetTypeData(int i, Uri uri, String str) {
        FrameworkStatsLog.write(FrameworkStatsLog.GET_TYPE_ACCESSED_WITHOUT_PERMISSION, 1, i, uri.getAuthority(), str);
    }

    private boolean canClearIdentity(int i, int i2, int i3) {
        return UserHandle.getUserId(i2) == i3 || ActivityManagerService.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS", i, i2, -1, true) == 0 || ActivityManagerService.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS_FULL", i, i2, -1, true) == 0;
    }

    private boolean isHolderVisibleToCaller(ContentProviderHolder contentProviderHolder, int i, int i2) {
        ProviderInfo providerInfo;
        if (contentProviderHolder == null || (providerInfo = contentProviderHolder.info) == null) {
            return false;
        }
        if (ContentProvider.isAuthorityRedirectedForCloneProfile(providerInfo.authority) && resolveParentUserIdForCloneProfile(i2) != i2) {
            return !this.mService.getPackageManagerInternal().filterAppAccess(contentProviderHolder.info.packageName, i, i2, false);
        }
        return !this.mService.getPackageManagerInternal().filterAppAccess(contentProviderHolder.info.packageName, i, i2);
    }

    private static int resolveParentUserIdForCloneProfile(int i) {
        UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        UserInfo userInfo = userManagerInternal.getUserInfo(i);
        return (userInfo == null || !userInfo.isCloneProfile()) ? i : userManagerInternal.getProfileParentId(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String checkContentProviderAccess(String str, int i) {
        boolean z;
        ProviderInfo providerInfo;
        UserManagerInternal userManagerInternal;
        UserInfo userInfo;
        if (i == -1) {
            this.mService.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", TAG);
            i = UserHandle.getCallingUserId();
        }
        if (ContentProvider.isAuthorityRedirectedForCloneProfile(str) && (userInfo = (userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).getUserInfo(i)) != null && userInfo.isCloneProfile()) {
            i = userManagerInternal.getProfileParentId(i);
            z = false;
        } else {
            z = true;
        }
        int i2 = i;
        boolean z2 = z;
        try {
            providerInfo = AppGlobals.getPackageManager().resolveContentProvider(str, 790016L, i2);
        } catch (RemoteException unused) {
            providerInfo = null;
        }
        ProviderInfo providerInfo2 = providerInfo;
        if (providerInfo2 == null) {
            return "Failed to find provider " + str + " for user " + i2 + "; expected to find a valid ContentProvider for this authority";
        }
        int callingPid = Binder.getCallingPid();
        synchronized (this.mService.mPidsSelfLocked) {
            ProcessRecord processRecord = this.mService.mPidsSelfLocked.get(callingPid);
            if (processRecord == null) {
                return "Failed to find PID " + callingPid;
            }
            String processRecord2 = processRecord.toString();
            enforceContentProviderRestrictionsForSdkSandbox(providerInfo2);
            return checkContentProviderPermission(providerInfo2, callingPid, Binder.getCallingUid(), i2, z2, processRecord2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00de A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int checkContentProviderUriPermission(Uri uri, int i, int i2, int i3) {
        ContentProviderHolder contentProviderHolder;
        ?? holdsLock = Thread.holdsLock(this.mService.mActivityTaskManager.getGlobalLock());
        if (holdsLock != 0) {
            Slog.wtf(TAG, new IllegalStateException("Unable to check Uri permission because caller is holding WM lock; assuming permission denied"));
            return -1;
        }
        String authority = uri.getAuthority();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                contentProviderHolder = getContentProviderExternalUnchecked(authority, null, i2, "*checkContentProviderUriPermission*", i);
                if (contentProviderHolder == null) {
                    if (contentProviderHolder != null) {
                        try {
                            removeContentProviderExternalUnchecked(authority, null, i);
                        } finally {
                        }
                    }
                    return -1;
                }
                try {
                    AndroidPackage androidPackage = this.mService.getPackageManagerInternal().getPackage(Binder.getCallingUid());
                    if (androidPackage == null) {
                        try {
                            removeContentProviderExternalUnchecked(authority, null, i);
                            return -1;
                        } finally {
                        }
                    }
                    int checkUriPermission = contentProviderHolder.provider.checkUriPermission(new AttributionSource(i2, androidPackage.getPackageName(), null), uri, i2, i3);
                    try {
                        removeContentProviderExternalUnchecked(authority, null, i);
                        return checkUriPermission;
                    } finally {
                    }
                } catch (RemoteException e) {
                    e = e;
                    Log.w(TAG, "Content provider dead retrieving " + uri, e);
                    if (contentProviderHolder != null) {
                        try {
                            removeContentProviderExternalUnchecked(authority, null, i);
                        } finally {
                        }
                    }
                    return -1;
                } catch (Exception e2) {
                    e = e2;
                    Log.w(TAG, "Exception while determining type of " + uri, e);
                    if (contentProviderHolder != null) {
                        try {
                            removeContentProviderExternalUnchecked(authority, null, i);
                        } finally {
                        }
                    }
                    return -1;
                }
            } catch (RemoteException e3) {
                e = e3;
                contentProviderHolder = null;
            } catch (Exception e4) {
                e = e4;
                contentProviderHolder = null;
            } catch (Throwable th) {
                th = th;
                holdsLock = 0;
                if (holdsLock != 0) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            if (holdsLock != 0) {
                try {
                    removeContentProviderExternalUnchecked(authority, null, i);
                } finally {
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mService"})
    public void processContentProviderPublishTimedOutLocked(ProcessRecord processRecord) {
        cleanupAppInLaunchingProvidersLocked(processRecord, true);
        this.mService.mProcessList.removeProcessLocked(processRecord, false, true, 7, 0, "timeout publishing content providers");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ProviderInfo> generateApplicationProvidersLocked(ProcessRecord processRecord) {
        try {
            List<ProviderInfo> list = AppGlobals.getPackageManager().queryContentProviders(processRecord.processName, processRecord.uid, 268438528L, (String) null).getList();
            if (list == null) {
                return null;
            }
            if (ActivityManagerDebugConfig.DEBUG_MU) {
                Slog.v("ActivityManager_MU", "generateApplicationProvidersLocked, app.info.uid = " + processRecord.uid);
            }
            int size = list.size();
            ProcessProviderRecord processProviderRecord = processRecord.mProviders;
            processProviderRecord.ensureProviderCapacity(processProviderRecord.numberOfProviders() + size);
            int i = 0;
            while (i < size) {
                ProviderInfo providerInfo = list.get(i);
                boolean isSingleton = this.mService.isSingleton(providerInfo.processName, providerInfo.applicationInfo, providerInfo.name, providerInfo.flags);
                if (isSingleton && processRecord.userId != 0) {
                    list.remove(i);
                } else {
                    boolean isInstantApp = providerInfo.applicationInfo.isInstantApp();
                    String str = providerInfo.splitName;
                    boolean z = str == null || ArrayUtils.contains(providerInfo.applicationInfo.splitNames, str);
                    if (isInstantApp && !z) {
                        list.remove(i);
                    } else {
                        ComponentName componentName = new ComponentName(providerInfo.packageName, providerInfo.name);
                        ContentProviderRecord providerByClass = this.mProviderMap.getProviderByClass(componentName, processRecord.userId);
                        if (providerByClass == null) {
                            ContentProviderRecord contentProviderRecord = new ContentProviderRecord(this.mService, providerInfo, processRecord.info, componentName, isSingleton);
                            this.mProviderMap.putProviderByClass(componentName, contentProviderRecord);
                            providerByClass = contentProviderRecord;
                        }
                        if (ActivityManagerDebugConfig.DEBUG_MU) {
                            Slog.v("ActivityManager_MU", "generateApplicationProvidersLocked, cpi.uid = " + providerByClass.uid);
                        }
                        processProviderRecord.installProvider(providerInfo.name, providerByClass);
                        if (!providerInfo.multiprocess || !"android".equals(providerInfo.packageName)) {
                            ApplicationInfo applicationInfo = providerInfo.applicationInfo;
                            processRecord.addPackage(applicationInfo.packageName, applicationInfo.longVersionCode, this.mService.mProcessStats);
                        }
                        this.mService.notifyPackageUse(providerInfo.applicationInfo.packageName, 4);
                        i++;
                    }
                }
                size--;
                i--;
                i++;
            }
            if (list.isEmpty()) {
                return null;
            }
            return list;
        } catch (RemoteException unused) {
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class DevelopmentSettingsObserver extends ContentObserver {
        private final ComponentName mBugreportStorageProvider;
        private final Uri mUri;

        DevelopmentSettingsObserver() {
            super(ContentProviderHelper.this.mService.mHandler);
            Uri uriFor = Settings.Global.getUriFor("development_settings_enabled");
            this.mUri = uriFor;
            this.mBugreportStorageProvider = new ComponentName("com.android.shell", "com.android.shell.BugreportStorageProvider");
            ContentProviderHelper.this.mService.mContext.getContentResolver().registerContentObserver(uriFor, false, this, -1);
            onChange();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            if (this.mUri.equals(uri)) {
                onChange();
            }
        }

        private void onChange() {
            ContentProviderHelper.this.mService.mContext.getPackageManager().setComponentEnabledSetting(this.mBugreportStorageProvider, Settings.Global.getInt(ContentProviderHelper.this.mService.mContext.getContentResolver(), "development_settings_enabled", Build.IS_ENG ? 1 : 0) != 0 ? 1 : 0, 0);
        }
    }

    public final void installSystemProviders() {
        List<ProviderInfo> generateApplicationProvidersLocked;
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                generateApplicationProvidersLocked = generateApplicationProvidersLocked((ProcessRecord) this.mService.mProcessList.getProcessNamesLOSP().get("system", 1000));
                if (generateApplicationProvidersLocked != null) {
                    for (int size = generateApplicationProvidersLocked.size() - 1; size >= 0; size--) {
                        ProviderInfo providerInfo = generateApplicationProvidersLocked.get(size);
                        if ((providerInfo.applicationInfo.flags & 1) == 0) {
                            Slog.w(TAG, "Not installing system proc provider " + providerInfo.name + ": not system .apk");
                            generateApplicationProvidersLocked.remove(size);
                        }
                    }
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
        if (generateApplicationProvidersLocked != null) {
            this.mService.mSystemThread.installSystemProviders(generateApplicationProvidersLocked);
        }
        synchronized (this) {
            this.mSystemProvidersInstalled = true;
        }
        ActivityManagerService activityManagerService2 = this.mService;
        activityManagerService2.mConstants.start(activityManagerService2.mContext.getContentResolver());
        this.mService.mCoreSettingsObserver = new CoreSettingsObserver(this.mService);
        this.mService.mActivityTaskManager.installSystemProviders();
        new DevelopmentSettingsObserver();
        SettingsToPropertiesMapper.start(this.mService.mContext.getContentResolver());
        this.mService.mOomAdjuster.initSettings();
        RescueParty.onSettingsProviderPublished(this.mService.mContext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void installEncryptionUnawareProviders(int i) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                ArrayMap map = this.mService.mProcessList.getProcessNamesLOSP().getMap();
                int size = map.size();
                for (int i2 = 0; i2 < size; i2++) {
                    SparseArray sparseArray = (SparseArray) map.valueAt(i2);
                    int size2 = sparseArray.size();
                    for (int i3 = 0; i3 < size2; i3++) {
                        final ProcessRecord processRecord = (ProcessRecord) sparseArray.valueAt(i3);
                        if (processRecord.userId == i && processRecord.getThread() != null && !processRecord.isUnlocked()) {
                            processRecord.getPkgList().forEachPackage(new Consumer() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda4
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    ContentProviderHelper.this.lambda$installEncryptionUnawareProviders$2(processRecord, (String) obj);
                                }
                            });
                        }
                    }
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0059 A[Catch: RemoteException -> 0x009e, TRY_LEAVE, TryCatch #0 {RemoteException -> 0x009e, blocks: (B:2:0x0000, B:4:0x0013, B:6:0x001b, B:8:0x0022, B:10:0x002f, B:14:0x0037, B:16:0x0047, B:20:0x004f, B:22:0x0059, B:30:0x006f, B:33:0x0087), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0069 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$installEncryptionUnawareProviders$2(ProcessRecord processRecord, String str) {
        boolean z;
        boolean z2;
        String str2;
        try {
            PackageInfo packageInfo = AppGlobals.getPackageManager().getPackageInfo(str, 262152L, processRecord.userId);
            IApplicationThread thread = processRecord.getThread();
            if (packageInfo == null || ArrayUtils.isEmpty(packageInfo.providers)) {
                return;
            }
            for (ProviderInfo providerInfo : packageInfo.providers) {
                boolean z3 = true;
                if (!Objects.equals(providerInfo.processName, processRecord.processName) && !providerInfo.multiprocess) {
                    z = false;
                    if (this.mService.isSingleton(providerInfo.processName, providerInfo.applicationInfo, providerInfo.name, providerInfo.flags) && processRecord.userId != 0) {
                        z2 = false;
                        boolean isInstantApp = providerInfo.applicationInfo.isInstantApp();
                        str2 = providerInfo.splitName;
                        if (str2 != null && !ArrayUtils.contains(providerInfo.applicationInfo.splitNames, str2)) {
                            z3 = false;
                        }
                        if (!z && z2 && (!isInstantApp || z3)) {
                            Log.v(TAG, "Installing " + providerInfo);
                            thread.scheduleInstallProvider(providerInfo);
                        } else {
                            Log.v(TAG, "Skipping " + providerInfo);
                        }
                    }
                    z2 = true;
                    boolean isInstantApp2 = providerInfo.applicationInfo.isInstantApp();
                    str2 = providerInfo.splitName;
                    if (str2 != null) {
                        z3 = false;
                    }
                    if (z) {
                    }
                    Log.v(TAG, "Skipping " + providerInfo);
                }
                z = true;
                if (this.mService.isSingleton(providerInfo.processName, providerInfo.applicationInfo, providerInfo.name, providerInfo.flags)) {
                    z2 = false;
                    boolean isInstantApp22 = providerInfo.applicationInfo.isInstantApp();
                    str2 = providerInfo.splitName;
                    if (str2 != null) {
                    }
                    if (z) {
                    }
                    Log.v(TAG, "Skipping " + providerInfo);
                }
                z2 = true;
                boolean isInstantApp222 = providerInfo.applicationInfo.isInstantApp();
                str2 = providerInfo.splitName;
                if (str2 != null) {
                }
                if (z) {
                }
                Log.v(TAG, "Skipping " + providerInfo);
            }
        } catch (RemoteException unused) {
        }
    }

    @GuardedBy({"mService"})
    private ContentProviderConnection incProviderCountLocked(ProcessRecord processRecord, ContentProviderRecord contentProviderRecord, IBinder iBinder, int i, String str, String str2, boolean z, boolean z2, long j, ProcessList processList, int i2) {
        if (processRecord == null) {
            contentProviderRecord.addExternalProcessHandleLocked(iBinder, i, str2);
            return null;
        }
        ProcessProviderRecord processProviderRecord = processRecord.mProviders;
        int numberOfProviderConnections = processProviderRecord.numberOfProviderConnections();
        for (int i3 = 0; i3 < numberOfProviderConnections; i3++) {
            ContentProviderConnection providerConnectionAt = processProviderRecord.getProviderConnectionAt(i3);
            if (providerConnectionAt.provider == contentProviderRecord) {
                providerConnectionAt.incrementCount(z);
                return providerConnectionAt;
            }
        }
        ContentProviderConnection contentProviderConnection = new ContentProviderConnection(contentProviderRecord, processRecord, str, i2);
        contentProviderConnection.startAssociationIfNeeded();
        contentProviderConnection.initializeCount(z);
        contentProviderRecord.connections.add(contentProviderConnection);
        ProcessRecord processRecord2 = contentProviderRecord.proc;
        if (processRecord2 != null) {
            processRecord2.mProfile.addHostingComponentType(64);
        }
        processProviderRecord.addProviderConnection(contentProviderConnection);
        this.mService.startAssociationLocked(processRecord.uid, processRecord.processName, processRecord.mState.getCurProcState(), contentProviderRecord.uid, contentProviderRecord.appInfo.longVersionCode, contentProviderRecord.name, contentProviderRecord.info.processName);
        this.mContentProviderHelperExt.noteAssociation(processRecord.uid, contentProviderRecord.uid, true);
        if (z2 && contentProviderRecord.proc != null && processRecord.mState.getSetAdj() <= 250) {
            checkTime(j, "getContentProviderImpl: before updateLruProcess");
            processList.updateLruProcessLocked(contentProviderRecord.proc, false, null);
            checkTime(j, "getContentProviderImpl: after updateLruProcess");
        }
        return contentProviderConnection;
    }

    @GuardedBy({"mService"})
    private boolean decProviderCountLocked(final ContentProviderConnection contentProviderConnection, ContentProviderRecord contentProviderRecord, IBinder iBinder, final boolean z, boolean z2, final boolean z3) {
        if (contentProviderConnection == null) {
            contentProviderRecord.removeExternalProcessHandleLocked(iBinder);
            return false;
        }
        if (contentProviderConnection.totalRefCount() > 1) {
            contentProviderConnection.decrementCount(z);
            return false;
        }
        if (z2) {
            OplusIoThread.getHandler().postDelayed(new Runnable() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ContentProviderHelper.this.lambda$decProviderCountLocked$3(contentProviderConnection, z, z3);
                }
            }, 5000L);
        } else {
            lambda$decProviderCountLocked$3(contentProviderConnection, z, z3);
        }
        return true;
    }

    @GuardedBy({"mService"})
    private boolean hasProviderConnectionLocked(ProcessRecord processRecord) {
        for (int numberOfProviders = processRecord.mProviders.numberOfProviders() - 1; numberOfProviders >= 0; numberOfProviders--) {
            if (!processRecord.mProviders.getProviderAt(numberOfProviders).connections.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleProviderRemoval, reason: merged with bridge method [inline-methods] */
    public void lambda$decProviderCountLocked$3(ContentProviderConnection contentProviderConnection, boolean z, boolean z2) {
        ProcessRecord processRecord;
        ActivityManagerService activityManagerService = this.mService;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            if (contentProviderConnection != null) {
                try {
                    if (contentProviderConnection.provider != null && contentProviderConnection.decrementCount(z) == 0) {
                        ActivityManagerService.traceBegin(64L, "handleProviderRemoval: ", contentProviderConnection.toString());
                        Trace.traceEnd(64L);
                        ContentProviderRecord contentProviderRecord = contentProviderConnection.provider;
                        contentProviderConnection.stopAssociation();
                        contentProviderRecord.connections.remove(contentProviderConnection);
                        ProcessRecord processRecord2 = contentProviderRecord.proc;
                        if (processRecord2 != null && !hasProviderConnectionLocked(processRecord2)) {
                            contentProviderRecord.proc.mProfile.clearHostingComponentType(64);
                        }
                        contentProviderConnection.client.mProviders.removeProviderConnection(contentProviderConnection);
                        if (contentProviderConnection.client.mState.getSetProcState() < 15 && (processRecord = contentProviderRecord.proc) != null) {
                            processRecord.mProviders.setLastProviderTime(SystemClock.uptimeMillis());
                        }
                        ActivityManagerService activityManagerService2 = this.mService;
                        ProcessRecord processRecord3 = contentProviderConnection.client;
                        activityManagerService2.stopAssociationLocked(processRecord3.uid, processRecord3.processName, contentProviderRecord.uid, contentProviderRecord.appInfo.longVersionCode, contentProviderRecord.name, contentProviderRecord.info.processName);
                        this.mContentProviderHelperExt.noteAssociation(contentProviderConnection.client.uid, contentProviderRecord.uid, true);
                        if (z2) {
                            this.mService.updateOomAdjLocked(contentProviderConnection.provider.proc, 8);
                        }
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                } catch (Throwable th) {
                    ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    private String checkContentProviderPermission(ProviderInfo providerInfo, int i, int i2, int i3, boolean z, String str) {
        int i4;
        String str2;
        boolean z2;
        boolean z3 = false;
        if (z) {
            int unsafeConvertIncomingUser = this.mService.mUserController.unsafeConvertIncomingUser(i3);
            if (unsafeConvertIncomingUser == UserHandle.getUserId(i2)) {
                z2 = false;
            } else {
                if (this.mService.mUgmInternal.checkAuthorityGrants(i2, providerInfo, unsafeConvertIncomingUser, z)) {
                    return null;
                }
                z2 = true;
            }
            i4 = this.mService.mUserController.handleIncomingUser(i, i2, i3, false, 0, "checkContentProviderPermissionLocked " + providerInfo.authority, null);
            if (i4 == unsafeConvertIncomingUser) {
                z3 = z2;
            }
        } else {
            i4 = i3;
        }
        if (ActivityManagerService.checkComponentPermission(providerInfo.readPermission, i, i2, providerInfo.applicationInfo.uid, providerInfo.exported) == 0 || ActivityManagerService.checkComponentPermission(providerInfo.writePermission, i, i2, providerInfo.applicationInfo.uid, providerInfo.exported) == 0) {
            return null;
        }
        PathPermission[] pathPermissionArr = providerInfo.pathPermissions;
        if (pathPermissionArr != null) {
            int length = pathPermissionArr.length;
            while (length > 0) {
                length--;
                PathPermission pathPermission = pathPermissionArr[length];
                String readPermission = pathPermission.getReadPermission();
                if (readPermission != null && ActivityManagerService.checkComponentPermission(readPermission, i, i2, providerInfo.applicationInfo.uid, providerInfo.exported) == 0) {
                    return null;
                }
                String writePermission = pathPermission.getWritePermission();
                if (writePermission != null && ActivityManagerService.checkComponentPermission(writePermission, i, i2, providerInfo.applicationInfo.uid, providerInfo.exported) == 0) {
                    return null;
                }
            }
        }
        if (!z3 && this.mService.mUgmInternal.checkAuthorityGrants(i2, providerInfo, i4, z)) {
            return null;
        }
        if (!providerInfo.exported) {
            str2 = " that is not exported from UID " + providerInfo.applicationInfo.uid;
        } else if ("android.permission.MANAGE_DOCUMENTS".equals(providerInfo.readPermission)) {
            str2 = " requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs";
        } else {
            str2 = " requires " + providerInfo.readPermission + " or " + providerInfo.writePermission;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Permission Denial: opening provider ");
        sb.append(providerInfo.name);
        sb.append(" from ");
        sb.append(str != null ? str : "(null)");
        sb.append(" (pid=");
        sb.append(i);
        sb.append(", uid=");
        sb.append(i2);
        sb.append(")");
        sb.append(str2);
        String sb2 = sb.toString();
        Slog.w(TAG, sb2);
        return sb2;
    }

    private String checkContentProviderAssociation(final ProcessRecord processRecord, int i, final ProviderInfo providerInfo) {
        if (processRecord == null) {
            if (this.mService.validateAssociationAllowedLocked(providerInfo.packageName, providerInfo.applicationInfo.uid, null, i)) {
                return null;
            }
            return "<null>";
        }
        return (String) processRecord.getPkgList().searchEachPackage(new Function() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$checkContentProviderAssociation$4;
                lambda$checkContentProviderAssociation$4 = ContentProviderHelper.this.lambda$checkContentProviderAssociation$4(processRecord, providerInfo, (String) obj);
                return lambda$checkContentProviderAssociation$4;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$checkContentProviderAssociation$4(ProcessRecord processRecord, ProviderInfo providerInfo, String str) {
        if (this.mService.validateAssociationAllowedLocked(str, processRecord.uid, providerInfo.packageName, providerInfo.applicationInfo.uid)) {
            return null;
        }
        return providerInfo.packageName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProviderInfo getProviderInfoLocked(String str, int i, int i2) {
        ContentProviderRecord providerByName = this.mProviderMap.getProviderByName(str, i);
        if (providerByName != null) {
            return providerByName.info;
        }
        try {
            return AppGlobals.getPackageManager().resolveContentProvider(str, i2 | 2048, i);
        } catch (RemoteException unused) {
            return null;
        }
    }

    private void maybeUpdateProviderUsageStatsLocked(ProcessRecord processRecord, String str, String str2) {
        UserState startedUserState;
        if (processRecord == null || processRecord.mState.getCurProcState() > 6 || (startedUserState = this.mService.mUserController.getStartedUserState(processRecord.userId)) == null) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Long l = startedUserState.mProviderLastReportedFg.get(str2);
        if (l == null || l.longValue() < elapsedRealtime - 60000) {
            if (this.mService.mSystemReady) {
                this.mService.mUsageStatsService.reportContentProviderUsage(str2, str, processRecord.userId);
            }
            startedUserState.mProviderLastReportedFg.put(str2, Long.valueOf(elapsedRealtime));
        }
    }

    private boolean isProcessAliveLocked(ProcessRecord processRecord) {
        int pid = processRecord.getPid();
        if (pid <= 0) {
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                Slog.d(IActivityManagerServiceExt.TAG, "Process hasn't started yet: " + processRecord);
            }
            return false;
        }
        String str = "/proc/" + pid + "/stat";
        long[] jArr = this.mProcessStateStatsLongs;
        jArr[0] = 0;
        if (!Process.readProcFile(str, PROCESS_STATE_STATS_FORMAT, null, jArr, null)) {
            if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                Slog.d(IActivityManagerServiceExt.TAG, "UNABLE TO RETRIEVE STATE FOR " + str);
            }
            return false;
        }
        long j = this.mProcessStateStatsLongs[0];
        if (ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            Slog.d(IActivityManagerServiceExt.TAG, "RETRIEVED STATE FOR " + str + ": " + ((char) j));
        }
        return (j == 90 || j == 88 || j == 120 || j == 75 || Process.getUidForPid(pid) != processRecord.uid) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class StartActivityRunnable implements Runnable {
        private final Context mContext;
        private final Intent mIntent;
        private final UserHandle mUserHandle;

        StartActivityRunnable(Context context, Intent intent, UserHandle userHandle) {
            this.mContext = context;
            this.mIntent = intent;
            this.mUserHandle = userHandle;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mContext.startActivityAsUser(this.mIntent, this.mUserHandle);
        }
    }

    private boolean requestTargetProviderPermissionsReviewIfNeededLocked(ProviderInfo providerInfo, ProcessRecord processRecord, int i, Context context) {
        boolean z = true;
        if (!this.mService.getPackageManagerInternal().isPermissionsReviewRequired(providerInfo.packageName, i)) {
            return true;
        }
        if (processRecord != null && processRecord.mState.getSetSchedGroup() == 0) {
            z = false;
        }
        if (!z) {
            Slog.w(TAG, "u" + i + " Instantiating a provider in package " + providerInfo.packageName + " requires a permissions review");
            return false;
        }
        Intent intent = new Intent("android.intent.action.REVIEW_PERMISSIONS");
        intent.addFlags(276824064);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", providerInfo.packageName);
        if (ActivityManagerDebugConfig.DEBUG_PERMISSIONS_REVIEW) {
            Slog.i(TAG, "u" + i + " Launching permission review for package " + providerInfo.packageName);
        }
        this.mService.mHandler.post(new StartActivityRunnable(context, intent, new UserHandle(i)));
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean removeDyingProviderLocked(ProcessRecord processRecord, ContentProviderRecord contentProviderRecord, boolean z) {
        boolean z2;
        IContentProvider iContentProvider;
        boolean contains = this.mLaunchingProviders.contains(contentProviderRecord);
        if (contains && !z) {
            int i = contentProviderRecord.mRestartCount + 1;
            contentProviderRecord.mRestartCount = i;
            if (i > 3) {
                z2 = true;
                int i2 = 0;
                if (contains || z2) {
                    synchronized (contentProviderRecord) {
                        contentProviderRecord.launchingApp = null;
                        contentProviderRecord.notifyAll();
                        contentProviderRecord.onProviderPublishStatusLocked(false);
                        this.mService.mHandler.removeMessages(73, contentProviderRecord);
                    }
                    int userId = UserHandle.getUserId(contentProviderRecord.uid);
                    if (this.mProviderMap.getProviderByClass(contentProviderRecord.name, userId) == contentProviderRecord) {
                        this.mProviderMap.removeProviderByClass(contentProviderRecord.name, userId);
                    }
                    String[] split = contentProviderRecord.info.authority.split(";");
                    for (int i3 = 0; i3 < split.length; i3++) {
                        if (this.mProviderMap.getProviderByName(split[i3], userId) == contentProviderRecord) {
                            this.mProviderMap.removeProviderByName(split[i3], userId);
                        }
                    }
                }
                int size = contentProviderRecord.connections.size() - 1;
                while (size >= 0) {
                    ContentProviderConnection contentProviderConnection = contentProviderRecord.connections.get(size);
                    if (!contentProviderConnection.waiting || !contains || z2) {
                        ProcessRecord processRecord2 = contentProviderConnection.client;
                        IApplicationThread thread = processRecord2.getThread();
                        contentProviderConnection.dead = true;
                        if (contentProviderConnection.stableCount() > 0) {
                            int pid = processRecord2.getPid();
                            if (!processRecord2.isPersistent() && thread != null && pid != 0 && pid != ActivityManagerService.MY_PID) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("depends on provider ");
                                sb.append(contentProviderRecord.name.flattenToShortString());
                                sb.append(" in dying proc ");
                                sb.append(processRecord != null ? processRecord.processName : "??");
                                sb.append(" (adj ");
                                sb.append(processRecord != null ? Integer.valueOf(processRecord.mState.getSetAdj()) : "??");
                                sb.append(")");
                                processRecord2.killLocked(sb.toString(), 12, i2, true);
                            }
                        } else if (thread != null && (iContentProvider = contentProviderConnection.provider.provider) != null) {
                            try {
                                thread.unstableProviderDied(iContentProvider.asBinder());
                            } catch (RemoteException unused) {
                            }
                            contentProviderRecord.connections.remove(size);
                            ProcessRecord processRecord3 = contentProviderRecord.proc;
                            if (processRecord3 != null && !hasProviderConnectionLocked(processRecord3)) {
                                contentProviderRecord.proc.mProfile.clearHostingComponentType(64);
                            }
                            if (contentProviderConnection.client.mProviders.removeProviderConnection(contentProviderConnection)) {
                                this.mService.stopAssociationLocked(processRecord2.uid, processRecord2.processName, contentProviderRecord.uid, contentProviderRecord.appInfo.longVersionCode, contentProviderRecord.name, contentProviderRecord.info.processName);
                                this.mContentProviderHelperExt.noteAssociation(contentProviderConnection.client.uid, contentProviderRecord.uid, true);
                            }
                        }
                    }
                    size--;
                    i2 = 0;
                }
                if (!contains || !z2) {
                    return contains;
                }
                ProcessRecord processRecord4 = contentProviderRecord.launchingApp;
                if (processRecord4 != null && processRecord4.getWrapper() != null) {
                    contentProviderRecord.launchingApp.getWrapper().getExtImpl().updateExecutingComponent(contentProviderRecord.launchingApp, "provider", 2);
                }
                this.mLaunchingProviders.remove(contentProviderRecord);
                contentProviderRecord.mRestartCount = 0;
                return false;
            }
        }
        z2 = z;
        int i22 = 0;
        if (contains) {
        }
        synchronized (contentProviderRecord) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkAppInLaunchingProvidersLocked(ProcessRecord processRecord) {
        for (int size = this.mLaunchingProviders.size() - 1; size >= 0; size--) {
            if (this.mLaunchingProviders.get(size).launchingApp == processRecord) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cleanupAppInLaunchingProvidersLocked(ProcessRecord processRecord, boolean z) {
        boolean z2 = false;
        for (int size = this.mLaunchingProviders.size() - 1; size >= 0; size--) {
            ContentProviderRecord contentProviderRecord = this.mLaunchingProviders.get(size);
            if (contentProviderRecord.launchingApp == processRecord) {
                int i = contentProviderRecord.mRestartCount + 1;
                contentProviderRecord.mRestartCount = i;
                if (i > 3) {
                    z = true;
                }
                if (z || processRecord.mErrorState.isBad() || !contentProviderRecord.hasConnectionOrHandle()) {
                    removeDyingProviderLocked(processRecord, contentProviderRecord, true);
                } else {
                    z2 = true;
                }
            }
        }
        return z2;
    }

    void cleanupLaunchingProvidersLocked() {
        for (int size = this.mLaunchingProviders.size() - 1; size >= 0; size--) {
            ContentProviderRecord contentProviderRecord = this.mLaunchingProviders.get(size);
            if (contentProviderRecord.connections.size() <= 0 && !contentProviderRecord.hasExternalProcessHandles()) {
                synchronized (contentProviderRecord) {
                    contentProviderRecord.launchingApp = null;
                    contentProviderRecord.notifyAll();
                }
            }
        }
    }

    private void checkTime(long j, String str) {
        long uptimeMillis = SystemClock.uptimeMillis() - j;
        if (uptimeMillis > 50) {
            Slog.w(TAG, "Slow operation: " + uptimeMillis + "ms so far, now at " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpProvidersLocked(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i, boolean z, String str) {
        new ActivityManagerService.ItemMatcher().build(strArr, i);
        printWriter.println("ACTIVITY MANAGER CONTENT PROVIDERS (dumpsys activity providers)");
        boolean dumpProvidersLocked = this.mProviderMap.dumpProvidersLocked(printWriter, z, str);
        if (this.mLaunchingProviders.size() > 0) {
            boolean z2 = false;
            boolean z3 = dumpProvidersLocked;
            for (int size = this.mLaunchingProviders.size() - 1; size >= 0; size--) {
                ContentProviderRecord contentProviderRecord = this.mLaunchingProviders.get(size);
                if (str == null || str.equals(contentProviderRecord.name.getPackageName())) {
                    if (!z2) {
                        if (z3) {
                            printWriter.println();
                        }
                        printWriter.println("  Launching content providers:");
                        dumpProvidersLocked = true;
                        z3 = true;
                        z2 = true;
                    }
                    printWriter.print("  Launching #");
                    printWriter.print(size);
                    printWriter.print(": ");
                    printWriter.println(contentProviderRecord);
                }
            }
        }
        if (dumpProvidersLocked) {
            return;
        }
        printWriter.println("  (nothing)");
    }

    private void enforceContentProviderRestrictionsForSdkSandbox(ProviderInfo providerInfo) {
        if (Process.isSdkSandboxUid(Binder.getCallingUid())) {
            SdkSandboxManagerLocal sdkSandboxManagerLocal = (SdkSandboxManagerLocal) LocalManagerRegistry.getManager(SdkSandboxManagerLocal.class);
            if (sdkSandboxManagerLocal == null) {
                throw new IllegalStateException("SdkSandboxManagerLocal not found when checking whether SDK sandbox uid may access the contentprovider.");
            }
            if (sdkSandboxManagerLocal.canAccessContentProviderFromSdkSandbox(providerInfo)) {
                return;
            }
            throw new SecurityException("SDK sandbox uid may not access contentprovider " + providerInfo.name);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dumpProvider(FileDescriptor fileDescriptor, PrintWriter printWriter, String str, String[] strArr, int i, boolean z) {
        return this.mProviderMap.dumpProvider(fileDescriptor, printWriter, str, strArr, i, z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dumpProviderProto(FileDescriptor fileDescriptor, PrintWriter printWriter, String str, String[] strArr) {
        return this.mProviderMap.dumpProviderProto(fileDescriptor, printWriter, str, strArr);
    }
}
