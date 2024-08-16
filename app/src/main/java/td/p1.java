package td;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.n;
import ma.Unit;
import qa.g;
import td.i1;

/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000¦\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u001a\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0004\u009a\u0001\u008d\u0001B\u0012\u0012\u0007\u0010\u0097\u0001\u001a\u00020\u0015¢\u0006\u0006\b\u0098\u0001\u0010\u0099\u0001J#\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b\b\u0010\tJ'\u0010\r\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0006\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002¢\u0006\u0004\b\r\u0010\u000eJ%\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002¢\u0006\u0004\b\u0011\u0010\u0012J!\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b\u0016\u0010\u0017J!\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u001f\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\u001d\u0010\u001eJ\u0017\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u000bH\u0002¢\u0006\u0004\b\u001f\u0010 J\u001d\u0010!\u001a\u00020\u0010*\u00020\u001a2\b\u0010\u001c\u001a\u0004\u0018\u00010\u000bH\u0002¢\u0006\u0004\b!\u0010\u001eJ\u0019\u0010#\u001a\u00020\"2\b\u0010\u0006\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b#\u0010$J1\u0010*\u001a\u00020)2\u0018\u0010'\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0004\u0012\u00020\u00100%j\u0002`&2\u0006\u0010(\u001a\u00020\u0015H\u0002¢\u0006\u0004\b*\u0010+J'\u0010.\u001a\u00020\u00152\u0006\u0010,\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020)H\u0002¢\u0006\u0004\b.\u0010/J\u0017\u00101\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u000200H\u0002¢\u0006\u0004\b1\u00102J\u0017\u00103\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u00020)H\u0002¢\u0006\u0004\b3\u00104J\u001b\u00105\u001a\u0004\u0018\u00010\u00042\b\u0010\u001c\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b5\u00106J\u0019\u00107\u001a\u00020\u000b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b7\u00108J\u001b\u00109\u001a\u0004\u0018\u00010\u00042\b\u0010\u001c\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b9\u00106J\u0019\u0010:\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0006\u001a\u00020\u0013H\u0002¢\u0006\u0004\b:\u0010;J\u001f\u0010<\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u000bH\u0002¢\u0006\u0004\b<\u0010=J%\u0010>\u001a\u0004\u0018\u00010\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b>\u0010?J#\u0010@\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0006\u001a\u00020\u00132\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\b@\u0010AJ\u0019\u0010C\u001a\u0004\u0018\u00010B2\u0006\u0010\u0006\u001a\u00020\u0013H\u0002¢\u0006\u0004\bC\u0010DJ*\u0010F\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010E\u001a\u00020B2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0082\u0010¢\u0006\u0004\bF\u0010GJ)\u0010I\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010H\u001a\u00020B2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\bI\u0010JJ\u0015\u0010L\u001a\u0004\u0018\u00010B*\u00020KH\u0002¢\u0006\u0004\bL\u0010MJ\u0019\u0010O\u001a\u00020N2\b\u0010\u0006\u001a\u0004\u0018\u00010\u0004H\u0002¢\u0006\u0004\bO\u0010PJ\u0019\u0010R\u001a\u00020\u00102\b\u0010Q\u001a\u0004\u0018\u00010\u0001H\u0004¢\u0006\u0004\bR\u0010SJ\r\u0010T\u001a\u00020\u0015¢\u0006\u0004\bT\u0010UJ\u000f\u0010V\u001a\u00020\u0010H\u0014¢\u0006\u0004\bV\u0010WJ\u0011\u0010Z\u001a\u00060Xj\u0002`Y¢\u0006\u0004\bZ\u0010[J#\u0010]\u001a\u00060Xj\u0002`Y*\u00020\u000b2\n\b\u0002\u0010\\\u001a\u0004\u0018\u00010NH\u0004¢\u0006\u0004\b]\u0010^J7\u0010a\u001a\u00020`2\u0006\u0010(\u001a\u00020\u00152\u0006\u0010_\u001a\u00020\u00152\u0018\u0010'\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0004\u0012\u00020\u00100%j\u0002`&¢\u0006\u0004\ba\u0010bJ\u0017\u0010c\u001a\u00020\u00102\u0006\u0010-\u001a\u00020)H\u0000¢\u0006\u0004\bc\u00104J\u001f\u0010d\u001a\u00020\u00102\u000e\u0010\u001c\u001a\n\u0018\u00010Xj\u0004\u0018\u0001`YH\u0016¢\u0006\u0004\bd\u0010eJ\u000f\u0010f\u001a\u00020NH\u0014¢\u0006\u0004\bf\u0010gJ\u0017\u0010h\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u000bH\u0016¢\u0006\u0004\bh\u0010iJ\u0015\u0010k\u001a\u00020\u00102\u0006\u0010j\u001a\u00020\u0003¢\u0006\u0004\bk\u0010lJ\u0017\u0010m\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u000bH\u0016¢\u0006\u0004\bm\u0010 J\u0019\u0010n\u001a\u00020\u00152\b\u0010\u001c\u001a\u0004\u0018\u00010\u0004H\u0000¢\u0006\u0004\bn\u0010oJ\u0013\u0010p\u001a\u00060Xj\u0002`YH\u0016¢\u0006\u0004\bp\u0010[J\u001b\u0010q\u001a\u0004\u0018\u00010\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0004H\u0000¢\u0006\u0004\bq\u00106J\u0015\u0010s\u001a\u00020r2\u0006\u0010E\u001a\u00020\u0002¢\u0006\u0004\bs\u0010tJ\u0017\u0010v\u001a\u00020\u00102\u0006\u0010u\u001a\u00020\u000bH\u0010¢\u0006\u0004\bv\u0010iJ\u0019\u0010w\u001a\u00020\u00102\b\u0010\u001c\u001a\u0004\u0018\u00010\u000bH\u0014¢\u0006\u0004\bw\u0010iJ\u0017\u0010x\u001a\u00020\u00152\u0006\u0010u\u001a\u00020\u000bH\u0014¢\u0006\u0004\bx\u0010 J\u0019\u0010y\u001a\u00020\u00102\b\u0010\u0006\u001a\u0004\u0018\u00010\u0004H\u0014¢\u0006\u0004\by\u0010zJ\u0019\u0010{\u001a\u00020\u00102\b\u0010\u0006\u001a\u0004\u0018\u00010\u0004H\u0014¢\u0006\u0004\b{\u0010zJ\u000f\u0010|\u001a\u00020NH\u0016¢\u0006\u0004\b|\u0010gJ\u000f\u0010}\u001a\u00020NH\u0007¢\u0006\u0004\b}\u0010gJ\u000f\u0010~\u001a\u00020NH\u0010¢\u0006\u0004\b~\u0010gR\u001d\u0010\u0080\u0001\u001a\u0004\u0018\u00010\u000b*\u0004\u0018\u00010\u00048BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u007f\u00108R\u0019\u0010\u0084\u0001\u001a\u0007\u0012\u0002\b\u00030\u0081\u00018F¢\u0006\b\u001a\u0006\b\u0082\u0001\u0010\u0083\u0001R.\u0010\u008a\u0001\u001a\u0004\u0018\u00010r2\t\u0010\u0085\u0001\u001a\u0004\u0018\u00010r8@@@X\u0080\u000e¢\u0006\u0010\u001a\u0006\b\u0086\u0001\u0010\u0087\u0001\"\u0006\b\u0088\u0001\u0010\u0089\u0001R\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00048@X\u0080\u0004¢\u0006\b\u001a\u0006\b\u008b\u0001\u0010\u008c\u0001R\u0016\u0010\u008e\u0001\u001a\u00020\u00158VX\u0096\u0004¢\u0006\u0007\u001a\u0005\b\u008d\u0001\u0010UR\u0013\u0010\u0090\u0001\u001a\u00020\u00158F¢\u0006\u0007\u001a\u0005\b\u008f\u0001\u0010UR\u0016\u0010\u0092\u0001\u001a\u00020\u00158PX\u0090\u0004¢\u0006\u0007\u001a\u0005\b\u0091\u0001\u0010UR\u0016\u0010\u0094\u0001\u001a\u00020\u00158TX\u0094\u0004¢\u0006\u0007\u001a\u0005\b\u0093\u0001\u0010UR\u0016\u0010\u0096\u0001\u001a\u00020\u00158PX\u0090\u0004¢\u0006\u0007\u001a\u0005\b\u0095\u0001\u0010U¨\u0006\u009b\u0001"}, d2 = {"Ltd/p1;", "Ltd/i1;", "Ltd/s;", "Ltd/w1;", "", "Ltd/p1$b;", "state", "proposedUpdate", "B", "(Ltd/p1$b;Ljava/lang/Object;)Ljava/lang/Object;", "", "", "exceptions", "E", "(Ltd/p1$b;Ljava/util/List;)Ljava/lang/Throwable;", "rootCause", "Lma/f0;", "n", "(Ljava/lang/Throwable;Ljava/util/List;)V", "Ltd/d1;", "update", "", "u0", "(Ltd/d1;Ljava/lang/Object;)Z", "y", "(Ltd/d1;Ljava/lang/Object;)V", "Ltd/t1;", "list", "cause", "Z", "(Ltd/t1;Ljava/lang/Throwable;)V", "s", "(Ljava/lang/Throwable;)Z", "b0", "", "p0", "(Ljava/lang/Object;)I", "Lkotlin/Function1;", "Lkotlinx/coroutines/CompletionHandler;", "handler", "onCancelling", "Ltd/o1;", "V", "(Lya/l;Z)Ltd/o1;", "expect", "node", "k", "(Ljava/lang/Object;Ltd/t1;Ltd/o1;)Z", "Ltd/v0;", "g0", "(Ltd/v0;)V", "k0", "(Ltd/o1;)V", "r", "(Ljava/lang/Object;)Ljava/lang/Object;", "A", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "R", "H", "(Ltd/d1;)Ltd/t1;", "v0", "(Ltd/d1;Ljava/lang/Throwable;)Z", "w0", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "x0", "(Ltd/d1;Ljava/lang/Object;)Ljava/lang/Object;", "Ltd/r;", "C", "(Ltd/d1;)Ltd/r;", "child", "y0", "(Ltd/p1$b;Ltd/r;Ljava/lang/Object;)Z", "lastChild", "z", "(Ltd/p1$b;Ltd/r;Ljava/lang/Object;)V", "Lkotlinx/coroutines/internal/n;", "Y", "(Lkotlinx/coroutines/internal/n;)Ltd/r;", "", "q0", "(Ljava/lang/Object;)Ljava/lang/String;", "parent", "O", "(Ltd/i1;)V", "start", "()Z", "f0", "()V", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "w", "()Ljava/util/concurrent/CancellationException;", "message", "r0", "(Ljava/lang/Throwable;Ljava/lang/String;)Ljava/util/concurrent/CancellationException;", "invokeImmediately", "Ltd/u0;", "v", "(ZZLya/l;)Ltd/u0;", "l0", "m0", "(Ljava/util/concurrent/CancellationException;)V", "t", "()Ljava/lang/String;", "q", "(Ljava/lang/Throwable;)V", "parentJob", "e0", "(Ltd/w1;)V", "x", "p", "(Ljava/lang/Object;)Z", "h0", "T", "Ltd/q;", "U", "(Ltd/s;)Ltd/q;", "exception", "M", "c0", "K", "d0", "(Ljava/lang/Object;)V", "o", "toString", "t0", "W", "D", "exceptionOrNull", "Lqa/g$c;", "getKey", "()Lqa/g$c;", "key", ThermalBaseConfig.Item.ATTR_VALUE, "I", "()Ltd/q;", "n0", "(Ltd/q;)V", "parentHandle", "J", "()Ljava/lang/Object;", "b", "isActive", "P", "isCompleted", "G", "onCancelComplete", "Q", "isScopedCoroutine", "F", "handlesException", "active", "<init>", "(Z)V", "a", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class p1 implements i1, s, w1 {

    /* renamed from: e, reason: collision with root package name */
    private static final /* synthetic */ AtomicReferenceFieldUpdater f18770e = AtomicReferenceFieldUpdater.newUpdater(p1.class, Object.class, "_state");
    private volatile /* synthetic */ Object _parentHandle;
    private volatile /* synthetic */ Object _state;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\b\u0010\r\u001a\u0004\u0018\u00010\f¢\u0006\u0004\b\u000e\u0010\u000fJ\u0013\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0096\u0002¨\u0006\u0010"}, d2 = {"Ltd/p1$a;", "Ltd/o1;", "", "cause", "Lma/f0;", "F", "Ltd/p1;", "parent", "Ltd/p1$b;", "state", "Ltd/r;", "child", "", "proposedUpdate", "<init>", "(Ltd/p1;Ltd/p1$b;Ltd/r;Ljava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class a extends o1 {

        /* renamed from: i, reason: collision with root package name */
        private final p1 f18771i;

        /* renamed from: j, reason: collision with root package name */
        private final b f18772j;

        /* renamed from: k, reason: collision with root package name */
        private final r f18773k;

        /* renamed from: l, reason: collision with root package name */
        private final Object f18774l;

        public a(p1 p1Var, b bVar, r rVar, Object obj) {
            this.f18771i = p1Var;
            this.f18772j = bVar;
            this.f18773k = rVar;
            this.f18774l = obj;
        }

        @Override // td.x
        public void F(Throwable th) {
            this.f18771i.z(this.f18772j, this.f18773k, this.f18774l);
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            F(th);
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    @Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0012\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u00022\u00020\u0003B!\u0012\u0006\u0010\u001b\u001a\u00020\u001a\u0012\u0006\u0010$\u001a\u00020\u001f\u0012\b\u0010(\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b/\u00100J\u001f\u0010\u0007\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006H\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u001d\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\b\u0010\t\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u000b\u0010\fJ\u0015\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u0005¢\u0006\u0004\b\u000f\u0010\u0010J\u000f\u0010\u0012\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0012\u0010\u0013R(\u0010\u0019\u001a\u0004\u0018\u00010\u00012\b\u0010\u0014\u001a\u0004\u0018\u00010\u00018B@BX\u0082\u000e¢\u0006\f\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u001b\u001a\u00020\u001a8\u0016X\u0096\u0004¢\u0006\f\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u001d\u0010\u001eR$\u0010$\u001a\u00020\u001f2\u0006\u0010\u0014\u001a\u00020\u001f8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R(\u0010(\u001a\u0004\u0018\u00010\u00052\b\u0010\u0014\u001a\u0004\u0018\u00010\u00058F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b%\u0010&\"\u0004\b'\u0010\u0010R\u0011\u0010*\u001a\u00020\u001f8F¢\u0006\u0006\u001a\u0004\b)\u0010!R\u0011\u0010,\u001a\u00020\u001f8F¢\u0006\u0006\u001a\u0004\b+\u0010!R\u0014\u0010.\u001a\u00020\u001f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b-\u0010!¨\u00061"}, d2 = {"Ltd/p1$b;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "Ltd/d1;", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "c", "()Ljava/util/ArrayList;", "proposedException", "", "j", "(Ljava/lang/Throwable;)Ljava/util/List;", "exception", "Lma/f0;", "a", "(Ljava/lang/Throwable;)V", "", "toString", "()Ljava/lang/String;", ThermalBaseConfig.Item.ATTR_VALUE, "d", "()Ljava/lang/Object;", "l", "(Ljava/lang/Object;)V", "exceptionsHolder", "Ltd/t1;", "list", "Ltd/t1;", "f", "()Ltd/t1;", "", "h", "()Z", "k", "(Z)V", "isCompleting", "e", "()Ljava/lang/Throwable;", "m", "rootCause", "i", "isSealed", "g", "isCancelling", "b", "isActive", "<init>", "(Ltd/t1;ZLjava/lang/Throwable;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class b implements d1 {
        private volatile /* synthetic */ Object _exceptionsHolder = null;
        private volatile /* synthetic */ int _isCompleting;
        private volatile /* synthetic */ Object _rootCause;

        /* renamed from: e, reason: collision with root package name */
        private final t1 f18775e;

        public b(t1 t1Var, boolean z10, Throwable th) {
            this.f18775e = t1Var;
            this._isCompleting = z10 ? 1 : 0;
            this._rootCause = th;
        }

        private final ArrayList<Throwable> c() {
            return new ArrayList<>(4);
        }

        /* renamed from: d, reason: from getter */
        private final Object get_exceptionsHolder() {
            return this._exceptionsHolder;
        }

        private final void l(Object obj) {
            this._exceptionsHolder = obj;
        }

        public final void a(Throwable exception) {
            Throwable e10 = e();
            if (e10 == null) {
                m(exception);
                return;
            }
            if (exception == e10) {
                return;
            }
            Object obj = get_exceptionsHolder();
            if (obj == null) {
                l(exception);
                return;
            }
            if (obj instanceof Throwable) {
                if (exception == obj) {
                    return;
                }
                ArrayList<Throwable> c10 = c();
                c10.add(obj);
                c10.add(exception);
                l(c10);
                return;
            }
            if (obj instanceof ArrayList) {
                ((ArrayList) obj).add(exception);
                return;
            }
            throw new IllegalStateException(("State is " + obj).toString());
        }

        @Override // td.d1
        /* renamed from: b */
        public boolean getF18801e() {
            return e() == null;
        }

        public final Throwable e() {
            return (Throwable) this._rootCause;
        }

        @Override // td.d1
        /* renamed from: f, reason: from getter */
        public t1 getF18726e() {
            return this.f18775e;
        }

        public final boolean g() {
            return e() != null;
        }

        /* JADX WARN: Type inference failed for: r0v1, types: [int, boolean] */
        public final boolean h() {
            return this._isCompleting;
        }

        public final boolean i() {
            Symbol symbol;
            Object obj = get_exceptionsHolder();
            symbol = q1.f18783e;
            return obj == symbol;
        }

        public final List<Throwable> j(Throwable proposedException) {
            ArrayList<Throwable> arrayList;
            Symbol symbol;
            Object obj = get_exceptionsHolder();
            if (obj == null) {
                arrayList = c();
            } else if (obj instanceof Throwable) {
                ArrayList<Throwable> c10 = c();
                c10.add(obj);
                arrayList = c10;
            } else {
                if (!(obj instanceof ArrayList)) {
                    throw new IllegalStateException(("State is " + obj).toString());
                }
                arrayList = (ArrayList) obj;
            }
            Throwable e10 = e();
            if (e10 != null) {
                arrayList.add(0, e10);
            }
            if (proposedException != null && !za.k.a(proposedException, e10)) {
                arrayList.add(proposedException);
            }
            symbol = q1.f18783e;
            l(symbol);
            return arrayList;
        }

        public final void k(boolean z10) {
            this._isCompleting = z10 ? 1 : 0;
        }

        public final void m(Throwable th) {
            this._rootCause = th;
        }

        public String toString() {
            return "Finishing[cancelling=" + g() + ", completing=" + h() + ", rootCause=" + e() + ", exceptions=" + get_exceptionsHolder() + ", list=" + getF18726e() + ']';
        }
    }

    /* compiled from: LockFreeLinkedList.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\u0010\u0004\u001a\u00060\u0002j\u0002`\u0003H\u0016¨\u0006\u0007"}, d2 = {"td/p1$c", "Lkotlinx/coroutines/internal/n$a;", "Lkotlinx/coroutines/internal/n;", "Lkotlinx/coroutines/internal/Node;", "affected", "", "i", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes2.dex */
    public static final class c extends n.a {

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ p1 f18776d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Object f18777e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(kotlinx.coroutines.internal.n nVar, p1 p1Var, Object obj) {
            super(nVar);
            this.f18776d = p1Var;
            this.f18777e = obj;
        }

        @Override // kotlinx.coroutines.internal.c
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public Object g(kotlinx.coroutines.internal.n affected) {
            if (this.f18776d.J() == this.f18777e) {
                return null;
            }
            return kotlinx.coroutines.internal.m.a();
        }
    }

    public p1(boolean z10) {
        this._state = z10 ? q1.f18785g : q1.f18784f;
        this._parentHandle = null;
    }

    private final Throwable A(Object cause) {
        if (cause == null ? true : cause instanceof Throwable) {
            Throwable th = (Throwable) cause;
            return th == null ? new j1(t(), null, this) : th;
        }
        Objects.requireNonNull(cause, "null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
        return ((w1) cause).h0();
    }

    private final Object B(b state, Object proposedUpdate) {
        boolean g6;
        Throwable E;
        v vVar = proposedUpdate instanceof v ? (v) proposedUpdate : null;
        Throwable th = vVar != null ? vVar.f18800a : null;
        synchronized (state) {
            g6 = state.g();
            List<Throwable> j10 = state.j(th);
            E = E(state, j10);
            if (E != null) {
                n(E, j10);
            }
        }
        if (E != null && E != th) {
            proposedUpdate = new v(E, false, 2, null);
        }
        if (E != null) {
            if (s(E) || K(E)) {
                Objects.requireNonNull(proposedUpdate, "null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                ((v) proposedUpdate).b();
            }
        }
        if (!g6) {
            c0(E);
        }
        d0(proposedUpdate);
        f18770e.compareAndSet(this, state, q1.g(proposedUpdate));
        y(state, proposedUpdate);
        return proposedUpdate;
    }

    private final r C(d1 state) {
        r rVar = state instanceof r ? (r) state : null;
        if (rVar != null) {
            return rVar;
        }
        t1 f18726e = state.getF18726e();
        if (f18726e != null) {
            return Y(f18726e);
        }
        return null;
    }

    private final Throwable D(Object obj) {
        v vVar = obj instanceof v ? (v) obj : null;
        if (vVar != null) {
            return vVar.f18800a;
        }
        return null;
    }

    private final Throwable E(b state, List<? extends Throwable> exceptions) {
        Object obj = null;
        if (exceptions.isEmpty()) {
            if (state.g()) {
                return new j1(t(), null, this);
            }
            return null;
        }
        Iterator<T> it = exceptions.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            if (!(((Throwable) next) instanceof CancellationException)) {
                obj = next;
                break;
            }
        }
        Throwable th = (Throwable) obj;
        return th != null ? th : exceptions.get(0);
    }

    private final t1 H(d1 state) {
        t1 f18726e = state.getF18726e();
        if (f18726e != null) {
            return f18726e;
        }
        if (state instanceof v0) {
            return new t1();
        }
        if (state instanceof o1) {
            k0((o1) state);
            return null;
        }
        throw new IllegalStateException(("State should have list: " + state).toString());
    }

    private final Object R(Object cause) {
        Symbol symbol;
        Symbol symbol2;
        Symbol symbol3;
        Symbol symbol4;
        Symbol symbol5;
        Symbol symbol6;
        Throwable th = null;
        while (true) {
            Object J = J();
            if (J instanceof b) {
                synchronized (J) {
                    if (((b) J).i()) {
                        symbol2 = q1.f18782d;
                        return symbol2;
                    }
                    boolean g6 = ((b) J).g();
                    if (cause != null || !g6) {
                        if (th == null) {
                            th = A(cause);
                        }
                        ((b) J).a(th);
                    }
                    Throwable e10 = g6 ^ true ? ((b) J).e() : null;
                    if (e10 != null) {
                        Z(((b) J).getF18726e(), e10);
                    }
                    symbol = q1.f18779a;
                    return symbol;
                }
            }
            if (J instanceof d1) {
                if (th == null) {
                    th = A(cause);
                }
                d1 d1Var = (d1) J;
                if (d1Var.getF18801e()) {
                    if (v0(d1Var, th)) {
                        symbol4 = q1.f18779a;
                        return symbol4;
                    }
                } else {
                    Object w02 = w0(J, new v(th, false, 2, null));
                    symbol5 = q1.f18779a;
                    if (w02 != symbol5) {
                        symbol6 = q1.f18781c;
                        if (w02 != symbol6) {
                            return w02;
                        }
                    } else {
                        throw new IllegalStateException(("Cannot happen in " + J).toString());
                    }
                }
            } else {
                symbol3 = q1.f18782d;
                return symbol3;
            }
        }
    }

    private final o1 V(ya.l<? super Throwable, Unit> handler, boolean onCancelling) {
        o1 o1Var;
        if (onCancelling) {
            o1Var = handler instanceof k1 ? (k1) handler : null;
            if (o1Var == null) {
                o1Var = new g1(handler);
            }
        } else {
            o1Var = handler instanceof o1 ? (o1) handler : null;
            if (o1Var == null) {
                o1Var = new h1(handler);
            }
        }
        o1Var.H(this);
        return o1Var;
    }

    private final r Y(kotlinx.coroutines.internal.n nVar) {
        while (nVar.z()) {
            nVar = nVar.v();
        }
        while (true) {
            nVar = nVar.u();
            if (!nVar.z()) {
                if (nVar instanceof r) {
                    return (r) nVar;
                }
                if (nVar instanceof t1) {
                    return null;
                }
            }
        }
    }

    private final void Z(t1 list, Throwable cause) {
        c0(cause);
        y yVar = null;
        for (kotlinx.coroutines.internal.n nVar = (kotlinx.coroutines.internal.n) list.t(); !za.k.a(nVar, list); nVar = nVar.u()) {
            if (nVar instanceof k1) {
                o1 o1Var = (o1) nVar;
                try {
                    o1Var.F(cause);
                } catch (Throwable th) {
                    if (yVar != null) {
                        ma.b.a(yVar, th);
                    } else {
                        yVar = new y("Exception in completion handler " + o1Var + " for " + this, th);
                        Unit unit = Unit.f15173a;
                    }
                }
            }
        }
        if (yVar != null) {
            M(yVar);
        }
        s(cause);
    }

    private final void b0(t1 t1Var, Throwable th) {
        y yVar = null;
        for (kotlinx.coroutines.internal.n nVar = (kotlinx.coroutines.internal.n) t1Var.t(); !za.k.a(nVar, t1Var); nVar = nVar.u()) {
            if (nVar instanceof o1) {
                o1 o1Var = (o1) nVar;
                try {
                    o1Var.F(th);
                } catch (Throwable th2) {
                    if (yVar != null) {
                        ma.b.a(yVar, th2);
                    } else {
                        yVar = new y("Exception in completion handler " + o1Var + " for " + this, th2);
                        Unit unit = Unit.f15173a;
                    }
                }
            }
        }
        if (yVar != null) {
            M(yVar);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [td.c1] */
    private final void g0(v0 state) {
        t1 t1Var = new t1();
        if (!state.getF18801e()) {
            t1Var = new c1(t1Var);
        }
        f18770e.compareAndSet(this, state, t1Var);
    }

    private final boolean k(Object expect, t1 list, o1 node) {
        int E;
        c cVar = new c(node, this, expect);
        do {
            E = list.v().E(node, list, cVar);
            if (E == 1) {
                return true;
            }
        } while (E != 2);
        return false;
    }

    private final void k0(o1 state) {
        state.p(new t1());
        f18770e.compareAndSet(this, state, state.u());
    }

    private final void n(Throwable rootCause, List<? extends Throwable> exceptions) {
        if (exceptions.size() <= 1) {
            return;
        }
        Set newSetFromMap = Collections.newSetFromMap(new IdentityHashMap(exceptions.size()));
        for (Throwable th : exceptions) {
            if (th != rootCause && th != rootCause && !(th instanceof CancellationException) && newSetFromMap.add(th)) {
                ma.b.a(rootCause, th);
            }
        }
    }

    private final int p0(Object state) {
        v0 v0Var;
        if (state instanceof v0) {
            if (((v0) state).getF18801e()) {
                return 0;
            }
            AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = f18770e;
            v0Var = q1.f18785g;
            if (!atomicReferenceFieldUpdater.compareAndSet(this, state, v0Var)) {
                return -1;
            }
            f0();
            return 1;
        }
        if (!(state instanceof c1)) {
            return 0;
        }
        if (!f18770e.compareAndSet(this, state, ((c1) state).getF18726e())) {
            return -1;
        }
        f0();
        return 1;
    }

    private final String q0(Object state) {
        if (!(state instanceof b)) {
            return state instanceof d1 ? ((d1) state).getF18801e() ? "Active" : "New" : state instanceof v ? "Cancelled" : "Completed";
        }
        b bVar = (b) state;
        return bVar.g() ? "Cancelling" : bVar.h() ? "Completing" : "Active";
    }

    private final Object r(Object cause) {
        Symbol symbol;
        Object w02;
        Symbol symbol2;
        do {
            Object J = J();
            if ((J instanceof d1) && (!(J instanceof b) || !((b) J).h())) {
                w02 = w0(J, new v(A(cause), false, 2, null));
                symbol2 = q1.f18781c;
            } else {
                symbol = q1.f18779a;
                return symbol;
            }
        } while (w02 == symbol2);
        return w02;
    }

    private final boolean s(Throwable cause) {
        if (Q()) {
            return true;
        }
        boolean z10 = cause instanceof CancellationException;
        q I = I();
        return (I == null || I == u1.f18798e) ? z10 : I.d(cause) || z10;
    }

    public static /* synthetic */ CancellationException s0(p1 p1Var, Throwable th, String str, int i10, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toCancellationException");
        }
        if ((i10 & 1) != 0) {
            str = null;
        }
        return p1Var.r0(th, str);
    }

    private final boolean u0(d1 state, Object update) {
        if (!f18770e.compareAndSet(this, state, q1.g(update))) {
            return false;
        }
        c0(null);
        d0(update);
        y(state, update);
        return true;
    }

    private final boolean v0(d1 state, Throwable rootCause) {
        t1 H = H(state);
        if (H == null) {
            return false;
        }
        if (!f18770e.compareAndSet(this, state, new b(H, false, rootCause))) {
            return false;
        }
        Z(H, rootCause);
        return true;
    }

    private final Object w0(Object state, Object proposedUpdate) {
        Symbol symbol;
        Symbol symbol2;
        if (!(state instanceof d1)) {
            symbol2 = q1.f18779a;
            return symbol2;
        }
        if (((state instanceof v0) || (state instanceof o1)) && !(state instanceof r) && !(proposedUpdate instanceof v)) {
            if (u0((d1) state, proposedUpdate)) {
                return proposedUpdate;
            }
            symbol = q1.f18781c;
            return symbol;
        }
        return x0((d1) state, proposedUpdate);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.Throwable, T] */
    /* JADX WARN: Type inference failed for: r2v2 */
    private final Object x0(d1 state, Object proposedUpdate) {
        Symbol symbol;
        Symbol symbol2;
        Symbol symbol3;
        t1 H = H(state);
        if (H == null) {
            symbol3 = q1.f18781c;
            return symbol3;
        }
        b bVar = state instanceof b ? (b) state : null;
        if (bVar == null) {
            bVar = new b(H, false, null);
        }
        za.y yVar = new za.y();
        synchronized (bVar) {
            if (bVar.h()) {
                symbol2 = q1.f18779a;
                return symbol2;
            }
            bVar.k(true);
            if (bVar != state && !f18770e.compareAndSet(this, state, bVar)) {
                symbol = q1.f18781c;
                return symbol;
            }
            boolean g6 = bVar.g();
            v vVar = proposedUpdate instanceof v ? (v) proposedUpdate : null;
            if (vVar != null) {
                bVar.a(vVar.f18800a);
            }
            ?? e10 = Boolean.valueOf(g6 ? false : true).booleanValue() ? bVar.e() : 0;
            yVar.f20376e = e10;
            Unit unit = Unit.f15173a;
            if (e10 != 0) {
                Z(H, e10);
            }
            r C = C(state);
            if (C != null && y0(bVar, C, proposedUpdate)) {
                return q1.f18780b;
            }
            return B(bVar, proposedUpdate);
        }
    }

    private final void y(d1 state, Object update) {
        q I = I();
        if (I != null) {
            I.a();
            n0(u1.f18798e);
        }
        v vVar = update instanceof v ? (v) update : null;
        Throwable th = vVar != null ? vVar.f18800a : null;
        if (state instanceof o1) {
            try {
                ((o1) state).F(th);
                return;
            } catch (Throwable th2) {
                M(new y("Exception in completion handler " + state + " for " + this, th2));
                return;
            }
        }
        t1 f18726e = state.getF18726e();
        if (f18726e != null) {
            b0(f18726e, th);
        }
    }

    private final boolean y0(b state, r child, Object proposedUpdate) {
        while (i1.a.d(child.f18786i, false, false, new a(this, state, child, proposedUpdate), 1, null) == u1.f18798e) {
            child = Y(child);
            if (child == null) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void z(b state, r lastChild, Object proposedUpdate) {
        r Y = Y(lastChild);
        if (Y == null || !y0(state, Y, proposedUpdate)) {
            o(B(state, proposedUpdate));
        }
    }

    /* renamed from: F */
    public boolean getF18760f() {
        return true;
    }

    public boolean G() {
        return false;
    }

    public final q I() {
        return (q) this._parentHandle;
    }

    public final Object J() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof kotlinx.coroutines.internal.v)) {
                return obj;
            }
            ((kotlinx.coroutines.internal.v) obj).c(this);
        }
    }

    protected boolean K(Throwable exception) {
        return false;
    }

    public void M(Throwable exception) {
        throw exception;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void O(i1 parent) {
        if (parent == null) {
            n0(u1.f18798e);
            return;
        }
        parent.start();
        q U = parent.U(this);
        n0(U);
        if (P()) {
            U.a();
            n0(u1.f18798e);
        }
    }

    public final boolean P() {
        return !(J() instanceof d1);
    }

    protected boolean Q() {
        return false;
    }

    public final Object T(Object proposedUpdate) {
        Object w02;
        Symbol symbol;
        Symbol symbol2;
        do {
            w02 = w0(J(), proposedUpdate);
            symbol = q1.f18779a;
            if (w02 != symbol) {
                symbol2 = q1.f18781c;
            } else {
                throw new IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + proposedUpdate, D(proposedUpdate));
            }
        } while (w02 == symbol2);
        return w02;
    }

    @Override // td.i1
    public final q U(s child) {
        return (q) i1.a.d(this, true, false, new r(child), 2, null);
    }

    public String W() {
        return DebugStrings.a(this);
    }

    @Override // td.i1
    public boolean b() {
        Object J = J();
        return (J instanceof d1) && ((d1) J).getF18801e();
    }

    @Override // qa.g.b, qa.g
    public <E extends g.b> E c(g.c<E> cVar) {
        return (E) i1.a.c(this, cVar);
    }

    protected void c0(Throwable cause) {
    }

    protected void d0(Object state) {
    }

    @Override // td.s
    public final void e0(w1 parentJob) {
        p(parentJob);
    }

    protected void f0() {
    }

    @Override // qa.g.b
    public final g.c<?> getKey() {
        return i1.f18746d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.lang.Throwable] */
    @Override // td.w1
    public CancellationException h0() {
        CancellationException cancellationException;
        Object J = J();
        if (J instanceof b) {
            cancellationException = ((b) J).e();
        } else if (J instanceof v) {
            cancellationException = ((v) J).f18800a;
        } else {
            if (J instanceof d1) {
                throw new IllegalStateException(("Cannot be cancelling child in this state: " + J).toString());
            }
            cancellationException = null;
        }
        CancellationException cancellationException2 = cancellationException instanceof CancellationException ? cancellationException : null;
        if (cancellationException2 != null) {
            return cancellationException2;
        }
        return new j1("Parent job is " + q0(J), cancellationException, this);
    }

    @Override // qa.g
    public <R> R i0(R r10, ya.p<? super R, ? super g.b, ? extends R> pVar) {
        return (R) i1.a.b(this, r10, pVar);
    }

    @Override // qa.g
    public qa.g j0(g.c<?> cVar) {
        return i1.a.e(this, cVar);
    }

    public final void l0(o1 node) {
        Object J;
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater;
        v0 v0Var;
        do {
            J = J();
            if (!(J instanceof o1)) {
                if (!(J instanceof d1) || ((d1) J).getF18726e() == null) {
                    return;
                }
                node.A();
                return;
            }
            if (J != node) {
                return;
            }
            atomicReferenceFieldUpdater = f18770e;
            v0Var = q1.f18785g;
        } while (!atomicReferenceFieldUpdater.compareAndSet(this, J, v0Var));
    }

    @Override // td.i1
    public void m0(CancellationException cause) {
        if (cause == null) {
            cause = new j1(t(), null, this);
        }
        q(cause);
    }

    public final void n0(q qVar) {
        this._parentHandle = qVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(Object state) {
    }

    @Override // qa.g
    public qa.g o0(qa.g gVar) {
        return i1.a.f(this, gVar);
    }

    public final boolean p(Object cause) {
        Object obj;
        Symbol symbol;
        Symbol symbol2;
        Symbol symbol3;
        obj = q1.f18779a;
        if (G() && (obj = r(cause)) == q1.f18780b) {
            return true;
        }
        symbol = q1.f18779a;
        if (obj == symbol) {
            obj = R(cause);
        }
        symbol2 = q1.f18779a;
        if (obj == symbol2 || obj == q1.f18780b) {
            return true;
        }
        symbol3 = q1.f18782d;
        if (obj == symbol3) {
            return false;
        }
        o(obj);
        return true;
    }

    public void q(Throwable cause) {
        p(cause);
    }

    protected final CancellationException r0(Throwable th, String str) {
        CancellationException cancellationException = th instanceof CancellationException ? (CancellationException) th : null;
        if (cancellationException == null) {
            if (str == null) {
                str = t();
            }
            cancellationException = new j1(str, th, this);
        }
        return cancellationException;
    }

    @Override // td.i1
    public final boolean start() {
        int p02;
        do {
            p02 = p0(J());
            if (p02 == 0) {
                return false;
            }
        } while (p02 != 1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String t() {
        return "Job was cancelled";
    }

    public final String t0() {
        return W() + '{' + q0(J()) + '}';
    }

    public String toString() {
        return t0() + '@' + DebugStrings.b(this);
    }

    @Override // td.i1
    public final u0 v(boolean onCancelling, boolean invokeImmediately, ya.l<? super Throwable, Unit> handler) {
        o1 V = V(handler, onCancelling);
        while (true) {
            Object J = J();
            if (J instanceof v0) {
                v0 v0Var = (v0) J;
                if (v0Var.getF18801e()) {
                    if (f18770e.compareAndSet(this, J, V)) {
                        return V;
                    }
                } else {
                    g0(v0Var);
                }
            } else {
                if (J instanceof d1) {
                    t1 f18726e = ((d1) J).getF18726e();
                    if (f18726e == null) {
                        Objects.requireNonNull(J, "null cannot be cast to non-null type kotlinx.coroutines.JobNode");
                        k0((o1) J);
                    } else {
                        u0 u0Var = u1.f18798e;
                        if (onCancelling && (J instanceof b)) {
                            synchronized (J) {
                                r3 = ((b) J).e();
                                if (r3 == null || ((handler instanceof r) && !((b) J).h())) {
                                    if (k(J, f18726e, V)) {
                                        if (r3 == null) {
                                            return V;
                                        }
                                        u0Var = V;
                                    }
                                }
                                Unit unit = Unit.f15173a;
                            }
                        }
                        if (r3 != null) {
                            if (invokeImmediately) {
                                handler.invoke(r3);
                            }
                            return u0Var;
                        }
                        if (k(J, f18726e, V)) {
                            return V;
                        }
                    }
                } else {
                    if (invokeImmediately) {
                        v vVar = J instanceof v ? (v) J : null;
                        handler.invoke(vVar != null ? vVar.f18800a : null);
                    }
                    return u1.f18798e;
                }
            }
        }
    }

    @Override // td.i1
    public final CancellationException w() {
        Object J = J();
        if (J instanceof b) {
            Throwable e10 = ((b) J).e();
            if (e10 != null) {
                CancellationException r02 = r0(e10, DebugStrings.a(this) + " is cancelling");
                if (r02 != null) {
                    return r02;
                }
            }
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        }
        if (!(J instanceof d1)) {
            if (J instanceof v) {
                return s0(this, ((v) J).f18800a, null, 1, null);
            }
            return new j1(DebugStrings.a(this) + " has completed normally", null, this);
        }
        throw new IllegalStateException(("Job is still new or active: " + this).toString());
    }

    public boolean x(Throwable cause) {
        if (cause instanceof CancellationException) {
            return true;
        }
        return p(cause) && getF18760f();
    }
}
