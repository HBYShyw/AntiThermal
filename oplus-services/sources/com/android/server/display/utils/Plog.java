package com.android.server.display.utils;

import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class Plog {
    private long mId;

    protected abstract void emit(String str);

    public static Plog createSystemPlog(String str) {
        return new SystemPlog(str);
    }

    public Plog start(String str) {
        this.mId = System.currentTimeMillis();
        write(formatTitle(str));
        return this;
    }

    public Plog logPoint(String str, float f, float f2) {
        write(formatPoint(str, f, f2));
        return this;
    }

    public Plog logCurve(String str, float[] fArr, float[] fArr2) {
        write(formatCurve(str, fArr, fArr2));
        return this;
    }

    private String formatTitle(String str) {
        return "title: " + str;
    }

    private String formatPoint(String str, float f, float f2) {
        return "point: " + str + ": (" + f + "," + f2 + ")";
    }

    private String formatCurve(String str, float[] fArr, float[] fArr2) {
        StringBuilder sb = new StringBuilder();
        sb.append("curve: " + str + ": [");
        int length = fArr.length <= fArr2.length ? fArr.length : fArr2.length;
        for (int i = 0; i < length; i++) {
            sb.append("(" + fArr[i] + "," + fArr2[i] + "),");
        }
        sb.append("]");
        return sb.toString();
    }

    private void write(String str) {
        emit("[PLOG " + this.mId + "] " + str);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class SystemPlog extends Plog {
        private final String mTag;

        public SystemPlog(String str) {
            this.mTag = str;
        }

        @Override // com.android.server.display.utils.Plog
        protected void emit(String str) {
            Slog.d(this.mTag, str);
        }
    }
}
