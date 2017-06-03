

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class NewMain {

    public int getNew(String ProjectIds) throws Exception{
        Connection con = new GetConnection().getCon();
        Statement stmt = con.createStatement();
        Status st = new Status();
        String sql = "SELECT Top 1 ID from Task where Status="+ Status.status.NEW_ONE.getValue()+" and Project_ID in "+ ProjectIds;

        ResultSet rs = stmt.executeQuery(sql);
        int LastID=0;
        while(rs.next()) {
            LastID = rs.getInt(1);
        }
        return LastID;
    }

    public static void main(String[] args){
        NewMain Main = new NewMain();
        GetInfFromDb getInfFromDb = new GetInfFromDb();
        LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();

        try {
            ArrayList<Interf> MyJars = new FindJars().findJar();
            String ProjectIds = " ( ";
            for (int i = 0; i < MyJars.size(); i++) {
                if (ProjectIds != " ( ") {
                    ProjectIds += ", ";
                }
                ProjectIds += MyJars.get(i).getProjectID();
            }
            ProjectIds+=" )";

            Connection con = new GetConnection().getCon();//заповнили чергу
            NewWorker w1 = new NewWorker(queue, MyJars);
            NewWorker w2 = new NewWorker(queue, MyJars);
            Thread t1 = new Thread(w1);
            Thread t2 = new Thread(w2);
            t1.start();
            t2.start();
            while(true) {
                if (Main.getNew(ProjectIds) !=0){
                    while (queue.size() < 2 ) {
                        Task task = getInfFromDb.GetInfFromBD(Main.getNew(ProjectIds));
                        String s = "UPDATE Task " +
                                " SET Status = " + Status.status.IN_PROCESS.getValue() +//in process
                                " WHERE ID = ? ";
                        PreparedStatement pstmt = con.prepareStatement(s);
                        pstmt.setInt(1, task.getId());
                        pstmt.executeUpdate();
                        queue.put(task);
                    }
                }else{
                    sleep(100);
                    if (Main.getNew(ProjectIds) == 0){
                        t1.join();
                        t2.join();
                        break;
                    }
                }

            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
}

