package androidx.emoji2.text;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.MetadataRepo;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: EmojiProcessor.java */
/* renamed from: androidx.emoji2.text.h, reason: use source file name */
/* loaded from: classes.dex */
public final class EmojiProcessor {

    /* renamed from: a, reason: collision with root package name */
    private final EmojiCompat.i f2619a;

    /* renamed from: b, reason: collision with root package name */
    private final MetadataRepo f2620b;

    /* renamed from: c, reason: collision with root package name */
    private EmojiCompat.d f2621c;

    /* renamed from: d, reason: collision with root package name */
    private final boolean f2622d;

    /* renamed from: e, reason: collision with root package name */
    private final int[] f2623e;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EmojiProcessor.java */
    /* renamed from: androidx.emoji2.text.h$a */
    /* loaded from: classes.dex */
    public static final class a {
        static int a(CharSequence charSequence, int i10, int i11) {
            int length = charSequence.length();
            if (i10 < 0 || length < i10 || i11 < 0) {
                return -1;
            }
            while (true) {
                boolean z10 = false;
                while (i11 != 0) {
                    i10--;
                    if (i10 < 0) {
                        return z10 ? -1 : 0;
                    }
                    char charAt = charSequence.charAt(i10);
                    if (z10) {
                        if (!Character.isHighSurrogate(charAt)) {
                            return -1;
                        }
                        i11--;
                    } else if (!Character.isSurrogate(charAt)) {
                        i11--;
                    } else {
                        if (Character.isHighSurrogate(charAt)) {
                            return -1;
                        }
                        z10 = true;
                    }
                }
                return i10;
            }
        }

