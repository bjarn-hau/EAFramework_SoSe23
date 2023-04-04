package de.heaal.eaf.mutation;

import de.heaal.eaf.base.Individual;

import java.util.Random;

public class HillClimbingMutation implements Mutation {

    private Random rng;
    private final float min;
    private final float max;

    public HillClimbingMutation(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void setRandom(Random rng) {
        this.rng = rng;
    }

    @Override
    public void mutate(Individual ind, MutationOptions opt) {
        int dimension = ind.getGenome().array().length;
        int toChange = opt.get(MutationOptions.KEYS.FEATURE_INDEX, rng.nextInt(dimension));
        float changeValueBy = rng.nextFloat(min, max);
        ind.getGenome().array()[toChange] += changeValueBy;
    }
}
