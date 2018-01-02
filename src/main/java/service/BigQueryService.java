package service;

import com.google.cloud.bigquery.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threewks.thundr.logger.Logger;

import java.util.List;
import java.util.Map;

public class BigQueryService {

    private static final String TABLE_REQUESTS = "Requests";
    private static final String TABLE_RESPONSES = "Responses";

    private final BigQuery bigQuery;
    private final String bigQueryDataSet;
    private final Gson gson;

    public BigQueryService(String bigQueryProjectId, String bigQueryDataSet) {
        this.bigQuery = BigQueryOptions.newBuilder().setProjectId(bigQueryProjectId).build().getService();
        this.bigQueryDataSet = bigQueryDataSet;
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
    }

    /**
     * @param input must be one of [domain.Request, domain.Response] or you will get an IllegalArgumentException
     * @return
     */
    public InsertAllResponse insert(Object input) {
        return _insert(getTableId(input), objectToMap(input));
    }

    private InsertAllResponse _insert(TableId tableId, Map<String, Object> rowContent) {
        InsertAllRequest.Builder builder = InsertAllRequest.newBuilder(tableId);
        builder.addRow(rowContent);
        return _insert(builder.build());
    }

    private InsertAllResponse _insert(TableId tableId, List<Map<String, Object>> rowContentList) {
        InsertAllRequest.Builder builder = InsertAllRequest.newBuilder(tableId);
        rowContentList.stream().forEach(builder::addRow);
        return _insert(builder.build());
    }

    private InsertAllResponse _insert(InsertAllRequest request) {
        InsertAllResponse response = bigQuery.insertAll(request);
        if (response.hasErrors()) {
            Map<Long, List<BigQueryError>> errors = response.getInsertErrors();
            Logger.info("Encountered %s errors inserting into \"%s\"", errors.size(), request.getTable().getTable());
            errors.forEach((k, v) -> Logger.error("Error: %s, %s", k, v));
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> objectToMap(Object obj) {
        JsonElement jsonElement = gson.toJsonTree(obj);
        return (Map<String, Object>) gson.fromJson(jsonElement, Map.class);
    }

    private TableId getTableId(Object object) {
        if (object instanceof domain.Request) {
            return TableId.of(bigQueryDataSet, TABLE_REQUESTS);
        } else if (object instanceof domain.Response) {
            return TableId.of(bigQueryDataSet, TABLE_RESPONSES);
        } else {
            throw new IllegalArgumentException(object.getClass().getSimpleName() + " is not a valid type to insert to BigQuery");
        }
    }

}
