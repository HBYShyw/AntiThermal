package com.android.server.tv.tunerresourcemanager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CasResource {
    private int mAvailableSessionNum;
    private int mMaxSessionNum;
    private Map<Integer, Integer> mOwnerClientIdsToSessionNum = new HashMap();
    private final int mSystemId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CasResource(Builder builder) {
        this.mSystemId = builder.mSystemId;
        int i = builder.mMaxSessionNum;
        this.mMaxSessionNum = i;
        this.mAvailableSessionNum = i;
    }

    public int getSystemId() {
        return this.mSystemId;
    }

    public int getMaxSessionNum() {
        return this.mMaxSessionNum;
    }

    public int getUsedSessionNum() {
        return this.mMaxSessionNum - this.mAvailableSessionNum;
    }

    public boolean isFullyUsed() {
        return this.mAvailableSessionNum == 0;
    }

    public void updateMaxSessionNum(int i) {
        this.mAvailableSessionNum = Math.max(0, this.mAvailableSessionNum + (i - this.mMaxSessionNum));
        this.mMaxSessionNum = i;
    }

    public void setOwner(int i) {
        this.mOwnerClientIdsToSessionNum.put(Integer.valueOf(i), Integer.valueOf(this.mOwnerClientIdsToSessionNum.get(Integer.valueOf(i)) == null ? 1 : this.mOwnerClientIdsToSessionNum.get(Integer.valueOf(i)).intValue() + 1));
        this.mAvailableSessionNum--;
    }

    public void removeOwner(int i) {
        this.mAvailableSessionNum += this.mOwnerClientIdsToSessionNum.get(Integer.valueOf(i)).intValue();
        this.mOwnerClientIdsToSessionNum.remove(Integer.valueOf(i));
    }

    public Set<Integer> getOwnerClientIds() {
        return this.mOwnerClientIdsToSessionNum.keySet();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CasResource[systemId=");
        sb.append(this.mSystemId);
        sb.append(", isFullyUsed=");
        sb.append(this.mAvailableSessionNum == 0);
        sb.append(", maxSessionNum=");
        sb.append(this.mMaxSessionNum);
        sb.append(", ownerClients=");
        sb.append(ownersMapToString());
        sb.append("]");
        return sb.toString();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Builder {
        protected int mMaxSessionNum;
        private int mSystemId;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i) {
            this.mSystemId = i;
        }

        public Builder maxSessionNum(int i) {
            this.mMaxSessionNum = i;
            return this;
        }

        public CasResource build() {
            return new CasResource(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String ownersMapToString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Integer> it = this.mOwnerClientIdsToSessionNum.keySet().iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            sb.append(" clientId=");
            sb.append(intValue);
            sb.append(", owns session num=");
            sb.append(this.mOwnerClientIdsToSessionNum.get(Integer.valueOf(intValue)));
            sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }
}
