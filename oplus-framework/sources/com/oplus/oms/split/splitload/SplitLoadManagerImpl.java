package com.oplus.oms.split.splitload;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.SignatureValidator;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.listener.OnSplitLoadListener;
import com.oplus.oms.split.splitreport.SplitReporterManager;
import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import com.oplus.oms.split.splitrequest.OMSRunTimeException;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInfoManager;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import com.oplus.oms.split.splitrequest.SplitInstallUtil;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class SplitLoadManagerImpl extends SplitLoadManager {
    private static final AtomicReference<SplitLoadManager> sReference = new AtomicReference<>();
    private final List<String> mInstallProcessList;

    public static void install(Context context, int splitLoadMode, String currentProcessName, List<String> installProcessList) {
        AtomicReference<SplitLoadManager> atomicReference = sReference;
        if (atomicReference.get() == null) {
            atomicReference.set(create(context, splitLoadMode, currentProcessName, installProcessList));
        }
    }

    public static boolean hasInstance() {
        return sReference.get() != null;
    }

    public static SplitLoadManager getInstance() {
        AtomicReference<SplitLoadManager> atomicReference = sReference;
        if (atomicReference.get() == null) {
            throw new OMSRunTimeException("Have you invoke SplitLoadManagerImpl#install(Context) method?");
        }
        return atomicReference.get();
    }

    private static SplitLoadManager create(Context context, int splitLoadMode, String currentProcessName, List<String> installProcssList) {
        return new SplitLoadManagerImpl(context, splitLoadMode, currentProcessName, installProcssList);
    }

    SplitLoadManagerImpl(Context context, int splitLoadMode, String currentProcessName, List<String> installProcessList) {
        super(context, currentProcessName, splitLoadMode);
        this.mInstallProcessList = installProcessList;
    }

    @Override // com.oplus.oms.split.splitload.SplitLoadManager
    public void injectPathClassloader() {
        if (isInjectPathClassloaderNeeded()) {
            injectClassLoader(getContext().getClassLoader());
        }
        ClassLoader curCl = getContext().getClassLoader();
        if (curCl instanceof SplitDelegateClassloader) {
            ClassNotFoundInterceptor classNotFoundInterceptor = new DefaultClassNotFoundInterceptor(getContext(), getOriginClassLoader(), splitLoadMode());
            ((SplitDelegateClassloader) curCl).setClassNotFoundInterceptor(classNotFoundInterceptor);
        }
    }

    @Override // com.oplus.oms.split.splitload.SplitLoadManager
    public void preloadInstalledSplits(Collection<String> splitNames) {
        SplitLog.d("SplitLoadManager", "preloadInstalledSplits = " + splitNames, new Object[0]);
        synchronized (SplitLoadManagerImpl.class) {
            loadInstalledSplitsInternal(splitNames);
        }
    }

    @Override // com.oplus.oms.split.splitload.SplitLoadManager
    public void getResources(Resources resources) {
        try {
            SplitCompatResourcesLoader.loadResources(getContext(), resources);
        } catch (SplitCompatResourcesException e) {
            SplitLog.e("SplitLoadManager", "getResources error", new Object[0]);
        }
    }

    private Runnable createSplitLoadTask(List<Intent> splitFileIntents, OnSplitLoadListener loadListener) {
        if (splitLoadMode() == 1) {
            return new SplitMultipleModeLoadTaskImpl(this, splitFileIntents, loadListener);
        }
        return new SplitSingleModeLoadTaskImpl(this, splitFileIntents, loadListener);
    }

    private Runnable createSplitUnloadTask(Split split, SplitReporterInfo reporterInfo) {
        if (splitLoadMode() == 1) {
            return new SplitMultipleModeUnloadTaskImpl(getContext(), split, reporterInfo);
        }
        return new SplitSingleModeUnloadTaskImpl(getContext(), split, reporterInfo);
    }

    @Override // com.oplus.oms.split.splitload.SplitLoadManager
    public void unloadSplit(String splitName) {
        Split split = removeLoadedSplits(splitName);
        if (split == null) {
            SplitLog.w("SplitLoadManager", "unloadSplit split not exists", new Object[0]);
            return;
        }
        SplitReporterInfo reporterInfo = SplitReporterManager.getSplitReporterInfo();
        Runnable task = createSplitUnloadTask(split, reporterInfo);
        task.run();
    }

    @Override // com.oplus.oms.split.splitload.SplitLoadManager
    public Set<SplitDexClassLoader> getAllClassLoaders() {
        return SplitApplicationLoaders.getInstance().getAllClassLoaders();
    }

    private void loadInstalledSplitsInternal(Collection<String> splitNames) {
        Collection<SplitInfo> allSplitInfo;
        if (isInstallProcess()) {
            return;
        }
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        List<SplitInfo> splitInfoList = new ArrayList<>();
        if (splitNames == null) {
            allSplitInfo = manager.getAllSplitInfo(getContext());
        } else {
            allSplitInfo = manager.getSplitInfos(getContext(), splitNames);
        }
        if (allSplitInfo != null) {
            splitInfoList.addAll(allSplitInfo);
        }
        if (splitInfoList.isEmpty()) {
            SplitLog.w("SplitLoadManager", "Failed to get Split-Info list!", new Object[0]);
            return;
        }
        List<Intent> splitFileIntents = new ArrayList<>();
        for (SplitInfo splitInfo : splitInfoList) {
            if (!canBeWorkedInThisProcessForSplit(splitInfo)) {
                SplitLog.i("SplitLoadManager", "Split " + splitInfo.getSplitName() + " do not need work in process " + this.mCurrentProcessName, new Object[0]);
            } else if (getLoadedSplitNames().contains(splitInfo.getSplitName())) {
                SplitLog.i("SplitLoadManager", "Split " + splitInfo.getSplitName() + " has been loaded, ignore it!", new Object[0]);
            } else {
                Intent intent = createInstalledSplitFileIntents(getContext(), splitInfo);
                if (intent != null) {
                    splitFileIntents.add(intent);
                }
            }
        }
        if (splitFileIntents.isEmpty()) {
            SplitLog.w("SplitLoadManager", "There are no installed splits!", new Object[0]);
        } else {
            loadNow(splitFileIntents, new OnSplitLoadListener() { // from class: com.oplus.oms.split.splitload.SplitLoadManagerImpl.1
                @Override // com.oplus.oms.split.splitload.listener.OnSplitLoadListener
                public void onCompleted() {
                    SplitLog.i("SplitLoadManager", "SplitLoadManagerImpl:loadInstalledSplitsInternal:onCompleted", new Object[0]);
                }

                @Override // com.oplus.oms.split.splitload.listener.OnSplitLoadListener
                public void onFailed(int errorCode) {
                    SplitLog.i("SplitLoadManager", "SplitLoadManagerImpl:loadInstalledSplitsInternal:onFailed,errorCode:" + errorCode, new Object[0]);
                }
            });
        }
    }

    @Override // com.oplus.oms.split.splitload.SplitLoadManager
    public void loadNow(List<Intent> splitFileIntents, OnSplitLoadListener loadListener) {
        SplitLog.d("SplitLoadManager", "loadNow ", new Object[0]);
        Runnable runnable = createSplitLoadTask(splitFileIntents, loadListener);
        runnable.run();
    }

    public static Intent getInstalledSplitIntent(Context context, String splitName) {
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        SplitInfo splitInfo = manager.getSplitInfo(context, splitName);
        if (splitInfo == null) {
            return null;
        }
        return createInstalledSplitFileIntents(context, splitInfo);
    }

    private boolean isInjectPathClassloaderNeeded() {
        boolean exist = getContext().getClassLoader() instanceof SplitDelegateClassloader;
        return !exist;
    }

    private static Intent createInstalledSplitFileIntents(Context context, SplitInfo splitInfo) {
        File[] splitLibDirs;
        try {
            SplitInfo.LibData libData = splitInfo.getPrimaryLibData(context);
            File splitLibDir = null;
            boolean isLibExtracted = false;
            String splitName = splitInfo.getSplitName();
            if (libData != null) {
                int splitVersion = SplitInstallUtil.getSplitInstallVersion(context, splitName);
                splitLibDir = SplitPathManager.require().getSplitLibDir(splitName, libData.getAbi(), splitVersion);
                if (splitLibDir.exists() && (splitLibDirs = splitLibDir.listFiles(new FileFilter() { // from class: com.oplus.oms.split.splitload.SplitLoadManagerImpl$$ExternalSyntheticLambda0
                    @Override // java.io.FileFilter
                    public final boolean accept(File file) {
                        return SplitLoadManagerImpl.lambda$createInstalledSplitFileIntents$0(file);
                    }
                })) != null) {
                    int count = splitLibDirs.length;
                    isLibExtracted = count > 0;
                    SplitLog.i("SplitLoadManager", "createInstalledSplitFileIntents count:" + count, new Object[0]);
                }
            }
            if (libData != null && !isLibExtracted) {
                SplitLog.i("SplitLoadManager", "split " + splitName + " not installed,lib need extract " + splitLibDir, new Object[0]);
                return null;
            }
            Intent splitFileIntent = createLastInstalledSplitFileIntent(context, splitLibDir, splitInfo);
            return splitFileIntent;
        } catch (IOException e) {
            SplitLog.e("SplitLoadManager", "createInstalledSplitFileIntents error " + e.getMessage(), new Object[0]);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$createInstalledSplitFileIntents$0(File pathname) {
        SplitLog.d("SplitLoadManager", "accept:" + pathname.getName(), new Object[0]);
        return pathname.getName().endsWith(SplitConstants.DOT_SO);
    }

    private boolean canBeWorkedInThisProcessForSplit(SplitInfo splitInfo) {
        List<String> workProcesses = splitInfo.getWorkProcesses();
        SplitLog.i("SplitLoadManager", "canBeWorkedInThisProcessForSplit:" + workProcesses + ",currentProcessName:" + this.mCurrentProcessName, new Object[0]);
        if (workProcesses != null && !workProcesses.isEmpty()) {
            return workProcesses.contains(this.mCurrentProcessName);
        }
        return true;
    }

    private boolean isInstallProcess() {
        boolean isInstallProcess = false;
        List<String> list = this.mInstallProcessList;
        if (list != null && list.contains(this.mCurrentProcessName)) {
            isInstallProcess = true;
        }
        SplitLog.d("SplitLoadManager", "isInstallProcess = " + isInstallProcess, new Object[0]);
        return isInstallProcess;
    }

    private static Intent createLastInstalledSplitFileIntent(Context context, File splitLibDir, SplitInfo splitInfo) {
        String splitName = splitInfo.getSplitName();
        int currentSplitVersion = SplitInstallUtil.getSplitInstallVersion(context, splitName);
        if (currentSplitVersion == -1) {
            currentSplitVersion = getSplitVersionFromFile(splitName);
        }
        if (currentSplitVersion == -1) {
            SplitLog.e("SplitLoadManager", "split apk has not installed, split Name = " + splitName, new Object[0]);
            return null;
        }
        File splitApkDir = SplitPathManager.require().getSplitApkDir(splitName, currentSplitVersion, false);
        File splitApk = new File(splitApkDir, splitName + "-" + SplitConstants.MASTER + SplitConstants.DOT_APK);
        if (!splitApk.exists()) {
            SplitLog.i("SplitLoadManager", "split apk installed error, split Name = " + splitName, new Object[0]);
        }
        if (!FileUtil.isLegalFile(splitApk) || !SignatureValidator.validateSplit(context, splitApk)) {
            SplitLog.w("SplitLoadManager", "split apk file is not legal or sign error", new Object[0]);
            FileUtil.deleteDir(splitApk.getParentFile(), true);
            cleanUpFileSplitVersion(splitName);
            return null;
        }
        SplitLog.d("SplitLoadManager", "createLastInstalledSplitFileIntent " + splitApk.getAbsolutePath(), new Object[0]);
        ArrayList<String> addedDexPaths = null;
        if (splitInfo.hasDex()) {
            addedDexPaths = new ArrayList<>();
            addedDexPaths.add(splitApk.getAbsolutePath());
            splitApk.setReadOnly();
        }
        Intent splitFileIntent = new Intent();
        splitFileIntent.putExtra(SplitConstants.KEY_SPLIT_NAME, splitName);
        splitFileIntent.putExtra(SplitConstants.KEY_APK, splitApk.getAbsolutePath());
        if (splitLibDir != null) {
            splitFileIntent.putExtra(SplitConstants.KEY_NATIVE_LIB_DIR, splitLibDir.getAbsolutePath());
            splitLibDir.setReadOnly();
        }
        if (addedDexPaths != null) {
            splitFileIntent.putStringArrayListExtra(SplitConstants.KEY_ADDED_DEX, addedDexPaths);
        }
        splitFileIntent.putExtra(SplitConstants.KEY_SPLIT_VERSION, currentSplitVersion);
        HashMap<String, String> infoForSplit = splitInfo.getInfoForSplit();
        if (infoForSplit != null && !TextUtils.isEmpty(infoForSplit.get("independent")) && "true".equals(infoForSplit.get("independent"))) {
            splitFileIntent.putExtra(SplitConstants.KEY_INDEPENDENT_SPLIT, true);
        }
        SplitLog.d("SplitLoadManager", splitName + " will work in process current process,apk:" + splitApk.getAbsolutePath() + ",splitLibDir " + splitLibDir, new Object[0]);
        return splitFileIntent;
    }

    private void injectClassLoader(ClassLoader originalClassloader) {
        try {
            SplitDelegateClassloader.inject(originalClassloader, getBaseContext());
        } catch (Exception e) {
            SplitLog.e("SplitLoadManager", "Failed to hook PathClassloader", e);
        }
    }

    private Context getBaseContext() {
        Context ctx = getContext();
        while (ctx instanceof ContextWrapper) {
            ctx = ((ContextWrapper) ctx).getBaseContext();
        }
        return ctx;
    }

    private static int getSplitVersionFromFile(String splitName) {
        FileInputStream inputStream;
        int splitVersionCode = -1;
        File splitDir = SplitPathManager.require().getSplitDir(splitName);
        File versionCodeFile = new File(splitDir, "SplitCopier.lock");
        if (!versionCodeFile.exists()) {
            return -1;
        }
        try {
            inputStream = new FileInputStream(versionCodeFile);
        } catch (FileNotFoundException e) {
            SplitLog.e("SplitLoadManager", "SplitCopier.lock no found", e);
        } catch (IOException e2) {
            SplitLog.e("SplitLoadManager", "get split version code error", e2);
        }
        try {
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            try {
                BufferedReader fileReader = new BufferedReader(streamReader);
                try {
                    splitVersionCode = fileReader.read();
                    fileReader.close();
                    streamReader.close();
                    inputStream.close();
                    SplitLog.d("SplitLoadManager", "getSplitVersionFromFile splitName = " + splitName + ", splitVersionCode = " + splitVersionCode, new Object[0]);
                    return splitVersionCode;
                } finally {
                }
            } finally {
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static void cleanUpFileSplitVersion(String splitName) {
        File splitDir = SplitPathManager.require().getSplitDir(splitName);
        File versionCodeFile = new File(splitDir, "SplitCopier.lock");
        try {
            FileOutputStream outputStream = new FileOutputStream(versionCodeFile);
            try {
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                try {
                    BufferedWriter fileWriter = new BufferedWriter(streamWriter);
                    try {
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                        streamWriter.close();
                        outputStream.close();
                    } finally {
                    }
                } finally {
                }
            } catch (Throwable th) {
                try {
                    outputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (FileNotFoundException e) {
            SplitLog.e("SplitLoadManager", "SplitCopier.lock no found", e);
        } catch (IOException e2) {
            SplitLog.e("SplitLoadManager", "clean up split version code error", e2);
        }
    }
}