        static int b(CharSequence charSequence, int i10, int i11) {
            int length = charSequence.length();
            if (i10 < 0 || length < i10 || i11 < 0) {
                return -1;
            }
            while (true) {
                boolean z10 = false;
                while (i11 != 0) {
                    if (i10 >= length) {
                        if (z10) {
                            return -1;
                        }
                        return length;
                    }
                    char charAt = charSequence.charAt(i10);
                    if (z10) {
                        if (!Character.isLowSurrogate(charAt)) {
                            return -1;
                        }
                        i11--;
                        i10++;
                    } else if (!Character.isSurrogate(charAt)) {
                        i11--;
                        i10++;
                    } else {
                        if (Character.isLowSurrogate(charAt)) {
                            return -1;
                        }
                        i10++;
                        z10 = true;
                    }
                }
                return i10;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EmojiProcessor.java */
    /* renamed from: androidx.emoji2.text.h$b */
    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: a, reason: collision with root package name */
        private int f2624a = 1;

        /* renamed from: b, reason: collision with root package name */
        private final MetadataRepo.a f2625b;

        /* renamed from: c, reason: collision with root package name */
        private MetadataRepo.a f2626c;

        /* renamed from: d, reason: collision with root package name */
        private MetadataRepo.a f2627d;

        /* renamed from: e, reason: collision with root package name */
        private int f2628e;

        /* renamed from: f, reason: collision with root package name */
        private int f2629f;

        /* renamed from: g, reason: collision with root package name */
        private final boolean f2630g;

        /* renamed from: h, reason: collision with root package name */
        private final int[] f2631h;

        b(MetadataRepo.a aVar, boolean z10, int[] iArr) {
            this.f2625b = aVar;
            this.f2626c = aVar;
            this.f2630g = z10;
            this.f2631h = iArr;
        }

        private static boolean d(int i10) {
            return i10 == 65039;
        }

        private static boolean f(int i10) {
            return i10 == 65038;
        }

        private int g() {
            this.f2624a = 1;
            this.f2626c = this.f2625b;
            this.f2629f = 0;
            return 1;
        }

        private boolean h() {
            if (this.f2626c.b().j() || d(this.f2628e)) {
                return true;
            }
            if (this.f2630g) {
                if (this.f2631h == null) {
                    return true;
                }
                if (Arrays.binarySearch(this.f2631h, this.f2626c.b().b(0)) < 0) {
                    return true;
                }
            }
            return false;
        }

        int a(int i10) {
            MetadataRepo.a a10 = this.f2626c.a(i10);
            int i11 = 3;
            if (this.f2624a == 2) {
                if (a10 != null) {
                    this.f2626c = a10;
                    this.f2629f++;
                } else if (f(i10)) {
                    i11 = g();
                } else if (!d(i10)) {
                    if (this.f2626c.b() != null) {
                        if (this.f2629f == 1) {
                            if (h()) {
                                this.f2627d = this.f2626c;
                                g();
                            } else {
                                i11 = g();
                            }
                        } else {
                            this.f2627d = this.f2626c;
                            g();
                        }
                    } else {
                        i11 = g();
                    }
                }
                i11 = 2;
            } else if (a10 == null) {
                i11 = g();
            } else {
                this.f2624a = 2;
                this.f2626c = a10;
                this.f2629f = 1;
                i11 = 2;
            }
            this.f2628e = i10;
            return i11;
        }

        EmojiMetadata b() {
            return this.f2626c.b();
        }

        EmojiMetadata c() {
            return this.f2627d.b();
        }

        boolean e() {
            return this.f2624a == 2 && this.f2626c.b() != null && (this.f2629f > 1 || h());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmojiProcessor(MetadataRepo metadataRepo, EmojiCompat.i iVar, EmojiCompat.d dVar, boolean z10, int[] iArr) {
        this.f2619a = iVar;
        this.f2620b = metadataRepo;
        this.f2621c = dVar;
        this.f2622d = z10;
        this.f2623e = iArr;
    }

    private void a(Spannable spannable, EmojiMetadata emojiMetadata, int i10, int i11) {
        spannable.setSpan(this.f2619a.a(emojiMetadata), i10, i11, 33);
    }

    private static boolean b(Editable editable, KeyEvent keyEvent, boolean z10) {
        EmojiSpan[] emojiSpanArr;
        if (g(keyEvent)) {
            return false;
        }
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        if (!f(selectionStart, selectionEnd) && (emojiSpanArr = (EmojiSpan[]) editable.getSpans(selectionStart, selectionEnd, EmojiSpan.class)) != null && emojiSpanArr.length > 0) {
            for (EmojiSpan emojiSpan : emojiSpanArr) {
                int spanStart = editable.getSpanStart(emojiSpan);
                int spanEnd = editable.getSpanEnd(emojiSpan);
                if ((z10 && spanStart == selectionStart) || ((!z10 && spanEnd == selectionStart) || (selectionStart > spanStart && selectionStart < spanEnd))) {
                    editable.delete(spanStart, spanEnd);
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(InputConnection inputConnection, Editable editable, int i10, int i11, boolean z10) {
        int max;
        int min;
        if (editable != null && inputConnection != null && i10 >= 0 && i11 >= 0) {
            int selectionStart = Selection.getSelectionStart(editable);
            int selectionEnd = Selection.getSelectionEnd(editable);
            if (f(selectionStart, selectionEnd)) {
                return false;
            }
            if (z10) {
                max = a.a(editable, selectionStart, Math.max(i10, 0));
                min = a.b(editable, selectionEnd, Math.max(i11, 0));
                if (max == -1 || min == -1) {
                    return false;
                }
            } else {
                max = Math.max(selectionStart - i10, 0);
                min = Math.min(selectionEnd + i11, editable.length());
            }
            EmojiSpan[] emojiSpanArr = (EmojiSpan[]) editable.getSpans(max, min, EmojiSpan.class);
            if (emojiSpanArr != null && emojiSpanArr.length > 0) {
                for (EmojiSpan emojiSpan : emojiSpanArr) {
                    int spanStart = editable.getSpanStart(emojiSpan);
                    int spanEnd = editable.getSpanEnd(emojiSpan);
                    max = Math.min(spanStart, max);
                    min = Math.max(spanEnd, min);
                }
                int max2 = Math.max(max, 0);
                int min2 = Math.min(min, editable.length());
                inputConnection.beginBatchEdit();
                editable.delete(max2, min2);
                inputConnection.endBatchEdit();
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d(Editable editable, int i10, KeyEvent keyEvent) {
        boolean b10;
        if (i10 != 67) {
            b10 = i10 != 112 ? false : b(editable, keyEvent, true);
        } else {
            b10 = b(editable, keyEvent, false);
        }
        if (!b10) {
            return false;
        }
        MetaKeyKeyListener.adjustMetaAfterKeypress(editable);
        return true;
    }

    private boolean e(CharSequence charSequence, int i10, int i11, EmojiMetadata emojiMetadata) {
        if (emojiMetadata.d() == 0) {
            emojiMetadata.k(this.f2621c.a(charSequence, i10, i11, emojiMetadata.h()));
        }
        return emojiMetadata.d() == 2;
    }

    private static boolean f(int i10, int i11) {
        return i10 == -1 || i11 == -1 || i10 != i11;
    }

    private static boolean g(KeyEvent keyEvent) {
        return !KeyEvent.metaStateHasNoModifiers(keyEvent.getMetaState());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0125, code lost:
    
        ((androidx.emoji2.text.SpannableBuilder) r10).d();
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0047 A[Catch: all -> 0x012c, TryCatch #0 {all -> 0x012c, blocks: (B:99:0x000d, B:102:0x0012, B:104:0x0016, B:106:0x0025, B:9:0x0036, B:11:0x0040, B:13:0x0043, B:15:0x0047, B:17:0x0053, B:19:0x0056, B:23:0x0063, B:29:0x0072, B:30:0x0080, B:34:0x009b, B:60:0x00ab, B:64:0x00b7, B:65:0x00c1, B:47:0x00cb, B:50:0x00d2, B:37:0x00d7, B:39:0x00e2, B:71:0x00e9, B:75:0x00f3, B:78:0x00ff, B:79:0x0105, B:81:0x010e, B:6:0x002b), top: B:98:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00d7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00ff A[Catch: all -> 0x012c, TryCatch #0 {all -> 0x012c, blocks: (B:99:0x000d, B:102:0x0012, B:104:0x0016, B:106:0x0025, B:9:0x0036, B:11:0x0040, B:13:0x0043, B:15:0x0047, B:17:0x0053, B:19:0x0056, B:23:0x0063, B:29:0x0072, B:30:0x0080, B:34:0x009b, B:60:0x00ab, B:64:0x00b7, B:65:0x00c1, B:47:0x00cb, B:50:0x00d2, B:37:0x00d7, B:39:0x00e2, B:71:0x00e9, B:75:0x00f3, B:78:0x00ff, B:79:0x0105, B:81:0x010e, B:6:0x002b), top: B:98:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x010e A[Catch: all -> 0x012c, TRY_LEAVE, TryCatch #0 {all -> 0x012c, blocks: (B:99:0x000d, B:102:0x0012, B:104:0x0016, B:106:0x0025, B:9:0x0036, B:11:0x0040, B:13:0x0043, B:15:0x0047, B:17:0x0053, B:19:0x0056, B:23:0x0063, B:29:0x0072, B:30:0x0080, B:34:0x009b, B:60:0x00ab, B:64:0x00b7, B:65:0x00c1, B:47:0x00cb, B:50:0x00d2, B:37:0x00d7, B:39:0x00e2, B:71:0x00e9, B:75:0x00f3, B:78:0x00ff, B:79:0x0105, B:81:0x010e, B:6:0x002b), top: B:98:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x011a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public CharSequence h(CharSequence charSequence, int i10, int i11, int i12, boolean z10) {
        b bVar;
        int codePointAt;
        UnprecomputeTextOnModificationSpannable unprecomputeTextOnModificationSpannable;
        int i13;
        int a10;
        EmojiSpan[] emojiSpanArr;
        boolean z11 = charSequence instanceof SpannableBuilder;
        if (z11) {
            ((SpannableBuilder) charSequence).a();
        }
        UnprecomputeTextOnModificationSpannable unprecomputeTextOnModificationSpannable2 = null;
        if (!z11) {
            try {
                if (!(charSequence instanceof Spannable)) {
                    if ((charSequence instanceof Spanned) && ((Spanned) charSequence).nextSpanTransition(i10 - 1, i11 + 1, EmojiSpan.class) <= i11) {
                        unprecomputeTextOnModificationSpannable2 = new UnprecomputeTextOnModificationSpannable(charSequence);
                    }
                    if (unprecomputeTextOnModificationSpannable2 != null && (emojiSpanArr = (EmojiSpan[]) unprecomputeTextOnModificationSpannable2.getSpans(i10, i11, EmojiSpan.class)) != null && emojiSpanArr.length > 0) {
                        for (EmojiSpan emojiSpan : emojiSpanArr) {
                            int spanStart = unprecomputeTextOnModificationSpannable2.getSpanStart(emojiSpan);
                            int spanEnd = unprecomputeTextOnModificationSpannable2.getSpanEnd(emojiSpan);
                            if (spanStart != i11) {
                                unprecomputeTextOnModificationSpannable2.removeSpan(emojiSpan);
                            }
                            i10 = Math.min(spanStart, i10);
                            i11 = Math.max(spanEnd, i11);
                        }
                    }
                    if (i10 != i11 && i10 < charSequence.length()) {
                        if (i12 != Integer.MAX_VALUE && unprecomputeTextOnModificationSpannable2 != null) {
                            i12 -= ((EmojiSpan[]) unprecomputeTextOnModificationSpannable2.getSpans(0, unprecomputeTextOnModificationSpannable2.length(), EmojiSpan.class)).length;
                        }
                        bVar = new b(this.f2620b.f(), this.f2622d, this.f2623e);
                        codePointAt = Character.codePointAt(charSequence, i10);
                        int i14 = 0;
                        unprecomputeTextOnModificationSpannable = unprecomputeTextOnModificationSpannable2;
                        loop1: while (true) {
                            i13 = i10;
                            while (i10 < i11 && i14 < i12) {
                                a10 = bVar.a(codePointAt);
                                if (a10 != 1) {
                                    i13 += Character.charCount(Character.codePointAt(charSequence, i13));
                                    if (i13 < i11) {
                                        codePointAt = Character.codePointAt(charSequence, i13);
                                    }
                                    i10 = i13;
                                } else if (a10 == 2) {
                                    i10 += Character.charCount(codePointAt);
                                    if (i10 < i11) {
                                        codePointAt = Character.codePointAt(charSequence, i10);
                                    }
                                } else if (a10 == 3) {
                                    if (z10 || !e(charSequence, i13, i10, bVar.c())) {
                                        if (unprecomputeTextOnModificationSpannable == null) {
                                            unprecomputeTextOnModificationSpannable = new UnprecomputeTextOnModificationSpannable((Spannable) new SpannableString(charSequence));
                                        }
                                        a(unprecomputeTextOnModificationSpannable, bVar.c(), i13, i10);
                                        i14++;
                                    }
                                }
                            }
                        }
                        if (bVar.e() && i14 < i12 && (z10 || !e(charSequence, i13, i10, bVar.b()))) {
                            if (unprecomputeTextOnModificationSpannable == null) {
                                unprecomputeTextOnModificationSpannable = new UnprecomputeTextOnModificationSpannable(charSequence);
                            }
                            a(unprecomputeTextOnModificationSpannable, bVar.b(), i13, i10);
                        }
                        if (unprecomputeTextOnModificationSpannable == null) {
                            return unprecomputeTextOnModificationSpannable.b();
                        }
                        if (z11) {
                            ((SpannableBuilder) charSequence).d();
                        }
                        return charSequence;
                    }
                    return charSequence;
                }
            } finally {
                if (z11) {
                    ((SpannableBuilder) charSequence).d();
                }
            }
        }
        unprecomputeTextOnModificationSpannable2 = new UnprecomputeTextOnModificationSpannable((Spannable) charSequence);
        if (unprecomputeTextOnModificationSpannable2 != null) {
            while (r5 < r4) {
            }
        }
        if (i10 != i11) {
            if (i12 != Integer.MAX_VALUE) {
                i12 -= ((EmojiSpan[]) unprecomputeTextOnModificationSpannable2.getSpans(0, unprecomputeTextOnModificationSpannable2.length(), EmojiSpan.class)).length;
            }
            bVar = new b(this.f2620b.f(), this.f2622d, this.f2623e);
            codePointAt = Character.codePointAt(charSequence, i10);
            int i142 = 0;
            unprecomputeTextOnModificationSpannable = unprecomputeTextOnModificationSpannable2;
            loop1: while (true) {
                i13 = i10;
                while (i10 < i11) {
                    a10 = bVar.a(codePointAt);
                    if (a10 != 1) {
                    }
                }
            }
            if (bVar.e()) {
                if (unprecomputeTextOnModificationSpannable == null) {
                }
                a(unprecomputeTextOnModificationSpannable, bVar.b(), i13, i10);
            }
            if (unprecomputeTextOnModificationSpannable == null) {
            }
        }
        return charSequence;
    }
}
