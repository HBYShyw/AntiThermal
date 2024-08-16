package com.android.server.wm;

import android.text.TextUtils;
import android.util.Slog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class OplusCounter {
    private static final String TAG = "OplusCounter";
    private EffectiveInteger count;
    private final HashMap<String, EffectiveInteger> mMap;
    private String name;

    public OplusCounter(String str, int i) {
        HashMap<String, EffectiveInteger> hashMap = new HashMap<>();
        this.mMap = hashMap;
        this.name = str;
        EffectiveInteger effectiveInteger = new EffectiveInteger(i);
        this.count = effectiveInteger;
        hashMap.put(this.name, effectiveInteger);
    }

    public OplusCounter(OplusCounter oplusCounter) {
        this.mMap = new HashMap<>();
    }

    public OplusCounter() {
        this.mMap = new HashMap<>();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static final class EffectiveInteger {
        private int val;

        public EffectiveInteger(int i) {
            this.val = i;
        }

        public int getInt() {
            return this.val;
        }

        public void setInt(int i) {
            this.val = i;
        }
    }

    public boolean containsKey(String str) {
        boolean containsKey;
        synchronized (this.mMap) {
            containsKey = this.mMap.containsKey(str);
        }
        return containsKey;
    }

    public boolean plus(String str) {
        if (TextUtils.isEmpty(str)) {
            Slog.w(TAG, "plus: name is null");
            return false;
        }
        synchronized (this.mMap) {
            EffectiveInteger effectiveInteger = new EffectiveInteger(1);
            EffectiveInteger put = this.mMap.put(str, effectiveInteger);
            if (put != null) {
                effectiveInteger.setInt(put.getInt() + 1);
            }
        }
        return true;
    }

    public boolean minus(String str) {
        if (TextUtils.isEmpty(str)) {
            Slog.w(TAG, "minus: name is null");
            return false;
        }
        synchronized (this.mMap) {
            EffectiveInteger effectiveInteger = this.mMap.get(str);
            if (effectiveInteger != null) {
                if (effectiveInteger.getInt() == 0) {
                    Slog.w(TAG, "minus: Val is 0");
                    return false;
                }
                effectiveInteger.setInt(effectiveInteger.getInt() - 1);
                if (effectiveInteger.getInt() == 0) {
                    this.mMap.remove(str);
                }
            }
            return true;
        }
    }

    public boolean clear() {
        synchronized (this.mMap) {
            this.mMap.clear();
        }
        return true;
    }

    public boolean remove(String str) {
        if (TextUtils.isEmpty(str)) {
            Slog.w(TAG, "resetValue: name is null");
            return false;
        }
        synchronized (this.mMap) {
            if (this.mMap.containsKey(str)) {
                this.mMap.remove(str);
                return true;
            }
            Slog.w(TAG, "remove: name is not in map");
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Counter{ ");
        synchronized (this.mMap) {
            for (Map.Entry<String, EffectiveInteger> entry : this.mMap.entrySet()) {
                sb.append(entry.getKey() + "(" + entry.getValue().getInt() + ") ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public int size() {
        int size;
        synchronized (this.mMap) {
            size = this.mMap.size();
        }
        return size;
    }

    public int size(String str) {
        synchronized (this.mMap) {
            EffectiveInteger effectiveInteger = this.mMap.get(str);
            if (effectiveInteger == null) {
                return 0;
            }
            return effectiveInteger.getInt();
        }
    }

    public int getSingleMaxSize() {
        int i;
        synchronized (this.mMap) {
            Iterator<Map.Entry<String, EffectiveInteger>> it = this.mMap.entrySet().iterator();
            i = 0;
            while (it.hasNext()) {
                i = Math.max(it.next().getValue().getInt(), i);
            }
        }
        return i;
    }
}
