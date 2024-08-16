package com.android.server.integrity;

import android.content.integrity.AppInstallMetadata;
import android.content.integrity.Rule;
import android.os.Environment;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.integrity.model.RuleMetadata;
import com.android.server.integrity.parser.RandomAccessObject;
import com.android.server.integrity.parser.RuleBinaryParser;
import com.android.server.integrity.parser.RuleIndexRange;
import com.android.server.integrity.parser.RuleIndexingController;
import com.android.server.integrity.parser.RuleMetadataParser;
import com.android.server.integrity.parser.RuleParseException;
import com.android.server.integrity.parser.RuleParser;
import com.android.server.integrity.serializer.RuleBinarySerializer;
import com.android.server.integrity.serializer.RuleMetadataSerializer;
import com.android.server.integrity.serializer.RuleSerializeException;
import com.android.server.integrity.serializer.RuleSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class IntegrityFileManager {
    private static final String INDEXING_FILE = "indexing";
    private static final String METADATA_FILE = "metadata";
    private static final String RULES_FILE = "rules";
    private static final Object RULES_LOCK = new Object();
    private static final String TAG = "IntegrityFileManager";
    private static IntegrityFileManager sInstance;
    private final File mDataDir;
    private RuleIndexingController mRuleIndexingController;
    private RuleMetadata mRuleMetadataCache;
    private final RuleParser mRuleParser;
    private final RuleSerializer mRuleSerializer;
    private final File mRulesDir;
    private final File mStagingDir;

    public static synchronized IntegrityFileManager getInstance() {
        IntegrityFileManager integrityFileManager;
        synchronized (IntegrityFileManager.class) {
            if (sInstance == null) {
                sInstance = new IntegrityFileManager();
            }
            integrityFileManager = sInstance;
        }
        return integrityFileManager;
    }

    private IntegrityFileManager() {
        this(new RuleBinaryParser(), new RuleBinarySerializer(), Environment.getDataSystemDirectory());
    }

    @VisibleForTesting
    IntegrityFileManager(RuleParser ruleParser, RuleSerializer ruleSerializer, File file) {
        this.mRuleParser = ruleParser;
        this.mRuleSerializer = ruleSerializer;
        this.mDataDir = file;
        File file2 = new File(file, "integrity_rules");
        this.mRulesDir = file2;
        File file3 = new File(file, "integrity_staging");
        this.mStagingDir = file3;
        if (!file3.mkdirs() || !file2.mkdirs()) {
            Slog.e(TAG, "Error creating staging and rules directory");
        }
        File file4 = new File(file2, METADATA_FILE);
        if (file4.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file4);
                try {
                    this.mRuleMetadataCache = RuleMetadataParser.parse(fileInputStream);
                    fileInputStream.close();
                } finally {
                }
            } catch (Exception e) {
                Slog.e(TAG, "Error reading metadata file.", e);
            }
        }
        updateRuleIndexingController();
    }

    public boolean initialized() {
        return new File(this.mRulesDir, RULES_FILE).exists() && new File(this.mRulesDir, METADATA_FILE).exists() && new File(this.mRulesDir, INDEXING_FILE).exists();
    }

    public void writeRules(String str, String str2, List<Rule> list) throws IOException, RuleSerializeException {
        try {
            writeMetadata(this.mStagingDir, str2, str);
        } catch (IOException e) {
            Slog.e(TAG, "Error writing metadata.", e);
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mStagingDir, RULES_FILE));
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(new File(this.mStagingDir, INDEXING_FILE));
            try {
                this.mRuleSerializer.serialize(list, Optional.empty(), fileOutputStream, fileOutputStream2);
                fileOutputStream2.close();
                fileOutputStream.close();
                switchStagingRulesDir();
                updateRuleIndexingController();
            } finally {
            }
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public List<Rule> readRules(AppInstallMetadata appInstallMetadata) throws IOException, RuleParseException {
        List<Rule> parse;
        synchronized (RULES_LOCK) {
            List<RuleIndexRange> emptyList = Collections.emptyList();
            if (appInstallMetadata != null) {
                try {
                    emptyList = this.mRuleIndexingController.identifyRulesToEvaluate(appInstallMetadata);
                } catch (Exception e) {
                    Slog.w(TAG, "Error identifying the rule indexes. Trying unindexed.", e);
                }
            }
            parse = this.mRuleParser.parse(RandomAccessObject.ofFile(new File(this.mRulesDir, RULES_FILE)), emptyList);
        }
        return parse;
    }

    public RuleMetadata readMetadata() {
        return this.mRuleMetadataCache;
    }

    private void switchStagingRulesDir() throws IOException {
        synchronized (RULES_LOCK) {
            File file = new File(this.mDataDir, "temp");
            if (!this.mRulesDir.renameTo(file) || !this.mStagingDir.renameTo(this.mRulesDir) || !file.renameTo(this.mStagingDir)) {
                throw new IOException("Error switching staging/rules directory");
            }
            for (File file2 : this.mStagingDir.listFiles()) {
                file2.delete();
            }
        }
    }

    private void updateRuleIndexingController() {
        File file = new File(this.mRulesDir, INDEXING_FILE);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    this.mRuleIndexingController = new RuleIndexingController(fileInputStream);
                    fileInputStream.close();
                } finally {
                }
            } catch (Exception e) {
                Slog.e(TAG, "Error parsing the rule indexing file.", e);
            }
        }
    }

    private void writeMetadata(File file, String str, String str2) throws IOException {
        this.mRuleMetadataCache = new RuleMetadata(str, str2);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(file, METADATA_FILE));
        try {
            RuleMetadataSerializer.serialize(this.mRuleMetadataCache, fileOutputStream);
            fileOutputStream.close();
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
