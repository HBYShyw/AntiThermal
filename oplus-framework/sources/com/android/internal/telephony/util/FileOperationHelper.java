package com.android.internal.telephony.util;

import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class FileOperationHelper {
    private static final String TAG = "FileOperationHelper";

    private static boolean writeStringToFileImpl(String logTag, String path, String value, boolean debug) {
        boolean result = false;
        FileWriter writer = null;
        try {
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                File file = new File(path);
                writer = new FileWriter(file);
                writer.write(String.valueOf(value));
                result = true;
                if (debug) {
                    Log.d(logTag, "writeStringToFile write " + value + " to " + path);
                }
                writer.close();
            } catch (Exception e2) {
                Log.e(logTag, "writeStringToFile sorry write wrong");
                e2.printStackTrace();
                if (writer != null) {
                    writer.close();
                }
            }
            return result;
        } catch (Throwable th) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static boolean writeStringToFile(String logTag, String path, String value) {
        return writeStringToFileImpl(logTag, path, value, true);
    }

    public static boolean writeStringToFileSilent(String logTag, String path, String value) {
        return writeStringToFileImpl(logTag, path, value, false);
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0038 -> B:6:0x004e). Please report as a decompilation issue!!! */
    public static boolean writeintToFile(String logTag, String nodePath, int value) {
        boolean result = false;
        FileWriter writer = null;
        try {
            try {
                try {
                    File file = new File(nodePath);
                    writer = new FileWriter(file);
                    writer.write(value);
                    result = true;
                    Log.d(logTag, "writeintToFile write " + value + " to " + nodePath);
                    writer.close();
                } catch (IOException e) {
                    Log.e(logTag, "writeintToFile sorry write wrong");
                    e.printStackTrace();
                    if (writer != null) {
                        writer.close();
                    }
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return result;
        } catch (Throwable th) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x006b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int readIntFromFile(String logTag, String path, int defaultValue) {
        StringBuilder sb;
        File file = new File(path);
        BufferedReader reader = null;
        int result = defaultValue;
        String tempString = null;
        try {
            try {
                reader = new BufferedReader(new FileReader(file));
                tempString = reader.readLine();
                try {
                    reader.close();
                } catch (IOException e) {
                    e1 = e;
                    sb = new StringBuilder();
                    Log.e(logTag, sb.append("readIntFromFile io close exception :").append(e1.getMessage()).toString());
                    if (!TextUtils.isEmpty(tempString)) {
                    }
                    Log.i(logTag, "readIntFromFile path:" + path + ", result:" + result + ", defaultValue:" + defaultValue);
                    return result;
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e(logTag, "readIntFromFile io close exception :" + e1.getMessage());
                    }
                }
                throw th;
            }
        } catch (Exception e2) {
            Log.e(logTag, "readIntFromFile io exception:" + e2.getMessage());
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    e1 = e3;
                    sb = new StringBuilder();
                    Log.e(logTag, sb.append("readIntFromFile io close exception :").append(e1.getMessage()).toString());
                    if (!TextUtils.isEmpty(tempString)) {
                    }
                    Log.i(logTag, "readIntFromFile path:" + path + ", result:" + result + ", defaultValue:" + defaultValue);
                    return result;
                }
            }
        }
        if (!TextUtils.isEmpty(tempString)) {
            try {
                result = Integer.valueOf(tempString).intValue();
            } catch (NumberFormatException e4) {
                Log.e(logTag, "readIntFromFile NumberFormatException:" + e4.getMessage());
            }
        }
        Log.i(logTag, "readIntFromFile path:" + path + ", result:" + result + ", defaultValue:" + defaultValue);
        return result;
    }

    public static int readIntFromFileSilent(String path, int defaultValue) {
        File file = new File(path);
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                tempString = reader.readLine();
                reader.close();
            } finally {
            }
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(tempString)) {
            return defaultValue;
        }
        try {
            int result = Integer.valueOf(tempString).intValue();
            return result;
        } catch (NumberFormatException e2) {
            return defaultValue;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0096  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String readStringFromFileImpl(String logTag, String path, boolean debug) {
        StringBuilder sb;
        if (path == null || path.isEmpty()) {
            Log.i(logTag, "readStringFromFile invalid file path");
            return null;
        }
        if (logTag == null) {
            logTag = TAG;
        }
        File file = new File(path);
        BufferedReader reader = null;
        String tempString = null;
        try {
            if (!new File(path).exists()) {
                Log.i(logTag, "readStringFromFile file not exists : " + path);
                return null;
            }
            try {
                reader = new BufferedReader(new FileReader(file));
                tempString = reader.readLine();
            } catch (IOException e) {
                Log.e(logTag, "readStringFromFile io exception:" + e.getMessage());
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e2) {
                        e1 = e2;
                        sb = new StringBuilder();
                        Log.e(logTag, sb.append("readStringFromFile io close exception :").append(e1.getMessage()).toString());
                        if (debug) {
                        }
                        return tempString;
                    }
                }
            }
            try {
                reader.close();
            } catch (IOException e3) {
                e1 = e3;
                sb = new StringBuilder();
                Log.e(logTag, sb.append("readStringFromFile io close exception :").append(e1.getMessage()).toString());
                if (debug) {
                }
                return tempString;
            }
            if (debug) {
                Log.i(logTag, "readStringFromFile path:" + path + ", result:" + tempString);
            }
            return tempString;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    Log.e(logTag, "readStringFromFile io close exception :" + e1.getMessage());
                }
            }
            throw th;
        }
    }

    public static String readStringFromFile(String logTag, String path) {
        return readStringFromFileImpl(logTag, path, true);
    }

    public static String readStringFromFileSilent(String logTag, String path) {
        return readStringFromFileImpl(logTag, path, false);
    }

    private static void copy(InputStream input, OutputStream output, int length) throws IOException {
        byte[] buf = new byte[length];
        while (true) {
            int bytesRead = input.read(buf);
            if (bytesRead != -1) {
                output.write(buf, 0, bytesRead);
            } else {
                return;
            }
        }
    }

    public static byte[] readByteFromFile(String logTag, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            Log.i(logTag, "readByteFromFile invalid file path");
            return null;
        }
        File sourceFile = new File(fileName);
        InputStream input = null;
        ByteArrayOutputStream output = null;
        if (!sourceFile.isFile() || !sourceFile.exists()) {
            return null;
        }
        long fileLength = sourceFile.length();
        Log.i(logTag, "readByteFromFile file length : " + fileLength);
        try {
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                input = new FileInputStream(fileName);
                output = new ByteArrayOutputStream();
                copy(input, output, (int) fileLength);
                input.close();
            } catch (IOException e2) {
                e2.printStackTrace();
                if (input != null) {
                    input.close();
                }
            }
            if (output == null) {
                return null;
            }
            byte[] ret = output.toByteArray();
            try {
                output.close();
            } catch (IOException e3) {
            }
            return ret;
        } catch (Throwable th) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }
}
