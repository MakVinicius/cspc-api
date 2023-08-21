DECLARE @rowCount INT;

-- Check if the table contains data
SELECT @rowCount = COUNT(*) FROM roles;

-- If the table is empty, insert data
IF @rowCount = 0
BEGIN
    INSERT INTO roles (name) VALUES ("ROLE_ADMIN");
    INSERT INTO roles (name) VALUES ("ROLE_STUDENT");
    INSERT INTO roles (name) VALUES ("ROLE_COORDINATOR");
    INSERT INTO roles (name) VALUES ("ROLE_SCRUM_MASTER");
    INSERT INTO roles (name) VALUES ("ROLE_INSTRUCTOR");
    PRINT 'Data inserted successfully.';
END
ELSE
BEGIN
    PRINT 'Table already contains data, skipping insert.';
END