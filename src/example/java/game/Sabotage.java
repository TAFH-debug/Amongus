package example.java.game;


import arc.util.Interval;
import example.java.MainAmogus;
import mindustry.Vars;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.Packets;
import mindustry.world.Tile;

public class Sabotage {
    static Interval timer;
    static int end_game;

    public static void reactorSabotage() {
        timer = new Interval();
        MainAmogus.game.isSabotage = true;
        end_game = 60;
    }
    public static void electricitySabotage() {
        Call.sendMessage("[cyan]LOL");
    }

    public static void commSabotage() {
        Call.sendMessage("[cyan]LOL");
    }

    public static void sabotagePreventing() {

    }
    public static void update() {
        if(MainAmogus.game.isSabotage) {
            /*
            Fixme 23.06.2021
            Можно сделать освещение красным
             */
            if(timer.get(0, 60f)) {
                end_game -= 1;
                Call.announce("[scarlet]Reactor explosion in: " + end_game);
                if(end_game == 0) {
                    MainAmogus.game.end(true);
                }
            }
        }
    }
}
