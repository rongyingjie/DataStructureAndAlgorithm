package cn.gxufe.graph;

import org.junit.Test;

import java.util.List;

public class MainClass {

    @Test
    public void testBfsAndDfs(){
        UndirectedGraph graph = new UndirectedGraph("simple_graph.txt");
        List<Integer> bfs = graph.bfs();
        System.out.println("bfs = "+bfs);
        List<Integer> dfs = graph.dfs();
        System.out.println("dfs = "+ dfs);
    }

    @Test // 最小生成树
    public void testKruskal(){
        UndirectedWeightGraph undirectedWeightGraph = new UndirectedWeightGraph("simple_weighteGraph.txt");
        UndirectedWeightGraph undirectedWeightGraph2 = undirectedWeightGraph.mstKruskal();
        System.out.println(undirectedWeightGraph2);
    }


    @Test // 最小生成树
    public void testPrim(){
        UndirectedWeightGraph undirectedWeightGraph = new UndirectedWeightGraph("simple_weighteGraph.txt");
        UndirectedWeightGraph undirectedWeightGraph1 = undirectedWeightGraph.mstPrim();
        System.out.println(undirectedWeightGraph1);
    }


    @Test // 单源最短路径
    public void testDijkstra(){
        WeightDiGraph weightDiGraph = new WeightDiGraph("simple_weightedDigraph.txt");
        WeightDiGraph.Result dijkstra = weightDiGraph.Dijkstra(0, 6);
        System.out.println(dijkstra.points); // 经过的节点
        System.out.println(dijkstra.weight); // 总的权重值
    }

    @Test // 多源最短路径
    public void testFloyd(){
        WeightDiGraph weightDiGraph = new WeightDiGraph("simple_weightedDigraph.txt");
        double[][] floyd = weightDiGraph.Floyd();
        // floyd[i][j] 表示为 i -> j  的最短路径
        System.out.println(floyd);
    }


}