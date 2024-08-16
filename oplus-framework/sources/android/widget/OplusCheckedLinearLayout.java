package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/* loaded from: classes.dex */
public class OplusCheckedLinearLayout extends LinearLayout implements Checkable {
    public OplusCheckedLinearLayout(Context context) {
        this(context, null);
    }

    public OplusCheckedLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusCheckedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public OplusCheckedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            KeyEvent.Callback childAt = getChildAt(i);
            if (childAt instanceof Checkable) {
                return ((Checkable) childAt).isChecked();
            }
        }
        return false;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            KeyEvent.Callback childAt = getChildAt(i);
            if (childAt instanceof Checkable) {
                ((Checkable) childAt).setChecked(checked);
            }
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            KeyEvent.Callback childAt = getChildAt(i);
            if (childAt instanceof Checkable) {
                ((Checkable) childAt).toggle();
            }
        }
    }
}
