package io.github.folgue02.sapphire.exchange;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    UNKNOWN;

    public static HttpMethod ofString(String methodString) {
        return switch (methodString) {
            case "GET" -> GET;
            case "POST" -> POST;
            case "PUT" -> PUT;
            case "PATCH" -> PATCH;
            case "DELETE" -> DELETE;
            default -> UNKNOWN;
        };
    }
}
