package com.android.server.am.trace;

import android.util.Slog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BinderTransactions {
    private static final String BINDER_TRANSATION_FILE = "/dev/binderfs/binder_logs/state";
    private static final int DUMP_MAX_COUNT = 10;
    private static final String REGEX_PATTERN = "\\s+(outgoing|incoming|pending)\\s+transaction.*from\\s+(\\d+):\\d+\\s+to\\s+(\\d+):\\d+\\s+.*";
    private static final String TAG = "BinderTransactions";
    private int mCheckPid;
    private boolean mRecursiveMode;
    private Map<Integer, HashSet<Integer>> mLocalToRemotesMap = new HashMap();
    private Set<Integer> mRemotePids = new HashSet();

    public BinderTransactions(boolean z) {
        this.mRecursiveMode = z;
    }

    public Set<Integer> getTargetPidsStuckInBinder(int i) {
        this.mCheckPid = i;
        parseFromFile();
        if (!this.mRecursiveMode) {
            if (this.mLocalToRemotesMap.containsKey(Integer.valueOf(i))) {
                Iterator<Integer> it = this.mLocalToRemotesMap.get(Integer.valueOf(i)).iterator();
                while (it.hasNext()) {
                    this.mRemotePids.add(Integer.valueOf(it.next().intValue()));
                }
            }
        } else {
            for (Integer num : this.mLocalToRemotesMap.keySet()) {
                if (num.intValue() == this.mCheckPid) {
                    this.mRemotePids.add(num);
                    findRemotePid(num.intValue());
                }
            }
        }
        final HashSet hashSet = new HashSet();
        this.mRemotePids.forEach(new Consumer() { // from class: com.android.server.am.trace.BinderTransactions$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BinderTransactions.lambda$getTargetPidsStuckInBinder$0(hashSet, (Integer) obj);
            }
        });
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getTargetPidsStuckInBinder$0(Set set, Integer num) {
        if (num.intValue() == 0 || set.size() > 10) {
            return;
        }
        set.add(num);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x004c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x004d A[Catch: IOException -> 0x007a, TryCatch #0 {IOException -> 0x007a, blocks: (B:3:0x0002, B:32:0x0043, B:16:0x004d, B:17:0x0064, B:19:0x006a, B:21:0x0073, B:37:0x0022), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x002a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void binderStateRead(File file) {
        BufferedReader bufferedReader;
        IOException e;
        boolean z;
        IOException e2;
        BufferedReader bufferedReader2;
        try {
            Slog.i(TAG, "Collecting Binder Transaction Status Information");
            try {
                bufferedReader = new BufferedReader(new FileReader(BINDER_TRANSATION_FILE));
                try {
                    Slog.i(TAG, "Collecting Binder state file from binderfs");
                    z = true;
                } catch (IOException e3) {
                    e = e3;
                    Slog.i(TAG, "Binderfs node not found, Trying to collect it from debugfs", e);
                    z = false;
                    if (!z) {
                    }
                    bufferedReader2 = bufferedReader;
                    if (bufferedReader2 == null) {
                    }
                }
            } catch (IOException e4) {
                bufferedReader = null;
                e = e4;
            }
            if (!z) {
                try {
                    bufferedReader2 = new BufferedReader(new FileReader("/sys/kernel/debug/binder/state"));
                    try {
                        Slog.i(TAG, "Collecting Binder state file from debugfs");
                        bufferedReader = bufferedReader2;
                    } catch (IOException e5) {
                        e2 = e5;
                        Slog.i(TAG, "Debugfs node not found", e2);
                        if (bufferedReader2 == null) {
                        }
                    }
                } catch (IOException e6) {
                    BufferedReader bufferedReader3 = bufferedReader;
                    e2 = e6;
                    bufferedReader2 = bufferedReader3;
                }
            }
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 == null) {
                return;
            }
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(new Date());
            FileWriter fileWriter = new FileWriter(file, true);
            while (true) {
                String readLine = bufferedReader2.readLine();
                if (readLine != null) {
                    fileWriter.write(readLine);
                    fileWriter.write(10);
                } else {
                    bufferedReader2.close();
                    fileWriter.close();
                    return;
                }
            }
        } catch (IOException e7) {
            Slog.w(TAG, "Failed to collect state file", e7);
        }
    }

    private void findRemotePid(int i) {
        if (this.mLocalToRemotesMap.containsKey(Integer.valueOf(i))) {
            Iterator<Integer> it = this.mLocalToRemotesMap.get(Integer.valueOf(i)).iterator();
            while (it.hasNext()) {
                Integer next = it.next();
                if (!this.mRemotePids.contains(next)) {
                    this.mRemotePids.add(next);
                    findRemotePid(next.intValue());
                }
            }
        }
    }

    private void parseFromFile() {
        try {
            this.mLocalToRemotesMap.clear();
            this.mRemotePids.clear();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(BINDER_TRANSATION_FILE));
            Pattern compile = Pattern.compile(REGEX_PATTERN);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    Matcher matcher = compile.matcher(readLine);
                    if (matcher.find()) {
                        addItem(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), matcher.group(1).equals("outgoing"));
                    }
                } else {
                    bufferedReader.close();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            Slog.w(TAG, "Unexpected FileNotFoundException", e);
        } catch (IOException e2) {
            Slog.w(TAG, "Unexpected IOException", e2);
        } catch (NumberFormatException e3) {
            Slog.w(TAG, "Unexpected NumberFormatException ", e3);
        }
    }

    private void addItem(int i, int i2, boolean z) {
        if (z) {
            if (this.mLocalToRemotesMap.containsKey(Integer.valueOf(i))) {
                this.mLocalToRemotesMap.get(Integer.valueOf(i)).add(Integer.valueOf(i2));
                return;
            }
            HashSet<Integer> hashSet = new HashSet<>();
            hashSet.add(Integer.valueOf(i2));
            this.mLocalToRemotesMap.put(Integer.valueOf(i), hashSet);
            return;
        }
        if (i2 == this.mCheckPid) {
            this.mRemotePids.add(Integer.valueOf(i));
        }
    }
}
