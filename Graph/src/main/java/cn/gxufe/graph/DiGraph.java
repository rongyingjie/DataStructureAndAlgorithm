package cn.gxufe.graph;

import java.io.*;

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
        }


    }

}
