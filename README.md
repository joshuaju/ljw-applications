# ljw-applications

This repository contains projects to support [LJW Aachen e.V.](http://ljw-aachen.de/) in organizational tasks. 

## Account management
To keep track of all participants of the summer camp.

## Lagerbank (translates to "summer camp bank")
Manage expenses of the camp participats. Allows deposits, withdrawal and transfering money.

## LJW FX Client
A GUI supporting everyday tasks. Currently the application supports:
- Creating and editing accounts
- Making money deposits and withdrawals 
- Transfering money between two accounts

# Setting up
## Prerequisites
- Java 11
- Maven
## Install
- download the repository
- open a terminal in the top directory
- execute `mvn clean install -DskipTests`
- change directory to `ljw-fx-client/shade`
- execute `java -jar ljw-fx-client.jar`


