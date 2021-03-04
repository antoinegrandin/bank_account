# Secure Development : Mobile applications

Hey here is my README file.

## Report guidelines

- You create a github/gitlab public project **only** containing:
  - Your source code in a **src** folder
  - <your_application>.apk
  - README.md
- You send your repository link **before 11pm59 on the 4th february 2021**. After this timeline, you will loose 1pts per hour.

## Exercice

### Explain how you ensure user is the right one starting the app

The first time the application is opened, a message box tells the user that his default code is '123'. Of course, in reality, the user would not see this message and would receive this code by email for example. At the first connection, the user cannot use the fingerprint to unlock the application.

This code, as well as information about whether the user is logging in for the first time, is stored in the shared preferences in private mode.

The user enters the temporary code and is directed to a page where he can change the temporary code to personalize it. Rules (Regex) have been put in place to ensure that the password is complex enough.
Once the user validates his password, it is hashed with the SHA-256 algorithm and stored in the shared preferences.

The next time the user logs in, he will have the choice of :

- enter his password in the dedicated field (the password will then be hashed and compared with the one stored in the shared preferences).
- unlock the application with his fingerprint.

### How do you securely save user's data on your phone ?

As explained above, the user's login data is stored in shared preferences in private mode (so that only the banking application can access this data) and is hashed with the SHA-256 algorithm.
They are therefore theoretically not recoverable.

Nevertheless, the client's bank account data, which is stored locally so that the user can access it offline, is stored in a SQLite database whose access is protected by code obfuscation during compilation.

### How did you hide the API url ?

Using ndk-build, the API url can be protected by including it in a C encoded file which, when compiled, contains only binary numbers i.e. in the form of 0s and 1s. So, even after reverse engineering, you will get 0s and 1s and it is very difficult to identify what is written in the form of 0s and 1s.

In the account activity, we just have to go and retrieve the url contained in the file previously created.

Moreover, as already said before, the application is obfuscated when it is compiled, which makes it all the more difficult to reverse engineer in order to find the url of the API.

### Screenshots of your application
![Alt text](https://github.com/antoinegrandin/screenshot_android_project/blob/master/About_Dev_Info.jpg "About Developer Information")
