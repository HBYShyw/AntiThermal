package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.prediction.AppTarget;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.net.OplusNetworkingControlManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.IChooserTargetResult;
import android.service.chooser.IChooserTargetService;
import android.text.TextUtils;
import com.oplus.resolver.OplusResolverUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class OplusChooserCtsConnection {
    private static final float ZERO_ZERO_ONE = 0.01f;
    private final ChooserHandler mChooserHandler = new ChooserHandler();
    private Context mContext;
    private Object mResolverListAdapter;
    private OplusResolverManager mResolverManager;

    public void testCts(Context context, OplusResolverManager manager, ResolverListAdapter adapter, UserHandle userHandle) {
    }

    public void testCts(Context context, OplusResolverManager manager, Object adapter, UserHandle userHandle) {
        OplusResolverManager oplusResolverManager = manager;
        if (ActivityManager.isLowRamDeviceStatic()) {
            return;
        }
        this.mContext = context;
        this.mResolverListAdapter = adapter;
        this.mResolverManager = oplusResolverManager;
        Intent originIntent = ((Activity) context).getIntent();
        Parcelable[] pa = originIntent.getParcelableArrayExtra("android.intent.extra.CHOOSER_TARGETS");
        if (pa != null) {
            int count = Math.min(pa.length, 2);
            int i = 0;
            while (i < count && (pa[i] instanceof ChooserTarget)) {
                ChooserTarget target = (ChooserTarget) pa[i];
                Intent intent = new Intent();
                intent.setComponent(target.getComponentName());
                ResolveInfo resolveInfo = oplusResolverManager.getResolveInfo(intent, this.mContext.getPackageManager());
                if (resolveInfo != null) {
                    try {
                        Class cls = Class.forName("com.android.intentresolver.chooser.DisplayResolveInfo", false, context.getClassLoader());
                        Method method = cls.getDeclaredMethod("newDisplayResolveInfo", Intent.class, ResolveInfo.class, Intent.class, Class.forName("com.android.intentresolver.TargetPresentationGetter", false, context.getClassLoader()));
                        Object ri = method.invoke(null, manager.getTargetIntent(), resolveInfo, manager.getTargetIntent(), null);
                        ((ResolveInfo) OplusResolverUtils.invokeMethod(ri, "getResolveInfo", new Object[0])).nonLocalizedLabel = target.getTitle();
                        ((List) OplusResolverUtils.getFiledValue(this.mResolverListAdapter, "mDisplayList")).add(0, ri);
                    } catch (Exception e) {
                    }
                }
                i++;
                oplusResolverManager = manager;
            }
        }
        queryDirectShareTargets(context, userHandle);
    }

    private void queryDirectShareTargets(final Context context, final UserHandle userHandle) {
        final IntentFilter filter = getTargetIntentFilter();
        if (filter == null) {
            return;
        }
        final List driList = (List) OplusResolverUtils.getFiledValue(this.mResolverListAdapter, "mDisplayList");
        AsyncTask.execute(new Runnable() { // from class: com.android.internal.app.OplusChooserCtsConnection$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OplusChooserCtsConnection.this.lambda$queryDirectShareTargets$0(context, userHandle, filter, driList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$queryDirectShareTargets$0(Context context, UserHandle userHandle, IntentFilter filter, List driList) {
        Context selectedProfileContext = context.createContextAsUser(userHandle, 0);
        ShortcutManager sm = (ShortcutManager) selectedProfileContext.getSystemService("shortcut");
        List<ShortcutManager.ShareShortcutInfo> resultList = sm.getShareTargets(filter);
        sendShareShortcutInfoList(resultList, driList, null, userHandle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OplusChooserTargetServiceConnection implements ServiceConnection {
        private OplusChooserCtsConnection mConnection;
        private Context mContext;
        private Object mOriginalTarget;
        private final UserHandle mUserHandle;
        private final Object mLock = new Object();
        private final IChooserTargetResult mChooserTargetResult = new IChooserTargetResult.Stub() { // from class: com.android.internal.app.OplusChooserCtsConnection.OplusChooserTargetServiceConnection.1
            public void sendResult(List<ChooserTarget> targets) throws RemoteException {
                synchronized (OplusChooserTargetServiceConnection.this.mLock) {
                    if (OplusChooserTargetServiceConnection.this.mContext == null) {
                        return;
                    }
                    Context contextAsUser = OplusChooserTargetServiceConnection.this.mContext.createContextAsUser(OplusChooserTargetServiceConnection.this.mUserHandle, 0);
                    OplusChooserTargetServiceConnection oplusChooserTargetServiceConnection = OplusChooserTargetServiceConnection.this;
                    oplusChooserTargetServiceConnection.filterServiceTargets(contextAsUser, (String) OplusResolverUtils.getFiledValue(OplusResolverUtils.getFiledValue(OplusResolverUtils.invokeMethod(oplusChooserTargetServiceConnection.mOriginalTarget, "getResolveInfo", new Object[0]), "activityInfo"), OplusNetworkingControlManager.EXTRA_PACKAGE_NAME), targets);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    Object obj = OplusChooserTargetServiceConnection.this.mOriginalTarget;
                    OplusChooserTargetServiceConnection oplusChooserTargetServiceConnection2 = OplusChooserTargetServiceConnection.this;
                    msg.obj = new ServiceResultInfo(obj, targets, oplusChooserTargetServiceConnection2, oplusChooserTargetServiceConnection2.mUserHandle);
                    OplusChooserTargetServiceConnection.this.mConnection.mChooserHandler.sendMessage(msg);
                }
            }
        };

        public OplusChooserTargetServiceConnection(Context context, OplusChooserCtsConnection connetion, Object dri, UserHandle userHandle) {
            this.mContext = context;
            this.mConnection = connetion;
            this.mOriginalTarget = dri;
            this.mUserHandle = userHandle;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filterServiceTargets(Context contextAsUser, String packageName, List<ChooserTarget> targets) {
            boolean remove;
            if (targets == null) {
                return;
            }
            PackageManager pm = contextAsUser.getPackageManager();
            for (int i = targets.size() - 1; i >= 0; i--) {
                ChooserTarget target = targets.get(i);
                ComponentName targetName = target.getComponentName();
                if (packageName == null || !packageName.equals(targetName.getPackageName())) {
                    try {
                        ActivityInfo ai = pm.getActivityInfo(targetName, 0);
                        remove = (ai.exported && ai.permission == null) ? false : true;
                    } catch (PackageManager.NameNotFoundException e) {
                        remove = true;
                    }
                    if (remove) {
                        targets.remove(i);
                    }
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (this.mLock) {
                if (this.mContext == null) {
                    return;
                }
                IChooserTargetService icts = IChooserTargetService.Stub.asInterface(service);
                try {
                    icts.getChooserTargets((ComponentName) OplusResolverUtils.invokeMethod(this.mOriginalTarget, "getResolvedComponentName", new Object[0]), (IntentFilter) OplusResolverUtils.getFiledValue(OplusResolverUtils.invokeMethod(this.mOriginalTarget, "getResolveInfo", new Object[0]), "filter"), this.mChooserTargetResult);
                } catch (RemoteException e) {
                    this.mContext.unbindService(this);
                    destroy();
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            synchronized (this.mLock) {
                Context context = this.mContext;
                if (context == null) {
                    return;
                }
                context.unbindService(this);
                destroy();
            }
        }

        public void destroy() {
            synchronized (this.mLock) {
                this.mContext = null;
                this.mOriginalTarget = null;
            }
        }

        public ComponentName getComponentName() {
            ComponentName componentName;
            synchronized (this.mLock) {
                componentName = (ComponentName) OplusResolverUtils.getFiledValue(OplusResolverUtils.getFiledValue(OplusResolverUtils.invokeMethod(this.mOriginalTarget, "getResolveInfo", new Object[0]), "activityInfo"), "getComponentName");
            }
            return componentName;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ServiceResultInfo {
        public final OplusChooserTargetServiceConnection connection;
        public final Object originalTarget;
        public final List<ChooserTarget> resultTargets;
        public final UserHandle userHandle;

        public ServiceResultInfo(Object ot, List<ChooserTarget> rt, OplusChooserTargetServiceConnection c, UserHandle userHandle) {
            this.originalTarget = ot;
            this.resultTargets = rt;
            this.connection = c;
            this.userHandle = userHandle;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ChooserHandler extends Handler {
        private static final int CHOOSER_TARGET_SERVICE_RESULT = 1;
        private static final int SHORTCUT_MANAGER_SHARE_TARGET_RESULT = 4;

        private ChooserHandler() {
        }

        /* JADX WARN: Type inference failed for: r15v0 */
        /* JADX WARN: Type inference failed for: r15v26 */
        /* JADX WARN: Type inference failed for: r15v27 */
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            String str;
            String str2;
            Method method;
            Object[] objArr;
            if (((Activity) OplusChooserCtsConnection.this.mContext).isDestroyed()) {
                return;
            }
            String str3 = "com.android.intentresolver.TargetPresentationGetter";
            String str4 = "com.android.intentresolver.chooser.DisplayResolveInfo";
            int i = 4;
            int i2 = 0;
            switch (msg.what) {
                case 1:
                    ServiceResultInfo sri = (ServiceResultInfo) msg.obj;
                    if (sri.resultTargets != null) {
                        for (ChooserTarget target : sri.resultTargets) {
                            Intent intent = new Intent((Intent) OplusResolverUtils.invokeMethod(sri.originalTarget, "getResolvedIntent", new Object[0]));
                            intent.setComponent((ComponentName) OplusResolverUtils.invokeMethod(sri.originalTarget, "getResolvedComponentName", new Object[0]));
                            ResolveInfo resolveInfo = OplusChooserCtsConnection.this.mResolverManager.getResolveInfo(intent, OplusChooserCtsConnection.this.mContext.getPackageManager());
                            if (resolveInfo != null) {
                                try {
                                    try {
                                        Class<?> cls = Class.forName(str4, false, OplusChooserCtsConnection.this.mContext.getClassLoader());
                                        Class<?>[] clsArr = new Class[4];
                                        try {
                                            clsArr[0] = Intent.class;
                                            clsArr[1] = ResolveInfo.class;
                                            clsArr[2] = Intent.class;
                                            str2 = str4;
                                            try {
                                                try {
                                                    clsArr[3] = Class.forName(str3, false, OplusChooserCtsConnection.this.mContext.getClassLoader());
                                                    try {
                                                        method = cls.getDeclaredMethod("newDisplayResolveInfo", clsArr);
                                                        objArr = new Object[4];
                                                        str = str3;
                                                    } catch (Exception e) {
                                                        str = str3;
                                                    }
                                                } catch (Exception e2) {
                                                    str = str3;
                                                }
                                            } catch (Exception e3) {
                                                str = str3;
                                            }
                                            try {
                                                objArr[0] = OplusResolverUtils.invokeMethod(sri.originalTarget, "getResolvedIntent", new Object[0]);
                                                objArr[1] = resolveInfo;
                                                try {
                                                    objArr[2] = OplusResolverUtils.invokeMethod(sri.originalTarget, "getResolvedIntent", new Object[0]);
                                                    try {
                                                        objArr[3] = null;
                                                        Object ri = method.invoke(null, objArr);
                                                        try {
                                                            ((ResolveInfo) OplusResolverUtils.invokeMethod(ri, "getResolveInfo", new Object[0])).nonLocalizedLabel = target.getTitle();
                                                            try {
                                                                ((ResolveInfo) OplusResolverUtils.invokeMethod(ri, "getResolveInfo", new Object[0])).activityInfo.name = target.getComponentName().getClassName();
                                                                ((List) OplusResolverUtils.getFiledValue(OplusChooserCtsConnection.this.mResolverListAdapter, "mDisplayList")).add(0, ri);
                                                            } catch (Exception e4) {
                                                            }
                                                        } catch (Exception e5) {
                                                        }
                                                    } catch (Exception e6) {
                                                        str4 = str2;
                                                        str3 = str;
                                                    }
                                                } catch (Exception e7) {
                                                    str4 = str2;
                                                    str3 = str;
                                                }
                                            } catch (Exception e8) {
                                                str4 = str2;
                                                str3 = str;
                                            }
                                        } catch (Exception e9) {
                                            str = str3;
                                            str2 = str4;
                                        }
                                    } catch (Exception e10) {
                                        str = str3;
                                        str2 = str4;
                                    }
                                } catch (Exception e11) {
                                    str = str3;
                                    str2 = str4;
                                }
                                str4 = str2;
                                str3 = str;
                            }
                        }
                        OplusChooserCtsConnection.this.mResolverManager.resortListAndNotifyChange();
                    }
                    OplusChooserCtsConnection.this.mContext.unbindService(sri.connection);
                    sri.connection.destroy();
                    return;
                case 4:
                    ServiceResultInfo resultInfo = (ServiceResultInfo) msg.obj;
                    if (resultInfo.resultTargets != null) {
                        for (ChooserTarget target2 : resultInfo.resultTargets) {
                            Intent intent2 = new Intent((Intent) OplusResolverUtils.invokeMethod(resultInfo.originalTarget, "getResolvedIntent", new Object[i2]));
                            intent2.setComponent((ComponentName) OplusResolverUtils.invokeMethod(resultInfo.originalTarget, "getResolvedComponentName", new Object[i2]));
                            ResolveInfo resolveInfo2 = OplusChooserCtsConnection.this.mResolverManager.getResolveInfo(intent2, OplusChooserCtsConnection.this.mContext.getPackageManager());
                            if (resolveInfo2 != null) {
                                try {
                                    Class<?> cls2 = Class.forName("com.android.intentresolver.chooser.DisplayResolveInfo", i2, OplusChooserCtsConnection.this.mContext.getClassLoader());
                                    Class<?>[] clsArr2 = new Class[i];
                                    clsArr2[i2] = Intent.class;
                                    clsArr2[1] = ResolveInfo.class;
                                    clsArr2[2] = Intent.class;
                                    clsArr2[3] = Class.forName("com.android.intentresolver.TargetPresentationGetter", i2, OplusChooserCtsConnection.this.mContext.getClassLoader());
                                    Method method2 = cls2.getDeclaredMethod("newDisplayResolveInfo", clsArr2);
                                    Object[] objArr2 = new Object[4];
                                    try {
                                        objArr2[0] = OplusResolverUtils.invokeMethod(resultInfo.originalTarget, "getResolvedIntent", new Object[0]);
                                        objArr2[1] = resolveInfo2;
                                        objArr2[2] = OplusResolverUtils.invokeMethod(resultInfo.originalTarget, "getResolvedIntent", new Object[0]);
                                        objArr2[3] = null;
                                        Object ri2 = method2.invoke(null, objArr2);
                                        ((ResolveInfo) OplusResolverUtils.invokeMethod(ri2, "getResolveInfo", new Object[0])).nonLocalizedLabel = target2.getTitle();
                                        ((ResolveInfo) OplusResolverUtils.invokeMethod(ri2, "getResolveInfo", new Object[0])).activityInfo.name = target2.getComponentName().getClassName();
                                        ((List) OplusResolverUtils.getFiledValue(OplusChooserCtsConnection.this.mResolverListAdapter, "mDisplayList")).add(0, ri2);
                                    } catch (Exception e12) {
                                    }
                                } catch (Exception e13) {
                                }
                                i = 4;
                                i2 = 0;
                            }
                        }
                        OplusChooserCtsConnection.this.mResolverManager.resortListAndNotifyChange();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private String convertServiceName(String packageName, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) {
            return null;
        }
        if (serviceName.startsWith(".")) {
            String fullName = packageName + serviceName;
            return fullName;
        }
        if (serviceName.indexOf(46) >= 0) {
            return serviceName;
        }
        return null;
    }

    private IntentFilter getTargetIntentFilter() {
        try {
            Intent intent = this.mResolverManager.getTargetIntent();
            String dataString = intent.getDataString();
            if (!TextUtils.isEmpty(dataString)) {
                return new IntentFilter(intent.getAction(), dataString);
            }
            if (intent.getType() == null) {
                return null;
            }
            IntentFilter intentFilter = new IntentFilter(intent.getAction(), intent.getType());
            List<Uri> contentUris = new ArrayList<>();
            if ("android.intent.action.SEND".equals(intent.getAction())) {
                Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.STREAM");
                if (uri != null) {
                    contentUris.add(uri);
                }
            } else {
                List<Uri> uris = intent.getParcelableArrayListExtra("android.intent.extra.STREAM");
                if (uris != null) {
                    contentUris.addAll(uris);
                }
            }
            for (Uri uri2 : contentUris) {
                intentFilter.addDataScheme(uri2.getScheme());
                intentFilter.addDataAuthority(uri2.getAuthority(), null);
                intentFilter.addDataPath(uri2.getPath(), 0);
            }
            return intentFilter;
        } catch (Exception e) {
            return null;
        }
    }

    private void sendShareShortcutInfoList(List<ShortcutManager.ShareShortcutInfo> resultList, List driList, List appTargets, UserHandle userHandle) {
        if (appTargets != null && appTargets.size() != resultList.size()) {
            throw new RuntimeException("resultList and appTargets must have the same size. resultList.size()=" + resultList.size() + " appTargets.size()=" + appTargets.size());
        }
        int shortcutType = appTargets == null ? 2 : 3;
        for (int i = 0; i < driList.size(); i++) {
            List<ShortcutManager.ShareShortcutInfo> matchingShortcuts = new ArrayList<>();
            for (int j = 0; j < resultList.size(); j++) {
                if (((ComponentName) OplusResolverUtils.invokeMethod(driList.get(i), "getResolvedComponentName", new Object[0])).equals(resultList.get(j).getTargetComponent())) {
                    matchingShortcuts.add(resultList.get(j));
                }
            }
            if (!matchingShortcuts.isEmpty()) {
                List<ChooserTarget> chooserTargets = convertToChooserTarget(matchingShortcuts, resultList, appTargets, shortcutType);
                Message msg = Message.obtain();
                msg.what = 4;
                msg.obj = new ServiceResultInfo(driList.get(i), chooserTargets, null, userHandle);
                msg.arg1 = shortcutType;
                this.mChooserHandler.sendMessage(msg);
            }
        }
    }

    private List<ChooserTarget> convertToChooserTarget(List<ShortcutManager.ShareShortcutInfo> matchingShortcuts, List<ShortcutManager.ShareShortcutInfo> allShortcuts, List<AppTarget> allAppTargets, int shortcutType) {
        float score;
        List<Integer> scoreList = new ArrayList<>();
        if (shortcutType == 2) {
            for (int i = 0; i < matchingShortcuts.size(); i++) {
                int shortcutRank = matchingShortcuts.get(i).getShortcutInfo().getRank();
                if (!scoreList.contains(Integer.valueOf(shortcutRank))) {
                    scoreList.add(Integer.valueOf(shortcutRank));
                }
            }
            Collections.sort(scoreList);
        }
        List<ChooserTarget> chooserTargetList = new ArrayList<>(matchingShortcuts.size());
        for (int i2 = 0; i2 < matchingShortcuts.size(); i2++) {
            ShortcutInfo shortcutInfo = matchingShortcuts.get(i2).getShortcutInfo();
            int indexInAllShortcuts = allShortcuts.indexOf(matchingShortcuts.get(i2));
            if (shortcutType == 3) {
                score = Math.max(1.0f - (indexInAllShortcuts * ZERO_ZERO_ONE), 0.0f);
            } else {
                int rankIndex = scoreList.indexOf(Integer.valueOf(shortcutInfo.getRank()));
                score = Math.max(1.0f - (rankIndex * ZERO_ZERO_ONE), 0.0f);
            }
            Bundle extras = new Bundle();
            extras.putString("android.intent.extra.shortcut.ID", shortcutInfo.getId());
            ChooserTarget chooserTarget = new ChooserTarget(shortcutInfo.getLabel(), null, score, matchingShortcuts.get(i2).getTargetComponent().clone(), extras);
            chooserTargetList.add(chooserTarget);
        }
        Comparator<ChooserTarget> byScore = new Comparator() { // from class: com.android.internal.app.OplusChooserCtsConnection$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return OplusChooserCtsConnection.lambda$convertToChooserTarget$1((ChooserTarget) obj, (ChooserTarget) obj2);
            }
        };
        Collections.sort(chooserTargetList, byScore);
        return chooserTargetList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$convertToChooserTarget$1(ChooserTarget a, ChooserTarget b) {
        return -Float.compare(a.getScore(), b.getScore());
    }
}
