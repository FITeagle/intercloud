[![Build Status](https://travis-ci.org/FITeagle/intercloud.svg?branch=master)](https://travis-ci.org/FITeagle/intercloud)

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
Relocate into the intercloud folder and use the following command
```
mvn clean test wildfly:deploy
```

##Check the function of the root and gateway
Open web site for root
```
http://localhost:8080/root/ 
```
and for gateway 
```
http://localhost:8080/gateway/ 
```

For login in the gateway, you can use any username, if the username is used for the first time, an account will be created, if the username has already be registered, make sure your password is correct.

![](https://raw.github.com/FITeagle/intercloud/master/image/login.png)

After login, two functions of the gateway can be tested.

![](https://raw.github.com/FITeagle/intercloud/master/image/gatewayfunction.png)

With the "Add Resource" function, a new node with relevant geograghical information can be added.

![](https://raw.github.com/FITeagle/intercloud/master/image/addresource.png)

With the "Query Resource" function, resources hosted on the root can be queried with SPARQL. 

![](https://raw.github.com/FITeagle/intercloud/master/image/rootSPARQL.png)

Try the following SPARQL query:

```
SELECT ?s ?p ?o
FROM <http://localhost:3030/IaaS/data?default>
WHERE { ?s ?p ?o }
LIMIT 10
```
A same query function is also provided on the root side.

But to query the resources, a web GUI LodLive is also available on the root side with the following link

```
http://localhost:8080/root/LodLive/app_en.html
```

You can use the resource URI to search the resource.

![](https://raw.github.com/FITeagle/intercloud/master/image/lodlive.png)

And get the resource demonstrated after start

![](https://raw.github.com/FITeagle/intercloud/master/image/lodlivefiteagle.png)

The specific information about the resource can be found on the webpage as well as the geographical position of the resource shown on Google map.

![](https://raw.github.com/FITeagle/intercloud/master/image/lodlivemap.png)

##Check the XMPP clients status on XMPP server of OpenFire
Go to web page
```
http://localhost:9090/ 
```
Login with

Username:admin

Password:admin 

