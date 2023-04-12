package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.crossover.Combination;
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
                            float mutationProbability,
                            int N_e) {
        super(comparator, mutator);
        this.individualFactory = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.individuals = individuals;
        this.combination = combination;

        this.combination.setRandom(new Random());
        this.mutationOptions = new MutationOptions();
        mutationOptions.put(MutationOptions.KEYS.MUTATION_PROBABILITY, mutationProbability);

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
        bestIndividualEachGeneration.clear();
        int generation = 0;

        population.sort(comparator);
        this.bestIndividualEachGeneration.add(population.get(0));

        while (!isTerminationCondition() && generation < maximumGeneration) {
            nextGeneration();
            generation++;
        }

        System.out.println("Best individual: " + bestIndividualEachGeneration.get(bestIndividualEachGeneration.size() - 1).getCache());

        return this.bestIndividualEachGeneration;
    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();
        Random random = new Random();
        Population<Individual> children = new Population<>(individuals);

        for (int i = 0; i < this.N_e; i++) {
            children.add(population.get(i));
        }

        while (children.size() < population.size()) {

            Individual parent1 = SelectionUtils.selectNormal(population, random, null);
            Individual parent2 = SelectionUtils.selectNormal(population, random, parent1);

            Individual child = combination.combine(new Individual[]{parent1, parent2});

            mutator.mutate(child, this.mutationOptions);

            children.add(child);
        }

        population = children;

        children.sort(comparator);

        this.bestIndividualEachGeneration.add(children.get(0));

    }
}
