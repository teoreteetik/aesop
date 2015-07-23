package ee.lis.util;

import com.google.gson.Gson;

public class JsonUtil {

    private static Gson gson = new Gson();

    public static String asJson(Object obj) {
        return gson.toJson(obj);
    }

    public  static <T> T fromJson(String string, Class<T> clazz) {
        return gson.fromJson(string, clazz);
    }
}