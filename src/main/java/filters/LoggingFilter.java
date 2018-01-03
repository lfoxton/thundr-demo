package filters;

import com.google.gson.Gson;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.request.Request;
import com.threewks.thundr.request.Response;
import com.threewks.thundr.route.controller.Filter;
import com.threewks.thundr.view.json.JsonView;
import service.LogService;
import servlet.MultiReadHttpServletRequest;

import javax.servlet.ServletRequest;
import java.io.IOException;

public class LoggingFilter implements Filter {

    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_FORWARDED_FOR = "X-FORWARDED-FOR";

    // Special useful headers provided by app engine
    private static final String HEADER_APP_ENGINE_COUNTRY = "X-AppEngine-Country";
    private static final String HEADER_APP_ENGINE_REGION = "X-AppEngine-Region";
    private static final String HEADER_APP_ENGINE_CITY = "X-AppEngine-City";
    private static final String HEADER_APP_ENGINE_CITY_LAT_LONG = "X-AppEngine-CityLatLong";

    private static final String PROP_SERVER_TIME = "serverTime";

    private final LogService logService;

    public LoggingFilter(LogService logService) {
        this.logService = logService;
    }

    @Override
    public <T> T before(Request req, Response resp) {
        try {

            req.putData(PROP_SERVER_TIME, System.currentTimeMillis());

            domain.Request request = new domain.Request(
                req.getId().toString(),
                req.getMethod(),
                req.getContentType(),
                req.getRequestPath(),
                getUserIpAddress(req),
                req.getHeader(HEADER_USER_AGENT),
                req.getContentLength(),
                req.getHeader(HEADER_APP_ENGINE_COUNTRY),
                req.getHeader(HEADER_APP_ENGINE_REGION),
                req.getHeader(HEADER_APP_ENGINE_CITY),
                req.getHeader(HEADER_APP_ENGINE_CITY_LAT_LONG),
                getJson(req)
            );

            logService.logRequest(request);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

    private String getUserIpAddress(Request req) {
        String remoteAddr = req.getHeader(HEADER_FORWARDED_FOR);
        if (remoteAddr == null || "".equals(remoteAddr)) {
            remoteAddr = req.getRawRequest(ServletRequest.class).getRemoteAddr();
        }
        return remoteAddr;
    }

    @Override
    public <T> T after(Object view, Request req, Response resp) {

        domain.Response response = new domain.Response(
            req.getId().toString(),
            resp.getStatusCode().name(),
            System.currentTimeMillis() - (Long)req.getData(PROP_SERVER_TIME),
            getJson(view)
        );

        logService.logResponse(response);

        return null;
    }

    @Override
    public <T> T exception(Exception exception, Request req, Response resp) {

        domain.Response response = new domain.Response(
            req.getId().toString(),
            resp.getStatusCode().name(),
            System.currentTimeMillis() - (Long)req.getData(PROP_SERVER_TIME),
            exception
        );

        logService.logResponse(response);

        return null;
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
}
