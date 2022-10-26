#!/bin/bash
set -e;

	"${mongo[@]}" <<-EOJS
	  use $MONGO_CREDENTIAL_SERVICE_DB
		db.createUser({
        user: '$MONGO_CREDENTIAL_SERVICE_USERNAME',
        pwd:  '$MONGO_CREDENTIAL_SERVICE_PASSWORD',
        roles: [{
          role: 'readWrite',
          db: '$MONGO_CREDENTIAL_SERVICE_DB'
        }]
    })
	EOJS