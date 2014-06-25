# FITeagle Intercloud

Intercloud project implementing simplified functionalities of IEEE P2303 Intercloud draft Standard

## Prerequisite
Deploy FITeagle platform according to https://github.com/FITeagle/bootstrap/README.md

## Download the intercloud code
```
git clone https://github.com/FITeagle/intercloud.git
```

## Start J2EE Server
```
./bootstrap/fiteagle.sh startJ2EE
```
Keep the J2EE Server running
## Start XMPP Server
Open a new terminal and start XMPP server 
```
./bootstrap/fiteagle.sh startXMPP
```
Keep the XMPP server running
##Compile and Deploy root and gateway
Relocate into the root or gateway folder and use the following command
```
./intercloud.sh deployRoot
```
or
```
./intercloud.sh deployGateway
```
##Create the root and gateway
Open web site for root 
```
http://localhost:8080/root/ 
```
and for gateway 
```
http://localhost:8080/gateway/ 
```
to check the messages exchanged between the root and the gateway.

Refresh the gateway page to realize sending more messages to the root

##Check the XMPP clients status on XMPP server of OpenFire
Go to web page
```
http://localhost:9090/ 
```
Login with

Username:admin

Password:admin 
