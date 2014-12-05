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

import org.failearly.dataset.NoDataSet;
import org.failearly.dataset.config.DataSetConstants;
import org.failearly.dataset.datastore.DataStore;
import org.failearly.dataset.datastore.DataStores;
import org.failearly.dataset.datastore.internal.SqlDataStoreDriverManager;
import org.failearly.dataset.junit4.AbstractDataSetBaseTest;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * SqlDataStoreTest contains tests for SqlDataStore.
 */
@SqlDataStore
public class SqlDataStoreTest extends AbstractDataSetBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        // DataSetProperties.reload();
    }

    @Test @NoDataSet
    public void defaultSqlDataStore() throws Exception {
        final DataStore dataStore = DataStores.getDefaultDataStore(this.getClass());
        assertThat("Associated DataStore type?", dataStore, is(instanceOf(SqlDataStoreDriverManager.class)));
        assertThat("DataStore ID?", dataStore.getId(), is(DataSetConstants.DATASET_DEFAULT_DATASTORE_ID));
        assertThat("DataStore config?", dataStore.getConfig(), is("/sql-datastore.properties"));
        assertThat("DataStore setup suffix?", dataStore.getSetupSuffix(), is("setup.sql"));
        assertThat("DataStore cleanup suffix?", dataStore.getCleanupSuffix(), is("cleanup.sql"));
    }
}
