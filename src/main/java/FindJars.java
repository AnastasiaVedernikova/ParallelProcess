import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by cs.ucu.edu.ua on 26.05.2017.
 */
public class FindJars {
    public boolean scan = false;
    public ArrayList<Interf> findJar() throws Exception {
        final File folder = new File("C:/Users/cs.ucu.edu.ua/IdeaProjects/papochka1");
        List<String> JarNames = new ArrayList<String>();
        ArrayList<Interf> MyJars = new ArrayList<Interf>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                findJar();
            } else {
                JarNames.add(fileEntry.getName());

            }
        }

        for (String a : JarNames) {
            String pathToJar = "C:/Users/cs.ucu.edu.ua/IdeaProjects/papochka1/" + a;
            JarFile jarFile = new JarFile(pathToJar);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl.loadClass(className);
                Constructor<?> ctor = c.getConstructor();
                Object object = ctor.newInstance();
                if (object instanceof Interf) {
                    Interf ic = (Interf) object;
                    MyJars.add(ic);
                }
            }
        }
        return MyJars;
    }
}
