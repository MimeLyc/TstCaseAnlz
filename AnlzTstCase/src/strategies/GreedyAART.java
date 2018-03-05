package strategies;

public class GreedyAART {
    private int m = 0;//待选择测试数
    int[][] originalMatrix;
    int[][] constMatrix;
    int[][] cvdMatrix;
    int[] sortedSeq;
    int colLen=0;
    int num = 0;


    public GreedyAART(int[][] cvdMatrix,int m){
        this.num = cvdMatrix.length;
        this.m = m;
        colLen = cvdMatrix[0].length;
        this.originalMatrix = cvdMatrix;
        this.constMatrix = cvdMatrix;
        this.sortedSeq = new int[num];
        this.cvdMatrix = new int[num][colLen];

    }

    public int[] getSelectedTestSequence(){

        for (int i = 0;i<num;i++){
            GreedyAdditional greedy = new GreedyAdditional(originalMatrix);
            int[] tempSeq = greedy.getSelectedTestSequence();

            for (int j=0;j<tempSeq.length;j++) {
                for (int k = 0; k < i; k++) {
                    if (tempSeq[j] == sortedSeq[k]){
                        for (int p = j;p<tempSeq.length-1;p++){
                            tempSeq[p] = tempSeq[p+1];
                        }
                    }
                }
            }

            int[] mSeq = new int[m];
            for (int j = 0;j<m;j++){
//                cvdMatrix[j] = constMatrix[tempSeq[j]];
                mSeq[j] = tempSeq[j];
            }

            double[] minDis = new double[m];

            for (int j = 0 ;j<m;j++){
                minDis[j] = this.getMinDis(constMatrix[mSeq[j]],i);


            }

            int maxIndex = this.getMaxIndex(minDis);
            int selectIndex = mSeq[maxIndex];
            sortedSeq[i] = selectIndex;
            cvdMatrix[i] = constMatrix[selectIndex];
            int[] currentCovered = constMatrix[selectIndex];
            this.delCovered(currentCovered);
//            for (int k=0;k<colLen;k++){
//                System.out.println(originalMatrix[selectIndex][k]+" ");
//            }

//            originalMatrix[selectIndex] = new int[colLen];




        }

        return sortedSeq;



    }



    private void delCovered(int[] currentCovert){


        for (int i=0;i<originalMatrix.length;i++){
            for (int j = 0;j<colLen;j++) {
                if (currentCovert[j] == 1 & originalMatrix[i][j]==1) {
                    originalMatrix[i][j]  = 0;
                }
            }



        }
//        originalMatrix = temp;



    }

    private int getMaxIndex(double[] a){

        int result = 0;
        double temp = 0;
        for (int i=0;i<a.length;i++){
            if (temp<a[i]){
                temp = a[i];
                result = i;

            }
        }

        return result;


    }

    private double getMinDis(int[] a,int limit){
        double result = Double.MAX_VALUE;
        for (int i=0;i<limit;i++) {
            double temp = this.getJaccardDistance(a,cvdMatrix[i]);
            if (temp<=result) {
                result = temp;
            }
        }

        return result;

    }

    private double getDis(int[] a,int limit){
        double result = 0;

        for (int i=0;i<limit;i++) {
            double temp = this.getJaccardDistance(a,cvdMatrix[i]);
            result += temp;
        }

        return result;



    }

    // Calculate the Jaccard distance between two vector.
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

}
