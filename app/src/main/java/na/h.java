package na;

import java.io.Externalizable;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections.SetsJVM;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ListBuilder.kt */
/* loaded from: classes2.dex */
public final class h implements Externalizable {

    /* renamed from: g, reason: collision with root package name */
    public static final a f15948g = new a(null);
    private static final long serialVersionUID = 0;

    /* renamed from: e, reason: collision with root package name */
    private Collection<?> f15949e;

    /* renamed from: f, reason: collision with root package name */
    private final int f15950f;

    /* compiled from: ListBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public h(Collection<?> collection, int i10) {
        k.e(collection, "collection");
        this.f15949e = collection;
        this.f15950f = i10;
    }

    private final Object readResolve() {
        return this.f15949e;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) {
        List d10;
        Collection<?> a10;
        Set c10;
        k.e(objectInput, "input");
        byte readByte = objectInput.readByte();
        int i10 = readByte & 1;
        if ((readByte & (-2)) == 0) {
            int readInt = objectInput.readInt();
            if (readInt >= 0) {
                int i11 = 0;
                if (i10 == 0) {
                    d10 = CollectionsJVM.d(readInt);
                    while (i11 < readInt) {
                        d10.add(objectInput.readObject());
                        i11++;
                    }
                    a10 = CollectionsJVM.a(d10);
                } else if (i10 == 1) {
                    c10 = SetsJVM.c(readInt);
                    while (i11 < readInt) {
                        c10.add(objectInput.readObject());
                        i11++;
                    }
                    a10 = SetsJVM.a(c10);
                } else {
                    throw new InvalidObjectException("Unsupported collection type tag: " + i10 + '.');
                }
                this.f15949e = a10;
                return;
            }
            throw new InvalidObjectException("Illegal size value: " + readInt + '.');
        }
        throw new InvalidObjectException("Unsupported flags value: " + ((int) readByte) + '.');
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) {
        k.e(objectOutput, "output");
        objectOutput.writeByte(this.f15950f);
        objectOutput.writeInt(this.f15949e.size());
        Iterator<?> it = this.f15949e.iterator();
        while (it.hasNext()) {
            objectOutput.writeObject(it.next());
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public h() {
        this(r0, 0);
        List j10;
        j10 = r.j();
    }
}
