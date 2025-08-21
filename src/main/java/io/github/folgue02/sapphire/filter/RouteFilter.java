package io.github.folgue02.sapphire.filter;

import java.util.Optional;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.router.handler.BaseRouteHandler;


public interface RouteFilter {
	/// Determines if the request is passed on, or filtered out.
	///
	/// @param request HTTP request of the client.
	/// @return If the request is filtered out, [Optional#of(T)] is returned with the
	/// handler to be executed by the server, but if the request passes the filter, 
	/// [Optional#empty()] should be returned.
	Optional<BaseRouteHandler> filter(HttpRequest request) throws Exception;
}