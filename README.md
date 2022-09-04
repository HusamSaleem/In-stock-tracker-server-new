# Server for the In-Stock Tracker

# Features
- Scheduled notification service for all users who allow it
- The notifications will be sent to the device of the user which notifies them that 1 or more of their items are in stock
- Authenticaion service using FireBase
- Utilizes Postegresql as the DB

# More information about this project
- https://github.com/HusamSaleem/in_stock_flutterApp/blob/main/README.md

# To deploy on Heroku
- Make sure to use postegresql
- Edit the config vars on heroku to include these properties
- You need firebase credentials which you can generate in service account. (https://firebase.google.com/docs/admin/setup for more info)
```
Config Vars
{
    SPRING_PROFILES_ACTIVE : prod,
    GOOGLE_CREDENTIALS: {YOUR FIREBASE CREDENTIALS AS JSON}
}

```

# REST API
- You can view the api here: https://in-stock-tracker-api-new.herokuapp.com/swagger-ui/index.html#/

# TODO
- Add more testing
- Add more website support like bestbuy, newegg, etc
