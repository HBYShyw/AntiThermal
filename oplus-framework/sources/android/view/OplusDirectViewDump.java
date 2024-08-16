package android.view;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.TextView;
import com.oplus.deepthinker.OplusDeepThinkerManager;
import com.oplus.direct.IOplusDirectFindCallback;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.direct.OplusDirectFindCmds;
import com.oplus.direct.OplusDirectFindResult;
import com.oplus.direct.OplusDirectUtils;
import com.oplus.favorite.IOplusFavoriteConstans;
import com.oplus.favorite.IOplusFavoriteManager;
import com.oplus.favorite.OplusFavoriteCallback;
import com.oplus.favorite.OplusFavoriteData;
import com.oplus.favorite.OplusFavoriteHelper;
import com.oplus.favorite.OplusFavoriteResult;
import com.oplus.util.OplusLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusDirectViewDump implements IOplusFavoriteConstans {
    private static final boolean DBG = OplusDirectUtils.DBG;
    private static final String TAG = "DirectService";

    public void findCmd(ViewRootImpl viewAncestor, OplusDirectFindCmd findCmd) {
        OplusDirectFindCmds cmd = findCmd.getCommand();
        OplusLog.d(DBG, "DirectService", "findCmd : " + cmd);
        switch (AnonymousClass1.$SwitchMap$com$oplus$direct$OplusDirectFindCmds[cmd.ordinal()]) {
            case 1:
                Bundle bundle = findCmd.getBundle();
                ArrayList<String> idNames = bundle.getStringArrayList(OplusDirectFindCmd.EXTRA_ID_NAMES);
                findText(viewAncestor, findCmd.getCallback(), idNames);
                return;
            case 2:
                saveFavorite(viewAncestor, findCmd.getCallback());
                return;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                findFavorite(viewAncestor, findCmd.getCallback(), findCmd.getOplusDirectFindParam());
                return;
            default:
                OplusDirectUtils.onFindFailed(findCmd.getCallback(), OplusDirectFindResult.ERROR_UNKNOWN_CMD);
                return;
        }
    }

    /* renamed from: android.view.OplusDirectViewDump$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$oplus$direct$OplusDirectFindCmds;

        static {
            int[] iArr = new int[OplusDirectFindCmds.values().length];
            $SwitchMap$com$oplus$direct$OplusDirectFindCmds = iArr;
            try {
                iArr[OplusDirectFindCmds.FIND_TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.SAVE_FAVORITE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTIVITY_INFO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_VIEW_INFO.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_REFLECTION_INFO.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTION_LONG_CLICK.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTION_DOUBLE_CLICK.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_ACTION_CLICK.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_CONTENT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$oplus$direct$OplusDirectFindCmds[OplusDirectFindCmds.FIND_FAVORITE.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    private String getPackageName(String idName, String packageName) {
        String[] s = idName.toString().split(":");
        if (s != null && s.length == 2) {
            return s[0];
        }
        return packageName;
    }

    private String getIdName(String idName) {
        String[] s = idName.toString().split(":");
        if (s != null && s.length == 2) {
            return s[1].replace("id/", "");
        }
        return idName;
    }

    private CharSequence getTextFromView(ViewRootImpl viewAncestor, Resources resources, String idName, String packageName) {
        int id;
        View view;
        OplusLog.d(DBG, "DirectService", "getTextFromView : [" + packageName + ":id/" + idName + "]");
        View root = viewAncestor.getView();
        if (root == null || (id = resources.getIdentifier(idName, OplusDeepThinkerManager.ARG_ID, packageName)) <= 0 || (view = root.findViewById(id)) == null || !(view instanceof TextView)) {
            return null;
        }
        TextView textView = (TextView) view;
        return textView.getText();
    }

    private void findText(ViewRootImpl viewAncestor, IOplusDirectFindCallback callback, ArrayList<String> idNames) {
        if (idNames != null && idNames.size() > 0) {
            OplusLog.d(DBG, "DirectService", "findText");
            Context context = viewAncestor.mContext;
            Resources resources = context.getResources();
            String currPkgName = context.getPackageName();
            Iterator<String> it = idNames.iterator();
            while (it.hasNext()) {
                String idName = it.next();
                String packageName = getPackageName(idName, currPkgName);
                CharSequence text = getTextFromView(viewAncestor, resources, getIdName(idName), packageName);
                if (!TextUtils.isEmpty(text)) {
                    OplusLog.d(DBG, "DirectService", "findText : text=" + ((Object) text));
                    Bundle result = new Bundle();
                    result.putCharSequence(OplusDirectFindResult.EXTRA_RESULT_TEXT, text);
                    OplusDirectUtils.onFindSuccess(callback, result);
                    return;
                }
            }
            OplusDirectUtils.onFindFailed(callback, OplusDirectFindResult.ERROR_NO_TEXT);
            return;
        }
        OplusDirectUtils.onFindFailed(callback, OplusDirectFindResult.EXTRA_NO_IDNAMES);
    }

    private void findFavorite(ViewRootImpl viewAncestor, IOplusDirectFindCallback callback, String param) {
        OplusLog.d(DBG, "DirectService", "findFavorite");
        IOplusFavoriteManager favoriteManager = (IOplusFavoriteManager) OplusFeatureCache.getOrCreate(IOplusFavoriteManager.DEFAULT, new Object[0]);
        favoriteManager.processCrawl(viewAncestor.getView(), new FavoriteCallback(callback), param);
    }

    private void saveFavorite(ViewRootImpl viewAncestor, IOplusDirectFindCallback callback) {
        OplusLog.d(DBG, "DirectService", "saveFavorite");
        IOplusFavoriteManager favoriteManager = (IOplusFavoriteManager) OplusFeatureCache.getOrCreate(IOplusFavoriteManager.DEFAULT, new Object[0]);
        favoriteManager.processSave(viewAncestor.getView(), new FavoriteCallback(callback));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FavoriteCallback extends OplusFavoriteCallback {
        private final WeakReference<IOplusDirectFindCallback> mCallback;

        public FavoriteCallback(IOplusDirectFindCallback callback) {
            this.mCallback = new WeakReference<>(callback);
        }

        @Override // com.oplus.favorite.IOplusFavoriteCallback
        public boolean isSettingOn(Context context) {
            return OplusFavoriteHelper.isSettingOn(context);
        }

        @Override // com.oplus.favorite.OplusFavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public void onFavoriteStart(Context context, OplusFavoriteResult result) {
            IOplusDirectFindCallback callback = this.mCallback.get();
            if (callback != null) {
                try {
                    OplusDirectFindResult findResult = new OplusDirectFindResult();
                    Bundle bundle = findResult.getBundle();
                    String packageName = result.getPackageName();
                    OplusLog.d(false, this.TAG, "onFavoriteStart : packageName=" + packageName);
                    if (!TextUtils.isEmpty(packageName)) {
                        bundle.putString("package_name", packageName);
                    }
                    callback.onDirectInfoFound(findResult);
                } catch (RemoteException e) {
                    OplusLog.e(this.TAG, e.toString());
                } catch (Exception e2) {
                    OplusLog.e(this.TAG, e2.toString());
                }
            }
        }

        @Override // com.oplus.favorite.OplusFavoriteCallback, com.oplus.favorite.IOplusFavoriteCallback
        public void onFavoriteFinished(Context context, OplusFavoriteResult result) {
            IOplusDirectFindCallback callback = this.mCallback.get();
            if (callback != null) {
                try {
                    OplusDirectFindResult findResult = new OplusDirectFindResult();
                    Bundle bundle = findResult.getBundle();
                    bundle.putString("package_name", context.getPackageName());
                    ArrayList<OplusFavoriteData> data = result.getData();
                    if (!data.isEmpty()) {
                        OplusLog.d(false, this.TAG, "onFavoriteFinished : data=" + data.size() + ":" + data.get(0));
                        ArrayList<Bundle> bundles = extractData(data);
                        bundle.putParcelableArrayList(IOplusFavoriteConstans.EXTRA_RESULT_DATA, bundles);
                        ArrayList<String> titles = extractTitles(data);
                        bundle.putStringArrayList(IOplusFavoriteConstans.EXTRA_RESULT_TITLES, titles);
                    }
                    String error = result.getError();
                    if (!TextUtils.isEmpty(error)) {
                        OplusLog.d(false, this.TAG, "onFavoriteFinished : error=" + error);
                        bundle.putString(IOplusFavoriteConstans.EXTRA_RESULT_ERROR, error);
                    }
                    callback.onDirectInfoFound(findResult);
                } catch (RemoteException e) {
                    OplusLog.e(this.TAG, e.toString());
                } catch (Exception e2) {
                    OplusLog.e(this.TAG, e2.toString());
                }
            }
        }

        private ArrayList<Bundle> extractData(ArrayList<OplusFavoriteData> data) {
            ArrayList<Bundle> bundles = new ArrayList<>();
            synchronized (data) {
                Iterator<OplusFavoriteData> it = data.iterator();
                while (it.hasNext()) {
                    OplusFavoriteData d = it.next();
                    Bundle bundle = new Bundle();
                    bundle.putString(OplusFavoriteData.DATA_TITLE, d.getTitle());
                    bundle.putString(OplusFavoriteData.DATA_URL, d.getUrl());
                    bundle.putString(OplusFavoriteData.DATA_CONTENT, d.getContent());
                    bundle.putString(OplusFavoriteData.DATA_EXTRA, d.getExtra());
                    bundles.add(bundle);
                }
            }
            return bundles;
        }

        private ArrayList<String> extractTitles(ArrayList<OplusFavoriteData> data) {
            ArrayList<String> titles = new ArrayList<>();
            synchronized (data) {
                Iterator<OplusFavoriteData> it = data.iterator();
                while (it.hasNext()) {
                    OplusFavoriteData d = it.next();
                    titles.add(d.getTitle());
                }
            }
            return titles;
        }
    }
}
