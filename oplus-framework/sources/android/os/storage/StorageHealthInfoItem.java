package android.os.storage;

/* loaded from: classes.dex */
public class StorageHealthInfoItem {
    private String mDescription;
    private boolean mHighFirst;
    private int mLength;
    private String mName;
    private int mOffset;
    private int mValue;

    public StorageHealthInfoItem(String name, int offset, int length, boolean highFirst, String description) {
        this.mName = name;
        this.mOffset = offset;
        this.mLength = length;
        this.mHighFirst = highFirst;
        this.mDescription = description;
    }

    public String getName() {
        return this.mName;
    }

    public int getOffset() {
        return this.mOffset;
    }

    public int getLength() {
        return this.mLength;
    }

    public boolean getHighFirst() {
        return this.mHighFirst;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setValue(int value) {
        this.mValue = value;
    }

    public int getValue() {
        return this.mValue;
    }
}
