<h1>// COMP335 Assignment 1: Stage 2 // Victor Nikolic 45158290</h1>

<h3>Main class: Client.java</h3> containing scheduling client functionality

- Server - class containing a server data
- Servers - class containing a list of Server class loaded from the system.xml
- SystemXMLParser - class reading and parsing the system.xml file

build jar by executing command: <b>mvn clean install</b>

scheduling-1.0-SNAPSHOT.jar file built in the target directory.

<h3>Client Execution</h3>
1) Copy the generated jar file onto the VM host into the same directory as the <b>server</b>.
1) Rename the jar file to client.jar (optional)
1) Start the server (e.g. by executing <b>./server -v brief</b>)
1) Start the client by executing <b>java -jar client.jar [-h] [-v] [-a algo_name]</b>. <b>server</b> will generate system.xml file needed by the scheduling client.

<h3>Alternate way to build a client jar</h3>
1) Create ds-client folder
1) Copy conf and client folders into it
1) Go (cd) into the conf folder
1) execute ./makejar.sh - Will create a client.jar in the ds-client folder.
