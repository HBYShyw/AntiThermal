package android.view.debug;

import android.os.SystemProperties;
import android.util.Log;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class OplusViewExtraInfoHelper {
    private static final boolean ISNEWJDKVERSION;
    private static final int JDK9 = 9;
    private static final int KILOBYTE = 1024;
    private static final int MEMORY_ALIGNMENT = 8;
    private static final int MEMORY_DEFAULT = 500;
    private static final int MEMORY_INT = 4;
    private static final int MEMORY_LINKEDLIST = 28;
    private static final int MEMORY_OBJECTHEAD = 8;
    private static final int MEMORY_STRING = 40;
    private static final int MEMORY_STRING_JDK9 = 24;
    private static final String TAG = "ViewExtraInfo";
    private static volatile OplusViewExtraInfoHelper sInstance;
    private Set<ExtraInfoNode> mInfoLinkedList = ConcurrentHashMap.newKeySet();
    private int mMaxMemory = 512000;
    private int mCurMemory = 28;

    static {
        ISNEWJDKVERSION = SystemProperties.getInt("java.version", 9) >= 9;
        sInstance = null;
    }

    private OplusViewExtraInfoHelper() {
        Log.d(TAG, "ViewExtraInfoHelper : " + this);
    }

    public static OplusViewExtraInfoHelper resetInstance() {
        OplusViewExtraInfoHelper oplusViewExtraInfoHelper;
        synchronized (OplusViewExtraInfoHelper.class) {
            sInstance = new OplusViewExtraInfoHelper();
            oplusViewExtraInfoHelper = sInstance;
        }
        return oplusViewExtraInfoHelper;
    }

    public static OplusViewExtraInfoHelper getInstance() {
        if (sInstance == null) {
            synchronized (OplusViewExtraInfoHelper.class) {
                if (sInstance == null) {
                    sInstance = new OplusViewExtraInfoHelper();
                }
            }
        }
        return sInstance;
    }

    public void updateMaxMemory(int maxMemory) {
        synchronized (OplusViewExtraInfoHelper.class) {
            if (maxMemory <= 0) {
                return;
            }
            this.mMaxMemory = maxMemory;
        }
    }

    public void pushInfo(int level, String info) {
        synchronized (OplusViewExtraInfoHelper.class) {
            ExtraInfoNode infoNode = new ExtraInfoNode(level, info);
            boolean result = this.mInfoLinkedList.add(infoNode);
            if (!result) {
                Log.d(TAG, "Failed to add infoNode, return");
                return;
            }
            int memory = this.mCurMemory + infoNode.getMemory();
            this.mCurMemory = memory;
            if (memory > this.mMaxMemory * 2) {
                Log.d(TAG, "Reset when pop failure");
                resetInstance();
                return;
            }
            int count = 0;
            while (true) {
                if (this.mCurMemory <= this.mMaxMemory) {
                    break;
                }
                count++;
                popInfo();
                if (count > 10000) {
                    Log.d(TAG, "There may be an infinite loop, break it");
                    break;
                }
            }
        }
    }

    private void popInfo() {
        ExtraInfoNode popNode = null;
        for (ExtraInfoNode infoNode : this.mInfoLinkedList) {
            infoNode.mLevel--;
            if (popNode == null || infoNode.mLevel < popNode.mLevel) {
                popNode = infoNode;
            }
            if (infoNode.mLevel <= 0) {
                removeNode(infoNode);
                return;
            }
        }
        if (popNode != null) {
            removeNode(popNode);
        }
    }

    private void removeNode(ExtraInfoNode infoNode) {
        boolean result = this.mInfoLinkedList.remove(infoNode);
        if (result) {
            this.mCurMemory -= infoNode.getMemory();
        }
    }

    public void dump(String prefix, PrintWriter writer) {
        writer.println(prefix + TAG + ":");
        String innerPrefix = prefix + "  ";
        dumpViewExtraInfo(innerPrefix, writer);
        resetInstance();
    }

    private void dumpViewExtraInfo(String prefix, PrintWriter writer) {
        for (ExtraInfoNode infoNode : this.mInfoLinkedList) {
            writer.println(prefix + infoNode.mInfo);
        }
    }

    /* loaded from: classes.dex */
    public static class ExtraInfoNode {
        private String mInfo;
        private int mLevel;
        private int mMemory;

        public ExtraInfoNode(int level, String info) {
            this.mMemory = 0;
            this.mLevel = 1;
            this.mInfo = "";
            this.mLevel = level;
            this.mInfo = info;
            if (OplusViewExtraInfoHelper.ISNEWJDKVERSION) {
                this.mMemory = ((int) Math.ceil(((((this.mInfo.length() + 1) * 2) + 40) * 1.0d) / 8.0d)) * 8;
            } else {
                this.mMemory = ((int) Math.ceil(((((this.mInfo.length() + 1) * 2) + 56) * 1.0d) / 8.0d)) * 8;
            }
        }

        public int getMemory() {
            return this.mMemory;
        }
    }
}
