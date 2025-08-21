package io.github.folgue02.sapphire.exchange;

import io.github.folgue02.sapphire.consts.HeaderConsts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// Contains the data to be passed back to the client
/// in response to its request.
public final class HttpResponse {
	public StatusCode status;
	public Map<String, List<String>> headers;
	public byte[] body;

	public HttpResponse() {
		this.status = StatusCode.OK;
		this.headers = new HashMap<>();
	}

	public void setBody(String body) {
		this.body = body.getBytes();
	}

	/// Adds the given value to the current value of the specified header. If it doesn't
	/// exist, a list of a single value *(the one provided)* is put.
	/// 
	/// @param headerName Name of the header
	/// @param headerValue Value of the header
	public void addToHeader(String headerName, String headerValue) {
		var values = Objects.requireNonNullElse(this.headers.get(headerName), new ArrayList<String>());
		values.add(headerValue);
		this.headers.put(headerName, values);
	}

	/// Sets the value of the specified header to a list of a single element *(the one provided)*,
	/// replacing any other element.
	///
	/// @param headerName Name of the header to replace/set.
	/// @param headerValue Value of the header to set.
	public void putHeader(String headerName, String headerValue) {
		var values = new ArrayList<String>();
		values.add(headerValue);
		this.headers.put(headerName, values);
	}

	public void setCalculatedBodyLength() {
		this.putHeader(HeaderConsts.CONTENT_LENGTH, Integer.toString(this.body.length));
	}
}
