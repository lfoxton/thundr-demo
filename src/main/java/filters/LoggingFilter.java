package filters;

import com.google.gson.Gson;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.StatusCode;
import com.threewks.thundr.request.Request;
import com.threewks.thundr.request.Response;
import com.threewks.thundr.route.HttpMethod;
import com.threewks.thundr.route.controller.Filter;
import com.threewks.thundr.view.json.JsonView;
import org.apache.commons.lang3.RandomStringUtils;
import servlet.MultiReadHttpServletRequest;

import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public <T> T before(Request req, Response resp) {
        try {

            String trace = RandomStringUtils.randomAlphanumeric(16);
            req.putData("trace", trace);

            RequestDto requestDto = new RequestDto(
                trace,
                req.getMethod(),
                req.getContentType(),
                req.getRequestPath(),
                req.getHeader("X-FORWARDED-FOR"),
                req.getHeader("User-Agent"),
                req.getContentLength(),
                getJson(req)
            );

            System.out.println(requestDto);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

    @Override
    public <T> T after(Object view, Request req, Response resp) {

        ResponseDto responseDto = new ResponseDto(
            req.getData("trace"),
            resp.getStatusCode(),
            getJson(view)
        );

        System.out.println(responseDto);

        return null;
    }

    @Override
    public <T> T exception(Exception e, Request req, Response resp) {

        ResponseDto responseDto = new ResponseDto(
            req.getData("trace"),
            resp.getStatusCode(),
            e
        );

        System.out.println(responseDto);

        return null;
    }

    private static class RequestDto {

        private final String trace;
        private final HttpMethod httpMethod;
        private final ContentType contentType;
        private final String contextPath;
        private final String requesterIp;
        private final String userAgent;
        private final long contentLength;
        private final String json;

        public RequestDto(String trace, HttpMethod httpMethod, ContentType contentType, String contextPath, String requesterIp, String userAgent, long contentLength, String json) {
            this.trace = trace;
            this.httpMethod = httpMethod;
            this.contentType = contentType;
            this.contextPath = contextPath;
            this.requesterIp = requesterIp;
            this.userAgent = userAgent;
            this.contentLength = contentLength;
            this.json = json;
        }

        @Override
        public String toString() {

            return "request: {" +
                "httpMethod='" + httpMethod + '\'' +
                ", contextPath='" + contextPath + '\'' +
                ", traceToken='" + trace + '\'' +
                ", contentType='" + contentType + '\'' +
                ", requesterIp='" + requesterIp + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", contentLength='" + contentLength + '\'' +
                ", json='" + json + '\'' +
                '}';
        }
    }

    private String getJson(Request request) throws IOException {

        if (request.getContentType() == ContentType.ApplicationJson) {

            MultiReadHttpServletRequest multiReadRequest = request.getRawRequest(MultiReadHttpServletRequest.class);

            if (multiReadRequest != null) {
                return multiReadRequest.getRequestBody();
            }
        }

        return "";
    }

    private String getJson(Object view) {

        if (view instanceof JsonView) {
            return new Gson().toJson(((JsonView)view).getOutput());
        }

        return "";
    }

    private static class ResponseDto {

        private final String trace;
        private final StatusCode statusCode;
        private final Exception exception;
        private final String json;

        public ResponseDto(String trace, StatusCode statusCode, Exception exception, String json) {
            this.trace = trace;
            this.statusCode = statusCode;
            this.exception = exception;
            this.json = json;
        }

        public ResponseDto(String trace, StatusCode statusCode, String json) {
            this(trace, statusCode, null, json);
        }

        public ResponseDto(String trace, StatusCode statusCode, Exception exception) {
            this(trace, statusCode, exception, null);
        }

        @Override
        public String toString() {

            if (exception != null) {
                return "response: {" +
                    "trace='" + trace + '\'' +
                    ", statusCode='" + statusCode + '\'' +
                    ", exception='" + exception.getClass().getSimpleName() + '\'' +
                    ", message='" + exception.getMessage() + '\'' +
                    '}';
            }

            return "response: {" +
                "trace='" + trace + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", json='" + json + '\'' +
                '}';
        }
    }
}
