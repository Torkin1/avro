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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ProtocolTest {

  private static class Oracle {

    private String expected;
    private String actual;

    public Oracle(String expected, String actual) {

      // Logger.getLogger(this.getClass().getName()).log(Level.INFO,
      // String.format("expected is %s\n actual is %s", expected, actual));
      this.expected = expected;
      this.actual = actual;
    }

    private void removeEmptyMapsAndLists(List<?> l) {
      l.removeIf(e -> {
        if (e instanceof List) {
          if (((List<?>) e).isEmpty()) {
            return true;
          }
          removeEmptyMapsAndLists((List<?>) e);
        }
        if (e instanceof Map) {
          if (((Map<?, ?>) e).isEmpty()) {
            return true;
          }
          removeEmptyMapsAndLists((Map<?, ?>) e);
        }
        return false;
      });
    }

    private void removeEmptyMapsAndLists(Map<?, ?> m) {
      Set<?> keys = new HashSet<>(m.keySet());
      keys.forEach(k -> {
        Object v = m.get(k);
        if (v instanceof List) {
          if (((List<?>) v).isEmpty()) {
            m.remove(k);
          } else {
            removeEmptyMapsAndLists((List<?>) v);
          }
        }
        if (v instanceof Map) {
          if (((Map<?, ?>) v).isEmpty()) {
            m.remove(k);
          } else {
            removeEmptyMapsAndLists((Map<?, ?>) v);
          }
        }
      });
    }

    private void removeNulls(Map<?, ?> m) {
      Set<?> keys = new HashSet<>(m.keySet());
      keys.forEach(k -> {
        if (m.get(k) == null) {
          m.remove(k);
        }
      });
    }

    public boolean askIfEqual() throws JsonProcessingException {
      ObjectMapper om = new ObjectMapper();
      Map<String, Object> mExpected = (Map<String, Object>) om.readValue(expected, Map.class);
      Map<String, Object> mActual = (Map<String, Object>) om.readValue(actual, Map.class);

      // if an actual prop is an empty list or map or is a null value, and that prop
      // isn't listed among expected ones, we exclude them from the comparison
      removeEmptyMapsAndLists(mActual);
      removeNulls(mActual);

      // Logger.getLogger(this.getClass().getName()).log(Level.INFO,
      // String.format("mExpected is %s\n mActual is %s", mExpected, mActual));
      return mExpected.equals(mActual);
    }
  }

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
  }

  @Parameterized.Parameters(name = "{0}, {1}")
  public static Collection<Object[]> getParams() {
    return Arrays.asList(new Object[][] { { Exception.class, null }, { Exception.class, "" },
        { null, PROTOCOL_HELLO_WORLD }, { null, PROTOCOL_PROP_UNRESERVED } });
  }

  @Test
  public void parseTest() throws Exception {

    try {
      Protocol protocol = Protocol.parse(value);
      assertNotNull(protocol);

      // assert that the returned Protocol instance reflects the one described in the
      // JSON input string (mutation coverage ++)
      assertTrue(new Oracle(value, protocol.toString()).askIfEqual());

    } catch (JsonProcessingException e) {
      // something went wrong when asking the oracle
      throw new Exception("oracle failed to give a response", e);
    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).log(Level.INFO, e.getMessage(), e);
      assertTrue(expectedException.isAssignableFrom(e.getClass()));
    }
  }
}
