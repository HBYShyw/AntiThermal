package com.oplus.epona.route;

/* loaded from: classes.dex */
public class RouteInfo {
    public static final int ROUTE_TYPE_ACTIVITY = 0;
    public static final int ROUTE_TYPE_FRAGMENT = 1;
    private final Class<?> mClazz;
    private final String mPath;
    private final int mRouteType;

    public RouteInfo(String str, int i10, Class<?> cls) {
        this.mPath = str;
        this.mRouteType = i10;
        this.mClazz = cls;
    }

    public Class<?> getClazz() {
        return this.mClazz;
    }

    public String getPath() {
        return this.mPath;
    }

    public int getRouteType() {
        return this.mRouteType;
    }
}
