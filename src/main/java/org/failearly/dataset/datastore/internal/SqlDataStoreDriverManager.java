/*
 * dataSet - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2014 Marko Umek (http://fail-early.com/contact)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.failearly.dataset.datastore.internal;

import org.failearly.dataset.datastore.DataSetResource;
import org.failearly.dataset.datastore.internal.connection.ConnectionHolder;
import org.failearly.dataset.datastore.support.SimpleFileTransactionalSupportDataStoreBase;
import org.failearly.dataset.simplefile.SimpleFileStatement;
import org.failearly.dataset.util.ClassUtils;
import org.failearly.dataset.util.ExtendedProperties;

import java.sql.*;

/**
 * A SQL DataStore Implementation based on {@link java.sql.DriverManager}.
 */
public final class SqlDataStoreDriverManager extends AbstractSqlDataStore {

    private String url;
    private ConnectionHolder connectionHolder;

    SqlDataStoreDriverManager(String dataStoreId, String dataStoreConfig) {
        super(dataStoreId, dataStoreConfig);
    }

    @Override
    protected void doInitialize(ExtendedProperties properties) {
        final String driverClass = properties.getMandatoryProperty("datastore.sql.driver");
        this.url = properties.getProperty("datastore.sql.url");
        ClassUtils.loadClass(Driver.class, driverClass);
        connectionHolder=ConnectionHolder.create(
                with.producer("Get connection", () -> DriverManager.getConnection(url, properties))
            );
    }

    @Override
    protected Connection getConnection() {
        return connectionHolder.fetch();
    }

}
