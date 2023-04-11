package de.heaal.eaf.algorithm;

import de.heaal.eaf.Tuple;
import de.heaal.eaf.base.*;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.crossover.SinglePointCrossover;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.selection.SelectionUtils;

import java.util.*;


public class GenericAlgorithm extends Algorithm<Individual> {

    private final IndividualFactory<Individual> individualFactory;
    private final ComparatorIndividual terminationCriterion;
    private final int individuals;
    private final MutationOptions mutationOptions;
    private final Combination combination;
    private final int N_e;


    public GenericAlgorithm(float[] min, float[] max,
                            Comparator<Individual> comparator,
                            Mutation mutator,
                            ComparatorIndividual terminationCriterion,
                            Combination combination,
                            int individuals,
                            int N_e) {
        super(comparator, mutator);
        this.individualFactory = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.individuals = individuals;
        this.combination = combination;

        this.combination.setRandom(new Random());
        this.mutationOptions = new MutationOptions();
        mutationOptions.put(MutationOptions.KEYS.MUTATION_PROBABILITY, 0.05f);

        this.N_e = N_e;
    }

    @Override
    protected boolean isTerminationCondition() {
        population.sort(comparator);
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public List<Individual> run() {
        initialize(individualFactory, individuals);
        int generation = 0;

//        isTerminationCondition();

        while (!isTerminationCondition() || generation <= 10.000) {
            nextGeneration();
            generation++;
        }

        population.sort(comparator);

        return this.bestIndividualEachGeneration;
    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();
        Random random = new Random();
        Population<Individual> children = new Population<>(individuals);

        population.sort(comparator); // calculates fitness and sorts them

        this.bestIndividualEachGeneration.add(population.get(0));

        population.forEach(individual -> System.out.println(individual.getCache()));

        for (int i = 0; i < this.N_e; i++) {
            children.add(population.get(i));
        }

        while (children.size() < population.size()) {

            Individual parent1 = SelectionUtils.selectNormal(population, random, null);
            Individual parent2 = SelectionUtils.selectNormal(population, random, null);

            Individual child = combination.combine(new Individual[]{parent1, parent2});

            System.out.println("Parent1 genome: " + Arrays.toString(parent1.getGenome().array()));
            System.out.println("Parent2 genome: " + Arrays.toString(parent2.getGenome().array()));
            System.out.println("Child genome: " + Arrays.toString(child.getGenome().array()));

            children.add(child);
        }

        children.forEach(individual -> mutator.mutate(individual, this.mutationOptions));

        population = children;

    }
}
