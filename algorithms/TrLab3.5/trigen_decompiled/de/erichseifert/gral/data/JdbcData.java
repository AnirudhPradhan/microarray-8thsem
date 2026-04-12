/*
 * Decompiled with CFR 0.152.
 */
package de.erichseifert.gral.data;

import de.erichseifert.gral.data.AbstractDataSource;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class JdbcData
extends AbstractDataSource {
    private static final long serialVersionUID = 5196527358266585129L;
    private final Connection a;
    private final String b;
    private boolean c;
    private int d;
    private ResultSet e;
    private int f;

    public JdbcData(Connection object, String string, boolean bl) {
        super(new Class[0]);
        this.a = object;
        this.b = string;
        this.setBuffered(bl);
        try {
            object = this;
            object = ((JdbcData)object).a.prepareStatement("SELECT * FROM " + ((JdbcData)object).b + " WHERE 1 = 0");
            object = object.getMetaData();
            int n = object.getColumnCount();
            Class[] classArray = new Class[n];
            for (int i = 0; i < n; ++i) {
                int n2 = object.getColumnType(i + 1);
                Class clazz = null;
                switch (n2) {
                    case -6: {
                        clazz = Byte.class;
                        break;
                    }
                    case 5: {
                        clazz = Short.class;
                        break;
                    }
                    case 4: {
                        clazz = Integer.class;
                        break;
                    }
                    case -5: {
                        clazz = Long.class;
                        break;
                    }
                    case 7: {
                        clazz = Float.class;
                        break;
                    }
                    case 6: 
                    case 8: {
                        clazz = Double.class;
                        break;
                    }
                    case 91: {
                        clazz = Date.class;
                        break;
                    }
                    case 92: {
                        clazz = Time.class;
                        break;
                    }
                    case 93: {
                        clazz = Timestamp.class;
                        break;
                    }
                    case -16: 
                    case -15: 
                    case -9: 
                    case -1: 
                    case 1: 
                    case 12: {
                        clazz = String.class;
                    }
                }
                classArray[i] = clazz;
            }
            this.setColumnTypes(classArray);
            return;
        }
        catch (SQLException sQLException) {
            object = sQLException;
            sQLException.printStackTrace();
            return;
        }
    }

    public JdbcData(Connection connection, String string) {
        this(connection, string, true);
    }

    @Override
    public Comparable<?> get(int n, int n2) {
        try {
            AutoCloseable autoCloseable = this.e;
            if (!this.isBuffered() || autoCloseable == null) {
                autoCloseable = this.a.prepareStatement("SELECT * FROM " + this.b, 1005, 1007);
                autoCloseable = autoCloseable.executeQuery();
                if (this.isBuffered()) {
                    this.e = autoCloseable;
                }
            }
            if (!this.isBuffered() || n2 != this.f) {
                autoCloseable.absolute(n2 + 1);
                this.f = n2;
            }
            int n3 = n;
            ResultSet resultSet = autoCloseable;
            Serializable serializable = this;
            serializable = serializable.getColumnTypes()[n3];
            ++n3;
            if (Byte.class.equals((Object)serializable)) {
                return resultSet.getByte(n3);
            }
            if (Short.class.equals((Object)serializable)) {
                return resultSet.getShort(n3);
            }
            if (Integer.class.equals((Object)serializable)) {
                return resultSet.getInt(n3);
            }
            if (Long.class.equals((Object)serializable)) {
                return resultSet.getLong(n3);
            }
            if (Float.class.equals((Object)serializable)) {
                return Float.valueOf(resultSet.getFloat(n3));
            }
            if (Double.class.equals((Object)serializable)) {
                return resultSet.getDouble(n3);
            }
            if (Date.class.equals((Object)serializable)) {
                return resultSet.getDate(n3);
            }
            if (Time.class.equals((Object)serializable)) {
                return resultSet.getTime(n3);
            }
            if (Timestamp.class.equals((Object)serializable)) {
                return resultSet.getTimestamp(n3);
            }
            if (String.class.equals((Object)serializable)) {
                return resultSet.getString(n3);
            }
            return null;
        }
        catch (SQLException sQLException) {
            SQLException sQLException2 = sQLException;
            sQLException.printStackTrace();
            return null;
        }
    }

    @Override
    public int getColumnCount() {
        if (this.getColumnTypes() != null) {
            return this.getColumnTypes().length;
        }
        return 0;
    }

    @Override
    public int getRowCount() {
        int n = this.d;
        if (!this.isBuffered() || n < 0) {
            try {
                PreparedStatement preparedStatement = this.a.prepareStatement("SELECT COUNT(*) FROM " + this.b, 1005, 1007);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.first()) {
                    this.d = n = resultSet.getInt(1);
                } else {
                    n = 0;
                }
                resultSet.close();
            }
            catch (SQLException sQLException) {
                SQLException sQLException2 = sQLException;
                sQLException.printStackTrace();
                n = 0;
            }
        }
        return n;
    }

    public boolean isBuffered() {
        return this.c;
    }

    public void setBuffered(boolean bl) {
        this.c = bl;
        this.d = -1;
        this.e = null;
        this.f = -1;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws ClassNotFoundException, IOException {
        throw new UnsupportedOperationException("JDBC data sources cannot be serialized.");
    }
}

