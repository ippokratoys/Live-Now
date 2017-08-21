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
}