package cn.gxufe.sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeapSort implements Iterator<Integer> {

    private List<Integer> data ;

    public HeapSort(int size){
        data = new ArrayList<>(size);
    }

    public void add(Integer value) {
        data.add(value);
        int index = data.size() - 1;
        int parent = (index -1 ) / 2;
        makeBalance(index,parent);
    }

    protected void makeBalance(int index,int parent){
        if( index <= parent ){
            return;
        }
        if( data.get(index) < data.get(parent)) {
            Integer tmp = data.get(parent);
            data.set(parent,data.get(index));
            data.set(index,tmp);
            makeBalance(parent,(parent -1 ) / 2 );
        }
    }

    public boolean hasNext() {
        return !data.isEmpty();
    }

    public Integer next() {
        Integer integer = data.get(0);
        Integer last = data.get(data.size() - 1);
        data.set(0,last);
        data.remove(data.size()-1);
        removeMakeBalance(0);
        return integer;
    }

    protected void removeMakeBalance(int parent){
        int right = (parent + 1) *  2;
        int left = right - 1;
        if( (data.size() -1 ) < left ){ // 没有子节点
            return;
        }
        if( (data.size() -1 ) >= left && (data.size() -1 ) < right ) { // 只有左孩子
            Integer value = data.get(parent);
            Integer leftValue = data.get(left);
            if( value > leftValue ) {
                swap(parent,left);
            }
            return;
        }
        Integer rightValue = data.get(right);
        Integer leftValue = data.get(left);
        Integer value = data.get(parent);
        if ( rightValue > leftValue ){
            if( leftValue < value ){
                swap(parent,left);
            }
        }else {
            if( rightValue < value  ){
                swap(parent,right);
            }
        }
    }

    protected void swap(int parent,int children){
        Integer value = data.get(parent);
        Integer leftValue = data.get(children);
        value = value + leftValue;
        leftValue = value - leftValue;
        value = value - leftValue;
        data.set( parent, value);
        data.set(children,leftValue);
        removeMakeBalance(children);
    }

    public int size(){
        return this.data.size();
    }

}
