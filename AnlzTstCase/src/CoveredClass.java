import java.util.ArrayList;

public class CoveredClass {
    private int totalLine = 0;
    private int cvrdLineNum=0;
    private String name = "";
    private ArrayList<CoveredMethod> methods = new ArrayList<>();
    private ArrayList<CoveredMethod> cvrdMthds = new ArrayList<CoveredMethod>();
    private ArrayList<Integer> cvrdLines = new ArrayList<>();
    private int id=0;

    public int getCvrdLineNum() {
        return cvrdLineNum;
    }


    public ArrayList<Integer> getCvrdLines() {
        return cvrdLines;
    }

    public void addCvrdLines(int line) {
        this.cvrdLines.add(line);
        this.cvrdLineNum++;
    }

    public String cvdLine2String(){

        String result = ";";
        for (int l:cvrdLines
                ) {
            result += (l+";");
        }
        return result;

    }

    public String method2String(){
        String result = ";";
        for (CoveredMethod cm:methods
             ) {
            result += (cm.getId()+";");
        }
        return result;
    }

    public ArrayList<CoveredMethod> getMethods() {
        return methods;
    }

    public void addMethods(CoveredMethod methods) {
        this.methods.add(methods);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCoveredLine() {
        int result = 0;
        for (CoveredMethod m:cvrdMthds
             ) {
            result+=m.getCvrdLine().size();
        }
        return result;
    }

    public int getTotalLine(){
        return this.totalLine;
    }

    public void setTotalLine(int ttl){
        this.totalLine = ttl;
    }

    public ArrayList<CoveredMethod> getCvrdMthds() {
        return cvrdMthds;
    }

    public void addCvrdMthds(CoveredMethod cm){
        cvrdMthds.add(cm);
    }

}
