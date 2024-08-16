package kc;

import fb.PrimitiveRanges;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import kotlin.collections.PrimitiveIterators;
import kotlin.collections._Collections;
import kotlin.collections.s;
import lc.BinaryVersion;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: BuiltInsBinaryVersion.kt */
/* renamed from: kc.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class BuiltInsBinaryVersion extends BinaryVersion {

    /* renamed from: g, reason: collision with root package name */
    public static final a f14279g = new a(null);

    /* renamed from: h, reason: collision with root package name */
    public static final BuiltInsBinaryVersion f14280h = new BuiltInsBinaryVersion(1, 0, 7);

    /* renamed from: i, reason: collision with root package name */
    public static final BuiltInsBinaryVersion f14281i = new BuiltInsBinaryVersion(new int[0]);

    /* compiled from: BuiltInsBinaryVersion.kt */
    /* renamed from: kc.a$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final BuiltInsBinaryVersion a(InputStream inputStream) {
            int u7;
            int[] y02;
            k.e(inputStream, "stream");
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            PrimitiveRanges primitiveRanges = new PrimitiveRanges(1, dataInputStream.readInt());
            u7 = s.u(primitiveRanges, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<Integer> it = primitiveRanges.iterator();
            while (it.hasNext()) {
                ((PrimitiveIterators) it).b();
                arrayList.add(Integer.valueOf(dataInputStream.readInt()));
            }
            y02 = _Collections.y0(arrayList);
            return new BuiltInsBinaryVersion(Arrays.copyOf(y02, y02.length));
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BuiltInsBinaryVersion(int... iArr) {
        super(Arrays.copyOf(iArr, iArr.length));
        k.e(iArr, "numbers");
    }

    public boolean h() {
        return f(f14280h);
    }
}
