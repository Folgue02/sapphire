package io.github.folgue02.sapphire.exchange;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class HttpRequest {
	public HttpMethod method;
	public String httpProtocol;
	public URI requestUri;
	public Map<String, List<String>> headers;
	public Map<String, String> pathParams;
	public Map<String, String> pathQueryAttributes;
	public Map<String, Object> attributes;
	public byte[] body;

	public String getBodyString() {
		return new String(this.body);
	}

	@Override
	public String toString() {
		// NOTE: This is not supposed to be a 1:1 representation of the
		// contents of a request, this is just for debugging purposes.
		var sb = new StringBuilder();

		sb.append("%s %s %s\n".formatted(this.method, this.requestUri.getPath(), this.httpProtocol));
		this.headers.forEach((header, value) -> sb.append("%s: %s\n".formatted(header, value)));
		sb.append(body);

		return sb.toString();		
	}
}

