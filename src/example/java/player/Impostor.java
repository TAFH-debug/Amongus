package example.java.player;

import arc.Events;
import arc.math.Mathf;
import arc.util.Log;
import example.java.MainAmogus;
import example.java.data.AUEvents;
import example.java.data.DeadBody;
import mindustry.content.UnitTypes;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.world.Tile;

import java.util.HashMap;

public class Impostor extends PlayerA {
    private static int i;
    public static Player player;
    public static HashMap<Player, Impostor> getImpostor = new HashMap<>();
    public boolean isInVent;
    private Rules rules;

    public Impostor(Player player) {
        super(player);
        this.player = player;
        isInVent = false;
        PlayerA.getPlayerA.get(player).isImpostor = true;
    }

    public static void update() {
        if(!MainAmogus.game.isStarted) {
            return;
        }
        if(MainAmogus.game.isVoteKick) {
            return;
        }
        for(Player player : Groups.player) {
            if(player.unit().dead || !Spectator.getSpectator.containsValue(player)) {
                DeadBody body = new DeadBody((int)player.x, (int)player.y);
                DeadBody.deadBodies.add(body);
            }
        }
    }

    public void inVent(Tile tile) {
        if(isInVent) {
            rules = new Rules();
            rules.ambientLight.r = 0.005f;
            rules.ambientLight.g = 0.0f;
            rules.ambientLight.b = 0.02f;
            rules.ambientLight.a = 1f;
            Call.setRules(player.con, rules);
            unfreeze();
            isInVent = false;
            return;
        }
        isInVent = true;
        freeze();
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
