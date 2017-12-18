package filters;

import com.threewks.thundr.request.Request;
import com.threewks.thundr.request.Response;
import com.threewks.thundr.route.HttpMethod;
import com.threewks.thundr.route.controller.Filter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public <T> T before(Request req, Response resp) {
        try {

            String contentType = req.getContentTypeString();

            RequestDto requestDto = new RequestDto(
                req.getMethod(),
                contentType,
                req.getRequestPath(),
                req.getHeader("X-FORWARDED-FOR"),
                req.getHeader("User-Agent"),
                IOUtils.toString(req.getReader())
            );

            System.out.println(requestDto);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

    @Override
    public <T> T after(Object view, Request req, Response resp) {
        return null;
    }

    @Override
    public <T> T exception(Exception e, Request req, Response resp) {
        return null;
    }

    private static class RequestDto {

        private final HttpMethod httpMethod;
        private final String requestJson;
        private final String contentType;
        private final String contextPath;
        private final String traceToken;
        private final String requesterIp;
        private final String userAgent;

        public RequestDto(HttpMethod httpMethod, String contentType, String contextPath, String requesterIp, String userAgent, String requestJson) {

            this.traceToken = RandomStringUtils.randomAlphanumeric(16);

            this.httpMethod = httpMethod;
            this.contentType = contentType;
            this.requestJson = requestJson;
            this.contextPath = contextPath;
            this.requesterIp = requesterIp;
            this.userAgent = userAgent;
        }

        @Override
        public String toString() {

            // TODO: null handling
            return "request: {" +
                "httpMethod='" + httpMethod + '\'' +
                ", contextPath='" + contextPath + '\'' +
                ", traceToken='" + traceToken + '\'' +
                ", contentType='" + contentType + '\'' +
                ", requesterIp='" + requesterIp + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", requestJson='" + requestJson + '\'' +
                '}';
        }
    }



}
