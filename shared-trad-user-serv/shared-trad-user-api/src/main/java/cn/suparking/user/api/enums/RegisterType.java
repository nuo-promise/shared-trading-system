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
 * User Register Type.
 */
@Getter
@AllArgsConstructor
public enum RegisterType {

    UNKNOWN("unknown", "0"),
    IPHONE("Iphone", "1"),
    WECHAT_MINI("Mini", "2"),
    ALI_MINI("Ali", "3"),
    OTHER("Other", "4");


    private final String description;

    private final String code;

    /**
     * get desc by code.
     * @param code  user register code
     * @return user register desc
     */
    public static RegisterType convert(final String code) {
        return Stream.of(values())
                .filter(registerType -> registerType.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
