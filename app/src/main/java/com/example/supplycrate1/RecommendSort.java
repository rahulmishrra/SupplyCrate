package com.example.supplycrate1;

import java.util.Collections;
import java.util.List;

public class RecommendSort {

    List<Integer> recommendList;

    public RecommendSort(List<Integer> recommendList) {
        this.recommendList = recommendList;
    }

    public List<Integer> getRecommendList() {
        Collections.sort(recommendList,Collections.reverseOrder());
        return recommendList;
    }
}
