package io.github.folgue02.sapphire.exchange;

import lombok.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest {
    private HttpMethod method;
    private URI requestUri;
    private Map<String, List<String>> headers;
    private Map<String, String> pathParams;
    private Map<String, String> pathQueryAttributes;
    private byte[] body;

    public String getBodyString() {
        return new String(this.body);
    }
}
