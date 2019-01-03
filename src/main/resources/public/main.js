
$(function () {
// ================================================================================ MAIN BUSINESS LOGIC
    let checkSum = 0;
    let currentPageNumber = 0;
    let currentPageLink = '';
    let currentDocName = '';
    let createdDate = '';
    let updatedDate = '';
    let totalPages = 0;
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
                /* NB: the "href" attribute will be set by the following function for each pageLink */
                getDocLinksSortedByNameAndCreatedDate(obtainedData);
                /* set behavior of each elem 'a' in the '#docLinks' context */
                setLinksOnclickBehavior($('a', '#docLinks'));
            },
            error: function () {
                // TODO: make error handling
                alert('error in getSavedDocLinks()');
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
            error: function () {
                console.log('error message: this.url = ' + this.url);
                // TODO: make error handling
                alert('error');
            }
        });
                                    console.log('createNewDoc() end ---');
    }

// --------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------- GET and RENDER current page
// --------------------------------------------------------------------------------------------------------------------

function extractPageContent(link) {
                                                console.log('extractPageContent() begin ---------------------------');
                                                console.log('extractPageContent() pageLink =  ' + link);
    $.ajax({
        type: 'GET', // http://localhost:8074/textsaver/doc-data/837/pages?page=1
        url: link,
        dataType: 'json', // returns PageResource instance
        success: function (data, status, jqXHR) {
                                                    console.log('extractPageContent(): success');
            totalPages = data.totalPages;
            // currentPageNumber = data.pageNumber;
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
            // setInsertPageButtonBehavior(data.pageNumber);
        },
        error: function () {
            alert('error in extractPageContent')
        }
    });
                                        console.log('extractPageContent() end ---');
}

// --------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    function extractBookmarks(currentPageLink) {
        let parcell = {"pageNumber":currentPageNumber, "totalPages":totalPages};
        // parcell["pageNumber"] = currentPageNumber;
        // parcell["totalPages"] = totalPages;
        console.log('FROM EXTRACTBOOKMARKS parcell.pageNumber = ' + parcell.pageNumber + ', parcell["totalPages"] = ' + parcell["totalPages"]);
        console.log('extractBookmarks() begin ---------------------------');
        $.ajax({
            type: 'POST',
            url: getBookmarksLink(currentPageLink), // http://localhost:8074/textsaver/doc-data/1733/bookmarks
            contentType: "application/hal+json; application/json; charset=utf-8",
            data: JSON.stringify({"pageNumber":currentPageNumber, "totalPages":totalPages}),
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                                            console.log('EXTRACTBOOKMARKS obtainedData.color = ' + obtainedData.color +
                                            ', obtainedData.pageLink = ' + obtainedData.pageLink +
                                            ', obtainedData.pageNumber = ' + obtainedData.pageNumber);

                // let bookmarksArray = obtainedData
                resetPageNmBtns();
            },
            error: function () {
                // TODO: make error handling
                alert('error in extractBookmarks()');
            }
        });


        console.log('extractBookmarks() end ---');



    }

// --------------------------------------------------------------------------------------------------------------------

    function resetPageNmBtns(bookmarksArray) {

    }

// --------------------------------------------------------------------------------------------------------------------
// -------------------------------------------------------
// --------------------------------------------------------------------------------------------------------------------

    function setBookmarks(link) {
        console.log('setBookmarks() begin ---------------------------');
        console.log('setBookmarks() end ---');

    }

