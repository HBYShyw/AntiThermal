package com.android.internal.inputmethod;

import android.util.Log;
import android.view.KeyEvent;
import android.widget.Editor;
import android.widget.IEditorExt;
import android.widget.ITextViewWrapper;
import android.widget.TextView;

/* loaded from: classes.dex */
public class EditableInputConnectionExtImpl implements IEditableInputConnectionExt {
    private static final String TAG = EditableInputConnectionExtImpl.class.getSimpleName();
    private EditableInputConnection mBase;

    public EditableInputConnectionExtImpl(Object base) {
        this.mBase = (EditableInputConnection) base;
    }

    public void handleSendKeyEvent(TextView textView, KeyEvent keyEvent) {
        log("handleSendKeyEvent");
        if (keyEvent == null) {
            return;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode == 67) {
            handleKeyCodeDel(textView, keyEvent);
        }
    }

    private void handleKeyCodeDel(TextView textView, KeyEvent keyEvent) {
        log("handleKeyCodeDel");
        int action = keyEvent.getAction();
        if (action == 0) {
            handleKeyCodeDelDown(textView, keyEvent);
        } else if (action == 1) {
            handleKeyCodeDelUp(textView, keyEvent);
        }
    }

    private void handleKeyCodeDelDown(TextView textView, KeyEvent keyEvent) {
        Editor editor;
        log("handleKeyCodeDelDown");
        if (textView == null || (editor = ((ITextViewWrapper) textView.getWrapper()).getEditor()) == null) {
            return;
        }
        IEditorExt editorExt = editor.getWrapper().getEditorExt();
        editorExt.handleKeyCodeDelDown(editor, keyEvent);
    }

    private void handleKeyCodeDelUp(TextView textView, KeyEvent keyEvent) {
        Editor editor;
        log("handleKeyCodeDelUp");
        if (textView == null || (editor = ((ITextViewWrapper) textView.getWrapper()).getEditor()) == null) {
            return;
        }
        IEditorExt editorExt = editor.getWrapper().getEditorExt();
        editorExt.handleKeyCodeDelUp(editor, keyEvent);
    }

    private void log(String content) {
        Log.d(TAG, content);
    }
}
