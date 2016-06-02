package com.artemdanilov.fourinarow;
import junit.framework.Assert;

import org.junit.Test;


import static com.artemdanilov.fourinarow.Computer.*;

/**
 * Created by artemdanilov
 */
public class Fourtest {


    @Test
    public void Computer() {
        Four game = new Four();
        Computer comp = new Computer();
        setDEPTH(4);
        for (int i = 0; i <3; i++) {
            Four.Cell move;
            move = game.makeMove(Four.Player.WHITE,3);
        }
        int move = foundMove(game);
        Assert.assertEquals(3,move);

    }




}
