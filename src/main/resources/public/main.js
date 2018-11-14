/** this array allows to note if any text (i.e. textCommonData) is already called */
let dataAccessCounter = [];
/** this array allows to note if any text (i.e. textCommonData) is already called */
let textpartsQualifier = [];

$(function () {
    $('#helper1').click(function (e) {
        // .html( function ), where Function( Integer index, htmlString oldhtml ) => htmlString... Within the
        // function, "this" refers to the current element in the set. (in this case, that is object HTMLButtonElement)
        $('#hel1txt').html('<h1>ui<div style="display:none;">ou</div>hou</h1>');
        $('#hel1txt').html((index, oldhtml) => {return oldhtml + '<p>' + $('#ta26').html() + '</p>'});
        $('#hel1txt').html((index, oldhtml) => {return oldhtml + '<p id="rrr">tot\r\r\rou</p>'});
        $('#hel1txt').html((index, oldhtml) => {return oldhtml + $('p#hel1txt p#rrr').html()});
        $('#ta26').append($('p#hel1txt p#rrr').html());
        let soMany = 10;
        console.log(`This is ${soMany} times easier!   tot\r\r\rou`);
    });

// ================================================================================ MAIN BUSINESS LOGIC

// --------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------- GET and RENDER all the TEXT PART with given textCommonDataId
// --------------------------------------------------------------------------------------------------------------------

    /** get page from db, create necessary page tags on the index.html and fill them with them content */
    function extractTextPartsToHtml(textCommonDataId) {
        /** id for the form that will be created by createTextCommonDataElement() method  */
        let textFormId = 'txt' + textCommonDataId;

        /* when first call of given function with such textCommonDataId, create form element */
        createTextCommonDataElement(textFormId);

        /* get page, create textarea and buttons for it and render its content.
         * To avoid data duplication, check the flag in dataAccessCounter */
        if (!dataAccessCounter.includes(textCommonDataId)) { // todo: don't forget to let down the flag
            $.ajax({
                type:'GET',
                /* get all the textparts */ // FIXME: make to get a textpart bunch with limited size
                url:'text-common-data/'+ textCommonDataId + '/text-parts/pages?page=0&size=1',
                success: function (data, textStatus, jqXHR) {

                    console.log(textStatus);
                    // console.log(jqXHR.getAllResponseHeaders());
                    console.log(data.status);
                    console.log(jqXHR.status);
                    if (jqXHR.status === 200) {
                        /* create the textarea elements and fill them with the textparts content */
                        data.forEach(data => {
                            let thisTextareaId = 'ta' + data.id;

                            /* create current textarea element */
                            let textarea = createTextareaElement(textFormId, thisTextareaId);

                            /* fill it with content and auto grow its height according loaded content */
                            fillTextareaWithContent(textarea, data);

//    TODO: добавить новый объект в массив, содержащий объекты типа: {textPart.id, textarea}
                            /* push created textarea in appropriate container to bind it with its textpart reference */
                            // textpartsQualifier.push({thisTextareaId: data.id});
                            console.log(textarea.attr('id'));

                            /* create handlers that decide when the given textpart has to be updated accordingly with textarea changes */
                            createTextareaContentEventHandlers(textarea);
                        });

// todo: установить на .change, чтобы когда textarea опустела, она становилась hidden (и при закрытии уничтожалась), чтобы
// при выделении или движении стрелки (в т.ч. Ctrl + RIGHT) или выделении стрелкой (SHIFT + RIGHT, Ctrl + SHIFT + RIGHT)
// происходило перескакивание на соседнюю область - а это как сделать?
                        // check
                        console.log('extractTextPartsToHtml(' + textCommonDataId + ') textStatus: ' + textStatus);

                        /** set the flag "dataAccessCounter.includes(textCommonDataId)" to true */
                        dataAccessCounter.push(textCommonDataId);
                    } else if (jqXHR.status === 404) {
                        console.log('404 ************************************');
                    }

                },
                // todo: to write appropriate methods
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('some error');
                }
            });
        }
    }

// -------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------- UPDATE TEXTPART
// -------------------------------------------------------------------------------------------------------------------
    /**  */
    function updateTextpart(content) {

    }




// =========================================================================================== PERFORMING

    $('#helper2').click(function (e) {
        e.preventDefault();
        extractTextPartsToHtml(36);
    });

    $('#txt32').submit(function (e) {
        e.preventDefault();
    })
});
