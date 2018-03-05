import java.io.*;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestFactory {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        TestFactory test = new TestFactory("/home/mimelyc/PGWork/tacocofork");
        TestProj tp = test.getTestProj().get(0);
        System.out.println("**"+tp.getName());
        for (TestClass tc:tp.getList()
             ) {
            System.out.println("*"+tc.getName()+":"+tc.getCaseCount());
            for (TestCase tca:tc.getCaseList()
                 ) {
                System.out.println(tca.getId()+":"+tca.getName());
            }
        }



    }


    ArrayList<TestProj> testProj = new ArrayList<>();
    DbService db;
    private static String dbpath="";
    public TestFactory(String path) throws IOException, SQLException, ClassNotFoundException {
        db = new DbService(path+"/output");
        dbpath = path+"/output";
        db.connect();
        this.iniTestProject();
    }

    public TestFactory(){

    };

    public ArrayList<TestProj> getTestProj() {
        return testProj;
    }

    /**
     * 初始化项目下的测试类
     */
    private void iniTestProject() throws IOException, SQLException, ClassNotFoundException {
        ResultSet rs = db.excuteQuery("Select * from PROJECT P;");
        while(rs.next()){
            Project tempPro= new TestProj();
            tempPro.setId(rs.getInt(1));
//            System.out.println(rs.getInt(1));
            tempPro.setName(rs.getString(2));
//            System.out.println(tempPro.getId()+":"+tempPro.getName());
            this.iniClass(tempPro);
//            System.out.println("over");
            testProj.add((TestProj)tempPro);
        }
        db.closeConnection();

    }

    /**
     * 设置testcase下被覆盖类覆盖信息（这里是分支覆盖）
     * @param tca
     */
    public void setMethodCoverage(TestCase tca) throws SQLException, ClassNotFoundException, IOException {
        db = new DbService(dbpath);
        db.connect();
//        System.out.println(dbpath+";"+tca.getId());
        ResultSet rs = db.excuteQuery("select * from method_coverage where test_id="+tca.getId()+";");
//        System.out.println("get");
        while (rs.next()){
            int methodid = rs.getInt(2);
            int coveredBranch = rs.getInt(3);
            int proid = rs.getInt(5);
            for (CoveredProj pro:tca.getCoverPro()){
                if(pro.getId()==proid){
                    for (CoveredClass cc:pro.getClassList()){
                        if (cc.method2String().contains(";"+methodid+";")){
                            for (CoveredMethod cm:cc.getMethods()){
                                if (cm.getId() == methodid){
//                                    System.out.println(coveredBranch);
                                    cm.setBranchNum(coveredBranch);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }


        }

        db.closeConnection();
    }

    private void iniClass(Project pro) throws SQLException, IOException, ClassNotFoundException {
        ResultSet rs = db.excuteQuery("Select * from TESTCASE where PROJECT_ID="+pro.getId()+";");
//        System.out.println("test");
//        ArrayList<TestClass> classList = new ArrayList<>();
        while (rs.next()){
            if(rs.getString(2).split("\\(").length<=1) {
                continue;
            }

            TestClass tempClass = new TestClass();
            tempClass.setName(rs.getString(2).split("\\(")[1].split("\\)")[0]);

            TestCase tempCase = new TestCase();
            tempCase.setId(rs.getInt(1));
            tempCase.setName(rs.getString(2).split("\\(")[0]);

            boolean contain = false;
            for (TestClass tc:((TestProj)pro).getList()
                 ) {
                if(tc.getName().equals(tempClass.getName())){
                    tc.addCase(tempCase);
                    contain = true;
                }
            }
            if(contain==false){
                tempClass.addCase(tempCase);
                ((TestProj)pro).addTestClass(tempClass);
            }

        }
//        pro.setClassList(classList);
//        this.iniMethod(pro);
    }



}
