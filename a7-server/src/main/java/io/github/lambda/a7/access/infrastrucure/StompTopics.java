package io.github.lambda.a7.access.infrastrucure;

public class StompTopics {
    /** implicitly, unicast toics have `/app` as the prefix */
    public static final String BROADCAST_ACCESS_COUNT = "/topic/access.count";
    public static final String UNICAST_ACCESS_COUNT = "/access.count";
    public static final String UNICAST_WEBSOCKET_COUNT = "/websocket.count";
    public static final String BROADCAST_WEBSOCKET_COUNT = "/topic/websocket.count";
    public static final String UNICAST_SESSION_COUNT = "/session.count";
    public static final String BROADCAST_SESSION_COUNT = "/topic/session.count";

    public static final String AMQP_WEBSOCKET_SUBSCRIPTION = BROADCAST_WEBSOCKET_COUNT.substring(7);
}
