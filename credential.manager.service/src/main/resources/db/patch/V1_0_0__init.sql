
CREATE TABLE IF NOT EXISTS password_credential (
  password_credential uuid,
  password VARCHAR(50) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  credential_name VARCHAR(100) NOT NULL,
  PRIMARY KEY ( password_credential ),
  CONSTRAINT credential_name_unique UNIQUE (credential_name)
);





--CREATE TABLE IF NOT EXISTS credential_user (
--  credential_id uuid,
--  user_id uuid NOT NULL,
--  PRIMARY KEY ( credential_id,user_id ),
  --FOREIGN KEY ( interrogation_id )
--);