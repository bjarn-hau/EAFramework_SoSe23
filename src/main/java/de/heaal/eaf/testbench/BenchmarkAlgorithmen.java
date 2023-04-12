package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.GeneticAlgorithm;
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
            bestIndividuals.put(algorithm.toString(), algorithm.run());
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
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
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

        BenchmarkAlgorithmen.benchmarkAlgorithms(new Algorithm[]{geneticAlgorithm, hillClimbingAlgorithm},
                new File("src/main/java/de/heaal/eaf/testbench/algorithmOne.csv"));

        var min2 = new float[]{-100f, -100f};
        var max2 = new float[]{+100f, +100f};

        GeneticAlgorithm geneticAlgorithm2 = new GeneticAlgorithm(
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
                new HillClimbingMutation(-3.5f, 3.5f),
                new ComparatorIndividual(0.001f)
        );

        BenchmarkAlgorithmen.benchmarkAlgorithms(new Algorithm[]{geneticAlgorithm2, hillClimbingAlgorithm2}, new File("src/main/java/de/heaal/eaf/testbench/algorithmTwo.csv"));

        GeneticAlgorithm geneticAlgorithm3 = new GeneticAlgorithm(
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

        BenchmarkAlgorithmen.benchmarkAlgorithms(new Algorithm[]{geneticAlgorithm3, hillClimbingAlgorithm2}, new File("src/main/java/de/heaal/eaf/testbench/algorithmThree.csv"));

        GeneticAlgorithm geneticAlgorithm4 = new GeneticAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-3.5f, 3.5f),
                new ComparatorIndividual(0.001f),
                new MeanCombination(),
                20,
                0.05f,
                2
        );

        benchmarkAlgorithms(new Algorithm[]{geneticAlgorithm4, hillClimbingAlgorithm2}, new File("src/main/java/de/heaal/eaf/testbench/algorithmFour.csv"));

        GeneticAlgorithm geneticAlgorithm5 = new GeneticAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-3.5f, 3.5f),
                new ComparatorIndividual(0.001f),
                new MeanCombination(),
                20,
                0.3f,
                2
        );

        GeneticAlgorithm geneticAlgorithm6 = new GeneticAlgorithm(
                min2,
                max2,
                new MinimizeFunctionComparator(new AckleyFunction()),
                new HillClimbingMutation(-3.5f, 3.5f),
                new ComparatorIndividual(0.001f),
                new MeanCombination(),
                10,
                0.3f,
                0
        );


        benchmarkAlgorithms(new Algorithm[]{geneticAlgorithm, geneticAlgorithm2, geneticAlgorithm3, geneticAlgorithm4, geneticAlgorithm5, geneticAlgorithm6,
                hillClimbingAlgorithm, hillClimbingAlgorithm2},
                new File("src/main/java/de/heaal/eaf/testbench/all.csv"));
    }

}