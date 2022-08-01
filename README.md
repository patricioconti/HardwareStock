Hardware Stock App
===================================

An stock or inventory app for our solid brass hardware.

The user adds new item entries and update existing ones.

The data is stored in a local database using Room.

Navigation was implmented using Navigation component.

There's an overflow menu in the tool bar for settings that navigates to a Settings Fragment.

On Settings Fragment the user can enable or disable reminder notifications. This data is stored using PreferenceDataStore (Coroutines and Flow).

The notifications were implemented using WorkManager.

There's a second menu in the tool bar that navigates to a Shopping Fragment.

On Shopping Fragment the user can navigate to hardware suppliers websites using implicit intents.


Instrumented tests using Espresso were implemented:

* DataTransactionTests (test the database). Orchestrator was used.
* NavigationTests using test navigation.
* NotificationTest using uiautomator.

A custom .xml icon launcher was included.

Architecture: MVVM


Purpose
--------------

This code demonstrates the Android Architecture component- ViewModel and LiveData.

It also shows how to implement:

* Navigation component
* Notifications
* Workmanager
* Preference DataStore
* Testing (Espresso, Orchestrator, Uiautomator, Fragment and Navigation tester).


Getting Started
---------------

1. Download and run the app.
