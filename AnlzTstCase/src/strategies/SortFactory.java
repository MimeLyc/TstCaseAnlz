package strategies;

import java.util.ArrayList;

public class SortFactory {
    private ArrayList<Integer> arr2Sort = new ArrayList<>();
    private ArrayList<Integer> sortedIndex = new ArrayList<>();
    SortStrategy strategy;

    public SortFactory(ArrayList<Integer> arr2Sort,SortType type){
        if (type==SortType.GREEDY){
            strategy = new GreedyTotal(arr2Sort);
        }
    }

    public ArrayList<Integer> getL2SSortedIndex(){
        return strategy.sortL2S();
    }



}
