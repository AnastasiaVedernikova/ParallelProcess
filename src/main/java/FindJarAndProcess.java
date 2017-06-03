import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by cs.ucu.edu.ua on 22.05.2017.
 */
public class FindJarAndProcess {
    public Task FindJarAndProcess( ArrayList<Interf> MyJars,Task ClientData) throws Exception {

        int ClientId = ClientData.getClientID();
        int ProjectId = ClientData.getProjectID();
        String data = ClientData.getData();
        Task task = new Task();

        for (int i = 0; i < MyJars.size(); i++) {

            int client = MyJars.get(i).getClientID();
            int project = MyJars.get(i).getProjectID();

            if (client == ClientId && project == ProjectId) {
                if (!data.contains("/")) {
                    task = MyJars.get(i).DoAndBornTasks(ClientData);
                } else {
                    task = MyJars.get(i).doTask(ClientData);
                }
            }
        }
        if (task.getResult()== null){
            Connection con = new GetConnection().getCon();
            String s = "UPDATE Task " +
                    " SET Status = "+Status.status.FAILED.getValue() +
                    " WHERE ID = ? ";
            PreparedStatement pstmt1 = con.prepareStatement(s);
            pstmt1.setInt(1, ClientData.getId());
            pstmt1.executeUpdate();
        }
        return task;
    }

}
