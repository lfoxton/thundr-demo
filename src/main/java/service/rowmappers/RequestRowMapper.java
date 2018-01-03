package service.rowmappers;

import com.google.cloud.bigquery.FieldValueList;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.route.HttpMethod;
import domain.Request;

public class RequestRowMapper implements RowMapper<Request> {

    @Override
    public Request map(FieldValueList row) {
        return new Request(
            getStringValue(row, "trace"),
            getDateValue(row, "dateTime"),
            HttpMethod.from(getStringValue(row, "httpMethod")),
            ContentType.from(getStringValue(row, "contentType")),
            getStringValue(row, "contextPath"),
            getStringValue(row, "requesterIp"),
            getStringValue(row, "userAgent"),
            getLongValue(row, "contentLength"),
            getStringValue(row, "userCountry"),
            getStringValue(row, "userRegion"),
            getStringValue(row, "userCity"),
            getStringValue(row, "userCityLatLong"),
            getStringValue(row, "json")
        );
    }



}
