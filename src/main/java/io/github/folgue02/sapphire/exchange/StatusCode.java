package io.github.folgue02.sapphire.exchange;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum StatusCode {
	OK(200),

	PERMANENT_REDIRECT(301),

	NOT_FOUND(404),
	FORBIDDEN(403),

	INTERNAL_SERVER_ERROR(500);

	private final int statusCode;

	StatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return StringUtils.capitalize(this.toString().toLowerCase().replace("_", " "));
	}
}
