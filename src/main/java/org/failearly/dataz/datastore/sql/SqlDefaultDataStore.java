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
