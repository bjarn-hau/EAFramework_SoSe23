package de.heaal.eaf.testbench.functions;

import de.heaal.eaf.base.Individual;

import java.util.Map;
import java.util.function.Function;

public class SinusFunction implements Function<Individual, Float> {

    private Map<Integer, Float> data;

    public SinusFunction(Map<Integer, Float> data) {
        this.data = data;
    }

    @Override
    public Float apply(Individual individual) {
        float A = individual.getGenome().array()[0];
        float f = individual.getGenome().array()[1];
        float phi = individual.getGenome().array()[2];
        float D = individual.getGenome().array()[3];
        float diffSum = 0;
        for (Map.Entry<Integer, Float> entry : data.entrySet()) {
            float diff = (float) Math.abs((A * Math.sin(2 * Math.PI * f * entry.getKey() + phi) + D) - entry.getValue());
            diffSum += diff * diff;
        }
        return diffSum;
    }

//    @Override
//    public Float apply(Individual individual, Integer time) {
//        float A = individual.getGenome().array()[0];
//        float f = individual.getGenome().array()[1];
//        float phi = individual.getGenome().array()[2];
//        float D = individual.getGenome().array()[3];
//        return (float) (A * Math.sin(2 * Math.PI * f * time + phi) + D);
//    }
}
