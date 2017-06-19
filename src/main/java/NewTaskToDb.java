import java.sql.*;

/**
 * Created by cs.ucu.edu.ua on 22.05.2017.
 */
public class NewTaskToDb {
    public synchronized void strToDB(Task task) throws Exception{
        Connection con = new GetConnection().getCon();

        int clientID=task.getClientID();
        int projectID=task.getProjectID();
        String newTask = task.getNewTask();

        String sql = "insert into Task (Project_ID, Data, Parent_Task_ID, Status) values ( ?, ?, ?, 0)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, projectID);
        stmt.setString(2, newTask);
        stmt.setInt(3, task.getId());
        stmt.executeUpdate();

    }
}
