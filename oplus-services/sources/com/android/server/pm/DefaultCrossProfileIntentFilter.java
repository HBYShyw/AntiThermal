package com.android.server.pm;

import android.content.IntentFilter;
import com.android.internal.annotations.Immutable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@Immutable
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DefaultCrossProfileIntentFilter {
    public final int direction;
    public final WatchedIntentFilter filter;
    public final int flags;
    public final boolean letsPersonalDataIntoProfile;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface Direction {
        public static final int TO_PARENT = 0;
        public static final int TO_PROFILE = 1;
    }

    private DefaultCrossProfileIntentFilter(WatchedIntentFilter watchedIntentFilter, int i, int i2, boolean z) {
        Objects.requireNonNull(watchedIntentFilter);
        this.filter = watchedIntentFilter;
        this.flags = i;
        this.direction = i2;
        this.letsPersonalDataIntoProfile = z;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static final class Builder {
        private int mDirection;
        private WatchedIntentFilter mFilter = new WatchedIntentFilter();
        private int mFlags;
        private boolean mLetsPersonalDataIntoProfile;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i, int i2, boolean z) {
            this.mDirection = i;
            this.mFlags = i2;
            this.mLetsPersonalDataIntoProfile = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder addAction(String str) {
            this.mFilter.addAction(str);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder addCategory(String str) {
            this.mFilter.addCategory(str);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder addDataType(String str) {
            try {
                this.mFilter.addDataType(str);
            } catch (IntentFilter.MalformedMimeTypeException unused) {
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder addDataScheme(String str) {
            this.mFilter.addDataScheme(str);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public DefaultCrossProfileIntentFilter build() {
            return new DefaultCrossProfileIntentFilter(this.mFilter, this.mFlags, this.mDirection, this.mLetsPersonalDataIntoProfile);
        }
    }
}
