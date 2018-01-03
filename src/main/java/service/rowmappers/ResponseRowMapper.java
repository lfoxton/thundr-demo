package service.rowmappers;

import com.google.cloud.bigquery.FieldValueList;
import domain.Response;

public class ResponseRowMapper implements RowMapper<Response> {

    @Override
    public Response map(FieldValueList row) {
        return new Response (
            getStringValue(row, "trace"),
            getDateValue(row, "dateTime"),
            getLongValue(row, "duration"),
            getStringValue(row, "statusCode"),
            getStringValue(row, "exception"),
            getStringValue(row, "json")
        );
    }
}
