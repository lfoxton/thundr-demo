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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class LoggingFilter implements Filter {

    @Override
    public <T> T before(Request req, Response resp) {
        try {

            String trace = RandomStringUtils.randomAlphanumeric(16);
            req.putData("trace", trace);
            req.putData("serverTime", System.currentTimeMillis());

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
            System.currentTimeMillis() - (Long)req.getData("serverTime"),
            getJson(view)
        );

        System.out.println(responseDto);

        return null;
    }

    @Override
    public <T> T exception(Exception exception, Request req, Response resp) {

        ResponseDto responseDto = new ResponseDto(
            req.getData("trace"),
            resp.getStatusCode(),
            System.currentTimeMillis() - (Long)req.getData("serverTime"),
            exception
        );

        System.out.println(responseDto);

        return null;
    }

    private static class RequestDto {

        private final String trace;
        private final ZonedDateTime dateTime;
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
            this.dateTime = ZonedDateTime.now(ZoneOffset.UTC);
        }

        @Override
        public String toString() {

            return "request: {" +
                "trace='" + trace + '\'' +
                ", date='" + dateTime + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
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
        private final ZonedDateTime dateTime;
        private final long duration;
        private final StatusCode statusCode;
        private final Exception exception;
        private final String json;

        public ResponseDto(String trace, StatusCode statusCode, long duration, Exception exception, String json) {
            this.trace = trace;
            this.statusCode = statusCode;
            this.duration = duration;
            this.exception = exception;
            this.json = json;
            this.dateTime = ZonedDateTime.now(ZoneOffset.UTC);
        }

        public ResponseDto(String trace, StatusCode statusCode, long duration, String json) {
            this(trace, statusCode, duration, null, json);
        }

        public ResponseDto(String trace, StatusCode statusCode, long duration, Exception exception) {
            this(trace, statusCode, duration, exception, null);
        }

        @Override
        public String toString() {

            if (exception != null) {
                return "response: {" +
                    "trace='" + trace + '\'' +
                    ", date='" + dateTime + '\'' +
                    ", statusCode='" + statusCode + '\'' +
                    ", duration='" + duration + "ms\'" +
                    ", exception='" + exception.getClass().getSimpleName() + '\'' +
                    ", message='" + exception.getMessage() + '\'' +
                    '}';
            }

            return "response: {" +
                "trace='" + trace + '\'' +
                ", date='" + dateTime + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", duration='" + duration + "ms\'" +
                ", json='" + json + '\'' +
                '}';
        }
    }
}
