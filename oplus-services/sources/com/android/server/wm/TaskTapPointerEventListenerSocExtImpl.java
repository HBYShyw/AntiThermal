package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class TaskTapPointerEventListenerSocExtImpl implements ITaskTapPointerEventListenerSocExt {
    TaskTapPointerEventListener mListener;

    @Override // com.android.server.wm.ITaskTapPointerEventListenerSocExt
    public void onPointerEventCheck() {
    }

    public TaskTapPointerEventListenerSocExtImpl(Object obj) {
        this.mListener = (TaskTapPointerEventListener) obj;
    }
}
