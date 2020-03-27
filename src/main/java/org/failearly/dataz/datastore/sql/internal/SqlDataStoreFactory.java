/*
 * dataZ - Test Support For Data Stores.
 *
 * Copyright 2014-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.failearly.dataz.datastore.sql.internal;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.datastore.DataStore;
import org.failearly.dataz.datastore.DataStoreFactory;
import org.failearly.dataz.datastore.sql.SqlDataStore;

/**
 * SqlDataStoreFactory is responsible for ...
 */
public final class SqlDataStoreFactory implements DataStoreFactory<SqlDataStore> {
    @Override
    public DataStore createDataStore(Class<? extends NamedDataStore> namedDataStore, SqlDataStore dataStoreAnnotation) {
        return new SqlDataStoreImplementation(namedDataStore, dataStoreAnnotation);
    }
}
