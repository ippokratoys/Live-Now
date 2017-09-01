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
            $("#dates_table").append(
                "<tr>" +
                "<td class='text-center'>"+"No dates yet :("+"</td>" +
                "<td class='text-center'>"+"Add one ASAP"+"</td>" +
                "</tr>");
        }else{
            var i;
            for(i=0;i<data.length;i++){
                $("#dates_table").append(
                    "<tr>" +
                    "<td class='text-center'>"+data[i].fromAv+"</td>" +
                    "<td class='text-center'>"+data[i].toAv+"</td>" +
                    "</tr>");
            }

        }
        $("#add_actual").removeClass("hidden");
        $("#add_loading").addClass("hidden");
    });
};

function editApartment(apartmentId) {
    cur_apartment_id=apartmentId;
    $("#edit_actual").addClass("hidden");
    $("#edit_loading").removeClass("hidden");
    $.getJSON("/profile/host/apartment/"+apartmentId,function (data) {
        if(data==="" || data===null){
            return ;
        }
        $.each( data, function( key, value ) {
            // alert(key.toString() + " " + value.toString());
            if(value===false || value===true){
                $("input[name='"+key.toString()+"']").prop("checked",value);
            }else if(key==="trasnportationDescription" || key==="houseDescription" || key==="rules"){
                $("textarea[name='"+key.toString()+"']").html(value);
            }else if(key==="type"){
            //    is the select menu
            //     $('select>option:eq(3)').prop('selected', true);
                $("input[name='"+key.toString()+"']").val(value);
            }else{
                $("input[name='"+key.toString()+"']").val(value);
            }
        });
        $("#edit_actual").removeClass("hidden");
        $("#edit_loading").addClass("hidden");
    });
}

function viewApartmentChats(apartmentId){
    $("#chats_actual").addClass("hidden");
    $("#chats_loading").removeClass("hidden");

    // $("#chats_actual").removeClass("hidden");
    // $("#chats_loading").addClass("hidden");

}