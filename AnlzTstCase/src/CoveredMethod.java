import java.util.ArrayList;

public class CoveredMethod {
    private int id=0;
    private String name="";
    private int[] range = new int[2];
    private ArrayList<Integer> cvrdLine = new ArrayList<Integer>();
    private int branchNum = 0;


    public int getBranchNum() {
        return branchNum;
    }

    public void setBranchNum(int branchNum) {
        this.branchNum = branchNum;
    }

    public String cvdline2String(){
        String result = ";";
        for (int l:cvrdLine
                ) {
            result += (l+";");
        }
        return result;




    }
    //    private int coveredTimes = 0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getRange() {
        return range;
    }

    public void setRange(int[] range) {
        this.range = range;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Integer> getCvrdLine() {
        return cvrdLine;
    }

    public void addCvrdLine(int line){
        cvrdLine.add(line);
    }



}
