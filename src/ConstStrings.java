import java.util.Set;

public final class ConstStrings {
    public static final String FAREWELL = "Пока!\n";
    public static final String OPTIONS_WARNING =
            """
            !Пожалуйста, введите корректное значение!
            """;
    public static final String GREETING =
            """
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Добро пожаловать в игру ПЛЮСОМИНУСЫ!
            Выберите одну из следующих опций:
            1. Начать игру
            2. Посмотреть лучший результат
            3. Выйти из игры
            4. Изменить скин
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            """;
    public static final String GAME_PAGE_TEXT =
            """
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Выберите одну из следующих опций:
            1. Начать игру для двоих
            2. Начать игру против компьютера (лёгкий)
            3. Начать игру против компьютера (сложный)
            4. В главное меню
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            """;
    public static final String SCORES_PAGE_TEXT = "Для выхода в главное меню введите любой текст...";
    public static final String PLAYER_SCORE = "Лучший счет игрока против ботов: ";
    public static final String WHITE_SCORE = "Лучший счет ПЛЮСОВ в режиме pvp: ";
    public static final String BLACK_SCORE = "Лучший счет МИНУСОВ в режиме pvp: ";
    public static final String NO_SCORE = "Ой, а ведь таких игр не проводилось!";
    public static final String YOUR_MOVE =
            """
            , ваш ход!
            Ввод хода строго из большой английской буквы и цифры в 1 строке без пробелов!
            Если хотите отменить ход, введите cancel.
            Если хотите добавить/убрать подсветку возможных ходов, введите P или NP.
            Если хотите выйти из игры досрочно, введите exit.
            """;
    public static final String WRONG_MOVE = "Ваш ход некорректен!";
    public static final String BOARD_TITLE = "  A B C D E F G H";
    public static final String NO_MOVES_WHITE = "Нет ходов. Белые пропускают ход...";
    public static final String NO_MOVES_BLACK = "Нет ходов. Черные пропускают ход...";
    public static final String FINISHER = "Игра закончена! Победитель: ";
    public static final Set<String> GAME_OPTIONS = Set.of(
            "exit", "cancel", "P", "NP"
    );
    public static final String CHOOSE_COLOR = """
            Выберите свой цвет:
            1. ПЛЮСЫ
            2. МИНУСЫ
            """;
    public static final char [][] SKINS =
            {{'+', '-', 'o'}, {'●', '○', '.'}, {'W', 'B', 'P'}};
    public static final String CHOOSE_SKIN =
            "Выберите скин:\n" +
                    "1. Плюсоминусы " + SKINS[0][0] + SKINS[0][1] + SKINS[0][2] + '\n' +
                    "2. Кружочки " + SKINS[1][0] + SKINS[1][1] + SKINS[1][2] + '\n' +
                    "3. Буквы " + SKINS[2][0] + SKINS[2][1] + SKINS[2][2] + '\n';
    private ConstStrings() {}
}
