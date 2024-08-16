package vendor.pixelworks.hardware.display;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class IrisFixedConfig implements Parcelable {
    public static final Parcelable.Creator<IrisFixedConfig> CREATOR = new Parcelable.Creator<IrisFixedConfig>() { // from class: vendor.pixelworks.hardware.display.IrisFixedConfig.1
        @Override // android.os.Parcelable.Creator
        public IrisFixedConfig createFromParcel(Parcel parcel) {
            IrisFixedConfig irisFixedConfig = new IrisFixedConfig();
            irisFixedConfig.readFromParcel(parcel);
            return irisFixedConfig;
        }

        @Override // android.os.Parcelable.Creator
        public IrisFixedConfig[] newArray(int i) {
            return new IrisFixedConfig[i];
        }
    };
    public int[] hdrLut;
    public int[] reserved;
    public int hdrFormal = 0;
    public int memcEnable = 0;
    public int memcLevel = 0;
    public int dualChannel = 0;
    public int movingLayer = 0;
    public int memcVideoLayer = 0;
    public int inMemcState = 0;
    public int videoFps = 0;
    public int videoInMemory = 0;
    public int gameMode = 0;
    public int captureDisable = 0;
    public int dualPrepare = 0;
    public int inOsdSwitch = 0;
    public int dualPreload = 0;
    public int metadataDone = 0;
    public int memcToPt = 0;
    public int clientCompRequest = 0;
    public int hdrRequest = 0;
    public int motionLayerIdUsing = 0;
    public int testOption = 0;
    public int activeTask = 0;
    public int emvMvdId = 0;
    public int emvGameId = 0;
    public int pqSwitchType = 0;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.hdrFormal);
        parcel.writeIntArray(this.hdrLut);
        parcel.writeInt(this.memcEnable);
        parcel.writeInt(this.memcLevel);
        parcel.writeInt(this.dualChannel);
        parcel.writeInt(this.movingLayer);
        parcel.writeInt(this.memcVideoLayer);
        parcel.writeInt(this.inMemcState);
        parcel.writeInt(this.videoFps);
        parcel.writeInt(this.videoInMemory);
        parcel.writeInt(this.gameMode);
        parcel.writeInt(this.captureDisable);
        parcel.writeInt(this.dualPrepare);
        parcel.writeInt(this.inOsdSwitch);
        parcel.writeInt(this.dualPreload);
        parcel.writeInt(this.metadataDone);
        parcel.writeInt(this.memcToPt);
        parcel.writeInt(this.clientCompRequest);
        parcel.writeInt(this.hdrRequest);
        parcel.writeInt(this.motionLayerIdUsing);
        parcel.writeInt(this.testOption);
        parcel.writeInt(this.activeTask);
        parcel.writeInt(this.emvMvdId);
        parcel.writeInt(this.emvGameId);
        parcel.writeInt(this.pqSwitchType);
        parcel.writeIntArray(this.reserved);
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
                this.hdrFormal = parcel.readInt();
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.hdrLut = parcel.createIntArray();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.memcEnable = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.memcLevel = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.dualChannel = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.movingLayer = parcel.readInt();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.memcVideoLayer = parcel.readInt();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.inMemcState = parcel.readInt();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.videoFps = parcel.readInt();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.videoInMemory = parcel.readInt();
                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                        this.gameMode = parcel.readInt();
                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                            this.captureDisable = parcel.readInt();
                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                this.dualPrepare = parcel.readInt();
                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                    this.inOsdSwitch = parcel.readInt();
                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                        this.dualPreload = parcel.readInt();
                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                            this.metadataDone = parcel.readInt();
                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                this.memcToPt = parcel.readInt();
                                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                    this.clientCompRequest = parcel.readInt();
                                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                        this.hdrRequest = parcel.readInt();
                                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                            this.motionLayerIdUsing = parcel.readInt();
                                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                this.testOption = parcel.readInt();
                                                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                    this.activeTask = parcel.readInt();
                                                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                        this.emvMvdId = parcel.readInt();
                                                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                            this.emvGameId = parcel.readInt();
                                                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                                this.pqSwitchType = parcel.readInt();
                                                                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                                    this.reserved = parcel.createIntArray();
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
}
