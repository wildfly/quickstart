/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.quickstarts.jaxrsjwt.user;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;

@Stateless
public class UserService {

    private static final Map<String, User> USER_DB = new HashMap<String, User>() {{
        put("customer", new User("customer", "customerpw", Roles.CUSTOMER));
        put("admin", new User("admin", "adminpw", Roles.ADMIN));
    }};

    public User authenticate(final String username, final String password) throws Exception {
        User user = USER_DB.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new Exception("Failed logging in org.jboss.user with name '" + username + "': unknown username or wrong password");
    }
}
