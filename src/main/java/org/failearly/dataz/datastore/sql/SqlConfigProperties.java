/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
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
