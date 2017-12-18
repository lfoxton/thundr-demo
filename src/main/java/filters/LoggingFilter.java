package filters;

import com.threewks.thundr.route.HttpMethod;
import com.threewks.thundr.route.controller.Filter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoggingFilter implements Filter {

    @Override
    public <T> T before(HttpMethod method, HttpServletRequest req, HttpServletResponse resp) {

        try {

            String contentType = req.getContentType();

            RequestDto requestDto = new RequestDto(
                method,
                contentType,
                req.getContextPath(),
                getRequesterIp(req),
                req.getHeader("User-Agent"),
                getRequestJson(req, contentType)
            );

            System.out.println(requestDto);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

    @Override
    public <T> T after(HttpMethod method, Object view, HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }

    @Override
    public <T> T exception(HttpMethod method, Exception e, HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }

    private static String getRequestJson(HttpServletRequest req, String contentType) throws IOException {
        if (contentType != null && req.getContentType().equals("application/json")) {
            return IOUtils.toString(req.getReader());
        } else {
            return "";
        }
    }

    private static String getRequesterIp(HttpServletRequest req) {
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        return ipAddress;
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
