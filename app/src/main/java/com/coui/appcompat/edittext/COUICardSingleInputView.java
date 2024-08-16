package com.coui.appcompat.edittext;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;

/* loaded from: classes.dex */
public class COUICardSingleInputView extends COUIInputView {
    public COUICardSingleInputView(Context context) {
        super(context);
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected COUIEditText S(Context context, AttributeSet attributeSet) {
        return new COUIEditText(context, attributeSet, R$attr.couiCardSingleInputEditTextStyle);
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected void b0() {
        int paddingTop;
        int paddingBottom;
        CheckBox checkBox = (CheckBox) findViewById(R$id.checkbox_password);
        COUIEditText editText = getEditText();
        if (!TextUtils.isEmpty(getTitle())) {
            paddingTop = getResources().getDimensionPixelSize(R$dimen.coui_single_input_edit_text_has_title_padding_top);
            paddingBottom = getResources().getDimensionPixelSize(R$dimen.coui_single_input_edit_text_has_title_padding_bottom);
            int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_single_input_edit_text_has_title_button_padding_bottom);
            View view = this.B;
            int i10 = paddingBottom - dimensionPixelSize;
            view.setPaddingRelative(view.getPaddingStart(), this.B.getPaddingTop(), this.B.getPaddingEnd(), i10);
            this.C.setPaddingRelative(0, 0, 0, i10);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) checkBox.getLayoutParams();
            marginLayoutParams.topMargin = getResources().getDimensionPixelSize(R$dimen.coui_input_edit_text_has_title_padding_top) / (-2);
            checkBox.setLayoutParams(marginLayoutParams);
        } else {
            paddingTop = editText.getPaddingTop();
            paddingBottom = editText.getPaddingBottom();
        }
        editText.setPaddingRelative(0, paddingTop, 0, paddingBottom);
    }

    @Override // com.coui.appcompat.edittext.COUIInputView
    protected int getLayoutResId() {
        return R$layout.coui_single_input_card_view;
    }

    public COUICardSingleInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public COUICardSingleInputView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
