package io.github.folgue02.sapphire.router.handler.impl;

import io.github.folgue02.sapphire.SapphireConsts;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.router.handler.RawStringHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ErrorHandler extends RawStringHandler {
	private final String errorMessage;
	private final String errorCause;

	public ErrorHandler(String message) {
		this.errorMessage = message;
		this.errorCause = null;
	}

	public ErrorHandler(String message, String errorCause) {
		this.errorMessage = message;
		this.errorCause = errorCause;
	}

	@Override
	public String processInput(HttpRequest request) throws Exception {
		String errorCauseMsg = "<p class=\"error-cause\">" + Objects.requireNonNullElse(this.errorCause, StringUtils.EMPTY) + "</p>";
		return """
				<style>
					p.server-version {
						background-color: blue;
						color: white;
						top-padding: 5px;
						bottom-padding: 5px;
					}
					
					p.error-cause {
						font-size: 10px;
						font-style: italic;
					}
				</style>
				<h1>%s</h1>
				%s
				<hr>
				<p class="server-version">Sapphire v%s</p>
				""".formatted(this.errorMessage, errorCauseMsg, SapphireConsts.VERSION);
	}
}
