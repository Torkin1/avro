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

import java.util.Arrays;

import org.junit.Test;

public class ArraysUtilTest {

  @Test
  public void testMax() {
    final int[] array = new int[] { 1, 2, 3, 2 };
    assert 3 == ArraysUtil.max(array, array.length);
  }

  @Test
  public void testSort() {
    final int[] array = new int[] { 2, 3, 1 };
    final int[] expected = new int[] { 3, 2, 1 };
    ArraysUtil.sort(array, array.length);
    assert Arrays.equals(expected, array);
  }

}
