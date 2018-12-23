/** to check if this page is already opened */
let currentPageLink;
/**  */
let docFormId;

/** this array allows to note if any text (i.e. textCommonData) is already called */
let textpartsQualifier = [];

/** id of current page textarea */
const TEXTAREA_ID = 'page-tarea';
const UPPER_REF_BUTTONS = 'upper-page-reference-buttons-row'; // TODO: to remove
const LOWER_REF_BUTTONS = 'lower-page-reference-buttons-row'; // TODO: to remove
// const UPPER_DOC_BAR = 'upper-doc-bar';
// const LOWER_DOC_BAR = 'lower-doc-bar';
const TEXT = $('#text');
const DOC_LINKS = $('#docLinks');

$(function () {
// ================================================================================ MAIN BUSINESS LOGIC

// --------------------------------------------------------------------------------------------------------------------
// ----------------------------------- (GET) obtain a list of saved doc links and render them in '#docLinks' block
// --------------------------------------------------------------------------------------------------------------------

    function getSavedDocLinks() {
        $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: 'doc-data',
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                console.log('check: obtainedData[1].links[0].href = ' + obtainedData[1].links[0].href + ', name: ' +
                    obtainedData[1].name);
                console.log('obtainedData[0].firstPageLink = ' + obtainedData[1].firstPageLink + ', name: ' +
                    obtainedData[1].name);
                getDocLinksSortedByNameAndCreatedDate(obtainedData);
            },
            error: function () {
                console.log('error: this.url = ' + this.url);
                // TODO: make error handling
                alert('error');
            }
        });
    }


// --------------------------------------------------------------------------------------------------------------------
// --------------------------------------------- (POST) create page and RENDER it on the created document form
// --------------------------------------------------------------------------------------------------------------------

    function createNewDoc() {
        let input = $('#create-doc-text-input');
        let docName = input.val();
        // $('#create-doc-btn').prop('disabled', true);
        // reset input value
        input.val("");

        clearMainDocMenu(['create-doc-block', 'search-doc']);
        addMainDocButtons('search-doc');
        createNameBar(docName, 'upper-doc-name-bar');
        createInitialButtonsRow('upper-page-buttons-row');
        createTextarea();
        createDocLink(docName);
        createTextareaContentEventHandlers(TEXT);

        // jQuery.ajax( [settings ] )
        $.ajax({
            type: 'POST',
            // NB: this property must be defined
            contentType: "application/json",
            url: 'doc-data',
            data: JSON.stringify(docName),
           dataType: 'json',
            // dataType: 'text',
            success: function (obtainedData, status, jqXHR) {
                                                        console.log('POST - create doc: SUCCESS, returned value: ' + obtainedData);

                // DOC_LINKS.prepend('<a href="' + obtainedData + '">' + docName + '</a>');
                $('#upper-doc-name-bar').append(', created date: ' + obtainedData.createdDate);
                                                            console.log('****** obtainedData._links.self.href = ' + obtainedData._links.self.href);
                addHref(obtainedData._links.self.href);
            },
            error: function () {
                console.log('error: this.url = ' + this.url);
                // TODO: make error handling
                alert('error');
            }
        });
    }

// --------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------- GET and RENDER references for all the documents that persisted in db
// --------------------------------------------------------------------------------------------------------------------


// --------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------- GET and RENDER all the TEXT PART with given textCommonDataId
// --------------------------------------------------------------------------------------------------------------------

    /** create necessary form for page rendering in the index.html.
     * And then fill it with it's content (extractPage(pageLink)) */
    function extractDocToHtml(textCommonDataId) {
        /** id for the document pages form that will be created by createPageTextFormElement() method  */
         docFormId = textCommonDataId;

        /* when first call of given function with such textCommonDataId, create page form element
        into the tag <div id="text"></div> on the index.html */
        createPageTextFormElement(textCommonDataId, TEXTAREA_ID, UPPER_REF_BUTTONS, LOWER_REF_BUTTONS);

        //---------------------- TODO: obtain self- link, not page 1
        let pageLink = 'text-common-data/'+ textCommonDataId + '/text-parts/pages?page=1';
        extractPage(pageLink);
    }

// --------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------- GET page and RENDER it on the created document form
// --------------------------------------------------------------------------------------------------------------------

