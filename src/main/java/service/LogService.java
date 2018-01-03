package service;

import com.google.cloud.MonitoredResource;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import domain.Request;
import domain.Response;
import service.bigquery.BigQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static service.rowmappers.RowMapper.DATE_TIME_FORMAT;

public class LogService {

    private static final String QUERY_REQUESTS = "SELECT jsonPayload.* FROM Access.Access_20180104 where jsonPayload.type = \"REQUEST\" ORDER BY jsonPayload.datetime DESC LIMIT 30";
    private static final String QUERY_RESPONSES = "SELECT jsonPayload.* FROM Access.Access_20180104 where jsonPayload.type = \"RESPONSE\" ORDER BY jsonPayload.datetime DESC LIMIT 30";

    private static final String LOG_NAME_ACCESS = "Access";
    private static final String MONITOR_GLOBAL = "global";

    private final BigQueryService bigQueryService;
    private final Logging logging;
    private final Gson gson;

    public LogService(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
        this.logging = LoggingOptions.getDefaultInstance().getService();;
        this.gson = new GsonBuilder().setDateFormat(DATE_TIME_FORMAT).create();
    }

    public List<Request> getRequests() throws InterruptedException {
        return bigQueryService.query(QueryJobConfiguration.newBuilder(QUERY_REQUESTS).build(), Request.class);
    }

    public List<Response> getResponses() throws InterruptedException {
        return bigQueryService.query(QueryJobConfiguration.newBuilder(QUERY_RESPONSES).build(), Response.class);
    }

    public void logRequest(Request input) {
        Map<String, Object> jsonMap = objectToMap(input);
        jsonMap.put("type", "REQUEST");
        List<LogEntry> entries = new ArrayList<>();
        entries.add(LogEntry.of(Payload.JsonPayload.of(jsonMap)));
        logAccess(entries);
    }

    public void logResponse(Response input) {
        Map<String, Object> jsonMap = objectToMap(input);
        jsonMap.put("type", "RESPONSE");
        List<LogEntry> entries = new ArrayList<>();
        entries.add(LogEntry.of(Payload.JsonPayload.of(jsonMap)));
        logAccess(entries);
    }

    private void logAccess(List<LogEntry> entries) {
        logging.write(entries, Logging.WriteOption.logName(LOG_NAME_ACCESS), Logging.WriteOption.resource(MonitoredResource.newBuilder(MONITOR_GLOBAL).build()));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> objectToMap(Object obj) {
        JsonElement jsonElement = gson.toJsonTree(obj);
        return (Map<String, Object>) gson.fromJson(jsonElement, Map.class);
    }
}
