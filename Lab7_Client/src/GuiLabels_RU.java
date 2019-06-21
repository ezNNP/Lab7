import java.util.ListResourceBundle;

public class GuiLabels_RU extends ListResourceBundle {

    private Object[][] contents = {
            {"Language", "Язык"},
            {"Russian", "Русский"},
            {"Serbian", "Сербский"},
            {"Italian", "Итальянский"},
            {"Spanish", "Испанский"},
            {"ExecuteCommand", "Выполнить команду"},
            {"Add", "Добавить объект"},
            {"Import", "Импортировать коллекцию из файла"},
            {"AddIfMin", "Добавить объект если он минимален"},
            {"Show", "Просмотреть коллекцию"},
            {"Current", "Текущий пользователь"},
            {"Exit", "Выход"},
            {"USER_NOT_FOUND", "Пользователь не найден"},
            {"USER_IN_SYSTEM", "Пользователь в системе"},
            {"WRONG_PASSWORD", "Неверный пароль"},
            {"AuthorizeTitle", "Авторизация"},
            {"Enter", "Вход"},
            {"Register", "Регистрация"},
            {"Login", "Логин"},
            {"Password", "Пароль"},
            {"Email", "Почта"},
            {"Token", "Токен"},
            {"Error", "Ошибка"},
            {"CheckEmail", "Проверьте свою почту и введите присланный токен"},
            {"CompleteRegistration", "Закончите регистрацию"},
            {"UserExist", "Пользовать уже существует"},
            {"WrongToken", "Неверный токен"},
            {"ExpiredToken", "Время подтверждения регистрации истекло, попробуйте зарегистрироваться еще раз"},
            {"ConfirmRegistration", "Подтвердить регистрацию"},
            {"FillFields", "Пожалуйста, заполните поля"},
            {"CorrectToken", "Регистрация подтверждена"},
            {"IncorrectToken", "Неверный токен, попробуйте снова"},
            {"Complete", "Готово"},
            {"Back", "Назад"},
            {"Name", "Имя"},
            {"Age", "Возраст"},
            {"Size", "Размер"},
            {"IllegalField", "Поля заполнены неверно"},
            {"", ""},
            {"", ""},
            {"", ""},
            {"", ""},
            {"", ""},
            {"", ""},
            {"", ""},
            {"", ""}
    };

    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
