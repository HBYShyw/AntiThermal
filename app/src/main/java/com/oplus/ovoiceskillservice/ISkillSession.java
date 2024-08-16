package com.oplus.ovoiceskillservice;

import java.util.Map;
import l7.ISkillCard;

/* loaded from: classes.dex */
public interface ISkillSession {
    void cancel();

    boolean completeAction();

    boolean completeAction(int i10, ISkillCard iSkillCard);

    void finish();

    String getActionID();

    Map<String, String> getParameters();

    void setParameters(Map<String, String> map);
}
