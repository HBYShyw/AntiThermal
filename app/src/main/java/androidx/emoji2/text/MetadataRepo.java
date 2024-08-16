package androidx.emoji2.text;

import android.graphics.Typeface;
import android.util.SparseArray;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import java.nio.ByteBuffer;
import t.MetadataList;

/* compiled from: MetadataRepo.java */
/* renamed from: androidx.emoji2.text.m, reason: use source file name */
/* loaded from: classes.dex */
public final class MetadataRepo {

    /* renamed from: a, reason: collision with root package name */
    private final MetadataList f2652a;

    /* renamed from: b, reason: collision with root package name */
    private final char[] f2653b;

    /* renamed from: c, reason: collision with root package name */
    private final a f2654c = new a(1024);

    /* renamed from: d, reason: collision with root package name */
    private final Typeface f2655d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MetadataRepo.java */
    /* renamed from: androidx.emoji2.text.m$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private final SparseArray<a> f2656a;

        /* renamed from: b, reason: collision with root package name */
        private EmojiMetadata f2657b;

        private a() {
            this(1);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a a(int i10) {
            SparseArray<a> sparseArray = this.f2656a;
            if (sparseArray == null) {
                return null;
            }
            return sparseArray.get(i10);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final EmojiMetadata b() {
            return this.f2657b;
        }

        void c(EmojiMetadata emojiMetadata, int i10, int i11) {
            a a10 = a(emojiMetadata.b(i10));
            if (a10 == null) {
                a10 = new a();
                this.f2656a.put(emojiMetadata.b(i10), a10);
            }
            if (i11 > i10) {
                a10.c(emojiMetadata, i10 + 1, i11);
            } else {
                a10.f2657b = emojiMetadata;
            }
        }

        a(int i10) {
            this.f2656a = new SparseArray<>(i10);
        }
    }

    private MetadataRepo(Typeface typeface, MetadataList metadataList) {
        this.f2655d = typeface;
        this.f2652a = metadataList;
        this.f2653b = new char[metadataList.k() * 2];
        a(metadataList);
    }

    private void a(MetadataList metadataList) {
        int k10 = metadataList.k();
        for (int i10 = 0; i10 < k10; i10++) {
            EmojiMetadata emojiMetadata = new EmojiMetadata(this, i10);
            Character.toChars(emojiMetadata.f(), this.f2653b, i10 * 2);
            h(emojiMetadata);
        }
    }

    public static MetadataRepo b(Typeface typeface, ByteBuffer byteBuffer) {
        try {
            TraceCompat.a("EmojiCompat.MetadataRepo.create");
            return new MetadataRepo(typeface, MetadataListReader.b(byteBuffer));
        } finally {
            TraceCompat.b();
        }
    }

    public char[] c() {
        return this.f2653b;
    }

    public MetadataList d() {
        return this.f2652a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e() {
        return this.f2652a.l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a f() {
        return this.f2654c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Typeface g() {
        return this.f2655d;
    }

    void h(EmojiMetadata emojiMetadata) {
        Preconditions.e(emojiMetadata, "emoji metadata cannot be null");
        Preconditions.a(emojiMetadata.c() > 0, "invalid metadata codepoint length");
        this.f2654c.c(emojiMetadata, 0, emojiMetadata.c() - 1);
    }
}
