    public class TableConfig {
        public static final int MAX_PLAYERS = 6;
        public static final int MIN_PLAYERS = 2;

        public static final int STARTING_MONEY = 5000;

        public static final int SMALL_BLIND = 25;
        public static final int BIG_BLIND = 50;

        public static final int ANTE = 0;

        public static final int MIN_BET = BIG_BLIND;
        public static final int MIN_RAISE = BIG_BLIND;

        private TableConfig(){
            throw new AssertionError("Cannot instantiate TableConfig");
        }
    }