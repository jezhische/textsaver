
$(function () {
// ================================================================================ MAIN BUSINESS LOGIC
let currentPageNumber = 1;
// --------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------- GET a list of saved doc links and render them in '#docLinks' block
// --------------------------------------------------------------------------------------------------------------------

    function getSavedDocLinks() {
        $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: 'doc-data', // returns List<TextCommonDataResource>
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                                console.log('check: obtainedData[1].links[0].href = ' + obtainedData[1].links[0].href + ', name: ' +
                                    obtainedData[1].name);
                                console.log('obtainedData[0].firstPageLink = ' + obtainedData[1].firstPageLink + ', name: ' +
                                    obtainedData[1].name);
                getDocLinksSortedByNameAndCreatedDate(obtainedData);
                setLinkOnclickBehavior();
                console.log('***************** $(\'.d_link\')[0] = ' + $('.d_link')[0]);
            },
            error: function () {
                // TODO: make error handling
                alert('error in getSavedDocLinks()');
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

        createDocLink(docName);
        setMarkup(docName);

        // jQuery.ajax( [settings ] )
        $.ajax({
            type: 'POST', // http://localhost:8074/textsaver/doc-data
            // NB: this property must be defined in POST request
            contentType: "application/json; charset=utf-8",
            url: 'doc-data',
            data: JSON.stringify(docName),
           // dataType: 'json',
            dataType: 'text', // obtainedData = "http://localhost:8074/textsaver/doc-data/815/pages?page=1"
            success: function (obtainedData, status, jqXHR) {
                /* add href to link and set link onclick behavior */
                addHref(obtainedData, docName);
                setPageNumberButtonBehavior(obtainedData, 1);
                setInsertPageButtonBehavior(1);
                                        // alert(getNextPageLink($('#' + 1).attr('formaction')));
                // window.frames[0].location = obtainedData;
            },
            error: function () {
                console.log('error message: this.url = ' + this.url);
                // TODO: make error handling
                alert('error');
            }
        });
    }

// --------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------- GET and RENDER current page
// --------------------------------------------------------------------------------------------------------------------

function extractPageContent(link) {
                                    console.log('*********** extractPageContent link: ' + link);
    $.ajax({
        type: 'GET', // http://localhost:8074/textsaver/doc-data/837/pages?page=1
        url: link,
        dataType: 'json', // returns PageResource instance
        success: function (data, status, jqXHR) {
            let text = $('#container').find('#text');
                                                    console.log('extractPageContent: success');
            text.val(data.body);
            // setInsertPageButtonBehavior(data.pageNumber);
        },
        error: function () {
            alert('error in extractPageContent')
        }
    });
}

// --------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    function extractBookmarks(link) {

    }

// --------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    function setBookmarks(link) {

    }

// -------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------- (POST) CREATE NEW PAGE
// -------------------------------------------------------------------------------------------------------------------

    function insertPage(currentPageNm) {
        let newPageLink = getNextPageLink($('#' + currentPageNm).attr('formaction'), currentPageNm);
        let newPageNm = currentPageNm + 1;
                                            console.log('function insertPage(' + currentPageNm + '): newPageLink = ' + newPageLink);
            $.ajax({
            type: 'POST', // http://localhost:8074/textsaver/doc-data815/pages?page=25
            contentType: "application/json; charset=utf-8",
            url: newPageLink,
            // data: JSON.stringify(docName),
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                                                                // alert(status);
                                                                // alert('link = ' + obtainedData._links.href);
                                            console.log('insertPage(' + currentPageNm + ') status is ' + status, 'newPageNm = ' + newPageNm);
                setPageNumberButtonBehavior(newPageLink, newPageNm);
                setInsertPageButtonBehavior(newPageNm);
                $('#text').val('');

                /* redirect to iframe */
                // window.frames[0].location = obtainedData;
            },
            error: function () {
                console.log('error message: this.url = ' + this.url);
                // TODO: make error handling
                alert('error in function insertPage(currentPageNm)');
            }
        });
    }

