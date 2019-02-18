package com.sean.init.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtils {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
    private static final ObjectMapper objectMapper;
    private static Gson gson;

    public JSONUtils() {
    }

    public static <T> T readValue(String jsonContent, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonContent, clazz);
        } catch (IOException var3) {
            logger.error("Fail to convert json[{}] to bean[{}]", new Object[]{jsonContent, clazz, var3});
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValueForJsonObject(String jsonContent, Class<T> clazz) {
        try {
            return gson.fromJson(jsonContent, clazz);
        } catch (JsonSyntaxException var3) {
            logger.error("Fail to convert json[{}] to bean[{}]", new Object[]{jsonContent, clazz, var3});
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValue(String jsonContent, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonContent, typeReference);
        } catch (IOException var3) {
            logger.error("Fail to convert json[{}] to bean[{}]", new Object[]{jsonContent, typeReference, var3});
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValueFromRequest(String jsonContent, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonContent, typeReference);
        } catch (IOException var3) {
            logger.error("Fail to convert json[{}] to bean[{}]", new Object[]{jsonContent, typeReference, var3});
            throw new IllegalArgumentException("Fail to parse json str");
        }
    }

    public static String writeValue(Object obj) {
        if(obj == null) {
            return "";
        } else {
            try {
                return objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException var2) {
                logger.error("failed to process json obj", var2);
                return "";
            }
        }
    }

    public static String writeValueWithClass(Class c, Object obj) {
        if(obj == null) {
            return "";
        } else {
            try {
                return objectMapper.writerWithView(c).writeValueAsString(obj);
            } catch (JsonProcessingException var3) {
                logger.error("failed to process json obj", var3);
                return "";
            }
        }
    }

    public static String writePrettyValue(Object obj) {
        if(obj == null) {
            return "";
        } else {
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (JsonProcessingException var2) {
                logger.error("Failed to parse object to json str", var2);
                return "";
            }
        }
    }

    public static String toString(Object obj) {
        Preconditions.checkNotNull(obj, "Require non-null param \'obj\'");

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            logger.error("Failed to parse object to json str", var2);
            return "";
        }
    }

    public static String toStringForJsonObject(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (JsonSyntaxException var2) {
            logger.error("Failed to parse object to json str", var2);
            return "";
        }
    }

    public static Map<String, String> readStringMap(String content) {
        return (Map)gson.fromJson(content, (new TypeToken() {
        }).getType());
    }

    public static Map<Object, Object> readObjectMap(String content) {
        return (Map)gson.fromJson(content, Map.class);
    }

    public static <T> T convertMap2Obj(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    public static <T> List<T> convertArrayStr2ObjList(String jsonArrayStr, Class<T> clazz) throws IOException {
        List list = (List)objectMapper.readValue(jsonArrayStr, new TypeReference() {
        });
        ArrayList result = new ArrayList();
        Iterator var4 = list.iterator();

        while(var4.hasNext()) {
            Map map = (Map)var4.next();
            result.add(convertMap2Obj(map, clazz));
        }

        return result;
    }

    public static String toJsonStr(Object obj) {
        if(obj == null) {
            return null;
        } else {
            try {
                return gson.toJson(obj);
            } catch (JsonSyntaxException var2) {
                logger.error("Failed to convert object to json str", var2);
                return null;
            }
        }
    }

    static {
        objectMapper = (new ObjectMapper()).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getDefault());
        gson = (new GsonBuilder()).disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}