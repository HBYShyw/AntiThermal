package cn.teddymobile.free.anteater.rule.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DetectionFileUtils {
    public static void logFile(String content, String filePath) {
        File file = new File(filePath);
        try {
            FileOutputStream fop = new FileOutputStream(file);
            try {
                if (!file.exists()) {
                    boolean flag = file.createNewFile();
                    if (!flag) {
                        throw new IOException("Create file failed!");
                    }
                }
                byte[] contentInBytes = content.getBytes();
                fop.write(contentInBytes);
                fop.flush();
                fop.close();
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadRestrictions(String filePath) {
        List<String> list = new ArrayList<>();
        File file = new File(filePath);
        try {
            FileInputStream fip = new FileInputStream(file);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fip));
                while (true) {
                    String str = bufferedReader.readLine();
                    if (str != null) {
                        list.add(str);
                    } else {
                        fip.close();
                        bufferedReader.close();
                        fip.close();
                        return list;
                    }
                }
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }
    }
}
