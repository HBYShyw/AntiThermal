package android.net;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ResolverParamsParcel implements Parcelable {
    public static final Parcelable.Creator<ResolverParamsParcel> CREATOR = new Parcelable.Creator<ResolverParamsParcel>() { // from class: android.net.ResolverParamsParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResolverParamsParcel createFromParcel(Parcel parcel) {
            ResolverParamsParcel resolverParamsParcel = new ResolverParamsParcel();
            resolverParamsParcel.readFromParcel(parcel);
            return resolverParamsParcel;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResolverParamsParcel[] newArray(int i) {
            return new ResolverParamsParcel[i];
        }
    };
    public String[] domains;
    public ResolverOptionsParcel resolverOptions;
    public String[] servers;
    public String tlsName;
    public String[] tlsServers;
    public int netId = 0;
    public int sampleValiditySeconds = 0;
    public int successThreshold = 0;
    public int minSamples = 0;
    public int maxSamples = 0;
    public int baseTimeoutMsec = 0;
    public int retryCount = 0;
    public String[] tlsFingerprints = new String[0];
    public String caCertificate = "";
    public int tlsConnectTimeoutMs = 0;
    public int[] transportTypes = new int[0];

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.netId);
        parcel.writeInt(this.sampleValiditySeconds);
        parcel.writeInt(this.successThreshold);
        parcel.writeInt(this.minSamples);
        parcel.writeInt(this.maxSamples);
        parcel.writeInt(this.baseTimeoutMsec);
        parcel.writeInt(this.retryCount);
        parcel.writeStringArray(this.servers);
        parcel.writeStringArray(this.domains);
        parcel.writeString(this.tlsName);
        parcel.writeStringArray(this.tlsServers);
        parcel.writeStringArray(this.tlsFingerprints);
        parcel.writeString(this.caCertificate);
        parcel.writeInt(this.tlsConnectTimeoutMs);
        parcel.writeTypedObject(this.resolverOptions, i);
        parcel.writeIntArray(this.transportTypes);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        try {
            if (readInt < 4) {
                throw new BadParcelableException("Parcelable too small");
            }
            if (parcel.dataPosition() - dataPosition < readInt) {
                this.netId = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.sampleValiditySeconds = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.successThreshold = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.minSamples = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.maxSamples = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.baseTimeoutMsec = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.retryCount = parcel.readInt();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.servers = parcel.createStringArray();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.domains = parcel.createStringArray();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.tlsName = parcel.readString();
                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                        this.tlsServers = parcel.createStringArray();
                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                            this.tlsFingerprints = parcel.createStringArray();
                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                this.caCertificate = parcel.readString();
                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                    this.tlsConnectTimeoutMs = parcel.readInt();
                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                        this.resolverOptions = (ResolverOptionsParcel) parcel.readTypedObject(ResolverOptionsParcel.CREATOR);
                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                            this.transportTypes = parcel.createIntArray();
                                                                            if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                                                            }
                                                                            parcel.setDataPosition(dataPosition + readInt);
                                                                            return;
                                                                        }
                                                                        if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                                                        }
                                                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                                                    }
                                                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                                                }
                                                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                                            }
                                                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                                        }
                                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                                    }
                                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                                }
                                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                            }
                                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                        }
                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                    }
                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                }
                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                throw new BadParcelableException("Overflow in the size of parcelable");
                            }
                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            parcel.setDataPosition(dataPosition + readInt);
        } catch (Throwable th) {
            if (dataPosition > Integer.MAX_VALUE - readInt) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            parcel.setDataPosition(dataPosition + readInt);
            throw th;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.resolverOptions) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
