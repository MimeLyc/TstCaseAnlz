import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CoveredFactory {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        CoveredFactory test = new CoveredFactory("/home/mimelyc/PGWork/tacocofork");
        CoveredProj TP = test.getProjList().get(0);
        System.out.println("**"+TP.getName());
        for (CoveredClass c:TP.getClassList()
             ) {
            System.out.println("*"+c.getId()+":"+c.getName());
            for (CoveredMethod cm:c.getMethods()
                 ) {
                System.out.println(cm.getId()+":"+cm.getName()+":"+cm.getRange()[0]+"~"+cm.getRange()[1]);
            }
        }
    }

    private ArrayList<CoveredProj> projList = new ArrayList<CoveredProj>();
    private  DbService db;

    public ArrayList<CoveredProj> getProjList() {
        return projList;
    }

    public CoveredFactory(String path) throws SQLException, IOException, ClassNotFoundException {
        db = new DbService(path+"/output");
        db.connect();
        this.iniProj();
    }
    /**
     * 项目的root地址，实际还是根据tacocoDB文件中的SOURCE获取
     */
    public void iniProj() throws SQLException, IOException, ClassNotFoundException {
//        DbService db = new DbService(path+"/output");
        ResultSet rs = db.excuteQuery("Select * from PROJECT P;");
        while(rs.next()){
            CoveredProj tempPro= new CoveredProj();
            tempPro.setId(rs.getInt(1));
//            System.out.println(rs.getInt(1));
            tempPro.setName(rs.getString(2));
//            System.out.println(tempPro.getId()+":"+tempPro.getName());
            this.iniClass(tempPro);
//            System.out.println("over");
            projList.add(tempPro);
        }
        db.closeConnection();
    }

    private void iniClass(CoveredProj pro) throws SQLException, IOException, ClassNotFoundException {
        ResultSet rs = db.excuteQuery("Select * from SOURCE where PROJECT_ID="+pro.getId()+";");
//        System.out.println("test");
        ArrayList<CoveredClass> classList = new ArrayList<>();
        while (rs.next()){
            CoveredClass tempClass = new CoveredClass();

            tempClass.setId(rs.getInt(1));
            tempClass.setName(rs.getString(2));
            tempClass.setTotalLine(rs.getInt(3));

            classList.add(tempClass);
        }

        pro.setClassList(classList);
        this.iniMethod(pro);
    }

    private void iniMethod(CoveredProj proj) throws SQLException, IOException, ClassNotFoundException {
        ResultSet rs = db.excuteQuery("Select * from METHOD_INFO m where PROJECT_ID="+proj.getId()+";");
        while(rs.next()){
            String className = rs.getString(2).split(",")[0].substring(0)+".java";
            String methodName = rs.getString(2).split(",")[1].split("\"")[0];
//            System.out.println(className+"dbmc");
            for (CoveredClass cc:proj.getClassList()
                 ) {
                if (cc.getName().equals(className)){
//                    System.out.println("11");
                    CoveredMethod tempMethod = new CoveredMethod();
                    tempMethod.setId(rs.getInt(1));
                    int[] range = new int[2];
                    range[0] = rs.getInt(3);
                    range[1] = rs.getInt(4);
                    tempMethod.setRange(range);
                    tempMethod.setName(methodName);
                    int branch = rs.getInt(6);
                    tempMethod.setBranchNum(branch);
                    cc.addMethods(tempMethod);
                }
            }



        }

    }










}
