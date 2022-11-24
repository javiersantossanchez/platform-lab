
CREATE TABLE IF NOT EXISTS user_password_credential (
  user_password_credential_id uuid,
  password VARCHAR(50) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  credential_name VARCHAR(100) NOT NULL,
  creation_time timestamp without time zone NOT NULL,
  PRIMARY KEY ( user_password_credential_id ),
  CONSTRAINT user_name_unique UNIQUE (user_name)
);


CREATE TABLE IF NOT EXISTS user_password_credential_user (
  user_password_credential_id uuid NOT NULL,
  user_id uuid NOT NULL,
  PRIMARY KEY ( user_password_credential_id,user_id ),
  FOREIGN KEY ( user_password_credential_id ) REFERENCES user_password_credential (user_password_credential_id)
);