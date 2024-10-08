package com.android.server.people.prediction;

import android.app.usage.UsageEvents;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.Range;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ChooserActivity;
import com.android.server.people.data.AppUsageStatsData;
import com.android.server.people.data.DataManager;
import com.android.server.people.data.Event;
import com.android.server.people.prediction.ShareTargetPredictor;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.ToLongFunction;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class SharesheetModelScorer {
    private static final boolean DEBUG = false;

    @VisibleForTesting
    static final float FOREGROUND_APP_WEIGHT = 0.0f;
    private static final float FREQUENTLY_USED_APP_SCORE_INITIAL_DECAY = 0.3f;
    private static final float RECENCY_INITIAL_BASE_SCORE = 0.4f;
    private static final float RECENCY_SCORE_INITIAL_DECAY = 0.05f;
    private static final float RECENCY_SCORE_SUBSEQUENT_DECAY = 0.02f;
    private static final String TAG = "SharesheetModelScorer";
    private static final float USAGE_STATS_CHOOSER_SCORE_INITIAL_DECAY = 0.9f;
    private static final Integer RECENCY_SCORE_COUNT = 6;
    private static final long ONE_MONTH_WINDOW = TimeUnit.DAYS.toMillis(30);
    private static final long FOREGROUND_APP_PROMO_TIME_WINDOW = TimeUnit.MINUTES.toMillis(10);

    @VisibleForTesting
    static final String CHOOSER_ACTIVITY = ChooserActivity.class.getSimpleName();

    private static float normalizeFreqScore(double d) {
        if (d >= 2.5d) {
            return 0.2f;
        }
        if (d >= 1.5d) {
            return 0.15f;
        }
        if (d >= 1.0d) {
            return 0.1f;
        }
        return d >= 0.75d ? RECENCY_SCORE_INITIAL_DECAY : FOREGROUND_APP_WEIGHT;
    }

    private static float normalizeMimeFreqScore(double d) {
        if (d >= 2.0d) {
            return 0.2f;
        }
        if (d >= 1.2d) {
            return 0.15f;
        }
        if (d > 0.0d) {
            return 0.1f;
        }
        return FOREGROUND_APP_WEIGHT;
    }

    private static float probOR(float f, float f2) {
        return 1.0f - ((1.0f - f) * (1.0f - f2));
    }

    private SharesheetModelScorer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void computeScore(List<ShareTargetPredictor.ShareTarget> list, int i, long j) {
        if (list.isEmpty()) {
            return;
        }
        PriorityQueue priorityQueue = new PriorityQueue(RECENCY_SCORE_COUNT.intValue(), Comparator.comparingLong(new ToLongFunction() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda2
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                long lambda$computeScore$0;
                lambda$computeScore$0 = SharesheetModelScorer.lambda$computeScore$0((Pair) obj);
                return lambda$computeScore$0;
            }
        }));
        ArrayList arrayList = new ArrayList(list.size());
        int i2 = 0;
        int i3 = 0;
        float f = 0.0f;
        float f2 = 0.0f;
        for (ShareTargetPredictor.ShareTarget shareTarget : list) {
            ShareTargetRankingScore shareTargetRankingScore = new ShareTargetRankingScore();
            arrayList.add(shareTargetRankingScore);
            if (shareTarget.getEventHistory() != null) {
                List<Range<Long>> activeTimeSlots = shareTarget.getEventHistory().getEventIndex(Event.SHARE_EVENT_TYPES).getActiveTimeSlots();
                if (!activeTimeSlots.isEmpty()) {
                    Iterator<Range<Long>> it = activeTimeSlots.iterator();
                    while (it.hasNext()) {
                        shareTargetRankingScore.incrementFrequencyScore(getFreqDecayedOnElapsedTime(j - it.next().getLower().longValue()));
                    }
                    f += shareTargetRankingScore.getFrequencyScore();
                    i2++;
                }
                List<Range<Long>> activeTimeSlots2 = shareTarget.getEventHistory().getEventIndex(i).getActiveTimeSlots();
                if (!activeTimeSlots2.isEmpty()) {
                    Iterator<Range<Long>> it2 = activeTimeSlots2.iterator();
                    while (it2.hasNext()) {
                        shareTargetRankingScore.incrementMimeFrequencyScore(getFreqDecayedOnElapsedTime(j - it2.next().getLower().longValue()));
                    }
                    f2 += shareTargetRankingScore.getMimeFrequencyScore();
                    i3++;
                }
                Range<Long> mostRecentActiveTimeSlot = shareTarget.getEventHistory().getEventIndex(Event.SHARE_EVENT_TYPES).getMostRecentActiveTimeSlot();
                if (mostRecentActiveTimeSlot != null) {
                    int size = priorityQueue.size();
                    Integer num = RECENCY_SCORE_COUNT;
                    if (size < num.intValue() || mostRecentActiveTimeSlot.getUpper().longValue() > ((Long) ((Range) ((Pair) priorityQueue.peek()).second).getUpper()).longValue()) {
                        if (priorityQueue.size() == num.intValue()) {
                            priorityQueue.poll();
                        }
                        priorityQueue.offer(new Pair(shareTargetRankingScore, mostRecentActiveTimeSlot));
                    }
                }
            }
        }
        while (!priorityQueue.isEmpty()) {
            ((ShareTargetRankingScore) ((Pair) priorityQueue.poll()).first).setRecencyScore(priorityQueue.size() > 1 ? 0.35f - ((priorityQueue.size() - 2) * RECENCY_SCORE_SUBSEQUENT_DECAY) : RECENCY_INITIAL_BASE_SCORE);
        }
        Float valueOf = Float.valueOf(i2 != 0 ? f / i2 : 0.0f);
        Float valueOf2 = Float.valueOf(i3 != 0 ? f2 / i3 : 0.0f);
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            ShareTargetPredictor.ShareTarget shareTarget2 = list.get(i4);
            ShareTargetRankingScore shareTargetRankingScore2 = (ShareTargetRankingScore) arrayList.get(i4);
            double d = 0.0d;
            shareTargetRankingScore2.setFrequencyScore(normalizeFreqScore(valueOf.equals(Float.valueOf(FOREGROUND_APP_WEIGHT)) ? 0.0d : shareTargetRankingScore2.getFrequencyScore() / valueOf.floatValue()));
            if (!valueOf2.equals(Float.valueOf(FOREGROUND_APP_WEIGHT))) {
                d = shareTargetRankingScore2.getMimeFrequencyScore() / valueOf2.floatValue();
            }
            shareTargetRankingScore2.setMimeFrequencyScore(normalizeMimeFreqScore(d));
            shareTargetRankingScore2.setTotalScore(probOR(probOR(shareTargetRankingScore2.getRecencyScore(), shareTargetRankingScore2.getFrequencyScore()), shareTargetRankingScore2.getMimeFrequencyScore()));
            shareTarget2.setScore(shareTargetRankingScore2.getTotalScore());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ long lambda$computeScore$0(Pair pair) {
        return ((Long) ((Range) pair.second).getUpper()).longValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void computeScoreForAppShare(List<ShareTargetPredictor.ShareTarget> list, int i, int i2, long j, DataManager dataManager, int i3) {
        computeScore(list, i, j);
        postProcess(list, i2, dataManager, i3);
    }

    private static void postProcess(List<ShareTargetPredictor.ShareTarget> list, int i, DataManager dataManager, int i2) {
        ArrayMap arrayMap = new ArrayMap();
        for (ShareTargetPredictor.ShareTarget shareTarget : list) {
            String packageName = shareTarget.getAppTarget().getPackageName();
            arrayMap.computeIfAbsent(packageName, new Function() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    List lambda$postProcess$1;
                    lambda$postProcess$1 = SharesheetModelScorer.lambda$postProcess$1((String) obj);
                    return lambda$postProcess$1;
                }
            });
            List list2 = (List) arrayMap.get(packageName);
            int i3 = 0;
            while (i3 < list2.size() && shareTarget.getScore() <= ((ShareTargetPredictor.ShareTarget) list2.get(i3)).getScore()) {
                i3++;
            }
            list2.add(i3, shareTarget);
        }
        promoteForegroundApp(arrayMap, dataManager, i2);
        promoteMostChosenAndFrequentlyUsedApps(arrayMap, i, dataManager, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List lambda$postProcess$1(String str) {
        return new ArrayList();
    }

    private static void promoteMostChosenAndFrequentlyUsedApps(Map<String, List<ShareTargetPredictor.ShareTarget>> map, int i, DataManager dataManager, int i2) {
        Iterator<List<ShareTargetPredictor.ShareTarget>> it = map.values().iterator();
        int i3 = 0;
        float f = 1.0f;
        while (it.hasNext()) {
            for (ShareTargetPredictor.ShareTarget shareTarget : it.next()) {
                if (shareTarget.getScore() > FOREGROUND_APP_WEIGHT) {
                    i3++;
                    f = Math.min(shareTarget.getScore(), f);
                }
            }
        }
        if (i3 >= i) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, AppUsageStatsData> queryAppUsageStats = dataManager.queryAppUsageStats(i2, currentTimeMillis - ONE_MONTH_WINDOW, currentTimeMillis, map.keySet());
        float promoteApp = promoteApp(map, queryAppUsageStats, new Function() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((AppUsageStatsData) obj).getChosenCount());
            }
        }, USAGE_STATS_CHOOSER_SCORE_INITIAL_DECAY * f, f);
        promoteApp(map, queryAppUsageStats, new Function() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((AppUsageStatsData) obj).getLaunchCount());
            }
        }, FREQUENTLY_USED_APP_SCORE_INITIAL_DECAY * promoteApp, promoteApp);
    }

    private static float promoteApp(Map<String, List<ShareTargetPredictor.ShareTarget>> map, Map<String, AppUsageStatsData> map2, Function<AppUsageStatsData, Integer> function, float f, float f2) {
        Iterator<AppUsageStatsData> it = map2.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            i = Math.max(i, function.apply(it.next()).intValue());
        }
        if (i > 0) {
            for (Map.Entry<String, AppUsageStatsData> entry : map2.entrySet()) {
                if (map.containsKey(entry.getKey())) {
                    ShareTargetPredictor.ShareTarget shareTarget = map.get(entry.getKey()).get(0);
                    if (shareTarget.getScore() <= FOREGROUND_APP_WEIGHT) {
                        float intValue = (function.apply(entry.getValue()).intValue() * f) / i;
                        shareTarget.setScore(intValue);
                        if (intValue > FOREGROUND_APP_WEIGHT) {
                            f2 = Math.min(f2, intValue);
                        }
                    }
                }
            }
        }
        return f2;
    }

    private static void promoteForegroundApp(Map<String, List<ShareTargetPredictor.ShareTarget>> map, DataManager dataManager, int i) {
        String findSharingForegroundApp = findSharingForegroundApp(map, dataManager, i);
        if (findSharingForegroundApp != null) {
            ShareTargetPredictor.ShareTarget shareTarget = map.get(findSharingForegroundApp).get(0);
            shareTarget.setScore(probOR(shareTarget.getScore(), FOREGROUND_APP_WEIGHT));
        }
    }

    private static String findSharingForegroundApp(Map<String, List<ShareTargetPredictor.ShareTarget>> map, DataManager dataManager, int i) {
        long currentTimeMillis = System.currentTimeMillis();
        List<UsageEvents.Event> queryAppMovingToForegroundEvents = dataManager.queryAppMovingToForegroundEvents(i, currentTimeMillis - FOREGROUND_APP_PROMO_TIME_WINDOW, currentTimeMillis);
        String str = null;
        for (int size = queryAppMovingToForegroundEvents.size() - 1; size >= 0; size--) {
            String className = queryAppMovingToForegroundEvents.get(size).getClassName();
            String packageName = queryAppMovingToForegroundEvents.get(size).getPackageName();
            if (packageName != null && ((className == null || !className.contains(CHOOSER_ACTIVITY)) && !packageName.contains(CHOOSER_ACTIVITY))) {
                if (str == null) {
                    str = packageName;
                } else if (!packageName.equals(str) && map.containsKey(packageName)) {
                    return packageName;
                }
            }
        }
        return null;
    }

    private static float getFreqDecayedOnElapsedTime(long j) {
        Duration ofMillis = Duration.ofMillis(j);
        if (ofMillis.compareTo(Duration.ofDays(1L)) <= 0) {
            return 1.0f;
        }
        if (ofMillis.compareTo(Duration.ofDays(3L)) <= 0) {
            return USAGE_STATS_CHOOSER_SCORE_INITIAL_DECAY;
        }
        if (ofMillis.compareTo(Duration.ofDays(7L)) <= 0) {
            return 0.8f;
        }
        return ofMillis.compareTo(Duration.ofDays(14L)) <= 0 ? 0.7f : 0.6f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ShareTargetRankingScore {
        private float mFrequencyScore;
        private float mMimeFrequencyScore;
        private float mRecencyScore;
        private float mTotalScore;

        private ShareTargetRankingScore() {
            this.mRecencyScore = SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
            this.mFrequencyScore = SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
            this.mMimeFrequencyScore = SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
            this.mTotalScore = SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
        }

        float getTotalScore() {
            return this.mTotalScore;
        }

        void setTotalScore(float f) {
            this.mTotalScore = f;
        }

        float getRecencyScore() {
            return this.mRecencyScore;
        }

        void setRecencyScore(float f) {
            this.mRecencyScore = f;
        }

        float getFrequencyScore() {
            return this.mFrequencyScore;
        }

        void setFrequencyScore(float f) {
            this.mFrequencyScore = f;
        }

        void incrementFrequencyScore(float f) {
            this.mFrequencyScore += f;
        }

        float getMimeFrequencyScore() {
            return this.mMimeFrequencyScore;
        }

        void setMimeFrequencyScore(float f) {
            this.mMimeFrequencyScore = f;
        }

        void incrementMimeFrequencyScore(float f) {
            this.mMimeFrequencyScore += f;
        }
    }
}
