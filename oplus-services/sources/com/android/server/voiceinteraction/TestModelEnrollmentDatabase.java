package com.android.server.voiceinteraction;

import android.hardware.soundtrigger.SoundTrigger;
import com.android.server.voiceinteraction.TestModelEnrollmentDatabase;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TestModelEnrollmentDatabase implements IEnrolledModelDb {
    private final Map<EnrollmentKey, SoundTrigger.KeyphraseSoundModel> mModelMap = new HashMap();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class EnrollmentKey {
        private final int mKeyphraseId;
        private final String mLocale;
        private final List<Integer> mUserIds;

        EnrollmentKey(int i, List<Integer> list, String str) {
            this.mKeyphraseId = i;
            Objects.requireNonNull(list);
            this.mUserIds = list;
            Objects.requireNonNull(str);
            this.mLocale = str;
        }

        int keyphraseId() {
            return this.mKeyphraseId;
        }

        List<Integer> userIds() {
            return this.mUserIds;
        }

        String locale() {
            return this.mLocale;
        }

        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
            stringJoiner.add("keyphraseId: " + this.mKeyphraseId);
            stringJoiner.add("userIds: " + this.mUserIds.toString());
            stringJoiner.add("locale: " + this.mLocale.toString());
            return "EnrollmentKey: " + stringJoiner.toString();
        }

        public int hashCode() {
            return ((((this.mKeyphraseId + 31) * 31) + this.mUserIds.hashCode()) * 31) + this.mLocale.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof EnrollmentKey)) {
                return false;
            }
            EnrollmentKey enrollmentKey = (EnrollmentKey) obj;
            return this.mKeyphraseId == enrollmentKey.mKeyphraseId && this.mUserIds.equals(enrollmentKey.mUserIds) && this.mLocale.equals(enrollmentKey.mLocale);
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) {
        SoundTrigger.Keyphrase keyphrase = keyphraseSoundModel.getKeyphrases()[0];
        this.mModelMap.put(new EnrollmentKey(keyphrase.getId(), Arrays.stream(keyphrase.getUsers()).boxed().toList(), keyphrase.getLocale().toLanguageTag()), keyphraseSoundModel);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$deleteKeyphraseSoundModel$0(int i, String str, int i2, EnrollmentKey enrollmentKey) {
        return enrollmentKey.keyphraseId() == i && enrollmentKey.locale().equals(str) && enrollmentKey.userIds().contains(Integer.valueOf(i2));
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean deleteKeyphraseSoundModel(final int i, final int i2, final String str) {
        return this.mModelMap.keySet().removeIf(new Predicate() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$deleteKeyphraseSoundModel$0;
                lambda$deleteKeyphraseSoundModel$0 = TestModelEnrollmentDatabase.lambda$deleteKeyphraseSoundModel$0(i, str, i2, (TestModelEnrollmentDatabase.EnrollmentKey) obj);
                return lambda$deleteKeyphraseSoundModel$0;
            }
        });
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(final int i, final int i2, final String str) {
        return (SoundTrigger.KeyphraseSoundModel) this.mModelMap.entrySet().stream().filter(new Predicate() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getKeyphraseSoundModel$1;
                lambda$getKeyphraseSoundModel$1 = TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$1(i, str, i2, (Map.Entry) obj);
                return lambda$getKeyphraseSoundModel$1;
            }
        }).findFirst().map(new Function() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                SoundTrigger.KeyphraseSoundModel lambda$getKeyphraseSoundModel$2;
                lambda$getKeyphraseSoundModel$2 = TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$2((Map.Entry) obj);
                return lambda$getKeyphraseSoundModel$2;
            }
        }).orElse(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getKeyphraseSoundModel$1(int i, String str, int i2, Map.Entry entry) {
        return ((EnrollmentKey) entry.getKey()).keyphraseId() == i && ((EnrollmentKey) entry.getKey()).locale().equals(str) && ((EnrollmentKey) entry.getKey()).userIds().contains(Integer.valueOf(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ SoundTrigger.KeyphraseSoundModel lambda$getKeyphraseSoundModel$2(Map.Entry entry) {
        return (SoundTrigger.KeyphraseSoundModel) entry.getValue();
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(final String str, final int i, final String str2) {
        return (SoundTrigger.KeyphraseSoundModel) this.mModelMap.entrySet().stream().filter(new Predicate() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getKeyphraseSoundModel$3;
                lambda$getKeyphraseSoundModel$3 = TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$3(str, str2, i, (Map.Entry) obj);
                return lambda$getKeyphraseSoundModel$3;
            }
        }).findFirst().map(new Function() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                SoundTrigger.KeyphraseSoundModel lambda$getKeyphraseSoundModel$4;
                lambda$getKeyphraseSoundModel$4 = TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$4((Map.Entry) obj);
                return lambda$getKeyphraseSoundModel$4;
            }
        }).orElse(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getKeyphraseSoundModel$3(String str, String str2, int i, Map.Entry entry) {
        return ((SoundTrigger.KeyphraseSoundModel) entry.getValue()).getKeyphrases()[0].getText().equals(str) && ((EnrollmentKey) entry.getKey()).locale().equals(str2) && ((EnrollmentKey) entry.getKey()).userIds().contains(Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ SoundTrigger.KeyphraseSoundModel lambda$getKeyphraseSoundModel$4(Map.Entry entry) {
        return (SoundTrigger.KeyphraseSoundModel) entry.getValue();
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public void dump(PrintWriter printWriter) {
        printWriter.println("Using test enrollment database, with enrolled models:");
        printWriter.println(this.mModelMap);
    }
}
