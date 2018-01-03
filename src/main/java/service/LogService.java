package service;

import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.QueryJobConfiguration;
import domain.Request;
import domain.Response;
import service.bigquery.BigQueryService;

import java.util.List;

public class LogService {

    private static final String QUERY_REQUESTS = "SELECT * FROM HelloWorld.Requests ORDER BY dateTime DESC LIMIT 30";
    private static final String QUERY_RESPONSES = "SELECT * FROM HelloWorld.Responses ORDER BY dateTime DESC LIMIT 30";

    private final BigQueryService bigQueryService;

    public LogService(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    public List<Request> getRequests() throws InterruptedException {
        return bigQueryService.query(QueryJobConfiguration.newBuilder(QUERY_REQUESTS).build(), Request.class);
    }

    public List<Response> getResponses() throws InterruptedException {
        return bigQueryService.query(QueryJobConfiguration.newBuilder(QUERY_RESPONSES).build(), Response.class);
    }

    public InsertAllResponse logRequest(Request input) {
        return bigQueryService.insert(input);
    }

    public InsertAllResponse logResponse(Response input) {
        return bigQueryService.insert(input);
    }
}
