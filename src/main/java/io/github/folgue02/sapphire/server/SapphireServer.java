package io.github.folgue02.sapphire.server;

import java.net.InetSocketAddress;

import io.github.folgue02.sapphire.SapphireConsts;
import io.github.folgue02.sapphire.router.Router;
import io.github.folgue02.sapphire.router.handler.RouteHandler;

/// Sapphire server specification.
/// 
/// # Concepts
/// 
/// - [RouteHandler](Route handlers)
/// - [io.github.folgue02.sapphire.filter.RouteFilter](Route filters)
public abstract class SapphireServer {
    protected final InetSocketAddress address;
    protected final Router router;

    /// Route handler executed when no route specification matched the requested path.
    public RouteHandler noMappingFoundHandler;

    /// Route handler executed when an internal error occurs *(includes route handler errors)*.
    public RouteHandler internalServerErrorHandler;

    public SapphireServer(Router router, InetSocketAddress address) {
        this.router = router;
        this.address = address;
        this.noMappingFoundHandler = SapphireConsts.DEFAULT_NOT_FOUND_HANDLER;
        this.internalServerErrorHandler = SapphireConsts.DEFAULT_INTERNAL_ERROR_HANDLER;
    }

    /// Runs the sapphire server implementation.
    public abstract void run() throws Exception;
}
