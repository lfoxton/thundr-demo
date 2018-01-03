package service.rowmappers;

import com.google.cloud.bigquery.FieldValueList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface RowMapper<T> {

    String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    T map(FieldValueList row);

    default String getStringValue(FieldValueList row, String field) {
        return row.get(field) != null && !row.get(field).isNull() ? row.get(field).getStringValue() : null;
    }

    /**
     * We need to convert to Double -> to long because StackDriver is converting the -1 content length the -1.0
     */
    default Long getLongValue(FieldValueList row, String field) {
        return row.get(field) != null && !row.get(field).isNull() ? new Double(row.get(field).getDoubleValue()).longValue() : null;
    }

    default Date getDateValue(FieldValueList row, String field) {
        try {
            return row.get(field) != null && !row.get(field).isNull() ? new SimpleDateFormat(DATE_TIME_FORMAT).parse(row.get(field).getStringValue()) : null;
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date field", e);
        }

    }
}
