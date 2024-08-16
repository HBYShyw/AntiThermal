package com.oplus.wrapper.content.pm;

import android.content.pm.PackageParser;
import java.io.File;

/* loaded from: classes.dex */
public class PackageParser {
    private final android.content.pm.PackageParser mPackageParser = new android.content.pm.PackageParser();

    public Package parsePackage(File packageFile, int flags, boolean useCaches) throws PackageParserException {
        try {
            PackageParser.Package parsePackage = this.mPackageParser.parsePackage(packageFile, flags, useCaches);
            if (parsePackage == null) {
                return null;
            }
            return new Package(parsePackage);
        } catch (PackageParser.PackageParserException parserException) {
            throw new PackageParserException(parserException);
        }
    }

    /* loaded from: classes.dex */
    public static class PackageParserException extends Exception {
        private PackageParserException(PackageParser.PackageParserException parserException) {
            super((Throwable) parserException);
        }
    }

    /* loaded from: classes.dex */
    public static final class Package {
        private final PackageParser.Package mPackage;

        Package(PackageParser.Package pkg) {
            this.mPackage = pkg;
        }

        public String getPackageName() {
            return this.mPackage.packageName;
        }

        public String getVersionName() {
            return this.mPackage.mVersionName;
        }

        public int getVersionCode() {
            return this.mPackage.mVersionCode;
        }

        public android.content.pm.ApplicationInfo getApplicationInfo() {
            return this.mPackage.applicationInfo;
        }
    }
}
