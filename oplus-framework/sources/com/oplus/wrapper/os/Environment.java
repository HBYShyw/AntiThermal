package com.oplus.wrapper.os;

import android.os.Environment;
import java.io.File;

/* loaded from: classes.dex */
public class Environment {
    public static File getVendorDirectory() {
        return android.os.Environment.getVendorDirectory();
    }

    public static File getDataMiscDirectory() {
        return android.os.Environment.getDataMiscDirectory();
    }

    /* loaded from: classes.dex */
    public static class UserEnvironment {
        private final Environment.UserEnvironment mUserEnvironment;

        public UserEnvironment(int userId) {
            this.mUserEnvironment = new Environment.UserEnvironment(userId);
        }

        public File getExternalStorageDirectory() {
            return this.mUserEnvironment.getExternalStorageDirectory();
        }
    }
}
