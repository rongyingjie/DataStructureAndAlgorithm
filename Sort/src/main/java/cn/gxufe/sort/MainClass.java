package cn.gxufe.sort;

import org.junit.Test;

import java.io.*;
import java.util.Random;

public class MainClass {


    @Test
    public void testQuickSort(){
        Integer[] comparables = new Integer[100];
        Random random = new Random();
        for (int i=0;i<comparables.length;i++){
            comparables[i] = Math.abs(random.nextInt(500));
        }
        QuickSort.sort(comparables);
        for (Comparable c : comparables  ) {
            System.out.println(c);
        }
    }


    @Test
    public void testHeapSort(){
        HeapSort heapSort = new HeapSort(1000);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            heapSort.add( random.nextInt(1000) );
        }
        while (heapSort.hasNext()){
           Integer next = heapSort.next();
           System.out.println(next);
        }
    }

    @Test
    /**
     * 产生模拟数据
     */
    public void preData() throws IOException {
        FileWriter fileWriter = new FileWriter("analog_data.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        Random random = new Random();
        for (int i = 0; i< 100_000_00;i++){
            bufferedWriter.write(Integer.toString(Math.abs(random.nextInt())));
            bufferedWriter.newLine();
        }
    }

    @Test
    public void testExternalSort() throws IOException {
        ExternalSort externalSort = new ExternalSort("analog_data.txt");
        externalSort.sort("/tmp/result.data","/tmp/externalSort/");
    }




}
