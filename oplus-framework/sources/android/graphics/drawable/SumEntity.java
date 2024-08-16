package android.graphics.drawable;

/* loaded from: classes.dex */
public class SumEntity {
    float mMin = 0.0f;
    float mMax = 0.0f;
    float mTotal = 0.0f;
    int mCount = 0;

    public void reset() {
        this.mMin = 0.0f;
        this.mMax = 0.0f;
        this.mTotal = 0.0f;
        this.mCount = 0;
    }

    public void add(float sample) {
        if (this.mCount == 0) {
            this.mMin = sample;
            this.mMax = sample;
        } else {
            this.mMin = Math.min(this.mMin, sample);
            this.mMax = Math.max(this.mMax, sample);
        }
        this.mTotal += sample;
        this.mCount++;
    }

    public float average() {
        return this.mTotal / this.mCount;
    }

    public float min() {
        return this.mMin;
    }

    public float max() {
        return this.mMax;
    }

    public float delta() {
        return this.mMax - this.mMin;
    }

    public int count() {
        return this.mCount;
    }
}
