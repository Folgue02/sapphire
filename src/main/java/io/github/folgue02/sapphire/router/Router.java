package io.github.folgue02.sapphire.router;

import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.router.exception.RouteExistsException;
import io.github.folgue02.sapphire.router.functional.RouteConfigurator;
import io.github.folgue02.sapphire.router.handler.BaseRouteHandler;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Router {
	private final String prefix;
	private final Map<RouteSpecification, BaseRouteHandler> routes;

	public Router() {
		this.routes = new HashMap<>();
		this.prefix = null;
	}

	private Router(String prefix) {
		this.routes = new HashMap<>();
		this.prefix = prefix;
	}

	/// Creates an instance of [Router] which will prepend the given prefix to all the given routes.
	public static Router withPrefix(String prefix) {
		return new Router(prefix);
	}

	/// Adds a route to the router.
	///
	/// @param rSpec Route specification for the
	/// @param rHandler Handler for the route.
	/// @throws RouteExistsException If a route with the same HTTP method and route already exists.
	/// @return The instance of the router.
	public Router addRoute(RouteSpecification rSpec, BaseRouteHandler rHandler) throws RouteExistsException {
		rSpec = this.prefix != null ? rSpec.cloneWithPrefix(this.prefix) : rSpec;
		if (this.routes.containsKey(rSpec))
			throw new RouteExistsException(rSpec);

		this.routes.put(rSpec, rHandler);
		return this;
	}

	/// Adds a route to the router.
	///
	/// @param method Method of the request that the handler will handle.
	/// @param route Route for the handler.
	/// @param rHandler Handler for the route.
	/// @throws RouteExistsException If a route with the same HTTP method and route already exists.
	/// @return The instance of the router.
	public Router addRoute(HttpMethod method, String route, BaseRouteHandler<?> rHandler) throws RouteExistsException {
		return this.addRoute(new RouteSpecification(method, route), rHandler);
	}

	/// Merges this router to the provided one.
	///
	/// @param toMerge Router containing the routes to insert in the current router.
	/// @return The instance of the router.
	/// @throws RouteExistsException If one of the routes in the given router already exists.
	public Router mergeRouter(Router toMerge) throws RouteExistsException {
		List<RouteSpecification> conflictingRSpecs = toMerge.routes.keySet().stream()
				.filter(this.routes::containsKey)
				.toList();

		if (!conflictingRSpecs.isEmpty())
			throw new RouteExistsException(conflictingRSpecs);

		for (var kv : toMerge.routes.entrySet())
			this.addRoute(kv.getKey(), kv.getValue());

		return this;
	}

	public Router withPrefix(String prefix, RouteConfigurator rConfigurator) throws RouteExistsException {
		Router rWithPrefix = Router.withPrefix(prefix);
		rConfigurator.configure(rWithPrefix);
		this.mergeRouter(rWithPrefix);
		return this;
	}

	public Optional<Map.Entry<RouteSpecification, BaseRouteHandler>> findMatchedRoute(HttpRequest request) {
		return this.routes.entrySet().stream()
				.filter(kv -> {
					final var routeSpecification = kv.getKey();
					final var rMethod = routeSpecification.method();

					return request.getMethod() == rMethod && routeSpecification.matchesPath(request.getRequestUri().getPath());
				})
				.findFirst();
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append("ROUTE COUNT: ").append(this.routes.size()).append('\n');
		sb.append("-".repeat(20));
		sb.append("\n");
		String routeList = this.routes.entrySet().stream()
				.map(kv -> "- %s %s -> %s".formatted(kv.getKey().method(), kv.getKey().routeSpecification(), kv.getValue().getHandlerDescription()))
				.sorted()
				.collect(Collectors.joining("\n"));

		sb.append(routeList);
		sb.append("\n");
		sb.append("-".repeat(20));
		sb.append("\n");
		sb.append("ROUTE COUNT: ").append(this.routes.size()).append('\n');

		return sb.toString();
	}
}
