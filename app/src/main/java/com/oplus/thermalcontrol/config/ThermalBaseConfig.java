package com.oplus.thermalcontrol.config;

import android.util.ArrayMap;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.GpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.HeatSourceLevelConfig;
import com.oplus.thermalcontrol.config.feature.HeatSourceOffsetConfig;
import com.oplus.thermalcontrol.config.feature.TemperatureLevelConfig;
import com.oplus.thermalcontrol.config.feature.TsensorConfig;
import com.oplus.thermalcontrol.config.feature.TsensorExceptScenesConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: classes2.dex */
public class ThermalBaseConfig {
    public static final String CONFIG_NAME = "ThermalBaseConfig";
    public static final String FEATURE_NAME = "ThermalBaseConfig";
    private static final String TAG = "ThermalBaseConfig";
    public static final int TYPE_FEATURE = 1;
    public static final int TYPE_POLICY = 2;
    public String mConfigName;
    private final Map<String, String> mConfigProperties = new ConcurrentHashMap();
    private final List<Item> mItems = new CopyOnWriteArrayList();
    public int mTagName;
    public int mType;

    /* loaded from: classes2.dex */
    public static class Item {
        public static final String ATTR_INDEX = "index";
        public static final String ATTR_LEVEL = "level";
        public static final String ATTR_NAME = "name";
        public static final String ATTR_VALUE = "value";
        public String mName;
        public List<SubItem> mSubItemList;
        public String mTagName;
        public Map<String, String> mProperties = new ArrayMap();
        public int mLevel = Integer.MIN_VALUE;
    }

    /* loaded from: classes2.dex */
    public static class SubItem {
        public static final String ATTR_COMPONENT = "component";
        public String mName;
        public String mTagName;
        public final Map<String, String> mProperties = new ArrayMap();
        public int mLevel = Integer.MIN_VALUE;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x004d, code lost:
    
        if (r3.equals(com.oplus.thermalcontrol.config.feature.HeatSourceOffsetConfig.CONFIG_NAME) == false) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ThermalBaseConfig create(int i10, String str) {
        char c10 = 1;
        if (i10 == 1) {
            str.hashCode();
            switch (str.hashCode()) {
                case -1914910208:
                    if (str.equals(GpuLevelConfig.CONFIG_NAME)) {
                        c10 = 0;
                        break;
                    }
                    c10 = 65535;
                    break;
                case -1412703634:
                    break;
                case -1034736338:
                    if (str.equals(TsensorConfig.CONFIG_NAME)) {
                        c10 = 2;
                        break;
                    }
                    c10 = 65535;
                    break;
                case -833233671:
                    if (str.equals(TemperatureLevelConfig.CONFIG_NAME)) {
                        c10 = 3;
                        break;
                    }
                    c10 = 65535;
                    break;
                case -296216956:
                    if (str.equals(CpuLevelConfig.CONFIG_NAME)) {
                        c10 = 4;
                        break;
                    }
                    c10 = 65535;
                    break;
                case 95073101:
                    if (str.equals(TsensorExceptScenesConfig.CONFIG_NAME)) {
                        c10 = 5;
                        break;
                    }
                    c10 = 65535;
                    break;
                case 576982096:
                    if (str.equals(HeatSourceLevelConfig.CONFIG_NAME)) {
                        c10 = 6;
                        break;
                    }
                    c10 = 65535;
                    break;
                default:
                    c10 = 65535;
                    break;
            }
            switch (c10) {
                case 0:
                    return new GpuLevelConfig();
                case 1:
                    return new HeatSourceOffsetConfig();
                case 2:
                    return new TsensorConfig();
                case 3:
                    return new TemperatureLevelConfig();
                case 4:
                    return new CpuLevelConfig();
                case 5:
                    return new TsensorExceptScenesConfig();
                case 6:
                    return new HeatSourceLevelConfig();
            }
        }
        return new ThermalBaseConfig();
    }

    private void parseSubItems(Element element, Item item) {
        if (element.hasChildNodes()) {
            NodeList childNodes = element.getChildNodes();
            item.mSubItemList = new ArrayList();
            for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
                Node item2 = childNodes.item(i10);
                if (item2.getNodeType() == 1) {
                    Element element2 = (Element) item2;
                    SubItem subItem = new SubItem();
                    NamedNodeMap attributes = element2.getAttributes();
                    for (int i11 = 0; i11 < attributes.getLength(); i11++) {
                        Node item3 = attributes.item(i11);
                        String nodeName = item3.getNodeName();
                        String nodeValue = item3.getNodeValue();
                        subItem.mProperties.put(nodeName, nodeValue);
                        if (!"level".equals(nodeName) && !Item.ATTR_INDEX.equals(nodeName)) {
                            if ("name".equals(nodeName) || SubItem.ATTR_COMPONENT.equals(nodeName)) {
                                subItem.mName = nodeValue;
                            }
                        } else {
                            subItem.mLevel = Integer.parseInt(nodeValue);
                        }
                    }
                    subItem.mTagName = element2.getTagName();
                    onSubItemLoaded(item, subItem);
                }
            }
        }
    }

    public <T> T get(Class<T> cls) {
        if (getClass().equals(cls)) {
            return cls.cast(this);
        }
        return null;
    }

    public List<Item> getConfigItems() {
        return this.mItems;
    }

    public String getConfigName() {
        return this.mConfigName;
    }

    public Map<String, String> getConfigProperties() {
        return this.mConfigProperties;
    }

    protected void onItemLoaded(Item item) {
        this.mItems.add(item);
    }

    protected void onSubItemLoaded(Item item, SubItem subItem) {
        item.mSubItemList.add(subItem);
    }

    public void parseConfigItems(Element element) {
        NodeList childNodes = element.getChildNodes();
        this.mItems.clear();
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if (item.getNodeType() == 1) {
                Element element2 = (Element) item;
                Item item2 = new Item();
                NamedNodeMap attributes = element2.getAttributes();
                for (int i11 = 0; i11 < attributes.getLength(); i11++) {
                    Node item3 = attributes.item(i11);
                    String nodeName = item3.getNodeName();
                    String nodeValue = item3.getNodeValue();
                    item2.mProperties.put(nodeName, nodeValue);
                    if (!"level".equals(nodeName) && !Item.ATTR_INDEX.equals(nodeName)) {
                        if ("name".equals(nodeName)) {
                            item2.mName = nodeValue;
                        }
                    } else {
                        item2.mLevel = Integer.parseInt(nodeValue);
                    }
                }
                item2.mTagName = element2.getTagName();
                parseSubItems(element2, item2);
                onItemLoaded(item2);
            }
        }
    }
}
