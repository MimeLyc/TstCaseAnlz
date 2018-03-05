import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DbService {

    File dbfile = null;
    private static final String Class_Name = "org.sqlite.JDBC";
    private Connection connection = null;
    public DbService(String path) throws SQLException, ClassNotFoundException {
        dbfile = this.setDbFile(path);
    }

    public File getDbfile() {
        return dbfile;
    }

    /**
     * 获取db文件
     * @param path projectrootpath/output
     * @return dbFile
     */
    private File setDbFile(String path) throws SQLException, ClassNotFoundException {
        File dbFile = null;
        File dir = new File(path);
//        System.out.println(dir);
        for(File f:dir.listFiles()){
            if(f.getName().contains(".db")){
                System.out.println(f.getName());
                dbFile = f;
                break;
            }else{
                continue;
            }
        }
        return dbFile;
    }


    public ResultSet excuteQuery(String query) throws IOException, SQLException, ClassNotFoundException {

        // 0 连接SQLite的JDBC
//        Connection connection = null;

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        ResultSet rs = statement.executeQuery(query);
//        while(rs.next()){
////            System.out.println(rs.getString(count));
//            dbTestCase.add(rs.getString(1));
//        }
        return rs;

    }
    public void connect() throws SQLException, ClassNotFoundException {
        connection = createConnection(dbfile);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    private Connection createConnection(File dbFile) throws ClassNotFoundException, SQLException {
        Class.forName(Class_Name);
        return DriverManager.getConnection("jdbc:sqlite:"+dbFile.getAbsolutePath());
    }





}
