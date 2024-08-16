package com.oplus.resolver;

import android.R;
import android.app.OplusUxIconConfigParser;
import android.app.uxicons.CustomAdaptiveIconConfig;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.OplusBaseConfiguration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.widget.RecyclerView;
import com.oplus.resolver.OplusOShareManager;
import com.oplus.resolver.OplusResolverGridAdapter;
import com.oplus.util.OplusChangeTextUtil;
import com.oplus.util.OplusTypeCastingHelper;
import com.oplus.widget.OplusItem;
import com.oplus.widget.OplusToolTips;
import java.util.List;

/* loaded from: classes.dex */
public class OplusResolverGridAdapter extends RecyclerView.Adapter<GridViewHolder> {
    private static final int DEFAULT_TYPE = 0;
    private static final int NEARBY_TYPE = 2;
    private static final int OSHARE_TYPE = 1;
    private final int mAppNameSize;
    private final Context mContext;
    private int mCurrentPagerNumber;
    private int mMoreIconIndex = -1;
    private final OplusOShareManager mOShareManager;
    private final int mOffsetWidth;
    private IOplusResolverGridItemClickListener mOnItemClickListener;
    private int mPagerNumber;
    private List<OplusItem> oplusItems;

    public OplusResolverGridAdapter(Context context, OplusOShareManager oShareManager) {
        this.mContext = context;
        this.mOShareManager = oShareManager;
        this.mOffsetWidth = context.getResources().getDimensionPixelSize(201654468);
        int textSize = context.getResources().getDimensionPixelSize(201654341);
        float fontScale = context.getResources().getConfiguration().fontScale;
        this.mAppNameSize = (int) OplusChangeTextUtil.getSuitableFontSize(textSize, fontScale, 2);
    }

