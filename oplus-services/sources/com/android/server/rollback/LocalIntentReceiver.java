package com.android.server.rollback;

import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.IBinder;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class LocalIntentReceiver {
    final Consumer<Intent> mConsumer;
    private IIntentSender.Stub mLocalSender = new IIntentSender.Stub() { // from class: com.android.server.rollback.LocalIntentReceiver.1
        public void send(int i, Intent intent, String str, IBinder iBinder, IIntentReceiver iIntentReceiver, String str2, Bundle bundle) {
            LocalIntentReceiver.this.mConsumer.accept(intent);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalIntentReceiver(Consumer<Intent> consumer) {
        this.mConsumer = consumer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IntentSender getIntentSender() {
        return new IntentSender(this.mLocalSender);
    }
}
