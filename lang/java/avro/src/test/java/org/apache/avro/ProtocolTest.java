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
public class ProtocolTest {

  /** protocol sample JSON description */
  private static final String PROTOCOL_HELLO_WORLD = "{\n" + "  \"namespace\": \"com.acme\",\n"
      + "  \"protocol\": \"HelloWorld\",\n" + "  \"doc\": \"Protocol Greetings\",\n" + "\n" + "  \"types\": [\n"
      + "    {\"name\": \"Greeting\", \"type\": \"record\", \"fields\": [\n"
      + "      {\"name\": \"message\", \"type\": \"string\"}]},\n"
      + "    {\"name\": \"Curse\", \"type\": \"error\", \"fields\": [\n"
      + "      {\"name\": \"message\", \"type\": \"string\"}]}\n" + "  ],\n" + "\n" + "  \"messages\": {\n"
      + "    \"hello\": {\n" + "      \"doc\": \"Say hello.\",\n"
      + "      \"request\": [{\"name\": \"greeting\", \"type\": \"Greeting\" }],\n"
      + "      \"response\": \"Greeting\",\n" + "      \"errors\": [\"Curse\"]\n" + "    }\n" + "  }\n" + "}";
  // +1 def-use, +1 LOC
  private static final String PROTOCOL_PROP_UNRESERVED = "{\n" + "  \"protocol\": \"unreservedProp\",\n"
      + "  \"myProp\": \"myValue\"\n" + "}";
  private String value;
  private Class<? extends Exception> expectedException;

  public ProtocolTest(Class<? extends Exception> expectedException, String value) {
    this.value = value;
    this.expectedException = expectedException;
    configure();
  }

  private void configure() {

    // no configuration required yet ...
  }

  @Parameterized.Parameters(name = "{0}, {1}")
  public static Collection<Object[]> getParams() {
    return Arrays.asList(new Object[][] { { Exception.class, null }, { Exception.class, "" },
        { null, PROTOCOL_HELLO_WORLD }, { null, PROTOCOL_PROP_UNRESERVED } });
  }

  @Test
  public void parseTest() {

    try {
      assertNotNull(Protocol.parse(value));
    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).log(Level.INFO, e.getMessage(), e);
      assertTrue(expectedException.isAssignableFrom(e.getClass()));
    }
  }
}
