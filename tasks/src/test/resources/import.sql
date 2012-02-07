BEGIN
INSERT INTO Tasks_user (ID, USERNAME) VALUES (-1, 'jdoe');
INSERT INTO Tasks_user (ID, USERNAME) VALUES (-2, 'emuster');
COMMIT

BEGIN
INSERT INTO Tasks_task (ID, OWNER_ID, TITLE) VALUES (-1, -1, 'johns first task');
INSERT INTO Tasks_task (ID, OWNER_ID, TITLE) VALUES (-2, -1, 'johns second task');
COMMIT