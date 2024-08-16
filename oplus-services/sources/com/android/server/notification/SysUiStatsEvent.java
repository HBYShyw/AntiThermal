package com.android.server.notification;

import android.util.StatsEvent;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SysUiStatsEvent {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class Builder {
        private final StatsEvent.Builder mBuilder;

        protected Builder(StatsEvent.Builder builder) {
            this.mBuilder = builder;
        }

        public StatsEvent build() {
            return this.mBuilder.build();
        }

        public Builder setAtomId(int i) {
            this.mBuilder.setAtomId(i);
            return this;
        }

        public Builder writeInt(int i) {
            this.mBuilder.writeInt(i);
            return this;
        }

        public Builder addBooleanAnnotation(byte b, boolean z) {
            this.mBuilder.addBooleanAnnotation(b, z);
            return this;
        }

        public Builder writeString(String str) {
            this.mBuilder.writeString(str);
            return this;
        }

        public Builder writeBoolean(boolean z) {
            this.mBuilder.writeBoolean(z);
            return this;
        }

        public Builder writeByteArray(byte[] bArr) {
            this.mBuilder.writeByteArray(bArr);
            return this;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class BuilderFactory {
        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder newBuilder() {
            return new Builder(StatsEvent.newBuilder());
        }
    }
}
