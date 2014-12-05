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
package org.failearly.dataset.datastore.internal.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ReservedConnection wraps the actually connection and override {@link #close()}  for returning the actually connection to
 * the {@link org.failearly.dataset.datastore.internal.connection.ConnectionHolder}.
 */
final class ReservedConnection extends DelegateConnection {

    private final ConnectionHolder connectionHolder;

    ReservedConnection(Connection connection, ConnectionHolder connectionHolder) {
        super(connection);
        this.connectionHolder = connectionHolder;
    }

    @Override
    public void close() throws SQLException {
        this.connectionHolder.back(disposeDelegation());
    }

    @Override
    public boolean isClosed() throws SQLException {
        return getInnerMostConnection()==null;
    }
}
