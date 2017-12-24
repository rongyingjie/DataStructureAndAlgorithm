package cn.gxufe.sort;

public class QuickSort {


    public static void sort(Comparable[] comparables){
        sortThreeWayOrder(comparables,0,comparables.length-1 );
    }

    /**
     * 双路快排
     * @param comparables
     * @param start
     * @param end
     */
    private static void sortTwoWayOrder(Comparable[] comparables, int start, int end){
        if( start >= end ){
            return;
        }
        Comparable p = comparables[start];
        int index = start;
        int i = start+1,j = end;
        while ( i <= j ){
            if(comparables[i].compareTo(p) < 0){
                swap(comparables,index,i);
                index++;
                i++;
            } else {
                swap(comparables,i,j);
                j--;
            }
        }
        sortTwoWayOrder(comparables,start,i - 2);
        sortTwoWayOrder(comparables,i,end);
    }
    private static void swap(Comparable[] comparables,int i,int j){
        Comparable tmp = comparables[i];
        comparables[i] = comparables[j];
        comparables[j] = tmp;
    }

    /**
     * 三路快排
     * @param comparables
     * @param start
     * @param end
     */
    private static void sortThreeWayOrder(Comparable[] comparables, int start, int end){
        if( start >= end){
            return;
        }
        Comparable s1 = comparables[start];
        Comparable s2 = comparables[end];
        int index = start+1;
        while ( s1.equals(s2) && index <= end ) {
            s1 = comparables[index];
            index++;
        }
        if( index > end ){
            return;
        }

        if(s1.compareTo(s2) > 0 ){
            swap(comparables,index -1,end);
        }else {
            swap(comparables,start,index - 1 );
        }
        int i  = start + 1, j = end ;
        int pi = start;
        s1 = comparables[start];
        s2 = comparables[end];
        while ( i < j ){
            if( s2.compareTo(comparables[i]) <= 0 ){
                swap(comparables,i,j);
                j--;
                swap(comparables,i,j);
            }else if( s1.compareTo(comparables[i]) >= 0 ){
                swap(comparables,i,pi);
                pi++;
                swap(comparables,i,pi);
                i++;
            }else {
                i++;
            }
        }
        sortThreeWayOrder(comparables,start,pi-1);
        sortThreeWayOrder(comparables,pi+1,j);
        sortThreeWayOrder(comparables,j+1,end);
    }


}