// -------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------- (PUT) UPDATE PAGE
// -------------------------------------------------------------------------------------------------------------------

    function updatePage(link) {
        let currentPageContent = $('#text').val();
        $.ajax({
            type: 'PUT', // http://localhost:8074/textsaver/doc-data/pages?page=25
            contentType: "application/json; charset=utf-8",
            url: link,
            data: JSON.stringify(currentPageContent),
            dataType: 'text', // currentPageContent
            success: function (obtainedData, status, jqXHR) {
                                        console.log('success updating page ' + link + ', ' +
                                            'pageContent = ' + currentPageContent.toString().substring(0, 5) + "...");
            },
            error: function () {
                alert('error updating page ' + link);
            }
        });
    }


// -------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------------------


// ============================================================================================= AUXILIARY

// ------------------------------------------------------------------------------------------------------------

    // several util functions placed here, 'cause from utility.js I get "Uncaught ReferenceError: extractPageContent
    // is not defined"
    /* set the onclick behavior of the links in context '#docLinks' (class .col-1, left column) */
    function setLinkOnclickBehavior() {
        /* each elem 'a' in the '#docLinks' context */
        $('a', '#docLinks').click(function (event) {
            event.preventDefault();
                                                console.log('$(this).attr(\'href\')' + $(this).attr('href'));
            setMarkup($(this).html());
            let docHref = $(this).attr('href');
            extractPageContent(docHref);
            extractBookmarks(docHref);
        });
    }

// ------------------------------------------------------------------------------------------------------------

    /* add href to link and set link onclick behavior */
    function addHref(docHref, docName) {
        let alink = $('#docLinks').children().eq(0);
        /* add href to link */
        alink.prop('href', docHref);
        /* set link onclick behavior */
        alink.click(function (event) {
            event.preventDefault();
            setMarkup(docName);
            extractPageContent(docHref);
            extractBookmarks(docHref);
        });
    }

// ------------------------------------------------------------------------------------------------------------

    function setPageNumberButtonBehavior(pageHref, pageNm) {
        let pageNmButton = $('#' + pageNm);
        pageNmButton.attr('formaction', pageHref);
        pageNmButton.click(function (event) {
                                                        console.log('page ' + pageNm + ' clicked');
            event.preventDefault();
            extractPageContent(pageHref);
            setInsertPageButtonBehavior(pageNm);
            setBookmarks(pageHref);
        });
    }

// ------------------------------------------------------------------------------------------------------------

    function setInsertPageButtonBehavior(currentPageNm) {
                                        alert('setInsertPageButtonBehavior()  currentPageNm = ' + currentPageNm);
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let currentPageButton = form.find('#' + currentPageNm);
        let insertPageButton = form.find('#insert-page');
        let totalPageNm = form.find('.page-number-button:last').html();
        let insertedPageNm = currentPageNm + 1;
        let currentPageLink = currentPageButton.attr('formaction');
                                                    // console.log('@@@@@@@@@@@@@@@@@@@@@@@' + totalPageNm);
                                                    // console.log('@@@@@@@@@@@@@@@@@@@@@@@' + currentPageNm);
        insertPageButton.click(function (event) {
            event.preventDefault();
            /*insert element after "currentPageButton" element */
            $('<button id="' + insertedPageNm + '" formaction="" class="page-number-button" disabled>'
                + insertedPageNm + '</button>').insertAfter(currentPageButton);
            currentPageButton.prop('disabled', false);

            updatePage(currentPageLink);
            insertPage(currentPageNm);
            // setInsertPageButtonBehavior(insertedPageNm);
            // extractPageContent(getNextPageLink(currentPageLink, currentPageNm));
            // процесс установки поведения кнопки и т.д. перенести в метод insertPage(), там можно добыть pageHref, чтобы привязать его к кнопке
            // setInsertPageButtonBehavior(insertedPageNm);
            // не забыть перейти на созданную страницу
            if (totalPageNm > currentPageNm.toString()) {
                // прибавить 1 к номерам последующих страниц;
                // создать новую страницу;
                // сохранить новую букмарк (притом с новыми цветами - но это на сервере через LRU)
            }
        });
    }

// ------------------------------------------------------------------------------------------------------------

    function setPlusPageButtonBehavior(currentPageNm, totalPageNm) {}

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

// =========================================================================================== PERFORMING


    getSavedDocLinks();
    $('#create-doc-btn').click(function (event) {
        event.preventDefault();
        createNewDoc();
    });
});
