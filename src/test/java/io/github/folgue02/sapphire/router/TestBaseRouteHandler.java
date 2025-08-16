package io.github.folgue02.sapphire.router;

import static org.junit.jupiter.api.Assertions.*;

import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.router.exception.RouteExistsException;
import io.github.folgue02.sapphire.router.handler.DumbHandler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

public class TestBaseRouteHandler {
    @Test
    public void testRouteWithPrefix() throws RouteExistsException {
        var expectedRoutes = Stream.of("/accounts/profile", "/accounts/logout", "/homepage").sorted().toList();
        var router = new Router();
        router.addRoute(HttpMethod.GET, "/homepage", new DumbHandler())
                .withPrefix("/accounts", accountRouter -> {
                    accountRouter.addRoute(HttpMethod.GET, "profile", new DumbHandler());
                    accountRouter.addRoute(HttpMethod.GET, "logout", new DumbHandler());
                });
        List<String> routes = router.getRoutes().keySet().stream()
                .map(RouteSpecification::routeSpecification)
                .sorted()
                .toList();

        assertEquals(expectedRoutes, routes);
    }
}
