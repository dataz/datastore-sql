/*
 * Copyright (c) 2009.
 *
 * Date: 20.05.16
 * 
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
