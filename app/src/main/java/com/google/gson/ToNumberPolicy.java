package com.google.gson;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.math.BigDecimal;

/* loaded from: classes.dex */
public enum ToNumberPolicy implements ToNumberStrategy {
    DOUBLE { // from class: com.google.gson.ToNumberPolicy.1
        @Override // com.google.gson.ToNumberStrategy
        public Double readNumber(JsonReader jsonReader) {
            return Double.valueOf(jsonReader.nextDouble());
        }
    },
    LAZILY_PARSED_NUMBER { // from class: com.google.gson.ToNumberPolicy.2
        @Override // com.google.gson.ToNumberStrategy
        public Number readNumber(JsonReader jsonReader) {
            return new LazilyParsedNumber(jsonReader.nextString());
        }
    },
    LONG_OR_DOUBLE { // from class: com.google.gson.ToNumberPolicy.3
        @Override // com.google.gson.ToNumberStrategy
        public Number readNumber(JsonReader jsonReader) {
            String nextString = jsonReader.nextString();
            try {
                try {
                    return Long.valueOf(Long.parseLong(nextString));
                } catch (NumberFormatException unused) {
                    Double valueOf = Double.valueOf(nextString);
                    if ((!valueOf.isInfinite() && !valueOf.isNaN()) || jsonReader.isLenient()) {
                        return valueOf;
                    }
                    throw new MalformedJsonException("JSON forbids NaN and infinities: " + valueOf + "; at path " + jsonReader.getPreviousPath());
                }
            } catch (NumberFormatException e10) {
                throw new JsonParseException("Cannot parse " + nextString + "; at path " + jsonReader.getPreviousPath(), e10);
            }
        }
    },
    BIG_DECIMAL { // from class: com.google.gson.ToNumberPolicy.4
        @Override // com.google.gson.ToNumberStrategy
        public BigDecimal readNumber(JsonReader jsonReader) {
            String nextString = jsonReader.nextString();
            try {
                return new BigDecimal(nextString);
            } catch (NumberFormatException e10) {
                throw new JsonParseException("Cannot parse " + nextString + "; at path " + jsonReader.getPreviousPath(), e10);
            }
        }
    }
}
