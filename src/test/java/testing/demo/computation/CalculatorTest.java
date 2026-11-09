package testing.demo.computation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CalculatorTest {

	private static final String USERNAME = "kneupane";

	private static int testCounter = 0;

	private Calculator calculator;

	// ---------- Lifecycle hooks ----------

	@BeforeAll
	static void beforeAll() {
		System.out.println("[Before All] Calculator Test suite starting ... by " + USERNAME);
		System.out.println();
	}

	@BeforeEach
	void beforeEach(TestInfo testInfo) {
		testCounter++;
		calculator = new Calculator();
		System.out.println("[Before Each] Starting Test #" + testCounter + ": " + testInfo.getDisplayName());
	}

	@AfterEach
	void afterEach(TestInfo testInfo) {
		System.out.println("[After Each] Finished Test #" + testCounter + ": " + testInfo.getDisplayName());
		System.out.println();
	}

	@AfterAll
	static void afterAll() {
		System.out.println();
		System.out.println("[After All] completed " + testCounter + " test invocations by " + USERNAME + ".");
	}

	// ---------- 1. add() with @MethodSource ----------

	static Stream<Arguments> addProvider() {
		return Stream.of(
				Arguments.of(100, 2, 102),
				Arguments.of(100, -2, 98),
				Arguments.of(-100, 2, -98),
				Arguments.of(-100, -2, -102));
	}

	@Order(1)
	@DisplayName("Add two numbers")
	@ParameterizedTest(name = "{0} + {1} = {2}")
	@MethodSource("addProvider")
	void add(int input1, int input2, int expected) {
		assertEquals(expected, calculator.add(input1, input2));
	}

	// ---------- 2. multiple() with @CsvFileSource ----------

	@Order(2)
	@DisplayName("Multiple two numbers")
	@ParameterizedTest(name = "{0} * {1} = {2}")
	@CsvFileSource(resources = "/multiple.csv", numLinesToSkip = 1)
	void multiple(int input1, int input2, int expected) {
		assertEquals(expected, calculator.multiple(input1, input2));
	}

	// ---------- 3. substract() with @CsvSource ----------

	@Order(3)
	@DisplayName("Substract two numbers")
	@ParameterizedTest(name = "{0} - {1} = {2}")
	@CsvSource({
		"100, 2, 98",
		"100, -2, 102",
		"-100, 2, -102",
		"-100, -2, -98"
	})
	void substract(int input1, int input2, int expected) {
		assertEquals(expected, calculator.substract(input1, input2));
	}

	// ---------- 4. divide() - ONE negative test ----------

	@Order(4)
	@Test
	void divide_byZero() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> calculator.divide(100, 0));
		assertEquals("Denominator value cannot be zero.", exception.getMessage());
	}

	// ---------- 5. divide() - positive tests (added to raise line coverage to 100%) ----------

	@Order(5)
	@DisplayName("Divide two numbers")
	@ParameterizedTest(name = "{0} / {1} = {2}")
	@CsvSource({
		"100, 2, 50",
		"100, -2, -50",
		"-100, 2, -50",
		"-100, -2, 50"
	})
	void divide(int input1, int input2, int expected) {
		assertEquals(expected, calculator.divide(input1, input2));
	}
}
