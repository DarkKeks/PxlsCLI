package com.darkkeks.PxlsCLI.bot;

import com.darkkeks.PxlsCLI.SoundUtils;
import com.darkkeks.PxlsCLI.board.Board;
import com.darkkeks.PxlsCLI.board.BoardGraphics;
import com.darkkeks.PxlsCLI.board.Pixel;
import com.darkkeks.PxlsCLI.board.Template;

public class ManualBot {

    private static final int[] dx = {1, 0, -1, 0};
    private static final int[] dy = {0, 1, 0, -1};

    private final Board board;
    private final Template template;
    private final BoardGraphics graphics;
    private final UserProvider userProvider;

    public ManualBot(Board board, Template template, UserProvider userProvider, BoardGraphics graphics) {
        this.board = board;
        this.template = template;
        this.userProvider = userProvider;
        this.graphics = graphics;
    }

    public void start() {
        graphics.setBoardClickListener((x, y) -> {
            boolean canPlace = board.get(x, y) >= 0;
            for(int d = 0; d < 4; ++d) {
                canPlace |= board.get(x + dx[d], y + dy[d]) >= 0;
            }

            canPlace &= template.isLoaded();
            canPlace &= template.checkRange(x, y);

            if(canPlace) {
                int color = template.get(x, y);
                if(color < 0) color = 1;

                userProvider.checkAuth();
                int userCount = userProvider.getCount();
                while(userProvider.hasNext() && userCount > 0) {
                    User user = userProvider.getNext();
                    if(user.canPlace())
                        if(user.tryPlace(new Pixel(x, y, color)))
                            break;
                    userCount--;
                }
            }
        });

        new Thread(() -> {
            boolean alertFlag = true;
            while (true) {
                try {
                    float cooldown = userProvider.getMinimalCooldown();
                    if(cooldown < 0) {
                        if(alertFlag)
                            SoundUtils.alert();
                        alertFlag = false;
                    } else {
                        alertFlag = true;
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}
