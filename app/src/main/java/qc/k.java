package qc;

import java.io.IOException;

/* compiled from: InvalidProtocolBufferException.java */
/* loaded from: classes2.dex */
public class k extends IOException {

    /* renamed from: e, reason: collision with root package name */
    private q f17317e;

    public k(String str) {
        super(str);
        this.f17317e = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k b() {
        return new k("Protocol message end-group tag did not match expected tag.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k c() {
        return new k("Protocol message contained an invalid tag (zero).");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k d() {
        return new k("Protocol message had invalid UTF-8.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k e() {
        return new k("Protocol message tag had invalid wire type.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k f() {
        return new k("CodedInputStream encountered a malformed varint.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k g() {
        return new k("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k h() {
        return new k("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k j() {
        return new k("Protocol message was too large.  May be malicious.  Use CodedInputStream.setSizeLimit() to increase the size limit.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static k k() {
        return new k("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.");
    }

    public q a() {
        return this.f17317e;
    }

    public k i(q qVar) {
        this.f17317e = qVar;
        return this;
    }
}
