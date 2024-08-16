package l7;

/* compiled from: TTSPayload.java */
/* renamed from: l7.e, reason: use source file name */
/* loaded from: classes.dex */
public class TTSPayload extends CardPayload {

    /* renamed from: b, reason: collision with root package name */
    private String f14651b;

    public TTSPayload() {
        super("TTS");
    }

    public void a(String str) {
        this.f14651b = str;
    }

    public String toString() {
        return String.format("{\"ttsText\":\"%s\"}", this.f14651b);
    }
}
