package cn.gxufe.graph;

public class UndirectedEdge implements Comparable<UndirectedEdge>{

    protected Integer from;
    protected Integer to;
    protected double weight;

    public UndirectedEdge(int from, int to, double weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        int max;
        int min;
        if(from > to){
            max = from;
            min = to;
        }else {
            max = to;
            min = from;
        }
        max = max << 16;
        return max | min;
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof UndirectedEdge)) {
            return false;
        }
        UndirectedEdge other = (UndirectedEdge)obj;
        if(from.equals(other.from) && to.equals(other.to)){
            return true;
        }
        if(to.equals(other.from) && to.equals(other.from)){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(UndirectedEdge o) {
        if( this.weight > o.weight  ){
            return 1;
        }else {
            return -1;
        }
    }
}
