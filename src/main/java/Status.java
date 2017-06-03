/**
 * Created by cs.ucu.edu.ua on 26.05.2017.
 */
public class Status {
    public enum status {
        NEW_ONE(0),
        DONE(1),
        IN_PROCESS(2),
        FAILED(3);

        private final int value;

        status(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }
}
