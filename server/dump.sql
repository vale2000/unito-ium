BEGIN TRANSACTION;

-- ----------------------------
-- Table structure for `roles`
-- ----------------------------
CREATE TABLE IF NOT EXISTS roles (
  id   INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,

  lesson_add            INTEGER NOT NULL,
  lesson_update         INTEGER NOT NULL,
  lesson_delete         INTEGER NOT NULL,

  booking_get           INTEGER NOT NULL,
  booking_get_others    INTEGER NOT NULL,
  booking_add           INTEGER NOT NULL,
  booking_add_others    INTEGER NOT NULL,
  booking_list          INTEGER NOT NULL,
  booking_list_others   INTEGER NOT NULL,
  booking_update        INTEGER NOT NULL,
  booking_update_others INTEGER NOT NULL,
  booking_delete        INTEGER NOT NULL,
  booking_delete_others INTEGER NOT NULL,

  user_get              INTEGER NOT NULL,
  user_get_others       INTEGER NOT NULL,
  user_add              INTEGER NOT NULL,
  user_list             INTEGER NOT NULL,
  user_update           INTEGER NOT NULL,
  user_delete           INTEGER NOT NULL,

  course_add            INTEGER NOT NULL,
  course_update         INTEGER NOT NULL,
  course_delete         INTEGER NOT NULL
);

-- ----------------------------
-- Records of `roles`
-- ----------------------------
INSERT INTO roles (id, name, lesson_add, lesson_update, lesson_delete, booking_get, booking_get_others, booking_add, booking_add_others, booking_list, booking_list_others, booking_update, booking_update_others, booking_delete, booking_delete_others, user_get, user_get_others, user_add, user_list, user_update, user_delete, course_add, course_update, course_delete)
VALUES
	(0, 'Deleted', 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
	(1, 'User', 0,0,0,1,0,1,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0),
	(2, 'Teacher', 0,0,0,1,0,1,0,1,0,1,0,0,0,1,1,0,0,0,0,0,0,0),
	(3, 'Admin', 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
CREATE TABLE IF NOT EXISTS users (
  id       INTEGER PRIMARY KEY,
  email    TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  role_id  INTEGER NOT NULL DEFAULT 1,
  name     TEXT,
  surname  TEXT,
  gender   INTEGER NOT NULL DEFAULT 0,
  FOREIGN KEY (role_id)
  	REFERENCES roles (id)
  		ON DELETE SET DEFAULT
  		ON UPDATE CASCADE
);

-- ----------------------------
-- Records of `users`
-- ----------------------------
INSERT INTO users (id, email, password, role_id, name, surname, gender)
VALUES
  (1, 'mario.rossi@email.com', '', 2, 'Mario', 'Rossi', 1),
  (2, 'monica.beneventi@email.com', '', 2, 'Monica', 'Beneventi', 2),
  (3, 'anna.panicucci@email.com', '', 2, 'Anna', 'Panicucci', 2),
  (4, 'saverio.bianchi@email.com', '', 2, 'Saverio', 'Bianchi', 1),
  (5, 'remo.moretti@email.com', '', 2, 'Remo', 'Moretti', 1),
  (6, 'tiziana.ferri@email.com', '', 2, 'Tiziana', 'Ferri', 2),
  (7, 'martina.colombo.@email.com', '', 2, 'Martina', 'Colombo', 2),
  (8, 'luigi.marchese@email.com', '', 2, 'Luigi', 'Marchese', 1),
  (9, 'elisabetta.ferrari@email.com', '', 2, 'Elisabetta', 'Ferrari', 2),
  (10, 'gustavo.costa@email.com', '', 2, 'Gustavo', 'Costa', 1),
  (11, 'giuseppe.eletto@edu.unito.it', 'password123', 1, 'Giuseppe', 'Eletto', 1),
  (12, 'admin@ium.unito.it', 'password123', 3, 'Admin', 'IUM', 0);

-- ----------------------------
-- Table structure for `courses`
-- ----------------------------
CREATE TABLE IF NOT EXISTS courses (
  id   INTEGER PRIMARY KEY,
  name TEXT NOT NULL UNIQUE
);

-- ----------------------------
-- Records of `courses`
-- ----------------------------
INSERT INTO courses (id, name)
VALUES
  (1, 'Italiano'),
  (2, 'Matematica'),
  (3, 'Geometria'),
  (4, 'Informatica'),
  (5, 'Storia'),
  (6, 'Geografia'),
  (7, 'Inglese'),
  (8, 'Latino'),
  (9, 'Greco'),
  (10, 'Scienze'),
  (11, 'Filosofia');

-- ----------------------------
-- Table structure for `teachers`
-- ----------------------------
CREATE TABLE IF NOT EXISTS teachers (
  user_id   INTEGER,
  course_id INTEGER,
  PRIMARY KEY (user_id, course_id),
  FOREIGN KEY (user_id)
    REFERENCES users (id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (course_id)
    REFERENCES courses (id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

-- ----------------------------
-- Records of `teachers`
-- ----------------------------
INSERT INTO teachers (user_id, course_id)
VALUES
  (1, 7),
  (1, 8),
  (1, 10),
  (2, 1),
  (2, 8),
  (2, 9),
  (3, 1),
  (3, 2),
  (4, 2),
  (4, 3),
  (5, 3),
  (5, 4),
  (6, 4),
  (6, 10),
  (7, 5),
  (7, 6),
  (8, 5),
  (8, 6),
  (9, 9),
  (9, 11),
  (10, 7),
  (10, 11);

-- ----------------------------
-- Table structure for `lessons`
-- ----------------------------
CREATE TABLE IF NOT EXISTS lessons (
  id         INTEGER PRIMARY KEY,
  course_id  INTEGER NOT NULL,
  unix_day   INTEGER NOT NULL,
  init_hour  INTEGER NOT NULL,
  UNIQUE (course_id, unix_day, init_hour),
  FOREIGN KEY (course_id)
    REFERENCES courses (id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

-- ----------------------------
-- Records of `teachers`
-- ----------------------------
INSERT INTO lessons (course_id, unix_day, init_hour)
VALUES
  (7, 0, 09),
  (8, 0, 16),
  (11, 0, 22);

-- ----------------------------
-- Table structure for `bookings`
-- ----------------------------
CREATE TABLE IF NOT EXISTS bookings (
  id         INTEGER PRIMARY KEY,
  user_id    INTEGER NOT NULL,
  teacher_id INTEGER NOT NULL,
  lesson_id  INTEGER NOT NULL,
  status     TEXT NOT NULL DEFAULT 'RESERVED',
  UNIQUE (user_id, lesson_id, teacher_id),
  FOREIGN KEY (user_id)
    REFERENCES users (id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (teacher_id)
    REFERENCES users (id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
  FOREIGN KEY (lesson_id)
    REFERENCES lessons (id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

-- ----------------------------
-- Records of `teachers`
-- ----------------------------
INSERT INTO bookings (user_id, teacher_id, lesson_id)
VALUES
  (11, 10, 1),
  (11, 2, 2),
  (11, 9, 3);

COMMIT TRANSACTION;