package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.GenericAlgorithm;
import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.crossover.SinglePointCrossover;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.HillClimbingMutation;
import de.heaal.eaf.testbench.functions.SphereFunction2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BenchmarkAlgorithmen {


    public BenchmarkAlgorithmen(Algorithm<Individual> one, Algorithm<Individual> two) throws IOException {
        List<Individual> bestIndividualsAlgoOne = one.run();
        List<Individual> bestIndividualsAlgoTwo = two.run();

        // write the best individuals of the first algorithm to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/de/heaal/eaf/testbench/algorithmOne.csv"))) {
            writer.write("Generation;Fitness");
            writer.newLine();
            for (int i = 0; i < bestIndividualsAlgoOne.size(); i++) {
                writer.write(i + ";" + bestIndividualsAlgoOne.get(i).getCache());
                writer.newLine();
            }
        }

        // write the best individuals of the second algorithm to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/de/heaal/eaf/testbench/algorithmTwo.csv"))) {
            writer.write("Generation;Fitness");
            writer.newLine();
            for (int i = 0; i < bestIndividualsAlgoTwo.size(); i++) {
                writer.write(i + ";" + bestIndividualsAlgoTwo.get(i).getCache());
                writer.newLine();
            }
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
                0
        );

        HillClimbingAlgorithm hillClimbingAlgorithm = new HillClimbingAlgorithm(
                min,
                max,
                new MinimizeFunctionComparator(new SphereFunction2D()),
                new HillClimbingMutation(-.5f, .5f),
                new ComparatorIndividual(0.001f)
        );

        BenchmarkAlgorithmen benchmarkAlgorithmen = new BenchmarkAlgorithmen(genericAlgorithm, hillClimbingAlgorithm);
    }

}
