
$(function () {
// ================================================================================ MAIN BUSINESS LOGIC
    let checkSum = 0;
    let currentPageNumber = 0;
    let currentPageLink = '';
    let currentDocName = '';
    let createdDate = '';
    let updatedDate = '';
    let totalPages = 0;
    let isPageUpdated = false;
    let isSpecialBookmark = false;
    let clickedPgNmBackgroundColor = '';
// --------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------- GET a list of saved doc links and render them in '#docLinks' block
// --------------------------------------------------------------------------------------------------------------------

    function getSavedDocLinks() {
                                console.log('getSavedDocLinks() begin ---------------------------');
        $.ajax({
            type: 'GET',
            url: 'doc-data', // returns List<TextCommonDataResource>
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                                console.log('check: obtainedData[0].links[0].href = ' + obtainedData[0].links[0].href + ', name: ' +
                                    obtainedData[0].name);
                                console.log('obtainedData[0].firstPageLink = ' + obtainedData[0].firstPageLink + ', name: ' +
                                    obtainedData[0].name);

                let err = $('#error-panel');
                err.find('pre').html('');
                err.css('visibility', 'hidden');
                /* NB: the "href" attribute will be set by the following function for each pageLink */
                getDocLinksSortedByNameAndCreatedDate(obtainedData);
                /* set behavior of each elem 'a' in the '#docLinks' context */
                setLinksOnclickBehavior($('a', '#docLinks'));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let err = $('#error-panel');
                err.css('visibility', 'visible');
                err.find('pre').html(jqXHR.responseText);
            }
        });
                                console.log('getSavedDocLinks() end ---');
    }

// --------------------------------------------------------------------------------------------------------------------
// --------------------------------------------- (POST) create page and RENDER it on the created document form
// --------------------------------------------------------------------------------------------------------------------

    function createNewDoc() {
                                    console.log('createNewDoc() begin ---------------------------');

        if (currentPageLink !== '') updatePage(currentPageLink);

        let input = $('#create-doc-text-input');
        currentDocName = input.val();
        input.val("");

        createDocLink(currentDocName);
        setMarkup(currentDocName);

        // jQuery.ajax( [settings ] )
        $.ajax({
            type: 'POST', // http://localhost:8074/textsaver/doc-data
            // NB: this property must be defined in POST request
            contentType: "application/json; charset=utf-8",
            url: 'doc-data',
            data: JSON.stringify(currentDocName),
           dataType: 'json', // obtainedData = TextCommonDataResource {"name":..., "createdDate":..., "updatedDate":...,
            // _links": {"self": {"href": "http://localhost:8074/textsaver/doc-data/1534/pages?page=0"}}}
            success: function (obtainedData, status, jqXHR) {
                let err = $('#error-panel');
                err.find('pre').html('');
                err.css('visibility', 'hidden');

                currentPageNumber = 0;
                currentPageLink = obtainedData._links.self.href;
                createdDate = obtainedData.createdDate;
                updatedDate = obtainedData.updatedDate;
                checkSum = 0;
                // setInsertPageButtonBehavior();
                totalPages = 1;

                setNewDocLinkOnclickBehavior(currentPageLink, currentDocName);
                extractBookmarks();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let err = $('#error-panel');
                err.css('visibility', 'visible');
                err.find('pre').html(jqXHR.responseText);
            }
        });
                                    console.log('createNewDoc() end ---');
    }

// --------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------- GET and RENDER current page
// --------------------------------------------------------------------------------------------------------------------

function extractPageContent(previousPageLink, targetPageLink) {
                                                console.log('extractPageContent() begin ---------------------------');
                                                console.log('EXTRACTPAGECONTENT() pageLink =  ' + targetPageLink +
                                                ', background = ' + $('#' + getPageNumberByLink(targetPageLink)) //.css('background').substring(0, 16));
                                                    + ', isSpecialBookmark = ' + isSpecialBookmark);
    $.ajax({
        type: 'GET',
        url: targetPageLink, // http://localhost:8074/textsaver/doc-data/837/pages?page=0
        dataType: 'json', // returns PageResource instance
        success: function (data, status, jqXHR) {
                                                    console.log('extractPageContent(): success');
            let err = $('#error-panel');
            err.find('pre').html('');
            err.css('visibility', 'hidden');

            totalPages = data.totalPages;

            console.log('extractPageContent(): totalPages = ' + totalPages);
            let text = $('#container').find('#text');
            let pageContent = data.body;

            if (pageContent !== null) {
                /* since JSON.stringify replace all "new line" tokens with "\\n", I need here the inverse transform */
                let regex = new RegExp('\\\\n', 'g'); // flag 'g' means 'all matches'
                pageContent = pageContent.toString().replace(regex, '\r\n');

                text.val(pageContent);
                checkSum = text.val().toString().length;
                                                    console.log('pageContent = ' + pageContent.toString().substring(0, 8) + '...');
            } else {
                checkSum = 0;
                text.val('');
                                console.log('pageContent = null');
            }

                                                        console.log('EXTRACTPAGECONTENT: checkSum = ' + checkSum +
                                                        ', currentPageNumber = ' + currentPageNumber +
                                                        ', data.pageNumber = ' + data.pageNumber +
                                                        + ', currentPageLink = ' + currentPageLink + ', totalPages = ' + totalPages +
                                                        ', color = ' + $('#' + data.pageNumber).css('background') +
                                                        ', isSpecialBookmark = ' + isSpecialBookmark);

            updateBookmarks(previousPageLink, targetPageLink);
            setCloseDocButtonBehavior(targetPageLink);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let err = $('#error-panel');
            err.css('visibility', 'visible');
            err.find('pre').html(jqXHR.responseText);
        }
    });
                                        console.log('extractPageContent() end ---');
}

// --------------------------------------------------------------------------------------------------------------------
                                                        /** POST to get an array of bookmarks for current page number
 * POST request was selected except of GET since POST doesn't use for bookmarks to create new ones (the "bookmarks"
 * field of the db table is created first time by create() method of TextCommonDataServiceImpl and after it needs only
 * PUT requests) */
// --------------------------------------------------------------------------------------------------------------------

    // TODO: to complete it
    function extractBookmarks() {
        console.log('extractBookmarks() begin ---------------------------');
        $.ajax({
            type: 'POST',
            url: getBookmarksLink(currentPageLink), // http://localhost:8074/textsaver/doc-data/1733/bookmarks
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({"pageNumber":currentPageNumber, "totalPages":totalPages}),
            dataType: 'json', // obtainedData = BookmarkResource array
            success: function (obtainedData, status, jqXHR) {
                                            console.log('EXTRACTBOOKMARKS: url = ' + getBookmarksLink(currentPageLink));
                                            obtainedData.forEach(d => console.log('; ' + d.color + '::' + d.pageLink + '::' + d.pageNumber));

        // isSpecialBookmark variable assigning occurs here
                obtainedData.forEach(bookmarkResource => {
                    if (bookmarkResource.pageNumber.toString() === nextPageNumber.toString()) {
                        nextBackgroundColor = bookmarkResource.color;
                        isSpecialBookmark = nextBackgroundColor === "ffb704";
                    }
                });

                toggleSpecialBookmarkCss();
                resetPageNmBtns(obtainedData);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let err = $('#error-panel');
                err.css('visibility', 'visible');
                err.find('pre').html(jqXHR.responseText);
            }
        });
        console.log('extractBookmarks() end ---');
    }

// --------------------------------------------------------------------------------------------------------------------
                                    /** PUT to update bookmarks and  get an array of bookmarks for current page number */
// --------------------------------------------------------------------------------------------------------------------

    function updateBookmarks(previousPageLink, nextPageLink) {
        console.log('updateBookmarks() begin ---------------------------');

        let nextPageNumber = getPageNumberByLink(nextPageLink);
        let nextBackgroundColor;

        $.ajax({
            type: 'PUT',
            url: getBookmarksLink(previousPageLink), // http://localhost:8074/textsaver/doc-data/1733/bookmarks
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({"previousPageNumber":getPageNumberByLink(previousPageLink),
                "currentPageNumber":getPageNumberByLink(nextPageLink), "totalPages":totalPages,
                "isPageUpdated": isPageUpdated, "isSpecialBookmark": isSpecialBookmark}),
            dataType: 'json', // obtainedData = BookmarkResource array
            success: function (obtainedData, status, jqXHR) {

                isPageUpdated = false;

        // to assign isSpecialBookmark variable value:
                obtainedData.forEach(bookmarkResource => {
                    if (bookmarkResource.pageNumber.toString() === nextPageNumber.toString()) {
                        nextBackgroundColor = bookmarkResource.color;
                        isSpecialBookmark = nextBackgroundColor === "ffb704";
                    }
                });

                toggleSpecialBookmarkCss();
                                console.log('UPDATEBOOKMARKS: nextPageNumber = ' + nextPageNumber + ', nextBackgroundColor = ' +
                                    nextBackgroundColor + ', isPageUpdated = ' + isPageUpdated +
                                    ', isSpecialBookmark = ' + isSpecialBookmark + ', url = ' + getBookmarksLink(previousPageLink) +
                                ', getPageNumberByLink(previousPageLink) = ' + getPageNumberByLink(previousPageLink));
                                // obtainedData.forEach(d => console.log('; ' + d.color + '::' + d.pageLink + '::' + d.pageNumber));

                // to reset bookmark buttons row:
                resetPageNmBtns(obtainedData);

            },
            error: function (jqXHR, textStatus, errorThrown) {
                let err = $('#error-panel');
                err.css('visibility', 'visible');
                err.find('pre').html(jqXHR.responseText);
            }
        });
        console.log('updateBookmarks() end ---');

    }

// --------------------------------------------------------------------------------------------------------------------

    function resetPageNmBtns(bookmarksArray) {

        let upperPageButtons = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row');
        // let isSpecialBookmarkButton = upperPageButtons.find('.page-btn-bar').find('#is-special-bookmark');
        upperPageButtons.find('.page-number-button').remove();

        bookmarksArray.forEach(bookmarkResource => {
            let pageNumber = bookmarkResource.pageNumber;
            let numberButton =
                // bookmarkResource.color === "00f6eb"?
                // $('<button id="'+ pageNumber + '" type="submit" formaction="' +
                //     bookmarkResource.pageLink + '" class="is-special">'+ (pageNumber + 1) + '</button>'):
                $('<button id="'+ pageNumber + '" type="submit" formaction="' +
                bookmarkResource.pageLink + '" class="page-number-button">'+ (pageNumber + 1) + '</button>');
            upperPageButtons.find('.bookmarks-bar').append(numberButton);
    // ONCLICK BEHAVIOR assigning:
            numberButton.click(function (event) {
                event.preventDefault();

                // todo: if the page updating is necessary in forEach cycle? CHECK (but without this line there is no
                //  updating for insertPageButton and PageNmButtons, only for back-forward)
                updatePage(currentPageLink);

                            console.log('RESETPAGENMBTNS clickedPgNmBackgroundColor = ' + clickedPgNmBackgroundColor +
                                ', pageNumber = ' + pageNumber + ', isSpecialBookmark = ' + isSpecialBookmark);

                // toggleSpecialBookmarkCss(isSpecialBookmarkButton);

                let previousPageLink = currentPageLink;
                currentPageNumber = pageNumber;
                currentPageLink = bookmarkResource.pageLink;
                extractPageContent(previousPageLink, currentPageLink); // checkSum will be updated here
                // isPageUpdated = false;

                console.log('setPageNumberButtonBehavior() after click: currentPageNumber = '
                    + currentPageNumber + ', currentPageLink = ' + currentPageLink);

                });

            setBookmarkButtonCss(currentPageNumber, numberButton, bookmarkResource.color);
        });

    }
// -------------------------------------------------------------------------------------------------------------------

    function setBookmarkButtonCss(currentPageNumber, numberButtonElement, bookmarkResourceColor) {
        let prefix = '#';
        if (bookmarkResourceColor.toString().substring(0, 3) !== 'rgb') bookmarkResourceColor = prefix + bookmarkResourceColor;

        numberButtonElement.css("background", bookmarkResourceColor);
        numberButtonElement.mousedown(function () {$(this).css("background", "#efff00");});
        numberButtonElement.mouseup(function () {$(this).css("background", "#ffdf00");});
        numberButtonElement.hover(function () {$(this).css("background", "#8AB8CC");},
            function () {$(this).css("background", bookmarkResourceColor);});
        $('#' + currentPageNumber).prop('disabled', true);
        numberButtonElement.prop('disabled', false);
    }

// -------------------------------------------------------------------------------------------------------------------


    function setIsSpecialBookmarkButtonBehavior() {
        let isSpecialBookmarkButton = $('#container').find('#upper-doc-bar')
            .find('#upper-page-buttons-row').find('.page-btn-bar').find('#is-special-bookmark');
        isSpecialBookmarkButton.click(function (event) {
            event.preventDefault();
            isSpecialBookmark = isSpecialBookmark !== true; // toggle true/false
                                    console.log('SETSPECIALBOOKMARK: isSpecialBookmark = ' + isSpecialBookmark);
            toggleSpecialBookmarkCss(isSpecialBookmarkButton);
        });

    }

// ------------------------------------------------------------------------------------------------------------

    function toggleSpecialBookmarkCss() {
        isSpecialBookmarkButton = $('#container').find('#upper-doc-bar')
            .find('#upper-page-buttons-row').find('.page-btn-bar').find('#is-special-bookmark');

        let backgroundColor = isSpecialBookmark ? "#00f6eb" : "#f6f4f6";

        isSpecialBookmarkButton.css("background", backgroundColor);
        isSpecialBookmarkButton.mousedown(function () {$(this).css("background", "#efff00");});
        isSpecialBookmarkButton.mouseup(function () {$(this).css("background", "#ffdf00");});
        isSpecialBookmarkButton.hover(function () {$(this).css("background", "#8AB8CC");},
            function () {$(this).css("background", backgroundColor);});
    }

// ------------------------------------------------------------------------------------------------------------

// -------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------- (POST) CREATE NEW PAGE
// -------------------------------------------------------------------------------------------------------------------

    function insertPage(pageNm) {
                                            console.log('insertPage(' + pageNm + ') begin ---------------------------');
                                            console.log('currentPageLink = ' + currentPageLink);
                                            console.log('$(\'#\' + pageNm).attr(\'formaction\') = ' + $('#' + pageNm).attr('formaction'));

        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row')
            .find('.page-btn-bar').find('bookmarks-bar');

        // let currentPageButton = form.find('#' + currentPageNumber);
        let insertedPageNm = currentPageNumber + 1;
        let insertedPageLink = getNextPageLink(currentPageLink, currentPageNumber);

        updatePage(currentPageLink);
        // updatePage(getNextPageLink(currentPageLink, currentPageNumber));

        // let insertedPageButton = $('<button id="' + insertedPageNm + '" formaction="' + insertedPageLink + '" class="page-number-button" disabled>'
        //     + (insertedPageNm + 1) + '</button>');
        // insertedPageButton.insertAfter(currentPageButton);

            $.ajax({
            type: 'POST', // http://localhost:8074/textsaver/doc-data815/pages?page=25
            contentType: "application/json; charset=utf-8",
            url: insertedPageLink,
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                let err = $('#error-panel');
                err.find('pre').html('');
                err.css('visibility', 'hidden');

                let previousPageLink = currentPageLink;
                currentPageNumber = insertedPageNm;
                currentPageLink = insertedPageLink;
                                            console.log('insertPage(): status is ' + status + ', currentPageNumber = '
                                                + currentPageNumber + ', currentPageLink = ' + currentPageLink);
                updateBookmarks(previousPageLink, insertedPageLink);
                checkSum = 0;
                totalPages++;
                $('#text').val('');
            },
                error: function (jqXHR, textStatus, errorThrown) {
                    let err = $('#error-panel');
                    err.css('visibility', 'visible');
                    err.find('pre').html(jqXHR.responseText);
                }
        });
                                        console.log('insertPage() end ---');
    }

