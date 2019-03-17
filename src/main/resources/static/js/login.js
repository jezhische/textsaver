$(function () {

    let username = $('#username');
    let password = $('#password');
    let submit = $('#submit');
    let errormessage = $('#error-message');

// ------------------------------------------------------------------------------
    function extractUserData() {
        let data = {};
        data.username = username.val();
        data.password = password.val();
        return data;
    }
// ------------------------------------------------------------------------------

    function getJsonInputStream() {
        console.log('getJsonInputStream begins');

        let data = extractUserData();

        console.log(data);

        $.ajax({
            type: 'post',
            url: 'login',
//            contentType: 'application/json; charset=utf-8',
//            data: JSON.stringify(data),
            dataType: 'json',
            timeout: 10000,
            success: function (data, status, jqXHR) {
                            console.log(data);
//                if (data === null) errormessage.html('Login or Password invalid, please verify');
                window.location = '';
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('getJsonInputStream error; status = ' + jqXHR.status);
//                if(jqXHR.status !== 200)
                errormessage.html('Login or Password invalid, please verify');
//                else
//                window.location = '/textsaver';

            }
        });
    }
// ------------------------------------------------------------------------------

    function loginSubmit() {
        submit.click(function (event) {
//            event.preventDefault();
            console.log('submit button pushed');
            
            getJsonInputStream();
        });
    }
// -------------------------------------------------------------------------------- performance
    loginSubmit();

});