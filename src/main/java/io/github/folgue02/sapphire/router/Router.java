package io.github.folgue02.sapphire.router;

import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.router.exception.RouteExistsException;
import io.github.folgue02.sapphire.router.functional.RouteConfigurator;
import io.github.folgue02.sapphire.router.handler.RouteHandler;
import io.github.folgue02.sapphire.filter.FilterSpecification;
import io.github.folgue02.sapphire.filter.RouteFilter;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class Router {
	public final String prefix;
	public final Map<RouteSpecification, RouteHandler> routes;
	public final Set<Pair<FilterSpecification, RouteFilter>> filters;

	public Router() {
		this.routes = new HashMap<>();
		this.filters = new HashSet<>();
		this.prefix = null;
	}

	private Router(String prefix) {
		this.prefix = prefix;
		this.routes = new HashMap<>();
		this.filters = new HashSet<>();
	}

	/// Creates an instance of [Router] which will prepend the given prefix to all the routes.
	public static Router withPrefix(String prefix) {
		return new Router(prefix);
	}

	/// Adds a route to the router.
	///
	/// @param rSpec Route specification for the handler.
	/// @param rHandler Handler which will be executed when a request matches its spe
	///                 specification.
	/// @throws RouteExistsException If a route with the same HTTP method and route already exists.
	/// @return The instance of the router.
	public Router addRoute(RouteSpecification rSpec, RouteHandler<?> rHandler) throws RouteExistsException {
		rSpec = this.prefix != null ? rSpec.cloneWithPrefix(this.prefix) : rSpec;
		if (this.routes.containsKey(rSpec))
			throw new RouteExistsException(rSpec);

		this.routes.put(rSpec, rHandler);
		return this;
	}

	public Router addFilter(FilterSpecification fSpec, RouteFilter filter) {
		fSpec = this.prefix != null ? fSpec.cloneWithPrefix(this.prefix) : fSpec;

		this.filters.add(Pair.of(fSpec, filter));
		return this;
	}

	/// Adds a route to the router.
	///
	/// @param method Method of the request that the handler will handle.
	/// @param route Route for the handler.
	/// @param rHandler Handler for the route.
	/// @throws RouteExistsException If a route with the same HTTP method and route already exists.
	/// @return The instance of the router.
	public Router addRoute(HttpMethod method, String route, RouteHandler<?> rHandler) throws RouteExistsException {
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
			throw RouteExistsException.ofConflictedRouteSpecs(conflictingRSpecs);

		for (var kv : toMerge.routes.entrySet())
			this.addRoute(kv.getKey(), kv.getValue());

		for (var pair : toMerge.filters)
			this.addFilter(pair.getLeft(), pair.getRight());

		return this;
	}

	public Router withPrefix(String prefix, RouteConfigurator rConfigurator) throws RouteExistsException {
		Router rWithPrefix = Router.withPrefix(prefix);
		rConfigurator.configure(rWithPrefix);
		this.mergeRouter(rWithPrefix);
		return this;
	}

	/// Finds the matched route for the given request.
	/// 
	/// @param request Request object used to determine which route is appropiate.
	/// @return The matched route, or in case there is no match, [Optional#empty()] is returned.
	public Optional<Map.Entry<RouteSpecification, RouteHandler>> findMatchedRoute(HttpRequest request) {
		return this.routes.entrySet().stream()
				.filter(kv -> {
					final var routeSpecification = kv.getKey();
					final var rMethod = routeSpecification.method();

					return request.method == rMethod && routeSpecification.matchesPath(request.requestUri.getPath());
				})
				.sorted()
				.findFirst();
	}

	/// Returns all the filters which match the given request, in order of priority
	/// *(highest priority first)*.
	///
	/// @param request Request object to be used to find matches.
	/// @return Sorted list *(high priority first)* of the filters which match the
	/// request.
	public List<RouteFilter> findMatchedFilters(HttpRequest request) {
		return this.filters.stream()
			.filter(pair -> pair.getLeft().matchesRequest(request))
			.sorted(Comparator.comparing(Pair::getKey))
			.map(Map.Entry::getValue)
			.toList();
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
