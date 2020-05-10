package com.shilei.tourist;

import java.util.LinkedList;
import java.util.List;

public class huigui {
    public static double [] getLinePara(Double [] [] points ) {
        double dbRt [] =new double [2];
        double dbXSum=0;
        for(int i=0;i<points[0].length;i++) {
            dbXSum=dbXSum+points[0][i];
        }
        double dbXAvg=dbXSum/points[0].length;
        double dbWHeadVal=0;
        for(int i=0;i<points[0].length;i++) {
            dbWHeadVal=dbWHeadVal+(points[0][i]-dbXAvg)*points[1][i];
        }
        double dbWDown=0;
        double dbWDownP=0;
        double dbWDownN=0;
        dbXSum=0;
        for(int i=0;i<points[0].length;i++) {
            dbWDownP=dbWDownP+points[0][i]*points[0][i];
            dbXSum=dbXSum+points[0][i];
        }
        dbWDown=dbWDownP-(dbXSum*dbXSum/points[0].length);
        double dbW=dbWHeadVal/dbWDown;
        dbRt[0]=dbW;
        double dbBSum=0;
        for(int i=0;i<points[0].length;i++) {
            dbBSum=dbBSum+(points[1][i]-dbW*points[0][i]);
        }
        double dbB=dbBSum/points[0].length;
        dbRt[1]=dbB;
        return dbRt;
    }

    /**
     * 二次指数平滑法求预测值
     * @param list 基础数据集合
     * @param year 未来第几期
     * @param modulus 平滑系数
     * @return 预测值
     */
    private static Double getExpect(List<Double> list, int year, Double modulus ) {
        if (list.size() < 10 || modulus <= 0 || modulus >= 1) {
            return null;
        }
        Double modulusLeft = 1 - modulus;
        Double lastIndex = list.get(0);
        Double lastSecIndex = list.get(0);
        for (Double data :list) {
            lastIndex = modulus * data + modulusLeft * lastIndex;
            lastSecIndex = modulus * lastIndex + modulusLeft * lastSecIndex;
        }
        Double a = 2 * lastIndex - lastSecIndex;
        Double b = (modulus / modulusLeft) * (lastIndex - lastSecIndex);
        return a + b * year;
    }




    public static void main(String[] args) {
        List<Double> list = new LinkedList<Double>();
        list.add(1d);
        list.add(2d);
        list.add(3d);
        list.add(4d);
        list.add(5d);
        list.add(6d);
        list.add(7d);
        list.add(8d);
        list.add(9d);
        list.add(10d);

        Double value = getExpect(list, 1, 0.6);
        System.out.println(value);
}

}