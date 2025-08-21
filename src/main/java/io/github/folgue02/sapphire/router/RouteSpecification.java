package io.github.folgue02.sapphire.router;

import io.github.folgue02.sapphire.exchange.HttpMethod;
import io.github.folgue02.sapphire.utils.RouteUtils;
import org.apache.commons.lang3.StringUtils;

public record RouteSpecification(HttpMethod method, String routeSpecification) {

	///  Creates a cloned instance prepending the given prefix on the routeSpecification specification.
	public RouteSpecification cloneWithPrefix(String prefix) {
		return new RouteSpecification(this.method, RouteUtils.joinRoutes(prefix, this.routeSpecification));
	}

	/// Checks if the given path of the request matches with the routeSpecification
	/// specification.
	///
	/// *NOTE:* This method supports routeSpecification specifications such as
	/// `/users/:user/profile`, which would match with `/users/someone/profile`.
	///
	/// @param requestPath Path requested by the client
	/// @return `true` if the path matches, or `false` if it doesn't.
	public boolean matchesPath(final String requestPath) {
		final String[] rsSegments = StringUtils.splitByWholeSeparator(this.routeSpecification, "/");
		final String[] rpSegments = StringUtils.splitByWholeSeparator(requestPath, "/");

		if (rsSegments.length != rpSegments.length)
			return false;

		for (int i = 0; i < rsSegments.length; i++) {
			final var rsSegment = rsSegments[i];
			final var rpSegment = rpSegments[i];

			if (!rsSegment.equals(rpSegment)) {
				return false;
			}
		}

		return true;
	}
}
