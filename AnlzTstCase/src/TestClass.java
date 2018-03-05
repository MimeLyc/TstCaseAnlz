import java.util.ArrayList;

/**
 * 测试类
 */
public class TestClass {
//    测试类包含的测试用例
    private ArrayList<TestCase> caseList = new ArrayList<TestCase>();
    private String name = "";
//    包含测试用例个数
    private int caseCount=0;
//    使用到的次数
    private int coverCount=0;

    private int cbranch=0;

    public int getCbranch(){
        cbranch = 0;
        for (TestCase tc:caseList){
            cbranch+=tc.getCbranch();
        }
        return cbranch;

    }

    public String case2String(){
        String result = ";";
        for (TestCase tc:caseList
                ) {
            result += (tc.getId()+";");
        }
        return result;



    }

    public int getCaseCount(){
        return this.caseCount;
    }

    public int getCoverCount(){
        return this.coverCount;
    }

    public void addCoverCount(){
        this.coverCount++;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addCase(TestCase tc){
        this.caseList.add(tc);
        this.caseCount++;
    }

    public ArrayList<TestCase> getCaseList() {
        return caseList;
    }
}
