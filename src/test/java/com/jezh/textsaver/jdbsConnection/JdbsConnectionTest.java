package com.jezh.textsaver.jdbsConnection;

import com.jezh.textsaver.entity.TextCommonData;
import com.jezh.textsaver.entity.TextPart;
import com.jezh.textsaver.jpaTestUtils.JpaTestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {TextsaverApplication.class, TestEntityManagerDataSourcePostgresConfig.class})
public class JdbsConnectionTest {

    private Connection connection;
//    private Transaction transaction;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private CallableStatement callableStatement;
    private ResultSet resultSet;

    private TextPart textPart;

//    private static final String PROP_DATABASE_DRIVER = "org.postgresql.Driver";
//    private static final String PROP_DATABASE_PASSWORD = "password";
//    private static final String PROP_DATABASE_URL = "jdbc:postgresql://localhost:5432/textsaver";
//    private static final String PROP_DATABASE_USERNAME = "postgres";
    private static final String PROP_DATABASE_DRIVER;
    private static final String PROP_DATABASE_PASSWORD;
    private static final String PROP_DATABASE_URL;
    private static final String PROP_DATABASE_USERNAME;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("src\\main\\resources\\application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PROP_DATABASE_DRIVER = properties.getProperty("spring.datasource.driver-class-name");
        PROP_DATABASE_PASSWORD = properties.getProperty("spring.datasource.password");
        PROP_DATABASE_URL = properties.getProperty("spring.datasource.url");
        PROP_DATABASE_USERNAME = properties.getProperty("spring.datasource.username");
    }

    public static final String INSERT_SQL = "insert into public.text_parts(id, body) values(?, ?)";
    public static final String CREATE_TABLE_text_parts = "CREATE TABLE IF NOT EXISTS text_parts (" +
            "  id SERIAL PRIMARY KEY," +
            "  body TEXT" +
            ");";

    private DriverManager driverManager;

    @Before
    public void setUp() throws Exception {
        textPart = TextPart.builder().body(JpaTestUtils.newTextPartBody()).build();

        connection = DriverManager.getConnection(
                PROP_DATABASE_URL,
                PROP_DATABASE_USERNAME,
                PROP_DATABASE_PASSWORD
        );
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
    }

    @After
    public void tearDown() throws Exception {
        // if resultSet etc. equally null, they throw SQLException when close() method is called.
        // So the condition "not null" verifies here and the "&&" statement used.
        if (resultSet != null && !resultSet.isClosed()) resultSet.close();
        if (statement != null && !statement.isClosed()) statement.close();
        if (preparedStatement != null && !preparedStatement.isClosed()) preparedStatement.close();
        if (connection != null && !connection.isClosed()) {
//            connection.rollback();
            connection.close();
        }

        textPart = null;
    }

    @Test
    public void getAvailableDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) System.out.println(drivers.nextElement().getClass().getName());
    }

    @Test
    public void getConnection() throws SQLException {
        Assert.assertNotNull(connection);
        Assert.assertTrue(connection.isValid(1000));
    }

    @Test
    public void testSaveTextPart_thenOk() throws Exception {
//        preparedStatement = connection.prepareStatement(CREATE_TABLE_text_parts);
//        preparedStatement.execute();
        preparedStatement = connection.prepareStatement(INSERT_SQL);
        preparedStatement.setLong(1, new Random().nextInt(1000) + 1);
        preparedStatement.setString(2, "JdbsConnectionTest/ testSaveTextPart_thenOk()/ " +
                "preparedStatement.executeUpdate(): " + JpaTestUtils.newTextPartBody());
        preparedStatement.executeUpdate();
        connection.commit();
    }

    @Test
    public void test_get_textparts_ordered_set_storedFunction() throws SQLException {
//      CallableStatement - The interface used to execute SQL stored procedures
        CallableStatement function = connection.prepareCall(
                "{? = call get_textparts_ordered_set(?, ?)}"
// The constant indicating the type for a ResultSet object that is scrollable but generally not sensitive to changes
// to the data that underlies the ResultSet.
                , ResultSet.TYPE_SCROLL_INSENSITIVE
// The constant indicating the concurrency mode for a ResultSet object that may be updated.
                , ResultSet.CONCUR_UPDATABLE
// The constant indicating that open ResultSet objects with this holdability will be closed when the current transaction
// is committed.
                , ResultSet.CLOSE_CURSORS_AT_COMMIT);
        function.setLong(1, 28L);
        function.setInt(2, 5);
//        function.registerOutParameter(3, Types.OTHER);
        function.execute();
        ResultSet result = function.getResultSet();
        List<TextPart> textParts = new ArrayList<>();
        Long id = 0L;
        while (result.next()) {
            id = result.getLong(4);}
        TextCommonData textCommonData = TextCommonData.builder().build();
        textCommonData.setId(id);
// Second iteration through ResultSet object is possible, since I've set ResutSet type as ResultSet.TYPE_SCROLL_INSENSITIVE
        while (result.next()) {
            TextPart textPart = TextPart
                    .builder()
                    .body(result.getString(1))
                    .previousItem(result.getLong(2))
                    .nextItem(result.getLong(3))
                    .textCommonData(textCommonData)
                    .lastUpdate(result.getDate(5))
                    .build();
            textParts.add(textPart);
        }
        connection.commit();

        textParts.forEach(System.out::println);
        System.out.println("TextCommonData.id = " + textParts.get(0).getTextCommonData().getId());
    }
//
//    @Test
//    public void testFindTextPartById() throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT public.get_tp_by_id(28)");
//
//
//        preparedStatement.execute();
//    }
}
