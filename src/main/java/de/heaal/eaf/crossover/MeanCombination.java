package de.heaal.eaf.crossover;

import de.heaal.eaf.base.GenericIndividual;
import de.heaal.eaf.base.Individual;
import de.heaal.eaf.base.VecN;

import java.util.Random;

public class MeanCombination implements Combination<Individual> {

    private Random random;

    @Override
    public void setRandom(Random rng) {
        this.random = rng;
    }

    @Override
    public Individual combine(Individual[] parents) {
        assert parents.length == 2;

        VecN childGenome = new VecN(parents[0].getGenome().len());

        for (Individual parent : parents) {
            childGenome.add(parent.getGenome());
        }

        for (int i = 0; i < childGenome.len(); i++) {
            childGenome.array()[i] /= childGenome.len();
        }

        return new GenericIndividual(childGenome);
    }
}
