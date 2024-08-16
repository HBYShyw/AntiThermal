package td;

import java.io.Closeable;
import kotlin.Metadata;
import qa.g;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: Executors.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u00012\u00020\u0002:\u0001\u0005B\u0007¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0006"}, d2 = {"Ltd/b1;", "Ltd/c0;", "Ljava/io/Closeable;", "<init>", "()V", "a", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* renamed from: td.b1, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class Executors extends CoroutineDispatcher implements Closeable {

    /* renamed from: g, reason: collision with root package name */
    public static final a f18722g = new a(null);

    /* compiled from: Executors.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\u0003\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\t\b\u0002¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Ltd/b1$a;", "Lqa/b;", "Ltd/c0;", "Ltd/b1;", "<init>", "()V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
    /* renamed from: td.b1$a */
    /* loaded from: classes2.dex */
    public static final class a extends qa.b<CoroutineDispatcher, Executors> {

        /* compiled from: Executors.kt */
        @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Lqa/g$b;", "it", "Ltd/b1;", "a", "(Lqa/g$b;)Ltd/b1;"}, k = 3, mv = {1, 6, 0})
        /* renamed from: td.b1$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0105a extends Lambda implements ya.l<g.b, Executors> {

            /* renamed from: e, reason: collision with root package name */
            public static final C0105a f18723e = new C0105a();

            C0105a() {
                super(1);
            }

            @Override // ya.l
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Executors invoke(g.b bVar) {
                if (bVar instanceof Executors) {
                    return (Executors) bVar;
                }
                return null;
            }
        }

        private a() {
            super(CoroutineDispatcher.f18724f, C0105a.f18723e);
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}
