## TEnmo Project

The goal is to complete an online payment service for transferring "TE bucks" between friends, similar to venmo. The task was to finalize the server side of the application: a database and a RESTful API server.

 In order to comple tackle down te the task, we have the controllers, models, DAOs, and database tables to implement the following features:

## Use cases

### Required use cases

You should attempt to complete all of the following required use cases.

1. As a user of the system, I need to be able to register myself with a username and password.
   1. The ability to register has been provided in your starter code.
2. As a user of the system, I need to be able to log in using my registered username and password.
   1. Logging in returns an Authentication Token. I need to include this token with all my subsequent interactions with the system outside of registering and logging in.
   2. The ability to log in has been provided in your starter code.
   3. User ids start at 1001.
3. As a user, when I register a new account is created for me.
   1. The new account has an initial balance of $1000.
   2. Account ids start at 2001.
4. As an authenticated user of the system, I need to be able to see my Account Balance. 

**Endpoint Specification:** 
```
An endpoint mapping with a suitable path: api/accounts/get-balance and request type. 
The request only contain a token. 
No id's or usernames were presented on the request.
```

**Output:**
The example response is in the following format:
```
{
   "username" : "user",
   "balance" : 1000
}
```

5. As an authenticated user of the system, I need to be able to *send* a transfer of a specific amount of TE Bucks to a registered user.
   1. I need an endpoint that shows the users I can send money to.
   2. I must not be allowed to send money to myself.
   3. A transfer includes the username of the recipient and the amount of TE Bucks.
   4. The receiver's account balance is increased by the amount of the transfer.
   5. The sender's account balance is decreased by the amount of the transfer.
   6. I can't send more TE Bucks than I have in my account.
   7. I can't send a zero or negative amount.
   8. A Sending Transfer has an initial status of *Approved*.
   9. Transfer ids start at 3001.

**Endpoint Specification (for 5.i):**
```
An endpoint mapping with a suitable path: api/transfers/available-accounts and request type. 
The request only accept a token. 
No id's or usernames is present on the request.
```

**Output (for 5.i):**
The example response is in the following format:
```
[
   { "username" : "Alice"},
   { "username" : "Bob"},
   { "username  : "Carly"}
]
```

**Endpoint Specification (for 5.iii):**
```
An endpoint with a suitable path and request type: api/transfers/{transferId}
This request support a request body. The body only contain the amount and recipient name.
```

**Output (for 5.iii):**
The example response should be in the following format:
```
{
   "transferId": 1,
   "transferAmount" : 125,
   "from" : "Carly",
   "to": "Bob"
}
```
**A successful response have a status code of 201 Created**

6. As an authenticated user of the system, I need to be able to see transfers I have sent or received.

**Endpoint Specification:**
```
An endpoint mapping with a suitable path: api/transfers/my-transfer-list and request type. 
The request only accept a token. No id's or usernames should be present on the request.
```

**Output:**
The example response is in the following format:
```
[
   {
      "transferId": 1,
      "transferAmount" : 125,
      "from" : "Carly",
      "to": "Bob"
   }, 
   {
      "transferId": 2,
      "transferAmount" : 10,
      "from" : "Carly",
      "to": "David"
   }
]
```
7. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.

**Endpoint Specification:**
```
An endpoint mapping with a suitable path: api/transfers/my-transfers/{transferId} and request type. 
The request contain a token, and no account or user id's should be present. 
However, in this case, include a path variable that represents the transfer ID.
```
**Output:**
The example response is in the following format:
```
{
    "transferId": 2,
    "transferAmount" : 10,
    "from" : "Carly",
    "to": "David"
}
```

*No sensitive information (i.e. account numbers, user ids, etc) is passed in the URL.*  Integration testing is included on each method that connects to the database.

8. As an authenticated user of the system, I need to be able to *request* a transfer of a specific amount of TE Bucks from another registered user.
   1. I should be able to choose from a list of users to request TE Bucks from.
   2. I must not be allowed to request money from myself.
   3. I can't request a zero or negative amount.
   4. A transfer includes the usernames of the from and to users and the amount of TE Bucks.
   5. A Request Transfer has an initial status of *Pending*.
   6. No account balance changes until the request is approved.
   7. The transfer request should appear in both users' list of transfers 

9.  As an authenticated user of the system, I need to be able to see my *Pending* transfers.
10. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
   1. I can't "approve" a given Request Transfer for more TE Bucks than I have in my account.
   2. The Request Transfer status is *Approved* if I approve, or *Rejected* if I reject the request.
   3. If the transfer is approved, the requester's account balance is increased by the amount of the request.
   4. If the transfer is approved, the requestee's account balance is decreased by the amount of the request.
   5. If the transfer is rejected, no account balance changes.


***  Run and TenmoApplication and open the following link to test the program: http://localhost:8080/swagger-ui/#/  ***