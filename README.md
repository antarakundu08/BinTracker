# BinTracker
SERVICES AND TECH USED: 
1) Firebase- For authentication and realtime database
2) Google Map Api
3) Authentication - Firebase gmail, facebook and email


HOW APP WORKS:
The app is based on the circumstances when there is no family member in our house and garbage collecting vehicle comes to pick the garbage, and we cannot throw it.
So whenever the user wants to throw their garbage, user will have to login to the app send the request. The app on identifying driver's location, notifies him to collect the garbage of that area. After 24 hours, all the requests gets deleted, so that old requests doesn't contribute to the new requests.

# USER (FLOW)
                                              User
                                                |
                                                |
                                               \/
                                            SignIn/SignUp
                                                |
                                                |
                                               \/
                                  App asks permission to access location
                                                |
                                                |
                                               \/
                                     User sends request to server
                                                |
                                                |
                                               \/
                   User can track the garbage collecting vehicle through Google Maps
                                                |
                                                |
                                               \/
                                             Logout
                                             
                                             
# DRIVER (FLOW)
                                              Driver
                                                |
                                                |
                                               \/
                                              SignIn
                                                |
                                                |
                                               \/
                                  App asks permission to access location
                                                |
                                                |
                                               \/
                The driver is notified when a specific number of requests are collected
                                                |
                                                |
                                               \/
                                 The driver goes and picks up the garbage
                                                |
                                                |
                                               \/
                                             Logout
                                             
