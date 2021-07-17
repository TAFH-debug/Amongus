package example.java.player;

import arc.struct.Seq;
import mindustry.gen.Player;

import java.util.HashMap;

public class Spectator {
    public Player player;
    public static Seq<Spectator> spectators = new Seq<>();
    public static HashMap<Player, Spectator> getSpectator = new HashMap<>();

    public Spectator(Player player) {
        this.player = player;
        spectators.add(this);
    }
}
