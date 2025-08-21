package io.github.folgue02.sapphire.router;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.github.folgue02.sapphire.router.Router;
import io.github.folgue02.sapphire.router.handler.RouteHandler;
import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.exchange.HttpRequest;
import io.github.folgue02.sapphire.filter.FilterPriority;
import io.github.folgue02.sapphire.filter.FilterSpecification;
import io.github.folgue02.sapphire.filter.RouteFilter;


public class TestRouter {
	static class RouteFilterLowPrio implements RouteFilter {
		@Override
		public Optional<RouteHandler> filter(HttpRequest request) throws Exception {
		    return Optional.empty();
		}
	}

	static class RouteFilterHighPrio implements RouteFilter {
		@Override
		public Optional<RouteHandler> filter(HttpRequest request) throws Exception {
		    return Optional.empty();
		}
	}

	static class RouteFilterDefaultPrio implements RouteFilter {
		@Override
		public Optional<RouteHandler> filter(HttpRequest request) throws Exception {
		    return Optional.empty();
		}
	}

	static class RouteFilterExtremePrio implements RouteFilter {
		@Override
		public Optional<RouteHandler> filter(HttpRequest request) throws Exception {
		    return Optional.empty();
		}
	}

	@Test
	public void testPriorities() throws Exception {
		var filterLow = new RouteFilterLowPrio();
		var filterDefault = new RouteFilterDefaultPrio();
		var filterHigh = new RouteFilterHighPrio();
		var filterExtreme = new RouteFilterHighPrio();

		var router = new Router();

		var extremeSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.EXTREME);
		var highSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.HIGH);
		var defaultSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.DEFAULT);
		var lowSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.LOW);
				
		var expectedFSpecs = List.of(filterExtreme, filterHigh, filterDefault, filterLow);

		Map.of(
			highSpec, filterHigh,
			defaultSpec, filterDefault,
			extremeSpec, filterExtreme,
			lowSpec, filterLow
		).forEach((spec, filter) -> router.addFilter(spec, filter));

		var mockedRequest = new HttpRequest();
		mockedRequest.requestUri = URI.create("https://localhost:8080/same");
		mockedRequest.method = HttpMethod.GET;

		assertEquals(expectedFSpecs, router.findMatchedFilters(mockedRequest));		
	}
}