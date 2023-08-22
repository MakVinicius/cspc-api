-- Create Roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

-- Insert roles
INSERT INTO roles (name) VALUES ("ROLE_ADMIN");
INSERT INTO roles (name) VALUES ("ROLE_STUDENT");
INSERT INTO roles (name) VALUES ("ROLE_COORDINATOR");
INSERT INTO roles (name) VALUES ("ROLE_SCRUM_MASTER");
INSERT INTO roles (name) VALUES ("ROLE_INSTRUCTOR");

-- Create Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);

-- Create Users_Roles table
CREATE TABLE users_roles (
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE users_roles ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE users_roles ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

-- Create Coordinators table
CREATE TABLE coordinators (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NULL,
    CONSTRAINT pk_coordinators PRIMARY KEY (id)
);

ALTER TABLE coordinators ADD CONSTRAINT FK_COORDINATORS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- Create Classrooms table
CREATE TABLE classrooms (
    id BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(100) NOT NULL,
    coordinator_id BIGINT NOT NULL,
    CONSTRAINT pk_classrooms PRIMARY KEY (id)
);

ALTER TABLE classrooms ADD CONSTRAINT FK_CLASSROOMS_ON_COORDINATOR FOREIGN KEY (coordinator_id) REFERENCES coordinators (id);

-- Create Squads table
CREATE TABLE squads (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    classroom_id BIGINT NULL,
    CONSTRAINT pk_squads PRIMARY KEY (id)
);

ALTER TABLE squads ADD CONSTRAINT FK_SQUADS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);

-- Create Grades table
CREATE TABLE grades (
    id BIGINT AUTO_INCREMENT NOT NULL,
    communication DOUBLE NOT NULL,
    collaboration DOUBLE NOT NULL,
    autonomy DOUBLE NOT NULL,
    quiz DOUBLE NOT NULL,
    individual_challenge DOUBLE NOT NULL,
    squad_challenge DOUBLE NOT NULL,
    final_grade DOUBLE NOT NULL,
    CONSTRAINT pk_grades PRIMARY KEY (id)
);

-- Create Students table
CREATE TABLE students (
    id BIGINT AUTO_INCREMENT NOT NULL,
    grades_id BIGINT NULL,
    user_id BIGINT NOT NULL,
    squad_id BIGINT NULL,
    classroom_id BIGINT NULL,
    CONSTRAINT pk_students PRIMARY KEY (id)
);

ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);
ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_GRADES FOREIGN KEY (grades_id) REFERENCES grades (id);
ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_SQUAD FOREIGN KEY (squad_id) REFERENCES squads (id);
ALTER TABLE students ADD CONSTRAINT FK_STUDENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- Create Instructors table
CREATE TABLE instructors (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    classroom_id BIGINT NULL,
    CONSTRAINT pk_instructors PRIMARY KEY (id)
);

ALTER TABLE instructors ADD CONSTRAINT FK_INSTRUCTORS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);
ALTER TABLE instructors ADD CONSTRAINT FK_INSTRUCTORS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- Create Scrum Masters table
CREATE TABLE scrum_masters (
    id BIGINT AUTO_INCREMENT NOT NULL,
    user_id BIGINT NOT NULL,
    classroom_id BIGINT NULL,
    CONSTRAINT pk_scrum_masters PRIMARY KEY (id)
);

ALTER TABLE scrum_masters ADD CONSTRAINT FK_SCRUM_MASTERS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classrooms (id);
ALTER TABLE scrum_masters ADD CONSTRAINT FK_SCRUM_MASTERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);