/** to check if this page is already opened */
let currentPageLink;
/**  */
let docFormId;

/** this array allows to note if any text (i.e. textCommonData) is already called */
let textpartsQualifier = [];

$(function () {
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

// ================================================================================ MAIN BUSINESS LOGIC

// --------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------- GET and RENDER references for all the documents that persisted in db
// --------------------------------------------------------------------------------------------------------------------


// --------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------- GET and RENDER all the TEXT PART with given textCommonDataId
// --------------------------------------------------------------------------------------------------------------------

    /** create necessary form for page rendering in the index.html.
     * And then fill it with it's content (extractPage(linkFirst)) */
    function extractDocToHtml(textCommonDataId) {
        /** id for the document pages form that will be created by createPageTextFormElement() method  */
         docFormId = textCommonDataId;

        /* when first call of given function with such textCommonDataId, create page form element
        into the tag <div id="text"></div> on the index.html */
        createPageTextFormElement(textCommonDataId);

        let pageLink = 'text-common-data/'+ textCommonDataId + '/text-parts/pages?page=1';
        extractPage(pageLink);
    }

// --------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------- GET page and RENDER it on the created document form
// --------------------------------------------------------------------------------------------------------------------

    function extractPage(link) {
        console.log('extraction beginning: ' + link);

       /** get and render page content. To avoid content duplication,     */
        if (link !== currentPageLink) {
            $.ajax({
                type:'GET',
                /* get all the textparts */
                url: link,
                cache: true, // default
                accepts: {json: 'application/json, application/hal+json'}, // application/json is default
                isModified: true, // todo: check this property and be careful
                success: function (data, textStatus, jqXHR) {

                    console.log('textStatus ', textStatus);
                    // console.log(jqXHR.getAllResponseHeaders());
                    console.log('data.status ', data.status);
                    console.log('jqXHR.status ', jqXHR.status);
                    console.log('data.body', data.body);
                    console.log('data._links.first.href', data._links.first.href);

                    /* create buttons row with certain pages (including bookmarks) links  */
                    getPagesReferenceButtons(docFormId, data);

                    /* create the textarea element and fill it with the textpart content */

                    let textareaId = 'page-tarea';

                    /* create current textarea element */
                    let textarea = createTextareaElement(docFormId, textareaId);

                    /* fill it with content and auto grow its height according loaded content */
                    fillTextareaWithContent(textarea, data);

//    TODO: добавить новый объект в массив, содержащий объекты типа: {textPart.id, textarea}
                    /* push created textarea in appropriate container to bind it with its textpart reference */
                    // textpartsQualifier.push({textareaId: data.id});
                    console.log(textarea.attr('id'));

                    /* create handlers that decide when the given textpart has to be updated accordingly with textarea changes */
                    createTextareaContentEventHandlers(textarea);
                    // });

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

    /** create buttons row with certain pages (including bookmarks) links  */
    function getPagesReferenceButtons(docFormId, data) {
        // $('#' + docFormId).append('<div id=page-buttons></div>')
        // $('#page-buttons').append('<input type="submit" value="' + data.page_number + '" class="button-bar">');
        let links = data._links;
        let nextLink = links.next.href;
        console.log('nextLink ======== ' + nextLink);
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
});
