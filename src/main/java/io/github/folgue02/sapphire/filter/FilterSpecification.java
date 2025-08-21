package io.github.folgue02.sapphire.filter;

import java.util.Set;

import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.utils.RouteUtils;

/// A filter specification defines what request should be passed throught the filter.
///
/// Components of a filter:
///
/// - `routePattern`, the pattern used to check if the request should pass through the filter
///   if the request's path matches.
/// - `expectedMethods`, specify which kind of request should be passed through the filters, *(i.e.
///   GET, POST...)*, if this is empty, the filter should catch all request *(as long as it matches
///   the rest of criteria)*.
/// - `priority`, sapphire's way to determine which filter should be executed first is using priority, higher priority
///   filters are executed first *(considering that [FilterPriority#EXTREME] is higher than [FilterPriority#HIGH])*.
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