// -------------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------- (PUT) UPDATE PAGE
// -------------------------------------------------------------------------------------------------------------------

    function updatePage(targetPageLink) {
                                            console.log('updatePage(' + currentPageLink + ') begins ---------------------------');
                                            console.log('currentPageNumber = ' + currentPageNumber +
                                                ', \ncurrentPageLink = ' + currentPageLink +
                                            ', \ncheckSum = ' + checkSum +
                                            ', \ntotalPages' + totalPages);

        let currentPageContent = $('#text').val();
        let currentPageCheckSum = currentPageContent.toString().length;

        let targetPageNumber = getPageNumberByLink(targetPageLink);

                                            console.log('UPDATEPAGE targetPageNumber = ' + targetPageNumber + ', target button color = ' +
                                                $('#' + currentPageNumber).css('background').substring(0, 16) +
                                                ', currentPageContent = ' + currentPageContent.toString().substring(0, 8) +
                                            '..., \ncurrentPageCheckSum = ' + currentPageCheckSum);
        if (checkSum !== currentPageCheckSum) {
            isPageUpdated = true;
            $.ajax({
                type: 'PUT',
                contentType: "application/json; charset=utf-8",
                url: currentPageLink, // http://localhost:8074/textsaver/doc-data/106/pages?page=25
                data: JSON.stringify(currentPageContent),
//                dataType: 'text', // obtainedData
                success: function () {
                                            console.log('success updating page ' + currentPageLink + ', ' +
                                                'pageContent = ' + currentPageContent.toString().substring(0, 5) + "...");
                    let err = $('#error-panel');
                    err.find('pre').html('');
                    err.css('visibility', 'hidden');

                    checkSum = currentPageCheckSum;
                },
                error: function (jqXHR) {
                    let err = $('#error-panel');
                    err.css('visibility', 'visible');
                    err.find('pre').html(jqXHR.responseText);
                }
            });
        } else {
            isPageUpdated = false;
                                            console.log('updatePage(): checkSum = ' + checkSum + ', there was nothing changed');
                                            }
    }


