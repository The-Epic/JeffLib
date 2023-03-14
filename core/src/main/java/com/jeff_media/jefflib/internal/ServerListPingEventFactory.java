package com.jeff_media.jefflib.internal;

import com.jeff_media.jefflib.ReflUtils;
import com.jeff_media.jefflib.internal.annotations.Internal;
import com.jeff_media.jefflib.internal.annotations.Paper;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Constructor;
import java.net.InetAddress;

/**
 * Factory for {@link ServerListPingEvent}, which is required because Paper again broke compatibility with Spigot, without even adjusting their javadocs.
 */
@SuppressWarnings("unchecked")
@Internal
public class ServerListPingEventFactory {

    private static final ServerListPingEventConstructorInvoker CONSTRUCTOR_INVOKER;

    static {
        Constructor<ServerListPingEvent> constructor;

        findCoonstructor:
        {
            // Try the proper constructor as declared in Bukkit
            constructor = getProperConstructor();
            if (constructor != null) {
                CONSTRUCTOR_INVOKER = new ProperServerListPingEventConstructorInvoker(constructor);
                break findCoonstructor;
            }

            // Try the corrupted Paper constructor
            constructor = getCorruptedConstructor();
            if (constructor != null) {
                CONSTRUCTOR_INVOKER = new CorruptedServerListPingEventConstructorInvoker(constructor);
                break findCoonstructor;
            }

            // Couldn't find any constructor
            throw new RuntimeException("Couldn't find any constructor for ServerListPingEvent");
        }
    }

    /**
     * Creates a new ServerListPingEvent
     * @param hostname The hostname
     * @param address The address
     * @param motd The MOTD
     * @param shouldSendChatPreviews Whether chat previews should be sent
     * @param numPlayers The number of online players
     * @param maxPlayers The maximum number of players
     * @return The new ServerListPingEvent
     */
    @Contract("_, _, _, _, _, _ -> new")
    public static ServerListPingEvent createServerListPingEvent(String hostname, InetAddress address, String motd, boolean shouldSendChatPreviews, int numPlayers, int maxPlayers) {
        return CONSTRUCTOR_INVOKER.create(hostname, address, motd, shouldSendChatPreviews, numPlayers, maxPlayers);

    }

    /**
     * Gets the proper constructor for ServerListPingEvent as declared in Bukkit API
     *
     * @return The constructor
     */
    private static Constructor<ServerListPingEvent> getProperConstructor() {
        return (Constructor<ServerListPingEvent>) ReflUtils.getConstructor(ServerListPingEvent.class, String.class, InetAddress.class, String.class, int.class, int.class);
    }

    /**
     * Gets Paper's corrupted constructor for ServerListPingEvent which breaks compatibility with Bukkit API
     *
     * @return The corrupted constructor
     */
    @Paper
    private static Constructor<ServerListPingEvent> getCorruptedConstructor() {
        return (Constructor<ServerListPingEvent>) ReflUtils.getConstructor(ServerListPingEvent.class, String.class, InetAddress.class, boolean.class, String.class, int.class, int.class);
    }

    private interface ServerListPingEventConstructorInvoker {
        ServerListPingEvent create(String hostname, InetAddress address, String motd, boolean shouldSendChatPreviews, int numPlayers, int maxPlayers);
    }

    @RequiredArgsConstructor
    private static class ProperServerListPingEventConstructorInvoker implements ServerListPingEventConstructorInvoker {

        private final Constructor<ServerListPingEvent> constructor;

        @Override
        public ServerListPingEvent create(String hostname, InetAddress address, String motd, boolean shouldSendChatPreviews, int numPlayers, int maxPlayers) {
            try {
                return constructor.newInstance(hostname, address, motd, numPlayers, maxPlayers);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RequiredArgsConstructor
    private static class CorruptedServerListPingEventConstructorInvoker implements ServerListPingEventConstructorInvoker {

        private final Constructor<ServerListPingEvent> constructor;

        @Override
        public ServerListPingEvent create(String hostname, InetAddress address, String motd, boolean shouldSendChatPreviews, int numPlayers, int maxPlayers) {
            try {
                return constructor.newInstance(hostname, address, motd, shouldSendChatPreviews, numPlayers, maxPlayers);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
