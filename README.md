# AccesControlFlexso-frontend
this repository contains the frontend of an internship project. It's a wallet app that should replace RFID cards.

### Prerequisites

* NFC support

## Built With

* [Gradle](https://gradle.org//) - Dependency Management

## Features
USER
- Login 
- Register
- Wallet
- Update account
- Fingerprint authentication
- Settings

COMPANY
- USER features (no register)
- Update company
- CRUD offices
- De/authorize users

ADMIN
- COMPANY features (no register)
- CRUD companies

## Demo

[![Watch here](http://img.youtube.com/vi/2uZ_ksTwxjc/0.jpg)](http://www.youtube.com/watch?v=2uZ_ksTwxjc&feature=share)

## Screenshots

- USER

| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img width="200" src="/readme/user/login.png"> |  <img width="200" alt="" src="/readme/user/register.png">|<img width="200" alt="" src="/readme/user/home.png">|
|<img width="200" alt="" src="/readme/user/nav_user.png">  |  <img width="200" alt="" src="/readme/user/wallet.png">|<img width="200" alt="" src="/readme/user/account_update.png">|
     
- COMPANY

| | | |
|:-------------------------:|:-------------------------:|:-------------------------:|
|<img width="200" src="/readme/company/nav_company.png"> |  <img width="200" alt="" src="/readme/company/office_list.png">|<img width="200" alt="" src="/readme/company/office_list_add.png">|
|<img width="200" alt="" src="/readme/company/office_list_delete.png">  |  <img width="200" alt="" src="/readme/company/authorized_person_list.png">|<img width="200" alt="" src="/readme/company/authorized_person_list_add.png">|
|<img width="200" alt="" src="/readme/company/authorized_person_list_delete.png">  |  <img width="200" alt="" src="/readme/company/edit_office_address.png">|<img width="200" alt="" src="/readme/company/edit_company_name.png">|
    
- ADMIN

| | | 
|:-------------------------:|:-------------------------:|
|<img width="200" src="/readme/admin/nav_admin.png"> |  <img width="200" alt="" src="/readme/admin/company_list.png">|
|<img width="200" alt="" src="/readme/admin/company_list_add.png">  |  <img width="200" alt="" src="/readme/admin/company_list_delete.png">|

## Permissions

- Full Network Access.
- NFC

## Authors

* [Bert Van Eeckhoutte](https://github.com/bertve)

## Libraries

- [Goldfinger - Fingerprint authentication](https://github.com/infinum/Android-Goldfinger)
- [Saripaar - Form validation] (https://github.com/ragunathjawahar/android-saripaar)
- [Lottie - Animation] (https://github.com/airbnb/lottie-android)
- [Gson - JSON serialization/deserialization] (https://github.com/google/gson)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [Example HCE with PN532 module](https://github.com/Lexycon/android-pn532-hce)
