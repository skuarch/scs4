package net;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author skuarch
 */
public class ConnectPool {

    private DataSource dataSource = null;

    //==========================================================================
    public ConnectPool(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //==========================================================================
    private Connection getConnection() throws Exception {

        Connection connection = null;

        try {

            if (dataSource != null) {
                connection = dataSource.getConnection();
            }

        } catch (Exception e) {
            throw e;
        }

        return connection;

    } // end getConnection

    //=========================================================================    
    public ResultSet executeQuery(String sql) throws Exception {

        if (sql == null || sql.length() < 1) {
            throw new NullPointerException("sql is null or empty");
        }

        ResultSet resultSet = null;
        Connection connection = null;
        Statement statement = null;
        CachedRowSet crs = null;

        try {

            connection = getConnection();

            if (connection == null) {
                return null;
            }

            statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery(sql);

            //resultset is null ?
            if (resultSet == null) {
                throw new NullPointerException("resultSet is null");
            }

            crs = new CachedRowSetImpl();
            crs.populate(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseUtilities.closeConnection(connection);
            DataBaseUtilities.closeStatement(statement);
            DataBaseUtilities.closeResultSet(resultSet);
        }

        return crs;
    } //end executeQuery

    //==========================================================================
    public void update(String sql) throws Exception {

        if (sql == null || sql.length() < 1) {
            throw new NullPointerException("sql is null or empty");
        }

        Connection connection = null;
        Statement statement = null;

        try {

            connection = getConnection();

            if (connection == null) {
                return;
            }

            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseUtilities.closeConnection(connection);
            DataBaseUtilities.closeStatement(statement);            
            sql = null;
        }

    } //end update

    //==========================================================================
    public int getNumRows(ResultSet resultSet) throws Exception {

        if (resultSet == null) {
            throw new NullPointerException("resultSet is null");
        }

        int numRows = 0;

        try {

            resultSet.last();
            numRows = resultSet.getRow();
            resultSet.beforeFirst();

        } catch (Exception e) {
            throw e;
        }

        return numRows;

    } // getNumRows

    //**************************************************************************
    //**************************************************************************
    public static class DataBaseUtilities {

        //======================================================================
        public DataBaseUtilities() {
        }        

        //==========================================================================
        public static void closeConnection(Connection connection) {
            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        } // end closeConnection

        //==========================================================================
        public static void closeStatement(Statement statement) {
            try {

                if (statement != null) {
                    statement.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                statement = null;
            }
        } // end closeStatement

        //==========================================================================
        public static void closeResultSet(ResultSet resultSet) {
            try {

                if (resultSet != null) {
                    resultSet.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                resultSet = null;
            }
        } // end closeStatement
    } // end nested class
    
} // end class