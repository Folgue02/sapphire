package io.github.folgue02.sapphire.router.exception;

import io.github.folgue02.sapphire.exception.SapphireException;
import io.github.folgue02.sapphire.router.RouteSpecification;

import java.util.Collection;
import java.util.stream.Collectors;

public class RouteExistsException extends SapphireException {
	public RouteExistsException(RouteSpecification rSpec) {
		super("A routeSpecification with the %s specification already exists, therefore, it cannot be added to the router.".formatted(rSpec));
	}

	public RouteExistsException(Collection<RouteSpecification> conflictingRSpecs) {
		super("An attempt to merge routes was done, but failed due to the following conflicting routeSpecification specifications: \n" + conflictingRSpecs.stream().map(Object::toString).collect(Collectors.joining("\n- ")));
	}
}
