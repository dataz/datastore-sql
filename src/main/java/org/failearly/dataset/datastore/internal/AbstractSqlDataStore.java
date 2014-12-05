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
import org.failearly.dataset.datastore.support.SimpleFileTransactionalSupportDataStoreBase;
import org.failearly.dataset.simplefile.SimpleFileStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * AbstractSqlDataStore is responsible for ...
 */
public abstract class AbstractSqlDataStore extends SimpleFileTransactionalSupportDataStoreBase<Statement> {

    protected AbstractSqlDataStore(String dataStoreId, String dataStoreConfig) {
        super(dataStoreId, dataStoreConfig);
    }

    protected AbstractSqlDataStore() {
    }

    private static Connection getConnection(Statement statement) {
        return with.producer("Get connection from statement", statement::getConnection);
    }

    /**
     * Create/fetch a DB connection.
     * @return a connection.
     */
    protected abstract Connection getConnection();

    @Override
    protected Statement startTransaction(DataSetResource dataSetResource, boolean useTransaction) throws SQLException {
        final Connection connection = getConnection();
        connection.setAutoCommit(!useTransaction);
        return connection.createStatement();
    }

    @Override
    protected void doExecuteStatement(Statement sqlStatement, SimpleFileStatement simpleFileStatement) throws SQLException {
        sqlStatement.executeUpdate(simpleFileStatement.getContent());
    }

    @Override
    protected void commitTransaction(Statement sqlStatement) throws SQLException {
        sqlStatement.getConnection().commit();
    }

    @Override
    protected void rollbackTransaction(Statement sqlStatement) throws SQLException {
        sqlStatement.getConnection().rollback();
    }

    @Override
    protected void closeTransaction(Statement sqlStatement) throws Exception {
        final Connection connection=getConnection(sqlStatement);
        try {
            connection.close();
            sqlStatement.close();
        } finally {
            assert connection.isClosed() : "Connection must be closed.";
            assert sqlStatement.isClosed() : "Statement must be closed.";
        }
    }
}
