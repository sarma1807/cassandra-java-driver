##### ** compile java program **

[cass@metalgear ~]$ `javac -classpath /u01/cass/cassandra-java-driver-3.0.0/cassandra-driver-core-3.0.0.jar:. readWriteCassEmp.java`

NO OUTPUT, but compiled `readWriteCassEmp.class` file got generated.

---

##### ** run java program **

[cass@metalgear ~]$ `java -classpath /u01/cass/cassandra-java-driver-3.0.0/*:/u01/cass/cassandra-java-driver-3.0.0/lib/*:. readWriteCassEmp`


Output :
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

-------------------------------------------------------
first_name | last_name | empid
-------------------------------------------------------
Scott | Tiger | 1001
Queen | John | 1003
Larry | Cool | 1002
-------------------------------------------------------
```
