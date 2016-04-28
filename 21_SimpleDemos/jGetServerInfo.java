import java.net.* ;

public class jGetServerInfo
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("-------------------------------------------------------------------- \n") ;

			System.out.format("Server IP   : %s \n\n", InetAddress.getLocalHost().getHostAddress() ) ;
			System.out.format("Server Name : %s : %s \n\n",  InetAddress.getLocalHost().getCanonicalHostName(), InetAddress.getLocalHost().getHostName() ) ;

			System.out.println("-------------------------------------------------------------------- \n\n") ;
		}
		catch (Exception e)
		{
			System.out.println("Error :( \n") ;
		}
	}
}
