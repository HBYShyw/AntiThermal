package android.graphics;

/* loaded from: classes.dex */
public class RenderNodeExtImpl extends OplusBaseRenderNode implements IRenderNodeExt {
    RenderNode mRenderNode;

    public RenderNodeExtImpl(Object base) {
        this.mRenderNode = (RenderNode) base;
    }

    @Override // android.graphics.OplusBaseRenderNode
    public long getNativeRenderNode() {
        return this.mRenderNode.mNativeRenderNode;
    }

    @Override // android.graphics.OplusBaseRenderNode
    public void setUsageForceDarkAlgorithmType(int algorithmType) {
        super.setUsageForceDarkAlgorithmType(algorithmType);
    }
}
