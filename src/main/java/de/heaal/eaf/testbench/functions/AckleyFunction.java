package de.heaal.eaf.testbench.functions;

import de.heaal.eaf.base.Individual;

import java.util.function.Function;

public class AckleyFunction implements Function<Individual, Float> {
    @Override
    public Float apply(Individual individual) {
        float x = individual.getGenome().array()[0];
        float y = individual.getGenome().array()[1];
        return (float) (-20 * Math.exp(-0.2 * Math.sqrt(0.5 * (x * x + y * y))) -
                Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y))) +
                Math.E + 20);
    }
}
