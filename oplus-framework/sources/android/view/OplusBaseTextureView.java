package android.view;

import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class OplusBaseTextureView extends View {
    private static final boolean DEBUG = true;
    private static final String LOCAL_TAG = "OplusBaseTextureView";
    protected boolean mCallBackSizeChangeWhenLayerUpdate;
    protected boolean mReleaseTextureWhenDestory;

    public void setReleaseTextureWhenDestory(boolean release) {
        this.mReleaseTextureWhenDestory = release;
    }

    public OplusBaseTextureView(Context context) {
        super(context);
        this.mReleaseTextureWhenDestory = false;
        this.mCallBackSizeChangeWhenLayerUpdate = false;
    }

    public OplusBaseTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mReleaseTextureWhenDestory = false;
        this.mCallBackSizeChangeWhenLayerUpdate = false;
    }

    public OplusBaseTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mReleaseTextureWhenDestory = false;
        this.mCallBackSizeChangeWhenLayerUpdate = false;
    }

    public OplusBaseTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mReleaseTextureWhenDestory = false;
        this.mCallBackSizeChangeWhenLayerUpdate = false;
    }

    public boolean isReleaseTextureWhenDestory() {
        return this.mReleaseTextureWhenDestory;
    }

    public void setCallBackSizeChangeWhenLayerUpdate(boolean doCallBack) {
        this.mCallBackSizeChangeWhenLayerUpdate = doCallBack;
    }

    public boolean isCallBackSizeChangeWhenLayerUpdate() {
        return this.mCallBackSizeChangeWhenLayerUpdate;
    }
}
