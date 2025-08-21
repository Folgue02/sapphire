package io.github.folgue02.sapphire.exchange;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class HttpRequest {
	public HttpMethod method;
	public URI requestUri;
	public Map<String, List<String>> headers;
	public Map<String, String> pathParams;
	public Map<String, String> pathQueryAttributes;
	public Map<String, Object> attributes;
	public byte[] body;

	public String getBodyString() {
		return new String(this.body);
	}
}
