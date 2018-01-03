package service.rowmappers;

import com.google.cloud.bigquery.FieldValueList;
import domain.Response;

public class ResponseRowMapper implements RowMapper<Response> {

    @Override
    public Response map(FieldValueList row) {
        // The StackDriver export sink puts all fields to lower case for some reason
        return new Response (
            getStringValue(row, "trace"),
            getDateValue(row, "datetime"),
            getLongValue(row, "duration"),
            getStringValue(row, "statuscode"),
            getStringValue(row, "exception"),
            getStringValue(row, "json")
        );
    }
}
