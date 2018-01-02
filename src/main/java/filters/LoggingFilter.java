package filters;

import com.google.gson.Gson;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.request.Request;
import com.threewks.thundr.request.Response;
import com.threewks.thundr.route.controller.Filter;
import com.threewks.thundr.view.json.JsonView;
import org.apache.commons.lang3.RandomStringUtils;
import service.BigQueryService;
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

    private static final String PROP_TRACE = "trace";
    private static final String PROP_SERVER_TIME = "serverTime";


    private final BigQueryService bigQueryService;

    public LoggingFilter(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    @Override
    public <T> T before(Request req, Response resp) {
        try {

            String trace = RandomStringUtils.randomAlphanumeric(16);
            req.putData(PROP_TRACE, trace);
            req.putData(PROP_SERVER_TIME, System.currentTimeMillis());

            domain.Request request = new domain.Request(
                trace,
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

            Logger.info(request.toString());

            bigQueryService.insert(request);

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
            req.getData(PROP_TRACE),
            resp.getStatusCode(),
            System.currentTimeMillis() - (Long)req.getData(PROP_SERVER_TIME),
            getJson(view)
        );

        Logger.info(response.toString());

        bigQueryService.insert(response);

        return null;
    }

    @Override
    public <T> T exception(Exception exception, Request req, Response resp) {

        domain.Response responseDto = new domain.Response(
            req.getData("trace"),
            resp.getStatusCode(),
            System.currentTimeMillis() - (Long)req.getData("serverTime"),
            exception
        );

        Logger.info(responseDto.toString());

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
