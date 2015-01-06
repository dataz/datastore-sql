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

package org.failearly.dataset.datastore.sql;

import org.failearly.dataset.datastore.DataStoreException;
import org.failearly.dataset.datastore.sql.internal.AbstractSqlDataStore;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * A SQL DataStore Implementation based on {@link javax.sql.DataSource}. To be used with a DI container like Spring.
 */
@SuppressWarnings("UnusedDeclaration")
public final class SqlDataStoreDataSource extends AbstractSqlDataStore {

    private DataSource dataSource;

    public SqlDataStoreDataSource() {
    }

    public SqlDataStoreDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void initialize() throws DataStoreException {
    }

    @Override
    protected Connection getConnection() {
        return with.producer("Get connection from data source", dataSource::getConnection);
    }
}
