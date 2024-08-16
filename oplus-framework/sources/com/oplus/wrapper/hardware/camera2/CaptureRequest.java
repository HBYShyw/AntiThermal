package com.oplus.wrapper.hardware.camera2;

import android.hardware.camera2.CaptureRequest;

/* loaded from: classes.dex */
public class CaptureRequest {

    /* loaded from: classes.dex */
    public static final class Key<T> {
        private final CaptureRequest.Key<T> mKey;

        public Key(String name, Class<T> type, long vendorId) {
            this.mKey = new CaptureRequest.Key<>(name, type, vendorId);
        }

        public Key(String name, Class<T> type) {
            this.mKey = new CaptureRequest.Key<>(name, type);
        }

        public CaptureRequest.Key<T> getKey() {
            return this.mKey;
        }
    }
}
