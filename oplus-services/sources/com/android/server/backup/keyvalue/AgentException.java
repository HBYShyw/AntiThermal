package com.android.server.backup.keyvalue;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class AgentException extends BackupException {
    private final boolean mTransitory;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AgentException transitory() {
        return new AgentException(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AgentException transitory(Exception exc) {
        return new AgentException(true, exc);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AgentException permanent() {
        return new AgentException(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AgentException permanent(Exception exc) {
        return new AgentException(false, exc);
    }

    private AgentException(boolean z) {
        this.mTransitory = z;
    }

    private AgentException(boolean z, Exception exc) {
        super(exc);
        this.mTransitory = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransitory() {
        return this.mTransitory;
    }
}
