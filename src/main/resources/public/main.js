/** to check if this page is already opened */
let currentPageLink;
/**  */
let docFormId;

/** this array allows to note if any text (i.e. textCommonData) is already called */
let textpartsQualifier = [];

/** id of current page textarea */
const TEXTAREA_ID = 'page-tarea';
const UPPER_REF_BUTTONS = 'upper-page-reference-buttons-row';
const LOWER_REF_BUTTONS = 'lower-page-reference-buttons-row';

$(function () {

// ========================================================== TODO: to remove
    $('#helper1').click(function (e) {
        // // .html( function ), where Function( Integer index, htmlString oldhtml ) => htmlString... Within the
        // // function, "this" refers to the current element in the set. (in this case, that is object HTMLButtonElement)
        // $('#hel1txt').html('<h1>ui<div style="display:none;">ou</div>hou</h1>');
        // $('#hel1txt').html((index, oldhtml) => {return oldhtml + '<p>' + $('#ta26').html() + '</p>'});
        // $('#hel1txt').html((index, oldhtml) => {return oldhtml + '<p id="rrr">tot\r\r\rou</p>'});
        // $('#hel1txt').html((index, oldhtml) => {return oldhtml + $('p#hel1txt p#rrr').html()});
        // $('#ta26').append($('p#hel1txt p#rrr').html());
        // let soMany = 10;
        // console.log(`This is ${soMany} times easier!   tot\r\r\rou`);
    });
// ===========================================================


// ================================================================================ MAIN BUSINESS LOGIC

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
// --------------------------------------------- (POST) create page and RENDER it on the created document form
// --------------------------------------------------------------------------------------------------------------------

    function createNewDoc() {
        let name = $('#create-doc-text-input').val();
        $('#create-doc-btn').prop('disabled', true);
                                                            console.log("$('#create-doc-btn').submit(function () {...");
        // jQuery.ajax( [settings ] )
            $.ajax({
                type: 'POST',
                // NB: this property must be defined
                contentType: "application/json",
                url: 'doc-data',
                data: JSON.stringify(name),
                dataType: 'json',
                success: function (obtainedData, status, jqXHR) {
                                                            console.log('POST - create doc: SUCCESS');
                                                            alert('success: ' + status);

            // TODO: MAYBE NEED TO MAKE WITH TEXTCOMMONDATAID, DON'T KNOW... BE CAREFUL WITH NEXT METHODS
                    docFormId = 'formId'; // FIXME

                    createPageTextFormElement(docFormId, TEXTAREA_ID, UPPER_REF_BUTTONS, LOWER_REF_BUTTONS);
            // todo: засунуть этот метод в utility, лучше даже объединить с createPageTextFormElement
                    createPageButtonRow(obtainedData, UPPER_REF_BUTTONS);

                    let textarea = createTextareaElement(docFormId, TEXTAREA_ID);
                                                            console.log('let textarea = ' + textarea[0].toString());

                    fillTextareaWithContent(textarea, obtainedData);

                    createTextareaContentEventHandlers(textarea);

                    $('#create-doc-btn').prop('disabled', false);
                },
                error: function () {
                    console.log(this.url);
                                                            alert('error');
                    $('#create-doc-btn').prop('disabled', false);
                }
            });
    }
// --------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------- GET page and RENDER it on the created document form
// --------------------------------------------------------------------------------------------------------------------

    function extractPage(link) {
                                                console.log('extraction beginning: ' + link);

       /** get and render page content. To avoid content duplication, check the condition    */
        if (link !== currentPageLink) {
            $.ajax({
                type:'GET',
                contentType: "application/json",
                /* get all the textparts */
                url: link,
                // cache: true, // default
                // accepts: {json: 'application/json, application/hal+json'}, // application/json is default
                // isModified: true, // todo: check this property and be careful
                success: function (data, textStatus, jqXHR) {

                    /* create buttons row with certain pages (including bookmarks) links  */
                    getPagesReferenceButtons(docFormId, data);

                    /* create current textarea element */
                    let textarea = createTextareaElement(docFormId, TEXTAREA_ID);

                    /* fill it with content and auto grow its height according loaded content */
                    fillTextareaWithContent(textarea, data);

//    TODO: добавить новый объект в массив, содержащий объекты типа: {textPart.id, textarea}
                                                                console.log("textarea.attr('id'): " + textarea.attr('id'));

                    /* create handlers that decide when the given textpart has to be updated accordingly with textarea changes */
                    createTextareaContentEventHandlers(textarea);
                                                                console.log('extractDocToHtml(' + docFormId + ') textStatus: ' + textStatus);

                        /** for "if" check to avoid page loading duplication */ // TODO: don't forget to assign undefined when document closed
                        currentPageLink = link;
                },
                // todo: to write appropriate methods
                error: function (jqXHR, textStatus, errorThrown) {
                    // $('#text').html('eee: ' + jqXHR.responseJSON.errors);
                    $('#text').append('<div style="margin-left: 60px"><h3><i id="err"></i></h3></h2></div>');
                    $('#err').html(textStatus + ': ' + jqXHR.status + '. '
                        + '<br/>' + jqXHR.responseJSON.status + ': ' + jqXHR.responseJSON.message
                    + '<br/>' + jqXHR.responseJSON.errors);

                    console.log('some error: ' + textStatus + ': ' + jqXHR.status);
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
    function getPagesReferenceButtons(docFormId, data) {
        let links = data._links;
        let nextLink = links.next.href;
                                                console.log('nextLink ======== ' + nextLink);
        // docFormId is id of form created by createPageTextFormElement(textCommonDataId), see extractDocToHtml(textCommonDataId)
        $('#' + docFormId).append('<div id=page-buttons>' +
            '<button type="button" id="'+ data.pageNumber + '" >' + data.pageNumber + '</button>' +
            // '<button type="button" id="'+ data.page_number + '" onclick="extractPage(' + nextLink + ')">' + data.page_number + '</button>' +
            '</div>');
        $('#' + data.pageNumber).click(function () {
            extractPage(nextLink);
        });
    }

// =========================================================================================== PERFORMING

    $('#helper2').click(function (e) {
        e.preventDefault();
        extractDocToHtml(36);
        // docFormId = 36;
        // extractPage('http://localhost:8074/textsaver/text-common-data/36/text-parts/pages?page=2');

    });

    $('#create-doc-btn').click(function (event) {
        event.preventDefault();
        createNewDoc();
    });
});
