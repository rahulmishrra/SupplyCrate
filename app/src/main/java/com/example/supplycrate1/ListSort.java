package com.example.supplycrate1;

import java.util.Collections;
import java.util.List;

public class ListSort {
    List<Integer> integerList;

    public ListSort(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public List<Integer> getIntegerList() {
        Collections.sort(integerList,Collections.reverseOrder());
        return integerList;
    }


}
