package com.oplus.wrapper.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class GnssMeasurement {
    private android.location.GnssMeasurement mGnssMeasurement;

    public GnssMeasurement(android.location.GnssMeasurement gnssMeasurement) {
        this.mGnssMeasurement = gnssMeasurement;
    }

    public Collection<CorrelationVector> getCorrelationVectors() {
        final Collection<CorrelationVector> wrapperCollection = new ArrayList<>();
        Collection<android.location.CorrelationVector> collection = this.mGnssMeasurement.getCorrelationVectors();
        collection.forEach(new Consumer() { // from class: com.oplus.wrapper.location.GnssMeasurement$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                wrapperCollection.add(new CorrelationVector((android.location.CorrelationVector) obj));
            }
        });
        return wrapperCollection;
    }
}
