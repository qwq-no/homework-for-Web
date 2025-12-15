<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>聊天室 - ${sessionScope.username}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: "Microsoft YaHei", sans-serif;
            background: #f5f5f5;
            display: flex;
            height: 100vh;
        }
        #onlinePanel {
            width: 200px;
            background: #fff;
            border-right: 1px solid #ddd;
            padding: 10px;
            display: flex;
            flex-direction: column;
        }
        #onlinePanel h3 {
            margin-bottom: 10px;
            font-size: 16px;
            color: #333;
        }
        #onlineCount {
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
        }
        #onlineList {
            flex: 1;
            overflow-y: auto;
        }
        #chatArea {
            flex: 1;
            display: flex;
            flex-direction: column;
            padding: 15px;
        }
        #messageList {
            flex: 1;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 10px;
            overflow-y: auto;
            margin-bottom: 10px;
        }
        .sender {
            font-weight: bold;
            color: #2c6fb3;
        }
        .content {
            margin: 4px 0;
            word-break: break-word;
        }
        .time {
            font-size: 12px;
            color: #888;
        }
        .to {
            font-size: 12px;
            color: #555;
            margin-top: 2px;
        }
        #inputArea {
            display: flex;
            gap: 8px;
        }
        #messageInput {
            flex: 1;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        #sendBtn {
            padding: 8px 16px;
            background: #2c6fb3;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        #sendBtn:hover {
            background: #1e5a99;
        }
    </style>
</head>
<body>

<div id="onlinePanel">
    <h3>在线用户</h3>
    <div id="onlineCount">加载中...</div>
    <div id="onlineList"></div>
</div>

<div id="chatArea">
    <div id="messageList"></div>
    <div id="inputArea">
        <div id="sendTarget">发送给：<strong>所有人</strong></div>
        <input type="text" id="messageInput" placeholder="输入消息，回车发送" maxlength="200" />
        <button id="sendBtn" onclick="sendMessage()">发送</button>
    </div>
</div>

<script>
    const CTX = "${pageContext.request.contextPath}";
    const CURRENT_USERNAME = "${sessionScope.username}";
    let lastMessageId = 0;
    let selectedReceivers = [];

    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    function loadOnlineUsers() {
        fetch(CTX + "/talking_room?action=getOnlineUsers")
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    document.getElementById("onlineCount").textContent = data.onlineCount + " 人在线";

                    const listEl = document.getElementById("onlineList");
                    listEl.innerHTML = "";
                    for (const username in data.users) {
                        if (!data.users.hasOwnProperty(username)) continue;
                        if (username === CURRENT_USERNAME) continue;

                        const userDiv = document.createElement("div");
                        userDiv.className = "online-user";
                        userDiv.textContent = username;
                        userDiv.dataset.username = username;
                        userDiv.onclick = function () {
                            toggleSelect(this, username);
                        };
                        listEl.appendChild(userDiv);
                    }
                } else {
                    console.error("获取在线用户失败:", data.message);
                }
            })
            .catch(err => console.error("加载在线用户出错:", err));
    }
    function updateSendTarget() {
        const targetEl = document.getElementById("sendTarget");
        if (selectedReceivers.length === 0) {
            targetEl.innerHTML = "发送给：<strong>所有人</strong>";
        } else if (selectedReceivers.length === 1) {
            targetEl.innerHTML = "发送给：<strong>" + escapeHtml(selectedReceivers[0]) + "</strong>";
        } else {
            targetEl.innerHTML = "发送给：<strong>" + escapeHtml(selectedReceivers.join(", ")) + "</strong>";
        }
    }
    //告诉服务器该用户还在
    function sendHeartbeat() {
        fetch(CTX + "/talking_room", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "type=heartbeat"
        }).catch(err => console.warn("心跳失败:", err));
    }
    if (CURRENT_USERNAME) {
        sendHeartbeat(); // 立即发一次
        setInterval(sendHeartbeat, 10000); // 每10秒一次
    }

    function toggleSelect(element, username) {
        const index = selectedReceivers.indexOf(username);
        if (index > -1) {
            selectedReceivers.splice(index, 1);
            element.classList.remove("selected");
        } else {
            selectedReceivers.push(username);
            element.classList.add("selected");
        }
        updateSendTarget();
    }

    // ========== 拉取新消息 ==========
    function fetchNewMessages() {
        fetch(CTX + "/talking_room?action=getNewMessages&lastId=" + lastMessageId)
            .then(async res => {
                const text = await res.text();
                try {
                    const messages = JSON.parse(text);
                    if (!Array.isArray(messages)) return;

                    const listEl = document.getElementById("messageList");
                    messages.forEach(msg => {
                        const div = document.createElement("div");
                        div.className = "message";

                        let toText = "发给：所有人";
                        if (msg.receiveDesc !== "所有人") {
                            const receivers = msg.receiveDesc.split(",");
                            if (receivers.includes(CURRENT_USERNAME)) {
                                toText = "私信给你";
                            } else {
                                toText = "发给：" + msg.receiveDesc;
                            }
                        }

                        div.innerHTML =
                            '<div class="sender">' + escapeHtml(msg.username) + '</div>' +
                            '<div class="content">' + escapeHtml(msg.content) + '</div>' +
                            '<div class="time">' + escapeHtml(msg.time) + '</div>' +
                            '<div class="to">' + escapeHtml(toText) + '</div>';

                        listEl.appendChild(div);
                        if (msg.id > lastMessageId) {
                            lastMessageId = msg.id;
                        }
                    });

                    listEl.scrollTop = listEl.scrollHeight;
                } catch (e) {
                    console.error("解析消息失败，返回内容:", text);
                }
            })
            .catch(err => console.error("拉取消息失败:", err));
    }

    // ========== 发送消息 ==========
    function sendMessage() {
        const content = document.getElementById("messageInput").value.trim();
        if (!content) {
            alert("消息内容不能为空！");
            return;
        }

        const receiveUsers = selectedReceivers.length > 0
            ? selectedReceivers.join(",")
            : "all";

        fetch(CTX + "/talking_room", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "type=sendMessage&content=" + encodeURIComponent(content) + "&receiveUsers=" + encodeURIComponent(receiveUsers)
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    document.getElementById("messageInput").value = "";
                    selectedReceivers = [];
                    document.querySelectorAll(".online-user.selected").forEach(el => el.classList.remove("selected"));
                } else {
                    alert("发送失败：" + data.message);
                }
            })
            .catch(err => {
                console.error("发送消息出错:", err);
                alert("网络错误，请重试");
            });
        updateSendTarget();
    }

    // 回车发送
    document.getElementById("messageInput").addEventListener("keypress", function(e) {
        if (e.key === "Enter") {
            sendMessage();
        }
    });

    // 初始化
    loadOnlineUsers();
    fetchNewMessages();
    setInterval(function() {
        fetchNewMessages();
        loadOnlineUsers();
    }, 2000);
</script>

</body>
</html>