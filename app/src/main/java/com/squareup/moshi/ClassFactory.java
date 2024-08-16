package com.squareup.moshi;

import com.squareup.moshi.internal.Util;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
abstract class ClassFactory<T> {
    ClassFactory() {
    }

    public static <T> ClassFactory<T> get(final Class<?> cls) {
        try {
            final Constructor<?> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            return new ClassFactory<T>() { // from class: com.squareup.moshi.ClassFactory.1
                @Override // com.squareup.moshi.ClassFactory
                public T newInstance() {
                    return (T) declaredConstructor.newInstance(null);
                }

                public String toString() {
                    return cls.getName();
                }
            };
        } catch (NoSuchMethodException unused) {
            try {
                Class<?> cls2 = Class.forName("sun.misc.Unsafe");
                Field declaredField = cls2.getDeclaredField("theUnsafe");
                declaredField.setAccessible(true);
                final Object obj = declaredField.get(null);
                final Method method = cls2.getMethod("allocateInstance", Class.class);
                return new ClassFactory<T>() { // from class: com.squareup.moshi.ClassFactory.2
                    @Override // com.squareup.moshi.ClassFactory
                    public T newInstance() {
                        return (T) method.invoke(obj, cls);
                    }

                    public String toString() {
                        return cls.getName();
                    }
                };
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException unused2) {
                try {
                    try {
                        Method declaredMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                        declaredMethod.setAccessible(true);
                        final int intValue = ((Integer) declaredMethod.invoke(null, Object.class)).intValue();
                        final Method declaredMethod2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                        declaredMethod2.setAccessible(true);
                        return new ClassFactory<T>() { // from class: com.squareup.moshi.ClassFactory.3
                            @Override // com.squareup.moshi.ClassFactory
                            public T newInstance() {
                                return (T) declaredMethod2.invoke(null, cls, Integer.valueOf(intValue));
                            }

                            public String toString() {
                                return cls.getName();
                            }
                        };
                    } catch (IllegalAccessException unused3) {
                        throw new AssertionError();
                    } catch (NoSuchMethodException unused4) {
                        final Method declaredMethod3 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                        declaredMethod3.setAccessible(true);
                        return new ClassFactory<T>() { // from class: com.squareup.moshi.ClassFactory.4
                            @Override // com.squareup.moshi.ClassFactory
                            public T newInstance() {
                                return (T) declaredMethod3.invoke(null, cls, Object.class);
                            }

                            public String toString() {
                                return cls.getName();
                            }
                        };
                    } catch (InvocationTargetException e10) {
                        throw Util.rethrowCause(e10);
                    }
                } catch (Exception unused5) {
                    throw new IllegalArgumentException("cannot construct instances of " + cls.getName());
                }
            } catch (IllegalAccessException unused6) {
                throw new AssertionError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract T newInstance();
}
