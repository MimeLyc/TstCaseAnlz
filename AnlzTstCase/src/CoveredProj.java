import java.util.ArrayList;

public class CoveredProj extends Project{
    private ArrayList<CoveredClass> classList = new ArrayList<CoveredClass>();

    public String class2String(){
        String result = ";";
        for (CoveredClass cc:classList
                ) {
            result += (cc.getId()+";");
        }
        return result;



    }

    public void setClassList(ArrayList<CoveredClass> list){
        this.classList = list;
    }

    public ArrayList<CoveredClass> getClassList() {
        return classList;
    }


}
