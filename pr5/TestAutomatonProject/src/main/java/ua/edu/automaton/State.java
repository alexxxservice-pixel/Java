package ua.edu.automaton;

/**
 * Перелік можливих станів скінченного автомата для розпізнавання слова "TEST".
 */
public enum State {
    /** Початковий стан (Start) — жодної літери слова "TEST" ще не співпало */
    S("S"),
    /** Стан 1 — розпізнано префікс "T" */
    ONE("1"),
    /** Стан 2 — розпізнано префікс "TE" */
    TWO("2"),
    /** Стан 3 — розпізнано префікс "TES" */
    THREE("3"),
    /** Кінцевий стан (Final) — слово "TEST" знайдено у вхідному рядку */
    F("F");

    private final String label;

    State(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
