/**
 * Created by thanasis on 5/8/2017.
 */
$( document ).ready(function() {
    update_image_desc();
    console.log( "ready!");
    $( ".hotel-link" ).hover(
        function() {
            $( this ).children(".caption").css("visibility","hidden");
            $( this ).children(".caption-top").css("visibility","hidden");
        }, function() {
            $( this ).children(".caption").css("visibility","visible");
            $( this ).children(".caption-top").css("visibility","visible");
        }
    );
    filterBar();
});

$( window ).resize(function() {
    update_image_desc();
});

function update_image_desc() {
    var imageWidth=$(".img-responsive").width();
    console.log(imageWidth);
    $(".caption").css("width",imageWidth);
    $(".caption-top").css("width",imageWidth);
    $(".caption-top").css("width",imageWidth);

    // wid=$(".filters-btn-text").width();
    // hei=$(".filters-btn-text").height();
    // alert(wid);
    // alert(hei);
    // $("#filter-btn").css("width",wid);
    // $("#filter-btn").css("height",hei);
}


// nav bar js

function filterBar(){
    var trigger = $('.hamburger'),
        overlay = $('.overlay'),
        isClosed = false;

    trigger.click(function () {
        hamburger_cross();
    });

    function hamburger_cross() {

        if (isClosed == true) {
            overlay.hide();
            trigger.removeClass('is-open');
            trigger.addClass('is-closed');
            isClosed = false;
            $('.filters-btn-text').html('filters');
        } else {
            overlay.show();
            trigger.removeClass('is-closed');
            trigger.addClass('is-open');
            $('.filters-btn-text').html('close');
            isClosed = true;
        }
    }

    $('[data-toggle="offcanvas"]').click(function () {
        $('#wrapper').toggleClass('toggled');
    });
}

function clearFilters(){
    $('input').value = "";
}