package io.github.folgue02.sapphire.exchange;

import io.github.folgue02.sapphire.consts.HeaderConsts;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public final class HttpResponse {
    private StatusCode status;
    private Map<String, String> headers;
    private byte[] body;

    public HttpResponse() {
        this.status = StatusCode.OK;
        this.headers = new HashMap<>();
    }

    public void setBody(String body) {
        this.body = body.getBytes();
    }

    public void putHeader(String headerName, String headerValue) {
        this.headers.put(headerName, headerValue);
    }

    public void setCalculatedBodyLength() {
        this.headers.put(HeaderConsts.CONTENT_LENGTH, Integer.toString(this.body.length));
    }
}
