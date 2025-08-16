package io.github.folgue02.sapphire.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestRouteUtils {
    @Test
    public void testExtractPathVariables_oneElement() {
        final var pSpec = "/profile/:id/edit";
        final var path = "/profile/32/edit";
        final var expected = Map.of("id", "32");

        assertEquals(expected, RouteUtils.extractPathVariables(pSpec, path));
    }

    @Test
    public void testExtractPathVariables_multipleElements() {
        final var pSpec = "/profile/:id/edit/:action";
        final var path = "/profile/32/edit/update";
        final var expected = Map.of("id", "32", "action", "update");

        assertEquals(expected, RouteUtils.extractPathVariables(pSpec, path));
    }

    @Test
    public void testParseQueryAttributes_empty() {
        final var input = "/some/route";
        final var expected = new HashMap<String, String>();

        assertEquals(expected, RouteUtils.parseQueryAttributes(input));
    }

    @Test
    public void testParseQueryAttributes_oneElement() {
        final var input = "/some/route?name=user";
        final var expected = Map.of("name", "user");

        assertEquals(expected, RouteUtils.parseQueryAttributes(input));
    }

    @Test
    public void testParseQueryAttributes_multipleElements() {
        final var input = "/some/route?name=user&surname=usurname";
        final var expected = Map.of("name", "user", "surname", "usurname");

        assertEquals(expected, RouteUtils.parseQueryAttributes(input));
    }

    @Test
    public void testParseQueryAttributes_multipleElementsEncoded() {
        final var input = "/some/route?name=user&surname=usur%20name";
        final var expected = Map.of("name", "user", "surname", "usur name");

        assertEquals(expected, RouteUtils.parseQueryAttributes(input));
    }
}
