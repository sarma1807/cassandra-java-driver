#### Apache Cassandra CQL Commands

`01_cassandra_sales_keyspace.cql` Apache Cassandra CQL command to create sales keyspace.

`02_sales_create_tables.cql` Apache Cassandra CQL commands to create tables in sales keyspace.

`03_load_data_in_lookup_tables.cql` Apache Cassandra CQL insert data into lookup tables in sales keyspace.

---

#### SalesApp Java code written using cassandra-java-driver.

`SalesApp_GenerateUsers.java` Generates Users. Typically executed only 1 time during initial setup.

`SalesApp_GenerateProducts.java` Generates Products. Typically executed only 1 time during initial setup.

`SalesApp_GenerateOrders.java` Generates Orders. Execute it whenever you want to generate Orders or automate the execution using cron.

---

#### Shell-scripts

`compile_all_java_code.sh` Shell-script to compile all java code.

`SalesApp_GenerateOrders.sh` Shell-script to generate Orders.

---

#### crontab

`crontab_for_auto_order_generation.md` crontab entries to automatically generate Orders.

---

NOTE : JAVA CODE IS WRITTEN FOR DEMONSTRATION PURPOSES ONLY. IT IS NOT OPTIMIZED, NOR WRITTEN BY A PROFESSIONAL JAVA PROGRAMMER.

