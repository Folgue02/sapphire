package io.github.folgue02.sapphire.filter;

import java.util.Optional;

import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.router.handler.RouteHandler;

/// Route filters are units of logic which are executed before route handlers are.
///
/// Filters don't only function as some sort of middleware, they can also cut the chain of execution
/// and force the execution of their own, chosen route handler *(preventing the server from 
/// passing the request to the rest of handlers and filters, also known as the chain)*.
///
/// A good example of this would be access filters *(i.e. checking if the user's token 
/// corresponds to an account in the database, if it is, pass it to the next element in the chain,
/// if not, force the execution of a handler with the login form)*.
public interface RouteFilter {
	/// Determines if the request is passed on, or filtered out.
	///
	/// @param request HTTP request of the client.
	/// @return If the request is filtered out, [Optional#of(T)] is returned with the
	/// handler to be executed by the server, but if the request passes the filter, 
	/// [Optional#empty()] should be returned.
	Optional<RouteHandler> filter(HttpRequest request) throws Exception;
}