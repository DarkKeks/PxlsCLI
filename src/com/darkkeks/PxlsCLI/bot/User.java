package com.darkkeks.PxlsCLI.bot;

import com.darkkeks.PxlsCLI.board.Pixel;
import com.darkkeks.PxlsCLI.network.MessageReceiver;
import com.darkkeks.PxlsCLI.network.ProxiedSocketClient;
import com.darkkeks.PxlsCLI.network.UserProxy;

import java.util.Date;

public class User extends MessageReceiver {

    private static final float DEFAULT_COOLDOWN = -1;

    private final String token;
    private String username;
    private boolean banned;
    private String banReason;

    private long cooldownStart;
    private float cooldown = DEFAULT_COOLDOWN;


    private boolean gotUserInfo = false;

    public User(String token, UserProxy proxy) {
        this.token = token;
        this.connect(new ProxiedSocketClient(this, proxy, token));
    }

    public User(String token) {
        this.token = token;
        this.connect(token);
    }

    public boolean canPlace() {
        return cooldown != DEFAULT_COOLDOWN && getCooldown() < 0;
    }

    public boolean tryPlace(Pixel pixel) {
        if(canPlace()) {
            cooldown = DEFAULT_COOLDOWN;

            System.out.println(username + " placed pixel " + pixel.toString());
            sendPixel(pixel.getX(), pixel.getY(), (byte)pixel.getColor());
            return true;
        }
        return false;
    }

    public boolean gotUserInfo() {
        return gotUserInfo;
    }

    public boolean isConnected() {
        return connectionActive() && !banned;
    }

    public boolean isClosed() {
        return connectionClosed() || banned;
    }

    public String getName() {
        return username;
    }

    public String getToken() {
        return token;
    }

    @Override
    protected void handleUserInfo(String username, String role, boolean banned, long banExpiry, String ban_reason, String method) {
        System.out.println(username + " authorized.");

        if(isProxied() && banned) {
            // login without proxy to clear last login ip
            close();
            new User(this.token);
        }

        this.gotUserInfo = true;
        this.username = username;
        this.banned = banned;
        this.banReason = ban_reason;
    }

    @Override
    protected void handleCooldown(float wait) {
        System.out.println(username + " cooldown: " + wait);
        if(!gotUserInfo) {
            close();
            throw new IllegalStateException("Couldn't login");
        }

        this.cooldown = wait;
        this.cooldownStart = new Date().getTime();
    }

    @Override
    protected void handlePixel(int x, int y, byte color) {
        if(!gotUserInfo) {
            close();
            throw new IllegalStateException("Couldn't login");
        }
    }

    public int getId() {
        if(token == null)
            return -1;
        return Integer.parseInt(this.getToken().split("\\|")[0]);
    }

    public float getCooldown() {
        return cooldown * 1000 - (new Date().getTime() - cooldownStart);
    }
}
