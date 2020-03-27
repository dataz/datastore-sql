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

package org.failearly.dataz.datastore.sql;

import org.failearly.dataz.NamedDataStore;
import org.failearly.dataz.common.Property;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.datastore.DataStoreFactory;
import org.failearly.dataz.datastore.sql.internal.SqlDataStoreFactory;

import java.lang.annotation.*;

/**
 * The Datastore annotation for SQL based datastores.
 *
 * Assign in to your {@link NamedDataStore} class. A sample usage could be seen at {@link SqlDefaultDataStore}.
 *
 * @see SqlDefaultDataStore
 */
@SuppressWarnings("unused")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SqlDataStore.SqlDataStores.class)
@DataStoreFactory.Definition(factory = SqlDataStoreFactory.class)
public @interface SqlDataStore {
    /**
     * If your tests uses multiple data stores, you must identify each data store.
     *
     * @return the (unique) data store id.
     */
    String name() default Constants.DATAZ_DEFAULT_DATASTORE_NAME;

    /**
     * The datastore (optional) configuration file will be used by the actually DataStore Implementation. So what's inside these configuration property file depends
     * on the DataStore type. Could be overwritten by {@link #properties()}.
     *
     * @return the datastore configuration file(name).
     *
     * @see #properties()
     */
    String config() default Constants.DATAZ_NO_CONFIG_FILE;

    /**
     * Optional properties or named arguments (key value pairs). Overwrites the {@link #config()}.
     *
     * @return an array of {@link Property}.
     *
     * @see #config()
     * @see SqlConfigProperties
     */
    Property[] properties() default {};


    /**
     * Containing Annotation Type.
     *
     * Remark: This will be used by Java8 compiler.
     *
     * @see java.lang.annotation.Repeatable
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface SqlDataStores {
        SqlDataStore[] value();
    }
}
