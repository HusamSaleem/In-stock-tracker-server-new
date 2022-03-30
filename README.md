# Server for the In-Stock Tracker

# More information about this project
- https://github.com/HusamSaleem/in_stock_flutterApp/blob/main/README.md

# REST API
- Documentation for my api

# Registration (/api/v1/user/register)
**You send:** registration info via request body in JSON. **You get:** a status code ```200``` for successful response

Example

Request: 
```
{
    "password" : "password",
    "email": "email@domain.com"
}
```

Successful Response: ```200 ok```

Failed Response (Duplicate emails):
```
{
    "message": "email is taken",
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "2022-03-30T22:49:55.6772545Z"
}
```

# Login (/api/v1/user/login)
**You send:** login credentials via request body in JSON. **You get:** registered user with a ```uniqueIdentifier```

Example

Request:
```
{
    "password" : "password",
    "email": "email@domain.com"
}
```

Successful Response:
```
{
    "id": 1,
    "email": "email@domain.com",
    "password": "$2a$10$hdnwUETfo4KTDFxs7TCCm.D9E367rrZRNJ/6Art3cLw45NPK1m.SS",
    "notifyByEmail": false,
    "uniqueIdentifier": "6e9cc4a1-fef4-42b7-92f3-7623003ec1b1"
}
```

Failed Response (Bad password):
```
{
    "message": "invalid password",
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "2022-03-30T22:54:04.9167182Z"
}
```

# Get Item from website

# Get Item (/api/v1/item?itemId=<itemId>&website=<website>)
**You send:** item id of the product and website name as query params. **You get** the item's info

Example

    
