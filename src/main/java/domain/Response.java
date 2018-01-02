package domain;

import com.threewks.thundr.http.StatusCode;

import java.util.Date;

public class Response {

    private final String trace;
    private final Date dateTime;
    private final long duration;
    private final StatusCode statusCode;
    private final Exception exception;
    private final String json;

    public Response(String trace, StatusCode statusCode, long duration, Exception exception, String json) {
        this.trace = trace;
        this.statusCode = statusCode;
        this.duration = duration;
        this.exception = exception;
        this.json = json;
        this.dateTime = new Date();
    }

    public Response(String trace, StatusCode statusCode, long duration, String json) {
        this(trace, statusCode, duration, null, json);
    }

    public Response(String trace, StatusCode statusCode, long duration, Exception exception) {
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
