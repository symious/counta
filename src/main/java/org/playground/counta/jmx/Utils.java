package org.playground.counta.jmx;

import org.apache.commons.lang.StringUtils;

public class Utils {
    public static String getPrefix(String nameString) {
        try {
            String[] nameSplit = nameString.split(",");

            String type = null;
            String name = null;
            String component = null;

            for (String namePart : nameSplit) {
                String[] splitKV = namePart.split("=");
                String key = splitKV[0];
                if (key.endsWith("type")) {
                    type = splitKV.length < 2 ? null : splitKV[1];
                } else if (key.endsWith("name")) {
                    StringBuilder nameBuilder = new StringBuilder();
                    for (int i = 1; i < splitKV.length; i++) {
                        if (i != 1) {
                            nameBuilder.append("_");
                        }
                        if (i == 2) {
                            nameBuilder.append(splitKV[i].replace(" ", "_"));
                        } else {
                            nameBuilder.append(splitKV[i]);
                        }
                    }
                    name = nameBuilder.toString();
                } else if (key.endsWith("component")) {
                    component = splitKV.length < 2 ? null : splitKV[1];
                }
            }
            //如果type为数字 则不进行处理
            if (StringUtils.isNumeric(type)) {
                return null;
            }

            //按照type name component 顺序拼接
            StringBuilder sb = new StringBuilder();
            sb.append(type == null ? "" : type);
            if (name != null && type != null) {
                sb.append("_");
            }
            //去掉空格，点. 冒号: 引号" 转换为下划线_
            sb.append(name == null ? "" : name.replace(" ", "")
                    .replace('.', '_').replace(":", "_")
                    .replace("\"", ""));
            if (component != null && name != null) {
                sb.append("_");
            }
            sb.append(component == null ? "" : component);
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
