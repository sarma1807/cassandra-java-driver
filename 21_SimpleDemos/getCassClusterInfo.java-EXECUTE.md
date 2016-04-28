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

[cass@metalgear ~]$ `javac -classpath /u01/cass/cassandra-java-driver-3.0.0/cassandra-driver-core-3.0.0.jar:. getCassClusterInfo.java`

NO OUTPUT, but compiled getCassClusterInfo.class` file got generated.

---

#### run java program

[cass@metalgear ~]$ `java -classpath /u01/cass/cassandra-java-driver-3.0.0/*:/u01/cass/cassandra-java-driver-3.0.0/lib/*:. getCassClusterInfo`


Output :
```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
-------------------------------------------------------
Cassandra Cluster Information
-------------------------------------------------------
Cluster Name : waltham_cass_cluster
Partitioner Name : org.apache.cassandra.dht.Murmur3Partitioner
-------------------------------------------------------
Cluster Nodes :
1 : Datatacenter: Cass_DC2; Host: /192.168.0.163; Rack: Cass_DC2_Rack1; Version: 2.2.4;
2 : Datatacenter: Cass_DC3; Host: /192.168.0.165; Rack: Cass_DC3_Rack1; Version: 2.2.4;
3 : Datatacenter: Cass_DC1; Host: /192.168.0.94; Rack: Cass_DC1_Rack2; Version: 2.2.4;
4 : Datatacenter: Cass_DC1; Host: /192.168.0.91; Rack: Cass_DC1_Rack1; Version: 2.2.4;
5 : Datatacenter: Cass_DC2; Host: /192.168.0.67; Rack: Cass_DC2_Rack1; Version: 2.2.4;
6 : Datatacenter: Cass_DC1; Host: /192.168.0.93; Rack: Cass_DC1_Rack2; Version: 2.2.4;
7 : Datatacenter: Cass_DC3; Host: /192.168.0.157; Rack: Cass_DC3_Rack1; Version: 2.2.4;
8 : Datatacenter: Cass_DC2; Host: /192.168.0.236; Rack: Cass_DC2_Rack1; Version: 2.2.4;
9 : Datatacenter: Cass_DC1; Host: /192.168.0.92; Rack: Cass_DC1_Rack1; Version: 2.2.4;
-------------------------------------------------------
Cassandra Keyspaces :

Keyspace Name: cassdemo
Replication Factor: { Cass_DC2:2 , Cass_DC3:2 , Cass_DC1:2 , class:org.apache.cassandra.locator.NetworkTopologyStrategy }

Keyspace Name: system_traces
Replication Factor: { replication_factor:2 , class:org.apache.cassandra.locator.SimpleStrategy }

Keyspace Name: system
Replication Factor: { class:org.apache.cassandra.locator.LocalStrategy }

Keyspace Name: system_distributed
Replication Factor: { Cass_DC2:2 , Cass_DC3:2 , Cass_DC1:2 , class:org.apache.cassandra.locator.NetworkTopologyStrategy }

Keyspace Name: system_auth
Replication Factor: { replication_factor:1 , class:org.apache.cassandra.locator.SimpleStrategy }

-------------------------------------------------------
```
