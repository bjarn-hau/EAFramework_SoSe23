package de.heaal.eaf.algorithm;

import de.heaal.eaf.base.*;
import de.heaal.eaf.crossover.Combination;
import de.heaal.eaf.evaluation.ComparatorIndividual;
import de.heaal.eaf.mutation.Mutation;
import de.heaal.eaf.mutation.MutationOptions;
import de.heaal.eaf.selection.SelectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DifferentialAlgorithm extends Algorithm<Individual> {

    private final IndividualFactory<Individual> indFac;
    private final int individualSize;
    private final ComparatorIndividual terminationCriterion;
    private final Combination<Individual> combination;
    float c = rng.nextFloat(0.1f, 1f);

    public DifferentialAlgorithm(float[] min, float[] max,
                                 Comparator<Individual> comparator,
                                 Combination<Individual> combination,
                                 ComparatorIndividual terminationCriterion,
                                 int individualSize) {
        super(comparator, null);
        this.indFac = new GenericIndividualFactory(min, max);
        this.terminationCriterion = terminationCriterion;
        this.combination = combination;
        this.individualSize = individualSize;
        System.out.println("c = " + c);
    }

    @Override
    protected boolean isTerminationCondition() {
        return comparator.compare(population.get(0), terminationCriterion) > 0;
    }

    @Override
    public List<Individual> run() {

        initialize(indFac, individualSize);

        int generation = 0;

        while (!isTerminationCondition() && generation < maximumGeneration) {
            nextGeneration();
            generation++;
            if (generation % 1000 == 0)
                System.out.printf("Generation %d: %s with fitness %.12f\n", generation,
                    Arrays.toString(population.get(0).getGenome().array()), population.get(0).getCache());
        }

        population.sort(comparator);

        return null;
    }

    @Override
    protected void nextGeneration() {
        super.nextGeneration();

        Population<Individual> u_is = new Population<>(population.size());
//        for (Individual ind : population)
//            u_is.add(ind.copy());

        //              individual ist das individuum, an stelle i
//        System.out.println(" NEW GENERATION ");
        for (Individual individual : population) {
//            System.out.println(individual.getGenome());
            Individual u_i = null;
            Individual r1 = SelectionUtils.selectUniform(population, rng, new Individual[]{individual}).copy(); // r1 != i
            Individual r2 = SelectionUtils.selectUniform(population, rng, new Individual[]{individual, r1}).copy(); // r2 != i != r1
            Individual r3 = SelectionUtils.selectUniform(population, rng, new Individual[]{individual, r1, r2}).copy(); // r3 != i != r1 != r2


//            Individual mutated = new GenericIndividual(new VecN(r1.getGenome().len()).add(r2.getGenome().sub(r3.getGenome())));
//            mutator.mutate(mutated, new MutationOptions());
//
//            VecN v_i = mutated.add(r1).mul(F).getGenome(); // TODO needs to be overworked

            VecN v_i = combination.combine(new Individual[]{r1, r2, r3}).getGenome();

//            System.out.println("Vi" + Arrays.toString(v_i.array()));

            int J_r = rng.nextInt(population.get(0).getGenome().len());

            VecN test = new VecN(r1.getGenome().len());
            for (int j = 0; j < r1.getGenome().len(); j++) {
                float r_cj = rng.nextFloat();
                u_i = new GenericIndividual(new VecN(r1.getGenome().len()));
//                System.out.println("iteration " + j);
//                System.out.println("Ui" + Arrays.toString(u_i.getGenome().array()));
//                System.out.println("v_i" + Arrays.toString(v_i.array()));
//                System.out.println("test" + Arrays.toString(test.array()));
//                System.out.println("ind" + Arrays.toString(individual.getGenome().array()));
                if (r_cj < c || J_r == j) {
//                    u_i.getGenome().array()[j] = v_i.array()[j];
                    test.array()[j] = v_i.array()[j];
                } else {
//                    u_i.getGenome().array()[j] = individual.getGenome().array()[j];
                    test.array()[j] = individual.getGenome().array()[j];
                }
            }
            u_i = new GenericIndividual(test);
//            System.out.println("Ui" + Arrays.toString(u_i.getGenome().array()));
            u_is.add(u_i);
        }

//        for (Individual ind : u_is) {
//            System.out.println(ind.getCache() + " " + ind.getGenome());
//        }

        for (int i = 0; i < population.size(); i++) {
            if (comparator.compare(u_is.get(i), population.get(i)) > 0) {
                population.set(i, u_is.get(i));
            }
        }

        population.sort(comparator);

    }
}
