// GET TEXT PART
var outerData = {};
var isComplited = false;
function extractTextPart(documentLineId, textPartId) {
// This is ajax(options), where options is "опции запроса в виде пар ключ-значение".
// "success: функция, вызываемая, если запрос завершится успехом. Она может принимать три параметра:
// function(data, textStatus, jqXHR). Параметр data представляет пришедшие от сервера данные.
// Параметр textStatus передает код статуса. Третий параметр представляет связанный с запросом объект jqXHR"
    // т.е. я передаю опции в запросе на сервер (например, по какому url искать ресурс, метод запроса и т.д.), и
    // в том числе передаю опцию success, значение которой - некоторая функция. Параметры этой функции заполняются
    // аргументами, полученными на сервере (это data, textStatus, jqXHR), и затем функция выполняется
    $.ajax({
        type:'GET',
        url:'text-common-data/26/text-parts/' + textPartId,
        success: function (data, textStatus, jqXHR) {
            console.log('from $.ajax: ************ textPart id = ' + data.id + ', textPart body = ' + data.body);
            $(documentLineId).html(data.body);
// запрос асинхронный, поэтому вот это присвоение (assign) переменной может произойти позже, чем я эту переменную вызову.
// Поэтому использовать эту внешнюю переменную, вообще-то, получится только после того, как запрос вернется. Попробую
// этим и воспользоваться - поставить флаг о том, что запрос выполнен.
            outerData = data;
            isComplited = true;
        },
        error: function (jqXHR, textStatus, errorThrown) {
        }
    });
}

// --------------------------------------------------------------------------------------------------------------------

