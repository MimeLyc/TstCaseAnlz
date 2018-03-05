import java.util.ArrayList;

/**
 * 项目
 */
public class TestProj extends Project{

    ArrayList<TestClass> list = new ArrayList<>();


    public void addTestClass(TestClass tc){
        list.add(tc);
    }


    public ArrayList<TestClass> getList() {
        return list;
    }
}
