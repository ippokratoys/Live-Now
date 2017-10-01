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

function deletePhoto(photoId,apartmentId){
    var link = "/profile/host/delete_image?photoId="+photoId;
    $.getJSON(link,function (data) {
        if(data===true){
            alert("Photo Has been Deleted");
            editPhotos(apartmentId);
        }else{
            console.log(data.toString());
        }
    });

    console.log("deleted photoId:"+photoId.toString()+" of apartment id "+ apartmentId);
}

function editPhotos(apartmentId) {
    cur_apartment_id=apartmentId;
    $("#edit_photos").addClass("hidden");
    $("#photo_loading").removeClass("hidden");
    $.getJSON("/profile/host/apartment/images/"+apartmentId,function (data) {
        $("#photos_header").html("<h4>"+
            data.length+" Images"+
            "</h4>"
        );
        $("#apartmentId").val(apartmentId);
        $("#photos_table").html("<tr>"+
            "<th class='text-center'>Photo</th>"+
            "<th class='text-center'>Delete</th>"+
            "</tr>");
        if(data=="" || data ==null){
            $("#photos_table").append(
                "<tr>" +
                "<td class='text-center'>"+"No Photos yet :("+"</td>" +
                "<td class='text-center'>"+"Add one"+"</td>" +
                "</tr>");
        }else{
            var i;
            for(i=0;i<data.length;i++){
                $("#photos_table").append(
                    "<tr>" +
                    "<td class='text-center'>"
                      +"<img class=\"img-responsive\" src=\"/"+data[i].picturePath+"\" alt=\"\"/>"
                    +"</td>" +

                    "<td class='text-center'>"+
                      "<button type='button' class='btn btn-primary' onclick=\'deletePhoto("+data[i].imageId+","+apartmentId + ")'>Delete</button>"+
                    "</td>" +

                    "</tr>");
            }
        }
        $("#edit_photos").removeClass("hidden");
        $("#photo_loading").addClass("hidden");
    });
}

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

    $("#chat_div").addClass("hidden");
    $("#chats_table").addClass("hidden");

    $("#chats_loading").removeClass("hidden");
    var chatsUrl="/profile/host/apartment/chats/"+apartmentId;
    curChatId=-1;
    $.getJSON(chatsUrl,function (data) {
        $("#chats_table").html(
            "<tr>"+
            "<th class='text-center'>Message From</th>"+
            "</tr>");

        // alert(chatId);
        if(data==="" || data===null || data[0]===null || data[0]===""){
            alert("nothing");
            ;
        }else{
            // alert("somthing");
            var i;
            for(i=0;i<data.length;i++){
                $("#chats_table").append(
                    '<tr onclick="hostShowChat('+data[i].chatId +');">' +
                    '<td class="text-center">'+data[i].login.username+'</td>' +
                    '</tr>'
                );
            }
        }

        $("#chats_loading").addClass("hidden");
        $("#chats_table").removeClass("hidden");
    })

}

function userShowChat(chatId) {
    $("#chat_actual").addClass("hidden");
    $("#chat_loading").removeClass("hidden");
    $.getJSON("/profile/user/chat/messages/"+chatId,function (data) {
        // alert(chatId);
        resetChat();
        me={};
        you={};
        me.avatar = "";
        you.avatar = "";
        if(data==="" || data===null){
            return ;
        }
        curChatId=chatId;
        var i;
        for(i=0;i<data.length;i++){
            if(data[i].fromCustomer===true){
                insertChat("me",data[i].content);
            }else{
                insertChat("you",data[i].content);
            }
        }
        var d = $('#message-list');
        d.scrollTop(d.prop("scrollHeight"));
        $("#chat_actual").removeClass("hidden");
        $("#chat_loading").addClass("hidden");
    });
}

function hostShowChat(chatId) {
    $("#chat_div").addClass("hidden");
    $("#chats_table").addClass("hidden");

    $("#chats_loading").removeClass("hidden");

    $.getJSON("/profile/host/chats/messages/"+chatId,function (data) {
        // alert(chatId);
        resetChat();
        me={};
        you={};
        me.avatar = "";
        you.avatar = "";
        curChatId=chatId;
        if(data==="" || data===null){
            ;
        }else{
            var i;
            for(i=0;i<data.length;i++){
                if(data[i].fromCustomer===true){
                    insertChat("you",data[i].content);
                }else{
                    insertChat("me",data[i].content);
                }
            }
            var d = $('#message-list');
            d.scrollTop(d.prop("scrollHeight"));
        }

        $("#chats_table").addClass("hidden");

        $("#chats_loading").addClass("hidden");
        $("#chat_div").removeClass("hidden");
    });
}
function hostCloseChat(){
    $("#chats_table").removeClass("hidden");

    $("#chat_div").addClass("hidden");

}
/////////////
//chat code//
/////////////
// var me = {};
// me.avatar = "https://lh6.googleusercontent.com/-lr2nyjhhjXw/AAAAAAAAAAI/AAAAAAAARmE/MdtfUmC0M4s/photo.jpg?sz=48";
//
// var you = {};
// you.avatar = "https://a11.t26.net/taringa/avatares/9/1/2/F/7/8/Demon_King1/48x48_5C5.jpg";

function formatAMPM(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0'+minutes : minutes;
    var strTime = hours + ':' + minutes + ' ' + ampm;
    return strTime;
}

//-- No use time. It is a javaScript effect.
function insertChat(who, text, time = 0){
    var control = "";
    var date = formatAMPM(new Date());

    if (who == "me"){

        control = '<li style="width:100%">' +
            '<div class="msj macro">' +
            '<div class="avatar"><img class="img-circle" style="width:100%;" src="'+ me.avatar +'" /></div>' +
            '<div class="text text-l">' +
            '<p>'+ text +'</p>' +
            '<p><small>'+date+'</small></p>' +
            '</div>' +
            '</div>' +
            '</li>';
    }else{
        control = '<li style="width:100%;">' +
            '<div class="msj-rta macro">' +
            '<div class="text text-r">' +
            '<p>'+text+'</p>' +
            '<p><small>'+date+'</small></p>' +
            '</div>' +
            '<div class="avatar" style="padding:0px 0px 0px 10px !important"><img class="img-circle" style="width:100%;" src="'+you.avatar+'" /></div>' +
            '</li>';
    }
    setTimeout(
        function(){
            $("#message-list").append(control);
            var d = $('#message-list');
            d.scrollTop(d.prop("scrollHeight"));

        }, time);

}

function resetChat(){
    $("#message-list").empty();
}

function rateApartment(bookId, apartmentName) {
    $("#rate-name").text(apartmentName);
    $("input[name='book-id']").val(bookId);
    console.log("done");
    return ;
}
function rateHost(bookId, hostName) {
    $("#rate-host-name").text(hostName);
    $("input[name='book-id']").val(bookId);
    console.log("done host");
    return ;
}

//-- NOTE: No use time on insertChat.