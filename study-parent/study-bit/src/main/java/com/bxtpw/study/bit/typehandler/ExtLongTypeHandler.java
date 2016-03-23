package com.bxtpw.study.bit.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <pre>
 * BIT 类型转换成 Long类型
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/12 8:43
 * @since 0.1
 */
public class ExtLongTypeHandler extends BaseTypeHandler<Long> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj instanceof  byte[]) {
            byte[] bytes = (byte[]) obj;
            System.out.println("Size: " + bytes.length);
            for (byte b : bytes) {
                System.out.print(b + ",");
            }
        }
        System.out.println(obj);
        System.out.println(obj.getClass());
        return rs.getLong(columnName);
    }

    @Override
    public Long getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getLong(columnIndex);
    }

    @Override
    public Long getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getLong(columnIndex);
    }
}
