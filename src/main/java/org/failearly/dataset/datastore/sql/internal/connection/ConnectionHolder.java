/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright (C) 2014-2016 'Marko Umek' (http://fail-early.com)
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

package org.failearly.dataset.datastore.sql.internal.connection;

import org.failearly.common.test.With;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * ConnectionHolder protects the connection instance against access of multiple threads.
 */
public class ConnectionHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHolder.class);

    private volatile Connection connection;

    private ConnectionHolder(Connection connection) {
        this.connection = connection;
    }

    public static ConnectionHolder create(Connection connection) {
        return new ConnectionHolder(connection);
    }

    public Connection reserve() {
        LOGGER.debug("Reserve connection: {}", this.connection);
        checkThreadAccess();
        final ReservedConnection reservedConnection = new ReservedConnection(this.connection, this);
        this.connection = null;
        return reservedConnection;
    }

    private void checkThreadAccess() {
        if( this.connection==null ) {
            throw new IllegalStateException("Attempt to run tests multi threaded.");
        }
    }

    void release(Connection connection) {
        LOGGER.debug("Release connection: {}", connection);
        if( connection==null ) {
            throw new IllegalArgumentException("Attempt to close a connection twice.");
        }
        this.connection = connection;
    }

    public void dispose() {
        With.ignore().action("Close actually connection", () -> {
            if( connection!=null ) {
                connection.close();
            }
        });
    }
}
