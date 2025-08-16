package io.github.folgue02.sapphire.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RouteUtils {
    public static String joinRoutes(String first, String second) {
        if (first.endsWith("/") && second.startsWith("/"))
            return first.substring(0, first.length() - 1) + second;

        if (!first.endsWith("/") && !second.startsWith("/"))
            return first + "/" + second;

        return first + second;
    }

    public static Map<String, String> parseQueryAttributes(String rawQuery) {
        Map<String, String> queryParams = new HashMap<>();
        if (StringUtils.isBlank(rawQuery) || !rawQuery.contains("?"))
            return queryParams;

        final var queryPath = StringUtils.splitByWholeSeparator(rawQuery, "?")[1];

        final String[] pairs = queryPath.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            String key = HttpUtils.decodeHttpComponent(kv[0]);
            String value = kv.length > 1 ? HttpUtils.decodeHttpComponent(kv[1]) : "";
            queryParams.put(key, value);
        }

        return queryParams;
    }

    public static Map<String, String> extractPathVariables(String pSpec, String p) {
        final String[] specSegments = pSpec.split("/");
        final String[] pSegments = p.split("/");
        final Map<String, String> result = new HashMap<>();
        final Map<String, Integer> positions = new HashMap<>();

        for (int i = 0; i < specSegments.length; i++) {
            String segment = specSegments[i];

            if (segment.startsWith(":"))
                positions.put(segment.substring(1), i);
        }

        try {
            for (var kv : positions.entrySet())
                result.put(kv.getKey(), pSegments[kv.getValue()]);

            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Cannot extract path variables from '%s', because it doesn't correspond to the given path specification; '%s'".formatted(p, pSpec));
        }
    }
}
