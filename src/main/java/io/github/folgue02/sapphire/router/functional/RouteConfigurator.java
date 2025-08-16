package io.github.folgue02.sapphire.router.functional;

import io.github.folgue02.sapphire.router.Router;
import io.github.folgue02.sapphire.router.exception.RouteExistsException;

@FunctionalInterface
public interface RouteConfigurator {
    void configure(Router route) throws RouteExistsException;
}
