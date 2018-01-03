package domain;

import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.route.HttpMethod;

import java.util.Date;

public class Request {

    private final String trace;
    private final Date dateTime;
    private final HttpMethod httpMethod;
    private final ContentType contentType;
    private final String contextPath;
    private final String requesterIp;
    private final String userAgent;
    private final long contentLength;
    private final String userCountry;
    private final String userRegion;
    private final String userCity;
    private final String userCityLatLong;
    private final String json;

    public Request(String trace, Date dateTime, HttpMethod httpMethod, ContentType contentType, String contextPath, String requesterIp, String userAgent, long contentLength, String userCountry, String userRegion, String userCity, String userCityLatLong, String json) {
        this.trace = trace;
        this.dateTime = dateTime;
        this.httpMethod = httpMethod;
        this.contentType = contentType;
        this.contextPath = contextPath;
        this.requesterIp = requesterIp;
        this.userAgent = userAgent;
        this.contentLength = contentLength;
        this.userCountry = userCountry;
        this.userRegion = userRegion;
        this.userCity = userCity;
        this.userCityLatLong = userCityLatLong;
        this.json = json;
    }

    public Request(String trace, HttpMethod httpMethod, ContentType contentType, String contextPath, String requesterIp, String userAgent, long contentLength, String userCountry, String userRegion, String userCity, String userCityLatLong, String json) {
        this(trace, new Date(), httpMethod, contentType, contextPath, requesterIp, userAgent, contentLength, userCountry, userRegion, userCity, userCityLatLong, json);
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
                ", userCountry='" + userCountry + '\'' +
                ", userRegion='" + userRegion + '\'' +
                ", userCity='" + userCity + '\'' +
                ", userCityLatLong='" + userCityLatLong + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
