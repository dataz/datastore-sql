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

import org.failearly.dataset.DataStoreDefinition;
import org.failearly.dataset.DataStoreSetup;
import org.failearly.dataset.junit4.AbstractDataSetBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * SqlDataStoreTypeTest contains tests for ... .
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SqlDataStoreTest.Config.class)
@DataStoreDefinition(setupSuffix = "setup.sql", cleanupSuffix = "cleanup.sql")
@DataStoreSetup(setup = "H2-Test-DB-schema.sql", failOnError = false)
public class SqlDataStoreTypeTest extends AbstractDataSetBaseTest {

}