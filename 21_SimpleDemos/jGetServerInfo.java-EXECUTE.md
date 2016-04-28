[cass@metalgear ~]$ `javac -version`
```
javac 1.8.0_72
```

[cass@metalgear ~]$ `java -version`
```
java version "1.8.0_72"
Java(TM) SE Runtime Environment (build 1.8.0_72-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.72-b15, mixed mode)
```

---

#### compile java program

[cass@metalgear ~]$ `javac -classpath /u01/cass/cassandra-java-driver-3.0.0/cassandra-driver-core-3.0.0.jar:. jGetServerInfo.java`

NO OUTPUT, but compiled `jGetServerInfo.class` file got generated.

---

#### run java program

[cass@metalgear ~]$ `java -classpath /u01/cass/cassandra-java-driver-3.0.0/*:/u01/cass/cassandra-java-driver-3.0.0/lib/*:. jGetServerInfo`

Output :
```
--------------------------------------------------------------------

Server IP   : 192.168.0.57

Server Name : metalgear : metalgear.mycompany.com

--------------------------------------------------------------------
```

---
