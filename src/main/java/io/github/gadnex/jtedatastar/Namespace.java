package io.github.gadnex.jtedatastar;

/** The namespace used by Datastar when pathing special DOM elements such as SVG or MathML. */
public enum Namespace {

  /** Namespace for SVG images */
  HTML(" namespace html"),
  /** Namespace for SVG images */
  SVG(" namespace svg"),
  /** Namespace for MathML markup */
  MATHML(" namespace mathml");

  private final String output;

  private Namespace(String output) {
    this.output = output;
  }

  String output() {
    return output;
  }
}
