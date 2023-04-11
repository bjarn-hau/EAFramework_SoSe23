/*
 * Evolutionary Algorithms Framework
 *
 * Copyright (c) 2023 Christian Lins <christian.lins@haw-hamburg.de>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.heaal.eaf.testbench;

import de.heaal.eaf.Tuple;
import de.heaal.eaf.algorithm.HillClimbingAlgorithm;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.evaluation.MinimizeFunctionComparator;
import de.heaal.eaf.mutation.HillClimbingMutation;
import de.heaal.eaf.testbench.functions.AckleyFunction;
import de.heaal.eaf.testbench.functions.SphereFunction2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Test bench for the Hill Climbing algorithm.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class TestHillClimbing {
    public static void main(String[] args) {
        runSphere2DFunctionHillClimbing();

        runAckleyFunctionHillClimbing();
    }

    private static void runSphere2DFunctionHillClimbing() {
        float[] min = {-5.12f, -5.12f};
        float[] max = {+5.12f, +5.12f};

        // Sphere Function n=2
        Function<Individual,Float> evalSphereFunc2D = new SphereFunction2D();

        var comparator = new MinimizeFunctionComparator(evalSphereFunc2D);
        
        var algo = new HillClimbingAlgorithm(min, max,
                comparator, new HillClimbingMutation(-.5f, .5f), new ComparatorIndividual(0.001f));
//        algo.run();
        List<Tuple> bestIndividualsHill = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
//            bestIndividualsHill.add(algo.run());
        }
//        bestIndividualsHill.stream().map(Individual::getCache).mapToDouble(Float::doubleValue).average().ifPresent(System.out::println);
        // get the average of generations
        bestIndividualsHill.stream()
                .mapToInt(Tuple::generations)
                .average()
                .ifPresent(value -> System.out.println("Hill Climbing Average generations: " + value));
    }

    private static void runAckleyFunctionHillClimbing() {
        var min = new float[] {-100f, -100f};
        var max = new float[] {+100f, +100f};

        Function<Individual, Float> evalAckleyFunc = new AckleyFunction();

        var comparator = new MinimizeFunctionComparator(evalAckleyFunc);
        var algo2 = new HillClimbingAlgorithm(min, max,
                comparator, new HillClimbingMutation(-5f, 5f), new ComparatorIndividual(0.001f));
        List<Tuple> bestIndividuals = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
//            bestIndividuals.add(algo2.run());
        }

        // get the average of the best individuals
        bestIndividuals.stream()
                .map(Tuple::bestIndividual)
                .map(Individual::getCache)
                .mapToDouble(Float::doubleValue)
                .average()
                .ifPresent(System.out::println);

        algo2.run();
    }
}
