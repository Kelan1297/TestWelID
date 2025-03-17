//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.testmicro.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Configurazione {
    INSTANCE;

    private Map<String, Object> properties;

    private Configurazione() {
    }

    public void setProperties(Map<String, Object> prop) {
        INSTANCE.properties = prop;
    }

    public void setProperty(String key, Object oggetto) {
        INSTANCE.properties.put(key, oggetto);
    }

    public Object getProperty(String key) {
        return INSTANCE.properties.get(key);
    }

    public String getString(String key) {
        return this.getString(key, "");
    }

    public String getString(String key, String def) {
        return this.getEnvOr(key, def);
    }

    public int getInt(String key) {
        return Integer.valueOf(this.getString(key).trim());
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.valueOf(this.getString(key).trim());
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        return Long.valueOf(this.getString(key).trim());
    }

    public long getLong(String key, long defaultValue) {
        try {
            return Long.valueOf(this.getString(key).trim());
        } catch (NumberFormatException var5) {
            return defaultValue;
        }
    }

    public Short getShort(String key) {
        return Short.valueOf(this.getString(key).trim());
    }

    public Byte getByte(String key) {
        return Byte.valueOf(this.getString(key).trim());
    }

    public Byte getByte(String key, byte defValue) {
        return INSTANCE.properties.get(key) == null ? defValue : Byte.valueOf(this.getString(key).trim());
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(this.getString(key).trim());
    }

    public boolean getBoolean(String key, boolean defValue) {
        return INSTANCE.properties.get(key) == null ? defValue : Boolean.valueOf(this.getString(key).trim());
    }

    public Map<String, Object> getAllProperties() {
        return INSTANCE.properties;
    }

    public void removeAttr(String key) {
        this.properties.remove(key);
    }

    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }

    public String getEnvOr(String key, String def) {
        try {
            String possibleValue = "";
            if (INSTANCE.getProperty(key) != null) {
                possibleValue = String.valueOf(INSTANCE.getProperty(key));
            } else {
                possibleValue = def;
            }

            if (possibleValue.contains("${") && possibleValue.contains("}")) {
                String envKey = possibleValue.split(":")[0].replace("${", "");
                envKey = envKey.replace("}", "");
                String envIpVar = System.getenv(envKey);
                if (envIpVar != null && !"".equals(envIpVar)) {
                    return envIpVar;
                } else {
                    String value = "";

                    try {
                        value = possibleValue.split(":")[1].replace("}", "");
                    } catch (Exception var8) {
                        value = possibleValue.replace("}", "");
                    }

                    return value != null && !"".equals(value) ? value : def;
                }
            } else {
                return possibleValue;
            }
        } catch (Exception var9) {
            return def;
        }
    }

    public void init(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            Enumeration enuKeys = properties.keys();

            while(enuKeys.hasMoreElements()) {
                String key = (String)enuKeys.nextElement();
                String value = properties.getProperty(key);
                this.properties.put(key, value);
            }
        } catch (FileNotFoundException var8) {
            var8.printStackTrace();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int count = 0;
        String regex = "[$\\{:\\}]";
        String string = "${MYSQL_HOST:localhost}";
        Pattern pattern = Pattern.compile("[$\\{:\\}]", 8);

        for(Matcher matcher = pattern.matcher("${MYSQL_HOST:localhost}"); matcher.find(); ++count) {
        }

        System.out.println(count);
    }

    static {
        INSTANCE.properties = new HashMap();
    }
}
