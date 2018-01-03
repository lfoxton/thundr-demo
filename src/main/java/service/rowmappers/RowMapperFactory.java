package service.rowmappers;

import domain.Request;
import domain.Response;

public class RowMapperFactory {

    @SuppressWarnings("unchecked")
    public static <T> RowMapper<T> build(Class<T> type) {

        if(Request.class.isAssignableFrom(type)) {
            return (RowMapper<T>) new RequestRowMapper();
        } else if (Response.class.isAssignableFrom(type)) {
            return (RowMapper<T>) new ResponseRowMapper();
        } else {
            throw new IllegalArgumentException("Unknown type for bigquery table " + type.getSimpleName());
        }

    }

}
