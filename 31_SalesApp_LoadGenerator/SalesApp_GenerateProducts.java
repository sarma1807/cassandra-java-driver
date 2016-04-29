
import com.datastax.driver.core.* ;
import java.lang.Long ;
import java.math.* ;
import java.util.Date ;
import java.util.UUID ;
import java.util.Random ;

public class SalesApp_GenerateProducts {

	private Cluster			var_cluster		;
	private Session			var_session		;
	private Metadata		var_metadata	;

	public void connectToCassCluster(String serverNode, String keySpace, String printClusterInfo)
	{
		// connect to the cassandra cluster
		var_cluster = Cluster.builder().addContactPoint(serverNode).build() ;

		// connect to keyspace
		var_session = var_cluster.connect(keySpace);

		// get cluster metadata
		var_metadata = var_cluster.getMetadata() ;

		if (printClusterInfo == "yes")
		{
			// print cluster metadata
			System.out.printf("------------------------------------------------------- \n") ;
			System.out.printf("Cassandra Cluster Information \n") ;
			System.out.printf("------------------------------------------------------- \n") ;

			System.out.printf("Cluster Name : %s\n", var_metadata.getClusterName()) ;

			for ( Host var_host : var_metadata.getAllHosts() )
			{
				System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", var_host.getDatacenter(), var_host.getAddress(), var_host.getRack()) ;
			}

			System.out.printf("------------------------------------------------------- \n") ;
		}
	}


	public void disconnectFromCassCluster()
	{
		var_cluster.close() ;
	}


	public static void main(String[] args)
	{
		// variables
		Long		var_product_id			= new Long(1) ;
		String		var_product_code		= new String("") ;
		String		var_product_name		= new String("") ;
		String		var_product_description	= new String("") ;
		String		var_product_category	= new String("") ;
		BigDecimal	var_product_price		;
		Long		var_product_qoh			= new Long(1) ;

		Random		var_randomGenerator		= new Random() ;
		int			var_randomNumber		= 0 ;

		Long		gen_no_of_prods			= new Long(80000) ;  // will generate 80,000 products

		ResultSet	db_results ;


		// instantiate the class
		SalesApp_GenerateProducts prog = new SalesApp_GenerateProducts() ;

		// connect to cluster and keyspace
		prog.connectToCassCluster("192.168.0.45", "sales", "no") ;

		// prepare cql bound statements
		PreparedStatement var_cqlcmd_pcat = prog.var_session.prepare("SELECT product_category FROM sales.lookup_product_categories WHERE id = ?") ;
		BoundStatement var_bs_pcat = new BoundStatement(var_cqlcmd_pcat) ;

		PreparedStatement var_cqlcmd_product_insert = prog.var_session.prepare("INSERT INTO sales.products (product_id, product_code, product_name, product_description, product_category, product_price, product_qoh) VALUES (?, ?, ?, ?, ?, ?, ?)") ;
		BoundStatement var_bs_product_insert = new BoundStatement(var_cqlcmd_product_insert) ;


		// generate products
		System.out.format("------------------------------------------------------- \n");
		for (var_product_id = new Long(1); var_product_id <= gen_no_of_prods; var_product_id++)
		{
			var_product_code = UUID.randomUUID().toString().replace("-", "").substring(1, 9) ;
			
			var_product_name = UUID.randomUUID().toString().replace("-", "").substring(1, var_randomGenerator.nextInt(6)+1) ;
			var_product_name = var_product_name.concat(" ") ;
			var_product_name = var_product_name.concat(UUID.randomUUID().toString().replace("-", "").substring(1, var_randomGenerator.nextInt(7)+1)) ;

			var_product_description = UUID.randomUUID().toString().replace("-", "").substring(1, var_randomGenerator.nextInt(8)+1) ;
			var_product_description = var_product_description.concat(" ") ;
			var_product_description = var_product_description.concat(UUID.randomUUID().toString().replace("-", "").substring(1, var_randomGenerator.nextInt(7)+1)) ;
			var_product_description = var_product_description.concat(" ") ;
			var_product_description = var_product_description.concat(UUID.randomUUID().toString().replace("-", "").substring(1, var_randomGenerator.nextInt(5)+1)) ;
			var_product_description = var_product_description.concat(" ") ;
			var_product_description = var_product_description.concat(UUID.randomUUID().toString().replace("-", "").substring(1, var_randomGenerator.nextInt(10)+1)) ;

			// generate product category - start
			var_randomNumber = var_randomGenerator.nextInt(20) ;
			if (var_randomNumber < 01) var_randomNumber = 1 ;
			if (var_randomNumber > 20) var_randomNumber = 20 ;

			// get product category
			db_results = prog.var_session.execute(var_bs_pcat.bind(var_randomNumber)) ;

			for (Row row : db_results)
			{
				var_product_category = row.getString("product_category") ;
			}
			// generate product category - end

			var_product_price = new BigDecimal(2 + (var_randomGenerator.nextInt(60) + var_randomGenerator.nextFloat())) ;
			var_product_price = var_product_price.setScale(2, RoundingMode.CEILING) ;

			var_product_qoh = new Long(var_randomGenerator.nextInt(90)) ;
			var_product_qoh = var_product_qoh * 1000 ;

			// insert user into users table
			prog.var_session.execute(var_bs_product_insert.bind(var_product_id, var_product_code, var_product_name, var_product_description, var_product_category, var_product_price, var_product_qoh)) ;

			// print EOT
			// System.out.format("%d | %s | %s | %s | %s | %f | %d \n", var_product_id, var_product_code, var_product_name, var_product_description, var_product_category, var_product_price, var_product_qoh) ;

		}

		System.out.format("%d Products generated. \n", var_product_id);
		System.out.format("------------------------------------------------------- \n");

		// disconnect from cluster
		prog.disconnectFromCassCluster() ;
	}
}

