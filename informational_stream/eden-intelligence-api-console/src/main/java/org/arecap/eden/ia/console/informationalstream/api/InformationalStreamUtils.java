package org.arecap.eden.ia.console.informationalstream.api;


import org.arecap.eden.ia.console.informationalstream.api.bean.FeatureStreamBean;
import org.arecap.eden.ia.console.informationalstream.api.factory.FeatureStreamPropertiesFactory;
import org.arecap.eden.ia.console.informationalstream.api.factory.SignalPropertiesFactory;
import org.arecap.eden.ia.console.informationalstream.api.factory.StreamApplicationGraphPropertiesFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.arecap.eden.ia.console.informationalstream.api.StreamProcessType.*;

public final class InformationalStreamUtils {

    public static Map<String, String> streamApplicationTypeTopology = Arrays.stream(new String[][]{
            //upstream map
            {UpstreamSelectorFunction.name(), "u(S)"}, {UpstreamDetectorFunction.name(), "u(D)"}, {UpstreamConsumerFunction.name(), "u(C)"},
            {UpstreamSelectorSystem.name(), "S(u)"}, {UpstreamDetectorSystem.name(), "D(u)"}, {UpstreamConsumerSystem.name(), "C(u)"},
            //downstream map
            {DownstreamSelectorFunction.name(), "d(S)"}, {DownstreamDetectorFunction.name(), "d(D)"}, {DownstreamConsumerFunction.name(), "d(C)"},
            {DownstreamSelectorSystem.name(), "S(d)"}, {DownstreamDetectorSystem.name(), "D(d)"}, {DownstreamConsumerSystem.name(), "C(d)"}
    }).collect(Collectors.toMap(v -> v[0], v -> v[1]));

    public static Set upstreamTopology = Arrays.stream(new String[][]{
            //upstream map 8
            {"u(S)", "D(u)"}, {"D(u)", "u(C)"}, {"u(C)", "S(u)"}, {"S(u)", "u(C)"}, {"u(D)", "u(S)"}, {"C(u)", "u(D)"}, {"C(u)", "u(S)"}, {"u(S)", "C(u)"}

    }).collect(Collectors.toSet());

    public static Set downstreamTopology = Arrays.stream(new String[][]{
            //downstream map 8
            {"D(d)", "d(S)"}, {"D(d)", "d(C)"}, {"d(C)", "S(d)"}, {"S(d)", "d(C)"}, {"S(d)", "d(D)"}, {"C(d)", "d(D)"}, {"C(d)", "d(S)"}, {"d(S)", "C(d)"},

    }).collect(Collectors.toSet());

    public static Set upstreamFunctionTopology = Arrays.stream(new String[][]{
            //function upstream map 4
            {"u(S)", "u(C)"}, {"u(C)", "u(S)"}, {"u(D)", "u(S)"}, {"u(C)", "u(D)"}

    }).collect(Collectors.toSet());

    public static Set upstreamSystemTopology = Arrays.stream(new String[][]{
            //system upstream map 4
            {"S(u)", "C(u)"}, {"C(u)", "S(u)"}, {"S(u)", "D(u)"}, {"D(u)", "C(u)"}

    }).collect(Collectors.toSet());

    public static Set downstreamFunctionTopology = Arrays.stream(new String[][]{
            //function downstream map 4
            {"d(C)", "d(S)"}, {"d(S)", "d(C)"}, {"d(D)", "d(S)"}, {"d(D)", "d(C)"}

    }).collect(Collectors.toSet());

    public static Set downstreamSystemTopology = Arrays.stream(new String[][]{
            //system downstream map 4
            {"C(d)", "S(d)"}, {"S(d)", "C(d)"}, {"S(d)", "d(D)"}, {"C(d)", "D(d)"}

    }).collect(Collectors.toSet());

    public static Set upstreamsTopology = Arrays.stream(new String[][]{
            //alternate 1/3 16
            {"u(S)", "C(d)"}, {"C(d)", "u(S)"}, {"d(S)", "u(C)"}, {"u(C)", "d(S)"},
            {"d(D)", "u(S)"}, {"u(D)", "S(d)"},
            {"u(C)", "D(d)"}, {"d(C)", "u(D)"},
            {"S(u)", "D(d)"}, {"d(S)", "D(u)"},
            {"S(u)", "d(C)"}, {"d(C)", "S(u)"}, {"C(u)", "S(d)"}, {"S(d)", "C(u)"},
            {"D(u)", "C(d)"}, {"d(D)", "C(u)"}
    }).collect(Collectors.toSet());

