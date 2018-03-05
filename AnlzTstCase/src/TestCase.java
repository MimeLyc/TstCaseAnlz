import java.util.ArrayList;

/**
 * 测试用例
 */
public class TestCase {
//    是否被调用（覆盖到）
    private boolean covered = false;
    private String name ="";
//    被覆盖到次数
    private int cvrCount=0;

    private int id = 0;

    private int cbranch = 0;
//    覆盖到的项目/类/方法，这里只保留id
    private ArrayList<CoveredProj> coverPro = new ArrayList<>();

    public int getCbranch(){
        cbranch=0;
        for (CoveredProj cp:coverPro
             ) {
            for (CoveredClass cc:cp.getClassList()
                 ) {
                for (CoveredMethod cm:cc.getMethods()
                     ) {
                    cbranch+=cm.getBranchNum();
                }
            }
        }
        return cbranch;
    }

    public String cvdPro2String(){
        String result = ";";
        for (CoveredProj tp:coverPro
                ) {
            result += (tp.getId()+";");
        }
        return result;



    }

    public void addCoverPro(CoveredProj cp){
        this.coverPro.add(cp);

    }

    public ArrayList<CoveredProj> getCoverPro() {
        return coverPro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private void setCovered(){
        this.covered=true;
    }
    public boolean getCovered(){
        return this.covered;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }


    public void addCount(){
        this.cvrCount++;
    }

    public int getCvrCount() {
        return cvrCount;
    }
}
