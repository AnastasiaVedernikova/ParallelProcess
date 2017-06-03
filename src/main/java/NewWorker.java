import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * Created by cs.ucu.edu.ua on 03.06.2017.
 */
public class NewWorker implements Runnable{

    private LinkedBlockingQueue<Task> queue;
    //ArrayList<Interf> MyJars;
    FindJarAndProcess findJarAndProcess = new FindJarAndProcess();
    WriteResult writeResult = new WriteResult();
    NewTaskToDb newTaskToDb = new NewTaskToDb();



    public NewWorker(LinkedBlockingQueue<Task> blockingQueue){
        this.queue = blockingQueue;
        //this.MyJars = MyJars;
    }


    public void run() {
        try {
            FindJars findJars = new FindJars();
            ArrayList<Interf> MyJars =  findJars.findJar();//fix
            while (!queue.isEmpty()) {
                Task newtask = queue.take();
                try {
                    Task task = findJarAndProcess.FindJarAndProcess(MyJars, newtask);//потім поміняєм шоб не повертало
                    writeResult.resultToDB(task);
                    if (task.getNewTask() != null) {
                        newTaskToDb.strToDB(task);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Connection con = new GetConnection().getCon();
                    String s = "UPDATE Task " +
                            " SET Status = " + Status.status.FAILED.getValue() +//failed
                            " WHERE ID = ? ";
                    PreparedStatement pstmt1 = con.prepareStatement(s);
                    pstmt1.setInt(1, newtask.getId());
                    pstmt1.executeUpdate();

                }
            }
            sleep(1000);
            System.out.print("I wait  "+Thread.currentThread().getName());
            if (!queue.isEmpty()){
                run();
            }
        }catch (Exception a){
            a.printStackTrace();

        }
    }
}

