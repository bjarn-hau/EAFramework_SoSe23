package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DifferentialAlgorithm extends Algorithm<Individual> {

    private final IndividualFactory<Individual> indFac;
    private final int individualSize;
    private final ComparatorIndividual terminationCriterion;
    float c = rng.nextFloat(0.1f, 1f);
    float F = rng.nextFloat(0.4f, 0.9f);

    public DifferentialAlgorithm(float[] min, float[] max,
                                 Comparator<Individual> comparator, Mutation mutator,
                                 ComparatorIndividual terminationCriterion,
                                 int individualSize) {
        super(comparator,mutator);
        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.individualSize = individualSize;
    }

    @Override
    protected boolean isTerminationCondition() {
        return false;
    }

    @Override
    public List<Individual> run() {

        initialize(indFac, individualSize);

        int generation = 0;

        while (!isTerminationCondition() && generation < maximumGeneration) {
            nextGeneration();
            generation++;
        }

        return null;
    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();

        Individual u_i = null;
        Population u_is = new Population(population.size());

        for (Individual individual : population) {
            // TODO but i dont know what haha
            Individual r1 = population.get(rng.nextInt()); // r1 != i
            Individual r2 = population.get(rng.nextInt()); // r2 != i != r1
            Individual r3 = population.get(rng.nextInt()); // r3 != i != r1 != r2

            Individual mutated = new GenericIndividual(new VecN(r1.getGenome().len()).add(r2.getGenome().sub(r3.getGenome())));
            mutator.mutate(mutated, new MutationOptions());

            VecN v_i = r1.getGenome().add(mutated.getGenome());

            int J_r = rng.nextInt(population.get(0).getGenome().len());

            for (int j = 0; j < r1.getGenome().len(); j++) {
                float r_cj = rng.nextFloat();
                u_i = new GenericIndividual(new VecN(r1.getGenome().len()));
                if (r_cj < c || J_r == j) {
                    u_i.getGenome().array()[j] = v_i.array()[j];
                } else {
                    u_i.getGenome().array()[j] = individual.getGenome().array()[j];
                }
            }
        }

        for (int i  = 0; i < population.size(); i++) {
            if (comparator.compare(population.get(i), u_i) > 0) {
                population.set(i, u_i);
            }
        }

    }
}
