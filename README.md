# ljw-applications

This repository contains tooling to support [LJW Aachen e.V.](http://ljw-aachen.de/) in organizational tasks. 

# Client
A GUI to manage expenses of the summer camp participants. The application supports:
- creating and editing accounts
- making money deposits and withdrawals 
- transferring money between two accounts
- cashing up ("how much money is in the till?")
- list accounts with negative balance
- import accounts from CSV

![UI Showcase](client-showcase.png)

# Setting up
## Prerequisites
- Java 11
- Maven
## Install
- download the repository
- open a terminal in the top directory
- execute `mvn clean install -DskipTests`
## Run
- execute `java -jar client/target/client.jar`


