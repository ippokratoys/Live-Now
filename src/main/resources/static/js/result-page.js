/**
 * Created by thanasis on 5/8/2017.
 */
$( document ).ready(function() {
    update_image_desc();
    console.log( "ready!");
    $( ".hotel-link" ).hover(
        function() {
            $( this ).children(".caption").css("visibility","hidden");
        }, function() {
            $( this ).children(".caption").css("visibility","visible");
        }
    );
});

$( window ).resize(function() {
    update_image_desc();
});

function update_image_desc() {
    var imageWidth=$(".img-responsive").width();
    console.log(imageWidth);
    $(".caption").css("width",imageWidth);
}