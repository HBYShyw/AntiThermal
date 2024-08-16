package com.oplus.favorite;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

/* loaded from: classes.dex */
public class OplusFavoriteFactory {

    /* renamed from: com.oplus.favorite.OplusFavoriteFactory$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$favorite$OplusFavoriteEngines;

        static {
            int[] iArr = new int[OplusFavoriteEngines.values().length];
            $SwitchMap$com$oplus$favorite$OplusFavoriteEngines = iArr;
            try {
                iArr[OplusFavoriteEngines.TEDDY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    public OplusFavoriteEngine setEngine(OplusFavoriteEngines engine) {
        switch (AnonymousClass1.$SwitchMap$com$oplus$favorite$OplusFavoriteEngines[engine.ordinal()]) {
            case 1:
                return new OplusEngineTeddy();
            default:
                return new OplusEngineNone();
        }
    }

    /* loaded from: classes.dex */
    private static class OplusEngineNone extends OplusFavoriteEngine {
        private OplusEngineNone() {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onInit() {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onRelease() {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onLoadRule(Context context, String data, OplusFavoriteCallback callback) {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onProcessClick(View clickView, OplusFavoriteCallback callback) {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onProcessCrawl(View rootView, OplusFavoriteCallback callback, String param) {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onProcessSave(View rootView, OplusFavoriteCallback callback) {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onLogActivityInfo(Activity activity) {
        }

        @Override // com.oplus.favorite.OplusFavoriteEngine
        protected void onLogViewInfo(View view) {
        }

        @Override // com.oplus.favorite.IOplusFavoriteEngine
        public Handler getWorkHandler() {
            return null;
        }
    }
}
