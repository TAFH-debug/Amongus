package example.java.player;

import arc.math.Mathf;
import arc.util.Log;
import mindustry.content.UnitTypes;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Nulls;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.world.Tile;

import java.util.HashMap;

public class Impostor extends PlayerA{
    private static int i;
    public static Player player;
    public static HashMap<Player, Impostor> getImpostor = new HashMap<>();
    public boolean isInVent;
    private Rules rules;
    public Unit u;

    public Impostor(Player player) {
        super(player);
        this.player = player;
        isInVent = false;
        PlayerA.getPlayerA.get(player).isImpostor = true;
    }

    public static void update() {
    }

    public void inVent(Tile tile) {
        if(isInVent) {
            rules = new Rules();
            rules.ambientLight.r = 0.005f;
            rules.ambientLight.g = 0.0f;
            rules.ambientLight.b = 0.02f;
            rules.ambientLight.a = 1f;
            u = UnitTypes.mace.spawn(Team.sharded, tile.x, tile.y);
            u.type(UnitTypes.dagger);
            player.unit(u);
            isInVent = false;
            return;
        }
        isInVent = true;
        player.unit().damage(1000);
        rules = new Rules();
        rules.ambientLight.r = 0.005f;
        rules.ambientLight.g = 0.0f;
        rules.ambientLight.b = 0.02f;
        rules.ambientLight.a = 0.70f;
        Call.setRules(player.con, rules);
    }

    public static void chooseImpostor() {
        i = Mathf.random(0, players.size - 1);
        Log.info("Random: " + i);
        Log.info("Size: " + players.size);
        PlayerA.players.get(i).setImpostor();
    }
}
