import strategies.*;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AutoSort {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
//        String tes = "11112";
//        System.out.println(tes.split("")[0]);


        AutoSort test = new AutoSort("/home/mimelyc/PGWork/30Programs");
//        AutoSort test = new AutoSort();
//        test.proDir.add("/home/mimelyc/PGWork/30Programs/jackson-datatype-guava");
//        test.sortASaveByControlled("/home/mimelyc/PGWork",SortType.RANDOM,SortType.GREEDY,CalDisType.SUM,10,0.1);
//
//
        for(int i=0;i<30;i++) {
            String outPath = "/home/mimelyc/PGWork/RepeatTest"+"/"+i;
            File dirToMk = new File(outPath);
            dirToMk.mkdir();

        test.sortASaveByGenetic(outPath+"/SortedByGenetic");

//        test.iniMethodCover();


        test.sortASaveByGreedy(outPath+"/SortedByGreedy");
        test.sortASaveByART(outPath+"/SortedByART");
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.GREEDY,CalDisType.CLOSED,10,0.5);
            test.sortASaveByControlled(outPath+"/SortedByControl", SortType.RANDOM, SortType.GREEDY, CalDisType.CLOSED, 10, 0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.RANDOM,CalDisType.CLOSED,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.RANDOM,CalDisType.CLOSED,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.GREEDY,CalDisType.SUM,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.GREEDY,CalDisType.SUM,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.RANDOM,CalDisType.SUM,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.RANDOM,CalDisType.SUM,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.GREEDY,CalDisType.CLOSED,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.GREEDY,CalDisType.CLOSED,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.RANDOM,CalDisType.CLOSED,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.RANDOM,CalDisType.CLOSED,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.GREEDY,CalDisType.SUM,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.GREEDY,CalDisType.SUM,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.RANDOM,CalDisType.SUM,5,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.RANDOM,SortType.RANDOM,CalDisType.SUM,5,0.5);


        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.1);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.2);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.3);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.4);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.6);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.7);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.8);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.9);




//        test.sortASaveByControlled("/home/mimelyc/PGWork/SortedByControl",SortType.RANDOM,SortType.COMBINE,CalDisType.CLOSED,10,0.5);
        test.sortASaveByControlled(outPath+"/SortedByControl",SortType.GREEDY,SortType.COMBINE,CalDisType.CLOSED,10,0.5);
