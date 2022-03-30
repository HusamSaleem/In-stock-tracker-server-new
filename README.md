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

Failed Response:
```
{
    "message": "email is taken",
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "2022-03-30T22:49:55.6772545Z"
}
```
