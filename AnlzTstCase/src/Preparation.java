import java.io.*;
import java.util.ArrayList;

public class Preparation {

    public static void main(String[] args) throws IOException, InterruptedException {
        Preparation test = new Preparation("/home/mimelyc/PGWork/30Programs");
        ArrayList<String> dir = test.getDir();
//        test.execTacoco("/home/mimelyc/PGWork/tacoco");
//        test.makeTimeCost("/home/mimelyc/PGWork/TimeCost");

//        for (String d:dir
//             ) {
//            System.out.println(d);
//        }
    }


    private String path="";
    private ArrayList<String> allDir = new ArrayList<String>();


    public Preparation(String path){
        this.path = path;
        allDir = this.getAllDir();
    }

    public ArrayList<String> getDir(){
        return this.allDir;
    }

    private ArrayList<String> getAllDir(){
        ArrayList<String> allDir = new ArrayList<>();
        File parent = new File(path);
        File[] flist = parent.listFiles();
        for (File f:flist){
            if (f.isDirectory()&&f.getName().equals("SortedByGreedy")==false){
//                System.out.println(f.getAbsolutePath());
                allDir.add(f.getAbsolutePath());
            }
        }
        return allDir;

    }

    private void makeOutputDir(){
        for (String d:allDir
             ) {
            File dir = new File(d+"/output");
            dir.mkdir();
        }

    }

    private void makeTimeCost(String output) throws IOException {
        for (String d:allDir
                ) {
            String logFile = d+"/screen.log";
            File log = new File(logFile);
            if (!log.exists()){
                continue;
            }

            String testFile = d+"/"+"TestClassList.txt";
            ArrayList<String> testList = new ArrayList<>();
            FileReader in = new FileReader(testFile);
            BufferedReader br = new BufferedReader(in);
            String s ="";
            while ((s=br.readLine())!=null){
                testList.add(s);
            }

            br.close();
            in.close();

             in = new FileReader(logFile);
             br = new BufferedReader(in);
             s ="";

             ArrayList<String> timeList = new ArrayList<>();
            while ((s=br.readLine())!=null){
                if (s.split(" ")[0].equals("Finishing")){
                    for (String x:testList){
                        if (x.equals(s.split(" ")[1])){
                            String testCost = x+":"+s.split("Time elapsed:")[1];
                            timeList.add(testCost);
                        }
                    }
                }
            }

            br.close();
            in.close();

//            for (String x:timeList){
//                System.out.println(x);
//            }

            String outName = output+"/"+ d.substring(d.lastIndexOf("/")+1)+"TestCost.txt";
//            System.out.println(outName);
            File file = new File(outName);
            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            for (String x:timeList){
                bw.write(x);
                bw.newLine();
            }

            bw.flush();
            bw.close();
            out.close();

        }
    }

    private void makeConfigFile() throws IOException {
        for (String d:allDir
                ) {
            String fileName  = d.substring(d.lastIndexOf("/")+1);
//            System.out.println(d+"/"+fileName);
            File file = new File(d+"/"+fileName+".config");
            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            bw.write("-javaagent:/root/.m2/repository/org/jacoco/org.jacoco.agent/0.7.8/org.jacoco.agent-0.7.8-runtime.jar=destfile=" +
                    d+"/output/"+fileName+".exec");
            bw.newLine();
            bw.write("-Dtacoco.listeners=org.spideruci.tacoco.testlisteners.JacocoListener");
            bw.newLine();
            bw.write("-Dtacoco.analyzer=org.spideruci.tacoco.analysis.TacocoAnalyzer");
            bw.newLine();
            bw.write("-Dtacoco.db=On");

            bw.flush();
            bw.close();
            out.close();

        }
    }

    private void execTacoco(String tacocoPath) throws IOException, InterruptedException {
        for (String d:allDir
                ) {
            String fileName = d.substring(d.lastIndexOf("/") + 1);
            String execFile = d+"/"+fileName+".config";
            String command = "mvn -q exec:java -Plauncher " +
                    "-Dtacoco.sut="+d+" " +
                    "-Dtacoco.project="+fileName+" " +
                    "-Dtacoco.outdir="+d+"/output"+" " +
//                    "-Dtacoco.home="+tacocoPath+" " +
                    "-Danalyzer.opts="+execFile+"";

//            String fileName  = d.substring(d.lastIndexOf("/")+1);
            System.out.println(d+"/"+fileName);
            File file = new File(d+"/"+fileName+"Tacoco.txt");
            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter out = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(out);
            bw.write(command);


            bw.flush();
            bw.close();
            out.close();
//            System.out.println(command);

//            String[] cmdA = { "/bin/sh", "-c", "cd "+tacocoPath+";"+command };
//
//            Runtime run = Runtime.getRuntime();
////            Process process = run.exec("cd "+tacocoPath);
////            process.waitFor();
//            Process process = run.exec(cmdA);
////            process.waitFor();
////            String[] cmdB = {"/bin/sh", "-c", command};
////            process = run.exec(cmdB);
////            process.waitFor();
//
//            InputStream in = process.getInputStream();
//            BufferedReader read = new BufferedReader(new InputStreamReader(in));
//            String result = read.readLine();
//            System.out.println("INFO:"+result);
//            process.waitFor();
//            process.destroy();
        }
    }

}
