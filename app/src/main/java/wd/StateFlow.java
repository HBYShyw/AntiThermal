package wd;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;

/* compiled from: StateFlow.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u0002R\u001c\u0010\u0007\u001a\u00028\u00008&@&X¦\u000e¢\u0006\f\u001a\u0004\b\u0003\u0010\u0004\"\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"Lwd/f;", "T", "", "getValue", "()Ljava/lang/Object;", "setValue", "(Ljava/lang/Object;)V", ThermalBaseConfig.Item.ATTR_VALUE, "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: wd.f, reason: use source file name */
/* loaded from: classes2.dex */
public interface StateFlow<T> extends b, FlowCollector {
    void setValue(T t7);
}
