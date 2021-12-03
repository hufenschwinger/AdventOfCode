package org.hrw.stud.pomawies.aoc21;

import static org.assertj.core.api.Assertions.assertThat;
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

}