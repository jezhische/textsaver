// ===================================================================================== AUXILIARY FUNCTIONS

/** clear the forms with "create doc" and "search doc" buttons and inputs */
function clearMainDocMenu(elemIds) {
    elemIds.forEach((elemId) => $('#' + elemId).html(''));
                                        console.log('clearMainDocMenu: success');
}
// ----------------------------------------------------------------------------------------------------------------

function addMainDocButtons(elemId) {
    let element = $('#' + elemId);
    element.html('<button id="delete-doc" class="create-btn" style="color: rgba(192,0,0,0.55)">delete document</button>' +
        '<button id="close-doc" class="create-btn">close document</button>' +
        '<button id="search-page" type="submit" class="create-btn" disabled>search page</button>\n' +
        '<input type="text" id="search-page-input" class="create-input" style="width: 100px" placeholder="Page number" disabled>' +
        '<button id="save-doc" class="create-btn" disabled>save and update</button>');
                                        console.log('addMainDocButtons: success');
}
// ----------------------------------------------------------------------------------------------------------------

function createNameBar(docName, elemId) {
    let bar = $('#' + elemId);
    bar.addClass("doc-name-bar");
    bar.html(docName);
                                        console.log('createNameBar: success');
}
// ----------------------------------------------------------------------------------------------------------------

function createInitialButtonsRow(elemId) {
    let row = $('#' + elemId);
    row.html('<div class="page-btn-bar" style="child-align: middle">' +
        '<button id="delete-page" style="width: 20%" disabled>delete page</button>' +
        '<button id="-" style="width: 10%" disabled>-</button>' +
        '<button id="1" style="width: 10%" disabled>1</button>' +
        '<button id="+" style="width: 10%" disabled>+</button>' +
        '<button id="insert-page" style="width: 20%">insert page</button>' +
        '</div>');
                                        console.log('createInitialButtonsRow: ' + $('#upper-page-buttons-row')
                                            .find('.page-btn-bar').find('#delete_page').html());
}
// ----------------------------------------------------------------------------------------------------------------

function createTextarea() {
// TODO: create it previously, and then in ajax only fill with content
        let text = $('#text');
    console.log('createTextarea: text.html = ' + text.html());
    if (text.html() === '') {
            text.html('<textarea id="pageTextarea" wrap="soft" cols="200"' +
                /** HTML oninput event attribute here allows to set the height of this textarea dynamically
                 * in accordance with the number of entered lines, when an element gets user input.
                 * Scroll bar won't be appeared */
                // "this" references current element, i.e. this textarea, "px" means "pixels"
                ' oninput=\'this.style.height = ""; this.style.height = this.scrollHeight + "px"\'></textarea>');
        }
                                        console.log('createTextarea: success');
}
// ----------------------------------------------------------------------------------------------------------------

function createDocLink(docName) {
    // let docLinks = $('#docLinks');
    $('#docLinks').prepend('<a href="">' + docName + '</a>');
                                        console.log('createDocLink: success');
}
// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------

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
        let pageTextarea = '<textarea id="' + textareaId + '" wrap="soft" cols="200"' +
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
            '        <textarea wrap="soft" cols="200" rows="20" id="' + textareaId + '"' +
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
    let auxTimerId;
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

        /* create recursive setTimeout with checking textarea content changes every 5 second
        to create lower name bar and buttons row */
        auxTimerId = setTimeout(function check() {
            let taHeight = textarea.css('height');
            if (taHeight.substring(0, taHeight.length - 2) > 170) {
                                                            console.log('HURUKU! ' + taHeight);
                let docName = $('#lower-doc-name-bar').html();
                if (docName === '') {
                    docName = $('#upper-doc-name-bar').html();
                    createInitialButtonsRow('lower-page-buttons-row');
                    createNameBar(docName, 'lower-doc-name-bar');
                }
                clearTimeout(auxTimerId);
            }
            else auxTimerId = setTimeout(check, 5000);
        }, 5000);
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