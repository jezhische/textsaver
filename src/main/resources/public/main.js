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

    /** get all the textparts from db in proper order, create textarea for each one and fill the textarea with its content */
    function extractTextPartsToHtml(textCommonDataId) {
        /* id for the form that will be created by createTextCommonDataElement() method  */
        let textFormId = 'txt' + textCommonDataId;

        /** when first call of given function with such textCommonDataId, create form element */
        createTextCommonDataElement(textFormId);

        /* get data (i.e. sorted array of textpart objects), create textarea for each object and render its content.
         * To avoid data duplication, check the flag in dataAccessCounter */
        if (!dataAccessCounter.includes(textCommonDataId)) {
            $.ajax({
                type:'GET',
                /* get all the textparts */ // FIXME: make to get a textpart bunch with limited size
                url:'text-common-data/'+ textCommonDataId + '/text-parts',
                success: function (data, textStatus, jqXHR) {
                    // check the array filling
                    // data.forEach(data => console.log('id: ' + data.id + ', ' +
                    //     'nextItem: ' + data.nextItem + '/ '));

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

// ===================================================================================== AUXILIARY FUNCTIONS

    /** when first call of given function with such textCommonDataId, create form element */
    function createTextCommonDataElement(textFormId) {
        // condition check to avoid duplication
        if ($('#' + textFormId).html() === undefined) {
            $('#text').append(function (index, html) {
                return $('<form id="' + textFormId + '" style="margin-left: 60px">' +
                    '<input type="submit" value="close" class="button-bar"></form>')
            });
        }
    }
// ----------------------------------------------------------------------------------------------------------------

    /** create and append textarea child element to existing form element: */
    function createTextareaElement(formId, textareaId) {
        let textarea = $('#' + textareaId);
        // condition check to avoid duplication
        if (textarea.html() === undefined) {
            // append to parent element
            $('#' + formId).append(
                //todo: set convenient textarea width by cols number setting
                // soft wrap means word wrap (перенос по словам)
                '        <textarea wrap="soft" cols="100" id="' + textareaId + '"' +
                /** HTML oninput Event Attribute here allows to set the height of this textarea dynamically
                 * in accordance with the number of entered lines, when an element gets user input.
                 * Scroll bar won't be appeared */
                // "this" references current element, i.e. this textarea, "px" means "pixels"
                ' oninput=\'this.style.height = ""; this.style.height = this.scrollHeight + "px"\'></textarea>'
            );
            textarea = $('#' + textareaId);
        }
        return textarea;
    }
// ---------------------------------------------------------------------------------------------------------------

    /** fill textarea element with content and auto grow its height according loaded content */
    function fillTextareaWithContent(textarea, data) {
        // let thisTextArea = $('#' + thisTextareaId);
        textarea.val('id: ' + data.id + ', ' +
            'nextItem: ' + data.nextItem + ', body: '+ data.body + '/ ');
        /** to auto grow the created textarea height according loaded content */
        textarea.height(textarea[0].scrollHeight);
    }
// ===================================--------------------------------------------------------- TEXTAREA EVENT HANDLERS

    /** create a handler for given textarea to watch changes in the content, when the focus is obtained.
     * @param textarea - current textarea element
     * @return void
     * @exception
     * @see dataAccessCounter */

    function createTextareaContentEventHandlers(textarea) {
        /* handle textarea when it gains focus, i.e. either mouse clicks on the textarea or it's selected
         * with Tab key from the keyboard */
        let timerId;
        textarea.focus(function () {
            let savedLength = textarea.val().length;
            /* create recursive setTimeout to check textarea content changes every 1 second */
            timerId = setTimeout(function check() {
// // и вот сюда функцию для проверки содержимого и определения, не пора ли перезаписать в бд эту сущность,
// // а затем promise(?), который ищет id из следующего элемента в
                let newLength = textarea.val().length;
                console.log(newLength);
                if (Math.abs(newLength - savedLength) > 5) {
                    console.log('updating required');
                    savedLength = newLength;
                }
                timerId = setTimeout(check, 1000);
            }, 1000);
        });
        /* handle event of loosing focus  */
// NB: this handler must not to be created into the timer because of creating the new blur handler
// in each iteration of timer
        textarea.blur(function () {
            let length = textarea.val().length;
            console.log('focus lost: ' + length);
            clearTimeout(timerId);
        });
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
