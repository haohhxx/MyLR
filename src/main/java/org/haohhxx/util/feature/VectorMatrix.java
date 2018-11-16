package org.haohhxx.util.feature;

import com.google.common.collect.Sets;
import org.haohhxx.util.io.IteratorReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhenyuan_hao@163.com
 */
public class VectorMatrix extends ArrayList<VectorLine> {

    public VectorMatrix(){

    }

    private VectorMatrix(List<VectorLine> lines){
        this.addAll(lines);
    }

    private HashMap<Integer,Double> max = new HashMap<>(256);
    private HashMap<Integer,Double> min = new HashMap<>(256);


    public VectorMatrix cut(int start, int end){
        return new VectorMatrix(this.subList(start,end));
    }

    @Override
    public boolean add(VectorLine vectorLine){
        vectorLine.forEach((index,value)->{
            double maxOrDefault = max.getOrDefault(index,Double.MIN_VALUE);
            if(maxOrDefault<value){
                max.put(index,value);
            }
            double minOrDefault = min.getOrDefault(index,Double.MAX_VALUE);
            if(minOrDefault>value){
                min.put(index,value);
            }
        });
        return super.add(vectorLine);
    }

    public HashMap<Integer, Double> getMax() {
        return max;
    }

    public HashMap<Integer, Double> getMin() {
        return min;
    }

    public List<Double> getTargetList(){
        return this.stream().map(VectorLine::getTarget).collect(Collectors.toList());
    }

    public static RankVectorMatrixBuilder rankVectorMatrixBuilder(){
        return new RankVectorMatrixBuilder();
    }

    public static VectorMatrix loadSampleSVMRankFile(String filePath){
        RankVectorMatrixBuilder rankVectorMatrixBuilder = new RankVectorMatrixBuilder();
        IteratorReader.getIteratorReader(filePath).forEach(rankVectorMatrixBuilder::add);
        return rankVectorMatrixBuilder.getVectorMatrix();
    }


}