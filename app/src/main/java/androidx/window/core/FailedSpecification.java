package androidx.window.core;

import androidx.window.core.SpecificationComputer;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.collections._Arrays;
import ma.NoWhenBranchMatchedException;
import ya.l;
import za.k;

/* compiled from: SpecificationComputer.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\b\u0012\u0004\u0012\u00028\u00000\u0003B/\u0012\u0006\u0010\u000f\u001a\u00028\u0000\u0012\u0006\u0010\u0013\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u001b\u001a\u00020\u0016\u0012\u0006\u0010!\u001a\u00020\u001c¢\u0006\u0004\b(\u0010)J*\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\u0006\u0010\u0005\u001a\u00020\u00042\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\u0011\u0010\n\u001a\u0004\u0018\u00018\u0000H\u0016¢\u0006\u0004\b\n\u0010\u000bR\u0017\u0010\u000f\u001a\u00028\u00008\u0006¢\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\u000e\u0010\u000bR\u0017\u0010\u0013\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\t\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0005\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0014\u0010\u0010\u001a\u0004\b\u0015\u0010\u0012R\u0017\u0010\u001b\u001a\u00020\u00168\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010!\u001a\u00020\u001c8\u0006¢\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001f\u0010 R\u0017\u0010'\u001a\u00020\"8\u0006¢\u0006\f\n\u0004\b#\u0010$\u001a\u0004\b%\u0010&¨\u0006*"}, d2 = {"Landroidx/window/core/FailedSpecification;", "", "T", "Landroidx/window/core/SpecificationComputer;", "", "message", "Lkotlin/Function1;", "", "condition", "c", "a", "()Ljava/lang/Object;", "b", "Ljava/lang/Object;", "getValue", ThermalBaseConfig.Item.ATTR_VALUE, "Ljava/lang/String;", "getTag", "()Ljava/lang/String;", TriggerEvent.NOTIFICATION_TAG, "d", "getMessage", "Landroidx/window/core/Logger;", "e", "Landroidx/window/core/Logger;", "getLogger", "()Landroidx/window/core/Logger;", "logger", "Landroidx/window/core/SpecificationComputer$VerificationMode;", "f", "Landroidx/window/core/SpecificationComputer$VerificationMode;", "getVerificationMode", "()Landroidx/window/core/SpecificationComputer$VerificationMode;", "verificationMode", "Landroidx/window/core/WindowStrictModeException;", "g", "Landroidx/window/core/WindowStrictModeException;", "getException", "()Landroidx/window/core/WindowStrictModeException;", "exception", "<init>", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Landroidx/window/core/Logger;Landroidx/window/core/SpecificationComputer$VerificationMode;)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class FailedSpecification<T> extends SpecificationComputer<T> {

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final T value;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final String tag;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private final String message;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private final Logger logger;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final SpecificationComputer.VerificationMode verificationMode;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private final WindowStrictModeException exception;

    /* compiled from: SpecificationComputer.kt */
    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f4320a;

        static {
            int[] iArr = new int[SpecificationComputer.VerificationMode.values().length];
            iArr[SpecificationComputer.VerificationMode.STRICT.ordinal()] = 1;
            iArr[SpecificationComputer.VerificationMode.LOG.ordinal()] = 2;
            iArr[SpecificationComputer.VerificationMode.QUIET.ordinal()] = 3;
            f4320a = iArr;
        }
    }

    public FailedSpecification(T t7, String str, String str2, Logger logger, SpecificationComputer.VerificationMode verificationMode) {
        List x10;
        k.e(t7, ThermalBaseConfig.Item.ATTR_VALUE);
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        k.e(str2, "message");
        k.e(logger, "logger");
        k.e(verificationMode, "verificationMode");
        this.value = t7;
        this.tag = str;
        this.message = str2;
        this.logger = logger;
        this.verificationMode = verificationMode;
        WindowStrictModeException windowStrictModeException = new WindowStrictModeException(b(t7, str2));
        StackTraceElement[] stackTrace = windowStrictModeException.getStackTrace();
        k.d(stackTrace, "stackTrace");
        x10 = _Arrays.x(stackTrace, 2);
        Object[] array = x10.toArray(new StackTraceElement[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        windowStrictModeException.setStackTrace((StackTraceElement[]) array);
        this.exception = windowStrictModeException;
    }

    @Override // androidx.window.core.SpecificationComputer
    public T a() {
        int i10 = WhenMappings.f4320a[this.verificationMode.ordinal()];
        if (i10 == 1) {
            throw this.exception;
        }
        if (i10 == 2) {
            this.logger.a(this.tag, b(this.value, this.message));
            return null;
        }
        if (i10 == 3) {
            return null;
        }
        throw new NoWhenBranchMatchedException();
    }

    @Override // androidx.window.core.SpecificationComputer
    public SpecificationComputer<T> c(String str, l<? super T, Boolean> lVar) {
        k.e(str, "message");
        k.e(lVar, "condition");
        return this;
    }
}
