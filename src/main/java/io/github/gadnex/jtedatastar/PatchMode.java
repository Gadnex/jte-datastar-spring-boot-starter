package io.github.gadnex.jtedatastar;

/**
 * The patch mode used by Datastar when updating HTML in the browser upon receiving a PatchElements
 * SSE event.
 */
public enum PatchMode {
  /** Patches the element’s outerHTML using Idiomorph. This is the default mode. */
  OUTER(" mode outer"),
  /** Patches the element’s innerHTML using Idiomorph. */
  INNER(" mode inner"),
  /** Replaces the element’s outerHTML. */
  REPLACE(" mode replace"),
  /** Prepends the element to the target’s children. */
  PREPEND(" mode prepend"),
  /** Appends the element to the target’s children. */
  APPEND(" mode append"),
  /** Inserts the element before the target as a sibling. */
  BEFORE(" mode before"),
  /** Inserts the element after the target as a sibling. */
  AFTER(" mode after"),
  /** Remove the target element. */
  REMOVE(" mode remove");

  private final String output;

  PatchMode(String output) {
    this.output = output;
  }

  String output() {
    return output;
  }
}
