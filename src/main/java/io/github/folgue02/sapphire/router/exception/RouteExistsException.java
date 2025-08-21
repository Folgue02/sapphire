package io.github.folgue02.sapphire.router.exception;

import io.github.folgue02.sapphire.exception.SapphireException;
import io.github.folgue02.sapphire.router.RouteSpecification;
import io.github.folgue02.sapphire.filter.FilterSpecification;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RouteExistsException extends SapphireException {
	private RouteExistsException(String message) {
		super(message);
	}

	public RouteExistsException(RouteSpecification rSpec) {
		super("A route specification with the %s specification already exists, therefore, it cannot be registered.".formatted(rSpec));
	}

	public static RouteExistsException ofConflictedRouteSpecs(List<RouteSpecification> conflictingRSpecs) {
		return new RouteExistsException("An attempt to merge routes was done, but failed due to the following conflicting routeSpecification specifications: \n" + conflictingRSpecs.stream().map(Object::toString).collect(Collectors.joining("\n- ")));
	}

	public RouteExistsException(FilterSpecification fSpec) {
		super("A filter specification with the %s specification already exists, therefore, it cannot be registered.".formatted(fSpec));
	}

	public static RouteExistsException ofConflictedFilterSpecs(List<FilterSpecification> conflictingFSpecs) {
		return new RouteExistsException("An attempt to merge filters was done, but failed due to the following conflicting filter specifications: \n" + conflictingFSpecs.stream().map(Object::toString).collect(Collectors.joining("\n- ")));
	}
}
  