package org.hrw.stud.pomawies.aoc21;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void binaryConversionTest() {
		assertThat(Util.binaryToLong("")).isZero();
		assertThat(Util.binaryToLong("0")).isZero();
		assertThat(Util.binaryToLong("1")).isEqualTo(1L);
		assertThat(Util.binaryToLong("10110")).isEqualTo(22L);
		assertThat(Util.binaryToLong("01001")).isEqualTo(9L);
	}


	private record Person(String company, int deptId, String lastName, int grandPerYear, String jobTitle) { }
	@Test
	@DisplayName("Perform equivalence class partitioning")
	void equivalencePartitioning() {
		Person david = new Person("A", 12, "Fischer", 60, "dev");
		Person michaela = new Person("A", 12, "Schmidt", 72, "dev");
		Person robert = new Person("A", 42, "Meier", 60, "dev");
		Person christine = new Person("B", 12, "Fischer", 60, "dev");
		Set<Person> allPeople = Set.of(david, michaela, robert, christine);
		assertThat(Util.equivalencePartitioning(
			allPeople,
			Set.of(
				Person::company,
				Person::deptId
			)
		)).as("Groups according to classifiers")
			.containsExactlyInAnyOrder(
				Set.of(david, michaela),
				Set.of(robert),
				Set.of(christine)
			);

		assertThat(Util.equivalencePartitioning(
			Collections.<Person>emptySet(),
			Set.of(Person::company)
		)).as("No input, no output")
			.isEmpty();

		assertThat(Util.equivalencePartitioning(
			allPeople,
			Collections.emptySet()
		)).as("No classifiers, no change")
			.containsExactly(Set.of(david, michaela, robert, christine));

		assertThat(Util.equivalencePartitioning(
			allPeople,
			Collections.singleton(Person::jobTitle)
		)).as("Same class, no change")
			.containsExactly(Set.of(david, michaela, robert, christine));
	}
}