package io.github.folgue02.sapphire.exchange;

/// Represents the different HTTP methods/verbs of HTTP 
/// requests.
public enum HttpMethod {
	GET,
	POST,
	PUT,
	PATCH,
	DELETE,
	UNKNOWN;

	public static HttpMethod ofString(String methodString) {
		return switch (methodString.toUpperCase()) {
			case "GET" -> GET;
			case "POST" -> POST;
			case "PUT" -> PUT;
			case "PATCH" -> PATCH;
			case "DELETE" -> DELETE;
			default -> UNKNOWN;
		};
	}
}
