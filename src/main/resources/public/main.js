
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
// --------------------------------------------------------------------------------------------------------------------
// ----------------------------------------------- GET a list of saved doc links and render them in '#docLinks' block
// --------------------------------------------------------------------------------------------------------------------

    function getSavedDocLinks() {
                                console.log('getSavedDocLinks() begin ---------------------------');
        $.ajax({
            type: 'GET',
            // contentType: 'application/json',
            url: 'doc-data', // returns List<TextCommonDataResource>
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                                console.log('check: obtainedData[1].links[0].href = ' + obtainedData[1].links[0].href + ', name: ' +
                                    obtainedData[1].name);
                                console.log('obtainedData[0].firstPageLink = ' + obtainedData[1].firstPageLink + ', name: ' +
                                    obtainedData[1].name);

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
            // _links": {"self": {"href": "http://localhost:8074/textsaver/doc-data/1534/pages?page=1"}}}
            // dataType: 'text', // obtainedData = "http://localhost:8074/textsaver/doc-data/815/pages?page=1"
            success: function (obtainedData, status, jqXHR) {
                let err = $('#error-panel');
                err.find('pre').html('');
                err.css('visibility', 'hidden');

                if (currentDocName !== obtainedData.name) alert("Something wrong with data saving. " +
                    "The doc name will be changed to avoid data loss. Please refrain from using any " +
                    "strange, weird and odd characters in the name");
                currentPageNumber = 1;
                currentPageLink = obtainedData._links.self.href;
                createdDate = obtainedData.createdDate;
                updatedDate = obtainedData.updatedDate;

                setNewDocLinkOnclickBehavior(currentPageLink, currentDocName);
                setPageNumberButtonBehavior(currentPageLink, 1);
                setInsertPageButtonBehavior();
                checkSum = 0;
                totalPages = 1;
                                        // alert(getNextPageLink($('#' + 1).attr('formaction')));
                // window.frames[0].location = obtainedData;
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
                                                console.log('extractPageContent() pageLink =  ' + targetPageLink);
    $.ajax({
        type: 'GET',
        url: targetPageLink, // http://localhost:8074/textsaver/doc-data/837/pages?page=1
        dataType: 'json', // returns PageResource instance
        success: function (data, status, jqXHR) {
                                                    console.log('extractPageContent(): success');
            let err = $('#error-panel');
            err.find('pre').html('');
            err.css('visibility', 'hidden');

            totalPages = data.totalPages;
            console.log('EXTRACTPAGECONTENT totalPages = ' + totalPages);
            // currentPageNumber = data.currentPageNumber;
            // extractBookmarks();
            // currentPageLink = data._links.self.href;
                                                    console.log('extractPageContent(): totalPages = ' + totalPages);
            let text = $('#container').find('#text');
            let pageContent = data.body;

            if (pageContent !== null) { // 'Cannot read property 'toString' of null'
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
                                                    console.log('extractPageContent: checkSum = ' + checkSum);
                                                    console.log('extractPageContent() currentPageNumber = ' + currentPageNumber
                                                        + ', currentPageLink = ' + currentPageLink
                                                    + ', totalPages = ' + totalPages);
            updateBookmarks(previousPageLink, targetPageLink);
            // setInsertPageButtonBehavior(data.currentPageNumber);
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

    function extractBookmarks() {
        console.log('extractBookmarks() begin ---------------------------');
        $.ajax({
            type: 'POST',
            url: getBookmarksLink(currentPageLink), // http://localhost:8074/textsaver/doc-data/1733/bookmarks
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({"pageNumber":currentPageNumber, "totalPages":totalPages}),
            dataType: 'json', // obtainedData = BookmarkResource array
            success: function (obtainedData, status, jqXHR) {
                                            console.log('EXTRACTBOOKMARKS: url = ' + getBookmarksLink(currentPageLink) +
                                                ', obtainedData[0].color = ' + obtainedData[0].color +
                                            ', obtainedData[0].pageLink = ' + obtainedData[0].pageLink +
                                            ', obtainedData[0].currentPageNumber = ' + obtainedData[0].pageNumber);
                // $('#error-panel').css('visibility', 'hidden');

            },
            error: function (jqXHR, textStatus, errorThrown) {
                let err = $('#error-panel');
                err.css('visibility', 'visible');
                err.find('pre').html(jqXHR.responseText);
            }
        });


        console.log('extractBookmarks() end ---');

// TODO: extractBookmarks() только когда открывается сохраненный по линке документ (подумать, может, сделать для линков
//  особую функцию вместо extractPage()); updateBookmarks(), только когда в updatePage() происходит запрос на сохранение; в
//  остальных случаях сделать функцию, которая просто строит кнопки на странице. А как быть с lastOpened? Это ведь тоже
//  изменение bookmarks

    }


// --------------------------------------------------------------------------------------------------------------------
                                    /** PUT to update bookmarks and  get an array of bookmarks for current page number */
// --------------------------------------------------------------------------------------------------------------------

    function updateBookmarks(previousPageLink, nextPageLink) {
        console.log('updateBookmarks() begin ---------------------------');
        $.ajax({
            type: 'PUT',
            url: getBookmarksLink(previousPageLink), // http://localhost:8074/textsaver/doc-data/1733/bookmarks
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({"previousPageNumber":getPageNumberByLink(previousPageLink),
                // "currentPageNumber":currentPageNumber, "totalPages":totalPages,
                "currentPageNumber":getPageNumberByLink(nextPageLink), "totalPages":totalPages,
                "isPageUpdated": isPageUpdated, "isSpecialBookmark": isSpecialBookmark}),
            dataType: 'json', // obtainedData = BookmarkResource array
            success: function (obtainedData, status, jqXHR) {
                                console.log('SETBOOKMARKS: url = ' + getBookmarksLink(previousPageLink));
                                obtainedData.forEach(d => console.log('; ' + d.color + '::' + d.pageLink + '::' + d.pageNumber));

                // $('#error-panel').css('visibility', 'hidden');

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
        // elem.html('<div class="page-btn-bar">' +
        //     '<button id="delete-page" style="width: 20%" disabled>delete current page</button>' +
        //     '<button id="minus" style="width: 10%" disabled>back</button>' +
        //     '<button id="1" type="submit" formaction="" class="page-number-button" disabled>1</button>' +
        //     '<button id="plus" style="width: 10%">forward</button>' +
        //     '<button id="insert-page" style="width: 20%">insert new page</button>' +
        //     '</div>');

        let upperPageButtons = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row');
                                        console.log('BEFORE PAGE BUTTONS REMOVING: upperPageButtons.find(\'.page-number-button\').html() = '
                                            + upperPageButtons.find('.page-number-button').html());
        upperPageButtons.find('.page-number-button').remove();
                                        console.log('AFTER PAGE BUTTONS REMOVING: upperPageButtons.find(\'.page-number-button\').html() = '
                                            + upperPageButtons.find('.page-number-button').html());
        bookmarksArray.forEach(bookmarkResource => {
            let pageNumber = bookmarkResource.pageNumber;
            let numberButton = $('<button id="'+ pageNumber + '" type="submit" formaction="' +
                bookmarkResource.pageLink + '" class="page-number-button">'+ pageNumber + '</button>');
            numberButton.insertBefore(upperPageButtons.find('#plus'));
                                        // console.log('BOOKMARKSARRAY create page number ' + pageNumber);


    // ONCLICK BEHAVIOR
            numberButton.click(function (event) {
                event.preventDefault();
                $('#' + currentPageNumber).prop('disabled', false);
                numberButton.prop('disabled', true);
                updatePage(currentPageLink);


// FIXME: check and trace the method behavior in this place
                let previousPageLink = currentPageLink;
                currentPageNumber = pageNumber;
                currentPageLink = bookmarkResource.pageLink;
                extractPageContent(previousPageLink, currentPageLink); // checkSum will be updated here
                console.log('setPageNumberButtonBehavior() after click: currentPageNumber = '
                    + currentPageNumber + ', currentPageLink = ' + currentPageLink);

                // updateBookmarks(previousPageLink, currentPageLink);
                });

            setBookmarksArrayButtonCss(numberButton, '#' + bookmarkResource.color);
        });

    }
// -------------------------------------------------------------------------------------------------------------------

    function setBookmarksArrayButtonCss(numberButtonElement, bookmarkResourceColor) {
        numberButtonElement.css("background", bookmarkResourceColor);
        numberButtonElement.mousedown(function () {$(this).css("background", "#efff00");});
        numberButtonElement.mouseup(function () {$(this).css("background", "#ffdf00");});
        numberButtonElement.hover(function () {$(this).css("background", "#8AB8CC");},
            function () {$(this).css("background", bookmarkResourceColor);});
    }

// -------------------------------------------------------------------------------------------------------------------

// -------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------- (POST) CREATE NEW PAGE
// -------------------------------------------------------------------------------------------------------------------

    function insertPage(pageNm) {
                                            console.log('insertPage(' + pageNm + ') begin ---------------------------');
                                            console.log('currentPageLink = ' + currentPageLink);
                                            console.log('$(\'#\' + pageNm).attr(\'formaction\') = ' + $('#' + pageNm).attr('formaction'));


        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        // let totalPageNm = form.find('.page-number-button:last').html(); // fixme: брать из тотальной переменной

        let currentPageButton = form.find('#' + currentPageNumber);
        let insertedPageNm = currentPageNumber + 1;
        let insertedPageLink = getNextPageLink(currentPageLink, currentPageNumber);

        updatePage(currentPageLink);


        // if (totalPages > currentPageNumber) {
        //     let nextPagesArray = form.find('button').get().slice(currentPageNumber + 1, totalPages);
        //     console.log('insertPage(): totalPages = ' + totalPages +
        //         ', currentPageNumber = ' + currentPageNumber +
        //         ', current slice of next buttons: ');
        //     nextPagesArray.forEach(btn => console.log($(btn).html()));
        //
        //     nextPagesArray.forEach(elem => {
        //         let element = $(elem);
        //     });
        //     nextPagesArray.forEach(arr => console.log(arr));
        // }
        /* insert page number button element after "currentPageButton" element. This button will get the necessary
        properties in the insertPage() method if success, or will be removed if error */
        let insertedPageButton = $('<button id="' + insertedPageNm + '" formaction="' + insertedPageLink + '" class="page-number-button" disabled>'
            + insertedPageNm + '</button>');
        insertedPageButton.insertAfter(currentPageButton);
        // let insertedPageButton = form.find('#' + insertedPageNm);

        currentPageButton.prop('disabled', false);
        setPageButtonCss(currentPageButton, insertedPageButton);
        // currentPageNumber = insertedPageNm;
        // currentPageLink = insertedPageLink;

            $.ajax({
            type: 'POST', // http://localhost:8074/textsaver/doc-data815/pages?page=25
            contentType: "application/json; charset=utf-8",
            url: insertedPageLink,
            // data: JSON.stringify(docName),
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
                // setPageNumberButtonBehavior(insertedPageLink, insertedPageNm);
                updateBookmarks(previousPageLink, insertedPageLink);
                checkSum = 0;
                totalPages++;
                // setInsertPageButtonBehavior(newPageNm);
                $('#text').val('');
                // updateBookmarks(previousPageLink); // todo: может быть, оставить updateBookmarks() только для update(), а здесь extractBookmarks?
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

    function updatePage(link) {
                                            console.log('updatePage(' + link + ') begins ---------------------------');
                                            console.log('currentPageNumber = ' + currentPageNumber +
                                                ', \ncurrentPageLink = ' + currentPageLink +
                                            ', \ncheckSum = ' + checkSum +
                                            ', \ntotalPages' + totalPages);

        let currentPageContent = $('#text').val();
        let currentPageCheckSum = currentPageContent.toString().length;
                                            console.log('currentPageContent = ' + currentPageContent.toString().substring(0, 8) +
                                            '..., \ncurrentPageCheckSum = ' + currentPageCheckSum);
        if (checkSum !== currentPageCheckSum) {
            isPageUpdated = true;
            $.ajax({
                type: 'PUT', // http://localhost:8074/textsaver/doc-data/pages?page=25
                contentType: "application/json; charset=utf-8",
                url: link,
                data: JSON.stringify(currentPageContent
                    // , function (val) {
                    // if (val === regex) return '\\n';}
                ),
                dataType: 'text', // currentPageContent
                success: function (obtainedData, status, jqXHR) {
                                            console.log('success updating page ' + link + ', ' +
                                                'pageContent = ' + currentPageContent.toString().substring(0, 5) + "...");
                    let err = $('#error-panel');
                    err.find('pre').html('');
                    err.css('visibility', 'hidden');

                    checkSum = currentPageCheckSum;
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    let err = $('#error-panel');
                    err.css('visibility', 'visible');
                    err.find('pre').html(jqXHR.responseText);
                }
            });
        } else {
            isPageUpdated = false;
                                            console.log('updatePage(): checkSum = ' + checkSum + ', there was nothing changed');
                                            console.log('updatePage() end ---');}
        // updateBookmarks(link);
    }


// -------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------------------------


// ============================================================================================= AUXILIARY

// ------------------------------------------------------------------------------------------------------------

    // several util functions placed here, 'cause from utility.js I get "Uncaught ReferenceError: extractPageContent
    // is not defined"
    /* set the onclick behavior of the links in context '#docLinks' (class .col-1, left column) */
    function setLinksOnclickBehavior(docLink) {
                                                console.log('setLinksOnclickBehavior() begin ---------------------------');

        docLink.click(function (event) {
            event.preventDefault();
                                                console.log('LINK CHECK: \n$(this).attr(\'href\') = ' + $(this).attr('href'));
                                                console.log('currentPageLink = ' + currentPageLink);
            if (currentPageLink !== '') updatePage(currentPageLink); // checking control sum locates in updatePage() method

                                            console.log('setLinksOnclickBehavior: currentPageLink = ' + currentPageLink);
                                            console.log('setLinksOnclickBehavior: $(this).attr(\'href\') = ' + $(this).attr('href'));
            if (currentPageLink !== $(this).attr('href')) {
                currentDocName = $(this).html();
                currentPageLink = $(this).attr('href');
                                    console.log('setLinksOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);
                currentPageNumber = 1;
                setMarkup(currentDocName);
                $('#1').attr('formaction', currentPageLink);
                // setPageNumberButtonBehavior(currentPageLink, 1);
                extractPageContent(currentPageLink, currentPageLink);
                                        console.log('setLinksOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);

                // updateBookmarks(currentPageLink, currentPageLink);

                // setPlusPageButtonBehavior(); // in setMarkup()
                setInsertPageButtonBehavior();
                // extractBookmarks();
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
                currentPageNumber = 1;
                setMarkup(currentDocName);
                $('#1').attr('formaction', currentPageLink);
                // setPageNumberButtonBehavior(currentPageLink, 1);
                extractPageContent(currentPageLink, currentPageLink);
                                        console.log('setNewDocLinkOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);
                // extractBookmarks();
                // updateBookmarks(currentPageLink, currentPageLink);
                setInsertPageButtonBehavior();
            }
        });
                                        console.log('setNewDocLinkOnclickBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------

    /* 'cause the current pageNumberButton is always disabled, this behaviour will be used
    only on the different from current buttons */
    function setPageNumberButtonBehavior(pageLink, pageNm) {
                                    console.log('setPageNumberButtonBehavior() begin ---------------------------');


        updateBookmarks(currentPageLink, pageLink);
        // let currentPageNmBtn = $('#' + currentPageNumber);
//         let pageNmButton = $('#' + pageNm);
//
//         pageNmButton.click(function (event) {
//             event.preventDefault();
//                                                     console.log('page ' + pageNm + ' clicked');
//                                                     console.log('setPageNumberButtonBehavior(): currentPageNumber = '
//                                                         + currentPageNumber + ', currentPageLink = ' + currentPageLink);
//
//             pageNmButton.attr('formaction', pageLink);
//             $('#' + currentPageNumber).prop('disabled', false);
//             pageNmButton.prop('disabled', true);
//             updatePage(currentPageLink);
//
//             // let previousPageLink = currentPageLink;
//                                             console.log('BEFORE SETBOOKMARKS: currentPageLink = ' + currentPageLink + ', pageLink = ' + pageLink);
//
// // FIXME: check and trace the method behavior in this place
//             updateBookmarks(currentPageLink, pageLink);
//             currentPageNumber = pageNm;
//             currentPageLink = pageLink;
//             extractPageContent(currentPageLink); // checkSum will be updated here
//                                             console.log('setPageNumberButtonBehavior() after click: currentPageNumber = '
//                                                 + currentPageNumber + ', currentPageLink = ' + currentPageLink);
//         });
                                            console.log('setPageNumberButtonBehavior() end ---');
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

    function setPlusPageButtonBehavior() {
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let plusButton = form.find('#plus');

            plusButton.click(function (event) {
                event.preventDefault();
                                    console.log('setPlusPageButtonBehavior() totalPages = ' + totalPages);
                                    console.log('bookmarks link = ' + getBookmarksLink(currentPageLink));

                let currentPageButton = form.find('#' + currentPageNumber);
                let insertedPageNm = currentPageNumber + 1;
                let insertedPageLink = getNextPageLink(currentPageLink, currentPageNumber);
                if (currentPageNumber < totalPages) {
                    updatePage(currentPageLink);
                    // currentPageNumber = insertedPageNm;
                    // currentPageLink = insertedPageLink;
                    let insertedPageButton = form.find('#' + insertedPageNm);
                    extractPageContent(currentPageLink, insertedPageLink);
                    /* if desired button doesn't exist, create it, else update textarea content, button css and disability */
                    // if (insertedPageButton.html() === undefined) {
                    //     $('<button id="' + insertedPageNm + '" formaction="' + insertedPageLink + '" class="page-number-button" disabled>'
                    //         + insertedPageNm + '</button>').insertAfter(currentPageButton);
                    //     currentPageButton.prop('disabled', false);
                    //     // setPageNumberButtonBehavior(insertedPageLink, insertedPageNm); // I can use currentPageLink and currentPageNumber,
                    //     // 'cause these two variables must be changed to now with extractPageContent(), but as js is asynchronous,
                    //     // it has no guarantees
                    //     // setPageButtonCss(currentPageButton, insertedPageButton);
                    //
                    //     updateBookmarks(currentPageLink, insertedPageLink);
                    //
                    // } else {
                    //     currentPageButton.prop('disabled', false);
                    //     form.find('#' + insertedPageNm).prop('disabled', true);
                    // }
                    // updateBookmarks(currentPageLink, insertedPageLink);
                    currentPageButton.prop('disabled', false);
                    form.find('#' + insertedPageNm).prop('disabled', true);
                    // setPageButtonCss(currentPageButton, insertedPageButton);
                    currentPageNumber = insertedPageNm;
                    currentPageLink = insertedPageLink;
                }
            });
    }

// ------------------------------------------------------------------------------------------------------------
    function setMinusPageButtonBehavior() {
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let minusButton = form.find('#minus');

        minusButton.click(function (event) {
            event.preventDefault();
            console.log('setMinusPageButtonBehavior() totalPages = ' + totalPages);
            console.log('bookmarks link = ' + getBookmarksLink(currentPageLink));

            let currentPageButton = form.find('#' + currentPageNumber);
            let insertedPageNm = currentPageNumber - 1;
            let insertedPageLink = getPreviousPageLink(currentPageLink, currentPageNumber);
            if (currentPageNumber > 1) {
                updatePage(currentPageLink);
                // currentPageNumber = insertedPageNm;
                // currentPageLink = insertedPageLink;
                let insertedPageButton = form.find('#' + insertedPageNm);
                extractPageContent(currentPageLink, insertedPageLink);
                /* if desired button doesn't exist, create it, else update textarea content, button css and disability */
                // if (insertedPageButton.html() === undefined) {
                //     $('<button id="' + insertedPageNm + '" formaction="' + insertedPageLink + '" class="page-number-button" disabled>'
                //         + insertedPageNm + '</button>').insertAfter(currentPageButton);
                //     currentPageButton.prop('disabled', false);
                //
                //     updateBookmarks(currentPageLink, insertedPageLink);
                //
                // } else {
                //     currentPageButton.prop('disabled', false);
                //     form.find('#' + insertedPageNm).prop('disabled', true);
                // }
                // updateBookmarks(currentPageLink, insertedPageLink);
                currentPageButton.prop('disabled', false);
                form.find('#' + insertedPageNm).prop('disabled', true);
                // setPageButtonCss(currentPageButton, insertedPageButton);
                currentPageNumber = insertedPageNm;
                currentPageLink = insertedPageLink;
            }
        });
        // }
        console.log('setPlusPageButtonBehavior() end ---');
    }

// // ------------------------------------------------------------------------------------------------------------

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
        console.log('regex: ' + currentPageLink.replace(regex, previousPageNumber));
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
        // setIframeVisible();
        createInitialButtonsRow(upperPageButtons);
        createNameBar(docName, upperNameBar);
        createTextarea();
        createTextareaContentEventHandlers(text);

        setPlusPageButtonBehavior();
        setMinusPageButtonBehavior();
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
            let upperPageButtons = container.find('#upper-page-buttons-row');
            let lowerPageButtons = container.find('#lower-page-buttons-row');
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

    function setPageButtonCss(currentPageButton, insertedPageButton) {
        currentPageButton.css("background", "rgb(246, 244, 246)");
        currentPageButton.mousedown(function () {$(this).css("background", "#efff00");});
        currentPageButton.mouseup(function () {$(this).css("background", "#ffdf00");});
        currentPageButton.hover(function () {$(this).css("background", "#8AB8CC");},
            function () {$(this).css("background", "rgb(246, 244, 246)");});

        insertedPageButton.css("background", "#ffdf00");
    }

// ----------------------------------------------------------------------------------------------------------------
// =========================================================================================== PERFORMING


    getSavedDocLinks();
    // setPlusPageButtonBehavior();
    $('#create-doc-btn').click(function (event) {
        event.preventDefault();
        createNewDoc();
    });
});
