#!/bin/bash
# this script assumes SalesApp code is stored in "/home/cass/SalesApp_JavaCode" folder
# this script assumes cassandra-java-driver is stored in "/u01/cass/cassandra-java-driver-3.0.0"

cd /home/cass/SalesApp_JavaCode

# compile java code - only 1 time requirement after code change
javac -classpath /u01/cass/cassandra-java-driver-3.0.0/cassandra-driver-core-3.0.0.jar:. SalesApp_GenerateUsers.java
javac -classpath /u01/cass/cassandra-java-driver-3.0.0/cassandra-driver-core-3.0.0.jar:. SalesApp_GenerateProducts.java
javac -classpath /u01/cass/cassandra-java-driver-3.0.0/cassandra-driver-core-3.0.0.jar:. SalesApp_GenerateOrders.java

# this will generate SalesApp_GenerateUsers.class + SalesApp_GenerateProducts.class + SalesApp_GenerateOrders.class

