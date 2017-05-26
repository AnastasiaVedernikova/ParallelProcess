import javafx.concurrent.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by cs.ucu.edu.ua on 22.05.2017.
 */
public class GetInfFromDb {
    public Task GetInfFromBD(int id) throws Exception {
//    Class.forName("GetInfFromDb");
//    String server = "localhost\\SOLEXPRESS";//Q?
//    int port = 1433;
//    String database = "Parallel_Processing";
//    String Url = "jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database + ";integratedSecurity=true";
//    Connection con = DriverManager.getConnection(Url);

    Connection con = new GetConnection().getCon();

    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT  Data FROM Task where id ="+id+";" );
    String data ="";
        while(rs.next()) {
            data = rs.getString(1);
        }



//    Statement stmt1 = con.createStatement();
//    ResultSet rs1 = stmt1.executeQuery("SELECT Status FROM Task where id ="+id+";");
    ///i dont know how to control it when for what

    Statement stmt2 = con.createStatement();
    ResultSet rs2 = stmt2.executeQuery("SELECT Project_ID FROM Task WHERE id = " +id+ ";");
    int pid = 0;//projectid
        while(rs2.next()) {
        String c = rs2.getString(1);
        pid = Integer.parseInt(c);
    }

    Statement stmt3 = con.createStatement();
    ResultSet rs3 = stmt3.executeQuery("Select Client_ID From Project JOIN Task on Project.ID = Task.Project_ID where Project_ID = "+pid);

    int cid = 0;
    while(rs3.next()) {
        String c = rs3.getString(1);
        cid = Integer.parseInt(c);
    }
   // System.out.print("id="+id+"data="+data+"cid="+cid+"pid="+pid+"\n");
    Task task = new Task();
    task.setId(id);
    task.setData(data);
    task.setClientID(cid);
    task.setProjectID(pid);
    return task;
    }
}
