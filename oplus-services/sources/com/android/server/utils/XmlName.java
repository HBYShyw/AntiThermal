package com.android.server.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public @interface XmlName {
    String value();
}
