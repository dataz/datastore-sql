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

import org.failearly.dataz.common.Property;
import org.failearly.dataz.config.Constants;
import org.failearly.dataz.datastore.DataStoreFactory;
import org.failearly.dataz.datastore.sql.internal.SqlDataStoreFactory;

import java.lang.annotation.*;

/**
 * SqlDataStore is responsible for ...
 */
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
