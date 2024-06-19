package swe4.mapper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

public class PersistenceManager {

    private final String connectionString;
    private final String userName;
    private final String password;
    private Connection connection;

    public PersistenceManager(String connectionString) {
        this(connectionString, null, null);
    }

    public PersistenceManager(String connectionString, String userName, String password) {
        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnection() {
        try {
            if (connection == null)
                connection = DriverManager.getConnection(connectionString, userName, password);
            return connection;
        } catch (SQLException ex) {
            throw new IllegalStateException("Can't establish connection to database. SQLException: "
                    + ex.getMessage());
        }
    }

    public <T> T get(Class<T> entityType, Object id) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(entityType);
        String sql = queryBuilder.buildSelectByIdQuery();
        List<FieldDescription> fieldDescriptions = queryBuilder.getFieldDescriptions();

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try {
                        T instance = entityType.getDeclaredConstructor().newInstance();
                        for (var f : fieldDescriptions) {
                            Field field = entityType.getDeclaredField(f.getFieldName());
                            field.setAccessible(true); // make private fields accessible
                            field.set(instance, rs.getObject(f.getColumnName()));
                        }

                        return instance;
                    } catch (Exception e) {
                        throw new DataAccessException("Exception while executing query '%s'".formatted(sql));
                    }
                } else {
                    return null;
                }
            }
        }
    }

}
