package lab1.verifier;

import java.util.ArrayList;
import java.util.List;

public class ExpVerifier extends Verifier {

    private static final double CHI = 28.9;

    @Override
    protected List<Double> getDistributionParameters(List<Double> seq) {
        List<Double> params = new ArrayList<>();
        params.add(seq.size() / seq.stream().reduce((a, b) -> a + b).get());
        return params;
    }

    @Override
    protected String printDistributionInfo(double chi, List<Double> params) {
        if (chi < CHI) {
            return String.format(
                    "CHI = %.2f With probability 0.95 It's a exponential distribution with lambda = %.2f\n",
                    chi,
                    params.get(0));
        } else {
            return "With probability 0.95 It's not a exponential distribution";
        }
    }

    @Override
    protected double rangeProbability(double from, double to, List<Double> params) {
        return Math.exp(-params.get(0) * from) - Math.exp(-params.get(0) * to);
    }
}
