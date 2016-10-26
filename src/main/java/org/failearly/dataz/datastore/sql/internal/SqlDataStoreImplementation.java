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

package org.failearly.dataz.datastore.sql.internal;

import org.failearly.common.classutils.ClassLoader;
import org.failearly.common.proputils.PropertiesAccessor;
import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStoreBase;
import org.failearly.dataz.datastore.DataStoreException;
import org.failearly.dataz.datastore.sql.SqlConfigProperties;
import org.failearly.dataz.datastore.sql.SqlDataStore;
import org.failearly.dataz.datastore.support.simplefile.SimpleFileParser;
import org.failearly.dataz.datastore.support.simplefile.SimpleFileStatement;
import org.failearly.dataz.datastore.support.simplefile.StatementProcessor;
import org.failearly.dataz.datastore.support.transaction.*;
import org.failearly.dataz.internal.util.With;
import org.failearly.dataz.resource.DataResource;

import java.sql.*;

import static org.failearly.dataz.datastore.support.transaction.TransactionalSupportBuilder.Provider.USE_DATA_RESOURCE_PROVIDER;
import static org.failearly.dataz.datastore.support.transaction.TransactionalSupportBuilder.Provider.USE_STATEMENT_PROVIDER;

/**
 * A SQL DataStore Implementation based on {@link java.sql.DriverManager}.
 */
public final class SqlDataStoreImplementation extends DataStoreBase
    implements SqlConfigProperties {

    private static final String NO_PASSWORD = "";
    private String url;
    private Connection connection;

    private final TransactionalSupportBuilder<Statement> transactionalSupportBuilder;

    SqlDataStoreImplementation(Class<? extends NamedDataStore> namedDataStore, SqlDataStore annotation) {
        super(namedDataStore, annotation);
        transactionalSupportBuilder = TransactionalSupportBuilder.createBuilder(Statement.class)
                                .withPerDataResource(new SqlPerDataResourceProvider())
                                .withPerStatement(new SqlPerStatementProvider());
    }

    @Override
    protected void doEstablishConnection(PropertiesAccessor properties) {
        this.url = properties.getMandatoryProperty(DATASTORE_SQL_URL);
        this.connection=doCreateSqlConnection(properties);
    }

    @Override
    protected void doApplyResource(DataResource dataResource) throws DataStoreException {
        final TransactionalSupport<Statement> transactionalSupport = transactionalSupportBuilder
            .withProvider(chooseProvider(dataResource))
            .build();

        transactionalSupport.process(dataResource);
    }

    private TransactionalSupportBuilder.Provider chooseProvider(DataResource dataResource) {
        return dataResource.isFailOnError() ? USE_STATEMENT_PROVIDER : USE_DATA_RESOURCE_PROVIDER;
    }

    private Connection doCreateSqlConnection(PropertiesAccessor properties) {
        final String driverClass = properties.getMandatoryProperty(DATASTORE_SQL_DRIVER);
        ClassLoader.loadClass(Driver.class, driverClass);

        final String user = properties.getStringValue(DATASTORE_SQL_USER, NO_PASSWORD);
        final String password = properties.getStringValue(DATASTORE_SQL_PASSWORD, NO_PASSWORD);

        return with.producer("Get connection to url=" + url + ", user='" + user + "'", () -> DriverManager.getConnection(url, user, password));
    }



    protected void doCloseConnection() {
        With.ignore().action("Close actually connection", () -> {
            if( connection!=null ) {
                connection.close();
            }
        });
    }

    private abstract class SqlProviderBase {
        private final SimpleFileParser simpleFileParser = new SimpleFileParser();

        final void doProcess(Statement sqlStatement, DataResource dataResource) throws Exception {
            simpleFileParser.processStatements(dataResource, sqlStatement, new StatementProcessor<Statement>() {
                @Override
                public void process(SimpleFileStatement simpleFileStatement, Statement sqlStatement) throws Exception {
                    sqlStatement.executeUpdate(simpleFileStatement.getContent());
                }

                @Override
                public void commit(Statement sqlStatement) throws Exception {
                    doCommit(sqlStatement);
                }
            });
        }

        abstract void doCommit(Statement sqlStatement) throws SQLException;


        final void doCloseSqlStatement(Statement sqlStatement) throws SQLException {
            sqlStatement.close();
        }
    }

    private class SqlPerDataResourceProvider extends SqlProviderBase implements PerDataResourceProvider<Statement> {
        @Override
        public Statement createTransactionContext() throws Exception {
            connection.setAutoCommit(false);
            return connection.createStatement();
        }

        public void process(Statement sqlStatement, DataResource dataResource) throws Exception {
            doProcess(sqlStatement, dataResource);
        }

        @Override
        public void commit(Statement sqlStatement) throws Exception {
            doCommit(sqlStatement);
        }

        @Override
        public void rollback(Statement sqlStatement) throws Exception {
            sqlStatement.getConnection().rollback();
        }

        @Override
        public final void close(Statement sqlStatement, ProcessingState processingState) throws Exception {
            doCloseSqlStatement(sqlStatement);
        }

        void doCommit(Statement sqlStatement) throws SQLException {
            sqlStatement.getConnection().commit();
        }
    }

    private class SqlPerStatementProvider extends SqlProviderBase implements PerStatementProvider<Statement> {
        @Override
        public Statement createTransactionContext() throws Exception {
            connection.setAutoCommit(true);
            return connection.createStatement();
        }

        public void process(Statement sqlStatement, DataResource dataResource) throws Exception {
            doProcess(sqlStatement, dataResource);
        }

        @Override
        public final void close(Statement sqlStatement, ProcessingState processingState) throws Exception {
            doCloseSqlStatement(sqlStatement);
        }

        void doCommit(Statement sqlStatement) {
            throw new UnsupportedOperationException("Commit() must not be called");
        }
    }
}
