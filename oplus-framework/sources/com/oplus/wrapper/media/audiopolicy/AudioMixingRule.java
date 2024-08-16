package com.oplus.wrapper.media.audiopolicy;

import android.media.AudioAttributes;
import android.media.audiopolicy.AudioMixingRule;

/* loaded from: classes.dex */
public class AudioMixingRule {
    public static final int RULE_MATCH_ATTRIBUTE_USAGE = getRuleMatchAttributeUsage();
    public static final int RULE_MATCH_UID = getRuleMatchUid();
    private final android.media.audiopolicy.AudioMixingRule mTarget;

    private AudioMixingRule(android.media.audiopolicy.AudioMixingRule audioMixingRule) {
        this.mTarget = audioMixingRule;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final AudioMixingRule.Builder mTarget = new AudioMixingRule.Builder();

        public Builder addRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            this.mTarget.addRule(attrToMatch, rule);
            return this;
        }

        public Builder addMixRule(int rule, Object property) throws IllegalArgumentException {
            this.mTarget.addMixRule(rule, property);
            return this;
        }

        public AudioMixingRule build() {
            return new AudioMixingRule(this.mTarget.build());
        }
    }

    private static int getRuleMatchAttributeUsage() {
        return 1;
    }

    private static int getRuleMatchUid() {
        return 4;
    }

    public android.media.audiopolicy.AudioMixingRule getAudioMixingRule() {
        return this.mTarget;
    }
}
