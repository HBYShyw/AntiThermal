package com.oplus.hiddenapi;

import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusHiddenApiParser {
    private static final String TAG = "OplusHiddenApiParser";
    private MessageDigest mMessageDigest;

    public OplusHiddenApiParser() {
        try {
            this.mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "failed to get md5 algorithm");
        }
    }

    public Map<String, Set<String>> parse(File target, boolean needCheckMD5) throws Exception {
        MessageDigest messageDigest;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(target)));
        try {
            String expectedMD5 = reader.readLine();
            Map<String, Set<String>> exemptions = parseExemptions(reader, needCheckMD5);
            if (needCheckMD5 && (messageDigest = this.mMessageDigest) != null) {
                String actualMD5 = md5(messageDigest);
                if (!actualMD5.equals(expectedMD5)) {
                    throw new Exception("md5 check failed for " + target + " expected md5 = " + expectedMD5 + " actual md5 = " + actualMD5);
                }
            }
            reader.close();
            return exemptions;
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private Map<String, Set<String>> parseExemptions(BufferedReader reader, boolean needCheckMD5) throws IOException {
        MessageDigest messageDigest;
        List<String> apis = new ArrayList<>();
        Map<String, Set<String>> exemptions = new ArrayMap<>();
        int countOfApis = 0;
        int index = 0;
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                if (index == 0) {
                    countOfApis = Integer.parseInt(line);
                } else if (index <= countOfApis) {
                    apis.add(line);
                } else {
                    readPackages(apis, exemptions, line);
                }
                index++;
                if (needCheckMD5 && (messageDigest = this.mMessageDigest) != null) {
                    messageDigest.update(line.getBytes());
                }
            } else {
                return exemptions;
            }
        }
    }

    private String md5(MessageDigest messageDigest) {
        byte[] md5sum = messageDigest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        String output = bigInt.toString(16);
        return String.format("%32s", output).replace(' ', '0');
    }

    private void readPackages(List<String> apis, Map<String, Set<String>> target, String line) {
        String[] raw = line.split("#");
        String packageName = raw[0];
        String rawExemptions = raw[1];
        String[] exemptionsIndex = rawExemptions.split(",");
        Set<String> exemptions = new ArraySet<>(exemptionsIndex.length);
        for (String index : exemptionsIndex) {
            exemptions.add(apis.get(Integer.parseInt(index)));
        }
        target.put(packageName, exemptions);
    }
}
