package com.oplus.screenshot;

import android.view.View;
import com.oplus.screenshot.OplusWebViewScrollable;
import com.oplus.util.OplusLog;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class OplusWebViewScrollable implements IOplusScrollable<View> {
    private static final float DEFAULT_SCALE = 1.0f;
    private static final String METHOD_NAME_GET_CONTENT_HEIGHT = "getContentHeight";
    private static final String METHOD_NAME_GET_CONTENT_WIDTH = "getContentWidth";
    private static final String METHOD_NAME_GET_SCALE = "getScale";
    private static final String TAG = "OplusWebViewScrollable";

    @Override // com.oplus.screenshot.IOplusScrollable
    public void scrollBy(View view, int x, int y) {
        view.scrollBy(x, y);
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public void scrollTo(View view, int x, int y) {
        view.scrollTo(x, y);
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getHorizontalScrollOffset(View view) {
        return view.getScrollX();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getHorizontalScrollExtent(View view) {
        return view.getWidth();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getHorizontalScrollRange(View view) {
        return (int) (getContentWidth(view) * getScale(view));
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getVerticalScrollOffset(View view) {
        return view.getScrollY();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getVerticalScrollExtent(View view) {
        return view.getHeight();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getVerticalScrollRange(View view) {
        return (int) (getContentHeight(view) * getScale(view));
    }

    private static int getContentWidth(View view) {
        try {
            Integer result = (Integer) Invoker.invokes(view, Invoker.INTEGER_INVOKER, METHOD_NAME_GET_CONTENT_WIDTH, Integer.TYPE, new Class[0]);
            if (result != null) {
                return result.intValue();
            }
        } catch (ReflectiveOperationException e) {
            OplusLog.d(TAG, "Invoke getContentWidth failed, view is not WebView like.", e);
        }
        return 0;
    }

    private static int getContentHeight(View view) {
        try {
            Integer result = (Integer) Invoker.invokes(view, Invoker.INTEGER_INVOKER, METHOD_NAME_GET_CONTENT_HEIGHT, Integer.TYPE, new Class[0]);
            if (result != null) {
                return result.intValue();
            }
        } catch (ReflectiveOperationException e) {
            OplusLog.d(TAG, "Invoke getContentHeight failed, view is not WebView like.", e);
        }
        return 0;
    }

    private static float getScale(View view) {
        try {
            Float result = (Float) Invoker.invokes(view, Invoker.FLOAT_INVOKER, METHOD_NAME_GET_SCALE, Float.TYPE, new Class[0]);
            if (result != null) {
                return result.floatValue();
            }
            return 1.0f;
        } catch (ReflectiveOperationException e) {
            OplusLog.d(TAG, "Invoke getContentHeight failed, view is not WebView like.", e);
            return 1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface Invoker<T> {
        public static final Invoker<Integer> INTEGER_INVOKER = new Invoker() { // from class: com.oplus.screenshot.OplusWebViewScrollable$Invoker$$ExternalSyntheticLambda0
            @Override // com.oplus.screenshot.OplusWebViewScrollable.Invoker
            public final Object invoke(Method method, Object obj, Object[] objArr) {
                return OplusWebViewScrollable.Invoker.lambda$static$0(method, obj, objArr);
            }
        };
        public static final Invoker<Float> FLOAT_INVOKER = new Invoker() { // from class: com.oplus.screenshot.OplusWebViewScrollable$Invoker$$ExternalSyntheticLambda1
            @Override // com.oplus.screenshot.OplusWebViewScrollable.Invoker
            public final Object invoke(Method method, Object obj, Object[] objArr) {
                return OplusWebViewScrollable.Invoker.lambda$static$1(method, obj, objArr);
            }
        };

        T invoke(Method method, Object obj, Object... objArr) throws ReflectiveOperationException;

        static /* synthetic */ Integer lambda$static$0(Method method, Object obj, Object[] args) throws ReflectiveOperationException {
            return (Integer) method.invoke(obj, args);
        }

        static /* synthetic */ Float lambda$static$1(Method method, Object obj, Object[] args) throws ReflectiveOperationException {
            return (Float) method.invoke(obj, args);
        }

        static <T> T invokes(View view, Invoker<T> invoker, String methodName, Class<T> returnType, Class<?>... parameterTypes) throws ReflectiveOperationException {
            Method method = null;
            for (Class<?> clazz = view.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    method = clazz.getDeclaredMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                }
                if (method.getReturnType() == returnType) {
                    break;
                }
                method = null;
            }
            if (method == null) {
                throw new NoSuchMethodException("Return type of method " + methodName + " is not match");
            }
            return invoker.invoke(method, view, new Object[0]);
        }
    }
}
