function editInfo() {
    var all = $(".a-field");
    all.each(function () {
        $(this).children(".a-input").val( $(this).children(".a-value").text() );
        $(this).children(".a-value").addClass("hidden");
        $(this).children(".a-input").removeClass("hidden");
        $(this).children(".a-label").removeClass("hidden");
    });
    $("#edit-info").addClass("hidden");
    $("input[type='submit']").removeClass("hidden");
};

function editApartment(apartmentId) {
    $("#edit_actual").addClass("hidden");
    $("#edit_loading").removeClass("hidden");
    setTimeout(function(){
        $("#edit_actual").removeClass("hidden");
        $("#edit_loading").addClass("hidden");
    }, 500);
}

function viewApartmentMsg(apartmentId) {

}