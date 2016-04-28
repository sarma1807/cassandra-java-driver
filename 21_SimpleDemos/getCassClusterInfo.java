// import cassandra-java-driver
import com.datastax.driver.core.* ;

public class getCassClusterInfo
{
	private Cluster			var_cluster		;
	private Session			var_session		;
	private Metadata		var_metadata	;

	public void connectToCassCluster(String serverNode, String keySpace, String printClusterInfo)
	{
		// connect to cassandra cluster
		var_cluster = Cluster.builder().addContactPoint(serverNode).build() ;

		// connect to cassandra keyspace
		var_session = var_cluster.connect(keySpace);

		// get cluster metadata
		var_metadata = var_cluster.getMetadata() ;

		if (printClusterInfo == "yes")
		{
			System.out.printf("------------------------------------------------------- \n") ;
			System.out.printf("Cassandra Cluster Information \n") ;
			System.out.printf("------------------------------------------------------- \n") ;

			// print cassandra cluster name and partitioner
			System.out.printf("Cluster Name : %s \n", var_metadata.getClusterName() ) ;
			System.out.printf("Partitioner Name : %s \n", var_metadata.getPartitioner() ) ;

			System.out.printf("------------------------------------------------------- \n") ;

			// print cassandra cluster nodes info
			System.out.printf("Cluster Nodes : \n") ;
			int nodeCount = 0 ;
			for ( Host var_host : var_metadata.getAllHosts() )
			{
				nodeCount++ ;
				System.out.printf("%d : Datatacenter: %s; Host: %s; Rack: %s; Version: %s;\n", nodeCount, var_host.getDatacenter(), var_host.getAddress(), var_host.getRack(), var_host.getCassandraVersion().toString() ) ;
			}

			System.out.printf("------------------------------------------------------- \n") ;

			// print cassandra keyspaces
			System.out.printf("Cassandra Keyspaces : \n") ;
			for ( KeyspaceMetadata var_ks : var_metadata.getKeyspaces() )
			{
				System.out.printf("\nKeyspace Name: %s\nReplication Factor: { ", var_ks.getName() ) ;

				var_ks.getReplication().forEach((k,v) -> System.out.print( k + ":" + v + " , ") ) ;

				System.out.printf("\b\b}\n") ;
			}

			System.out.printf("\n------------------------------------------------------- \n") ;
		}
	}


	public void disconnectFromCassCluster()
	{
		var_cluster.close() ;
	}

	public static void main(String[] args)
	{
		// instantiate the class
		getCassClusterInfo prog = new getCassClusterInfo() ;

		// connect to cassandra cluster and keyspace and print cluster info
		// prog.connectToCassCluster("192.168.0.67", "system", "yes") ;
		// prog.connectToCassCluster("192.168.0.67,192.168.0.76,192.168.0.88", "system", "yes") ;
		prog.connectToCassCluster("192.168.0.67", "system", "yes") ;

		// disconnect from cluster
		prog.disconnectFromCassCluster() ;
	}
}
