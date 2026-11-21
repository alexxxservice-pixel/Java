package ua.edu.automaton;

/**
 * Реалізація скінченного автомата для розпізнавання наявності в рядку слова "TEST".
 *
 * <p>Підтримує два режими роботи:
 * <ul>
 *   <li><b>Базовий режим (за початковою діаграмою, Завдання 1):</b> при невідповідності чергового
 *       символу в станах 1, 2, 3 автомат завжди повертається в стан {@link State#S}. Через це
 *       на рядках із накладанням префікса на символ 'T' або 'E' (наприклад, {@code "TTEST"}, {@code "TETEST"},
 *       {@code "TESTEST"}) слово {@code "TEST"} пропускається.</li>
 *   <li><b>Виправлений режим (Завдання 3*):</b> враховує префікс-функцію (як в алгоритмі КМП):
 *       символ {@code 'T'} у станах 1 та 2 є початком нового входження слова {@code "TEST"}
 *       (перехід у стан {@link State#ONE}), а в стані 3 (після {@code "TES"}) символ {@code 'E'}
 *       утворює суфікс {@code "TE"} (перехід у стан {@link State#TWO}).</li>
 * </ul>
 */
public class TestAutomaton {

    private State currentState;
    private final boolean fixedMode;

    /**
     * Створює виправлений скінченний автомат (за замовчуванням fixedMode = true).
     */
    public TestAutomaton() {
        this(true);
    }

    /**
     * Створює скінченний автомат у вибраному режимі.
     *
     * @param fixedMode {@code true} — виправлений автомат (Завдання 3*),
     *                  {@code false} — базовий автомат строго за початковою схемою (Завдання 1).
     */
    public TestAutomaton(boolean fixedMode) {
        this.fixedMode = fixedMode;
        this.currentState = State.S;
    }

    /**
     * Скидає автомат у початковий стан {@link State#S}.
     */
    public void reset() {
        this.currentState = State.S;
    }

    /**
     * Повертає поточний стан автомата.
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Обробляє один символ і змінює стан автомата відповідно до функції переходів.
     *
     * @param ch вхідний символ
     * @return новий стан автомата після обробки символу
     */
    public State step(char ch) {
        if (fixedMode) {
            currentState = nextStateFixed(currentState, ch);
        } else {
            currentState = nextStateNaive(currentState, ch);
        }
        return currentState;
    }

    /**
     * Обробляє весь вхідний рядок по одному символу і повертає кінцевий стан як об'єкт {@link State}.
     *
     * @param input вхідний рядок (якщо {@code null}, повертає {@link State#S})
     * @return кінцевий стан автомата після завершення рядка
     */
    public State evaluateState(String input) {
        reset();
        if (input == null) {
            return currentState;
        }
        for (int i = 0; i < input.length(); i++) {
            step(input.charAt(i));
        }
        return currentState;
    }

    /**
     * Обробляє весь вхідний рядок по одному символу і повертає строкове позначення кінцевого стану
     * ("S", "1", "2", "3" або "F").
     *
     * @param input вхідний рядок
     * @return назва кінцевого стану ("S", "1", "2", "3", "F")
     */
    public String process(String input) {
        return evaluateState(input).getLabel();
    }

    /**
     * Функція переходів за початковою діаграмою (Завдання 1).
     * <pre>
     * S: 'T' -> 1, !T -> S
     * 1: 'E' -> 2, !E -> S
     * 2: 'S' -> 3, !S -> S
     * 3: 'T' -> F, !T -> S
     * F:  *  -> F
     * </pre>
     */
    private static State nextStateNaive(State state, char ch) {
        switch (state) {
            case S:
                return (ch == 'T') ? State.ONE : State.S;
            case ONE:
                return (ch == 'E') ? State.TWO : State.S;
            case TWO:
                return (ch == 'S') ? State.THREE : State.S;
            case THREE:
                return (ch == 'T') ? State.F : State.S;
            case F:
                return State.F;
            default:
                throw new IllegalStateException("Невідомий стан: " + state);
        }
    }

    /**
     * Виправлена функція переходів (Завдання 3*).
     * <p>Якщо у стані 1 (після "T") або стані 2 (після "TE") надходить символ 'T',
     * цей символ сам є початком нового потенційного слова "TEST", тому автомат
     * повинен переходити у стан 1, а не скидатися в S.
     * <pre>
     * S: 'T' -> 1, !T -> S
     * 1: 'E' -> 2, 'T' -> 1, !(E|T) -> S
     * 2: 'S' -> 3, 'T' -> 1, !(S|T) -> S
     * 3: 'T' -> F, !T -> S
     * F:  *  -> F
     * </pre>
     */
    private static State nextStateFixed(State state, char ch) {
        switch (state) {
            case S:
                return (ch == 'T') ? State.ONE : State.S;
            case ONE:
                if (ch == 'E') {
                    return State.TWO;
                } else if (ch == 'T') {
                    return State.ONE;
                } else {
                    return State.S;
                }
            case TWO:
                if (ch == 'S') {
                    return State.THREE;
                } else if (ch == 'T') {
                    return State.ONE;
                } else {
                    return State.S;
                }
            case THREE:
                return (ch == 'T') ? State.F : State.S;
            case F:
                return State.F;
            default:
                throw new IllegalStateException("Невідомий стан: " + state);
        }
    }
}
