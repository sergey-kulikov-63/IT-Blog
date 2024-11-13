# IT-Blog — это веб-приложение для управления статьями, поддерживающее регистрацию пользователей. Приложение использует Spring Boot и интегрировано с базой данных PostgreSQL для хранения пользователей и постов. Оно также имеет систему аутентификации и авторизации.

**Краткое описание:**

Регистрация и аутентификация пользователей: пользователи могут зарегистрироваться и войти в систему. Для аутентификации используется Spring Security. 

Просмотр постов: все пользователи могут просматривать посты. Страница с постами доступна всем зарегистрированным пользователям. Доступ к созданию, редактированию и удалению постов ограничен ролью ADMIN.

**MainController:**
* redirectToPosts (GET /) — перенаправляет на страницу всех постов или на страницу входа в зависмости от статуса пользователя.
* postsPage (GET /posts) — отображает список всех постов.
* viewPost (GET /posts/{postUrl}) — отображает страницу просмотра конкретного поста.
* createPostPage (GET /posts/create-post) — страница для создания нового поста.
* createPost (POST /posts/create-post) — сохраняет новый пост.
* updatePostPage (GET /posts/{postUrl}/update) — страница для редактирования поста.
* updatePost (POST /posts/{postUrl}/update) — обновляет пост.
* deletePost (POST /posts/{postUrl}/delete) — удаляет пост.
* signupPage (GET /signup) — страница регистрации нового пользователя.
* signup (POST /signup) — сохраняет нового пользователя в базе данных.
* login (GET /login) — страница входа в систему.
* accessDenied (GET /access-denied) — страница с ошибкой доступа.

**Репозитории:**
* UserRepo — интерфейс для работы с сущностью пользователя. Используется для поиска пользователей по логину.
* PostRepo — интерфейс для работы с сущностью поста. Позволяет искать посты по уникальному URL.

**Модели:**
* User — модель для пользователя, содержит поля для имени, логина, пароля и роли.
* Post — модель для поста, содержит поля для заголовка, изображения, описания, текста и уникального URL.

**Конфигурация безопасности:**
* WebSecurityConfig — конфигурация безопасности, использующая Spring Security для настройки аутентификации и авторизации. Настроены страницы логина и ошибок доступа. Используется кодирование паролей с помощью BCryptPasswordEncoder.


**Описание маршрутов:**

        /posts
Главная страница с лентой постов. Доступна всем зарегистрированным пользователям.

        /posts/{postUrl}
Страница с полным содержимым поста. Доступна всем зарегистрированным пользователям.

        /posts/create-post
Страница для создания нового поста. Доступна только пользователям с ролью ADMIN.

        /posts/{postUrl}/update
Страница для редактирования поста. Доступна только пользователям с ролью ADMIN.

        /posts/{postUrl}/delete
Удаление поста. Доступно только пользователям с ролью ADMIN.

        /signup
Страница регистрации. Доступна всем.

        /login
Страница входа. Доступна всем.

        /access-denied
Страница с ошибкой доступа, если у пользователя нет прав для выполнения действия.
