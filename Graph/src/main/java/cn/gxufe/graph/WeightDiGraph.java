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

    private WeightDiGraph() {

    }

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
            vertices[v2].inDegree ++ ;
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

    public Object bellmanFord(int point) {

        Double[][] result = new Double[vertexs][vertexs];
        for(int i=0;i < vertexs;i++){
            result[point][i] = 0.0;
        }
        LinkedList<WeightDiEdge> stack = new LinkedList<>();
        for (UndirectedEdge undirectedEdge : this.vertices[point].edges){
            stack.add(  (WeightDiEdge) undirectedEdge );
        }
        int i = 1;
        int size =  stack.size();
        while ( ! stack.isEmpty() && i < vertexs){
            WeightDiEdge weightDiEdge = stack.pop();
            if ( result[i][weightDiEdge.getTo()] == null ) {
                result[i][weightDiEdge.getTo()] = weightDiEdge.weight  + result[i-1][weightDiEdge.getFrom()];
            }else {
                if( result[i][weightDiEdge.getFrom()] + weightDiEdge.weight < result[i][weightDiEdge.getTo()] ){
                    result[i][weightDiEdge.getTo()] =  result[i][weightDiEdge.getFrom()] + weightDiEdge.weight;
                }
            }
            for (UndirectedEdge undirectedEdge : this.vertices[weightDiEdge.to].edges ){
                if( result[i][undirectedEdge.getTo()] == null ){
                    stack.add(  (WeightDiEdge) undirectedEdge );
                }else{
                    if( result[i][undirectedEdge.getTo()] <  result[i][weightDiEdge.getTo()] + weightDiEdge.weight){
                        stack.add(  (WeightDiEdge) undirectedEdge );
                    }
                }
            }
            size --;
            if( size == 0 ){
                i ++ ;
                size =  stack.size();
                for(int j = 0; j  < vertexs && i < vertexs; j++ ){
                    if( result[i - 1][j] != null ){
                        result[i][j] = result[i - 1][j];
                    }
                }
            }
        }
        return result;
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

    public List<WeightVertex> topSort() {
        List<WeightVertex> result = new ArrayList<>(this.vertexs);
        LinkedList<WeightVertex> stack = new LinkedList<>();
        for (int i = 0; i < this.vertexs; i++) {
            WeightVertex vertex = this.vertices[i];
            if(vertex.inDegree == 0){
                stack.add(vertex);
            }
        }
        while (!stack.isEmpty()){
            WeightVertex pop = stack.pop();
            result.add(pop);
            if(pop.edges != null && pop.edges.size() > 0){
                List<UndirectedEdge> edges = pop.edges;
                for (UndirectedEdge v : edges) {
                    WeightVertex vertex = this.vertices[v.to];
                    vertex.inDegree--;
                    if( vertex.inDegree == 0 ){
                        stack.add(vertex);
                    }
                }
            }
        }
        return result;
    }

    public Object[] criticalPath(){
        double[][] ve_vl= new double[2][this.vertexs];
        double[][] e_l= new double[2][this.edges];
        List<WeightVertex> weightVertices = this.topSort();
        Set<UndirectedEdge> set = new HashSet<>();
        Map<UndirectedEdge,Integer> map = new HashMap<>(this.edges);
        int index = 0 ;
        for (WeightVertex weightVertex : weightVertices ) {
            List<UndirectedEdge> edges = weightVertex.edges;
            for (UndirectedEdge u : edges ) {
                map.put(u,index);
                set.add(u);
                if( ve_vl[0][u.to] < u.weight +  ve_vl[0][weightVertex.point] ){
                    ve_vl[0][u.to] = u.weight +  ve_vl[0][weightVertex.point];
                }
                e_l[0][index] = ve_vl[0][u.getFrom()]; // 活动最早发生时间
                index ++;
            }
        }
        ve_vl[1][weightVertices.get(weightVertices.size()-1).point] = ve_vl[0][weightVertices.get(weightVertices.size()-1).point];
        for (int i = weightVertices.size() -1; i > 0 ; i -- ) {
            WeightVertex weightVertex = weightVertices.get(i);
            Iterator<UndirectedEdge> iterator = set.iterator();
            while (iterator.hasNext()){
                UndirectedEdge next = iterator.next();
                if(next.getTo().equals(weightVertex.point)){
                    ve_vl[1][next.getFrom()] = ve_vl[1][next.getTo()] - next.weight;
                    Integer integer = map.get(next);
                    e_l[1][integer] = ve_vl[1][next.getFrom()];
                    iterator.remove();
                }
            }
        }
        List<WeightVertex> criticalPathVertex = new ArrayList<>();
        List<UndirectedEdge> criticalPathEdge = new ArrayList<>();
        for (int i = 0; i < ve_vl[0].length; i++) {
            if( ve_vl[0][i] == ve_vl[1][i] ) {
                criticalPathVertex.add(this.vertices[i]);
                List<UndirectedEdge> edges = this.vertices[i].edges;
                for (UndirectedEdge u : edges  ) {
                    Integer integer = map.get(u);
                    if (e_l[0][integer] == e_l[1][integer]){
                        criticalPathEdge.add(u);
                    }
                }
            }
        }
        Object[] result  = new Object[2];
        result[0]  =  criticalPathVertex;
        result[1]  =  criticalPathEdge;
        return result;
    }

}
