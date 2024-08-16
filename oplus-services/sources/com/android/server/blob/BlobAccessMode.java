package com.android.server.blob;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.os.Binder;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.Base64;
import android.util.DebugUtils;
import android.util.IndentingPrintWriter;
import com.android.internal.util.XmlUtils;
import com.android.server.LocalServices;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class BlobAccessMode {
    public static final int ACCESS_TYPE_ALLOWLIST = 8;
    public static final int ACCESS_TYPE_PRIVATE = 1;
    public static final int ACCESS_TYPE_PUBLIC = 2;
    public static final int ACCESS_TYPE_SAME_SIGNATURE = 4;
    private int mAccessType = 1;
    private final ArraySet<PackageIdentifier> mAllowedPackages = new ArraySet<>();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface AccessType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void allow(BlobAccessMode blobAccessMode) {
        if ((blobAccessMode.mAccessType & 8) != 0) {
            this.mAllowedPackages.addAll((ArraySet<? extends PackageIdentifier>) blobAccessMode.mAllowedPackages);
        }
        this.mAccessType = blobAccessMode.mAccessType | this.mAccessType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void allowPublicAccess() {
        this.mAccessType |= 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void allowSameSignatureAccess() {
        this.mAccessType |= 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void allowPackageAccess(String str, byte[] bArr) {
        this.mAccessType |= 8;
        this.mAllowedPackages.add(PackageIdentifier.create(str, bArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPublicAccessAllowed() {
        return (this.mAccessType & 2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSameSignatureAccessAllowed() {
        return (this.mAccessType & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPackageAccessAllowed(String str, byte[] bArr) {
        if ((this.mAccessType & 8) == 0) {
            return false;
        }
        return this.mAllowedPackages.contains(PackageIdentifier.create(str, bArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAccessAllowedForCaller(Context context, String str, int i, int i2) {
        int i3 = this.mAccessType;
        if ((i3 & 2) != 0) {
            return true;
        }
        if ((i3 & 4) != 0 && checkSignatures(i, i2)) {
            return true;
        }
        if ((this.mAccessType & 8) != 0) {
            PackageManager packageManager = context.createContextAsUser(UserHandle.of(UserHandle.getUserId(i)), 0).getPackageManager();
            for (int i4 = 0; i4 < this.mAllowedPackages.size(); i4++) {
                PackageIdentifier valueAt = this.mAllowedPackages.valueAt(i4);
                if (valueAt.packageName.equals(str) && packageManager.hasSigningCertificate(str, valueAt.certificate, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkSignatures(int i, int i2) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).checkUidSignaturesForAllUsers(i, i2) == 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAccessType() {
        return this.mAccessType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAllowedPackagesCount() {
        return this.mAllowedPackages.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("accessType: " + DebugUtils.flagsToString(BlobAccessMode.class, "ACCESS_TYPE_", this.mAccessType));
        indentingPrintWriter.print("Explicitly allowed pkgs:");
        if (this.mAllowedPackages.isEmpty()) {
            indentingPrintWriter.println(" (Empty)");
            return;
        }
        indentingPrintWriter.increaseIndent();
        int size = this.mAllowedPackages.size();
        for (int i = 0; i < size; i++) {
            indentingPrintWriter.println(this.mAllowedPackages.valueAt(i).toString());
        }
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeToXml(XmlSerializer xmlSerializer) throws IOException {
        XmlUtils.writeIntAttribute(xmlSerializer, "t", this.mAccessType);
        int size = this.mAllowedPackages.size();
        for (int i = 0; i < size; i++) {
            xmlSerializer.startTag(null, "wl");
            PackageIdentifier valueAt = this.mAllowedPackages.valueAt(i);
            XmlUtils.writeStringAttribute(xmlSerializer, "p", valueAt.packageName);
            XmlUtils.writeByteArrayAttribute(xmlSerializer, "ct", valueAt.certificate);
            xmlSerializer.endTag(null, "wl");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BlobAccessMode createFromXml(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        BlobAccessMode blobAccessMode = new BlobAccessMode();
        blobAccessMode.mAccessType = XmlUtils.readIntAttribute(xmlPullParser, "t");
        int depth = xmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            if ("wl".equals(xmlPullParser.getName())) {
                blobAccessMode.allowPackageAccess(XmlUtils.readStringAttribute(xmlPullParser, "p"), XmlUtils.readByteArrayAttribute(xmlPullParser, "ct"));
            }
        }
        return blobAccessMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PackageIdentifier {
        public final byte[] certificate;
        public final String packageName;

        private PackageIdentifier(String str, byte[] bArr) {
            this.packageName = str;
            this.certificate = bArr;
        }

        public static PackageIdentifier create(String str, byte[] bArr) {
            return new PackageIdentifier(str, bArr);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof PackageIdentifier)) {
                return false;
            }
            PackageIdentifier packageIdentifier = (PackageIdentifier) obj;
            return this.packageName.equals(packageIdentifier.packageName) && Arrays.equals(this.certificate, packageIdentifier.certificate);
        }

        public int hashCode() {
            return Objects.hash(this.packageName, Integer.valueOf(Arrays.hashCode(this.certificate)));
        }

        public String toString() {
            return "[" + this.packageName + ", " + Base64.encodeToString(this.certificate, 2) + "]";
        }
    }
}
