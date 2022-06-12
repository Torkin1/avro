/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.avro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SchemaTest {

  private String name;
  private String value;
  private boolean isPropertyAlreadyAdded;
  private Class<? extends Exception> expectedExceptionType;

  /** SUT */
  private Schema schema;

  public SchemaTest(Class<? extends Exception> expectedExceptionType, String name, String value,
      boolean isPropertyAlreadyAdded) {
    this.name = name;
    this.value = value;
    this.isPropertyAlreadyAdded = isPropertyAlreadyAdded;
    this.expectedExceptionType = expectedExceptionType;
    configure();
  }

  private void configure() {

    // creates a schema of a null type
    schema = Schema.create(Schema.Type.NULL);

    if (isPropertyAlreadyAdded) {
      schema.addProp(name, value + "_alreadyPresent");
    }
  }

  @Parameterized.Parameters(name = "{0}, {1}, {2}, {3}")
  public static Collection<Object[]> getParams() {
    return Arrays.asList(new Object[][] {
        // first iteration - boundary analysis
        { Exception.class, null, null, false, }, { null, "", "", false, }, { null, "a", "test", false, },
        { Exception.class, "a", "test", true, }, { Exception.class, "type", "test", false } // +1 def-use, +1 LOC
    });
  }

  @Test
  public void addPropTest() {
    String actualValue;

    try {
      // do the test
      schema.addProp(name, value);
      // assert that the value inserted is the expected one, assuming that the
      // getProp() method is correct
      actualValue = schema.getProp(name);
      assertEquals(value, actualValue);
      assertNull(expectedExceptionType);
    } catch (Exception e) {
      // assert that an exception is expected
      Logger.getLogger(this.getClass().getName()).log(Level.INFO, e.getMessage(), e);
      assertTrue(expectedExceptionType.isAssignableFrom(e.getClass()));
    }
  }
}
