// ===================================================================================== AUXILIARY FUNCTIONS

/** when first call of given function with such textCommonDataId, create page form element
 * into the tag <div id="text"></div> on the index.html */
function createPageTextFormElement(textCommonDataId, textareaId, upperRefButtons, lowerRefButtons) {
    // condition check to avoid duplication
    if($("#" + textCommonDataId).html() === undefined) { // TODO: don't forget to let down the flag when close document. The flag will be raised in the end of this method
                                                    console.log('*******$("#" + textCommonDataId).html() = ' + $("#" +
                                                        textCommonDataId).html() + ', creating form with id = ' + textCommonDataId);
        let closeButton = '<br/><br/><input id="close-btn" type="submit" value="close" onclick="closeDoc()">';
        let upperRefButtonsArea = '<br/><p id="'+ upperRefButtons + '"></p>';
        let lowerRefButtonsArea = '<br/><p id="'+ lowerRefButtons + '"></p>';
        // soft wrap means word wrap (перенос по словам)
        let pageTextarea = '<textarea id="' + textareaId + '" wrap="soft" cols="100"' +
        /** HTML oninput event attribute here allows to set the height of this textarea dynamically
         * in accordance with the number of entered lines, when an element gets user input.
         * Scroll bar won't be appeared */
        // "this" references current element, i.e. this textarea, "px" means "pixels"
        ' oninput=\'this.style.height = ""; this.style.height = this.scrollHeight + "px"\'></textarea>';

        // <div id="text"></div> is located on the index.html. The method must be .html(), not .append(),
        // to replace existing document page form element by the new one when page number is changed
        $('#text').html('<form id="' + textCommonDataId + '" style="margin-left: 60px">' +
            closeButton +
            upperRefButtonsArea +
            pageTextarea +
            lowerRefButtonsArea +
            '</form>');
    }
}
// ----------------------------------------------------------------------------------------------------------------
/** close current document by 'close' button */
function closeDoc() {
    // TODO: here must be function to save all the changes
    $('#text').html('');
                                                            console.log('CLOSED');
    isDocOpen = false; // ??? fixme
}

// ----------------------------------------------------------------------------------------------------------------


// ----------------------------------------------------------------------------------------------------------------
// /** create buttons row with certain pages (including bookmarks) links  */
// function getPagesReferenceButtons(docFormId, data) {
//     // $('#' + docFormId).append('<div id=page-buttons></div>')
//     // $('#page-buttons').append('<input type="submit" value="' + data.page_number + '" class="button-bar">');
//     let links = data._links;
//     let nextLink = links.next.href;
//     console.log('nextLink ======== ' + nextLink);
//     $('#' + docFormId).append('<div id=page-buttons>' +
//         '<button type="button" id="'+ data.page_number + '" >' + data.page_number + '</button>' +
//         // '<button type="button" id="'+ data.page_number + '" onclick="extractPage(' + nextLink + ')">' + data.page_number + '</button>' +
//         '</div>');
//     $('#' + data.page_number).click(function () {
//         extractPage(nextLink);
//     });
// }

// ----------------------------------------------------------------------------------------------------------------

/** create and append textarea child element to existing form element: */
function createTextareaElement(formId, textareaId) {
    let textarea = $('#' + textareaId);
    // condition check to avoid duplication
    if (textarea.html() === undefined) {
        // append to parent element
        $('#' + formId).append(  // FIXME html()? append()?
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
    // TODO: fix it - define right val() instead of test one
    textarea.val('page: ' + data.pageNumber + ', ' +
        ', body: '+ data.body + '/ ');
    /** to auto grow the created textarea height according loaded content */
    textarea.height(textarea[0].scrollHeight);
    textarea[0].maxHeight = 50; // fixme: don't know is it working right
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