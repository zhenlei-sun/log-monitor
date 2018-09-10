package com.binace.logmonitor.collectors;

import io.prometheus.client.Collector;
import io.prometheus.client.CounterMetricFamily;
import io.prometheus.client.DoubleAdder;
import io.prometheus.client.SimpleCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Counter metric with set method, to track counts of events or running totals and can also be updated by set value.
 */
public class CounterWithSet extends SimpleCollector<CounterWithSet.Child> implements Collector.Describable {

    CounterWithSet(CounterWithSet.Builder b) {
        super(b);
    }

    public static class Builder extends SimpleCollector.Builder<CounterWithSet.Builder, CounterWithSet> {
        @Override
        public CounterWithSet create() {
            return new CounterWithSet(this);
        }
    }

    /**
     * Return a Builder to allow configuration of a new Counter. Ensures required fields are provided.
     *
     * @param name The name of the metric
     * @param help The help string of the metric
     */
    public static CounterWithSet.Builder build(String name, String help) {
        return new CounterWithSet.Builder().name(name).help(help);
    }

    /**
     * Return a Builder to allow configuration of a new Counter.
     */
    public static CounterWithSet.Builder build() {
        return new CounterWithSet.Builder();
    }

    @Override
    protected CounterWithSet.Child newChild() {
        return new CounterWithSet.Child();
    }

    /**
     * The value of a single Counter.
     * <p>
     * <em>Warning:</em> References to a Child become invalid after using
     * {@link SimpleCollector#remove} or {@link SimpleCollector#clear},
     */
    public static class Child {
        private final DoubleAdder value = new DoubleAdder();

        /**
         * Increment the counter by 1.
         */
        public void inc() {
            inc(1);
        }

        /**
         * Increment the counter by the given amount.
         *
         * @throws IllegalArgumentException If amt is negative.
         */
        public void inc(double amt) {
            if (amt < 0) {
                throw new IllegalArgumentException("Amount to increment must be non-negative.");
            }
            value.add(amt);
        }

        public void set(double value) {
            synchronized(this) {
                this.value.reset();
                this.value.add(value);
            }
        }

        /**
         * Get the value of the counter.
         */
        public double get() {
            synchronized (this) {
                return value.sum();
            }
        }
    }

    // Convenience methods.

    /**
     * Increment the counter with no labels by 1.
     */
    public void inc() {
        inc(1);
    }

    /**
     * Increment the counter with no labels by the given amount.
     *
     * @throws IllegalArgumentException If amt is negative.
     */
    public void inc(double amt) {
        noLabelsChild.inc(amt);
    }


    /**
     * Increment the counter with no labels by 1.
     */
    public void set(double value) {
        noLabelsChild.set(value);
    }

    /**
     * Get the value of the counter.
     */
    public double get() {
        return noLabelsChild.get();
    }

    @Override
    public List<MetricFamilySamples> collect() {
        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>(children.size());
        for (Map.Entry<List<String>, CounterWithSet.Child> c : children.entrySet()) {
            samples.add(new MetricFamilySamples.Sample(fullname, labelNames, c.getKey(), c.getValue().get()));
        }
        return familySamplesList(Type.COUNTER, samples);
    }

    @Override
    public List<MetricFamilySamples> describe() {
        return Collections.<MetricFamilySamples>singletonList(new CounterMetricFamily(fullname, help, labelNames));
    }
}

