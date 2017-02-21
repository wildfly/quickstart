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

BEGIN
INSERT INTO User (ID, USERNAME) VALUES (1, 'jdoe');
INSERT INTO User (ID, USERNAME) VALUES (2, 'emuster');
COMMIT

BEGIN
INSERT INTO Task (ID, OWNER_ID, TITLE) VALUES (1, 1, 'johns first task');
INSERT INTO Task (ID, OWNER_ID, TITLE) VALUES (2, 1, 'johns second task');
COMMIT