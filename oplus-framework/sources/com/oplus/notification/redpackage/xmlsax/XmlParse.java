package com.oplus.notification.redpackage.xmlsax;

import android.util.Log;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public abstract class XmlParse {
    protected static final String TAG = XmlParse.class.getSimpleName();
    private static Class<DefaultXmlParse> sCalzz = DefaultXmlParse.class;

    public abstract Xml parse(InputStream inputStream);

    public abstract Xml parse(String str);

    public static XmlParse builder() {
        try {
            return sCalzz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            Log.e(TAG, "XmlParse builderException ", e);
            return null;
        }
    }
}
