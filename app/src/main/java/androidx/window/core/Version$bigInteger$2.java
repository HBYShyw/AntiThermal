package androidx.window.core;

import java.math.BigInteger;
import kotlin.Metadata;
import ya.a;
import za.Lambda;

/* compiled from: Version.kt */
@Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Ljava/math/BigInteger;", "kotlin.jvm.PlatformType", "a", "()Ljava/math/BigInteger;"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class Version$bigInteger$2 extends Lambda implements a<BigInteger> {

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ Version f4346e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Version$bigInteger$2(Version version) {
        super(0);
        this.f4346e = version;
    }

    @Override // ya.a
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final BigInteger invoke() {
        return BigInteger.valueOf(this.f4346e.getMajor()).shiftLeft(32).or(BigInteger.valueOf(this.f4346e.getMinor())).shiftLeft(32).or(BigInteger.valueOf(this.f4346e.getPatch()));
    }
}
