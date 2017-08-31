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

function addDates(apartmentId) {
    cur_apartment_id=apartmentId;
    $("#add_actual").addClass("hidden");
    $("#add_loading").removeClass("hidden");
    $.getJSON("/profile/host/apartment/dates/"+apartmentId,function (data) {
        $("#dates_table").html("<tr>"+
            "<th class='text-center'>From</th>"+
            "<th class='text-center'>To</th>"+
            "</tr>");
        if(data=="" || data ==null){
            return ;
        }
        var i;
        for(i=0;i<data.length;i++){
            $("#dates_table").append(
                "<tr>" +
                "<td class='text-center'>"+data[i].fromAv+"</td>" +
                "<td class='text-center'>"+data[i].toAv+"</td>" +
                "</tr>");
        }
        $("#add_actual").removeClass("hidden");
        $("#add_loading").addClass("hidden");
    })
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