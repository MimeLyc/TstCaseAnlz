package strategies;

import java.util.ArrayList;
import java.util.Arrays;

public class ControllableSort {

    public static void main(String[] args) {
//        int[] t1={1,2,3,4};
//        int[] t2 = t1.clone();
//        t2[2] = 1;
//        int t1 = 3;
//        int t2 = t1;
//        t2 = 4;
//        System.out.println(t1);


    }
    private int[][] originMatrix;
    private int m;
    private int[] selectedSeq;
    private int size=0;
    private int colNum = 0;
    private SortType firstS ;
    private SortType candidateS;
    private int[][] canMatrix;
    private CalDisType disType;
    private double alpha= 0.0;

    public ControllableSort(SortType firstSel, SortType candidate,CalDisType type, int[][] cvdMatrix, int m){
        if (candidate == SortType.RANDOM&&m<=0){
            System.out.println("m can't <=0");
        }
        this.originMatrix = cvdMatrix;
        this.m = m;
        this.size = cvdMatrix.length;
        this.colNum = cvdMatrix[0].length;
        this.selectedSeq = new int[size];
        this.firstS = firstSel;
        this.candidateS = candidate;
        this.disType = type;
        this.canMatrix = new int[size][colNum];

    }

    public ControllableSort(SortType firstSel, SortType candidate,CalDisType type, int[][] cvdMatrix, int m,double alpha){
        if (candidate == SortType.RANDOM&&m<=0){
            System.out.println("m can't <=0");
        }
        this.originMatrix = cvdMatrix;
        this.m = m;
        this.size = cvdMatrix.length;
        this.colNum = cvdMatrix[0].length;
        this.selectedSeq = new int[size];
        this.firstS = firstSel;
        this.candidateS = candidate;
        this.disType = type;
        this.canMatrix = new int[size][colNum];
        this.alpha = alpha;

    }

    public int[] getSelectedTestSequence() {
        int[][] candidateM = originMatrix;
        int firstSelect = -1;
        if (firstS ==SortType.GREEDY){
            GreedyAdditional temp = new GreedyAdditional(candidateM);
            firstSelect = temp.getSelectedTestSequence()[0];
//            System.out.println(firstSelect);
//            for (int x:originMatrix[firstSelect]
//                 ) {
//                System.out.print(x);
//            }
        }else if(firstS == SortType.RANDOM){
            firstSelect = this.getFirstRandomSelc(candidateM);
        }

        candidateM = new int[size][colNum];

        if (candidateS==SortType.RANDOM){

            this.sortByRandom(firstSelect);

        }else if(candidateS==SortType.GREEDY){

            this.sortByGreedy(firstSelect);

        }else if(candidateS==SortType.COMBINE){
            this.sortByCombine(firstSelect);
        }

        return this.selectedSeq;
    }

