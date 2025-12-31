from enum import Enum, auto

class InformationalStreamNeuronType(str, Enum):
    """
    Reprezintă cei 12 neuroni logici din spațiul coerent (fost "StreamApplicationType").
    Păstrăm aceeași enumerare, dar cu o semantică orientată pe neuron logic, nu "aplicație".
    """

    UpstreamSelectorFunction = "UpstreamSelectorFunction"
    UpstreamDetectorFunction = "UpstreamDetectorFunction"
    UpstreamConsumerFunction = "UpstreamConsumerFunction"
    UpstreamSelectorSystem = "UpstreamSelectorSystem"
    UpstreamDetectorSystem = "UpstreamDetectorSystem"
    UpstreamConsumerSystem = "UpstreamConsumerSystem"

    DownstreamSelectorFunction = "DownstreamSelectorFunction"
    DownstreamDetectorFunction = "DownstreamDetectorFunction"
    DownstreamConsumerFunction = "DownstreamConsumerFunction"
    DownstreamSelectorSystem = "DownstreamSelectorSystem"
    DownstreamDetectorSystem = "DownstreamDetectorSystem"
    DownstreamConsumerSystem = "DownstreamConsumerSystem"

    def __str__(self):
        return self.value

# Backward-compat alias while restul codului este migrat.
StreamApplicationType = InformationalStreamNeuronType

class InformationalStreamNetting(str, Enum):
    """
    Represents the 4 types of netting (directions of flow relative to vertices/edges).
    Mirrors org.arecap.eden.ia.console.informationalstream.api.InformationalStreamNetting
    """
    UpstreamEdge = "UpstreamEdge"
    DownstreamEdge = "DownstreamEdge"
    UpstreamVertex = "UpstreamVertex"
    DownstreamVertex = "DownstreamVertex"

    def __str__(self):
        return self.value


class InformationalStreamVectorDirection(str, Enum):
    """
    Indicates the orientation of the informational stream around the three graph nodes.
    Mirrors org.arecap.eden.ia.console.informationalstream.api.InformationalStreamVectorDirection
    Defines the geometric orientation of the hexagon connection.
    Previously SDC/CDS. Renamed to reflect position on lattice (CornerParity vs SideParity).
    """
    CornerParity = "CornerParity" # Was SelectorDetectorConsumer (Input/D->S)
    SideParity = "SideParity"     # Was ConsumerDetectorSelector (Output/S->D)

    def __str__(self):
        return self.value

class StreamTopology(str, Enum):
    """
    Classifies the topology connection type.
    Mirrors org.arecap.eden.ia.console.informationalstream.api.StreamTopology
    """
    Upstream = "Upstream"
    Downstream = "Downstream"
    UpstreamFunction = "UpstreamFunction"
    DownstreamFunction = "DownstreamFunction"
    UpstreamSystem = "UpstreamSystem"
    DownstreamSystem = "DownstreamSystem"
    Upstreams = "Upstreams"
    Downstreams = "Downstreams"
    UpstreamDownstream = "UpstreamDownstream"
    DownstreamUpstream = "DownstreamUpstream"
    Trail = "Trail"

    def __str__(self):
        return self.value
