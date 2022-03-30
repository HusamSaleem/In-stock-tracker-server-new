# Server for the In-Stock Tracker

# Features
- Scheduled notification service for all users who allow it
- The notifications will be sent to the email of the user which consists of all the ```Items``` that the user has in their ```watchlist```
- Authenticaion service
- Utilizes Postegresql as the DB

# More information about this project
- https://github.com/HusamSaleem/in_stock_flutterApp/blob/main/README.md

# To deploy on Heroku
- Make sure to use postegresql
- Have a gmail account to use to send notifications
- Edit the config vars on heroku to include these properties
```
Config Vars
{
    EMAIL : ${YOUR_EMAIL_ADDRESS}
    EMAIL_PASSWORD : ${YOUR_APP_PASSWORD},
    SPRING_PROFILES_ACTIVE : prod
}

```

# REST API
- Documentation for my api

# Registration (/api/v1/user/register) - Type POST
**You send:** registration info via request body in JSON. **You get:** a status code ```200``` for successful response

Example Request: 
```
POST /api/v1/user/register
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

# Login (/api/v1/user/login) - Type POST
**You send:** login credentials via request body in JSON. **You get:** registered user with a ```uniqueIdentifier```

Example Request:
```
POST /api/v1/user/login
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

# Update Email (/api/v1/user/email/{uniqueIdentifer}?newEmail={newEmail}) - Type PUT
**You send:** ```uniqueIdentifier``` and ```newEmail```. **You get:** ```200``` status code

Example Request:
```
PUT /api/v1/user/email/1b2b3b7e-9f53-422e-b91c-c1b6f85ba7e0?newEmail=potato@gmail.com
```

Successful Response: ```200 ok```

Failed Response:
```
{
    "message": "user does not exist",
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "2022-03-30T23:07:45.9204719Z"
}
```

# Update Notification Preference (/api/v1/user/notifications/{uniqueIdentifer}?notifyByEmail={newEmail}) - Type PUT
**You send:** ```uniqueIdentifier``` and ```notifyByEmail```. **You get:** ```200``` status code

Example Request:
```
PUT /api/v1/user/notifications/1b2b3b7e-9f53-422e-b91c-c1b6f85ba7e0?notifyByEmail=true
```

Successful Response: ```200 ok```

Failed Response:
```
{
    "message": "user does not exist",
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "2022-03-30T23:07:45.9204719Z"
}
```

# Get item from website (/api/v1/item?itemId={itemId}&website={website} - Type GET
    
**You send:** item id of the product and website name as query params. **You get** an ```Item``` back

Example Request:
```
GET /api/v1/item?itemId=B08166SLDF&website=amazon
```

Successful Response:
```
{
    "itemId": "B08166SLDF",
    "price": "$229.00",
    "name": "AMD Ryzen 5 5600X 6-core, 12-Thread Unlocked Desktop Processor with Wraith Stealth Cooler",
    "website": "amazon",
    "inStock": true
}
```

Failed Responses:
```
{
    "message": "Invalid website name",
    "httpStatus": "NOT_FOUND",
    "timeStamp": "2022-03-30T23:01:47.7765126Z"
}
```
```
{
    "message": "item from amazon with id: B08166LDFA not found",
    "httpStatus": "NOT_FOUND",
    "timeStamp": "2022-03-30T23:02:05.1125958Z"
}
```

# Get user watchlist (/api/v1/watchlist/{uniqueIdentifer}) - Type GET
**You send:** ```uniqueIdentifier``` **You get:** a list of ```Item```

Example Request:
```
GET /api/v1/watchlist/1b2b3b7e-9f53-422e-b91c-c1b6f85ba7e0
```

Successful Response:
```
[
    {
        "itemId": "B0815XFSGK",
        "price": "$339.99",
        "name": "AMD Ryzen 7 5800X 8-core, 16-Thread Unlocked Desktop Processor",
        "website": "amazon",
        "inStock": true
    }
]
```

Failed Response:
```
{
    "message": "user does not exist",
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "2022-03-30T23:14:27.9539695Z"
}
```

# Add item to the user's watchlist (/api/v1/watchlist/{uniqueIdentifer}) - Type POST
**You send ```uniqueIdentifer``` and ```itemId```, ```website``` in the request body as JSON. **You get:** the ```Item``` back

Example Request:
```
POST /api/v1/watchlist/1b2b3b7e-9f53-422e-b91c-c1b6f85ba7e0
{
    "itemId": "B0815XFSGK",
    "website": "amazon"
}
```

Successful Response:
```
{
    "itemId": "B0815XFSGK",
    "price": "$339.99",
    "name": "AMD Ryzen 7 5800X 8-core, 16-Thread Unlocked Desktop Processor",
    "website": "amazon",
    "inStock": true
}
```

Failed Response:
```
{
    "message": "item already in watchlist",
    "httpStatus": "NOT_FOUND",
    "timeStamp": "2022-03-30T23:16:52.0647395Z"
}
```

# Delete an item from a user's watchlist (/api/v1/watchlist/{uniqueIdentifer}) - Type Delete
**You send ```uniqueIdentifer``` and ```itemId``` in request body as JSON. **You get:** ```200``` status code

Example Request
```
DELETE /api/v1/watchlist/1b2b3b7e-9f53-422e-b91c-c1b6f85ba7e0
{
    "itemId": "B0815XFSGK"
}
```

Successful Response: ```200 ok```

Failed Response:
```
{
    "message": "not able to find item id: B0815XFSGK in the user's watchlist",
    "httpStatus": "NOT_FOUND",
    "timeStamp": "2022-03-30T23:18:51.3202069Z"
}
```

# TODO
- Add more testing
- Add more website support
