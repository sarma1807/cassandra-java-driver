-- create a keyspace for sales transaction generator "SalesApp" using cassandra-java-driver

CREATE KEYSPACE sales WITH REPLICATION = { 'class' : 'SimpleStrategy' , 'replication_factor' : 3 } ;
