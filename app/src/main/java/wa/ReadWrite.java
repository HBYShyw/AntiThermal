package wa;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import za.k;

/* compiled from: ReadWrite.kt */
/* renamed from: wa.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class ReadWrite {
    public static final long a(Reader reader, Writer writer, int i10) {
        k.e(reader, "<this>");
        k.e(writer, "out");
        char[] cArr = new char[i10];
        int read = reader.read(cArr);
        long j10 = 0;
        while (read >= 0) {
            writer.write(cArr, 0, read);
            j10 += read;
            read = reader.read(cArr);
        }
        return j10;
    }

    public static /* synthetic */ long b(Reader reader, Writer writer, int i10, int i11, Object obj) {
        if ((i11 & 2) != 0) {
            i10 = 8192;
        }
        return a(reader, writer, i10);
    }

    public static final String c(Reader reader) {
        k.e(reader, "<this>");
        StringWriter stringWriter = new StringWriter();
        b(reader, stringWriter, 0, 2, null);
        String stringWriter2 = stringWriter.toString();
        k.d(stringWriter2, "buffer.toString()");
        return stringWriter2;
    }
}