//     function extractPage(link) {
//                                                 console.log('extraction beginning: ' + link);
//
//        /** get and render page content. To avoid content duplication, check the condition    */
//         if (link !== currentPageLink) {
//             $.ajax({
//                 type:'GET',
//                 contentType: "application/json",
//                 /* get all the textparts */
//                 url: link,
//                 // cache: true, // default
//                 // accepts: {json: 'application/json, application/hal+json'}, // application/json is default
//                 // isModified: true, // todo: check this property and be careful
//                 success: function (data, textStatus, jqXHR) {
//
//                     /* create buttons row with certain pages (including bookmarks) links  */
//                     getPagesReferenceButtons(docFormId, data);
//
//                     /* create current textarea element */
//                     let textarea = createTextareaElement(docFormId, TEXTAREA_ID);
//
//                     /* fill it with content and auto grow its height according loaded content */
//                     fillTextareaWithContent(textarea, data);
//
// //    TODO: добавить новый объект в массив, содержащий объекты типа: {textPart.id, textarea}
//                                                                 console.log("textarea.attr('id'): " + textarea.attr('id'));
//
//                     /* create handlers that decide when the given textpart has to be updated accordingly with textarea changes */
//                     createTextareaContentEventHandlers(textarea);
//                                                                 console.log('extractDocToHtml(' + docFormId + ') textStatus: ' + textStatus);
//
//                         /** for "if" check to avoid page loading duplication */ // TODO: don't forget to assign undefined when document closed
//                         currentPageLink = link;
//                 },
//                 // todo: to write appropriate methods
//                 error: function (jqXHR, textStatus, errorThrown) {
//                     // $('#text').html('eee: ' + jqXHR.responseJSON.errors);
//                     $('#text').append('<div style="margin-left: 60px"><h3><i id="err"></i></h3></h2></div>');
//                     $('#err').html(textStatus + ': ' + jqXHR.status + '. '
//                         + '<br/>' + jqXHR.responseJSON.status + ': ' + jqXHR.responseJSON.message
//                     + '<br/>' + jqXHR.responseJSON.errors);
//
//                     console.log('some error: ' + textStatus + ': ' + jqXHR.status);
//                 }
//             });
//         }
//     }

// -------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------- UPDATE TEXTPART
// -------------------------------------------------------------------------------------------------------------------
    /**  */
    function updateTextpart(content) {

    }

// ============================================================================================= AUXILIARY

// ------------------------------------------------------------------------------------------------------------
    /** create a row of buttons with pages references */
    function createPageButtonRow(obtainedData, elemId) {
        let bookmarkResources = obtainedData.bookmarkResources;
        bookmarkResources.forEach(function (item, i, arr) {
            $('#' + elemId).append(
                '<button type="button" id="p'+ item.pageNumber + '" class="page-button" >' + item.pageNumber + '</button>');
            let pbutton = $('#p' + item.pageNumber);
            pbutton.click(function () {
                extractPage(item.link);
            });
            /** the button with current page reference must be disabled */
            if (item.pageNumber === obtainedData.pageNumber) pbutton.prop('disabled', true);
        });
    }

// ------------------------------------------------------------------------------------------------------------



// ------------------------------------------------------------------------------------------------------------

    /** create buttons row with certain pages (including bookmarks) links  */ // TODO: to remove
    // function getPagesReferenceButtons(docFormId, data) {
    //     let links = data._links;
    //     let nextLink = links.next.href;
    //                                             console.log('nextLink ======== ' + nextLink);
    //     // docFormId is id of form created by createPageTextFormElement(textCommonDataId), see extractDocToHtml(textCommonDataId)
    //     $('#' + docFormId).append('<div id=page-buttons>' +
    //         '<button type="button" id="'+ data.pageNumber + '" >' + data.pageNumber + '</button>' +
    //         // '<button type="button" id="'+ data.page_number + '" onclick="extractPage(' + nextLink + ')">' + data.page_number + '</button>' +
    //         '</div>');
    //     $('#' + data.pageNumber).click(function () {
    //         extractPage(nextLink);
    //     });
    // }

// =========================================================================================== PERFORMING


    getSavedDocLinks();
    $('#create-doc-btn').click(function (event) {
        event.preventDefault();
        createNewDoc();
    });
});
