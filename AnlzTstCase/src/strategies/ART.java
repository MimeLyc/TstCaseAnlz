package strategies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ART {

    int[][] CoverageMatrix;
    final String sep = File.separator;
    ArrayList<Integer> coveredZero = new ArrayList<Integer>(); // Store the test
    int m = 0;
    // case in case
    // it covers 0
    // statements/methods/branches.

    public ART(int[][] cvdMatrix,int m) {

        this.CoverageMatrix = cvdMatrix;
        this.m = m;
    }



    public int[] getSelectedTestSequence() {

//		this.getCoverageMatrix(this.coverageFile);

        int len = this.CoverageMatrix.length, columnNum = this.CoverageMatrix[0].length;
        int[] selectedTestSequence = new int[len];
        ArrayList<Integer> selected = new ArrayList<Integer>(); // Store the
        // current
        // selected test
        // cases.

//        final int LIMIT = 10; // The size of candidate tests, WARNING: test case
        // number should be more than 10.
        // Generate procedure
        int first = (int) (len * Math.random()); // Randomly select the first
        // element.
        selected.add(first);

        while (selected.size() < len) {
            // Generate procedure
            ArrayList<Integer> candidate = new ArrayList<Integer>(); // Store
            // the
            // already
            // selected
            // candidate
            // tests.

            int[] covered = new int[columnNum]; // Record the already covered
            // statements/methods/branches.
            this.clearArray(covered);
            int coveredNum = 0; // Store the number of
            // statements/methods/branches.
            boolean stop = false;
            // this.Print(selected);
            // Randomly select the first candidate.
            // int firstRandomCandidate = -1;
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
                    this.CoverageMatrix[firstRandom]);
            coveredNum = this.getCoveredNumber(covered);

             while(candidate.size() < m){
//            while (true) {
                // int[] leftCandidate = new int[len - selected];

                ArrayList<Integer> leftToChoose = new ArrayList<Integer>(); // int[len-selected.size()-candidate.size()];
                // //The
                // left
                // unselected
                // candidates
                // to
                // choose.
                for (int i = 0; i < len; i++) {
                    if (!selected.contains(i) && !candidate.contains(i)) {
                        leftToChoose.add(i);
                    }
                }
                if (leftToChoose.size()==0) {
                    // System.out.println("Nothing to choose.");
                    break;
                }

                int selcetedRandom = (int) (Math.random() * leftToChoose.size()); // Randomly
                // select
                // the
                // next
                // candidate.
                // System.out.println(selcetedRandom+","+leftToChoose.size());
                int newCandiadteIndex = leftToChoose.get(selcetedRandom); // Get
                // the
                // index
                // of
                // new
                // selected
                // candidate.
                // if(this.getCoveredNumber(this.CoverageMatrix[newCandiadteIndex])
                // == 0) continue;
//                this.mergeIntoCurrentArray(covered,
//                        this.CoverageMatrix[newCandiadteIndex]); // Merge the
                // new
                // statements/methods/branches
                // coverage
                // into the
                // covered
                // array.
//                int currentCovered = this.getCoveredNumber(covered);
//                if (currentCovered > coveredNum) {
//                    coveredNum = currentCovered;
                    candidate.add(newCandiadteIndex); // Add the selected
                    // candidate to the
                    // candidate arraylist.
                    // leftCandidates[newCandiadteIndex] = -1;
//                } else {
//                    // System.out.println(newCandiadteIndex+" break :");this.Print(this.CoverageMatrix[newCandiadteIndex]);
//                    break; // If the statements/methods/branches coverage is not
//                    // increase, then stop.
//                }
            }
            // if(candidate.size() ==0) continue;
            // this.Print(candidate);
            // Select procedure
            // double[][] Distance = new
            // double[selected.size()][candidate.size()]; //Store the distances
            // of selected test cases to candidates.
            double[] MaxDistances = new double[candidate.size()]; // Get the
            // maximum
            // distance
            // from the
            // candidate
            // minimum
            // distances.
            for (int j = 0; j < candidate.size(); j++) {
                int candidateNo = candidate.get(j);
                double[] MinDistance = new double[selected.size()]; // Get the
                // minimum
                // distance
                // from the
                // selected
                // minimum
                // distances.
                for (int i = 0; i < selected.size(); i++) {
                    int testCaseNo = selected.get(i);
                    MinDistance[i] = this.getJaccardDistance(
                            this.CoverageMatrix[testCaseNo],
                            this.CoverageMatrix[candidateNo]);
					/*
					 * if(MinDistance[i] == Double.NEGATIVE_INFINITY ||
					 * MinDistance[i] == Double.NaN){ MinDistance[i] = 0;
					 * System.out.println("Got a MinDistance is Double.NaN"); }
					 */
                }
                int MinIndex = this.getMinIndex(MinDistance);
                if (MinIndex == -1) {
                    System.out
                            .println("ERROR: getSelectedTestSequence MinIndex == -1");
                    System.exit(1);
                }
                MaxDistances[j] = MinDistance[MinIndex]; // Assign each
                // candidate's
                // minimum distance
                // to the
                // MaxDistances
                // array.
            }
            int MaxIndex = this.getMaxIndex(MaxDistances);
            if (MaxIndex == -1) {
                System.out
                        .println("ERROR: getSelectedTestSequence MaxIndex == -1");

                System.exit(1);
            }
            // Select the candidate to selected arraylist.
            selected.add(candidate.get(MaxIndex));
            // leftCandidates[newCandiadteIndex] = -1;
        }
        // Add the elements of selected arraylist to the test case sequence.
        for (int i = 0; i < selected.size(); i++) {
            selectedTestSequence[i] = selected.get(i);
        }
        return selectedTestSequence;
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
                max = a[i];
                index = i;
            }
        }
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

	/*
	 * //For Unit Test. public static void main(String[] args){ ARTMaxMin art =
	 * new ARTMaxMin("you owen directory", "BranchCommonTestCasesMatrix.txt");
	 * art.Print(art.getSelectedTestSequence());
	 *
	 * }
	 */
}
