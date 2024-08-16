package android.text;

import android.common.OplusFrameworkFactory;

/* loaded from: classes.dex */
public class StaticLayoutExtImpl implements IStaticLayoutExt {
    private StaticLayout mBase;
    public ITextJustificationHooks mBuilderJustificationHooksImpl = (ITextJustificationHooks) OplusFrameworkFactory.getInstance().getFeature(ITextJustificationHooks.DEFAULT, new Object[0]);
    ITextJustificationHooks mTextJustificationHooksImpl;

    public StaticLayoutExtImpl(Object base) {
        this.mBase = (StaticLayout) base;
    }

    public void setBuilderToTextJustificationHooks() {
        this.mTextJustificationHooksImpl = this.mBuilderJustificationHooksImpl;
    }

    public ITextJustificationHooks getBuilderJustificationHooks() {
        return this.mBuilderJustificationHooksImpl;
    }

    public void setTextJustificationHooks() {
        if (this.mTextJustificationHooksImpl == null) {
            this.mTextJustificationHooksImpl = (ITextJustificationHooks) OplusFrameworkFactory.getInstance().getFeature(ITextJustificationHooks.DEFAULT, new Object[0]);
        }
    }

    public float getLayoutParaSpacingAdded(StaticLayout layout, Object builder, boolean moreChars, CharSequence source, int endPos) {
        return this.mTextJustificationHooksImpl.getLayoutParaSpacingAdded(layout, builder, moreChars, source, endPos);
    }

    public boolean lineShouldIncludeFontPad(boolean firstLine, StaticLayout layout) {
        return this.mTextJustificationHooksImpl.lineShouldIncludeFontPad(firstLine, layout);
    }

    public boolean lineNeedMultiply(boolean needMultiply, boolean addLastLineLineSpacing, boolean lastLine, StaticLayout layout) {
        return this.mTextJustificationHooksImpl.lineNeedMultiply(needMultiply, addLastLineLineSpacing, lastLine, layout);
    }

    public void setLayoutParaSpacingAdded(Object object, float paraSpacing) {
        this.mBuilderJustificationHooksImpl.setLayoutParaSpacingAdded(object, paraSpacing);
    }
}
