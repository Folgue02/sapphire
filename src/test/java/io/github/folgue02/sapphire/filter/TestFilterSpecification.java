package io.github.folgue02.sapphire.filter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.folgue02.sapphire.exchange.HttpMethod;

public class TestFilterSpecification {

	@Test
	public void testFilterPrioritiesSorting() {
		var input = new ArrayList<>(List.of(FilterPriority.HIGH, FilterPriority.DEFAULT, FilterPriority.LOW, FilterPriority.EXTREME));
		var expected = List.of(FilterPriority.EXTREME, FilterPriority.HIGH, FilterPriority.DEFAULT, FilterPriority.LOW);

		Collections.sort(input);

		assertEquals(expected, input);
	}

	@Test
	public void testFilterPriorityList() {
		var extremeSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.EXTREME);
		var highSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.HIGH);
		var defaultSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.DEFAULT);
		var lowSpec = new FilterSpecification("/same", Set.of(HttpMethod.GET), FilterPriority.LOW);

		var inputFSpecs = new ArrayList<>(List.of(
			lowSpec,
			extremeSpec,
			defaultSpec,
			highSpec
		));
		Collections.sort(inputFSpecs);

		var expectedFSpecs = List.of(
			extremeSpec, highSpec, defaultSpec, lowSpec
		);
		assertEquals(expectedFSpecs, inputFSpecs);
	}

	@Test
	public void testFilterSpecificationComparing() {
		var high = new FilterSpecification("/same", Set.of(), FilterPriority.HIGH);
		var extreme = new FilterSpecification("/same", Set.of(), FilterPriority.EXTREME);

		assertEquals(-1, extreme.compareTo(high));
	}
}