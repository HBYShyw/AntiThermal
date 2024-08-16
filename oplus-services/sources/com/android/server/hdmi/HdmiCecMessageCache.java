package com.android.server.hdmi;

import android.util.FastImmutableArraySet;
import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class HdmiCecMessageCache {
    private static final FastImmutableArraySet<Integer> CACHEABLE_OPCODES = new FastImmutableArraySet<>(new Integer[]{71, 132, 135, 158});
    private final SparseArray<SparseArray<HdmiCecMessage>> mCache = new SparseArray<>();

    public HdmiCecMessage getMessage(int i, int i2) {
        SparseArray<HdmiCecMessage> sparseArray = this.mCache.get(i);
        if (sparseArray == null) {
            return null;
        }
        return sparseArray.get(i2);
    }

    public void flushMessagesFrom(int i) {
        this.mCache.remove(i);
    }

    public void flushAll() {
        this.mCache.clear();
    }

    public void cacheMessage(HdmiCecMessage hdmiCecMessage) {
        int opcode = hdmiCecMessage.getOpcode();
        if (isCacheable(opcode)) {
            int source = hdmiCecMessage.getSource();
            SparseArray<HdmiCecMessage> sparseArray = this.mCache.get(source);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                this.mCache.put(source, sparseArray);
            }
            sparseArray.put(opcode, hdmiCecMessage);
        }
    }

    private boolean isCacheable(int i) {
        return CACHEABLE_OPCODES.contains(Integer.valueOf(i));
    }
}
