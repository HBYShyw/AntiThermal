package u1;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import c.AppCompatResources;
import com.support.appcompat.R$color;

/* compiled from: COUIClickableSpan.java */
/* renamed from: u1.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIClickableSpan extends ClickableSpan {

    /* renamed from: e, reason: collision with root package name */
    a f18846e;

    /* renamed from: f, reason: collision with root package name */
    private ColorStateList f18847f;

    /* compiled from: COUIClickableSpan.java */
    /* renamed from: u1.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void a();
    }

    public COUIClickableSpan(Context context) {
        this.f18847f = AppCompatResources.a(context, R$color.coui_clickable_text_color);
    }

    public void a(a aVar) {
        this.f18846e = aVar;
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        a aVar = this.f18846e;
        if (aVar != null) {
            aVar.a();
        }
    }

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(this.f18847f.getColorForState(textPaint.drawableState, 0));
    }
}
