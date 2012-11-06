personal-genome-client
======================

Java client for the 23andMe Personal Genome API.


###Hacking personal-genome-client

Install

 * JDK 1.6 or later, http://openjdk.java.net/
 * Apache Maven 3.0.4 or later, http://maven.apache.org/


To build

    $ mvn install


To build demo webapp

    $ cd webapp
    $ mvn assembly:assembly


To run demo webapp, your 23andMe Personal Genome API client details must be provided as command line system properties

    $ java -DapiKey=apiKey -DapiSecret=apiSecret -Dcallback=http://localhost:8080/after-auth-landing/ -Dscope=scope -jar 


Open

    http://localhost:8080/

in a browser.