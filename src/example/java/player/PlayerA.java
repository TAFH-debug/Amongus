package example.java.player;


import arc.math.Mathf;
import arc.struct.Seq;
import example.java.MainAmogus;
import example.java.game.Game;
import mindustry.Vars;
import mindustry.content.UnitTypes;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.gen.Unit;

import java.util.HashMap;

public class PlayerA {
    public boolean isVoted;
    public boolean isImpostor;
    public int votesToKick;
    public int number;
    public Player player;
    public static Seq<PlayerA> players = new Seq<>();
    public static HashMap<Player, PlayerA> getPlayerA = new HashMap<>();
    public static  HashMap<Integer, PlayerA> getPlayerAbyNum = new HashMap<>();

    public PlayerA(Player player) {
        this.player = player;
        this.votesToKick = 0;
        this.isVoted = false;
        this.number = setNumber();
        getPlayerAbyNum.put(number, this);
    }

    public void freeze() {
        player.unit().speedMultiplier(0);
        player.unit().reloadMultiplier(0);
    }

    public void unfreeze() {
        player.unit().speedMultiplier(1);
        player.unit().reloadMultiplier(1);
    }

    public void setImpostor() {
        isImpostor = true;
        Impostor impostor = new Impostor(player);
        Impostor.getImpostor.put(player, impostor);
    }

    public void setSpectator() {
        this.player.team(Team.derelict);
        this.player.clearUnit();
        players.remove(this);
        Spectator spectator = new Spectator(this.player);
        Rules rules = Vars.state.rules.copy();
        rules.ambientLight.r = 0.005f;
        rules.ambientLight.g = 0.0f;
        rules.ambientLight.b = 0.02f;
        rules.ambientLight.a = 0.00f;
        Call.setRules(player.con, rules);
    }

    public int setNumber() {
        int number = Mathf.random(0, 1000);
        return number;
    }

    public static void sendInfo() {
        Call.sendMessage("Numbers of players.\n" +
                "You can using the command /vote <number> to vote.");
        for(PlayerA playerA : players) {
            Call.sendMessage(playerA.player.name() + ":[white] " + playerA.number + "\n");
        }
    }
    public static void spawn() {
        int i = 0;
        for(PlayerA playerA : players) {
            if(Impostor.getImpostor.containsValue(playerA)) {
                Unit imp = UnitTypes.mace.spawn(Team.sharded, MainAmogus.game.spawnPos.get(i).x, MainAmogus.game.spawnPos.get(i).y);
                imp.type = UnitTypes.dagger;
                imp.spawnedByCore = true;
                playerA.player.unit(imp);
            }
            else {
                Unit crw = UnitTypes.dagger.spawn(Team.sharded, MainAmogus.game.spawnPos.get(i).x, MainAmogus.game.spawnPos.get(i).y);
                crw.spawnedByCore = true;
                playerA.player.unit(crw);
            }
            i++;
        }
    }
}
