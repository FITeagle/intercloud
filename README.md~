# FITeagle Intercloud

Intercloud project implementing simplified functionalities of IEEE P2303 Intercloud draft Standard
This is the source code of all the components in the Intercloud project

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
##Check the function of the root and gateway
Open web site for root
```
http://localhost:8080/root/ 
```
and for gateway 
```
http://localhost:8080/gateway/ 
```
##Take use of the deployed intercloud infrastructure

There is already deployed infrastructure for one root and two gateways Alice and Bob.

To visit the root, open web page

```
http://root-intercloud.av.tu-berlin.de/root/
```

To visit the gateway Alice and Bob

```
http://alice-gw-intercloud.av.tu-berlin.de/gateway/login.html
```
```
http://bob-gw-intercloud.av.tu-berlin.de/gateway/login.html
```

For login in the gateway, use

```
username:alice
password:alice
```
for gateway alice, and

```
username:bob
password:bob
```
for gateway bob

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
http://root-intercloud.av.tu-berlin.de/root/gui/lodlive/
```

Choose the example-IEEE Intercloud Testbed to provision the testbed

![](https://raw.github.com/FITeagle/intercloud/master/image/lodlive.png)

And get the resource demonstrated after start

![](https://raw.github.com/FITeagle/intercloud/master/image/lodlivemap.png)

The specific information aout all the components and their geographical position can be checked through the GUI

##Check the XMPP clients status on XMPP server of OpenFire
Go to web page
```
http://root-intercloud.av.tu-berlin.de:9090/login.jsp?url=%2Findex.jsp 
```
Login with

Username:admin

Password:admin 

