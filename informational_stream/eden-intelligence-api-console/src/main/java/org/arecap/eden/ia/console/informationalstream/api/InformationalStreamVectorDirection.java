package org.arecap.eden.ia.console.informationalstream.api;

/**
 * Indicates the orientation of the informational stream around the three graph nodes
 * (Selector, Detector and Consumer). The detector remains part of the path even if it is
 * not explicitly referenced by the drawing code.
 */
public enum InformationalStreamVectorDirection {

  SelectorDetectorConsumer,
  ConsumerDetectorSelector

}