    public static Set downstreamsTopology = Arrays.stream(new String[][]{
            //alternate 1/3 16
            {"u(C)", "d(S)"}, {"D(u)", "d(S)"}, {"d(S)", "u(C)"},
            {"u(C)", "D(d)"}, {"S(u)", "D(d)"},
            {"d(C)", "S(u)"}, {"u(D)", "d(C)"}, {"S(u)", "d(C)"},
            {"S(d)", "u(D)"}, {"C(u)", "S(d)"}, {"S(d)", "C(u)"},
            {"d(D)", "u(S)"}, {"d(D)", "C(u)"},
            {"C(d)", "u(S)"}, {"C(d)", "D(u)"}, {"u(S)", "C(d)"}
    }).collect(Collectors.toSet());

    public static Set upstreamDownstreamTopology = Arrays.stream(new String[][]{
            //alternate 1/2 16
            {"u(S)", "d(S)"}, {"d(S)", "u(S)"}, {"C(d)", "u(C)"}, {"u(C)", "C(d)"},
            {"S(d)", "u(S)"}, {"u(D)", "d(D)"},
            {"u(C)", "d(C)"}, {"D(d)", "u(D)"},
            {"S(u)", "d(S)"}, {"D(d)", "D(u)"},
            {"S(u)", "S(d)"}, {"S(d)", "S(u)"}, {"C(u)", "d(C)"}, {"d(C)", "C(u)"},
            {"D(u)", "d(D)"}, {"C(d)", "C(u)"}
    }).collect(Collectors.toSet());


    public static Set downstreamUpstreamTopology = Arrays.stream(new String[][]{
            //alternate 1/2 16
            {"u(S)", "d(S)"}, {"S(u)", "d(S)"}, {"d(S)", "S(U)"},
            {"u(D)", "D(d)"}, {"D(u)", "D(d)"},
            {"d(C)", "u(C)"}, {"C(u)", "d(C)"}, {"u(C)", "d(C)"},
            {"u(S)", "S(d)"}, {"S(d)", "S(u)"}, {"S(d)", "u(S)"},
            {"d(D)", "u(D)"}, {"d(D)", "D(u)"},
            {"C(d)", "u(C)"}, {"C(d)", "C(u)"}, {"C(u)", "C(d)"}
    }).collect(Collectors.toSet());

    public static void setFeatureStreamTopology(FeatureStreamBean featureStream) {
        featureStream.setTopology(getStreamTopology(featureStream));
    }

    public static String getStreamApplicationTypeTopology(SignalPropertiesFactory signal) {
        return streamApplicationTypeTopology.get(signal.getStreamApplicationType().name());
    }

    public static StreamTopology getStreamTopology(FeatureStreamPropertiesFactory featureStream) {

        if (upstreamTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.Upstream;
        }
        if (downstreamTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.Downstream;
        }
        if (upstreamFunctionTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.UpstreamFunction;
        }
        if (downstreamFunctionTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.DownstreamFunction;
        }
        if (upstreamSystemTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.UpstreamSystem;
        }
        if (downstreamSystemTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.DownstreamSystem;
        }
        if (upstreamsTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.Upstreams;
        }
        if (downstreamsTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.Downstreams;
        }
        if (upstreamDownstreamTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.UpstreamDownstream;
        }
        if (downstreamUpstreamTopology.stream()
                .filter(e -> containsTopology(featureStream, (String[]) e))
                .count() > 0) {
            return StreamTopology.DownstreamUpstream;
        }
        return StreamTopology.Trail;
    }

    private static boolean containsTopology(FeatureStreamPropertiesFactory featureStream, String[] e) {
        return e[0].equalsIgnoreCase(getStreamApplicationTypeTopology(featureStream.getUpstream()))
                && e[1].equalsIgnoreCase(getStreamApplicationTypeTopology(featureStream.getDownstream()));
    }


    public static int getNGaussSum(int n, int gaussSumN) {
        return  n * gaussSumN * (gaussSumN + 1) / 2 ;
    }

    public static List<StreamApplicationGraphPropertiesFactory> getGraphVectorStreams(StreamApplicationGraphPropertiesFactory sgraph, List<StreamApplicationGraphPropertiesFactory> graph) {
        return graph.stream()
                .filter(sg -> {

                    return true;
                }).collect(Collectors.toList());
    }
}
