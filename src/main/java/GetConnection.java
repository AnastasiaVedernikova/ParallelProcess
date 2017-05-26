import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by cs.ucu.edu.ua on 22.05.2017.
 */
public class GetConnection {
    public Connection getCon() throws Exception{
       // Class.forName(className);
        String server = "localhost\\SOLEXPRESS";//Q?
        int port = 1433;
        String database = "Parallel_Processing";
        String Url = "jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database + ";integratedSecurity=true";
        Connection con = DriverManager.getConnection(Url);

        return con;
    }
}
