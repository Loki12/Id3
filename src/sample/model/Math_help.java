package sample.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Math_help {

    public static ArrayList<List<String>> rotateMatrix(ArrayList<List<String>> matrix) {

        ArrayList<List<String>> matrix2 = new ArrayList<>();

        for (int j = 0; j < matrix.get(0).size(); j++) {
            List<String> list1 = new ArrayList<>();
            for (int i = 1; i != matrix.size(); i++) {
                list1.add(matrix.get(i).get(j));
            }
            matrix2.add(list1);
        }

        return matrix2;
    }

    public static Double EntropyLevel(List<String> list) {

        List<String> Distinctcollect = list.stream().distinct().collect(Collectors.toList());

        DoubleStream probabilities = Distinctcollect.stream().mapToDouble(str->
                (double)list.stream().filter(p-> p.equals(str)).count() / (double)list.size()
        );

        List<Double> doubleList = new ArrayList<>();
        probabilities.forEach(doubleList::add);

        return Math.abs(doubleList.stream().mapToDouble(str-> str*Math.log(str)/Math.log(2)).sum());

        //Большой вопрос, почему нельзя сделть так
//        Double DumD = probabilities.map(str-> str*Math.log1p(str)).sum();
    }

    public static Map.Entry<Integer, Double> indexOfStringWithMaxGain(ArrayList<List<String>> matrix, Double entropy) {

        TreeMap<Integer, Double> gainList = new TreeMap<>();
        for(int i=1; i!=matrix.size(); i++)
        {
            double Gain = GainLevel(matrix, i, entropy);
            System.out.print("Gain for " + i + " = "+ Gain);
            gainList.put(i, Gain);
        }
         return gainList.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
    }

    private static Double GainLevel(ArrayList<List<String>> matrix, int i, Double rootEntropy) {

        List<String> Distinctcollect  = matrix.get(i).stream().distinct().collect(Collectors.toList());

        List<List<String>> listList1 = new ArrayList<>();
        List<String> str;
        for(int g = 0; g!=Distinctcollect.size(); g++){
            str = new ArrayList<>();
            listList1.add(str);
        }

        for (int h=0; h!=matrix.get(i).size(); h++)
        {
            for (int g=0; g!=Distinctcollect.size(); g++)
            {
                if (matrix.get(i).get(h).equals(Distinctcollect.get(g)))
                {
                    listList1.get(g).add(matrix.get(0).get(h));
                }
            }
        }

        List<Double> doubles = listList1.stream().map(Math_help::EntropyLevel).collect(Collectors.toList());


        double sum = 0;
        for (int j = 0; j < listList1.size(); j++) {
            sum += ((double)listList1.get(j).size()/(double) matrix.get(i).size() * doubles.get(j));
        }

        return rootEntropy - sum;
    }
}
