// import cassandra-java-driver
import com.datastax.driver.core.* ;

public class readWriteCassEmp
{
	public static void main(String[] args)
	{
		// Cluster & Session are provided by cassandra-java-driver
		Cluster cluster ;
		Session session ;

		// connect to the cassandra cluster
		// cluster = Cluster.builder().addContactPoint("127.0.0.1").build() ;
		// cluster = Cluster.builder().addContactPoint("192.168.0.67").build() ;
		// cluster = Cluster.builder().addContactPoints("192.168.0.67,192.168.0.76,192.168.0.88").build() ;
		cluster = Cluster.builder().addContactPoint("132.197.63.91").build() ;

		// connect to cassdemo cassandra keyspace
		session = cluster.connect("cassdemo") ;

		// insert a new record into the users table
		session.execute("INSERT INTO cassdemo.emp (empid, deptid, first_name, last_name) VALUES (1002, 200, 'Larry', 'Cool')") ;

		// select data from users table
		// ResultSet results = session.execute("SELECT * FROM cassdemo.users WHERE lastname='Larry'") ;
		ResultSet results = session.execute("SELECT * FROM cassdemo.emp") ;

		// print header info
		System.out.format("------------------------------------------------------- \n") ;
		System.out.format("first_name | last_name | empid \n") ;
		System.out.format("------------------------------------------------------- \n") ;

		// process results set to print all rows as output
		for (Row row : results)
		{
			System.out.format("%s | %s | %d\n", row.getString("first_name"), row.getString("last_name"), row.getInt("empid")) ;
		}

		// print footer
		System.out.format("------------------------------------------------------- \n") ;

		// clean up and close the cassandra connection
		cluster.close();
	}
}
