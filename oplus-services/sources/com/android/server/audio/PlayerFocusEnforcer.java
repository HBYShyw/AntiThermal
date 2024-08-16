package com.android.server.audio;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface PlayerFocusEnforcer {
    boolean duckPlayers(FocusRequester focusRequester, FocusRequester focusRequester2, boolean z);

    boolean fadeOutPlayers(FocusRequester focusRequester, FocusRequester focusRequester2);

    void forgetUid(int i);

    void mutePlayersForCall(int[] iArr);

    void restoreVShapedPlayers(FocusRequester focusRequester);

    void unmutePlayersForCall();
}
