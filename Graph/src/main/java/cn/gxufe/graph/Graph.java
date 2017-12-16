package cn.gxufe.graph;

import java.io.*;
import java.util.*;

public abstract class Graph  {

    protected int vertexs; // 顶点数量
    protected int edges; // 边数量
    protected Vertex[] vertexList;

    public Graph(String path){
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        try {
            Reader reader = new InputStreamReader(resourceAsStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            readVertexsAndDdges(bufferedReader);
            init(bufferedReader);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
            }
        }
    }

    protected abstract void init(BufferedReader bufferedReader) throws IOException;

    protected void readVertexsAndDdges(BufferedReader bufferedReader)throws IOException{
        String tmp = bufferedReader.readLine();
        vertexs = Integer.valueOf(tmp.trim());
        tmp = bufferedReader.readLine();
        edges = Integer.valueOf(tmp);
        vertexList = new Vertex[vertexs];
    }



    public List<Integer> bfs(){
        return bfs(0);
    }

    public List<Integer> bfs(int i){
        Vertex vertex = vertexList[i];
        LinkedList<Vertex> stack = new LinkedList<>();
        Set<Integer> isVisit = new HashSet<>();
        stack.add(vertex);
        List<Integer> result = new ArrayList<>(this.vertexs);
        while (!stack.isEmpty()){
            Vertex pop = stack.pop();
            if(!isVisit.contains(pop.point) ){
                result.add(pop.point);
                isVisit.add(pop.point);
                for (int j = 0; j < pop.nextVertexs.size(); j++) {
                    if(!isVisit.contains(pop.nextVertexs.get(j).point)){
                        stack.add(pop.nextVertexs.get(j));
                    }
                }
            }
        }
        return result;
    }

    public List<Integer> dfs(){
        return dfs(0);
    }

    public List<Integer> dfs(int i){
        Vertex vertex = vertexList[i];
        LinkedList<Vertex> stack = new LinkedList<>();
        Set<Integer> isVisit = new HashSet<>();
        stack.add(vertex);
        List<Integer> result = new ArrayList<>(this.vertexs);
        while (!stack.isEmpty()){
            Vertex pop = stack.pop();
            if(!isVisit.contains(pop.point)) {
                isVisit.add(pop.point);
                result.add(pop.point);
                for (int j = pop.nextVertexs.size() -1; j >= 0 ; j -- ) {
                    if( !isVisit.contains( pop.nextVertexs.get(j).point ) ){
                        stack.push(pop.nextVertexs.get(j));
                    }
                }
            }
        }
        return result;
    }
}