    public void setOnItemClickListener(IOplusResolverGridItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View item = LayoutInflater.from(parent.getContext()).inflate(201917500, parent, false);
            return new OshareGridViewHolder(this.mOShareManager, item);
        }
        if (viewType == 2) {
            View item2 = LayoutInflater.from(parent.getContext()).inflate(201917502, parent, false);
            return new GridViewHolder(item2);
        }
        View item3 = LayoutInflater.from(parent.getContext()).inflate(201917467, parent, false);
        return new GridViewHolder(item3);
    }

    public void onBindViewHolder(final GridViewHolder holder, final int position) {
        int i;
        List<OplusItem> list = this.oplusItems;
        if (list == null || list.size() <= position) {
            return;
        }
        ViewGroup.LayoutParams params = holder.mLayout.getLayoutParams();
        Resources resources = this.mContext.getResources();
        if (OplusResolverPagerAdapter.COLUMN_SIZE >= 4) {
            i = 201654472;
        } else {
            i = 201654473;
        }
        params.width = resources.getDimensionPixelSize(i);
        holder.mLayout.setLayoutParams(params);
        holder.mIcon.setImageDrawable(this.oplusItems.get(position).getIcon());
        String packageName = this.oplusItems.get(position).getLabel();
        String resolverName = this.oplusItems.get(position).getText();
        holder.mPackageNameTx.setTextSize(0, this.mAppNameSize);
        holder.mResolverNameTx.setTextSize(0, this.mAppNameSize);
        boolean nearbyType = getItemViewType(position) == 2;
        if (TextUtils.isEmpty(packageName) && TextUtils.isEmpty(resolverName)) {
            if (holder.mPackageNameTx.getVisibility() != 4) {
                holder.mPackageNameTx.setVisibility(4);
            }
            if (holder.mResolverNameTx.getVisibility() != 4) {
                holder.mResolverNameTx.setVisibility(4);
            }
        } else if (!TextUtils.isEmpty(packageName) && TextUtils.isEmpty(resolverName)) {
            if (holder.mPackageNameTx.getVisibility() != 0) {
                holder.mPackageNameTx.setVisibility(0);
            }
            if (holder.mResolverNameTx.getVisibility() != 4) {
                holder.mResolverNameTx.setVisibility(4);
            }
            holder.mPackageNameTx.setText(packageName);
        } else if (TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(resolverName)) {
            if (holder.mPackageNameTx.getVisibility() != 0) {
                holder.mPackageNameTx.setVisibility(0);
            }
            if (holder.mResolverNameTx.getVisibility() != 4) {
                holder.mResolverNameTx.setVisibility(4);
            }
            holder.mPackageNameTx.setText(resolverName);
        } else {
            if (holder.mPackageNameTx.getVisibility() != 0) {
                holder.mPackageNameTx.setVisibility(0);
            }
            if (!nearbyType) {
                if (holder.mResolverNameTx.getVisibility() != 0) {
                    holder.mResolverNameTx.setVisibility(0);
                }
                holder.mPackageNameTx.setText(packageName);
                holder.mResolverNameTx.setText(resolverName);
            } else {
                if (holder.mResolverNameTx.getVisibility() != 4) {
                    holder.mResolverNameTx.setVisibility(4);
                }
                holder.mPackageNameTx.setText(resolverName);
            }
        }
        if (holder instanceof OshareGridViewHolder) {
            if (this.mPagerNumber == this.mCurrentPagerNumber) {
                ((OshareGridViewHolder) holder).showTips(this.mContext);
            }
            ((OshareGridViewHolder) holder).setOShareListener();
            OplusBaseConfiguration oplusBaseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, this.mContext.getResources().getConfiguration());
            if (oplusBaseConfiguration != null) {
                long iconConfig = oplusBaseConfiguration.mOplusExtraConfiguration.mUxIconConfig;
                int maxIconSize = new CustomAdaptiveIconConfig.Builder(this.mContext.getResources()).create().getDefaultIconSize();
                int scale = maxIconSize - OplusUxIconConfigParser.getPxFromIconConfigDp(this.mContext.getResources(), (int) ((iconConfig >> 16) & 65535));
                ((OshareGridViewHolder) holder).mDotViewGroup.setTranslationX(this.mOffsetWidth - (scale / 2.0f));
                ((OshareGridViewHolder) holder).mDotViewGroup.setTranslationY((scale / 2.0f) - this.mOffsetWidth);
            }
            OplusOShareManager oplusOShareManager = this.mOShareManager;
            if (oplusOShareManager != null && oplusOShareManager.isSwitchSendOn() && this.mOShareManager.getDeviceSize() != 0) {
                ((OshareGridViewHolder) holder).mDotText.setText(String.valueOf(this.mOShareManager.getDeviceSize()));
                ((OshareGridViewHolder) holder).mDotViewGroup.setVisibility(0);
            } else {
                ((OshareGridViewHolder) holder).mDotViewGroup.setVisibility(8);
            }
        }
        if (!nearbyType) {
            holder.mPackageNameTx.setCompoundDrawablesRelativeWithIntrinsicBounds(this.oplusItems.get(position).isPinned() ? this.mContext.getResources().getDrawable(201851000) : null, (Drawable) null, (Drawable) null, (Drawable) null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.resolver.OplusResolverGridAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OplusResolverGridAdapter.this.lambda$onBindViewHolder$0(position, view);
            }
        });
        holder.mIcon.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.resolver.OplusResolverGridAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OplusResolverGridAdapter.this.lambda$onBindViewHolder$1(position, view);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.oplus.resolver.OplusResolverGridAdapter$$ExternalSyntheticLambda2
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$onBindViewHolder$2;
                lambda$onBindViewHolder$2 = OplusResolverGridAdapter.this.lambda$onBindViewHolder$2(position, holder, view);
                return lambda$onBindViewHolder$2;
            }
        });
        holder.mIcon.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.oplus.resolver.OplusResolverGridAdapter$$ExternalSyntheticLambda3
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$onBindViewHolder$3;
                lambda$onBindViewHolder$3 = OplusResolverGridAdapter.this.lambda$onBindViewHolder$3(position, holder, view);
                return lambda$onBindViewHolder$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(int position, View view) {
        IOplusResolverGridItemClickListener iOplusResolverGridItemClickListener = this.mOnItemClickListener;
        if (iOplusResolverGridItemClickListener != null) {
            iOplusResolverGridItemClickListener.onItemClick(this.mPagerNumber, position);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(int position, View view) {
        IOplusResolverGridItemClickListener iOplusResolverGridItemClickListener = this.mOnItemClickListener;
        if (iOplusResolverGridItemClickListener != null) {
            iOplusResolverGridItemClickListener.onItemClick(this.mPagerNumber, position);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onBindViewHolder$2(int position, GridViewHolder holder, View view) {
        IOplusResolverGridItemClickListener iOplusResolverGridItemClickListener = this.mOnItemClickListener;
        if (iOplusResolverGridItemClickListener != null) {
            iOplusResolverGridItemClickListener.onItemLongClick(this.mPagerNumber, position);
            this.mOnItemClickListener.onItemLongClick(this.mPagerNumber, position, holder.mIcon);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onBindViewHolder$3(int position, GridViewHolder holder, View view) {
        IOplusResolverGridItemClickListener iOplusResolverGridItemClickListener = this.mOnItemClickListener;
        if (iOplusResolverGridItemClickListener != null) {
            iOplusResolverGridItemClickListener.onItemLongClick(this.mPagerNumber, position);
            this.mOnItemClickListener.onItemLongClick(this.mPagerNumber, position, holder.mIcon);
            return true;
        }
        return true;
    }

    public int getItemCount() {
        List<OplusItem> list = this.oplusItems;
        if (list == null) {
            return 0;
        }
        int i = this.mMoreIconIndex;
        return i != -1 ? i + 1 : list.size();
    }

    public int getItemViewType(int position) {
        ComponentName nearbyCN;
        String packageName = this.oplusItems.get(position).getPackageName();
        if (OplusOShareUtil.isOsharePackage(this.mContext, packageName)) {
            return 1;
        }
        if (position == 0 && this.mPagerNumber == 0 && (nearbyCN = NearbyUtil.getNearbySharingComponent(this.mContext)) != null && nearbyCN.getPackageName() != null && nearbyCN.getPackageName().equals(packageName)) {
            return 2;
        }
        return 0;
    }

    public void setOplusItems(List<OplusItem> oplusItems) {
        this.oplusItems = oplusItems;
    }

    public void setPagerNumber(int pagerNumber) {
        this.mPagerNumber = pagerNumber;
    }

    public void setCurrentPagerNumber(int pagerNumber) {
        this.mCurrentPagerNumber = pagerNumber;
        if (this.mPagerNumber == pagerNumber) {
            notifyDataSetChanged();
        }
    }

    public void startMoreAnimation(int iconIndex) {
        this.mMoreIconIndex = iconIndex;
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.oplus.resolver.OplusResolverGridAdapter$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                OplusResolverGridAdapter.this.lambda$startMoreAnimation$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startMoreAnimation$4() {
        int old = this.mMoreIconIndex + 1;
        this.mMoreIconIndex = -1;
        notifyItemRangeInserted(old, getItemCount());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        LinearLayout mLayout;
        TextView mPackageNameTx;
        TextView mResolverNameTx;

        public GridViewHolder(View itemView) {
            super(itemView);
            this.mIcon = (ImageView) itemView.findViewById(201457675);
            this.mPackageNameTx = (TextView) itemView.findViewById(R.id.text1);
            this.mResolverNameTx = (TextView) itemView.findViewById(201457676);
            this.mLayout = (LinearLayout) itemView.findViewById(201457809);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OshareGridViewHolder extends GridViewHolder {
        private static final String TAG = "OshareGridViewHolder";
        private static final long TIPS_SHOW_DELAY = 500;
        TextView mDotText;
        ViewGroup mDotViewGroup;
        private final OplusOShareManager.OShareListener mOShareListener;
        private final OplusOShareManager mOShareManager;
        OplusToolTips mTips;

        public OshareGridViewHolder(OplusOShareManager oShareManager, View itemView) {
            super(itemView);
            this.mOShareManager = oShareManager;
            this.mDotViewGroup = (ViewGroup) itemView.findViewById(201457836);
            this.mDotText = (TextView) itemView.findViewById(201457837);
            this.mOShareListener = new OplusOShareManager.OShareListener() { // from class: com.oplus.resolver.OplusResolverGridAdapter$OshareGridViewHolder$$ExternalSyntheticLambda0
                @Override // com.oplus.resolver.OplusOShareManager.OShareListener
                public final void onDevicesChange(boolean z, int i) {
                    OplusResolverGridAdapter.OshareGridViewHolder.this.lambda$new$0(z, i);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(boolean open, int size) {
            if (size == 0 || !open) {
                this.mDotViewGroup.setVisibility(4);
            } else {
                this.mDotText.setText(String.valueOf(size));
                this.mDotViewGroup.setVisibility(0);
            }
        }

        public void showTips(Context context) {
            SharedPreferences sp = OplusResolverSharedPrefs.getResolverSharedPrefs(context);
            if (sp.getBoolean(OplusResolverSharedPrefs.OTA_COLOROS13_TIPS, true)) {
                sp.edit().putBoolean(OplusResolverSharedPrefs.OTA_COLOROS13_TIPS, false).apply();
                this.mTips = new OplusToolTips(context);
                if (OplusResolverUtils.isRTLLanguage(context)) {
                    this.mTips.setTextDirection(2);
                }
                this.mTips.setDismissOnTouchOutside(true);
                this.mTips.setContent(context.getString(201589209));
                this.itemView.postDelayed(new Runnable() { // from class: com.oplus.resolver.OplusResolverGridAdapter$OshareGridViewHolder$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusResolverGridAdapter.OshareGridViewHolder.this.lambda$showTips$1();
                    }
                }, TIPS_SHOW_DELAY);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showTips$1() {
            try {
                this.mTips.show(this.mIcon);
            } catch (Exception error) {
                Log.e(TAG, "show tips error: " + error.getMessage());
            }
        }

        public void setOShareListener() {
            OplusOShareManager oplusOShareManager = this.mOShareManager;
            if (oplusOShareManager != null) {
                oplusOShareManager.addListener(this.mOShareListener);
                this.itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.oplus.resolver.OplusResolverGridAdapter.OshareGridViewHolder.1
                    @Override // android.view.View.OnAttachStateChangeListener
                    public void onViewAttachedToWindow(View v) {
                    }

                    @Override // android.view.View.OnAttachStateChangeListener
                    public void onViewDetachedFromWindow(View v) {
                        Log.d(OshareGridViewHolder.TAG, "setOShareListener onViewDetachedFromWindow");
                        OshareGridViewHolder.this.mIcon.removeOnAttachStateChangeListener(this);
                        OshareGridViewHolder.this.mOShareManager.removeListener(OshareGridViewHolder.this.mOShareListener);
                    }
                });
            }
        }
    }
}
