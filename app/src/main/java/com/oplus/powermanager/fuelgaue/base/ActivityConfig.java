package com.oplus.powermanager.fuelgaue.base;

/* loaded from: classes.dex */
public interface ActivityConfig {
    public static final int DEFAULT_STATUS_TYPE = 1;
    public static final int STATUS_DEFAULT = 0;
    public static final int STATUS_IMMERSE = 1;
    public static final int STATUS_TRANSLUCENT = 2;

    int getStatusType();

    boolean isHomeAsUpEnabled();

    boolean isShowMenuDescription();

    boolean isTitleNeedUpdate();
}
