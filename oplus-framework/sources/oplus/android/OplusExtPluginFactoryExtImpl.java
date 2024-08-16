package oplus.android;

import android.common.IOplusCommonFactory;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.common.OplusFeatureManager;
import android.graphics.ITypefaceExt;
import android.graphics.Typeface;
import android.graphics.TypefaceExtImpl;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusExtPluginFactoryExtImpl implements IOplusExtPluginFactoryExt {
    private static final String TAG = "OplusExtPluginFactoryExtImpl";
    private static volatile OplusExtPluginFactoryExtImpl sInstance = null;
    private IOplusCommonFactory mInnerFactory = new FactoryImplInner();

    private OplusExtPluginFactoryExtImpl() {
    }

    public static IOplusExtPluginFactoryExt getInstance(Object base) {
        if (sInstance == null) {
            synchronized (OplusExtPluginFactoryExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new OplusExtPluginFactoryExtImpl();
                }
            }
        }
        return sInstance;
    }

    public <T extends IOplusCommonFeature> T getFeature(T t, Object... objArr) {
        return (T) this.mInnerFactory.getFeature(t, objArr);
    }

    /* loaded from: classes.dex */
    private static class FactoryImplInner implements IOplusCommonFactory {
        private FactoryImplInner() {
        }

        public boolean isValid(int index) {
            return index < OplusFeatureList.OplusIndex.EndAOSPExtensionPluginFactory.ordinal() && index > OplusFeatureList.OplusIndex.StartAOSPExtensionPluginFactory.ordinal();
        }

        public <T extends IOplusCommonFeature> T getFeature(T t, Object... objArr) {
            verityParams(t);
            if (!OplusFeatureManager.isSupport(t)) {
                return t;
            }
            switch (AnonymousClass1.$SwitchMap$android$common$OplusFeatureList$OplusIndex[t.index().ordinal()]) {
                case 1:
                    return (T) OplusFeatureManager.getTraceMonitor(getTypefaceExt(objArr));
                default:
                    Log.i(OplusExtPluginFactoryExtImpl.TAG, "Unknow feature:" + t.index().name());
                    return t;
            }
        }

        private ITypefaceExt getTypefaceExt(Object... vars) {
            Typeface typeface = (Typeface) vars[0];
            return new TypefaceExtImpl(typeface);
        }
    }

    /* renamed from: oplus.android.OplusExtPluginFactoryExtImpl$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$common$OplusFeatureList$OplusIndex;

        static {
            int[] iArr = new int[OplusFeatureList.OplusIndex.values().length];
            $SwitchMap$android$common$OplusFeatureList$OplusIndex = iArr;
            try {
                iArr[OplusFeatureList.OplusIndex.ITypefaceExt.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }
}
