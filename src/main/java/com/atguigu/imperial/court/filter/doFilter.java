package com.atguigu.imperial.court.filter;

import com.atguigu.imperial.court.util.JDBCUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class doFilter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, SQLException {
        // 1、获取数据库连接
        // 重要：要保证参与事务的多个数据库操作（SQL 语句）使用的是同一个数据库连接
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            // 重要操作：关闭自动提交功能
            connection.setAutoCommit(false);

            // 2、核心操作：通过 chain 对象放行当前请求
            // 这样就可以保证当前请求覆盖的 Servlet 方法、Service 方法、Dao 方法都在同一个事务中。
            // 同时各个请求都经过这个 Filter，所以当前事务控制的代码在这里只写一遍就行了，
            // 避免了代码的冗余。
            chain.doFilter(request, response);

            // 3、核心操作成功结束，可以提交事务
            connection.commit();
        } catch (Exception e) {
            // 4、核心操作抛出异常，必须回滚事务
            if(connection != null){
                connection.rollback();
            }


        }finally {
            // 5、释放数据库连接
            JDBCUtils.releaseConnection(connection);
        }


    }
}
