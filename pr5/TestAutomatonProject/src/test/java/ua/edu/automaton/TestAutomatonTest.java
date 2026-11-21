package ua.edu.automaton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Параметризовані Unit-тести (JUnit 5) для перевірки всіх функціональних кейсів
 * скінченного автомата розпізнавання слова "TEST", включаючи крайові випадки з накладанням (Завдання 3*).
 */
class TestAutomatonTest {

    private TestAutomaton automaton;

    @BeforeEach
    void setUp() {
        // Використовуємо виправлений автомат (Завдання 1 + 3*)
        automaton = new TestAutomaton(true);
    }

    @DisplayName("1. Порожній рядок та null повинні залишати автомат у початковому стані S")
    @ParameterizedTest(name = "Вхід: {0} -> Очікуваний стан: S")
    @NullAndEmptySource
    void testNullAndEmptyStringsReturnStateS(String input) {
        assertEquals("S", automaton.process(input));
        assertEquals(State.S, automaton.getCurrentState());
    }

    @DisplayName("2. Рядки без слова 'TEST', які закінчуються у станах S, 1, 2 або 3")
    @ParameterizedTest(name = "[{index}] \"{0}\" -> стан {1}")
    @CsvSource({
            // Закінчення у стані S (немає активного префікса наприкінці)
            "'abcdef',           S",
            "'12345!@#',         S",
            "'test',             S",
            "'TesX',             S",
            "'T_E_S_X',          S",
            "'TEa',              S",
            "'TESa',             S",
            "'abcTa',            S",
            "'abcTEa',           S",
            "'abcTESa',          S",

            // Закінчення у стані 1 (рядок закінчується на 'T')
            "'T',                1",
            "'abcT',             1",
            "'TesT',             1",
            "'T_E_S_T',          1",
            "'TT',               1",
            "'TET',              1",
            "'TESaT',            1",

            // Закінчення у стані 2 (рядок закінчується на 'TE')
            "'TE',               2",
            "'abcTE',            2",
            "'TTE',              2",
            "'TETE',             2",
            "'TESaTE',           2",

            // Закінчення у стані 3 (рядок закінчується на 'TES')
            "'TES',              3",
            "'abcTES',           3",
            "'TTES',             3",
            "'TETES',            3",
            "'TESaTES',          3"
    })
    void testPartialAndNonMatchingStrings(String input, String expectedState) {
        assertEquals(expectedState, automaton.process(input));
    }

    @DisplayName("3. Базові рядки зі словом 'TEST' повинні переводити автомат у кінцевий стан F")
    @ParameterizedTest(name = "[{index}] \"{0}\" -> стан F")
    @CsvSource({
            "'TEST',             F",
            "'TESTabc',          F",
            "'abcTEST',          F",
            "'abcTESTabc',       F",
            "'TESTTEST',         F",
            "'aTaTEaTESaTESTa',  F"
    })
    void testStandardMatchingStringsReturnStateF(String input, String expectedState) {
        assertEquals(expectedState, automaton.process(input));
        assertEquals(State.F, automaton.evaluateState(input));
    }

    @DisplayName("4. (Завдання 3*) Рядки з накладанням префіксів на 'TEST': базовий автомат помиляється, виправлений повертає F")
    @ParameterizedTest(name = "[{index}] \"{0}\": базовий -> {1} (помилка), виправлений -> {2}")
    @CsvSource({
            // Зі стану 1 надходить 'T' перед "EST" ("TTEST"):
            // Базовий: T(1) -> T(S) -> E(S) -> S(S) -> T(1) => повертає 1 замість F!
            "'TTEST',            1, F",
            "'abcTTEST',         1, F",
            "'TTESTabc',         S, F",
            "'abcTTESTxyz',      S, F",
            "'TTTTEST',          1, F",
            // Зі стану 2 надходить 'T' перед "EST" ("TETEST"):
            // Базовий: T(1) -> E(2) -> T(S) -> E(S) -> S(S) -> T(1) => повертає 1 замість F!
            "'TETEST',           1, F",
            "'abcTETEST',        1, F",
            "'TETESTabc',        S, F",
            "'abcTETESTxyz',     S, F",
            // Комбіновані випадки накладання префіксів "T" і "TE":
            "'TTETTEST',         1, F",
            "'TETTTEST',         1, F",
            "'TETETTEST',        1, F",
            "'TTETTETTEST',      1, F"
    })
    void testOverlappingCasesTask3Star(String input, String expectedNaiveState, String expectedFixedState) {
        TestAutomaton naiveAutomaton = new TestAutomaton(false);
        TestAutomaton fixedAutomaton = new TestAutomaton(true);

        assertEquals(expectedNaiveState, naiveAutomaton.process(input),
                "Базовий автомат за початковою схемою має демонструвати втрату стану на рядку: " + input);
        assertEquals(expectedFixedState, fixedAutomaton.process(input),
                "Виправлений автомат повинен розпізнавати 'TEST' у рядку: " + input);
    }

    @DisplayName("5. Покрокова перевірка таблиці переходів виправленого автомата для всіх станів")
    @ParameterizedTest(name = "Стан {0} + символ ''{1}'' -> Стан {2}")
    @MethodSource("provideStateTransitions")
    void testAllSingleCharacterTransitions(State initialState, char symbol, State expectedNextState) {
        // Встановлюємо початковий стан шляхом подачі відповідного префікса
        switch (initialState) {
            case S -> automaton.evaluateState("");
            case ONE -> automaton.evaluateState("T");
            case TWO -> automaton.evaluateState("TE");
            case THREE -> automaton.evaluateState("TES");
            case F -> automaton.evaluateState("TEST");
        }
        assertEquals(initialState, automaton.getCurrentState());

        // Виконуємо один крок
        State actualNext = automaton.step(symbol);
        assertEquals(expectedNextState, actualNext);
    }

    private static Stream<Arguments> provideStateTransitions() {
        return Stream.of(
                // Переходи зі стану S
                Arguments.of(State.S, 'T', State.ONE),
                Arguments.of(State.S, 'E', State.S),
                Arguments.of(State.S, 'S', State.S),
                Arguments.of(State.S, 'A', State.S),

                // Переходи зі стану 1 (після "T")
                Arguments.of(State.ONE, 'E', State.TWO),
                Arguments.of(State.ONE, 'T', State.ONE), // Виправлення (3*): "TT" залишає в стані 1
                Arguments.of(State.ONE, 'S', State.S),
                Arguments.of(State.ONE, 'A', State.S),

                // Переходи зі стану 2 (після "TE")
                Arguments.of(State.TWO, 'S', State.THREE),
                Arguments.of(State.TWO, 'T', State.ONE), // Виправлення (3*): "TET" переводить у стан 1
                Arguments.of(State.TWO, 'E', State.S),
                Arguments.of(State.TWO, 'A', State.S),

                // Переходи зі стану 3 (після "TES")
                Arguments.of(State.THREE, 'T', State.F),
                Arguments.of(State.THREE, 'E', State.S),
                Arguments.of(State.THREE, 'S', State.S),
                Arguments.of(State.THREE, 'A', State.S),

                // Переходи з кінцевого стану F (залишається в F на будь-якому символі)
                Arguments.of(State.F, 'T', State.F),
                Arguments.of(State.F, 'E', State.F),
                Arguments.of(State.F, 'S', State.F),
                Arguments.of(State.F, 'A', State.F)
        );
    }
}
