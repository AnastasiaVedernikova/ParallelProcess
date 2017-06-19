

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class NewMain {

    public Task getNew( String nsql) throws Exception{//String ProjectIds,
        Connection con = new GetConnection().getCon();
        Statement stmt = con.createStatement();
        Task t = null;
        try {
            ResultSet rs = stmt.executeQuery(nsql);
            while (rs.next()) {
                t = new Task();
                t.setId(rs.getInt("ID"));
                t.setProjectID(rs.getInt("Project_ID"));
                t.setClientID(rs.getInt("Client_ID"));
                t.setData(rs.getString("Data"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return t;
    }

    public static void main(String[] args){
        // TODO: tomcat - veb- підняти локальний томкат і получити мін сторінку
        NewMain Main = new NewMain();
        LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();

        try {
            ArrayList<Interf> MyJars = FindJars.findJar();

            if (MyJars.size()==0){
                System.out.print("Error jars");
                System.exit(0);
            }
                StringBuffer buf = new StringBuffer("(");


            for (int i = 0,k=MyJars.size(),f=k-1; i < k; i++) {
                buf.append(MyJars.get(i).getProjectID());
                if(i<f) {
                    buf.append(',');
                }

            }
            buf.append(')');
            String nsql = "declare @tmp table(ID int not null, \n" +
                    "              Project_ID int not null,\n" +
                    "              Data nvarchar(max) not null,\n" +
                    "              Result nvarchar(max) null,\n" +
                    "              Parent_Task_ID int null,\n" +
                    "              Status int not null,\n" +
                    "              Client_ID int not null);\n" +
                    "               \n" +
                    "               with q as\n" +
                    "               (select top(1) T.ID, T.Project_ID, T.Data, T.Result, T.Parent_Task_ID, T.Status, P.Client_ID from Task T\n" +
                    "                join Project P on T.Project_ID = P.ID \n" +
                    "                where [Status] = 0"+"\n" +
                    "               and T.Project_ID in "+ buf.toString()+"\n" +
                    "\t\t\t   )\n" +
                    "\t\t\t   \n" +
                    "             \n" +
                    "\t\t\tupdate q with(readpast)\n" +
                    "              set [Status] = 2"+"\n" +
                    "               output deleted.ID, deleted.Project_ID, deleted.Data, deleted.Result, deleted.Parent_Task_ID, deleted.Status, deleted.Client_ID into @tmp;\n" +
                    "              \n" +
                    "               select * from @tmp;";


            Connection con = new GetConnection().getCon();
            NewWorker w1 = new NewWorker(queue, MyJars);
            NewWorker w2 = new NewWorker(queue, MyJars);
            Thread t1 = new Thread(w1);
            Thread t2 = new Thread(w2);
            t1.start();
            t2.start();
            while(true) {
                if (queue.size() < 2) {
                    Task t = Main.getNew(nsql);
                    if (t != null) {
                        queue.put(t);
                    }
                }
                else{
                    sleep(100);
                }
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
}

