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

import org.failearly.dataz.DataCleanup;
import org.failearly.dataz.DataSet;
import org.failearly.dataz.DataSetup;
import org.failearly.dataz.NamedDataStore;

/**
 * SqlDefaultDataStore is the default {@link org.failearly.dataz.datastore.DataStore} used for SQL DataStores,
 * in case you do not provide your own default.
 * <br><br>
 * The default {@code DataStore} instance will be, if you don't set {@link DataSet#datastores()},
 * {@link DataSetup#datastores()} or {@link DataCleanup#datastores()}.
 * <br><br>
 * The default SQL datastores expects two files within your classpath root.
 * <br><br>
 * <ul>
 *    <li>{@value #CONFIG_FILE}: The SQL properties to be used to establish a connection.</li>
 *    <li>{@value #SCHEME_FILE}: The SQL file to create the appropriate scheme.</li>
 * </ul>
 *
 * @see SqlConfigProperties
 */
@SqlDataStore(config = SqlDefaultDataStore.CONFIG_FILE)
@DataSetup(name="scheme", value = SqlDefaultDataStore.SCHEME_FILE)
public final class SqlDefaultDataStore extends NamedDataStore {
    public static final String CONFIG_FILE="/sql-datastore.properties";
    public static final String SCHEME_FILE="/create-scheme.sql";
}
