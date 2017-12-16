package cn.gxufe.graph;

import java.io.*;
import java.util.*;

public class UndirectedWeightGraph {

    private Integer vertexs;
    private Integer edges;
    private WeightVertex[] vertices;
    private Set<UndirectedEdge> undirectedEdges;

    public UndirectedWeightGraph(Integer vertexs){
        undirectedEdges = new HashSet<>();
        this.vertexs = vertexs;
        this.vertices = new WeightVertex[vertexs];
    }

    public UndirectedWeightGraph(String path){
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

    protected void init(BufferedReader bufferedReader)  throws IOException {
        String tmp;
        while (( tmp =  bufferedReader.readLine() )!=null){
            String[] split = tmp.split(" ");
            int v1 = Integer.valueOf(split[0].trim());
            int v2 = Integer.valueOf(split[1].trim());
            double weight = Double.valueOf(split[2].trim());
            UndirectedEdge undirectedEdgeV1 = new UndirectedEdge(v1, v2, weight);
            UndirectedEdge undirectedEdgeV2 = new UndirectedEdge(v2, v1, weight);
            if (vertices[v1] == null){
                vertices[v1] = new WeightVertex();
                vertices[v1].point = v1;
            }

            if (vertices[v2] == null){
                vertices[v2] = new WeightVertex();
                vertices[v2].point = v2;
            }
            vertices[v1].edges.add(undirectedEdgeV1);
            vertices[v2].edges.add(undirectedEdgeV2);
            undirectedEdges.add(undirectedEdgeV2);
        }
    }

    protected void readVertexsAndDdges(BufferedReader bufferedReader) throws IOException {
        String tmp = bufferedReader.readLine();
        vertexs = Integer.valueOf(tmp.trim());
        tmp = bufferedReader.readLine();
        edges = Integer.valueOf(tmp);
        vertices = new WeightVertex[vertexs];
        undirectedEdges = new HashSet<>(edges);
    }


    public UndirectedWeightGraph mstPrim() {
        UndirectedEdge pop = this.getMin();
        UndirectedWeightGraph mst = new UndirectedWeightGraph(this.vertexs);
        PriorityQueue<UndirectedEdge> priorityQueue = new PriorityQueue();
        Set<UndirectedEdge> isAdd = new HashSet<>();
        add(mst,pop,isAdd,priorityQueue);
        int counter = 2;
        while (!mst.vertexs.equals(counter)){
            pop = priorityQueue.poll();
            if(mst.vertices[ pop.getFrom() ] == null || mst.vertices[pop.getTo()] == null){
                counter += add(mst,pop,isAdd,priorityQueue);
            }
        }
        mst.edges = mst.undirectedEdges.size();
        return mst;
    }

    protected int add(UndirectedWeightGraph mst,UndirectedEdge pop,Set<UndirectedEdge> isAdd,PriorityQueue<UndirectedEdge> priorityQueue){
        mst.undirectedEdges.add(pop);
        int counter = 0 ;
        if(mst.vertices[pop.getFrom()] == null){
            mst.vertices[pop.getFrom()] = new WeightVertex();
            mst.vertices[pop.getFrom()].point = pop.getFrom();
            counter++;
        }
        mst.vertices[pop.getFrom()].edges.add(pop);
        for (int i = 0; i < this.vertices[pop.getFrom()].edges.size(); i++) {
            if(!isAdd.contains(this.vertices[pop.getFrom()].edges.get(i)) ){
                isAdd.add(this.vertices[pop.getFrom()].edges.get(i));
                priorityQueue.add(this.vertices[pop.getFrom()].edges.get(i));
            }
        }
        if(mst.vertices[pop.getTo()] == null ){
            mst.vertices[pop.getTo()] = new WeightVertex();
            mst.vertices[pop.getTo()].point = pop.getTo();
            counter++;
        }
        mst.vertices[pop.getTo()].edges.add(new UndirectedEdge(pop.getTo(), pop.getFrom(), pop.getWeight()));
        for (int i = 0; i < this.vertices[pop.getTo()].edges.size(); i++) {
            if(!isAdd.contains(this.vertices[pop.getTo()].edges.get(i))){
                isAdd.add(this.vertices[pop.getTo()].edges.get(i));
                priorityQueue.add(this.vertices[pop.getTo()].edges.get(i));
            }
        }
        return counter;
    }



    private UndirectedEdge getMin(){
        Optional<UndirectedEdge> min = undirectedEdges.stream().min((UndirectedEdge o1, UndirectedEdge o2) -> {
            if( o1.getWeight() > o2.getWeight() ){
                return 1;
            }else {
                return -1;
            }
        });
        return min.get();
    }

    public UndirectedWeightGraph mstKruskal(){
        PriorityQueue<UndirectedEdge> priorityQueue = new PriorityQueue();
        priorityQueue.addAll(this.undirectedEdges);
        UndirectedWeightGraph mst = new UndirectedWeightGraph(this.vertexs);
        int counter = 0;
        while (!mst.vertexs.equals(counter)){
            UndirectedEdge poll = priorityQueue.poll();
            if( findVertiex(mst,poll.getTo(),poll.getFrom())){
                counter+=add(poll,mst);
            }
        }
        mst.edges = mst.undirectedEdges.size();
        return mst;
    }


    public boolean findVertiex(UndirectedWeightGraph mst ,Integer v1,Integer v2){
        if( mst.vertices[v1] == null || mst.vertices[v2] == null ){
            return true;
        }
        Set<Integer> isVisit = new HashSet<>();
        LinkedList<WeightVertex> stack = new LinkedList<>();
        stack.add(mst.vertices[v1]);
        while (!stack.isEmpty()){
            WeightVertex pop = stack.pop();
            if( isVisit.contains( pop.point )){
                 continue;
            }
            isVisit.add(pop.point);
            List<UndirectedEdge> edges = pop.edges;
            for (int i = 0; i < edges.size(); i++) {
                UndirectedEdge tmp = edges.get(i);
                if( !isVisit.contains(tmp.getTo()) ){
                    stack.add(mst.vertices[tmp.getTo()]);
                }
                if(!isVisit.contains(tmp.getFrom())){
                    stack.add(mst.vertices[tmp.getFrom()]);
                }
            }
        }
       if(isVisit.contains(v1) && isVisit.contains(v2)){
            return false;
       }else {
           return true;
       }
    }


    protected int add(UndirectedEdge poll,UndirectedWeightGraph mst) {
        int counter = 0;
        if( mst.vertices[poll.getFrom()] == null ){
            mst.vertices[poll.getFrom()] = new WeightVertex();
            mst.vertices[poll.getFrom()].point = poll.getFrom();
            counter++;
        }
        mst.vertices[poll.getFrom()].edges.add(poll);
        if( mst.vertices[poll.getTo()] == null ){
            mst.vertices[poll.getTo()] = new WeightVertex();
            mst.vertices[poll.getTo()].point = poll.getTo();
            counter++;
        }
        mst.vertices[poll.getTo()].edges.add(new UndirectedEdge( poll.getTo(),poll.getFrom(),poll.getWeight()));
        mst.undirectedEdges.add(poll);
        return counter;
    }


}
