package com.android.server.soundtrigger_middleware;

import com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class AudioSessionProviderImpl extends SoundTriggerMiddlewareImpl.AudioSessionProvider {
    @Override // com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider
    public native SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession acquireSession();

    @Override // com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider
    public native void releaseSession(int i);
}
