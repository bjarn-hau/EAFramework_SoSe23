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

package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.GenericIndividualFactory;
import de.heaal.eaf.base.Algorithm;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.IndividualFactory;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Implementation of the Hill Climbing algorithm.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 */
public class HillClimbingAlgorithm extends Algorithm<Individual> {

    private final IndividualFactory<Individual> indFac;
    private final ComparatorIndividual terminationCriterion;
    
    public HillClimbingAlgorithm(float[] min, float[] max, 
            Comparator<Individual> comparator, Mutation mutator, 
            ComparatorIndividual terminationCriterion) 
    {
        super(comparator, mutator);
        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
    }
    
    @Override
    public void nextGeneration() {
        super.nextGeneration();

        // HIER KÖNNTE DER ALGORITHMUS-LOOP STEHEN
        Individual parent = population.get(0);
        Individual child = parent.copy();

        MutationOptions options = new MutationOptions();
        options.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 1.0f);

        mutator.mutate(child, new MutationOptions());

        if(comparator.compare(child, parent) > 0) {
            population.set(0, child);
        }
    }
  
    @Override
    public boolean isTerminationCondition() {
        // Because we only have a population of 1 individual we know that
        // this individual is our current best.
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public void run() {
        initialize(indFac, 1);
        int generation = 0;
        
        while(!isTerminationCondition()) {
            nextGeneration();
            System.out.printf("Generation %d: %s with fitness %f\n", generation,
                    Arrays.toString(population.get(0).getGenome().array()), population.get(0).getCache());
            generation++;
        }
        System.out.printf("Best individual: %s with fitness %f after %d generations",
                Arrays.toString(population.get(0).getGenome().array()), population.get(0).getCache(), generation);
    }

}
