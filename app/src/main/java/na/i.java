package na;

import java.io.Externalizable;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import kotlin.collections.MapsJVM;
import kotlin.collections.m0;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: MapBuilder.kt */
/* loaded from: classes2.dex */
final class i implements Externalizable {

    /* renamed from: f, reason: collision with root package name */
    public static final a f15951f = new a(null);
    private static final long serialVersionUID = 0;

    /* renamed from: e, reason: collision with root package name */
    private Map<?, ?> f15952e;

    /* compiled from: MapBuilder.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public i(Map<?, ?> map) {
        k.e(map, "map");
        this.f15952e = map;
    }

    private final Object readResolve() {
        return this.f15952e;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) {
        Map d10;
        Map<?, ?> b10;
        k.e(objectInput, "input");
        byte readByte = objectInput.readByte();
        if (readByte == 0) {
            int readInt = objectInput.readInt();
            if (readInt >= 0) {
                d10 = MapsJVM.d(readInt);
                for (int i10 = 0; i10 < readInt; i10++) {
                    d10.put(objectInput.readObject(), objectInput.readObject());
                }
                b10 = MapsJVM.b(d10);
                this.f15952e = b10;
                return;
            }
            throw new InvalidObjectException("Illegal size value: " + readInt + '.');
        }
        throw new InvalidObjectException("Unsupported flags value: " + ((int) readByte));
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) {
        k.e(objectOutput, "output");
        objectOutput.writeByte(0);
        objectOutput.writeInt(this.f15952e.size());
        for (Map.Entry<?, ?> entry : this.f15952e.entrySet()) {
            objectOutput.writeObject(entry.getKey());
            objectOutput.writeObject(entry.getValue());
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public i() {
        this(r0);
        Map i10;
        i10 = m0.i();
    }
}
