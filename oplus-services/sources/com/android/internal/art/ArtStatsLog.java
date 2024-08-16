package com.android.internal.art;

import android.util.StatsEvent;
import android.util.StatsLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ArtStatsLog {
    public static final byte ANNOTATION_ID_DEFAULT_STATE = 6;
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    public static final byte ANNOTATION_ID_IS_UID = 1;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    public static final int ART_DATUM_DELTA_REPORTED = 565;
    public static final int ART_DATUM_DELTA_REPORTED__APK_TYPE__ART_APK_TYPE_BASE = 1;
    public static final int ART_DATUM_DELTA_REPORTED__APK_TYPE__ART_APK_TYPE_SPLIT = 2;
    public static final int ART_DATUM_DELTA_REPORTED__APK_TYPE__ART_APK_TYPE_UNKNOWN = 0;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_AB_OTA = 7;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BG_DEXOPT = 6;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BOOT = 4;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BOOT_AFTER_MAINLINE_UPDATE = 21;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BOOT_AFTER_OTA = 17;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_CMDLINE = 19;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_ERROR = 1;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_FIRST_BOOT = 3;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INACTIVE = 8;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL = 5;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK = 13;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK_DOWNGRADED = 15;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY = 14;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY_DOWNGRADED = 16;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_FAST = 12;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_WITH_DEX_METADATA = 10;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_POST_BOOT = 11;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_PREBUILT = 18;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_SHARED = 9;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_UNKNOWN = 2;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_UNSPECIFIED = 0;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_VDEX = 20;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_ASSUMED_VERIFIED = 3;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_ERROR = 1;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_EVERYTHING = 12;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_EVERYTHING_PROFILE = 11;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_EXTRACT = 4;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_FAKE_RUN_FROM_APK = 13;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_FAKE_RUN_FROM_APK_FALLBACK = 14;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_FAKE_RUN_FROM_VDEX_FALLBACK = 15;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_QUICKEN = 6;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPACE = 8;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPACE_PROFILE = 7;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPEED = 10;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPEED_PROFILE = 9;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_UNKNOWN = 2;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_UNSPECIFIED = 0;
    public static final int ART_DATUM_DELTA_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_VERIFY = 5;
    public static final int ART_DATUM_DELTA_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_ERROR = 5;
    public static final int ART_DATUM_DELTA_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_NONE = 4;
    public static final int ART_DATUM_DELTA_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_PROFILE = 1;
    public static final int ART_DATUM_DELTA_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_PROFILE_AND_VDEX = 3;
    public static final int ART_DATUM_DELTA_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_UNKNOWN = 0;
    public static final int ART_DATUM_DELTA_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_VDEX = 2;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_COPYING = 5;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_COPYING_BACKGROUND = 6;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_MARK_COMPACT = 3;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_MARK_SWEEP = 2;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_MARK_SWEEP = 1;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_SEMI_SPACE = 4;
    public static final int ART_DATUM_DELTA_REPORTED__GC__ART_GC_COLLECTOR_TYPE_UNKNOWN = 0;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_ARM = 1;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_ARM64 = 2;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_MIPS = 5;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_MIPS64 = 6;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_RISCV64 = 7;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_UNKNOWN = 0;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_X86 = 3;
    public static final int ART_DATUM_DELTA_REPORTED__ISA__ART_ISA_X86_64 = 4;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_CLASS_LOADING_TIME_MICROS = 9;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_CLASS_VERIFICATION_COUNT = 16;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_CLASS_VERIFICATION_TIME_MICROS = 8;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_FULL_HEAP_COLLECTION_COUNT = 5;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_FULL_HEAP_COLLECTION_DURATION_MS = 36;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_FULL_HEAP_COLLECTION_FREED_BYTES = 35;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_FULL_HEAP_COLLECTION_SCANNED_BYTES = 34;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_TOTAL_BYTES_ALLOCATED = 17;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_TOTAL_COLLECTION_TIME_MS = 28;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_WORLD_STOP_COUNT = 30;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_WORLD_STOP_TIME_US = 29;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_YOUNG_GENERATION_COLLECTION_COUNT = 3;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_YOUNG_GENERATION_COLLECTION_DURATION_MS = 33;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_YOUNG_GENERATION_COLLECTION_FREED_BYTES = 32;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_GC_YOUNG_GENERATION_COLLECTION_SCANNED_BYTES = 31;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_INVALID = 0;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_JIT_METHOD_COMPILE_COUNT = 21;
    public static final int ART_DATUM_DELTA_REPORTED__KIND__ART_DATUM_DELTA_JIT_METHOD_COMPILE_TIME_MICROS = 6;
    public static final int ART_DATUM_DELTA_REPORTED__THREAD_TYPE__ART_THREAD_BACKGROUND = 2;
    public static final int ART_DATUM_DELTA_REPORTED__THREAD_TYPE__ART_THREAD_MAIN = 1;
    public static final int ART_DATUM_DELTA_REPORTED__THREAD_TYPE__ART_THREAD_UNKNOWN = 0;
    public static final int ART_DATUM_DELTA_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_MINOR_FAULT_MODE_NOT_SUPPORTED = 2;
    public static final int ART_DATUM_DELTA_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_MINOR_FAULT_MODE_SUPPORTED = 3;
    public static final int ART_DATUM_DELTA_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_UFFD_NOT_SUPPORTED = 1;
    public static final int ART_DATUM_DELTA_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_UNKNOWN = 0;
    public static final int ART_DATUM_REPORTED = 332;
    public static final int ART_DATUM_REPORTED__APK_TYPE__ART_APK_TYPE_BASE = 1;
    public static final int ART_DATUM_REPORTED__APK_TYPE__ART_APK_TYPE_SPLIT = 2;
    public static final int ART_DATUM_REPORTED__APK_TYPE__ART_APK_TYPE_UNKNOWN = 0;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_AB_OTA = 7;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BG_DEXOPT = 6;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BOOT = 4;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BOOT_AFTER_MAINLINE_UPDATE = 21;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_BOOT_AFTER_OTA = 17;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_CMDLINE = 19;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_ERROR = 1;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_FIRST_BOOT = 3;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INACTIVE = 8;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL = 5;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK = 13;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK_DOWNGRADED = 15;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY = 14;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_BULK_SECONDARY_DOWNGRADED = 16;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_FAST = 12;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_INSTALL_WITH_DEX_METADATA = 10;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_POST_BOOT = 11;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_PREBUILT = 18;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_SHARED = 9;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_UNKNOWN = 2;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_UNSPECIFIED = 0;
    public static final int ART_DATUM_REPORTED__COMPILATION_REASON__ART_COMPILATION_REASON_VDEX = 20;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_ASSUMED_VERIFIED = 3;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_ERROR = 1;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_EVERYTHING = 12;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_EVERYTHING_PROFILE = 11;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_EXTRACT = 4;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_FAKE_RUN_FROM_APK = 13;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_FAKE_RUN_FROM_APK_FALLBACK = 14;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_FAKE_RUN_FROM_VDEX_FALLBACK = 15;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_QUICKEN = 6;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPACE = 8;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPACE_PROFILE = 7;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPEED = 10;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_SPEED_PROFILE = 9;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_UNKNOWN = 2;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_UNSPECIFIED = 0;
    public static final int ART_DATUM_REPORTED__COMPILE_FILTER__ART_COMPILATION_FILTER_VERIFY = 5;
    public static final int ART_DATUM_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_ERROR = 5;
    public static final int ART_DATUM_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_NONE = 4;
    public static final int ART_DATUM_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_PROFILE = 1;
    public static final int ART_DATUM_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_PROFILE_AND_VDEX = 3;
    public static final int ART_DATUM_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_UNKNOWN = 0;
    public static final int ART_DATUM_REPORTED__DEX_METADATA_TYPE__ART_DEX_METADATA_TYPE_VDEX = 2;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_COPYING = 5;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_COPYING_BACKGROUND = 6;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_MARK_COMPACT = 3;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_CONCURRENT_MARK_SWEEP = 2;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_MARK_SWEEP = 1;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_SEMI_SPACE = 4;
    public static final int ART_DATUM_REPORTED__GC__ART_GC_COLLECTOR_TYPE_UNKNOWN = 0;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_ARM = 1;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_ARM64 = 2;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_MIPS = 5;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_MIPS64 = 6;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_RISCV64 = 7;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_UNKNOWN = 0;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_X86 = 3;
    public static final int ART_DATUM_REPORTED__ISA__ART_ISA_X86_64 = 4;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_AOT_COMPILE_TIME = 7;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_CLASS_LOADING_TIME_COUNTER_MICROS = 9;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_CLASS_VERIFICATION_COUNT = 16;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_CLASS_VERIFICATION_TIME_COUNTER_MICROS = 8;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_DEX2OAT_DEX_CODE_COUNTER_BYTES = 11;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_DEX2OAT_FAST_VERIFY_TIME_COUNTER_MILLIS = 14;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_DEX2OAT_RESOLVE_METHODS_AND_FIELDS_TIME_COUNTER_MILLIS = 15;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_DEX2OAT_RESULT_CODE = 10;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_DEX2OAT_TOTAL_TIME_COUNTER_MILLIS = 12;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_DEX2OAT_VERIFY_DEX_FILE_TIME_COUNTER_MILLIS = 13;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_COUNT = 5;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_DURATION_MS = 36;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_FREED_BYTES = 35;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_SCANNED_BYTES = 34;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_THROUGHPUT_AVG_MB_PER_SEC = 25;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_THROUGHPUT_HISTO_MB_PER_SEC = 20;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_COLLECTION_TIME_HISTO_MILLIS = 4;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_TRACING_THROUGHPUT_AVG_MB_PER_SEC = 27;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_FULL_HEAP_TRACING_THROUGHPUT_HISTO_MB_PER_SEC = 23;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_TOTAL_BYTES_ALLOCATED = 17;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_TOTAL_COLLECTION_TIME_MS = 28;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_TOTAL_METADATA_SIZE_BYTES = 18;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_WORLD_STOP_COUNT = 30;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_WORLD_STOP_TIME_AVG_MICROS = 1;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_WORLD_STOP_TIME_US = 29;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_COUNT = 3;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_DURATION_MS = 33;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_FREED_BYTES = 32;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_SCANNED_BYTES = 31;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_THROUGHPUT_AVG_MB_PER_SEC = 24;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_THROUGHPUT_HISTO_MB_PER_SEC = 19;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_COLLECTION_TIME_HISTO_MILLIS = 2;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_TRACING_THROUGHPUT_AVG_MB_PER_SEC = 26;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_GC_YOUNG_GENERATION_TRACING_THROUGHPUT_HISTO_MB_PER_SEC = 22;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_INVALID = 0;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_JIT_METHOD_COMPILE_COUNT = 21;
    public static final int ART_DATUM_REPORTED__KIND__ART_DATUM_JIT_METHOD_COMPILE_TIME_MICROS = 6;
    public static final int ART_DATUM_REPORTED__THREAD_TYPE__ART_THREAD_BACKGROUND = 2;
    public static final int ART_DATUM_REPORTED__THREAD_TYPE__ART_THREAD_MAIN = 1;
    public static final int ART_DATUM_REPORTED__THREAD_TYPE__ART_THREAD_UNKNOWN = 0;
    public static final int ART_DATUM_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_MINOR_FAULT_MODE_NOT_SUPPORTED = 2;
    public static final int ART_DATUM_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_MINOR_FAULT_MODE_SUPPORTED = 3;
    public static final int ART_DATUM_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_UFFD_NOT_SUPPORTED = 1;
    public static final int ART_DATUM_REPORTED__UFFD_SUPPORT__ART_UFFD_SUPPORT_UNKNOWN = 0;
    public static final int ART_DEVICE_DATUM_REPORTED = 550;
    public static final int ART_DEVICE_DATUM_REPORTED__BOOT_IMAGE_STATUS__STATUS_FULL = 1;
    public static final int ART_DEVICE_DATUM_REPORTED__BOOT_IMAGE_STATUS__STATUS_MINIMAL = 2;
    public static final int ART_DEVICE_DATUM_REPORTED__BOOT_IMAGE_STATUS__STATUS_NONE = 3;
    public static final int ART_DEVICE_DATUM_REPORTED__BOOT_IMAGE_STATUS__STATUS_UNSPECIFIED = 0;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED = 467;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_APP_STANDBY = 12;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_BACKGROUND_RESTRICTION = 11;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_CANCELLED_BY_APP = 1;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_CONSTRAINT_BATTERY_NOT_LOW = 5;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_CONSTRAINT_CHARGING = 6;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_CONSTRAINT_CONNECTIVITY = 7;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_CONSTRAINT_DEVICE_IDLE = 8;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_CONSTRAINT_STORAGE_NOT_LOW = 9;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_DEVICE_STATE = 4;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_ESTIMATED_APP_LAUNCH_TIME_CHANGED = 15;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_PREEMPT = 2;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_QUOTA = 10;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_SYSTEM_PROCESSING = 14;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_TIMEOUT = 3;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_UNDEFINED = 0;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__CANCELLATION_REASON__STOP_REASON_USER = 13;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_ABORT_BATTERY = 5;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_ABORT_BY_API = 6;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_ABORT_BY_CANCELLATION = 2;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_ABORT_NO_SPACE_LEFT = 3;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_ABORT_THERMAL = 4;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_FATAL_ERROR = 7;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_JOB_FINISHED = 1;
    public static final int BACKGROUND_DEXOPT_JOB_ENDED__STATUS__STATUS_UNKNOWN = 0;
    public static final int EARLY_BOOT_COMP_OS_ARTIFACTS_CHECK_REPORTED = 419;
    public static final int ISOLATED_COMPILATION_ENDED = 458;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_COMPILATION_FAILED = 5;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_COMPOSD_DIED = 7;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_FAILED_TO_ENABLE_FSVERITY = 8;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_FAILED_TO_START = 3;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_JOB_CANCELED = 4;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_SUCCESS = 1;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_UNEXPECTED_COMPILATION_RESULT = 6;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_UNKNOWN = 0;
    public static final int ISOLATED_COMPILATION_ENDED__COMPILATION_RESULT__RESULT_UNKNOWN_FAILURE = 2;
    public static final int ISOLATED_COMPILATION_SCHEDULED = 457;
    public static final int ISOLATED_COMPILATION_SCHEDULED__SCHEDULING_RESULT__SCHEDULING_FAILURE = 1;
    public static final int ISOLATED_COMPILATION_SCHEDULED__SCHEDULING_RESULT__SCHEDULING_RESULT_UNKNOWN = 0;
    public static final int ISOLATED_COMPILATION_SCHEDULED__SCHEDULING_RESULT__SCHEDULING_SUCCESS = 2;
    public static final int ODREFRESH_REPORTED = 366;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_COMPILATION_TYPE__BCP_COMPILATION_TYPE_MAINLINE = 2;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_COMPILATION_TYPE__BCP_COMPILATION_TYPE_PRIMARY_AND_MAINLINE = 1;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_COMPILATION_TYPE__BCP_COMPILATION_TYPE_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_EXITED = 1;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_NOT_RUN = 5;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_SIGNALED = 2;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_START_FAILED = 4;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_TIMED_OUT = 3;
    public static final int ODREFRESH_REPORTED__PRIMARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_COMPILATION_TYPE__BCP_COMPILATION_TYPE_MAINLINE = 2;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_COMPILATION_TYPE__BCP_COMPILATION_TYPE_PRIMARY_AND_MAINLINE = 1;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_COMPILATION_TYPE__BCP_COMPILATION_TYPE_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_EXITED = 1;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_NOT_RUN = 5;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_SIGNALED = 2;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_START_FAILED = 4;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_TIMED_OUT = 3;
    public static final int ODREFRESH_REPORTED__SECONDARY_BCP_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_CHECK = 10;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_COMPLETE = 60;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_PREPARATION = 20;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_PRIMARY_BOOT_CLASSPATH = 30;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_SECONDARY_BOOT_CLASSPATH = 40;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_SYSTEM_SERVER_CLASSPATH = 50;
    public static final int ODREFRESH_REPORTED__STAGE_REACHED__STAGE_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_DALVIK_CACHE_PERMISSION_DENIED = 8;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_DEX2OAT_ERROR = 4;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_INSTALL_FAILED = 7;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_IO_ERROR = 3;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_NO_SPACE = 2;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_OK = 1;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_STAGING_FAILED = 6;
    public static final int ODREFRESH_REPORTED__STATUS__STATUS_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__SYSTEM_SERVER_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_EXITED = 1;
    public static final int ODREFRESH_REPORTED__SYSTEM_SERVER_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_NOT_RUN = 5;
    public static final int ODREFRESH_REPORTED__SYSTEM_SERVER_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_SIGNALED = 2;
    public static final int ODREFRESH_REPORTED__SYSTEM_SERVER_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_START_FAILED = 4;
    public static final int ODREFRESH_REPORTED__SYSTEM_SERVER_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_TIMED_OUT = 3;
    public static final int ODREFRESH_REPORTED__SYSTEM_SERVER_DEX2OAT_RESULT_STATUS__EXEC_RESULT_STATUS_UNKNOWN = 0;
    public static final int ODREFRESH_REPORTED__TRIGGER__TRIGGER_APEX_VERSION_MISMATCH = 1;
    public static final int ODREFRESH_REPORTED__TRIGGER__TRIGGER_DEX_FILES_CHANGED = 2;
    public static final int ODREFRESH_REPORTED__TRIGGER__TRIGGER_MISSING_ARTIFACTS = 3;
    public static final int ODREFRESH_REPORTED__TRIGGER__TRIGGER_UNKNOWN = 0;
    public static final int ODSIGN_REPORTED = 548;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_ALL_OK = 1;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_CERT_FAILED = 5;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_CLEANUP_FAILED = 6;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_KEYSTORE_FAILED = 4;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_ODREFRESH_FAILED = 3;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_PARTIAL_OK = 2;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_SIGNING_FAILED = 7;
    public static final int ODSIGN_REPORTED__STATUS__STATUS_UNSPECIFIED = 0;
    public static final int OPLUS_ART_LOCK_JANK_REPORTED = 100013;
    public static final int OPLUS_ART_THREAD_SLEEP_INFO_REPORTED = 100076;
    public static final int OPLUS_ART_VERIFIER_JANK_REPORTED = 100014;
    public static final int OPLUS_DEX_JANK_REPORTED = 100012;
    public static final int OPLUS_GC_BLOCKED_REPORTED = 100011;

    public static void write(int i, boolean z, boolean z2, boolean z3) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeBoolean(z);
        newBuilder.writeBoolean(z2);
        newBuilder.writeBoolean(z3);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, int i2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, int i2, int i3, long j, long j2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeLong(j);
        newBuilder.writeLong(j2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.writeInt(i5);
        newBuilder.writeInt(i6);
        newBuilder.writeInt(i7);
        newBuilder.writeInt(i8);
        newBuilder.writeInt(i9);
        newBuilder.writeInt(i10);
        newBuilder.writeInt(i11);
        newBuilder.writeInt(i12);
        newBuilder.writeInt(i13);
        newBuilder.writeInt(i14);
        newBuilder.writeInt(i15);
        newBuilder.writeInt(i16);
        newBuilder.writeInt(i17);
        newBuilder.writeInt(i18);
        newBuilder.writeInt(i19);
        newBuilder.writeInt(i20);
        newBuilder.writeInt(i21);
        newBuilder.writeInt(i22);
        newBuilder.writeInt(i23);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, int i4, long j2, int i5, int i6, long j3, int i7, int i8, int i9, int i10, int i11) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        if (332 == i) {
            newBuilder.addBooleanAnnotation((byte) 1, true);
        }
        if (565 == i) {
            newBuilder.addBooleanAnnotation((byte) 1, true);
        }
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.writeLong(j2);
        newBuilder.writeInt(i5);
        newBuilder.writeInt(i6);
        newBuilder.writeLong(j3);
        newBuilder.writeInt(i7);
        newBuilder.writeInt(i8);
        newBuilder.writeInt(i9);
        newBuilder.writeInt(i10);
        newBuilder.writeInt(i11);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, int i4, String str, long j2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.writeString(str);
        newBuilder.writeLong(j2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, long j2, long j3) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeLong(j2);
        newBuilder.writeLong(j3);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, String str, long j2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeString(str);
        newBuilder.writeLong(j2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, String str, String str2, long j2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeString(str);
        newBuilder.writeString(str2);
        newBuilder.writeLong(j2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, long j, int i2, int i3, String str, String str2, String str3, String str4, String str5) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        newBuilder.writeInt(i2);
        newBuilder.writeInt(i3);
        newBuilder.writeString(str);
        newBuilder.writeString(str2);
        newBuilder.writeString(str3);
        newBuilder.writeString(str4);
        newBuilder.writeString(str5);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }
}
