package cn.teddymobile.free.anteater.rule.html;

import android.view.View;

/* loaded from: classes.dex */
public interface HtmlRule {
    String getExtra(View view, String str);

    String getHtml(View view);

    String getTitle(View view);

    String getUrl(View view);
}
