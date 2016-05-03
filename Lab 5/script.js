var msgList = [];

function run() {
    var appContainer = document.getElementsByClassName('todos')[0];
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('keydown', delegateEvent);

    msgList = loadMsgs() || [];
    renderLocalFiles(msgList);
    document.getElementById('chat').scrollTop = document.getElementById('chat').scrollHeight - document.getElementById('chat').clientHeight;
}

function loadMsgs() {
    if (typeof(Storage) == "undefined") {
        alert("cant access localStorage");
        return;
    }
    var item = localStorage.getItem("MessageList");
    return item && JSON.parse(item);
}

function delegateEvent(evtObj){

   if (evtObj.type == 'click' && evtObj.target.id =='rename') {
    renameButton(evtObj.target.id.substring(12), evtObj);
}
if (evtObj.type == 'click' && evtObj.target.id =='addButton' || evtObj.keyCode == '13' ) {
    sendMessage(evtObj);
    document.getElementById('chat').scrollTop = document.getElementById('chat').scrollHeight - document.getElementById('chat').clientHeight;
}
if(evtObj.type == 'click' && evtObj.target.id.substring(0,8) == 'btn-edit'){
    onEditMessage(evtObj.target.id.substring(8), evtObj);
}

if(evtObj.type == 'click' && evtObj.target.id.substring(0,10) == 'btn-delete'){
    onDeleteMessage(evtObj.target.id.substring(10), evtObj);
}
}
function renameButton(messageID) {
  var name = document.getElementById('nickName', document.getElementById("nickName").innerHTML).value;
  var newName = prompt("Enter your nick:", '');
  if(newName.length > null){
   document.getElementById('nickName').innerHTML = newName;
}
else{
   alert("Unacceptable nick name!");
}
}
function getCharCode(event) {
    return null === event.which
    ? event.keyCode
    : !(0 === event.which || 0 === event.charCode) ? event.which : -1;
}

function createButton(name, clName){
    var button = document.createElement('button');
    button.classList.add('pull-right');
    button.background = name;
    button.classList.add(name);
    button.id = clName;
    return button;
}

function sendMessage(event){
    var text = document.getElementById('shadyText').value;
    var author = document.getElementById('nickName').innerHTML;

    if(text.length > 0){
        var message = createMessage(author,text);
        var msgArea = document.getElementById('chat');
        msgArea.appendChild(message);
    }
    event.preventDefault();
    document.getElementById('shadyText').value = '';

}
function createMessage(author, text){
    //var day = new Date();
    //var  month = new Array("January", "February", "March","April","May", "June", "July", "August","September","October","November","December"); 
    //var time = day.getDate() + " " + month[day.getMonth()] + "   " + day.getFullYear() + "    "+ day.getHours() +":"+day.getMinutes();

    var msgId = uniqueId();
    var divItem = document.createElement('div');
    divItem.classList.add('message_box');
    divItem.id = 'mb' + msgId;
    var auth = document.createElement('div');
    auth.classList.add('author');
    auth.id = 'au'+ msgId;
    auth.appendChild(document.createTextNode(author));
    divItem.appendChild(auth);
    var t = document.createElement('div');
    t.classList.add('text');
    t.id = 't' + msgId;
    t.appendChild(document.createTextNode(text));
    divItem.appendChild(t);

    var buttonEdit = createButton('button_rewrite','btn-edit' + msgId);
    var buttonDelete = createButton('button_delete','btn-delete' + msgId);
    divItem.appendChild(buttonDelete);
    divItem.appendChild(buttonEdit);
    var timeShow = document.createElement('span');
    //timeShow.classList.add('time');
    //timeShow.id = 'data' + msgId;
    //timeShow.innerHTML = time;
    //divItem.appendChild(timeShow);
    var msg = msgLocal(text, author, msgId);
    msgList.push(msg);
    saveMsgs(msgList);
    return divItem;
}

function onDeleteMessage(messageID){
    for (var i = 0; i < msgList.length; i++) {
        if ((msgList[i].ide == messageID)&&(!msgList[i].deleted)) {
            msgList[i].deleted = !msgList[i].deleted;
        }
    }
    
saveMsgs(msgList);
document.getElementById('chat').innerHTML = '';
renderLocalFiles(msgList);
}


function onEditMessage(messageID){
    var newMsg = prompt("Enter new message", document.getElementById('t' + messageID).innerText);
    if(newMsg.length > 0){
        for (var i = 0; i < msgList.length; i++) {
            if (msgList[i].ide ==  messageID) {
                msgList[i].texter = newMsg;
                if (!msgList[i].edited) {
                    msgList[i].edited = !msgList[i].edited;
                }
            }
        }
        saveMsgs(msgList);
        document.getElementById('chat').innerHTML = '';
        renderLocalFiles(msgList);
    }
}

function msgDeleted(element){

    var msgId = element.ide;

    var divItem = document.createElement('div');
    divItem.classList.add('del_message');
    divItem.id = 'mb' + msgId;
    var warning = document.createElement('div');
    warning.classList.add('delMesInf');
    warning.id = 'dmi'+ msgId;
    warning.innerHTML = "Message is no longer available!";
    divItem.appendChild(warning);
    return divItem;
}

function msgNotDeleted(element){
    var msgId = element.ide;
    var divItem = document.createElement('div');
    divItem.classList.add('message_box');
    divItem.id = 'mb' + msgId;
    var auth = document.createElement('div');
    auth.classList.add('author');
    auth.id = 'au'+ msgId;
    auth.appendChild(document.createTextNode(element.author));
    divItem.appendChild(auth);
    var t = document.createElement('div');
    t.classList.add('text');
    t.id = 't' + msgId;
    t.appendChild(document.createTextNode(element.texter));
    divItem.appendChild(t);
    
    var buttonEdit = createButton('button_rewrite','btn-edit' + msgId);
    var buttonDelete = createButton('button_delete','btn-delete' + msgId);
    divItem.appendChild(buttonDelete);
    divItem.appendChild(buttonEdit);
    if (element.edited) {
        var mod = addIcon('modif', 'm' + msgId);
        divItem.appendChild(mod);
    }
    var timeShow = document.createElement('span');
    //timeShow.classList.add('time');
    //timeShow.id = 'data' + msgId;
    //timeShow.appendChild(document.createTextNode(element.timer));
    //divItem.appendChild(timeShow);
    return divItem;
}

function addIcon(name, clName){
    var icon = document.createElement('a');
    icon.classList.add('pull-right');
    icon.classList.add(name);
    icon.id = clName;
    return icon;
}

function uniqueId() {
    var date = Date.now();
    var random = Math.random() * Math.random();
    return Math.floor(date * random).toString();
}

function renderLocalFiles(list) {
    document.getElementById('nickName').innerHTML = list[list.length - 1].author;
    for (var i = 0; i < list.length; i++) {
        renderMsg(list[i]);
    }
}

function renderMsg(element) {
   if(element.deleted) {
    var divItem = msgDeleted(element);
    document.getElementById('chat').appendChild(divItem);
} else {
    var divItem = msgNotDeleted(element);
    document.getElementById('chat').appendChild(divItem);
}
}

function saveMsgs(listSave) {
    if (typeof(Storage) == "undefined") {
        alert("cant access localStorage");
        return;
    }
    localStorage.setItem("MessageList", JSON.stringify(listSave));
}


function msgLocal(text, auth, ID, edited, deleted) {
    return {
        texter: text,
        author: auth,
        //timer: time,
        ide: ID,
        edited: !!edited,
        deleted: !!deleted
    }
}