    private void sortByCombine(int firstSelect){
//        int[] tempSort = new int[size];
//        tempSort[0] = firstSelect;
        ArrayList<Integer> selected = new ArrayList<Integer>();
        selected.add(firstSelect);
        int[][] tempMatrix = new int[size][colNum];
        for (int i=0;i<size;i++) {
            tempMatrix[i] = originMatrix[i].clone();
        }

        this.delCovered(originMatrix[firstSelect].clone(),tempMatrix);
//        for (int x:originMatrix[firstSelect]
//                ) {
//            System.out.print(x);
////            System.out.println();
//        }
        this.canMatrix[0] = originMatrix[firstSelect].clone();

        int len = this.originMatrix.length, columnNum = this.originMatrix[0].length;


        for (int i = 1;i<size;i++){
//            根据贪婪算法选出m个候选集
            GreedyAdditional greedy = new GreedyAdditional(tempMatrix);
            int[] tempSeq = greedy.getSelectedTestSequence();

            for (int j=0;j<size-i;j++) {
//                System.out.println(j);
                for (int k = 0; k < i; k++) {
                    if (tempSeq[j] == selected.get(k)){
//                        System.out.println(j);
//                        count++;
//                        System.out.println(tempSeq[j]+" repeat");
                        for (int p = j;p<tempSeq.length-1;p++){
//                            int temp = tempSeq[p];
                            tempSeq[p] = tempSeq[p+1];
//                            tempSeq[p+1] = temp;
                        }
                        j--;
                        break;
                    }
                }
            }

            int tempm=m;
            if(size-i<m){
                tempm = size - i;
            }

            int[] mSeq = new int[tempm];
            for (int j = 0;j<tempm;j++){
//                cvdMatrix[j] = constMatrix[tempSeq[j]];
                mSeq[j] = tempSeq[j];
            }

            double[] minDis = new double[tempm];

            for (int j = 0 ;j<tempm;j++){

                if(this.disType==CalDisType.CLOSED) {
                    minDis[j] = this.getMinDisByClosed(originMatrix[mSeq[j]], i);
//                    System.out.println(minDis[j]);
                }else if(this.disType==CalDisType.SUM){
                    minDis[j] = this.getDisBySum(originMatrix[mSeq[j]], i);
                }

            }

/**
 * 根据随机算法选取
 */

            ArrayList<Integer> candidate = new ArrayList<Integer>(); // Store


            int[] covered = new int[columnNum]; // Record the already covered
            this.clearArray(covered);
            int coveredNum = 0; // Store the number of
            boolean stop = false;

            ArrayList<Integer> tempList = new ArrayList<Integer>();
            for (int j = 0; j < len; j++) {
                if (!selected.contains(j)) {
                    tempList.add(j);
                }
            }

            int firstRandom = (int) (Math.random() * tempList.size());
            candidate.add(tempList.get(firstRandom));
            // tempList.remove(firstRandom);
            this.mergeIntoCurrentArray(covered,
                    this.originMatrix[firstRandom]);
            coveredNum = this.getCoveredNumber(covered);

            while(candidate.size() < m){


                ArrayList<Integer> leftToChoose = new ArrayList<Integer>(); // int[len-selected.size()-candidate.size()];

                for (int j = 0; j < len; j++) {
                    if (!selected.contains(j) && !candidate.contains(j)) {
                        leftToChoose.add(j);
                    }
                }
                if (leftToChoose.size()==0) {
                    break;
                }

                int selcetedRandom = (int) (Math.random() * leftToChoose.size()); // Randomly

                int newCandiadteIndex = leftToChoose.get(selcetedRandom); // Get

                candidate.add(newCandiadteIndex); // Add the selected

            }

            double[] MaxDistances = new double[candidate.size()]; // 保存候选集到已选集的距离
            if(this.disType == CalDisType.CLOSED) {
                for (int j = 0; j < candidate.size(); j++) {
                    int candidateNo = candidate.get(j);
                    double[] MinDistance = new double[selected.size()];

                    for (int k = 0; k < selected.size(); k++) {
                        int testCaseNo = selected.get(k);
                        MinDistance[k] = this.getJaccardDistance(
                                this.originMatrix[testCaseNo],
                                this.originMatrix[candidateNo]);
                    }
                    int MinIndex = this.getMinIndex(MinDistance);
                    if (MinIndex == -1) {
                        System.out
                                .println("ERROR: getSelectedTestSequence MinIndex == -1");
                        System.exit(1);
                    }
//                    System.out.println(MinDistance[MinIndex]);
                    MaxDistances[j] = MinDistance[MinIndex];
//                    System.out.println(MaxDistances[j]);

                }
            }else if(this.disType == CalDisType.SUM){

                for (int j = 0; j < candidate.size(); j++) {
                    int candidateNo = candidate.get(j);
                    double sum =0;
                    for (int k = 0; k < selected.size(); k++) {
                        int testCaseNo = selected.get(k);
                        sum += this.getJaccardDistance(
                                this.originMatrix[testCaseNo],
                                this.originMatrix[candidateNo]);
                    }
                    MaxDistances[j] = sum;
                }

            }


            /**
             * 对数组距离归一化处理
             */
//            System.out.println(minDis[0]);
            this.normArray(minDis);
            this.normArray(MaxDistances);
//            double sum=0;
//            for (int q=0;q<MaxDistances.length;q++){
//                sum+=Math.pow(MaxDistances[q],2);
//            }
//            System.out.println(sum);


            ArrayList<Integer> indexList = new ArrayList<>();
            ArrayList<Double> disList = new ArrayList<>();

//            System.out.println(minDis.length+";"+MaxDistances.length);
            for (int j = 0;j<MaxDistances.length;j++){
                indexList.add(mSeq[j]);
                disList.add(this.alpha*minDis[j]);
//                System.out.println(alpha+":"+minDis[j]);
//                System.out.println("greedy:"+mSeq[j]+":"+disList.get(j));
            }

            for (int j=0;j<MaxDistances.length;j++){
                boolean contain =false;
                for (int k=0;k<indexList.size();k++){
                    if (candidate.get(j)==indexList.get(k)){
                        disList.set(k,disList.get(k)+(1-this.alpha)*MaxDistances[j]);
//                        System.out.println("Random combine:"+indexList.get(k)+":"+MaxDistances[j]+":"+disList.get(k));
                        contain = true;
                        break;
                    }
                }

                if (contain == true){
                    continue;
                }else{
                    indexList.add(candidate.get(j));
                    disList.add(MaxDistances[j]*(1-this.alpha));
//                    System.out.println(MaxDistances[j]);
//                    System.out.println("random passed:"+candidate.get(j));
                }
            }

            double[] combineDis = new double[disList.size()];
//            System.out.println(disList.size());
            for (int j = 0;j<combineDis.length;j++){
                combineDis[j] = disList.get(j);
//                System.out.println(disList.get(j));
            }

            //TODO
            int MaxIndex = this.getMaxIndex(combineDis);
//            for (double x:combineDis){
//                System.out.println(x);
//            }
//            System.out.println(MaxIndex);
            int selectIndex = indexList.get(MaxIndex);
//            System.out.println(selectIndex+" test");
            selected.add(selectIndex);
            canMatrix[i] = originMatrix[selectIndex];
            int[] currentCovered = originMatrix[selectIndex];
            this.delCovered(currentCovered,tempMatrix);
//            int MaxIndex = this.getMaxIndex(MaxDistances);
//            if (MaxIndex == -1) {
//                System.out
//                        .println("ERROR: getSelectedTestSequence MaxIndex == -1");
//
//                System.exit(1);
//            }



//            int maxIndex = this.getMaxIndex(minDis);
//            int selectIndex = mSeq[maxIndex];
//            tempSort[i] = selectIndex;
//            canMatrix[i] = originMatrix[selectIndex];
//            int[] currentCovered = originMatrix[selectIndex];
//            this.delCovered(currentCovered,tempMatrix);



        }

        for (int i = 0; i < selected.size(); i++) {
            selectedSeq[i] = selected.get(i);
        }


    }


