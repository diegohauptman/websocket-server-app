
var echo_websocket;
var myIp;
getUserIP(function(ip){
    myIp = ip;
});
            function init() {
                output = document.getElementById("output");
            }
            
            function open_connection(){
                
                var connection_type = update_connection();
                var wsUri = "ws://85.214.108.235:8080/PuntoCeroServerMavenjdk10/endpoint/" + connection_type;
                //var wsUri = "ws://localhost:8080/PuntoCeroServerMavenjdk10/endpoint/" + connection_type;
                //var wsUri = "ws://localhost:8080/PuntoCeroServerMaven/endpoint/" + connection_type;
                console.log(wsUri);
                writeToScreen("Connecting to " + wsUri);
                echo_websocket = new WebSocket(wsUri);
                echo_websocket.onopen = function (evt) {
                    writeToScreen("Connected !");
                    
                };
                
            }
            
            function update_connection(){
                var connection_type = connectiontype.value;
                return connection_type;
                
            }
            
            function close_connection(){
                writeToScreen("Cerrando sesión...");
                echo_websocket.close();
                
            }

            function send_echo() {
                
                //doSend(textID.value);
                doSendJson(textID.value);
                
                echo_websocket.onmessage = function (evt) {
                    writeToScreen('<span style = "color: blue;">Server response: </span><br>' + evt.data);
                    //echo_websocket.close();
                };
                echo_websocket.onerror = function (evt) {
                    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
                    //echo_websocket.close();
                }; 
            }

            function doSend(message) {
                echo_websocket.send(message);
                writeToScreen('<span style = "color: green;">Client message: </span><br>' + message);
            }
            
            function doSendJson(message){
            	
                var json = {
                    text: message,
                    mac: "not possible to get mac with javascript",
                    ip: myIp
                };
                message = JSON.stringify(json);
                echo_websocket.send(message);
                writeToScreen('<span style = "color: green;">Client message: </span><br>' + message);
                
            }

            function writeToScreen(message) {
                var pre = document.createElement("p");
                pre.style.wordWrap = "break-word";
                pre.innerHTML = message;
                output.appendChild(pre);
            }
            
            window.addEventListener("load", init, false);