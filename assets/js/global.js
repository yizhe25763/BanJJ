$(document).ready(function() {
    var deviceHeight = $(window).height();
    $('.home-page').height(deviceHeight);
    $('.age-bar').css('top', deviceHeight);
    $(window).resize(function() {
        var deviceHeight = $(window).height();
        $('.home-page').height(deviceHeight);

    });
});

window.onload = function() {

    isImgLoad(function() {
        var deviceHeight = $(window).height();
        var windowScrollTop = $(window).scrollTop();
        var agebarHigeht = $('.age-bar').height();
        $('.bxslider li').css('padding-top', agebarHigeht);

        $(window).on('scroll', function() {
            var windowScrollTop = $(window).scrollTop();
            var agebarH = $('.age-bar').height();
            $('.age-bar').height(agebarH)

            if (windowScrollTop > deviceHeight) {
                $('.age-bar').addClass('fixed').css('top', 0);
                
                var bx_controls = $('.bx-controls-direction');
                var bx_controls_h = bx_controls.find('a').height();
                bx_controls.find('a').addClass('fixed').height(bx_controls_h);
            } else {
                $('.age-bar').removeClass('fixed').css('top', deviceHeight);
                var bx_controls = $('.bx-controls-direction');
                bx_controls.find('a').removeClass('fixed');
            }

            if (windowScrollTop > deviceHeight) {
                $('#order-btn').fadeIn(500);
            } else {
                $('#order-btn').fadeOut(500);
            }
        });

        $('.arrow-top').click(function() {
            $('html, body').animate({ scrollTop: deviceHeight }, 500);
        });
    });
    function judgeDevice() {
        if (/(iphone)/i.test(navigator.userAgent) || /(ipad)/i.test(navigator.userAgent)) {
            var allLink = document.querySelectorAll('a[onclick]');
            var iosFuncName = 'huodongClick';
            for (var i = 0; i < allLink.length; i++) {
                var funcObj = allLink[i].getAttribute('onclick');
                var splitObj = funcObj.split('(');
                var funcName = iosFuncName;

                for (var j = 1; j < splitObj.length; j++) {
                    allLink[i].setAttribute('onclick', funcName + '(' + funcObj.split('(')[j]);
                }
            }
        }
    }
    judgeDevice();
}

var t_img;
var isLoad = true;

function isImgLoad(callback) {
    $('img').each(function() {
        if(this.height === 0) {
            isLoad = false;
            return false;
        }
    });
    if(isLoad) {
        clearTimeout(t_img);
        callback();
    }else{
        isLoad = true;
        t_img = setTimeout(function() {
            isImgLoad(callback);
        }, 500);
    }
}

var ready;

ready = function() {
    setTimeout(function() {
        var loading = document.getElementById('loading');
        loading.style.opacity = 0;
        $('.home-page').show();
        setTimeout(function() {
            loading.parentNode.removeChild(loading);
            ready = function() {};
        }, 600);
    }, 500);
}
setTimeout(function() { ready() }, 1000);