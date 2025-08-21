package io.github.folgue02.sapphire.exchange;

import io.github.folgue02.sapphire.consts.HeaderConsts;

import java.util.HashMap;
import java.util.Map;

/// Contains the data to be passed back to the client
/// in response to its request.
public final class HttpResponse {
	public StatusCode status;
	public Map<String, String> headers;
	public byte[] body;

	public HttpResponse() {
		this.status = StatusCode.OK;
		this.headers = new HashMap<>();
	}

	public void setBody(String body) {
		this.body = body.getBytes();
	}

	public void putHeader(String headerName, String headerValue) {
		this.headers.put(headerName, headerValue);
	}

	public void setCalculatedBodyLength() {
		this.headers.put(HeaderConsts.CONTENT_LENGTH, Integer.toString(this.body.length));
	}
}
