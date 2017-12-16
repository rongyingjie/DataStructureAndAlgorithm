package cn.gxufe.graph;

import java.io.*;
import java.util.*;

/**
 * 有向带权图
 */
public class WeightDiGraph {

    private Integer vertexs;
    private Integer edges;
    private WeightVertex[] vertices;

    private double[][] map;

    public WeightDiGraph(String path){
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

    private void init(BufferedReader bufferedReader) throws IOException {
        String tmp;
        while (( tmp =  bufferedReader.readLine() )!=null){
            String[] split = tmp.split(" ");
            int v1 = Integer.valueOf(split[0].trim());
            int v2 = Integer.valueOf(split[1].trim());
            double weight = Double.valueOf(split[2].trim());
            WeightDiEdge weightDiEdge = new WeightDiEdge(v1, v2, weight);
            if (vertices[v1] == null) {
                vertices[v1] = new WeightVertex();
                vertices[v1].point = v1;
            }
            if (vertices[v2] == null){
                vertices[v2] = new WeightVertex();
                vertices[v2].point = v2;
            }
            vertices[v1].edges.add(weightDiEdge);
            map[v1][v2] = weight;
        }
    }

    private void readVertexsAndDdges(BufferedReader bufferedReader) throws IOException {
        String tmp = bufferedReader.readLine();
        vertexs = Integer.valueOf(tmp.trim());
        tmp = bufferedReader.readLine();
        edges = Integer.valueOf(tmp);
        vertices = new WeightVertex[vertexs];
        map = new double[vertexs][vertexs];
        for (int i = 0; i < vertexs; i++) {
            for (int j = 0; j < vertexs; j++) {
                if( i == j){
                    map[i][j] = 0;
                }else {
                    map[i][j] = Double.MAX_VALUE;
                }
            }
        }
    }

    /**
     * @param start
     * @param end
     * @return
     */

    public Result Dijkstra(int start ,int end){
        if( start == end ){
            return Result.getResult();
        }
        List<UndirectedEdge> edges = this.vertices[start].edges;
        PriorityQueue<WeightDiEdge> priorityQueue = new PriorityQueue<>();
        for (UndirectedEdge weightDiEdge :edges ){
            priorityQueue.add((WeightDiEdge)weightDiEdge);
        }
        while (true) {
            WeightDiEdge poll = priorityQueue.poll();
            if(poll == null){
                return Result.getResult();
            }
            Integer to = poll.getTo();
            if(to.equals(end)){
                Result result = new Result();
                result.weight = poll.getWeight();
                while (poll != null){
                    result.points.push(poll.getTo());
                    poll = poll.getBefore();
                }
                result.points.push(start);
                return result;
            }
            edges = this.vertices[to].edges;
            for (UndirectedEdge weightDiEdge :edges ) {
                WeightDiEdge item = new WeightDiEdge(weightDiEdge.getFrom(),weightDiEdge.getTo(),weightDiEdge.getWeight() + poll.getWeight());
                item.setBefore(poll);
                priorityQueue.add(item);
            }
        }
    }

    public static class Result {
        public double weight = -1;
        public LinkedList<Integer> points  = new LinkedList<>();
        private static Result result = new Result();
        public static Result getResult(){
            return result;
        }

    }


    public double[][] Floyd(){
        for (int i = 0; i < this.vertexs; i++) {
            for (int j = 0; j < this.vertexs; j++) {
                if( i!=j )
                {
                    for (int k = 0; k < this.vertexs; k++) {
                        if( i != k && j!= k  ){
                            if(map[j][k] > map[j][i] + map[i][k]){
                                map[j][k] = map[j][i] + map[i][k];
                            }
                        }
                    }
                }
            }
        }
        return map;
    }
}
