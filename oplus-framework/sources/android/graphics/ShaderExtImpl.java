package android.graphics;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ShaderExtImpl implements IShaderExt {
    private Shader mShader;

    public ShaderExtImpl(Object base) {
        this.mShader = (Shader) base;
    }

    public void setColors(long[] color) {
        Shader shader = this.mShader;
        if (shader instanceof LinearGradient) {
            ((LinearGradient) shader).getWrapper().setColorLongs(color);
            this.mShader.discardNativeInstance();
        } else if (shader instanceof SweepGradient) {
            ((SweepGradient) shader).getWrapper().setColorLongs(color);
            this.mShader.discardNativeInstance();
        } else if (shader instanceof RadialGradient) {
            ((RadialGradient) shader).getWrapper().setColorLongs(color);
            this.mShader.discardNativeInstance();
        }
    }

    public long[] getColorLongs() {
        Shader shader = this.mShader;
        if (shader instanceof LinearGradient) {
            return ((LinearGradient) shader).getWrapper().getColorLongs();
        }
        if (shader instanceof SweepGradient) {
            return ((SweepGradient) shader).getWrapper().getColorLongs();
        }
        if (shader instanceof RadialGradient) {
            return ((RadialGradient) shader).getWrapper().getColorLongs();
        }
        return null;
    }

    public List<long[]> getComposeShaderColor() {
        Shader shader = this.mShader;
        if (shader instanceof ComposeShader) {
            Shader shaderA = ((ComposeShader) shader).getWrapper().getShaderA();
            Shader shaderB = ((ComposeShader) this.mShader).getWrapper().getShaderB();
            return getComposeShaderColor(shaderA, shaderB);
        }
        return null;
    }

    private List<long[]> getComposeShaderColor(Shader shaderA, Shader shaderB) {
        List<long[]> colors = new ArrayList<>();
        if (shaderA != null && shaderA.mShaderExt != null && shaderB != null && shaderB.mShaderExt != null) {
            colors.add(shaderA.mShaderExt.getColorLongs());
            colors.add(shaderB.mShaderExt.getColorLongs());
        }
        return colors;
    }

    public void setComposeShaderColor(List<long[]> color) {
        Shader shader = this.mShader;
        if (shader instanceof ComposeShader) {
            Shader shaderA = ((ComposeShader) shader).getWrapper().getShaderA();
            Shader shaderB = ((ComposeShader) this.mShader).getWrapper().getShaderB();
            setComposeShaderColor(shaderA, shaderB, color);
        }
    }

    private void setComposeShaderColor(Shader shaderA, Shader shaderB, List<long[]> color) {
        if (color != null && color.size() == 2 && shaderA != null && shaderA.mShaderExt != null && shaderB != null && shaderB.mShaderExt != null) {
            shaderA.mShaderExt.setColors(color.get(0));
            shaderB.mShaderExt.setColors(color.get(1));
            this.mShader.discardNativeInstance();
        }
    }
}
