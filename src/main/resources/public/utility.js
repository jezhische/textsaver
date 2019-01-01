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

// function setPageNumberButtonAction(pageNm, link) {
//     $('#' + pageNm).attr('formaction', link);
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

function createNameBar(docName, elem) {
    elem.html(docName);
}
// ----------------------------------------------------------------------------------------------------------------

function createInitialButtonsRow(elem) {
    // let row = $('#' + elemId);
    elem.html('<form class="page-btn-bar">' +
        '<button id="delete-page" style="width: 20%" disabled>delete page</button>' +
        '<button id="minus" style="width: 10%" disabled>-</button>' +
        '<button id="1" type="submit" formaction="" class="active-page-number-button" disabled>1</button>' +
        '<button id="plus" style="width: 10%">+</button>' +
        '<button id="insert-page" style="width: 20%">insert page</button>' +
        '</form>');
    console.log('******************* ' + $('#1').html());
}
// ----------------------------------------------------------------------------------------------------------------

function createTextarea() {
    let text = $('#container').find('#text');
    text.attr({'wrap':'soft', 'cols':'200', 'placeholder':'please input text here',
    'oninput':'this.style.height = ""; this.style.height = this.scrollHeight + "px"',
    'onmousemove':'this.style.height = this.scrollHeight + "px"'});
    text.css('visibility', 'visible');
                                        console.log('createTextarea: success');
}
// ----------------------------------------------------------------------------------------------------------------

function createDocLink(docName) {
    /* add link */
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
        '        <div id="lower-page-buttons-row"></div>\n' +
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
