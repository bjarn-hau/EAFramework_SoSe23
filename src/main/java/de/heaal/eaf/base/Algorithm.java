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

package de.heaal.eaf.base;

import de.heaal.eaf.Tuple;
import de.heaal.eaf.mutation.Mutation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * An abstract Algorithm class.The Algorithm class is intended to represent a specific algorithm and holds
 values of one run of this algorithm.
 * 
 * It is not recommended to reuse Algorithm
 objects but create new ones for every new run of an algorithm.
 * 
 * @author Christian Lins <christian.lins@haw-hamburg.de>
 * @param <T>
 */
public abstract class Algorithm<T extends Individual> {
    
    protected Comparator<T> comparator;
    protected Mutation mutator;
    protected Population<T> population;
    protected Random rng;
    protected List<Individual> bestIndividualEachGeneration = new ArrayList<>();
    protected int maximumGeneration = 100_000;

    
    public Algorithm(Random rng) {
        this.rng = rng;
    }
    
    public Algorithm(Random rng, Comparator<T> comparator) {
        this(comparator, null, rng);
    }

    public Algorithm(Comparator<T> comparator, Mutation mutator) {
        this(comparator, mutator, new Random());
    }

    public Algorithm(Comparator<T> comparator, Mutation mutator, int maximumGeneration) {
        this(comparator, mutator, new Random());
        this.maximumGeneration = maximumGeneration;
    }
    
    public Algorithm(Comparator<T> comparator, Mutation mutator, long seed) {
        this(comparator, mutator, new Random(seed));
    }
    
    public Algorithm(Comparator<T> comparator, Mutation mutator, Random rng) {
        this.rng = rng;
        this.comparator = comparator;
        this.mutator = mutator;
        
        if (mutator != null)
            mutator.setRandom(rng);
    }
    
    protected void createPopulation(IndividualFactory iFak, int num) {
        population = new Population<>(iFak, num);
    }
    
    protected abstract boolean isTerminationCondition();
    
    protected void initialize(IndividualFactory iFak, int numIndividuals) {
        createPopulation(iFak, numIndividuals);
    }
    
    protected void nextGeneration() {
        population.nextGeneration();
    }
    
    public abstract List<Individual> run();
}
