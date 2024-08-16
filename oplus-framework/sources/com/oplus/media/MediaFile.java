package com.oplus.media;

import android.content.Context;
import android.media.DecoderCapabilities;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.oplus.wrapper.content.pm.PackageInstaller;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import libcore.content.type.MimeMap;

/* loaded from: classes.dex */
public class MediaFile {
    public static final int FILE_TYPE_3GPP = 23;
    public static final int FILE_TYPE_3GPP2 = 24;
    public static final int FILE_TYPE_AAC = 8;
    public static final int FILE_TYPE_AMR = 4;
    public static final int FILE_TYPE_APE = 1001;
    public static final int FILE_TYPE_APK = 10011;
    public static final int FILE_TYPE_ARW = 304;
    public static final int FILE_TYPE_ASF = 26;
    public static final int FILE_TYPE_AUDIO_3GPP = 11;
    public static final int FILE_TYPE_AVI = 29;
    public static final int FILE_TYPE_AWB = 5;
    public static final int FILE_TYPE_BMP = 34;
    public static final int FILE_TYPE_CHM = 10021;
    public static final int FILE_TYPE_CR2 = 301;
    public static final int FILE_TYPE_CSV = 10022;
    public static final int FILE_TYPE_CUE = 1003;
    public static final int FILE_TYPE_DNG = 300;
    public static final int FILE_TYPE_EBK = 10026;
    public static final int FILE_TYPE_EPUB = 10027;
    public static final int FILE_TYPE_FL = 51;
    public static final int FILE_TYPE_FLAC = 10;
    public static final int FILE_TYPE_FLV = 1101;
    public static final int FILE_TYPE_GIF = 32;
    public static final int FILE_TYPE_HEIF = 37;
    public static final int FILE_TYPE_HTML = 101;
    public static final int FILE_TYPE_HTTPLIVE = 44;
    public static final int FILE_TYPE_ICS = 10023;
    public static final int FILE_TYPE_IMY = 13;
    public static final int FILE_TYPE_JAR = 10002;
    public static final int FILE_TYPE_JPEG = 31;
    public static final int FILE_TYPE_M2TS = 1104;
    public static final int FILE_TYPE_M3U = 41;
    public static final int FILE_TYPE_M4A = 2;
    public static final int FILE_TYPE_M4V = 22;
    public static final int FILE_TYPE_MID = 11;
    public static final int FILE_TYPE_MKA = 9;
    public static final int FILE_TYPE_MKV = 27;
    public static final int FILE_TYPE_MOV = 1103;
    public static final int FILE_TYPE_MP2 = 1002;
    public static final int FILE_TYPE_MP2PS = 200;
    public static final int FILE_TYPE_MP2TS = 28;
    public static final int FILE_TYPE_MP3 = 1;
    public static final int FILE_TYPE_MP4 = 21;
    public static final int FILE_TYPE_MS_EXCEL = 105;
    public static final int FILE_TYPE_MS_POWERPOINT = 106;
    public static final int FILE_TYPE_MS_WORD = 104;
    public static final int FILE_TYPE_NEF = 302;
    public static final int FILE_TYPE_NRW = 303;
    public static final int FILE_TYPE_OGG = 7;
    public static final int FILE_TYPE_ORF = 306;
    public static final int FILE_TYPE_PDF = 102;
    public static final int FILE_TYPE_PEF = 308;
    public static final int FILE_TYPE_PLS = 42;
    public static final int FILE_TYPE_PNG = 33;
    public static final int FILE_TYPE_QT = 201;
    public static final int FILE_TYPE_RA = 1004;
    public static final int FILE_TYPE_RAF = 307;
    public static final int FILE_TYPE_RAR = 10001;
    public static final int FILE_TYPE_RV = 1102;
    public static final int FILE_TYPE_RW2 = 305;
    public static final int FILE_TYPE_SMF = 12;
    public static final int FILE_TYPE_SRW = 309;
    public static final int FILE_TYPE_TEXT = 100;
    public static final int FILE_TYPE_VCF = 10024;
    public static final int FILE_TYPE_VCS = 10025;
    public static final int FILE_TYPE_WAV = 3;
    public static final int FILE_TYPE_WBMP = 35;
    public static final int FILE_TYPE_WEBM = 30;
    public static final int FILE_TYPE_WEBP = 36;
    public static final int FILE_TYPE_WMA = 6;
    public static final int FILE_TYPE_WMV = 25;
    public static final int FILE_TYPE_WPL = 43;
    public static final int FILE_TYPE_XML = 103;
    public static final int FILE_TYPE_ZIP = 107;
    private static final int FIRST_APK_FILE_TYPE = 10011;
    private static final int FIRST_AUDIO_FILE_TYPE = 1;
    private static final int FIRST_COMPRESS_FILE_TYPE = 10001;
    private static final int FIRST_DOC_FILE_TYPE = 10021;
    private static final int FIRST_DRM_FILE_TYPE = 51;
    private static final int FIRST_FFMPEG_AUDIO_FILE_TYPE = 1001;
    private static final int FIRST_FFMPEG_VIDEO_FILE_TYPE = 1101;
    private static final int FIRST_IMAGE_FILE_TYPE = 31;
    private static final int FIRST_MIDI_FILE_TYPE = 11;
    private static final int FIRST_PLAYLIST_FILE_TYPE = 41;
    private static final int FIRST_POPULAR_DOC_FILE_TYPE = 100;
    private static final int FIRST_RAW_IMAGE_FILE_TYPE = 300;
    private static final int FIRST_VIDEO_FILE_TYPE = 21;
    private static final int FIRST_VIDEO_FILE_TYPE2 = 200;
    private static final int LAST_APK_FILE_TYPE = 10011;
    private static final int LAST_AUDIO_FILE_TYPE = 11;
    private static final int LAST_COMPRESS_FILE_TYPE = 10002;
    private static final int LAST_DOC_FILE_TYPE = 10027;
    private static final int LAST_DRM_FILE_TYPE = 51;
    private static final int LAST_FFMPEG_AUDIO_FILE_TYPE = 1004;
    private static final int LAST_FFMPEG_VIDEO_FILE_TYPE = 1104;
    private static final int LAST_IMAGE_FILE_TYPE = 37;
    private static final int LAST_MIDI_FILE_TYPE = 13;
    private static final int LAST_PLAYLIST_FILE_TYPE = 44;
    private static final int LAST_POPULAR_DOC_FILE_TYPE = 106;
    private static final int LAST_RAW_IMAGE_FILE_TYPE = 309;
    private static final int LAST_VIDEO_FILE_TYPE = 30;
    private static final int LAST_VIDEO_FILE_TYPE2 = 201;
    public static final int MEDIA_TYPE_APK = 10002;
    public static final int MEDIA_TYPE_COMPRESS = 10001;
    public static final int MEDIA_TYPE_DOC = 10003;
    public static final String OPLUS_DEFAULT_ALARM = "oplus_customize_default_alarm";
    public static final String OPLUS_DEFAULT_NOTIFICATION = "oplus_customize_default_notification";
    public static final String OPLUS_DEFAULT_NOTIFICATION_SIM2 = "oplus_customize_default_notification_sim2";
    public static final String OPLUS_DEFAULT_RINGTONE = "oplus_customize_default_ringtone";
    public static final String OPLUS_DEFAULT_RINGTONE_SIM2 = "oplus_customize_default_ringtone_sim2";
    public static final String OPLUS_DEFAULT_SMS_NOTIFICATION = "oplus_customize_default_sms_notification_sound";
    public static final int SCAN_ALL_FILE = 0;
    public static final int SCAN_AUDIO_FILE = 1;
    public static final int SCAN_IMAGE_FILE = 2;
    public static final int SCAN_OTHER_FILE = 8;
    public static final int SCAN_VIDEO_FILE = 4;
    private static final HashMap<Integer, String> sFormatToMimeTypeMap;
    private static final HashMap<String, Integer> sMimeTypeToFormatMap;
    private static final HashMap<String, MediaFileType> sFileTypeMap = new HashMap<>();
    private static final HashMap<String, Integer> sMimeTypeMap = new HashMap<>();
    private static final HashMap<String, Integer> sFileTypeToFormatMap = new HashMap<>();
    private static final HashMap<String, Integer> sDeprecatedMimeTypeToFormatMap = new HashMap<>();
    private static final HashMap<Integer, String> sDeprecatedFormatToMimeTypeMap = new HashMap<>();

