package de.heaal.eaf.crossover;

import de.heaal.eaf.base.Individual;

import java.util.Random;

public class DACrossover implements Combination {

    float F = new Random().nextFloat(0.4f, 0.9f);


    @Override
    public void setRandom(Random rng) {
        F = rng.nextFloat(0.4f, 0.9f);
    }

    @Override
    public Individual combine(Individual[] parents) {
        Individual child = parents[0].copy();
        Individual sub = parents[1].copy().sub(parents[2]);
        sub.mul(F);
        return child.add(sub);
    }
}
