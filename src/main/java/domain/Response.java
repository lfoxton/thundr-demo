package domain;


import java.util.Date;

public class Response {

    private final String trace;
    private final Date dateTime;
    private final long duration;
    private final String statusCode;
    private final String exception;
    private final String json;


    public Response(String trace, Date dateTime, long duration, String statusCode, String exception, String json) {
        this.trace = trace;
        this.dateTime = dateTime;
        this.duration = duration;
        this.statusCode = statusCode;
        this.exception = exception;
        this.json = json;
    }

    public Response(String trace, String statusCode, long duration, String exception, String json) {
        this(trace, new Date(), duration, statusCode, exception, json);
    }

    public Response(String trace, String statusCode, long duration, String json) {
        this(trace, statusCode, duration, null, json);
    }

    public Response(String trace, String statusCode, long duration, Exception exception) {
        this(trace, statusCode, duration, exception.getClass().getSimpleName(), null);
    }

    @Override
    public String toString() {
        return "Response{" +
            "trace='" + trace + '\'' +
            ", dateTime=" + dateTime +
            ", duration=" + duration +
            ", statusCode='" + statusCode + '\'' +
            ", exception='" + exception + '\'' +
            ", json='" + json + '\'' +
            '}';
    }
}