// -------------------------------------------------------------------------------------------------------------------

    function updateBookmarks(link) {
        console.log('updateBookmarks() begin ---------------------------');
        console.log('updateBookmarks() begin ---');

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

        /* insert page number button element after "currentPageButton" element. This button will get the necessary
        properties in the insertPage() method if success, or will be removed if error */
        $('<button id="' + insertedPageNm + '" formaction="' + insertedPageLink + '" class="page-number-button" disabled>'
            + insertedPageNm + '</button>').insertAfter(currentPageButton);
        let insertedPageButton = form.find('#' + insertedPageNm);

        currentPageButton.prop('disabled', false);
        setPageButtonCss(currentPageButton, insertedPageButton);
        currentPageNumber = insertedPageNm;
        currentPageLink = insertedPageLink;

        // if (totalPages > currentPageNumber) {
        //     let nextPagesArray = form.find('button').get().slice(currentPageNumber + 1, totalPages);
        //                                                 console.log('insertPage(): totalPages = ' + totalPages +
        //                                                     ', currentPageNumber = ' + currentPageNumber);
        //
        //     nextPagesArray.forEach(elem => {
        //         let element = $(elem);
        //     });
        //     nextPagesArray.forEach(arr => console.log(arr));
        // }

                                            console.log('insertPage(' + pageNm + ') insertedPageLink = ' + insertedPageLink);
            $.ajax({
            type: 'POST', // http://localhost:8074/textsaver/doc-data815/pages?page=25
            contentType: "application/json; charset=utf-8",
            url: insertedPageLink,
            // data: JSON.stringify(docName),
            dataType: 'json',
            success: function (obtainedData, status, jqXHR) {
                                            console.log('insertPage(' + pageNm + ') status is ' + status, 'insertedPageNm = ' + insertedPageNm);
                currentPageNumber = insertedPageNm;
                currentPageLink = insertedPageLink;
                                            console.log('currentPageNumber = ' + currentPageNumber + ', currentPageLink = ' + currentPageLink);
                setPageNumberButtonBehavior(insertedPageLink, insertedPageNm);
                checkSum = 0;
                totalPages++;
                // setInsertPageButtonBehavior(newPageNm);
                $('#text').val('');
                setBookmarks(currentPageLink);
            },
            error: function () {
                console.log('error message: this.url = ' + this.url);
                // TODO: make error handling
                alert('error in function insertPage(pageNm)');
                $('#' + (currentPageNumber + 1)).remove();
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
            // let regex = new RegExp('(\n)');
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
                    checkSum = currentPageCheckSum;
                },
                error: function () {
                    alert('error updating page ' + link);
                }
            });
        } else console.log('updatePage(): checkSum = ' + checkSum + ', there was nothing changed')
                                            console.log('updatePage() end ---');
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
                setPageNumberButtonBehavior(currentPageLink, 1);
                extractPageContent(currentPageLink);
                                        console.log('setLinksOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);

                // setPlusPageButtonBehavior(); // in setMarkup()
                setInsertPageButtonBehavior();
                extractBookmarks(currentPageLink);
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
                setPageNumberButtonBehavior(currentPageLink, 1);
                extractPageContent(currentPageLink);
                                        console.log('setNewDocLinkOnclickBehavior - after setting: currentPageLink = ' + currentPageLink);
                extractBookmarks(currentPageLink);
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

        let currentPageNmBtn = $('#' + currentPageNumber);
        let pageNmButton = $('#' + pageNm);

        pageNmButton.click(function (event) {
            event.preventDefault();
                                                    console.log('page ' + pageNm + ' clicked');
                                                    console.log('setPageNumberButtonBehavior(): currentPageNumber = '
                                                        + currentPageNumber + ', currentPageLink = ' + currentPageLink);

            pageNmButton.attr('formaction', pageLink);
            $('#' + currentPageNumber).prop('disabled', false);
            pageNmButton.prop('disabled', true);
            updatePage(currentPageLink);

            currentPageNumber = pageNm;
            currentPageLink = pageLink;
            extractPageContent(currentPageLink); // checkSum will be updated here
                                            console.log('setPageNumberButtonBehavior() after click: currentPageNumber = '
                                                + currentPageNumber + ', currentPageLink = ' + currentPageLink);
            setPageButtonCss(currentPageNmBtn, pageNmButton);
            setBookmarks(currentPageLink);
        });
                                            console.log('setPageNumberButtonBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------
    // нужно задавать только в 2 случаях: когда создается новый документ; и когда открывается сохраненный документ
    // с помощью линка.
    function setInsertPageButtonBehavior() {
                                    console.log('setInsertPageButtonBehavior() begin ---------------------------');
                                    console.log('setInsertPageButtonBehavior()  currentPageNm = ' + currentPageNumber);
                                    console.log('setInsertPageButtonBehavior()  currentPageLink = ' + currentPageLink);
        let form = $('#container').find('#upper-doc-bar').find('#upper-page-buttons-row').find('.page-btn-bar');
        let insertPageButton = form.find('#insert-page');
        // let totalPageNm = form.find('.page-number-button:last').html(); // fixme: брать из тотальной переменной

                                                    // console.log('@@@@@@@@@@@@@@@@@@@@@@@' + totalPageNm);
                                                    // console.log('@@@@@@@@@@@@@@@@@@@@@@@' + currentPageNm);
        insertPageButton.click(function (event) {
            event.preventDefault();
            insertPage(currentPageNumber);
            // the following actions was moved to insertPage() method:
            // currentPageNumber = insertedPageNm;
            // currentPageLink = insertedPageLink;
            // setBookmarks();
        });
                                            console.log('setInsertPageButtonBehavior() end ---');
    }

// ------------------------------------------------------------------------------------------------------------

    function setPlusPageButtonBehavior() {
                                    console.log('setPlusPageButtonBehavior() begin ---------------------------');


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
                    extractPageContent(insertedPageLink);
                    /* if desired button doesn't exist, create it, else update textarea content, button css and disability */
                    if (insertedPageButton.html() === undefined) {
                        $('<button id="' + insertedPageNm + '" formaction="' + insertedPageLink + '" class="page-number-button" disabled>'
                            + insertedPageNm + '</button>').insertAfter(currentPageButton);
                        currentPageButton.prop('disabled', false);
                        setPageNumberButtonBehavior(insertedPageLink, insertedPageNm); // I can use currentPageLink and currentPageNumber,
                        // 'cause these two variables must be changed to now with extractPageContent(), but as js is asynchronous,
                        // it has no guarantees
                        // setPageButtonCss(currentPageButton, insertedPageButton);

                    } else {
                        currentPageButton.prop('disabled', false);
                        form.find('#' + insertedPageNm).prop('disabled', true);
                        // setPageButtonCss(currentPageButton, insertedPageButton);
                    }
                    setPageButtonCss(currentPageButton, insertedPageButton);
                    currentPageNumber = insertedPageNm;
                    currentPageLink = insertedPageLink;
                }
            });
        // }
                                    console.log('setPlusPageButtonBehavior() end ---');

    }

