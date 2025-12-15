package listener;

import com.example.talking_room.TalkingServlet;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class OnlineUserListener implements HttpSessionListener {
    // 存储：username -> 最后活跃时间戳（毫秒）
    private static final Map<String, Long> ONLINE_USERS = new ConcurrentHashMap<>();
    private static final long TIMEOUT_MS = 60_000; // 60秒超时（可调）

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String username = (String) se.getSession().getAttribute("username");
        if (username != null) {
            ONLINE_USERS.remove(username);
            TalkingServlet servlet = TalkingServlet.getInstance();
            if (servlet != null) {
                servlet.broadcastUserOffline(username);
            }
        }
    }

    // 登录时调用
    public static void addOnlineUser(String username) {
        ONLINE_USERS.put(username, System.currentTimeMillis());
    }

    // 前端心跳时调用（更新活跃时间）
    public static void updateActive(String username) {
        ONLINE_USERS.put(username, System.currentTimeMillis());
    }

    // 清理超时用户（由 TalkingServlet 定期调用）
    public static void cleanupExpired() {
        long now = System.currentTimeMillis();
        List<String> expiredUsers = new ArrayList<>();

        ONLINE_USERS.entrySet().removeIf(entry -> {
            boolean expired = (now - entry.getValue()) > TIMEOUT_MS;
            if (expired) {
                expiredUsers.add(entry.getKey());
            }
            return expired;
        });

        // 通知回调
        if (offlineCallback != null) {
            for (String user : expiredUsers) {
                offlineCallback.onUserOffline(user);
            }
        }
    }

    public static int getOnlineCount() {
        cleanupExpired(); // 每次获取前先清理
        return ONLINE_USERS.size();
    }

    public static Map<String, String> getOnlineUsers() {
        cleanupExpired(); // 清理后再返回
        Map<String, String> result = new ConcurrentHashMap<>();
        for (String user : ONLINE_USERS.keySet()) {
            result.put(user, user);
        }
        return result;
    }

    // 在 OnlineUserListener.java 中新增
    public interface UserOfflineCallback {
        void onUserOffline(String username);
    }

    private static UserOfflineCallback offlineCallback;

    public static void setUserOfflineCallback(UserOfflineCallback callback) {
        offlineCallback = callback;
    }
}