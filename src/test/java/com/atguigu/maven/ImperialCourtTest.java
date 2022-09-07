package com.atguigu.maven;

import com.atguigu.imperial.court.util.JDBCUtils;
import java.sql.Connection;
import org.junit.Test;

public class ImperialCourtTest {
    @Test
    public void testGetConnection() {

        Connection connection = JDBCUtils.getConnection();
        System.out.println("connection = " + connection);

        JDBCUtils.releaseConnection(connection);

    }
}
