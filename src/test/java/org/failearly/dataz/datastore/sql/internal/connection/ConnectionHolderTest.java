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

package org.failearly.dataz.datastore.sql.internal.connection;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * ConnectionHolderTest contains tests for ... .
 */
@Deprecated
public class ConnectionHolderTest {

    private final Connection connection=Mockito.mock(Connection.class);
    private ConnectionHolder connectionHolder;

    @Before
    public void setUp() throws Exception {
        this.connectionHolder = Mockito.spy(ConnectionHolder.create(connection));
    }

    @Test
    public void fetchConnection() throws Exception {
        // act / when
        final Connection fetchedConnection = connectionHolder.reserve();

        // assert / then
        assertThat("Fetched connection?", fetchedConnection, Matchers.notNullValue());
        assertThat("connection type?", fetchedConnection, Matchers.instanceOf(ReservedConnection.class));
        Mockito.verifyZeroInteractions(this.connection);
    }

    @Test(expected = RuntimeException.class)
    public void fetchConnectionTwice() throws Exception {
        // arrange / given
        connectionHolder.reserve();

        // act / when
        connectionHolder.reserve();
    }

    @Test
    public void closeFetchedConnection() throws Exception {
        final Connection fetchedConnection = connectionHolder.reserve();

        // act / when
        fetchedConnection.close();

        // assert / then
        assertTrue("Is closed?", fetchedConnection.isClosed());
        Mockito.verify(this.connectionHolder).release(this.connection);
        Mockito.verifyZeroInteractions(this.connection);
    }

    @Test
    public void fetchTwiceButWithClose() throws Exception {
        final Connection fetchedConnection = connectionHolder.reserve();

        // act / when
        fetchedConnection.close();
        final Connection fetchedConnection2 = connectionHolder.reserve();

        // assert / then
        assertThat("Fetched connection?", fetchedConnection2, Matchers.notNullValue());
        assertThat("connection type?", fetchedConnection2, Matchers.instanceOf(ReservedConnection.class));
    }

    @Test
    public void delegation() throws Exception {
        // arrange / given
        final Connection fetchedConnection = connectionHolder.reserve();

        // act / when
        fetchedConnection.createStatement();

        // assert / then
        Mockito.verify(this.connection).createStatement();
        Mockito.verifyZeroInteractions(this.connection);
    }

    @Test
    public void dispose() throws Exception {
        // act / when
        connectionHolder.dispose();

        // assert / then
        Mockito.verify(this.connection).close();
    }
}