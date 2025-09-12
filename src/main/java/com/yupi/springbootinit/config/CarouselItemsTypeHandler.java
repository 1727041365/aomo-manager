package com.yupi.springbootinit.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;
import com.yupi.springbootinit.model.entity.CityCarousel.CarouselItem;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CarouselItemsTypeHandler extends BaseTypeHandler<List<CarouselItem>> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<CarouselItem> parameter, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        try {
            String jsonString = objectMapper.writeValueAsString(parameter);
            jsonObject.setValue(jsonString);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize CarouselItem list to JSON", e);
        }
        ps.setObject(i, jsonObject);
    }

    @Override
    public List<CarouselItem> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getObject(columnName));
    }

    @Override
    public List<CarouselItem> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getObject(columnIndex));
    }

    @Override
    public List<CarouselItem> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getObject(columnIndex));
    }
    
    private List<CarouselItem> parseJson(Object obj) throws SQLException {
        if (obj == null) {
            return null;
        }
        
        PGobject pgObject = (PGobject) obj;
        if (pgObject.getValue() == null) {
            return null;
        }
        
        try {
            return objectMapper.readValue(pgObject.getValue(), 
                new TypeReference<List<CarouselItem>>() {});
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to deserialize JSON to CarouselItem list: " + pgObject.getValue(), e);
        }
    }
}
