package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exception.SapphireException;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.exchange.HttpResponse;
import io.github.folgue02.sapphire.server.exception.HandlerExecutionException;

public abstract class BaseRouteHandler<T> {
	/// Handles the request, and returns the expected result.
	public abstract T handleRequest(HttpRequest request, HttpResponse response) throws Exception;

	/// Processes the result from the handler, and returns the response which will be given to the user.
	public abstract HttpResponse processResult(HttpResponse response, T result) throws Exception;

	public HttpResponse runHandler(HttpRequest request) throws SapphireException {
		var response = new HttpResponse();

		T result;
		try {
			result = this.handleRequest(request, response);
		} catch (Exception e) {
			throw new HandlerExecutionException(this.getClass(), "Execution of handle() failed.", e);
		}

		try {
			return this.processResult(response, result);
		} catch (Exception e) {
			throw new HandlerExecutionException(this.getClass(), "Couldn't process the result of the handle().", e);
		}
	}

	/// Provides a description of the handler, which might be used in things
	/// like listing the routes.
	public abstract String getHandlerDescription();
}
