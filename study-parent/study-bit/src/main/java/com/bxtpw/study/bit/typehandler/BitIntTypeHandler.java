package com.bxtpw.study.bit.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <pre>
 * BIT 类型转换成 Integer类型
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/12 8:43
 * @since 0.1
 */
public class BitIntTypeHandler implements TypeHandler<Integer> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Integer getResult(ResultSet rs, String columnName) throws SQLException {
        return NumberUtils.bytes2int(rs.getBytes(columnName));
    }

    @Override
    public Integer getResult(ResultSet rs, int columnIndex) throws SQLException {
        return NumberUtils.bytes2int(rs.getBytes(columnIndex));
    }

    @Override
    public Integer getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return NumberUtils.bytes2int(cs.getBytes(columnIndex));
    }

}
