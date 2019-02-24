package lab1.verifier;

import java.util.ArrayList;
import java.util.List;

public class UniformVerifier extends Verifier {

    private static final double CHI = 27.587;

    @Override
    protected List<Double> getDistributionParameters(List<Double> seq) {
        List<Double> params = new ArrayList<>();
        double expectedValue = seq.stream().reduce((a, b) -> a + b).get() / seq.size();
        double variance = seq.stream().map(x -> Math.pow(expectedValue - x, 2)).reduce((a, b) -> a + b).get() / seq.size();
        params.add(expectedValue - Math.sqrt(3*variance));
        params.add(expectedValue + Math.sqrt(3*variance));
        return params;
    }

    private double density(double x, List<Double> params) {
        if (x < params.get(0)) {
            return 0;
        }
        if (x > params.get(1)) {
            return 1;
        }
        return (x - params.get(0)) / (params.get(1) - params.get(0));
    }

    @Override
    protected double rangeProbability(double from, double to, List<Double> params) {
        return density(to, params) - density(from, params);
    }

    @Override
    protected String printDistributionInfo(double chi, List<Double> params) {
        if (chi < CHI) {
            return "With probability 0.95 It's a uniform distribution";
        } else {
            return "With probability 0.95 It's not a uniform distribution";
        }
    }
}
