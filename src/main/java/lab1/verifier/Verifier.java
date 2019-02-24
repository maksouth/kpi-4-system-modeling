package lab1.verifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Verifier {

    private static final int BUCKETS_NUM = 20;

    protected abstract double rangeProbability(double from, double to, List<Double> params);

    protected abstract List<Double> getDistributionParameters(List<Double> seq);

    protected abstract String printDistributionInfo(double chi, List<Double> params);

    public String verifyResult(List<Double> seq) {
        List<Double> params = getDistributionParameters(seq);

        double min = Collections.min(seq);
        double max = Collections.max(seq);
        double step = (max - min) / (BUCKETS_NUM - 1);
        int buckets[] = new int[BUCKETS_NUM];
        Arrays.fill(buckets, 0);
        List<Integer> bucketsToPrint = new ArrayList<>();
        for (double val : seq) {
            int bucket = (int) ((val - min) / step);
            ++buckets[bucket];
            bucketsToPrint.add(bucket);
        }
        double chi = 0.0;
        for (int i = 0; i < BUCKETS_NUM; ++i) {
            double expectedValue = rangeProbability(min + step*i, min + step*(i + 1), params);
            chi += Math.pow(buckets[i]*1.0 - seq.size()*expectedValue, 2) / (seq.size()*expectedValue);
        }
        return printDistributionInfo(chi, params);
    }
}
