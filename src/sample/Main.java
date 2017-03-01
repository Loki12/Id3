package sample;

import sample.model.Math_help;
import sample.model.MyPoint;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main  {

    private static List<String> names;
    private static final String FILE_NAME = "C:\\Users\\Sergej\\IdeaProjects\\Spring\\Id3\\src\\sample\\1.txt";


    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME)));
        List<String> list =  reader.lines().collect(Collectors.toList());
        ArrayList<List<String>> matrix = new ArrayList<>();
        ArrayList<List<String>> finalMatrix = matrix;
        list.forEach(str -> finalMatrix.add(Arrays.asList(str.split(" "))));

        names = finalMatrix.get(0);
        matrix = Math_help.rotateMatrix(finalMatrix);

        MyPoint root = new MyPoint("Root");
        root.setRoot(true);

        initEntropyAndGain(matrix, root);
        String end = "End";
        System.out.println(end);
    }


    private static void initEntropyAndGain(ArrayList<List<String>> matrix, MyPoint root) {

        //посчитали энтропию
        Double entropy = Math_help.EntropyLevel(matrix.get(0));


        if (entropy!=0)
        {
            //выбираем максимальный Gain
            Map.Entry maxGainEntry = Math_help.indexOfStringWithMaxGain(matrix, entropy);
            Integer maxGainIndex = (Integer) maxGainEntry.getKey();

            if ((Double) maxGainEntry.getValue()==0.0 && matrix.size()==2)
            {
                //вычислить значение по вероятности
                List<String> list = matrix.get(0);
                List<Double> prob = list.stream().map(str->
                        (double)list.stream().filter(a -> a.equals(str)).count() / (double)list.size()
                ).collect(Collectors.toList());


                Double maxprob = prob.stream().max(Double::compareTo).get();
                Integer index = prob.indexOf(maxprob);

                root.leaves.add(new MyPoint(matrix.get(0).get(index)));
            }
            else {
                //пишем его в Root
                root.name += names.get(maxGainIndex);
                names.remove(maxGainIndex);

                //создаем листья
                List<String> leaves_names = matrix.get(maxGainIndex).stream().distinct().collect(Collectors.toList());
                List<ArrayList<List<String>>> lists = SeparateTable(matrix, maxGainIndex);

                //разделим на несколько таблиц и запустим каждую из них
                /*for (int i = 0; i < leaves_names.size(); i++) {
                    MyPoint myPoint = new MyPoint(leaves_names.get(i));
                    root.leaves.add(myPoint);
                    initEntropyAndGain(lists.get(i), myPoint);
                }*/

                leaves_names.forEach(leave->{
                    MyPoint myPoint = new MyPoint(leave);
                    root.leaves.add(myPoint);
                    initEntropyAndGain(lists.get(leaves_names.indexOf(leave)), myPoint);
                });
            }
        }
        else
        {
            root.leaves.add(new MyPoint(matrix.get(0).get(0)));
        }
    }

    private static List<ArrayList<List<String>>> SeparateTable(ArrayList<List<String>> matrix, Integer maxgain) {

        List<String> leaves_names = matrix.get(maxgain).stream().distinct().collect(Collectors.toList());
        List<ArrayList<List<String>>> lists = new ArrayList<>();

        //получаем список индексов строк
        List<List<Integer>> DistinctListInteger = new ArrayList<>();
        for(int i = 0; i!= leaves_names.size(); i++)
        {
            List<Integer> list = new ArrayList<>();
            for(int j=0; j!=matrix.get(0).size(); j++ )
            {
                if (Objects.equals(matrix.get(maxgain).get(j), leaves_names.get(i)))
                {
                    list.add(j);
                }
            }
            DistinctListInteger.add(list);
        }

        System.out.println();
        System.out.println("Смотрим это");
        DistinctListInteger.forEach(System.out::println);

        //удаляем строку по которой разделяем
        matrix.remove(maxgain);


        //для каждого значения
        for (List<Integer> aDistinctListInteger : DistinctListInteger) {
            ArrayList<List<String>> a = new ArrayList<>();
            for (List<String> aMatrix : matrix) {
                List<String> b = new LinkedList<>();
                for (Integer anADistinctListInteger : aDistinctListInteger) {
                    b.add(aMatrix.get(anADistinctListInteger));
                }
                a.add(b);
            }
            lists.add(a);
        }

        return lists;
    }
}
