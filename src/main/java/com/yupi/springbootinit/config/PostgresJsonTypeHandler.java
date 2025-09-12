package com.yupi.springbootinit.config;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(List.class)
public class PostgresJsonTypeHandler extends JacksonTypeHandler {

    private final Class<?> type;

    public PostgresJsonTypeHandler(Class<?> type) {
        super(type);
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(parameter);
            jsonObject.setValue(jsonString);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize parameter to JSON", e);
        }
        ps.setObject(i, jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getObject(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getObject(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getObject(columnIndex));
    }

    private Object parseJson(Object obj) throws SQLException {
        if (obj == null) {
            return null;
        }

        PGobject pgObject = (PGobject) obj;
        if (pgObject.getValue() == null) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            // 根据实际类型进行反序列化
            if (type.equals(List.class)) {
                // 如果是 List 类型，需要知道具体的泛型类型
                // 这里假设是 List<CityCarousel.CarouselItem>
                return mapper.readValue(pgObject.getValue(),
                        new TypeReference<List<com.yupi.springbootinit.model.entity.CityCarousel.CarouselItem>>() {});
            }
            return mapper.readValue(pgObject.getValue(), type);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to deserialize JSON: " + pgObject.getValue(), e);
        }
    }
}
