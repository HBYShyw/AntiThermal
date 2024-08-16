package androidx.window.core;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import ya.l;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SpecificationComputer.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\b \u0018\u0000 \t*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0001:\u0002\u000f\u0010B\u0007¢\u0006\u0004\b\r\u0010\u000eJ*\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0004\u001a\u00020\u00032\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00060\u0005H&J\u0011\u0010\t\u001a\u0004\u0018\u00018\u0000H&¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0003H\u0004¨\u0006\u0011"}, d2 = {"Landroidx/window/core/SpecificationComputer;", "", "T", "", "message", "Lkotlin/Function1;", "", "condition", "c", "a", "()Ljava/lang/Object;", ThermalBaseConfig.Item.ATTR_VALUE, "b", "<init>", "()V", "Companion", "VerificationMode", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public abstract class SpecificationComputer<T> {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* compiled from: SpecificationComputer.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ=\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00010\t\"\b\b\u0001\u0010\u0002*\u00020\u0001*\u00028\u00012\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Landroidx/window/core/SpecificationComputer$Companion;", "", "T", "", TriggerEvent.NOTIFICATION_TAG, "Landroidx/window/core/SpecificationComputer$VerificationMode;", "verificationMode", "Landroidx/window/core/Logger;", "logger", "Landroidx/window/core/SpecificationComputer;", "a", "(Ljava/lang/Object;Ljava/lang/String;Landroidx/window/core/SpecificationComputer$VerificationMode;Landroidx/window/core/Logger;)Landroidx/window/core/SpecificationComputer;", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ SpecificationComputer b(Companion companion, Object obj, String str, VerificationMode verificationMode, Logger logger, int i10, Object obj2) {
            if ((i10 & 2) != 0) {
                verificationMode = BuildConfig.f4306a.a();
            }
            if ((i10 & 4) != 0) {
                logger = AndroidLogger.f4301a;
            }
            return companion.a(obj, str, verificationMode, logger);
        }

        public final <T> SpecificationComputer<T> a(T t7, String str, VerificationMode verificationMode, Logger logger) {
            k.e(t7, "<this>");
            k.e(str, TriggerEvent.NOTIFICATION_TAG);
            k.e(verificationMode, "verificationMode");
            k.e(logger, "logger");
            return new ValidSpecification(t7, str, verificationMode, logger);
        }
    }

    /* compiled from: SpecificationComputer.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Landroidx/window/core/SpecificationComputer$VerificationMode;", "", "(Ljava/lang/String;I)V", "STRICT", "LOG", "QUIET", "window_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum VerificationMode {
        STRICT,
        LOG,
        QUIET
    }

    public abstract T a();

    /* JADX INFO: Access modifiers changed from: protected */
    public final String b(Object value, String message) {
        k.e(value, ThermalBaseConfig.Item.ATTR_VALUE);
        k.e(message, "message");
        return message + " value: " + value;
    }

    public abstract SpecificationComputer<T> c(String str, l<? super T, Boolean> lVar);
}
