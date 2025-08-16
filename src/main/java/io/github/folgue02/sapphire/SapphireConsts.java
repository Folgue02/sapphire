package io.github.folgue02.sapphire;

import io.github.folgue02.sapphire.exchange.StatusCode;
import io.github.folgue02.sapphire.router.handler.BaseRouteHandler;
import io.github.folgue02.sapphire.router.handler.impl.InlineStringHandler;

public class SapphireConsts {
	public static final String VERSION = "0.1.0";
	public static final BaseRouteHandler DEFAULT_NOT_FOUND_HANDLER = new InlineStringHandler("<h1>Not found</h1><p style=\"background-color: light-blue;\">Sapphire v" + SapphireConsts.VERSION + "</p>", StatusCode.NOT_FOUND);
	public static final BaseRouteHandler DEFAULT_INTERNAL_ERROR_HANDLER = new InlineStringHandler("<h1>Internal server error</h1><p style=\"background-color: light-blue;\">Sapphire v" + SapphireConsts.VERSION + "</p>", StatusCode.INTERNAL_SERVER_ERROR);
}
