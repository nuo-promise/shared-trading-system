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

package cn.suparking.user.dao.entity;

import cn.suparking.user.api.enums.RegisterType;
import cn.suparking.user.api.enums.UserStatus;
import cn.suparking.user.dao.converter.RegisterTypeConverter;
import cn.suparking.user.dao.converter.UserStatusConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "spk_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "iphone", nullable = false)
    private String iphone;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "enabled", nullable = false)
    @Convert(converter = UserStatusConverter.class)
    private UserStatus enabled;

    @Column(name = "register_type", nullable = false)
    @Convert(converter = RegisterTypeConverter.class)
    private RegisterType registerType;

    @Column(name = "merchant_id", nullable = true)
    private String merchantId;

    @CreatedDate
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "date_updated", nullable = false)
    private Timestamp dateUpdated;
}
