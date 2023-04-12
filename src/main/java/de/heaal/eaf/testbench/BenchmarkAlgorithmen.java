package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.GenericAlgorithm;
import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.crossover.MeanCombination;
import de.heaal.eaf.crossover.SinglePointCrossover;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.HillClimbingMutation;
import de.heaal.eaf.testbench.functions.AckleyFunction;
import de.heaal.eaf.testbench.functions.SphereFunction2D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BenchmarkAlgorithmen {


    public static void benchmarkAlgorithms(Algorithm<Individual>[] algorithms, File file) throws IOException {
        Map<String, List<Individual>> bestIndividuals = new HashMap<>();

        for (Algorithm<Individual> algorithm : algorithms) {
            bestIndividuals.put(algorithm.getClass().getSimpleName(), algorithm.run());
        }

        int max = 0;
        for (List<Individual> bestIndividual : bestIndividuals.values()) {
            max = Math.max(max, bestIndividual.size());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.format("Generation;%s", String.join(";", bestIndividuals.keySet())));
            writer.newLine();
            for (int i = 0; i < max; i++) {
                writer.write(i + ";");
                for (List<Individual> bestIndividual : bestIndividuals.values()) {
                    printAlgo(bestIndividual, writer, i);
                }
                writer.newLine();
            }
        }
    }


//    public static void benchMarkAlgorithm(Algorithm<Individual> one, Algorithm<Individual> two, File file) throws IOException {
//        List<Individual> bestIndividualsAlgoOne = one.run();
//        List<Individual> bestIndividualsAlgoTwo = two.run();
//
//        int max = Math.max(bestIndividualsAlgoOne.size(), bestIndividualsAlgoTwo.size());
//
//        // write the best individuals of the first algorithm to a file
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.write(String.format("Generation;%s;%s", one.getClass().getSimpleName(), two.getClass().getSimpleName()));
//            writer.newLine();
//            for (int i = 0; i < max; i++) {
//                writer.write(i + ";");
//
//                printAlgo(bestIndividualsAlgoOne, writer, i);
//
//                printAlgo(bestIndividualsAlgoTwo, writer, i);
//
//                writer.newLine();
//            }
//        }
//    }

    private static void printAlgo(List<Individual> bestIndividuals, BufferedWriter writer, int i) throws IOException {
        if (i < bestIndividuals.size()) {
            writer.write(String.format(Locale.GERMAN, "%.8f", bestIndividuals.get(i).getCache()) + ";");
        } else {
            writer.write(String.format(Locale.GERMAN, "%.8f", bestIndividuals.get(bestIndividuals.size() - 1).getCache()) + ";");
        }
    }

    public static void main(String[] args) throws IOException {
        float[] min = {-5.12f, -5.12f};
        float[] max = {+5.12f, +5.12f};
        GenericAlgorithm genericAlgorithm = new GenericAlgorithm(
                min,
                max,
                new MinimizeFunctionComparator(new SphereFunction2D()),
                new HillClimbingMutation(-.5f, .5f),
                new ComparatorIndividual(0.001f),
                new SinglePointCrossover(),
                10,
                0.05f,
                0
        );

        HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm(
                min,
                max,
                new MinimizeFunctionComparator(new SphereFunction2D()),
                new HillClimbingMutation(-.5f, .5f),
                new ComparatorIndividual(0.001f)
        );

        BenchmarkAlgorithmen.benchmarkAlgorithms(new Algorithm[]{genericAlgorithm, hillClimbingAlgorithm},
                new File("src/main/java/de/heaal/eaf/testbench/algorithmOne.csv"));

        var min2 = new float[]{-100f, -100f};
        var max2 = new float[]{+100f, +100f};

        GenericAlgorithm genericAlgorithm2 = new GenericAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-1.5f, 1.5f),
                new ComparatorIndividual(0.001f),
                new SinglePointCrossover(),
                10,
                0.05f,
                1
        );

        HillClimbingAlgorithm hillClimbingAlgorithm2 = new HillClimbingAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-1.5f, 1.5f),
                new ComparatorIndividual(0.001f)
        );

        BenchmarkAlgorithmen.benchmarkAlgorithms(new Algorithm[]{genericAlgorithm2, hillClimbingAlgorithm2}, new File("src/main/java/de/heaal/eaf/testbench/algorithmTwo.csv"));

        GenericAlgorithm genericAlgorithm3 = new GenericAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-.5f, .5f),
                new ComparatorIndividual(0.001f),
                new MeanCombination(),
                10,
                0.05f,
                0
        );

        BenchmarkAlgorithmen.benchmarkAlgorithms(new Algorithm[]{genericAlgorithm3, hillClimbingAlgorithm2}, new File("src/main/java/de/heaal/eaf/testbench/algorithmThree.csv"));

        GenericAlgorithm genericAlgorithm4 = new GenericAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-1.5f, 1.5f),
                new ComparatorIndividual(0.001f),
                new MeanCombination(),
                10,
                0.05f,
                2
        );

        benchmarkAlgorithms(new Algorithm[]{genericAlgorithm4, hillClimbingAlgorithm2}, new File("src/main/java/de/heaal/eaf/testbench/algorithmFour.csv"));

    }

}
