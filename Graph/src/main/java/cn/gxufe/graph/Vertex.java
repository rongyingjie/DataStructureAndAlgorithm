package cn.gxufe.graph;

import java.util.ArrayList;
import java.util.List;

public  class Vertex {
        public int point;
        public int inDegree = 0 ;
        public List<Vertex> nextVertexs = new ArrayList<>();

        @Override
        public String toString() {
                return Integer.toString(point);
        }
}