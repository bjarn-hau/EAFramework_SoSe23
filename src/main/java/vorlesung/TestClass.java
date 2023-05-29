package vorlesung;

import io.jenetics.Mutator;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.*;
import io.jenetics.prog.regression.Error;
import io.jenetics.prog.regression.LossFunction;
import io.jenetics.prog.regression.Regression;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;

import java.util.List;
import java.util.stream.IntStream;

public class TestClass {

    static Double f(double x) {
        return Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x + 1;
    }

    static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
            MathOp.ADD,
            MathOp.SUB,
            MathOp.MUL
    );

    static final ISeq<Op<Double>> TERMINALS = ISeq.of(
            Var.of("x", 0),
            EphemeralConst.of(() ->
                    (double) RandomRegistry.random().nextInt(10))
    );

    static final List<Sample<Double>> SAMPLES = IntStream.of(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5)
            .mapToObj(i -> Sample.ofDouble(i, f(i)))
            .collect(java.util.stream.Collectors.toList());

    private static final Regression<Double> REGRESSION = Regression.of(
            Regression.codecOf(OPERATIONS, TERMINALS, 5),
            Error.of(LossFunction::mse),
            SAMPLES
    );


    public static void main(String[] args) {

        final Engine<ProgramGene<Double>, Double> engine = Engine
                .builder(REGRESSION)
                .minimizing()
                .alterers(
                        new SingleNodeCrossover<>(0.1),
                        new Mutator<>())
                .build();

        final EvolutionResult<ProgramGene<Double>, Double> result = engine
                .stream()
                .limit(Limits.byFitnessThreshold(0.01))
                .collect(EvolutionResult.toBestEvolutionResult());

        final ProgramGene<Double> program = result.bestPhenotype()
                .genotype()
                .gene();

        final TreeNode<Op<Double>> tree = program.toTreeNode();
        MathExpr.rewrite(tree); // Simplify result program.
        System.out.println("Generations: " + result.totalGenerations());
        System.out.println("Function:    " + new MathExpr(tree));
        System.out.println("Error:       " + REGRESSION.error(tree));
    }
}