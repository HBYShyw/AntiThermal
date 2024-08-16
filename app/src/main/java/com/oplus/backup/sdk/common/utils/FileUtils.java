package com.oplus.backup.sdk.common.utils;

import android.text.TextUtils;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/* loaded from: classes.dex */
public class FileUtils {
    public static final String TAG = "FileUtils";

    public static boolean copyFile(InputStream inputStream, String str) {
        if (inputStream == null || TextUtils.isEmpty(str)) {
            return false;
        }
        mkdirs(new File(str).getParentFile());
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(str);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read != -1) {
                            fileOutputStream2.write(bArr, 0, read);
                        } else {
                            fileOutputStream2.flush();
                            fileOutputStream2.close();
                            inputStream.close();
                            return true;
                        }
                    }
                } catch (IOException e10) {
                    throw e10;
                } catch (Exception unused) {
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    inputStream.close();
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream = fileOutputStream2;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    inputStream.close();
                    throw th;
                }
            } catch (IOException e11) {
                throw e11;
            } catch (Exception unused2) {
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean copyFolder(String str, String str2) {
        File file;
        try {
            new File(str2).mkdirs();
            String[] list = new File(str).list();
            for (int i10 = 0; i10 < list.length; i10++) {
                String str3 = File.separator;
                if (str.endsWith(str3)) {
                    file = new File(str + list[i10]);
                } else {
                    file = new File(str + str3 + list[i10]);
                }
                if (file.isFile()) {
                    nioTransferCopy(file, new File(str2 + "/" + file.getName().toString()));
                }
                if (file.isDirectory()) {
                    copyFolder(str + "/" + list[i10], str2 + "/" + list[i10]);
                }
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean deleteFileOrFolder(File file) {
        if (file == null) {
            return false;
        }
        try {
            boolean z10 = true;
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        if (!deleteFileOrFolder(file2)) {
                            z10 = false;
                        }
                    }
                }
                if (!file.delete()) {
                    return false;
                }
            }
            return z10;
        } catch (Exception unused) {
            BRLog.e(TAG, "deleteFileOrFolder exception");
            return false;
        }
    }

    public static boolean isEmptyFolder(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                return false;
            }
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (!isEmptyFolder(file2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean mkdirs(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return file.mkdirs();
        }
        if (file.isDirectory()) {
            return true;
        }
        if (file.isFile()) {
            File file2 = new File(file.getAbsolutePath() + file.lastModified());
            if (file.renameTo(file2)) {
                return file.mkdirs();
            }
            BRLog.w(TAG, "file.renameTo false, " + BRLog.getModifiedFile(file2));
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0069  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean nioTransferCopy(File file, File file2) {
        FileOutputStream fileOutputStream;
        FileChannel fileChannel;
        FileChannel fileChannel2;
        FileOutputStream fileOutputStream2;
        FileInputStream fileInputStream;
        FileChannel fileChannel3;
        FileInputStream fileInputStream2;
        FileInputStream fileInputStream3 = null;
        r0 = null;
        FileChannel fileChannel4 = null;
        r0 = null;
        r0 = null;
        FileChannel fileChannel5 = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                fileOutputStream = new FileOutputStream(file2);
                try {
                    fileChannel = fileInputStream.getChannel();
                    try {
                        fileChannel4 = fileOutputStream.getChannel();
                        fileChannel.transferTo(0L, fileChannel.size(), fileChannel4);
                        fileInputStream.close();
                        fileChannel.close();
                        fileOutputStream.close();
                        if (fileChannel4 == null) {
                            return true;
                        }
                        fileChannel4.close();
                        return true;
                    } catch (IOException e10) {
                        e = e10;
                        fileOutputStream2 = fileOutputStream;
                        fileChannel2 = fileChannel4;
                        fileChannel5 = fileChannel;
                        try {
                            throw e;
                        } catch (Throwable th) {
                            th = th;
                            fileInputStream2 = fileInputStream;
                            fileChannel3 = fileChannel2;
                            fileOutputStream = fileOutputStream2;
                            fileChannel = fileChannel5;
                            fileInputStream3 = fileInputStream2;
                            if (fileInputStream3 != null) {
                                fileInputStream3.close();
                            }
                            if (fileChannel != null) {
                                fileChannel.close();
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (fileChannel3 != null) {
                                fileChannel3.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream2 = fileInputStream;
                        fileChannel3 = fileChannel4;
                        fileInputStream3 = fileInputStream2;
                        if (fileInputStream3 != null) {
                        }
                        if (fileChannel != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        if (fileChannel3 != null) {
                        }
                        throw th;
                    }
                } catch (IOException e11) {
                    e = e11;
                    fileOutputStream2 = fileOutputStream;
                    fileChannel2 = null;
                } catch (Throwable th3) {
                    th = th3;
                    fileChannel = null;
                    fileInputStream3 = fileInputStream;
                    fileChannel3 = fileChannel;
                    if (fileInputStream3 != null) {
                    }
                    if (fileChannel != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    if (fileChannel3 != null) {
                    }
                    throw th;
                }
            } catch (IOException e12) {
                e = e12;
                fileChannel2 = null;
                fileOutputStream2 = null;
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = null;
                fileChannel = null;
            }
        } catch (IOException e13) {
            e = e13;
            fileChannel2 = null;
            fileOutputStream2 = null;
            fileInputStream = null;
        } catch (Throwable th5) {
            th = th5;
            fileOutputStream = null;
            fileChannel = null;
        }
    }

    public static void setPermissionsReadOnly(String str) {
        android.os.FileUtils.setPermissions(str, DataLinkConstants.AUTO_BRIGHTNESS, -1, -1);
    }

    public static boolean copyFile(String str, String str2) {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream = null;
        try {
            try {
                File file = new File(str);
                if (file.exists() && file.isFile()) {
                    fileInputStream = new FileInputStream(str);
                    try {
                        FileOutputStream fileOutputStream2 = new FileOutputStream(str2);
                        try {
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream2.write(bArr, 0, read);
                            }
                            fileOutputStream2.flush();
                            fileOutputStream = fileOutputStream2;
                        } catch (IOException e10) {
                            throw e10;
                        } catch (Exception unused) {
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (fileInputStream == null) {
                                return false;
                            }
                            fileInputStream.close();
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            throw th;
                        }
                    } catch (IOException e11) {
                        throw e11;
                    } catch (Exception unused2) {
                    }
                } else {
                    fileInputStream = null;
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return true;
            } catch (IOException e12) {
                throw e12;
            } catch (Exception unused3) {
                fileInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
