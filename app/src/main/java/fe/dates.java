package fe;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: dates.kt */
@Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u000e\u0010\u0002\u001a\u0004\u0018\u00010\u0001*\u00020\u0000H\u0000\u001a\f\u0010\u0003\u001a\u00020\u0000*\u00020\u0001H\u0000¨\u0006\u0004"}, d2 = {"", "Ljava/util/Date;", "a", "b", "okhttp"}, k = 2, mv = {1, 6, 0})
/* renamed from: fe.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class dates {

    /* renamed from: a, reason: collision with root package name */
    private static final a f11455a = new a();

    /* renamed from: b, reason: collision with root package name */
    private static final String[] f11456b;

    /* renamed from: c, reason: collision with root package name */
    private static final DateFormat[] f11457c;

    /* compiled from: dates.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0002H\u0014¨\u0006\u0004"}, d2 = {"fe/c$a", "Ljava/lang/ThreadLocal;", "Ljava/text/DateFormat;", "a", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: fe.c$a */
    /* loaded from: classes2.dex */
    public static final class a extends ThreadLocal<DateFormat> {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public DateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            simpleDateFormat.setLenient(false);
            simpleDateFormat.setTimeZone(ae.d.f242f);
            return simpleDateFormat;
        }
    }

    static {
        String[] strArr = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z"};
        f11456b = strArr;
        f11457c = new DateFormat[strArr.length];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.text.DateFormat[]] */
    /* JADX WARN: Type inference failed for: r8v0 */
    public static final Date a(String str) {
        za.k.e(str, "<this>");
        if (str.length() == 0) {
            return null;
        }
        ParsePosition parsePosition = new ParsePosition(0);
        Date parse = f11455a.get().parse(str, parsePosition);
        if (parsePosition.getIndex() == str.length()) {
            return parse;
        }
        String[] strArr = f11456b;
        synchronized (strArr) {
            int length = strArr.length;
            int i10 = 0;
            while (i10 < length) {
                int i11 = i10 + 1;
                ?? r72 = f11457c;
                ?? r82 = r72[i10];
                SimpleDateFormat simpleDateFormat = r82;
                if (r82 == 0) {
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(f11456b[i10], Locale.US);
                    simpleDateFormat2.setTimeZone(ae.d.f242f);
                    r72[i10] = simpleDateFormat2;
                    simpleDateFormat = simpleDateFormat2;
                }
                parsePosition.setIndex(0);
                Date parse2 = simpleDateFormat.parse(str, parsePosition);
                if (parsePosition.getIndex() != 0) {
                    return parse2;
                }
                i10 = i11;
            }
            Unit unit = Unit.f15173a;
            return null;
        }
    }

    public static final String b(Date date) {
        za.k.e(date, "<this>");
        String format = f11455a.get().format(date);
        za.k.d(format, "STANDARD_DATE_FORMAT.get().format(this)");
        return format;
    }
}
