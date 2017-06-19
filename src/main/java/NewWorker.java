import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * Created by cs.ucu.edu.ua on 03.06.2017.
 */
public class NewWorker implements Runnable {

    private LinkedBlockingQueue<Task> queue;
    ArrayList<Interf> MyJars;
    FindJarAndProcess findJarAndProcess = new FindJarAndProcess();
    WriteResult writeResult = new WriteResult();
    NewTaskToDb newTaskToDb = new NewTaskToDb();


    public NewWorker(LinkedBlockingQueue<Task> blockingQueue, ArrayList<Interf> MyJars) {
        this.queue = blockingQueue;
        this.MyJars = MyJars;
    }


    public void run() {

        while (true) {
            try {
                Task newtask = queue.poll();
                if (newtask != null) {
                    try {
                        findJarAndProcess.FindJarAndProcess(MyJars, newtask);
                        writeResult.resultToDB(newtask);
                        if (newtask.getNewTask() != null) {
                            newTaskToDb.strToDB(newtask);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Connection con = new GetConnection().getCon();
                        String s = "UPDATE Task " +
                                " SET Status = 3"+//failed
                                " WHERE ID = ? ";
                        PreparedStatement pstmt1 = con.prepareStatement(s);
                        pstmt1.setInt(1, newtask.getId());
                        pstmt1.executeUpdate();
                    }

                } else {
                    sleep(100);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
