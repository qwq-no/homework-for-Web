<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>聊天室</title>
    <style>
        .chat-container {
            width: 80%;
            margin: 20px auto;
            border: 1px solid #ddd;
            border-radius: 8px;
            overflow: hidden;
        }
        .messages {
            height: 400px;
            padding: 10px;
            background-color: #f9f9f9;
            overflow-y: auto; /* 滚动条 */
        }
        .message {
            margin: 8px 0;
            padding: 8px 12px;
            border-radius: 16px;
            max-width: 70%;
            clear: both;
        }
        .message.self {
            background-color: #0084ff;
            color: white;
            float: right;
        }
        .message.other {
            background-color: #e5e5e5;
            float: left;
        }
        .sender {
            font-size: 12px;
            opacity: 0.8;
            margin-bottom: 4px;
        }
        .msg-content {
            font-size: 14px;
            line-height: 1.4;
        }
        .time {
            font-size: 10px;
            opacity: 0.7;
            margin-top: 4px;
            text-align: right;
        }
        .input-area {
            display: flex;
            padding: 10px;
            border-top: 1px solid #ddd;
        }
        #messageInput {
            flex: 1;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 20px;
            outline: none;
        }
        #sendBtn {
            margin-left: 10px;
            padding: 8px 20px;
            background-color: #0084ff;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <div class="messages" id="messagesContainer"></div>
    <div class="input-area">
        <input type="text" id="messageInput" placeholder="输入消息...">
        <button id="sendBtn">发送</button>
    </div>
</div>
<script>
    let currentUser = "";  // 当前登录用户
    let lastMessageId = 0;  // 记录最后一条消息的ID（用于轮询）
    let pollInterval;  // 轮询定时器
    // 页面加载时初始化
    window.onload = function() {
        // 从Cookie获取当前用户
        const params = new URLSearchParams(window.location.search);
        currentUser = params.get("username");

        if (!currentUser) {
            // 未获取到用户名，跳转到登录页
            window.location.href = "login.html";
            return;
        }

        // 加载历史消息（首次加载所有消息）
        fetchNewMessages();
        // 启动轮询（每2秒获取一次新消息）
        pollInterval = setInterval(fetchNewMessages, 2000);

        // 发送消息事件
        document.getElementById('sendBtn').addEventListener('click', sendMessage);
        document.getElementById('messageInput').addEventListener('keypress', e => {
            if (e.key === 'Enter') sendMessage();
        });
    };

    // 轮询获取新消息
    function fetchNewMessages() {
        fetch(`talking_room?action=getNewMessages&lastId=${lastMessageId}`)
            .then(response => response.json())
            .then(messages => {
                if (messages.length > 0) {
                    let maxId = lastMessageId; // 临时变量记录最大ID
                    messages.forEach(msg => {
                        addMessageToUI(msg);
                        if (msg.id > maxId) {
                            maxId = msg.id; // 更新为当前消息的最大ID
                        }
                    });
                    lastMessageId = maxId; // 最终更新全局变量
                }
            })
            .catch(error => {
                console.error('获取消息失败:', error);
            });
    }


    function sendMessage() {
        const input = document.getElementById('messageInput');
        const content = input.value.trim();
        if (!content) return;

        // 使用URLSearchParams替代FormData，确保参数正确传递
        const params = new URLSearchParams();
        params.append('type', 'sendMessage');
        params.append('content', content);

        fetch('talking_room', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: params
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('网络响应异常');
                }
                return response.json(); // 直接解析JSON
            })
            .then(result => {
                if (result.success) {
                    document.getElementById('messageInput').value = '';
                } else {
                    alert('发送失败：' + result.message);
                }
            })
            .catch(error => {
                console.error('请求出错：', error);
            });
    }

    // 展示消息到界面
    const renderedMessageIds = new Set();
    function addMessageToUI(message) {
        const container = document.getElementById('messagesContainer');
        if (!container||renderedMessageIds.has(message.id)) {
            console.error("消息容器不存在！");
            return;
        }
        renderedMessageIds.add(message.id);
        const isSelf = message.username === currentUser;

        // 创建消息容器
        const msgDiv = document.createElement('div');
        msgDiv.className = `message ${isSelf ? 'self' : 'other'}`;

        // 手动创建子元素（避免innerHTML拼接问题）
        const senderDiv = document.createElement('div');
        senderDiv.className = 'sender';
        senderDiv.textContent = isSelf ? '我' : message.nickname;

        const contentDiv = document.createElement('div');
        contentDiv.className = 'msg-content';
        contentDiv.textContent = message.content;

        const timeDiv = document.createElement('div');
        timeDiv.className = 'time';
        timeDiv.textContent = message.time;

        // 组装消息元素
        msgDiv.appendChild(senderDiv);
        msgDiv.appendChild(contentDiv);
        msgDiv.appendChild(timeDiv);

        container.appendChild(msgDiv);
        container.scrollTop = container.scrollHeight; // 滚动到底部
    }

    // 页面关闭时清除轮询
    window.onbeforeunload = function() {
        clearInterval(pollInterval);
    };
</script>
</body>
</html>