package com.example.minesweeper.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MinesweeperScreen extends Screen {
    private MinesweeperGame game;
    private static final int CELL_SIZE = 24;
    private static final int BOARD_OFFSET_X = 20;
    private static final int BOARD_OFFSET_Y = 40;

    private int boardPixelWidth;
    private int boardPixelHeight;

    public MinesweeperScreen() {
        super(Text.literal("\u0421\u0430\u043f\u0435\u0440"));
        this.game = new MinesweeperGame(16, 16, 40);
    }

    @Override
    protected void init() {
        super.init();
        boardPixelWidth = game.getWidth() * CELL_SIZE;
        boardPixelHeight = game.getHeight() * CELL_SIZE;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
            this.textRenderer, 
            this.title, 
            this.width / 2, 
            10, 
            0xFFFFFF
        );

        for (int y = 0; y < game.getHeight(); y++) {
            for (int x = 0; x < game.getWidth(); x++) {
                int px = BOARD_OFFSET_X + x * CELL_SIZE;
                int py = BOARD_OFFSET_Y + y * CELL_SIZE;

                int color = getCellColor(x, y);
                context.fill(px, py, px + CELL_SIZE - 1, py + CELL_SIZE - 1, color);

                context.drawHorizontalLine(px, px + CELL_SIZE - 1, py, 0xFF808080);
                context.drawVerticalLine(px, py, py + CELL_SIZE - 1, 0xFF808080);

                String text = getCellText(x, y);
                if (!text.isEmpty()) {
                    int textColor = getTextColor(x, y);
                    context.drawTextWithShadow(
                        this.textRenderer,
                        text,
                        px + 8,
                        py + 7,
                        textColor
                    );
                }
            }
        }

        String status = game.isGameOver() ? (game.isWin() ? "\u041f\u043e\u0431\u0435\u0434\u0430!" : "\u041f\u0440\u043e\u0438\u0433\u0440\u044b\u0448!") 
                       : "\u041c\u0438\u043d\u044b: " + game.getRemainingMines();
        context.drawTextWithShadow(
            this.textRenderer,
            status,
            BOARD_OFFSET_X,
            BOARD_OFFSET_Y + boardPixelHeight + 10,
            game.isWin() ? 0x00FF00 : (game.isGameOver() ? 0xFF0000 : 0xFFFFFF)
        );

        context.drawTextWithShadow(
            this.textRenderer,
            "\u041d\u0430\u0436\u043c\u0438\u0442\u0435 R \u0434\u043b\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0443\u0441\u043a\u0430",
            BOARD_OFFSET_X,
            BOARD_OFFSET_Y + boardPixelHeight + 25,
            0xAAAAAA
        );
    }

    private int getCellColor(int x, int y) {
        if (game.isRevealed(x, y)) {
            if (game.isMine(x, y) && game.isGameOver()) {
                return game.isExploded(x, y) ? 0xFFFF0000 : 0xFF000000;
            }
            return 0xFFCCCCCC;
        }
        if (game.isFlagged(x, y)) {
            return 0xFFFF0000;
        }
        return 0xFF999999;
    }

    private String getCellText(int x, int y) {
        if (!game.isRevealed(x, y)) {
            return game.isFlagged(x, y) ? "\u2691" : "";
        }
        if (game.isMine(x, y)) {
            return "\u2620";
        }
        int count = game.getNeighborMines(x, y);
        return count > 0 ? String.valueOf(count) : "";
    }

    private int getTextColor(int x, int y) {
        int count = game.getNeighborMines(x, y);
        switch (count) {
            case 1: return 0x0000FF;
            case 2: return 0x008000;
            case 3: return 0xFF0000;
            case 4: return 0x000080;
            case 5: return 0x800000;
            case 6: return 0x008080;
            case 7: return 0x000000;
            case 8: return 0x808080;
            default: return 0xFFFFFF;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int boardX = (int) ((mouseX - BOARD_OFFSET_X) / CELL_SIZE);
        int boardY = (int) ((mouseY - BOARD_OFFSET_Y) / CELL_SIZE);

        if (boardX >= 0 && boardX < game.getWidth() && 
            boardY >= 0 && boardY < game.getHeight() && 
            !game.isGameOver()) {

            if (button == 0) {
                game.reveal(boardX, boardY);
            } else if (button == 1) {
                game.toggleFlag(boardX, boardY);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_R) {
            game = new MinesweeperGame(16, 16, 40);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
