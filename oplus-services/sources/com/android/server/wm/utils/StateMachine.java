package com.android.server.wm.utils;

import android.annotation.IntRange;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.util.AnnotationValidations;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class StateMachine {
    private static final String TAG = "StateMachine";
    private final Queue<Command> mCommands;
    private int mLastRequestedState;
    private int mState;
    private final SparseArray<Handler> mStateHandlers;
    private final IntArray mTmp;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Handler {
        default void enter() {
        }

        default void exit() {
        }

        default boolean handle(int i, Object obj) {
            return false;
        }
    }

    public static boolean isIn(int i, int i2) {
        while (i > i2) {
            i >>= 4;
        }
        return i == i2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    protected static class Command {
        static final int COMMIT = 1;
        static final int ENTER = 2;
        static final int EXIT = 3;
        final int mState;
        final int mType;

        private Command(int i, int i2) {
            this.mType = i;
            AnnotationValidations.validate(IntRange.class, (IntRange) null, i2, "from", 0L);
            this.mState = i2;
        }

        static Command newCommit(int i) {
            return new Command(1, i);
        }

        static Command newEnter(int i) {
            return new Command(2, i);
        }

        static Command newExit(int i) {
            return new Command(3, i);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Command{ type: ");
            int i = this.mType;
            if (i == 1) {
                sb.append("commit");
            } else if (i == 2) {
                sb.append("enter");
            } else if (i == 3) {
                sb.append("exit");
            } else {
                sb.append("UNKNOWN(");
                sb.append(this.mType);
                sb.append(")");
            }
            sb.append(" state: ");
            sb.append(Integer.toHexString(this.mState));
            sb.append(" }");
            return sb.toString();
        }
    }

    public StateMachine() {
        this(0);
    }

    public StateMachine(int i) {
        this.mTmp = new IntArray();
        this.mStateHandlers = new SparseArray<>();
        this.mCommands = new ArrayDeque();
        this.mState = i;
        AnnotationValidations.validate(IntRange.class, (IntRange) null, i, "from", 0L);
        this.mLastRequestedState = i;
    }

    public int getState() {
        return this.mLastRequestedState;
    }

    protected int getCurrentState() {
        return this.mState;
    }

    protected Command[] getCommands() {
        Command[] commandArr = new Command[this.mCommands.size()];
        this.mCommands.toArray(commandArr);
        return commandArr;
    }

    public Handler addStateHandler(int i, Handler handler) {
        Handler handler2 = this.mStateHandlers.get(i);
        this.mStateHandlers.put(i, handler);
        return handler2;
    }

    public void handle(int i, Object obj) {
        int i2 = this.mState;
        while (true) {
            Handler handler = this.mStateHandlers.get(i2);
            if ((handler != null && handler.handle(i, obj)) || i2 == 0) {
                return;
            } else {
                i2 >>= 4;
            }
        }
    }

    protected void enter(int i) {
        AnnotationValidations.validate(IntRange.class, (IntRange) null, i, "from", 0L);
        Handler handler = this.mStateHandlers.get(i);
        if (handler != null) {
            handler.enter();
        }
    }

    protected void exit(int i) {
        AnnotationValidations.validate(IntRange.class, (IntRange) null, i, "from", 0L);
        Handler handler = this.mStateHandlers.get(i);
        if (handler != null) {
            handler.exit();
        }
    }

    public boolean isIn(int i) {
        return isIn(this.mLastRequestedState, i);
    }

    public void transit(int i) {
        AnnotationValidations.validate(IntRange.class, (IntRange) null, i, "from", 0L);
        this.mCommands.add(Command.newCommit(i));
        int i2 = this.mLastRequestedState;
        if (i2 == i) {
            this.mCommands.add(Command.newExit(i));
            this.mCommands.add(Command.newEnter(i));
        } else {
            while (!isIn(i, i2)) {
                this.mCommands.add(Command.newExit(i2));
                i2 >>= 4;
            }
            this.mTmp.clear();
            for (int i3 = i; !isIn(this.mLastRequestedState, i3); i3 >>= 4) {
                this.mTmp.add(i3);
            }
            for (int size = this.mTmp.size() - 1; size >= 0; size--) {
                this.mCommands.add(Command.newEnter(this.mTmp.get(size)));
            }
        }
        this.mLastRequestedState = i;
        while (!this.mCommands.isEmpty()) {
            Command remove = this.mCommands.remove();
            int i4 = remove.mType;
            if (i4 == 1) {
                this.mState = remove.mState;
            } else if (i4 == 2) {
                enter(remove.mState);
            } else if (i4 == 3) {
                exit(remove.mState);
            } else {
                Slog.e(TAG, "Unknown command type: " + remove.mType);
            }
        }
    }
}
