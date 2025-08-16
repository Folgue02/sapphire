package io.github.folgue02.sapphire.router.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.folgue02.sapphire.exchange.HttpResponse;

public abstract class JsonHandler extends BaseRouteHandler<Object> {
	private ObjectMapper oMapper = new ObjectMapper();

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.oMapper = objectMapper;
	}

	@Override
	public HttpResponse processResult(HttpResponse response, Object result) throws Exception {
		response.setBody(this.oMapper.writeValueAsString(result));

		return response;
	}

	@Override
	public String getHandlerDescription() {
		return "A handler which turns the result of handle() into a JSON string.";
	}
}
