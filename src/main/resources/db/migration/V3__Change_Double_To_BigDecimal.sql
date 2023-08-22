-- First, create new columns with the desired data type
ALTER TABLE grades ADD communication_bigdecimal DECIMAL(4,2);
ALTER TABLE grades ADD collaboration_bigdecimal DECIMAL(4,2);
ALTER TABLE grades ADD autonomy_bigdecimal DECIMAL(4,2);
ALTER TABLE grades ADD quiz_bigdecimal DECIMAL(4,2);
ALTER TABLE grades ADD individual_challenge_bigdecimal DECIMAL(4,2);
ALTER TABLE grades ADD squad_challenge_bigdecimal DECIMAL(4,2);
ALTER TABLE grades ADD final_grade_bigdecimal DECIMAL(4,2);

-- Update the new columns with the values from the old columns
UPDATE grades SET communication_bigdecimal = communication;
UPDATE grades SET collaboration_bigdecimal = collaboration;
UPDATE grades SET autonomy_bigdecimal = autonomy;
UPDATE grades SET quiz_bigdecimal = quiz;
UPDATE grades SET individual_challenge_bigdecimal = individual_challenge;
UPDATE grades SET squad_challenge_bigdecimal = squad_challenge;
UPDATE grades SET final_grade_bigdecimal = final_grade;

-- Drop the old column
ALTER TABLE grades DROP COLUMN communication;
ALTER TABLE grades DROP COLUMN collaboration;
ALTER TABLE grades DROP COLUMN autonomy;
ALTER TABLE grades DROP COLUMN quiz;
ALTER TABLE grades DROP COLUMN individual_challenge;
ALTER TABLE grades DROP COLUMN squad_challenge;
ALTER TABLE grades DROP COLUMN final_grade;

-- Rename the new columns to match the original columns name
ALTER TABLE grades RENAME COLUMN communication_bigdecimal TO communication;
ALTER TABLE grades RENAME COLUMN collaboration_bigdecimal TO collaboration;
ALTER TABLE grades RENAME COLUMN autonomy_bigdecimal TO autonomy;
ALTER TABLE grades RENAME COLUMN quiz_bigdecimal TO quiz;
ALTER TABLE grades RENAME COLUMN individual_challenge_bigdecimal TO individual_challenge;
ALTER TABLE grades RENAME COLUMN squad_challenge_bigdecimal TO squad_challenge;
ALTER TABLE grades RENAME COLUMN final_grade_bigdecimal TO final_grade;