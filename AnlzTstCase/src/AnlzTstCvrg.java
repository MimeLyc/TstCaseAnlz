

//分析测试覆盖测试用例的情况，tacoco执行结果默认保存在项目根目录的output文件夹下

import strategies.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class AnlzTstCvrg {
//    项目，包含测试的类集合（TestClass）,测试类包含测试用例集合（TestCase）
    private ArrayList<TestProj> testPro= new ArrayList<TestProj>();

    private ArrayList<CoveredProj> projs= new ArrayList<CoveredProj>();
//    数据库中包含的测试用例集合（重复出现记为次数+1）
//    private ArrayList<String> TestCase =new ArrayList<String>();
    DbService db ;



    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
//        System.out.println("你好");



        String path = "/home/mimelyc/PGWork/30Programs/lambdaj";
        AnlzTstCvrg test = new AnlzTstCvrg();
        test.run(path);

    }

    /**
     *
     * @param path 项目根目录
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public void run(String path) throws IOException, SQLException, ClassNotFoundException {
        db = new DbService(path+"/output");
        db.connect();
        CoveredFactory cf = new CoveredFactory(path);
        projs = cf.getProjList();

        TestFactory tf = new TestFactory(path);
        testPro = tf.getTestProj();

        this.mapTst2Case();
        int[][] cvdMatrix = this.getMthdCvrMatrix();
        GreedyAART sortByCombine = new GreedyAART(cvdMatrix,10);
        int[] sortedSeq = sortByCombine.getSelectedTestSequence();
        for (int i:sortedSeq
             ) {
            System.out.println(allTestClass.get(i).getName());
        }
//        this.outputCBranch();
    }

    public DbService getDb(String dirPath) throws SQLException, ClassNotFoundException {
        db = new DbService(dirPath+"/output");
        System.out.println(dirPath);
        if (db.getDbfile()==null){
//            System.out.println("!no db");
            return null;
        }
        return db;
    }

    public void saveTClassACMethod(String dirPath) throws SQLException, ClassNotFoundException, IOException {
//DB 由外部获得
//        db = new DbService(dirPath+"/output");
        db.connect();
        CoveredFactory cf = new CoveredFactory(dirPath);
        projs = cf.getProjList();

        TestFactory tf = new TestFactory(dirPath);
        testPro = tf.getTestProj();

        this.mapTst2Case();
        int[][] cvdMatrix = this.getMthdCvrMatrix();

        String fileName  = dirPath+"/TestClassList.txt";
//            System.out.println(d+"/"+fileName);
        File file = new File(fileName);
        if (!file.exists()){
            file.createNewFile();
        }

        FileWriter out = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(out);
        for (TestClass tc:allTestClass){
            bw.write(tc.getName());
            bw.newLine();
        }
        bw.flush();
        bw.close();
        out.close();

        fileName  = dirPath+"/CoveredMethodMatrix.txt";
//            System.out.println(d+"/"+fileName);
        file = new File(fileName);
        if (!file.exists()){
            file.createNewFile();
        }

        out = new FileWriter(file);
        bw = new BufferedWriter(out);
        for (int i=0;i<cvdMatrix.length;i++){
            for (int j=0;j<cvdMatrix[0].length;j++){
                bw.write(cvdMatrix[i][j]+"");
            }
            bw.newLine();
        }
        bw.flush();
        bw.close();
        out.close();

        System.out.println(dirPath+":Over "+allTestClass.size()+" testClasses "+cvdMatrix.length+" coresspond");




    }


    /**
     * Start--------------构造关于方法的测试类的覆盖矩阵
     */
    private ArrayList<TestClass> allTestClass = new ArrayList<>();
    private ArrayList<CoveredMethod> allMethod = new ArrayList<>();
    public int[][] getMthdCvrMatrix(){
//        ArrayList<TestClass> allTestClass = new ArrayList<>();
        allTestClass = this.getAllTestClass();
        int tclen = allTestClass.size();
//        ArrayList<CoveredMethod> allMethod = new ArrayList<>();
        allMethod = this.getAllCoveredMethod();
        int mlen = allMethod.size();


        int[][] cvdMatrix = new int[tclen][mlen];
        for(int i=0;i<tclen;i++){

            for (int j=0;j<mlen;j++){
                boolean isCover = isCovered(allTestClass.get(i),allMethod.get(j));
                if(isCover){
                    cvdMatrix[i][j] = 1;
                }else{
                    cvdMatrix[i][j] = 0;
                }
            }


        }

        return cvdMatrix;

    }

    private boolean isCovered(TestClass tc,CoveredMethod cm){

        for (TestCase tca:tc.getCaseList()
             ) {
            for (CoveredProj cp:tca.getCoverPro()
                 ) {
                for (CoveredClass cc:cp.getClassList()
                     ) {
                    for (CoveredMethod cmd:cc.getMethods()
                         ) {
                        if (cmd.getId()==cm.getId()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }
    private ArrayList<TestClass> getAllTestClass(){
        ArrayList<TestClass> result = new ArrayList<>();
        for (TestProj tp:testPro
             ) {
            for (TestClass tc:tp.getList()
                 ) {
                result.add(tc);
            }
        }

        return result;

    }

    private ArrayList<CoveredMethod> getAllCoveredMethod(){
        ArrayList<CoveredMethod> result = new ArrayList<>();
        for (CoveredProj cp:projs
                ) {
            for (CoveredClass cc:cp.getClassList()
                    ) {
                for (CoveredMethod cm:cc.getMethods()
                     ) {
                    result.add(cm);
                }
            }
        }

        return result;

    }


    /**
     * END---------------构造关于方法的覆盖矩阵
     */


    public void outputCBranch() throws SQLException, IOException, ClassNotFoundException {
        this.mapTst2Case();
        for (TestProj tp:testPro
             ) {
            for (TestClass tc:tp.getList()){
                System.out.println(tp.getId()+":"+tc.getName()+" cover branch num: "+tc.getCbranch());
            }
        }
        System.out.println("============================================================");
        ArrayList<TestClass> testClass = new ArrayList<>();
        ArrayList<Integer> testClassCBranch = new ArrayList<>();

        for (TestProj tp:testPro
             ) {
            for (TestClass tc:tp.getList()){
                testClass.add(tc);
                testClassCBranch.add(tc.getCbranch());
            }

        }
        ArrayList<Integer> sortedIndex = new ArrayList<>();
        SortFactory sortFactory = new SortFactory(testClassCBranch, SortType.GREEDY);
        sortedIndex = sortFactory.getL2SSortedIndex();
        for (int i = 0;i<testClass.size();i++){
//            System.out.println(sortedIndex.get(i));
                        System.out.println(testClass.get(sortedIndex.get(i)).getName()+":"+testClassCBranch.get(sortedIndex.get(i)));
        }


    }


    public void outputBasedOnCase() throws SQLException, IOException, ClassNotFoundException {
        this.mapTst2Case();
//        db.closeConnection();
        System.out.print(String.format("%-10s","测试项目"));
        System.out.print(String.format("%-100s","测试类"));
        System.out.print(String.format("%-100s","测试用例"));
//        System.out.print(String.format("%-20s",""));
        System.out.print(String.format("%-10s","被测项目"));
        System.out.print(String.format("%-100s","被测类"));
        System.out.print(String.format("%-300s","被测方法"));
        System.out.print(String.format("%-100s","被测行"));
        System.out.println(String.format("%-100s","分支覆盖情况"));
//        int proID= 0;
        for (TestProj tp:testPro
             ) {
                System.out.print(String.format("%-10s",tp.getId()));
                String className = "";
                for (int i=0;i<tp.getList().size();i++
                 ) {
                    TestClass tc = tp.getList().get(i);
                    if (i!=0) {
                        System.out.print(String.format("%-10s"," "));
                    }
                    System.out.print(String.format("%-100s",tc.getName()));
                    for (int j = 0;j<tc.getCaseList().size();j++
                         ) {
                        TestCase tca = tc.getCaseList().get(j);
                        if(j!=0) {
                            System.out.print(String.format("%-110s"," "));
                        }
                        System.out.print(String.format("%-100s",tca.getName()));
//                        System.out.println(tca.getCoverPro().size());
                        if (tca.getCoverPro().size()==0){
                            System.out.println("Doesn't cover any line");
                            continue;
                        }
                        for (int k=0;k<tca.getCoverPro().size();k++){
                            CoveredProj cp = tca.getCoverPro().get(k);
                            if (k!=0){
                                System.out.print(String.format("%-210s"," "));
                            }
                            System.out.print(String.format("%-10s",cp.getId()));

                            CoveredProj cpIndex = this.getProjInfo(cp);
//                            System.out.println(cp.getClassList().size());
                            for (int i1=0;i1<cp.getClassList().size();i1++){

                                CoveredClass cc = cp.getClassList().get(i1);
                                if (i1!=0){
                                    System.out.print(String.format("%-220s"," "));
                                }
//                                获取被测类信息
                                cc = this.getClassInfo(cc,cpIndex);
                                System.out.print(String.format("%-100s",cc.getName()));
                                cc = this.getClassInfo(cc,cp);
                                for (int j1=0;j1<cc.getMethods().size();j1++){
                                    CoveredMethod cm = cc.getMethods().get(j1);
                                    if (j1!=0){
                                        System.out.print(String.format("%-320s"," "));
                                    }
                                    System.out.print(String.format("%-300s",cm.getName()));

                                    String lines="";
//                                    System.out.println(cm.getCvrdLine().size());
                                    for (int line:cm.getCvrdLine()){
                                        lines+=line+";";
                                    }
                                    System.out.print(String.format("%-100s",lines));

                                    int coveredBranch = cm.getBranchNum();
                                    CoveredClass sourceC = this.getClassInfo(cc,cpIndex);
                                    CoveredMethod sourceM = this.getMethodInfo(cm,sourceC);
                                    int totalBranch = sourceM.getBranchNum();
                                    System.out.println(String.format("%-100s",coveredBranch+"/"+totalBranch));



                                }

//                                System.out.print(String.format("%-30s",projs.get(cpIndex).getClassList().get(cc.getId())));

                            }







                    }


                }




            }

        }



    }

    private CoveredMethod getMethodInfo(CoveredMethod cm,CoveredClass cc){
        CoveredMethod result = new CoveredMethod();
        for (CoveredMethod tempc:cc.getMethods()
                ) {
            if(cm.getId()==tempc.getId()){
                result = tempc;
            }
        }

        return result;
    }

    private CoveredProj getProjInfo(CoveredProj tempP){
        CoveredProj result = new CoveredProj();
        for (CoveredProj tempc:projs
                ) {
            if(tempP.getId()==tempc.getId()){
                result = tempc;
            }
        }

        return result;
    }

    private CoveredClass getClassInfo(CoveredClass cc,CoveredProj cp){

        CoveredClass result = new CoveredClass();
        for (CoveredClass tempc:cp.getClassList()
             ) {
            if(cc.getId()==tempc.getId()){
                result = tempc;
            }
        }

        return result;

    }


//    private TestClass mapClass = new TestClass();
    public void mapTst2Case() throws SQLException, IOException, ClassNotFoundException {
        ResultSet rs = db.excuteQuery("Select * from STMT_COVERAGE;");
//        db.closeConnection();
        while(rs.next()){
            int proID = rs.getInt(4);
            int testID = rs.getInt(1);
            int sourceID = rs.getInt(2);
            int line = rs.getInt(3);

            for (TestProj tp:testPro
                 ) {
                if (tp.getId()==proID) {
                    for (TestClass tcl : tp.getList()
                            ) {
                        for (TestCase tca : tcl.getCaseList()
                                ) {
                            if (tca.getId()==testID){
                                this.addTst2Case(tca,proID,sourceID,line);
//                                TestFactory toAddBranch = new TestFactory();
//                                toAddBranch.setMethodCoverage(tca);
                            }
                        }
                    }
                }
            }

        }

        db.closeConnection();
        this.addBranch2TestCase();
    }

    private void addBranch2TestCase() throws SQLException, IOException, ClassNotFoundException {

        for (TestProj tp:testPro
                ) {
//            if (tp.getId()==proID) {
                for (TestClass tcl : tp.getList()
                        ) {
                    for (TestCase tca : tcl.getCaseList()
                            ) {
//                        if (tca.getId()==testID){
//                            this.addTst2Case(tca,proID,sourceID,line);
                            TestFactory toAddBranch = new TestFactory();
                            toAddBranch.setMethodCoverage(tca);
                        }
                    }
                }
//            }
//        }

    }
    private void addTst2Case(TestCase tc,int proID,int sourceid,int line){

        if (tc.cvdPro2String().contains(";"+proID+";")==false){
            CoveredProj tempP = new CoveredProj();
            tempP.setId(proID);
            tc.addCoverPro(tempP);
        }

        for (CoveredProj cp:tc.getCoverPro()
             ) {
            if(cp.getId()==proID){
//                将测试用例覆盖的类添加到测试用例下的列表中
                if (cp.class2String().contains(";"+sourceid+";")==false){
                    ArrayList<CoveredClass> tempL =cp.getClassList();
                    CoveredClass tempC = new CoveredClass();
                    tempC.setId(sourceid);
                    tempL.add(tempC);
                    cp.setClassList(tempL);
                }

                for (CoveredClass cc:cp.getClassList()
                     ) {
                    if(cc.getId()==sourceid){
                        this.mapMethod2Class(cp,cc,line);
//                        System.out.println(cc.getMethods().get(0).getCvrdLine().size());

                    }
                }



            }
        }

    }

    public void mapMethod2Class(CoveredProj cp,CoveredClass cc,int line){
        for (CoveredProj tempp:projs
             ) {
            if(tempp.getId()==cp.getId()){
                for (CoveredClass tempc:tempp.getClassList()
                     ) {
                    if (cc.getId()== tempc.getId()){
                        for (CoveredMethod tempm:tempc.getMethods()){

                            if (line>=tempm.getRange()[0]&&line<=tempm.getRange()[1]){
                                if (cc.method2String().contains(";"+tempm.getId()+";")==false) {
                                    CoveredMethod cm2add = new CoveredMethod();
                                    cm2add.setId(tempm.getId());
                                    cm2add.setName(tempm.getName());
//                                if(cm2add.getName().contains("getTag()")){
//                                    System.out.println(cm2add.getRange()[0]+":"+cm2add.getRange()[1]+":"+line);
//                                    java.lang.System.exit(0);
//                                }
                                    if (cm2add.cvdline2String().contains(";" + line + ";") == false) {
//                                    System.out.println(line);
//                                    tempm.addCvrdLine(line);
                                        cm2add.addCvrdLine(line);
//                                    cm2add.addCvrdLine(line);
                                    }
                                    if (tempm.cvdline2String().contains(";" + line + ";") == false) {
//                                    System.out.println(line);
//                                    tempm.addCvrdLine(line);
                                        tempm.addCvrdLine(line);
//                                    cm2add.addCvrdLine(line);
                                    }
                                    if (cc.method2String().contains(";" + tempm.getId() + ";") == false) {
//                                    cm2add.setId(tempm.getId());
                                        cc.addMethods(cm2add);
                                    }
                                }else {
                                    CoveredMethod cm2add = this.getMethodInfo(tempm,cc);
                                    if (cm2add.cvdline2String().contains(";" + line + ";") == false) {
//                                    System.out.println(line);
//                                    tempm.addCvrdLine(line);
                                        cm2add.addCvrdLine(line);
//                                    cm2add.addCvrdLine(line);
                                    }
                                    if (tempm.cvdline2String().contains(";" + line + ";") == false) {
//                                    System.out.println(line);
//                                    tempm.addCvrdLine(line);
                                        tempm.addCvrdLine(line);
//                                    cm2add.addCvrdLine(line);
                                    }



                                }

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

}