// -------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------------- (DELETE) DELETE PAGE
// -------------------------------------------------------------------------------------------------------------------

    function deletePage(pageNumber) {

        if (isSpecialBookmark &&
            $('#' + (currentPageNumber + 1)).css('background').toString().substring(0, 16) !== "rgb(0, 246, 235)")
            $('#is-special-bookmark').click();

        // todo: extractPageContent() происходит в resetPageNmBtns(), который происходит в updateBookmarks(),
        //  который происходит в extractPageContent(). Break the vicious circle!
        $.ajax({
            type: 'DELETE',
            url: currentPageLink, // http://localhost:8074/textsaver/doc-data/1812/pages?page=6
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                let err = $('#error-panel');
                err.find('pre').html('');
                err.css('visibility', 'hidden');

                console.log('DELETEPAGE(): currentPageLink = ' + currentPageLink);

                if (pageNumber === totalPages - 1) {
                    currentPageLink = getPreviousPageLink(currentPageLink, currentPageNumber);
                    currentPageNumber--;
                }

                totalPages--;

                extractPageContent(currentPageLink, currentPageLink);

            },
            error: function (jqXHR, textStatus, errorThrown) {
                let err = $('#error-panel');
                err.css('visibility', 'visible');
                err.find('pre').html(jqXHR.responseText);
            }
        });
    }

