package com.android.server.integrity.serializer;

import android.content.integrity.AtomicFormula;
import android.content.integrity.CompoundFormula;
import android.content.integrity.InstallerAllowedByManifestFormula;
import android.content.integrity.IntegrityFormula;
import android.content.integrity.IntegrityUtils;
import android.content.integrity.Rule;
import com.android.internal.util.Preconditions;
import com.android.server.audio.AudioService$;
import com.android.server.integrity.model.BitOutputStream;
import com.android.server.integrity.model.ByteTrackedOutputStream;
import com.android.server.integrity.model.IndexingFileConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RuleBinarySerializer implements RuleSerializer {
    static final int INDEXED_RULE_SIZE_LIMIT = 100000;
    static final int NONINDEXED_RULE_SIZE_LIMIT = 1000;
    static final int TOTAL_RULE_SIZE_LIMIT = 200000;

    @Override // com.android.server.integrity.serializer.RuleSerializer
    public byte[] serialize(List<Rule> list, Optional<Integer> optional) throws RuleSerializeException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            serialize(list, optional, byteArrayOutputStream, new ByteArrayOutputStream());
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuleSerializeException(e.getMessage(), e);
        }
    }

    @Override // com.android.server.integrity.serializer.RuleSerializer
    public void serialize(List<Rule> list, Optional<Integer> optional, OutputStream outputStream, OutputStream outputStream2) throws RuleSerializeException {
        try {
            if (list == null) {
                throw new IllegalArgumentException("Null rules cannot be serialized.");
            }
            if (list.size() > TOTAL_RULE_SIZE_LIMIT) {
                throw new IllegalArgumentException("Too many rules provided: " + list.size());
            }
            Map<Integer, Map<String, List<Rule>>> splitRulesIntoIndexBuckets = RuleIndexingDetailsIdentifier.splitRulesIntoIndexBuckets(list);
            verifySize(splitRulesIntoIndexBuckets.get(1), 100000);
            verifySize(splitRulesIntoIndexBuckets.get(2), 100000);
            verifySize(splitRulesIntoIndexBuckets.get(0), 1000);
            ByteTrackedOutputStream byteTrackedOutputStream = new ByteTrackedOutputStream(outputStream);
            serializeRuleFileMetadata(optional, byteTrackedOutputStream);
            LinkedHashMap<String, Integer> serializeRuleList = serializeRuleList(splitRulesIntoIndexBuckets.get(1), byteTrackedOutputStream);
            LinkedHashMap<String, Integer> serializeRuleList2 = serializeRuleList(splitRulesIntoIndexBuckets.get(2), byteTrackedOutputStream);
            LinkedHashMap<String, Integer> serializeRuleList3 = serializeRuleList(splitRulesIntoIndexBuckets.get(0), byteTrackedOutputStream);
            BitOutputStream bitOutputStream = new BitOutputStream(outputStream2);
            serializeIndexGroup(serializeRuleList, bitOutputStream, true);
            serializeIndexGroup(serializeRuleList2, bitOutputStream, true);
            serializeIndexGroup(serializeRuleList3, bitOutputStream, false);
            bitOutputStream.flush();
        } catch (Exception e) {
            throw new RuleSerializeException(e.getMessage(), e);
        }
    }

    private void verifySize(Map<String, List<Rule>> map, int i) {
        int intValue = ((Integer) map.values().stream().map(new Function() { // from class: com.android.server.integrity.serializer.RuleBinarySerializer$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer lambda$verifySize$0;
                lambda$verifySize$0 = RuleBinarySerializer.lambda$verifySize$0((List) obj);
                return lambda$verifySize$0;
            }
        }).collect(Collectors.summingInt(new AudioService$.ExternalSyntheticLambda3()))).intValue();
        if (intValue <= i) {
            return;
        }
        throw new IllegalArgumentException("Too many rules provided in the indexing group. Provided " + intValue + " limit " + i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Integer lambda$verifySize$0(List list) {
        return Integer.valueOf(list.size());
    }

    private void serializeRuleFileMetadata(Optional<Integer> optional, ByteTrackedOutputStream byteTrackedOutputStream) throws IOException {
        int intValue = optional.orElse(1).intValue();
        BitOutputStream bitOutputStream = new BitOutputStream(byteTrackedOutputStream);
        bitOutputStream.setNext(8, intValue);
        bitOutputStream.flush();
    }

    private LinkedHashMap<String, Integer> serializeRuleList(Map<String, List<Rule>> map, ByteTrackedOutputStream byteTrackedOutputStream) throws IOException {
        Preconditions.checkArgument(map != null, "serializeRuleList should never be called with null rule list.");
        BitOutputStream bitOutputStream = new BitOutputStream(byteTrackedOutputStream);
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(IndexingFileConstants.START_INDEXING_KEY, Integer.valueOf(byteTrackedOutputStream.getWrittenBytesCount()));
        int i = 0;
        for (String str : (List) map.keySet().stream().sorted().collect(Collectors.toList())) {
            if (i >= 50) {
                linkedHashMap.put(str, Integer.valueOf(byteTrackedOutputStream.getWrittenBytesCount()));
                i = 0;
            }
            Iterator<Rule> it = map.get(str).iterator();
            while (it.hasNext()) {
                serializeRule(it.next(), bitOutputStream);
                bitOutputStream.flush();
                i++;
            }
        }
        linkedHashMap.put(IndexingFileConstants.END_INDEXING_KEY, Integer.valueOf(byteTrackedOutputStream.getWrittenBytesCount()));
        return linkedHashMap;
    }

    private void serializeRule(Rule rule, BitOutputStream bitOutputStream) throws IOException {
        if (rule == null) {
            throw new IllegalArgumentException("Null rule can not be serialized");
        }
        bitOutputStream.setNext();
        serializeFormula(rule.getFormula(), bitOutputStream);
        bitOutputStream.setNext(3, rule.getEffect());
        bitOutputStream.setNext();
    }

    private void serializeFormula(IntegrityFormula integrityFormula, BitOutputStream bitOutputStream) throws IOException {
        if (integrityFormula instanceof AtomicFormula) {
            serializeAtomicFormula((AtomicFormula) integrityFormula, bitOutputStream);
        } else if (integrityFormula instanceof CompoundFormula) {
            serializeCompoundFormula((CompoundFormula) integrityFormula, bitOutputStream);
        } else {
            if (integrityFormula instanceof InstallerAllowedByManifestFormula) {
                bitOutputStream.setNext(3, 3);
                return;
            }
            throw new IllegalArgumentException(String.format("Invalid formula type: %s", integrityFormula.getClass()));
        }
    }

    private void serializeCompoundFormula(CompoundFormula compoundFormula, BitOutputStream bitOutputStream) throws IOException {
        if (compoundFormula == null) {
            throw new IllegalArgumentException("Null compound formula can not be serialized");
        }
        bitOutputStream.setNext(3, 1);
        bitOutputStream.setNext(2, compoundFormula.getConnector());
        Iterator it = compoundFormula.getFormulas().iterator();
        while (it.hasNext()) {
            serializeFormula((IntegrityFormula) it.next(), bitOutputStream);
        }
        bitOutputStream.setNext(3, 2);
    }

    private void serializeAtomicFormula(AtomicFormula atomicFormula, BitOutputStream bitOutputStream) throws IOException {
        if (atomicFormula == null) {
            throw new IllegalArgumentException("Null atomic formula can not be serialized");
        }
        bitOutputStream.setNext(3, 0);
        bitOutputStream.setNext(4, atomicFormula.getKey());
        if (atomicFormula.getTag() == 1) {
            AtomicFormula.StringAtomicFormula stringAtomicFormula = (AtomicFormula.StringAtomicFormula) atomicFormula;
            bitOutputStream.setNext(3, 0);
            serializeStringValue(stringAtomicFormula.getValue(), stringAtomicFormula.getIsHashedValue().booleanValue(), bitOutputStream);
        } else {
            if (atomicFormula.getTag() == 2) {
                AtomicFormula.LongAtomicFormula longAtomicFormula = (AtomicFormula.LongAtomicFormula) atomicFormula;
                bitOutputStream.setNext(3, longAtomicFormula.getOperator().intValue());
                long longValue = longAtomicFormula.getValue().longValue();
                serializeIntValue((int) (longValue >>> 32), bitOutputStream);
                serializeIntValue((int) longValue, bitOutputStream);
                return;
            }
            if (atomicFormula.getTag() == 3) {
                bitOutputStream.setNext(3, 0);
                serializeBooleanValue(((AtomicFormula.BooleanAtomicFormula) atomicFormula).getValue().booleanValue(), bitOutputStream);
                return;
            }
            throw new IllegalArgumentException(String.format("Invalid atomic formula type: %s", atomicFormula.getClass()));
        }
    }

    private void serializeIndexGroup(LinkedHashMap<String, Integer> linkedHashMap, BitOutputStream bitOutputStream, boolean z) throws IOException {
        serializeStringValue(IndexingFileConstants.START_INDEXING_KEY, false, bitOutputStream);
        serializeIntValue(linkedHashMap.get(IndexingFileConstants.START_INDEXING_KEY).intValue(), bitOutputStream);
        if (z) {
            for (Map.Entry<String, Integer> entry : linkedHashMap.entrySet()) {
                if (!entry.getKey().equals(IndexingFileConstants.START_INDEXING_KEY) && !entry.getKey().equals(IndexingFileConstants.END_INDEXING_KEY)) {
                    serializeStringValue(entry.getKey(), false, bitOutputStream);
                    serializeIntValue(entry.getValue().intValue(), bitOutputStream);
                }
            }
        }
        serializeStringValue(IndexingFileConstants.END_INDEXING_KEY, false, bitOutputStream);
        serializeIntValue(linkedHashMap.get(IndexingFileConstants.END_INDEXING_KEY).intValue(), bitOutputStream);
    }

    private void serializeStringValue(String str, boolean z, BitOutputStream bitOutputStream) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("String value can not be null.");
        }
        byte[] bytesForString = getBytesForString(str, z);
        bitOutputStream.setNext(z);
        bitOutputStream.setNext(8, bytesForString.length);
        for (byte b : bytesForString) {
            bitOutputStream.setNext(8, b);
        }
    }

    private void serializeIntValue(int i, BitOutputStream bitOutputStream) throws IOException {
        bitOutputStream.setNext(32, i);
    }

    private void serializeBooleanValue(boolean z, BitOutputStream bitOutputStream) throws IOException {
        bitOutputStream.setNext(z);
    }

    private static byte[] getBytesForString(String str, boolean z) {
        if (!z) {
            return str.getBytes(StandardCharsets.UTF_8);
        }
        return IntegrityUtils.getBytesFromHexDigest(str);
    }
}
