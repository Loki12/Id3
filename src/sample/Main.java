package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.model.MyTree;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

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

        for (int j = 0; j < matrix.get(0).size(); j++) {
            List<String> list1 = new ArrayList<>();
            for (int i = 0; i != matrix.size(); i++) {
                list1.add(matrix.get(i).get(j));
            }
            matrix2.add(list1);
        }

//        matrix2.forEach(System.out::println);

        MyTree root = new MyTree();
        initEntropyAndGain(matrix2);

    }

   // @Override
    public void start(Stage primaryStage) throws Exception{
        
        /*this.primaryStage = primaryStage;
        primaryStage.setTitle("ID3");
        
        initRootLayout();*/

        //fromMatToTree(matrix2, root, entropy);
    }



    private static void initEntropyAndGain(ArrayList<List<String>> matrix) {

        Double entropy = EntropyLevel(matrix.get(0));
        System.out.println("Entropy for 1= " + entropy);

        if (entropy!=0&&entropy!=1)
        {
            /*OptionalDouble GainLevel = matrix.stream().filter(str-> matrix.indexOf(str)>0).
                    mapToDouble(str->GainLevel(matrix, matrix.indexOf(str), entropy)).peek();
            */

            TreeMap<Integer, Double> gainList = new TreeMap<>();
            for(int i=1; i!=matrix.size(); i++)
            {
                double Gain = GainLevel(matrix, i, entropy);
                System.out.print("Gain for " + i + " = "+ Gain);
                gainList.put(i, Gain);
            }
            gainList.entrySet().forEach(str->System.out.println(str.getKey() + " " + str.getValue()));
            //gainList.entrySet().stream().max();

            //входим в этот ряд, сами продолжаем
            //учесть если нет продолжения или если оно одно
        }
    }

    private MyTree fromMatToTree(ArrayList<List<String>> matrix, MyTree root, Double entropy) {



        return null;
    }

    private static Double GainLevel(ArrayList<List<String>> matrix, int i, Double rootEntropy) {

        List<String> Distinctcollect  = matrix.get(i).stream().distinct().collect(Collectors.toList());

        System.out.print("Значения по одному разу");
        Distinctcollect.forEach(System.out::println);


        //возвращает списки значений
       /* List<List<String>> listList = Distinctcollect.stream().map(d -> {
           return matrix.get(i).stream().filter(d::equals).map(str ->
           {
                return matrix.get(0).get(matrix.get(i).indexOf(str));
           }).collect(Collectors.toList());
        }).collect(Collectors.toList());

        List<List<Integer>> listListInt = Distinctcollect.stream().map(d -> {
            return matrix.get(i).stream().filter(d::equals).map(str ->
            {
                return matrix.get(i).indexOf(str);
            }).collect(Collectors.toList());
        }).collect(Collectors.toList());
*/
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


        System.out.println("Список список " + i);
        listList1.forEach(System.out::println);

        //Считаем уровень для каждого
        List<Double> doubles = listList1.stream().map(Main::EntropyLevel).collect(Collectors.toList());

        System.out.println("Энтропия одного и второго");
        doubles.forEach(System.out::println);

        //вычисляем Gain
        double sum = 0;
        for (int j = 0; j < listList1.size(); j++) {
            sum += ((double)listList1.get(j).size()/(double) matrix.get(i).size() * doubles.get(j));
        }

        return rootEntropy - sum;
    }

    private static Double EntropyLevel(List<String> list) {
        List<String> Distinctcollect = list.stream().distinct().collect(Collectors.toList());

        //        Distinctcollect.forEach(System.out::println);

        //считаем вероятности
/*
        List<Double> probabilityList = new ArrayList<>();
        for (String string: Distinctcollect)
        {
            probabilityList.add((double)list.stream().filter(str-> str.equals(string)).count() / (double) list.size());
        }
*/

        DoubleStream doubleStream = Distinctcollect.stream().mapToDouble(str-> {
            return (double)list.stream().filter(p-> p.equals(str)).count() / (double)list.size();
                }
        );

        List<Double> doubleList = new ArrayList<>();
        doubleStream.forEach(doubleList::add);

//        doubleList.forEach(System.out::println);

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
