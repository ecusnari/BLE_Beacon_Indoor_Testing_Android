# Indoor Beacon Testing Application for Android
This repository contains an Android Mobile Application that tests the BLE beacons installed indoors, and stores the test data on the project server. The mobile application uses BOSSA Platform APIs.

Table of contents
=================

<!--ts-->
   * [About](#about)
   * [Features](#features)
   * [Screenshots](#screenshots)
   * [Type of Beacon devices detected](#type-of-beacon-devices-detected)
   * [Getting Started](#getting-started)
      * [Prerequisites](#prerequisites)
      * [Deployment](#deployment)
   * [Acknowledgements](#acknowledgements)
   * [Contact](#contact)
   * [License](#license)
<!--te-->

## About

The BLE Beacon Indoor Testing Application for Android is part of the bigger project lead by the [IIT Real-Time Communications Lab(IIT RTC Lab)](https://appliedtech.iit.edu/rtc-lab) at Illinois Institute of Technology. IIT RTC Lab parent project has the long term goal of developing an emergency response platform for dispatchers to locate potential victims during an emergency incidence indoors. The key concept of the emergency platform implies using hundreds of BLE beacons installed on every floor of a building, which would communicate with the victim's mobile device which would send the location, humidity and temperature information transmitted from the BLE beacons.
This particular repository contains only the code designed and developed by me to test the installed BLE beacons for correct signal transmittion, safe storage and reliable UX for the project participant conducting the tests.

## Features
- Tester has to authenticate to receive access to the allocated positions for testing
- Tester can choose the building in which the BLE beacons are tested
- Tester can choose the floor on which the BLE beacons are tested
- Tester can choose the position around which the BLE beacons are tested
- Tester can see the real-time collected BLE beacon signals
- Tester receives the confirmation of successful test data being stored

![Happy Flow](https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/happyFlow.png?raw=true)

## Screenshots
<img src="https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/screenshots/1Login.png" width="162" height="288" title="Login Page"> <img src="https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/screenshots/2Login_Error.png" width="162" height="288" title="Login Page Error"> <img src="https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/screenshots/3Positions_List.png" width="162" height="288" title="Positions List"> <img src="https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/screenshots/4Test_Page.png" width="162" height="288" title="Test Page"> <img src="https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/screenshots/5List_Results.png" width="162" height="288" title="List Results">

## Type of Beacon devices detected

#### AXAET PC037 Bluetooth Beacon
<img src="https://github.com/ecusnari/BLE_Beacon_Indoor_Testing_Android/blob/master/illustrations/beaconType.png" width="249" height="253" title="Beacon Type">

## Getting Started

#### Prerequisites
- For deployment: mobile device supporting Android OS starting from Jelly Bean 4.1(API 16) and after, supporting Bluetooth 2.0 and after.
- For code editing:
  * JDK 1.8 
  * [Android Studio](https://developer.android.com/studio/index.html)
  * [Android Beacon Library(AltBeacon)](https://altbeacon.github.io/android-beacon-library/)
  * AXAET PC037 Bluetooth Beacons(NOTE: the major and minor signature must be modified according to your the specific beacons' UUID)

#### Deployment
* After installing Android Studio and JDK 1.8, use "gradle build" command or generate and SDK for the application
* The SDK generated can be installed on an Android device described in [Prerequisites](#prerequisites), via direct USB transfer or donwload
* Find in the menu the "Beacons Testing App" and click to lunch the application

## Acknowledgements
* [Carol Davids](https://appliedtech.iit.edu/people/carol-davids) for creating this amazing project and platform
* [Enzo Piacenza](https://www.linkedin.com/in/enzo-piacenza-b21706128/) for being a great mentor and guiding our projects with skill

## Contact
* [Erica Cusnariov](https://www.linkedin.com/in/ericacusnariov/) for questions about this repository

* [Carol Davids](https://appliedtech.iit.edu/people/carol-davids) for questions about the [IIT Real-Time Communications Lab](https://appliedtech.iit.edu/rtc-lab) or the [BOSSA Platform](https://api.iitrtclab.com/)

## License

&copy; [IIT Real-Time Communications Lab](https://appliedtech.iit.edu/rtc-lab)
