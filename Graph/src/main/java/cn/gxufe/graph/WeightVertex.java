package cn.gxufe.graph;

import java.util.ArrayList;
import java.util.List;

public class WeightVertex {
    public int point;
    public int inDegree = 0;
    public List<UndirectedEdge> edges = new ArrayList<>();

    @Override
    public String toString() {
        return Integer.toString(point);
    }
}
