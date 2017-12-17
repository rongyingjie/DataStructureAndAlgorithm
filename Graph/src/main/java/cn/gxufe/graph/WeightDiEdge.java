package cn.gxufe.graph;

public class WeightDiEdge extends UndirectedEdge{


    private WeightDiEdge before;

    public WeightDiEdge(int from, int to, double weight) {
        super(from, to, weight);
    }

    public int hashCode() {
        return getFrom()<<16 + getTo();
    }

    public boolean equals(Object obj) {
        if(! (obj instanceof WeightDiEdge)) {
            return false;
        }
        WeightDiEdge other = (WeightDiEdge) obj;
        if( this.getTo().equals(other.getTo()) && this.getFrom().equals(other.getFrom()) ){
            return true;
        }
        return false;
    }

    public WeightDiEdge getBefore() {
        return before;
    }

    public void setBefore(WeightDiEdge before) {
        this.before = before;
    }

    @Override
    public String toString() {
        return this.from + "," + this.to + ","+ this.weight;
    }
}
