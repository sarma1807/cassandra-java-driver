
import com.datastax.driver.core.* ;
import java.lang.Long ;
import java.util.Date ;
import java.util.UUID ;
import java.util.Random ;

public class SalesApp_GenerateUsers {

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
		Long		var_user_id				= new Long(1) ;
		String		var_user_name			= new String("") ;
		String		var_user_email_id		= new String("") ;
		String		var_user_state_code		= new String("") ;
		String		var_user_phone_number	= new String("") ;
		String		var_user_platform		= new String("") ;

		Random		var_randomGenerator		= new Random() ;
		int			var_randomNumber		= 0 ;

		Long		gen_no_of_users			= new Long(100000) ;  // this will generate 100,000 users

		ResultSet	db_results ;


		// instantiate the class
		SalesApp_GenerateUsers prog = new SalesApp_GenerateUsers() ;

		// connect to cluster and keyspace
		prog.connectToCassCluster("192.168.0.34", "sales", "no") ;

		// prepare cql bound statements
		PreparedStatement var_cqlcmd_email = prog.var_session.prepare("SELECT email_server FROM sales.lookup_email_servers WHERE id = ?") ;
		BoundStatement var_bs_email = new BoundStatement(var_cqlcmd_email) ;

		PreparedStatement var_cqlcmd_state = prog.var_session.prepare("SELECT state_code FROM sales.lookup_usa_states WHERE id = ?") ;
		BoundStatement var_bs_state = new BoundStatement(var_cqlcmd_state) ;

		PreparedStatement var_cqlcmd_platform = prog.var_session.prepare("SELECT platform FROM sales.lookup_user_platforms WHERE id = ?") ;
		BoundStatement var_bs_platform = new BoundStatement(var_cqlcmd_platform) ;

		PreparedStatement var_cqlcmd_user_insert = prog.var_session.prepare("INSERT INTO sales.users (user_id, user_name, user_email_id, user_state_code, user_phone_number, user_platform) VALUES (?, ?, ?, ?, ?, ?)") ;
		BoundStatement var_bs_user_insert = new BoundStatement(var_cqlcmd_user_insert) ;


		// generate users
		System.out.format("------------------------------------------------------- \n");
		for (var_user_id = new Long(1); var_user_id <= gen_no_of_users; var_user_id++)
		{
			var_user_name = UUID.randomUUID().toString().replace("-", "").substring(1, 11) ;

			// generate email id - start
			var_randomNumber = var_randomGenerator.nextInt(11) ;
			if (var_randomNumber < 01) var_randomNumber = 1 ;
			if (var_randomNumber > 10) var_randomNumber = 10 ;

			// get email server
			db_results = prog.var_session.execute(var_bs_email.bind(var_randomNumber)) ;

			for (Row row : db_results)
			{
				var_user_email_id = var_user_name.concat(row.getString("email_server")) ;
			}
			// generate email id - end

			// generate state code - start
			var_randomNumber = var_randomGenerator.nextInt(52) ;
			if (var_randomNumber < 01) var_randomNumber = 1 ;
			if (var_randomNumber > 51) var_randomNumber = 51 ;

			// get state code
			db_results = prog.var_session.execute(var_bs_state.bind(var_randomNumber)) ;

			for (Row row : db_results)
			{
				var_user_state_code = row.getString("state_code") ;
			}
			// generate state code - end

			// generate phone number - start
			var_user_phone_number	= new String("") ;
			for (int i = 1; i <= 10 ; i++ )
			{
				var_randomNumber = var_randomGenerator.nextInt(11) ;
				if (var_randomNumber < 2) var_randomNumber = 2 ;
				if (var_randomNumber > 9) var_randomNumber = 9 ;

				if(i == 4 || i == 7) var_user_phone_number = var_user_phone_number.concat("-") ;
				
				var_user_phone_number = var_user_phone_number.concat(Integer.toString(var_randomNumber)) ;
			}
			// generate phone number - end

			// generate user platform - start
			var_randomNumber = var_randomGenerator.nextInt(11) ;
			if (var_randomNumber < 01) var_randomNumber = 1 ;
			if (var_randomNumber > 10) var_randomNumber = 10 ;

			// get user platform
			db_results = prog.var_session.execute(var_bs_platform.bind(var_randomNumber)) ;

			for (Row row : db_results)
			{
				var_user_platform = row.getString("platform") ;
			}
			// generate user platform - end

			// insert user into users table
			prog.var_session.execute(var_bs_user_insert.bind(var_user_id, var_user_name, var_user_email_id, var_user_state_code, var_user_phone_number, var_user_platform)) ;

			// print EOT
			// System.out.format("%d | %s | %s | %s | %s | %s \n", var_user_id, var_user_name, var_user_email_id, var_user_state_code, var_user_phone_number, var_user_platform) ;

		}

		System.out.format("%d Users generated. \n", var_user_id);
		System.out.format("------------------------------------------------------- \n");

		// disconnect from cluster
		prog.disconnectFromCassCluster() ;
	}
}

