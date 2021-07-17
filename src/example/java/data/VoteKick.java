package example.java.data;

import arc.util.Interval;
import example.java.MainAmogus;
import example.java.player.PlayerA;
import mindustry.content.UnitTypes;
import mindustry.gen.Call;

import java.util.Arrays;

public class VoteKick {
    static Interval interval;
    static int disscussion_time;

    public static void start() {
        if(MainAmogus.game.isVoteKick) {
            return;
        }
        interval = new Interval();
        MainAmogus.game.isVoteKick = true;
        PlayerA.sendInfo();
        disscussion_time = 60;
        for (PlayerA playerA : PlayerA.players) {
            playerA.player.unit().damage(1000);
            playerA.player.unit();
        }
    }

    public static void update() {
        if(MainAmogus.game.isVoteKick) {
            if(interval.get(0, 60f)) {
                disscussion_time -= 1;
                Call.setHudText("[accent]Disscussion time: " + disscussion_time);
                if(disscussion_time == 0) {
                    Call.infoMessage("[scarlet]The disscussion is over!");
                    checkVotes();
                    reset();
                }
            }
        }

    }
    public static void reset() {
        Call.hideHudText();
        for(PlayerA player : PlayerA.players) {
            player.isVoted = false;
            player.votesToKick = 0;
        }
        MainAmogus.game.isVoteKick = false;
    }
    public static void checkVotes() {
        int[] votes = new int[15];
        Integer i = 0;
        for(PlayerA playerA : PlayerA.players) {
            votes[i] = playerA.number;
            i++;
        }
        Arrays.sort(votes);
        if(votes[votes.length - 1] == votes[votes.length - 2]) {
            Call.sendMessage("[scarlet]We were unable to make a single decision. Vote passed.");
            return;
        }
        else {
            PlayerA pl = PlayerA.getPlayerAbyNum.get(votes[votes.length - 1]);
            Call.sendMessage(pl.player.name + " [white]has thrown into space!");
            pl.setSpectator();
            if(pl.isImpostor) {
                Call.sendMessage(pl.player.name + " [scarlet]has an impostor.");
                reset();
                MainAmogus.game.end(false);
                return;
            }
            Call.sendMessage(pl.player.name + " [cyan]not has been an impostor.");
        }
    }
}
