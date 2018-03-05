import java.util.ArrayList;

public class Project {
//    private ArrayList<CoveredClass> classList = new ArrayList<CoveredClass>();
    private String name = "";
    private int id=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public void setClassList(ArrayList<CoveredClass> list){
//        this.classList = list;
//    }
//
//    public ArrayList<CoveredClass> getClassList() {
//        return classList;
//    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}
