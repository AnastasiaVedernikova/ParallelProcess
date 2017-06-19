public enum TaskStatus {
    NEW_ONE(0),
    DONE(1),
    IN_PROCESS(2),
    FAILED(3);

    private final int value;

    TaskStatus(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}