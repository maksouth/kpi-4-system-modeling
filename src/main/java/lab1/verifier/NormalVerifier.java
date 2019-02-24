package lab1.verifier;

import java.util.ArrayList;
import java.util.List;

public class NormalVerifier extends Verifier {

    private static final double CHI = 27.587;

    @Override
    protected List<Double> getDistributionParameters(List<Double> seq) {
        List<Double> params = new ArrayList<>();
        double mean = seq.stream().reduce((a, b) -> a + b).get() / seq.size();
        double variance = seq.stream().map(x -> Math.pow(mean - x, 2)).reduce((a, b) -> a + b).get() / seq.size();
        params.add(mean);
        params.add(Math.sqrt(variance));
        return params;
    }


    @Override
    protected double rangeProbability(double from, double to, List<Double> params) {
        return  cdf((to - params.get(0)) / params.get(1), params)
                - cdf((from - params.get(0)) / params.get(1), params);
    }

    private double cdf(double x, List<Double> params) {
        double sum = x;
        double value = x;
        for (int i = 0; i < 100; ++i) {
            value += x*x/(2*i+1);
            sum = sum + value;
        }
        return 0.5 + (sum / Math.sqrt(2 * Math.PI)) * Math.exp(-(x*x)/2.0);
    }

    @Override
    protected String printDistributionInfo(double chi, List<Double> params) {
        if (chi < CHI) {
            return String.format("With probability 0.95 It's a normal distribution with mean = %.2f and deviation = %.2f\n",
                    params.get(0), params.get(1));
        } else {
            return "With probability 0.95 It's not a normal distribution";
        }
    }
}
