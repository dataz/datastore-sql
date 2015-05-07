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

import org.failearly.dataset.config.Constants;
import org.failearly.dataset.datastore.DataStoreFactoryDefinition;
import org.failearly.dataset.datastore.sql.internal.SqlDataStoreFactory;

import java.lang.annotation.*;

/**
 * SqlDataStore is responsible for ...
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SqlDataStore.SqlDataStores.class)
@DataStoreFactoryDefinition(dataStoreFactory = SqlDataStoreFactory.class)
public @interface SqlDataStore {
    /**
     * If your tests uses multiple data stores, you must identify each data store.
     *
     * @return the (unique) data store id.
     */
    String id() default Constants.DATASET_DEFAULT_DATASTORE_ID;

    /**
     * The datastore configuration file will be used by the actually DataStore Implementation. So what's inside these configuration property file depends
     * on the DataStore type. If you use multiple DataStoreDefinition's
     *
     * @return the datastore configuration file(name).
     */
    String config() default "/sql-datastore.properties";

    /**
     * Default suffix for setup resource files.
     *
     * @return suffix to be used for {@link org.failearly.dataset.DataSet#setup()} (if no setup resource is specified).
     *
     * @see org.failearly.dataset.DataStoreDefinition#setupSuffix()
     */
    String setupSuffix() default "setup.sql";

    /**
     * Default suffix for cleanup resource files.
     *
     * @return suffix to be used for {@link org.failearly.dataset.DataSet#cleanup()} (if no cleanup resource is specified).
     *
     * @see org.failearly.dataset.DataStoreDefinition#cleanupSuffix()
     */
    String cleanupSuffix() default "cleanup.sql";

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