    /* loaded from: classes.dex */
    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    static {
        addFileAndMineType("MP3", 1, "audio/mpeg", 12297, true);
        addFileAndMineType("MPGA", 1, "audio/mpeg", 12297, false);
        addFileAndMineType("M4A", 2, "audio/mp4", 12299, false);
        addFileAndMineType("WAV", 3, "audio/x-wav", 12296, true);
        addFileAndMineType("AMR", 4, "audio/amr");
        addFileAndMineType("3GPP", 11, "audio/3gpp");
        addFileAndMineType("AWB", 5, "audio/amr-wb");
        if (isWMAEnabled()) {
            addFileAndMineType("WMA", 6, "audio/x-ms-wma", 47361, true);
        }
        addFileAndMineType("OGG", 7, "audio/ogg", 47362, false);
        addFileAndMineType("OGG", 7, "application/ogg", 47362, true);
        addFileAndMineType("OGA", 7, "application/ogg", 47362, false);
        addFileAndMineType("AAC", 8, "audio/aac", 47363, true);
        addFileAndMineType("AAC", 8, "audio/aac-adts", 47363, false);
        addFileAndMineType("MKA", 9, "audio/x-matroska");
        addFileAndMineType("MID", 11, "audio/mid");
        addFileAndMineType("MID", 11, "audio/midi");
        addFileAndMineType("MIDI", 11, "audio/midi");
        addFileAndMineType("XMF", 11, "audio/midi");
        addFileAndMineType("RTTTL", 11, "audio/midi");
        addFileAndMineType("SMF", 12, "audio/sp-midi");
        addFileAndMineType("IMY", 13, "audio/imelody");
        addFileAndMineType("RTX", 11, "audio/midi");
        addFileAndMineType("OTA", 11, "audio/midi");
        addFileAndMineType("MXMF", 11, "audio/midi");
        addFileAndMineType("MPEG", 21, "video/mpeg", 12299, true);
        addFileAndMineType("MPG", 21, "video/mpeg", 12299, false);
        addFileAndMineType("MP4", 21, "video/mp4", 12299, false);
        addFileAndMineType("M4V", 22, "video/mp4", 12299, false);
        addFileAndMineType("MOV", 201, "video/quicktime", 12299, false);
        addFileAndMineType("3GP", 23, "video/3gpp", 47492, true);
        addFileAndMineType("3GPP", 23, "video/3gpp", 47492, false);
        addFileAndMineType("3G2", 24, "video/3gpp2", 47492, false);
        addFileAndMineType("3GPP2", 24, "video/3gpp2", 47492, false);
        addFileAndMineType("MKV", 27, "video/x-matroska");
        addFileAndMineType("WEBM", 30, "video/webm");
        addFileAndMineType("TS", 28, "video/mp2ts");
        addFileAndMineType("AVI", 29, "video/avi");
        if (isWMVEnabled()) {
            addFileAndMineType("WMV", 25, "video/x-ms-wmv", 47489, true);
            addFileAndMineType("ASF", 26, "video/x-ms-asf");
        }
        addFileAndMineType("JPG", 31, "image/jpeg", 14337, true);
        addFileAndMineType("JPEG", 31, "image/jpeg", 14337, false);
        addFileAndMineType("GIF", 32, "image/gif", 14343, true);
        addFileAndMineType("PNG", 33, "image/png", 14347, true);
        addFileAndMineType("BMP", 34, "image/x-ms-bmp", 14340, true);
        addFileAndMineType("BMP", 34, "image/bmp", 14340, false);
        addFileAndMineType("WBMP", 35, "image/vnd.wap.wbmp", 14336, false);
        addFileAndMineType("WEBP", 36, "image/webp", 14336, false);
        addFileAndMineType("HEIC", 37, "image/heif", 14354, true);
        addFileAndMineType("HEIF", 37, "image/heif", 14354, false);
        addFileAndMineType("DNG", 300, "image/x-adobe-dng", 14353, true);
        addFileAndMineType("CR2", 301, "image/x-canon-cr2", 14349, false);
        addFileAndMineType("NEF", 302, "image/x-nikon-nef", 14338, false);
        addFileAndMineType("NRW", 303, "image/x-nikon-nrw", 14349, false);
        addFileAndMineType("ARW", 304, "image/x-sony-arw", 14349, false);
        addFileAndMineType("RW2", 305, "image/x-panasonic-rw2", 14349, false);
        addFileAndMineType("ORF", 306, "image/x-olympus-orf", 14349, false);
        addFileAndMineType("RAF", 307, "image/x-fuji-raf", 14336, false);
        addFileAndMineType("PEF", 308, "image/x-pentax-pef", 14349, false);
        addFileAndMineType("SRW", 309, "image/x-samsung-srw", 14349, false);
        addFileAndMineType("M3U", 41, "audio/x-mpegurl", 47633, true);
        addFileAndMineType("M3U", 41, "application/x-mpegurl", 47633, false);
        addFileAndMineType("PLS", 42, "audio/x-scpls", 47636, true);
        addFileAndMineType("WPL", 43, "application/vnd.ms-wpl", 47632, true);
        addFileAndMineType("M3U8", 44, "application/vnd.apple.mpegurl");
        addFileAndMineType("M3U8", 44, "audio/mpegurl");
        addFileAndMineType("M3U8", 44, "audio/x-mpegurl");
        addFileAndMineType("FL", 51, "application/x-android-drm-fl");
        addFileAndMineType("TXT", 100, "text/plain", 12292, true);
        addFileAndMineType("HTM", 101, "text/html", 12293, true);
        addFileAndMineType("HTML", 101, "text/html", 12293, false);
        addFileAndMineType("PDF", 102, "application/pdf");
        addFileAndMineType("DOC", 104, "application/msword", 47747, true);
        addFileAndMineType("XLS", 105, "application/vnd.ms-excel", 47749, true);
        addFileAndMineType("PPT", 106, "application/mspowerpoint", 47750, true);
        addFileAndMineType("FLAC", 10, "audio/flac", 47366, true);
        addFileAndMineType("ZIP", 107, "application/zip");
        addFileAndMineType("MPG", 200, "video/mp2p");
        addFileAndMineType("MPEG", 200, "video/mp2p");
        addFileAndMineType("APE", 1001, "audio/ape");
        addFileAndMineType("MP2", 1002, "audio/mpeg");
        addFileAndMineType("CUE", 1003, "audio/cue");
        addFileAndMineType("FLV", 1101, "video/x-flv");
        addFileAndMineType("F4V", 1101, "video/x-flv");
        addFileAndMineType("MOV", FILE_TYPE_MOV, "video/x-quicktime");
        addFileAndMineType("M2TS", 1104, "video/m2ts");
        addFileAndMineType("DOCX", 104, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileAndMineType("XLSX", 105, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        addFileAndMineType("PPTX", 106, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        addFileAndMineType("RAR", 10001, "application/rar");
        addFileAndMineType("JAR", 10002, "application/java-archive");
        addFileAndMineType("APK", 10011, "application/vnd.android.package-archive");
        addFileAndMineType("CHM", 10021, "application/x-expandedbook");
        addFileAndMineType("CSV", 10022, "text/comma-separated-values");
        addFileAndMineType("ICS", 10023, "text/calendar");
        addFileAndMineType("VCF", 10024, "text/x-vcard");
        addFileAndMineType("VCS", 10025, "text/x-vcalendar");
        addFileAndMineType("EBK2", 10026, "text/x-expandedbook");
        addFileAndMineType("EBK3", 10026, "text/x-expandedbook");
        addFileAndMineType("EPUB", 10027, "text/plain");
        sMimeTypeToFormatMap = new HashMap<>();
        sFormatToMimeTypeMap = new HashMap<>();
        addFileType(12297, "audio/mpeg");
        addFileType(12296, "audio/x-wav");
        addFileType(47361, "audio/x-ms-wma");
        addFileType(47362, "audio/ogg");
        addFileType(47363, "audio/aac");
        addFileType(47366, "audio/flac");
        addFileType(12295, "audio/x-aiff");
        addFileType(47491, "audio/mpeg");
        addFileType(12299, "video/mpeg");
        addFileType(47490, "video/mp4");
        addFileType(47492, "video/3gpp");
        addFileType(47492, "video/3gpp2");
        addFileType(12298, "video/avi");
        addFileType(47489, "video/x-ms-wmv");
        addFileType(12300, "video/x-ms-asf");
        addFileType(14337, "image/jpeg");
        addFileType(14343, "image/gif");
        addFileType(14347, "image/png");
        addFileType(14340, "image/x-ms-bmp");
        addFileType(14354, "image/heif");
        addFileType(14353, "image/x-adobe-dng");
        addFileType(14349, "image/tiff");
        addFileType(14349, "image/x-canon-cr2");
        addFileType(14349, "image/x-nikon-nrw");
        addFileType(14349, "image/x-sony-arw");
        addFileType(14349, "image/x-panasonic-rw2");
        addFileType(14349, "image/x-olympus-orf");
        addFileType(14349, "image/x-pentax-pef");
        addFileType(14349, "image/x-samsung-srw");
        addFileType(14338, "image/tiff");
        addFileType(14338, "image/x-nikon-nef");
        addFileType(14351, "image/jp2");
        addFileType(14352, "image/jpx");
        addFileType(47633, "audio/x-mpegurl");
        addFileType(47636, "audio/x-scpls");
        addFileType(47632, "application/vnd.ms-wpl");
        addFileType(47635, "video/x-ms-asf");
        addFileType(12292, "text/plain");
        addFileType(12293, "text/html");
        addFileType(47746, "text/xml");
        addFileType(47747, "application/msword");
        addFileType(47747, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileType(47749, "application/vnd.ms-excel");
        addFileType(47749, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        addFileType(47750, "application/vnd.ms-powerpoint");
        addFileType(47750, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    static void addFileAndMineType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, Integer.valueOf(fileType));
    }

    private static void addFileAndMineType(String extension, int fileType, String mimeType, int mtpFormatCode, boolean primaryType) {
        addFileAndMineType(extension, fileType, mimeType);
        sFileTypeToFormatMap.put(extension, Integer.valueOf(mtpFormatCode));
        sDeprecatedMimeTypeToFormatMap.put(mimeType, Integer.valueOf(mtpFormatCode));
        if (primaryType) {
            HashMap<Integer, String> hashMap = sDeprecatedFormatToMimeTypeMap;
            Preconditions.checkArgument(!hashMap.containsKey(Integer.valueOf(mtpFormatCode)));
            hashMap.put(Integer.valueOf(mtpFormatCode), mimeType);
        }
    }

    private static boolean isWMAEnabled() {
        List<DecoderCapabilities.AudioDecoder> decoders = DecoderCapabilities.getAudioDecoders();
        int count = decoders.size();
        for (int i = 0; i < count; i++) {
            DecoderCapabilities.AudioDecoder decoder = decoders.get(i);
            if (decoder == DecoderCapabilities.AudioDecoder.AUDIO_DECODER_WMA) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWMVEnabled() {
        List<DecoderCapabilities.VideoDecoder> decoders = DecoderCapabilities.getVideoDecoders();
        int count = decoders.size();
        for (int i = 0; i < count; i++) {
            DecoderCapabilities.VideoDecoder decoder = decoders.get(i);
            if (decoder == DecoderCapabilities.VideoDecoder.VIDEO_DECODER_WMV) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAudioFileType(int fileType) {
        if (fileType >= 1 && fileType <= 11) {
            return true;
        }
        if (fileType < 11 || fileType > 13) {
            return fileType >= 1001 && fileType <= 1004;
        }
        return true;
    }

    public static boolean isVideoFileType(int fileType) {
        return (fileType >= 21 && fileType <= 30) || (fileType >= 200 && fileType <= 201) || (fileType >= 1101 && fileType <= 1104);
    }

    public static boolean isImageFileType(int fileType) {
        return (fileType >= 31 && fileType <= 37) || (fileType >= 300 && fileType <= 309);
    }

    public static boolean isRawImageFileType(int fileType) {
        return fileType >= 300 && fileType <= 309;
    }

    public static boolean isPlayListFileType(int fileType) {
        return fileType >= 41 && fileType <= 44;
    }

    public static boolean isDrmFileType(int fileType) {
        return fileType >= 51 && fileType <= 51;
    }

    public static boolean isCompressFileType(int fileType) {
        return (fileType >= 10001 && fileType <= 10002) || fileType == 107;
    }

    public static boolean isApkFileType(int fileType) {
        return fileType >= 10011 && fileType <= 10011;
    }

    public static boolean isDocFileType(int fileType) {
        return (fileType >= 10021 && fileType <= 10027) || (fileType >= 100 && fileType <= 106 && fileType != 103);
    }

    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(46);
        if (lastDot < 0) {
            return null;
        }
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(Locale.ROOT));
    }

    public static boolean isMimeTypeMedia(String mimeType) {
        int fileType = getFileTypeForMimeType(mimeType);
        return isAudioFileType(fileType) || isVideoFileType(fileType) || isImageFileType(fileType) || isPlayListFileType(fileType);
    }

    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public static Uri getDefaultRingtoneUri(Context context) {
        return getUriFor(context, "oplus_customize_default_ringtone");
    }

    public static Uri getDefaultAlarmUri(Context context) {
        return getUriFor(context, "oplus_customize_default_alarm");
    }

    public static Uri getDefaultNotificationUri(Context context) {
        return getUriFor(context, "oplus_customize_default_notification");
    }

    public static Uri getDefaultRingtoneUriSIM2(Context context) {
        return getUriFor(context, "oplus_customize_default_ringtone_sim2");
    }

    public static Uri getDefaultNotificationUriSIM2(Context context) {
        return getUriFor(context, "oplus_customize_default_notification_sim2");
    }

    public static Uri getDefaultSmsNotificationUri(Context context) {
        return getUriFor(context, "oplus_customize_default_sms_notification_sound");
    }

    private static Uri getUriFor(Context context, String name) {
        String value = Settings.System.getString(context.getContentResolver(), name);
        if (value != null) {
            return Uri.parse(value);
        }
        Log.e("MediaFile", name + " not set?!!!");
        return null;
    }

    static void addFileType(String extension, int fileType, String mimeType) {
    }

    private static void addFileType(int mtpFormatCode, String mimeType) {
        HashMap<String, Integer> hashMap = sMimeTypeToFormatMap;
        if (!hashMap.containsKey(mimeType)) {
            hashMap.put(mimeType, Integer.valueOf(mtpFormatCode));
        }
        HashMap<Integer, String> hashMap2 = sFormatToMimeTypeMap;
        if (!hashMap2.containsKey(Integer.valueOf(mtpFormatCode))) {
            hashMap2.put(Integer.valueOf(mtpFormatCode), mimeType);
        }
    }

    public static boolean isExifMimeType(String mimeType) {
        return isImageMimeType(mimeType);
    }

    public static boolean isAudioMimeType(String mimeType) {
        return normalizeMimeType(mimeType).startsWith("audio/");
    }

    public static boolean isVideoMimeType(String mimeType) {
        return normalizeMimeType(mimeType).startsWith("video/");
    }

    public static boolean isImageMimeType(String mimeType) {
        return normalizeMimeType(mimeType).startsWith("image/");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean isPlayListMimeType(String mimeType) {
        char c;
        String normalizeMimeType = normalizeMimeType(mimeType);
        switch (normalizeMimeType.hashCode()) {
            case -1165508903:
                if (normalizeMimeType.equals("audio/x-scpls")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -979095690:
                if (normalizeMimeType.equals("application/x-mpegurl")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -622808459:
                if (normalizeMimeType.equals("application/vnd.apple.mpegurl")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -432766831:
                if (normalizeMimeType.equals("audio/mpegurl")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 264230524:
                if (normalizeMimeType.equals("audio/x-mpegurl")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1872259501:
                if (normalizeMimeType.equals("application/vnd.ms-wpl")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDrmMimeType(String mimeType) {
        return normalizeMimeType(mimeType).equals("application/x-android-drm-fl");
    }

    public static boolean isApkMimeType(String mimeType) {
        return normalizeMimeType(mimeType).equals("application/vnd.android.package-archive");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean isCompressMimeType(String mimeType) {
        char c;
        String normalizeMimeType = normalizeMimeType(mimeType);
        switch (normalizeMimeType.hashCode()) {
            case -1248333084:
                if (normalizeMimeType.equals("application/rar")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1248325150:
                if (normalizeMimeType.equals("application/zip")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2049276534:
                if (normalizeMimeType.equals("application/java-archive")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean isDocMimeType(String mimeType) {
        char c;
        String normalizeMimeType = normalizeMimeType(mimeType);
        switch (normalizeMimeType.hashCode()) {
            case -2135895576:
                if (normalizeMimeType.equals("text/comma-separated-values")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -2008589971:
                if (normalizeMimeType.equals("application/epub+zip")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1248334925:
                if (normalizeMimeType.equals("application/pdf")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1082243251:
                if (normalizeMimeType.equals("text/html")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1073633483:
                if (normalizeMimeType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -1071817359:
                if (normalizeMimeType.equals("application/vnd.ms-powerpoint")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1050893613:
                if (normalizeMimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -1004747228:
                if (normalizeMimeType.equals("text/csv")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -958424608:
                if (normalizeMimeType.equals("text/calendar")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -366307023:
                if (normalizeMimeType.equals("application/vnd.ms-excel")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 262346941:
                if (normalizeMimeType.equals("text/x-vcalendar")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 501428239:
                if (normalizeMimeType.equals("text/x-vcard")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 817335912:
                if (normalizeMimeType.equals("text/plain")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 904647503:
                if (normalizeMimeType.equals("application/msword")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1496903267:
                if (normalizeMimeType.equals("chemical/x-chemdraw")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1993842850:
                if (normalizeMimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMediaMimeType(String mimeType) {
        return isAudioMimeType(mimeType) || isVideoMimeType(mimeType) || isImageMimeType(mimeType) || isPlayListMimeType(mimeType);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static boolean isRawImageMimeType(String mimeType) {
        char c;
        String normalizeMimeType = normalizeMimeType(mimeType);
        switch (normalizeMimeType.hashCode()) {
            case -1635437028:
                if (normalizeMimeType.equals("image/x-samsung-srw")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1594371159:
                if (normalizeMimeType.equals("image/x-sony-arw")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1487103447:
                if (normalizeMimeType.equals("image/tiff")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1423313290:
                if (normalizeMimeType.equals("image/x-adobe-dng")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -985160897:
                if (normalizeMimeType.equals("image/x-panasonic-rw2")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -332763809:
                if (normalizeMimeType.equals("image/x-pentax-pef")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1378106698:
                if (normalizeMimeType.equals("image/x-olympus-orf")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 2099152104:
                if (normalizeMimeType.equals("image/x-nikon-nef")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 2099152524:
                if (normalizeMimeType.equals("image/x-nikon-nrw")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 2111234748:
                if (normalizeMimeType.equals("image/x-canon-cr2")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
                return true;
            default:
                return false;
        }
    }

    public static String getFileTitle(String path) {
        int lastSlash;
        int lastSlash2 = path.lastIndexOf(47);
        if (lastSlash2 >= 0 && (lastSlash = lastSlash2 + 1) < path.length()) {
            path = path.substring(lastSlash);
        }
        int lastDot = path.lastIndexOf(46);
        if (lastDot > 0) {
            return path.substring(0, lastDot);
        }
        return path;
    }

    public static String getFileExtension(String path) {
        int lastDot;
        if (path == null || (lastDot = path.lastIndexOf(46)) < 0) {
            return null;
        }
        return path.substring(lastDot + 1);
    }

    public static String getMimeType(String path, int formatCode) {
        String mimeType = getMimeTypeForFile(path);
        if (!"application/octet-stream".equals(mimeType)) {
            return mimeType;
        }
        return getMimeTypeForFormatCode(formatCode);
    }

    public static String getMimeTypeForFile(String path) {
        String mimeType = guessMimeTypeFromExtension(getFileExtension(path));
        return mimeType != null ? mimeType : "application/octet-stream";
    }

    public static String getMimeTypeForFormatCode(int formatCode) {
        String mimeType = sFormatToMimeTypeMap.get(Integer.valueOf(formatCode));
        return mimeType != null ? mimeType : "application/octet-stream";
    }

    public static int getFormatCode(String path, String mimeType) {
        int formatCode = getFormatCodeForMimeType(mimeType);
        if (formatCode != 12288) {
            return formatCode;
        }
        return getFormatCodeForFile(path);
    }

    public static int getFormatCodeForFile(String path) {
        return getFormatCodeForMimeType(getMimeTypeForFile(path));
    }

    public static int getFormatCodeForMimeType(String mimeType) {
        if (mimeType == null) {
            return PackageInstaller.SessionParamsWrapper.COMPILE_MODE_QIUCKEN;
        }
        HashMap<String, Integer> hashMap = sMimeTypeToFormatMap;
        Integer value = hashMap.get(mimeType);
        if (value != null) {
            return value.intValue();
        }
        String mimeType2 = normalizeMimeType(mimeType);
        Integer value2 = hashMap.get(mimeType2);
        if (value2 != null) {
            return value2.intValue();
        }
        if (mimeType2.startsWith("audio/")) {
            return 47360;
        }
        if (mimeType2.startsWith("video/")) {
            return 47488;
        }
        if (!mimeType2.startsWith("image/")) {
            return PackageInstaller.SessionParamsWrapper.COMPILE_MODE_QIUCKEN;
        }
        return 14336;
    }

    private static String normalizeMimeType(String mimeType) {
        String extensionMimeType;
        MimeMap mimeMap = MimeMap.getDefault();
        String extension = mimeMap.guessExtensionFromMimeType(mimeType);
        if (extension == null || (extensionMimeType = mimeMap.guessMimeTypeFromExtension(extension)) == null) {
            return mimeType != null ? mimeType : "application/octet-stream";
        }
        return extensionMimeType;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0065, code lost:
    
        if (r6.equals("f4v") != false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
    
        if (r0.equals("audio/x-pn-realaudio-plugin") != false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String guessMimeTypeFromExtension(String extension) {
        String mimeType = null;
        if (extension != null) {
            MimeMap mimeMap = MimeMap.getDefault();
            mimeType = mimeMap.guessMimeTypeFromExtension(extension);
            char c = 1;
            if (mimeType != null) {
                switch (mimeType.hashCode()) {
                    case -794081673:
                        if (mimeType.equals("audio/x-pn-realaudio")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 379957065:
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                        return null;
                    default:
                        return mimeType;
                }
            }
            String extension2 = extension.toLowerCase(Locale.ROOT);
            switch (extension2.hashCode()) {
                case 98867:
                    if (extension2.equals("cue")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 99752:
                    break;
                case 3106436:
                    if (extension2.equals("ebk2")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 3106437:
                    if (extension2.equals("ebk3")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3298980:
                    if (extension2.equals("m2ts")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    mimeType = "audio/cue";
                    break;
                case 1:
                    mimeType = "video/x-flv";
                    break;
                case 2:
                    mimeType = "video/m2ts";
                    break;
                case 3:
                case 4:
                    mimeType = "text/x-expandedbook";
                    break;
            }
        }
        return mimeType != null ? mimeType : "application/octet-stream";
    }
}
