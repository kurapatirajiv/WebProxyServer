# WebProxyServer
WebProxyServer for Non Persistent Connections:

WorkFlow :
1. Web Proxy server creates the socket to listen for HTTP requests from the client

2. Get host information to contact from the request message

3. Create socket to communicate with web server

4. Regenerate request message and send it to web server

5. Read the HTTP responses from web server

6. Regenerate response message and send it to client


Before running the project we have to create a folder name "cache" in the IDE project folder to ensure the cache is stored in the folder

Browser Settings: 

Netscape:
 Edit-->Preferences
 Select Advanced tab and Proxies
 
Firefox:
 Tools-->Options
 Select Advanced menu and Network tab --> Click Connection settings
 
Internet Explorer
 Tools-->Internet Options
 Select Connections tab and Click LAN settings
 
 Change to Manual proxy settings and Use port 8080 with Ip 127.0.0.1
