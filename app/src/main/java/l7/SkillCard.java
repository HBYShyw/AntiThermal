package l7;

import l7.CardPayload;

/* compiled from: SkillCard.java */
/* renamed from: l7.d, reason: use source file name */
/* loaded from: classes.dex */
public class SkillCard<T extends CardPayload> implements ISkillCard {

    /* renamed from: a, reason: collision with root package name */
    private T f14650a;

    public String a() {
        return this.f14650a.f14649a;
    }

    public T b() {
        return this.f14650a;
    }

    public void c(T t7) {
        this.f14650a = t7;
    }

    public String toString() {
        return String.format("{\"cardType\":\"%s\", \"payload\":%s}", a(), b().toString());
    }
}
