#!/usr/bin/env sh

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# $1: path to compiled test classes to instrument
# $2: where to save report

MVN_LOCAL_REPO=$(mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout)
BADUA_CLI_PATH="$MVN_LOCAL_REPO/br/usp/each/saeg/ba-dua-cli/0.6.0/ba-dua-cli-0.6.0-all.jar"

java -jar "$BADUA_CLI_PATH" report -classes "$1" -xml "$2" -input target/badua.ser -show-classes -show-methods