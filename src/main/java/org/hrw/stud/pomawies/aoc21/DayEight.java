package org.hrw.stud.pomawies.aoc21;

import java.io.IOException;
import java.util.Arrays;

public class DayEight {
/*
 aaaa
b    c
b    c
 dddd
e    f
e    f
 gggg
*/

	public static void main(String[] args) throws IOException {
		var lines = Util.readFile("src/main/resources/dayEightInput");

		var sides = lines.parallelStream()
			.map(line -> line.split(" \\| "))
			.toList();

		var amountOf1478onRightSide = sides.parallelStream()
			.map(parts -> parts[1])
			.map(rightSide -> rightSide.split(" "))
			.flatMap(Arrays::stream)
			.parallel()
			.mapToInt(String::length)
			.filter(num -> num < 5 || num == 7)
			.count();
		System.out.println(amountOf1478onRightSide);
	}
}
