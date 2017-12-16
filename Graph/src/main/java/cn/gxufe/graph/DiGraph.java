package cn.gxufe.graph;

import java.io.*;
import java.util.*;

public class DiGraph extends Graph{


    public DiGraph(String path){
        super(path);
    }

    @Override
    protected void init(BufferedReader bufferedReader) throws IOException {
        String tmp;
        while ((tmp = bufferedReader.readLine())!=null){
            String[] split = tmp.split(" ");
            int from = Integer.valueOf(split[0].trim());
            int to = Integer.valueOf(split[1].trim());
            if(vertexList[from] == null){
                vertexList[from] = new Vertex();
                vertexList[from].point = from;
            }
            if(vertexList[to] == null){
                vertexList[to] = new Vertex();
                vertexList[to].point = to;
            }
            vertexList[from].nextVertexs.add(vertexList[to]);
            vertexList[to].inDegree ++;
        }
    }


    public List<Vertex> topSort() {
        List<Vertex> result = new ArrayList<>(this.vertexs);
        LinkedList<Vertex> stack = new LinkedList<>();
        for (int i = 0; i < this.vertexs; i++) {
            Vertex vertex = this.vertexList[i];
            if(vertex.inDegree == 0){
                stack.add(vertex);
            }
        }
        while (!stack.isEmpty()){
            Vertex pop = stack.pop();
            result.add(pop);
            if(pop.nextVertexs != null && pop.nextVertexs.size() > 0){
                for (Vertex v : pop.nextVertexs ) {
                    v.inDegree--;
                    if( v.inDegree == 0 ){
                        stack.add(v);
                    }
                }
            }
        }
        return result;
    }
}
