package strategies;

import java.util.ArrayList;
import java.util.Arrays;

public class GreedyTotal implements SortStrategy{
    ArrayList<Integer> arr2Sort = new ArrayList<Integer>();
    ArrayList<Integer> sortedIndex = new ArrayList<>();

    public GreedyTotal(ArrayList<Integer> arr2Sort){
        this.arr2Sort = arr2Sort;
    }

    public ArrayList<Integer> sortL2S(){

        ArrayList<Integer> originalArray = (ArrayList<Integer>)arr2Sort.clone();
//        System.out.println(arr2Sort.get(14));
        int[] list2Sort = new int[arr2Sort.size()];
        for (int i=0;i<list2Sort.length;i++){
            list2Sort[i] = arr2Sort.get(i);
        }

        Arrays.sort(list2Sort);
//        System.out.println(arr2Sort.get(13));
        for (int i=arr2Sort.size()-1;i>=0;i--){
            int max = list2Sort[i];
//            System.out.println(max);
            for (int j = 0; j < arr2Sort.size(); j++) {
                if(originalArray.get(j)==max){
                    sortedIndex.add(j);
                    originalArray.set(j,-1);
                    break;
                }
            }
        }
        return sortedIndex;

    }
}
