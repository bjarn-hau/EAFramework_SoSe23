package de.heaal.eaf.testbench;

import de.heaal.eaf.algorithm.GeneticAlgorithm;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.crossover.SinglePointCrossover;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.HillClimbingMutation;
import de.heaal.eaf.testbench.functions.SphereFunction2D;

import java.util.function.Function;

public class TestGenericAlgorithm {

    public static void main(String[] args) {

        float[] min = {-5.12f, -5.12f};
        float[] max = {+5.12f, +5.12f};

        Function<Individual, Float> evalSphereFunc2D = new SphereFunction2D();

        var comparator = new MinimizeFunctionComparator<>(evalSphereFunc2D);
        GeneticAlgorithm algo = new GeneticAlgorithm(min, max,
                comparator,
                new HillClimbingMutation(-.5f, .5f),
                new ComparatorIndividual(0.001f),
                new SinglePointCrossover(),
                10,
                0.05f,
                0);
        algo.run();
    }
}
