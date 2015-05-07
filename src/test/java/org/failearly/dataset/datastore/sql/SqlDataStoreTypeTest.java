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

import org.failearly.dataset.*;
import org.failearly.dataset.config.Constants;
import org.failearly.dataset.datastore.DataStore;
import org.failearly.dataset.datastore.DataStores;
import org.failearly.dataset.datastore.sql.internal.AbstractSqlDataStore;
import org.failearly.dataset.datastore.sql.internal.SqlDataStoreDriverManager;
import org.failearly.dataset.generator.Limit;
import org.failearly.dataset.generator.ListGenerator;
import org.failearly.dataset.generator.RangeGenerator;
import org.failearly.dataset.junit4.AbstractDataSetTest;
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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * SqlDataStoreTypeTest contains tests for ... .
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SqlDataStoreTypeTest.Config.class)
// dataSet annotations
@DataStoreDefinition(setupSuffix = "setup.sql", cleanupSuffix = "cleanup.sql")
@DataStoreSetup(setup = "H2-Test-DB-schema.sql", failOnError = false)
public class SqlDataStoreTypeTest extends AbstractDataSetTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeClass @AfterClass
    public static void resetDataStores() throws Exception {
        DataStores.reset();
    }

    @Test
    @NoDataSet
    public void defaultSqlDataStore() throws Exception {
        final DataStore dataStore = DataStores.getDefaultDataStore(this.getClass());
        assertThat("Associated DataStore type?", dataStore, is(instanceOf(SqlDataStoreDriverManager.class)));
        assertThat("DataStore ID?", dataStore.getId(), is(Constants.DATASET_DEFAULT_DATASTORE_ID));
        assertThat("DataStore config?", dataStore.getConfig(), is("/datastore.properties"));
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
    @RangeGenerator(name = "id", limit = Limit.UNLIMITED, start = 1)
    @ListGenerator(name = "user", values = {"Marko", "Loddar", "Frodo"})
    @ListGenerator(name = "alias", values = {"Kurt", "Bodo", "Bilbo"})
    public void testAlias() throws Exception {
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