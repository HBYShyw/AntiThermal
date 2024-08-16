package com.android.server.app;

import android.content.ComponentName;
import android.service.games.IGameSession;
import android.view.SurfaceControlViewHost;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class GameSessionRecord {
    private final IGameSession mIGameSession;
    private final ComponentName mRootComponentName;
    private final State mState;
    private final SurfaceControlViewHost.SurfacePackage mSurfacePackage;
    private final int mTaskId;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private enum State {
        NO_GAME_SESSION_REQUESTED,
        GAME_SESSION_REQUESTED,
        GAME_SESSION_ATTACHED,
        GAME_SESSION_ENDED_PROCESS_DEATH
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GameSessionRecord awaitingGameSessionRequest(int i, ComponentName componentName) {
        return new GameSessionRecord(i, State.NO_GAME_SESSION_REQUESTED, componentName, null, null);
    }

    private GameSessionRecord(int i, State state, ComponentName componentName, IGameSession iGameSession, SurfaceControlViewHost.SurfacePackage surfacePackage) {
        this.mTaskId = i;
        this.mState = state;
        this.mRootComponentName = componentName;
        this.mIGameSession = iGameSession;
        this.mSurfacePackage = surfacePackage;
    }

    public boolean isAwaitingGameSessionRequest() {
        return this.mState == State.NO_GAME_SESSION_REQUESTED;
    }

    public GameSessionRecord withGameSessionRequested() {
        return new GameSessionRecord(this.mTaskId, State.GAME_SESSION_REQUESTED, this.mRootComponentName, null, null);
    }

    public boolean isGameSessionRequested() {
        return this.mState == State.GAME_SESSION_REQUESTED;
    }

    public GameSessionRecord withGameSession(IGameSession iGameSession, SurfaceControlViewHost.SurfacePackage surfacePackage) {
        Objects.requireNonNull(iGameSession);
        return new GameSessionRecord(this.mTaskId, State.GAME_SESSION_ATTACHED, this.mRootComponentName, iGameSession, surfacePackage);
    }

    public GameSessionRecord withGameSessionEndedOnProcessDeath() {
        return new GameSessionRecord(this.mTaskId, State.GAME_SESSION_ENDED_PROCESS_DEATH, this.mRootComponentName, null, null);
    }

    public boolean isGameSessionEndedForProcessDeath() {
        return this.mState == State.GAME_SESSION_ENDED_PROCESS_DEATH;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public ComponentName getComponentName() {
        return this.mRootComponentName;
    }

    public IGameSession getGameSession() {
        return this.mIGameSession;
    }

    public SurfaceControlViewHost.SurfacePackage getSurfacePackage() {
        return this.mSurfacePackage;
    }

    public String toString() {
        return "GameSessionRecord{mTaskId=" + this.mTaskId + ", mState=" + this.mState + ", mRootComponentName=" + this.mRootComponentName + ", mIGameSession=" + this.mIGameSession + ", mSurfacePackage=" + this.mSurfacePackage + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GameSessionRecord)) {
            return false;
        }
        GameSessionRecord gameSessionRecord = (GameSessionRecord) obj;
        return this.mTaskId == gameSessionRecord.mTaskId && this.mState == gameSessionRecord.mState && this.mRootComponentName.equals(gameSessionRecord.mRootComponentName) && Objects.equals(this.mIGameSession, gameSessionRecord.mIGameSession) && Objects.equals(this.mSurfacePackage, gameSessionRecord.mSurfacePackage);
    }

    public int hashCode() {
        Integer valueOf = Integer.valueOf(this.mTaskId);
        State state = this.mState;
        return Objects.hash(valueOf, state, this.mRootComponentName, this.mIGameSession, state, this.mSurfacePackage);
    }
}