// ------------------------------------------------------------------------------------------------------------

    function getNextPageLink(currentPageLink, currentPageNm) {
                                    console.log('getNextPageLink() begin ---------------------------');

        let regex = new RegExp('(' + currentPageNm + '$)');
        let nextPageNumber = currentPageNm + 1;
                                    console.log('nextPageNumber = ' + nextPageNumber);
                                    console.log('regex: ' + currentPageLink.replace(regex, nextPageNumber));
        return currentPageLink.replace(regex, nextPageNumber);
    }

// ------------------------------------------------------------------------------------------------------------

    function getBookmarksLink(currentPageLink) {
        let regex = new RegExp('pages[\?]page=[\\d]*$');
        return currentPageLink.replace(regex, 'bookmarks');
    }

// ------------------------------------------------------------------------------------------------------------

    // /** create a row of buttons with pages references */
    // function createPageButtonRow(obtainedData, elemId) {
    //     let bookmarkResources = obtainedData.bookmarkResources;
    //     bookmarkResources.forEach(function (item, i, arr) {
    //         $('#' + elemId).append(
    //             '<button type="button" id="p'+ item.pageNumber + '" class="page-button" >' + item.pageNumber + '</button>');
    //         let pbutton = $('#p' + item.pageNumber);
    //         pbutton.click(function () {
    //             extractPage(item.pageLink);
    //         });
    //         /** the button with current page reference must be disabled */
    //         if (item.pageNumber === obtainedData.pageNumber) pbutton.prop('disabled', true);
    //     });
    // }

// ------------------------------------------------------------------------------------------------------------

    // function resetGlobalData(currentDocNm, currentPageLnk) {
    //     // let checkSum = 0;
    //     // let currentPageNumber = 0;
    //     // let currentPageLink = '';
    //     // let currentDocName = '';
    //     // let createdDate = '';
    //     // let updatedDate = '';
    //     currentDocName = currentDocNm;
    //     currentPageLink = currentPageLnk;
    //
    //
    // }

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
                        lowerPageButtons.html(upperPageButtons.html());
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
