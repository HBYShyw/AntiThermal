package android.processor.immutability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public @interface Immutable {

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Ignore {
        String reason() default "";
    }

    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface Policy {

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public enum Exception {
            FINAL_CLASSES_WITH_FINAL_FIELDS
        }

        Exception[] exceptions() default {};
    }
}
