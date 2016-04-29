
import com.datastax.driver.core.* ;

import java.lang.Long ;
import java.math.* ;
import java.util.Date ;
import java.util.UUID ;
import java.util.Random ;
import java.util.Calendar ;
import java.util.TimeZone ;
import java.text.SimpleDateFormat ;

import java.net.* ;

public class SalesApp_GenerateOrders {

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

		// assumes that program is written and executed in EST timezone
		// timezone set to GMT
		// other timezones might cause issues while storing data in Apache Cassandra
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT")) ;
		Date var_datetime = cal.getTime() ; // get timestamp
		int var_hour = cal.get(Calendar.HOUR_OF_DAY) ; // get hour
		// remove time to get date-only
		cal.set(Calendar.HOUR_OF_DAY, 0) ;
        cal.set(Calendar.MINUTE, 0) ;
        cal.set(Calendar.SECOND, 0) ;
        cal.set(Calendar.MILLISECOND, 0) ;
		Date var_date = cal.getTime() ; // get date-only without time

		SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String reportDate = timestampFormatter.format(var_datetime) ;

		int			gen_no_of_orders					= 20 ; // this will generate 20 x 1,000 = 20,000 orders randomly

		int			db_total_users						= 100000 ; // SELECT count(*) FROM sales.users ;
		int			db_total_products					= 80000 ;  // SELECT count(*) FROM sales.products ;

		int			var_total_generated_orders			= 0 ;

		UUID		var_order_code						= UUID.randomUUID() ;

		Date		var_order_date						= var_date ;
		int			var_order_date_hour                 = var_hour ;
		Date        var_order_timestamp                 = var_datetime ;
		
		BigDecimal	var_order_total						= new BigDecimal(0) ;
		int			var_order_discount_percent			= 0 ;
		BigDecimal	var_order_grand_total				= new BigDecimal(0) ;
		Date		var_order_estimated_shipping_date	= new Date() ;
		int			var_order_number_of_products		= 0 ;

		int			var_product_sold_quantity			= 0 ;
		BigDecimal	var_product_price_total				= new BigDecimal(0) ;

		Random		var_randomGenerator					= new Random() ;
		int			var_randomNumber					= 0 ;

		ResultSet	db_results_user ;
		ResultSet	db_results_product ;

		String var_server_ip = "" ;

		try {
			// uses : import java.net.* ;
			var_server_ip = InetAddress.getLocalHost().getHostAddress() ;
			// System.out.format("This Server IP is : %s \n", var_server_ip) ;
		} catch(Exception e) { System.out.println("Error while getting Server IP. \n") ; }



		// instantiate the class
		SalesApp_GenerateOrders prog = new SalesApp_GenerateOrders() ;

		// connect to cluster and keyspace
		// prog.connectToCassCluster("192.168.0.21", "sales", "no") ;
		prog.connectToCassCluster(var_server_ip, "sales", "no") ;


		// prepare cql bound statements
		PreparedStatement var_cqlcmd_user = prog.var_session.prepare("SELECT user_id, user_name, user_email_id, user_state_code, user_phone_number, user_platform FROM sales.users WHERE user_id = ?") ;
		BoundStatement var_bs_user = new BoundStatement(var_cqlcmd_user) ;

		PreparedStatement var_cqlcmd_product = prog.var_session.prepare("SELECT product_id, product_code, product_name, product_category, product_price, product_qoh FROM sales.products WHERE product_id = ?") ;
		BoundStatement var_bs_product = new BoundStatement(var_cqlcmd_product) ;

		PreparedStatement var_cqlcmd_sales_orders_insert = prog.var_session.prepare("INSERT INTO sales.sales_orders (order_code, order_date, order_date_hour, order_timestamp, order_total, order_discount_percent, order_grand_total, order_estimated_shipping_date, order_number_of_products, user_id, user_name, user_email_id, user_state_code, user_phone_number, user_platform) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)") ;
		BoundStatement var_bs_sales_orders_insert = new BoundStatement(var_cqlcmd_sales_orders_insert) ;
		var_bs_sales_orders_insert.setConsistencyLevel(ConsistencyLevel.ANY) ;

		PreparedStatement var_cqlcmd_sales_order_products_insert = prog.var_session.prepare("INSERT INTO sales.sales_order_products (order_date, order_code, product_id, product_code, product_name, product_category, product_price_each, product_sold_quantity, product_price_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)") ;
		BoundStatement var_bs_sales_order_products_insert = new BoundStatement(var_cqlcmd_sales_order_products_insert) ;
		var_bs_sales_order_products_insert.setConsistencyLevel(ConsistencyLevel.ANY) ;


		// generate random number of orders
		var_randomNumber = var_randomGenerator.nextInt(gen_no_of_orders)+1 ;
		var_randomNumber = var_randomNumber*1000 ;

