package io.github.folgue02.sapphire.filter;

import java.util.Set;

import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.utils.RouteUtils;

public record FilterSpecification(String routePattern, Set<HttpMethod> expectedMethods, FilterPriority priority) 
		implements Comparable<FilterSpecification> {
	/// Calls the constructor passing the given route pattern to cover, and 
	/// using default values for the expected methods to cover *(an empty set, 
	/// meaning that it will cover all requests no matter the method)* and for the
	/// filter priority *([FilterPriority#DEFAULT])*.
	/// 
	/// @return The created filter specification.
	public static FilterSpecification createWithDefaults(String routePattern) {
		return new FilterSpecification(routePattern, Set.of(), FilterPriority.DEFAULT);
	}

	/// Calls the constructor passing the given route pattern to cover and the priority for the
	/// filter and leaving the expected methods empty *(meaning, it covers all request no matter the
	/// request method)*.
	/// 
	/// @return The created filter specification.
	public static FilterSpecification createWithPriority(String routePattern, FilterPriority priority) {
		return new FilterSpecification(routePattern, Set.of(), priority);
	}

	/// @return A clone of the instance, with the prepended prefix to the route pattern.
	public FilterSpecification cloneWithPrefix(String prefix) {
		return new FilterSpecification(RouteUtils.joinRoutes(prefix, this.routePattern), expectedMethods, this.priority);
	}


	/// @return `true` if there is no HTTP method specified, meaning that all
	/// request, no matter the method, should be filtered.
	public boolean filtersAllMethods() {
		return this.expectedMethods.isEmpty();
	}

	/// Checks if the request must be passed to the associated filter or not.
	/// This includes, both matching the path as well as the request method to the specified
	/// criteria.
	///
	/// @return `true` if the request should be passed to the filter, `false` otherwise.
	public boolean matchesRequest(HttpRequest request) {
		return RouteUtils.routeMatchesGlob(this.routePattern, request.requestUri.getPath());
	}

	@Override
	public int compareTo(FilterSpecification other) {
		return this.priority.compareTo(other.priority());
	}
}