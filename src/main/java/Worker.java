import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * Created by cs.ucu.edu.ua on 14.05.2017.
 */
//public class Worker implements Runnable{
//
//    private LinkedBlockingQueue<Task>  queue;
//    Main Main = new Main();
//    FindJarAndProcess findJarAndProcess = new FindJarAndProcess();
//    WriteResult writeResult = new WriteResult();
//    NewTaskToDb newTaskToDb = new NewTaskToDb();
//    GetInfFromDb getInfFromDb = new GetInfFromDb();
//
//
//    public Worker(LinkedBlockingQueue<Task> blockingQueue){
//        this.queue = blockingQueue;
//
//    }
//
//
//    public void run(){
//
//
//
//        try {
////
////            while(true){
////                Task newtask = queue.take();
////                findJarAndProcess.FindJarAndProcess(MyJars, newtask);
////
////                writeResult.resultToDB(newtask);
////                if (newtask.getNewTask() != null) {
////                    newTaskToDb.strToDB(newtask);
////                }
////
////
////            }
//
//            FindJars findJars = new FindJars();
//            ArrayList<Interf> MyJars =  findJars.findJar();
//
//            while (!queue.isEmpty()) {
//                if (queue.size() < 2 && Main.getNew() != 0 && !findJars.scan) {//2threads
//                    try {
//                        Task myTask = getInfFromDb.GetInfFromBD(Main.getNew());
//                        queue.put(myTask);
//                    } catch (Exception o) {
//                        sleep(100);
//                        // o.printStackTrace();
//                        //and try again
//                    }
//                }
//
//                final File folder = new File("C:/Users/cs.ucu.edu.ua/IdeaProjects/papochka1");
//                Task newOne = queue.poll();
//                try {
//                    Task task = findJarAndProcess.FindJarAndProcess(MyJars, newOne);
//                    writeResult.resultToDB(task);
//                    if (task.getNewTask() != null) {
//                        newTaskToDb.strToDB(task);
//                    }
//                } catch (Exception a) {
//                    // a.printStackTrace();
//                    Connection con = new GetConnection().getCon();
//                    String s = "UPDATE Task " +
//                            " SET Status = "+Status.status.FAILED.getValue()+//failed
//                            " WHERE ID = ? ";
//                    PreparedStatement pstmt1 = con.prepareStatement(s);
//                    pstmt1.setInt(1, newOne.getId());
//                    pstmt1.executeUpdate();
//                }
//
//
//                System.out.println("---------------------------------------" + Thread.currentThread().getName());
//            }
//            if (findJars.scan && queue.isEmpty()){
//                findJars.scan = false;
//                run();
//            }
//            sleep(100);
//            System.out.print("I wait  "+Thread.currentThread().getName());
//            if (!queue.isEmpty()){
//                run();
//            }
//        }catch (Exception e){
//           // e.printStackTrace();
//            //.out.println(e.getMessage());
////            System.out.print("LOL/Error");
//
//        }
//    }
//}