		for (int o = 1; o <= var_randomNumber; o++)
		{
			var_total_generated_orders = o ;
			
			// intialize variables
			var_order_total = new BigDecimal(0) ;
			var_order_number_of_products = 0 ;

			var_product_sold_quantity = 0 ;
			var_product_price_total = new BigDecimal(0) ;

			// generate order code and order date
			var_order_code = UUID.randomUUID() ;

			// order dates code - start
			cal = Calendar.getInstance(TimeZone.getTimeZone("GMT")) ;
			var_datetime = cal.getTime() ; // get timestamp
			var_hour = cal.get(Calendar.HOUR_OF_DAY) ; // get hour
			// remove time to get date-only
			cal.set(Calendar.HOUR_OF_DAY, 0) ;
			cal.set(Calendar.MINUTE, 0) ;
			cal.set(Calendar.SECOND, 0) ;
			cal.set(Calendar.MILLISECOND, 0) ;
			var_date = cal.getTime() ; // get date-only without time
			var_order_date = var_date ;
			var_order_date_hour = var_hour ;
			var_order_timestamp = var_datetime ;
			// order dates code - end

			// System.out.format("----------------------------------------------------------- \n") ;

			// generate sales_order_products
			for (int p = 1; p <= var_randomGenerator.nextInt(9)+1; p++)
			{
				// generate products - start
				// get product
				db_results_product = prog.var_session.execute(var_bs_product.bind(new Long(var_randomGenerator.nextInt(db_total_products)))) ;

				for (Row rowp : db_results_product)
				{
					var_product_sold_quantity = var_randomGenerator.nextInt(9) ;
					if (var_product_sold_quantity < 1) var_product_sold_quantity = 1 ;
					if (var_product_sold_quantity > 9) var_product_sold_quantity = 9 ;

					var_product_price_total = rowp.getDecimal("product_price").multiply(new BigDecimal(var_product_sold_quantity)) ;
					var_product_price_total = var_product_price_total.setScale(2, RoundingMode.CEILING) ;

					// insert into sales_order_products
					prog.var_session.execute(var_bs_sales_order_products_insert.bind( var_order_date, var_order_code, rowp.getLong("product_id"), rowp.getString("product_code"), rowp.getString("product_name"), rowp.getString("product_category"), rowp.getDecimal("product_price"), var_product_sold_quantity, var_product_price_total )) ;

					// print order_products row
					// System.out.format("%s | %s | %d | %s | %s | %s | %f | %d | %f \n", timestampFormatter.format(var_order_date), var_order_code, rowp.getLong("product_id"), rowp.getString("product_code"), rowp.getString("product_name"), rowp.getString("product_category"), rowp.getDecimal("product_price"), var_product_sold_quantity, var_product_price_total) ;

					var_order_total = var_order_total.add(var_product_price_total) ;
				}
				// generate products - end

				var_order_number_of_products = p ;
			}

			// System.out.format("----------------------------------------------------------- \n") ;

			// build final order

			var_order_total = var_order_total.setScale(2, RoundingMode.CEILING) ;
			var_order_discount_percent = var_randomGenerator.nextInt(6) ;

			var_order_grand_total = var_order_total.subtract(var_order_total.multiply(new BigDecimal(var_order_discount_percent).divide(new BigDecimal(100)))) ;
	
			var_order_grand_total = var_order_grand_total.setScale(2, RoundingMode.CEILING) ;

			var_order_estimated_shipping_date.setTime(var_order_date.getTime() + (var_randomGenerator.nextInt(9)+3) * 24 * 60 * 60 * 1000) ;

			// get user and insert final order - start
			db_results_user = prog.var_session.execute(var_bs_user.bind(new Long(var_randomGenerator.nextInt(db_total_users)))) ;

			for (Row rowu : db_results_user)
			{
				// insert into sales_orders
				prog.var_session.execute(var_bs_sales_orders_insert.bind( var_order_code, var_order_date, var_order_date_hour, var_order_timestamp, var_order_total, var_order_discount_percent, var_order_grand_total, var_order_estimated_shipping_date, var_order_number_of_products, rowu.getLong("user_id"), rowu.getString("user_name"), rowu.getString("user_email_id"), rowu.getString("user_state_code"), rowu.getString("user_phone_number"), rowu.getString("user_platform") )) ;

				// print order row
				// System.out.format("%s | %s | %d | %s | %f | %d | %f | %s | %d | %d | %s | %s | %s | %s | %s \n", var_order_code, timestampFormatter.format(var_order_date), var_order_date_hour, timestampFormatter.format(var_order_timestamp), var_order_total, var_order_discount_percent, var_order_grand_total, timestampFormatter.format(var_order_estimated_shipping_date), var_order_number_of_products, rowu.getLong("user_id"), rowu.getString("user_name"), rowu.getString("user_email_id"), rowu.getString("user_state_code"), rowu.getString("user_phone_number"), rowu.getString("user_platform") ) ;
			}
			// get user and insert final order - end

			// System.out.format("=========================================================== \n") ;

		}

		System.out.format("%s | %d Orders Generated. \n", timestampFormatter.format(var_order_timestamp), var_total_generated_orders) ;

		// disconnect from cluster
		prog.disconnectFromCassCluster() ;
	}
}

