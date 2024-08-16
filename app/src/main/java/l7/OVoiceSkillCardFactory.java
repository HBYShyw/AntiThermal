package l7;

/* compiled from: OVoiceSkillCardFactory.java */
/* renamed from: l7.c, reason: use source file name */
/* loaded from: classes.dex */
public class OVoiceSkillCardFactory {
    public static ISkillCard a(String str) {
        SkillCard skillCard = new SkillCard();
        TTSPayload tTSPayload = new TTSPayload();
        skillCard.c(tTSPayload);
        tTSPayload.a(str);
        return skillCard;
    }
}