// -------------------------------------------------------------------------------------------------------------------


// ============================================================================================= AUXILIARY

// ------------------------------------------------------------------------------------------------------------

    // several util functions placed here, 'cause from utility.js I get "Uncaught ReferenceError: extractPageContent
    // is not defined"


// --------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------- update current page and CLOSE doc
// --------------------------------------------------------------------------------------------------------------------
function setCloseDocButtonBehavior(targetPageLink) {
    let closeButton = $('#close-doc');
    closeButton.click(function (event) {
        event.preventDefault();
                                            console.log('setCloseDocButtonBehavior() begins;');
        updatePage(targetPageLink);
        location.reload();
    });
}

// ------------------------------------------------------------------------------------------------------------

    /* set the onclick behavior of the links in context '#docLinks' (class .col-1, left column) */
    function setLinksOnclickBehavior(docLink) {
                                                console.log('setLinksOnclickBehavior() begin ---------------------------');

        docLink.click(function (event) {
            event.preventDefault();
                                                console.log('LINK CHECK: $(this).attr(\'href\') = ' + $(this).attr('href'));
                                                console.log('currentPageLink = ' + currentPageLink);
            if (currentPageLink !== '') updatePage(currentPageLink); // checking control sum operation locates in updatePage() method

                                            console.log('setLinksOnclickBehavior: currentPageLink = ' + currentPageLink);
                                            console.log('setLinksOnclickBehavior: $(this).attr(\'href\') = ' + $(this).attr('href'));
            if (currentPageLink !== $(this).attr('href')) {
                currentDocName = $(this).html();
                currentPageLink = $(this).attr('href');
                                    console.log('setLinksOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);
                currentPageNumber = 0;
                setMarkup(currentDocName);
                $('#1').attr('formaction', currentPageLink);
                extractPageContent(currentPageLink, currentPageLink);
                                        console.log('setLinksOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);

                // setInsertPageButtonBehavior();
            }
        });
                                        console.log('setLinksOnclickBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------

    /* add href to pageLink and set pageLink onclick behavior */
    function setNewDocLinkOnclickBehavior(docHref) {
                                    console.log('******************************************************');
                                    console.log('setNewDocLinkOnclickBehavior() begin ---------------------------');

        let docLink = $('#docLinks').children().eq(0);
        /* add href to pageLink */
        docLink.prop('href', docHref);
        /* set pageLink onclick behavior */
        docLink.click(function (event) {
            event.preventDefault();
                                        console.log('setNewDocLinkOnclickBehavior: currentPageLink = ' + currentPageLink);
                                        console.log('setNewDocLinkOnclickBehavior: $(this).attr(\'href\') = ' + $(this).attr('href'));
            if (currentPageLink !== '') updatePage(currentPageLink); // checking control sum locates in updatePage() method
            if (currentPageLink !== $(this).attr('href')) {
                currentDocName = $(this).html();
                currentPageLink = $(this).attr('href');
                                        console.log('setNewDocLinkOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);
                currentPageNumber = 0;
                setMarkup(currentDocName);
                $('#1').attr('formaction', currentPageLink);
                // setPageNumberButtonBehavior(currentPageLink, 1);
                extractPageContent(currentPageLink, currentPageLink);
                                        console.log('setNewDocLinkOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);
                // setInsertPageButtonBehavior();
            }
        });
                                        console.log('setNewDocLinkOnclickBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------

    // нужно задавать только в 2 случаях: когда создается новый документ; и когда открывается сохраненный документ
    // с помощью линка.
    function setInsertPageButtonBehavior() {
                                    console.log('setInsertPageButtonBehavior() begin ---------------------------');
                                    console.log('setInsertPageButtonBehavior()  currentPageNm = ' + currentPageNumber +
                                    ', currentPageLink = ' + currentPageLink);
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let insertPageButton = form.find('#insert-page');
        insertPageButton.click(function (event) {
            event.preventDefault();
            insertPage(currentPageNumber);
        });
                                            console.log('setInsertPageButtonBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------

    function setForwardPageButtonBehavior() {
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let plusButton = form.find('#plus');

            plusButton.click(function (event) {
                event.preventDefault();
                                    console.log('setForwardPageButtonBehavior() totalPages = ' + totalPages);
                                    console.log('bookmarks link = ' + getBookmarksLink(currentPageLink));

                // let currentPageButton = form.find('#' + currentPageNumber);
                let insertedPageNm = currentPageNumber + 1;
                let insertedPageLink = getNextPageLink(currentPageLink, currentPageNumber);
                if (currentPageNumber < totalPages - 1) {
                    updatePage(insertedPageLink);
                    extractPageContent(currentPageLink, insertedPageLink);
                    // isPageUpdated = false;
                    currentPageNumber = insertedPageNm;
                    currentPageLink = insertedPageLink;
                }
            });
    }

// ------------------------------------------------------------------------------------------------------------
    function setBackPageButtonBehavior() {
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let minusButton = form.find('#minus');

        minusButton.click(function (event) {
            event.preventDefault();
            console.log('setBackPageButtonBehavior() totalPages = ' + totalPages);
            console.log('bookmarks link = ' + getBookmarksLink(currentPageLink));

            // let currentPageButton = form.find('#' + currentPageNumber);
            let insertedPageNm = currentPageNumber - 1;
            let insertedPageLink = getPreviousPageLink(currentPageLink, currentPageNumber);
            if (currentPageNumber > 0) {
                updatePage(insertedPageLink);
                extractPageContent(currentPageLink, insertedPageLink);
                // isPageUpdated = false;
                currentPageNumber = insertedPageNm;
                currentPageLink = insertedPageLink;
            }
        });
        console.log('setForwardPageButtonBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------

    function setDeletePageButtonBehavior() {
        let deleteButton = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row')
            .find('.page-btn-bar').find('#delete-page');

        deleteButton.click(function (event) {
            event.preventDefault();

            if (currentPageNumber === 0) alert('You cannot delete page 1');
            else {if (confirm('Do you want to delete page ' + (currentPageNumber + 1) + '?')) {
                deletePage(currentPageNumber);
            }}
        });
    }

// ------------------------------------------------------------------------------------------------------------

    function getPageLinkByPageNumber(currentPageLink, currentPageNm, pageNm) {
        let regex = new RegExp('(' + currentPageNm + '$)');
        return currentPageLink.replace(regex, pageNm);
    }

// ------------------------------------------------------------------------------------------------------------

    function getNextPageLink(currentPageLink, currentPageNm) {

        let regex = new RegExp('(' + currentPageNm + '$)');
        let nextPageNumber = currentPageNm + 1;
                                    console.log('nextPageNumber = ' + nextPageNumber);
                                    console.log('regex: ' + currentPageLink.replace(regex, nextPageNumber));
        return currentPageLink.replace(regex, nextPageNumber);
    }

// ------------------------------------------------------------------------------------------------------------

    function getPreviousPageLink(currentPageLink, currentPageNm) {

        let regex = new RegExp('(' + currentPageNm + '$)');
        let previousPageNumber = currentPageNm - 1;
        console.log('previousPageNumber = ' + previousPageNumber);
        console.log('regex from getPreviousPageLink(): ' + currentPageLink.replace(regex, previousPageNumber));
        return currentPageLink.replace(regex, previousPageNumber);
    }

// ------------------------------------------------------------------------------------------------------------

    function getBookmarksLink(currentPageLink) {
        let regex = new RegExp('pages[\?]page=\\d+$'); // \d+$  [\d]*$
        return currentPageLink.replace(regex, 'bookmarks');
    }

// ------------------------------------------------------------------------------------------------------------

    function getPageNumberByLink(link) {
        return link.match(/\d+$/)[0];
    }

// ------------------------------------------------------------------------------------------------------------

// --------------------------------------------------------------------------------------------------------------------
// --------------------------------------------------------------------------------------- create html container markup
// --------------------------------------------------------------------------------------------------------------------

    function    setMarkup(docName) {
                                            console.log('setMarkup() begins ------------');

        setContainer();

        let container = $('#container');
        let text = container.find('#text');
        let upperNameBar = container.find('#upper-doc-name-bar');
        let upperPageButtons = container.find('#upper-page-buttons-row');

        clearMainDocMenu(['create-doc-block', 'search-doc']);
        addMainDocButtons('create-doc-block');
        createInitialButtonsRow(upperPageButtons);
        renderNameOnTheBar(docName, upperNameBar);
        createTextarea();
        createTextareaContentEventHandlers(text);

        setForwardPageButtonBehavior();
        setBackPageButtonBehavior();
        setInsertPageButtonBehavior();
        setIsSpecialBookmarkButtonBehavior();
        setDeletePageButtonBehavior();
        // console.log('@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@' + $('#container').find('#upper-doc-bar')
        //     .find('#upper-page-buttons-row').find('.page-btn-bar').find('#is-special-bookmark').html());
        console.log('setMarkup() end ---');
    }
// ----------------------------------------------------------------------------------------------------------------
// ===================================--------------------------------------------------------- TEXTAREA EVENT HANDLERS

    /** create a handler for given textarea to watch changes in the content, when the focus is obtained.
     * @param textarea - current textarea element
     * @return void
     * @exception
     * @see dataAccessCounter */

    function createTextareaContentEventHandlers(textarea) {
                                        console.log('createTextareaContentEventHandlers() begins --------------');
        /* handle textarea when it gains focus, i.e. either mouse clicks on the textarea or it's selected
         * with Tab key from the keyboard */
        let timerId;
        let auxTimerId;
        textarea.focus(function () {
            let textLength = textarea.val().length;
            /* create recursive setTimeout to check textarea content changes every 1 second */
            timerId = setTimeout(function check() {
// // и вот сюда функцию для проверки содержимого и определения, не пора ли перезаписать в бд эту сущность,
// // а затем promise(?), который ищет id из следующего элемента в
                let newLength = textarea.val().length;
                console.log(newLength);
                if (Math.abs(newLength - textLength) > 5) {
                    console.log('updating required');
                    textLength = newLength;
                }
                timerId = setTimeout(check, 1000);
            }, 1000);

            /* create recursive setTimeout with checking textarea content changes every 5 second
            to create lower name bar and buttons row */
            let container = $('#container');
            let upperNameBar = container.find('#upper-doc-name-bar');
            let lowerNameBar = container.find('#lower-doc-name-bar');
            // let upperPageButtons = container.find('#upper-page-buttons-row');
            // let lowerPageButtons = container.find('#lower-page-buttons-row');
            auxTimerId = setTimeout(function check() {
                let docName = lowerNameBar.html();
                if (docName === '') {
                    console.log('docName === undefined');
                    let taHeight = textarea.css('height');
                    if (taHeight.substring(0, taHeight.length - 2) > 170) {
                        console.log('TEXT height ' + taHeight + ', need lower button row');
                        // lowerPageButtons.html(upperPageButtons.html());
                        lowerNameBar.html(upperNameBar.html());
                        clearTimeout(auxTimerId);
                    }
                    else auxTimerId = setTimeout(check, 5000);
                }
            }, 5000);
        });
        /* handle event of loosing focus  */
// NB: this handler must not to be created into the timer because of creating the new blur handler
// in each iteration of timer
        textarea.blur(function () {
            let length = textarea.val().length;
            console.log('focus lost: ' + length);
            clearTimeout(timerId);
            clearTimeout(auxTimerId);
        });
                                    console.log('createTextareaContentEventHandlers() end ---');
    }

// ----------------------------------------------------------------------------------------------------------------
// =========================================================================================== PERFORMING


    getSavedDocLinks();
    $('#create-doc-btn').click(function (event) {
        event.preventDefault();
        createNewDoc();
    });
});
