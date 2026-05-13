package com.gxa.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(String.class)
public class BlobTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = rs.getBlob(columnName);
        return blobToString(blob);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = rs.getBlob(columnIndex);
        return blobToString(blob);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        return blobToString(blob);
    }

    private String blobToString(Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            throw new SQLException("Failed to convert BLOB to String", e);
        }
    }
}
