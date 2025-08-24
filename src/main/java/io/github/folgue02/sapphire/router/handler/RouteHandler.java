package io.github.folgue02.sapphire.router.handler;

import io.github.folgue02.sapphire.exchange.*;
import io.github.folgue02.sapphire.router.Router;

/// Definition of a route handler class to be inherited from.
/// 
/// A route handler takes a request and returns a response, performing all
/// required logic in the middle of the process *(processing input, inserting records,
/// generating the output, etc...)*.
/// 
/// Route handlers are supposed to be registered in a [Router] object, being paired
/// with a [io.github.folgue02.sapphire.router.RouteSpecification].
public abstract class RouteHandler {
    public abstract HttpResponse runHandler(HttpRequest request, HttpResponse response) throws Exception;
	public abstract String getHandlerDescription();
}