//        test.sortASaveByControlled("/home/mimelyc/PGWork/SortedByControl",SortType.RANDOM,SortType.COMBINE,CalDisType.CLOSED,10,0.5);

        }
        //        SortedByGANorandomSortedByGANorandom
    }


    private String parentDir = "";
    ArrayList<String> proDir = new ArrayList<String>();

    public AutoSort(String parentPath){
        parentDir = parentPath;
        Preparation pre = new Preparation(parentDir);
        proDir = pre.getDir();

    }

    public AutoSort(){

    }

    private ArrayList<String> getTCList(String dir) throws IOException {
        ArrayList<String> tcList = new ArrayList<>();
        File file = new File(dir+"/TestClassList.txt");

        FileReader in = new FileReader(file);
        BufferedReader br = new BufferedReader(in);

        String s ="";
        while ((s=br.readLine())!=null){
            tcList.add(s);
        }

        br.close();
        in.close();
        return tcList;
    }

    private int[][] getCovMMatrix(String dir,int size) throws IOException {
        int[][] cvdMetrix;
        File file = new File(dir+"/CoveredMethodMatrix.txt");

        FileReader in = new FileReader(file);
        BufferedReader br = new BufferedReader(in);

        String s ="";
        s = br.readLine();
        int colNum = s.length();


        cvdMetrix = new int[size][colNum];
        in = new FileReader(file);
        br = new BufferedReader(in);
        int count = 0;

        while ((s=br.readLine())!=null){
            for (int i=0;i<colNum;i++){

                cvdMetrix[count][i] = Integer.parseInt(s.split("")[i]);
            }
            count++;

        }

        br.close();
        in.close();
        return cvdMetrix;
    }



    public void sortASaveByControlled(String outputPath, SortType firstSel, SortType candidate, CalDisType type, int m,double alpha) throws IOException {

        File odirToMk = new File(outputPath);
        if(odirToMk.exists()==false) {
            odirToMk.mkdir();
        }

        File dirToMk;
        if (candidate != SortType.COMBINE) {
            dirToMk = new File(outputPath + "/" + firstSel.toString() + "_" + candidate.toString() + "_" + type.toString() +
                    "_" + m);
            dirToMk.mkdir();
        }else{
            dirToMk = new File(outputPath + "/" + firstSel.toString() + "_" + candidate.toString() + "_" + type.toString() +
                    "_" + m+"_"+alpha);
            dirToMk.mkdir();
        }
        System.out.println(firstSel.toString()+candidate.toString()+type.toString()+m+":begin");
        for (String dir:proDir
                ) {
//        temp

//        String dir = outputPath;
//
            boolean runable = this.isRunable(dir);
            if (!runable){
                continue;
            }
            System.out.println(dir+"runable:"+firstSel.toString()+candidate.toString()+type.toString()+m);
            ArrayList<String> tcList = new ArrayList<>();

            tcList = this.getTCList(dir);

            int[][] cvdMetrix = this.getCovMMatrix(dir,tcList.size());


            ControllableSort sortByControl;
            if (candidate != SortType.COMBINE) {
                sortByControl = new ControllableSort(firstSel, candidate, type, cvdMetrix, m);
            }else{
                sortByControl = new ControllableSort(firstSel, candidate, type, cvdMetrix, m,alpha);
            }
            int[] sortedSeq = sortByControl.getSelectedTestSequence();
/**temp
 *
 */
//            for (int i:sortedSeq){
//                System.out.println(tcList.get(i));
//            }
            /**
             *
             */

            File file = new File(dirToMk.getAbsolutePath()+"/"+dir.substring(dir.lastIndexOf("/")+1)+".txt");
            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            for (int i:sortedSeq){
                bw.write(tcList.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            out.close();


        }

    }

    public void sortASaveByGANoRandom(String outputPath) throws IOException {
        for (String dir:proDir
                ) {
            boolean runable = this.isRunable(dir);
            if (!runable){
                continue;
            }

            File dirToMk = new File(outputPath);
            if(dirToMk.exists()==false) {
                dirToMk.mkdir();
            }

            System.out.println(dir+"runable");
            ArrayList<String> tcList = new ArrayList<>();

            tcList = this.getTCList(dir);

            int[][] cvdMetrix = this.getCovMMatrix(dir,tcList.size());

            GreedyAART sortByART = new GreedyAART(cvdMetrix,10);
            int[] sortedSeq = sortByART.getSelectedTestSequence();

            File file = new File(outputPath+"/"+dir.substring(dir.lastIndexOf("/")+1)+"ByGA.txt");
            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            for (int i:sortedSeq){
                bw.write(tcList.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            out.close();


        }
    }

    public void sortASaveByGenetic(String outputPath) throws IOException {
        for (String dir:proDir
                ) {
            boolean runable = this.isRunable(dir);
            if (!runable){
                continue;
            }

            File dirToMk = new File(outputPath);
            if(dirToMk.exists()==false) {
                dirToMk.mkdir();
            }
            System.out.println(dir+"runable");
            ArrayList<String> tcList = new ArrayList<>();

            tcList = this.getTCList(dir);

            int[][] cvdMetrix = this.getCovMMatrix(dir,tcList.size());

            Genetic sortByGenetic = new Genetic(cvdMetrix,dir+"/","CoveredMethodMatrix.txt");
            int[] sortedSeq = sortByGenetic.getSelectedTestSequence();

            File file = new File(outputPath+"/"+dir.substring(dir.lastIndexOf("/")+1)+"ByGenetic.txt");
            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            for (int i:sortedSeq){
                bw.write(tcList.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            out.close();


        }
    }

    public void sortASaveByART(String outputPath) throws IOException {
        for (String dir:proDir
                ) {
            boolean runable = this.isRunable(dir);
            if (!runable){
                continue;
            }

            File dirToMk = new File(outputPath);
            if(dirToMk.exists()==false) {
                dirToMk.mkdir();
            }

            System.out.println(dir+"runable");
            ArrayList<String> tcList = new ArrayList<>();

            tcList = this.getTCList(dir);

            int[][] cvdMetrix = this.getCovMMatrix(dir,tcList.size());

            ART sortByART = new ART(cvdMetrix,10);
            int[] sortedSeq = sortByART.getSelectedTestSequence();

            File file = new File(outputPath+"/"+dir.substring(dir.lastIndexOf("/")+1)+"ByART.txt");
            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            for (int i:sortedSeq){
                bw.write(tcList.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            out.close();


        }
    }

    public void sortASaveByGreedy(String outputPath) throws IOException {
        for (String dir:proDir
                ) {
            boolean runable = this.isRunable(dir);
            if (!runable){
                continue;
            }

            File dirToMk = new File(outputPath);
            if(dirToMk.exists()==false) {
                dirToMk.mkdir();
            }
            System.out.println(dir+"runable");
            ArrayList<String> tcList = new ArrayList<>();

            tcList = this.getTCList(dir);

            int[][] cvdMetrix = this.getCovMMatrix(dir,tcList.size());

            GreedyAdditional sortByGreedy = new GreedyAdditional(cvdMetrix);
            int[] sortedSeq = sortByGreedy.getSelectedTestSequence();

            File file = new File(outputPath+"/"+dir.substring(dir.lastIndexOf("/")+1)+"ByGreedy.txt");
            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            for (int i:sortedSeq){
                bw.write(tcList.get(i));
                bw.newLine();
            }
            bw.flush();
            bw.close();
            out.close();


        }
    }

    public boolean isRunable(String dirPath){
        File dirF = new File(dirPath);
        boolean result = false;
        for(File f:dirF.listFiles()){
            if(f.getName().equals("TestClassList.txt")){
                result = true;
//                System.out.println(dir+" already processed");
                break;
            }else{
                continue;
            }
        }
//        if(dirPath.substring(dirPath.lastIndexOf("/"+1)).equals("jackson-datatype-guava")==false){
//            result = false;
//        }
        return result;
    }

    public void iniMethodCover() throws SQLException, ClassNotFoundException, IOException {
        for (String dir:proDir
             ) {

            File dirF = new File(dir);
            boolean alreadyIini = false;
            for(File f:dirF.listFiles()){
                if(f.getName().equals("TestClassList.txt")){
                     alreadyIini = true;
                    System.out.println(dir+" already processed");
                    break;
                }else{
                    continue;
                }
            }
            if (alreadyIini){
                continue;
            }

            AnlzTstCvrg anl = new AnlzTstCvrg();
            DbService db = anl.getDb(dir);
//            System.out.println();
            if (db==null){
                System.out.println("??");
                continue;
            }
            try {
                anl.saveTClassACMethod(dir);
            }catch (Exception e){
                System.out.println(dir+" error");
            }
        }


    }

}
