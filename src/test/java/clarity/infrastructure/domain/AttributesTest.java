package clarity.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AttributesTest {

  @Test
  void ensureEmpty() {
    assertThat(Attributes.empty()).isNotNull().hasFieldOrPropertyWithValue("empty", true).isEmpty();
  }

  @Test
  void ensureMutatesAndCopies() {

    var empty = Attributes.empty();

    var a1 = empty.with("foo", "bar");
    assertThat(a1).isNotNull().isNotEqualTo(empty).contains(entry("foo", "bar"));

    var a2 = a1.without("nope");
    assertThat(a2).isNotNull().isEqualTo(a1).contains(entry("foo", "bar"));

    var a3 = a2.withAll(Map.of("hello", 123, "baz", "goo"));
    assertThat(a3)
        .isNotNull()
        .isNotEqualTo(a2)
        .contains(entry("foo", "bar"), entry("hello", 123), entry("baz", "goo"));

    var a4 = a3.without("hello");
    assertThat(a4)
        .isNotNull()
        .isNotSameAs(a3)
        .isNotEqualTo(a3)
        .doesNotContain(entry("hello", 123))
        .contains(entry("foo", "bar"), entry("baz", "goo"));
  }

  @Test
  void ensureWithNullableRemovesOnNull() {

    var a1 = Attributes.empty().with("foo", "bar");
    assertThat(a1).isNotNull().contains(entry("foo", "bar"));

    var a2 = a1.withNullable("foo", null);
    assertThat(a2)
        .isNotNull()
        .isNotEqualTo(a1)
        .doesNotContain(entry("foo", "bar"))
        .doesNotContainKey("foo");

    var a3 = a2.withNullable("hello", 123);
    assertThat(a3).isNotNull().isNotEqualTo(a2).contains(entry("hello", 123));
  }

  @Test
  void ensureReplacesFully() {
    var a1 = Attributes.empty().with("foo", "bar");

    var a2 = a1.replace(Map.of("hello", 123));
    assertThat(a2).isNotNull().isNotEqualTo(a1).containsExactly(entry("hello", 123));
  }

  @Test
  void ensureIsModifiableMap() {
    var a1 = Attributes.empty();
    assertThat(a1).isInstanceOfAny(Map.class);
    a1.put("foo", "bar");
  }

  @Test
  void ensureProvidesUnmodifiableMap() {
    var a1 = Attributes.empty().with("foo", "bar").toUnmodifiableMap();
    assertThat(a1).isInstanceOfAny(Map.class);
    assertThrows(UnsupportedOperationException.class, () -> a1.put("foo", "bar"));
  }
}
