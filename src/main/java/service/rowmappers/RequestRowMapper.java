package service.rowmappers;

import com.google.cloud.bigquery.FieldValueList;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.route.HttpMethod;
import domain.Request;

public class RequestRowMapper implements RowMapper<Request> {

    @Override
    public Request map(FieldValueList row) {
        // The StackDriver export sink puts all fields to lower case for some reason
        return new Request(
            getStringValue(row, "trace"),
            getDateValue(row, "datetime"),
            HttpMethod.from(getStringValue(row, "httpmethod")),
            ContentType.from(getStringValue(row, "contenttype")),
            getStringValue(row, "contextpath"),
            getStringValue(row, "requesterip"),
            getStringValue(row, "useragent"),
            getLongValue(row, "contentlength"),
            getStringValue(row, "usercountry"),
            getStringValue(row, "userregion"),
            getStringValue(row, "usercity"),
            getStringValue(row, "usercitylatlong"),
            getStringValue(row, "json")
        );
    }



}
