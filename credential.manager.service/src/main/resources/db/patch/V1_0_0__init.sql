CREATE TABLE IF NOT EXISTS user_password_credential (
  user_password_credential_id uuid,
  user_id uuid NOT NULL,
  password VARCHAR(50) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  credential_name VARCHAR(100) NOT NULL,
  creation_time timestamp without time zone NOT NULL,
  PRIMARY KEY ( user_password_credential_id ),
  CONSTRAINT user_name_unique UNIQUE (user_name)
);
COMMENT ON TABLE user_password_credential IS 'information for credential of user and password type';
COMMENT ON COLUMN user_password_credential.user_password_credential_id IS 'primary key, identify every record on the table';
COMMENT ON COLUMN user_password_credential.password IS 'Passphrase for the credential';
COMMENT ON COLUMN user_password_credential.user_name IS 'User name for the credential';
COMMENT ON COLUMN user_password_credential.credential_name IS 'name to identify the credential';
COMMENT ON COLUMN user_password_credential.user_id IS 'user id owner of credential';