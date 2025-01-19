package io.github.gadnex.jtedatastar;

/**
 * The merge mode used by Datastar when updating HTML in the browser upon receiving a MergeFragments
 * SSE event.
 */
public enum MergeMode {
  /** Merges the fragment using Idiomorph. This is the default merge strategy. */
  MORPH(" mergeMode morph"),
  /** Replaces the target’s innerHTML with the fragment. */
  INNER(" mergeMode inner"),
  /** Replaces the target’s outerHTML with the fragment. */
  OUTER(" mergeMode outer"),
  /** Prepends the fragment to the target’s children. */
  PREPEND(" mergeMode prepend"),
  /** Appends the fragment to the target’s children. */
  APPEND(" mergeMode append"),
  /** Inserts the fragment before the target as a sibling. */
  BEFORE(" mergeMode before"),
  /** Inserts the fragment after the target as a sibling. */
  AFTER(" mergeMode after"),
  /** Merges attributes from the fragment into the target – useful for updating a signals. */
  UPSERT_ATTRIBUTES(" mergeMode upsertAttributes");

  private String output;

  MergeMode(String output) {
    this.output = output;
  }

  String output() {
    return output;
  }
}