    private void normArray(double[] array){
        double sumQ = 0.0;
        for (double x:array
             ) {
            sumQ+=Math.pow(x,2);
        }
//        if (sumQ ==0){
//            System.out.println("error");
//        }
        if (sumQ!=0) {
            for (int i = 0; i < array.length; i++) {
                array[i] = array[i] / Math.sqrt(sumQ);
            }
        }
    }

    private void sortByGreedy(int firstSelect){
        int[] tempSort = new int[size];
        tempSort[0] = firstSelect;
        int[][] tempMatrix = new int[size][colNum];
        for (int i=0;i<size;i++) {
            tempMatrix[i] = originMatrix[i].clone();
        }
        this.delCovered(originMatrix[firstSelect],tempMatrix);
        this.canMatrix[0] = originMatrix[firstSelect];
        System.out.println(firstSelect+" test");

        for (int i = 1;i<size;i++){

            GreedyAdditional greedy = new GreedyAdditional(tempMatrix);
            int[] tempSeq = greedy.getSelectedTestSequence();

//            for (int x:tempSeq){
//                System.out.println(x+" seq");
//            }
//            System.out.println();

//            int count = 0;
            for (int j=0;j<size-i;j++) {
//                System.out.println(j);
                for (int k = 0; k < i; k++) {
                    if (tempSeq[j] == tempSort[k]){
//                        System.out.println(j);
//                        count++;
//                        System.out.println(tempSeq[j]+" repeat");
                        for (int p = j;p<tempSeq.length-1;p++){
//                            int temp = tempSeq[p];
                            tempSeq[p] = tempSeq[p+1];
//                            tempSeq[p+1] = temp;
                        }
                        j--;
                        break;
                    }
                }
            }
//            System.out.println(count);
//            for (int x:tempSeq){
//                System.out.println(x+" seq");
//            }
//            System.out.println();
            if(size-i<m){
                m = size - i;
            }

            int[] mSeq = new int[m];
            for (int j = 0;j<m;j++){
//                cvdMatrix[j] = constMatrix[tempSeq[j]];
                mSeq[j] = tempSeq[j];
            }

            double[] minDis = new double[m];

            for (int j = 0 ;j<m;j++){

                if(this.disType==CalDisType.CLOSED) {
                    minDis[j] = this.getMinDisByClosed(originMatrix[mSeq[j]], i);
//                    System.out.println(minDis[j]);
                }else if(this.disType==CalDisType.SUM){
                    minDis[j] = this.getDisBySum(originMatrix[mSeq[j]], i);
                }

            }

            int maxIndex = this.getMaxIndex(minDis);
//            for (int o=0;o<minDis.length;o++
//                    ) {
//                System.out.println(minDis[o]+":"+mSeq[o]);
//            }
//
//            System.out.println("------------------"+maxIndex);
//            System.out.println();

            int selectIndex = mSeq[maxIndex];
//            System.out.println(selectIndex+" test");
            tempSort[i] = selectIndex;
            canMatrix[i] = originMatrix[selectIndex];
            int[] currentCovered = originMatrix[selectIndex];
            this.delCovered(currentCovered,tempMatrix);

        }

        selectedSeq = tempSort;

    }

