package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.Math_help;
import sample.model.MyPoint;

import java.io.*;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Main /*extends Application*/ {

    private Stage primaryStage;
    static MyPoint root;
    static List<String> names;


    public static void main(String[] args) throws FileNotFoundException {
        //launch(args);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Sergej\\IdeaProjects\\Spring\\Id3\\src\\sample\\1.txt")));
        List<String> list =  reader.lines().collect(Collectors.toList());
        ArrayList<List<String>> matrix = new ArrayList<List<String>>();
        ArrayList<List<String>> finalMatrix = matrix;
        list.forEach(str -> finalMatrix.add(Arrays.asList(str.split(" "))));

        names = finalMatrix.get(0);
        matrix = Math_help.rotateMatrix(finalMatrix);

        root = new MyPoint("Root");
        root.setRoot(true);

        initEntropyAndGain(matrix, root);
        double f = 32.0;
    }



    // @Override
    public void start(Stage primaryStage) throws Exception{
        
        /*this.primaryStage = primaryStage;
        primaryStage.setTitle("ID3");
        
        initRootLayout();*/

        //fromMatToTree(matrix2, root, entropy);
    }



    private static void initEntropyAndGain(ArrayList<List<String>> matrix, MyPoint root) {

        //посчитали энтропию
        Double entropy = Math_help.EntropyLevel(matrix.get(0));


        if (entropy!=0)
        {
            //выбираем максимальный Gain
            Map.Entry maxgEntry = Math_help.indexOfStringWithMaxGain(matrix, entropy);
            Integer maxGainIndex = (Integer) maxgEntry.getKey();
            Double maxGain = (Double) maxgEntry.getValue();

            if (maxGain==0.0 && matrix.size()==2)
            {
                //вычислить значение по вероятности
                List<String> list = matrix.get(0);
                List<Double> prob = list.stream().map(str->
                        { String string = str;
                            return (double)((double)list.stream().filter(a -> a.equals(string)).count() / (double)list.size());
                        }
                ).collect(Collectors.toList());


                Double maxprob = prob.stream().max(Double::compareTo).get();
                Integer index = list.indexOf(maxprob);

                root.leaves.add(new MyPoint(matrix.get(0).get(index)));
            }
            else {
                //пишем его в Root
                root.name = root.name += names.get(maxGainIndex);
                names.remove(maxGainIndex);

                //создаем листья
                List<String> leaves_names = matrix.get(maxGainIndex).stream().distinct().collect(Collectors.toList());
                List<ArrayList<List<String>>> lists = SeparateTable(matrix, maxGainIndex);

                //добавляем листья

            /*System.out.println();
            System.out.println("Мы сейчас проверяем это");
            lists.forEach(a-> a.forEach(System.out::println));*/

                //разделим на несколько таблиц и запустим каждую из них
                for (int i = 0; i < leaves_names.size(); i++) {
                    MyPoint myPoint = new MyPoint(leaves_names.get(i));
                    root.leaves.add(myPoint);
                    initEntropyAndGain(lists.get(i), myPoint);
                }
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
        for (int h = 0; h < DistinctListInteger.size(); h++) {
            ArrayList<List<String>> a = new ArrayList<>();
            for (int i = 0; i < matrix.size(); i++) {
                List<String> b = new LinkedList<>();
                for (int j = 0; j < DistinctListInteger.get(h).size(); j++) {
                    b.add(matrix.get(i).get(DistinctListInteger.get(h).get(j)));
                }
                a.add(b);
            }
            lists.add(a);
        }

        return lists;
    }

    private MyPoint fromMatToTree(ArrayList<List<String>> matrix, MyPoint root, Double entropy) {



        return null;
    }





    private void showPersonalOverView() {

    }

    private void initRootLayout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();

/*
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
*/
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
