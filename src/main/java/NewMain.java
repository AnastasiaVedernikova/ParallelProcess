/**
 * Created by cs.ucu.edu.ua on 03.06.2017.
 */
import com.microsoft.sqlserver.jdbc.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by cs.ucu.edu.ua on 22.05.2017.
 */

public class NewMain {

//    public int getID() throws Exception{
//        Connection con = new GetConnection().getCon();
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT Max(id) from Task");
//        String LastID ="";
//        while(rs.next()) {
//            LastID = rs.getString(1);
//        }
//        return Integer.parseInt(LastID);
//    }
    public int getNew() throws Exception{
        Connection con = new GetConnection().getCon();
        Statement stmt = con.createStatement();
        Status st = new Status();

        ResultSet rs = stmt.executeQuery("SELECT Top 1 ID from Task where Status="+ Status.status.NEW_ONE.getValue());
        int LastID=0;
        while(rs.next()) {
            LastID = rs.getInt(1);
        }
        return LastID;
    }

    public static void main(String[] args){
        Main Main = new Main();
        GetInfFromDb getInfFromDb = new GetInfFromDb();
        LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
     //   FindJarAndProcess findJarAndProcess = new FindJarAndProcess();
//        WriteResult writeResult = new WriteResult();
//        NewTaskToDb newTaskToDb = new NewTaskToDb();
       // FromDbToXml fromDbToXml = new FromDbToXml();

        try {
            Connection con = new GetConnection().getCon();//заповнили чергу
            while(queue.size()<2){
                Task task = getInfFromDb.GetInfFromBD(Main.getNew());
                String s = "UPDATE Task " +
                            " SET Status = "+Status.status.IN_PROCESS.getValue() +//in process
                            " WHERE ID = ? ";
                PreparedStatement pstmt = con.prepareStatement(s);
                pstmt.setInt(1, task.getId());
                pstmt.executeUpdate();
                queue.put(task);

            }
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT MAX(id) from Task");//@@IDENTITY" );
//            String last_id="";
//            while(rs.next()) {
//                last_id  = rs.getString(1);
//            }
//            int LastId = Integer.parseInt(last_id);
//            // System.out.print(LastId);
//            for (int i=1; i<=LastId; i++) {//not from first must be
//                Task task = getInfFromDb.GetInfFromBD(i);
//                // System.out.print(task.getData()+"\n");
//                // System.out.print(StringUtils.isEmpty(task.getData()));
//                if(!StringUtils.isEmpty(task.getData()) && !(task.getClientID()== 0)) {
//                    //
//                    String s = "UPDATE Task " +
//                            " SET Status = "+Status.status.IN_PROCESS.getValue() +//in process
//                            " WHERE ID = ? ";
//                    PreparedStatement pstmt1 = con.prepareStatement(s);
//                    // int id = ClientData.getId();
//                    pstmt1.setInt(1, i);
//                    pstmt1.executeUpdate();
//                    queue.put(task);
//                }
//
//            }

            //System.out.print(queue);

            NewWorker w1 = new NewWorker(queue);
            NewWorker w2 = new NewWorker(queue);
            Thread t = new Thread(w1);
            Thread t2 = new Thread(w2);
            t.start();
            t2.start();
            t.join();
            t2.join();


        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
}

