var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#spot").html("");
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/spot', function (spot) {
            showSpot(JSON.parse(spot.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.unsubscribe();
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function startSpot() {
    stompClient.send("/ws/spot/start", {}, JSON.stringify({'currencyPair': $("#currencyPair").val()}));
}

function stopSpot() {
    stompClient.send("/ws/spot/stop", {}, JSON.stringify({}));
}

function showSpot(spot) {
    $("#spot").append("<tr><td>Ccy: " + spot.currencyPair + ", Bid: " + spot.bid + ", Ask: " + spot.ask + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(connect);
    $( "#disconnect" ).click(disconnect);
    $( "#start" ).click(startSpot);
    $( "#stop" ).click(stopSpot);
});