    private double getDisBySum(int[] a,int limit){
        double result = 0;
        for (int i=0;i<limit;i++) {
            double temp = this.getJaccardDistance(a,canMatrix[i]);
                result += temp;
        }

        return result;


    }

    private double getMinDisByClosed(int[] a, int limit){
        double result = Double.MAX_VALUE;
        for (int i=0;i<limit;i++) {
            double temp = this.getJaccardDistance(a,canMatrix[i]);

            if (temp<=result) {
//                for (int x:a){
//                    System.out.print(x);
//                }
//                System.out.println();
//                for (int x:canMatrix[i]
//                     ) {
//                    System.out.print(x);
//                }
//                System.out.println();
//                System.out.println("---");
//                System.out.println(temp);
                result = temp;
            }
        }

        return result;

    }

    private void sortByRandom(int firstSelct){
        int len = this.originMatrix.length, columnNum = this.originMatrix[0].length;
        ArrayList<Integer> selected = new ArrayList<Integer>();
        selected.add(firstSelct);
        while (selected.size() < len) {

            ArrayList<Integer> candidate = new ArrayList<Integer>(); // Store


            int[] covered = new int[columnNum]; // Record the already covered
            this.clearArray(covered);
            int coveredNum = 0; // Store the number of
            boolean stop = false;

            ArrayList<Integer> tempList = new ArrayList<Integer>();
            for (int i = 0; i < len; i++) {
                if (!selected.contains(i)) {
                    tempList.add(i);
                }
            }
            int firstRandom = (int) (Math.random() * tempList.size());
            candidate.add(tempList.get(firstRandom));
            // tempList.remove(firstRandom);
            this.mergeIntoCurrentArray(covered,
                    this.originMatrix[firstRandom]);
            coveredNum = this.getCoveredNumber(covered);

            while(candidate.size() < m){


                ArrayList<Integer> leftToChoose = new ArrayList<Integer>(); // int[len-selected.size()-candidate.size()];

                for (int i = 0; i < len; i++) {
                    if (!selected.contains(i) && !candidate.contains(i)) {
                        leftToChoose.add(i);
                    }
                }
                if (leftToChoose.size()==0) {
                    break;
                }

                int selcetedRandom = (int) (Math.random() * leftToChoose.size()); // Randomly

                int newCandiadteIndex = leftToChoose.get(selcetedRandom); // Get

                candidate.add(newCandiadteIndex); // Add the selected

            }

            double[] MaxDistances = new double[candidate.size()]; // Get the
            if(this.disType == CalDisType.CLOSED) {
                for (int j = 0; j < candidate.size(); j++) {
                    int candidateNo = candidate.get(j);
                    double[] MinDistance = new double[selected.size()]; // Get the

                    for (int i = 0; i < selected.size(); i++) {
                        int testCaseNo = selected.get(i);
                        MinDistance[i] = this.getJaccardDistance(
                                this.originMatrix[testCaseNo],
                                this.originMatrix[candidateNo]);
                    }
                    int MinIndex = this.getMinIndex(MinDistance);
                    if (MinIndex == -1) {
                        System.out
                                .println("ERROR: getSelectedTestSequence MinIndex == -1");
                        System.exit(1);
                    }
                    MaxDistances[j] = MinDistance[MinIndex]; // Assign each

                }
            }else if(this.disType == CalDisType.SUM){

                for (int j = 0; j < candidate.size(); j++) {
                    int candidateNo = candidate.get(j);
                    double sum =0;
                    for (int i = 0; i < selected.size(); i++) {
                        int testCaseNo = selected.get(i);
                        sum += this.getJaccardDistance(
                                this.originMatrix[testCaseNo],
                                this.originMatrix[candidateNo]);
                    }
                    MaxDistances[j] = sum;
                }

            }
            int MaxIndex = this.getMaxIndex(MaxDistances);




            if (MaxIndex == -1) {
                System.out
                        .println("ERROR: getSelectedTestSequence MaxIndex == -1");

                System.exit(1);
            }

            selected.add(candidate.get(MaxIndex));

        }
        // Add the elements of selected arraylist to the test case sequence.
        for (int i = 0; i < selected.size(); i++) {
            selectedSeq[i] = selected.get(i);
        }
//        return selectedSeq;

    }

