-- Inserción en la tabla Task
INSERT INTO task (id, disabled, title, description) VALUES (1, FALSE, 'Catering Setup', 'Setting up the catering equipment for the event');
INSERT INTO task (id, disabled, title, description) VALUES (2, FALSE, 'Menu Planning', 'Planning the menu for the upcoming event');
INSERT INTO task (id, disabled, title, description) VALUES (3, FALSE, 'Ingredient Sourcing', 'Sourcing fresh ingredients from local markets');
INSERT INTO task (id, disabled, title, description) VALUES (4, FALSE, 'Table Arrangement', 'Arranging the tables and chairs for the dining area');
INSERT INTO task (id, disabled, title, description) VALUES (5, FALSE, 'Food Preparation', 'Preparing the dishes as per the planned menu');
INSERT INTO task (id, disabled, title, description) VALUES (6, FALSE, 'Staff Coordination', 'Coordinating with the staff for the event');
INSERT INTO task (id, disabled, title, description) VALUES (7, FALSE, 'Decoration Setup', 'Setting up decorations in the dining area');
INSERT INTO task (id, disabled, title, description) VALUES (8, FALSE, 'Guest Reception', 'Welcoming guests and guiding them to their tables');
INSERT INTO task (id, disabled, title, description) VALUES (9, FALSE, 'Beverage Service', 'Serving beverages to the guests');
INSERT INTO task (id, disabled, title, description) VALUES (10, FALSE, 'Event Cleanup', 'Cleaning up after the event');

-- Inserción en la tabla State
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (1, false, '2024-09-01T08:00:00', '2024-09-01T10:00:00', 'TO_DO', 1);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (2, false, '2024-09-01T10:00:00', '2024-09-01T12:00:00', 'IN_PROGRESS', 1);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (3, false, '2024-09-01T12:00:00', '2024-09-01T14:00:00', 'DONE', 1);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (4, false, '2024-09-02T09:00:00', '2024-09-02T11:00:00', 'TO_DO', 2);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (5, false, '2024-09-02T11:00:00', '2024-09-02T13:00:00', 'IN_PROGRESS', 2);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (6, false, '2024-09-02T13:00:00', '2024-09-02T15:00:00', 'DONE', 2);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (7, false, '2024-09-03T08:30:00', '2024-09-03T10:30:00', 'TO_DO', 3);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (8, false, '2024-09-03T10:30:00', '2024-09-03T12:30:00', 'IN_PROGRESS', 3);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (10, false, '2024-09-04T09:15:00', '2024-09-04T11:15:00', 'TO_DO', 4);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (11, false, '2024-09-04T11:15:00', '2024-09-04T13:15:00', 'IN_PROGRESS', 4);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (12, false, '2024-09-04T13:15:00', '2024-09-04T15:15:00', 'DONE', 4);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (13, false, '2024-09-05T08:45:00', '2024-09-05T10:45:00', 'TO_DO', 5);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (14, false, '2024-09-05T10:45:00', '2024-09-05T12:45:00', 'IN_PROGRESS', 5);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (15, false, '2024-09-05T12:45:00', '2024-09-05T14:45:00', 'DONE', 5);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (16, false, '2024-09-06T09:00:00', '2024-09-06T11:00:00', 'TO_DO', 6);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (19, false, '2024-09-07T08:30:00', '2024-09-07T10:30:00', 'TO_DO', 7);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (20, false, '2024-09-07T10:30:00', '2024-09-07T12:30:00', 'IN_PROGRESS', 7);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (21, false, '2024-09-07T12:30:00', '2024-09-07T14:30:00', 'DONE', 7);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (22, false, '2024-09-08T09:15:00', '2024-09-08T11:15:00', 'TO_DO', 8);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (23, false, '2024-09-08T11:15:00', '2024-09-08T13:15:00', 'IN_PROGRESS', 8);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (24, false, '2024-09-08T13:15:00', '2024-09-08T15:15:00', 'DONE', 8);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (25, false, '2024-09-09T08:45:00', '2024-09-09T10:45:00', 'TO_DO', 9);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (26, false, '2024-09-09T10:45:00', '2024-09-09T12:45:00', 'IN_PROGRESS', 9);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (27, false, '2024-09-09T12:45:00', '2024-09-09T14:45:00', 'DONE', 9);
INSERT INTO state (id, disabled, date_from, date_to, state, task_id) VALUES (28, false, '2024-09-10T09:00:00', '2024-09-10T11:00:00', 'TO_DO', 10);

-- Reseteo de secuencia
ALTER SEQUENCE task_sequence RESTART WITH 100;
