// ===================================================================================== AUXILIARY FUNCTIONS

/** when first call of given function with such textCommonDataId, create form element */
function createTextCommonDataElement(textFormId) {
    // condition check to avoid duplication
    if ($('#' + textFormId).html() === undefined) {
        $('#text').append('<form id="' + textFormId + '" style="margin-left: 60px">' +
            '<input type="submit" value="close" class="button-bar"></form>');
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