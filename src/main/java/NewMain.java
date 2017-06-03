

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class NewMain {

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
        NewMain Main = new NewMain();
        GetInfFromDb getInfFromDb = new GetInfFromDb();
        LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();
        try {
            Connection con = new GetConnection().getCon();//заповнили чергу
            NewWorker w1 = new NewWorker(queue);
            NewWorker w2 = new NewWorker(queue);
            Thread t1 = new Thread(w1);
            Thread t2 = new Thread(w2);
            t1.start();
            t2.start();
            while(true) {
                if (Main.getNew() !=0){
                    while (queue.size() < 2 ) {
                        Task task = getInfFromDb.GetInfFromBD(Main.getNew());
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
                    if (Main.getNew() == 0){
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

