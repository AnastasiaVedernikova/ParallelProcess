import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by cs.ucu.edu.ua on 22.05.2017.
 */
public class WriteResult {
    public synchronized void resultToDB(Task task) throws Exception{
        Connection con = new GetConnection().getCon();

        String result = task.getResult();
      //  if (result == "") {
            String id = "" + (task.getId());
            String sql = "UPDATE Task " +
                    " SET Result = ? " +
                    " WHERE ID = ? ";
            String s = "UPDATE Task " +
                    " SET Status = 1"+
                    " WHERE ID = ? ";
            PreparedStatement pstmt = con.prepareStatement(sql);
            PreparedStatement pstmt1 = con.prepareStatement(s);
            pstmt1.setString(1, id);
            pstmt.setString(1, result);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
            pstmt1.executeUpdate();
        }

   // }
}
