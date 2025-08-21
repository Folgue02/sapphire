package io.github.folgue02.sapphire.exchange;

import org.apache.commons.lang3.StringUtils;

public enum StatusCode {
	OK(200),

	PERMANENT_REDIRECT(301),

	NOT_FOUND(404),
	FORBIDDEN(403),

	INTERNAL_SERVER_ERROR(500);

	public final int code;

	StatusCode(int code) {
		this.code = code;
	}

	public String getReasonPhrase() {
		return StringUtils.capitalize(this.toString().toLowerCase().replace("_", " "));
	}
}
