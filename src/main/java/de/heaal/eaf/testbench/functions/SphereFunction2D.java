package de.heaal.eaf.testbench.functions;

import de.heaal.eaf.base.Individual;

import java.util.function.Function;

public class SphereFunction2D implements Function<Individual, Float> {

    @Override
    public Float apply(Individual individual) {
        float x = individual.getGenome().array()[0];
        float y = individual.getGenome().array()[1];
        return x * x + y * y;
    }
}
