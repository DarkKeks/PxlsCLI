package com.darkkeks.PxlsCLI;

import com.darkkeks.PxlsCLI.board.Board;
import com.darkkeks.PxlsCLI.board.BoardGraphics;
import com.darkkeks.PxlsCLI.board.BoardUpdateUser;
import com.darkkeks.PxlsCLI.board.Template;
import com.darkkeks.PxlsCLI.bot.ManualBot;
import com.darkkeks.PxlsCLI.bot.ProxyProvider;
import com.darkkeks.PxlsCLI.bot.UserProvider;
import com.darkkeks.PxlsCLI.network.BoardLoadThread;
import com.darkkeks.PxlsCLI.network.TemplateLoadThread;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PxlsCLI {

    private static final int WIDTH = 2000;
    private static final int HEIGHT = 2000;

    public static final JsonParser gson = new JsonParser();

    public static Config config = new Config( "config.ini",
                    PxlsCLI.class.getResource("/configDefault.ini"));

    private void start() {
        Board board = new Board(WIDTH, HEIGHT);
        BoardGraphics graphics = new BoardGraphics(board);

        Template template = new Template(graphics,
                config.get("template", "URI"),
                config.getInt("template", "offsetX"),
                config.getInt("template", "offsetY"),
                config.getFloat("template", "opacity"),
                config.getBool("template", "replacePixels"));

        graphics.setTemplate(template);

        new BoardUpdateUser(board, graphics);
        new BoardLoadThread(board, graphics).start();
        new TemplateLoadThread(template).start();

        UserProvider userProvider;
        if(config.getBool("proxy", "useProxy")) {
            ProxyProvider proxyProvider = new ProxyProvider(config.get("proxy", "filePath"));
            proxyProvider.init();
            userProvider = new UserProvider(proxyProvider);
        } else {
            userProvider = new UserProvider();
        }

        Object[] tokens = readTokens(config.get("main", "tokensFilePath"));

        System.out.println("Read " + tokens.length + " tokens.");

        for(Object token : tokens) {
            userProvider.add((String)token);
        }

        ManualBot bot = new ManualBot(board, template, userProvider, graphics);
        bot.start();
    }

    private Object[] readTokens(String path) {
        ArrayList<String> res = new ArrayList<>();
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null) {
                if(line.matches("\\d{5}\\|[a-zA-Z]{33}"))
                    res.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toArray();
    }

    public static void main(String[] args) throws URISyntaxException {
        new PxlsCLI().start();
    }
}
