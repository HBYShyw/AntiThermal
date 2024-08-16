package com.oplus.cust;

import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCfgFilePolicyExtImpl implements IOplusCfgFilePolicyExt {

    /* loaded from: classes.dex */
    private static class LazyHolder {
        private static final IOplusCfgFilePolicyExt INSTANCE = new OplusCfgFilePolicyExtImpl();

        private LazyHolder() {
        }
    }

    private OplusCfgFilePolicyExtImpl() {
    }

    public static IOplusCfgFilePolicyExt getInstance(Object obj) {
        return LazyHolder.INSTANCE;
    }

    public List<File> getCfgFileList(String fileName, String pathPrefix, int slotId) {
        return OplusCfgFilePolicy.getCfgFileList(fileName, pathPrefix, slotId);
    }

    public File getCfgTopPriorityFile(String fileName, String pathPrefix, int slotId) {
        return OplusCfgFilePolicy.getCfgTopPriorityFile(fileName, pathPrefix, slotId);
    }

    public List<String> getCfgLevelList(String pathPrefix, int slotId) {
        return OplusCfgFilePolicy.getCfgLevelList(pathPrefix, slotId);
    }

    public String getCarrierId(int slotId) {
        return OplusCfgFilePolicy.getCarrierId(slotId);
    }
}
