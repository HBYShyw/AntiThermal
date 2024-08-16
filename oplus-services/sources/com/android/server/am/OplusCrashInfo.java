package com.android.server.am;

import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.android.server.wm.WindowProcessController;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OplusCrashInfo {
    public ApplicationInfo aInfo;
    public boolean aboveSystem;
    public String activityShortComponentName;
    public String annotation;
    public ProcessRecord anrProcess;
    public Context context;
    public ApplicationErrorReport.CrashInfo crashInfo;
    public File dataFile;
    public String dropboxTag;
    public String eventId;
    public String eventType;
    public WindowProcessController parentProcess;
    public String parentShortComponentName;
    public ProcessRecord process;
    public String processName;
    public String subject;
}
