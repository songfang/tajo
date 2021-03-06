/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.tajo;

import org.apache.tajo.validation.Validator;

public enum QueryVars implements ConfigKey {
  COMMAND_TYPE,
  STAGING_DIR,
  OUTPUT_TABLE_NAME,
  OUTPUT_TABLE_PATH,
  OUTPUT_PARTITIONS,
  OUTPUT_OVERWRITE,
  OUTPUT_AS_DIRECTORY,
  OUTPUT_PER_FILE_SIZE,
  ;

  QueryVars() {
  }

  @Override
  public String keyname() {
    return name().toLowerCase();
  }

  @Override
  public ConfigType type() {
    return ConfigType.QUERY;
  }

  @Override
  public Class<?> valueClass() {
    return null;
  }

  @Override
  public Validator validator() {
    return null;
  }
}


