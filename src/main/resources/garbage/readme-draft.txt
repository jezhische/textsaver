- общее описание - что делает
(быстрая работа с большими текстами; обеспечивается:
- пэйджингом, скачивается не весь текст, а страница;
- SPA single page app.;
- действия на странице - js, и т.д. см ниже;
- и т.д. см ниже)
- инструменты, библиотеки



- на странице все действия обслуживаются джаваскриптом на стороне клиента, поэтому
все серверные процессы, связанные с обновлением/сохранением страниц - фоновые,
так что у клиента не происходит каких-либо зависаний страницы;
- документ загружается постранично, а не весь сразу, благодаря этому скорость загрузки большая
- поиск и сортировка данных осуществляется в основном с помощью хранимых функций PostgreSQL,
поля, используемые в поиске и сортировке, проиндексированы,
так что эти операции достаточно быстрые


- в базе данных сохраняются entity, в которых содержится в том числе информация, необходимая для сортировки (например,
id следующей страницы в TextPart) и секьюрити (например, юзер в TextCommonData).
При передаче данных на клиенту на основе этой информации в бизнес-лейере строятся DTO, содержащие
структурированные нужным образом данные, только те, что необходимы для построения отображения.
Кроме того, при создании DTO создаются новые данные, например, линки с URI ресурса.
Для этого используются в том числе некоторые возможности библиотеки Spring HATEOAS - например,
статические методы ControllerLinkBuilder:
methodOn(), that takes a controller class into parameters and returns a proxy of the controller.
Этот заместитель позволяет в свою очередь перехватить вызов метода контроллера
и построить URI of the last method invocation, из которого затем создается линк с помощью метода
linkTo() (creates a pointing to a controller method), который принимает this URI
в качестве параметра.
(расписать все это подробно шаг за шагом - по запросам и эндпойнтам)


Что еще нужно (todo):
- save/update doc и перед close - спрашивать, не нужно ли произвести переформатирование страниц -
убрать пустые, разбить слишком большие на несколько. И сделать метод
- поиск по сайту и по документу
- список ссылок на документы: сделать сортировку также по дате создания
- ссылок на документы: подгружать первые 30, по мере надобности - следующие. То же
 и с результатами поиска(это условие сразу для поиска в бд)

 ![alt text](https://github.com/jezhische/textsaver/blob/master/src/main/resources/static/img/login.png)

 Данный проект сделан исключительно с учебными целями, статус: работает, но незавершен.
 Использованные технологии: JDK 8 /Spring Boot 2.0.6.RELEASE /Spring Web MVC /Spring Security /Spring Data JPA /
 Spring HATEOAS /Spring AOP /Spring Boot Test /jQuery /PostgreSQL 9 /PL/pgSQL /Project Lombok /Thymeleaf /
 Orika mapper /Apache Log4j 2 /Springfox Swagger.
 Это приложение представляет собой браузерную записную книжку с возможностью быстрой, без задержек работы
 с большими объемами текста.
 Введенный текст автоматически сохраняется при переходе на другую страницу, вставке страницы,
 окончании работы с документом.
 Динамически обновляемая система закладок отображает 5 ближайших страниц влево и вправо от открытой,
 10 страниц, которые просматривались или редактировались в последнее время, и закладки, отмеченные как специальные.
 Цветовая маркировка должна облегчать навигацию.

 This project is created exclusively for educational purposes. Current status: working, unfinished.
 Technologies used: JDK 8 /Spring Boot 2.0.6.RELEASE /Spring Web MVC /Spring Security /Spring Data JPA /
 Spring HATEOAS /Spring AOP /Spring Boot Test /jQuery /PostgreSQL 9 /PL/pgSQL /Project Lombok /Thymeleaf /
 Orika mapper /Apache Log4j 2 /Springfox Swagger.
 This application is a browser notebook with the ability to quickly, without delay, work with large amounts  of text.
 The entered text is automatically saved when you go to another page, insert a page, finish working with a document.
 A dynamically updated bookmark system displays tabs for the 5 nearest pages left and right of the currently open one,
 10 pages that have been viewed or edited recently, and tabs marked as special. Color marking should facilitate
 navigation.