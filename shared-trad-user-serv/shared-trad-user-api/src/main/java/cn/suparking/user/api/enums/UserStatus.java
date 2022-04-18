/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.suparking.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * User Status Enum.
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    UNKNOWN("unknown", "0"),
    ACTIVE("Active", "1"),
    NO_ACTIVE("No_Active", "2");

    private final String description;

    private final String code;

    /**
     * get code desc.
     * @param code  user status code
     * @return user desc
     */
    public static UserStatus convert(final String code) {
        return Stream.of(values())
                .filter(userStatus -> userStatus.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
