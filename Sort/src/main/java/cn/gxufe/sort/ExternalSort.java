package cn.gxufe.sort;

import java.io.*;
import java.util.*;

public class ExternalSort {

    private String path;

    private List<String> tmpFiles = new ArrayList<>();
    private int fileMaxSize = 1_000_00;
    public ExternalSort(String path){
        this.path = path;
    }

    /**
     * @param outFile ： 输出文件
     * @param tmpPath ： 中间数据临时目录
     */
    public void sort(String outFile,String tmpPath) throws IOException {
        FileReader fileReader = new FileReader(this.path);
        HeapSort heapSort = new HeapSort(fileMaxSize);
        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tmp;
            while ((tmp = bufferedReader.readLine())!=null){
                Integer integer = Integer.valueOf(tmp);
                heapSort.add(integer);
                if(Integer.valueOf(fileMaxSize).equals( heapSort.size() )){
                    writeDisk(heapSort,tmpPath);
                }
            }
            writeDisk(heapSort,tmpPath);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            fileReader.close();
        }
        mergeFile(outFile);
    }


    protected void mergeFile(String outFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        HeapSortManager heapSortManager = new HeapSortManager();
        while (heapSortManager.hasNext()){
            bufferedWriter.write(heapSortManager.next().toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }


    protected void writeDisk(HeapSort heapSort,String tmpPath) throws IOException {
        String fileName = UUID.randomUUID().toString();
        FileWriter fileWriter = new FileWriter(tmpPath+fileName);
        tmpFiles.add(tmpPath+fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        while (heapSort.hasNext()){
            Integer next = heapSort.next();
            bufferedWriter.write(next.toString());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }


    private class HeapSortManager implements Iterator<Integer> {
        List<DataManager>  dataManagerList;
        public HeapSortManager() throws IOException {
            dataManagerList = new ArrayList<>(tmpFiles.size());
            for (String fileName : tmpFiles    ) {
                DataManager dataManager = new DataManager(fileName);
                dataManagerList.add(dataManager);
                int index = dataManagerList.size() - 1;
                int parent = (index -1 ) / 2;
                makeBalance(index,parent);
            }
        }

        protected void makeBalance(int index,int parent){
            if( index <= parent ){
                return;
            }
            DataManager di = dataManagerList.get(index);
            DataManager dp = dataManagerList.get(parent);
            if( di.peek() < dp.peek() ) {
                dataManagerList.set(parent,di);
                dataManagerList.set(index,dp);
                makeBalance(parent,(parent -1 ) / 2 );
            }
        }

        public boolean hasNext() {
            return !dataManagerList.isEmpty();
        }

        @Override
        public Integer next() {
            DataManager dataManager = dataManagerList.get(0);
            try {
                Integer pop = dataManager.pop();
                if( ! dataManager.hashNext()  ){
                    if( dataManagerList.size() > 1 ){
                        dataManager = dataManagerList.get(dataManagerList.size() - 1);
                        dataManagerList.remove(dataManagerList.size() - 1);
                        dataManagerList.set(0,dataManager);
                        removeMakeBalance(0);
                    }else {
                        dataManagerList.remove(0);
                    }
                }else {
                    removeMakeBalance(0);
                }
                return pop;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void removeMakeBalance(int parent){
            int right = (parent + 1) *  2;
            int left = right - 1;
            if( (dataManagerList.size() -1 ) < left ){ // 没有子节点
                return;
            }
            if( (dataManagerList.size() -1 ) >= left && (dataManagerList.size() -1 ) < right ) { // 只有左孩子
                DataManager parentDataManager = dataManagerList.get(parent);
                DataManager leftDataManager = dataManagerList.get(left);
                if( parentDataManager.peek() > leftDataManager.peek() ){
                    swap(parent,left);
                    removeMakeBalance(left);
                }
                return;
            }

            DataManager parentDataManager = dataManagerList.get(parent);
            DataManager leftDataManager = dataManagerList.get(left);
            DataManager rightDataManager = dataManagerList.get(right);
            if( rightDataManager.peek() > leftDataManager.peek() ){
                if( leftDataManager.peek() < parentDataManager.peek() ){
                    swap(parent,left);
                    removeMakeBalance(left);
                }
            }else {
                if( rightDataManager.peek() < parentDataManager.peek() ){
                    swap(parent,right);
                    removeMakeBalance(right);
                }
            }
        }
        protected void swap(int parent,int children){
            DataManager dataManager = dataManagerList.get(parent);
            DataManager dataManager1 = dataManagerList.get(children);
            dataManagerList.set(parent,dataManager1);
            dataManagerList.set(children,dataManager);
        }
    }

    private class DataManager {
        private LinkedList<Integer> linkedList = new LinkedList<>();
        private  BufferedReader bufferedReader;
        private int batch = 1000;
        public DataManager(String fileName) throws IOException {
            FileReader fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            this.readData();
        }
        public boolean hashNext(){
            return !linkedList.isEmpty();
        }
        public Integer peek(){
            return linkedList.peek();
        }
        public Integer pop() throws IOException {
            Integer pop = linkedList.pop();
            if(linkedList.isEmpty()){
                this.readData();
            }
            return pop;
        }
        protected void readData() throws IOException {
            String tmp ;
            int i = 0;
            while ((tmp = bufferedReader.readLine())!=null){
                Integer integer = Integer.valueOf(tmp);
                linkedList.add(integer);
                i ++;
                if( i>= batch ){
                    break;
                }
            }
        }
    }

}
