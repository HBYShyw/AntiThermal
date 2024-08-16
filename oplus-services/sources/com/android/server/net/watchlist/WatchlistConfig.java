package com.android.server.net.watchlist;

import android.os.FileUtils;
import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.HexDump;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class WatchlistConfig {
    private static final String NETWORK_WATCHLIST_DB_FOR_TEST_PATH = "/data/misc/network_watchlist/network_watchlist_for_test.xml";
    private static final String NETWORK_WATCHLIST_DB_PATH = "/data/misc/network_watchlist/network_watchlist.xml";
    private static final String TAG = "WatchlistConfig";
    private static final WatchlistConfig sInstance = new WatchlistConfig();
    private volatile CrcShaDigests mDomainDigests;
    private volatile CrcShaDigests mIpDigests;
    private boolean mIsSecureConfig;
    private File mXmlFile;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class XmlTags {
        private static final String CRC32_DOMAIN = "crc32-domain";
        private static final String CRC32_IP = "crc32-ip";
        private static final String HASH = "hash";
        private static final String SHA256_DOMAIN = "sha256-domain";
        private static final String SHA256_IP = "sha256-ip";
        private static final String WATCHLIST_CONFIG = "watchlist-config";

        private XmlTags() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CrcShaDigests {
        public final HarmfulCrcs crc32s;
        public final HarmfulDigests sha256Digests;

        CrcShaDigests(HarmfulCrcs harmfulCrcs, HarmfulDigests harmfulDigests) {
            this.crc32s = harmfulCrcs;
            this.sha256Digests = harmfulDigests;
        }
    }

    public static WatchlistConfig getInstance() {
        return sInstance;
    }

    private WatchlistConfig() {
        this(new File(NETWORK_WATCHLIST_DB_PATH));
    }

    @VisibleForTesting
    protected WatchlistConfig(File file) {
        this.mIsSecureConfig = true;
        this.mXmlFile = file;
        reloadConfig();
    }

    public void reloadConfig() {
        char c;
        if (this.mXmlFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(this.mXmlFile);
                try {
                    List<byte[]> arrayList = new ArrayList<>();
                    List<byte[]> arrayList2 = new ArrayList<>();
                    List<byte[]> arrayList3 = new ArrayList<>();
                    List<byte[]> arrayList4 = new ArrayList<>();
                    XmlPullParser newPullParser = Xml.newPullParser();
                    newPullParser.setInput(fileInputStream, StandardCharsets.UTF_8.name());
                    newPullParser.nextTag();
                    newPullParser.require(2, null, "watchlist-config");
                    while (newPullParser.nextTag() == 2) {
                        String name = newPullParser.getName();
                        switch (name.hashCode()) {
                            case -1862636386:
                                if (name.equals("crc32-domain")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -14835926:
                                if (name.equals("sha256-domain")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 835385997:
                                if (name.equals("sha256-ip")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 1718657537:
                                if (name.equals("crc32-ip")) {
                                    c = 1;
                                    break;
                                }
                                break;
                        }
                        c = 65535;
                        if (c == 0) {
                            parseHashes(newPullParser, name, arrayList);
                        } else if (c == 1) {
                            parseHashes(newPullParser, name, arrayList3);
                        } else if (c == 2) {
                            parseHashes(newPullParser, name, arrayList2);
                        } else if (c == 3) {
                            parseHashes(newPullParser, name, arrayList4);
                        } else {
                            Log.w(TAG, "Unknown element: " + newPullParser.getName());
                            XmlUtils.skipCurrentTag(newPullParser);
                        }
                    }
                    newPullParser.require(3, null, "watchlist-config");
                    this.mDomainDigests = new CrcShaDigests(new HarmfulCrcs(arrayList), new HarmfulDigests(arrayList2));
                    this.mIpDigests = new CrcShaDigests(new HarmfulCrcs(arrayList3), new HarmfulDigests(arrayList4));
                    Log.i(TAG, "Reload watchlist done");
                    fileInputStream.close();
                } catch (Throwable th) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException | IllegalStateException | IndexOutOfBoundsException | NullPointerException | NumberFormatException | XmlPullParserException e) {
                Slog.e(TAG, "Failed parsing xml", e);
            }
        }
    }

    private void parseHashes(XmlPullParser xmlPullParser, String str, List<byte[]> list) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, null, str);
        while (xmlPullParser.nextTag() == 2) {
            xmlPullParser.require(2, null, "hash");
            byte[] hexStringToByteArray = HexDump.hexStringToByteArray(xmlPullParser.nextText());
            xmlPullParser.require(3, null, "hash");
            list.add(hexStringToByteArray);
        }
        xmlPullParser.require(3, null, str);
    }

    public boolean containsDomain(String str) {
        CrcShaDigests crcShaDigests = this.mDomainDigests;
        if (crcShaDigests == null) {
            return false;
        }
        if (!crcShaDigests.crc32s.contains(getCrc32(str))) {
            return false;
        }
        return crcShaDigests.sha256Digests.contains(getSha256(str));
    }

    public boolean containsIp(String str) {
        CrcShaDigests crcShaDigests = this.mIpDigests;
        if (crcShaDigests == null) {
            return false;
        }
        if (!crcShaDigests.crc32s.contains(getCrc32(str))) {
            return false;
        }
        return crcShaDigests.sha256Digests.contains(getSha256(str));
    }

    private int getCrc32(String str) {
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        return (int) crc32.getValue();
    }

    private byte[] getSha256(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            messageDigest.update(str.getBytes());
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public boolean isConfigSecure() {
        return this.mIsSecureConfig;
    }

    public byte[] getWatchlistConfigHash() {
        if (!this.mXmlFile.exists()) {
            return null;
        }
        try {
            return DigestUtils.getSha256Hash(this.mXmlFile);
        } catch (IOException | NoSuchAlgorithmException e) {
            Log.e(TAG, "Unable to get watchlist config hash", e);
            return null;
        }
    }

    public void setTestMode(InputStream inputStream) throws IOException {
        Log.i(TAG, "Setting watchlist testing config");
        FileUtils.copyToFileOrThrow(inputStream, new File(NETWORK_WATCHLIST_DB_FOR_TEST_PATH));
        this.mIsSecureConfig = false;
        this.mXmlFile = new File(NETWORK_WATCHLIST_DB_FOR_TEST_PATH);
        reloadConfig();
    }

    public void removeTestModeConfig() {
        try {
            File file = new File(NETWORK_WATCHLIST_DB_FOR_TEST_PATH);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception unused) {
            Log.e(TAG, "Unable to delete test config");
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        byte[] watchlistConfigHash = getWatchlistConfigHash();
        StringBuilder sb = new StringBuilder();
        sb.append("Watchlist config hash: ");
        sb.append(watchlistConfigHash != null ? HexDump.toHexString(watchlistConfigHash) : null);
        printWriter.println(sb.toString());
        printWriter.println("Domain CRC32 digest list:");
        if (this.mDomainDigests != null) {
            this.mDomainDigests.crc32s.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("Domain SHA256 digest list:");
        if (this.mDomainDigests != null) {
            this.mDomainDigests.sha256Digests.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("Ip CRC32 digest list:");
        if (this.mIpDigests != null) {
            this.mIpDigests.crc32s.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println("Ip SHA256 digest list:");
        if (this.mIpDigests != null) {
            this.mIpDigests.sha256Digests.dump(fileDescriptor, printWriter, strArr);
        }
    }
}
