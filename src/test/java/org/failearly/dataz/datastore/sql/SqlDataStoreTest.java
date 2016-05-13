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

package org.failearly.dataz.datastore.sql;

import org.failearly.dataz.*;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStores;
import org.failearly.dataz.datastore.sql.internal.AbstractSqlDataStore;
import org.failearly.dataz.datastore.sql.internal.SqlDataStoreDriverManager;
import org.failearly.dataz.template.generator.Limit;
import org.failearly.dataz.template.generator.ListGenerator;
import org.failearly.dataz.template.generator.RangeGenerator;
import org.failearly.dataz.junit4.AbstractDataSetTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@SqlDataStore
@DataStoreSetup(setup = "H2-Test-DB-schema.sql", failOnError = false)
public class SqlDataStoreTest extends AbstractDataSetTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeClass
    @AfterClass
    public static void resetDataStores() throws Exception {
        DataStores.reset();
    }

    @Test
    @SuppressDataSet
    public void defaultSqlDataStore() throws Exception {
        final DataStore dataStore = DataStores.getDefaultDataStore(this.getClass());
        assertThat("Associated DataStore type?", dataStore, is(instanceOf(SqlDataStoreDriverManager.class)));
        assertThat("DataStore ID?", dataStore.getId(), is(Constants.DATAZ_DEFAULT_DATASTORE_ID));
        assertThat("DataStore config?", dataStore.getConfigFile(), is("/sql-datastore.properties"));
        assertThat("DataStore setup suffix?", dataStore.getSetupSuffix(), is("setup.sql"));
        assertThat("DataStore cleanup suffix?", dataStore.getCleanupSuffix(), is("cleanup.sql"));
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
            dataSource.setDriverClassName(env.getProperty(AbstractSqlDataStore.DATASTORE_SQL_DRIVER));
            dataSource.setUrl(env.getProperty(AbstractSqlDataStore.DATASTORE_SQL_URL));
            dataSource.setUsername(env.getProperty(AbstractSqlDataStore.DATASTORE_SQL_USER));
            dataSource.setPassword(env.getProperty(AbstractSqlDataStore.DATASTORE_SQL_PASSWORD));
            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
}
