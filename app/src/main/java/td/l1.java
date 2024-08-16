package td;

import kotlin.Metadata;

/* compiled from: JobSupport.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0011\u0018\u00002\u00020\u00012\u00020\u0002B\u0011\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u000b¢\u0006\u0004\b\r\u0010\u000eJ\b\u0010\u0004\u001a\u00020\u0003H\u0003R\u0014\u0010\u0007\u001a\u00020\u00038PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\b\u001a\u00020\u00038\u0010X\u0090\u0004¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u0006¨\u0006\u000f"}, d2 = {"Ltd/l1;", "Ltd/p1;", "Ltd/t;", "", "z0", "G", "()Z", "onCancelComplete", "handlesException", "Z", "F", "Ltd/i1;", "parent", "<init>", "(Ltd/i1;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public class l1 extends p1 implements CompletableJob {

    /* renamed from: f, reason: collision with root package name */
    private final boolean f18760f;

    public l1(i1 i1Var) {
        super(true);
        O(i1Var);
        this.f18760f = z0();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001f, code lost:
    
        r3 = r3.I();
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0025, code lost:
    
        if ((r3 instanceof td.r) == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0027, code lost:
    
        r3 = (td.r) r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002b, code lost:
    
        if (r3 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002d, code lost:
    
        r3 = r3.G();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0031, code lost:
    
        if (r3 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x002a, code lost:
    
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x001d, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0014, code lost:
    
        if (r3 != null) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001b, code lost:
    
        if (r3.getF18760f() == false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final boolean z0() {
        q I = I();
        r rVar = I instanceof r ? (r) I : null;
        if (rVar != null) {
            p1 G = rVar.G();
        }
        return false;
    }

    @Override // td.p1
    /* renamed from: F, reason: from getter */
    public boolean getF18760f() {
        return this.f18760f;
    }

    @Override // td.p1
    public boolean G() {
        return true;
    }
}
