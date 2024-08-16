package android.content.pm;

import android.content.pm.PackagePartitions;
import android.os.OplusPropertyList;
import android.os.OplusSystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import com.oplus.os.OplusEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* loaded from: classes.dex */
public class PackagePartitionsExtImpl implements IPackagePartitionsExt {
    private static final String ACTION_COTA_MOUNTED = "cotaMounted";
    private static final String ACTION_SIM_SWITCH_CURRENT = "simSwitchCurrent";
    private static final String ACTION_SIM_SWITCH_FIRST = "simSwitchFirst";
    private static final String ACTION_SIM_SWITCH_SSV = "operatorSwitch";
    private static final int CUSTOM_IMAGES_NOTOVERLAID = 0;
    private static final int CUSTOM_IMAGES_OVERLAID = 1;
    private static final int CUSTOM_IMAGES_UNINITIATED = 2;
    private static final List<String> DIRS_NON_OVERLAIED_APPS_IN_CUSTOM_PARTITIONS;
    private static final List<String> DIRS_SYSTEM_APK_WHEN_SIM_SWITCH;
    private static final HashMap<String, File> MAP_FOR_CUSTOM_IMAGES;
    private static final String NON_OVERLAIED_APPS_FOLDER_NAME = "/non_overlay";
    private static final String PARTITION_NAME_OPEX = "opex";
    private static final String PARTITION_PATH_OPEX = "/mnt/opex";
    private static ArrayList<PackagePartitions.SystemPartition> sNotOverlaidPartitionList;
    private static int sProductOverlaid;
    private static final String PARTITION_NAME_MY_COMPANY = "my_company";
    private static final String PARTITION_NAME_MY_CARRIER = "my_carrier";
    private static final String PARTITION_NAME_MY_REGION = "my_region";
    private static final String PARTITION_NAME_MY_PRELOAD = "my_preload";
    private static final String PARTITION_NAME_MY_BIGBALL = "my_bigball";
    private static final String PARTITION_NAME_MY_PRODUCT = "my_product";
    private static final String PARTITION_NAME_MY_HEYTAP = "my_heytap";
    private static final String PARTITION_NAME_MY_STOCK = "my_stock";
    private static final String PARTITION_NAME_MY_ENGINEERING = "my_engineering";
    private static final ArrayList<PackagePartitions.SystemPartition> CUSTOM_PARTITIONS = new ArrayList<>(Arrays.asList(PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyCompanyDirectory(), 4, PARTITION_NAME_MY_COMPANY, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyCarrierDirectory(), 4, PARTITION_NAME_MY_CARRIER, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyRegionDirectory(), 4, PARTITION_NAME_MY_REGION, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyPreloadDirectory(), 4, PARTITION_NAME_MY_PRELOAD, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyBigballDirectory(), 4, PARTITION_NAME_MY_BIGBALL, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyProductDirectory(), 4, PARTITION_NAME_MY_PRODUCT, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyHeytapDirectory(), 4, PARTITION_NAME_MY_HEYTAP, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyStockDirectory(), 4, PARTITION_NAME_MY_STOCK, true, true), PackagePartitions.getWrapper().newSystemPartitionInstance(OplusEnvironment.getMyEngineeringDirectory(), 4, PARTITION_NAME_MY_ENGINEERING, true, true)));

    static {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        MAP_FOR_CUSTOM_IMAGES = linkedHashMap;
        linkedHashMap.put(PARTITION_NAME_MY_COMPANY, OplusEnvironment.getMyCompanyDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_CARRIER, OplusEnvironment.getMyCarrierDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_BIGBALL, OplusEnvironment.getMyBigballDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_REGION, OplusEnvironment.getMyRegionDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_PRELOAD, OplusEnvironment.getMyPreloadDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_PRODUCT, OplusEnvironment.getMyProductDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_HEYTAP, OplusEnvironment.getMyHeytapDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_STOCK, OplusEnvironment.getMyStockDirectory());
        linkedHashMap.put(PARTITION_NAME_MY_ENGINEERING, OplusEnvironment.getMyEngineeringDirectory());
        DIRS_SYSTEM_APK_WHEN_SIM_SWITCH = new ArrayList(Arrays.asList(OplusEnvironment.getMyCompanyDirectory().getAbsolutePath(), OplusEnvironment.getMyCarrierDirectory().getAbsolutePath(), OplusEnvironment.getMyBigballDirectory().getAbsolutePath(), OplusEnvironment.getMyRegionDirectory().getAbsolutePath(), OplusEnvironment.getMyPreloadDirectory().getAbsolutePath(), OplusEnvironment.getMyProductDirectory().getAbsolutePath(), OplusEnvironment.getMyHeytapDirectory().getAbsolutePath(), OplusEnvironment.getMyStockDirectory().getAbsolutePath(), OplusEnvironment.getMyEngineeringDirectory().getAbsolutePath()));
        DIRS_NON_OVERLAIED_APPS_IN_CUSTOM_PARTITIONS = new ArrayList(Arrays.asList(OplusEnvironment.getMyCompanyDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyCarrierDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyBigballDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyRegionDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyPreloadDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyProductDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyHeytapDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyStockDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME, OplusEnvironment.getMyEngineeringDirectory().getAbsolutePath() + NON_OVERLAIED_APPS_FOLDER_NAME));
        sProductOverlaid = 2;
        sNotOverlaidPartitionList = new ArrayList<>();
    }

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final PackagePartitionsExtImpl INSTANCE = new PackagePartitionsExtImpl();

        private LazyHolder() {
        }
    }

    private PackagePartitionsExtImpl() {
    }

    public static PackagePartitionsExtImpl getInstance(Object obj) {
        return LazyHolder.INSTANCE;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0040, code lost:
    
        r3 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0041, code lost:
    
        r13 = android.content.pm.PackagePartitionsExtImpl.MAP_FOR_CUSTOM_IMAGES.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x004f, code lost:
    
        if (r13.hasNext() == false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0051, code lost:
    
        r14 = r13.next();
        r15 = r14.getKey();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0061, code lost:
    
        if (r12.contains(r15) != false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0063, code lost:
    
        r22 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0070, code lost:
    
        android.util.Log.i("PackagePartitionsExtImpl", r15 + " is not overlaid and scan as before");
        r17 = r14.getValue();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0095, code lost:
    
        r2 = android.content.pm.PackagePartitions.getWrapper().newSystemPartitionInstance(r17, 4, r15, true, true);
        r16 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x009a, code lost:
    
        r24.add(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00b4, code lost:
    
        r3 = r16;
        r0 = r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009e, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x009f, code lost:
    
        r5 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00fa, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:?, code lost:
    
        throw r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00fe, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0100, code lost:
    
        r5.addSuppressed(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0103, code lost:
    
        throw r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a2, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a3, code lost:
    
        r16 = r3;
        r5 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ac, code lost:
    
        r22 = r0;
        r16 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b9, code lost:
    
        r16 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00c2, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00c3, code lost:
    
        r16 = r3;
        r5 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00cb, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00cc, code lost:
    
        r16 = 1;
        r5 = r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int productOverlaid(ArrayList<PackagePartitions.SystemPartition> notOverlaidPartitions) {
        int overlaidStatus;
        BufferedReader br;
        int overlaidStatus2;
        int overlaidStatus3;
        PackagePartitions.SystemPartition systemPartition = null;
        int overlaidStatus4 = 0;
        try {
            br = new BufferedReader(new FileReader("/proc/mounts"));
        } catch (Exception e) {
            e = e;
            overlaidStatus = 0;
        }
        try {
            while (true) {
                try {
                    String readLine = br.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        overlaidStatus3 = overlaidStatus4;
                        break;
                    }
                    String[] fields = line.split(" ");
                    String source = fields[0];
                    String target = fields[1];
                    if (fields.length > 3) {
                        String optionParam = fields[3];
                        if (source.startsWith("overlay-overlay") && target.startsWith("/product/priv-app")) {
                            break;
                        }
                    }
                    overlaidStatus4 = overlaidStatus4;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    overlaidStatus2 = overlaidStatus4;
                }
            }
            br.close();
            overlaidStatus = overlaidStatus3;
        } catch (Exception e2) {
            e = e2;
            overlaidStatus = overlaidStatus2;
            Log.e("PackagePartitionsExtImpl", "parse /proc/mounts fail!", e);
            Log.i("PackagePartitionsExtImpl", " overlaidStatus value is " + overlaidStatus);
            return overlaidStatus;
        }
        Log.i("PackagePartitionsExtImpl", " overlaidStatus value is " + overlaidStatus);
        return overlaidStatus;
    }

    public <T> void adjustGetOrderedPartitions(Function<PackagePartitions.SystemPartition, T> producer, ArrayList<T> originList) {
        ArrayList<PackagePartitions.SystemPartition> simSwitchExtensionPartition = getExtensionSimSwitchPartiton();
        int n = simSwitchExtensionPartition.size();
        for (int i = 0; i < n; i++) {
            T v = producer.apply(simSwitchExtensionPartition.get(i));
            if (v != null) {
                originList.add(v);
            }
        }
        ArrayList<PackagePartitions.SystemPartition> nonOverlaidAppPartitions = getNonOverlaidAppsPartitons();
        int n2 = nonOverlaidAppPartitions.size();
        for (int i2 = 0; i2 < n2; i2++) {
            T v2 = producer.apply(nonOverlaidAppPartitions.get(i2));
            if (v2 != null) {
                originList.add(v2);
            }
        }
        if (!TextUtils.isEmpty(PARTITION_PATH_OPEX)) {
            File opexParentDir = new File(PARTITION_PATH_OPEX);
            File[] opexModuleDirs = opexParentDir.listFiles(new FilenameFilter() { // from class: android.content.pm.PackagePartitionsExtImpl$$ExternalSyntheticLambda0
                @Override // java.io.FilenameFilter
                public final boolean accept(File file, String str) {
                    boolean isDirectory;
                    isDirectory = file.isDirectory();
                    return isDirectory;
                }
            });
            if (opexModuleDirs != null) {
                for (File perModule : opexModuleDirs) {
                    T v3 = producer.apply(PackagePartitions.getWrapper().newSystemPartitionInstance(perModule, 5, PARTITION_NAME_OPEX, true, true));
                    if (v3 != null) {
                        originList.add(v3);
                    }
                }
            }
        }
        if (sProductOverlaid == 2) {
            sProductOverlaid = productOverlaid(sNotOverlaidPartitionList);
        }
        if (sProductOverlaid == 1) {
            Log.i("PackagePartitionsExtImpl", "custom partititions are overlaid;skip parse custom images");
            int n3 = sNotOverlaidPartitionList.size();
            for (int i3 = 0; i3 < n3; i3++) {
                T v4 = producer.apply(sNotOverlaidPartitionList.get(i3));
                if (v4 != null) {
                    originList.add(v4);
                }
            }
            return;
        }
        int n4 = CUSTOM_PARTITIONS.size();
        for (int i4 = 0; i4 < n4; i4++) {
            T v5 = producer.apply(CUSTOM_PARTITIONS.get(i4));
            if (v5 != null) {
                originList.add(v5);
            }
        }
    }

    private static ArrayList<PackagePartitions.SystemPartition> getExtensionSimSwitchPartiton() {
        PackagePartitions.SystemPartition systemPartition;
        String cotaMountStatus;
        String apkScanSystem;
        String str;
        String apkDir;
        String simSwitchSsv;
        String apkScanSystem2;
        String cotaMountStatus2;
        String simSwitchOpta;
        String simSwitchFirstPublicName;
        String simSwitchFirstName;
        String simSwitchFirstName2;
        String simSwitchCarrier;
        ArrayList<PackagePartitions.SystemPartition> simSwitchPartiton = new ArrayList<>();
        PackagePartitions.SystemPartition systemPartition2 = null;
        String simSwitchOpta2 = OplusSystemProperties.get(OplusPropertyList.PROPETY_COTA_MOUNTED_STATE, "0");
        String simSwitchOptb = OplusSystemProperties.get(OplusPropertyList.PROPETY_SIM_SWITCH_APK_SCAN_SYSTEM, "false");
        int partitionScan = 4;
        if (simSwitchOptb.equals("true")) {
            partitionScan = 0;
            Log.d("PackagePartitionsExtImpl", " apkScanSystem is true ");
        }
        if (simSwitchOpta2.equals("1")) {
            File folder = new File(OplusEnvironment.getMyCarrierDirectory().getAbsolutePath());
            if (folder.exists() && folder.isDirectory() && folder.canRead()) {
                systemPartition2 = PackagePartitions.getWrapper().newSystemPartitionInstance(folder, 4, PARTITION_NAME_MY_CARRIER, true, true);
                simSwitchPartiton.add(systemPartition2);
            }
        }
        String simSwitchFirstPublicName2 = OplusSystemProperties.get(OplusPropertyList.PROPETY_SIM_SWITCH_FIRST_PUBLIC, "");
        String simSwitchFirstName3 = OplusSystemProperties.get(OplusPropertyList.PROPETY_SIM_SWITCH_FIRST, "");
        String simSwitchCurrentName = OplusSystemProperties.get(OplusPropertyList.PROPETY_SIM_SWITCH_CURRENT, "");
        String simSwitchOpta3 = OplusSystemProperties.get(OplusPropertyList.PROPERTY_OPERATOR_SERVICE_OPTA, SplitPathManager.DEFAULT);
        String simSwitchOptb2 = OplusSystemProperties.get(OplusPropertyList.PROPERTY_OPERATOR_SERVICE_OPTB, SplitPathManager.DEFAULT);
        String simSwitchSsv2 = OplusSystemProperties.get(OplusPropertyList.PROPETY_SIM_SWITCH_SSV, SplitPathManager.DEFAULT);
        for (String apkDir2 : DIRS_SYSTEM_APK_WHEN_SIM_SWITCH) {
            String partitionName = getPartitionName(apkDir2);
            String simSwitchOpta4 = simSwitchOpta3;
            if (TextUtils.isEmpty(simSwitchFirstPublicName2)) {
                systemPartition = systemPartition2;
                cotaMountStatus = simSwitchOpta2;
                apkScanSystem = simSwitchOptb;
                str = "SimSwitchFirstTime";
                apkDir = apkDir2;
                simSwitchSsv = simSwitchSsv2;
                apkScanSystem2 = simSwitchOptb2;
                cotaMountStatus2 = simSwitchOpta4;
                simSwitchOpta = simSwitchFirstPublicName2;
                simSwitchFirstPublicName = "/apps_extension/";
            } else {
                systemPartition = systemPartition2;
                StringBuilder append = new StringBuilder().append(apkDir2).append("/apps_extension/").append(simSwitchFirstPublicName2);
                cotaMountStatus = simSwitchOpta2;
                String cotaMountStatus3 = File.separator;
                File folder2 = new File(append.append(cotaMountStatus3).append("SimSwitchFirstTime").toString());
                str = "SimSwitchFirstTime";
                if (!folder2.exists() || !folder2.isDirectory() || !folder2.canRead()) {
                    apkScanSystem = simSwitchOptb;
                    apkDir = apkDir2;
                    simSwitchSsv = simSwitchSsv2;
                    apkScanSystem2 = simSwitchOptb2;
                    cotaMountStatus2 = simSwitchOpta4;
                    simSwitchOpta = simSwitchFirstPublicName2;
                    simSwitchFirstPublicName = "/apps_extension/";
                } else {
                    apkDir = apkDir2;
                    simSwitchSsv = simSwitchSsv2;
                    apkScanSystem = simSwitchOptb;
                    apkScanSystem2 = simSwitchOptb2;
                    cotaMountStatus2 = simSwitchOpta4;
                    simSwitchOpta = simSwitchFirstPublicName2;
                    simSwitchFirstPublicName = "/apps_extension/";
                    PackagePartitions.SystemPartition systemPartition3 = PackagePartitions.getWrapper().newSystemPartitionInstance(folder2, partitionScan, partitionName, true, true);
                    simSwitchPartiton.add(systemPartition3);
                    Log.i("PackagePartitionsExtImpl", " SimSwitch first public dir " + folder2.getAbsolutePath());
                    systemPartition = systemPartition3;
                }
            }
            if (TextUtils.isEmpty(simSwitchFirstName3)) {
                simSwitchFirstName = simSwitchFirstName3;
                simSwitchFirstName2 = apkDir;
            } else {
                String apkDir3 = apkDir;
                File folder3 = new File(apkDir3 + simSwitchFirstPublicName + simSwitchFirstName3 + File.separator + str);
                if (!folder3.exists() || !folder3.isDirectory() || !folder3.canRead()) {
                    simSwitchFirstName = simSwitchFirstName3;
                    simSwitchFirstName2 = apkDir3;
                } else {
                    simSwitchFirstName = simSwitchFirstName3;
                    simSwitchFirstName2 = apkDir3;
                    PackagePartitions.SystemPartition systemPartition4 = PackagePartitions.getWrapper().newSystemPartitionInstance(folder3, partitionScan, partitionName, true, true);
                    simSwitchPartiton.add(systemPartition4);
                    Log.i("PackagePartitionsExtImpl", " SimSwitch first dir " + folder3.getAbsolutePath());
                    systemPartition = systemPartition4;
                }
            }
            if (!TextUtils.isEmpty(simSwitchCurrentName)) {
                File folder4 = new File(simSwitchFirstName2 + simSwitchFirstPublicName + simSwitchCurrentName + File.separator + "SimSwitchCurrentTime");
                if (folder4.exists() && folder4.isDirectory() && folder4.canRead()) {
                    PackagePartitions.SystemPartition systemPartition5 = PackagePartitions.getWrapper().newSystemPartitionInstance(folder4, partitionScan, partitionName, true, true);
                    simSwitchPartiton.add(systemPartition5);
                    Log.i("PackagePartitionsExtImpl", " SimSwitch current dir " + folder4.getAbsolutePath());
                    systemPartition = systemPartition5;
                }
            }
            if (SplitPathManager.DEFAULT.equals(cotaMountStatus2) || SplitPathManager.DEFAULT.equals(apkScanSystem2)) {
                simSwitchCarrier = simSwitchSsv;
                if (!SplitPathManager.DEFAULT.equals(simSwitchCarrier)) {
                    File folder5 = new File(simSwitchFirstName2 + simSwitchFirstPublicName + simSwitchCarrier);
                    if (folder5.exists() && folder5.isDirectory() && folder5.canRead()) {
                        PackagePartitions.SystemPartition systemPartition6 = PackagePartitions.getWrapper().newSystemPartitionInstance(folder5, partitionScan, partitionName, true, true);
                        simSwitchPartiton.add(systemPartition6);
                        Log.i("PackagePartitionsExtImpl", " ssv dir " + folder5.getAbsolutePath());
                        systemPartition = systemPartition6;
                    }
                }
            } else {
                File folder6 = new File(simSwitchFirstName2 + simSwitchFirstPublicName + (cotaMountStatus2 + "_" + apkScanSystem2));
                if (folder6.exists() && folder6.isDirectory() && folder6.canRead()) {
                    PackagePartitions.SystemPartition systemPartition7 = PackagePartitions.getWrapper().newSystemPartitionInstance(folder6, partitionScan, partitionName, true, true);
                    simSwitchPartiton.add(systemPartition7);
                    Log.i("PackagePartitionsExtImpl", " old ssv dir " + folder6.getAbsolutePath());
                    systemPartition = systemPartition7;
                }
                simSwitchCarrier = simSwitchSsv;
            }
            simSwitchSsv2 = simSwitchCarrier;
            simSwitchOpta3 = cotaMountStatus2;
            simSwitchOptb2 = apkScanSystem2;
            simSwitchFirstPublicName2 = simSwitchOpta;
            systemPartition2 = systemPartition;
            simSwitchOpta2 = cotaMountStatus;
            simSwitchFirstName3 = simSwitchFirstName;
            simSwitchOptb = apkScanSystem;
        }
        return simSwitchPartiton;
    }

    public static List<String> getExtensionSimSwitchDirs(String action, String carrierName) {
        List<String> outApkPath = new ArrayList<>();
        if (action.equals(ACTION_COTA_MOUNTED)) {
            String cotaMountStatus = OplusSystemProperties.get(OplusPropertyList.PROPETY_COTA_MOUNTED_STATE, "0");
            if (cotaMountStatus.equals("1")) {
                outApkPath.add(OplusEnvironment.getMyCarrierDirectory().getAbsolutePath());
            }
        } else if (action.equals(ACTION_SIM_SWITCH_FIRST)) {
            for (String apkDir : DIRS_SYSTEM_APK_WHEN_SIM_SWITCH) {
                String simSwitchDir = apkDir + "/apps_extension/" + carrierName + File.separator + "SimSwitchFirstTime";
                File folder = new File(simSwitchDir);
                if (folder.exists() && folder.isDirectory() && folder.canRead()) {
                    outApkPath.add(simSwitchDir);
                    Log.i("PackagePartitionsExtImpl", " getExtensionSimSwitchDirs first dir " + folder.getAbsolutePath());
                }
            }
        } else if (action.equals(ACTION_SIM_SWITCH_CURRENT)) {
            for (String apkDir2 : DIRS_SYSTEM_APK_WHEN_SIM_SWITCH) {
                String simSwitchDir2 = apkDir2 + "/apps_extension/" + carrierName + File.separator + "SimSwitchCurrentTime";
                File folder2 = new File(simSwitchDir2);
                if (folder2.exists() && folder2.isDirectory() && folder2.canRead()) {
                    outApkPath.add(simSwitchDir2);
                    Log.i("PackagePartitionsExtImpl", " getExtensionSimSwitchDirs current dir " + folder2.getAbsolutePath());
                }
            }
        } else if (action.equals(ACTION_SIM_SWITCH_SSV)) {
            for (String apkDir3 : DIRS_SYSTEM_APK_WHEN_SIM_SWITCH) {
                String simSwitchDir3 = apkDir3 + "/apps_extension/" + carrierName;
                File folder3 = new File(simSwitchDir3);
                if (folder3.exists() && folder3.isDirectory() && folder3.canRead()) {
                    outApkPath.add(simSwitchDir3);
                    Log.i("PackagePartitionsExtImpl", " getExtensionSimSwitchDirs ssv dir " + folder3.getAbsolutePath());
                }
            }
        }
        return outApkPath;
    }

    private static ArrayList<PackagePartitions.SystemPartition> getNonOverlaidAppsPartitons() {
        ArrayList<PackagePartitions.SystemPartition> nonOverlaidAppsPartitons = new ArrayList<>();
        for (String apkDir : DIRS_NON_OVERLAIED_APPS_IN_CUSTOM_PARTITIONS) {
            File folder = new File(apkDir);
            if (folder.exists() && folder.isDirectory() && folder.canRead()) {
                String partitionName = getPartitionName(apkDir);
                PackagePartitions.SystemPartition systemPartition = PackagePartitions.getWrapper().newSystemPartitionInstance(folder, 4, partitionName, true, true);
                nonOverlaidAppsPartitons.add(systemPartition);
                Log.i("PackagePartitionsExtImpl", " non overlaied app dir " + folder.getAbsolutePath() + ", partitionName " + partitionName);
            }
        }
        return nonOverlaidAppsPartitons;
    }

    private static String getPartitionName(String apkDir) {
        String partitionName = SplitPathManager.DEFAULT;
        for (Map.Entry<String, File> entry : MAP_FOR_CUSTOM_IMAGES.entrySet()) {
            String partitionName2 = entry.getKey();
            partitionName = partitionName2;
            if (apkDir.contains(partitionName)) {
                break;
            }
        }
        return partitionName;
    }
}
