--
-- JBoss, Home of Professional Open Source
-- Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE TABLE USERS (ID INT, USERNAME VARCHAR(20), PASSWORD VARCHAR(20));
CREATE TABLE ROLES (ID INT, NAME VARCHAR(20));
CREATE TABLE USERS_ROLES (USER_ID INT, ROLE_ID INT);

INSERT INTO USERS (ID, USERNAME, PASSWORD) VALUES (1, 'quickstartUser', 'quickstartPwd1!'); 
INSERT INTO USERS (ID, USERNAME, PASSWORD) VALUES (2, 'guest', 'guestPwd1!');

INSERT INTO ROLES (ID, NAME) VALUES (1, 'quickstarts');
INSERT INTO ROLES (ID, NAME) VALUES (2, 'guest');

INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (1,1);
INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (2,2);
