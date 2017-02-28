package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.MyPoint;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Main /*extends Application*/ {

    private Stage primaryStage;


    public static void main(String[] args) throws FileNotFoundException {
        //launch(args);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Sergej\\IdeaProjects\\Spring\\Id3\\src\\sample\\1.txt")));
        List<String> list =  reader.lines().collect(Collectors.toList());
        ArrayList<List<String>> matrix = new ArrayList<List<String>>();
        list.forEach(str -> matrix.add(Arrays.asList(str.split(" "))));

//        matrix.forEach(System.out::println);

        ArrayList<List<String>> matrix2 = new ArrayList<List<String>>();

        List<String> names = matrix.get(0);
        for (int j = 1; j < matrix.get(0).size(); j++) {
            List<String> list1 = new ArrayList<>();
            for (int i = 0; i != matrix.size(); i++) {
                list1.add(matrix.get(i).get(j));
            }
            matrix2.add(list1);
        }

//        matrix2.forEach(System.out::println);

        MyPoint root = new MyPoint("Root");
        root.setRoot(true);
        initEntropyAndGain(matrix2, names, root);

        double f = 32;
    }

   // @Override
    public void start(Stage primaryStage) throws Exception{
        
        /*this.primaryStage = primaryStage;
        primaryStage.setTitle("ID3");
        
        initRootLayout();*/

        //fromMatToTree(matrix2, root, entropy);
    }



    private static void initEntropyAndGain(ArrayList<List<String>> matrix, List<String> names, MyPoint root) {

        Double entropy = EntropyLevel(matrix.get(0));

        if (entropy!=0&&entropy!=1)
        {
            TreeMap<Integer, Double> gainList = new TreeMap<>();
            for(int i=1; i!=matrix.size(); i++)
            {
                double Gain = GainLevel(matrix, i, entropy);
                System.out.print("Gain for " + i + " = "+ Gain);
                gainList.put(i, Gain);
            }

            Integer maxgain = gainList.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();

            if (root.isRoot())
            {
                root.name = names.get(maxgain);
            }

            List<String> leaves_names = new LinkedList<>();
            List<List<List<String>>> lists = SeparateTable(matrix, maxgain, leaves_names);
            //разделим на несколько таблиц и запустим каждую из них

            for (int i = 0; i < leaves_names.size(); i++) {
                MyPoint myPoint = new MyPoint(leaves_names.get(i));
                initEntropyAndGain(matrix, names, myPoint);
                root.leaves.add(myPoint);
            }

        }
    }

    private static List<List<List<String>>> SeparateTable(ArrayList<List<String>> matrix, Integer maxgain, List<String> leaves_names) {

        List<String> DistinctList = matrix.get(maxgain).stream().distinct().collect(Collectors.toList());
        leaves_names = DistinctList;
        List<List<List<String>>> lists = new ArrayList<>();

        for(int i=0; i!=DistinctList.size(); i++)
        {
            List<List<String>> listList = new ArrayList<>();
            lists.add(listList);
        }

        for (int i = 0; i < matrix.get(maxgain).size(); i++) {
            for (int j=0; j !=DistinctList.size(); j++)
            {
                if (matrix.get(maxgain).get(i).equals(DistinctList.get(j)))
                {
                    for(int h=0; h!=matrix.size(); h++)
                    {
                        lists.get(j).get(h).add(matrix.get(h).get(i));
                    }
                }
            }

        }

        return lists;
    }

    private MyPoint fromMatToTree(ArrayList<List<String>> matrix, MyPoint root, Double entropy) {



        return null;
    }

    private static Double GainLevel(ArrayList<List<String>> matrix, int i, Double rootEntropy) {

        List<String> Distinctcollect  = matrix.get(i).stream().distinct().collect(Collectors.toList());

        System.out.print("Значения по одному разу");
        Distinctcollect.forEach(System.out::println);

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

        List<Double> doubles = listList1.stream().map(Main::EntropyLevel).collect(Collectors.toList());


        double sum = 0;
        for (int j = 0; j < listList1.size(); j++) {
            sum += ((double)listList1.get(j).size()/(double) matrix.get(i).size() * doubles.get(j));
        }

        return rootEntropy - sum;
    }

    private static Double EntropyLevel(List<String> list) {
        List<String> Distinctcollect = list.stream().distinct().collect(Collectors.toList());

        DoubleStream doubleStream = Distinctcollect.stream().mapToDouble(str->
                (double)list.stream().filter(p-> p.equals(str)).count() / (double)list.size()
        );

        List<Double> doubleList = new ArrayList<>();
        doubleStream.forEach(doubleList::add);

        return Math.abs(doubleList.stream().mapToDouble(str-> {
            return str*Math.log(str)/Math.log(2);
        }).sum());

        //Большой вопрос, почему нельзя сделть так
//        Double DumD = doubleStream.map(str-> str*Math.log1p(str)).sum();
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
