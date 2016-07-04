/*
 * Copyright (c) 2009.
 *
 * Date: 20.05.16
 * 
 */
package org.failearly.dataz.datastore.sql;

/**
 * Please use these property keys for configuration within a
 * property file or {@link SqlDataStore#properties()}.
 */
public interface SqlConfigProperties {
    String DATASTORE_SQL_DRIVER = "jdbc.driver";
    String DATASTORE_SQL_URL = "jdbc.url";
    String DATASTORE_SQL_USER = "jdbc.user";
    String DATASTORE_SQL_PASSWORD = "jdbc.password";
}
