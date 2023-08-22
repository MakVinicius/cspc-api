-- Adding link for linkedin in user object
ALTER TABLE users ADD linkedin_link VARCHAR(255) NULL;
ALTER TABLE classrooms ADD progress DECIMAL(5, 2) NULL;