    private void delCovered(int[] currentCovert,int[][] tempMatrix){


        for (int i=0;i<tempMatrix.length;i++){
            for (int j = 0;j<colNum;j++) {
                if (currentCovert[j] == 1 && tempMatrix[i][j]==1) {
                    tempMatrix[i][j]  = 0;
                }
            }



        }
//        for (int x:currentCovert
//                ) {
//            System.out.print(x);
//        }
//        originalMatrix = temp;
    }

    private int getFirstRandomSelc(int[][] M){

        int temp = (int) (size * Math.random()); // Randomly select the first
        // element.
       return temp;

    }

    public double getJaccardDistance(int[] a, int[] b) {
        if (a.length != b.length) {
            System.out.println("ERROR: length not equal.");
            System.exit(0);
        }
        int len = a.length;
        double distance = 0;
        int join = 0, combine = 0;
        int[] combinedArray = new int[len]; // Store the combined result of a
        // and b.

        for (int i = 0; i < len; i++) {
            if (a[i] == 1 && b[i] == 1) {
                join++;
            }
            if (a[i] == 1) {
                combinedArray[i] = 1;
            }
            if (b[i] == 1) {
                combinedArray[i] = 1;
            }
        }
        combine = this.getCoveredNumber(combinedArray);
        if (combine == 0) {
            return 0;
        }
        distance = 1.0 - (join / (double) combine);
        return distance;
    }

    // Return the minimum element's index of the double[].
    public int getMinIndex(double[] a) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < min) {
                min = a[i];
                index = i;
            }
        }
        return index;
    }

    // Return the maximum element's index of the double[].
    public int getMaxIndex(double[] a) {
        double max = -Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) {
//                System.out.println(max);
//                System.out.println(max);
                max = a[i];
                index = i;
            }
        }
//        System.out.println("---------");
//		System.out.println(max);
        return index;
    }

    // Calculate the number of '1' in the array.
    public int getCoveredNumber(int[] a) {
        int num = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 1) {
                num++;
            }
        }
        return num;
    }

    // Set all elements '0' in the array.
    public void clearArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
    }

    // Merge all the '1's in the new array into the current array.
    public void mergeIntoCurrentArray(int[] current, int[] newArray) {
        if (current.length != newArray.length) {
            System.out
                    .println("ERROR: mergeIntoCurrentArray: length is not equal.");
            System.exit(1);
        }
        int len = current.length;
        for (int i = 0; i < len; i++) {
            if (newArray[i] == 1) {
                current[i] = newArray[i];
            }
        }
    }


}
