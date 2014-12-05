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

import org.failearly.dataset.util.With;

import java.sql.Connection;

/**
 * ConnectionHolder protects the connection instance against access of multiple threads.
 */
public class ConnectionHolder {

    private volatile Connection connection;

    private ConnectionHolder(Connection connection) {
        this.connection = connection;
    }

    public static ConnectionHolder create(Connection connection) {
        return new ConnectionHolder(connection);
    }

    public Connection fetch() {
        checkThreadAccess();
        final Connection connection=this.connection;
        this.connection = null;
        return new ReservedConnection(connection, this);
    }

    private void checkThreadAccess() {
        if( this.connection==null ) {
            throw new IllegalStateException("Attempt to run tests multi threaded.");
        }
    }

    void back(Connection connection) {
        if( connection==null ) {
            throw new IllegalArgumentException("Attempt to close a connection twice.");
        }
        this.connection = connection;
    }

    public void dispose() {
        With.ignore().action("Close connection", new With.Action() {
            @Override
            public void apply() throws Exception {
                if( connection!=null ) {
                    connection.close();
                }
            }
        });
    }
}
