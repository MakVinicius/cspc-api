-- Create Roles table
CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);

-- Insert roles
INSERT INTO roles (name) VALUES ("ROLE_ADMIN");
INSERT INTO roles (name) VALUES ("ROLE_STUDENT");
INSERT INTO roles (name) VALUES ("ROLE_COORDINATOR");
INSERT INTO roles (name) VALUES ("ROLE_SCRUM_MASTER");
INSERT INTO roles (name) VALUES ("ROLE_INSTRUCTOR");


-- Create Users table
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(255)
);

-- Create Users_Roles table
CREATE TABLE users_roles(
    user_id BIGINT,
    role_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);


-- Create Coordinators table
CREATE TABLE coordinators (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- Create Classrooms table
CREATE TABLE classrooms (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    coordinator_id BIGINT,
    FOREIGN KEY (coordinator_id) REFERENCES coordinators(id)
);


-- Create Squads table
CREATE TABLE squads (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    classroom_id BIGINT,
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
);


-- Create Grades table
CREATE TABLE grades (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    communication DECIMAL(2, 2),
    collaboration DECIMAL(2, 2),
    autonomy DECIMAL(2, 2),
    quiz DECIMAL(2, 2),
    individualChallenge DECIMAL(2, 2),
    squadChallenge DECIMAL(2, 2),
    finalGrade DECIMAL(2, 2)
);


-- Create Students table
CREATE TABLE students (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    grade_id BIGINT,
    squad_id BIGINT,
    classroom_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (grade_id) REFERENCES grades(id),
    FOREIGN KEY (squad_id) REFERENCES squads(id),
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
);


-- Create Instructors table
CREATE TABLE instructors (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    classroom_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
);


-- Create Scrum Masters table
CREATE TABLE scrum_masters (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    classroom_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (classroom_id) REFERENCES classrooms(id)
);