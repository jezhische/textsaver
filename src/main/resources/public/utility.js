// ===================================================================================== AUXILIARY FUNCTIONS

// ----------------------------------------------------------------------------------------------------------------

// function setIframeVisible() {
//     $('iframe').css('visibility', 'visible');
// }



function getDocLinksSortedByNameAndCreatedDate(textCommonDataResourceArray) {
    textCommonDataResourceArray.forEach(res => {
        let link = res.links[0].href;
        $('#docLinks').append('<a href="' + link + '" class="d_link"><b>' + res.name + '</b></a><br>');
    });
}

// ----------------------------------------------------------------------------------------------------------------

// function setPageNumberButtonAction(pageNm, pageLink) {
//     $('#' + pageNm).attr('formaction', pageLink);
// }

// ----------------------------------------------------------------------------------------------------------------

/** clear the forms with "create doc" and "search doc" buttons and inputs */
function clearMainDocMenu(elemIds) {
    elemIds.forEach((elemId) => $('#' + elemId).html(''));
                                        // console.log('clearMainDocMenu: success');
}
// ----------------------------------------------------------------------------------------------------------------

function addMainDocButtons(elemId) {
    let element = $('#' + elemId);
    element.html('<button id="delete-doc" class="create-btn" style="margin-left: 20px; margin-right: 20px; ' +
        'color: rgba(183,0,0,0.55)">delete document</button>' +
        '<button id="close-doc" class="create-btn" style="margin-left: 20px; margin-right: 20px">close document</button>' +
        '<button id="search-page" type="submit" class="create-btn" style="margin-left: 20px; margin-right: 5px" disabled>' +
        'search page</button>\n' +
        '<input type="text" id="search-page-input" class="create-input" style="margin-right: 20px; width: 120px" ' +
        'placeholder="page number" disabled>' +
        '<button id="save-doc" class="create-btn" style="margin-left: 20px; margin-right: 20px" disabled>' +
        'save and update</button>');
}
// ----------------------------------------------------------------------------------------------------------------

function renderNameOnTheBar(docName, elem) {
    elem.html(docName);
}
// ----------------------------------------------------------------------------------------------------------------

function createInitialButtonsRow(elem) {
    // elem = container.find('#upper-page-buttons-row');
    elem.html('<div class="page-btn-bar">' +
        '<button id="delete-page" style="width: 15%">delete current page</button>' +
        '<button id="minus" style="width: 25%">back</button>' +
        '<button id="plus" style="width: 25%">forward</button>' +
        '<button id="insert-page" style="width: 15%">insert new page</button>' +
        '<button id="is-special-bookmark" style="width: 20%">mark as special bookmark</button>' +
        '<div class="bookmarks-bar">' +
        '<button id="0" type="submit" formaction="" class="page-number-button" disabled>1</button>' +
        '</div>' +
        '</div>');
}
// ----------------------------------------------------------------------------------------------------------------

function createTextarea() {
    let text = $('#container').find('#text');
    text.attr({'wrap':'soft', 'cols':'200', 'placeholder':'please input text here',
    'oninput':'this.style.height = ""; this.style.height = this.scrollHeight + "px"',
    'onmousemove':'this.style.height = ""; this.style.height = this.scrollHeight + "px"'});
    text.css('visibility', 'visible');
                                        console.log('createTextarea: success');
}
// ----------------------------------------------------------------------------------------------------------------

function createDocLink(docName) {
    /* add pageLink */
    $('#docLinks').prepend('<a href="" class="d_link"><b style="color: #ca8a00">' + docName + '</b></a><br>');
                                        // console.log('createDocLink: success');
}
// ----------------------------------------------------------------------------------------------------------------

function setContainer() {
    let container = $('#container');
    container.html('');
    container.html(
        '<div id="upper-doc-bar">\n' +
        '        <div id="upper-doc-name-bar" class="doc-name-bar"></div>\n' +
        '        <div id="upper-page-buttons-row" style="child-align: middle"></div>\n' +
        '    </div>\n' +
        '    <textarea id="text"></textarea>\n' +
        '    <div id="lower-doc-bar">\n' +
        '        <div id="lower-doc-name-bar" class="doc-name-bar"></div>\n' +
        '    </div>'
    );

}

// ----------------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------------

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
