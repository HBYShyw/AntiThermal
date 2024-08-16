package com.oplus.thermalcontrol;

import b6.LocalLog;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes2.dex */
public class ThermalConfigXmlEncryption {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_THERMAL = "thermal";
    private static final String TAG = "ThermalConfigXmlEncryption";
    private static Charset charset = StandardCharsets.UTF_8;
    private static Cipher cipher = null;
    private static final int ivLengthByte = 12;
    private static final int keyLength = 16;
    private static final int tagLengthBit = 128;

    private static String byte2Hex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b10 : bArr) {
            String hexString = Integer.toHexString(b10 & 255);
            if (hexString.length() == 1) {
                stringBuffer.append("0" + hexString);
            } else {
                stringBuffer.append(hexString);
            }
        }
        return stringBuffer.toString();
    }

    public static String decrypt(byte[] bArr, String str) {
        try {
            ByteBuffer wrap = ByteBuffer.wrap(Base64.getMimeDecoder().decode(str));
            byte[] bArr2 = new byte[wrap.get()];
            wrap.get(bArr2);
            byte[] bArr3 = new byte[wrap.remaining()];
            wrap.get(bArr3);
            Cipher cipher2 = Cipher.getInstance(ALGORITHM);
            cipher = cipher2;
            cipher2.init(2, new SecretKeySpec(bArr, "AES"), new GCMParameterSpec(128, bArr2));
            byte[] doFinal = cipher.doFinal(bArr3);
            Arrays.fill(bArr2, (byte) 0);
            Arrays.fill(bArr3, (byte) 0);
            return new String(doFinal, charset);
        } catch (Exception e10) {
            throw new Exception("could not decrypt", e10);
        }
    }

    public static InputStream decryptXmlFile(String str) {
        try {
            String readFile = readFile(str);
            if (readFile == null) {
                return null;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decrypt(getSha256Key().getBytes("UTF-8"), readFile).getBytes("UTF-8"));
            LocalLog.l(TAG, "decryptXmlFile:" + str);
            return byteArrayInputStream;
        } catch (Exception e10) {
            LocalLog.b(TAG, "Exception:" + e10);
            return null;
        }
    }

    public static String encrypt(byte[] bArr, String str) {
        try {
            byte[] bArr2 = new byte[12];
            new SecureRandom().nextBytes(bArr2);
            Cipher cipher2 = Cipher.getInstance(ALGORITHM);
            cipher = cipher2;
            cipher2.init(1, new SecretKeySpec(bArr, "AES"), new GCMParameterSpec(128, bArr2));
            byte[] doFinal = cipher.doFinal(str.getBytes(charset));
            ByteBuffer allocate = ByteBuffer.allocate(13 + doFinal.length);
            allocate.put((byte) 12);
            allocate.put(bArr2);
            allocate.put(doFinal);
            return Base64.getEncoder().encodeToString(allocate.array());
        } catch (Exception e10) {
            throw new Exception(e10);
        }
    }

    public static String getSHA256(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            return byte2Hex(messageDigest.digest());
        } catch (UnsupportedEncodingException e10) {
            e10.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e11) {
            e11.printStackTrace();
            return "";
        }
    }

    public static String getSha256Key() {
        return getSHA256(KEY_THERMAL).substring(0, 16);
    }

    public static String readFile(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(str));
            for (int read = bufferedInputStream.read(); read != -1; read = bufferedInputStream.read()) {
                byteArrayOutputStream.write((byte) read);
            }
            bufferedInputStream.close();
            return byteArrayOutputStream.toString("UTF-8");
        } catch (Exception e10) {
            e10.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String str, String str2) {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(str2)));
            bufferedOutputStream.write(str.getBytes("UTF-8"));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }
}
