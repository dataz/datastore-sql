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

import org.failearly.dataz.DataSet;
import org.failearly.dataz.NoDataSet;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.sql.internal.SqlDataStoreImplementation;
import org.failearly.dataz.template.generator.Limit;
import org.failearly.dataz.template.generator.ListGenerator;
import org.failearly.dataz.template.generator.RangeGenerator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static org.failearly.dataz.datastore.sql.SqlConfigProperties.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * SqlDataStoreTest contains tests for SqlDataStore.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SqlDataStoreTest.Config.class)

// dataSet annotations
@Ignore("New tests for SqlDataStore")
// TODO: New tests for SqlDataStore (use NamedDataStore)
public class SqlDataStoreTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeClass
    @AfterClass
    public static void resetDataStores() throws Exception {
    }

    @Test
    @NoDataSet
    public void defaultSqlDataStore() throws Exception {

        final DataStore dataStore = Mockito.mock(DataStore.class); // DataStores.getDefaultNamedDataStore(this.getClass());
        assertThat("Associated DataStore type?", dataStore, is(instanceOf(SqlDataStoreImplementation.class)));
        assertThat("DataStore Name?", dataStore.getName(), is(Constants.DATAZ_DEFAULT_DATASTORE_NAME));
        assertThat("DataStore config?", dataStore.getConfigFile(), is("/sql-datastore.properties"));
    }

    @Test
    @DataSet
    public void useDataSetDefaultResources() throws Exception {
        // assert / then
        int numUsers = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "PUBLIC.USERS");
        assertThat("#User(s)?", numUsers, is(1));
    }

    @Test
    @DataSet(setup = "SqlDataStoreTest-testAlias.setup.sql.vm", cleanup = "SqlDataStoreTest-testAlias.cleanup.sql")
    @RangeGenerator(name = "id", limit = Limit.UNLIMITED, from= 1)
    @ListGenerator(name = "user", values = {"Marko", "Loddar", "Frodo"})
    @ListGenerator(name = "alias", values = {"Kurt", "Bodo", "Bilbo"})
    public void useDataSetWithSettings() throws Exception {
        // assert / then
        int numUsers = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "PUBLIC.USERS");
        int numAliases = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "PUBLIC.ALIASES");

        assertThat("#Users?", numUsers, is(3));
        assertThat("#Aliases?", numAliases, is(9));
    }

    @Configuration
    @Lazy
    @PropertySource(value = {"classpath:/sql-datastore.properties"})
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    public static class Config {
        @Autowired
        private Environment env;

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean
        public DataSource dataSource() {
            final DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(env.getProperty(DATASTORE_SQL_DRIVER));
            dataSource.setUrl(env.getProperty(DATASTORE_SQL_URL));
            dataSource.setUsername(env.getProperty(DATASTORE_SQL_USER));
            dataSource.setPassword(env.getProperty(DATASTORE_SQL_PASSWORD));
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
}
